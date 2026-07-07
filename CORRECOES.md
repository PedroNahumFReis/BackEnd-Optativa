# Correções que apliquei na API

> Resumo: revisei a API inteira e vi que a estrutura (OAuth2 com custom password grant, HATEOAS,
> Swagger, Flyway, camadas Controller → Service → Repository, projections no login) já estava
> correta. Onde eu tinha perdido pontos foi em **validação**, **segurança de endpoints**,
> **tratamento de exceções**, **fluxo de e-mail** e um **build quebrado por encoding**. Abaixo
> descrevo o que notei em cada ponto e o que fiz para corrigir.

---

## 1. Validação de dados (Bean Validation)

**O que notei:** meu `pom.xml` não tinha a dependência `spring-boot-starter-validation` e meus DTOs
não tinham nenhuma anotação de validação. Por isso o `@Valid` dos controllers não validava nada e o
handler de erro `422` que eu já tinha escrito **nunca era acionado**. Percebi que no Spring Boot 3 a
dependência de validation é obrigatória (não vem mais junto do `starter-web`).

**O que fiz:**
- Adicionei a dependência no `pom.xml`:
  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
  </dependency>
  ```
- Coloquei as anotações nos DTOs:
  - `UserDTO`: `@NotBlank` no `name`; `@NotBlank` + `@Email` no `email`.
  - `UserInsertDTO`: `@NotBlank` + `@Size(min = 6)` no `password`.
  - `TaskDTO`: `@NotBlank` no `title`; `@NotNull` no `userId`.

**No git das aulas:** o `UsuarioDTO` usa `@NotBlank`/`@Email`, o `ProdutoDTO` usa
`@Size`/`@Positive`/`@NotNull`, e o `pom.xml` já traz o starter de validation.

---

## 2. Encoding dos arquivos `.properties` (build quebrado)

**O que notei:** ao tentar compilar, o build falhava com:
```
MalformedInputException: Input length = 1 ... application-dev.properties
```
Descobri que os arquivos `application*.properties` estavam salvos em Windows-1252 (com acentos nos
comentários), e o Maven lê recursos como UTF-8 por padrão. Isso pode ter feito o projeto simplesmente
**não rodar** durante a correção.

**O que fiz:** reescrevi os três arquivos (`application.properties`, `application-dev.properties`,
`application-test.properties`) em **UTF-8**. Também tirei um espaço em branco solto no fim de
`security.jwt.duration=86400`. Confirmei rodando `mvnw clean compile` **sem** forçar encoding — build
limpo.

---

## 3. Auto-cadastro seguro em `/auth/signup` (correção de escalonamento de privilégio)

**O que notei:** meu cadastro de usuário ficava no `POST /users` público e o `copyDtoToEntity`
copiava os perfis que vinham no corpo da requisição. Ou seja, qualquer um podia se cadastrar como
**ADMIN** enviando `"perfis": [{ "id": 1 }]`. Percebi que isso era uma falha de segurança séria.

**O que fiz:**
- Criei o método `UserService.signup(UserInsertDTO)` que **limpa os perfis do DTO** e força sempre
  `ROLE_USER`:
  ```java
  dto.getPerfis().clear();
  Perfil roleUser = perfilRepository.findByNome("ROLE_USER");
  user.addPerfil(roleUser);
  ```
- Criei o endpoint público `POST /auth/signup` no `AuthController` como porta de entrada do
  auto-cadastro.

**No git das aulas:** o `AuthService.signup` faz exatamente isso — `dto.getPerfis().clear()` e
adiciona `ROLE_CLIENTE` — exposto em `POST /auth/signup` no `AuthResource`.

---

## 4. Autorização por método no `UserController` (`@PreAuthorize`)

**O que notei:** meu `UserController` não tinha **nenhuma** regra de autorização. Como o filtro HTTP
libera as rotas (`permitAll`), qualquer um sem token conseguia **listar, buscar, editar e deletar**
usuários.

**O que fiz:** adicionei `@PreAuthorize("hasRole('ROLE_ADMIN')")` em `list`, `findById`, `insert`
(que virou administrativo), `update` e `delete`. O auto-cadastro público passou a ser o `/auth/signup`
do item 3.

**No git das aulas:** o `UsuarioResource` protege `findAll`, `findById` e `delete` com
`@PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR')")`, apoiado no `@EnableMethodSecurity`. O modelo
é **`permitAll` no filtro + segurança por método** — que é o mesmo que eu já usava nas tasks; só
faltava eu aplicar aos usuários.

---

## 5. Exceções customizadas no `AuthService`

**O que notei:** no meu `AuthService` eu estava lançando `RuntimeException` cru (token inválido,
e-mail não encontrado). Como o `GlobalExceptionHandler` não trata `RuntimeException`, o cliente
recebia **HTTP 500** em vez de **404**.

**O que fiz:** troquei por `ResourceNotFoundException`, que o handler já mapeia para `404`.
```java
if (list.isEmpty()) { throw new ResourceNotFoundException("Token not found or expired"); }
...
if (user == null) { throw new ResourceNotFoundException("Email not found"); }
```

**No git das aulas:** o `AuthService` usa `RegistroNaoEncontrado` (mapeada para 404 no
`ResourceExceptionHandler`) nesses mesmos pontos.

---

## 6. Envio real do e-mail de recuperação de senha

**O que notei:** no meu `createRecoverToken` o envio de e-mail estava comentado e o token só era
impresso com `System.out.println`. Ou seja, o fluxo estava **mockado**, mesmo eu já tendo um
`EmailService` funcional (usado no `AtivacaoUsuarioService`).

**O que fiz:** injetei o `EmailService` no `AuthService` e passei a enviar o e-mail de verdade:
```java
emailService.sendMail(new EmailDTO(dto.getEmail(), "Recuperação de senha", text));
```

**No git das aulas:** o `AuthService.createRecoverToken` envia o e-mail via
`emailService.sendMail(...)`.

---

## 7. `NullPointerException` ao serializar Task sem usuário

**O que notei:** meu construtor `TaskDTO(Task)` fazia `task.getUser().getId()` sem checar `null`.
Como a FK `user_id` é `ON DELETE SET NULL` (migration V2), pode existir task com usuário nulo, o que
causava **NPE** ao montar o DTO.

**O que fiz:** coloquei um null-check, no mesmo estilo que eu já fazia para `category`:
```java
if (task.getUser() != null) {
    this.userId = task.getUser().getId();
}
```

---

## 8. Fiz a falha de envio de e-mail deixar de ser silenciosa

**O que notei:** meu `EmailService` capturava qualquer exceção e só chamava `printStackTrace()`. Uma
falha real de envio (ex.: senha de app do Gmail não configurada) passava **completamente
despercebida** — o endpoint respondia sucesso mesmo sem o e-mail sair. (O código do git das aulas
também faz assim; então este item foi uma melhoria que fiz além do que foi feito em aula.)

**O que fiz (com critério por criticidade):**
- Criei a exceção `EmailException` e fiz o `EmailService` **propagá-la** em vez de engolir.
- Adicionei um handler no `GlobalExceptionHandler` mapeando `EmailException` para **`502 Bad Gateway`**
  (falha em serviço externo).
- **E-mail de recuperação de senha** (`/auth/recover-token`): tratei como crítico — deixo o erro subir
  e virar `502`, então a falha fica visível.
- **E-mail de boas-vindas** (cadastro/atualização): tratei como não crítico — no
  `AtivacaoUsuarioService` eu capturo o `EmailException`, registro um `WARN` e **não bloqueio nem dou
  rollback** no cadastro. Assim o usuário é criado normalmente mesmo se o e-mail falhar.

---

## Como validei

```bash
./mvnw clean compile        # build limpo (encoding corrigido)
./mvnw spring-boot:run      # subi a aplicação (perfil dev)
```

Subi a API e testei cada correção de ponta a ponta — todos os cenários se comportaram como esperado:

| Cenário | Resultado que obtive |
|---|---|
| `POST /auth/signup` com senha curta e e-mail inválido | `422` com as mensagens de validação por campo |
| `POST /auth/signup` mandando `perfis:[{id:1}]` (ADMIN) | `201`, mas criado como `ROLE_USER` |
| `GET /users` sem token | `401` (bloqueado) |
| `GET /users` com token de usuário comum | `403` (method security) |
| `GET /users` com token de ADMIN | `200` |
| `POST /auth/new-password` com token inválido | `404` (não mais `500`) |
| `POST /auth/recover-token` com Gmail sem senha | `502` (falha de e-mail visível) |

---

## Observação (não é erro)

Invalidar/apagar o token de recuperação após o uso seria uma boa prática de segurança, mas como o
código do git das aulas **também não faz isso**, deixei como está para manter o alinhamento com o
padrão da aula. Fica como melhoria futura opcional.

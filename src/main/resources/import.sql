-- 1. POPULAR PERFIS (Novo!)
-- IDs: 1 (ADMIN), 2 (USER), 3 (EMPLOYEE)
INSERT INTO tb_perfil (nome) VALUES ('ROLE_ADMIN');
INSERT INTO tb_perfil (nome) VALUES ('ROLE_USER');
INSERT INTO tb_perfil (nome) VALUES ('ROLE_EMPLOYEE');

-- 2. POPULAR USUÁRIOS
-- Usando tb_user conforme o novo Model
INSERT INTO tb_user (name, email, password, phone, created_at, updated_at) VALUES ('Pedro Nahum', 'pedro@email.com', '123456', '37999991111', NOW(), NOW());
INSERT INTO tb_user (name, email, password, phone, created_at, updated_at) VALUES ('Maria Silva', 'maria@email.com', '654321', '37999992222', NOW(), NOW());
INSERT INTO tb_user (name, email, password, phone, created_at, updated_at) VALUES ('Carlos Admin', 'admin@api.com', 'admin123', '37999993333', NOW(), NOW());

-- 3. VINCULAR USUÁRIOS AOS PERFIS (Tabela de Junção)
-- Pedro (1) é USER (2)
INSERT INTO tb_user_perfil (user_id, perfil_id) VALUES (1, 2);
-- Maria (2) é EMPLOYEE (3)
INSERT INTO tb_user_perfil (user_id, perfil_id) VALUES (2, 3);
-- Carlos (3) é ADMIN (1) e USER (2)
INSERT INTO tb_user_perfil (user_id, perfil_id) VALUES (3, 1);
INSERT INTO tb_user_perfil (user_id, perfil_id) VALUES (3, 2);

-- 4. POPULAR CATEGORIAS
INSERT INTO tb_category (name, created_at, updated_at) VALUES ('Estudos', NOW(), NOW());
INSERT INTO tb_category (name, created_at, updated_at) VALUES ('Trabalho', NOW(), NOW());
INSERT INTO tb_category (name, created_at, updated_at) VALUES ('Pessoal', NOW(), NOW());
INSERT INTO tb_category (name, created_at, updated_at) VALUES ('Fitness', NOW(), NOW());

-- 5. POPULAR TAREFAS (TASKS)
INSERT INTO tb_task (title, description, status, user_id, category_id, created_at, updated_at) VALUES ('Aprender Spring Boot', 'Praticar relacionamentos JPA e DTOs', 'DOING', 1, 1, NOW(), NOW());
INSERT INTO tb_task (title, description, status, user_id, category_id, created_at, updated_at) VALUES ('Finalizar Relatório', 'Enviar o report mensal para o RH', 'PENDING', 1, 2, NOW(), NOW());
INSERT INTO tb_task (title, description, status, user_id, category_id, created_at, updated_at) VALUES ('Treino de Perna', 'Agachamento e Leg Press', 'DONE', 2, 4, NOW(), NOW());
INSERT INTO tb_task (title, description, status, user_id, category_id, created_at, updated_at) VALUES ('Comprar Café', 'Ir ao mercado comprar grãos', 'PENDING', 2, 3, NOW(), NOW());
INSERT INTO tb_task (title, description, status, user_id, category_id, created_at, updated_at) VALUES ('Configurar Servidor', 'Deploy da aplicação em produção', 'DOING', 3, 2, NOW(), NOW());
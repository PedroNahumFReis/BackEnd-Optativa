-- ==========================================
-- 1. CRIAÇÃO DAS TABELAS (ESTRUTURA)
-- ==========================================

CREATE TABLE tb_perfil (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           nome VARCHAR(255) NOT NULL,
                           PRIMARY KEY (id)
);

CREATE TABLE tb_user (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         phone VARCHAR(50),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (id)
);

CREATE TABLE tb_user_perfil (
                                user_id BIGINT NOT NULL,
                                perfil_id BIGINT NOT NULL,
                                PRIMARY KEY (user_id, perfil_id),
                                FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE CASCADE,
                                FOREIGN KEY (perfil_id) REFERENCES tb_perfil(id) ON DELETE CASCADE
);

CREATE TABLE tb_category (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             name VARCHAR(255) NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             PRIMARY KEY (id)
);

CREATE TABLE tb_task (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         title VARCHAR(255) NOT NULL,
                         description TEXT,
                         status VARCHAR(50),
                         user_id BIGINT,
                         category_id BIGINT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (id),
                         FOREIGN KEY (user_id) REFERENCES tb_user(id) ON DELETE SET NULL,
                         FOREIGN KEY (category_id) REFERENCES tb_category(id) ON DELETE SET NULL
);

CREATE TABLE tb_tag (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL,
                        PRIMARY KEY (id)
);

CREATE TABLE tb_task_tag (
                             task_id BIGINT NOT NULL,
                             tag_id BIGINT NOT NULL,
                             PRIMARY KEY (task_id, tag_id),
                             FOREIGN KEY (task_id) REFERENCES tb_task(id) ON DELETE CASCADE,
                             FOREIGN KEY (tag_id) REFERENCES tb_tag(id) ON DELETE CASCADE
);

-- ==========================================
-- 2. INSERÇÃO DOS DADOS (SEEDS DO SEU IMPORT)
-- ==========================================

-- 1. POPULAR PERFIS
INSERT INTO tb_perfil (nome) VALUES ('ROLE_ADMIN');
INSERT INTO tb_perfil (nome) VALUES ('ROLE_USER');
INSERT INTO tb_perfil (nome) VALUES ('ROLE_EMPLOYEE');

-- 2. POPULAR USUÁRIOS
INSERT INTO tb_user (name, email, password, phone, created_at, updated_at) VALUES ('Pedro Nahum', 'pedro@email.com', '$2a$10$TOsG1PVqvStv5wbHNH6aue2qoKmH/4eWnqyP.isX0Iy1V76708dgu', '37999991111', NOW(), NOW());
INSERT INTO tb_user (name, email, password, phone, created_at, updated_at) VALUES ('Maria Silva', 'maria@email.com', '$2a$10$TOsG1PVqvStv5wbHNH6aue2qoKmH/4eWnqyP.isX0Iy1V76708dgu', '37999992222', NOW(), NOW());
INSERT INTO tb_user (name, email, password, phone, created_at, updated_at) VALUES ('Carlos Admin', 'admin@api.com', '$2a$10$TOsG1PVqvStv5wbHNH6aue2qoKmH/4eWnqyP.isX0Iy1V76708dgu', '37999993333', NOW(), NOW());

-- 3. VINCULAR USUÁRIOS AOS PERFIS
INSERT INTO tb_user_perfil (user_id, perfil_id) VALUES (1, 2);
INSERT INTO tb_user_perfil (user_id, perfil_id) VALUES (2, 3);
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

-- 6. POPULAR TAGS
INSERT INTO tb_tag (name) VALUES ('Urgente');
INSERT INTO tb_tag (name) VALUES ('Backend');
INSERT INTO tb_tag (name) VALUES ('DevOps');
INSERT INTO tb_tag (name) VALUES ('Saúde');
INSERT INTO tb_tag (name) VALUES ('Importante');

-- 7. VINCULAR TAREFAS ÀS TAGS
INSERT INTO tb_task_tag (task_id, tag_id) VALUES (1, 2);
INSERT INTO tb_task_tag (task_id, tag_id) VALUES (1, 5);
INSERT INTO tb_task_tag (task_id, tag_id) VALUES (2, 1);
INSERT INTO tb_task_tag (task_id, tag_id) VALUES (3, 4);
INSERT INTO tb_task_tag (task_id, tag_id) VALUES (5, 1);
INSERT INTO tb_task_tag (task_id, tag_id) VALUES (5, 3);
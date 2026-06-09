-- 1. POPULAR PERFIS
-- IDs: 1 (ADMIN), 2 (USER), 3 (EMPLOYEE)
INSERT INTO tb_perfil (nome) VALUES ('ROLE_ADMIN');
INSERT INTO tb_perfil (nome) VALUES ('ROLE_USER');
INSERT INTO tb_perfil (nome) VALUES ('ROLE_EMPLOYEE');

-- 2. POPULAR USUÁRIOS
INSERT INTO tb_user (name, email, password, phone, created_at, updated_at) VALUES ('Pedro Nahum', 'pedro@email.com', '2a$10$TOsG1PVqvStv5wbHNH6aue2qoKmH/4eWnqyP.isX0Iy1V76708dgu', '37999991111', NOW(), NOW());
INSERT INTO tb_user (name, email, password, phone, created_at, updated_at) VALUES ('Maria Silva', 'maria@email.com', '2a$10$TOsG1PVqvStv5wbHNH6aue2qoKmH/4eWnqyP.isX0Iy1V76708dgu', '37999992222', NOW(), NOW());
INSERT INTO tb_user (name, email, password, phone, created_at, updated_at) VALUES ('Carlos Admin', 'admin@api.com', '2a$10$TOsG1PVqvStv5wbHNH6aue2qoKmH/4eWnqyP.isX0Iy1V76708dgu', '37999993333', NOW(), NOW());

-- 3. VINCULAR USUÁRIOS AOS PERFIS (Tabela de Junção)
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
-- IDs gerados automaticamente de 1 a 5
INSERT INTO tb_task (title, description, status, user_id, category_id, created_at, updated_at) VALUES ('Aprender Spring Boot', 'Praticar relacionamentos JPA e DTOs', 'DOING', 1, 1, NOW(), NOW());
INSERT INTO tb_task (title, description, status, user_id, category_id, created_at, updated_at) VALUES ('Finalizar Relatório', 'Enviar o report mensal para o RH', 'PENDING', 1, 2, NOW(), NOW());
INSERT INTO tb_task (title, description, status, user_id, category_id, created_at, updated_at) VALUES ('Treino de Perna', 'Agachamento e Leg Press', 'DONE', 2, 4, NOW(), NOW());
INSERT INTO tb_task (title, description, status, user_id, category_id, created_at, updated_at) VALUES ('Comprar Café', 'Ir ao mercado comprar grãos', 'PENDING', 2, 3, NOW(), NOW());
INSERT INTO tb_task (title, description, status, user_id, category_id, created_at, updated_at) VALUES ('Configurar Servidor', 'Deploy da aplicação em produção', 'DOING', 3, 2, NOW(), NOW());

-- 6. POPULAR TAGS (Novo!)
-- IDs gerados automaticamente: 1 (Urgente), 2 (Backend), 3 (DevOps), 4 (Saúde), 5 (Importante)
INSERT INTO tb_tag (name) VALUES ('Urgente');
INSERT INTO tb_tag (name) VALUES ('Backend');
INSERT INTO tb_tag (name) VALUES ('DevOps');
INSERT INTO tb_tag (name) VALUES ('Saúde');
INSERT INTO tb_tag (name) VALUES ('Importante');

-- 7. VINCULAR TAREFAS ÀS TAGS (Tabela de Junção tb_task_tag - Novo!)
-- Tarefa 1 (Aprender Spring Boot) ganha as tags 'Backend' (2) e 'Importante' (5)
INSERT INTO tb_task_tag (task_id, tag_id) VALUES (1, 2);
INSERT INTO tb_task_tag (task_id, tag_id) VALUES (1, 5);

-- Tarefa 2 (Finalizar Relatório) ganha a tag 'Urgente' (1)
INSERT INTO tb_task_tag (task_id, tag_id) VALUES (2, 1);

-- Tarefa 3 (Treino de Perna) ganha a tag 'Saúde' (4)
INSERT INTO tb_task_tag (task_id, tag_id) VALUES (3, 4);

-- Tarefa 5 (Configurar Servidor) ganha as tags 'Urgente' (1) e 'DevOps' (3)
INSERT INTO tb_task_tag (task_id, tag_id) VALUES (5, 1);
INSERT INTO tb_task_tag (task_id, tag_id) VALUES (5, 3);
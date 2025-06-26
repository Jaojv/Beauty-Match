-- Limpar tabelas existentes
DELETE FROM usuario;

-- Inserir usuários de teste (senhas criptografadas com BCrypt)
-- Senha para todos: 123456

-- Administrador
INSERT INTO usuario (username, password, nome, email, telefone, criado_em, tipo_usuario)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Administrador', 'admin@beautymatch.com', '11999999999', NOW(), 'ADMINISTRADOR');

-- Proprietário
INSERT INTO usuario (username, password, nome, email, telefone, criado_em, tipo_usuario)
VALUES ('proprietario', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Proprietário', 'proprietario@beautymatch.com', '11988888888', NOW(), 'PROPRIETARIO');

-- Profissional
INSERT INTO usuario (username, password, nome, email, telefone, criado_em, tipo_usuario)
VALUES ('profissional', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Profissional', 'profissional@beautymatch.com', '11977777777', NOW(), 'PROFISSIONAL');

-- Cliente
INSERT INTO usuario (username, password, nome, email, telefone, criado_em, tipo_usuario)
VALUES ('cliente', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Cliente', 'cliente@beautymatch.com', '11966666666', NOW(), 'CLIENTE'); 
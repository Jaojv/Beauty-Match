-- Limpar tabelas existentes
DELETE FROM usuarios;

-- Inserir usuários de teste (senhas criptografadas com BCrypt)
-- Senha para todos: 123456

-- Administrador
INSERT INTO usuarios (username, password, email, telefone, criado_em, tipo_usuario, role)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin@beautymatch.com', '11999999999', NOW(), 'ADMINISTRADOR', 'ROLE_ADMIN');

-- Proprietário
INSERT INTO usuarios (username, password, email, telefone, criado_em, tipo_usuario, role)
VALUES ('proprietario', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'proprietario@beautymatch.com', '11988888888', NOW(), 'PROPRIETARIO', 'ROLE_PROPRIETARIO');

-- Profissional
INSERT INTO usuarios (username, password, email, telefone, criado_em, tipo_usuario, role)
VALUES ('profissional', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'profissional@beautymatch.com', '11977777777', NOW(), 'PROFISSIONAL', 'ROLE_PROFISSIONAL');

-- Cliente
INSERT INTO usuarios (username, password, email, telefone, criado_em, tipo_usuario, role)
VALUES ('cliente', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'cliente@beautymatch.com', '11966666666', NOW(), 'CLIENTE', 'ROLE_CLIENTE'); 
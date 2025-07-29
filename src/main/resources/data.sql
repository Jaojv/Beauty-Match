-- Inserir usuários de teste (senhas criptografadas com BCrypt)
-- Senha para todos: 123456

-- Administrador
INSERT IGNORE INTO usuario (username, password, nome, email, telefone, criado_em, atualizado_em, tipo_usuario)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Administrador', 'admin@beautymatch.com', '11999999999', NOW(), NOW(), 'ADMIN');

-- Proprietario
INSERT IGNORE INTO usuario (username, password, nome, email, telefone, criado_em, atualizado_em, tipo_usuario)
VALUES ('proprietario', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Proprietario', 'proprietario@beautymatch.com', '11988888888', NOW(), NOW(), 'PROPRIETARIO');

-- Profissional
INSERT IGNORE INTO usuario (username, password, nome, email, telefone, criado_em, atualizado_em, tipo_usuario)
VALUES ('profissional', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Profissional', 'profissional@beautymatch.com', '11977777777', NOW(), NOW(), 'PROFISSIONAL');

-- Cliente
INSERT IGNORE INTO usuario (username, password, nome, email, telefone, criado_em, atualizado_em, tipo_usuario)
VALUES ('cliente', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Cliente', 'cliente@beautymatch.com', '11966666666', NOW(), NOW(), 'CLIENTE');

-- Cliente Jao
INSERT IGNORE INTO usuario (username, password, nome, email, telefone, criado_em, atualizado_em, tipo_usuario)
VALUES ('Jao', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Jão', 'victor.joao@gmail.com', '11955555555', NOW(), NOW(), 'CLIENTE');

-- Inserir dados na tabela cliente
INSERT IGNORE INTO cliente (id_usuario, cpf, data_nascimento, endereco, preferencias)
VALUES 
(4, '12345678901', '1990-01-01', 'Rua das Flores, 123 - São Paulo, SP', 'Cabelos longos, cores naturais'),
(8, '98765432100', '1995-05-15', 'Av. Paulista, 1000 - São Paulo, SP', 'Cortes modernos, cores vibrantes');

-- ============================================================================
-- DADOS INICIAIS DO MODULO QUIZ
-- ============================================================================

-- LIMPEZA COMPLETA - Remover todas as perguntas e alternativas existentes
DELETE FROM alternativas WHERE id > 0;
DELETE FROM perguntas WHERE id > 0;
DELETE FROM recomendacoes WHERE id > 0;
DELETE FROM respostas_quiz_detalhes WHERE resposta_quiz_id > 0;
DELETE FROM respostas_quiz WHERE id > 0;

-- Resetar auto-increment
ALTER TABLE alternativas AUTO_INCREMENT = 1;
ALTER TABLE perguntas AUTO_INCREMENT = 1;
ALTER TABLE recomendacoes AUTO_INCREMENT = 1;
ALTER TABLE respostas_quiz AUTO_INCREMENT = 1;

-- Inserir perguntas do quiz (APENAS 4 PERGUNTAS)
INSERT INTO perguntas (texto, ordem, ativo, created_at, updated_at) VALUES
('Qual o seu tipo de cabelo?', 1, true, NOW(), NOW()),
('Como voce descreveria o seu tom de pele?', 2, true, NOW(), NOW()),
('Qual o formato do seu rosto?', 3, true, NOW(), NOW()),
('Qual o seu estilo preferido?', 4, true, NOW(), NOW());

-- Inserir alternativas para a pergunta 1 (Tipo de cabelo)
INSERT INTO alternativas (texto, pergunta_id, ativo, created_at, updated_at) VALUES
('Liso', 1, true, NOW(), NOW()),
('Cacheado', 1, true, NOW(), NOW()),
('Crespo', 1, true, NOW(), NOW()),
('Ondulado', 1, true, NOW(), NOW());

-- Inserir alternativas para a pergunta 2 (Tom de pele)
INSERT INTO alternativas (texto, pergunta_id, ativo, created_at, updated_at) VALUES
('Branca', 2, true, NOW(), NOW()),
('Preta', 2, true, NOW(), NOW()),
('Amarela', 2, true, NOW(), NOW()),
('Parda', 2, true, NOW(), NOW()),
('Indígena', 2, true, NOW(), NOW());

-- Inserir alternativas para a pergunta 3 (Formato do rosto)
INSERT INTO alternativas (texto, pergunta_id, ativo, created_at, updated_at) VALUES
('Oval', 3, true, NOW(), NOW()),
('Redondo', 3, true, NOW(), NOW()),
('Quadrado', 3, true, NOW(), NOW()),
('Diamante', 3, true, NOW(), NOW()),
('Triangular', 3, true, NOW(), NOW());

-- Inserir alternativas para a pergunta 4 (Estilo preferido)
INSERT INTO alternativas (texto, pergunta_id, ativo, created_at, updated_at) VALUES
('Classico', 4, true, NOW(), NOW()),
('Moderno', 4, true, NOW(), NOW()),
('Romantico', 4, true, NOW(), NOW()),
('Aventureiro', 4, true, NOW(), NOW()),
('Minimalista', 4, true, NOW(), NOW());



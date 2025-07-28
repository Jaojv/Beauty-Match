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
DELETE FROM alternativas;
DELETE FROM perguntas;
DELETE FROM recomendacoes;
DELETE FROM respostas_quiz_detalhes;
DELETE FROM respostas_quiz;

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
('Pele Clara', 2, true, NOW(), NOW()),
('Pele Morena Clara', 2, true, NOW(), NOW()),
('Pele Morena', 2, true, NOW(), NOW()),
('Pele Negra', 2, true, NOW(), NOW());

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

-- Inserir recomendacoes baseadas em criterios
INSERT INTO recomendacoes (criterio, descricao, ativo, created_at, updated_at) VALUES
-- Cabelo Liso + Pele Clara + Rosto Oval + Classico
('LISO_PELE_CLARA_OVAL_CLASSICO', 'Para cabelos lisos com pele clara e rosto oval, recomendamos um corte classico em camadas medias com franja lateral. Tons de cor: castanho claro ou loiro dourado suave. Este estilo valoriza a simetria do seu rosto oval e combina perfeitamente com sua pele clara.', true, NOW(), NOW()),

-- Cabelo Liso + Pele Clara + Rosto Oval + Moderno
('LISO_PELE_CLARA_OVAL_MODERNO', 'Para cabelos lisos com pele clara e rosto oval, sugerimos um corte moderno com assimetria e textura. Tons de cor: loiro platinado ou castanho chocolate. Este estilo adiciona personalidade mantendo a elegancia natural do seu rosto oval.', true, NOW(), NOW()),

-- Cabelo Cacheado + Pele Morena Clara + Rosto Redondo + Romantico
('CACHEADO_PELE_MORENA_CLARA_REDONDO_ROMANTICO', 'Para cabelos cacheados com pele morena clara e rosto redondo, recomendamos um corte em camadas longas com volume na parte superior. Tons de cor: castanho acobreado ou ruivo suave. Este estilo alonga visualmente o rosto e cria um visual romantico e feminino.', true, NOW(), NOW()),

-- Cabelo Cacheado + Pele Morena + Rosto Quadrado + Moderno
('CACHEADO_PELE_MORENA_QUADRADO_MODERNO', 'Para cabelos cacheados com pele morena e rosto quadrado, sugerimos um corte pixie moderno ou bob assimetrico. Tons de cor: castanho escuro ou caramelo. Este estilo suaviza os angulos do rosto e cria um visual contemporaneo e sofisticado.', true, NOW(), NOW()),

-- Cabelo Crespo + Pele Negra + Rosto Diamante + Aventureiro
('CRESPO_PELE_NEGRA_DIAMANTE_AVENTUREIRO', 'Para cabelos crespos com pele negra e rosto diamante, recomendamos um corte afro moderno ou dreadlocks. Tons de cor: manter a cor natural ou adicionar highlights dourados. Este estilo celebra a textura natural do cabelo e cria um visual unico e expressivo.', true, NOW(), NOW()),

-- Cabelo Ondulado + Pele Morena + Rosto Triangular + Minimalista
('ONDULADO_PELE_MORENA_TRIANGULAR_MINIMALISTA', 'Para cabelos ondulados com pele morena e rosto triangular, sugerimos um corte bob medio com volume equilibrado. Tons de cor: castanho natural ou caramelo. Este estilo equilibra a largura do rosto e mantem um visual limpo e minimalista.', true, NOW(), NOW()),

-- Cabelo Liso + Pele Negra + Rosto Redondo + Classico
('LISO_PELE_NEGRA_REDONDO_CLASSICO', 'Para cabelos lisos com pele negra e rosto redondo, recomendamos um corte longo com camadas suaves e franja lateral. Tons de cor: preto profundo ou castanho escuro. Este estilo alonga o rosto e cria um visual classico e elegante.', true, NOW(), NOW()),

-- Cabelo Cacheado + Pele Clara + Rosto Quadrado + Romantico
('CACHEADO_PELE_CLARA_QUADRADO_ROMANTICO', 'Para cabelos cacheados com pele clara e rosto quadrado, sugerimos um corte em camadas longas com volume lateral. Tons de cor: loiro mel ou castanho claro. Este estilo suaviza os angulos e cria um visual romantico e feminino.', true, NOW(), NOW()),

-- Cabelo Crespo + Pele Morena + Rosto Oval + Moderno
('CRESPO_PELE_MORENA_OVAL_MODERNO', 'Para cabelos crespos com pele morena e rosto oval, recomendamos um corte pixie texturizado ou bob curto. Tons de cor: castanho medio ou caramelo. Este estilo valoriza a textura natural e cria um visual moderno e versatil.', true, NOW(), NOW()),

-- Cabelo Ondulado + Pele Negra + Rosto Triangular + Aventureiro
('ONDULADO_PELE_NEGRA_TRIANGULAR_AVENTUREIRO', 'Para cabelos ondulados com pele negra e rosto triangular, sugerimos um corte assimetrico com volume superior. Tons de cor: preto azulado ou castanho escuro com highlights. Este estilo adiciona personalidade e equilibra a forma do rosto.', true, NOW(), NOW()),

-- Recomendacao padrao para combinacoes nao mapeadas
('PADRAO', 'Com base nas suas caracteristicas, recomendamos consultar um profissional para uma avaliacao personalizada. Cada pessoa e unica e merece um tratamento individualizado que valorize suas caracteristicas naturais e atenda as suas preferencias de estilo.', true, NOW(), NOW());

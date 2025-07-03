-- Inserir usuários de teste (senhas criptografadas com BCrypt)
-- Senha para todos: 123456

-- Administrador
INSERT IGNORE INTO usuario (username, password, nome, email, telefone, criado_em, atualizado_em, tipo_usuario)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Administrador', 'admin@beautymatch.com', '11999999999', NOW(), NOW(), 'ADMIN');

-- Proprietário
INSERT IGNORE INTO usuario (username, password, nome, email, telefone, criado_em, atualizado_em, tipo_usuario)
VALUES ('proprietario', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Proprietário', 'proprietario@beautymatch.com', '11988888888', NOW(), NOW(), 'PROPRIETARIO');

-- Profissional
INSERT IGNORE INTO usuario (username, password, nome, email, telefone, criado_em, atualizado_em, tipo_usuario)
VALUES ('profissional', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Profissional', 'profissional@beautymatch.com', '11977777777', NOW(), NOW(), 'PROFISSIONAL');

-- Cliente
INSERT IGNORE INTO usuario (username, password, nome, email, telefone, criado_em, atualizado_em, tipo_usuario)
VALUES ('cliente', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Cliente', 'cliente@beautymatch.com', '11966666666', NOW(), NOW(), 'CLIENTE');

-- ============================================================================
-- DADOS INICIAIS DO MÓDULO QUIZ
-- ============================================================================

-- Inserir perguntas do quiz
INSERT IGNORE INTO perguntas (texto, ordem, ativo, created_at, updated_at) VALUES
('Qual o seu tipo de cabelo?', 1, true, NOW(), NOW()),
('Qual o seu tom de pele?', 2, true, NOW(), NOW()),
('Qual o formato do seu rosto?', 3, true, NOW(), NOW()),
('Qual o seu estilo preferido?', 4, true, NOW(), NOW());

-- Inserir alternativas para a pergunta 1 (Tipo de cabelo)
INSERT IGNORE INTO alternativas (texto, pergunta_id, ativo, created_at, updated_at) VALUES
('Liso', 1, true, NOW(), NOW()),
('Cacheado', 1, true, NOW(), NOW()),
('Crespo', 1, true, NOW(), NOW()),
('Ondulado', 1, true, NOW(), NOW());

-- Inserir alternativas para a pergunta 2 (Tom de pele)
INSERT IGNORE INTO alternativas (texto, pergunta_id, ativo, created_at, updated_at) VALUES
('Clara', 2, true, NOW(), NOW()),
('Média', 2, true, NOW(), NOW()),
('Escura', 2, true, NOW(), NOW()),
('Muito escura', 2, true, NOW(), NOW());

-- Inserir alternativas para a pergunta 3 (Formato do rosto)
INSERT IGNORE INTO alternativas (texto, pergunta_id, ativo, created_at, updated_at) VALUES
('Oval', 3, true, NOW(), NOW()),
('Redondo', 3, true, NOW(), NOW()),
('Quadrado', 3, true, NOW(), NOW()),
('Diamante', 3, true, NOW(), NOW()),
('Triangular', 3, true, NOW(), NOW());

-- Inserir alternativas para a pergunta 4 (Estilo preferido)
INSERT IGNORE INTO alternativas (texto, pergunta_id, ativo, created_at, updated_at) VALUES
('Clássico', 4, true, NOW(), NOW()),
('Moderno', 4, true, NOW(), NOW()),
('Romântico', 4, true, NOW(), NOW()),
('Aventureiro', 4, true, NOW(), NOW()),
('Minimalista', 4, true, NOW(), NOW());

-- Inserir recomendações baseadas em critérios
INSERT IGNORE INTO recomendacoes (criterio, descricao, ativo, created_at, updated_at) VALUES
-- Cabelo Liso + Pele Clara + Rosto Oval + Clássico
('LISO_CLARA_OVAL_CLASSICO', 'Para cabelos lisos com pele clara e rosto oval, recomendamos um corte clássico em camadas médias com franja lateral. Tons de cor: castanho claro ou loiro dourado suave. Este estilo valoriza a simetria do seu rosto oval e combina perfeitamente com sua pele clara.', true, NOW(), NOW()),

-- Cabelo Liso + Pele Clara + Rosto Oval + Moderno
('LISO_CLARA_OVAL_MODERNO', 'Para cabelos lisos com pele clara e rosto oval, sugerimos um corte moderno com assimetria e textura. Tons de cor: loiro platinado ou castanho chocolate. Este estilo adiciona personalidade mantendo a elegância natural do seu rosto oval.', true, NOW(), NOW()),

-- Cabelo Cacheado + Pele Média + Rosto Redondo + Romântico
('CACHEADO_MEDIA_REDONDO_ROMANTICO', 'Para cabelos cacheados com pele média e rosto redondo, recomendamos um corte em camadas longas com volume na parte superior. Tons de cor: castanho acobreado ou ruivo suave. Este estilo alonga visualmente o rosto e cria um visual romântico e feminino.', true, NOW(), NOW()),

-- Cabelo Cacheado + Pele Escura + Rosto Quadrado + Moderno
('CACHEADO_ESCURA_QUADRADO_MODERNO', 'Para cabelos cacheados com pele escura e rosto quadrado, sugerimos um corte pixie moderno ou bob assimétrico. Tons de cor: preto azulado ou castanho escuro. Este estilo suaviza os ângulos do rosto e cria um visual contemporâneo e sofisticado.', true, NOW(), NOW()),

-- Cabelo Crespo + Pele Escura + Rosto Diamante + Aventureiro
('CRESPO_ESCURA_DIAMANTE_AVENTUREIRO', 'Para cabelos crespos com pele escura e rosto diamante, recomendamos um corte afro moderno ou dreadlocks. Tons de cor: manter a cor natural ou adicionar highlights dourados. Este estilo celebra a textura natural do cabelo e cria um visual único e expressivo.', true, NOW(), NOW()),

-- Cabelo Ondulado + Pele Média + Rosto Triangular + Minimalista
('ONDULADO_MEDIA_TRIANGULAR_MINIMALISTA', 'Para cabelos ondulados com pele média e rosto triangular, sugerimos um corte bob médio com volume equilibrado. Tons de cor: castanho natural ou caramelo claro. Este estilo equilibra a largura do rosto e mantém um visual limpo e minimalista.', true, NOW(), NOW()),

-- Cabelo Liso + Pele Escura + Rosto Redondo + Clássico
('LISO_ESCURA_REDONDO_CLASSICO', 'Para cabelos lisos com pele escura e rosto redondo, recomendamos um corte longo com camadas suaves e franja lateral. Tons de cor: preto profundo ou castanho escuro. Este estilo alonga o rosto e cria um visual clássico e elegante.', true, NOW(), NOW()),

-- Cabelo Cacheado + Pele Clara + Rosto Quadrado + Romântico
('CACHEADO_CLARA_QUADRADO_ROMANTICO', 'Para cabelos cacheados com pele clara e rosto quadrado, sugerimos um corte em camadas longas com volume lateral. Tons de cor: loiro mel ou castanho claro. Este estilo suaviza os ângulos e cria um visual romântico e feminino.', true, NOW(), NOW()),

-- Cabelo Crespo + Pele Média + Rosto Oval + Moderno
('CRESPO_MEDIA_OVAL_MODERNO', 'Para cabelos crespos com pele média e rosto oval, recomendamos um corte pixie texturizado ou bob curto. Tons de cor: castanho médio ou caramelo. Este estilo valoriza a textura natural e cria um visual moderno e versátil.', true, NOW(), NOW()),

-- Cabelo Ondulado + Pele Escura + Rosto Triangular + Aventureiro
('ONDULADO_ESCURA_TRIANGULAR_AVENTUREIRO', 'Para cabelos ondulados com pele escura e rosto triangular, sugerimos um corte assimétrico com volume superior. Tons de cor: preto azulado ou castanho escuro com highlights. Este estilo adiciona personalidade e equilibra a forma do rosto.', true, NOW(), NOW()),

-- Recomendação padrão para combinações não mapeadas
('PADRAO', 'Com base nas suas características, recomendamos consultar um profissional para uma avaliação personalizada. Cada pessoa é única e merece um tratamento individualizado que valorize suas características naturais e atenda às suas preferências de estilo.', true, NOW(), NOW());

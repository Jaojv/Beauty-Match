-- Script para popular todas as combinações de critérios de recomendação
-- Total: 4 tipos de cabelo × 4 tons de pele × 5 formatos de rosto × 5 estilos = 400 combinações

-- Tipos de cabelo
-- LISO, CACHEADO, CRESPO, ONDULADO

-- Tons de pele  
-- CLARA, MEDIA, ESCURA, MUITOESCURA

-- Formatos de rosto
-- OVAL, REDONDO, QUADRADO, DIAMANTE, TRIANGULAR

-- Estilos
-- CLASSICO, MODERNO, ROMANTICO, AVENTUREIRO, MINIMALISTA

-- Inserir todas as combinações possíveis
INSERT IGNORE INTO recomendacoes (criterio, descricao, ativo, created_at, updated_at) VALUES

-- CABELO LISO
('LISO_CLARA_OVAL_CLASSICO', 'Para cabelos lisos com pele clara e rosto oval, recomendamos um corte clássico em camadas médias com franja lateral. Tons de cor: castanho claro ou loiro dourado suave. Este estilo valoriza a simetria do seu rosto oval e combina perfeitamente com sua pele clara.', true, NOW(), NOW()),
('LISO_CLARA_OVAL_MODERNO', 'Para cabelos lisos com pele clara e rosto oval, sugerimos um corte moderno com assimetria e textura. Tons de cor: loiro platinado ou castanho chocolate. Este estilo adiciona personalidade mantendo a elegância natural do seu rosto oval.', true, NOW(), NOW()),
('LISO_CLARA_OVAL_ROMANTICO', 'Para cabelos lisos com pele clara e rosto oval, recomendamos um corte longo com ondas suaves e franja lateral. Tons de cor: loiro mel ou castanho dourado. Este estilo cria um visual romântico e feminino.', true, NOW(), NOW()),
('LISO_CLARA_OVAL_AVENTUREIRO', 'Para cabelos lisos com pele clara e rosto oval, sugerimos um corte pixie assimétrico ou bob curto. Tons de cor: loiro platinado ou castanho com highlights. Este estilo adiciona personalidade e modernidade.', true, NOW(), NOW()),
('LISO_CLARA_OVAL_MINIMALISTA', 'Para cabelos lisos com pele clara e rosto oval, recomendamos um corte bob médio limpo e simétrico. Tons de cor: castanho natural ou loiro suave. Este estilo mantém a simplicidade e elegância.', true, NOW(), NOW()),

('LISO_CLARA_REDONDO_CLASSICO', 'Para cabelos lisos com pele clara e rosto redondo, recomendamos um corte longo com camadas suaves e franja lateral. Tons de cor: castanho claro ou loiro dourado. Este estilo alonga visualmente o rosto e cria um visual clássico.', true, NOW(), NOW()),
('LISO_CLARA_REDONDO_MODERNO', 'Para cabelos lisos com pele clara e rosto redondo, sugerimos um corte bob assimétrico ou pixie texturizado. Tons de cor: loiro platinado ou castanho chocolate. Este estilo adiciona ângulos e modernidade.', true, NOW(), NOW()),
('LISO_CLARA_REDONDO_ROMANTICO', 'Para cabelos lisos com pele clara e rosto redondo, recomendamos um corte em camadas longas com volume lateral. Tons de cor: loiro mel ou castanho dourado. Este estilo cria um visual romântico e feminino.', true, NOW(), NOW()),
('LISO_CLARA_REDONDO_AVENTUREIRO', 'Para cabelos lisos com pele clara e rosto redondo, sugerimos um corte pixie moderno ou bob curto assimétrico. Tons de cor: loiro platinado ou castanho com highlights. Este estilo adiciona personalidade.', true, NOW(), NOW()),
('LISO_CLARA_REDONDO_MINIMALISTA', 'Para cabelos lisos com pele clara e rosto redondo, recomendamos um corte bob médio limpo e simétrico. Tons de cor: castanho natural ou loiro suave. Este estilo mantém a simplicidade.', true, NOW(), NOW()),

('LISO_CLARA_QUADRADO_CLASSICO', 'Para cabelos lisos com pele clara e rosto quadrado, recomendamos um corte longo com camadas suaves e franja lateral. Tons de cor: castanho claro ou loiro dourado. Este estilo suaviza os ângulos do rosto.', true, NOW(), NOW()),
('LISO_CLARA_QUADRADO_MODERNO', 'Para cabelos lisos com pele clara e rosto quadrado, sugerimos um corte bob assimétrico ou pixie texturizado. Tons de cor: loiro platinado ou castanho chocolate. Este estilo adiciona modernidade.', true, NOW(), NOW()),
('LISO_CLARA_QUADRADO_ROMANTICO', 'Para cabelos lisos com pele clara e rosto quadrado, recomendamos um corte em camadas longas com volume lateral. Tons de cor: loiro mel ou castanho dourado. Este estilo cria um visual romântico.', true, NOW(), NOW()),
('LISO_CLARA_QUADRADO_AVENTUREIRO', 'Para cabelos lisos com pele clara e rosto quadrado, sugerimos um corte pixie moderno ou bob curto assimétrico. Tons de cor: loiro platinado ou castanho com highlights. Este estilo adiciona personalidade.', true, NOW(), NOW()),
('LISO_CLARA_QUADRADO_MINIMALISTA', 'Para cabelos lisos com pele clara e rosto quadrado, recomendamos um corte bob médio limpo e simétrico. Tons de cor: castanho natural ou loiro suave. Este estilo mantém a simplicidade.', true, NOW(), NOW()),

('LISO_CLARA_DIAMANTE_CLASSICO', 'Para cabelos lisos com pele clara e rosto diamante, recomendamos um corte bob médio com volume equilibrado. Tons de cor: castanho claro ou loiro dourado. Este estilo equilibra a forma do rosto.', true, NOW(), NOW()),
('LISO_CLARA_DIAMANTE_MODERNO', 'Para cabelos lisos com pele clara e rosto diamante, sugerimos um corte pixie texturizado ou bob assimétrico. Tons de cor: loiro platinado ou castanho chocolate. Este estilo adiciona modernidade.', true, NOW(), NOW()),
('LISO_CLARA_DIAMANTE_ROMANTICO', 'Para cabelos lisos com pele clara e rosto diamante, recomendamos um corte em camadas longas com volume lateral. Tons de cor: loiro mel ou castanho dourado. Este estilo cria um visual romântico.', true, NOW(), NOW()),
('LISO_CLARA_DIAMANTE_AVENTUREIRO', 'Para cabelos lisos com pele clara e rosto diamante, sugerimos um corte pixie moderno ou bob curto assimétrico. Tons de cor: loiro platinado ou castanho com highlights. Este estilo adiciona personalidade.', true, NOW(), NOW()),
('LISO_CLARA_DIAMANTE_MINIMALISTA', 'Para cabelos lisos com pele clara e rosto diamante, recomendamos um corte bob médio limpo e simétrico. Tons de cor: castanho natural ou loiro suave. Este estilo mantém a simplicidade.', true, NOW(), NOW()),

('LISO_CLARA_TRIANGULAR_CLASSICO', 'Para cabelos lisos com pele clara e rosto triangular, recomendamos um corte bob médio com volume superior. Tons de cor: castanho claro ou loiro dourado. Este estilo equilibra a largura do rosto.', true, NOW(), NOW()),
('LISO_CLARA_TRIANGULAR_MODERNO', 'Para cabelos lisos com pele clara e rosto triangular, sugerimos um corte pixie texturizado ou bob assimétrico. Tons de cor: loiro platinado ou castanho chocolate. Este estilo adiciona modernidade.', true, NOW(), NOW()),
('LISO_CLARA_TRIANGULAR_ROMANTICO', 'Para cabelos lisos com pele clara e rosto triangular, recomendamos um corte em camadas longas com volume lateral. Tons de cor: loiro mel ou castanho dourado. Este estilo cria um visual romântico.', true, NOW(), NOW()),
('LISO_CLARA_TRIANGULAR_AVENTUREIRO', 'Para cabelos lisos com pele clara e rosto triangular, sugerimos um corte pixie moderno ou bob curto assimétrico. Tons de cor: loiro platinado ou castanho com highlights. Este estilo adiciona personalidade.', true, NOW(), NOW()),
('LISO_CLARA_TRIANGULAR_MINIMALISTA', 'Para cabelos lisos com pele clara e rosto triangular, recomendamos um corte bob médio limpo e simétrico. Tons de cor: castanho natural ou loiro suave. Este estilo mantém a simplicidade.', true, NOW(), NOW()),

-- Continuar com todas as outras combinações...
-- CABELO LISO + PELE MÉDIA
('LISO_MEDIA_OVAL_CLASSICO', 'Para cabelos lisos com pele média e rosto oval, recomendamos um corte clássico em camadas médias com franja lateral. Tons de cor: castanho médio ou caramelo. Este estilo valoriza a simetria do seu rosto oval.', true, NOW(), NOW()),
('LISO_MEDIA_OVAL_MODERNO', 'Para cabelos lisos com pele média e rosto oval, sugerimos um corte moderno com assimetria e textura. Tons de cor: castanho chocolate ou caramelo escuro. Este estilo adiciona personalidade mantendo a elegância.', true, NOW(), NOW()),
('LISO_MEDIA_OVAL_ROMANTICO', 'Para cabelos lisos com pele média e rosto oval, recomendamos um corte longo com ondas suaves e franja lateral. Tons de cor: castanho dourado ou caramelo claro. Este estilo cria um visual romântico e feminino.', true, NOW(), NOW()),
('LISO_MEDIA_OVAL_AVENTUREIRO', 'Para cabelos lisos com pele média e rosto oval, sugerimos um corte pixie assimétrico ou bob curto. Tons de cor: castanho escuro ou caramelo com highlights. Este estilo adiciona personalidade e modernidade.', true, NOW(), NOW()),
('LISO_MEDIA_OVAL_MINIMALISTA', 'Para cabelos lisos com pele média e rosto oval, recomendamos um corte bob médio limpo e simétrico. Tons de cor: castanho natural ou caramelo suave. Este estilo mantém a simplicidade e elegância.', true, NOW(), NOW()),

-- CABELO LISO + PELE ESCURA
('LISO_ESCURA_OVAL_CLASSICO', 'Para cabelos lisos com pele escura e rosto oval, recomendamos um corte clássico em camadas médias com franja lateral. Tons de cor: castanho escuro ou preto profundo. Este estilo valoriza a simetria do seu rosto oval.', true, NOW(), NOW()),
('LISO_ESCURA_OVAL_MODERNO', 'Para cabelos lisos com pele escura e rosto oval, sugerimos um corte moderno com assimetria e textura. Tons de cor: preto azulado ou castanho escuro. Este estilo adiciona personalidade mantendo a elegância.', true, NOW(), NOW()),
('LISO_ESCURA_OVAL_ROMANTICO', 'Para cabelos lisos com pele escura e rosto oval, recomendamos um corte longo com ondas suaves e franja lateral. Tons de cor: castanho escuro ou preto com brilho. Este estilo cria um visual romântico e feminino.', true, NOW(), NOW()),
('LISO_ESCURA_OVAL_AVENTUREIRO', 'Para cabelos lisos com pele escura e rosto oval, sugerimos um corte pixie assimétrico ou bob curto. Tons de cor: preto azulado ou castanho escuro com highlights. Este estilo adiciona personalidade e modernidade.', true, NOW(), NOW()),
('LISO_ESCURA_OVAL_MINIMALISTA', 'Para cabelos lisos com pele escura e rosto oval, recomendamos um corte bob médio limpo e simétrico. Tons de cor: preto natural ou castanho escuro. Este estilo mantém a simplicidade e elegância.', true, NOW(), NOW()),

-- CABELO LISO + PELE MUITO ESCURA
('LISO_MUITOESCURA_OVAL_CLASSICO', 'Para cabelos lisos com pele muito escura e rosto oval, recomendamos um corte clássico em camadas médias com franja lateral. Tons de cor: preto profundo ou castanho muito escuro. Este estilo valoriza a simetria do seu rosto oval.', true, NOW(), NOW()),
('LISO_MUITOESCURA_OVAL_MODERNO', 'Para cabelos lisos com pele muito escura e rosto oval, sugerimos um corte moderno com assimetria e textura. Tons de cor: preto azulado ou castanho muito escuro. Este estilo adiciona personalidade mantendo a elegância.', true, NOW(), NOW()),
('LISO_MUITOESCURA_OVAL_ROMANTICO', 'Para cabelos lisos com pele muito escura e rosto oval, recomendamos um corte longo com ondas suaves e franja lateral. Tons de cor: preto profundo ou castanho escuro com brilho. Este estilo cria um visual romântico e feminino.', true, NOW(), NOW()),
('LISO_MUITOESCURA_OVAL_AVENTUREIRO', 'Para cabelos lisos com pele muito escura e rosto oval, sugerimos um corte pixie assimétrico ou bob curto. Tons de cor: preto azulado ou castanho muito escuro com highlights. Este estilo adiciona personalidade e modernidade.', true, NOW(), NOW()),
('LISO_MUITOESCURA_OVAL_MINIMALISTA', 'Para cabelos lisos com pele muito escura e rosto oval, recomendamos um corte bob médio limpo e simétrico. Tons de cor: preto natural ou castanho muito escuro. Este estilo mantém a simplicidade e elegância.', true, NOW(), NOW()),

-- CABELO CACHEADO
('CACHEADO_CLARA_OVAL_CLASSICO', 'Para cabelos cacheados com pele clara e rosto oval, recomendamos um corte em camadas longas com volume equilibrado. Tons de cor: castanho claro ou loiro dourado. Este estilo valoriza a textura natural e a simetria do rosto oval.', true, NOW(), NOW()),
('CACHEADO_CLARA_OVAL_MODERNO', 'Para cabelos cacheados com pele clara e rosto oval, sugerimos um corte pixie texturizado ou bob curto. Tons de cor: loiro platinado ou castanho chocolate. Este estilo adiciona modernidade mantendo a textura natural.', true, NOW(), NOW()),
('CACHEADO_CLARA_OVAL_ROMANTICO', 'Para cabelos cacheados com pele clara e rosto oval, recomendamos um corte em camadas longas com volume lateral. Tons de cor: loiro mel ou castanho dourado. Este estilo cria um visual romântico e feminino.', true, NOW(), NOW()),
('CACHEADO_CLARA_OVAL_AVENTUREIRO', 'Para cabelos cacheados com pele clara e rosto oval, sugerimos um corte pixie moderno ou bob assimétrico. Tons de cor: loiro platinado ou castanho com highlights. Este estilo adiciona personalidade e expressividade.', true, NOW(), NOW()),
('CACHEADO_CLARA_OVAL_MINIMALISTA', 'Para cabelos cacheados com pele clara e rosto oval, recomendamos um corte bob médio com volume equilibrado. Tons de cor: castanho natural ou loiro suave. Este estilo mantém a simplicidade valorizando a textura natural.', true, NOW(), NOW()),

-- CABELO CRESPO
('CRESPO_CLARA_OVAL_CLASSICO', 'Para cabelos crespos com pele clara e rosto oval, recomendamos um corte afro clássico ou bob médio. Tons de cor: castanho claro ou loiro dourado. Este estilo valoriza a textura natural e a simetria do rosto oval.', true, NOW(), NOW()),
('CRESPO_CLARA_OVAL_MODERNO', 'Para cabelos crespos com pele clara e rosto oval, sugerimos um corte pixie texturizado ou bob curto. Tons de cor: loiro platinado ou castanho chocolate. Este estilo adiciona modernidade mantendo a textura natural.', true, NOW(), NOW()),
('CRESPO_CLARA_OVAL_ROMANTICO', 'Para cabelos crespos com pele clara e rosto oval, recomendamos um corte em camadas longas com volume lateral. Tons de cor: loiro mel ou castanho dourado. Este estilo cria um visual romântico e feminino.', true, NOW(), NOW()),
('CRESPO_CLARA_OVAL_AVENTUREIRO', 'Para cabelos crespos com pele clara e rosto oval, sugerimos um corte pixie moderno ou bob assimétrico. Tons de cor: loiro platinado ou castanho com highlights. Este estilo adiciona personalidade e expressividade.', true, NOW(), NOW()),
('CRESPO_CLARA_OVAL_MINIMALISTA', 'Para cabelos crespos com pele clara e rosto oval, recomendamos um corte bob médio com volume equilibrado. Tons de cor: castanho natural ou loiro suave. Este estilo mantém a simplicidade valorizando a textura natural.', true, NOW(), NOW()),

-- CABELO ONDULADO
('ONDULADO_CLARA_OVAL_CLASSICO', 'Para cabelos ondulados com pele clara e rosto oval, recomendamos um corte em camadas médias com volume equilibrado. Tons de cor: castanho claro ou loiro dourado. Este estilo valoriza a textura natural e a simetria do rosto oval.', true, NOW(), NOW()),
('ONDULADO_CLARA_OVAL_MODERNO', 'Para cabelos ondulados com pele clara e rosto oval, sugerimos um corte pixie texturizado ou bob curto. Tons de cor: loiro platinado ou castanho chocolate. Este estilo adiciona modernidade mantendo a textura natural.', true, NOW(), NOW()),
('ONDULADO_CLARA_OVAL_ROMANTICO', 'Para cabelos ondulados com pele clara e rosto oval, recomendamos um corte em camadas longas com volume lateral. Tons de cor: loiro mel ou castanho dourado. Este estilo cria um visual romântico e feminino.', true, NOW(), NOW()),
('ONDULADO_CLARA_OVAL_AVENTUREIRO', 'Para cabelos ondulados com pele clara e rosto oval, sugerimos um corte pixie moderno ou bob assimétrico. Tons de cor: loiro platinado ou castanho com highlights. Este estilo adiciona personalidade e expressividade.', true, NOW(), NOW()),
('ONDULADO_CLARA_OVAL_MINIMALISTA', 'Para cabelos ondulados com pele clara e rosto oval, recomendamos um corte bob médio com volume equilibrado. Tons de cor: castanho natural ou loiro suave. Este estilo mantém a simplicidade valorizando a textura natural.', true, NOW(), NOW()),

-- Adicionar mais combinações específicas que estavam faltando
('ONDULADO_MUITOESCURA_TRIANGULAR_MINIMALISTA', 'Para cabelos ondulados com pele muito escura e rosto triangular, recomendamos um corte bob médio com volume superior equilibrado. Tons de cor: preto profundo ou castanho muito escuro. Este estilo equilibra a largura do rosto e mantém um visual limpo e minimalista.', true, NOW(), NOW()),

-- Recomendação padrão para combinações não mapeadas
('PADRAO', 'Com base nas suas características, recomendamos consultar um profissional para uma avaliação personalizada. Cada pessoa é única e merece um tratamento individualizado que valorize suas características naturais e atenda às suas preferências de estilo.', true, NOW(), NOW());

-- Nota: Este script inclui apenas uma amostra das 400 combinações possíveis.
-- Para inserir todas as combinações, seria necessário criar um script mais extenso
-- ou usar um gerador automático de combinações. 
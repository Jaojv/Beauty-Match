-- Script para limpar horários de funcionamento duplicados
-- Execute este script no banco de dados se houver problemas de duplicação

-- Verificar horários duplicados
SELECT 
    salao_id, 
    dia_semana, 
    hora_inicio, 
    hora_fim, 
    COUNT(*) as quantidade
FROM horario_funcionamento_salao 
WHERE ativo = true
GROUP BY salao_id, dia_semana, hora_inicio, hora_fim
HAVING COUNT(*) > 1;

-- Remover duplicatas mantendo apenas o primeiro registro
DELETE h1 FROM horario_funcionamento_salao h1
INNER JOIN horario_funcionamento_salao h2 
WHERE h1.id > h2.id 
AND h1.salao_id = h2.salao_id 
AND h1.dia_semana = h2.dia_semana 
AND h1.hora_inicio = h2.hora_inicio 
AND h1.hora_fim = h2.hora_fim
AND h1.ativo = true 
AND h2.ativo = true;

-- Verificar se ainda há duplicatas
SELECT 
    salao_id, 
    dia_semana, 
    hora_inicio, 
    hora_fim, 
    COUNT(*) as quantidade
FROM horario_funcionamento_salao 
WHERE ativo = true
GROUP BY salao_id, dia_semana, hora_inicio, hora_fim
HAVING COUNT(*) > 1; 
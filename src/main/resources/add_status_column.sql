-- Script para adicionar o campo status na tabela salao
-- Execute este script no seu banco de dados MySQL

-- Adicionar coluna status se ela não existir
ALTER TABLE salao ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE';

-- Atualizar salões existentes para status APROVADO (assumindo que já estão aprovados)
UPDATE salao SET status = 'APROVADO' WHERE status IS NULL OR status = '';

-- Verificar se a coluna foi adicionada corretamente
SELECT id, nome, status FROM salao LIMIT 5; 
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
<<<<<<< HEAD
INSERT INTO usuario (username, password, nome, email, telefone, criado_em, tipo_usuario)
VALUES ('cliente', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Cliente', 'cliente@beautymatch.com', '11966666666', NOW(), 'CLIENTE'); 
=======
INSERT INTO usuarios (username, password, email, telefone, criado_em, tipo_usuario)
VALUES ('cliente', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'cliente@beautymatch.com', '11966666666', NOW(), 'CLIENTE');

-- Dados iniciais para o sistema BeautyMatch

-- Inserir roles
INSERT INTO roles (nome) VALUES ('ADMIN') ON DUPLICATE KEY UPDATE nome = nome;
INSERT INTO roles (nome) VALUES ('CLIENTE') ON DUPLICATE KEY UPDATE nome = nome;
INSERT INTO roles (nome) VALUES ('PROFISSIONAL') ON DUPLICATE KEY UPDATE nome = nome;
INSERT INTO roles (nome) VALUES ('PROPRIETARIO') ON DUPLICATE KEY UPDATE nome = nome;

-- Inserir admin padrão
INSERT INTO usuarios (nome, email, senha, tipo_usuario, ativo) 
VALUES ('Administrador', 'admin@beautymatch.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMIN', true)
ON DUPLICATE KEY UPDATE nome = nome;

-- Associar role ADMIN ao usuário admin
INSERT INTO usuario_roles (usuario_id, role_id) 
SELECT u.id_usuario, r.id FROM usuarios u, roles r 
WHERE u.email = 'admin@beautymatch.com' AND r.nome = 'ADMIN'
ON DUPLICATE KEY UPDATE usuario_id = usuario_id;

-- Criar tabela de horários de funcionamento do salão se não existir
CREATE TABLE IF NOT EXISTS horario_funcionamento_salao (
    id_horario_funcionamento BIGINT AUTO_INCREMENT PRIMARY KEY,
    salao_id BIGINT NOT NULL,
    dia_semana VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fim TIME NOT NULL,
    ativo BOOLEAN DEFAULT true,
    FOREIGN KEY (salao_id) REFERENCES salao(id_salao) ON DELETE CASCADE
);

-- Inserir horários padrão para salões existentes (Segunda a Sábado, 8:00 às 18:00)
INSERT INTO horario_funcionamento_salao (salao_id, dia_semana, hora_inicio, hora_fim, ativo)
SELECT 
    s.id_salao,
    'MONDAY',
    '08:00:00',
    '18:00:00',
    true
FROM salao s
WHERE NOT EXISTS (
    SELECT 1 FROM horario_funcionamento_salao hfs 
    WHERE hfs.salao_id = s.id_salao AND hfs.dia_semana = 'MONDAY'
);

INSERT INTO horario_funcionamento_salao (salao_id, dia_semana, hora_inicio, hora_fim, ativo)
SELECT 
    s.id_salao,
    'TUESDAY',
    '08:00:00',
    '18:00:00',
    true
FROM salao s
WHERE NOT EXISTS (
    SELECT 1 FROM horario_funcionamento_salao hfs 
    WHERE hfs.salao_id = s.id_salao AND hfs.dia_semana = 'TUESDAY'
);

INSERT INTO horario_funcionamento_salao (salao_id, dia_semana, hora_inicio, hora_fim, ativo)
SELECT 
    s.id_salao,
    'WEDNESDAY',
    '08:00:00',
    '18:00:00',
    true
FROM salao s
WHERE NOT EXISTS (
    SELECT 1 FROM horario_funcionamento_salao hfs 
    WHERE hfs.salao_id = s.id_salao AND hfs.dia_semana = 'WEDNESDAY'
);

INSERT INTO horario_funcionamento_salao (salao_id, dia_semana, hora_inicio, hora_fim, ativo)
SELECT 
    s.id_salao,
    'THURSDAY',
    '08:00:00',
    '18:00:00',
    true
FROM salao s
WHERE NOT EXISTS (
    SELECT 1 FROM horario_funcionamento_salao hfs 
    WHERE hfs.salao_id = s.id_salao AND hfs.dia_semana = 'THURSDAY'
);

INSERT INTO horario_funcionamento_salao (salao_id, dia_semana, hora_inicio, hora_fim, ativo)
SELECT 
    s.id_salao,
    'FRIDAY',
    '08:00:00',
    '18:00:00',
    true
FROM salao s
WHERE NOT EXISTS (
    SELECT 1 FROM horario_funcionamento_salao hfs 
    WHERE hfs.salao_id = s.id_salao AND hfs.dia_semana = 'FRIDAY'
);

INSERT INTO horario_funcionamento_salao (salao_id, dia_semana, hora_inicio, hora_fim, ativo)
SELECT 
    s.id_salao,
    'SATURDAY',
    '08:00:00',
    '18:00:00',
    true
FROM salao s
WHERE NOT EXISTS (
    SELECT 1 FROM horario_funcionamento_salao hfs 
    WHERE hfs.salao_id = s.id_salao AND hfs.dia_semana = 'SATURDAY'
); 
>>>>>>> 2e23924a7adca3377623ca0972aa530416dcc96e

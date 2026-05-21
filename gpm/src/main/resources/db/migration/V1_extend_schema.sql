-- Create categoria table
CREATE TABLE IF NOT EXISTS categoria (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         nome VARCHAR(150) NOT NULL UNIQUE,
    descricao TEXT
    );

-- Add categoria_id to produto
ALTER TABLE produto ADD COLUMN IF NOT EXISTS categoria_id BIGINT;
ALTER TABLE produto ADD COLUMN IF NOT EXISTS especificacao_tecnica TEXT;
ALTER TABLE produto ADD COLUMN IF NOT EXISTS imagem_url VARCHAR(512);
ALTER TABLE produto ADD CONSTRAINT IF NOT EXISTS fk_prod_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id);

-- Add materiais_utilizados and categoria_id to galeria_portfolio
ALTER TABLE galeria_portfolio ADD COLUMN IF NOT EXISTS materiais_utilizados TEXT;
ALTER TABLE galeria_portfolio ADD COLUMN IF NOT EXISTS categoria_id BIGINT;
ALTER TABLE galeria_portfolio ADD CONSTRAINT IF NOT EXISTS fk_gp_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id);

-- Create endereco table
CREATE TABLE IF NOT EXISTS endereco (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        rua VARCHAR(255),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado CHAR(2),
    cep VARCHAR(20),
    numero INT,
    complemento VARCHAR(255)
    );

-- Add endereco_id to orcamentos
ALTER TABLE orcamentos ADD COLUMN IF NOT EXISTS endereco_id BIGINT;
ALTER TABLE orcamentos ADD CONSTRAINT IF NOT EXISTS fk_orc_endereco FOREIGN KEY (endereco_id) REFERENCES endereco(id);
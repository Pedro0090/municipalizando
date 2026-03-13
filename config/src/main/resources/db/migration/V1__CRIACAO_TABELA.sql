CREATE TABLE estado(
    id BIGINT PRIMARY KEY NOT NULL,
    nome VARCHAR(50) UNIQUE NOT NULL,
    sigla VARCHAR(2) UNIQUE NOT NULL,
    nome_normalizado VARCHAR(100) NOT NULL
);

CREATE TABLE municipio(
    id BIGINT PRIMARY KEY NOT NULL,
    nome VARCHAR(100) NOT NULL,
    populacao INT NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    estado_id BIGINT NOT NULL,
    nome_normalizado VARCHAR(100) NOT NULL,
    CONSTRAINT fk_municipio_estado FOREIGN KEY (estado_id) REFERENCES estado(id) ON DELETE CASCADE
);
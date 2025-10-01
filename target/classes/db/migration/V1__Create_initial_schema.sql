CREATE TABLE filial (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255)
);

CREATE TABLE moto (
    id SERIAL PRIMARY KEY,
    modelo VARCHAR(255) NOT NULL,
    ano INTEGER,
    cor VARCHAR(255),
    filial_id INTEGER REFERENCES filial(id)
);

CREATE TABLE registro_rfid (
    id SERIAL PRIMARY KEY,
    tag_rfid VARCHAR(255) NOT NULL UNIQUE,
    data_hora TIMESTAMP NOT NULL,
    localizacao VARCHAR(255),
    moto_id INTEGER REFERENCES moto(id)
);

INSERT INTO filial (nome, endereco) VALUES ('Filial A', 'Rua A, 123');
INSERT INTO filial (nome, endereco) VALUES ('Filial B', 'Avenida B, 456');

INSERT INTO moto (modelo, ano, cor, filial_id) VALUES ('Honda CB 500', 2023, 'Vermelha', 1);
INSERT INTO moto (modelo, ano, cor, filial_id) VALUES ('Yamaha FZ25', 2022, 'Azul', 1);
INSERT INTO moto (modelo, ano, cor, filial_id) VALUES ('Kawasaki Ninja 400', 2024, 'Verde', 2);

INSERT INTO registro_rfid (tag_rfid, data_hora, localizacao, moto_id) VALUES ('TAG001', NOW(), 'Entrada Filial A', 1);
INSERT INTO registro_rfid (tag_rfid, data_hora, localizacao, moto_id) VALUES ('TAG002', NOW(), 'Estacionamento Filial A', 2);
INSERT INTO registro_rfid (tag_rfid, data_hora, localizacao, moto_id) VALUES ('TAG003', NOW(), 'Showroom Filial B', 3);



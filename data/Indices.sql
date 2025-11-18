USE monitoramentoconsumodeagua;

CREATE INDEX idx_imovel_cep ON imovel(cep_endereco);

CREATE INDEX idx_contrato_usuario ON Contrato(cod_usuario);
CREATE INDEX idx_contrato_imovel ON Contrato(cod_imovel);
CREATE INDEX idx_contrato_hidrometro ON Contrato(num_serie_hidrometro);

CREATE INDEX idx_leitura_hidrometro ON Leitura(num_serie_hidrometro);
CREATE INDEX idx_leitura_data ON Leitura(data_hora_leitura);

CREATE INDEX idx_alerta_leitura ON Alerta(cod_leitura);
CREATE INDEX idx_alerta_data ON Alerta(data_hora_alerta);


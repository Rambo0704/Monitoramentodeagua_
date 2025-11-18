DELIMITER //

CREATE TRIGGER trg_usuario_data_cadastro
BEFORE INSERT ON Usuario
FOR EACH ROW
BEGIN SET NEW.data_cadastro = NOW();
END;
//

CREATE TRIGGER trg_alerta_hora_alerta
BEFORE INSERT ON Alerta
FOR EACH ROW
BEGIN SET NEW.data_hora_alerta = NOW();
END;
//

CREATE TRIGGER trg_update_leitura
BEFORE UPDATE ON Leitura
FOR EACH ROW
BEGIN  SET NEW.data_hora_leitura = NOW();
END;
//

CREATE TRIGGER trg_alerta_consumo_alto
AFTER INSERT ON Leitura
FOR EACH ROW
BEGIN
    IF NEW.valor_medido > 500 THEN
        INSERT INTO Alerta (cod_alerta,tipo_alerta, cod_leitura)
        VALUES (gerar_id('A',10),'Consumo alto detectado', NEW.cod_leitura);
    END IF;
END;
//

CREATE TRIGGER trg_alerta_valor_invalido
AFTER INSERT ON Leitura
FOR EACH ROW
BEGIN
    IF NEW.valor_medido < 0 THEN
        INSERT INTO Alerta (cod_alerta,tipo_alerta, cod_leitura)
        VALUES (gerar_id('A',10),'Leitura inválida', NEW.cod_leitura);
    END IF;
END;
//

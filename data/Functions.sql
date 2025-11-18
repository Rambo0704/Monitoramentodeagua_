DELIMITER $$
CREATE FUNCTION gerar_id(tipo_prefixo VARCHAR(5), tamanho INT) 
RETURNS VARCHAR(255)
DETERMINISTIC
BEGIN
    DECLARE chars_str VARCHAR(62) DEFAULT 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    DECLARE result_str VARCHAR(255) DEFAULT '';
    DECLARE i INT DEFAULT 0;
    DECLARE rand_index INT;
    DECLARE prefixo VARCHAR(255) DEFAULT CONCAT(tipo_prefixo, '_');

    WHILE i < tamanho DO
        SET rand_index = FLOOR(1 + (RAND() * 62));
        SET result_str = CONCAT(result_str, SUBSTRING(chars_str, rand_index, 1));
        SET i = i + 1;
    END WHILE;

    RETURN CONCAT(prefixo, result_str);
END$$
DELIMITER ;

-- calcular consumo medio
DELIMITER $$
DELIMITER $$

CREATE FUNCTION consumo_medio_usuario(p_cod_usuario VARCHAR(20)) RETURNS DECIMAL(10,3)
DETERMINISTIC
BEGIN
    DECLARE hidrometro_usuario VARCHAR(50);
    DECLARE media_consumo DECIMAL(10,3);

    SELECT num_serie_hidrometro INTO hidrometro_usuario
    FROM Contrato
    WHERE cod_usuario = p_cod_usuario
    LIMIT 1;

    SELECT AVG(valor_medido) INTO media_consumo
    FROM Leitura
    WHERE num_serie_hidrometro = hidrometro_usuario;

    RETURN media_consumo;
END $$

DELIMITER ;

-- criar contrato

DELIMITER $$

CREATE PROCEDURE criar_contrato(
    IN p_cod_usuario VARCHAR(20),
    IN p_cod_imovel VARCHAR(20),
    IN p_num_serie_hidrometro VARCHAR(50),
    IN p_data_inicio DATETIME,
    IN p_status VARCHAR(50)
)
BEGIN
    DECLARE usuario_existente INT;
    DECLARE hidrometro_existente INT;

    SELECT COUNT(*) INTO usuario_existente
    FROM Usuario
    WHERE cod_usuario = p_cod_usuario;

    SELECT COUNT(*) INTO hidrometro_existente
    FROM Hidrometro
    WHERE num_serie_hidrometro = p_num_serie_hidrometro;

    IF usuario_existente = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Usuário não existe';
    ELSEIF hidrometro_existente = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Hidrometro não existe';
    ELSE
    
        INSERT INTO Contrato (cod_contrato,cod_usuario, cod_imovel, num_serie_hidrometro, data_inicio, status)
        VALUES (gerar_id('C',10),p_cod_usuario, p_cod_imovel, p_num_serie_hidrometro, p_data_inicio, p_status);
    END IF;
END $$

DELIMITER ;

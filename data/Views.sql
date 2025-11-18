USE MonitoramentoConsumoDeAgua;
CREATE USER 'user_cliente'@'localhost' identified by 'senha_user';
CREATE VIEW vw_leituras_por_usuario AS
SELECT
	u.nome,
    l.valor_medido,
    l.data_hora_leitura,
    l.num_serie_hidrometro
FROM Usuario u
JOIN Contrato c ON c.cod_usuario = u.cod_usuario
JOIN Leitura l ON l.num_serie_hidrometro = c.num_serie_hidrometro;

CREATE VIEW vw_dados_usuario AS
SELECT 
    u.cod_usuario,
    u.nome,
    u.email,
    u.data_cadastro,
    u.tipo_p,
    pf.cpf,
    pj.cnpj
FROM Usuario u
LEFT JOIN Pessoa_Fisica pf ON pf.cod_usuario = u.cod_usuario
LEFT JOIN Pessoa_Juridica pj ON pj.cod_usuario = u.cod_usuario;

CREATE VIEW vw_alertas_usuario AS
SELECT 
    u.nome,
    a.data_hora_alerta,
    a.tipo_alerta,
    l.cod_leitura,
    l.valor_medido,
    l.data_hora_leitura,
    l.num_serie_hidrometro
FROM Usuario u
JOIN Contrato c 
    ON c.cod_usuario = u.cod_usuario
JOIN Leitura l 
    ON l.num_serie_hidrometro = c.num_serie_hidrometro
JOIN Alerta a 
    ON a.cod_leitura = l.cod_leitura;
    
GRANT SELECT ON MonitoramentoConsumoDeAgua.vw_leituras_por_usuario TO 'user_cliente'@'localhost';
GRANT SELECT ON MonitoramentoConsumoDeAgua.vw_dados_usuario TO 'user_cliente'@'localhost';
GRANT SELECT ON MonitoramentoConsumoDeAgua.vw_alertas_usuario TO 'user_cliente'@'localhost';

CREATE USER 'user_tecnico'@'localhost' identified by 'senha_forte';
GRANT SELECT, INSERT, UPDATE, DELETE ON MonitoramentoConsumoDeAgua.* TO 'user_tecnico'@'localhost';
GRANT EXECUTE ON FUNCTION monitoramentoconsumodeagua.gerar_id TO 'user_tecnico'@'localhost';
GRANT EXECUTE ON FUNCTION monitoramentoconsumodeagua.consumo_medio_usuario TO 'user_tecnico'@'localhost';
GRANT EXECUTE ON PROCEDURE monitoramentoconsumodeagua.criar_contrato TO 'user_tecnico'@'localhost';
FLUSH PRIVILEGES;







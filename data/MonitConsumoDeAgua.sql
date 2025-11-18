CREATE DATABASE MonitoramentoConsumoDeAgua;

use MonitoramentoConsumoDeAgua;

create table Endereco (
	cep varchar(8) primary key,
    rua varchar(255),
    bairro varchar(255),
    cidade varchar(255),
    estado varchar(255)
);

create table Imovel (
	cod_imovel varchar(20) primary key,
    tipo_imovel varchar(100),
    cep_endereco varchar(8),
    constraint fk_imovel_endereco 
    foreign key (cep_endereco) references Endereco(cep)
    on update cascade
    on delete set null
);

create table Hidrometro (
	num_serie_hidrometro varchar(50) primary key,
    marca varchar(100),
    modelo varchar (100)
);

INSERT INTO hidrometro (num_serie_hidrometro, marca, modelo)
VALUES ('HIDRO-001', 'Marca Teste 2', 'Modelo-y');

INSERT INTO hidrometro (num_serie_hidrometro, marca, modelo)
VALUES ('HIDRO-002', 'Marca Teste 2', 'Modelo-Z');

INSERT INTO hidrometro (num_serie_hidrometro, marca, modelo)
VALUES ('HIDRO-003', 'Marca Teste 3', 'Modelo-A');

INSERT INTO hidrometro (num_serie_hidrometro, marca, modelo)
VALUES ('HIDRO-004', 'Marca Teste 4', 'Modelo-B');

INSERT INTO hidrometro (num_serie_hidrometro, marca, modelo)
VALUES ('HIDRO-005', 'Marca Teste 5', 'Modelo-C');

create table Usuario (
	cod_usuario varchar(20) primary key,
    nome varchar(255) not null,
    senha varchar (100) not null,
    email varchar(255) unique,
    data_cadastro datetime,
    tipo_p varchar(255)
);

create table Pessoa_Fisica (
	cod_usuario varchar(20) primary key,
    cpf char(11) unique not null,
    data_nasc date not null,
    constraint fk_pf_usuario 
    foreign key (cod_usuario) references Usuario(cod_usuario)
    on update cascade
    on delete cascade
);

create table Pessoa_Juridica (
	cod_usuario varchar(20) primary key,
    cnpj varchar(14) unique not null,
    razao_social varchar(200) not null,
    constraint fk_pj_usuario
    foreign key (cod_usuario) references Usuario(cod_usuario)
    on update cascade
    on delete cascade
);

create table Contrato (
	cod_contrato varchar(20) primary key,
    cod_usuario varchar(20) not null,
    cod_imovel varchar(20) not null,
    num_serie_hidrometro varchar(50) not null,
    data_inicio datetime,
    status varchar(50),
    constraint fk_contrato_usuario
    foreign key (cod_usuario) references Usuario(cod_usuario)
    on update cascade
    on delete restrict,
	constraint fk_contrato_imovel
    foreign key (cod_imovel) references Imovel(cod_imovel)
    on update cascade
    on delete restrict,
    constraint fk_contrato_hidrometro
    foreign key (num_serie_hidrometro) references Hidrometro(num_serie_hidrometro)
    on update cascade
    on delete restrict
);

create table Leitura (
	cod_leitura varchar(20) primary key,
    valor_medido decimal(10,3) not null,
    data_hora_leitura datetime not null,
    num_serie_hidrometro varchar(50) not null,
    constraint fk_leitura_hidrometro
    foreign key (num_serie_hidrometro) references Hidrometro(num_serie_hidrometro)
    on update cascade
    on delete restrict
);

create table Alerta (
	cod_alerta VARCHAR(20) primary key,
    data_hora_alerta datetime not null,
    tipo_alerta varchar(100) not null,
    cod_leitura varchar(20) not null,
    constraint fk_alerta_leitura
    foreign key(cod_leitura) references Leitura(cod_leitura)
    on update cascade
    on delete restrict
);

CREATE TABLE Grupo (
    cod_grupo INT PRIMARY KEY auto_increment,
    nome_grupo VARCHAR(100) NOT NULL UNIQUE,
    descricao TEXT
);
INSERT INTO Grupo (nome_grupo, descricao) -- preenchendo a tabela grupo pois ela sera estatica, somente para relacionar com usuarios por meio do Grupos_Usuarios
VALUES 
('Técnico', 'Usuários responsáveis pela instalação, manutenção e monitoramento dos sistemas de medição.'),
('Cliente', 'Usuários consumidores que consultam seu consumo e relatórios.');

CREATE TABLE Grupos_Usuarios (
    cod_usuario varchar(20) NOT NULL,
    cod_grupo INT NOT NULL,
    data_entrada DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cod_usuario, cod_grupo),
    CONSTRAINT fk_grupousu_usuario
        FOREIGN KEY (cod_usuario) REFERENCES Usuario(cod_usuario)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_grupousu_grupo
        FOREIGN KEY (cod_grupo) REFERENCES Grupo(cod_grupo)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- Fazendo o Insert do Primeiro Tecnico
SET @id := gerar_id('U', 10);

INSERT INTO Usuario (cod_usuario, nome, senha, email, tipo_p)
VALUES (@id, 'Tecnico Master', '$2a$12$PWzNPmbLBBFxLnlc55rm5ODfeKvZikIEeBrXH5FKc083mJLbF7nwu', 'tecnico@agua.com', 'FISICA');
INSERT INTO Pessoa_Fisica (cod_usuario, cpf, data_nasc)
VALUES (@id, '00000000000', '1990-01-01');

INSERT INTO Grupos_Usuarios (cod_usuario, cod_grupo)
VALUES (@id, 1);

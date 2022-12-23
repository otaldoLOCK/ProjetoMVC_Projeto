create database IF NOT EXISTS db_venda;
use db_venda;

CREATE TABLE IF NOT EXISTS tb_privilegio (
  id_privilegio int(11) NOT NULL AUTO_INCREMENT,
  nome varchar(300) DEFAULT NULL,
  PRIMARY KEY (id_privilegio)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

CREATE TABLE IF NOT EXISTS tb_login (
  id_login int(11) NOT NULL AUTO_INCREMENT,
  login varchar(300) not NULL,
  senha varchar(300) not NULL, 
  nome varchar(300) not NULL,
  id_privilegio int not NULL,
  foreign key FK_privilegio (id_privilegio) REFERENCES tb_privilegio (id_privilegio) ON DELETE RESTRICT,
  PRIMARY KEY (id_login)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

CREATE TABLE IF NOT EXISTS tb_cliente (
  id_cliente int(11) NOT NULL AUTO_INCREMENT,
  data date DEFAULT NULL,
  idade int default null,
  nome varchar(300) DEFAULT NULL,
  cpf_cnpj varchar(14) not null,
  identidade integer default null,
  inscricao_estadual integer default null,
  PRIMARY KEY (id_cliente)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

CREATE TABLE IF NOT EXISTS tb_produto (
  id_produto int(11) NOT NULL AUTO_INCREMENT,
  descricao varchar(300) not NULL,
  valor DECIMAL(12,2) not null,
  PRIMARY KEY (id_produto)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;

CREATE TABLE IF NOT EXISTS tb_venda (
  id_venda int(11) NOT NULL AUTO_INCREMENT,
  data_venda date DEFAULT NULL,
  id_cliente int not NULL, 
  foreign key FK_cliente (id_cliente) REFERENCES tb_cliente (id_cliente) ON DELETE RESTRICT,
  PRIMARY KEY (id_venda)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

ALTER TABLE tb_venda ADD DESCONTO DECIMAL(12,2);
ALTER TABLE tb_venda ADD VALOR_PAGO DECIMAL(12,2);
ALTER TABLE tb_venda ADD TOTAL_VENDA DECIMAL(12,2);
ALTER TABLE tb_produto modify valor DECIMAL(12,2);

CREATE TABLE IF NOT EXISTS tb_venda_item (
  id_venda_item int(11) NOT NULL AUTO_INCREMENT,
  quantidade integer not null,
  valor_unitario decimal not null,
  id_venda int not NULL, 
  id_produto int not NULL,
  foreign key FK_venda (id_venda) REFERENCES tb_venda (id_venda) ON DELETE RESTRICT,
  foreign key FK_produto (id_produto) REFERENCES tb_produto (id_produto) ON DELETE RESTRICT,
  PRIMARY KEY (id_venda_item)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

/*

select id_venda from tb_venda where data_venda = '2018-11-23' and id_cliente = 2 and DESCONTO=5.5 and VALOR_PAGO=50.0 and TOTAL_VENDA=42.5

select  * 
from tb_venda 
where TOTAL_VENDA=10.87

select * from tb_venda

select * from tb_venda_item where id_venda = 63

delete from tb_venda where id_venda > 19
*/

insert into tb_privilegio (nome) values ('Administrador');
insert into tb_privilegio (nome) values ('Operador');

insert into tb_login (login,senha,nome,id_privilegio) values ('christian','teste','Christian Adriano',(select id_privilegio from tb_privilegio where nome = 'Administrador'));
insert into tb_login (login,senha,nome,id_privilegio) values ('paulo','teste','Paulo da Silva',(select id_privilegio from tb_privilegio where nome = 'Operador'));
insert into tb_login (login,senha,nome,id_privilegio) values ('jackson','teste','Jackson',(select id_privilegio from tb_privilegio where nome = 'Administrador'));
insert into tb_login (login,senha,nome,id_privilegio) values ('yuri','teste','Yuri',(select id_privilegio from tb_privilegio where nome = 'Administrador'));
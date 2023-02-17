CREATE TABLE eticket.laudo (
	id int8 NOT NULL,
	ativo bool NULL,
	"data" varchar(255) NULL,
	descricao_laudo varchar(255) NULL,
	dispositivo varchar(255) NULL,
	responsavel varchar(255) NULL,
	CONSTRAINT laudo_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE eticket.seq_laudo_id
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;




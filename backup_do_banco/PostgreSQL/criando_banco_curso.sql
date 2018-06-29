CREATE TABLE public.endereco (
    id_endereco serial NOT NULL,
    id_estado integer NOT NULL,
    cep integer NOT NULL,
    ativo integer DEFAULT 1 NOT NULL,
    numero integer NOT NULL,
    logradouro character varying(150) NOT NULL,
    bairro character varying(45)
);

CREATE TABLE public.estado (
    id_estado integer NOT NULL,
    sigla character varying(2) NOT NULL,
    descricao character varying NOT NULL
);

CREATE TABLE public.tb_pessoa (
    id_pessoa bigserial NOT NULL,
    nome character varying(45) NOT NULL,
    cpf character varying(14) NOT NULL,
    sexo character(1) NOT NULL,
    data_nascimento date NOT NULL,
    ativo integer DEFAULT 1 NOT NULL,
    id_endereco integer
);

CREATE TABLE public.usuario (
    id_usuario serial NOT NULL,
    nome character varying(45) NOT NULL,
    login character varying(20) NOT NULL,
    senha character varying(32) NOT NULL,
    tipo integer DEFAULT 0 NOT NULL,
    ativo integer DEFAULT 0 NOT NULL
);

ALTER TABLE ONLY public.endereco
    ADD CONSTRAINT endereco_pkey PRIMARY KEY (id_endereco);

ALTER TABLE ONLY public.estado
    ADD CONSTRAINT estado_descricao_key UNIQUE (descricao);

ALTER TABLE ONLY public.estado
    ADD CONSTRAINT estado_pkey PRIMARY KEY (id_estado);

ALTER TABLE ONLY public.estado
    ADD CONSTRAINT estado_sigla_key UNIQUE (sigla);

ALTER TABLE ONLY public.tb_pessoa
    ADD CONSTRAINT tb_pessoa_cpf_key UNIQUE (cpf);

ALTER TABLE ONLY public.tb_pessoa
    ADD CONSTRAINT tb_pessoa_pkey PRIMARY KEY (id_pessoa);

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_login_key UNIQUE (login);

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);

ALTER TABLE ONLY public.tb_pessoa
    ADD CONSTRAINT fk_endereco FOREIGN KEY (id_endereco) REFERENCES public.endereco(id_endereco);

ALTER TABLE ONLY public.endereco
    ADD CONSTRAINT fk_estado FOREIGN KEY (id_estado) REFERENCES public.estado(id_estado);
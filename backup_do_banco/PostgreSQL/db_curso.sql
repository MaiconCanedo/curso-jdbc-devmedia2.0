PGDMP                         v            curso    9.6.3    9.6.3 %    y           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                       false            z           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                       false            {           1262    21206    curso    DATABASE     �   CREATE DATABASE curso WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Portuguese_Brazil.1252' LC_CTYPE = 'Portuguese_Brazil.1252';
    DROP DATABASE curso;
             postgres    false                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
             postgres    false            |           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                  postgres    false    3                        3079    12387    plpgsql 	   EXTENSION     ?   CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
    DROP EXTENSION plpgsql;
                  false            }           0    0    EXTENSION plpgsql    COMMENT     @   COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';
                       false    1            �            1259    21209    endereco    TABLE       CREATE TABLE endereco (
    id_endereco integer NOT NULL,
    id_estado integer NOT NULL,
    cep integer NOT NULL,
    ativo integer DEFAULT 1 NOT NULL,
    numero integer NOT NULL,
    logradouro character varying(150) NOT NULL,
    bairro character varying(45)
);
    DROP TABLE public.endereco;
       public         postgres    false    3            �            1259    21207    endereco_id_endereco_seq    SEQUENCE     z   CREATE SEQUENCE endereco_id_endereco_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.endereco_id_endereco_seq;
       public       postgres    false    186    3            ~           0    0    endereco_id_endereco_seq    SEQUENCE OWNED BY     G   ALTER SEQUENCE endereco_id_endereco_seq OWNED BY endereco.id_endereco;
            public       postgres    false    185            �            1259    21214    estado    TABLE     �   CREATE TABLE estado (
    id_estado integer NOT NULL,
    sigla character varying(2) NOT NULL,
    descricao character varying NOT NULL
);
    DROP TABLE public.estado;
       public         postgres    false    3            �            1259    21222 	   tb_pessoa    TABLE       CREATE TABLE tb_pessoa (
    id_pessoa bigint NOT NULL,
    nome character varying(45) NOT NULL,
    cpf character varying(14) NOT NULL,
    sexo character(1) NOT NULL,
    data_nascimento date NOT NULL,
    ativo integer DEFAULT 1 NOT NULL,
    id_endereco integer
);
    DROP TABLE public.tb_pessoa;
       public         postgres    false    3            �            1259    21220    tb_pessoa_id_pessoa_seq    SEQUENCE     y   CREATE SEQUENCE tb_pessoa_id_pessoa_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.tb_pessoa_id_pessoa_seq;
       public       postgres    false    3    189                       0    0    tb_pessoa_id_pessoa_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE tb_pessoa_id_pessoa_seq OWNED BY tb_pessoa.id_pessoa;
            public       postgres    false    188            �            1259    21229    usuario    TABLE       CREATE TABLE usuario (
    id_usuario integer NOT NULL,
    nome character varying(45) NOT NULL,
    login character varying(20) NOT NULL,
    senha character varying(32) NOT NULL,
    tipo integer DEFAULT 0 NOT NULL,
    ativo integer DEFAULT 0 NOT NULL
);
    DROP TABLE public.usuario;
       public         postgres    false    3            �            1259    21227    usuario_id_usuario_seq    SEQUENCE     x   CREATE SEQUENCE usuario_id_usuario_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.usuario_id_usuario_seq;
       public       postgres    false    191    3            �           0    0    usuario_id_usuario_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE usuario_id_usuario_seq OWNED BY usuario.id_usuario;
            public       postgres    false    190            �           2604    21212    endereco id_endereco    DEFAULT     n   ALTER TABLE ONLY endereco ALTER COLUMN id_endereco SET DEFAULT nextval('endereco_id_endereco_seq'::regclass);
 C   ALTER TABLE public.endereco ALTER COLUMN id_endereco DROP DEFAULT;
       public       postgres    false    186    185    186            �           2604    21225    tb_pessoa id_pessoa    DEFAULT     l   ALTER TABLE ONLY tb_pessoa ALTER COLUMN id_pessoa SET DEFAULT nextval('tb_pessoa_id_pessoa_seq'::regclass);
 B   ALTER TABLE public.tb_pessoa ALTER COLUMN id_pessoa DROP DEFAULT;
       public       postgres    false    189    188    189            �           2604    21232    usuario id_usuario    DEFAULT     j   ALTER TABLE ONLY usuario ALTER COLUMN id_usuario SET DEFAULT nextval('usuario_id_usuario_seq'::regclass);
 A   ALTER TABLE public.usuario ALTER COLUMN id_usuario DROP DEFAULT;
       public       postgres    false    190    191    191            q          0    21209    endereco 
   TABLE DATA                     public       postgres    false    186   �&       �           0    0    endereco_id_endereco_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('endereco_id_endereco_seq', 6, true);
            public       postgres    false    185            r          0    21214    estado 
   TABLE DATA                     public       postgres    false    187   �'       t          0    21222 	   tb_pessoa 
   TABLE DATA                     public       postgres    false    189   �)       �           0    0    tb_pessoa_id_pessoa_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('tb_pessoa_id_pessoa_seq', 6, true);
            public       postgres    false    188            v          0    21229    usuario 
   TABLE DATA                     public       postgres    false    191   �*       �           0    0    usuario_id_usuario_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('usuario_id_usuario_seq', 1, false);
            public       postgres    false    190            �           2606    21236    endereco endereco_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY endereco
    ADD CONSTRAINT endereco_pkey PRIMARY KEY (id_endereco);
 @   ALTER TABLE ONLY public.endereco DROP CONSTRAINT endereco_pkey;
       public         postgres    false    186    186            �           2606    21238    estado estado_descricao_key 
   CONSTRAINT     T   ALTER TABLE ONLY estado
    ADD CONSTRAINT estado_descricao_key UNIQUE (descricao);
 E   ALTER TABLE ONLY public.estado DROP CONSTRAINT estado_descricao_key;
       public         postgres    false    187    187            �           2606    21240    estado estado_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY estado
    ADD CONSTRAINT estado_pkey PRIMARY KEY (id_estado);
 <   ALTER TABLE ONLY public.estado DROP CONSTRAINT estado_pkey;
       public         postgres    false    187    187            �           2606    21242    estado estado_sigla_key 
   CONSTRAINT     L   ALTER TABLE ONLY estado
    ADD CONSTRAINT estado_sigla_key UNIQUE (sigla);
 A   ALTER TABLE ONLY public.estado DROP CONSTRAINT estado_sigla_key;
       public         postgres    false    187    187            �           2606    21244    tb_pessoa tb_pessoa_cpf_key 
   CONSTRAINT     N   ALTER TABLE ONLY tb_pessoa
    ADD CONSTRAINT tb_pessoa_cpf_key UNIQUE (cpf);
 E   ALTER TABLE ONLY public.tb_pessoa DROP CONSTRAINT tb_pessoa_cpf_key;
       public         postgres    false    189    189            �           2606    21246    tb_pessoa tb_pessoa_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY tb_pessoa
    ADD CONSTRAINT tb_pessoa_pkey PRIMARY KEY (id_pessoa);
 B   ALTER TABLE ONLY public.tb_pessoa DROP CONSTRAINT tb_pessoa_pkey;
       public         postgres    false    189    189            �           2606    21248    usuario usuario_login_key 
   CONSTRAINT     N   ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_login_key UNIQUE (login);
 C   ALTER TABLE ONLY public.usuario DROP CONSTRAINT usuario_login_key;
       public         postgres    false    191    191            �           2606    21250    usuario usuario_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);
 >   ALTER TABLE ONLY public.usuario DROP CONSTRAINT usuario_pkey;
       public         postgres    false    191    191            �           2606    21251    tb_pessoa fk_endereco    FK CONSTRAINT     v   ALTER TABLE ONLY tb_pessoa
    ADD CONSTRAINT fk_endereco FOREIGN KEY (id_endereco) REFERENCES endereco(id_endereco);
 ?   ALTER TABLE ONLY public.tb_pessoa DROP CONSTRAINT fk_endereco;
       public       postgres    false    2026    186    189            �           2606    21256    endereco fk_estado    FK CONSTRAINT     m   ALTER TABLE ONLY endereco
    ADD CONSTRAINT fk_estado FOREIGN KEY (id_estado) REFERENCES estado(id_estado);
 <   ALTER TABLE ONLY public.endereco DROP CONSTRAINT fk_estado;
       public       postgres    false    187    186    2030            q   8  x���]n�@�wO1oj� ��>i�F����N�M��x��g�	�Xg{���M�/���f��`�9l�*M�
�������j):{���H]�'����i�uu��Z��l}\�ax��q����N^_��Eؖ�t6�Xi�y(�W�F��p�2X>T�u*�DӠ�����֜ cKUG[�l��ǻ&��>?pO�b���0dX�T:לj2���ˢ>�H)�Q'K´�i�*���z3����`�oi�*Ib�����m#S��͹F�]$.~��R}V�J}��s��nW��~_��g��c�MNV� l0�C*�      r   �  x��սn�0���F+14�[� ����v���K�����mPg��A^�w�.��[K~rr����T)���Y���ڋ3[���Dk��Dm�*�
��x�,^2-Β�N�!�V����RcB�j`��ei��Ȩ�ö߳�+b���sm�M'�Mac�eݠ�fd��=o��=6�ml�ţ�M����!�ib�v����u��ޣ�?��{��y#I.�)�L
�6�'ou	�Q�G�7�m�$R�_�����7��j)�([��"Ǚ[��hd����1JFNO��������s��Q82V(Mp��U�QS2rA����(�D���a�xgl�qLɨ叚�gD_��1��G�4w��)!�]�9��;�r�:���;��ht�O��B��T�����sMH�5��S?:nxm��n��~�8��W�����g0�lB��      t     x����j�0 �{�B��`ۉC�Ned��vд�/q��إq�lo���t#��r�@�Cr�)�������᤻�)���~%`]�	T�#�N:���`UW�V[����Ѷ�g]���^�y	sN`�V�r��յ�a�1�p_��Y��g��\P���1*��'��V[L�D�<��%�����	`�qg��۱/���H*d�'�I��ri��4�uL�CbB9������b�«f������h[�N�J�n�ڋn ��_#]2�-qД#��.�~ ��      v   �   x����j!���sK۠��z
%��6�&�����BW���_��~>g�����r��x~��t�1$X���k�ip5|��k�\����oh?io����	ּ���!�iɦq�P��^�&�iٴ(�9I-�orO^�7�Uw3#��}�3K�TD���8���J4���qj��^0߶��=��fr1}��@���gV��_�GT�2O�Jh	za�N#i+��a�����߉�     
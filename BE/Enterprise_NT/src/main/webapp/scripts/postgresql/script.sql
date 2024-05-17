--Script para la creacion y actualizacion de la base de datos transaccional de PostgreSQL
CREATE OR REPLACE FUNCTION script() RETURNS integer AS $$
BEGIN
    -- Llaves de Configuracion
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab98')
    THEN
        CREATE TABLE lab98 (
            lab98c1   VARCHAR(64) NOT NULL,
            lab98c2   VARCHAR(256)
        );
        ALTER TABLE lab98 ADD CONSTRAINT lab98_pk PRIMARY KEY ( lab98c1 );
    END IF;
    -- Sesiones activas
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab100')
    THEN
        CREATE TABLE lab100 (
            lab100c1   VARCHAR NOT NULL,
            lab100c2   timestamp NOT NULL,
            lab100c3   VARCHAR(50) NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab05c1   INTEGER NOT NULL
        );
        ALTER TABLE lab100 ADD CONSTRAINT lab100_pk PRIMARY KEY ( lab100c1 );
    END IF;
    -- Listas
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab80')
    THEN
        CREATE TABLE lab80 (
            lab80c1   INTEGER NOT NULL,
            lab80c2   INTEGER,
            lab80c3   VARCHAR(8),
            lab80c4   VARCHAR(128) NOT NULL,
            lab80c5   VARCHAR(128) NOT NULL,
            lab80c6   VARCHAR(128)
        );
        ALTER TABLE lab80 ADD CONSTRAINT lab80_pk PRIMARY KEY ( lab80c1 );
    END IF;

    -- Usuario
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab04')
    THEN
        CREATE TABLE lab04 (
            lab04c1     SERIAL NOT NULL,
            lab04c2     VARCHAR(64) NOT NULL,
            lab04c3     VARCHAR(64) NOT NULL,
            lab04c4     VARCHAR(32) NOT NULL,
            lab04c5     VARCHAR(256) NOT NULL,
            lab07c1     smallint NOT NULL,
            lab04c6     TIMESTAMP NOT NULL,
            lab04c7     TIMESTAMP,
            lab04c8     TIMESTAMP NOT NULL,
            lab04c9     TIMESTAMP NOT NULL,
            lab04c1_1   INTEGER,
            lab04c10    VARCHAR(64),
            lab04c11    VARCHAR(128),
            lab04c12    BYTEA,
            lab04c13    VARCHAR(64),
            lab04c14    FLOAT(3) NOT NULL DEFAULT 0,
            lab04c15    smallint NOT NULL,
            lab04c16    BYTEA,
            lab04c17    smallint NOT NULL,
            lab04c18    smallint NOT NULL,
            lab04c19    smallint NOT NULL,
            lab04c20    smallint NOT NULL,
            lab04c21    smallint NOT NULL,
            lab04c22    smallint NOT NULL,
            lab04c23    smallint NOT NULL,
            lab04c24    smallint NOT NULL,
            lab103c1    INTEGER
        );

        ALTER TABLE lab04 ADD CONSTRAINT lab04_pk PRIMARY KEY ( lab04c1 );
    END IF;

    -- Tipos de Orden
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab103')
    THEN

        CREATE TABLE lab103 (
            lab103c1   SERIAL NOT NULL,
            lab103c2   VARCHAR(2),
            lab103c3   VARCHAR(62) NOT NULL,
            lab103c4   VARCHAR(8),
            lab103c5   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL
        );


        ALTER TABLE lab103 ADD CONSTRAINT lab103_pk PRIMARY KEY ( lab103c1 );
    END IF;

    -- Roles
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab82')
    THEN
        CREATE TABLE lab82 (
            lab82c1   SERIAL NOT NULL,
            lab82c2   VARCHAR(60) NOT NULL,
            lab82c3   smallint NOT NULL,
            lab82c4   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab82 ADD CONSTRAINT lab82_pk PRIMARY KEY ( lab82c1 );
    END IF;

    -- Unidad
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab45')
    THEN
        CREATE TABLE lab45 (
            lab45c1   SERIAL NOT NULL,
            lab45c2   VARCHAR(16) NOT NULL,
            lab45c3   VARCHAR(16),
            lab45c4   float,
            lab45c5   TIMESTAMP NOT NULL,
            lab07c1   smallint,
            lab04c1   INTEGER NOT NULL
        );
        ALTER TABLE lab45 ADD CONSTRAINT lab45_pk PRIMARY KEY ( lab45c1 );
    END IF;

    -- Area
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab43')
    THEN
        CREATE TABLE lab43 (
            lab43c1   SERIAL NOT NULL,
            lab43c2   smallint NOT NULL,
            lab43c3   VARCHAR(8) NOT NULL,
            lab43c4   VARCHAR(128) NOT NULL,
            lab43c5   VARCHAR(8) NOT NULL,
            lab43c6   smallint NOT NULL,
            lab43c7   TIMESTAMP NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL,
            lab43c8   smallint NOT NULL
        );

        ALTER TABLE lab43 ADD CONSTRAINT lab43_pk PRIMARY KEY ( lab43c1 );
    END IF;

    -- Recipientes
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab56')
    THEN
        CREATE TABLE lab56 (
        lab56c1   SERIAL NOT NULL,
        lab56c2   VARCHAR (60) NOT NULL,
        lab56c3   BYTEA,
        lab56c4   TIMESTAMP NOT NULL,
        lab04c1   INTEGER NOT NULL,
        lab56c5   smallint NOT NULL,
        lab07c1   smallint,
        lab45c1 INTEGER
        );

        ALTER TABLE lab56 ADD CONSTRAINT lab56_pk PRIMARY KEY ( lab56c1 );
    END IF;

    -- Tecnica
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab64')
    THEN
        CREATE TABLE lab64 (
            lab64c1   SERIAL NOT NULL,
            lab64c2   VARCHAR(16) NOT NULL,
            lab64c3   VARCHAR(64) NOT NULL,
            lab64c4   TIMESTAMP NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab64 ADD CONSTRAINT lab64_pk PRIMARY KEY ( lab64c1 );
    END IF;

    -- MAESTRO DE AUDITORIA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab34')
    THEN
        CREATE TABLE lab34 (
            lab34c1   SERIAL NOT NULL,
            lab34c2   TIMESTAMP NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab34c3   VARCHAR(16) NOT NULL,
            lab34c4   VARCHAR(128) NOT NULL,
            lab34c5   VARCHAR(512) NOT NULL,
            lab34c6   VARCHAR(64) NOT NULL,
            lab34c7   VARCHAR(128) NOT NULL,
            lab34c8   CHAR(1) NOT NULL
        );

        ALTER TABLE lab34 ADD CONSTRAINT lab34_pk PRIMARY KEY ( lab34c1 );
    END IF;

    -- DETALLE DE AUDITORIA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab36')
    THEN
        CREATE TABLE lab36 (
            lab36c1   SERIAL NOT NULL,
            lab34c1   INTEGER NOT NULL,
            lab36c2   VARCHAR(512) NOT NULL,
            lab36c3   TEXT,
            lab36c4   TEXT,
            lab36c5   TEXT
        );

        ALTER TABLE lab36 ADD CONSTRAINT lab36_pk PRIMARY KEY ( lab36c1 );
    END IF;

    -- GESTION DE ERRORES
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab151')
    THEN
        CREATE TABLE lab151 (
            lab151c1   SERIAL NOT NULL,
            lab151c2   timestamp NOT NULL,
            lab151c3   INTEGER NOT NULL,
            lab151c4   VARCHAR(64) NOT NULL,
            lab151c5   VARCHAR(64) NOT NULL,
            lab151c6   TEXT NOT NULL,
            lab151c7   TEXT NOT NULL,
            lab04c1    INTEGER,
            lab04c5    VARCHAR(32),
            lab151c8   INTEGER NOT NULL
        );

        ALTER TABLE lab151 ADD CONSTRAINT lab151_pk PRIMARY KEY ( lab151c1 );
    END IF;

    -- DEMOGRAFICO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab62')
    THEN
        CREATE TABLE lab62 (
            lab62c1    SERIAL NOT NULL,
            lab62c2    VARCHAR(64) NOT NULL,
            lab62c3    CHAR(1) NOT NULL,
            lab62c4    smallint NOT NULL,
            lab62c5    smallint NOT NULL,
            lab62c6    smallint NOT NULL,
            lab62c7    VARCHAR(32),
            lab62c8    VARCHAR(64),
            lab62c9    smallint NOT NULL,
            lab62c10   smallint NOT NULL,
            lab62c11   smallint NOT NULL,
            lab62c12   TIMESTAMP NOT NULL,
            lab62c13   smallint NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL,
            lab63c1    INTEGER
        );

        ALTER TABLE lab62 ADD CONSTRAINT lab62_pk PRIMARY KEY ( lab62c1 );
    END IF;

    -- Muestra
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab24')
    THEN
        CREATE TABLE lab24 (
            lab24c1    SERIAL NOT NULL,
            lab24c2    VARCHAR (64) NOT NULL,
            lab24c3    smallint NOT NULL,
            lab24c4    smallint NOT NULL,
            lab24c5    smallint NOT NULL,
            lab24c6    VARCHAR (1000),
            lab24c7    smallint NOT NULL,
            lab24c8    TIMESTAMP NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL,
            lab56c1    INTEGER NOT NULL,
            lab24c9    VARCHAR (3) NOT NULL,
            lab24c10   VARCHAR (32) NOT NULL,
            lab24c11   smallint NOT NULL,
            lab24c12   BIGINT,
            lab24c13   INTEGER
        );

        ALTER TABLE lab24 ADD CONSTRAINT lab24_pk PRIMARY KEY ( lab24c1 );
    END IF;

    -- Requisito
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab41')
    THEN
        CREATE TABLE lab41 (
            lab41c1   SERIAL NOT NULL,
            lab41c2   VARCHAR(64) NOT NULL,
            lab41c3   VARCHAR(4000) NOT NULL,
            lab07c1   smallint NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab41c4   timestamp NOT NULL
        );

        ALTER TABLE lab41 ADD CONSTRAINT lab41_pk PRIMARY KEY ( lab41c1 );
    END IF;

    -- SEDES
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab05')
    THEN
        CREATE TABLE lab05 (
            lab05c1   SERIAL NOT NULL,
            lab05c2   VARCHAR(64),
            lab05c4   VARCHAR(64) NOT NULL,
            lab05c5   VARCHAR(128),
            lab05c6   VARCHAR(32),
            lab05c7   INTEGER,
            lab05c8   INTEGER,
            lab05c9   timestamp NOT NULL,
            lab05c10   VARCHAR(8) NOT NULL,
            lab05c11   VARCHAR(64),
            lab05c12   VARCHAR(128),
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab05 ADD CONSTRAINT lab05_pk PRIMARY KEY ( lab05c1 );
    END IF;

    -- DemograficoItem
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab63')
    THEN
        CREATE TABLE lab63 (
            lab63c1    SERIAL NOT NULL,
            lab63c2    VARCHAR(64) NOT NULL,
            lab63c3    VARCHAR(128) NOT NULL,
            lab07c1    smallint NOT NULL,
            lab63c5    VARCHAR(256),
            lab63c6    timestamp NOT NULL,
            lab62c1    INTEGER NOT NULL,
            lab04c1    INTEGER NOT NULL
        );

        ALTER TABLE lab63 ADD CONSTRAINT lab63_pk PRIMARY KEY ( lab63c1 );
    END IF;

    -- RolesByUser
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab84')
    THEN
        CREATE TABLE lab84 (
            lab84c1   smallint NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab82c1   INTEGER NOT NULL
        );

    END IF;

    -- AreasByUser
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab69')
    THEN
        CREATE TABLE lab69 (
            lab69c1   smallint NOT NULL,
            lab69c2   smallint NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab43c1   INTEGER NOT NULL
        );

    END IF;

    -- BranchesByUser
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab93')
    THEN
        CREATE TABLE lab93 (
            lab93c1   smallint NOT NULL,
            lab93c2   smallint NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab05c1   INTEGER NOT NULL
        );

    END IF;

    -- Races
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab08')
    THEN
        CREATE TABLE lab08 (
            lab08c1   SERIAL NOT NULL,
            lab08c2   VARCHAR(60) NOT NULL,
            lab08c3   timestamp NOT NULL,
            lab08c4   FLOAT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint,
            lab08c5   VARCHAR(128) NOT NULL
        );

        ALTER TABLE lab08 ADD CONSTRAINT lab08_pk PRIMARY KEY ( lab08c1 );
    END IF;
    -- Specialist
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab09')
    THEN
        CREATE TABLE lab09 (
            lab09c1   SERIAL NOT NULL,
            lab09c2   VARCHAR(60) NOT NULL,
            lab09c3   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab09 ADD CONSTRAINT lab09_pk PRIMARY KEY ( lab09c1 );

    END IF;
    -- Service
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab10')
    THEN
        CREATE TABLE lab10 (
            lab10c1   SERIAL NOT NULL,
            lab10c2   VARCHAR(60) NOT NULL,
            lab10c3   INTEGER NULL,
            lab10c4   INTEGER NULL,
            lab10c5   smallint NOT NULL,
            lab10c6   timestamp NOT NULL,
            lab10c7   VARCHAR(8) NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab10 ADD CONSTRAINT lab10_pk PRIMARY KEY ( lab10c1 );
    END IF;

    -- Comentario
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab68')
    THEN
        CREATE TABLE lab68 (
            lab68c1   SERIAL NOT NULL,
            lab68c2   VARCHAR(32) NOT NULL,
            lab68c3   TEXT NOT NULL,
            lab68c4   timestamp NOT NULL,
            lab07c1   smallint NOT NULL,
            lab68c5   smallint NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab68c6   smallint NOT NULL
        );

        ALTER TABLE lab68 ADD CONSTRAINT lab68_pk PRIMARY KEY ( lab68c1 );
    END IF;

    -- Laboratorio
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab40')
    THEN
         CREATE TABLE lab40 (
            lab40c1   SERIAL NOT NULL,
            lab40c2   INTEGER NOT NULL,
            lab40c3   VARCHAR(64) NOT NULL,
            lab40c4   VARCHAR(128),
            lab40c5   VARCHAR(64),
            lab40c6   VARCHAR(64),
            lab40c7   smallint NOT NULL,
            lab40c8   VARCHAR(256) NOT NULL,
            lab40c9   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
            );
        ALTER TABLE lab40 ADD CONSTRAINT lab40_pk PRIMARY KEY ( lab40c1 );
    END IF;

    -- RESULTADO LITERAL
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab50')
    THEN
        CREATE TABLE lab50 (
            lab50c1   SERIAL NOT NULL,
            lab50c2   VARCHAR(64) NOT NULL,
            lab50c3   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab50 ADD CONSTRAINT lab50_pk PRIMARY KEY ( lab50c1 );
    END IF;
    -- GRUPOS POBLACIONALES
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab67')
    THEN
        CREATE TABLE lab67 (
            lab67c1   SERIAL NOT NULL,
            lab67c2   VARCHAR(64) NOT NULL,
            lab67c3   smallint,
            lab67c4   smallint NOT NULL,
            lab67c5   INTEGER NOT NULL,
            lab67c6   INTEGER NOT NULL,
            lab67c7   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab67 ADD CONSTRAINT lab67_pk PRIMARY KEY ( lab67c1 );
    END IF;

    -- MEDICOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab19')
    THEN
        CREATE TABLE lab19 (
            lab19c1    SERIAL NOT NULL,
            lab19c2    VARCHAR(64) NOT NULL,
            lab19c3    VARCHAR(64),
            lab19c4    VARCHAR(32),
            lab19c5    VARCHAR(32),
            lab19c6    VARCHAR(128),
            lab19c7    VARCHAR(128),
            lab19c8    VARCHAR(32),
            lab19c9    VARCHAR(16),
            lab19c10   VARCHAR(256),
            lab19c11   VARCHAR(128),
            lab19c12   VARCHAR(128),
            lab19c13   VARCHAR(128),
            lab19c14   VARCHAR(2),
            lab19c15   VARCHAR(16),
            lab19c16   smallint NOT NULL,
            lab19c17   VARCHAR(256),
            lab19c18   VARCHAR(16),
            lab19c19   VARCHAR(64) NOT NULL,
            lab19c20   timestamp NOT NULL,
            lab19c21   VARCHAR(256),
            lab09c1    INTEGER,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL,
            lab19c22   VARCHAR(128) NOT NULL,
            lab19c23   VARCHAR(128) NULL
        );


        ALTER TABLE lab19 ADD CONSTRAINT lab19_pk PRIMARY KEY ( lab19c1 );
    END IF;

    -- MODULOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab85')
    THEN
         CREATE TABLE lab85 (
            lab85c1   INTEGER NOT NULL,
            lab85c2   INTEGER,
            lab85c3   VARCHAR(128) NOT NULL
         );

        ALTER TABLE lab85 ADD CONSTRAINT lab85_pk PRIMARY KEY ( lab85c1 );
    END IF;

    -- RELACION DE MODULOS/ROL
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab86')
    THEN
        CREATE TABLE lab86 (
            lab85c1   INTEGER NOT NULL,
            lab82c1   INTEGER NOT NULL
        );
    END IF;

    -- CLIENTES
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab14')
    THEN
        CREATE TABLE lab14 (
            lab14c1    SERIAL NOT NULL,
            lab14c2    VARCHAR(64) NOT NULL,
            lab14c3    VARCHAR(64) NOT NULL,
            lab14c4    VARCHAR(20),
            lab14c5    VARCHAR(20),
            lab14c6    VARCHAR(64),
            lab14c7    FLOAT(10),
            lab14c8    FLOAT(10),
            lab14c9    FLOAT(10),
            lab14c10   FLOAT(3),
            lab14c11   VARCHAR(256),
            lab14c12   smallint,
            lab14c13   VARCHAR(64),
            lab14c14   VARCHAR(128),
            lab14c15   VARCHAR(128),
            lab14c16   VARCHAR(16),
            lab14c17   VARCHAR(64),
            lab14c18   smallint NOT NULL,
            lab14c19   smallint NOT NULL,
            lab14c20   smallint NOT NULL,
            lab14c21   VARCHAR(128),
            lab14c22   smallint NOT NULL,
            lab14c23   smallint NOT NULL,
            lab14c24   VARCHAR(128),
            lab14c25   VARCHAR(256),
            lab14c26   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL,
            lab14c28   VARCHAR(64),
            lab14c29   VARCHAR(64),
            lab14c30   VARCHAR(64) NOT NULL,
            lab14c31   smallint NOT NULL
        );

        ALTER TABLE lab14 ADD CONSTRAINT lab14_pk PRIMARY KEY ( lab14c1 );
    END IF;

    -- MOTIVOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab30')
    THEN
        CREATE TABLE lab30 (
            lab30c1   SERIAL NOT NULL,
            lab30c2   VARCHAR(64) NOT NULL,
            lab30c3   VARCHAR(256) NOT NULL,
            lab30c4   smallint NOT NULL,
            lab30c5   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab30 ADD CONSTRAINT lab30_pk PRIMARY KEY ( lab30c1 );
    END IF;

    -- SISTEMA CENTRAL
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab118')
    THEN
        CREATE TABLE lab118 (
            lab118c1   SERIAL NOT NULL,
            lab118c2   VARCHAR(64) NOT NULL,
            lab118c3   smallint NOT NULL,
            lab118c4   timestamp NOT NULL,
            lab118c5   smallint NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL
        );

        ALTER TABLE lab118 ADD CONSTRAINT lab118_pk PRIMARY KEY ( lab118c1 );
    END IF;
    -- ALARMAS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab73')
    THEN
        CREATE TABLE lab73 (
            lab73c1   SERIAL NOT NULL,
            lab73c2   VARCHAR(62) NOT NULL,
            lab73c3   VARCHAR NOT NULL,
            lab73c4   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab73 ADD CONSTRAINT lab73_pk PRIMARY KEY ( lab73c1 );

    END IF;
    -- ANTIBIÓTICO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab79')
    THEN
        CREATE TABLE lab79 (
            lab79c1   SERIAL NOT NULL,
            lab79c2   VARCHAR(62) NOT NULL,
            lab79c3   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab79 ADD CONSTRAINT lab79_pk PRIMARY KEY ( lab79c1 );
    END IF;
    -- TARJETA DE CREDITO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab59')
    THEN
        CREATE TABLE lab59 (
            lab59c1   SERIAL NOT NULL,
            lab59c2   VARCHAR(64) NOT NULL,
            lab59c3   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab59 ADD CONSTRAINT lab59_pk PRIMARY KEY ( lab59c1 );
    END IF;
    -- BANCO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab110')
    THEN
        CREATE TABLE lab110 (
            lab110c1   SERIAL NOT NULL,
            lab110c2   VARCHAR(64) NOT NULL,
            lab110c3   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL
        );

        ALTER TABLE lab110 ADD CONSTRAINT lab110_pk PRIMARY KEY ( lab110c1 );
    END IF;
    -- Nevera
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab31')
    THEN
        CREATE TABLE lab31 (
            lab31c1   SERIAL NOT NULL,
            lab31c2   VARCHAR(62) NOT NULL,
            lab31c3   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL,
            lab05c1   INTEGER NOT NULL
        );

        ALTER TABLE lab31 ADD CONSTRAINT lab31_pk PRIMARY KEY ( lab31c1 );
    END IF;
    -- SITIO ANATOMICO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab158')
    THEN
        CREATE TABLE lab158 (
            lab158c1   SERIAL NOT NULL,
            lab158c2   VARCHAR(64) NOT NULL,
            lab158c3   VARCHAR(16) NOT NULL,
            lab158c4   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL
        );

        ALTER TABLE lab158 ADD CONSTRAINT lab158_pk PRIMARY KEY ( lab158c1 );
    END IF;
    -- TAREAS DE MICROBIOLOGIA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab169')
    THEN
        CREATE TABLE lab169 (
            lab169c1   SERIAL NOT NULL,
            lab169c2   VARCHAR(64) NOT NULL,
            lab169c3   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL
        );

        ALTER TABLE lab169 ADD CONSTRAINT lab169_pk PRIMARY KEY ( lab169c1 );
    END IF;
    -- TARIFA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab904')
    THEN
        CREATE TABLE lab904
        (
            lab904c1 SERIAL NOT NULL ,
            lab904c2 VARCHAR (16) NOT NULL ,
            lab904c3 VARCHAR (64) NOT NULL ,
            lab904c4 VARCHAR (128) ,
            lab904c5 VARCHAR (128) ,
            lab904c6 VARCHAR (64) ,
            lab904c7 VARCHAR (64) ,
            lab904c8 VARCHAR (20) ,
            lab904c9 VARCHAR (128) ,
            lab904c10 VARCHAR (64) ,
            lab904c11 VARCHAR (128) ,
            lab904c12 SMALLINT NOT NULL ,
            lab904c13 SMALLINT NOT NULL ,
            lab904c15 INTEGER ,
            lab904c16 SMALLINT NOT NULL ,
            lab904c17 SMALLINT NOT NULL ,
            lab904c18 SMALLINT NOT NULL ,
            lab904c19 SMALLINT NOT NULL ,
            lab904c20 SMALLINT NOT NULL ,
            lab904c21 SMALLINT NOT NULL ,
            lab904c22 VARCHAR (64) ,
            lab904c23 SMALLINT NOT NULL ,
            lab904c24 VARCHAR (64) ,
            lab904c25 VARCHAR (64) ,
            lab904c26 VARCHAR (64) ,
            lab904c27 VARCHAR (64) ,
            lab904c28 VARCHAR (64) ,
            lab904c29 VARCHAR (64) ,
            lab905c1 INTEGER ,
            lab904c30 VARCHAR (64) ,
            lab904c31 VARCHAR (64) ,
            lab904c32 INTEGER ,
            lab904c33 INTEGER ,
            lab904c34 SMALLINT NOT NULL ,
            lab904c35 SMALLINT NOT NULL ,
            lab904c36 SMALLINT NOT NULL ,
            lab904c37 timestamp NOT NULL ,
            lab04c1 INTEGER NOT NULL ,
            lab07c1 SMALLINT NOT NULL ,
            lab904c38 VARCHAR(512)
        );

    END IF;

    -- MICROORGANISMOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab76')
    THEN
        CREATE TABLE lab76 (
            lab76c1   SERIAL NOT NULL,
            lab76c2   VARCHAR(62) NOT NULL,
            lab76c3   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab76 ADD CONSTRAINT lab76_pk PRIMARY KEY ( lab76c1 );
    END IF;
    -- PRUEBAS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab39')
    THEN
        CREATE TABLE lab39 (
            lab39c1    SERIAL NOT NULL,
            lab43c1    INTEGER,
            lab39c2    VARCHAR(16) NOT NULL,
            lab39c3    VARCHAR(16) NOT NULL,
            lab39c4    VARCHAR(64) NOT NULL,
            lab39c5    INTEGER,
            lab24c1    INTEGER,
            lab44c1    INTEGER,
            lab39c6    INTEGER,
            lab39c7    INTEGER,
            lab39c8    INTEGER,
            lab39c9    smallint,
            lab39c10   VARCHAR(256),
            lab39c11   smallint,
            lab39c12   smallint,
            lab64c1    INTEGER,
            lab45c1    INTEGER,
            lab39c13   VARCHAR(16),
            lab39c14   VARCHAR(128),
            lab39c15   smallint,
            lab39c16   smallint NOT NULL,
            lab39c17   VARCHAR(8),
            lab39c18   INTEGER NOT NULL,
            lab39c19   smallint NOT NULL,
            lab39c20   smallint NOT NULL,
            lab39c21   VARCHAR(64),
            lab39c22   INTEGER,
            lab39c23   smallint NOT NULL,
            lab39c24   smallint NOT NULL,
            lab39c25   smallint NOT NULL,
            lab39c26   smallint NOT NULL,
            lab39c27   smallint NOT NULL,
            lab39c28   smallint NOT NULL,
            lab39c29   smallint NOT NULL,
            lab39c30   smallint NOT NULL,
            lab39c31   smallint NOT NULL,
            lab39c32   VARCHAR(64),
            lab39c33   VARCHAR,
            lab39c34   VARCHAR,
            lab39c35   VARCHAR,
            lab39c36   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL,
            lab39c37   smallint NOT NULL,
            lab39c38   smallint NOT NULL,
            lab39c39   smallint NOT NULL,
            lab39c40   smallint NOT NULL,
            lab39c41   INTEGER,
            lab39c42   INTEGER,
            lab39c43   FLOAT,
            lab39c44   INTEGER,
            lab39c45   FLOAT,
            lab39c46   FLOAT,
            lab39c47   smallint,
            lab39c48   BIGINT,
            lab39c49   FLOAT
        );

        ALTER TABLE lab39 ADD CONSTRAINT lab39_pk PRIMARY KEY ( lab39c1 );
    END IF;
    -- REQUISIOS POR PRUEBA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab71')
    THEN
        CREATE TABLE lab71 (
            lab41c1   INTEGER NOT NULL,
            lab39c1   INTEGER NOT NULL
        );

        ALTER TABLE lab71 ADD CONSTRAINT lab71_pk PRIMARY KEY ( lab41c1,lab39c1 );
    END IF;
    -- CONCURRENCIAS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab46')
    THEN
        CREATE TABLE lab46 (
            lab46c1   INTEGER NOT NULL,
            lab39c1   INTEGER NOT NULL,
            lab46c2   smallint NOT NULL
        );
    END IF;
    -- Diagnostico
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab20')
    THEN
        CREATE TABLE lab20 (
            lab20c1   SERIAL NOT NULL,
            lab20c2   VARCHAR(8) NOT NULL,
            lab20c3   VARCHAR(256) NOT NULL,
            lab20c4   timestamp NOT NULL,
            lab20c5   smallint NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL
        );

        ALTER TABLE lab20 ADD CONSTRAINT lab20_pk PRIMARY KEY ( lab20c1 );
    END IF;
    -- ANTIBIOGRAMA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab77')
    THEN
        CREATE TABLE lab77 (
            lab77c1   SERIAL NOT NULL,
            lab77c2   VARCHAR(12) NOT NULL,
            lab77c3   VARCHAR(64),
            lab77c4   VARCHAR(64),
            lab77c5   timestamp NOT NULL,
            lab77c7   smallint NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );
        ALTER TABLE lab77 ADD CONSTRAINT lab77_pk PRIMARY KEY ( lab77c1 );
    END IF;
    -- ANTIBIOGRAMA-ANTIBIOTICO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab78')
    THEN
        CREATE TABLE lab78 (
            lab77c1   INTEGER NOT NULL,
            lab79c1   INTEGER NOT NULL,
            lab45c1   INTEGER,
            lab78c2   INTEGER
        );

        ALTER TABLE lab78 ADD CONSTRAINT lab77v1_pk PRIMARY KEY ( lab77c1,lab79c1 );
    END IF;
    -- PACIENTE
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab21')
    THEN
        CREATE TABLE lab21 (
            lab21c1    SERIAL NOT NULL,
            lab21c2    VARCHAR(64) NOT NULL,
            lab21c3    VARCHAR(128) NOT NULL,
            lab21c4    VARCHAR(128),
            lab21c5    VARCHAR(128) NOT NULL,
            lab21c6    VARCHAR(128),
            lab80c1    INTEGER,
            lab21c7    timestamp,
            lab21c8    VARCHAR(64),
            lab21c9    float,
            lab21c10   float,
            lab21c11   timestamp,
            lab21c12   timestamp NOT NULL,
            lab21c13   VARCHAR(4096),
            lab21c14   VARCHAR,
            lab04c1    INTEGER NOT NULL,
            lab08c1    INTEGER,
            lab54c1    INTEGER,
            lab21c15   smallint,
            lab21c16   VARCHAR(32),
            lab21c17   VARCHAR(128),
            lab21c18   VARCHAR(32),
            lab21c19   VARCHAR(256)
        );

        ALTER TABLE lab21 ADD CONSTRAINT lab21_pk PRIMARY KEY ( lab21c1 );
    END IF;
    --ORDEN
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab22')
    THEN
        CREATE TABLE lab22 (
            lab22c1    BIGINT NOT NULL,
            lab22c2    INTEGER NOT NULL,
            lab103c1   INTEGER NOT NULL,
            lab22c3    timestamp NOT NULL,
            lab21c1    INTEGER NOT NULL,
            lab22c4    smallint,
            lab22c5    INTEGER,
            lab22c6    timestamp,
            lab04c1    INTEGER NOT NULL,
            lab07c1    INTEGER NOT NULL,
            lab22c7    VARCHAR(256),
            lab05c1    INTEGER,
            lab10c1    INTEGER,
            lab19c1    INTEGER,
            lab14c1    INTEGER,
            lab904c1   INTEGER,
            lab22c8    INTEGER NOT NULL,
            lab22c9    smallint,
            lab22c10   smallint,
            lab22c11   BIGINT,
            lab57c9    smallint,
            lab22c12   INTEGER
        );
        CREATE INDEX lab22c2__IDX ON lab22 (lab22c2 ASC);
        ALTER TABLE lab22 ADD CONSTRAINT lab22_pk PRIMARY KEY ( lab22c1 );
    END IF;
    --COMENTARIOS DE LA ORDEN
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab60')
    THEN
        CREATE TABLE lab60 (
            lab60c1   SERIAL NOT NULL,
            lab60c2   BIGINT NOT NULL,
            lab60c3   VARCHAR NOT NULL,
            lab60c4   smallint NOT NULL,
            lab60c5   timestamp NOT NULL,
            lab60c6   smallint NOT NULL,
            lab04c1   INTEGER NOT NULL
        );
        ALTER TABLE lab60 ADD CONSTRAINT lab60_pk PRIMARY KEY ( lab60c1 );
    END IF;

    --RESULTADOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab57')
    THEN
        CREATE TABLE lab57 (
            lab22c1     BIGINT NOT NULL,
            lab39c1     INTEGER NOT NULL,
            lab57c1     VARCHAR(16),
            lab57c2     TIMESTAMP,
            lab57c3     INTEGER,
            lab57c4     TIMESTAMP NOT NULL,
            lab57c5     INTEGER,
            lab57c6     VARCHAR(16),
            lab57c7     TIMESTAMP,
            lab57c8     SMALLINT,
            lab57c9     SMALLINT,
            lab57c10    SMALLINT,
            lab57c11    TIMESTAMP,
            lab57c12    INTEGER,
            lab57c14    INTEGER,
            lab57c15    INTEGER,
            lab57c16    SMALLINT,
            lab57c17    INTEGER,
            lab57c18    TIMESTAMP,
            lab57c19    INTEGER,
            lab57c20    TIMESTAMP,
            lab57c21    INTEGER,
            lab57c22    TIMESTAMP,
            lab57c23    INTEGER,
            lab45c2     VARCHAR(64),
            lab64c1     INTEGER,
            lab48c1     INTEGER,
            lab57c24    SMALLINT,
            lab57c25    SMALLINT,
            lab57c26    INTEGER,
            lab57c27    FLOAT,
            lab57c28    FLOAT,
            lab40c1     INTEGER not null,
            lab57c29    SMALLINT,
            lab05c1     INTEGER,
            lab48c5     FLOAT,
            lab48c6     FLOAT,
            lab48c12    FLOAT,
            lab48c13    FLOAT,
            lab50c1_1   INTEGER,
            lab50c1_3   INTEGER,
            lab48c14    FLOAT,
            lab48c15    FLOAT,
            lab57c30    VARCHAR(16),
            lab57c31    TIMESTAMP,
            lab57c32    SMALLINT,
            lab57c33    INTEGER,
            lab57c34    INTEGER,
            lab57c35    SMALLINT,
            lab57c36    SMALLINT,
            lab57c37    TIMESTAMP,
            lab57c38    INTEGER,
            lab57c39    TIMESTAMP,
            lab57c40    INTEGER,
            lab24c1     INTEGER,
            lab24c1_1   INTEGER,
            lab158c1    INTEGER,
            lab201c1    INTEGER,
            lab57c41    INTEGER,
            lab57c42    SMALLINT
        );

        ALTER TABLE lab57 ADD CONSTRAINT lab57_pk PRIMARY KEY ( lab22c1, lab39c1 );
    END IF;

    --COMETARIOS DEL RESULTADO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab95')
    THEN
        CREATE TABLE lab95 (
            lab22c1   BIGINT NOT NULL,
            lab39c1   INTEGER NOT NULL,
            lab95c1   TEXT,
            lab95c2   timestamp NOT NULL,
            lab95c3   smallint NOT NULL,
            lab95c4   smallint NOT NULL,
            lab95c5   TEXT,
            lab04c1   INTEGER NOT NULL
        );

        ALTER TABLE lab95 ADD CONSTRAINT lab95_pk PRIMARY KEY ( lab22c1, lab39c1 );
    END IF;
    --RECEPTOR
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab905')
    THEN
        CREATE TABLE lab905 (
            lab905c1   SERIAL NOT NULL,
            lab905c2   VARCHAR(64) NOT NULL,
            lab905c3   VARCHAR(64) NOT NULL,
            lab905c4   VARCHAR(64),
            lab905c5   VARCHAR(64) NOT NULL,
            lab905c6   DATE NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    INTEGER NOT NULL
        );

        ALTER TABLE lab905 ADD CONSTRAINT lab905_pk PRIMARY KEY ( lab905c1 );
    END IF;
    --EXAMEN POR LABORATORIO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab145')
    THEN
        CREATE TABLE lab145 (
            lab39c1    INTEGER NOT NULL,
            lab05c1    INTEGER NOT NULL,
            lab145c1   INTEGER NOT NULL,
            lab40c1    INTEGER NOT NULL
        );

        ALTER TABLE lab145 ADD CONSTRAINT lab145_pk PRIMARY KEY ( lab39c1,lab05c1,lab145c1,lab40c1 );
    END IF;
    --RESULTADOS POR EXAMEN
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab49')
    THEN
        CREATE TABLE lab49 (
            lab50c1   INTEGER NOT NULL,
            lab39c1   INTEGER NOT NULL
        );

        ALTER TABLE lab49 ADD CONSTRAINT lab49_pk PRIMARY KEY ( lab50c1,lab39c1 );
    END IF;

    --VALORES DE REFERENCIA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab48')
    THEN
        CREATE TABLE lab48 (
            lab48c1    SERIAL NOT NULL,
            lab39c1    INTEGER NOT NULL,
            lab48c2    smallint,
            lab48c3    INTEGER,
            lab48c4    INTEGER,
            lab48c5    FLOAT,
            lab48c6    FLOAT,
            lab48c9    VARCHAR,
            lab08c1    INTEGER,
            lab48c10   INTEGER,
            lab04c1    INTEGER NOT NULL,
            lab48c11   timestamp NOT NULL,
            lab48c12    FLOAT,
            lab48c13    FLOAT,
            lab50c1_1   INTEGER,
            lab50c1_3   INTEGER,
            lab48c14    FLOAT,
            lab48c15    FLOAT,
            lab48c16    smallint NOT NULL
        );

        ALTER TABLE lab48 ADD CONSTRAINT lab48_pk PRIMARY KEY ( lab48c1 );
    END IF;
    --HOMOLOGACION PRUEBAS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab61')
    THEN
        CREATE TABLE lab61 (
            lab118c1   INTEGER NOT NULL,
            lab39c1    INTEGER NOT NULL,
            lab61c1    VARCHAR(16) NOT NULL
        );
        ALTER TABLE lab61 ADD CONSTRAINT lab61_pk PRIMARY KEY ( lab118c1,lab39c1,lab61c1 );
    END IF;
    --DESTINOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab53')
    THEN
        CREATE TABLE lab53 (
            lab53c1   SERIAL NOT NULL,
            lab53c2   VARCHAR(16) NOT NULL,
            lab53c3   VARCHAR(64) NOT NULL,
            lab53c4   VARCHAR(256),
            lab53c5   smallint NOT NULL,
            lab53c6   VARCHAR(8),
            lab53c7   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab53 ADD CONSTRAINT lab53_pk PRIMARY KEY ( lab53c1 );
    END IF;
    --PRUEBA AUTOMATICA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab47')
    THEN
        CREATE TABLE lab47 (
            lab39c1_1   INTEGER NOT NULL,
            lab47c1     INTEGER NOT NULL,
            lab47c2     VARCHAR(16) NOT NULL,
            lab47c3     VARCHAR(16),
            lab39c1_2   INTEGER NOT NULL
        );
    END IF;
    --RELACIÓN RESULTADOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab74')
    THEN
        CREATE TABLE lab74 (
            lab74c1   INTEGER,
            lab74c2   VARCHAR(16),
            lab74c3   VARCHAR(16) NOT NULL,
            lab74c4   VARCHAR(16),
            lab74c5   timestamp,
            lab74c6   INTEGER NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab73c1   INTEGER NOT NULL
        );
    END IF;
    --HOJA DE TRABAJO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab37')
    THEN
        CREATE TABLE lab37 (
            lab37c1   SERIAL NOT NULL,
            lab37c2   VARCHAR(64) NOT NULL,
            lab37c3   smallint,
            lab37c4   smallint,
            lab37c5   smallint NOT NULL,
            lab37c6   smallint NOT NULL,
            lab37c7   DATE NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab37 ADD CONSTRAINT lab37_pk PRIMARY KEY ( lab37c1 );
    END IF;
    --HOJA DE TRABAJO - EXAMEN
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab38')
    THEN
        CREATE TABLE lab38 (
            lab37c1   INTEGER NOT NULL,
            lab39c1   INTEGER NOT NULL
        );
    END IF;
    --PLANTILLA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab51')
    THEN
        CREATE TABLE lab51 (
                lab51c1   SERIAL NOT NULL,
                lab51c2   VARCHAR(64) NOT NULL,
                lab51c3   VARCHAR(512),
                lab51c4   smallint NOT NULL,
                lab39c1   INTEGER NOT NULL
        );

        ALTER TABLE lab51 ADD CONSTRAINT lab51_pk PRIMARY KEY ( lab51c1 );
    END IF;
    --RESULTADOS PLANTILLA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab146')
    THEN
        CREATE TABLE lab146 (
                lab146c1   VARCHAR(512) NOT NULL,
                lab146c2   smallint NOT NULL,
                lab146c3   smallint NOT NULL,
                lab51c1    INTEGER NOT NULL
        );
    END IF;
    --CONTADOR HEMATOLOGICO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab18')
    THEN
        CREATE TABLE lab18 (
            lab18c1   SERIAL NOT NULL,
            lab18c2   VARCHAR(1) NOT NULL,
            lab18c3   VARCHAR(128) NOT NULL,
            lab18c4   smallint NOT NULL,
            lab18c5   smallint NOT NULL,
            lab39c1   INTEGER,
            lab18c6   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab18 ADD CONSTRAINT lab18_pk PRIMARY KEY ( lab18c1 );
    END IF;
    --HOMOLOGACION DE DEMOGRAFICOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab117')
    THEN
        CREATE TABLE lab117 (
            lab118c1   INTEGER NOT NULL,
            lab117c1   INTEGER NOT NULL,
            lab117c2   INTEGER NOT NULL,
            lab117c3   VARCHAR(128) NOT NULL
        );

    END IF;
    --EXCLUIR EXAMEN X PRUEBA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab104')
    THEN
        CREATE TABLE lab104 (
            lab104c1   INTEGER NOT NULL,
            lab104c2   INTEGER NOT NULL,
            lab39c1   INTEGER NOT NULL
        );
    END IF;

    --Tarifas por cliente
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab15')
    THEN
        CREATE TABLE lab15 (
               lab14c1    INTEGER NOT NULL,
               lab904c1   INTEGER NOT NULL
        );
    END IF;
    --Pruebas por usuario
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab72')
    THEN
        CREATE TABLE lab72 (
            lab39c1   INTEGER NOT NULL,
            lab04c1   INTEGER NOT NULL
        );

    END IF;

    --Días por alarma
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab178')
    THEN
        CREATE TABLE lab178 (
            lab178c1   INTEGER NOT NULL,
            lab178c2   INTEGER,
            lab39c1    INTEGER NOT NULL,
            lab178c3   INTEGER NOT NULL
        );
    END IF;
    -- Pruebas por PyP
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab175')
    THEN
        CREATE TABLE lab175 (
            lab175c1   INTEGER NOT NULL,
            lab175c2   INTEGER,
            lab175c3   INTEGER,
            lab175c4   smallint,
            lab175c5   INTEGER,
            lab39c1    INTEGER NOT NULL
        );

        ALTER TABLE lab175 ADD CONSTRAINT lab175_pk PRIMARY KEY ( lab175c1,lab39c1 );
    END IF;
    --vigencia pos
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab116')
    THEN
        CREATE TABLE lab116 (
            lab116c1   SERIAL NOT NULL,
            lab116c2   VARCHAR(60) NOT NULL,
            lab116c3   DATE NOT NULL,
            lab116c4   DATE NOT NULL,
            lab116c5   smallint NOT NULL,
            lab07c1    smallint NOT NULL,
            lab116c7   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL
        );

        ALTER TABLE lab116 ADD CONSTRAINT lab116_pk PRIMARY KEY ( lab116c1 );
    END IF;
    --TIEMPOS DE PROCESO (MUESTRAS POR SERVICIO O PRUEBAS POR SERVICIO)
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab171')
    THEN
        CREATE TABLE lab171 (
            lab10c1    INTEGER NOT NULL,
            lab171c1   INTEGER NOT NULL,
            lab171c2   INTEGER NOT NULL,
            lab171c3   smallint NOT NULL,
            lab171c4   INTEGER
        );
    END IF;
    --MEDIO DE CULTIVO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab155')
    THEN
           CREATE TABLE lab155 (
            lab155c1   SERIAL NOT NULL,
            lab155c2   VARCHAR (10) NOT NULL,
            lab155c3   VARCHAR (60) NOT NULL,
            lab07c1    smallint NOT NULL,
            lab155c4   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL
        );

        ALTER TABLE lab155 ADD CONSTRAINT lab155_pk PRIMARY KEY ( lab155c1 );
    END IF;
    --PROCEDIMIENTO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab156')
    THEN
        CREATE TABLE lab156 (
            lab156c1   SERIAL NOT NULL,
            lab156c2   VARCHAR(16) NOT NULL,
            lab156c3   VARCHAR(64) NOT NULL,
            lab156c4   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL
        );

        ALTER TABLE lab156 ADD CONSTRAINT lab156_pk PRIMARY KEY ( lab156c1 );
    END IF;
    --Relacion de Prueba-Medio de cultivo
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab164')
    THEN
        CREATE TABLE lab164 (
            lab39c1    INTEGER NOT NULL,
            lab155c1   INTEGER NOT NULL,
            lab164c1   smallint NOT NULL
        );

        ALTER TABLE lab164 ADD CONSTRAINT lab164_pk PRIMARY KEY ( lab39c1, lab155c1 );
    END IF;
    --PREGUNTAS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab70')
    THEN
        CREATE TABLE lab70 (
            lab70c1   SERIAL NOT NULL,
            lab70c2   VARCHAR(64) NOT NULL,
            lab70c3   VARCHAR(512) NOT NULL,
            lab70c4   smallint NOT NULL,
            lab70c5   INTEGER NOT NULL,
            lab70c6   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab70 ADD CONSTRAINT lab70_pk PRIMARY KEY ( lab70c1 );
    END IF;
    --RESPUESTAS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab90')
    THEN
        CREATE TABLE lab90 (
            lab90c1   SERIAL NOT NULL,
            lab90c2   VARCHAR(64) NOT NULL,
            lab90c3   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab90 ADD CONSTRAINT lab90_pk PRIMARY KEY ( lab90c1 );
    END IF;
    --PREGUNTAS - RESPUESTAS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab91')
    THEN
        CREATE TABLE lab91 (
            lab70c1   INTEGER NOT NULL,
            lab90c1   INTEGER NOT NULL
        );
    END IF;
    --RELACIÓN DE PRUEBA PROCEDIMIENTO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab157')
    THEN
        CREATE TABLE lab157 (
            lab39c1    INTEGER NOT NULL,
            lab156c1   INTEGER NOT NULL,
            lab157c1   smallint,
            lab157c2   INTEGER
        );

        ALTER TABLE lab157 ADD CONSTRAINT lab157_pk PRIMARY KEY ( lab39c1,lab156c1 );
    END IF;
    --PRECIOS VIGENCIA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab120')
    THEN
        CREATE TABLE lab120 (
                lab39c1    INTEGER NOT NULL,
                lab904c1   INTEGER NOT NULL,
                lab116c1   INTEGER NOT NULL,
                lab120c1   float NOT NULL
        );

        ALTER TABLE lab120 ADD CONSTRAINT lab120_pk PRIMARY KEY ( lab39c1,lab904c1,lab116c1 );
    END IF;
    --PRECIOS VIGENCIA APLICADA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab55')
    THEN
	CREATE TABLE lab55 (
		lab39c1    INTEGER NOT NULL,
		lab904c1   INTEGER NOT NULL,
		lab55c1    float NOT NULL
	);

	ALTER TABLE lab55 ADD CONSTRAINT lab55_pk PRIMARY KEY ( lab39c1,lab904c1);
    END IF;

    --ENTREVISTA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab44')
    THEN
        CREATE TABLE lab44 (
            lab44c1   SERIAL NOT NULL,
            lab44c2   VARCHAR(60) NOT NULL,
            lab44c3   smallint NOT NULL,
            lab44c4   smallint NOT NULL,
            lab07c1   smallint NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab44c5   timestamp NOT NULL
        );

        ALTER TABLE lab44 ADD CONSTRAINT lab44_pk PRIMARY KEY ( lab44c1 );
    END IF;

    --ENTREVISTA-PREGUNTA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab92')
    THEN
        CREATE TABLE lab92 (
            lab44c1   INTEGER NOT NULL,
            lab70c1   INTEGER NOT NULL,
            lab92c1   smallint NOT NULL
        );

        ALTER TABLE lab92 ADD CONSTRAINT lab92_pk PRIMARY KEY ( lab44c1,lab70c1 );
    END IF;
    --ENTREVISTA-TIPO DE ENTREVISTA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab66')
    THEN
        CREATE TABLE lab66 (
            lab44c1   INTEGER NOT NULL,
            lab66c1   INTEGER NOT NULL
        );
        ALTER TABLE lab66 ADD CONSTRAINT lab66_pk PRIMARY KEY ( lab66c1,lab44c1 );
    END IF;
    --ASIGNACION DE DESTINOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab52')
    THEN
        CREATE TABLE lab52 (
            lab52c1    SERIAL NOT NULL,
            lab103c1   INTEGER NOT NULL,
            lab05c1    INTEGER,
            lab24c1    INTEGER NOT NULL,
            lab52c2    timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL
        );

        ALTER TABLE lab52 ADD CONSTRAINT lab52_pk PRIMARY KEY ( lab52c1 );
    END IF;
    --RUTA DE DESTINOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab42')
    THEN
        CREATE TABLE lab42 (
            lab42c1   SERIAL NOT NULL,
            lab53c1   INTEGER NOT NULL,
            lab52c1   INTEGER NOT NULL,
            lab42c2   INTEGER NOT NULL
        );

        ALTER TABLE lab42 ADD CONSTRAINT lab42_pk PRIMARY KEY ( lab42c1 );
    END IF;
    --RUTA DE DESTINOS - EXAMENES
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab87')
    THEN
        CREATE TABLE lab87 (
            lab42c1   INTEGER NOT NULL,
            lab39c1   INTEGER NOT NULL
        );

    END IF;
    -- SUB-MUESTRAS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab97')
    THEN
        CREATE TABLE lab97 (
            lab24c1   INTEGER NOT NULL,
            lab97c1   INTEGER NOT NULL
        );

        ALTER TABLE lab97 ADD CONSTRAINT lab97_pk PRIMARY KEY ( lab24c1,lab97c1 );
    END IF;
    --HOMOLOGACION DE USUARIOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab96')
    THEN
        CREATE TABLE lab96 (
            lab04c1    INTEGER NOT NULL,
            lab118c1   INTEGER NOT NULL,
            lab96c1    VARCHAR(64) NOT NULL
        );
    END IF;
    --TIPO DE DOCUMENTO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab54')
    THEN
        CREATE TABLE lab54 (
            lab54c1   SERIAL NOT NULL,
            lab54c2   VARCHAR(16) NOT NULL,
            lab54c3   VARCHAR(64) NOT NULL,
            lab54c4   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab54 ADD CONSTRAINT lab54_pk PRIMARY KEY ( lab54c1 );
    END IF;
    --TIPO DE PAGO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab01')
    THEN
        CREATE TABLE lab01 (
                lab01c1   SERIAL NOT NULL,
                lab01c2   VARCHAR(64) NOT NULL,
                lab01c3   smallint NOT NULL,
                lab01c4   smallint NOT NULL,
                lab01c5   smallint NOT NULL,
                lab01c6   smallint NOT NULL,
                lab01c7   timestamp NOT NULL,
                lab04c1   INTEGER NOT NULL,
                lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab01 ADD CONSTRAINT lab01_pk PRIMARY KEY ( lab01c1 );
    END IF;
    --FESTIVOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab81')
    THEN
        CREATE TABLE lab81 (
            lab81c1   SERIAL NOT NULL,
            lab81c2   VARCHAR(64) NOT NULL,
            lab81c3   timestamp NOT NULL,
            lab81c4   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab81 ADD CONSTRAINT lab81_pk PRIMARY KEY ( lab81c1 );
    END IF;
    --OPORTUNIDAD DE LA MUESTRA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab83')
    THEN
        CREATE TABLE lab83 (
            lab10c1   INTEGER NOT NULL,
            lab42c1   INTEGER NOT NULL,
            lab83c1   INTEGER NOT NULL,
            lab83c2   INTEGER NOT NULL
        );
    END IF;
    --TRAZABILIDAD DE LA MUESTRA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab144')
    THEN
        CREATE TABLE lab144 (
            lab22c1    BIGINT NOT NULL,
            lab24c1    INTEGER NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab30c1    INTEGER,
            lab05c1    INTEGER NOT NULL ,
            lab144c1   SERIAL,
            lab144c2   timestamp NOT NULL,
            lab144c3   smallint NOT NULL,
            lab144c4   VARCHAR
        );

        ALTER TABLE lab144 ADD CONSTRAINT lab144_pk PRIMARY KEY ( lab144c1 );
    END IF;
    --ACCESOS DIRECTOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab125')
    THEN
        CREATE TABLE lab125 (
            lab125c1   INTEGER NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab85c1    INTEGER NOT NULL
        );

        ALTER TABLE lab125 ADD CONSTRAINT lab125_pk PRIMARY KEY ( lab125c1,lab04c1,lab85c1 );
    END IF;
    --ALERTAS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab12')
    THEN
        CREATE TABLE lab12 (
            lab12c1   VARCHAR(64) NOT NULL,
            lab12c2   VARCHAR(512) NOT NULL,
            lab12c3   INTEGER NOT NULL,
            lab12c4   VARCHAR(64) NOT NULL,
            lab12c5   VARCHAR(64) ,
            lab12c6   INTEGER NOT NULL
        );
    END IF;
    --Grupos Filtros examenes
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab65')
    THEN
        CREATE TABLE lab65 (
            lab65c1   SERIAL NOT NULL,
            lab65c2   VARCHAR(8) NOT NULL,
            lab65c3   VARCHAR(64) NOT NULL,
            lab65c4   timestamp,
            lab04c1   INTEGER,
            lab07c1   INTEGER
        );
        ALTER TABLE lab65 ADD CONSTRAINT lab65_pk PRIMARY KEY ( lab65c1 );
    END IF;

    --FLUJO DE LA MUESTRA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab25')
    THEN
        CREATE TABLE lab25 (
            lab22c1   bigint NOT NULL,
            lab42c1   INTEGER NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab25c1   timestamp NOT NULL,
            lab25c2   smallint NOT NULL
        );

        ALTER TABLE lab25 ADD CONSTRAINT lab25_pk PRIMARY KEY ( lab22c1,lab42c1 );
    END IF;
    --ENTREVISTA - ORDEN
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab23')
    THEN
        CREATE TABLE lab23 (
            lab22c1   bigint NOT NULL,
            lab70c1   INTEGER NOT NULL,
            lab90c1   INTEGER,
            lab23c1   VARCHAR(8000),
            lab23c2   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL
        );
    END IF;
    --HOJA DE IMPRESIÓN
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab32')
    THEN
        CREATE TABLE lab32 (
            lab22c1   bigint NOT NULL,
            lab39c1   INTEGER NOT NULL,
            lab37c1   INTEGER NOT NULL,
            lab32c1   INTEGER,
            lab32c2   timestamp,
            lab04c1   INTEGER NOT NULL
        );

        ALTER TABLE lab32 ADD CONSTRAINT lab32_pk PRIMARY KEY ( lab22c1,lab39c1 );
    END IF;
    --ESTADO DE LA MUESTRA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab159')
    THEN
        CREATE TABLE lab159 (
            lab22c1    bigint NOT NULL,
            lab24c1    INTEGER NOT NULL,
            lab159c1   INTEGER NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab159c2   timestamp NOT NULL,
            lab53c1    INTEGER,
            lab05c1    INTEGER NOT NULL,
            lab52c1    INTEGER
        );

        ALTER TABLE lab159 ADD CONSTRAINT lab159_pk PRIMARY KEY ( lab24c1,lab22c1 );
    END IF;
    --TRAZABILIDAD DE LA MUESTRA - EXAMENES
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab160')
    THEN
        CREATE TABLE lab160 (
            lab144c1   INTEGER NOT NULL,
            lab39c1    INTEGER NOT NULL
        );

        ALTER TABLE lab160 ADD CONSTRAINT lab160_pk PRIMARY KEY ( lab144c1,lab39c1 );
    END IF;
    --Filtro Examenes
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab02')
    THEN
        CREATE TABLE lab02 (
            lab65c1   INTEGER NOT NULL,
            lab39c1   INTEGER NOT NULL
        );
        ALTER TABLE lab02 ADD CONSTRAINT lab02_pk PRIMARY KEY ( lab65c1,lab39c1 );
    END IF;
    --TRAZABILIDAD DE LA ORDEN
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab03')
    THEN
        CREATE TABLE lab03 (
            lab22c1   BIGINT NOT NULL,
            lab03c1   INTEGER,
            lab03c2   VARCHAR(4) NOT NULL,
            lab03c3   VARCHAR(4) NOT NULL,
            lab03c4   VARCHAR NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab03c5   timestamp NOT NULL,
            lab30c1   INTEGER,
            lab03c6   VARCHAR
        );
    END IF;
    --CONCURRENCIA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab113')
    THEN
        CREATE TABLE lab113 (
            lab113c1   smallint NOT NULL,
            lab113c2   INTEGER,
            lab113c3   VARCHAR(32) NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab113c4   timestamp NOT NULL
        );
    END IF;
    --INCONSISTENCIAS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab33')
    THEN
        CREATE TABLE lab33 (
            lab22c1   bigint NOT NULL,
            lab33c1   VARCHAR NOT NULL,
            lab33c2   VARCHAR NOT NULL,
            lab04c1   INTEGER NOT NULL,
            lab33c3   timestamp NOT NULL,
            lab33c4   VARCHAR(128)
        );

        ALTER TABLE lab33 ADD CONSTRAINT lab33_pk PRIMARY KEY ( lab22c1 );
    END IF;
    --HISTORICO RESULTADO - REPETICIONES
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab29')
    THEN
        CREATE TABLE lab29 (
            lab29c1     SERIAL NOT NULL,
            lab22c1     bigint NOT NULL,
            lab39c1     INTEGER NOT NULL,
            lab29c2     VARCHAR(128),
            lab29c3     timestamp NOT NULL,
            lab29c4     smallint NOT NULL,
            lab04c1_1   INTEGER NOT NULL,
            lab29c5     CHAR(1) NOT NULL,
            lab30c1     INTEGER NOT NULL,
            lab29c7     VARCHAR,
            lab29c8     timestamp NOT NULL,
            lab04c1_2   INTEGER NOT NULL,
            lab29c9     smallint NOT NULL
        );

        ALTER TABLE lab29 ADD CONSTRAINT lab29_pk PRIMARY KEY ( lab29c1 );
    END IF;
    --CONTROL DE ENTREGA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab167')
    THEN
        CREATE TABLE lab167 (
            lab167c1   SERIAL NOT NULL,
            lab22c1    bigint NOT NULL,
            lab167c2   VARCHAR(256),
            lab04c1    INTEGER,
            lab167c3   timestamp
        );
        ALTER TABLE lab167 ADD CONSTRAINT lab167_pk PRIMARY KEY ( lab167c1 );
    END IF;
    --EXAMENES X ENTREGA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab168')
    THEN
        CREATE TABLE lab168 (
            lab167c1   INTEGER NOT NULL,
            lab39c1    INTEGER NOT NULL
        );
        ALTER TABLE lab168 ADD CONSTRAINT lab168_pk PRIMARY KEY ( lab167c1,lab39c1 );
    END IF;
    --WIDGET - MUESTRAS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab300')
    THEN
        CREATE TABLE lab300 (
            lab300c1   INTEGER NOT NULL,
            lab05c1    INTEGER NOT NULL,
            lab300c2   INTEGER NOT NULL,
            lab300c3   INTEGER NOT NULL,
            lab300c4   INTEGER NOT NULL,
            lab300c5   INTEGER NOT NULL,
            lab300c6   INTEGER NOT NULL,
            lab300c7   INTEGER NOT NULL
        );

        ALTER TABLE lab300 ADD CONSTRAINT lab300_pk PRIMARY KEY ( lab300c1,lab05c1 );
    END IF;
    --SIEMBRA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab200')
    THEN
        CREATE TABLE lab200 (
            lab22c1     BIGINT NOT NULL,
            lab39c1     INTEGER NOT NULL,
            lab200c2    timestamp NOT NULL,
            lab04c1     INTEGER NOT NULL,
            lab200c3    timestamp,
            lab04c1_1   INTEGER
        );

        ALTER TABLE lab200 ADD CONSTRAINT lab200_pk PRIMARY KEY ( lab22c1,lab39c1 );
    END IF;
    --METODO DE RECOLECCION
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab201')
    THEN
        CREATE TABLE lab201 (
            lab201c1   SERIAL NOT NULL,
            lab201c2   VARCHAR(64) NOT NULL,
            lab201c3   timestamp,
            lab04c1    INTEGER,
            lab07c1    smallint
        );

        ALTER TABLE lab201 ADD CONSTRAINT lab201_pk PRIMARY KEY ( lab201c1 );
    END IF;
    --PROCEDIMIENTO POR SIEMBRA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab202')
    THEN
        CREATE TABLE lab202 (
            lab22c1    BIGINT,
            lab39c1    INTEGER,
            lab156c1   INTEGER
        );

        ALTER TABLE lab202 ADD CONSTRAINT lab202_pk PRIMARY KEY ( lab22c1,lab39c1,lab156c1 );
    END IF;
    --MEDIO DE CULTIVO POR SIEMBRA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab203')
    THEN
        CREATE TABLE lab203 (
            lab22c1   BIGINT NOT NULL,
            lab39c1   INTEGER NOT NULL,
            lab155c1    INTEGER NOT NULL
        );

        ALTER TABLE lab203 ADD CONSTRAINT lab203_pk PRIMARY KEY ( lab22c1,lab39c1,lab155c1 );
    END IF;
    --DETALLE EXAMEN PRECIO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab900')
    THEN
        CREATE TABLE lab900 (
            lab900c1   SERIAL NOT NULL,
            lab21c1   INTEGER NOT NULL,
            lab22c1    BIGINT NOT NULL,
            lab39c1    INTEGER NOT NULL,
            lab900c2    FLOAT NOT NULL,
            lab900c3    FLOAT,
            lab900c4    FLOAT,
            lab901c1P    INTEGER,
            lab901c1I    INTEGER,
            lab904c1    INTEGER NOT NULL,
            lab14c1    INTEGER,
            lab05c1    INTEGER
        );
        ALTER TABLE lab900 ADD CONSTRAINT lab900_pk PRIMARY KEY (lab900c1);
    END IF;
    --DETECCIÓN MICROBIANA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab204')
    THEN
        CREATE TABLE lab204 (
            lab204c1   SERIAL NOT NULL,
            lab22c1    bigint NOT NULL,
            lab39c1    INTEGER NOT NULL,
            lab76c1    INTEGER NOT NULL,
            lab204c2   VARCHAR,
            lab204c3   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab77c1    INTEGER NOT NULL
        );

        ALTER TABLE lab204 ADD CONSTRAINT lab204_pk PRIMARY KEY ( lab204c1 );
    END IF;
    --DETECCIÓN MICROBIANA - ANTIBIOGRAMA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab205')
    THEN
        CREATE TABLE lab205 (
            lab204c1    INTEGER NOT NULL,
            lab79c1     INTEGER NOT NULL,
            lab205c1    VARCHAR(64),
            lab205c2    VARCHAR(64),
            lab205c3    VARCHAR(64),
            lab205c4    VARCHAR(64),
            lab205c5    smallint NOT NULL,
            lab205c6    VARCHAR(64),
            lab205c7    VARCHAR(64),
            lab205c8    smallint NOT NULL,
            lab205c9    timestamp,
            lab04c1_1   INTEGER,
            lab205c10   timestamp,
            lab04c1_2   INTEGER,
            lab205c11   timestamp,
            lab04c1_3   INTEGER
        );

        ALTER TABLE lab205 ADD CONSTRAINT lab205_pk PRIMARY KEY ( lab204c1,lab79c1 );
    END IF;
    --Grupo Etario
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab13')
    THEN
        CREATE TABLE lab13 (
            lab13c1   SERIAL NOT NULL,
            lab13c2   VARCHAR(16) NOT NULL,
            lab13c3   VARCHAR(64) NOT NULL,
            lab13c4   INTEGER,
            lab13c5   smallint,
            lab13c6   INTEGER,
            lab13c7   INTEGER,
            lab07c1   smallint NOT NULL,
            lab13c8   timestamp,
            lab04c1   INTEGER
        );

        ALTER TABLE lab13 ADD CONSTRAINT lab13_pk PRIMARY KEY ( lab13c1 );
    END IF;
    --MICROORGAMISMO - ANTIBIOGRAMA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab206')
    THEN
        CREATE TABLE lab206 (
            lab77c1   INTEGER NOT NULL,
            lab76c1   INTEGER NOT NULL,
            lab39c1   INTEGER
        );
    END IF;
    --DESTINO DE MICROBIOLOGIA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab207')
    THEN
        CREATE TABLE lab207 (
            lab207c1   SERIAL NOT NULL,
            lab207c2   VARCHAR(16) NOT NULL,
            lab207c3   VARCHAR(64) NOT NULL,
            lab207c4   smallint NOT NULL,
            lab207c5   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL
        );

        ALTER TABLE lab207 ADD CONSTRAINT lab207_pk PRIMARY KEY ( lab207c1 );
    END IF;
    --CLASE HISTOGRAMA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab06')
    THEN
       CREATE TABLE lab06 (
            lab06c1   SERIAL NOT NULL,
            lab06c2   VARCHAR(32) NOT NULL,
            lab06c3   BIGINT NOT NULL,
            lab06c4   BIGINT,
            lab06c5   timestamp,
            lab04c1   INTEGER,
            lab07c1   smallint
        );
        ALTER TABLE lab06 ADD CONSTRAINT lab06_pk PRIMARY KEY ( lab06c1 );
    END IF;
    --MICROBIOLOGIA - DESTINOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab208')
    THEN
        CREATE TABLE lab208 (
            lab208c1    SERIAL NOT NULL,
            lab208c2    INTEGER NOT NULL,
            lab208c3    VARCHAR,
            lab208c4    smallint NOT NULL,
            lab22c1     bigint NOT NULL,
            lab39c1     INTEGER NOT NULL,
            lab207c1    INTEGER NOT NULL,
            lab208c5    timestamp NOT NULL,
            lab04c1     INTEGER NOT NULL,
            lab208c6    smallint NOT NULL,
            lab208c7    timestamp NOT NULL,
            lab04c1_1   INTEGER NOT NULL
        );

        ALTER TABLE lab208 ADD CONSTRAINT lab208_pk PRIMARY KEY ( lab208c1 );
    END IF;
    --MICROBIOLOGIA - TAREAS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab209')
    THEN
        CREATE TABLE lab209 (
            lab208c1   INTEGER NOT NULL,
            lab169c1   INTEGER NOT NULL
        );

        ALTER TABLE lab209 ADD CONSTRAINT lab209_pk PRIMARY KEY ( lab169c1,lab208c1 );
    END IF;
    --MICROBIOLOGIA - TAREAS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab210')
    THEN
        CREATE TABLE lab210 (
            lab76c1    INTEGER NOT NULL,
            lab79c1    INTEGER NOT NULL,
            lab210c1   smallint NOT NULL,
            lab210c2   smallint NOT NULL,
            lab210c3   VARCHAR(32) NOT NULL,
            lab210c4   VARCHAR(32),
            lab80c1    INTEGER NOT NULL,
            lab210c5   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL
        );

        ALTER TABLE lab210 ADD CONSTRAINT lab210_pk PRIMARY KEY ( lab76c1,lab79c1,lab210c2,lab210c1 );
    END IF;
    --DIAGNOSTICO - EXAMEN
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab26')
    THEN
        CREATE TABLE lab26 (
            lab39c1   INTEGER NOT NULL,
            lab20c1   INTEGER NOT NULL
        );
        ALTER TABLE lab26 ADD CONSTRAINT lab26_pk PRIMARY KEY ( lab39c1,lab20c1 );
    END IF;
    --MICROBIOLOGIA - COMENTARIO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab211')
    THEN
        CREATE TABLE lab211 (
            lab211c1   SERIAL NOT NULL,
            lab22c1    BIGINT NOT NULL,
            lab39c1    INTEGER NOT NULL,
            lab24c1    INTEGER NOT NULL,
            lab211c2   VARCHAR NOT NULL,
            lab211c3   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL
        );

        ALTER TABLE lab211 ADD CONSTRAINT lab211_pk PRIMARY KEY ( lab211c1 );
    END IF;
    --HISTORICO RESULTADOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab17')
    THEN
        CREATE TABLE lab17 (
            lab21c1     INTEGER,
            lab39c1     INTEGER,
            lab17c1     VARCHAR(128),
            lab17c2     TIMESTAMP,
            lab04c1_1   INTEGER,
            lab17c3     VARCHAR(128),
            lab17c4     TIMESTAMP,
            lab04c1_2   INTEGER
        );
        ALTER TABLE lab17 ADD CONSTRAINT lab17_pk PRIMARY KEY ( lab21c1, lab39c1 );
    END IF;
    --RESULTADOS - PLANTILLA DE RESULTADOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab58')
    THEN
        CREATE TABLE lab58 (
            lab22c1   bigint NOT NULL,
            lab39c1   INTEGER NOT NULL,
            lab58c1   VARCHAR,
            lab58c2   timestamp NOT NULL,
            lab58c3   VARCHAR(512),
            lab58c4   VARCHAR NOT NULL,
            lab04c1   INTEGER NOT NULL
        );
    END IF;
    --CAJA CABECERA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab902')
    THEN
        CREATE TABLE lab902 (
            lab902c1    SERIAL NOT NULL,
            lab22c1     bigint NOT NULL,
            lab904c1    INTEGER NOT NULL,
            lab902c2    FLOAT NOT NULL,
            lab902c3    FLOAT,
            lab902c4    FLOAT,
            lab902c5    FLOAT NOT NULL,
            lab902c6    FLOAT,
            lab902c7    FLOAT,
            lab902c8    FLOAT NOT NULL,
            lab902c9    FLOAT NOT NULL,
            lab04c1_i   INTEGER NOT NULL,
            lab902c10   TIMESTAMP NOT NULL,
            lab04c1_u   INTEGER,
            lab902c11   TIMESTAMP
        );
        ALTER TABLE lab902 ADD CONSTRAINT lab902_pk PRIMARY KEY ( lab902c1 );
    END IF;
    --CAJA ABONOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab903')
    THEN
        CREATE TABLE lab903 (
            lab903c1    SERIAL NOT NULL,
            lab22c1     BIGINT NOT NULL,
            lab01c1     INTEGER NOT NULL,
            lab903c2    VARCHAR(64),
            lab110c1    INTEGER,
            lab59c1     INTEGER,
            lab903c3    FLOAT,
            lab07c1     INTEGER,
            lab04c1_i   INTEGER NOT NULL,
            lab903c4    TIMESTAMP NOT NULL,
            lab04c1_u   INTEGER,
            lab903c5    TIMESTAMP
        );
        ALTER TABLE lab903 ADD CONSTRAINT lab903_pk PRIMARY KEY ( lab903c1 );
    END IF;
    --EMISOR(CLIENTE)
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab906')
    THEN
        CREATE TABLE lab906 (
            lab906c1   SERIAL NOT NULL,
            lab906c2   VARCHAR(64) NOT NULL,
            lab906c3   VARCHAR(128) NOT NULL,
            lab906c4   VARCHAR(128),
            lab906c5   VARCHAR(32),
            lab906c6   VARCHAR(64),
            lab906c7   VARCHAR(64),
            lab906c8   VARCHAR(16),
            lab906c9   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL
        );
        ALTER TABLE lab906 ADD CONSTRAINT lab906_pk PRIMARY KEY ( lab906c1 );
    END IF;
    --RESOLUCION
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab907')
    THEN
        CREATE TABLE lab907 (
            lab907c1   SERIAL NOT NULL,
            lab907c2   VARCHAR(256) NOT NULL,
            lab907c3   INTEGER NOT NULL,
            lab907c4   INTEGER NOT NULL,
            lab907c5   VARCHAR(8),
            lab907c6   INTEGER,
            lab906c1   INTEGER NOT NULL,
            lab907c7   timestamp NOT NULL,
            lab04c1    INTEGER NOT NULL,
            lab07c1    smallint NOT NULL
        );

        ALTER TABLE lab907 ADD CONSTRAINT lab907_pk PRIMARY KEY ( lab907c1 );
    END IF;
    --Gradillas
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab16')
    THEN
        CREATE TABLE lab16 (
            lab16c1   SERIAL NOT NULL,
            lab16c2   VARCHAR(8),
            lab16c3   VARCHAR(32) NOT NULL,
            lab16c4   smallint NOT NULL,
            lab05c1   INTEGER NOT NULL,
            lab16c5   smallint,
            lab16c6   smallint,
            lab16c7   timestamp NOT NULL,
            lab16c8   VARCHAR(32),
            lab31c1   INTEGER,
            lab04c1   INTEGER NOT NULL,
            lab07c1   smallint NOT NULL
        );

        ALTER TABLE lab16 ADD CONSTRAINT lab16_pk PRIMARY KEY ( lab16c1 );
    END IF;
    --GRADILLA DETALLE
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab11')
    THEN
        CREATE TABLE lab11 (
            lab11c1     INTEGER NOT NULL,
            lab16c1     INTEGER NOT NULL,
            lab24c1     INTEGER NOT NULL,
            lab22c1     bigint NOT NULL,
            lab11c2     timestamp NOT NULL,
            lab11c3     timestamp,
            lab11c4     smallint NOT NULL,
            lab11c5     timestamp,
            lab27c1     INTEGER,
            lab04c1     INTEGER NOT NULL,
            lab04c1_1   INTEGER
        );
    END IF;

    --ACTA DESECHO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab27')
    THEN
        CREATE TABLE lab27 (
            lab27c1     SERIAL NOT NULL,
            lab27c2     VARCHAR(1024) NOT NULL,
            lab27c3     smallint NOT NULL,
            lab27c4     timestamp,
            lab04c1     INTEGER,
            lab27c5     smallint NOT NULL,
            lab27c6     timestamp,
            lab27c7     VARCHAR(32) NOT NULL,
            lab04c1_1   INTEGER
        );
        ALTER TABLE lab27 ADD CONSTRAINT lab27_pk PRIMARY KEY ( lab27c1 );

    END IF;
    --ACTA DESECHO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab179')
    THEN
        CREATE TABLE lab179 (
            lab22c1    bigint NOT NULL,
            lab39c1    INTEGER NOT NULL,
            lab70c1    INTEGER NOT NULL,
            lab90c1    INTEGER,
            lab179c1   VARCHAR,
            lab179c2   timestamp NOT NULL,
            lab179c3   smallint NOT NULL,
            lab179c4   smallint NOT NULL,
            lab179c5   smallint NOT NULL,
            lab04c1    INTEGER NOT NULL
        );
    END IF;

    --Total pago
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab908')
    THEN
       CREATE TABLE lab908 (
           lab22c1    bigint NOT NULL,
           lab908c1   float,
           lab908c2   float,
           lab908c3   float,
           lab908c4   timestamp NOT NULL,
           lab04c1    INTEGER NOT NULL

       );
    ALTER TABLE lab908 ADD CONSTRAINT lab908_pk PRIMARY KEY ( lab22c1 );
    END IF;
    
    --ENTREGA DEL RESULTADO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab75')
    THEN
       CREATE TABLE lab75 (
           lab75c1  SERIAL NOT NULL,
           lab75c2  BIGINT NOT NULL,
           lab75c3  INTEGER,
           lab75c4  INTEGER,
           lab04c1  INTEGER NOT NULL,
           lab75c5  TIMESTAMP NOT NULL,
           lab80c1  INTEGER
           
        );
    ALTER TABLE lab75 ADD CONSTRAINT lab75_pk PRIMARY KEY ( lab75c1 );
    END IF;
    
    --CABECERA DE COTIZACION
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab101')
    THEN
       CREATE TABLE lab101 (
           lab101c1  SERIAL NOT NULL,
           lab101c2  VARCHAR,
           lab904c1  FLOAT,
           lab101c3  VARCHAR,
           lab101c4  FLOAT,
           lab04c1   INTEGER NOT NULL,          
           lab101c5  timestamp NOT NULL
        );
    ALTER TABLE lab101 ADD CONSTRAINT lab101_pk PRIMARY KEY ( lab101c1 );
    END IF;
    
    -- DETALLE DE COTIZACION
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab102')
    THEN
       CREATE TABLE lab102 (
           lab102c1  SERIAL NOT NULL,
           lab101c1  INTEGER,
           lab39c1   FLOAT,
           lab904c1  FLOAT,
           lab102c3  FLOAT,
           lab102c4  timestamp NOT NULL,
           lab04c1   INTEGER NOT NULL
        );
    ALTER TABLE lab102 ADD CONSTRAINT lab102_pk PRIMARY KEY ( lab102c1 );
    END IF;

    --Diagnosticos por orden
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab180')
    THEN
       CREATE TABLE lab180 (
           lab20c1    bigint NOT NULL,
           lab22c1    bigint NOT NULL
       );
    END IF;

    --Plantilla barcode
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab105')
    THEN
       CREATE TABLE lab105 (
            lab105c1   SERIAL NOT NULL,
            lab105c2   INTEGER ,
            lab105c3   VARCHAR(2048),
            lab105c4   VARCHAR(2048),
            lab07c1    INTEGER 
        );
        ALTER TABLE lab105 ADD CONSTRAINT lab105_pk PRIMARY KEY ( lab105c1 );
    END IF;

    --Seriales para impresion
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab106')
    THEN
       CREATE TABLE lab106 (
            lab106c1   VARCHAR NOT NULL,
            lab106c2   VARCHAR(40) ,
            lab106c3   timestamp NOT NULL
        );
        ALTER TABLE lab106 ADD CONSTRAINT lab106_pk PRIMARY KEY ( lab106c1 );
    END IF;

    --Agrupacion de ordenes
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab801')
    THEN
       CREATE TABLE lab801 (
            lab801c1 INTEGER NOT NULL,
            lab801c2 INTEGER NOT NULL,
            lab04c1  INTEGER NOT NULL,
            lab801c3 timestamp NOT NULL
        );
        ALTER TABLE lab801 ADD CONSTRAINT lab801_pk PRIMARY KEY ( lab801c1, lab801c2 );
    END IF;
    --Impresion por servicio
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab107')
    THEN
       CREATE TABLE lab107 (
            lab106c1 VARCHAR NOT NULL, -- Serial
            lab05c1  INTEGER NOT NULL, -- Sede
            lab10c1 INTEGER NOT NULL --Servicio
        );
    END IF;
    --Rellamado de ordenes
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab221')
    THEN
       CREATE TABLE lab221 (
            lab22c1_1 BIGINT NOT NULL, -- Orden padre
            lab22c1_2  BIGINT NOT NULL -- Orden hija
        );
        ALTER TABLE lab221 ADD CONSTRAINT lab221_pk PRIMARY KEY ( lab22c1_1, lab22c1_2 );
    END IF;
    --Relacion que establece que examen en la muestra maneja el medio de cultivo por orden
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab165')
    THEN
       CREATE TABLE lab165 (
            lab22c1 BIGINT NOT NULL, -- Orden
            lab24c1  INTEGER NOT NULL, -- Id de la muestra
            lab39c1  INTEGER NOT NULL -- Id del examen
        );
        ALTER TABLE lab165 ADD CONSTRAINT lab165_pk PRIMARY KEY ( lab22c1, lab24c1, lab39c1 );
    END IF;
    ----------------------------ACTUALIZACIONES------------------------------
    --TIENE PLANTILLA
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c42')
    THEN
        ALTER TABLE lab57 ADD lab57c42 SMALLINT;
    END IF;
    --MOFICACION: MENSAJE
    ALTER TABLE lab151 ALTER COLUMN lab151c6 TYPE VARCHAR;
    --TIPO DE ERROR
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab151' AND column_name='lab151c8')
    THEN
        ALTER TABLE lab151 ADD lab151c8 INTEGER NOT NULL DEFAULT 0;
    END IF;
    --ALMACENAMIENTO ESPECIAL DE LA MUESTRA
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab24' AND column_name='lab24c14')
    THEN
        ALTER TABLE lab24 ADD lab24c14 SMALLINT NOT NULL DEFAULT 0;
    END IF;
    --TIENE DELTA - RESULTADO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c43')
    THEN
        ALTER TABLE lab57 ADD lab57c43 SMALLINT NOT NULL DEFAULT 0;
    END IF;
    --IMPUESTO - EXAMEN
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c49')
    THEN
        ALTER TABLE lab39 ADD lab39c49 FLOAT NULL;
    END IF;
    --FECHA FINAL ALMACENAMIENTO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab11' AND column_name='lab11c5')
    THEN
        ALTER TABLE lab11 ADD lab11c5 timestamp NULL;
    END IF;
    --ACTA DE DESECHO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab11' AND column_name='lab27c1')
    THEN
        ALTER TABLE lab11 ADD lab27c1 INTEGER NULL;
    END IF;
    --REQUIERE VALIDACIÓN PRELIMINAR - EXAMEN
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c50')
    THEN
        ALTER TABLE lab39 ADD lab39c50 SMALLINT NOT NULL DEFAULT 0;
    END IF;
    --GENERA ALERTA POR TENDENCIA - EXAMEN
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c51')
    THEN
        ALTER TABLE lab39 ADD lab39c51 SMALLINT NOT NULL DEFAULT 0;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c51')
    THEN
        ALTER TABLE lab39 ALTER COLUMN lab39c51 TYPE INTEGER;
    END IF;
    --ESTADO DEL MEDICO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab19' AND column_name='lab19c23')
    THEN
        ALTER TABLE lab19 ADD lab19c23 VARCHAR(128) NULL;
    END IF;
    --TURNO DE LA ORDEN
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab22c13')
    THEN
        ALTER TABLE lab22 ADD lab22c13 VARCHAR(32) NULL;
    END IF;
    --CAJA
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab908' AND column_name='lab908c5')
    THEN
        ALTER TABLE lab908 ADD lab908c5 FLOAT;
        ALTER TABLE lab908 ADD lab908c6 FLOAT;
    END IF;
    --INTEGRACION CON MIDELWARE
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab40' AND column_name='lab40c10')
    THEN
        ALTER TABLE lab40 ADD lab40c10 VARCHAR(512) NULL;
    END IF;
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab40' AND column_name='lab40c11')
    THEN
        ALTER TABLE lab40 ADD lab40c11 SMALLINT NOT NULL DEFAULT 0;
    END IF;
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab40' AND column_name='lab40c12')
    THEN
        ALTER TABLE lab40 ADD lab40c12 SMALLINT NOT NULL DEFAULT 0;
    END IF;
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab40' AND column_name='lab40c13')
    THEN
        ALTER TABLE lab40 ADD lab40c13 SMALLINT NOT NULL DEFAULT 0;
    END IF;
    --MEDICO CAMBIO DEL USERNAME
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab19' AND column_name='lab19c17' AND character_maximum_length=265)
    THEN
        ALTER TABLE lab19 ALTER COLUMN lab19c17 TYPE VARCHAR(256);
    END IF;
    --CLIENTE CAMBIO DEL USERNAME
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c24' AND character_maximum_length=128)
    THEN
        ALTER TABLE lab14 ALTER COLUMN lab14c24 TYPE VARCHAR(128);
    END IF;
    --SEDE PARA EL VISOR DE SESIONES
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab100' AND column_name='lab05c1')
    THEN
        ALTER TABLE lab100 ADD lab05c1 INTEGER NOT NULL;
    END IF;
    ALTER TABLE lab105 ALTER COLUMN lab105c3 TYPE TEXT;
    ALTER TABLE lab105 ALTER COLUMN lab105c4 TYPE TEXT;
    --FECHA Y USUARIO DE CREACION DE PACIENTE
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c20')
    THEN
        ALTER TABLE lab21 ADD lab21c20 timestamp;
        ALTER TABLE lab21 ADD lab04c1_2 INTEGER NOT NULL DEFAULT 0;
    END IF;
    --CAMPO NOMBRE DE LA ETIQUETA DEL CODIGO DE BARRAS
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab105' AND column_name='lab105c5')
    THEN
        ALTER TABLE lab105 ADD lab105c5 INTEGER NOT NULL DEFAULT 0;
    END IF;
    --CAMBIAR TAMAÑO DE CAMPO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab45' AND column_name='lab45c2' AND character_maximum_length=265)
    THEN
        ALTER TABLE lab45 ALTER COLUMN lab45c2 TYPE VARCHAR(265);
        ALTER TABLE lab64 ALTER COLUMN lab64c3 TYPE VARCHAR(2555);
    END IF;
    --CAMBIAR TAMAÑO DE CAMPO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab45c2' AND character_maximum_length=265)
    THEN
        ALTER TABLE lab57 ALTER COLUMN lab45c2 TYPE VARCHAR(265);
    END IF;
    --CAMBIAR TAMAÑO DE CAMPO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c33' AND character_maximum_length=8000)
    THEN
        ALTER TABLE lab39 ALTER COLUMN lab39c33 TYPE VARCHAR(8000);
        ALTER TABLE lab39 ALTER COLUMN lab39c34 TYPE VARCHAR(8000);
        ALTER TABLE lab39 ALTER COLUMN lab39c35 TYPE VARCHAR(8000);
        ALTER TABLE lab41 ALTER COLUMN lab41c3 TYPE VARCHAR(4000);
        ALTER TABLE lab45 ALTER COLUMN lab45c3 TYPE VARCHAR(265);
        ALTER TABLE lab68 ALTER COLUMN lab68c3 TYPE VARCHAR(4000);
        ALTER TABLE lab73 ALTER COLUMN lab73c3 TYPE VARCHAR(4000);
    END IF;
    --CAMPO NOMBRE DE LA COLUMNA PARA LA AGRUPACION
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab801' AND column_name='lab801c4')
    THEN
        ALTER TABLE lab801 ADD lab801c4 VARCHAR(50) NOT NULL DEFAULT '';
    END IF;
    --VALIDAR SI LA PREGUNTA ES OBLIGATORIA
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab23' AND column_name='lab70c5')
    THEN
        ALTER TABLE lab23 ADD lab70c5 INTEGER NOT NULL DEFAULT -1; -- Tipo de la pregunta
    END IF;
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab92' AND column_name='lab92c2')
    THEN
        ALTER TABLE lab92 ADD lab92c2 SMALLINT NOT NULL DEFAULT 0; --Pregunta Requerida
    END IF;
    --USUARIO DE CREACION DE LA ORDEN
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab04c1_1')
    THEN
        ALTER TABLE lab22 ADD lab04c1_1 INTEGER NOT NULL DEFAULT 0;
    END IF;
    --PENULTIMO RESULTADO TEMPORAL
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab17' AND column_name='lab17c5')
    THEN
        ALTER TABLE lab17 ADD lab17c5 VARCHAR(128);
        ALTER TABLE lab17 ADD lab17c6 TIMESTAMP;
        ALTER TABLE lab17 ADD lab04c1_3 INTEGER;
    END IF;
    --AGREGAR CAMPOS DE REPETICION EN RESULTADO DE EXAMENES
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c44')
    THEN
        ALTER TABLE lab57 ADD lab57c44 TIMESTAMP; --Fecha de retoma
        ALTER TABLE lab57 ADD lab57c45 INTEGER; --Usuario de la retoma
        ALTER TABLE lab57 ADD lab57c46 TIMESTAMP; --Fecha de la repeticion
        ALTER TABLE lab57 ADD lab57c47 INTEGER; --Usuario de repeticion
    END IF;
    --CUERPO DEL CORREO
    ALTER TABLE lab98 ALTER COLUMN lab98c2 TYPE TEXT;
    --MOFICACION: CANTIDAD DE CARACTERES PARA URL DEL ERROR
    ALTER TABLE lab151 ALTER COLUMN lab151c5 TYPE VARCHAR(120);
    --CAMBIAR TAMAÑO DE COMENTARIO Y AGREGAR CAMBPO ESTADO EN VALROES DE REFERENCIA
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab48' AND column_name='lab48c17')
    THEN
        ALTER TABLE lab48 ALTER COLUMN lab48c9 TYPE VARCHAR(2050);
        ALTER TABLE lab48 ADD lab48c17 INTEGER NOT NULL DEFAULT 1;
    END IF;
    --MOFICACION: CAMBIAR TAMAÑO DE CAMPO NOMBRE DE EXAMEN
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c4' AND character_maximum_length=150)
    THEN
        ALTER TABLE lab39 ALTER COLUMN lab39c4 TYPE VARCHAR(150);
    END IF;
    --MOFICACION: CAMBIAR TAMAÑO DE CAMPO TITULO ESTADISTICO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c21' AND CHARACTER_MAXIMUM_LENGTH=150)
    THEN
        ALTER TABLE lab39 ALTER COLUMN lab39c21 TYPE VARCHAR(150);
    END IF;
    --MOFICACION: CAMBIAR TAMAÑO DE CAMPO NOMBRE DE LABORATORIO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab40' AND column_name='lab40c3' AND CHARACTER_MAXIMUM_LENGTH=150)
    THEN
        ALTER TABLE lab40 ALTER COLUMN lab40c3 TYPE VARCHAR(150);
    END IF;
    --MOFICACION: CAMBIAR TAMAÑO DE CAMPO NOMBRE DE COTIZACION
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab101' AND column_name='lab101c2' AND CHARACTER_MAXIMUM_LENGTH=150)
    THEN
        ALTER TABLE lab101 ALTER COLUMN lab101c2 TYPE VARCHAR(150);
    END IF;
    --AGREGA: SE AGREGA CAMPO PARA ALMACENAR ULTIMO RESULTADO REPETIDO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c48')
    THEN
        ALTER TABLE lab57 ADD lab57c48 VARCHAR(16);
    END IF;
    --AGREGA: SE AGREGA CAMPO PARA ALMACENAR EL DESTINO INICIAL
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c25')
    THEN
        ALTER TABLE lab04 ADD lab04c25 INTEGER;
    END IF;
    --AGREGA: FECHA DE ULTIMO INGRESO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c26')
    THEN
        ALTER TABLE lab04 ADD lab04c26 timestamp DEFAULT now();
    END IF;
    IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'lab116' AND COLUMN_NAME = 'lab116c3' AND DATA_TYPE = 'timestamp without time zone')
    THEN
        ALTER TABLE lab116 ALTER COLUMN lab116c3 TYPE  timestamp;
        ALTER TABLE lab116 ALTER COLUMN lab116c4 TYPE  timestamp;
    END IF;
    --Historico de contraseña
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c27')
    THEN
        ALTER TABLE lab04 ADD lab04c27 VARCHAR(256);
        ALTER TABLE lab04 ADD lab04c28 VARCHAR(256);
    END IF;
    --CONTADOR DE INTENTOS DE AUTENTICACION
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c29')
    THEN
        ALTER TABLE lab04 ADD lab04c29 INTEGER NOT NULL DEFAULT 0;
    END IF;
    --POSICION DE UNA MUESTRA EN UNA GRADILLA
    IF EXISTS (SELECT 1 FROM information_schema.KEY_COLUMN_USAGE where constraint_name='lab11_pk' AND column_name='lab11c1')
    THEN
        ALTER TABLE lab11 DROP constraint lab11_pk;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab11' AND column_name='lab11c1')
    THEN
        ALTER TABLE lab11 ALTER COLUMN lab11c1 TYPE VARCHAR(8);
    END IF;

    --Demografico consulta web
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab181')
    THEN
       CREATE TABLE lab181 (
            lab181c1 serial NOT NULL,
            lab181c2 character varying(50) NULL,
            lab181c3 character varying(32) NOT NULL,
            lab181c4 timestamp without time zone NOT NULL,
            lab181c5 timestamp without time zone,
            lab181c6 smallint NOT NULL,
            lab181c7 integer NOT NULL,
            lab181c8 integer NOT NULL,
            lab181c9 smallint NOT NULL,
            lab181c10 character varying(32),
            lab181c11 character varying(32),
            lab181c12 timestamp without time zone
        );
        ALTER TABLE lab181 ADD CONSTRAINT lab181_pkey PRIMARY KEY (lab181c1);
        END IF;
    --CREACION DE NUEVA TABLA -> LAB 182
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab182')
    THEN
        CREATE TABLE lab182 (
            lab05c1  INTEGER NOT NULL,
            lab40c1  INTEGER NOT NULL
            
        );
    ALTER TABLE lab182 ADD CONSTRAINT lab182_pk PRIMARY KEY ( lab05c1, lab40c1 );
    END IF; 
    -- CODIGO CENTRAL:
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name = 'lab57' AND column_name='lab57c49')
    THEN
        ALTER TABLE lab57 ADD lab57c49 VARCHAR(50);
    END IF;
    --Notación Obligatoria en referencia de valores
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab48' AND column_name='lab48c18')
    THEN
        ALTER TABLE lab48 ADD lab48c18 INTEGER;     
    END IF;

    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name = 'lab181' AND column_name='lab181c12')
    THEN
        ALTER TABLE lab181 ALTER COLUMN lab181c12 TYPE timestamp without time zone;
    END IF;

    --Toma de muestra hospitalaria en servicios de laboratorio
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab10' AND column_name='lab10c8')
    THEN
        ALTER TABLE lab10 ADD lab10c8 INTEGER;     
    END IF;
    --Alarma por prioridad en servicios de laboratorio
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab10' AND column_name='lab10c9')
    THEN
        ALTER TABLE lab10 ADD lab10c9 INTEGER;     
    END IF;
    --Envio al HIS y Fecha de envio al HIS - lab57c50 y lab57c51 (respectivamente)
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c50')
    THEN
        ALTER TABLE Lab57 ADD COLUMN lab57c50 INTEGER NOT NULL DEFAULT 0;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c51')
    THEN
        ALTER TABLE lab57 ADD lab57c51 TIMESTAMP NULL;
    END IF;
    --Id del motivo de estado pendiente en los resultados
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c52')
    THEN
        ALTER TABLE lab57 ADD lab57c52 INTEGER;     
    END IF;
    -- Reutilización de la gradilla -> lab16c9
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab16' AND column_name='lab16c9')
    THEN
        ALTER TABLE lab16 ADD COLUMN lab16c9 INTEGER NOT NULL DEFAULT 0;
    END IF;
    -- Lab183 -> Auditoria de la gradilla
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab183')
    THEN
       CREATE TABLE lab183 (
            lab183c1 VARCHAR(10) NOT NULL,
            lab16c1 INTEGER NOT NULL,
            lab24c1 INTEGER NOT NULL,
            lab22c1 BIGINT NOT NULL,
            lab183c2 timestamp without time zone NOT NULL,
            lab183c3 timestamp without time zone,
            lab183c4 SMALLINT NOT NULL,
            lab04c1 INTEGER NOT NULL,
            lab04c1_1 INTEGER,
            lab183c5 timestamp without time zone,
            lab27c1 INTEGER
        );
    END IF;
    
    -- campo es Encriptación del reporte de resultados
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c32')
    THEN
        ALTER TABLE lab14 ADD lab14c32 SMALLINT DEFAULT 0;     
    END IF;
    --SEGMENT
    --ENVIO LIH
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab22c14')
    THEN
        ALTER TABLE lab22 ADD lab22c14 SMALLINT DEFAULT 0;     
    END IF;
    -- lab185 -> ENCRIPTACION DE REPORTE POR DEMOGRAFICO
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'lab185')
    THEN
        CREATE TABLE lab185 (
            lab185c1 INTEGER NOT NULL,
            lab185c2 INTEGER NOT NULL,
            lab185c3 INTEGER DEFAULT 0 NOT NULL
        );
    END IF;
    --Entrevista Destino
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab184')
    THEN
        CREATE TABLE lab184 (
            lab44c1  INTEGER NOT NULL,
            lab53c1  INTEGER NOT NULL
        );
         ALTER TABLE lab184 ADD CONSTRAINT lab184_pk PRIMARY KEY ( lab44c1, lab53c1 );
    END IF;
    --Modificacion de la tabla entrevista-orden para registrar destino y sede
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab23' AND column_name='lab53c1')
    THEN      
         ALTER TABLE lab23 ADD lab53c1  INTEGER  NULL;
         ALTER TABLE lab23 ADD lab05c1  INTEGER  NULL;
    END IF;

    -- Lab186 -> Analizadores por destinos de microbiologia
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab186')
    THEN      
        CREATE TABLE lab186 (
            lab207c1 INTEGER NOT NULL,
            lab04c1 INTEGER NOT NULL
        );
    END IF;
    --Modificacion de la tabla Usuario adicionando laboratorio de referencia
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c30')
    THEN      
         ALTER TABLE lab04 ADD lab04c30 INTEGER  NULL;         
    END IF;
    --Modificacion de la tabla Entrevista-Orden adicionando la muestra
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab23' AND column_name='lab24c1')
    THEN      
         ALTER TABLE lab23 ADD lab24c1 INTEGER  NULL;         
    END IF;
    --Modificacion de la tabla Valores de Referencia adicionando id del usuario analizador
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab48' AND column_name='lab48c19')
    THEN      
         ALTER TABLE lab48 ADD lab48c19 INTEGER  NULL;         
    END IF;
    -- Lab108 -> Relacion Sede-Item Demografico
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab108')
    THEN      
        CREATE TABLE lab108 (
            lab05c1 INTEGER NOT NULL,
            lab62c1 INTEGER NOT NULL,
            lab63c1 INTEGER NOT NULL
           );
    END IF; 

    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab187')
    THEN
        CREATE TABLE lab187 (
            lab22c1  BIGINT NOT NULL,
            lab187c1 TEXT NOT NULL,
            lab187c2 timestamp without time zone NOT NULL
        );
    END IF;
   -- Lab109 -> Relacion Sede-Demografico
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab109')
    THEN      
        CREATE TABLE lab109 (
            lab05c1 INTEGER NOT NULL,
            lab62c1 INTEGER NOT NULL  
          );          
    END IF; 
   --Modificacion de la tabla Demograficos con campo valor por defecto requerido
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab62' AND column_name='lab62c14')
    THEN      
         ALTER TABLE lab62 ADD lab62c14 VARCHAR(64);     
    END IF;
    --Modificacion de la tabla muestra para campos de temperatura
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab24' AND column_name='lab24c15')
    THEN      
         ALTER TABLE lab24 ADD lab24c15  INTEGER  NULL;
         ALTER TABLE lab24 ADD lab24c16  INTEGER  NULL;
    END IF;
    --Modificacion de la tabla estado de la muestra para campo de temperatura
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab159' AND column_name='lab159c3')
    THEN      
         ALTER TABLE lab159 ADD lab159c3  VARCHAR(64);           
    END IF;
    --Modificacion de la tabla examen lab39 adicion de campo consentimiendo informado
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c52')
    THEN      
         ALTER TABLE lab39 ADD lab39c52 SMALLINT DEFAULT 0; 
    END IF; 
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab24' AND column_name='lab24c15')
    THEN
        ALTER TABLE LAB24 ALTER COLUMN lab24c15 TYPE FLOAT(5);
        ALTER TABLE LAB24 ALTER COLUMN lab24c16 TYPE FLOAT(5);
    END IF; 
    
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab159' AND column_name='lab159c3')
    THEN      
         ALTER TABLE lab159 ALTER COLUMN lab159c3  TYPE FLOAT(5) USING (lab159c3::float);           
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.KEY_COLUMN_USAGE where constraint_name='lab159_pk')
    THEN
        ALTER TABLE lab159 DROP constraint lab159_pk;
        ALTER TABLE lab159 ADD CONSTRAINT lab159_pk PRIMARY KEY ( lab24c1,lab22c1,lab05c1 );   
    END IF;
    --Modificacion de la tabla entrevista lab44 adicion de campo consentimiendo informado
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab44' AND column_name='lab44c6')
    THEN      
         ALTER TABLE lab44 ADD lab44c6 SMALLINT DEFAULT 0; 
    END IF; 
    --Creación de la tabla entrevista lab188 consentimiendo informado
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab188')
    THEN
        CREATE TABLE lab188 (
            lab22c1 BIGINT NOT NULL,
            lab39c1 INTEGER NOT NULL,
            lab188c1 BYTEA,
            lab04c1 INTEGER NOT NULL,
            lab188C2 TIMESTAMP
        );
    END IF;
     --Modificacion de la tabla Sedes lab05 adicion de campo URL de conexion
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab05' AND column_name='lab05c13')
    THEN      
         ALTER TABLE lab05 ADD lab05c13 VARCHAR(128);
    END IF;
    --Creación de la tabla Dependencia de Demoraficos lab190
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab190')
    THEN
        CREATE TABLE lab190 (
            lab190c1 INTEGER NOT NULL,
            lab190c2 INTEGER NOT NULL,
            lab190c3 INTEGER NOT NULL,
            lab190c4 INTEGER NOT NULL           
        );
    END IF;
    
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c16')
    THEN      
        ALTER TABLE lab57 ALTER COLUMN lab57c16 TYPE INTEGER;
    END IF;
    -- ORDENES ENVIADAS A UN SISTEMA EXTERNO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab191')
    THEN
        CREATE TABLE lab191 
        (
            lab118c1 INTEGER NOT NULL,
            lab39c1 INTEGER NOT NULL,
            lab191c1 VARCHAR(20),
            lab22c1 BIGINT NOT NULL,          
            lab191c2 SMALLINT DEFAULT 0 NOT NULL,           
            lab191c3 TIMESTAMP           
        );
    END IF;
    --Modificacion de la tabla muestra para campo de muestra tapada
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab24' AND column_name='lab24c17')
    THEN      
         ALTER TABLE lab24 ADD lab24c17  smallint  NULL;        
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab05' AND column_name='lab05c10')
    THEN      
        ALTER TABLE lab05 ALTER COLUMN lab05c10 TYPE VARCHAR(10);
    END IF;
    --SABER SI EL USUARIO TIENE PERMISO O NO EN EL TABLERO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c31')
    THEN
        ALTER TABLE lab04 ADD lab04c31 smallint NOT NULL DEFAULT 0;
    END IF;
    -- MODIFICACION DEL TAMAÑO DE LA CONTRASEÑA PARA LOS DIFERENTES TIPOS DE USUARIO
    -- MEDICO
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab19' AND column_name='lab19c18')
    THEN      
        ALTER TABLE lab19 ALTER COLUMN lab19c18 TYPE VARCHAR(150);
    END IF;
    -- PACIENTE
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c19')
    THEN      
        ALTER TABLE lab21 ALTER COLUMN lab21c19 TYPE VARCHAR(150);
    END IF;
    -- CLIENTE
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c25')
    THEN      
        ALTER TABLE lab14 ALTER COLUMN lab14c25 TYPE VARCHAR(150);
    END IF;
    -- EL EXAMEN ES UNA LICUOTA
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c53')
    THEN      
        ALTER TABLE lab39 ADD lab39c53 SMALLINT NOT NULL DEFAULT 0;
    END IF;
    --Impresion por servicio - ACTUALIZACION CAMPO SERIAL
    IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab107' AND column_name = 'lab106c1')
    THEN
       ALTER TABLE lab107 ALTER COLUMN lab106c1 TYPE VARCHAR(100);
    END IF;
    -- CORREOS ALTERNATIVOS (MEDICO)
    IF NOT EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab19' AND column_name = 'lab19c24')
    THEN
       ALTER TABLE lab19 ADD lab19c24 VARCHAR(640);
    END IF;
    -- CAMBIO DE TAMAÑO SOBRE EL RESULTADO
    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c1')
    THEN
       ALTER TABLE lab57 ALTER COLUMN lab57c1 TYPE VARCHAR(600);
    END IF;
    -- CAMBIO DE TAMAÑO SOBRE EL PENULTIMO RESULTADO 
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c30')
    THEN
        ALTER TABLE lab57 ALTER COLUMN lab57c30 TYPE VARCHAR(600);
    END IF;
    -- CAMBIO DE TAMAÑO SOBRE EL ULTIMO RESULTADO
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c48')
    THEN
        ALTER TABLE lab57 ALTER COLUMN lab57c48 TYPE VARCHAR(600);
    END IF;
    -- CLIENTES
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c28')
    THEN        
        ALTER TABLE lab14 ADD lab14c28 SMALLINT DEFAULT 0;
        ALTER TABLE lab14 ADD lab14c29 SMALLINT DEFAULT 0;      
    END IF;    
    --ADICION DE CAMPO DE SISTEMA CENTRAL
     IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab118c1')
    THEN        
       ALTER TABLE lab14 ADD lab118c1 SMALLINT DEFAULT 0;    
    END IF; 
     -- ENTIDAD - Regimen Fiscal
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c10')
    THEN
        ALTER TABLE lab906 ADD lab906c10 VARCHAR(64);
    END IF;
    -- ENTIDAD - Número actual
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c11')
    THEN
        ALTER TABLE lab906 ADD lab906c11 INTEGER;
    END IF;
    -- ENTIDAD - Estado (Departamento)
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c12')
    THEN
        ALTER TABLE lab906 ADD lab906c12 INTEGER;
    END IF;
    -- ENTIDAD - Municipio
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c13')
    THEN
        ALTER TABLE lab906 ADD lab906c13 INTEGER;
    END IF;
    -- ENTIDAD - Nombre factura electronica
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c14')
    THEN
        ALTER TABLE lab906 ADD lab906c14 VARCHAR(64);
    END IF;
    -- ENTIDAD - Telefono facturación electronica
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c15')
    THEN
        ALTER TABLE lab906 ADD lab906c15 VARCHAR(20);
    END IF;
    -- ENTIDAD - Llave privada
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c16')
    THEN
        ALTER TABLE lab906 ADD lab906c16 TEXT;
    END IF;
    -- ENTIDAD - Certificado
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c17')
    THEN
        ALTER TABLE lab906 ADD lab906c17 VARCHAR(64);
    END IF;
    -- ENTIDAD - Contraseña
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c18')
    THEN
        ALTER TABLE lab906 ADD lab906c18 VARCHAR(128);
    END IF;
    -- FACTURA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab909')
    THEN
        CREATE TABLE lab909 (
           lab909c1 BIGINT NOT NULL,
           lab07c1 SMALLINT NOT NULL DEFAULT 0
        );
        ALTER TABLE lab909 ADD CONSTRAINT lab909_pk PRIMARY KEY ( lab909c1 );
    END IF;
    -- ID DE LA FACTURA EN LA CABECERA DE LA CAJA
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab909c1')
    THEN
        ALTER TABLE lab902 ADD lab909c1 BIGINT;
    END IF;
    -- ID DE LA ENTIDAD EN LA CABECERA DE LA CAJA
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab906c1')
    THEN
        ALTER TABLE lab902 ADD lab906c1 INTEGER;
    END IF;
    -- DILUCIÓN
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c53')
    THEN
        ALTER TABLE lab57 ADD lab57c53 SMALLINT NOT NULL DEFAULT 0;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c8')
    THEN
        ALTER TABLE lab21 ALTER COLUMN lab21c8 TYPE VARCHAR(700);
    END IF;
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab40' AND column_name='lab05c1')
    THEN
        ALTER TABLE lab40 ADD lab05c1 INTEGER;
    END IF;
    -- MODIFICACIÓN DE TAMAÑO DE LOS CAMPOS DE LOS NOMBRES DEL PACIENTE
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c3')
    THEN
        ALTER TABLE lab21 ALTER COLUMN lab21c3 TYPE VARCHAR(600);
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c4')
    THEN
        ALTER TABLE lab21 ALTER COLUMN lab21c4 TYPE VARCHAR(600);
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c5')
    THEN
        ALTER TABLE lab21 ALTER COLUMN lab21c5 TYPE VARCHAR(600);
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c6')
    THEN
        ALTER TABLE lab21 ALTER COLUMN lab21c6 TYPE VARCHAR(600);
    END IF;
    -- MODIFICACIÓN DEL CAMPO DE USUARIO CONSULTA WEB
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c18')
    THEN
        ALTER TABLE lab21 ALTER COLUMN lab21c18 TYPE VARCHAR(600);
    END IF;
    -- MODIFICACIÓN DE TAMAÑO DEL CAMPO DE NUMERO DE DOCUMENTO DEL PACIENTE
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c2')
    THEN
        ALTER TABLE lab21 ALTER COLUMN lab21c2 TYPE VARCHAR(600);
    END IF;
    -- MODIFICACIÓN DE TAMAÑO DEL CAMPO DE TELEFONO DEL PACIENTE
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c16')
    THEN
        ALTER TABLE lab21 ALTER COLUMN lab21c16 TYPE VARCHAR(50);
    END IF;
    --Configuración RIPS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab980')
    THEN
        CREATE TABLE lab980 (
            lab980c1   VARCHAR(64) NOT NULL,
            lab980c2   TEXT,
            lab980c3   INTEGER,
            lab980c4   VARCHAR(120)
        );
        ALTER TABLE lab980 ADD CONSTRAINT lab980_pk PRIMARY KEY ( lab980c1 );
    END IF;
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab54' AND column_name='lab54c5')
    THEN
        ALTER TABLE lab54 ADD lab54c5 INTEGER NULL;
    END IF;
    -- ELIMINACION DE CAMPOS DE LA ENTREVISTA
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab23' AND column_name='lab44c3')
    THEN
        ALTER TABLE lab23 DROP COLUMN lab44c3;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab23' AND column_name='lab66c1')
    THEN
        ALTER TABLE lab23 DROP COLUMN lab66c1;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c6')
    THEN
        ALTER TABLE lab57 ALTER COLUMN lab57c6 TYPE VARCHAR(600);
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab50' AND column_name='lab50c2')
    THEN
        ALTER TABLE lab50 ALTER COLUMN lab50c2 TYPE VARCHAR(256);
    END IF;
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab03' AND column_name='lab03c7')
    THEN
        ALTER TABLE lab03 ADD lab03c7 INTEGER NULL;
    END IF;
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab03' AND column_name='lab03c8')
    THEN
        ALTER TABLE lab03 ADD lab03c8 VARCHAR(600);
    END IF;
    -- TIPO DE IMPUESTO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab910')
    THEN
        CREATE TABLE lab910 (
            lab910c1 SERIAL NOT NULL,
            lab910c2 VARCHAR(10) NOT NULL,
            lab910c3 VARCHAR(100) NOT NULL,
            lab910c4 DOUBLE PRECISION NOT NULL,
            lab07c1 SMALLINT NOT NULL,
            lab04c1 INTEGER NOT NULL,
            lab910c5 TIMESTAMP NOT NULL,
            lab04c1_2 INTEGER,
            lab910c6 TIMESTAMP
        );
        ALTER TABLE lab910 ADD CONSTRAINT lab910_pk PRIMARY KEY ( lab910c1 );
    END IF;
    -- IMPUESTOS POR CLIENTE
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab911')
    THEN
        CREATE TABLE lab911 (
            lab14c1 INTEGER NOT NULL,
            lab910c1 INTEGER NOT NULL,
            lab911c1 TIMESTAMP NOT NULL
        );
    END IF;
    -- NUEVA TABLA DE ALMACENAMIENTO DE USUARIOS-ESTADO BANDA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab192')
    THEN
        CREATE TABLE lab192 
        (
            lab192c1 SERIAL NOT NULL,
            lab192c2 INTEGER,    
            lab192c3 TIMESTAMP,
            lab192c4 INTEGER,
            lab192c5 INTEGER,          
            lab192c6 VARCHAR(256)         
        );
    ALTER TABLE lab192 ADD CONSTRAINT lab192_pk PRIMARY KEY ( lab192c1 );
    END IF;
    -- NUEVA TABLA DE IMPUESTO POR FACTURA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab916')
    THEN
        CREATE TABLE lab916 
        (
            lab916c1 SERIAL NOT NULL,
            lab916c2 INTEGER,    
            lab916c3 INTEGER,
            lab916c4 FLOAT       
        );
    ALTER TABLE lab916 ADD CONSTRAINT lab916_pk PRIMARY KEY ( lab916c1 );
    END IF;
    -- ADICION DE PORCENTAJE DEL PACIENTE EN PRECIOS POR TARIFA
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab55' AND column_name='lab55c2')
    THEN
        ALTER TABLE lab55 ADD lab55c2 DOUBLE PRECISION NOT NULL DEFAULT 0;
    END IF;
    -- ADICION DE PORCENTAJE DEL PACIENTE EN VIGENCIA POR TARIFA
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab120' AND column_name='lab120c2')
    THEN
        ALTER TABLE lab120 ADD lab120c2 DOUBLE PRECISION NOT NULL DEFAULT 0;
    END IF;
    -- FACTURA
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901')
    THEN
        CREATE TABLE lab901 (
            lab901c1 SERIAL NOT NULL,
            lab901c2 VARCHAR(16),
            lab901c3 DOUBLE PRECISION,
            lab901c4 DOUBLE PRECISION,
            lab901c5 TIMESTAMP,
            lab907c1 INTEGER,
            lab14c1 INTEGER,
            lab63c1 INTEGER,
            lab04c1 INTEGER,
            lab901c6 DOUBLE PRECISION,
            lab901c7 BIGINT NOT NULL,
            lab901c8 BIGINT NOT NULL,
            lab901c9 SMALLINT,
            lab901c10 SMALLINT NOT NULL,
            lab906c1 INTEGER,
            lab901c11 SMALLINT NOT NULL,
            lab901c12 DOUBLE PRECISION,
            lab909c1 BIGINT,
            lab901c13 VARCHAR(32),
            lab901c14 TEXT,
            lab901c15 DOUBLE PRECISION NOT NULL,
            lab901c16 DOUBLE PRECISION NOT NULL,
            lab901c17 DOUBLE PRECISION NOT NULL,
            lab901c19 SMALLINT,
            lab901c20 DOUBLE PRECISION,
            lab901c21 BIGINT,
            lab901c22 VARCHAR(128),
            lab901c23 SMALLINT,
            lab14c2 VARCHAR(64),
            lab14c3 VARCHAR(64),
            lab14c4 VARCHAR(20),
            lab14c14 VARCHAR(128),
            lab14c17 VARCHAR(64),
            lab14c37 VARCHAR(60),
            lab14c38 BYTEA,
            lab901c24 TIMESTAMP,
            lab901c25 SMALLINT
        );
        ALTER TABLE lab901 ADD CONSTRAINT lab901_pk PRIMARY KEY ( lab901c1 );
    END IF;
    -- ABONOS POSTERIORES
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab909')
    THEN
        CREATE TABLE lab909 (
            lab909c1 SERIAL NOT NULL,
            lab901c1 BIGINT,
            lab909c2 DOUBLE PRECISION,
            lab909c3 DOUBLE PRECISION,
            lab909c4 DOUBLE PRECISION,
            lab909c5 TIMESTAMP,
            lab909c6 SMALLINT,
            lab909c7 VARCHAR(64),
            lab59c1 BIGINT,
            lab110c1 INTEGER,
            lab909c8 SMALLINT,
            lab909c9 TEXT
        );
        ALTER TABLE lab909 ADD CONSTRAINT lab909_pk PRIMARY KEY ( lab909c1 );
    END IF;
    --IDENTIFICADOR PARA SABER SI LA  ENTIDAD ES PARA PARTICULARES O NO 
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c19')
    THEN
        ALTER TABLE lab906 ADD lab906c19 INTEGER;
    END IF;
    -- ORDENES FACTURADAS POR CAPITA
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab912')
    THEN
        CREATE TABLE lab912 (
            lab912c1 SERIAL NOT NULL,
            lab912c2 VARCHAR(6) NOT NULL,
            lab912c3 TIMESTAMP NOT NULL,
            lab14c1 INTEGER NOT NULL,
            lab912c4 INTEGER NOT NULL,
            lab901c1 BIGINT NOT NULL
        );
        ALTER TABLE lab912 ADD CONSTRAINT lab912_pk PRIMARY KEY ( lab912c1 );
    END IF;
    -- IMPRESORAS FISCALES
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab914')
    THEN
        CREATE TABLE lab914 (
            lab914c1 SERIAL NOT NULL,
            lab914c6 VARCHAR(64) NOT NULL, --codigo
            lab914c2 VARCHAR(64) NOT NULL, --nombre
            lab914c3 VARCHAR(256) NOT NULL, --url 
            lab914c4 VARCHAR(32) NOT NULL, --ip
            lab07c1  smallint NOT NULL, --estadp
            lab914c5   timestamp, --fecha
            lab04c1   INTEGER --usuario
        );
    END IF;
    -- ADICIÓN DE CODIGO EN TIPOS DE PAGO
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab01' AND column_name='lab01c8')
    THEN
        ALTER TABLE lab01 ADD lab01c8 VARCHAR(50);
    END IF;
    -- NOTA DE CREDITO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab913')
    THEN
        CREATE TABLE lab913 (
            lab913c1 SERIAL NOT NULL,
            lab901c1 BIGINT NOT NULL,
            lab913c2 INTEGER NOT NULL,
            lab913c3 TIMESTAMP NOT NULL,
            lab04c1 INTEGER NOT NULL,
            lab913c4 VARCHAR(200) NOT NULL,
            lab913c5 DOUBLE PRECISION NOT NULL
        );
        ALTER TABLE lab913 ADD CONSTRAINT lab913_pk PRIMARY KEY ( lab913c1 );
    END IF;
    -- DETALLE NOTA DE CREDITO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab915')
    THEN
        CREATE TABLE lab915 (
            lab913c1 BIGINT NOT NULL,
            lab22c1 BIGINT NOT NULL,
            lab39c1 INTEGER NOT NULL,
            lab21c1 INTEGER NOT NULL,
            lab915c2 DOUBLE PRECISION NOT NULL,
            lab915c3 DOUBLE PRECISION,
            lab915c4 DOUBLE PRECISION,
            lab904c1 INTEGER NOT NULL,
            lab14c1 INTEGER,
            lab05c1 INTEGER
        );
    END IF;
    -- ADICIÓN DEL ID DEL PACIENTE PARA GENERAR LA FACTURA A UN PARTICULAR
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab21c1')
    THEN
        ALTER TABLE lab901 ADD lab21c1 INTEGER;
    END IF;
    -- CAMBIO PARA QUE EL CODIGO DE LA NOTA CREDITO PERMITA NULL
    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab913' AND column_name='lab913c2')
    THEN
        ALTER TABLE lab913 ALTER COLUMN lab913c2 TYPE INTEGER;
    END IF;
    -- ID DE LA FACTURA EN SIIGO
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c26')
    THEN
        ALTER TABLE lab901 ADD lab901c26 VARCHAR(64);
    END IF;
    -- PERIODO DE FACTURACIÓN 
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c27')
    THEN
        ALTER TABLE lab901 ADD lab901c27 VARCHAR(20);
    END IF;
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab03' AND column_name='lab03c9')
    THEN
        ALTER TABLE lab03 ADD lab03c9 INTEGER;
    END IF;
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c21')
    THEN
        ALTER TABLE lab21 ADD lab21c21 timestamp;
    END IF;
    -- SE ADICIONA EL COMENTARIO A LA PLANTILLA DE RESULTADOS
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab146' AND column_name='lab146c4')
    THEN
        ALTER TABLE lab146 ADD lab146c4 VARCHAR(200);
    END IF;
    -- SE ADICIONA EL COMENTARIO GENERAL DEL EXAMEN PARA LAS PLANTILLAS
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c54')
    THEN
        ALTER TABLE lab39 ADD lab39c54 VARCHAR(200);
    END IF;
    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c17' AND data_type = 'character varying' )
    THEN
	ALTER TABLE lab14 drop lab14c17;
	ALTER TABLE lab14 ADD lab14c17 INTEGER;
    END IF;

    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab908' AND column_name='lab908c7')
    THEN
        ALTER TABLE lab908 ADD lab908c7 FLOAT;
        ALTER TABLE lab908 ADD lab908c8 FLOAT;
    END IF;
    -- CONTRATOS
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab990')
    THEN
        CREATE TABLE lab990( --- contratos ---
            lab990c1 SERIAL NOT NULL, --ID CONTRATO--
            lab990c2 VARCHAR(64) NOT NULL,--NOMBRE---
            lab07c1 SMALLINT NOT NULL, -- estado
            lab990c4 DOUBLE PRECISION, --- MONTO MAXIMO--
            lab990c5 DOUBLE PRECISION, --- MONTO ACTUAL--
	    lab990c6 DOUBLE PRECISION, --- MONTO ALARMA--
	    lab990c7 INTEGER, --- CAPITADO--
            lab990c8 INTEGER, --- DESCUENTO--
            lab990c9 BIGINT, --- ANTICIPO--
            lab990c10 INTEGER, --- DIAS DE PAGO EFECTIVO--
            lab990c11 INTEGER, --- DIAS PAGO CREDITO--
            lab990c12 INTEGER, --- PLAZO DE PAGO--
            lab990c13 SMALLINT NOT NULL, --- CUOTA MODERADORA--
            lab990c14 SMALLINT NOT NULL, --- COPAGO--
            lab990c15 VARCHAR(64), --- REGIMEN--
            lab990c16 VARCHAR(64), --- NOMBRE VENDEDOR--
            lab990c17 VARCHAR(64), --- IDENTIFICACION VENDEDOR--
            lab990c18 BYTEA, --- FIRMA--	
            lab990c19 TIMESTAMP NOT NULL, -- fecha creacion
            lab990c20 TIMESTAMP NOT NULL, --- fecha actualizacion
            lab14c1 INTEGER, --- id cliente--
            lab990c21 VARCHAR(64) NOT NULL--CODIGO---
	);
        ALTER TABLE lab990 ADD CONSTRAINT lab990_pk PRIMARY KEY ( lab990c1 );
    END IF;
    -- Contrato x Impuestos
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab992')
    THEN
        CREATE TABLE lab992(-----contrayimpu---
            lab990c1 INTEGER NOT NULL,--id contrato---
            lab910c1 INTEGER NOT NULL --ID impuesto--
	);
    END IF;
    -- Contrato x Tarifas
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab993')
    THEN
        CREATE TABLE lab993(-----contrato tarifa---
            lab990c1 INTEGER NOT NULL,--id contrato---
            lab904c1 INTEGER NOT NULL --ID tarifa--
	);
    END IF;
    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab990' AND column_name='lab990c18')
    THEN
        ALTER TABLE lab990 ALTER COLUMN lab990c18 TYPE BYTEA;
    END IF;
    -- ID ITEMS DEMOGRAFICOS DE LA CIUDAD Y EL DEPARTAMENTO PARA EL CONTRATO
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab990' AND column_name='lab990c22')
    THEN
        ALTER TABLE lab990 ADD lab990c22 INTEGER; -- CIUDAD
        ALTER TABLE lab990 ADD lab990c23 INTEGER; -- DEPARTAMENTO
    END IF;
    -- Eliminación de campos del cliente
    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c7')
    THEN
        ALTER TABLE lab14 DROP COLUMN lab14c7;
        ALTER TABLE lab14 DROP COLUMN lab14c8;
        ALTER TABLE lab14 DROP COLUMN lab14c9;
        ALTER TABLE lab14 DROP COLUMN lab14c10;
    END IF;

    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c36')
    THEN
        ALTER TABLE lab14 DROP COLUMN lab14c36;
    END IF;

    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c37')
    THEN
        ALTER TABLE lab14 DROP COLUMN lab14c37;
    END IF;

    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c38')
    THEN
        ALTER TABLE lab14 DROP COLUMN lab14c38;
    END IF;

    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c39')
    THEN
        ALTER TABLE lab14 DROP COLUMN lab14c39;
    END IF;

    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c40')
    THEN
        ALTER TABLE lab14 DROP COLUMN lab14c40;
    END IF;

    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c41')
    THEN
        ALTER TABLE lab14 DROP COLUMN lab14c41;
    END IF;

    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c42')
    THEN
        ALTER TABLE lab14 DROP COLUMN lab14c42;
    END IF;

    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c43')
    THEN
        ALTER TABLE lab14 DROP COLUMN lab14c43;
    END IF;

    -- Eliminación de campo de tipos de entrega
    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab75' AND column_name='lab75c4')
    THEN
        ALTER TABLE lab75 DROP COLUMN lab75c4;
    END IF;
   
    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab912' AND column_name='lab14c1')
    THEN
        ALTER TABLE lab912 RENAME COLUMN lab14c1 TO lab990c1;
    END IF;   
    -- CLIENTE (CAMPOS FACTURACIÓN EXTERNA)
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c33')
    THEN
        ALTER TABLE lab14 ADD lab14c33 SMALLINT;
    END IF;
    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c33')
    THEN
        ALTER TABLE lab14 ALTER COLUMN lab14c33 TYPE SMALLINT;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c34')
    THEN
        ALTER TABLE lab14 ADD lab14c34 SMALLINT;
    END IF;
    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c34')
    THEN
        ALTER TABLE lab14 ALTER COLUMN lab14c34 TYPE SMALLINT;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c35')
    THEN
        ALTER TABLE lab14 ADD lab14c35 VARCHAR(64);
    END IF;
    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c35')
    THEN
        ALTER TABLE lab14 ALTER COLUMN lab14c35 TYPE VARCHAR(64);
    END IF;

-- CLIENTE (campo uso Regimen Fiscal)
  IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c36')
    THEN
        ALTER TABLE lab14 ADD lab14c36 VARCHAR(64);
    END IF;



    -- CAMPOS ADICIONALES DEL CONTRATO
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab990' AND column_name='lab990c24')
    THEN
        ALTER TABLE lab990 ADD lab990c24 SMALLINT;
    END IF;
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab990' AND column_name='lab990c25')
    THEN
        ALTER TABLE lab990 ADD lab990c25 DOUBLE PRECISION;
    END IF;
    -- IMPUESTO DEL EXAMEN EN LAB900
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab900' AND column_name='lab900c5')
    THEN
        ALTER TABLE lab900 ADD lab900c5 DOUBLE PRECISION;
    END IF;
    -- DESCUENTO DEL EXAMEN EN LAB900
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab900' AND column_name='lab900c6')
    THEN
        ALTER TABLE lab900 ADD lab900c6 DOUBLE PRECISION;
    END IF;

    -- TAMAÑO DE CARACTERES ACEPTADOS PARA EL NÚMERO DE LA FACTURA
    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c2')
    THEN
        ALTER TABLE lab901 ALTER COLUMN lab901c2 TYPE VARCHAR(128);
    END IF;
    -- FECHA DE ATENCIÓN DE LA ORDEN
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab22c15')
    THEN
        ALTER TABLE lab22 ADD lab22c15 TIMESTAMP;
    END IF;
    -- FECHA DE PAGO DE LA FACTURA
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c30')
    THEN
        ALTER TABLE lab901 ADD lab901c30 TIMESTAMP;
    END IF;
    -- PAGADO: 0 - NO, 1 - SI
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c31')
    THEN
        ALTER TABLE lab901 ADD lab901c31 SMALLINT NOT NULL DEFAULT 0;
    END IF;
    -- DESCUENTO DE LA TARIFA POR VALOR
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c12')
    THEN
        ALTER TABLE lab902 ADD lab902c12 DOUBLE PRECISION;
    END IF;
    -- DESCUENTO DE LA TARIFA POR PORCENTAJE
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c13')
    THEN
        ALTER TABLE lab902 ADD lab902c13 DOUBLE PRECISION;
    END IF;
    -- TAMAÑO DE CARACTERES ACEPTADOS PARA EL COMENTARIO DE LA FACTURA
    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c13')
    THEN
        ALTER TABLE lab901 ALTER COLUMN lab901c13 TYPE TEXT;
    END IF;
    -- AUDITORIA PARA LAS OPERACIONES CON FACTURAS
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab917')
    THEN
        CREATE TABLE lab917(
            -- lab901c1 INTEGER NOT NULL,
            lab917c1 VARCHAR(4) NOT NULL,
            lab917c2 TEXT NOT NULL,
            lab917c3 VARCHAR(4) NOT NULL,
            lab04c1 INTEGER NOT NULL,
            lab917c4 TIMESTAMP NOT NULL,
            lab22c1 BIGINT NOT NULL
	);
    END IF;
    -- CAMPO PARA IDENTIFICAR SI ES NECESARIA LA PREVALIDACIÓN DE UN EXAMEN SEGÚN CONFIGURACIÓN DE USUARIO
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c32')
    THEN
        ALTER TABLE lab04 ADD lab04c32 SMALLINT NOT NULL DEFAULT 0;
    END IF;
    -- CAMPO PARA adicionar LA CANTIDAD DE EXAMENES DE LA COTIZACION
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab102' AND column_name='lab102c5')
    THEN
        ALTER TABLE lab102 ADD lab102c5 FLOAT NOT NULL DEFAULT 0;
    END IF;
    IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab102' AND column_name='lab102c5')
    THEN
        ALTER TABLE lab102 ALTER COLUMN lab102c5 TYPE INTEGER;
    END IF;
    -- CAMPO PARA adicionar el impuesto de la cotizacion
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab101' AND column_name='lab101c6')
    THEN
        ALTER TABLE lab101 ADD lab101c6 FLOAT NOT NULL DEFAULT 0;
    END IF;
    -- Campo para identificar el tipo de entrega de un resultado
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab03' AND column_name='lab75c1')
    THEN
        ALTER TABLE lab03 ADD lab75c1 INTEGER;
    END IF;
    -- REMISION
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c54')
    THEN
        ALTER TABLE lab57 ADD lab57c54 SMALLINT DEFAULT 0;
    END IF;

    -- TIPO DE ORDEN EN IMPRESION DE CODIGOS DE BARRAS
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab105' AND column_name='lab105c6')
    THEN
        ALTER TABLE lab105 ADD lab105c6 VARCHAR(200);
    END IF;

    -- FECHA REPORTE A MEDICO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c55')
    THEN
        ALTER TABLE lab57 ADD lab57c55 TIMESTAMP;
    END IF;

    -- Campo para identificar el laboratorio origen de una remision
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab40c1a')
    THEN
        ALTER TABLE lab57 ADD lab40c1a INTEGER;
    END IF;

    -- Campo alarma en ingreso de ordenes
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c55')
    THEN
        ALTER TABLE lab39 ADD lab39c55 INTEGER;
    END IF;

    -- Campo para guardar la temperatura del examen
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c56')
    THEN
        ALTER TABLE lab39 ADD lab39c56 INTEGER;
    END IF;

  --Modificacion de la tabla Sedes lab05 adicion de campo imagen ministerio de salud
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab05' AND column_name='lab05c14')
    THEN      
         ALTER TABLE lab05 ADD lab05c14 VARCHAR;
    END IF;

  --Modificacion de la tabla Vigencia tarifas lab120 adicion de campo ultima actualizacion
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab120' AND column_name='lab120c3')
    THEN      
         ALTER TABLE lab120 ADD lab120c3 TIMESTAMP;
    END IF;

    --Modificacion de la tabla Vigencia tarifas lab120 adicion de campo ultima actualizacion
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab120' AND column_name='lab04c1')
    THEN      
         ALTER TABLE lab120 ADD lab04c1 INTEGER;
    END IF;

    --Modificacion de la tabla demograficos de consulta web lab181 adicion de campo email
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab181' AND column_name='lab181c13')
    THEN      
         ALTER TABLE lab181 ADD lab181c13 VARCHAR(128);
    END IF;

    --Modificacion del campo de contraseña para que acepte nulos
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab181' AND column_name='lab181c3')
    THEN      
       ALTER TABLE lab181 ALTER COLUMN lab181c3 TYPE VARCHAR(32);
    END IF;

    -- EXAMENES POR DEMOGRAFICO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab600')
    THEN
        CREATE TABLE lab600(
            lab600c1 SERIAL NOT NULL,
            lab600c2 INTEGER NOT NULL,
            lab600c3 INTEGER NOT NULL,
            lab600c4 INTEGER,
            lab600c5 INTEGER,
            lab600c6 INTEGER,
            lab600c7 INTEGER,
            lab600c8 TIMESTAMP NOT NULL,
            lab04c1 INTEGER NOT NULL
	);
    END IF;

    -- EXAMENES POR DEMOGRAFICO - PRUEBAS
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab601')
    THEN
        CREATE TABLE lab601(
            lab600c1 INTEGER NOT NULL,
            lab39c1  INTEGER NOT NULL
	);
    END IF;

    --Modificacion de la tabla usuarios se adiciona el campo para validar si la contraseña se va recuperar
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c33')
    THEN      
         ALTER TABLE lab04 ADD lab04c33 smallint;
    END IF;


    --Modificacion de la tabla tipos de entrega se adiciona el campo para Medio por el cual fue impreso el examen
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab167' AND column_name='lab80c1')
    THEN      
         ALTER TABLE lab167 ADD lab80c1 INTEGER;
    END IF;

    --Modificacion de la tabla tipos de entrega se adiciona el examen 
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab167' AND column_name='lab39c1')
    THEN      
         ALTER TABLE lab167 ADD lab39c1 INTEGER;
    END IF;

    --Modificacion de la tabla tipos de entrega se adiciona el campo del perfil padre del examen entregado. 
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab167' AND column_name='lab46c1')
    THEN      
         ALTER TABLE lab167 ADD lab46c1 INTEGER;
    END IF;

    --Eliminacion de la tabla que guardaba el detalle de los examenes entregados
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab168')
    THEN      
        DROP TABLE lab168;
    END IF;

    -- ELIMINACION DE DATOS DE FACTURA
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c6')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab901c6;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c7')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab901c7;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c8')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab901c8;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c15')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab901c15;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c16')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab901c16;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab14c2')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab14c2;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab14c3')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab14c3;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab14c4')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab14c4;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab14c14')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab14c14;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab14c17')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab14c17;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab14c37')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab14c37;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab14c38')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab14c38;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c28')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab901c28;
    END IF;
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c29')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab901c29;
    END IF;

    --Eliminacion de la tabla que guardaba las ordenes por capitado
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab912')
    THEN      
        DROP TABLE lab912;
    END IF;

    -- CABECERAS DE UNA FACTURA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab920')
    THEN
        CREATE TABLE lab920 (
            lab920c1   SERIAL NOT NULL,
            lab14c1    INTEGER NOT NULL, 
            lab901c1   INTEGER NOT NULL, 
            lab990c1   INTEGER NOT NULL, 
            lab920c2   FLOAT NOT NULL, 
            lab920c3   BIGINT NOT NULL, 
            lab920c4   BIGINT NOT NULL,
            lab920c5   FLOAT NOT NULL, 
            lab920c6   FLOAT NOT NULL,
            lab920c7   FLOAT NOT NULL,
            lab920c8   FLOAT NOT NULL , 
            lab920c9   INTEGER NOT NULL, 
            lab920c10  INTEGER NOT NULL
        );
        ALTER TABLE lab920 ADD CONSTRAINT lab920_pk PRIMARY KEY ( lab920c1 );
    END IF;

    --FECHA EN LA QUE SE FACTURARON LAS ORDENES POR CAPITADO EN FORMATO YYYYMM
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab920' AND column_name='lab920c11')
    THEN
        ALTER TABLE lab920 ADD COLUMN lab920c11 VARCHAR(32) NOT NULL DEFAULT '';
    END IF;

    --TOTAL DE LA CABECERA
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab920' AND column_name='lab920c12')
    THEN
        ALTER TABLE lab920 ADD COLUMN lab920c12 FLOAT NOT NULL DEFAULT 0;
    END IF;

    ALTER TABLE lab201 ALTER COLUMN lab201c2 TYPE VARCHAR (100);

    --ORDENAMIENTO DEL AREA
    ALTER TABLE lab43 ALTER COLUMN lab43c2 TYPE INTEGER;

    --ID DE LA CABECERA PARA CLIENTE
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab900' AND column_name='lab920c1')
    THEN
        ALTER TABLE lab900 ADD COLUMN lab920c1 INTEGER;
    END IF;

    --ID DE LA CABECERA HISTORICO
    /*IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab900_2021' AND column_name='lab920c1')
    THEN
        ALTER TABLE lab900_2021 ADD COLUMN lab920c1 INTEGER;
    END IF;*/

    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab14c42')
    THEN
        ALTER TABLE lab901 DROP COLUMN lab14c42;
    END IF;

    --ID DE LA CABECERA PARA PARTICULAR
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab900' AND column_name='lab920c1p')
    THEN
        ALTER TABLE lab900 ADD COLUMN lab920c1p INTEGER;
    END IF;

    --ID DE LA CABECERA HISTORICO PARA PARTICULAR
    /*IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab900_2021' AND column_name='lab920c1p')
    THEN
        ALTER TABLE lab900_2021 ADD COLUMN lab920c1p INTEGER;
    END IF;*/
    
    --Modificacion de la tabla usuarios se adiciona el campo para validar si se muestra el reporte preliminar
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c34')
    THEN      
         ALTER TABLE lab04 ADD lab04c34 smallint;
    END IF;
    --Modificacion de la tabla tipos de entrega se adiciona el campo del perfil padre del examen entregado. 
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab03' AND column_name='lab03c10')
    THEN      
         ALTER TABLE lab03 ADD lab03c10 VARCHAR(64);
    END IF;

    --Adicion de campo para almacenar la fecha estimada de entrega de un resultado
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c56')
    THEN      
         ALTER TABLE lab57 ADD lab57c56 DATE;
    END IF;

    -- Configuracion PRUEBA excluir festivos de dias de procesamiento
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c57')
    THEN
        ALTER TABLE lab39 ADD lab39c57 SMALLINT DEFAULT 0;
    END IF;

    ALTER TABLE lab24 ALTER COLUMN lab24c7 TYPE INTEGER;

    -- CAMPO CLIENTE EN FACTURA
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab14c1')
    THEN
        ALTER TABLE lab901 ADD COLUMN lab14c1 INTEGER;
    END IF;

    -- SE ELIMINA EL CAMPO CLIENTE DE LAS SECCIONES DE LA FACTURA
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab920' AND column_name='lab14c1')
    THEN
        ALTER TABLE lab920 DROP COLUMN lab14c1;
    END IF;

    -- ELIMINACION FIRMA DEL VENDEDOR
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab990' AND column_name='lab990c18')
    THEN
        ALTER TABLE lab990 DROP COLUMN lab990c18;
    END IF;

    -- CAMPO IDIOMA INGLES AREAS
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab43' AND column_name='lab43c9')
    THEN
        ALTER TABLE lab43 ADD COLUMN lab43c9 VARCHAR(64);
    END IF;

    -- CAMPO IDIOMA INGLES EXAMENES
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c58')
    THEN
        ALTER TABLE lab39 ADD COLUMN lab39c58 VARCHAR(64);
    END IF;

    -- CAMPO COMENTARIO FIJO EN INGLES
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c59')
    THEN
        ALTER TABLE lab39 ADD COLUMN lab39c59 VARCHAR(8000);
    END IF;

    -- CAMPO COMENTARIO PRELIMINAR EN INGLES
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c60')
    THEN
        ALTER TABLE lab39 ADD COLUMN lab39c60 VARCHAR(8000);
    END IF;

    -- CAMPO INFORMACION GENERAL EN INGLES
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c61')
    THEN
        ALTER TABLE lab39 ADD COLUMN lab39c61 VARCHAR(8000);
    END IF;

    --Adicion de campo para almacenar la fecha de transporte de la muestra. 
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c57')
    THEN      
        ALTER TABLE lab57 ADD lab57c57 DATE;
    END IF;

    --Adicion de campo para almacenar el usuario que transporta de la muestra. 
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c58')
    THEN      
        ALTER TABLE lab57 ADD lab57c58 INTEGER;
    END IF;
    
    --Adicion de campo para almacenar la fecha de impresion de la muestra. 
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c59')
    THEN      
        ALTER TABLE lab57 ADD lab57c59 DATE;
    END IF;

    --Adicion de campo para almacenar el usuario que imprime la muestra. 
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c60')
    THEN      
        ALTER TABLE lab57 ADD lab57c60 INTEGER;
    END IF;

    -- ESTADO DEL PACIENTE
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c22')
    THEN      
        ALTER TABLE lab21 ADD lab21c22  SMALLINT DEFAULT 0;        
    END IF;
    -- DATOS DEL TRIBUNAL
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab193')
    THEN
        CREATE TABLE lab193 (
            lab21c1  Integer NOT NULL,
            lab193c1 TEXT NOT NULL
        );
    END IF;
    -- FECHA DE MODIFICACION DEL ESTADO DEL PACIENTE
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c23')
    THEN      
        ALTER TABLE lab21 ADD lab21c23 TIMESTAMP;        
    END IF;
    -- MAYOR CAPACIDAD DE CARACTERES PARA LOS CORREOS ASOCIADOS A UNA SEDE
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab05' AND column_name='lab05c12')
    THEN      
        ALTER TABLE lab05 ALTER COLUMN lab05c12 TYPE VARCHAR(600);
    END IF;
    -- DATOS DEL TRIBUNAL - Adición de llave primaria
    /*IF EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab193')
    THEN
        ALTER TABLE lab193 ADD CONSTRAINT lab193_pkey PRIMARY KEY (lab21c1);
    END IF;  */
    
    --Modificacion de la tabla usuarios se adiciona el campo para validar si tiene permisos para editar los examenes en ingreso de ordenes 
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c35')
    THEN      
         ALTER TABLE lab04 ADD lab04c35 smallint;
    END IF;

    --ID DE LA CABECERA HISTORICOS
    /*IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab900_2020' AND column_name='lab920c1')
    THEN
        ALTER TABLE lab900_2020 ADD COLUMN lab920c1 INTEGER;
    END IF;

    --ID DE LA CABECERA HISTORICO PARA PARTICULAR
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab900_2020' AND column_name='lab920c1p')
    THEN
        ALTER TABLE lab900_2020 ADD COLUMN lab920c1p INTEGER;
    END IF;

    --ID DE LA CABECERA HISTORICOS
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab900_2019' AND column_name='lab920c1')
    THEN
        ALTER TABLE lab900_2019 ADD COLUMN lab920c1 INTEGER;
    END IF;

    --ID DE LA CABECERA HISTORICO PARA PARTICULAR
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab900_2019' AND column_name='lab920c1p')
    THEN
        ALTER TABLE lab900_2019 ADD COLUMN lab920c1p INTEGER;
    END IF;

    --ID DE LA CABECERA HISTORICOS
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab900_2018' AND column_name='lab920c1')
    THEN
        ALTER TABLE lab900_2018 ADD COLUMN lab920c1 INTEGER;
    END IF;

    --ID DE LA CABECERA HISTORICO PARA PARTICULAR
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab900_2018' AND column_name='lab920c1p')
    THEN
        ALTER TABLE lab900_2018 ADD COLUMN lab920c1p INTEGER;
    END IF;*/
        
  --Modificacion de la tabla usuarios se adiciona el campo para validar si tiene permisos para editar los examenes de una orden con caja
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c36')
    THEN      
         ALTER TABLE lab04 ADD lab04c36 smallint not null default 0;
    END IF;
  --Modificacion de la tabla usuarios se adiciona el campo para validar si tiene permisos para editar los examenes de una orden con caja
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab44' AND column_name='lab44c7')
    THEN      
        ALTER TABLE lab44 ADD lab44c7 smallint not null default 0;
    END IF;

    --NOTAS INTERNAS DEL RESULTADO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab212')
    THEN
        CREATE TABLE lab212 (
            lab22c1   BIGINT NOT NULL,
            lab39c1   INTEGER NOT NULL,
            lab212c1   TEXT,
            lab212c2   timestamp NOT NULL,
            lab04c1   INTEGER NOT NULL
        );
        ALTER TABLE lab212 ADD CONSTRAINT lab212_pk PRIMARY KEY ( lab22c1, lab39c1 );
    END IF;

    --TIENE COMENTARIO INTERNO - RESULTADO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c61')
    THEN
        ALTER TABLE lab57 ADD lab57c61 SMALLINT NOT NULL DEFAULT 0;
    END IF;
    --RESULTADO ANTERIOR
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c62')
    THEN
        ALTER TABLE lab57 ADD lab57c62 VARCHAR(30);
    END IF;
       --TIENE COMENTARIO Recuentos - Antibiograma
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab204' AND column_name='lab204c4')
    THEN
        ALTER TABLE lab204 ADD lab204c4 VARCHAR;
    END IF;
    --TIENE COMENTARIO Determinaciones - Antibiograma
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab204' AND column_name='lab204c5')
    THEN
        ALTER TABLE lab204 ADD lab204c5 VARCHAR;
    END IF;
    --TIENE COMENTARIO complementaciones - Antibiograma
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab204' AND column_name='lab204c6')
    THEN
        ALTER TABLE lab204 ADD lab204c6 VARCHAR;
    END IF;
  --Modificacion de la tabla Usuario adicionando Demograficos para consultas
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c37')
    THEN
         ALTER TABLE lab04 ADD lab04c37 INTEGER;
    END IF;
    --Modificacion de la tabla Usuario adicionando Item demograficos para consultas
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c38')
    THEN
         ALTER TABLE lab04 ADD lab04c38 INTEGER;
    END IF;
    --AGENTE ETIOLOGICO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab213')
    THEN
        CREATE TABLE lab213 (
            lab213c1   SERIAL NOT NULL,
            lab213c2   SMALLINT NOT NULL,
            lab213c3   VARCHAR(256) NOT NULL,
            lab213c4   VARCHAR(128) NOT NULL,
            lab213c5   SMALLINT NOT NULL,
            lab213c6   TIMESTAMP NOT NULL,
            lab04c1    INTEGER NOT NULL
        );
        ALTER TABLE lab213 ADD CONSTRAINT lab213_pk PRIMARY KEY ( lab213c1 );
    END IF;

----LOGS CLIENTE DE IMPRESION
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'cliprint')
    THEN
        CREATE TABLE cliprint 
        (
            lab22c1 BIGINT NOT NULL , 
            cliprintc1 VARCHAR(800)  , --comentario
            cliprintc2 VARCHAR (300) , --lista correos
            lab05c1 INTEGER NOT NULL ,
            lab05c4   VARCHAR(64)
        );

       ALTER TABLE cliprint  ADD CONSTRAINT cliprint_pk PRIMARY KEY ( lab22c1,lab05c1 );    
    END IF;
--Modificacion de la tabla usuarios se adiciona el campo para validar si tiene permisos para eliminar caja
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c39')
    THEN      
        ALTER TABLE lab04 ADD lab04c39 smallint not null default 0;
    END IF;

    --Modificacion del campo correos a los que se envia los resultados 
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab03' AND column_name='lab03c10')
    THEN      
        ALTER TABLE lab03 ALTER COLUMN lab03c10 TYPE VARCHAR(512); 
    END IF;

    --Modificacion de la tabla caja para validar el tipo de copago 
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c14')
    THEN      
        ALTER TABLE lab902 ADD lab902c14 smallint not null default 0;
    END IF;

    -- COMENTARIO FIJO AL RESULTADO
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c63')
    THEN
        ALTER TABLE lab57 ADD COLUMN lab57c63 TEXT;
    END IF;

    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c62')
    THEN
        ALTER TABLE lab57 ALTER COLUMN lab57c62 TYPE VARCHAR(600);
    END IF;

    -- destino de microbiologia verificado
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c64')
    THEN
        ALTER TABLE lab57 ADD COLUMN lab57c64 int;
    END IF;
   --Modificacion de la tabla caja para validar el tipo de copago 
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c65')
    THEN      
        ALTER TABLE lab57 ADD lab57c65 smallint default 0;
    END IF;

    -- usuario que deja pediente examen
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c66')
    THEN
        ALTER TABLE lab57 ADD COLUMN lab57c66 int;
    END IF;

    -- ELIMINACION DEL CAMPO COMENTARIO DETERMINACIONES
    IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab204' AND column_name='lab204c5')
    THEN
        ALTER TABLE lab204 DROP COLUMN lab204c5;
    END IF;

    -- COMENTARIO DETERMINACIONES
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c67')
    THEN
        ALTER TABLE lab57 ADD COLUMN lab57c67 TEXT;
    END IF;

    -- destino de laboratorio  externo  verificado
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c68')
    THEN
        ALTER TABLE lab57 ADD COLUMN lab57c68 int  ;
    END IF;

   -- se agrega email a item demografico
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab63' AND column_name='lab63c7')
    THEN      
        ALTER TABLE lab63 ADD COLUMN lab63c7 VARCHAR(128);
    END IF;

    --Se le quita el valor por defecto al ultimo ingreso
    --IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c26')
    --THEN
      -- ALTER TABLE lab04 ALTER lab04c26 DROP DEFAULT;
    --END IF;

    -- CARGO DE DOMICILIO
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab101' AND column_name='lab101c7')
    THEN
        ALTER TABLE lab101 ADD lab101c7 FLOAT NOT NULL DEFAULT 0;
    END IF;

    -- MEDICOS AUXILIARES
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab194')
    THEN
        CREATE TABLE lab194 (
            lab22c1    BIGINT NOT NULL,
            lab19c1    INTEGER NOT NULL
        );
    END IF;
    -- se agrega email a tipo de orden
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab103' AND column_name='lab103c6')
    THEN      
        ALTER TABLE lab103 ADD COLUMN lab103c6 VARCHAR(128);
    END IF;

    -- se agrega email a servicio
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab10' AND column_name='lab10c10')
    THEN      
        ALTER TABLE lab10 ADD COLUMN lab10c10 VARCHAR(128);
    END IF;

    -- se agrega email a raza
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab08' AND column_name='lab08c6')
    THEN      
        ALTER TABLE lab08 ADD COLUMN lab08c6 VARCHAR(128);
    END IF;

    -- se agrega email a tipo de documento
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab54' AND column_name='lab54c6')
    THEN      
        ALTER TABLE lab54 ADD COLUMN lab54c6 VARCHAR(128);
    END IF;
  -- Observaciones 
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab22c16')
    THEN
        ALTER TABLE lab22 ADD COLUMN lab22c16 TEXT;
    END IF;

   --Editar observaciones en usuarios
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c40')
    THEN      
         ALTER TABLE lab04 ADD lab04c40 smallint;
    END IF;

    -- CARGO DE DOMICILIO CAJA
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c15')
    THEN
        ALTER TABLE lab902 ADD lab902c15 FLOAT DEFAULT 0;
    END IF;
  -- Ordenamiento de demograficos
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab195')
    THEN
        CREATE TABLE lab195 (
            lab62c1    INTEGER NOT NULL,
            lab195c1   INTEGER,
            lab195c2   VARCHAR(32), 
            lab04c1    INTEGER             
        );
    END IF;
    -- FECHA EN QUE SE CIERRA LA GRADILLA
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab16' AND column_name='lab16c10')
    THEN
        ALTER TABLE lab16 ADD COLUMN lab16c10 TIMESTAMP;
    END IF;

    -- AUDITORIA DE GRADILLA CERRADA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab271')
    THEN
        CREATE TABLE lab271 
        (
            lab16c1 INTEGER NOT NULL , 
            lab271c1 VARCHAR(32), 
            lab31c1 INTEGER ,
            lab04c1 INTEGER ,
            lab271c2 TIMESTAMP , 
            lab27c1 INTEGER
        );

       ALTER TABLE lab271  ADD CONSTRAINT lab271_pk PRIMARY KEY ( lab16c1, lab271c1  );    
    END IF;

    -- CARGO DOMICILIO
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab908' AND column_name='lab908c9')
    THEN
        ALTER TABLE lab908 ADD lab908c9 FLOAT;
    END IF;

    -- EnterpriseNT_CIADE marcar  registros que ya se procesaron
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c69')
    THEN
        ALTER TABLE lab57 ADD COLUMN lab57c69 int;
    END IF;

-- CAMPO examenes CPT para almacenar esos codigos 
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c62')
    THEN
        ALTER TABLE lab39 ADD COLUMN lab39c62 VARCHAR(200);
    END IF;
  --Editar pagos en usuarios
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c41')
    THEN      
         ALTER TABLE lab04 ADD lab04c41 smallint;
    END IF;

    -- Facturado a 
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c16')
    THEN
        ALTER TABLE lab902 ADD lab902c16 VARCHAR(256);
    END IF;

    -- RUC
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c17')
    THEN
        ALTER TABLE lab902 ADD lab902c17 VARCHAR(64);
    END IF;

    --TIPO DE CREDITO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c18')
    THEN      
        ALTER TABLE lab902 ADD lab902c18 SMALLINT;
    END IF;

   --Editar resultados por usuario
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c42')
    THEN      
         ALTER TABLE lab04 ADD lab04c42 smallint;
    END IF;

    --Identifica si la caja ya fue enviada a kbits
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c19')
    THEN      
        ALTER TABLE lab902 ADD lab902c19 SMALLINT;
    END IF;

    --Factura combo en caja
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c20')
    THEN      
        ALTER TABLE lab902 ADD lab902c20 SMALLINT;
    END IF;

    -- FACTURAS COMBO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab930')
    THEN
        CREATE TABLE lab930 (
            lab930c1 SERIAL NOT NULL,
            lab930c2 TIMESTAMP NOT NULL,
            lab04c1 INTEGER NOT NULL, 
            lab930c3 INTEGER NOT NULL,
            lab930c4 VARCHAR(516)
        );
        ALTER TABLE lab930 ADD CONSTRAINT lab930_pk PRIMARY KEY ( lab930c1 );
    END IF;

    -- Factura combo 
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab930c1')
    THEN
        ALTER TABLE lab22 ADD COLUMN lab930c1 INTEGER;
    END IF;

   
    -- NOTAS CREDITO FACTURAS COMBO
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab931')
    THEN
        CREATE TABLE lab931 (
            lab931c1 SERIAL NOT NULL,
            lab930c1 INTEGER NOT NULL, 
            lab931c2 TIMESTAMP NOT NULL,
            lab04c1 INTEGER NOT NULL, 
            lab931c3 VARCHAR(516),
            lab931c4 INTEGER
        );
        ALTER TABLE lab931 ADD CONSTRAINT lab931_pk PRIMARY KEY ( lab931c1 );
    END IF;
   --Modificacion de la tabla Demograficos con campo filtro para hora prometida
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab62' AND column_name='lab62c15')
    THEN      
         ALTER TABLE lab62 ADD lab62c15 smallint;     
    END IF;

     --Modificacion de la tabla de medicos auxiliares
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab194' AND column_name='lab194c1')
    THEN      
         ALTER TABLE lab194 ADD lab194c1 INTEGER;     
    END IF;
 ---- Configuración de la impresion 
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab22c17')
    THEN
        ALTER TABLE lab22 ADD COLUMN lab22c17 TEXT;
    END IF;


    -- CAMPO IDIOMA INGLES RESULTADOS LITERALES
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab50' AND column_name='lab50c4')
    THEN
        ALTER TABLE lab50 ADD COLUMN lab50c4 VARCHAR(64);
    END IF;

    -- Resultado en ingles
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c70')
    THEN
        ALTER TABLE lab57 ADD COLUMN lab57c70 VARCHAR(600);
    END IF;

    -- TELEFONO CAJA
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c21')
    THEN
        ALTER TABLE lab902 ADD lab902c21 VARCHAR(32);
    END IF;

    --TIPO DE INGRESO DE LA ORDEN HIS, MANUAL
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab22c18')
    THEN      
        ALTER TABLE lab22 ADD lab22c18 smallint;
    END IF;

 -- SE ADICION El COMENTARIO DEL RESULTADO 2 AL MAESTRO DE EXAMENES
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c63')
    THEN
        ALTER TABLE lab39 ADD lab39c63 VARCHAR(8000);
    END IF;

-- SE ADICION El COMENTARIO DEL RESULTADO 2
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c71')
    THEN
        ALTER TABLE lab57 ADD COLUMN lab57c71 TEXT;
    END IF;

    -- AUDITORIA DE PAQUETES
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab301')
    THEN
        CREATE TABLE lab301 (
            lab301c1 SERIAL NOT NULL,
            lab301c2 INTEGER NOT NULL,
            lab301c3 VARCHAR(128), 
            lab301c4 INTEGER NOT NULL,
            lab04c1 INTEGER NOT NULL, 
            lab301c5 TIMESTAMP NOT NULL,
            lab22c1 BIGINT NOT NULL
        );
        ALTER TABLE lab301 ADD CONSTRAINT lab301_pk PRIMARY KEY ( lab301c1 );
    END IF;

    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab48' AND column_name='lab48c20')
    THEN
        ALTER TABLE lab48 ADD lab48c20 VARCHAR(2050);
    END IF;

    --Usuario auditoria demografico consulta web
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab181' AND column_name='lab04c1')
    THEN
        ALTER TABLE lab181 ADD COLUMN lab04c1 INTEGER;
    END IF;

    --Fecha ultima actualizacion demografico consulta web
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab181' AND column_name='lab181c15')
    THEN
        ALTER TABLE lab181 ADD COLUMN lab181c15 TIMESTAMP;
    END IF;

    --Usuario auditoria tiempos de oportunidad
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab171' AND column_name='lab04c1')
    THEN
        ALTER TABLE lab171 ADD COLUMN lab04c1 INTEGER;
    END IF;

    --Fecha ultima actualizacion tiempos de oportunidad
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab171' AND column_name='lab171c5')
    THEN
        ALTER TABLE lab171 ADD COLUMN lab171c5 TIMESTAMP;
    END IF;

    --Usuario auditoria procedimiento por prueba
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab157' AND column_name='lab04c1')
    THEN
        ALTER TABLE lab157 ADD COLUMN lab04c1 INTEGER;
    END IF;

    --Fecha ultima actualizacion procedimiento por prueba
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab157' AND column_name='lab157c3')
    THEN
        ALTER TABLE lab157 ADD COLUMN lab157c3 TIMESTAMP;
    END IF;

    --Usuario auditoria excluir pruebas por demograficos
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab104' AND column_name='lab04c1')
    THEN
        ALTER TABLE lab104 ADD COLUMN lab04c1 INTEGER;
    END IF;

    --Fecha ultima actualizacion excluir pruebas por demograficos
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab104' AND column_name='lab104c3')
    THEN
        ALTER TABLE lab104 ADD COLUMN lab104c3 TIMESTAMP;
    END IF;

    --Usuario auditoria pruebas por laboratorio
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab145' AND column_name='lab04c1')
    THEN
        ALTER TABLE lab145 ADD COLUMN lab04c1 INTEGER;
    END IF;

    --Fecha ultima actualizacion pruebas por laboratorio
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab145' AND column_name='lab145c2')
    THEN
        ALTER TABLE lab145 ADD COLUMN lab145c2 TIMESTAMP;
    END IF;

    --Usuario auditoria dependencia de demograficos
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab190' AND column_name='lab04c1')
    THEN
        ALTER TABLE lab190 ADD COLUMN lab04c1 INTEGER;
    END IF;

    --Fecha ultima actualizacion dependencia de demograficos
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab190' AND column_name='lab190c5')
    THEN
        ALTER TABLE lab190 ADD COLUMN lab190c5 TIMESTAMP;
    END IF;

    -- SE ADICIONA VER PAQUETE DESPLEGADO EN TALON
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c64')
    THEN
        ALTER TABLE lab39 ADD lab39c64 INTEGER;
    END IF;

    --adiciona nombre item demografico en ingles
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab63' AND column_name='lab63c8')
    THEN      
        ALTER TABLE lab63 ADD COLUMN lab63c8 VARCHAR(128);
    END IF;

    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab68' AND column_name='lab68c7')
    THEN      
        ALTER TABLE lab68 ADD COLUMN lab68c7 TEXT;
    END IF;

    --indicador para envio a tableros en la validacion del examen
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c72')
    THEN
        ALTER TABLE lab57 ADD lab57c72 SMALLINT;
    END IF;

    -- SALDO COMPAÑIA CAJA
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c22')
    THEN
        ALTER TABLE lab902 ADD lab902c22 FLOAT;
    END IF;

    -- SALDO COMPAÑIA TOTALES
    IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab908' AND column_name='lab908c10')
    THEN
        ALTER TABLE lab908 ADD lab908c10 FLOAT;
    END IF;

    --indicador para envio a tableros en la creacion del examen
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c73')
    THEN
        ALTER TABLE lab57 ADD lab57c73 SMALLINT;
    END IF;

    -- INDICADOR PARA ACTUALIZAR LA CONTRASEÑA DEL PACIENTE
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c24')
    THEN      
        ALTER TABLE lab21 ADD lab21c24 SMALLINT;        
    END IF;

    -- guardado de remisiones
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab500')
    THEN
        CREATE TABLE lab500 (
            lab39c1 INTEGER NOT NULL, 
            lab22c1 BIGINT NOT NULL, 
            lab500c1 SMALLINT,
            lab500c2 INTEGER,
            lab500c3 SMALLINT
        );
        ALTER TABLE lab500 ADD CONSTRAINT lab500_pk PRIMARY KEY ( lab22c1, lab39c1 );
    END IF;
   --Modificacion de la tabla Sedes lab05 adicion de campo imagen ministerio de salud
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab05' AND column_name='lab05c15')
    THEN      
         ALTER TABLE lab05 ADD lab05c15 INTEGER;
    END IF;

    --indicador para cita
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab22c19')
    THEN
        ALTER TABLE lab22 ADD lab22c19 SMALLINT;
    END IF;


    --Plantillas cubo
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab35')
    THEN
        CREATE TABLE lab35 (
            lab35c1     SERIAL NOT NULL,
            lab35c2     VARCHAR(50) NOT NULL,
            lab35c3     VARCHAR(1050),
            lab35c4     VARCHAR(1050),
            lab35c5     VARCHAR(1050),
            lab35c6     timestamp NOT NULL,
            lab35c7     timestamp NOT NULL
        );
        ALTER TABLE lab35 ADD CONSTRAINT lab35_pk PRIMARY KEY ( lab35c1 );
    END IF;

    -- Agrupar por perfiles cubo
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab35' AND column_name='lab35c8')
    THEN      
        ALTER TABLE lab35 ADD COLUMN lab35c8 INTEGER;
    END IF;

    -- Ordenamiento de los demograficos en plantilla cubo
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab35' AND column_name='lab35c9')
    THEN      
        ALTER TABLE lab35 ADD COLUMN lab35c9 VARCHAR(1050);
    END IF;

    ---------- MODULO DE CITAS

    --JORNADA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'hmb09')
    THEN
        CREATE TABLE hmb09 (
            hmb09c1    SERIAL NOT NULL,
            hmb09c2    VARCHAR(64) NOT NULL,
            hmb09c3    VARCHAR(16) NOT NULL,
            hmb09c4    INTEGER NOT NULL,
            hmb09c5    INTEGER NOT NULL,
            hmb09c6    timestamp NOT NULL,
            hmb03c1    INTEGER NOT NULL,
            hmb07c1    smallint NOT NULL
        );

        ALTER TABLE hmb09 ADD CONSTRAINT hmb09_pk PRIMARY KEY ( hmb09c1 );
    END IF;

    --JORNADA - USUARIO
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'hmb10')
    THEN
        CREATE TABLE hmb10 (
            lab05c1   INTEGER NOT NULL,
            hmb09c1   INTEGER NOT NULL,
            hmb10c1   INTEGER NOT NULL
        );

        ALTER TABLE hmb10 ADD CONSTRAINT hmb10_pk PRIMARY KEY ( lab05c1,hmb09c1 );
    END IF;
    --CITA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'hmb12')
    THEN
        CREATE TABLE hmb12 (
            hmb12c1     SERIAL NOT NULL,
            hmb09c1     INTEGER NOT NULL,
            hmb12c2     INTEGER NOT NULL,
            hmb12c3     timestamp NOT NULL,
            lab04c1     INTEGER NOT NULL,
            hmb12c4     INTEGER NOT NULL,
            hmb12c5     BIGINT NOT NULL,
            lab05c1     INTEGER
        );

        ALTER TABLE hmb12 ADD CONSTRAINT hmb12_pk PRIMARY KEY ( hmb12c1 );
    END IF;
    --AGENDAMIENTO CITA - CONCURRENCIA
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'hmb13')
    THEN
        CREATE TABLE hmb13 (
            lab05c1   INTEGER NOT NULL,
            hmb09c1   INTEGER NOT NULL,
            hmb13c1   INTEGER NOT NULL,
            hmb03c1_1   INTEGER NOT NULL,
            hmb13c2   SERIAL NOT NULL
        );
        ALTER TABLE hmb13 ADD CONSTRAINT hmb13_pk PRIMARY KEY ( hmb13c2 );
    END IF;

    --TRAZABILIDAD DE LA CITA (ESTADOS)
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'hmb16')
    THEN
        CREATE TABLE hmb16 (
            hmb12c1   INTEGER NOT NULL,
            hmb16c1   INTEGER NOT NULL,
            hmb30c1   INTEGER,
            hmb16c2   VARCHAR,
            hmb16c3   timestamp NOT NULL,
            hmb03c1   INTEGER NOT NULL,
            hmb03c1_1 INTEGER,
            hmb16c4 INTEGER
        );

    END IF;
    --TRAZABILIDAD ENVIO DE CORREOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab166')
    THEN
        CREATE TABLE lab166 (
            lab166c1    SERIAL NOT NULL,
            lab22c1     BIGINT,
            lab166c2    VARCHAR(1050) NOT NULL,
            lab166c3    TEXT NOT NULL,
            lab166c4    INTEGER NOT NULL, 
            lab166c5    VARCHAR(256) NOT NULL,
            lab04c1     INTEGER NOT NULL,
            lab166c6    TIMESTAMP NOT NULL,
            lab166c7    VARCHAR(256)
        );
        ALTER TABLE lab166 ADD CONSTRAINT lab166_pk PRIMARY KEY ( lab166c1 );
    END IF;

    RETURN 0;
END;    
$$ LANGUAGE plpgsql;
select script();
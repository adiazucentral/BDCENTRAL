--Script para la creacion y actualizacion de la base de datos transaccional de PostgreSQL
CREATE OR REPLACE FUNCTION script() RETURNS integer AS $$
BEGIN
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'sta1')
    THEN
        CREATE TABLE sta1 (
            sta1c1    INTEGER NOT NULL,
            sta1c2    INTEGER,
            sta1c3    VARCHAR(128),
            sta1c4    VARCHAR(128),
            sta1c5    INTEGER,
            sta1c6    VARCHAR(16),
            sta1c7    VARCHAR(64),
            sta1c8    INTEGER,
            sta1c9    VARCHAR(16),
            sta1c10   VARCHAR(64)
        );

        ALTER TABLE sta1 ADD CONSTRAINT sta1_pk PRIMARY KEY ( sta1c1 );
    END IF;

    --Documentos de la Orden
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'sta2')
    THEN
        CREATE TABLE sta2
        (
            sta2c1 BIGINT NOT NULL ,
            sta1c1 INTEGER NOT NULL ,
            sta2c2 INTEGER ,
            sta2c3 VARCHAR (16) ,
            sta2c4 VARCHAR (64) ,
            sta2c5 INTEGER ,
            sta2c6 VARCHAR (16) ,
            sta2c7 VARCHAR (64) ,
            sta2c8 INTEGER ,
            sta2c9 VARCHAR (16) ,
            sta2c10 VARCHAR (64) ,
            sta2c11 INTEGER ,
            sta2c12 VARCHAR (16) ,
            sta2c13 VARCHAR (64) ,
            sta2c14 INTEGER ,
            sta2c15 VARCHAR (16) ,
            sta2c16 VARCHAR (64) ,
            sta2c17 INTEGER ,
            sta2c18 VARCHAR (16) ,
            sta2c19 VARCHAR (64) ,
            sta2c20 INTEGER ,
            sta2c21 TIMESTAMP ,
            sta2c22 INTEGER ,
            sta2c23 INTEGER ,
            sta2c24 FLOAT ,
            sta2c25 INTEGER,
            sta2c26 INTEGER ,
            sta2c27 VARCHAR (16) ,
            sta2c28 VARCHAR (64)
        );

        CREATE INDEX sta2__IDX ON sta2 (sta2c20 ASC);
        ALTER TABLE sta2 ADD CONSTRAINT sta2_pk PRIMARY KEY ( sta2c1 );
    END IF;

    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'sta3')
    THEN
        CREATE TABLE sta3 (
            sta3c1 INTEGER NOT NULL ,
            sta3c2 VARCHAR (16) ,
            sta3c3 VARCHAR (64) ,
            sta3c4 INTEGER ,
            sta3c5 VARCHAR (16) ,
            sta3c6 VARCHAR (64) ,
            sta3c7 INTEGER ,
            sta3c8 VARCHAR (16) ,
            sta3c9 VARCHAR (64) ,
            sta3c10 INTEGER ,
            sta3c11 INTEGER ,
            sta3c12 INTEGER ,
            sta3c13 INTEGER ,
            sta3c14 INTEGER ,
            sta2c1 BIGINT NOT NULL ,
            sta3c15 INTEGER ,
            sta3c16 INTEGER ,
            sta3c17 INTEGER ,
            sta3c18 VARCHAR (16) ,
            sta3c19 VARCHAR (64) ,
            sta3c20 FLOAT ,
            sta3c21 FLOAT ,
            sta3c22 FLOAT ,
            sta3c23 smallint,
            sta3c24 INTEGER NOT NULL,
            sta3c25 smallint
        );

        CREATE INDEX sta3__IDX ON sta3 (sta3c15 ASC);
        ALTER TABLE sta3 ADD CONSTRAINT sta3_pk PRIMARY KEY ( sta3c1,sta2c1 );
    END IF;
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'sta4')
    THEN
        CREATE TABLE sta4 (
            sta2c1    bigint NOT NULL,
            sta3c1    INTEGER NOT NULL,
            sta4c1    timestamp,
            sta4c2    timestamp,
            sta4c3    INTEGER,
            sta4c4    timestamp,
            sta4c5    bigint,
            sta4c6    INTEGER,
            sta4c7    timestamp,
            sta4c8    bigint,
            sta4c9    timestamp,
            sta4c10   bigint,
            sta4c11   INTEGER,
            sta4c12   timestamp,
            sta4c13   bigint,
            sta4c14   INTEGER,
            sta4c15   timestamp,
            sta4c16   bigint,
            sta4c17   INTEGER,
            sta4c18   timestamp,
            sta4c19   bigint,
            sta4c20   INTEGER,
            sta4c21   timestamp,
            sta4c22   bigint,
            sta4c23   INTEGER,
            sta4c24   timestamp,
            sta4c25   bigint,
            sta4c26   INTEGER
        );
        ALTER TABLE sta4 ADD CONSTRAINT sta4_pk PRIMARY KEY ( sta2c1,sta3c1 );
        ALTER TABLE sta3 ALTER COLUMN sta3c3 TYPE VARCHAR(150);

    END IF;

    -- ADICION DE CAMPO PARA MANEJO DE ESTADISTICAS CON PRECIOS 
    IF NOT EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name='sta3' AND column_name='sta3c26')
    THEN
        ALTER TABLE sta3 ADD sta3c26 smallint;
    END IF;

    -- ADICION DE CAMPO PARA GUARDAR EL ID DEL PERFIL/PAQUETE DE UN EXAMEN
    IF NOT EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name='sta3' AND column_name='sta3c27')
    THEN
        ALTER TABLE sta3 ADD sta3c27 INTEGER;
    END IF;

    -- ADICION DE CAMPO PARA GUARDAR ORDEN HIS DE LA ORDEN
    IF NOT EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name='sta2' AND column_name='sta2c29')
    THEN
        ALTER TABLE sta2 ADD sta2c29 VARCHAR(256);
    END IF;
    -- ADICION DE CAMPO PARA GUARDAR LA HISTORIA DEL PACIENTE
    IF NOT EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name='sta1' AND column_name='sta1c11')
    THEN
        ALTER TABLE sta1 ADD sta1c11 VARCHAR(256);
    END IF;

    --Adicion de campo para almacenar la fecha de impresion de la muestra. 
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='sta4' AND column_name='sta4c27')
    THEN      
        ALTER TABLE sta4 ADD sta4c27 DATE;
    END IF;

    --Adicion de campo para almacenar el usuario que imprime la muestra. 
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='sta4' AND column_name='sta4c28')
    THEN      
        ALTER TABLE sta4 ADD sta4c28 INTEGER;
    END IF;
    RETURN 0;
END;
$$ LANGUAGE plpgsql;
select script();
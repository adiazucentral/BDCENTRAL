--Script para la creacion y actualizacion de la base de datos de Patologia de PostgreSQL
CREATE OR REPLACE FUNCTION script() RETURNS integer AS $$
BEGIN

    --Casos
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat01')
    THEN
        CREATE TABLE pat01 (
            pat01c1     SERIAL NOT NULL,
            pat01c2     BIGINT NOT NULL,
            pat11c1     INTEGER NOT NULL,
            pat01c3     INTEGER NOT NULL,
            lab22c1     BIGINT NOT NULL,
            pat01c4     INTEGER NOT NULL,
            lab05c1     INTEGER NOT NULL,
            pat01c5     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat01c6     TIMESTAMP,
            lab04c1b    INTEGER,
            lab04c1c    INTEGER
        );
        ALTER TABLE pat01 ADD CONSTRAINT pat01_pk PRIMARY KEY ( pat01c1 );
    END IF;

    --Areas
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat02')
    THEN
        CREATE TABLE pat02 (
            pat02c1     SERIAL NOT NULL,
            pat02c2     VARCHAR (50) NOT NULL,
            pat02c3     VARCHAR (150) NOT NULL,
            pat02c4     INTEGER NOT NULL,
            pat02c5     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat02c6     TIMESTAMP,
            lab04c1b    INTEGER
        );
        ALTER TABLE pat02 ADD CONSTRAINT pat02_pk PRIMARY KEY ( pat02c1 );
    END IF;

    --Casetes
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat03')
    THEN
        CREATE TABLE pat03 (
            pat03c1     SERIAL NOT NULL,
            pat03c2     VARCHAR (50) NOT NULL,
            pat03c3     VARCHAR (150) NOT NULL,
            pat03c4     INTEGER NOT NULL,
            pat03c5     VARCHAR (50) NOT NULL,
            pat03c6     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat03c7     TIMESTAMP,
            lab04c1b    INTEGER
        );
        ALTER TABLE pat03 ADD CONSTRAINT pat03_pk PRIMARY KEY ( pat03c1 );
    END IF;

    --Submuestras
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat04')
    THEN
        CREATE TABLE pat04 (
            pat04c1     SERIAL NOT NULL,
            lab24c1     INTEGER NOT NULL,
            pat04c2     INTEGER NOT NULL,
            pat04c3     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat04c4     TIMESTAMP,
            lab04c1b    INTEGER
        );
        ALTER TABLE pat04 ADD CONSTRAINT pat04_pk PRIMARY KEY ( pat04c1 );
    END IF;

    -- Contenedores
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat05')
    THEN
        CREATE TABLE pat05 (
            pat05c1     SERIAL NOT NULL,
            pat05c2     VARCHAR (150) NOT NULL ,
            pat05c3     BYTEA,
            pat05c4     INTEGER NOT NULL, 
            pat05c5     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat05c6     TIMESTAMP,
            lab04c1b    INTEGER 
        );
    ALTER TABLE pat05 ADD CONSTRAINT pat05_pk PRIMARY KEY ( pat05c1 );
    END IF;

    --Coloraciones
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat06')
    THEN
        CREATE TABLE pat06 (
            pat06c1     SERIAL NOT NULL,
            pat06c2     VARCHAR (50) NOT NULL,
            pat06c3     VARCHAR (250) NOT NULL,
            pat06c4     INTEGER NOT NULL,
            pat06c5     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat06c6     TIMESTAMP,
            lab04c1b    INTEGER
        );
        ALTER TABLE pat06 ADD CONSTRAINT pat06_pk PRIMARY KEY ( pat06c1 );
    END IF;

    --Organo
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat07')
    THEN
        CREATE TABLE pat07 (
            pat07c1     SERIAL NOT NULL,
            pat07c2     VARCHAR (50) NOT NULL,
            pat07c3     VARCHAR (250) NOT NULL,
            pat07c4     INTEGER NOT NULL,
            pat07c5     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat07c6     TIMESTAMP,
            lab04c1b    INTEGER
        );
        ALTER TABLE pat07 ADD CONSTRAINT pat07_pk PRIMARY KEY ( pat07c1 );
    END IF;

    --Especimen - Casete - Laminas
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat08')
    THEN
        CREATE TABLE pat08 (
            pat08c1     SERIAL NOT NULL,
            pat09c1     INTEGER NOT NULL,
            pat06c1     INTEGER NOT NULL,
            pat08c2     INTEGER NOT NULL
        );
        ALTER TABLE pat08 ADD CONSTRAINT pat08_pk PRIMARY KEY ( pat08c1 );
    END IF;

    --Configuración de especimenes
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat09')
    THEN
        CREATE TABLE pat09 (
            pat09c1     SERIAL NOT NULL,
            lab24c1     INTEGER NOT NULL,
            pat07c1     INTEGER,
            pat03c1     INTEGER NOT NULL,
            pat09c2     INTEGER NOT NULL,
            pat09c3     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat09c4     TIMESTAMP,
            lab04c1b    INTEGER
        );
        ALTER TABLE pat09 ADD CONSTRAINT pat09_pk PRIMARY KEY ( pat09c1 );
    END IF;

    --Fijadores
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat10')
    THEN
        CREATE TABLE pat10 (
            pat10c1     SERIAL NOT NULL,
            pat10c2     VARCHAR (50) NOT NULL,
            pat10c3     VARCHAR (150) NOT NULL,
            pat10c4     INTEGER NOT NULL,
            pat10c5     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat10c6     TIMESTAMP,
            lab04c1b    INTEGER
        );
        ALTER TABLE pat10 ADD CONSTRAINT pat10_pk PRIMARY KEY ( pat10c1 );
    END IF;

    --Tipos de estudio
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat11')
    THEN
        CREATE TABLE pat11 (
            pat11c1     SERIAL NOT NULL,
            pat11c2     VARCHAR (50) NOT NULL,
            pat11c3     VARCHAR (150) NOT NULL,
            pat11c4     INTEGER NOT NULL,
            pat11c5     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat11c6     TIMESTAMP,
            lab04c1b    INTEGER
        );
        ALTER TABLE pat11 ADD CONSTRAINT pat11_pk PRIMARY KEY ( pat11c1 );
    END IF;

    --Estudios - tipos de estudios
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat12')
    THEN
        CREATE TABLE pat12 (
            pat12c1     SERIAL NOT NULL,
            pat11c1     INTEGER NOT NULL,
            lab39c1     INTEGER NOT NULL
        );
        ALTER TABLE pat12 ADD CONSTRAINT pat12_pk PRIMARY KEY ( pat12c1 );
    END IF;

    --Caso - Especimen
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat13')
    THEN
        CREATE TABLE pat13 (
            pat13c1     SERIAL NOT NULL,
            pat01c1     INTEGER NOT NULL,
            lab24c1     INTEGER NOT NULL,
            pat04c1     INTEGER,
            pat07c1     INTEGER NOT NULL
        );
        ALTER TABLE pat13 ADD CONSTRAINT pat13_pk PRIMARY KEY ( pat13c1 );
    END IF;

    --Muestras
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat14')
    THEN
        CREATE TABLE pat14 (
            pat14c1     SERIAL NOT NULL,
            pat14c2     INTEGER NOT NULL,
            pat05c1     INTEGER NOT NULL,
            pat10c1     INTEGER NOT NULL,
            pat13c1     INTEGER NOT NULL
        );
        ALTER TABLE pat14 ADD CONSTRAINT pat14_pk PRIMARY KEY ( pat14c1 );
    END IF;

    --Estudios
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat15')
    THEN
        CREATE TABLE pat15 (
            pat13c1     INTEGER NOT NULL,
            lab39c1     INTEGER NOT NULL
        );
        ALTER TABLE pat15 ADD CONSTRAINT pat15_pk PRIMARY KEY ( pat13c1, lab39c1 );
    END IF;

    --Muestras rechazadas
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat16')
    THEN
        CREATE TABLE pat16 (
            pat16c1     SERIAL NOT NULL,
            lab22c1     BIGINT NOT NULL,
            pat11c1     INTEGER NOT NULL,
            lab30c1     INTEGER NOT NULL,
            pat16c2     TEXT,
            pat16c3     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat16c4     TIMESTAMP,
            lab04c1b    TIMESTAMP
        );
        ALTER TABLE pat16 ADD CONSTRAINT pat16_pk PRIMARY KEY ( pat16c1 );
    END IF;

    --Agenda
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat17')
    THEN
        CREATE TABLE pat17 (
            pat17c1     SERIAL NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat17c2     TIMESTAMP NOT NULL,
            pat17c3     TIMESTAMP NOT NULL,
            pat17c4     INTEGER,
            pat17c5     TIMESTAMP NOT NULL,
            pat21c1     INTEGER,
            lab04c1b    INTEGER NOT NULL,
            pat17c6     TIMESTAMP,
            lab04c1c    INTEGER
        );
        ALTER TABLE pat17 ADD CONSTRAINT pat17_pk PRIMARY KEY ( pat17c1 );
    END IF;

    --Casetes Muestras
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat18')
    THEN
        CREATE TABLE pat18 (
            pat18c1     SERIAL NOT NULL,
            pat14c1     INTEGER NOT NULL,
            pat18c2     INTEGER NOT NULL,
            pat18c3     VARCHAR (5) NOT NULL,
            pat03c1     INTEGER NOT NULL
        );
        ALTER TABLE pat18 ADD CONSTRAINT pat18_pk PRIMARY KEY ( pat18c1 );
    END IF;

    --Especialidades
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat19')
    THEN
        CREATE TABLE pat19 (
            pat19c1     SERIAL NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat07c1     INTEGER NOT NULL,
            pat19c2     TIMESTAMP NOT NULL,
            lab04c1b    INTEGER NOT NULL,
            pat19c3     TIMESTAMP,
            lab04c1c    INTEGER 
        );
        ALTER TABLE pat19 ADD CONSTRAINT pat19_pk PRIMARY KEY ( pat19c1 );
    END IF;

    --Archivos caso
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat20')
    THEN
        CREATE TABLE pat20 (
            pat20c1     SERIAL NOT NULL,
            pat01c1     INTEGER NOT NULL,
            pat20c2     BYTEA NOT NULL,
            pat20c3     VARCHAR (50) NOT NULL,
            pat20c4     VARCHAR (50) NOT NULL,
            pat20c5     VARCHAR (50) NOT NULL,
            pat20c6     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat20c7     TIMESTAMP,
            lab04c1b    INTEGER
        );
        ALTER TABLE pat20 ADD CONSTRAINT pat20_pk PRIMARY KEY ( pat20c1 );
    END IF;

    -- Nuevo campo para el maestro de contenedores
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat05' AND column_name='pat05c7')
    THEN
        ALTER TABLE pat05 ADD pat05c7 SMALLINT NOT NULL DEFAULT 0;
    END IF;

    --Codigos de barras
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat100')
    THEN
        CREATE TABLE pat100 (
            pat100c1    SERIAL NOT NULL,
            pat100c2    TEXT,
            pat100c3    INTEGER,
            pat100c4    INTEGER,
            pat100c5    TEXT
        );
        ALTER TABLE pat100 ADD CONSTRAINT pat100_pk PRIMARY KEY ( pat100c1 );
    END IF;

    --Eventos
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat21')
    THEN
        CREATE TABLE pat21 (
            pat21c1     SERIAL NOT NULL,
            pat21c2     VARCHAR (50) NOT NULL,
            pat21c3     VARCHAR (150) NOT NULL,
            pat21c4     VARCHAR (50) NOT NULL,
            pat21c5     INTEGER NOT NULL,
            pat21c6     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat21c7     TIMESTAMP,
            lab04c1b    INTEGER
        );
        ALTER TABLE pat21 ADD CONSTRAINT pat21_pk PRIMARY KEY ( pat21c1 );
    END IF;

    --Macroscopia
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat25')
    THEN
        CREATE TABLE pat25 (
            pat25c1     SERIAL NOT NULL,
            pat01c1     INTEGER NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat25c3     TIMESTAMP NOT NULL,
            pat25c4     INTEGER NOT NULL,
            pat28c1     INTEGER
        );
        ALTER TABLE pat25 ADD CONSTRAINT pat25_pk PRIMARY KEY ( pat25c1 );
    END IF;

    --Campos
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat26')
    THEN
        CREATE TABLE pat26 (
            pat26c1     SERIAL NOT NULL,
            pat26c2     VARCHAR (50) NOT NULL,
            pat26c3     INTEGER NOT NULL,
            pat26c4     INTEGER NOT NULL,
            pat26c5     INTEGER NOT NULL,
            pat26c6     INTEGER NOT NULL,
            pat26c7     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat26c8     TIMESTAMP,
            lab04c1b    INTEGER
        );
        ALTER TABLE pat26 ADD CONSTRAINT pat26_pk PRIMARY KEY ( pat26c1 );
    END IF;

    --Plantillas
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat27')
    THEN
        CREATE TABLE pat27 (
            pat27c1     SERIAL NOT NULL,
            lab24c1     INTEGER NOT NULL,
            pat26c1     INTEGER NOT NULL,
            pat27c2     INTEGER NOT NULL,
            pat27c3     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat27c4     TIMESTAMP,
            lab04c1b    INTEGER 
        );
        ALTER TABLE pat27 ADD CONSTRAINT pat27_pk PRIMARY KEY ( pat27c1 );
    END IF;

    --Audios
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat28')
    THEN
        CREATE TABLE pat28 (
            pat28c1     SERIAL NOT NULL,
            pat28c2     VARCHAR (150) NOT NULL,
            pat28c3     VARCHAR (50) NOT NULL,
            pat28c4     VARCHAR (250) NOT NULL,
            pat28c5     TIMESTAMP NOT NULL,
            lab04c1     INTEGER NOT NULL
        );
        ALTER TABLE pat28 ADD CONSTRAINT pat28_pk PRIMARY KEY ( pat28c1 );
    END IF;

    --Valores de plantillas
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat29')
    THEN
        CREATE TABLE pat29 (
            pat29c1     SERIAL NOT NULL,
            pat25c1     INTEGER NOT NULL,
            pat29c2     TEXT NOT NULL,
            pat27c1     INTEGER
        );
        ALTER TABLE pat29 ADD CONSTRAINT pat29_pk PRIMARY KEY ( pat29c1 );
    END IF;

    -- Nuevo campo para las descripciones macroscopicas. Persona que realiza transcripcion
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat25' AND column_name='lab04c1b')
    THEN
        ALTER TABLE pat25 ADD lab04c1b INTEGER DEFAULT NULL;
    END IF;

    -- Nuevo campo para las descripciones macroscopicas. Fecha de transcripcion
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat25' AND column_name='pat25c5')
    THEN
        ALTER TABLE pat25 ADD pat25c5 TIMESTAMP DEFAULT NULL;
    END IF;

    -- Nuevo campo para las descripciones macroscopicas. Indica si la transcripcion requiere autorizacion
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat25' AND column_name='pat25c6')
    THEN
        ALTER TABLE pat25 ADD pat25c6 INTEGER NOT NULL DEFAULT 0;
    END IF;

    -- Nuevo campo para las descripciones macroscopicas. Indica el usuario que autoriza la transcripcion
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat25' AND column_name='lab04c1c')
    THEN
        ALTER TABLE pat25 ADD lab04c1c INTEGER DEFAULT NULL;
    END IF;

    -- Nuevo campo para las descripciones macroscopicas. Indica la fecha de autorizacion
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat25' AND column_name='pat25c7')
    THEN
        ALTER TABLE pat25 ADD pat25c7 TIMESTAMP DEFAULT NULL;
    END IF;

    -- Nuevo campo para los protocolos. Indica las horas de procesamiento de las muestras
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat09' AND column_name='pat09c5')
    THEN
        ALTER TABLE pat09 ADD pat09c5 INTEGER NOT NULL DEFAULT 1;
    END IF;

    --Horarios de procesamiento
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat31')
    THEN
        CREATE TABLE pat31 (
            pat31c1     SERIAL NOT NULL,
            pat31c2     VARCHAR (10) NOT NULL,
            pat31c3     INTEGER NOT NULL,
            pat31c4     TIMESTAMP NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat31c5     TIMESTAMP,
            lab04c1b    INTEGER
        );
        ALTER TABLE pat31 ADD CONSTRAINT pat31_pk PRIMARY KEY ( pat31c1 );
    END IF;

    --Lista de casetes
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat32')
    THEN
        CREATE TABLE pat32 (
            pat32c1     SERIAL NOT NULL,
            pat14c1     INTEGER NOT NULL,
            pat32c2     VARCHAR (5) NOT NULL,
            pat03c1     INTEGER NOT NULL,
            pat18c1     INTEGER NOT NULL
        );
        ALTER TABLE pat32 ADD CONSTRAINT pat32_pk PRIMARY KEY ( pat32c1 );
    END IF;

    -- Nuevo campo para los casetes. Indica el estado del casete en el modulo de histotecnologia
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat32' AND column_name='pat32c3')
    THEN
        ALTER TABLE pat32 ADD pat32c3 INTEGER NOT NULL DEFAULT 1;
    END IF;

    --Procesador de tejidos
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat33')
    THEN
        CREATE TABLE pat33 (
            pat33c1     SERIAL NOT NULL,
            pat32c1     INTEGER NOT NULL,
            pat33c2     INTEGER NOT NULL,
            pat31c1     INTEGER NOT NULL
        );
        ALTER TABLE pat33 ADD CONSTRAINT pat33_pk PRIMARY KEY ( pat33c1 );
    END IF;

    --Trazabilidad casetes
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'pat34')
    THEN
        CREATE TABLE pat34 (
            pat34c1     SERIAL NOT NULL,
            pat32c1     INTEGER NOT NULL,
            pat34c2     INTEGER NOT NULL,
            lab04c1a    INTEGER NOT NULL,
            pat34c3     TIMESTAMP NOT NULL
        );
        ALTER TABLE pat34 ADD CONSTRAINT pat34_pk PRIMARY KEY ( pat34c1 );
    END IF;

    RETURN 0;
END;
$$ LANGUAGE plpgsql;
select script();
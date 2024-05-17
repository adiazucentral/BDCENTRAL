-- En este script estaran todas las tablas relacionadas con la base de datos de
-- control
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'cont01')
BEGIN
    CREATE TABLE cont01 (
            cont01c1 BIGINT NOT NULL,
            cont01c2 INT NULL,
            cont01c3 VARCHAR(25) NULL,
            cont01c4 VARCHAR(25) NULL,
            cont01c5 VARCHAR(25) NULL,
            cont01c6 VARCHAR(25) NULL,
            cont01c7 VARCHAR(25) NULL,
            cont01c8 INT NULL,
            cont01c9 VARCHAR(20) NULL
    );
    ALTER TABLE cont01 ADD CONSTRAINT cont01c1_IX01 PRIMARY KEY (cont01c1);
END;
-- ENVIO DE MENSAJES ASTM AL MIDDLEWARE
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'cont02')
BEGIN
    CREATE TABLE cont02 (
        cont02c1 VARCHAR(100) NOT NULL,
        cont02c2 VARCHAR(100) NOT NULL,
        cont02c3 VARCHAR(7000) NOT NULL,
        cont02c4 SMALLINT NOT NULL DEFAULT 0
    );
END;

-- GUARDADO DE ESTADOS ENVIADOS POR EXAMEN. 
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'cont03')
BEGIN
    CREATE TABLE cont03 (
            cont03c1 BIGINT NOT NULL,  --numero de orden
            cont03c2 VARCHAR(25) NOT NULL, --numero de cuenta
            cont03c3 INT NULL, --examen
            cont03c4 VARCHAR(25) NOT NULL, -- estado
            cont03c5 DATETIME NULL  -- fecha de envio
    );
    
END;

-- ELIMINACION DE CAMPO DE EXAMEN ESTADOS ENVIADOS POR EXAMEN FSFB
IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'cont03' AND column_name = 'cont03c3')
BEGIN
    ALTER TABLE cont03 DROP COLUMN cont03c3;
END

--CODIGO DE HOMOLOGACION - ESTADOS ENVIADOS POR EXAMEN FSFB.
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='cont03' AND column_name='cont03c6')
BEGIN
     ALTER TABLE cont03 ADD cont03c6 VARCHAR(25);
END

--CANTIDAD DE ENVIOS - ESTADOS ENVIADOS POR EXAMEN FSFB.
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='cont03' AND column_name='cont03c7')
BEGIN
     ALTER TABLE cont03 ADD cont03c7 INTEGER NOT NULL DEFAULT 0;
END

--ENVIADO CON ERROR  - ESTADOS ENVIADOS POR EXAMEN FSFB.
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='cont03' AND column_name='cont03c8')
BEGIN
     ALTER TABLE cont03 ADD cont03c8 INTEGER NOT NULL DEFAULT 0;
END
--Script para la creacion y actualizacion de la base de datos transaccional de SQLServer
--Paciente
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[sta1]') )
BEGIN
    CREATE TABLE sta1
    (
        sta1c1 INTEGER NOT NULL ,
        sta1c2 INTEGER ,
        sta1c3 VARCHAR (128) ,
        sta1c4 VARCHAR (128) ,
        sta1c5 INTEGER ,
        sta1c6 VARCHAR (16) ,
        sta1c7 VARCHAR (64) ,
        sta1c8 INTEGER ,
        sta1c9 VARCHAR (16) ,
        sta1c10 VARCHAR (64)
    )

    ALTER TABLE sta1 ADD CONSTRAINT sta1_PK PRIMARY KEY CLUSTERED (sta1c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[sta2]') )
BEGIN
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
        sta2c21 DATETIME ,
        sta2c22 INTEGER ,
        sta2c23 INTEGER ,
        sta2c24 FLOAT ,
        sta2c25 INTEGER ,
        sta2c26 INTEGER ,
        sta2c27 VARCHAR (16) ,
        sta2c28 VARCHAR (64)
    )

    CREATE INDEX sta2__IDX ON sta2 (sta2c20)
    ALTER TABLE sta2 ADD CONSTRAINT sta2_PK PRIMARY KEY CLUSTERED (sta2c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON)
END
--SEGMENT
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[sta3]') )
BEGIN
    CREATE TABLE sta3
    (
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
        sta3c23 TINYINT,
        sta3c24 INTEGER NOT NULL,
        sta3c25 TINYINT
    )

    CREATE INDEX sta3__IDX ON sta3 (sta3c15)
    ALTER TABLE sta3 ADD CONSTRAINT sta3_PK PRIMARY KEY CLUSTERED (sta3c1, sta2c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[sta4]') )
BEGIN
    CREATE TABLE sta4
    (
        sta2c1 BIGINT NOT NULL ,
        sta3c1 INTEGER NOT NULL ,
        sta4c1 DATETIME ,
        sta4c2 DATETIME ,
        sta4c3 INTEGER ,
        sta4c4 DATETIME ,
        sta4c5 BIGINT ,
        sta4c6 INTEGER ,
        sta4c7 DATETIME ,
        sta4c8 BIGINT ,
        sta4c9 DATETIME ,
        sta4c10 BIGINT ,
        sta4c11 INTEGER ,
        sta4c12 DATETIME ,
        sta4c13 BIGINT ,
        sta4c14 INTEGER ,
        sta4c15 DATETIME ,
        sta4c16 BIGINT ,
        sta4c17 INTEGER ,
        sta4c18 DATETIME ,
        sta4c19 BIGINT ,
        sta4c20 INTEGER ,
        sta4c21 DATETIME ,
        sta4c22 BIGINT ,
        sta4c23 INTEGER ,
        sta4c24 DATETIME ,
        sta4c25 BIGINT ,
        sta4c26 INTEGER
    )
    ALTER TABLE sta4 ADD CONSTRAINT sta4_PK PRIMARY KEY CLUSTERED (sta2c1, sta3c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
ALTER TABLE sta3 ALTER COLUMN sta3c3 VARCHAR(150);
-- SE ELIMINA EL PROCEDIMIENTO ALMACENADO QUE RENOMBRA LAS TABLAS DE OPERACION, SOLO SI EXISTE
IF EXISTS(SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[RENAME_NEW_YEAR_STA]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
BEGIN
    DROP PROCEDURE [RENAME_NEW_YEAR_STA];
END
-- ADICION DE CAMPO PARA MANEJO DE ESTADISTICAS CON PRECIOS 
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='sta3' AND column_name='sta3c26')
BEGIN
    ALTER TABLE sta3 ADD sta3c26 TINYINT;
END
-- ADICION DE CAMPO PARA GUARDAR EL ID DEL PERFIL/PAQUETE DE UN EXAMEN
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='sta3' AND column_name='sta3c27')
BEGIN
    ALTER TABLE sta3 ADD sta3c27 INTEGER;
END

-- ADICION DE CAMPO PARA GUARDAR ORDEN HIS DE LA ORDEN
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='sta2' AND column_name='sta2c29')
BEGIN
    ALTER TABLE sta2 ADD sta2c29 VARCHAR(256);
END

-- ADICION DE CAMPO PARA GUARDAR ORDEN HIS DE LA ORDEN
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='sta1' AND column_name='sta1c11')
BEGIN
    ALTER TABLE sta1 ADD sta1c11 VARCHAR(256);
END

--Adicion de campo para almacenar la fecha de transporte de la muestra. 
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='sta4' AND column_name='sta4c27')
BEGIN      
    ALTER TABLE sta4 ADD sta4c27 DATETIME;
END

--Adicion de campo para almacenar el usuario que transporta de la muestra. 
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='sta4' AND column_name='sta4c28')
BEGIN      
    ALTER TABLE sta4 ADD sta4c28 INTEGER;
END
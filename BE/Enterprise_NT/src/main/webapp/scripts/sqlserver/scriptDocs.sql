--Script para la creacion y actualizacion de la base de datos transaccional de SQLServer
--Configuración
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[doc01]') )
BEGIN
    CREATE TABLE doc01 
    (
        lab22c1 BIGINT NOT NULL , 
        doc01c1 VARCHAR(128) NOT NULL , 
        doc01c2 IMAGE NOT NULL , 
        doc01c3 DATETIME NOT NULL , 
        doc01c4 VARCHAR (16) NOT NULL , 
        doc01c5 VARCHAR (4) NOT NULL , 
        lab04c1 INTEGER NOT NULL 
    )
    ALTER TABLE doc01 ADD CONSTRAINT doc01_PK PRIMARY KEY CLUSTERED (lab22c1, doc01c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[doc02]') )
BEGIN
    CREATE TABLE doc02 
    (
        lab22c1 BIGINT NOT NULL , 
        lab39c1 INTEGER NOT NULL , 
        doc02c1 VARCHAR(128) NOT NULL , 
        doc02c2 IMAGE NOT NULL , 
        doc02c3 DATETIME NOT NULL , 
        doc02c4 VARCHAR (16) NOT NULL , 
        doc02c5 VARCHAR (4) NOT NULL , 
        lab04c1 INTEGER NOT NULL 
    )
    ALTER TABLE doc02 ADD CONSTRAINT doc02_pk PRIMARY KEY CLUSTERED (doc02c1, lab22c1, lab39c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[doc03]') )
BEGIN
    CREATE TABLE doc03 
    (
        lab22c1 BIGINT NOT NULL , -- Numero de orden
            lab39c1 INTEGER NOT NULL , -- Id de examen
            doc03c1 VARCHAR(20) NOT NULL , -- Nombre de imagen 1
            doc03c2 IMAGE NOT NULL , -- Imagen 1
            doc03c3 VARCHAR(20) , -- Nombre de imagen 2
            doc03c4 IMAGE , -- Imagen 2
            doc03c5 VARCHAR(20) , -- Nombre de imagen 3
            doc03c6 IMAGE , -- Imagen 3
            doc03c7 VARCHAR(20) , -- Nombre de imagen 4
            doc03c8 IMAGE  , -- Imagen 4
            doc03c9 VARCHAR(20) , -- Nomnbre de imagen 5
            doc03c10 IMAGE  -- Imagen 5
    )
    ALTER TABLE doc03 ADD CONSTRAINT doc03_pk PRIMARY KEY CLUSTERED (lab22c1, lab39c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )

END

IF EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[doc03]') )
BEGIN
    ALTER TABLE doc03 ALTER COLUMN doc03c1 varchar(64);
    ALTER TABLE doc03 ALTER COLUMN doc03c3 varchar(64);
    ALTER TABLE doc03 ALTER COLUMN doc03c5 varchar(64);
    ALTER TABLE doc03 ALTER COLUMN doc03c7 varchar(64);
    ALTER TABLE doc03 ALTER COLUMN doc03c9 varchar(64);
END
--Modificacion de la tabla doc01 se adiciona el campo para validar si se imprime o nop
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='doc01' AND column_name='doc01c6')
BEGIN
    ALTER TABLE doc01 ADD doc01c6 TINYINT  DEFAULT 1;
END
--Modificacion de la tabla doc02 se adiciona el campo para validar si se imprime o nop
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='doc02' AND column_name='doc02c6')
BEGIN
    ALTER TABLE doc02 ADD doc02c6 TINYINT  DEFAULT 1;
END
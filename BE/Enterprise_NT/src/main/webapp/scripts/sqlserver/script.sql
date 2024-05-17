--Script para la creacion y actualizacion de la base de datos transaccional de SQLServer
--ConfiguraciÃ³n
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[Lab98]') )
BEGIN
    CREATE TABLE lab98 (
        lab98c1 VARCHAR (64) NOT NULL ,
        lab98c2 VARCHAR (256)
    )
    ALTER TABLE lab98 ADD CONSTRAINT lab98_PK PRIMARY KEY CLUSTERED (lab98c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--Sesiones
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab100]') )
BEGIN
    CREATE TABLE lab100 (
        lab100c1 VARCHAR (100) NOT NULL ,
        lab100c2 DATETIME NOT NULL,
        lab100c3 VARCHAR(50) NOT NULL,
        lab04c1  INTEGER NOT NULL,
        lab05c1  INTEGER NOT NULL
    )
    ALTER TABLE lab100 ADD CONSTRAINT lab100_PK PRIMARY KEY CLUSTERED (lab100c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Listas
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab80]') )
BEGIN
    CREATE TABLE lab80 (
        lab80c1 INTEGER NOT NULL ,
        lab80c2 INTEGER ,
        lab80c3 VARCHAR (8) ,
        lab80c4 VARCHAR (128) NOT NULL ,
        lab80c5 VARCHAR (128) NOT NULL ,
        lab80c6   VARCHAR(128)
    )
    ALTER TABLE lab80 ADD CONSTRAINT lab80_PK PRIMARY KEY CLUSTERED (lab80c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Usuario
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab04]') )
BEGIN
    CREATE TABLE lab04
    (
        lab04c1 INTEGER NOT NULL IDENTITY(1,1),
        lab04c2 VARCHAR (64) NOT NULL ,
        lab04c3 VARCHAR (64) NOT NULL ,
        lab04c4 VARCHAR (32) NOT NULL ,
        lab04c5 VARCHAR (256) NOT NULL ,
        lab07c1 TINYINT NOT NULL ,
        lab04c6 DATETIME NOT NULL ,
        lab04c7 DATETIME ,
        lab04c8 DATETIME NOT NULL ,
        lab04c9 DATETIME NOT NULL ,
        lab04c1_1 INTEGER ,
        lab04c10 VARCHAR (64) ,
        lab04c11 VARCHAR (128) ,
        lab04c12 IMAGE ,
        lab04c13 VARCHAR (64) ,
        lab04c14 FLOAT (3) NOT NULL ,
        lab04c15 TINYINT NOT NULL ,
        lab04c16 IMAGE ,
        lab04c17 TINYINT NOT NULL ,
        lab04c18 TINYINT NOT NULL ,
        lab04c19 TINYINT NOT NULL ,
        lab04c20 TINYINT NOT NULL ,
        lab04c21 TINYINT NOT NULL ,
        lab04c22 TINYINT NOT NULL ,
        lab04c23 TINYINT NOT NULL ,
        lab04c24 TINYINT NOT NULL ,
        lab103c1 INTEGER
    )
    ALTER TABLE lab04 ADD CONSTRAINT lab04_PK PRIMARY KEY CLUSTERED (lab04c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Tipo de orden
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab103]') )
BEGIN
    CREATE TABLE lab103
    (
        lab103c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab103c2 VARCHAR (2) ,
        lab103c3 VARCHAR (62) NOT NULL ,
        lab103c4 VARCHAR (8) ,
        lab103c5 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )

    ALTER TABLE lab103 ADD CONSTRAINT lab103_PK PRIMARY KEY CLUSTERED (lab103c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Rol
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab82]') )
BEGIN
    CREATE TABLE lab82 (
        lab82c1 INTEGER NOT NULL IDENTITY(1,1),
        lab82c2 VARCHAR (60) NOT NULL ,
        lab82c3 TINYINT NOT NULL ,
        lab82c4 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )

    ALTER TABLE Lab82 ADD CONSTRAINT Lab82_PK PRIMARY KEY CLUSTERED (lab82c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Unidad
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab45]') )
BEGIN
    CREATE TABLE lab45 (
        lab45c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab45c2 VARCHAR (16) NOT NULL ,
        lab45c3 VARCHAR (16) ,
        lab45c4 FLOAT ,
        lab45c5 DATETIME NOT NULL ,
        lab07c1 TINYINT ,
        lab04c1 INTEGER NOT NULL
    )

    ALTER TABLE lab45 ADD CONSTRAINT lab45_PK PRIMARY KEY CLUSTERED (lab45c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Area
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab43]') )
BEGIN
    CREATE TABLE lab43 (
        lab43c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab43c2 TINYINT NOT NULL ,
        lab43c3 VARCHAR (8) NOT NULL ,
        lab43c4 VARCHAR (128) NOT NULL ,
        lab43c5 VARCHAR (8) NOT NULL ,
        lab43c6 TINYINT NOT NULL ,
        lab43c7 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL ,
        lab43c8 TINYINT NOT NULL
    )
    ALTER TABLE lab43 ADD CONSTRAINT lab43_PK PRIMARY KEY CLUSTERED (lab43c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--RECIPIENTE
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab56]') )
BEGIN
    CREATE TABLE lab56 (
        lab56c1   INTEGER NOT NULL IDENTITY(1,1),
        lab56c2   VARCHAR (60) NOT NULL,
        lab56c3   IMAGE,
        lab56c4   DATETIME NOT NULL,
        lab04c1   INTEGER NOT NULL,
        lab56c5   smallint NOT NULL,
        lab07c1   smallint,
        lab45c1 INTEGER
    );

    ALTER TABLE lab56 ADD CONSTRAINT lab56_pk PRIMARY KEY CLUSTERED (lab56c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Tecnica
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab64]') )
BEGIN
    CREATE TABLE lab64 (
        lab64c1   INTEGER NOT NULL IDENTITY(1,1),
        lab64c2   VARCHAR(16) NOT NULL,
        lab64c3   VARCHAR(64) NOT NULL,
        lab64c4   DATETIME NOT NULL,
        lab04c1   INTEGER NOT NULL,
        lab07c1   smallint NOT NULL
    );

    ALTER TABLE lab64 ADD CONSTRAINT lab64_pk PRIMARY KEY CLUSTERED (lab64c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--MAESTRO DE AUDITORIA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab34]') )
BEGIN
    CREATE TABLE lab34 (
        lab34c1   INTEGER NOT NULL IDENTITY(1,1),
        lab34c2   DATETIME NOT NULL,
        lab04c1   INTEGER NOT NULL,
        lab34c3   VARCHAR(16) NOT NULL,
        lab34c4   VARCHAR(128) NOT NULL,
        lab34c5   VARCHAR(512) NOT NULL,
        lab34c6   VARCHAR(64) NOT NULL,
        lab34c7   VARCHAR(128) NOT NULL,
        lab34c8   VARCHAR(1) NOT NULL
    );

    ALTER TABLE lab34 ADD CONSTRAINT lab34_pk PRIMARY KEY CLUSTERED (lab34c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--DETALLE DE AUDITORIA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab36]') )
BEGIN
    CREATE TABLE lab36 (
        lab36c1   INTEGER NOT NULL IDENTITY(1,1),
        lab34c1   INTEGER NOT NULL,
        lab36c2   VARCHAR(512) NOT NULL,
        lab36c3   TEXT,
        lab36c4   TEXT,
        lab36c5   TEXT
    );

    ALTER TABLE lab36 ADD CONSTRAINT lab36_pk PRIMARY KEY CLUSTERED (lab36c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Tabla para registro de errores
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab151]') )
BEGIN
    CREATE TABLE lab151 (
        lab151c1 INTEGER NOT NULL IDENTITY(1,1),
        lab151c2 DATETIME NOT NULL ,
        lab151c3 INTEGER NOT NULL ,
        lab151c4 VARCHAR (64) NOT NULL ,
        lab151c5 VARCHAR (64) NOT NULL ,
        lab151c6 TEXT NOT NULL ,
        lab151c7 TEXT NOT NULL ,
        lab04c1  INTEGER ,
        lab04c5  VARCHAR(32),
        lab151c8 INTEGER NOT NULL
    );

    ALTER TABLE lab151 ADD CONSTRAINT lab151_PK PRIMARY KEY CLUSTERED (lab151c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--DEMOGRAFICOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab62]') )
BEGIN
    CREATE TABLE lab62
    (
        lab62c1 INTEGER NOT NULL IDENTITY(1,1),
        lab62c2 VARCHAR (64) NOT NULL ,
        lab62c3 CHAR (1) NOT NULL ,
        lab62c4 TINYINT NOT NULL ,
        lab62c5 TINYINT NOT NULL ,
        lab62c6 TINYINT NOT NULL ,
        lab62c7 VARCHAR (32) ,
        lab62c8 VARCHAR (64) ,
        lab62c9 TINYINT NOT NULL ,
        lab62c10 TINYINT NOT NULL ,
        lab62c11 TINYINT NOT NULL ,
        lab62c12 DATETIME NOT NULL ,
        lab62c13 TINYINT NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL ,
        lab63c1 INTEGER
    )
    ALTER TABLE lab62 ADD CONSTRAINT lab62_PK PRIMARY KEY CLUSTERED (lab62c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Tabla de muestra
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab24]') )
BEGIN
    CREATE TABLE lab24 (
        lab24c1    INTEGER NOT NULL IDENTITY(1,1),
        lab24c2    VARCHAR(64) NOT NULL,
        lab24c3    TINYINT NOT NULL,
        lab24c4    TINYINT NOT NULL,
        lab24c5    TINYINT NOT NULL,
        lab24c6    VARCHAR(1000),
        lab24c7    TINYINT NOT NULL,
        lab24c8    DATETIME NOT NULL,
        lab04c1    INTEGER NOT NULL,
        lab07c1    TINYINT NOT NULL,
        lab56c1    INTEGER NOT NULL,
        lab24c9    VARCHAR(3) NOT NULL,
        lab24c10   VARCHAR(32) NOT NULL,
        lab24c11   TINYINT NOT NULL,
        lab24c12   BIGINT,
        lab24c13   INTEGER
    );

    ALTER TABLE lab24 ADD CONSTRAINT lab24_PK PRIMARY KEY CLUSTERED (lab24c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Requisito
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab41]') )
BEGIN
    CREATE TABLE lab41
    (
        lab41c1 INTEGER NOT NULL IDENTITY(1,1),
        lab41c2 VARCHAR (64) NOT NULL ,
        lab41c3 VARCHAR (4000) NOT NULL ,
        lab07c1 TINYINT NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab41c4 DATETIME NOT NULL
    );

    ALTER TABLE lab41 ADD CONSTRAINT lab41_PK PRIMARY KEY CLUSTERED (lab41c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEDE
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab05]') )
BEGIN
    CREATE TABLE lab05
    (
        lab05c1 INTEGER NOT NULL IDENTITY(1,1),
        lab05c2 VARCHAR (64) ,
        lab05c4 VARCHAR (64) NOT NULL ,
        lab05c5 VARCHAR (128) ,
        lab05c6 VARCHAR (32) ,
        lab05c7 INTEGER ,
        lab05c8 INTEGER ,
        lab05c9 DATETIME NOT NULL ,
        lab05c10 VARCHAR(8) NOT NULL,
        lab05c11 VARCHAR(64),
        lab05c12 VARCHAR(128),
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )

    ALTER TABLE lab05 ADD CONSTRAINT lab05_PK PRIMARY KEY CLUSTERED (lab05c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--DEMOGRAFICOITEM
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab63]') )
BEGIN
    CREATE TABLE lab63
    (
        lab63c1 INTEGER NOT NULL IDENTITY(1,1),
        lab63c2 VARCHAR (64) NOT NULL ,
        lab63c3 VARCHAR (128) NOT NULL ,
        lab07c1 TINYINT NOT NULL ,
        lab63c5 VARCHAR (256),
        lab63c6 DATETIME NOT NULL ,
        lab62c1 INTEGER NOT NULL ,
        lab04c1 INTEGER NOT NULL
    )

    ALTER TABLE lab63 ADD CONSTRAINT lab63_PK PRIMARY KEY CLUSTERED (lab63c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--AREASBYUSER
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab69]') )
BEGIN
    CREATE TABLE lab69
    (
        lab69c1 TINYINT NOT NULL ,
        lab69c2 TINYINT NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab43c1 INTEGER NOT NULL
    )
END
--BRANCHESBYUSER
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab93]') )
BEGIN
    CREATE TABLE lab93
    (
        lab93c1 TINYINT NOT NULL ,
        lab93c2 TINYINT NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab05c1 INTEGER NOT NULL
    )
END
--ROLESBYUSER
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab84]') )
BEGIN
    CREATE TABLE lab84
    (
        lab84c1 TINYINT NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab82c1 INTEGER NOT NULL
    )
END
--SEGMENT
--Race
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab08]') )
BEGIN
    CREATE TABLE lab08
    (
        lab08c1 INTEGER NOT NULL IDENTITY(1,1),
        lab08c2 VARCHAR (60) NOT NULL ,
        lab08c3 DATETIME NOT NULL ,
        lab08c4 FLOAT NULL,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT,
        lab08c5 VARCHAR(128) NOT NULL
    )

    ALTER TABLE lab08 ADD CONSTRAINT lab08_PK PRIMARY KEY CLUSTERED (lab08c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Specialist
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab09]') )
BEGIN
    CREATE TABLE lab09
    (
        lab09c1 INTEGER NOT NULL IDENTITY(1,1),
        lab09c2 VARCHAR (60) NOT NULL ,
        lab09c3 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )

    ALTER TABLE lab09 ADD CONSTRAINT lab09_PK PRIMARY KEY CLUSTERED (lab09c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Service
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab10]') )
BEGIN
    CREATE TABLE lab10
    (
        lab10c1 INTEGER NOT NULL IDENTITY(1,1),
        lab10c2 VARCHAR (60) NOT NULL ,
        lab10c3 INTEGER  NULL ,
        lab10c4 INTEGER  NULL ,
        lab10c5 TINYINT  NOT NULL ,
        lab10c6 DATETIME NOT NULL ,
        lab10c7   VARCHAR(8) NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )

    ALTER TABLE lab10 ADD CONSTRAINT lab10_PK PRIMARY KEY CLUSTERED (lab10c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Comentario
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab68]') )
BEGIN
    CREATE TABLE lab68
    (
        lab68c1   INTEGER NOT NULL IDENTITY(1,1),
        lab68c2   VARCHAR(32) NOT NULL,
        lab68c3   TEXT NOT NULL,
        lab68c4   DATETIME NOT NULL,
        lab07c1   TINYINT NOT NULL,
        lab68c5   TINYINT NOT NULL,
        lab04c1   INTEGER NOT NULL,
        lab68c6   TINYINT NOT NULL
    );

    ALTER TABLE lab68 ADD CONSTRAINT lab68_PK PRIMARY KEY CLUSTERED (lab68c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--CABECERA DE COTIZACION
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab101')
BEGIN
   CREATE TABLE lab101 (
       lab101c1  INTEGER NOT NULL IDENTITY(1,1),
       lab101c2  VARCHAR,
       lab904c1  FLOAT,
       lab101c3  VARCHAR(100),
       lab101c4  FLOAT,
       lab04c1   INTEGER NOT NULL,          
       lab101c5  DATETIME NOT NULL
    );
ALTER TABLE lab101 ADD CONSTRAINT lab101_pk PRIMARY KEY ( lab101c1 );
END
-- DETALLE DE COTIZACION
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab102')
BEGIN
   CREATE TABLE lab102 (
       lab102c1  INTEGER NOT NULL IDENTITY(1,1),
       lab101c1  INTEGER,
       lab39c1   FLOAT,
       lab904c1  FLOAT,
       lab102c3  FLOAT,
       lab102c4  DATETIME NOT NULL,
       lab04c1   INTEGER NOT NULL
    );
ALTER TABLE lab102 ADD CONSTRAINT lab102_pk PRIMARY KEY ( lab102c1 );
END
--SEGMENT
--Laboratorio
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab40]') )
BEGIN
    CREATE TABLE lab40
    (
        lab40c1 INTEGER NOT NULL IDENTITY(1,1),
        lab40c2 INTEGER NOT NULL ,
        lab40c3 VARCHAR (64) NOT NULL ,
        lab40c4 VARCHAR (128) ,
        lab40c5 VARCHAR (64) ,
        lab40c6 VARCHAR (64) ,
        lab40c7 TINYINT NOT NULL ,
        lab40c8 VARCHAR (256) NOT NULL ,
        lab40C9 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    );

    ALTER TABLE lab40 ADD CONSTRAINT lab40_PK PRIMARY KEY CLUSTERED (lab40c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--RESULTADO LITERAL
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab50]') )
BEGIN
    CREATE TABLE lab50
    (
        lab50c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab50c2 VARCHAR (64) NOT NULL ,
        lab50c3 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab50 ADD CONSTRAINT lab50_PK PRIMARY KEY CLUSTERED (lab50c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON)
END
--SEGMENT
--Grupo Poblacional
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab67]') )
BEGIN
    CREATE TABLE lab67
    (
        lab67c1 INTEGER NOT NULL IDENTITY(1,1),
        lab67c2 VARCHAR (64) NOT NULL ,
        lab67c3 TINYINT NOT NULL,
        lab67c4 TINYINT NOT NULL ,
        lab67c5 INTEGER NOT NULL ,
        lab67c6 INTEGER NOT NULL ,
        lab67C7 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab67 ADD CONSTRAINT lab67_PK PRIMARY KEY CLUSTERED (lab67c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--MEDICO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab19]') )
BEGIN
    CREATE TABLE lab19
    (
        lab19c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab19c2 VARCHAR (64) NOT NULL ,
        lab19c3 VARCHAR (64) ,
        lab19c4 VARCHAR (32) ,
        lab19c5 VARCHAR (32) ,
        lab19c6 VARCHAR (128) ,
        lab19c7 VARCHAR (128) ,
        lab19c8 VARCHAR (32) ,
        lab19c9 VARCHAR (16) ,
        lab19c10 VARCHAR (256) ,
        lab19c11 VARCHAR (128) ,
        lab19c12 VARCHAR (128) ,
        lab19c13 VARCHAR (128) ,
        lab19c14 VARCHAR (2) ,
        lab19c15 VARCHAR (16) ,
        lab19c16 TINYINT NOT NULL ,
        lab19c17 VARCHAR (16) ,
        lab19c18 VARCHAR (16) ,
        lab19c19 VARCHAR (64) NOT NULL ,
        lab19c20 DATETIME NOT NULL ,
        lab19c21 VARCHAR(256) ,
        lab09c1 INTEGER ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL ,
        lab19c22 VARCHAR (128) NOT NULL,
        lab19c23 VARCHAR (128) 
    )

    ALTER TABLE lab19 ADD CONSTRAINT lab19_PK PRIMARY KEY CLUSTERED (lab19c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Modulo
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab85]') )
BEGIN
    CREATE TABLE lab85
    (
        lab85c1   INTEGER NOT NULL,
        lab85c2   INTEGER,
        lab85c3   VARCHAR (128) NOT NULL
    );
END
--SEGMENT
--RELACION DE MODULOS/ROL
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab86]') )
BEGIN
    CREATE TABLE lab86
    (
       lab85c1   INTEGER NOT NULL,
       lab82c1   INTEGER NOT NULL
    );
END
--SEGMENT
--CLIENTES
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab14]') )
BEGIN
    CREATE TABLE lab14
    (
        lab14c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab14c2 VARCHAR (64) NOT NULL ,
        lab14c3 VARCHAR (64) NOT NULL ,
        lab14c4 VARCHAR (20) ,
        lab14c5 VARCHAR (20) ,
        lab14c6 VARCHAR (64) ,
        lab14c7 FLOAT (10) ,
        lab14c8 FLOAT (10) ,
        lab14c9 FLOAT (10) ,
        lab14c10 FLOAT (3) ,
        lab14c11 VARCHAR (256) ,
        lab14c12 TINYINT ,
        lab14c13 VARCHAR (64) ,
        lab14c14 VARCHAR (128) ,
        lab14c15 VARCHAR (128) ,
        lab14c16 VARCHAR (16) ,
        lab14c17 VARCHAR (64) ,
        lab14c18 TINYINT NOT NULL ,
        lab14c19 TINYINT NOT NULL ,
        lab14c20 TINYINT NOT NULL ,
        lab14c21 VARCHAR (128) ,
        lab14c22 TINYINT NOT NULL ,
        lab14c23 TINYINT NOT NULL ,
        lab14c24 VARCHAR (32) ,
        lab14c25 VARCHAR (256) ,
        lab14c26 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL ,
        lab14c28 VARCHAR (64) ,
        lab14c29 VARCHAR (64) ,
        lab14c30 VARCHAR (64) NOT NULL ,
        lab14c31 TINYINT NOT NULL
    )
    ALTER TABLE lab14 ADD CONSTRAINT lab14_PK PRIMARY KEY CLUSTERED (lab14c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--MOTIVO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab30]') )
BEGIN
    CREATE TABLE lab30
    (
        lab30c1 INTEGER NOT NULL IDENTITY(1,1),
        lab30c2 VARCHAR (64) NOT NULL,
        lab30c3 VARCHAR (256) NOT NULL ,
        lab30c4 TINYINT NOT NULL,
        lab30c5 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab30 ADD CONSTRAINT lab30_PK PRIMARY KEY CLUSTERED (lab30c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON)
END
--SEGMENT
--SISTEMA CENTRAL
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab118]') )
BEGIN
    CREATE TABLE lab118
    (
        lab118c1 INTEGER NOT NULL IDENTITY(1,1),
        lab118c2 VARCHAR (64) NOT NULL ,
        lab118c3 TINYINT NOT NULL ,
        lab118c4 DATETIME NOT NULL ,
        lab118c5 TINYINT NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab118 ADD CONSTRAINT lab118_PK PRIMARY KEY CLUSTERED (lab118c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--ALARMAS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab73]') )
BEGIN
    CREATE TABLE lab73
    (
        lab73c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab73c2 VARCHAR (62) NOT NULL ,
        lab73c3 TEXT NOT NULL ,
        lab73c4 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )

    ALTER TABLE lab73 ADD CONSTRAINT lab73_PK PRIMARY KEY CLUSTERED (lab73c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--antibioticos
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab79]') )
BEGIN
    CREATE TABLE lab79
    (
        lab79c1 INTEGER NOT NULL IDENTITY(1,1),
        lab79c2 VARCHAR (62) NOT NULL ,
        lab79c3 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )

    ALTER TABLE lab79 ADD CONSTRAINT lab79_PK PRIMARY KEY CLUSTERED (lab79c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--TARJETA DE CREDITO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab59]') )
BEGIN
    CREATE TABLE lab59
    (
        lab59c1 INTEGER NOT NULL IDENTITY(1,1),
        lab59c2 VARCHAR (64) NOT NULL ,
        lab59c3 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab59 ADD CONSTRAINT lab59_PK PRIMARY KEY CLUSTERED (lab59c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--BANCO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab110]') )
BEGIN
    CREATE TABLE lab110
    (
        lab110c1 INTEGER NOT NULL IDENTITY(1,1),
        lab110c2 VARCHAR (64) NOT NULL ,
        lab110c3 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab110 ADD CONSTRAINT lab110_PK PRIMARY KEY CLUSTERED (lab110c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--NEVERA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab31]') )
BEGIN
    CREATE TABLE lab31
    (
        lab31c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab31c2 VARCHAR (62) NOT NULL ,
        lab31c3 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL,
        lab05c1 INTEGER NOT NULL
    )
    ALTER TABLE lab31 ADD CONSTRAINT lab31_PK PRIMARY KEY CLUSTERED (lab31c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--SITIO ANATOMICO
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab158')
BEGIN
    CREATE TABLE lab158
    (
        lab158c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab158c2 VARCHAR (64) NOT NULL ,
        lab158c3 VARCHAR (16) NOT NULL ,
        lab158c4 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab158 ADD CONSTRAINT lab158_PK PRIMARY KEY CLUSTERED (lab158c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--TAREAS DE MICROBIOLOGIA
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab169')
BEGIN
    CREATE TABLE lab169
    (
        lab169c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab169c2 VARCHAR (64) NOT NULL ,
        lab169c3 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab169 ADD CONSTRAINT lab169_PK PRIMARY KEY CLUSTERED (lab169c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--TARIFA
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab904')
BEGIN
    CREATE TABLE lab904
    (
        lab904c1 INTEGER NOT NULL IDENTITY(1,1) ,
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
        lab904c12 TINYINT NOT NULL ,
        lab904c13 TINYINT NOT NULL ,
        lab904c15 INTEGER ,
        lab904c16 TINYINT NOT NULL ,
        lab904c17 TINYINT NOT NULL ,
        lab904c18 TINYINT NOT NULL ,
        lab904c19 TINYINT NOT NULL ,
        lab904c20 TINYINT NOT NULL ,
        lab904c21 TINYINT NOT NULL ,
        lab904c22 VARCHAR (64) ,
        lab904c23 TINYINT NOT NULL ,
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
        lab904c34 TINYINT NOT NULL ,
        lab904c35 TINYINT NOT NULL ,
        lab904c36 TINYINT NOT NULL ,
        lab904c37 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL ,
        lab904c38 VARCHAR (512)
    )
    ALTER TABLE lab904 ADD CONSTRAINT lab904_PK PRIMARY KEY CLUSTERED (lab904c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--MICROORGANISMOS
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab76')
BEGIN
    CREATE TABLE lab76
    (
        lab76c1 INTEGER NOT NULL IDENTITY(1,1),
        lab76c2 VARCHAR (62) NOT NULL ,
        lab76c3 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab76 ADD CONSTRAINT lab76_PK PRIMARY KEY CLUSTERED (lab76c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--PRUEBA
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab39')
BEGIN
    CREATE TABLE lab39
    (
        lab39c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab43c1 INTEGER ,
        lab39c2 VARCHAR (16) NOT NULL ,
        lab39c3 VARCHAR (16) NOT NULL ,
        lab39c4 VARCHAR (64) NOT NULL ,
        lab39c5 INTEGER ,
        lab24c1 INTEGER ,
        lab44c1 INTEGER ,
        lab39c6 INTEGER ,
        lab39c7 INTEGER ,
        lab39c8 INTEGER ,
        lab39c9 TINYINT ,
        lab39c10 VARCHAR (256) ,
        lab39c11 TINYINT ,
        lab39c12 TINYINT ,
        lab64c1 INTEGER ,
        lab45c1 INTEGER ,
        lab39c13 VARCHAR (16) ,
        lab39c14 VARCHAR (128) ,
        lab39c15 TINYINT ,
        lab39c16 TINYINT NOT NULL ,
        lab39c17 VARCHAR (8) ,
        lab39c18 INTEGER NOT NULL ,
        lab39c19 TINYINT NOT NULL ,
        lab39c20 TINYINT NOT NULL ,
        lab39c21 VARCHAR (64) ,
        lab39c22 INTEGER ,
        lab39c23 TINYINT NOT NULL ,
        lab39c24 TINYINT NOT NULL ,
        lab39c25 TINYINT NOT NULL ,
        lab39c26 TINYINT NOT NULL ,
        lab39c27 TINYINT NOT NULL ,
        lab39c28 TINYINT NOT NULL ,
        lab39c29 TINYINT NOT NULL ,
        lab39c30 TINYINT NOT NULL ,
        lab39c31 TINYINT NOT NULL ,
        lab39c32 VARCHAR (64) ,
        lab39c33 TEXT ,
        lab39c34 TEXT ,
        lab39c35 TEXT ,
        lab39c36 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL ,
        lab39c37 TINYINT NOT NULL ,
        lab39c38 TINYINT NOT NULL ,
        lab39c39 TINYINT NOT NULL ,
        lab39c40 TINYINT NOT NULL ,
        lab39c41 INTEGER,
        lab39c42 INTEGER,
        lab39c43 FLOAT,
        lab39c44   INTEGER,
        lab39c45   FLOAT,
        lab39c46   FLOAT,
        lab39c47 TINYINT,
        lab39c48 BIGINT,
        lab39c49   FLOAT
    )

    ALTER TABLE lab39 ADD CONSTRAINT lab39_PK PRIMARY KEY CLUSTERED (lab39c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--REQUISITOS POR PRUEBA
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab71')
BEGIN
    CREATE TABLE lab71
    (
        lab41c1 INTEGER NOT NULL ,
        lab39c1 INTEGER NOT NULL
    )
    ALTER TABLE lab71 ADD CONSTRAINT lab71_PK PRIMARY KEY CLUSTERED (lab41c1, lab39c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--CONCURRENCIAS
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab46')
BEGIN
    CREATE TABLE lab46
    (
        lab46c1 INTEGER NOT NULL ,
        lab39c1 INTEGER NOT NULL ,
        lab46c2 TINYINT NOT NULL
    )
END
--SEGMENT
--DIAGNOSTICO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab20]') )
BEGIN
    CREATE TABLE lab20
    (
        lab20c1 INTEGER NOT NULL IDENTITY(1,1),
        lab20c2 VARCHAR (8) NOT NULL ,
        lab20c3 VARCHAR (256) NOT NULL ,
        lab20C4 DATETIME NOT NULL ,
        lab20c5 TINYINT NOT NULL,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab20 ADD CONSTRAINT lab20_PK PRIMARY KEY CLUSTERED (lab20c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--ANTIBIOGRAMA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab77]') )
BEGIN
    CREATE TABLE lab77
    (
        lab77c1 INTEGER NOT NULL IDENTITY(1,1),
        lab77c2 VARCHAR (12) NOT NULL ,
        lab77c3 NVARCHAR (64) ,
        lab77c4 NVARCHAR (64) ,
        lab77c5 DATETIME NOT NULL ,
        lab77c7 TINYINT NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab77 ADD CONSTRAINT lab77_PK PRIMARY KEY CLUSTERED (lab77c1)WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--ANTIBIOGRAMA-ANTIBIOTICO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab78]') )
BEGIN
    CREATE TABLE lab78
    (
        lab77c1 INTEGER NOT NULL ,
        lab79c1 INTEGER NOT NULL ,
        lab45c1 INTEGER ,
        lab78c2 INTEGER
    )
    ALTER TABLE lab78 ADD CONSTRAINT lab77v1_PK PRIMARY KEY CLUSTERED (lab77c1, lab79c1)WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--PACIENTE
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab21]') )
BEGIN
    CREATE TABLE lab21
    (
        lab21c1 INTEGER NOT NULL IDENTITY(0,1),
        lab21c2 VARCHAR (64) NOT NULL ,
        lab21c3 VARCHAR (128) NOT NULL ,
        lab21c4 VARCHAR (128) ,
        lab21c5 VARCHAR (128) NOT NULL ,
        lab21c6 VARCHAR (128) ,
        lab80c1 INTEGER ,
        lab21c7 DATETIME ,
        lab21c8 VARCHAR (64) ,
        lab21c9 FLOAT ,
        lab21c10 FLOAT ,
        lab21c11 DATETIME ,
        lab21c12 DATETIME NOT NULL ,
        lab21c13 VARCHAR (4096) ,
        lab21c14 TEXT ,
        lab04c1 INTEGER NOT NULL ,
        lab08c1 INTEGER ,
        lab54c1 INTEGER ,
        lab21c15 TINYINT ,
        lab21c16 VARCHAR (32) ,
        lab21c17 VARCHAR (128) ,
        lab21c18 VARCHAR (32) ,
        lab21c19 VARCHAR (256)
    )

    ALTER TABLE lab21 ADD CONSTRAINT lab21_PK PRIMARY KEY CLUSTERED (lab21c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--DIAGNOSTICO PERMANENTE DE PACIENTE
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab28]') )
BEGIN
    CREATE TABLE lab28
    (
        lab28c1 INTEGER NOT NULL IDENTITY(1,1),
        lab21c1 INTEGER NOT NULL ,
        lab28c2 VARCHAR (4096) ,
        lab28c3 DATETIME ,
        lab04c1 INTEGER NOT NULL
    )
    ALTER TABLE lab28 ADD CONSTRAINT lab28_PK PRIMARY KEY CLUSTERED (lab28c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--ORDEN
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab22]') )
BEGIN
    CREATE TABLE lab22
    (
        lab22c1 BIGINT NOT NULL ,
        lab22c2 INTEGER NOT NULL ,
        lab103c1 INTEGER NOT NULL ,
        lab22c3 DATETIME NOT NULL ,
        lab21c1 INTEGER NOT NULL ,
        lab22c4 TINYINT ,
        lab22c5 INTEGER ,
        lab22c6 DATETIME ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 INTEGER NOT NULL ,
        lab22c7 VARCHAR (256) ,
        lab05c1 INTEGER ,
        lab10c1 INTEGER ,
        lab19c1 INTEGER ,
        lab14c1 INTEGER ,
        lab904c1 INTEGER ,
        lab22c8 INTEGER NOT NULL,
        lab22c9 TINYINT ,
        lab22c10 TINYINT ,
        lab22c11 BIGINT ,
        lab57c9 TINYINT ,
        lab22c12 INTEGER
    )
    CREATE INDEX lab22c2__IDX ON lab22 (lab22c2)
    ALTER TABLE lab22 ADD CONSTRAINT lab22_PK PRIMARY KEY CLUSTERED (lab22c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--COMENTARIOS DE LA ORDEN
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab60]') )
BEGIN
    CREATE TABLE lab60
    (
        lab60c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab60c2 BIGINT NOT NULL ,
        lab60c3 TEXT NOT NULL ,
        lab60c4 TINYINT NOT NULL ,
        lab60c5 DATETIME NOT NULL ,
        lab60c6 TINYINT NOT NULL ,
        lab04c1 INTEGER NOT NULL
    )
    ALTER TABLE lab60 ADD CONSTRAINT lab60_PK PRIMARY KEY CLUSTERED (lab60c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON)
END
--SEGMENT
--RESULTADOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab57]') )
BEGIN
    CREATE TABLE lab57
    (
        lab22c1 BIGINT NOT NULL ,
        lab39c1 INTEGER NOT NULL ,
        lab57c1 VARCHAR (16) ,
        lab57c2 DATETIME ,
        lab57c3 INTEGER ,
        lab57c4 DATETIME NOT NULL ,
        lab57c5 INTEGER ,
        lab57c6 VARCHAR (16) ,
        lab57c7 DATETIME ,
        lab57c8 TINYINT ,
        lab57c9 TINYINT ,
        lab57c10 TINYINT ,
        lab57c11 DATETIME ,
        lab57c12 INTEGER ,
        lab57c14 INTEGER ,
        lab57c15 INTEGER ,
        lab57c16 TINYINT ,
        lab57c17 INTEGER ,
        lab57c18 DATETIME ,
        lab57c19 INTEGER ,
        lab57c20 DATETIME ,
        lab57c21 INTEGER ,
        lab57c22 DATETIME ,
        lab57c23 INTEGER ,
        lab45c2 VARCHAR (64) ,
        lab64c1 INTEGER ,
        lab48c1 INTEGER ,
        lab57c24 TINYINT ,
        lab57c25 TINYINT ,
        lab57c26 INTEGER ,
        lab57c27 FLOAT ,
        lab57c28 FLOAT ,
        lab40c1 INTEGER NOT NULL ,
        lab57c29 TINYINT ,
        lab05c1 INTEGER ,
        lab48c5 FLOAT ,
        lab48c6 FLOAT ,
        lab48c12 FLOAT ,
        lab48c13 FLOAT ,
        lab50c1_1 INTEGER ,
        lab50c1_3 INTEGER ,
        lab48c14 FLOAT ,
        lab48c15 FLOAT ,
        lab57c30 VARCHAR (16) ,
        lab57c31 DATETIME ,
        lab57c32 TINYINT ,
        lab57c33 INTEGER ,
        lab57c34 INTEGER ,
        lab57c35 TINYINT,
        lab57c36 TINYINT,
        lab57c37 DATETIME,
        lab57c38 INTEGER,
        lab57c39 DATETIME,
        lab57c40 INTEGER,
        lab24c1 INTEGER ,
        lab24c1_1 INTEGER ,
        lab158c1 INTEGER ,
        lab201c1 INTEGER ,
        lab57c41 INTEGER ,
        lab57c42 TINYINT
    )
    ALTER TABLE lab57 ADD CONSTRAINT lab57_PK PRIMARY KEY CLUSTERED (lab22c1, lab39c1) WITH (ALLOW_PAGE_LOCKS = ON ,ALLOW_ROW_LOCKS = ON)
END
--SEGMENT
--COMENTARIOS DEL RESULTADO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab95]') )
BEGIN
    CREATE TABLE lab95
    (
        lab22c1 BIGINT NOT NULL ,
        lab39c1 INTEGER NOT NULL ,
        lab95c1 VARCHAR (4096) ,
        lab95c2 DATETIME NOT NULL ,
        lab95c3 TINYINT NOT NULL ,
        lab95c4 TINYINT NOT NULL ,
        lab95c5 VARCHAR (4096) ,
        lab04c1 INTEGER NOT NULL
    )
    ALTER TABLE lab95 ADD CONSTRAINT lab95_PK PRIMARY KEY CLUSTERED (lab22c1, lab39c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON)
END
--SEGMENT
--COMENTARIOS DEL RESULTADO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab905]') )
BEGIN
CREATE TABLE lab905
    (
        lab905c1 INTEGER NOT NULL IDENTITY(1,1),
        lab905c2 VARCHAR (64) NOT NULL ,
        lab905c3 VARCHAR (64) NOT NULL ,
        lab905c4 VARCHAR (64) ,
        lab905c5 VARCHAR (64) NOT NULL ,
        lab905c6 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 INTEGER NOT NULL
    )
    ALTER TABLE lab905 ADD CONSTRAINT lab905_PK PRIMARY KEY CLUSTERED (lab905c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--EXAMEN POR LABORATORIO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab145]') )
BEGIN
    CREATE TABLE lab145
    (
        lab39c1 INTEGER NOT NULL ,
        lab05c1 INTEGER NOT NULL ,
        lab145c1 INTEGER NOT NULL ,
        lab40c1 INTEGER NOT NULL
    )
    ALTER TABLE lab145 ADD CONSTRAINT lab145_PK PRIMARY KEY CLUSTERED (lab39c1, lab05c1, lab145c1, lab40c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--RESULTADO POR EXAMEN
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab49]') )
BEGIN
    CREATE TABLE lab49
    (
        lab50c1 INTEGER NOT NULL ,
        lab39c1 INTEGER NOT NULL
    )

    ALTER TABLE lab49 ADD CONSTRAINT lab49_PK PRIMARY KEY CLUSTERED (lab50c1, lab39c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--EXAMEN POR LABORATORIO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab48]') )
BEGIN
    CREATE TABLE lab48
    (
        lab48c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab39c1 INTEGER NOT NULL ,
        lab48c2 TINYINT ,
        lab48c3 INTEGER ,
        lab48c4 INTEGER ,
        lab48c5 FLOAT ,
        lab48c6 FLOAT ,
        lab48c9 TEXT ,
        lab08c1 INTEGER ,
        lab48c10 INTEGER ,
        lab04c1 INTEGER NOT NULL ,
        lab48c11 DATETIME NOT NULL ,
        lab48c12 FLOAT ,
        lab48c13 FLOAT ,
        lab50c1_1 INTEGER ,
        lab50c1_3 INTEGER ,
        lab48c14 FLOAT ,
        lab48c15 FLOAT ,
        lab48c16 TINYINT NOT NULL
    )
    ALTER TABLE lab48 ADD CONSTRAINT lab48_PK PRIMARY KEY CLUSTERED (lab48c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--HOMOLOGACION PRUEBAS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab61]') )
BEGIN
    CREATE TABLE lab61
    (
     lab118c1 INTEGER NOT NULL ,
     lab39c1 INTEGER NOT NULL ,
     lab61c1 VARCHAR(16) NOT NULL
    )
    ALTER TABLE lab61 ADD CONSTRAINT lab61_PK PRIMARY KEY CLUSTERED (lab118c1, lab39c1, lab61c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--DESTINOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab53]') )
BEGIN
    CREATE TABLE lab53
    (
        lab53c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab53c2 VARCHAR (16) NOT NULL ,
        lab53c3 VARCHAR (64) NOT NULL ,
        lab53c4 VARCHAR (256) ,
        lab53c5 TINYINT NOT NULL ,
        lab53c6 VARCHAR (8) ,
        lab53c7 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab53 ADD CONSTRAINT lab53_PK PRIMARY KEY CLUSTERED (lab53c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--PRUEBA AUTOMATICA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab47]') )
BEGIN
    CREATE TABLE lab47
    (
        lab39c1_1 INTEGER NOT NULL ,
        lab47c1 INTEGER NOT NULL ,
        lab47c2 VARCHAR(16) NOT NULL ,
        lab47c3 VARCHAR(16) ,
        lab39c1_2 INTEGER NOT NULL
    )
END
--SEGMENT
--RELACIÃ“N RESULTADOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab74]') )
BEGIN
    CREATE TABLE lab74
    (
        lab74c1 INTEGER ,
        lab74c2 VARCHAR (16) ,
        lab74c3 VARCHAR (16) NOT NULL ,
        lab74c4 VARCHAR (16) ,
        lab74c5 DATETIME ,
        lab74c6 INTEGER NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab73c1 INTEGER NOT NULL
    )
END
--SEGMENT
--HOJA DE TRABAJO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab37]') )
BEGIN
    CREATE TABLE lab37
    (
        lab37c1 INTEGER NOT NULL IDENTITY(1,1),
        lab37c2 VARCHAR (64) NOT NULL ,
        lab37c3 TINYINT ,
        lab37c4 TINYINT ,
        lab37c5 TINYINT NOT NULL ,
        lab37c6 TINYINT NOT NULL ,
        lab37c7 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab37 ADD CONSTRAINT lab37_PK PRIMARY KEY CLUSTERED (lab37c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--HOJA DE TRABAJO - EXAMEN
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab38]') )
BEGIN
    CREATE TABLE lab38
    (
        lab37c1 INTEGER NOT NULL ,
        lab39c1 INTEGER NOT NULL
    )
END
--SEGMENT
--PLANTILLA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab51]') )
BEGIN
    CREATE TABLE lab51
    (
        lab51c1 INTEGER NOT NULL IDENTITY(1,1),
        lab51c2 VARCHAR (64) NOT NULL ,
        lab51c3 VARCHAR (512) ,
        lab51c4 TINYINT NOT NULL ,
        lab39c1 INTEGER NOT NULL
    )
    ALTER TABLE lab51 ADD CONSTRAINT lab51_PK PRIMARY KEY CLUSTERED (lab51c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--OPCIONES PLANTILLA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab146]') )
BEGIN
    CREATE TABLE lab146
    (
        lab146c1 VARCHAR (512) NOT NULL ,
        lab146c2 TINYINT NOT NULL ,
        lab146c3 TINYINT NOT NULL ,
        lab51c1 INTEGER NOT NULL
    )
END
--SEGMENT
--CONTADOR HEMATOLOGICO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab18]') )
BEGIN
    CREATE TABLE lab18
    (
        lab18c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab18c2 VARCHAR (1) NOT NULL ,
        lab18c3 VARCHAR (128) NOT NULL ,
        lab18c4 TINYINT NOT NULL ,
        lab18c5 TINYINT NOT NULL ,
        lab39c1 INTEGER ,
        lab18c6 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab18 ADD CONSTRAINT lab18_PK PRIMARY KEY CLUSTERED (lab18c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Tarifas por cliente
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab15]') )
BEGIN
    CREATE TABLE lab15
    (
           lab14c1    INTEGER NOT NULL,
           lab904c1   INTEGER NOT NULL
    )
END
--SEGMENT
--HOMOLOGACION DE DEMOGRAFICOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab117]') )
BEGIN
    CREATE TABLE lab117
    (
        lab118c1 INTEGER NOT NULL ,
        lab117c1 INTEGER NOT NULL ,
        lab117c2 INTEGER NOT NULL ,
        lab117c3 VARCHAR (128) NOT NULL
    )
END
--SEGMENT
--EXCLUIR EXAMEN X PRUEBA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab104]') )
BEGIN
    CREATE TABLE lab104
    (
        lab104c1 INTEGER NOT NULL ,
        lab104c2 INTEGER NOT NULL ,
        lab39c1 INTEGER NOT NULL
    )
END

--SEGMENT
--EXCLUIR EXAMEN POR USUARIO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab72]') )
BEGIN
    CREATE TABLE Lab72
    (
        lab39c1 INTEGER NOT NULL ,
        lab04c1 INTEGER NOT NULL
    )
END
--SEGMENT
--Dï¿½?AS POR ALARMA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab178]') )
BEGIN
    CREATE TABLE Lab178
    (
        Lab178c1 INTEGER NOT NULL ,
        Lab178c2 INTEGER ,
        Lab39c1 INTEGER NOT NULL ,
        Lab178c3 INTEGER NOT NULL
    )
END
--SEGMENT
--Pruebas por PyP
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab175]') )
BEGIN
    CREATE TABLE lab175
    (
        lab175c1 INTEGER NOT NULL ,
        lab175c2 INTEGER ,
        lab175c3 INTEGER ,
        lab175c4 TINYINT ,
        lab175c5 INTEGER ,
        lab39c1 INTEGER NOT NULL
    )
    ALTER TABLE lab175 ADD CONSTRAINT lab175_PK PRIMARY KEY CLUSTERED (lab175c1, lab39c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--VIGENCIAS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab116]') )
BEGIN
    CREATE TABLE lab116 (
        lab116c1   INTEGER NOT NULL IDENTITY(1,1),
        lab116c2   VARCHAR (60) NOT NULL,
        lab116c3   DATE NOT NULL,
        lab116c4   DATE NOT NULL,
        lab116c5   TINYINT NOT NULL,
        lab07c1    TINYINT NOT NULL,
        lab116c7   DATETIME NOT NULL,
        lab04c1    INTEGER NOT NULL
    );

    ALTER TABLE lab116 ADD CONSTRAINT lab116_PK PRIMARY KEY CLUSTERED (lab116c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--TIEMPOS DE PROCESO (MUESTRAS POR SERVICIO O PRUEBAS POR SERVICIO)
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab171]') )
BEGIN
    CREATE TABLE lab171
    (
        lab10c1 INTEGER NOT NULL ,
        lab171c1 INTEGER NOT NULL ,
        lab171c2 INTEGER NOT NULL ,
        lab171c3 TINYINT NOT NULL ,
        lab171c4 INTEGER
    )
END
--SEGMENT
--MEDIO DE CULTIVO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab155]') )
BEGIN
    CREATE TABLE lab155 (
        lab155c1   INTEGER NOT NULL IDENTITY(1,1),
        lab155c2   VARCHAR (10) NOT NULL,
        lab155c3   VARCHAR (60) NOT NULL,
        lab07c1    TINYINT NOT NULL,
        lab155c4   DATETIME NOT NULL,
        lab04c1    INTEGER NOT NULL
    );

    ALTER TABLE lab155 ADD CONSTRAINT lab155_PK PRIMARY KEY CLUSTERED (lab155c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--PROCEDIMIENTO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab156]') )
BEGIN
    CREATE TABLE lab156
    (
        lab156c1 INTEGER NOT NULL IDENTITY(1,1),
        lab156c2 VARCHAR (16) NOT NULL ,
        lab156c3 VARCHAR (64) NOT NULL ,
        lab156c4 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab156 ADD CONSTRAINT lab156_PK PRIMARY KEY CLUSTERED (lab156c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Relacion de prueba-Medio de cultivo
 IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab164]') )
 BEGIN
    CREATE TABLE lab164
    (
        lab39c1 INTEGER NOT NULL ,
        lab155c1 INTEGER NOT NULL ,
        lab164c1 TINYINT NOT NULL
    )

    ALTER TABLE lab164 ADD CONSTRAINT lab164_PK PRIMARY KEY CLUSTERED (lab39c1, lab155c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--PREGUNTAS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab70]') )
BEGIN
    CREATE TABLE lab70
    (
        lab70c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab70c2 VARCHAR (64) NOT NULL ,
        lab70c3 VARCHAR (512) NOT NULL ,
        lab70c4 TINYINT NOT NULL ,
        lab70c5 INTEGER NOT NULL ,
        lab70c6 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab70 ADD CONSTRAINT lab70_PK PRIMARY KEY CLUSTERED (lab70c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--PREGUNTAS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab90]') )
BEGIN
    CREATE TABLE lab90
    (
        lab90c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab90c2 VARCHAR (64) NOT NULL ,
        lab90c3 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab90 ADD CONSTRAINT lab90_PK PRIMARY KEY CLUSTERED (lab90c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--PREGUNTAS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab91]') )
BEGIN
    CREATE TABLE lab91
    (
        lab70c1 INTEGER NOT NULL ,
        lab90c1 INTEGER NOT NULL
    )
END
--SEGMENT
--Relacion de prueba-Medio de cultivo
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab157]') )
BEGIN
    CREATE TABLE lab157
    (
        lab39c1 INTEGER NOT NULL ,
        lab156c1 INTEGER NOT NULL ,
        lab157c1 TINYINT ,
        lab157c2 INTEGER
    )

    ALTER TABLE lab157 ADD CONSTRAINT lab157_PK PRIMARY KEY CLUSTERED (lab39c1, lab156c1) WITH (ALLOW_PAGE_LOCKS = ON ,ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Precios Vigencia
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab120]') )
BEGIN
    CREATE TABLE lab120
    (
        lab39c1 INTEGER NOT NULL ,
        lab904c1 INTEGER NOT NULL ,
        lab116c1 INTEGER NOT NULL ,
        lab120c1 FLOAT NOT NULL
    )
    ALTER TABLE lab120 ADD CONSTRAINT lab120_PK PRIMARY KEY CLUSTERED (lab39c1, lab904c1, lab116c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Precios Vigencia aplicada
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab55]') )
BEGIN
    CREATE TABLE lab55
    (
        lab39c1 INTEGER NOT NULL ,
        lab904c1 INTEGER NOT NULL ,
        lab55c1 FLOAT NOT NULL
    )
    ALTER TABLE lab55 ADD CONSTRAINT lab55_PK PRIMARY KEY CLUSTERED (lab39c1, lab904c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--ENTREVISTA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab44]') )
BEGIN
    CREATE TABLE lab44
    (
        lab44c1 INTEGER NOT NULL IDENTITY(1,1),
        lab44c2 VARCHAR (60) NOT NULL ,
        lab44c3 TINYINT NOT NULL ,
        lab44c4 TINYINT NOT NULL ,
        lab07c1 TINYINT NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab44c5 DATETIME NOT NULL
    )

    ALTER TABLE lab44 ADD CONSTRAINT lab44_PK PRIMARY KEY CLUSTERED (lab44c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--ENTREVISTA-PREGUNTA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab92]') )
BEGIN
    CREATE TABLE lab92
    (
        lab44c1 INTEGER NOT NULL ,
        lab70c1 INTEGER NOT NULL ,
        lab92c1 TINYINT NOT NULL
    )

    ALTER TABLE lab92 ADD CONSTRAINT lab92_PK PRIMARY KEY CLUSTERED (lab44c1,lab70c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--ENTREVISTA-TIPO DE ENTREVISTA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab66]') )
BEGIN
    CREATE TABLE lab66
    (
        lab44c1 INTEGER NOT NULL ,
        lab66c1 INTEGER NOT NULL
    )

    ALTER TABLE lab66 ADD CONSTRAINT lab66_PK PRIMARY KEY CLUSTERED (lab44c1, lab66c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--ASIGNACION DE DESTINOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab52]') )
BEGIN
    CREATE TABLE lab52
    (
        lab52c1 INTEGER NOT NULL IDENTITY(1,1),
        lab103c1 INTEGER NOT NULL ,
        lab05c1 INTEGER ,
        lab24c1 INTEGER NOT NULL ,
        lab52c2 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab52 ADD CONSTRAINT lab52_PK PRIMARY KEY CLUSTERED (lab52c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--RUTA DE DESTINOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab42]') )
BEGIN
    CREATE TABLE lab42
    (
        lab42c1 INTEGER NOT NULL IDENTITY(1,1),
        lab53c1 INTEGER NOT NULL ,
        lab52c1 INTEGER NOT NULL ,
        lab42c2 INTEGER NOT NULL
    )
    ALTER TABLE lab42 ADD CONSTRAINT lab42_PK PRIMARY KEY CLUSTERED (lab42c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--RUTA DE DESTINOS - EXAMENES
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab87]') )
BEGIN
    CREATE TABLE lab87
    (
        lab42c1 INTEGER NOT NULL ,
        lab39c1 INTEGER NOT NULL
    )
END
--SEGMENT
--SUB-MUESTRAS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab97]') )
BEGIN
    CREATE TABLE lab97
    (
        lab24c1 INTEGER NOT NULL ,
        lab97c1 INTEGER NOT NULL
    )

    ALTER TABLE lab97 ADD CONSTRAINT lab97_PK PRIMARY KEY CLUSTERED (lab24c1, lab97c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--HOMOLOGACION DE USUARIOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab96]') )
BEGIN
    CREATE TABLE lab96
    (
        lab04c1 INTEGER NOT NULL ,
        lab118c1 INTEGER NOT NULL ,
        lab96c1 VARCHAR (64) NOT NULL
    )
END
--SEGMENT
--TIPO DE DOCUMENTO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab54]') )
BEGIN
    CREATE TABLE lab54
    (
        lab54c1 INTEGER NOT NULL IDENTITY(1,1),
        lab54c2 VARCHAR (16) NOT NULL ,
        lab54c3 VARCHAR (64) NOT NULL ,
        lab54c4 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab54 ADD CONSTRAINT lab54_PK PRIMARY KEY CLUSTERED (lab54c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--TIPO DE PAGO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab01]') )
BEGIN
    CREATE TABLE lab01
    (
        lab01c1 INTEGER NOT NULL IDENTITY(1,1),
        lab01c2 VARCHAR (64) NOT NULL ,
        lab01c3 TINYINT NOT NULL ,
        lab01c4 TINYINT NOT NULL ,
        lab01c5 TINYINT NOT NULL ,
        lab01c6 TINYINT NOT NULL ,
        lab01c7 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )

    ALTER TABLE lab01 ADD CONSTRAINT lab01_PK PRIMARY KEY CLUSTERED (lab01c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--FESTIVOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab81]') )
BEGIN
    CREATE TABLE lab81
    (
        lab81c1 INTEGER NOT NULL IDENTITY(1,1),
        lab81c2 VARCHAR (64) NOT NULL ,
        lab81c3 DATETIME NOT NULL ,
        lab81c4 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab81 ADD CONSTRAINT lab81_PK PRIMARY KEY CLUSTERED (lab81c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--OPORTUNIDAD DE LA MUESTRA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab83]') )
BEGIN
    CREATE TABLE lab83
    (
        lab10c1 INTEGER NOT NULL ,
        lab42c1 INTEGER NOT NULL ,
        lab83c1 INTEGER NOT NULL ,
        lab83c2 INTEGER NOT NULL
    )
END
--SEGMENT
--RECHAZO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab144]') )
BEGIN
    CREATE TABLE lab144
    (
        lab22c1 BIGINT NOT NULL ,
        lab24c1 INTEGER NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab30c1 INTEGER ,
        lab05c1 INTEGER NOT NULL ,
        lab144c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab144c2 DATETIME NOT NULL ,
        lab144c3 TINYINT NOT NULL ,
        lab144c4 TEXT
    )
    ALTER TABLE lab144 ADD CONSTRAINT lab144_PK PRIMARY KEY CLUSTERED (lab144c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--ACCESOS DIRECTOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab125]') )
BEGIN
    CREATE TABLE lab125
    (
        lab125c1 INTEGER NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab85c1 INTEGER NOT NULL
    )
    ALTER TABLE lab125 ADD CONSTRAINT lab125_PK PRIMARY KEY CLUSTERED (lab125c1, lab04c1, lab85c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON)
END
--SEGMENT
--ALERTA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab12]') )
BEGIN
    CREATE TABLE lab12
    (
        lab12c1   VARCHAR(64) NOT NULL,
        lab12c2   VARCHAR(512) NOT NULL,
        lab12c3   INTEGER NOT NULL,
        lab12c4   VARCHAR(64) NOT NULL,
        lab12c5   VARCHAR(64) ,
        lab12c6   INTEGER NOT NULL
    )
END
--SEGMENT
--FLUJO DE LA MUESTRA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab25]') )
BEGIN
    CREATE TABLE lab25
    (
        lab22c1 BIGINT NOT NULL ,
        lab42c1 INTEGER NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab25c1 DATETIME NOT NULL ,
        lab25c2 TINYINT NOT NULL
    )
    ALTER TABLE lab25 ADD CONSTRAINT lab25_PK PRIMARY KEY CLUSTERED (lab22c1, lab42c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON)
END
--SEGMENT
--ENTREVISTA - ORDEN
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab23]') )
BEGIN
    CREATE TABLE lab23
    (
        lab22c1 BIGINT NOT NULL ,
        lab70c1 INTEGER NOT NULL ,
        lab90c1 INTEGER ,
        lab23c1 VARCHAR(8000) ,
        lab23c2 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL
    )
END
--SEGMENT
--Hoja de Impresion para Hojas de Trabajo
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab32]') )
BEGIN
    CREATE TABLE lab32
    (
        lab22c1 BIGINT NOT NULL ,
        lab39c1 INTEGER NOT NULL ,
        lab37c1 INTEGER NOT NULL ,
        lab32c1 INTEGER ,
        lab32c2 DATETIME ,
        lab04c1   INTEGER NOT NULL
    )
    ALTER TABLE lab32 ADD CONSTRAINT lab32_PK PRIMARY KEY CLUSTERED (lab22c1, lab39c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--ESTADO DE LA MUESTRA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab159]') )
BEGIN
    CREATE TABLE lab159
    (
        lab22c1 BIGINT NOT NULL ,
        lab24c1 INTEGER NOT NULL ,
        lab159c1 INTEGER NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab159c2 DATETIME NOT NULL ,
        lab53c1 INTEGER ,
        lab05c1 INTEGER NOT NULL,
        lab52c1    INTEGER
    )
    ALTER TABLE lab159 ADD CONSTRAINT lab159_PK PRIMARY KEY CLUSTERED (lab24c1, lab22c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--ESTADO DE LA MUESTRA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab160]') )
BEGIN
    CREATE TABLE lab160
    (
        lab144c1 INTEGER NOT NULL ,
        lab39c1 INTEGER NOT NULL
    )

    ALTER TABLE lab160 ADD CONSTRAINT lab160_PK PRIMARY KEY CLUSTERED (lab144c1, lab39c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--EXAMEN X FILTRO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab02]') )
BEGIN
    CREATE TABLE lab02
    (
        lab65c1 INTEGER NOT NULL ,
        lab39c1 INTEGER NOT NULL
    )
    ALTER TABLE lab02 ADD CONSTRAINT lab02_PK PRIMARY KEY CLUSTERED (lab65c1, lab39c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--FILTRO EXAMENES
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab65]') )
BEGIN
    CREATE TABLE lab65
    (
        lab65c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab65c2 VARCHAR (8) NOT NULL ,
        lab65c3 VARCHAR (64) NOT NULL ,
        lab65c4 DATE ,
        lab04c1 INTEGER ,
        lab07c1 INTEGER
    )
    ALTER TABLE lab65 ADD CONSTRAINT lab65_PK PRIMARY KEY CLUSTERED (lab65c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--FILTRO EXAMENES
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab03]') )
BEGIN
    CREATE TABLE lab03
    (
        lab22c1 BIGINT NOT NULL ,
        lab03c1 INTEGER ,
        lab03c2 VARCHAR (4) NOT NULL ,
        lab03c3 VARCHAR (4) NOT NULL ,
        lab03c4 TEXT NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab03c5 DATETIME NOT NULL ,
        lab30c1 INTEGER ,
        lab03c6 TEXT
    )
END
--SEGMENT
--CONCURRENCIA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab113]') )
BEGIN
    CREATE TABLE lab113
    (
        lab113c1 TINYINT NOT NULL ,
        lab113c2 INTEGER ,
        lab113c3 VARCHAR (32) NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab113c4 DATETIME NOT NULL
    )
END
--SEGMENT
--INCONSISTENCIAS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab33]') )
BEGIN
    CREATE TABLE lab33
    (
        lab22c1 BIGINT NOT NULL ,
        lab33c1 TEXT NOT NULL ,
        lab33c2 TEXT NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab33c3 DATETIME NOT NULL ,
        lab33c4 VARCHAR(128)
    )

    ALTER TABLE lab33 ADD CONSTRAINT lab33_PK PRIMARY KEY CLUSTERED (lab22c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--HISTORICO RESULTADO - REPETICIONES
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab29]') )
BEGIN
    CREATE TABLE lab29
    (
        lab29c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab22c1 BIGINT NOT NULL ,
        lab39c1 INTEGER NOT NULL ,
        lab29c2 VARCHAR (128) ,
        lab29c3 DATETIME NOT NULL ,
        lab29c4 TINYINT NOT NULL ,
        lab04c1_1 INTEGER NOT NULL ,
        lab29c5 CHAR (1) NOT NULL ,
        lab30c1 INTEGER NOT NULL ,
        lab29c7 TEXT ,
        lab29c8 DATETIME NOT NULL ,
        lab04c1_2 INTEGER NOT NULL ,
        lab29c9 TINYINT NOT NULL
    )

    ALTER TABLE lab29 ADD CONSTRAINT lab29_PK PRIMARY KEY CLUSTERED (lab29c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--CONTROL ENTREGA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab167]') )
BEGIN
    CREATE TABLE lab167
    (
        lab167c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab22c1 BIGINT NOT NULL ,
        lab167c2 VARCHAR (256) ,
        lab04c1 INTEGER ,
        lab167c3 DATETIME
    )
    ALTER TABLE lab167 ADD CONSTRAINT lab167_PK PRIMARY KEY CLUSTERED (lab167c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--EXAMENES X ENTREGA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab168]') )
BEGIN
    CREATE TABLE lab168
    (
        lab167c1 INTEGER NOT NULL ,
        lab39c1 INTEGER NOT NULL
    )
    ALTER TABLE lab168 ADD CONSTRAINT lab168_PK PRIMARY KEY CLUSTERED (lab167c1, lab39c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--WIDGET - MUESTRAS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab300]') )
BEGIN
    CREATE TABLE lab300
    (
        lab300c1 INTEGER NOT NULL ,
        lab05c1 INTEGER NOT NULL ,
        lab300c2 INTEGER NOT NULL ,
        lab300c3 INTEGER NOT NULL ,
        lab300c4 INTEGER NOT NULL ,
        lab300c5 INTEGER NOT NULL ,
        lab300c6 INTEGER NOT NULL ,
        lab300c7 INTEGER NOT NULL
    )
    ALTER TABLE lab300 ADD CONSTRAINT lab300_PK PRIMARY KEY CLUSTERED (lab300c1, lab05c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--SIEMBRA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab200]') )
BEGIN
    CREATE TABLE lab200 (
        lab22c1     BIGINT NOT NULL,
        lab39c1     INTEGER NOT NULL,
        lab200c2    DATETIME NOT NULL,
        lab04c1     INTEGER NOT NULL,
        lab200c3    DATETIME,
        lab04c1_1   INTEGER
    )

    ALTER TABLE lab200 ADD CONSTRAINT lab200_PK PRIMARY KEY CLUSTERED (lab22c1, lab39c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--METODO DE RECOLECCION
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab201]') )
BEGIN
    CREATE TABLE lab201
    (
        lab201c1 INTEGER NOT NULL IDENTITY(1,1),
        lab201c2 VARCHAR (64) NOT NULL ,
        lab201c3 DATETIME ,
        lab04c1 INTEGER ,
        lab07c1 TINYINT
    )

    ALTER TABLE lab201 ADD CONSTRAINT lab201_PK PRIMARY KEY CLUSTERED (lab201c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--PROCEDIMIENTO POR SIEMBRA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab202]') )
BEGIN
    CREATE TABLE lab202
    (
        lab22c1 BIGINT NOT NULL ,
        lab39c1 INTEGER NOT NULL ,
        lab156c1 INTEGER NOT NULL
    )

    ALTER TABLE lab202 ADD CONSTRAINT lab202_PK PRIMARY KEY CLUSTERED (lab22c1, lab39c1, lab156c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--MEDIO DE CULTIVO POR SIEMBRA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab203]') )
BEGIN
    CREATE TABLE lab203
    (
        lab22c1 BIGINT NOT NULL ,
        lab39c1 INTEGER NOT NULL ,
        lab155c1 INTEGER NOT NULL
    )

    ALTER TABLE lab203 ADD CONSTRAINT lab203_PK PRIMARY KEY CLUSTERED (lab22c1, lab39c1, lab155c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--DETALLE EXAMEN PRECIO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab900]') )
BEGIN
    CREATE TABLE lab900 (
        lab900c1    INTEGER NOT NULL IDENTITY(1,1),
        lab21c1     INTEGER NOT NULL,
        lab22c1     BIGINT NOT NULL,
        lab39c1     INTEGER NOT NULL,
        lab900c2    FLOAT NOT NULL,
        lab900c3    FLOAT,
        lab900c4    FLOAT,
        lab901c1P   INTEGER,
        lab901c1I   INTEGER,
        lab904c1    INTEGER NOT NULL,
        lab14c1     INTEGER,
        lab05c1     INTEGER
    )
    ALTER TABLE lab900 ADD CONSTRAINT lab900_PK PRIMARY KEY CLUSTERED (lab900c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--DETECCIÃ“N MICROBIANA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab204]') )
BEGIN
    CREATE TABLE lab204
    (
        lab204c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab22c1 BIGINT NOT NULL ,
        lab39c1 INTEGER NOT NULL ,
        lab76c1 INTEGER NOT NULL ,
        lab204c2 TEXT ,
        lab204c3 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL,
        lab77c1 INTEGER NOT NULL
    )

    ALTER TABLE lab204 ADD CONSTRAINT lab204_PK PRIMARY KEY CLUSTERED (lab204c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--DETECCIÃ“N MICROBIANA - ANTIBIOTICO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab205]') )
BEGIN
    CREATE TABLE lab205
    (
        lab204c1 INTEGER NOT NULL ,
        lab79c1 INTEGER NOT NULL ,
        lab205c1 VARCHAR (64) ,
        lab205c2 VARCHAR (64) ,
        lab205c3 VARCHAR (64) ,
        lab205c4 VARCHAR (64) ,
        lab205c5 TINYINT NOT NULL ,
        lab205c6 VARCHAR (64) ,
        lab205c7 VARCHAR (64) ,
        lab205c8 TINYINT NOT NULL,
        lab205c9 DATETIME ,
        lab04c1_1 INTEGER ,
        lab205c10 DATETIME ,
        lab04c1_2 INTEGER ,
        lab205c11 DATETIME ,
        lab04c1_3 INTEGER
    )

    ALTER TABLE lab205 ADD CONSTRAINT lab205_PK PRIMARY KEY CLUSTERED (lab204c1, lab79c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--GRUPO ETARIO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab13]') )
BEGIN
    CREATE TABLE lab13
    (
        lab13c1 INTEGER NOT NULL IDENTITY(1,1),
        lab13c2 VARCHAR (16) NOT NULL ,
        lab13c3 VARCHAR (64) NOT NULL ,
        lab13c4 INTEGER ,
        lab13c5 TINYINT ,
        lab13c6 INTEGER ,
        lab13c7 INTEGER ,
        lab07c1 TINYINT NOT NULL ,
        lab13c8 DATETIME ,
        lab04c1 INTEGER
    )
    ALTER TABLE lab13 ADD CONSTRAINT lab13_PK PRIMARY KEY CLUSTERED (lab13c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--MICROORGANISMOS - ANTIBIOGRAMA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab206]') )
BEGIN
    CREATE TABLE lab206
    (
        lab77c1 INTEGER NOT NULL ,
        lab76c1 INTEGER NOT NULL ,
        lab39c1 INTEGER
    )
END
--SEGMENT
--DESTINOS DE MICROBIOLOGIA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab207]') )
BEGIN
    CREATE TABLE lab207
    (
        lab207c1 INTEGER NOT NULL IDENTITY(1,1),
        lab207c2 VARCHAR (16) NOT NULL ,
        lab207c3 VARCHAR (64) NOT NULL ,
        lab207c4 TINYINT NOT NULL ,
        lab207c5 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )

    ALTER TABLE lab207 ADD CONSTRAINT lab207_PK PRIMARY KEY CLUSTERED (lab207c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--CLASE - HISTOGRAMA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab06]') )
BEGIN
    CREATE TABLE lab06
    (
        lab06c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab06c2 VARCHAR (32) NOT NULL ,
        lab06c3 BIGINT NOT NULL ,
        lab06c4 BIGINT ,
        lab06c5 DATETIME ,
        lab04c1 INTEGER ,
        lab07c1 TINYINT NOT NULL
    )

    ALTER TABLE lab06 ADD CONSTRAINT lab06_PK PRIMARY KEY CLUSTERED (lab06c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--MICROBIOLOGIA - DESTINOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab208]') )
BEGIN
    CREATE TABLE lab208
    (
        lab208c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab208c2 INTEGER NOT NULL ,
        lab208c3 TEXT ,
        lab208c4 TINYINT NOT NULL ,
        lab22c1 BIGINT NOT NULL ,
        lab39c1 INTEGER NOT NULL ,
        lab207c1 INTEGER NOT NULL ,
        lab208c5 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab208c6 TINYINT NOT NULL ,
        lab208c7 DATETIME NOT NULL ,
        lab04c1_1 INTEGER NOT NULL
    )

    ALTER TABLE lab208 ADD CONSTRAINT lab208_PK PRIMARY KEY CLUSTERED (lab208c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--MICROBIOLOGIA - DESTINOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab209]') )
BEGIN
    CREATE TABLE lab209
    (
        lab208c1 INTEGER NOT NULL ,
        lab169c1 INTEGER NOT NULL
    )

    ALTER TABLE lab209 ADD CONSTRAINT lab209_PK PRIMARY KEY CLUSTERED (lab169c1, lab208c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--MICROBIOLOGIA - DESTINOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab210]') )
BEGIN
    CREATE TABLE lab210
    (
        lab76c1 INTEGER NOT NULL ,
        lab79c1 INTEGER NOT NULL ,
        lab210c1 TINYINT NOT NULL ,
        lab210c2 TINYINT NOT NULL ,
        lab210c3 VARCHAR (32) NOT NULL ,
        lab210c4 VARCHAR (32) ,
        lab80c1 INTEGER NOT NULL ,
        lab210c5 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL
    )

    ALTER TABLE lab210 ADD CONSTRAINT lab210_PK PRIMARY KEY CLUSTERED (lab76c1, lab79c1, lab210c2, lab210c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--DIAGNOSTICO - PRUEBA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab26]') )
BEGIN
    CREATE TABLE lab26
    (
        lab39c1 INTEGER NOT NULL ,
        lab20c1 INTEGER NOT NULL
    )
    ALTER TABLE lab26 ADD CONSTRAINT lab26_PK PRIMARY KEY CLUSTERED (lab39c1, lab20c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--MICROBIOLOGIA - DESTINOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab211]'))
BEGIN
    CREATE TABLE lab211
    (
        lab211c1 INTEGER NOT NULL IDENTITY(1,1),
        lab22c1 BIGINT NOT NULL ,
        lab39c1 INTEGER NOT NULL ,
        lab24c1 INTEGER NOT NULL ,
        lab211c2 TEXT NOT NULL ,
        lab211c3 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL
    )

    ALTER TABLE lab211 ADD CONSTRAINT lab211_PK PRIMARY KEY CLUSTERED (lab211c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--HISTORICO RESULTADOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab17]'))
BEGIN
    CREATE TABLE lab17 (
        lab21c1     INTEGER NOT NULL,
        lab39c1     INTEGER NOT NULL,
        lab17c1     VARCHAR(128),
        lab17c2     DATETIME,
        lab04c1_1   INTEGER,
        lab17c3     VARCHAR(128),
        lab17c4     DATETIME,
        lab04c1_2   INTEGER
    );
    ALTER TABLE lab17 ADD CONSTRAINT lab17_PK PRIMARY KEY CLUSTERED (lab21c1, lab39c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--RESULTADOS - PLANTILLA DE RESULTADOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab58]'))
BEGIN
    CREATE TABLE lab58
    (
        lab22c1 BIGINT NOT NULL ,
        lab39c1 INTEGER NOT NULL ,
        lab58c1 TEXT ,
        lab58c2 DATETIME NOT NULL ,
        lab58c3 VARCHAR (512) ,
        lab58c4 TEXT NOT NULL ,
        lab04c1 INTEGER NOT NULL
    )
END
--SEGMENT
--INGRESO - CAJA CABECERA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab902]'))
BEGIN
    CREATE TABLE lab902 (
        lab902c1    INTEGER NOT NULL IDENTITY(1,1),
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
        lab902c10   DATETIME NOT NULL,
        lab04c1_u   INTEGER,
        lab902c11   DATETIME
    );
    ALTER TABLE lab902 ADD CONSTRAINT lab902_PK PRIMARY KEY CLUSTERED (lab902c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--INGRESO - CAJA DETALLE
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab903]'))
BEGIN
    CREATE TABLE lab903 (
        lab903c1    INTEGER NOT NULL IDENTITY(1,1),
        lab22c1     BIGINT NOT NULL,
        lab01c1     INTEGER NOT NULL,
        lab903c2    VARCHAR(64),
        lab110c1    INTEGER,
        lab59c1     INTEGER,
        lab903c3    FLOAT,
        lab07c1     INTEGER,
        lab04c1_i   INTEGER NOT NULL,
        lab903c4    DATETIME NOT NULL,
        lab04c1_u   INTEGER,
        lab903c5    DATETIME
    );
    ALTER TABLE lab903 ADD CONSTRAINT lab903_PK PRIMARY KEY CLUSTERED (lab903c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--EMISOR(ENTIDAD)
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab906]'))
BEGIN
    CREATE TABLE lab906
    (
        lab906c1 INTEGER NOT NULL IDENTITY(1,1),
        lab906c2 VARCHAR (64) NOT NULL ,
        lab906c3 VARCHAR (128) NOT NULL ,
        lab906c4 VARCHAR (128) ,
        lab906c5 VARCHAR (32) ,
        lab906c6 VARCHAR (64) ,
        lab906c7 VARCHAR (64) ,
        lab906c8 VARCHAR (16) ,
        lab906c9 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )
    ALTER TABLE lab906 ADD CONSTRAINT lab906_PK PRIMARY KEY CLUSTERED (lab906c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--RESOLUCION
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab907]'))
BEGIN
    CREATE TABLE lab907
    (
        lab907c1 INTEGER NOT NULL IDENTITY(1,1),
        lab907c2 VARCHAR (256) NOT NULL ,
        lab907c3 INTEGER NOT NULL ,
        lab907c4 INTEGER NOT NULL ,
        lab907c5 VARCHAR (8) ,
        lab907c6 INTEGER ,
        lab906c1 INTEGER NOT NULL ,
        lab907c7 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )

    ALTER TABLE lab907 ADD CONSTRAINT lab907_PK PRIMARY KEY CLUSTERED (lab907c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--GRADILLAS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab16]'))
BEGIN
   CREATE TABLE lab16
    (
        lab16c1 INTEGER NOT NULL IDENTITY(1,1),
        lab16c2 VARCHAR (8),
        lab16c3 VARCHAR (32) NOT NULL ,
        lab16c4 TINYINT NOT NULL ,
        lab05c1 INTEGER NOT NULL ,
        lab16c5 TINYINT,
        lab16c6 TINYINT,
        lab16c7 DATETIME NOT NULL ,
        lab16c8   VARCHAR(32),
        lab31c1   INTEGER,
        lab04c1 INTEGER NOT NULL ,
        lab07c1 TINYINT NOT NULL
    )

    ALTER TABLE lab16 ADD CONSTRAINT lab16_PK PRIMARY KEY CLUSTERED (lab16c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--GRADILLA DETALLE
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab11]'))
BEGIN
   CREATE TABLE lab11
    (
        lab11c1 INTEGER NOT NULL ,
        lab16c1 INTEGER NOT NULL ,
        lab24c1 INTEGER NOT NULL ,
        lab22c1 BIGINT NOT NULL ,
        lab11c2 DATETIME NOT NULL ,
        lab11c3 DATETIME ,
        lab11c4 TINYINT NOT NULL ,
        lab11c5 DATETIME ,
        lab27c1 INTEGER,
        lab04c1 INTEGER NOT NULL ,
        lab04c1_1 INTEGER
    )
END
--SEGMENT
--ACTA DESECHO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab27]'))
BEGIN
    CREATE TABLE lab27
    (
        lab27c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab27c2 VARCHAR (1024) NOT NULL ,
        lab27c3 TINYINT NOT NULL ,
        lab27c4 DATETIME ,
        lab04c1 INTEGER ,
        lab27c5 TINYINT NOT NULL ,
        lab27c6 DATETIME ,
        lab27c7 VARCHAR (32) NOT NULL ,
        lab04c1_1 INTEGER
    )

    ALTER TABLE lab27 ADD CONSTRAINT lab27_PK PRIMARY KEY CLUSTERED (lab27c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--ENTREVISTA PARA PANICOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab179]'))
BEGIN
    CREATE TABLE lab179
    (
        lab22c1 BIGINT NOT NULL ,
        lab39c1 INTEGER NOT NULL ,
        lab70c1 INTEGER NOT NULL ,
        lab90c1 INTEGER ,
        lab179c1 TEXT ,
        lab179c2 DATETIME NOT NULL ,
        lab179c3 TINYINT NOT NULL ,
        lab179c4 TINYINT NOT NULL ,
        lab179c5 TINYINT NOT NULL ,
        lab04c1 INTEGER NOT NULL
    )
END

--SEGMENT
--DIAGNOSTICO POR ORDEN
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab180]'))
BEGIN
    CREATE TABLE lab180 
    (
     lab20c1 BIGINT NOT NULL ,
     lab22c1 BIGINT NOT NULL 
    )
END

--SEGMENT
--PAGO TOTAL
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab908]'))
BEGIN
    CREATE TABLE lab908 
    (
     lab22c1 BIGINT NOT NULL , 
     lab908c1 FLOAT , 
     lab908c2 FLOAT , 
     lab908c3 FLOAT ,
     lab908c4   DATETIME NOT NULL,
     lab04c1    INTEGER NOT NULL
    )
    ALTER   TABLE lab908 ADD CONSTRAINT lab908_PK PRIMARY KEY CLUSTERED (lab22c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

--SEGMENT
--ENTREGA DEL RESULTADO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab75]'))
BEGIN
    CREATE TABLE lab75 
    (
    lab75c1  INTEGER NOT NULL IDENTITY(1,1),
    lab75c2  BIGINT,
    lab75c3  INTEGER,
    lab75c4  INTEGER,
    lab04c1  INTEGER NOT NULL,
    lab75c5  DATETIME NOT NULL,
    lab80c1  INTEGER
    )
    ALTER   TABLE lab75 ADD CONSTRAINT lab75_PK PRIMARY KEY CLUSTERED (lab75c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

--SEGMENT
--DISEÃ‘ADOR BARRAS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab105]'))
BEGIN
        CREATE TABLE lab105 (
            lab105c1   INTEGER NOT NULL IDENTITY(1,1),
            lab105c2   TINYINT NOT NULL,
            lab105c3   VARCHAR(2048),
            lab105c4   VARCHAR(2048),
            lab07c1    INT NOT NULL
        );
    ALTER   TABLE lab105 ADD CONSTRAINT lab105_PK PRIMARY KEY CLUSTERED (lab105c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--SERIAL DE IMPRESION
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab106]'))
BEGIN
        CREATE TABLE lab106 (
            lab106c1   VARCHAR(40) NOT NULL,
            lab106c2   VARCHAR(40) ,
            lab106c3   DATETIME NOT NULL
        );
    ALTER   TABLE lab106 ADD CONSTRAINT lab106_PK PRIMARY KEY CLUSTERED (lab106c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--AGRUPACION DE ORDEN
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab801]'))
BEGIN
       CREATE TABLE lab801 (
            lab801c1 INTEGER NOT NULL,
            lab801c2 INTEGER NOT NULL,
            lab04c1  INTEGER NOT NULL,
            lab801c3 DATETIME NOT NULL
        );
        ALTER TABLE lab801 ADD CONSTRAINT lab801_PK PRIMARY KEY CLUSTERED (lab801c1, lab801c2) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--Impresion por servicio
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab107')
BEGIN
   CREATE TABLE lab107 (
        lab106c1 VARCHAR(100) NOT NULL, -- Serial
        lab05c1  INTEGER NOT NULL, -- Sede
        lab10c1 INTEGER NOT NULL --Servicio
    );
END
--Rellamado de ordenes
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab221')
BEGIN
   CREATE TABLE lab221 (
        lab22c1_1 BIGINT NOT NULL, -- Orden padre
        lab22c1_2  BIGINT NOT NULL -- Orden hija
    );
    ALTER TABLE lab221 ADD CONSTRAINT lab221_pk PRIMARY KEY ( lab22c1_1, lab22c1_2 );
END
--Relacion que establece que examen en la muestra maneja el medio de cultivo por orden
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab165')
BEGIN
   CREATE TABLE lab165 (
        lab22c1 BIGINT NOT NULL, -- Orden
        lab24c1  INTEGER NOT NULL, -- Id de la muestra
        lab39c1  INTEGER NOT NULL -- Id del examen
    );
    ALTER TABLE lab165 ADD CONSTRAINT lab165_pk PRIMARY KEY ( lab22c1, lab24c1, lab39c1 );
END
------------------------ACTUALIZACIONES--------------------
--SEGMENT
--TIENE PLANTILLA
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab57]') and sys.syscolumns.name = 'lab57c42')
BEGIN
   ALTER TABLE lab57 ADD lab57c42 TINYINT;
END
--SEGMENT
--MOFICACION: MENSAJE
ALTER TABLE lab151 ALTER COLUMN lab151c6 TEXT NOT NULL;
--SEGMENT
--TIPO DE ERROR
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab151]') and sys.syscolumns.name = 'lab151c8')
BEGIN
   ALTER TABLE lab151 ADD lab151c8 INTEGER NOT NULL DEFAULT 0;
END
--SEGMENT
--ALMACENAMIENTO ESPECIAL
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab24]') and sys.syscolumns.name = 'lab24c14')
BEGIN
   ALTER TABLE lab24 ADD lab24c14 TINYINT NOT NULL DEFAULT 0;
END
--SEGMENT
--ALMACENAMIENTO ESPECIAL
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab57]') and sys.syscolumns.name = 'lab57c43')
BEGIN
   ALTER TABLE lab57 ADD lab57c43 TINYINT NOT NULL DEFAULT 0;
END
--SEGMENT
--IMPUESTO - EXAMEN
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab39]') and sys.syscolumns.name = 'lab39c49')
BEGIN
   ALTER TABLE lab39 ADD lab39c49 FLOAT;
END
--SEGMENT
--FECHA FINAL ALMACENAMIENTO
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab11]') and sys.syscolumns.name = 'lab11c5')
BEGIN
   ALTER TABLE lab11 ADD lab11c5 DATETIME;
END
--SEGMENT
--ACTA DE DESECHO
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab11]') and sys.syscolumns.name = 'lab27c1')
BEGIN
   ALTER TABLE lab11 ADD lab27c1 INTEGER;
END
--REQUIERE VALIDACIÃ“N PRELIMINAR - EXAMEN
IF NOT EXISTS (SELECT 1 FROM sys.syscolumns where id = object_id(N'[lab39]') and sys.syscolumns.name = 'lab39c50')
BEGIN
    ALTER TABLE lab39 ADD lab39c50 SMALLINT NOT NULL DEFAULT 0;
END
--SEGMENT
--GENERA ALERTA POR TENDENCIA - EXAMEN -> ADICIONAR CAMPO LAB39C51
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab39]') and sys.syscolumns.name = 'lab39c51')
BEGIN
   ALTER TABLE lab39 ADD lab39c51 TINYINT NOT NULL;
END
--SEGMENT
--GENERA ALERTA POR TENDENCIA - EXAMEN -> ASIGNAR DEFAULT A LAB39C51 CON SU RESPECTIVO NOMBRE DE CONSTRAINT
IF NOT EXISTS (SELECT  1 FROM dbo.sysobjects where id = object_id(N'[DF__lab39__lab39c51__0C50D423]'))
BEGIN
   ALTER TABLE lab39 ADD CONSTRAINT DF__lab39__lab39c51__0C50D423 DEFAULT 0 FOR lab39c51;
END
--SEGMENT
--REQUIERE VALIDACIÃ“N PRELIMINAR - EXAMEN
IF EXISTS (SELECT  1 FROM dbo.sysobjects where id = object_id(N'[DF__lab39__lab39c51__0C50D423]'))
BEGIN
    ALTER TABLE lab39 DROP CONSTRAINT DF__lab39__lab39c51__0C50D423;
END
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c51')
BEGIN
    ALTER TABLE lab39 ALTER COLUMN lab39c51 INTEGER;
END
--SEGMENT
--ESTADO9 DEL MEDICO
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab19]') and sys.syscolumns.name = 'lab19c23')
BEGIN
   ALTER TABLE lab19 ADD lab19c23 VARCHAR(128);
END
--SEGMENT
--TURNO DE LA ORDEN
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab22]') and sys.syscolumns.name = 'lab22c13')
BEGIN
   ALTER TABLE lab22 ADD lab22c13 VARCHAR(32);
END
--SEGMENT
--CAJA
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab908]') and sys.syscolumns.name = 'lab908c5')
BEGIN
   ALTER TABLE lab908 ADD lab908c5 FLOAT;
   ALTER TABLE lab908 ADD lab908c6 FLOAT;
END
--SEGMENT
--INTEGRACION CON MIDELWARE
IF NOT EXISTS (SELECT 1 from sys.syscolumns where id = object_id(N'[lab40]') and sys.syscolumns.name = 'lab40c10')
BEGIN
    ALTER TABLE lab40 ADD lab40c10 VARCHAR(512);
END
IF NOT EXISTS (SELECT 1 from sys.syscolumns where id = object_id(N'[lab40]') and sys.syscolumns.name = 'lab40c11')
BEGIN
    ALTER TABLE lab40 ADD lab40c11 TINYINT NOT NULL DEFAULT 0;
END
IF NOT EXISTS (SELECT 1 from sys.syscolumns where id = object_id(N'[lab40]') and sys.syscolumns.name = 'lab40c12')
BEGIN
    ALTER TABLE lab40 ADD lab40c12 TINYINT NOT NULL DEFAULT 0;
END
IF NOT EXISTS (SELECT 1 from sys.syscolumns where id = object_id(N'[lab40]') and sys.syscolumns.name = 'lab40c13')
BEGIN
    ALTER TABLE lab40 ADD lab40c13 TINYINT NOT NULL DEFAULT 0;
END
--SEGMENT
--MEDICO CAMBIO DEL USERNAME
IF NOT EXISTS (SELECT 1 from information_schema.columns WHERE TABLE_NAME='lab19' AND COLUMN_NAME='lab19c17' AND CHARACTER_MAXIMUM_LENGTH=256)
BEGIN
    ALTER TABLE lab19 ALTER COLUMN lab19c17 VARCHAR(256) NULL;
END
--SEGMENT
--CLIENTE CAMBIO DEL USERNAME
IF NOT EXISTS (SELECT 1 from information_schema.columns WHERE TABLE_NAME='lab14' AND COLUMN_NAME='lab14c24' AND CHARACTER_MAXIMUM_LENGTH=128)
BEGIN
    ALTER TABLE lab14 ALTER COLUMN lab14c24 VARCHAR(128) NULL;
END
--SEGMENT
--CLIENTE CAMBIO DEL USERNAME
IF NOT EXISTS (SELECT 1 from sys.syscolumns where id = object_id(N'[lab100]') and sys.syscolumns.name = 'lab05c1')
BEGIN
    ALTER TABLE lab100 ADD lab05c1 INTEGER NOT NULL;
END
ALTER TABLE lab105 ALTER COLUMN lab105c3 TEXT;
ALTER TABLE lab105 ALTER COLUMN lab105c4 TEXT;
--SEGMENT
--FECHA Y USUARIO DE CREACION DE PACIENTE
IF NOT EXISTS (SELECT 1 from sys.syscolumns where id = object_id(N'[lab21]') and sys.syscolumns.name = 'lab21c20')
BEGIN
    ALTER TABLE lab21 ADD lab21c20 DATETIME;
    ALTER TABLE lab21 ADD lab04c1_2 INTEGER NOT NULL DEFAULT 0;
END
--SEGMENT
--CAMPO NOMBRE DE LA ETIQUETA DEL CODIGO DE BARRAS
IF NOT EXISTS (SELECT 1 from sys.syscolumns where id = object_id(N'[lab105]') and sys.syscolumns.name = 'lab105c5')
BEGIN
    ALTER TABLE lab105 ADD lab105c5 INTEGER NOT NULL DEFAULT 0;
END
--CAMBIAR TAMAÃ‘O DE CAMPO
IF NOT EXISTS (SELECT 1 from information_schema.columns WHERE TABLE_NAME='lab45' AND COLUMN_NAME='lab45c2' AND CHARACTER_MAXIMUM_LENGTH=265)
BEGIN
    ALTER TABLE lab45 ALTER COLUMN lab45c2  VARCHAR(265);
    ALTER TABLE lab64 ALTER COLUMN lab64c3  VARCHAR(2555);
END
--CAMBIAR TAMAÃ‘O DE CAMPO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab45c2' AND CHARACTER_MAXIMUM_LENGTH=265)
BEGIN
    ALTER TABLE lab57 ALTER COLUMN lab45c2 VARCHAR(265);
END
--CAMBIAR TAMAÃ‘O DE CAMPO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c33' AND CHARACTER_MAXIMUM_LENGTH=8000)
BEGIN
    ALTER TABLE lab39 ALTER COLUMN lab39c33  VARCHAR(8000);
    ALTER TABLE lab39 ALTER COLUMN lab39c34  VARCHAR(8000);
    ALTER TABLE lab39 ALTER COLUMN lab39c35  VARCHAR(8000);
    ALTER TABLE lab41 ALTER COLUMN lab41c3  VARCHAR(4000);
    ALTER TABLE lab45 ALTER COLUMN lab45c3  VARCHAR(265);
    ALTER TABLE lab68 ALTER COLUMN lab68c3  VARCHAR(4000);
    ALTER TABLE lab73 ALTER COLUMN lab73c3  VARCHAR(4000);
END
--CAMPO NOMBRE DE LA COLUMNA PARA LA AGRUPACION
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab801' AND column_name='lab801c4')
BEGIN
    ALTER TABLE lab801 ADD lab801c4 VARCHAR(50) NOT NULL DEFAULT '';
END
--VALIDAR SI LA PREGUNTA ES OBLIGATORIA
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab23' AND column_name='lab70c5')
BEGIN
    ALTER TABLE lab23 ADD lab70c5 INTEGER NOT NULL DEFAULT -1; -- Tipo de la pregunta
END
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab92' AND column_name='lab92c2')
BEGIN
    ALTER TABLE lab92 ADD lab92c2 TINYINT NOT NULL DEFAULT 0; --Pregunta Requerida
END
--USUARIO DE CREACION DE LA ORDEN
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab04c1_1')
BEGIN
    ALTER TABLE lab22 ADD lab04c1_1 INTEGER NOT NULL DEFAULT 0;
END
--PENULTIMO RESULTADO TEMPORAL
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab17' AND column_name='lab17c5')
BEGIN
    ALTER TABLE lab17 ADD lab17c5 VARCHAR(128);
    ALTER TABLE lab17 ADD lab17c6 DATETIME;
    ALTER TABLE lab17 ADD lab04c1_3 INTEGER;
END
--AGREGAR CAMPOS DE REPETICION Y RELLAMADO EN RESULTADO DE EXAMENES
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c44')
BEGIN
    ALTER TABLE lab57 ADD lab57c44 DATETIME; --Fecha de retoma
    ALTER TABLE lab57 ADD lab57c45 INTEGER; --Usuario de la retoma
    ALTER TABLE lab57 ADD lab57c46 DATETIME; --Fecha de la repeticion
    ALTER TABLE lab57 ADD lab57c47 INTEGER; --Usuario de repeticion
END
--CUERPO DEL CORREO
ALTER TABLE lab98 ALTER COLUMN lab98c2 TEXT;
--MOFICACION: CANTIDAD DE CARACTERES PARA URL DEL ERROR
ALTER TABLE lab151 ALTER COLUMN lab151c5 VARCHAR(120);
--CAMBIAR TAMAÃ‘O DE COMENTARIO Y AGREGAR CAMBPO ESTADO EN VALROES DE REFERENCIA
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab48' AND column_name='lab48c17')
BEGIN
    ALTER TABLE lab48 ALTER COLUMN lab48c9 VARCHAR(2050);
    ALTER TABLE lab48 ADD lab48c17 INTEGER NOT NULL DEFAULT 1;
END
--MOFICACION: CAMBIAR TAMAÃ‘O DE CAMPO NOMBRE DE EXAMEN
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c4' AND CHARACTER_MAXIMUM_LENGTH=150)
BEGIN
    ALTER TABLE lab39 ALTER COLUMN lab39c4 VARCHAR(150);
END
--MOFICACION: CAMBIAR TAMAÃ‘O DE CAMPO TITULO ESTADISTICO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c21' AND CHARACTER_MAXIMUM_LENGTH=150)
BEGIN
    ALTER TABLE lab39 ALTER COLUMN lab39c21 VARCHAR(150);
END
--MOFICACION: CAMBIAR TAMAÃ‘O DE CAMPO NOMBRE DE LABORATORIO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab40' AND column_name='lab40c3' AND CHARACTER_MAXIMUM_LENGTH=150)
BEGIN
    ALTER TABLE lab40 ALTER COLUMN lab40c3 VARCHAR(150);
END
--MOFICACION: CAMBIAR TAMAÃ‘O DE CAMPO NOMBRE DE COTIZACION
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab101' AND column_name='lab101c2' AND CHARACTER_MAXIMUM_LENGTH=150)
BEGIN
    ALTER TABLE lab101 ALTER COLUMN lab101c2 VARCHAR(150);
END
--AGREGA: SE AGREGA CAMPO PARA ALMACENAR ULTIMO RESULTADO REPETIDO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c48')
BEGIN
    ALTER TABLE lab57 ADD lab57c48 VARCHAR(16);
END
--AGREGA: SE AGREGA CAMPO PARA ALMACENAR EL DESTINO INICIAL
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c25')
BEGIN
    ALTER TABLE lab04 ADD lab04c25 INTEGER;
END
--AGREGA: FECHA DE ULTIMO INGRESO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c26')
BEGIN
    ALTER TABLE lab04 ADD lab04c26 DATETIME DEFAULT getDate();
END
IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'lab116' AND COLUMN_NAME = 'lab116c3' AND DATA_TYPE = 'datetime')
BEGIN
    ALTER TABLE lab116 ALTER COLUMN lab116c3  DATETIME;
    ALTER TABLE lab116 ALTER COLUMN lab116c4  DATETIME;
END
--Historico de contraseÃ±a
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c27')
BEGIN
    ALTER TABLE lab04 ADD lab04c27 VARCHAR(256);
    ALTER TABLE lab04 ADD lab04c28 VARCHAR(256);
END
--CONTADOR DE INTENTOS DE AUTENTICACION
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c29')
BEGIN
    ALTER TABLE lab04 ADD lab04c29 INTEGER NOT NULL DEFAULT 0;
END
--SEGMENT
--DEMOGRAFICO CONSULTA WEB  
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab181]'))
BEGIN
    CREATE TABLE lab181
    (
        lab181c1 Integer NOT NULL IDENTITY,
        lab181c2 varchar(50) NULL,
        lab181c3 varchar(32) NOT NULL,
        lab181c4 DATETIME NOT NULL,
        lab181c5 DATETIME,
        lab181c6 smallint NOT NULL,
        lab181c7 integer NOT NULL,
        lab181c8 integer NOT NULL,
        lab181c9 smallint NOT NULL,
        lab181c10 varchar(32),
        lab181c11 varchar(32),
        lab181c12 DATETIME
    )
    ALTER TABLE lab181 ADD CONSTRAINT lab181_PK PRIMARY KEY CLUSTERED (lab181c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--Rol
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab182]') )
BEGIN
    CREATE TABLE lab182 (
        lab05c1 INTEGER NOT NULL ,
        lab40c1 INTEGER NOT NULL 
        
    )   
END
--Historico de contraseÃ±a
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab48' AND column_name='lab48c18')
BEGIN
    ALTER TABLE lab48 ADD lab48c18 INTEGER;  
END

IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE where Table_Name = 'lab11')
BEGIN
    ALTER TABLE lab11 DROP lab11_PK;
END
--SEGMENT
--POSICION DE UNA MUESTRA EN UNA GRADILLA
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab11' AND column_name='lab11c1')
BEGIN
    ALTER TABLE lab11 ALTER COLUMN lab11c1 VARCHAR(8);
END
-- CODIGO CENTRAL:
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name = 'lab57' AND column_name = 'lab57c49')
BEGIN
    ALTER TABLE lab57 ADD lab57c49 VARCHAR(50);
END

IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab181' AND column_name='lab181c12')
BEGIN
    ALTER TABLE lab181 ALTER COLUMN lab181c12 DATETIME;
END
--Toma de muestra hospitalaria en servicios de laboratorio
IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab10' AND column_name='lab10c8')
BEGIN
    ALTER TABLE lab10 ADD lab10c8 TINYINT;  
END
--Alarma por prioridad en servicios de laboratorio
IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab10' AND column_name='lab10c9')
BEGIN
    ALTER TABLE lab10 ADD lab10c9 TINYINT;  
END
--Envio al HIS y Fecha de envio al HIS - lab57c50 y lab57c51 (respectivamente)
IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c50')
BEGIN
    ALTER TABLE lab57 ADD lab57c50 INTEGER NOT NULL CONSTRAINT DF_lab57_lab57c50 DEFAULT 0;
END
IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c51')
BEGIN
    ALTER TABLE lab57 ADD lab57c51 DATETIME NULL;
END
--Id del motivo de estado pendiente en los resultados
IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c52')
BEGIN
    ALTER TABLE lab57 ADD lab57c52 INTEGER;  
END
-- ReutilizaciÃ³n de la gradilla -> lab16c9
IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab16' AND column_name='lab16c9')
BEGIN
    ALTER TABLE lab16 ADD lab16c9 INTEGER NOT NULL CONSTRAINT DF_lab16_lab16c9 DEFAULT 0;
END
--SEGMENT
--Auditoria Gradilla
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab183]') )
BEGIN
    CREATE TABLE lab183 (
        lab183c1 VARCHAR(10) NOT NULL,
        lab16c1 INTEGER NOT NULL,
        lab24c1 INTEGER NOT NULL,
        lab22c1 BIGINT NOT NULL,
        lab183c2 DATETIME NOT NULL,
        lab183c3 DATETIME,
        lab183c4 SMALLINT NOT NULL,
        lab04c1 INTEGER NOT NULL,
        lab04c1_1 INTEGER,
        lab183c5 DATETIME,
        lab27c1 INTEGER
    );
END
-- Eliminacion del campo lab27c1 del Detalle de la gradilla
IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab11' AND column_name = 'lab27c1')
BEGIN
    ALTER TABLE lab11 DROP COLUMN lab27c1;
END
-- campo Encriptacion del reporte de resultados
IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c32')
BEGIN
    ALTER TABLE lab14 ADD lab14c32 TINYINT DEFAULT 0;
END
--SEGMENT
--ENVIO LIH
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab22]') and sys.syscolumns.name = 'lab22c14')
BEGIN
   ALTER TABLE lab22 ADD lab22c14 TINYINT DEFAULT 0;
END
-- lab185 -> ENCRIPTACION DE REPORTE POR DEMOGRAFICO
IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'lab185')
BEGIN
    CREATE TABLE lab185 (
        lab185c1 INTEGER NOT NULL,
        lab185c2 INTEGER NOT NULL,
        lab185c3 INTEGER DEFAULT 0 NOT NULL
    );
END
--Entrevista Destino
 IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab184]') )
 BEGIN
    CREATE TABLE lab184
    (
        lab44c1   INTEGER NOT NULL,
        lab53c1   INTEGER NOT NULL
    )
    ALTER TABLE lab184 ADD CONSTRAINT lab184_PK PRIMARY KEY CLUSTERED (lab44c1, lab53c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--Modificacion de la tabla entrevista-orden para registrar destino y sede
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab23' AND column_name='lab53c1')
BEGIN
     ALTER TABLE lab23 ADD lab53c1  INTEGER  NULL;
     ALTER TABLE lab23 ADD lab05c1  INTEGER  NULL;
END
-- Lab186 -> Analizadores por destinos de microbiologia
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name = 'lab186')
BEGIN
    CREATE TABLE lab186 (
	lab207c1 INTEGER NOT NULL,
	lab04c1 INTEGER NOT NULL
    );
END
--Modificacion de la tabla Usuario adicionando laboratorio
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c30')
BEGIN
     ALTER TABLE lab04 ADD lab04c30 INTEGER  NULL;
END
--Modificacion de la tabla Entrevista-Orden adicionando la muestra
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab23' AND column_name='lab24c1')
BEGIN
     ALTER TABLE lab23 ADD lab24c1 INTEGER  NULL;
END
--Modificacion de la tabla Valores de Referencia adicionando id del usuario analizador
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab48' AND column_name='lab48c19')
BEGIN
     ALTER TABLE lab48 ADD lab48c19 INTEGER  NULL;
END
 -- Lab108 -> Relacion Sede-Item Demografico
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name = 'lab108')
BEGIN
    CREATE TABLE lab108 (
	lab05c1 INTEGER NOT NULL,
	lab62c1 INTEGER NOT NULL,
        lab63c1 INTEGER NOT NULL
    );
END
--Modificacion de valor por defecto para el campo lab04c14
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c14' AND COLUMN_DEFAULT is null)
BEGIN
    ALTER TABLE lab04 ADD DEFAULT '0' for lab04c14
END
-- Lab109 -> Relacion Sede-Demografico
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name = 'lab109')
BEGIN
    CREATE TABLE lab109 (
	lab05c1 INTEGER NOT NULL,
	lab62c1 INTEGER NOT NULL       
    );
END
--Modificacion de la tabla Demograficos con campo valor por defecto requerido
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab62' AND column_name='lab62c14')
BEGIN
     ALTER TABLE lab62 ADD lab62c14 VARCHAR(64); 
END
--Modificacion de la tabla FILTRO-EXAMEN se cambia lab65C4 de TIPO DATE A DATETIME
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab65' AND column_name='lab65c4')
BEGIN
    ALTER TABLE lab65 ALTER COLUMN lab65c4 DATETIME;
END
--Modificacion de la tabla muestra para campos de temperatura
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab24' AND column_name='lab24c15')
BEGIN
     ALTER TABLE lab24 ADD lab24c15 INTEGER  NULL;
     ALTER TABLE lab24 ADD lab24c16 INTEGER  NULL;
END
--Modificacion de la tabla estado de la muestra para campo de temperatura
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab159' AND column_name='lab159c3')
BEGIN
       ALTER TABLE lab159 ADD lab159c3  VARCHAR(64);  
END
--Modificacion de la tabla examen lab39 adicion de campo consentimiendo informado
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab39]') and sys.syscolumns.name = 'lab39c52')
BEGIN
   ALTER TABLE lab39 ADD lab39c52 TINYINT DEFAULT 0;
END

IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab24' AND column_name='lab24c15')
BEGIN
    ALTER TABLE LAB24 ALTER COLUMN lab24c15 FLOAT; 
    ALTER TABLE LAB24 ALTER COLUMN lab24c16 FLOAT;
END

IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab159' AND column_name='lab159c3')
BEGIN
    ALTER TABLE lab159 ALTER COLUMN lab159c3 FLOAT;
END
IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE where Table_Name = 'lab159')
BEGIN
    ALTER TABLE lab159 DROP lab159_PK;
    ALTER TABLE lab159 ADD CONSTRAINT lab159_PK PRIMARY KEY CLUSTERED (lab24c1, lab22c1, lab05c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON );
END
--Modificacion de la tabla entrevista lab44 adicion de campo consentimiendo informado
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab44' AND column_name='lab44c6')
BEGIN
   ALTER TABLE lab44 ADD lab44c6 TINYINT DEFAULT 0;
END
 --CreaciÃ³n de la tabla entrevista lab187 consentimiendo informado
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab188]') )
BEGIN
    CREATE TABLE lab188
    (
            lab22c1 BIGINT NOT NULL,
            lab39c1 INTEGER NOT NULL,
            lab188c1 VARCHAR,
            lab04c1 INTEGER NOT NULL,
            lab188C2 DATETIME
    );
END
 --Modificacion de la tabla Sedes lab05 adicion de campo URL de conexion
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab05' AND column_name='lab05c13')
BEGIN
    ALTER TABLE lab05 ADD lab05c13 VARCHAR(128);
END
--CreaciÃ³n de la tabla Registrar Sede Trazabilidad lab189 
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab189]') )
BEGIN
    CREATE TABLE lab189
    (
            lab05c1 INTEGER NOT NULL,
            lab189c1 VARCHAR (128),
            lab189c2 VARCHAR (100)NOT NULL,
            lab189c3 TEXT NOT NULL    
    );
END
--CreaciÃ³n de la tabla Dependencia de Demoraficos lab190
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab190]') )
BEGIN
   CREATE TABLE lab190 
    (
            lab190c1 INTEGER NOT NULL,
            lab190c2 INTEGER NOT NULL,
            lab190c3 INTEGER NOT NULL,
            lab190c4 INTEGER NOT NULL           
    );
END
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c16')
BEGIN
    ALTER TABLE lab57 ALTER COLUMN lab57c16 INTEGER;
END
-- ORDENES ENVIADAS A UN SISTEMA EXTERNO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab191]') )
BEGIN
    CREATE TABLE lab191 
    (
            lab118c1 INTEGER NOT NULL,
            lab39c1 INTEGER NOT NULL,
            lab191c1 VARCHAR(20),
            lab22c1 BIGINT NOT NULL,          
            lab191c2 SMALLINT DEFAULT 0 NOT NULL,           
            lab191c3 DATETIME          
    );
END
--Modificacion de la tabla muestra para campo de muestra tapada
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab24' AND column_name='lab24c17')
BEGIN
    ALTER TABLE lab24 ADD lab24c17 TINYINT NULL;
END
--Modificacion DEL CODIGO DE LA MUETRA
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab05' AND column_name='lab05c10')
BEGIN
    ALTER TABLE lab05 ALTER COLUMN lab05C10 VARCHAR(10);
END
--SABER SI EL USUARIO TIENE PERMISO O NO EN EL TABLERO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c31')
BEGIN
   ALTER TABLE lab04 ADD lab04c31 TINYINT NOT NULL DEFAULT 0;
END
-- MODIFICACION DE LOS CAMPOS DE CONTRASEÃ‘A PARA LOS DIFERENTES TIPOS DE USUARIO
-- MEDICO
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab19' AND column_name='lab19c18')
BEGIN
   ALTER TABLE lab19 ALTER COLUMN lab19c18 VARCHAR(150);
END 
-- PACIENTE
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c19')
BEGIN
   ALTER TABLE lab21 ALTER COLUMN lab21c19 VARCHAR(150);
END 
-- CLIENTE
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c25')
BEGIN
   ALTER TABLE lab14 ALTER COLUMN lab14c25 VARCHAR(150);
END 
-- EL EXAMEN ES UNA LICUOTA
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c53')
BEGIN      
    ALTER TABLE lab39 ADD lab39c53 TINYINT NOT NULL DEFAULT 0;
END
--Impresion por servicio - ACTUALIZACION CAMPO SERIAL
IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab107' AND column_name = 'lab106c1')
BEGIN
   ALTER TABLE lab107 ALTER COLUMN lab106c1 VARCHAR(100) NOT NULL; 
END
-- CORREOS ALTERNATIVOS (MEDICO)
IF NOT EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab19' AND column_name = 'lab19c24')
BEGIN
   ALTER TABLE lab19 ADD lab19c24 VARCHAR(640);
END
-- CAMBIO DE TAMAÃ‘O SOBRE EL RESULTADO
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c1')
BEGIN
   ALTER TABLE lab57 ALTER COLUMN lab57c1 VARCHAR(600);
END
-- CAMBIO DE TAMAÃ‘O SOBRE EL PENULTIMO RESULTADO 
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c30')
BEGIN
   ALTER TABLE lab57 ALTER COLUMN lab57c30 VARCHAR(600);
END 
-- CAMBIO DE TAMAÃ‘O SOBRE EL ULTIMO RESULTADO
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c48')
BEGIN
   ALTER TABLE lab57 ALTER COLUMN lab57c48 VARCHAR(600);
END 
-- CLIENTES
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c28')
BEGIN
    ALTER TABLE lab14 ADD lab14c28 SMALLINT DEFAULT 0;
    ALTER TABLE lab14 ADD lab14c29 SMALLINT DEFAULT 0;  
END
--ADICION DE CAMPO DE SISTEMA CENTRAL
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab118c1')
BEGIN
    ALTER TABLE lab14 ADD lab118c1 SMALLINT DEFAULT 0;     
END     
-- ENTIDAD - Regimen Fiscal
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c10')
BEGIN
   ALTER TABLE lab906 ADD lab906c10 VARCHAR(64);
END 
-- ENTIDAD - NÃºmero actual
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c11')
BEGIN
   ALTER TABLE lab906 ADD lab906c11 INTEGER;
END 
-- ENTIDAD - Estado (Departamento)
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c12')
BEGIN
   ALTER TABLE lab906 ADD lab906c12 INTEGER;
END 
-- ENTIDAD - Municipio
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c13')
BEGIN
   ALTER TABLE lab906 ADD lab906c13 INTEGER;
END 
-- ENTIDAD - Nombre facturaciÃ³n electronica
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c14')
BEGIN
   ALTER TABLE lab906 ADD lab906c14 VARCHAR(64);
END 
-- ENTIDAD - Telefono facturacion electronica
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c15')
BEGIN
   ALTER TABLE lab906 ADD lab906c15 VARCHAR(20);
END 
-- ENTIDAD - Llave privada
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c16')
BEGIN
   ALTER TABLE lab906 ADD lab906c16 TEXT;
END 
-- ENTIDAD - Certificado
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c17')
BEGIN
   ALTER TABLE lab906 ADD lab906c17 VARCHAR(64);
END 
-- ENTIDAD - ContraseÃ±a
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c18')
BEGIN
   ALTER TABLE lab906 ADD lab906c18 VARCHAR(128);
END
-- FACTURA
IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab909')
BEGIN
    CREATE TABLE lab909 (
       lab909c1 BIGINT NOT NULL,
       lab07c1 SMALLINT NOT NULL DEFAULT 0
    );
    ALTER TABLE lab909 ADD CONSTRAINT lab909_PK PRIMARY KEY CLUSTERED (lab909c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
-- ID DE LA FACTURA EN LA CABECERA DE LA CAJA
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab909c1')
BEGIN
    ALTER TABLE lab902 ADD lab909c1 BIGINT;
END
-- ID DE LA ENTIDAD EN LA CABECERA DE LA CAJA
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab906c1')
BEGIN
    ALTER TABLE lab902 ADD lab906c1 INTEGER;
END
-- DISOLUCIÃ“N
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c53')
BEGIN
    ALTER TABLE lab57 ADD lab57c53 SMALLINT NOT NULL DEFAULT 0;
END

-- ENTREVISTA
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab23' AND column_name='lab44c3')
BEGIN
    ALTER TABLE lab23 ADD lab44c3 INTEGER;
END
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab23' AND column_name='lab66c1')
BEGIN
    ALTER TABLE lab23 ADD lab66c1 INTEGER;
END

IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c8')
BEGIN
    ALTER TABLE lab21 ALTER COLUMN lab21c8 VARCHAR(700);
END
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab40' AND column_name='lab05c1')
BEGIN
    ALTER TABLE lab40 ADD lab05c1 INTEGER;
END
-- MODIFICACIÃ“N DE TAMAÃ‘O DE LOS CAMPOS DE LOS NOMBRES DEL PACIENTE
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c3')
BEGIN
    ALTER TABLE lab21 ALTER COLUMN lab21c3 VARCHAR(600);
END
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c4')
BEGIN
    ALTER TABLE lab21 ALTER COLUMN lab21c4 VARCHAR(600);
END
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c5')
BEGIN
    ALTER TABLE lab21 ALTER COLUMN lab21c5 VARCHAR(600);
END
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c6')
BEGIN
    ALTER TABLE lab21 ALTER COLUMN lab21c6 VARCHAR(600);
END
-- MODIFICACIÃ“N DEL CAMPO DE USUARIO CONSULTA WEB
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c18')
BEGIN
    ALTER TABLE lab21 ALTER COLUMN lab21c18 VARCHAR(600);
END
-- MODIFICACIÃ“N DE TAMAÃ‘O DEL CAMPO DE NUMERO DE DOCUMENTO DEL PACIENTE
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c2')
BEGIN
    ALTER TABLE lab21 ALTER COLUMN lab21c2 VARCHAR(600);
END
-- MODIFICACIÃ“N DE TAMAÃ‘O DEL CAMPO DE TELEFONO DEL PACIENTE
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c16')
BEGIN
    ALTER TABLE lab21 ALTER COLUMN lab21c16 VARCHAR(50);
END
--ConfiguraciÃ³n RIPS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[Lab980]') )
BEGIN
    CREATE TABLE lab980 (
        lab980c1 VARCHAR (64) NOT NULL ,
        lab980c2 TEXT , 
        lab980c3 INTEGER , 
        lab980c4 VARCHAR (120)
    )
    ALTER TABLE lab980 ADD CONSTRAINT lab980_PK PRIMARY KEY CLUSTERED (lab980c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab54' AND column_name='lab54c5')
BEGIN
    ALTER TABLE lab54 ADD lab54c5 INTEGER NULL;
END
-- ELIMINACION DE CAMPOS DE LA ENTREVISTA
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab23' AND column_name='lab44c3')
BEGIN
    ALTER TABLE lab23 DROP COLUMN lab44c3;
END
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab23' AND column_name='lab66c1')
BEGIN
    ALTER TABLE lab23 DROP COLUMN lab66c1;
END
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c6')
BEGIN
    ALTER TABLE lab57 ALTER COLUMN lab57c6 VARCHAR(600);
END
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab50' AND column_name='lab50c2')
BEGIN
    ALTER TABLE lab50 ALTER COLUMN lab50c2 VARCHAR(256);
END
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab03' AND column_name='lab03c7')
BEGIN
    ALTER TABLE lab03 ADD lab03c7 INTEGER;
END
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab03' AND column_name='lab03c8')
BEGIN
    ALTER TABLE lab03 ADD lab03c8 VARCHAR(600);
END
-- TIPO DE DESCUENTO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab910')
BEGIN
    CREATE TABLE lab910 (
        lab910c1 INTEGER NOT NULL IDENTITY(1,1),
        lab910c2 VARCHAR(10) NOT NULL,
        lab910c3 VARCHAR(100) NOT NULL,
        lab910c4 FLOAT NOT NULL,
        lab07c1 SMALLINT NOT NULL,
        lab04c1 INTEGER NOT NULL,
        lab910c5 DATETIME NOT NULL,
        lab04c1_2 INTEGER,
        lab910c6 DATETIME
    );
    ALTER TABLE lab910 ADD CONSTRAINT lab910_PK PRIMARY KEY CLUSTERED (lab910c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
-- IMPUESTOS POR CLIENTE
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab911')
BEGIN
    CREATE TABLE lab911 (
        lab14c1 INTEGER NOT NULL,
        lab910c1 INTEGER NOT NULL,
        lab911c1 DATETIME NOT NULL
    );
END
-- NUEVA TABLA DE ALMACENAMIENTO DE USUARIOS-ESTADO BANDA
IF NOT EXISTS(SELECT 1 FROM dbo.sysobjects where id = object_id(N'[lab192]'))
BEGIN
    CREATE TABLE lab192 
    (
        lab192c1 Integer NOT NULL IDENTITY,
        lab192c2 integer,
        lab192c3 DATETIME,
        lab192c4 integer,
        lab192c5 integer,          
        lab192c6 varchar(256)         
    );
ALTER TABLE lab192 ADD CONSTRAINT lab192_pk PRIMARY KEY CLUSTERED (lab192c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END 
-- NUEVA TABLA DE ALMACENAMIENTO DE USUARIOS-ESTADO BANDA
IF NOT EXISTS(SELECT 1 FROM dbo.sysobjects where id = object_id(N'[lab916]'))
BEGIN
    CREATE TABLE lab916 
    (
        lab916c1 Integer NOT NULL IDENTITY,
        lab916c2 integer,
        lab916c3 integer,
        lab916c4 float,        
    );
ALTER TABLE lab916 ADD CONSTRAINT lab916_pk PRIMARY KEY CLUSTERED (lab916c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END 
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab03' AND column_name='lab03c9')
BEGIN
    ALTER TABLE lab03 ADD lab03c9 INTEGER;
END
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c21')
BEGIN
    ALTER TABLE lab21 ADD lab21c21 DATETIME;
END
-- SE ADICIONA EL COMENTARIO A LA PLANTILLA DE RESULTADOS
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab146' AND column_name='lab146c4')
BEGIN
    ALTER TABLE lab146 ADD lab146c4 VARCHAR(200);
END
-- SE ADICIONA EL COMENTARIO GENERAL DEL EXAMEN PARA LAS PLANTILLAS
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c54')
BEGIN
    ALTER TABLE lab39 ADD lab39c54 VARCHAR(200);
END
-- ADICION DE PORCENTAJE DEL PACIENTE EN PRECIOS POR TARIFA
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab55' AND column_name='lab55c2')
BEGIN
    ALTER TABLE lab55 ADD lab55c2 FLOAT NOT NULL DEFAULT 0;
END
-- ADICION DE PORCENTAJE DEL PACIENTE EN VIGENCIA POR TARIFA
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab120' AND column_name='lab120c2')
BEGIN
    ALTER TABLE lab120 ADD lab120c2 FLOAT NOT NULL DEFAULT 0;
END
-- FACTURA
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab901')
BEGIN
    CREATE TABLE lab901 (
        lab901c1 BIGINT NOT NULL IDENTITY(1,1),
        lab901c2 VARCHAR(16),
        lab901c3 FLOAT,
        lab901c4 FLOAT,
        lab901c5 DATETIME,
        lab907c1 INTEGER,
        lab14c1 INTEGER,
        lab63c1 INTEGER,
        lab04c1 INTEGER,
        lab901c6 FLOAT,
        lab901c7 BIGINT NOT NULL,
        lab901c8 BIGINT NOT NULL,
        lab901c9 SMALLINT,
        lab901c10 SMALLINT NOT NULL,
        lab906c1 INTEGER,
        lab901c11 SMALLINT NOT NULL,
        lab901c12 FLOAT,
        lab909c1 BIGINT,
        lab901c13 VARCHAR(32),
        lab901c14 TEXT,
        lab901c15 FLOAT NOT NULL,
        lab901c16 FLOAT NOT NULL,
        lab901c17 FLOAT NOT NULL,
        lab901c19 SMALLINT,
        lab901c20 FLOAT,
        lab901c21 BIGINT,
        lab901c22 VARCHAR(128),
        lab901c23 SMALLINT,
        lab14c2 VARCHAR(64),
        lab14c3 VARCHAR(64),
        lab14c4 VARCHAR(20),
        lab14c14 VARCHAR(128),
        lab14c17 VARCHAR(64),
        lab14c37 VARCHAR(60),
        lab14c38 IMAGE,
        lab901c24 DATETIME,
        lab901c25 SMALLINT
    );
    ALTER TABLE lab901 ADD CONSTRAINT lab901_PK PRIMARY KEY CLUSTERED (lab901c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
-- ABONOS POSTERIORES
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab909')
BEGIN
    CREATE TABLE lab909 (
        lab909c1 BIGINT NOT NULL IDENTITY(1,1),
        lab901c1 BIGINT,
        lab909c2 FLOAT,
        lab909c3 FLOAT,
        lab909c4 FLOAT,
        lab909c5 DATETIME,
        lab909c6 SMALLINT,
        lab909c7 VARCHAR(64),
        lab59c1 BIGINT,
        lab110c1 INTEGER,
        lab909c8 SMALLINT,
        lab909c9 TEXT
    );
    ALTER TABLE lab909 ADD CONSTRAINT lab909_PK PRIMARY KEY CLUSTERED (lab909c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

--IDENTIFICADOR PARA SABER SI LA  ENTIDAD ES PARA PARTICULARES O NO 
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab906' AND column_name='lab906c19')
BEGIN
    ALTER TABLE lab906 ADD lab906c19 SMALLINT;
END
-- ORDENES FACTURADAS POR CAPITA
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab912')
BEGIN
    CREATE TABLE lab912 (
        lab912c1 BIGINT NOT NULL IDENTITY(1,1),
        lab912c2 VARCHAR(6) NOT NULL,
        lab912c3 DATETIME NOT NULL,
        lab14c1 INTEGER NOT NULL,
        lab912c4 INTEGER NOT NULL,
        lab901c1 BIGINT NOT NULL
    );
    ALTER TABLE lab912 ADD CONSTRAINT lab912_PK PRIMARY KEY CLUSTERED (lab912c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
-- IMPRESORAS FISCALES
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab914')
BEGIN
    CREATE TABLE lab914 (
        lab914c1 BIGINT NOT NULL IDENTITY(1,1),
        lab914c2 VARCHAR(64) NOT NULL,
        lab914c3 VARCHAR(256) NOT NULL,
        lab914c4 VARCHAR(32) NOT NULL,
        lab07c1  SMALLINT NOT NULL,
        lab04c1  INTEGER NOT NULL,
        lab914c5 DATETIME,
        lab914c6 VARCHAR(64) NOT NULL
    );
END
-- ADICIÃ“N DE CODIGO EN TIPO DE DOCUMENTO
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab01' AND column_name='lab01C8')
BEGIN
    ALTER TABLE lab01 ADD lab01C8 VARCHAR(50);
END
-- NOTA DE CREDITO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab913')
BEGIN
    CREATE TABLE lab913 (
        lab913c1 BIGINT NOT NULL IDENTITY(1,1),
        lab901c1 BIGINT NOT NULL,
        lab913c2 INTEGER NOT NULL,
        lab913c3 DATETIME NOT NULL,
        lab04c1 INTEGER NOT NULL,
        lab913c4 VARCHAR(200) NOT NULL,
        lab913c5 FLOAT NOT NULL
    );
    ALTER TABLE lab913 ADD CONSTRAINT lab913_PK PRIMARY KEY CLUSTERED (lab913c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
-- DETALLE NOTA DE CREDITO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab915')
BEGIN
    CREATE TABLE lab915 (
        lab913c1 BIGINT NOT NULL,
        lab22c1 BIGINT NOT NULL,
        lab39c1 INTEGER NOT NULL,
        lab21c1 INTEGER NOT NULL,
        lab915c2 FLOAT NOT NULL,
        lab915c3 FLOAT,
        lab915c4 FLOAT,
        lab904c1 INTEGER NOT NULL,
        lab14c1 INTEGER,
        lab05c1 INTEGER
    );
END


--LOSG CLIENTE DE IMPRECION
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[cliprint]') )
BEGIN
CREATE TABLE cliprint 
        (
            lab22c1 BIGINT NOT NULL , 
            cliprintc1 VARCHAR(800)  , --comentario
            cliprintc2 VARCHAR (300) , --lista correos
            lab05c1 INTEGER NOT NULL ,
            lab05c4   VARCHAR(64)
        );
ALTER TABLE cliprint ADD CONSTRAINT cliprint_PK PRIMARY KEY CLUSTERED ( lab22c1,lab05c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END






-- ADICIÃ“N DEL ID DEL PACIENTE PARA GENERAR LA FACTURA A UN PARTICULAR
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab21c1')
BEGIN
    ALTER TABLE lab901 ADD lab21c1 INTEGER;
END
-- CAMBIO PARA QUE EL CODIGO DE LA NOTA CREDITO PERMITA NULL
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab913' AND column_name='lab913c2')
BEGIN
    ALTER TABLE lab913 ALTER COLUMN lab913c2 INTEGER;
END
-- ID DE LA FACTURA EN SIIGO
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c26')
BEGIN
    ALTER TABLE lab901 ADD lab901c26 VARCHAR(64);
END
-- PERIODO DE FACTURACIÃ“N 
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c27')
BEGIN
    ALTER TABLE lab901 ADD lab901c27 VARCHAR(20);
END
-- PERIODO DE FACTURACIÃ“N 
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c17')
BEGIN
    ALTER TABLE lab14 ALTER COLUMN lab14c17 INTEGER;
END
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab908' AND column_name='lab908c7')
BEGIN
    ALTER TABLE lab908 ADD lab908c7 FLOAT;
    ALTER TABLE lab908 ADD lab908c8 FLOAT;
END
-- CONTRATOS
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab990')
BEGIN
    CREATE TABLE lab990( --- contratos ---
	lab990c1 INTEGER IDENTITY(1,1) NOT NULL, --ID CONTRATO--
	lab990c2 varchar(64) NOT NULL,--NOMBRE---
	lab07c1 TINYINT NOT NULL, -- estado
	lab990c4 float, --- MONTO MAXIMO--
	lab990c5 float, --- MONTO ACTUAL--
	lab990c6 float, --- MONTO ALARMA--
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
	lab990c18 image , --- FIRMA--	
	lab990c19 datetime NOT NULL, -- fecha creacion
	lab990c20 datetime NOT NULL, --- fecha actualizacion
	lab14c1 INTEGER, --- id cliente--
        lab990c21 VARCHAR(64) NOT NULL--CODIGO---
    );
    ALTER TABLE lab990 ADD CONSTRAINT lab990_PK PRIMARY KEY CLUSTERED (lab990c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
-- Contrato x Impuestos
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab992')
BEGIN
    CREATE TABLE lab992(-----contrayimpu---
        lab990c1 INTEGER NOT NULL,--id contrato---
        lab910c1 INTEGER NOT NULL --ID impuesto--
    );
END
-- Contrato x Tarifas
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab993')
BEGIN
    CREATE TABLE lab993(-----contrato tarifa---
        lab990c1 INTEGER NOT NULL,--id contrato---
        lab904c1 INTEGER NOT NULL --ID tarifa--
    );
END
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab990' AND column_name='lab990c18')
BEGIN
    ALTER TABLE lab990 ALTER COLUMN lab990c18 image;
END
-- ID ITEMS DEMOGRAFICOS DE LA CIUDAD Y EL DEPARTAMENTO PARA EL CONTRATO
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab990' AND column_name='lab990c22')
BEGIN
    ALTER TABLE lab990 ADD lab990c22 INTEGER; -- CIUDAD
    ALTER TABLE lab990 ADD lab990c23 INTEGER; -- DEPARTAMENTO
END
-- EliminaciÃ³n de campos del cliente
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c7')
BEGIN
    ALTER TABLE lab14 DROP COLUMN lab14c7;
    ALTER TABLE lab14 DROP COLUMN lab14c8;
    ALTER TABLE lab14 DROP COLUMN lab14c9;
    ALTER TABLE lab14 DROP COLUMN lab14c10;
END
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c36')
BEGIN
    ALTER TABLE lab14 DROP COLUMN lab14c36;
	END
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c37')
BEGIN
    ALTER TABLE lab14 DROP COLUMN lab14c37;
	END
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c38')
BEGIN
    ALTER TABLE lab14 DROP COLUMN lab14c38;
	END
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c39')
BEGIN
    ALTER TABLE lab14 DROP COLUMN lab14c39;
	END
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c40')
BEGIN
    ALTER TABLE lab14 DROP COLUMN lab14c40;
	END
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c41')
BEGIN
    ALTER TABLE lab14 DROP COLUMN lab14c41;
	END
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c42')
BEGIN
    ALTER TABLE lab14 DROP COLUMN lab14c42;
	END
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c43')
BEGIN
    ALTER TABLE lab14 DROP COLUMN lab14c43;
END
-- EliminaciÃ³n de campo de tipos de entrega
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab75' AND column_name='lab75c4')
BEGIN
    ALTER TABLE lab75 DROP COLUMN lab75c4;
END
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c28')
BEGIN
    ALTER TABLE lab901 ADD lab901c28 VARCHAR(64);
END

IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab912' AND column_name='lab14c1')
BEGIN
    EXEC sp_RENAME 'lab912.lab14c1' , 'lab990c1', 'COLUMN'
END
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c29')
BEGIN
    ALTER TABLE lab901 ADD lab901c29 TINYINT;
END

-- SE ELIMINA EL PROCEDIMIENTO ALMACENADO QUE RENOMBRA LAS TABLAS DE OPERACION, SOLO SI EXISTE
IF EXISTS(SELECT * FROM dbo.sysobjects WHERE id = object_id(N'[RENAME_NEW_YEAR]') AND OBJECTPROPERTY(id, N'IsProcedure') = 1)
BEGIN
    DROP PROCEDURE [RENAME_NEW_YEAR];
END
-- CLIENTE (campo facturar 1 -> si, 0 -> no)
IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c33')
BEGIN
    ALTER TABLE lab14 ADD lab14c33 SMALLINT NOT NULL;
END
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c33')
BEGIN
    ALTER TABLE lab14 ALTER COLUMN lab14c33 SMALLINT NOT NULL;
END
-- CLIENTE (campo convenio 1 -> si, 0 -> no)
IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c34')
BEGIN
    ALTER TABLE lab14 ADD lab14c34 SMALLINT NOT NULL;
END
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c34')
BEGIN
    ALTER TABLE lab14 ALTER COLUMN lab14c34 SMALLINT NOT NULL;
END
-- CLIENTE (campo uso CFDI)
IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c35')
BEGIN
    ALTER TABLE lab14 ADD lab14c35 VARCHAR(64);
END
-- CLIENTE (campo uso Regimen Fiscal)
IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c36')
BEGIN
    ALTER TABLE lab14 ADD lab14c36 VARCHAR(64);
END
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab14' AND column_name='lab14c35')
BEGIN
    ALTER TABLE lab14 ALTER COLUMN lab14c35 VARCHAR(64);
END
-- CAMPOS ADICIONALES DEL CONTRATO
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab990' AND column_name='lab990c24')
BEGIN
    ALTER TABLE lab990 ADD lab990c24 SMALLINT;
END
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab990' AND column_name='lab990c25')
BEGIN
    ALTER TABLE lab990 ADD lab990c25 FLOAT;
END
-- IMPUESTO DEL EXAMEN EN LAB900
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab900' AND column_name='lab900c5')
BEGIN
    ALTER TABLE lab900 ADD lab900c5 FLOAT;
END
-- DESCUENTO DEL EXAMEN EN LAB900
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab900' AND column_name='lab900c6')
BEGIN
    ALTER TABLE lab900 ADD lab900c6 FLOAT;
END
-- TAMA�O DE CARACTERES ACEPTADOS PARA EL N�MERO DE LA FACTURA

IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c2')
BEGIN
    ALTER TABLE lab901 ALTER COLUMN lab901c2 VARCHAR(128);
END

-- FECHA DE ATENCI�N DE LA ORDEN

IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab22c15')
BEGIN
    ALTER TABLE lab22 ADD lab22c15 DATETIME;
END
-- FECHA DE PAGO DE LA FACTURA
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c30')
BEGIN
    ALTER TABLE lab901 ADD lab901c30 DATETIME;
END
-- PAGADO: 0 - NO, 1 - SI
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c31')
BEGIN
    ALTER TABLE lab901 ADD lab901c31 SMALLINT NOT NULL DEFAULT 0;
END
-- DESCUENTO DE LA TARIFA POR VALOR
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c12')
BEGIN
    ALTER TABLE lab902 ADD lab902c12 FLOAT;
END
--DESCUENTO DE LA TARIFA POR PORCENTAJE
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c13')
BEGIN
    ALTER TABLE lab902 ADD lab902c13 FLOAT;
END

-- TAMA�O DE CARACTERES ACEPTADOS PARA EL COMENTARIO DE LA FACTURA

IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c13')
BEGIN
    ALTER TABLE lab901 ALTER COLUMN lab901c13 TEXT;
END
-- AUDITORIA PARA LAS OPERACIONES CON FACTURAS
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab917')
BEGIN
    CREATE TABLE lab917(
--         lab901c1 INTEGER NOT NULL,
        lab917c1 VARCHAR(4) NOT NULL,
        lab917c2 TEXT NOT NULL,
        lab917c3 VARCHAR(4) NOT NULL,
        lab04c1 INTEGER NOT NULL,
        lab917c4 DATETIME NOT NULL,
        lab22c1 BIGINT NOT NULL
    );
END;

-- CAMPO PARA IDENTIFICAR SI ES NECESARIA LA PREVALIDACI�N DE UN EXAMEN SEG�N CONFIGURACI�N DE USUARIO
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c32')
BEGIN
    ALTER TABLE lab04 ADD lab04c32 TINYINT NOT NULL DEFAULT 0;
END

-- CAMPO PARA adicionar la cantidad del examen en la cotizacion
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab102' AND column_name='lab102c5')
BEGIN
    ALTER TABLE lab102 ADD lab102c5 INTEGER NOT NULL DEFAULT 0;
END

-- TAMA�O DE CARACTERES ACEPTADOS PARA EL COMENTARIO DE LA FACTURA
IF EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab901' AND column_name='lab901c13')
BEGIN
    ALTER TABLE lab101 ALTER COLUMN lab101c3 VARCHAR(100) ;
END

IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab101' AND column_name='lab101c6')
BEGIN
    ALTER TABLE lab101 ADD lab101c6 FLOAT NOT NULL DEFAULT 0;
END
-- Campo para identificar el tipo de entrega de un resultado
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab03' AND column_name='lab75c1')
BEGIN
    ALTER TABLE lab03 ADD lab75c1 INTEGER;
END
-- REMISION
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c54')
BEGIN
    ALTER TABLE lab57 ADD lab57c54 SMALLINT DEFAULT 0;
END
-- TIPO DE ORDEN EN IMPRESION DE CODIGOS DE BARRAS
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab105' AND column_name='lab105c6')
BEGIN
    ALTER TABLE lab105 ADD lab105c6 VARCHAR(200);
END
-- FECHA REPORTE A MEDICO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c55')
BEGIN
    ALTER TABLE lab57 ADD lab57c55 DATETIME;
END
-- Campo para identificar el laboratorio origen de una remision
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab40c1a')
BEGIN
    ALTER TABLE lab57 ADD lab40c1a INTEGER;
END

-- Campo para guardar la temperatura del examen
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c56')
BEGIN
    ALTER TABLE lab39 ADD lab39c56 INTEGER;
END

-- Campo alarma en ingreso de ordenes
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c55')
BEGIN
    ALTER TABLE lab39 ADD lab39c55 INTEGER;
END

--Modificacion de la tabla Sedes lab05 adicion de campo Imagen ministerio de salud
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab05' AND column_name='lab05c14')
BEGIN
    ALTER TABLE lab05 ADD lab05c14 VARCHAR(MAX);
END


--Modificacion de la tabla Vigencia tarifas lab120 adicion de campo ultima actualizacion
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab120' AND column_name='lab120c3')
BEGIN
    ALTER TABLE lab120 ADD lab120c3 DATETIME;
END

--Modificacion de la tabla Vigencia tarifas lab120 adicion de campo ultima actualizacion
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab120' AND column_name='lab04c1')
BEGIN
    ALTER TABLE lab120 ADD lab04c1 INTEGER;
END
--Modificacion de la tabla demograficos de consulta web lab181 adicion de campo email
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab181' AND column_name='lab181c13')
BEGIN
    ALTER TABLE lab181 ADD lab181c13 VARCHAR(128);
END


--Modificacion del campo de contraseña para que acepte nulos
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab181' AND column_name='lab181c3')
BEGIN
    ALTER TABLE lab181 ALTER COLUMN lab181C3 varchar(32);
END

-- EXAMENES POR DEMOGRAFICO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab600')
BEGIN
    CREATE TABLE lab600(
        lab600c1 INTEGER NOT NULL IDENTITY(1,1),
        lab600c2 INTEGER NOT NULL,
        lab600c3 INTEGER NOT NULL,
        lab600c4 INTEGER,
        lab600c5 INTEGER,
        lab600c6 INTEGER,
        lab600c7 INTEGER,
        lab600c8 DATETIME NOT NULL,
        lab04c1 INTEGER NOT NULL
    );
END;

-- EXAMENES POR DEMOGRAFICO - PRUEBAS
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab601')
BEGIN
    CREATE TABLE lab601 (
        lab600c1 INTEGER NOT NULL,
        lab39c1  INTEGER NOT NULL
    );
END;


--Modificacion de la tabla usuarios se adiciona el campo para validar si la contraseña se va recuperar

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c33')
BEGIN
    ALTER TABLE lab04 ADD lab04c33 TINYINT;
END

--Modificacion de la tabla tipos de entrega se adiciona el campo para Medio por el cual fue impreso el examen
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab167' AND column_name='lab80c1')
BEGIN      
     ALTER TABLE lab167 ADD lab80c1 INTEGER;
END

--Modificacion de la tabla tipos de entrega se adiciona el examen 
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab167' AND column_name='lab39c1')
BEGIN      
     ALTER TABLE lab167 ADD lab39c1 INTEGER;
END

--Modificacion de la tabla tipos de entrega se adiciona el campo del perfil padre del examen entregado. 
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab167' AND column_name='lab46c1')
BEGIN      
     ALTER TABLE lab167 ADD lab46c1 INTEGER;
END

--Eliminacion de la tabla que guardaba el detalle de los examenes entregados
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab168')
BEGIN      
    DROP TABLE lab168;
END

IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab901c6')
BEGIN
    ALTER TABLE lab901 DROP COLUMN lab901c6;
END

IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab901c7')
BEGIN
    ALTER TABLE lab901 DROP COLUMN lab901c7;
END

IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab901c8')
BEGIN
    ALTER TABLE lab901 DROP COLUMN lab901c8;
END

IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab901c15')
BEGIN
    ALTER TABLE lab901 DROP COLUMN lab901c15;
END

IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab901c16')
BEGIN
    ALTER TABLE lab901 DROP COLUMN lab901c16;
END

IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab14c2')
BEGIN
    ALTER TABLE lab901 DROP COLUMN lab14c2;
END

IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab14c3')
BEGIN
    ALTER TABLE lab901 DROP COLUMN lab14c3;
END

IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab14c4')
BEGIN
    ALTER TABLE lab901 DROP COLUMN lab14c4;
END

IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab14c17')
BEGIN
    ALTER TABLE lab901 DROP COLUMN lab14c17;
END

IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab14c37')
BEGIN
    ALTER TABLE lab901 DROP COLUMN lab14c37;
END

IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab14c38')
BEGIN
    ALTER TABLE lab901 DROP COLUMN lab14c38;
END

IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab901c28')
BEGIN
    ALTER TABLE lab901 DROP COLUMN lab901c28;
END

IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab901c29')
BEGIN
    ALTER TABLE lab901 DROP COLUMN lab901c29;
END

--Eliminacion de la tabla ordenes por capitado
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab912')
BEGIN      
    DROP TABLE lab912;
END

-- CABECERAS DE UNA FACTURA
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab920')
BEGIN
    CREATE TABLE lab920(
        lab920c1 INTEGER NOT NULL IDENTITY(1,1),
        lab14c1 INTEGER NOT NULL,
        lab901c1 INTEGER NOT NULL,
        lab990c1 INTEGER NOT NULL,
        lab920c2 FLOAT NOT NULL, 
        lab920c3 BIGINT NOT NULL, 
        lab920c4 BIGINT NOT NULL,
        lab920c5 FLOAT NOT NULL, 
        lab920c6 FLOAT NOT NULL,
        lab920c7 FLOAT NOT NULL, 
        lab920c8 FLOAT NOT NULL, 
        lab920c9 INTEGER NOT NULL, 
        lab920c10 INTEGER NOT NULL
    );

    ALTER TABLE lab920 ADD CONSTRAINT lab920_pk PRIMARY KEY CLUSTERED (lab920c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )

END;

--FECHA EN LA QUE SE FACTURARON LAS ORDENES POR CAPITADO EN FORMATO YYYYMM
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab920]') and sys.syscolumns.name = 'lab920c11')
BEGIN
   ALTER TABLE lab920 ADD lab920c11 VARCHAR(32) NOT NULL DEFAULT '';
END

--TOTAL DE LA CABECERA
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab920]') and sys.syscolumns.name = 'lab920c12')
BEGIN
   ALTER TABLE lab920 ADD lab920c12 FLOAT NOT NULL DEFAULT 0;
END

ALTER TABLE lab201 ALTER COLUMN lab201c2 VARCHAR (100) NOT NULL;

--ORDENAMIENTO DEL AREA
ALTER TABLE lab43 ALTER COLUMN lab43c2 INTEGER NOT NULL;

--ID DE LA CABECERA
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab900]') and sys.syscolumns.name = 'lab920c1')
BEGIN
   ALTER TABLE lab900 ADD lab920c1 INTEGER;
END

--ID DE LA CABECERA HISTORICO
IF EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab900_2021]'))
BEGIN
    IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab900_2021]') and sys.syscolumns.name = 'lab920c1')
    BEGIN
       ALTER TABLE lab900_2021 ADD lab920c1 INTEGER;
    END
    IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab900_2021]') and sys.syscolumns.name = 'lab920c1p')
    BEGIN
       ALTER TABLE lab900_2021 ADD lab920c1p INTEGER;
    END
END

IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab14c42')
BEGIN
    ALTER TABLE lab901 DROP COLUMN lab14c42;
END

--ID DE LA CABECERA PARA PARTICULAR
IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab900]') and sys.syscolumns.name = 'lab920c1p')
BEGIN
   ALTER TABLE lab900 ADD lab920c1p INTEGER;
END

--Modificacion de la tabla usuarios se adiciona el campo para validar si se muestra el reporte preliminar
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c34')
BEGIN
    ALTER TABLE lab04 ADD lab04c34 TINYINT;
END

--Adicion de campo persona que recibe a la auditoria de impresion de las ordenes. 
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab03' AND column_name='lab03c10')
BEGIN      
    ALTER TABLE lab03 ADD lab03c10 varchar(64);
END

ALTER TABLE lab24 ALTER COLUMN lab24c7 INTEGER NOT NULL;

-- CAMPO CLIENTE EN FACTURA
IF NOT EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab901' AND column_name = 'lab14c1')
BEGIN
    ALTER TABLE lab901 ADD lab14c1 INTEGER NOT NULL DEFAULT 0;
END

-- SE ELIMINA EL CAMPO CLIENTE DE LAS SECCIONES DE LA FACTURA
IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab920' AND column_name = 'lab14c1')
BEGIN
    ALTER TABLE lab920 DROP COLUMN lab14c1;
END

-- ELIMINACION FIRMA DEL VENDEDOR
IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab990' AND column_name = 'lab990c18')
BEGIN
    ALTER TABLE lab990 DROP COLUMN lab990c18;
END

--CAMPO IDIOMA INGLES AREAS
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab43' AND column_name='lab43c9')
BEGIN      
    ALTER TABLE lab43 ADD lab43c9 varchar(64);
END

-- CAMPO IDIOMA INGLES EXAMEN
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c58')
BEGIN
    ALTER TABLE lab39 ADD lab39c58 varchar(64);
END

-- CAMPO COMENTARIO FIJO EN INGLES
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c59')
BEGIN
    ALTER TABLE lab39 ADD lab39c59 varchar(8000);
END

-- CAMPO COMENTARIO PRELIMINAR EN INGLES
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c60')
BEGIN
    ALTER TABLE lab39 ADD lab39c60 varchar(8000);
END

-- CAMPO INFORMACION GENERAL EN INGLES
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c61')
BEGIN
    ALTER TABLE lab39 ADD lab39c61 varchar(8000);
END

--Adicion de campo para almacenar la fecha estimada de entrega de un resultado
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c56')
BEGIN      
    ALTER TABLE lab57 ADD lab57c56 DATETIME;
END   

-- Configuracion PRUEBA excluir festivos de dias de procesamiento
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c57')
BEGIN
    ALTER TABLE lab39 ADD lab39c57 TINYINT;
END

--Adicion de campo para almacenar la fecha de transporte de la muestra. 
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c57')
BEGIN      
    ALTER TABLE lab57 ADD lab57c57 DATETIME;
END

--Adicion de campo para almacenar el usuario que transporta de la muestra. 
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c58')
BEGIN      
    ALTER TABLE lab57 ADD lab57c58 INTEGER;
END

--Adicion de campo para almacenar la fecha de transporte de la muestra. 
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c59')
BEGIN      
    ALTER TABLE lab57 ADD lab57c59 DATETIME;
END

--Adicion de campo para almacenar el usuario que transporta de la muestra. 
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c60')
BEGIN      
    ALTER TABLE lab57 ADD lab57c60 INTEGER;
END

 --SE AGREGA PORQUE ALGUNOS CLIENTES TIENEN LA COLUMNA CREADA COMO TIMESTAMP Y ESTA GENERANDO PROBLEMAS
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c22' AND data_type='datetime')
BEGIN
    ALTER TABLE lab21 DROP COLUMN lab21c22;
END

-- ESTADO DEL PACIENTE
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c22')
BEGIN
    ALTER TABLE lab21 ADD lab21c22 TINYINT DEFAULT 0;
END
-- DATOS DEL TRIBUNAL
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab193]') )
BEGIN
    CREATE TABLE lab193
    (
        lab21c1 INTEGER NOT NULL,
        lab193c1 TEXT NOT NULL
    );
END
-- FECHA DE MODIFICACION DEL ESTADO DEL PACIENTE
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c23')
BEGIN
    ALTER TABLE lab21 ADD lab21c23 DATETIME;
END
-- MAYOR CAPACIDAD DE CARACTERES PARA LOS CORREOS ASOCIADOS A UNA SEDE
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab05' AND column_name='lab05c12')
BEGIN
    ALTER TABLE lab05 ALTER COLUMN lab05c12 VARCHAR(600);
END
-- DATOS DEL TRIBUNAL - Adición de llave primaria
/*IF EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab193]'))
BEGIN
    ALTER TABLE lab193 ADD CONSTRAINT lab193_PK PRIMARY KEY CLUSTERED (lab21c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON)
END*/
 --Modificacion de la tabla usuarios se adiciona el campo para validar si tiene permisos para editar los examenes en ingreso de ordenes 
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c35')
BEGIN
    ALTER TABLE lab04 ADD lab04c35 TINYINT;
END

--ID DE LA CABECERA HISTORICOS
/*IF EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab900_2020]'))
BEGIN
    IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab900_2020]') and sys.syscolumns.name = 'lab920c1')
    BEGIN
       ALTER TABLE lab900_2020 ADD lab920c1 INTEGER;
    END
    IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab900_2020]') and sys.syscolumns.name = 'lab920c1p')
    BEGIN
       ALTER TABLE lab900_2020 ADD lab920c1p INTEGER;
    END
END

IF EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab900_2019]'))
BEGIN
    IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab900_2019]') and sys.syscolumns.name = 'lab920c1')
    BEGIN
       ALTER TABLE lab900_2019 ADD lab920c1 INTEGER;
    END
    IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab900_2019]') and sys.syscolumns.name = 'lab920c1p')
    BEGIN
       ALTER TABLE lab900_2019 ADD lab920c1p INTEGER;
    END
END

IF EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab900_2018]'))
BEGIN
    IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab900_2018]') and sys.syscolumns.name = 'lab920c1')
    BEGIN
       ALTER TABLE lab900_2018 ADD lab920c1 INTEGER;
    END
    IF NOT EXISTS (select 1 from sys.syscolumns where id = object_id(N'[lab900_2018]') and sys.syscolumns.name = 'lab920c1p')
    BEGIN
       ALTER TABLE lab900_2018 ADD lab920c1p INTEGER;
    END
END*/

 --Modificacion de la tabla usuarios se adiciona el campo para validar si tiene permisos para editar los examenes en ingreso de ordenes 
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c36')
BEGIN
    ALTER TABLE lab04 ADD lab04c36 TINYINT not null DEFAULT 0;
END
 --Modificacion de la tabla entrevistas se adiciona el campo ordenar por entrevista
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab44' AND column_name='lab44c7')
BEGIN
    ALTER TABLE lab44 ADD lab44c7 TINYINT not null DEFAULT 0;
END



--SEGMENT
--NOTAS INTERNAS DEL RESULTADO
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab212]') )
BEGIN
    CREATE TABLE lab212
    (
        lab22c1 BIGINT NOT NULL ,
        lab39c1 INTEGER NOT NULL ,
        lab212c1 VARCHAR (4096) ,
        lab212c2 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL
    )
    ALTER TABLE lab212 ADD CONSTRAINT lab212_PK PRIMARY KEY CLUSTERED (lab22c1, lab39c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON)
END
--TIENE COMENTARIO INTERNO - RESULTADO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c61')
BEGIN
    ALTER TABLE lab57 ADD lab57c61 TINYINT not null DEFAULT 0;
END
--RESULTADO MODIFICADO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c62')
BEGIN
    ALTER TABLE lab57 ADD lab57c62 VARCHAR(30);
END
--TIENE COMENTARIO Recuentos - Antibiograma
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab204' AND column_name='lab204c4')
BEGIN
    ALTER TABLE lab204 ADD lab204c4 TEXT;
END
--TIENE COMENTARIO Determinaciones - Antibiograma
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab204' AND column_name='lab204c5')
BEGIN
    ALTER TABLE lab204 ADD lab204c5 TEXT;
END
--TIENE COMENTARIO complementaciones - Antibiograma
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab204' AND column_name='lab204c6')
BEGIN
    ALTER TABLE lab204 ADD lab204c6 TEXT;
END
  --Modificacion de la tabla Usuario adicionando Demograficos para consultas
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c37')
BEGIN
     ALTER TABLE lab04 ADD lab04c37 INTEGER  NULL;
END
--Modificacion de la tabla Usuario adicionando Item demograficos para consultas
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c38')
BEGIN
     ALTER TABLE lab04 ADD lab04c38 INTEGER  NULL;
END
--Agente etiologico
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab213]') )
BEGIN
    CREATE TABLE lab213 (
        lab213c1 INTEGER NOT NULL IDENTITY(1,1) ,
        lab213c2 SMALLINT NOT NULL, 
        lab213c3 VARCHAR (256) NOT NULL ,
        lab213c4 VARCHAR (128) NOT NULL ,
        lab213c5 SMALLINT NOT NULL,
        lab213c6 DATETIME NOT NULL ,
        lab04c1 INTEGER NOT NULL 
    )
    ALTER TABLE lab213 ADD CONSTRAINT lab213_PK PRIMARY KEY CLUSTERED (lab213c1) WITH (ALLOW_PAGE_LOCKS = ON, ALLOW_ROW_LOCKS = ON )
END

 --Modificacion de la tabla usuarios se adiciona el campo para validar si tiene permisos para eliminar caja
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c39')
BEGIN
    ALTER TABLE lab04 ADD lab04c39 TINYINT not null DEFAULT 0;
END
--Modificacion del campo correos a los que se envia los resultados
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab03' AND column_name='lab03c10')
BEGIN
    ALTER TABLE lab03 ALTER COLUMN lab03c10 VARCHAR (512);
END
--Modificacion de la tabla caja para validar el tipo de copago
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c14')
BEGIN
    ALTER TABLE lab902 ADD lab902c14 TINYINT not null DEFAULT 0;
END
-- COMENTARIO FIJO AL RESULTADO
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c63')
BEGIN
    ALTER TABLE lab57 ADD lab57c63 TEXT;
END
--RESULTADO MODIFICADO
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c62')
BEGIN
    ALTER TABLE lab57 ALTER COLUMN lab57c62 VARCHAR(600);
END
 --Modificacion de la tabla entrevistas se adiciona el campo ordenar por entrevista
IF EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab44' AND column_name='lab44c7')
BEGIN
    ALTER TABLE lab44 ALTER COLUMN lab44c7 TINYINT null;
END

-- destino de microbiologia verificado
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c64')
BEGIN
    ALTER TABLE lab57 ADD lab57c64 int;
END;


-- destino de laboratorio  externo  verificado
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c68')
BEGIN
    ALTER TABLE lab57 ADD lab57c68 int ;
END;


--si se a enviado automatico
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c65')
BEGIN
    ALTER TABLE lab57 ADD lab57c65 TINYINT DEFAULT 0;
END

-- usuario que deja pendiente examen
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c66')
BEGIN
    ALTER TABLE lab57 ADD lab57c66 int;
END;

-- ELIMINACION DEL CAMPO COMENTARIO DETERMINACIONES
IF EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name = 'lab204' AND column_name = 'lab204c5')
BEGIN
    ALTER TABLE lab204 DROP COLUMN lab204c5;
END

--COMENTARIO DETERMINACIONES
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c67')
BEGIN
    ALTER TABLE lab57 ADD lab57c67 TEXT;
END

-- email en item demografico
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab63' AND column_name='lab63c7')
BEGIN
    ALTER TABLE lab63 ADD lab63c7 VARCHAR(128);
END

-- CARGO DE DOMICILIO
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab101' AND column_name='lab101c7')
BEGIN
    ALTER TABLE lab101 ADD lab101c7 FLOAT NOT NULL DEFAULT 0;
END

-- MEDICOS AUXILIARES
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab194]') )
BEGIN
    CREATE TABLE lab194
    (
        lab22c1 BIGINT NOT NULL ,
        lab19c1 INTEGER NOT NULL
    );
END

-- se agrega email a tipo de orden
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab103' AND column_name='lab103c6')
BEGIN
    ALTER TABLE lab103 ADD lab103c6 VARCHAR(128);
END

-- se agrega email a servicio
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab10' AND column_name='lab10c10')
BEGIN
    ALTER TABLE lab10 ADD lab10c10 VARCHAR(128);
END

-- se agrega email a raza
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab08' AND column_name='lab08c6')
BEGIN
    ALTER TABLE lab08 ADD lab08c6 VARCHAR(128);
END

-- se agrega email a tipo de documento
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab54' AND column_name='lab54c6')
BEGIN
    ALTER TABLE lab54 ADD lab54c6 VARCHAR(128);
END

-- CARGO DE DOMICILIO CAJA
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c15')
BEGIN
    ALTER TABLE lab902 ADD lab902c15 FLOAT DEFAULT 0;
END   

-- Observaciones 
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab22c16')
BEGIN
    ALTER TABLE lab22 ADD lab22c16 TEXT;
END

 --Editar observaciones en usuarios
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c40')
BEGIN
    ALTER TABLE lab04 ADD lab04c40 TINYINT;
END

-- Ordenamiento de demograficos
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab195]') )
BEGIN
    CREATE TABLE lab195
    (
        lab62c1  INTEGER NOT NULL ,
        lab195c1 INTEGER,
        lab195c2 VARCHAR(32),
        lab04c1  INTEGER  
    );
END
-- FECHA EN QUE SE CIERRA LA GRADILLA
IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='lab16' AND column_name='lab16c10')
BEGIN
    ALTER TABLE lab16 ADD lab16c10 DATETIME;
END
-- AUDITORIA DE GRADILLA CERRADA
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab271')
BEGIN
    CREATE TABLE lab271(
        lab16c1 INTEGER NOT NULL,
        lab271c1 VARCHAR(32),
        lab31c1 INTEGER, 
        lab04c1 INTEGER, 
        lab271c2 DATETIME ,
        lab27c1 INTEGER
    );
END
--CARGO DOMICILIO
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab908' AND column_name='lab908c9')
BEGIN
    ALTER TABLE lab908 ADD lab908c9 FLOAT;
END

-- EnterpriseNT_CIADE marcar  registros que ya se procesaron
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c69')
BEGIN
    ALTER TABLE lab57 ADD lab57c69 int;
END;

-- CAMPO examenes CPT para almacenar esos codigos 
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c62')
BEGIN
    ALTER TABLE lab39 ADD lab39c62 varchar(200);
END

 --Editar pagos en usuarios
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c41')
BEGIN
    ALTER TABLE lab04 ADD lab04c41 TINYINT;
END

-- Facturado a
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c16')
BEGIN
    ALTER TABLE lab902 ADD lab902c16 VARCHAR(256);
END   

-- RUC
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c17')
BEGIN
    ALTER TABLE lab902 ADD lab902c17 VARCHAR(64);
END   

--TIPO DE CREDITO
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c18')
BEGIN
    ALTER TABLE lab902 ADD lab902c18 TINYINT;
END

 --Editar resultados por usuario
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab04' AND column_name='lab04c42')
BEGIN
    ALTER TABLE lab04 ADD lab04c42 TINYINT;
END

--Identifica si la caja ya fue enviada a kbits
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c19')
BEGIN
    ALTER TABLE lab902 ADD lab902c19 TINYINT;
END

--Factura combo en caja
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c20')
BEGIN
    ALTER TABLE lab902 ADD lab902c20 TINYINT;
END

-- Facturas combo
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab930')
BEGIN
    CREATE TABLE lab930 (
        lab930c1 BIGINT NOT NULL IDENTITY(1,1),
        lab930c2 DATETIME NOT NULL,
        lab04c1 INTEGER NOT NULL, 
        lab930c3 INTEGER NOT NULL, 
        lab930c4 VARCHAR(516)
    );
    ALTER TABLE lab930 ADD CONSTRAINT lab930_PK PRIMARY KEY CLUSTERED (lab930c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Id de la factura combo 
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab930c1')
BEGIN
    ALTER TABLE lab22 ADD lab930c1 INTEGER;
END

--IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab22_2022' AND column_name='lab930c1')
--BEGIN
  --  ALTER TABLE lab22_2022 ADD lab930c1 INTEGER;
--END

-- Notas credito Facturas combo
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab931')
BEGIN
    CREATE TABLE lab931 (
        lab931c1 BIGINT NOT NULL IDENTITY(1,1),
        lab930c1 INTEGER NOT NULL,
        lab931c2 DATETIME NOT NULL,
        lab04c1 INTEGER NOT NULL, 
        lab931c3 VARCHAR(516),
        lab931c4 INTEGER
    );
    ALTER TABLE lab931 ADD CONSTRAINT lab931_PK PRIMARY KEY CLUSTERED (lab931c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

--CAMPO IDIOMA INGLES RESULTADOS LITERALES
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab50' AND column_name='lab50c4')
BEGIN      
    ALTER TABLE lab50 ADD lab50c4 varchar(64);
END

-- Resultados en ingles
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c70')
BEGIN
    ALTER TABLE lab57 ADD lab57c70 varchar(600);
END;

--Modificacion de la tabla Demograficos con campo filtro para hora prometida
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab62' AND column_name='lab62c15')
BEGIN
     ALTER TABLE lab62 ADD lab62c15 INTEGER; 
END

 --Modificacion de la tabla de medicos auxiliares
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab194' AND column_name='lab194c1')
BEGIN
     ALTER TABLE lab194 ADD lab194c1 INTEGER; 
END

-- Configuración de la impresion
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab22c17')
BEGIN
    ALTER TABLE lab22 ADD lab22c17 TEXT;
END

-- TELEFONO CAJA
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c21')
BEGIN
    ALTER TABLE lab902 ADD lab902c21 VARCHAR(32);
END 
--TIPO DE INGRESO DE LA ORDEN HIS, MANUAL
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab22c18')
BEGIN
    ALTER TABLE lab22 ADD lab22c18 TINYINT;
END

-- SE ADICION El COMENTARIO DEL RESULTADO 2 AL MAESTRO DE EXAMENES
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c63')
BEGIN
    ALTER TABLE lab39 ADD lab39c63 VARCHAR(8000);
END

-- SE ADICION El COMENTARIO DEL RESULTADO 2
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c71')
BEGIN
    ALTER TABLE lab57 ADD lab57c71 TEXT;
END

-- Auditoria de paquetes
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab301')
BEGIN
    CREATE TABLE lab301 (
        lab301c1 BIGINT NOT NULL IDENTITY(1,1),
        lab301c2 INTEGER NOT NULL,
        lab301c3 VARCHAR(128),
        lab301c4 INTEGER NOT NULL, 
        lab04c1 INTEGER NOT NULL, 
        lab301c5 DATETIME NOT NULL,
        lab22c1 BIGINT NOT NULL
    );
    ALTER TABLE lab301 ADD CONSTRAINT lab301_PK PRIMARY KEY CLUSTERED (lab301c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab48' AND column_name='lab48c20')
BEGIN
    ALTER TABLE lab48 ADD lab48c20 VARCHAR(2050);
END

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab181' AND column_name='lab04c1')
BEGIN
    ALTER TABLE lab181 ADD lab04c1 INTEGER;
END

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab181' AND column_name='lab181c15')
BEGIN
    ALTER TABLE lab181 ADD lab181c15 DATETIME;
END

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab171' AND column_name='lab04c1')
BEGIN
    ALTER TABLE lab171 ADD lab04c1 INTEGER;
END

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab171' AND column_name='lab171c5')
BEGIN
    ALTER TABLE lab171 ADD lab171c5 DATETIME;
END

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab157' AND column_name='lab04c1')
BEGIN
    ALTER TABLE lab157 ADD lab04c1 INTEGER;
END

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab157' AND column_name='lab157c3')
BEGIN
    ALTER TABLE lab157 ADD lab157c3 DATETIME;
END

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab104' AND column_name='lab04c1')
BEGIN
    ALTER TABLE lab104 ADD lab04c1 INTEGER;
END

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab104' AND column_name='lab104c3')
BEGIN
    ALTER TABLE lab104 ADD lab104c3 DATETIME;
END

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab145' AND column_name='lab04c1')
BEGIN
    ALTER TABLE lab145 ADD lab04c1 INTEGER;
END

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab145' AND column_name='lab145c2')
BEGIN
    ALTER TABLE lab145 ADD lab145c2 DATETIME;
END

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab190' AND column_name='lab04c1')
BEGIN
    ALTER TABLE lab190 ADD lab04c1 INTEGER;
END

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab190' AND column_name='lab190c5')
BEGIN
    ALTER TABLE lab190 ADD lab190c5 DATETIME;
END

-- SE ADICIONA VER PAQUETE DESPLEGADO EN TALON
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab39' AND column_name='lab39c64')
BEGIN
    ALTER TABLE lab39 ADD lab39c64 INTEGER;
END

IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab63' AND column_name='lab63c8')
BEGIN      
    ALTER TABLE lab63 ADD lab63c8 VARCHAR(128);
END

--COMENTARIO EN INGLES
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab68' AND column_name='lab68c7')
BEGIN      
    ALTER TABLE lab68 ADD lab68c7 TEXT;
END

--indicador para envio a tableros en la validacion del examen
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c72')
BEGIN
    ALTER TABLE lab57 ADD lab57c72 SMALLINT;
END

-- SALDO COMPAÑIA CAJA
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab902' AND column_name='lab902c22')
BEGIN
    ALTER TABLE lab902 ADD lab902c22 FLOAT;
END 

--SALDO COMPAÑIA TOTALES
IF NOT EXISTS(SELECT * FROM information_schema.columns WHERE table_name='lab908' AND column_name='lab908c10')
BEGIN
    ALTER TABLE lab908 ADD lab908c10 FLOAT;
END

--indicador para envio a tableros en la creacion del examen
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab57' AND column_name='lab57c73')
BEGIN
    ALTER TABLE lab57 ADD lab57c73 SMALLINT;
END

 -- INDICADOR PARA ACTUALIZAR LA CONTRASEÑA DEL PACIENTE
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab21' AND column_name='lab21c24')
BEGIN
    ALTER TABLE lab21 ADD lab21c24 SMALLINT;
END

-- guardado de remisiones
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab500')
BEGIN
    CREATE TABLE lab500 (
        lab22c1 BIGINT NOT NULL, --orden
        lab39c1 INTEGER NOT NULL, --examen
        lab500c1 SMALLINT, --si ya se envio al central
        lab500c2 INTEGER, 
        lab500c3 SMALLINT --si ya se recepciono el resultado
  
    );
    ALTER TABLE lab500 ADD CONSTRAINT lab500_PK PRIMARY KEY CLUSTERED (lab22c1, lab39c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--Modificacion de la tabla Sedes lab05 adicion de campo Imagen ministerio de salud
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab05' AND column_name='lab05c15')
BEGIN
    ALTER TABLE lab05 ADD lab05c15 INTEGER;
END
--indicador para cita
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab22' AND column_name='lab22c19')
BEGIN
    ALTER TABLE lab22 ADD lab22c19 SMALLINT;
END
--Plantillas cubo
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab35]') )
BEGIN
    CREATE TABLE lab35
    (
        lab35c1     INTEGER NOT NULL IDENTITY(1,1) ,
        lab35c2     VARCHAR (50) NOT NULL,  
        lab35c3     VARCHAR(1050),
        lab35c4     VARCHAR(1050),
        lab35c5     VARCHAR(1050),
        lab35c6     DATETIME NOT NULL ,
        lab35c7     DATETIME NOT NULL 
    )
    ALTER TABLE lab35 ADD CONSTRAINT lab35_PK PRIMARY KEY CLUSTERED (lab35c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON)
END

-- Agrupar por perfiles cubo
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab35' AND column_name='lab35c8')
BEGIN
    ALTER TABLE lab35 ADD lab35c8 INTEGER;
END

-- Ordenamiento de los demograficos en plantilla cubo
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab35' AND column_name='lab35c9')
BEGIN
    ALTER TABLE lab35 ADD lab35c9 VARCHAR(1050);
END

------------MODULO DE CITAS

--SEGMENT
--JORNADA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[hmb09]') )
BEGIN
    CREATE TABLE hmb09
    (
        hmb09c1 INTEGER NOT NULL IDENTITY(1,1) ,
        hmb09c2 VARCHAR (64) NOT NULL ,
        hmb09c3 VARCHAR (16) NOT NULL ,
        hmb09c4 INTEGER NOT NULL ,
        hmb09c5 INTEGER NOT NULL ,
        hmb09c6 DATETIME NOT NULL ,
        hmb03c1 INTEGER NOT NULL ,
        hmb07c1 TINYINT NOT NULL

    )
    ALTER TABLE hmb09 ADD CONSTRAINT hmb09_PK PRIMARY KEY CLUSTERED (hmb09c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
 END
--SEGMENT
--JORNADA - SEDE
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[hmb10]') )
BEGIN
    CREATE TABLE hmb10
    (
        lab05c1 INTEGER NOT NULL ,
        hmb09c1 INTEGER NOT NULL ,
        hmb10c1 INTEGER NOT NULL
    )

    ALTER TABLE hmb10 ADD CONSTRAINT hmb10_PK PRIMARY KEY CLUSTERED (lab05c1, hmb09c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--CITA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[hmb12]') )
BEGIN
    CREATE TABLE hmb12 
    (
        hmb12c1 INTEGER NOT NULL IDENTITY(1,1) , --id
        hmb09c1 INTEGER NOT NULL , --jornada
        hmb12c2 INTEGER NOT NULL , --fecha
        hmb12c3 DATETIME NOT NULL , --fecha de creacion
        lab04c1 INTEGER NOT NULL ,--usuario de creacion
        hmb12c4 INTEGER NOT NULL , --estado de la cita
        hmb12c5 BIGINT NOT NULL,  --numero de orden
        lab05c1 INTEGER NOT NULL
    )

    ALTER TABLE hmb12 ADD CONSTRAINT hmb12_PK PRIMARY KEY CLUSTERED (hmb12c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--AGENDAMIENTO CITA - CONCURRENCIA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[hmb13]') )
BEGIN
    CREATE TABLE hmb13
    (
        lab05c1 INTEGER NOT NULL ,
        hmb09c1 INTEGER NOT NULL ,
        hmb13c1 INTEGER NOT NULL ,
        hmb03c1_1 INTEGER NOT NULL,
        hmb13c2   INTEGER NOT NULL IDENTITY(1,1)
    )

    ALTER TABLE hmb13 ADD CONSTRAINT hmb13_PK PRIMARY KEY CLUSTERED (hmb13c2) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

--SEGMENT
--TRAZABILIDAD DE LA CITA (ESTADOS)
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[hmb16]'))
BEGIN
    CREATE TABLE hmb16 
    (
        hmb12c1 INTEGER NOT NULL , 
        hmb16c1 INTEGER NOT NULL , 
        hmb30c1 INTEGER , 
        hmb16c2 TEXT , 
        hmb16c3 DATETIME NOT NULL , 
        hmb03c1 INTEGER NOT NULL, 
        hmb03c1_1 INTEGER,
        hmb16c4 INTEGER
    )
END

--CONTROL ENTREGA
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[lab166]') )
BEGIN
    CREATE TABLE lab166
    (
        lab166c1    INTEGER NOT NULL IDENTITY(1,1) ,
        lab22c1     BIGINT,
        lab166c2    VARCHAR (1050) NOT NULL,
        lab166c3    TEXT NOT NULL,
        lab166c4    INTEGER NOT NULL, 
        lab166c5    VARCHAR (256) NOT NULL, 
        lab04c1     INTEGER NOT NULL, 
        lab166c6    DATETIME NOT NULL,
        lab166c7    VARCHAR (256)
    )
    ALTER TABLE lab166 ADD CONSTRAINT lab166_PK PRIMARY KEY CLUSTERED (lab166c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
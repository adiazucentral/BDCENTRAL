--Script para la creacion y actualizacion de la base de datos de patologia de SQLServer

-- Casos
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat01]') )
BEGIN
    CREATE TABLE pat01
    (
        pat01c1     INTEGER NOT NULL IDENTITY(1,1),
        pat01c2     BIGINT NOT NULL,
        pat11c1     INTEGER NOT NULL,
        pat01c3     INTEGER NOT NULL,
        lab22c1     BIGINT NOT NULL,
        pat01c4     INTEGER NOT NULL,
        lab05c1     INTEGER NOT NULL,
        pat01c5     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat01c6     DATETIME,
        lab04c1b    INTEGER,
        lab04c1c    INTEGER
    )
    ALTER TABLE pat01 ADD CONSTRAINT pat01_PK PRIMARY KEY CLUSTERED (pat01c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Areas
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat02]') )
BEGIN
    CREATE TABLE pat02
    (
        pat02c1     INTEGER NOT NULL IDENTITY(1,1),
        pat02c2     VARCHAR(50) NOT NULL,
        pat02c3     VARCHAR(150) NOT NULL,
        pat02c4     INTEGER NOT NULL,
        pat02c5     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat02c6     DATETIME,
        lab04c1b    INTEGER
    )
    ALTER TABLE pat02 ADD CONSTRAINT pat02_PK PRIMARY KEY CLUSTERED (pat02c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Casetes
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat03]') )
BEGIN
    CREATE TABLE pat03
    (
        pat03c1     INTEGER NOT NULL IDENTITY(1,1),
        pat03c2     VARCHAR(50) NOT NULL,
        pat03c3     VARCHAR(150) NOT NULL,
        pat03c4     INTEGER NOT NULL,
        pat03c5     VARCHAR(50) NOT NULL,
        pat03c6     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat03c7     DATETIME,
        lab04c1b    INTEGER
    )
    ALTER TABLE pat03 ADD CONSTRAINT pat03_PK PRIMARY KEY CLUSTERED (pat03c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Submuestras
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat04]') )
BEGIN
    CREATE TABLE pat04
    (
        pat04c1     INTEGER NOT NULL IDENTITY(1,1),
        lab24c1     INTEGER NOT NULL,
        pat04c2     INTEGER NOT NULL,
        pat04c3     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat04c4     DATETIME,
        lab04c1b    INTEGER
    )
    ALTER TABLE pat04 ADD CONSTRAINT pat04_PK PRIMARY KEY CLUSTERED (pat04c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Contenedores
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat05]') )
BEGIN
    CREATE TABLE pat05
    (
        pat05c1     INTEGER NOT NULL IDENTITY(1,1),
        pat05c2     VARCHAR(150) NOT NULL,
        pat05c3     IMAGE,
        pat05c4     INTEGER NOT NULL,
        pat05c5     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat05c6     DATETIME,
        lab04c1b    INTEGER
    )
    ALTER TABLE pat05 ADD CONSTRAINT pat05_PK PRIMARY KEY CLUSTERED (pat05c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Coloraciones
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat06]') )
BEGIN
    CREATE TABLE pat06
    (
        pat06c1     INTEGER NOT NULL IDENTITY(1,1),
        pat06c2     VARCHAR(50) NOT NULL,
        pat06c3     VARCHAR(150) NOT NULL,
        pat06c4     INTEGER NOT NULL,
        pat06c5     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat06c6     DATETIME,
        lab04c1b    INTEGER
    )
    ALTER TABLE pat06 ADD CONSTRAINT pat06_PK PRIMARY KEY CLUSTERED (pat06c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Organo
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat07]') )
BEGIN
    CREATE TABLE pat07
    (
        pat07c1     INTEGER NOT NULL IDENTITY(1,1),
        pat07c2     VARCHAR(50) NOT NULL,
        pat07c3     VARCHAR(250) NOT NULL,
        pat07c4     INTEGER NOT NULL,
        pat07c5     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat07c6     DATETIME,
        lab04c1b    INTEGER
    )
    ALTER TABLE pat07 ADD CONSTRAINT pat07_PK PRIMARY KEY CLUSTERED (pat07c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Especimen - Casete - Lamina
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat08]') )
BEGIN
    CREATE TABLE pat08
    (
        pat08c1     INTEGER NOT NULL IDENTITY(1,1),
        pat07c1     INTEGER NOT NULL,
        pat06c1     INTEGER NOT NULL,
        pat08c2     INTEGER NOT NULL
    )
    ALTER TABLE pat08 ADD CONSTRAINT pat08_PK PRIMARY KEY CLUSTERED (pat08c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Configuracion de especimenes
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat09]') )
BEGIN
    CREATE TABLE pat09
    (
        pat09c1     INTEGER NOT NULL IDENTITY(1,1),
        lab24c1     INTEGER NOT NULL,
        pat07c1     INTEGER,
        pat03c1     INTEGER NOT NULL,
        pat09c2     INTEGER NOT NULL,
        pat09c3     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat09c4     DATETIME,
        lab04c1b    INTEGER 
    )
    ALTER TABLE pat09 ADD CONSTRAINT pat09_PK PRIMARY KEY CLUSTERED (pat09c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Fijadores
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat10]') )
BEGIN
    CREATE TABLE pat10
    (
        pat10c1     INTEGER NOT NULL IDENTITY(1,1),
        pat10c2     VARCHAR(50) NOT NULL,
        pat10c3     VARCHAR(150) NOT NULL,
        pat10c4     INTEGER NOT NULL,
        pat10c5     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat10c6     DATETIME,
        lab04c1b    INTEGER
    )
    ALTER TABLE pat10 ADD CONSTRAINT pat10_PK PRIMARY KEY CLUSTERED (pat10c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Tipos de estudio
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat11]') )
BEGIN
    CREATE TABLE pat11
    (
        pat11c1     INTEGER NOT NULL IDENTITY(1,1),
        pat11c2     VARCHAR(50) NOT NULL,
        pat11c3     VARCHAR(150) NOT NULL,
        pat11c4     INTEGER NOT NULL,
        pat11c5     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat11c6     DATETIME,
        lab04c1b    INTEGER
    )
    ALTER TABLE pat11 ADD CONSTRAINT pat11_PK PRIMARY KEY CLUSTERED (pat11c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Estudios tipos de estudio
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat12]') )
BEGIN
    CREATE TABLE pat12
    (
        pat12c1     INTEGER NOT NULL IDENTITY(1,1),
        pat11c1     INTEGER NOT NULL,
        lab39c1     INTEGER NOT NULL
    )
    ALTER TABLE pat12 ADD CONSTRAINT pat12_PK PRIMARY KEY CLUSTERED (pat12c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Caso - Especimen
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat13]') )
BEGIN
    CREATE TABLE pat13
    (
        pat13c1     INTEGER NOT NULL IDENTITY(1,1),
        pat01c1     INTEGER NOT NULL,
        lab24c1     INTEGER NOT NULL,
        pat04c1     INTEGER,
        pat07c1     INTEGER NOT NULL
    )
    ALTER TABLE pat13 ADD CONSTRAINT pat13_PK PRIMARY KEY CLUSTERED (pat13c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Muestras
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat14]') )
BEGIN
    CREATE TABLE pat14
    (
        pat14c1     INTEGER NOT NULL IDENTITY(1,1),
        pat14c2     INTEGER NOT NULL,
        pat05c1     INTEGER NOT NULL,
        pat10c1     INTEGER NOT NULL,
        pat13c1     INTEGER NOT NULL
    )
    ALTER TABLE pat14 ADD CONSTRAINT pat14_PK PRIMARY KEY CLUSTERED (pat14c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Estudios
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat15]') )
BEGIN
    CREATE TABLE pat15
    (
        pat13c1     INTEGER NOT NULL,
        lab39c1     INTEGER NOT NULL
    )
    ALTER TABLE pat15 ADD CONSTRAINT pat15_PK PRIMARY KEY CLUSTERED (pat13c1, lab39c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Muestras rechazadas
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat16]') )
BEGIN
    CREATE TABLE pat16
    (
        pat16c1     INTEGER NOT NULL IDENTITY(1,1),
        lab22c1     BIGINT NOT NULL,
        pat11c1     INTEGER NOT NULL,
        lab30c1     INTEGER NOT NULL,
        pat16c2     TEXT,
        pat16c3     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat16c4     DATETIME,
        lab04c1b    INTEGER
    )
    ALTER TABLE pat16 ADD CONSTRAINT pat16_PK PRIMARY KEY CLUSTERED (pat16c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Agenda
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat17]') )
BEGIN
    CREATE TABLE pat17
    (
        pat17c1     INTEGER NOT NULL IDENTITY(1,1),
        lab04c1a    INTEGER NOT NULL,
        pat17c2     DATETIME NOT NULL,
        pat17c3     DATETIME NOT NULL,
        pat17c4     INTEGER,
        pat17c5     DATETIME NOT NULL,
        pat21c1     INTEGER,
        lab04c1b    INTEGER NOT NULL,
        pat17c6     DATETIME,
        lab04c1c    INTEGER
    )
    ALTER TABLE pat17 ADD CONSTRAINT pat17_PK PRIMARY KEY CLUSTERED (pat17c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Casetes - Muestras
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat18]') )
BEGIN
    CREATE TABLE pat18
    (
        pat18c1     INTEGER NOT NULL IDENTITY(1,1),
        pat14c1     INTEGER NOT NULL,
        pat18c2     INTEGER NOT NULL,
        pat18c3     VARCHAR(5) NOT NULL,
        pat03c1     INTEGER NOT NULL
    )
    ALTER TABLE pat18 ADD CONSTRAINT pat18_PK PRIMARY KEY CLUSTERED (pat18c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Especialidades
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat19]') )
BEGIN
    CREATE TABLE pat19
    (
        pat19c1     INTEGER NOT NULL IDENTITY(1,1),
        lab04c1a    INTEGER NOT NULL,
        pat07c1     INTEGER NOT NULL,
        pat19c2     DATETIME NOT NULL,
        lab04c1b    INTEGER NOT NULL,
        pat19c3     DATETIME,
        lab06c1c    INTEGER
    )
    ALTER TABLE pat19 ADD CONSTRAINT pat19_PK PRIMARY KEY CLUSTERED (pat19c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Archivos
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat20]') )
BEGIN
    CREATE TABLE pat20
    (
        pat20c1     INTEGER NOT NULL IDENTITY(1,1),
        pat01c1     INTEGER NOT NULL,
        pat20c2     IMAGE NOT NULL,
        pat20c3     VARCHAR (50) NOT NULL,
        pat20c4     VARCHAR (50) NOT NULL,
        pat20c5     VARCHAR (50) NOT NULL,
        pat20c6     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat20c7     DATETIME,
        lab04c1b    INTEGER
    )
    ALTER TABLE pat20 ADD CONSTRAINT pat20_PK PRIMARY KEY CLUSTERED (pat20c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Nuevo campo para el maestro de contenedores
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat05' AND column_name='pat05c7')
BEGIN
    ALTER TABLE pat05 ADD pat05c7 SMALLINT NOT NULL DEFAULT 0;
END

-- Codigos de barras
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat100]') )
BEGIN
    CREATE TABLE pat100
    (
        pat100c1    INTEGER NOT NULL IDENTITY(1,1),
        pat100c2    TEXT,
        pat100c3    INTEGER,
        pat100c4    INTEGER,
        pat100c5    TEXT
    )
    ALTER TABLE pat100 ADD CONSTRAINT pat100_PK PRIMARY KEY CLUSTERED (pat100c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Eventos
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat21]') )
BEGIN
    CREATE TABLE pat21
    (
        pat21c1     INTEGER NOT NULL IDENTITY(1,1),
        pat21c2     VARCHAR(50) NOT NULL,
        pat21c3     VARCHAR(150) NOT NULL,
        pat21c4     VARCHAR(50) NOT NULL,
        pat21c5     INTEGER NOT NULL,
        pat21c6     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat21c7     DATETIME,
        lab04c1b    INTEGER
    )
    ALTER TABLE pat21 ADD CONSTRAINT pat21_PK PRIMARY KEY CLUSTERED (pat21c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Macroscopia
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat25]') )
BEGIN
    CREATE TABLE pat25
    (
        pat25c1     INTEGER NOT NULL IDENTITY(1,1),
        pat01c1     INTEGER NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat25c3     DATETIME NOT NULL,
        pat25c4     INTEGER NOT NULL,
        pat28c1     INTEGER
    )
    ALTER TABLE pat25 ADD CONSTRAINT pat25_PK PRIMARY KEY CLUSTERED (pat25c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Campos
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat26]') )
BEGIN
    CREATE TABLE pat26
    (
        pat26c1     INTEGER NOT NULL IDENTITY(1,1),
        pat26c2     VARCHAR(50) NOT NULL,
        pat26c3     INTEGER NOT NULL,
        pat26c4     INTEGER NOT NULL,
        pat26c5     INTEGER NOT NULL,
        pat26c6     INTEGER NOT NULL,
        pat26c7     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat26c8     DATETIME,
        lab04c1b    INTEGER
    )
    ALTER TABLE pat26 ADD CONSTRAINT pat26_PK PRIMARY KEY CLUSTERED (pat26c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Plantillas
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat27]') )
BEGIN
    CREATE TABLE pat27
    (
        pat27c1     INTEGER NOT NULL IDENTITY(1,1),
        lab24c1     INTEGER NOT NULL,
        pat26c1     INTEGER NOT NULL,
        pat27c2     INTEGER NOT NULL,
        pat27c3     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat27c4     DATETIME,
        lab04c1b    INTEGER
    )
    ALTER TABLE pat27 ADD CONSTRAINT pat27_PK PRIMARY KEY CLUSTERED (pat27c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Audios
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat28]') )
BEGIN
    CREATE TABLE pat28
    (
        pat28c1     INTEGER NOT NULL IDENTITY(1,1),
        pat28c2     VARCHAR (150) NOT NULL,
        pat28c3     VARCHAR (50) NOT NULL,
        pat28c4     VARCHAR (250) NOT NULL,
        pat28c5     DATETIME NOT NULL,
        lab04c1     INTEGER NOT NULL
    )
    ALTER TABLE pat28 ADD CONSTRAINT pat28_PK PRIMARY KEY CLUSTERED (pat28c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Valores de plantillas
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat29]') )
BEGIN
    CREATE TABLE pat29
    (
        pat29c1     INTEGER NOT NULL IDENTITY(1,1),
        pat25c1     INTEGER NOT NULL,
        pat29c2     TEXT NOT NULL,
        pat27c1     INTEGER
    )
    ALTER TABLE pat29 ADD CONSTRAINT pat29_PK PRIMARY KEY CLUSTERED (pat29c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Nuevo campo para las descripciones macroscopicas. Indica la persona que realiza la transcripcion
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat25' AND column_name='lab04c1b')
BEGIN
    ALTER TABLE pat25 ADD lab04c1b INTEGER DEFAULT NULL;
END

-- Nuevo campo para las descripciones macroscopicas. Indica la fecha de la transcripcion
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat25' AND column_name='pat25c5')
BEGIN
    ALTER TABLE pat25 ADD pat25c5 DATETIME DEFAULT NULL;
END

-- Nuevo campo para las descripciones macroscopicas. Indica si la transcripcion necesita autorización
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat25' AND column_name='pat25c6')
BEGIN
    ALTER TABLE pat25 ADD pat25c6 INTEGER NOT NULL DEFAULT 0;
END

-- Nuevo campo para las descripciones macroscopicas. Indica la persona que autoriza la transcripcion
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat25' AND column_name='lab04c1c')
BEGIN
    ALTER TABLE pat25 ADD lab04c1c INTEGER DEFAULT NULL;
END

-- Nuevo campo para las descripciones macroscopicas. Indica la fecha de la autorizacion
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat25' AND column_name='pat25c7')
BEGIN
    ALTER TABLE pat25 ADD pat25c7 DATETIME DEFAULT NULL;
END

-- Nuevo campo para los protocolos. Indica las horas de procesamiento de las muestras
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat09' AND column_name='pat09c5')
BEGIN
    ALTER TABLE pat09 ADD pat09c5 INTEGER NOT NULL DEFAULT 1;
END

-- Horarios de procesamiento
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat31]') )
BEGIN
    CREATE TABLE pat31
    (
        pat31c1     INTEGER NOT NULL IDENTITY(1,1),
        pat31c2     VARCHAR (10) NOT NULL,
        pat31c3     INTEGER NOT NULL,
        pat31c4     DATETIME NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat31c5     DATETIME,
        lab04c1b    INTEGER
    )
    ALTER TABLE pat31 ADD CONSTRAINT pat31_PK PRIMARY KEY CLUSTERED (pat31c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Lista de casetes
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat32]') )
BEGIN
    CREATE TABLE pat32
    (
        pat32c1     INTEGER NOT NULL IDENTITY(1,1),
        pat14c1     INTEGER NOT NULL,
        pat32c2     VARCHAR(5) NOT NULL,
        pat03c1     INTEGER NOT NULL,
        pat18c1     INTEGER NOT NULL
    )
    ALTER TABLE pat32 ADD CONSTRAINT pat32_PK PRIMARY KEY CLUSTERED (pat32c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Nuevo campo para los casetes. Indica el estado del casete en el area de histotecnologia
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='pat32' AND column_name='pat32c3')
BEGIN
    ALTER TABLE pat32 ADD pat32c3 INTEGER NOT NULL DEFAULT 1;
END

-- Procesador de tejidos
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat33]') )
BEGIN
    CREATE TABLE pat33
    (
        pat33c1     INTEGER NOT NULL IDENTITY(1,1),
        pat32c1     INTEGER,
        pat33c2     INTEGER NOT NULL,
        pat31c1     INTEGER NOT NULL
    )
    ALTER TABLE pat33 ADD CONSTRAINT pat33_PK PRIMARY KEY CLUSTERED (pat33c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

-- Trazabilidad de Casetes
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[pat34]') )
BEGIN
    CREATE TABLE pat34
    (
        pat34c1     INTEGER NOT NULL IDENTITY(1,1),
        pat32c1     INTEGER NOT NULL,
        pat34c2     INTEGER NOT NULL,
        lab04c1a    INTEGER NOT NULL,
        pat34c3     DATETIME NOT NULL
    )
    ALTER TABLE pat34 ADD CONSTRAINT pat34_PK PRIMARY KEY CLUSTERED (pat34c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
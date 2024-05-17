--Script para la creacion y actualizacion de la base de datos transaccional de SQLServer
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[rep01]') )
BEGIN
    CREATE TABLE rep01
    (
        lab22c1 BIGINT NOT NULL ,
        rep01c1 TEXT NOT NULL ,
        rep01c2 DATETIME NOT NULL
    )
    ALTER TABLE rep01 ADD CONSTRAINT rep01_pk PRIMARY KEY CLUSTERED (lab22c1, rep01c1, rep01c2) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END

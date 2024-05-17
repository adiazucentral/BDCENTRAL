-- Script para actualizar el nombre de todas las tablas de operación de NT:

/**
 * Author:  javila
 * Created: 22/06/2021
 */

CREATE PROCEDURE [dbo].[RENAME_NEW_YEAR_STA] @param1 VARCHAR(4)
WITH ENCRYPTION
AS
BEGIN
        DECLARE @param1_v VARCHAR(4)
        SET @param1_v = @param1
        
        -- RENOMBRE DE LAS TABLAS Y DE SU LLAVE PRIMARIA
        -- STA2
        DECLARE @sta2 VARCHAR(15)
        SET @sta2 = 'sta2_' + @param1_v;
        DECLARE @sta2_pk VARCHAR(15)
        SET @sta2_pk = 'sta2_PK_' + @param1_v;
        DECLARE @sta2_idk VARCHAR(20)
        SET @sta2_idk = 'sta2_IDX_' + @param1_v;
        EXEC sp_rename 'sta2.sta2__IDX', @sta2_idk;
        EXEC sp_rename 'sta2', @sta2;
        EXEC sp_rename 'sta2_PK', @sta2_pk;
        -- STA3
        DECLARE @sta3 VARCHAR(15)
        SET @sta3 = 'sta3_' + @param1_v;
        DECLARE @sta3_pk VARCHAR(15)
        SET @sta3_pk = 'sta3_PK_' + @param1_v;
        DECLARE @sta3_idk VARCHAR(20)
        SET @sta3_idk = 'sta3_IDX_' + @param1_v;
        EXEC sp_rename 'sta3.sta3__IDX', @sta3_idk;
        EXEC sp_rename 'sta3', @sta3;
        EXEC sp_rename 'sta3_PK', @sta3_pk;
        -- STA4
        DECLARE @sta4 VARCHAR(15)
        SET @sta4 = 'sta4_' + @param1_v;
        DECLARE @sta4_pk VARCHAR(15)
        SET @sta4_pk = 'sta4_PK_' + @param1_v;
        EXEC sp_rename 'sta4', @sta4;
        EXEC sp_rename 'sta4_PK', @sta4_pk;
END
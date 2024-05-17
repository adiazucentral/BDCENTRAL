-- Script para actualizar el nombre de todas las tablas de operación de NT:

/**
 * Author:  javila
 * Created: 18/06/2021
 */

CREATE PROCEDURE [dbo].[RENAME_NEW_YEAR] @param1 VARCHAR(4)
WITH ENCRYPTION
AS
BEGIN
        DECLARE @param1_v VARCHAR(4)
        SET @param1_v = @param1
        
        -- RENOMBRE DE LAS TABLAS Y DE SU LLAVE PRIMARIA
        -- LAB22
        DECLARE @lab22 VARCHAR(15)
        SET @lab22 = 'lab22_' + @param1_v;
        DECLARE @lab22_pk VARCHAR(15)
        SET @lab22_pk = 'lab22_PK_' + @param1_v;
        DECLARE @lab22_idk VARCHAR(20)
        SET @lab22_idk = 'lab22c2_IDX_' + @param1_v;
        EXEC sp_rename 'lab22.lab22c2__IDX', @lab22_idk;
        EXEC sp_rename 'lab22', @lab22;
        EXEC sp_rename 'lab22_PK', @lab22_pk;
        -- Clonación de lab22 
        EXECUTE ('SELECT * INTO lab22 FROM lab22_' + @param1_v);
        EXECUTE ('CREATE INDEX lab22c2__IDX ON lab22 (lab22c2)');
        EXECUTE ('ALTER TABLE lab22 ADD CONSTRAINT lab22_PK PRIMARY KEY CLUSTERED (lab22c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )');
        -- LAB180
        DECLARE @lab180 VARCHAR(15)
        SET @lab180 = 'lab180_' + @param1_v;
        EXEC sp_rename 'lab180', @lab180;
        -- LAB900
        DECLARE @lab900 VARCHAR(15)
        SET @lab900 = 'lab900_' + @param1_v;
        DECLARE @lab900_pk VARCHAR(15)
        SET @lab900_pk = 'lab900_PK_' + @param1_v;
        EXEC sp_rename 'lab900', @lab900;
        EXEC sp_rename 'lab900_PK', @lab900_pk;
        -- LAB221
        DECLARE @lab221 VARCHAR(15)
        SET @lab221 = 'lab221_' + @param1_v;
        DECLARE @lab221_pk VARCHAR(15)
        SET @lab221_pk = 'lab221_PK_' + @param1_v;
        EXEC sp_rename 'lab221', @lab221;
        EXEC sp_rename 'lab221_PK', @lab221_pk;
        -- LAB03
        DECLARE @lab03 VARCHAR(15)
        SET @lab03 = 'lab03_' + @param1_v;
        EXEC sp_rename 'lab03', @lab03;
        -- LAB57
        DECLARE @lab57 VARCHAR(15)
        SET @lab57 = 'lab57_' + @param1_v;
        DECLARE @lab57_pk VARCHAR(15)
        SET @lab57_pk = 'lab57_PK_' + @param1_v;
		DECLARE @lab57_const VARCHAR(30)
		SET @lab57_const = 'DF_lab57_lab57c50_' + @param1_v;
        EXEC sp_rename 'lab57', @lab57;
        EXEC sp_rename 'lab57_PK', @lab57_pk;
		EXEC sp_rename 'DF_lab57_lab57c50', @lab57_const;
        -- LAB159
        DECLARE @lab159 VARCHAR(15)
        SET @lab159 = 'lab159_' + @param1_v;
        DECLARE @lab159_pk VARCHAR(15)
        SET @lab159_pk = 'lab159_PK_' + @param1_v;
        EXEC sp_rename 'lab159', @lab159;
        EXEC sp_rename 'lab159_PK', @lab159_pk;
        -- LAB25
        DECLARE @lab25 VARCHAR(15)
        SET @lab25 = 'lab25_' + @param1_v;
        DECLARE @lab25_pk VARCHAR(15)
        SET @lab25_pk = 'lab25_PK_' + @param1_v;
        EXEC sp_rename 'lab25', @lab25;
        EXEC sp_rename 'lab25_PK', @lab25_pk;
        -- LAB144
        DECLARE @lab144 VARCHAR(15)
        SET @lab144 = 'lab144_' + @param1_v;
        DECLARE @lab144_pk VARCHAR(15)
        SET @lab144_pk = 'lab144_PK_' + @param1_v;
        EXEC sp_rename 'lab144', @lab144;
        EXEC sp_rename 'lab144_PK', @lab144_pk;
        -- LAB160
        DECLARE @lab160 VARCHAR(15)
        SET @lab160 = 'lab160_' + @param1_v;
        DECLARE @lab160_pk VARCHAR(15)
        SET @lab160_pk = 'lab160_PK_' + @param1_v;
        EXEC sp_rename 'lab160', @lab160;
        EXEC sp_rename 'lab160_PK', @lab160_pk;
        -- LAB60
        DECLARE @lab60 VARCHAR(15)
        SET @lab60 = 'lab60_' + @param1_v;
        DECLARE @lab60_pk VARCHAR(15)
        SET @lab60_pk = 'lab60_PK_' + @param1_v;
        EXEC sp_rename 'lab60', @lab60;
        EXEC sp_rename 'lab60_PK', @lab60_pk;
        -- LAB95
        DECLARE @lab95 VARCHAR(15)
        SET @lab95 = 'lab95_' + @param1_v;
        DECLARE @lab95_pk VARCHAR(15)
        SET @lab95_pk = 'lab95_PK_' + @param1_v;
        EXEC sp_rename 'lab95', @lab95;
        EXEC sp_rename 'lab95_PK', @lab95_pk;
        -- LAB212
        DECLARE @lab212 VARCHAR(15)
        SET @lab212 = 'lab212_' + @param1_v;
        DECLARE @lab212_pk VARCHAR(15)
        SET @lab212_pk = 'lab212_PK_' + @param1_v;
        EXEC sp_rename 'lab212', @lab212;
        EXEC sp_rename 'lab212_PK', @lab212_pk;
        -- LAB29
        DECLARE @lab29 VARCHAR(15)
        SET @lab29 = 'lab29_' + @param1_v;
        DECLARE @lab29_pk VARCHAR(15)
        SET @lab29_pk = 'lab29_PK_' + @param1_v;
        EXEC sp_rename 'lab29', @lab29;
        EXEC sp_rename 'lab29_PK', @lab29_pk;
        -- LAB191
        DECLARE @lab191 VARCHAR(15)
        SET @lab191 = 'lab191_' + @param1_v;
        EXEC sp_rename 'lab191', @lab191;
        -- LAB179
        DECLARE @lab179 VARCHAR(15)
        SET @lab179 = 'lab179_' + @param1_v;
        EXEC sp_rename 'lab179', @lab179;
        -- LAB23
        DECLARE @lab23 VARCHAR(15)
        SET @lab23 = 'lab23_' + @param1_v;
        EXEC sp_rename 'lab23', @lab23;
        -- LAB58
        DECLARE @lab58 VARCHAR(15)
        SET @lab58 = 'lab58_' + @param1_v;
        EXEC sp_rename 'lab58', @lab58;
        -- LAB75
        DECLARE @lab75 VARCHAR(15)
        SET @lab75 = 'lab75_' + @param1_v;
        DECLARE @lab75_pk VARCHAR(15)
        SET @lab75_pk = 'lab75_PK_' + @param1_v;
        EXEC sp_rename 'lab75', @lab75;
        EXEC sp_rename 'lab75_PK', @lab75_pk;
        -- LAB205
        DECLARE @lab205 VARCHAR(15)
        SET @lab205 = 'lab205_' + @param1_v;
        DECLARE @lab205_pk VARCHAR(15)
        SET @lab205_pk = 'lab205_PK_' + @param1_v;
        EXEC sp_rename 'lab205', @lab205;
        EXEC sp_rename 'lab205_PK', @lab205_pk;
        -- LAB204
        DECLARE @lab204 VARCHAR(15)
        SET @lab204 = 'lab204_' + @param1_v;
        DECLARE @lab204_pk VARCHAR(15)
        SET @lab204_pk = 'lab204_PK_' + @param1_v;
        EXEC sp_rename 'lab204', @lab204;
        EXEC sp_rename 'lab204_PK', @lab204_pk;
END
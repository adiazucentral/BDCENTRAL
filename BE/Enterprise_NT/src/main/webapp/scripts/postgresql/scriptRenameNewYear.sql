-- Script para actualizar el nombre de todas las tablas de operación de NT:

/**
 * Author:  javila
 * Created: 18/06/2021
 */

CREATE OR REPLACE FUNCTION RENAME_NEW_YEAR(param1 regclass)
RETURNS void AS
$$
BEGIN
    -- RENOMBRE DE LAS TABLAS Y DE SU LLAVE PRIMARIA
    -- LAB22
    EXECUTE 'ALTER INDEX lab22c2__IDX RENAME TO lab22c2_idx_' || param1;
    EXECUTE 'ALTER TABLE lab22 RENAME CONSTRAINT "lab22_pk" TO "lab22_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab22 RENAME TO lab22_' || param1;
    -- Clonación de lab22 
    EXECUTE 'CREATE TABLE lab22 AS SELECT * FROM lab22_' || param1 || ' WHERE 1 = 2';
    EXECUTE 'CREATE INDEX lab22c2__IDX ON lab22 (lab22c2 ASC)';
    EXECUTE 'ALTER TABLE lab22 ADD CONSTRAINT lab22_pk PRIMARY KEY ( lab22c1 )';
    -- LAB180
    EXECUTE 'ALTER TABLE lab180 RENAME TO lab180_' || param1;
    -- LAB900
    EXECUTE 'ALTER TABLE lab900 RENAME CONSTRAINT "lab900_pk" TO "lab900_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab900 RENAME TO lab900_' || param1;
    -- LAB221
    EXECUTE 'ALTER TABLE lab221 RENAME CONSTRAINT "lab221_pk" TO "lab221_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab221 RENAME TO lab221_' || param1;
    -- LAB03
    EXECUTE 'ALTER TABLE lab03 RENAME TO lab03_' || param1;
    -- LAB57
    EXECUTE 'ALTER TABLE lab57 RENAME CONSTRAINT "lab57_pk" TO "lab57_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab57 RENAME TO lab57_' || param1;
    -- LAB159
    EXECUTE 'ALTER TABLE lab159 RENAME CONSTRAINT "lab159_pk" TO "lab159_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab159 RENAME TO lab159_' || param1;
    -- LAB25
    EXECUTE 'ALTER TABLE lab25 RENAME CONSTRAINT "lab25_pk" TO "lab25_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab25 RENAME TO lab25_' || param1;
    -- LAB144
    EXECUTE 'ALTER TABLE lab144 RENAME CONSTRAINT "lab144_pk" TO "lab144_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab144 RENAME TO lab144_' || param1;
    -- LAB160
    EXECUTE 'ALTER TABLE lab160 RENAME CONSTRAINT "lab160_pk" TO "lab160_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab160 RENAME TO lab160_' || param1;
    -- LAB60
    EXECUTE 'ALTER TABLE lab60 RENAME CONSTRAINT "lab60_pk" TO "lab60_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab60 RENAME TO lab60_' || param1;
    -- LAB95
    EXECUTE 'ALTER TABLE lab95 RENAME CONSTRAINT "lab95_pk" TO "lab95_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab95 RENAME TO lab95_' || param1;
    -- LAB212
    EXECUTE 'ALTER TABLE lab212 RENAME CONSTRAINT "lab212_pk" TO "lab212_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab212 RENAME TO lab212_' || param1;
    -- LAB29
    EXECUTE 'ALTER TABLE lab29 RENAME CONSTRAINT "lab29_pk" TO "lab29_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab29 RENAME TO lab29_' || param1;
    -- LAB191
    EXECUTE 'ALTER TABLE lab191 RENAME TO lab191_' || param1;
    -- LAB179
    EXECUTE 'ALTER TABLE lab179 RENAME TO lab179_' || param1;
    -- LAB23
    EXECUTE 'ALTER TABLE lab23 RENAME TO lab23_' || param1;
    -- LAB58
    EXECUTE 'ALTER TABLE lab58 RENAME TO lab58_' || param1;
    -- LAB75
    EXECUTE 'ALTER TABLE lab75 RENAME CONSTRAINT "lab75_pk" TO "lab75_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab75 RENAME TO lab75_' || param1;
    -- LAB205
    EXECUTE 'ALTER TABLE lab205 RENAME CONSTRAINT "lab205_pk" TO "lab205_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab205 RENAME TO lab205_' || param1;
    -- LAB204
    EXECUTE 'ALTER TABLE lab204 RENAME CONSTRAINT "lab204_pk" TO "lab204_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE lab204 RENAME TO lab204_' || param1;
END;
$$ LANGUAGE plpgsql;
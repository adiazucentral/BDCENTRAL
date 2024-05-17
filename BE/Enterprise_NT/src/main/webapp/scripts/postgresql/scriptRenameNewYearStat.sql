-- Script para actualizar el nombre de todas las tablas de operación de NT:

/**
 * Author:  javila
 * Created: 22/06/2021
 */

CREATE OR REPLACE FUNCTION RENAME_NEW_YEAR_STA(param1 regclass)
RETURNS void AS
$$
BEGIN
    -- RENOMBRE DE LAS TABLAS Y DE SU LLAVE PRIMARIA
    -- STA2
    EXECUTE 'ALTER INDEX sta2__IDX RENAME TO sta2_IDX_' || param1;
    EXECUTE 'ALTER TABLE sta2 RENAME CONSTRAINT "sta2_pk" TO "sta2_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE sta2 RENAME TO sta2_' || param1;
    -- STA3
    EXECUTE 'ALTER INDEX sta3__IDX RENAME TO sta3_IDX_' || param1;
    EXECUTE 'ALTER TABLE sta3 RENAME CONSTRAINT "sta3_pk" TO "sta3_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE sta3 RENAME TO sta3_' || param1;
    -- STA4
    EXECUTE 'ALTER TABLE sta4 RENAME CONSTRAINT "sta4_pk" TO "sta4_pk_' || param1 || '"';
    EXECUTE 'ALTER TABLE sta4 RENAME TO sta4_' || param1;
END;
$$ LANGUAGE plpgsql;
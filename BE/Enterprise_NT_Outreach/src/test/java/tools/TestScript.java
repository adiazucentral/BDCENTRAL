/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Ejecuta scripts de base de datos para pruebas
 *
 * @version 1.0.0
 * @author dcortes
 * @since 24/04/2017
 * @see Creacion
 */
@Repository
public class TestScript
{

    private static JdbcTemplate jdbc;

    public static void setDataSource(DataSource ds)
    {
        jdbc = new JdbcTemplate(ds);
    }

    /**
     * Ejecuta el script inicial para pruebas
     */
    public static void execTestScript()
    {
        jdbc.execute("drop table lab99; "
                + "drop table lab88;");
    }

    /**
     * Metodo de actualización base de datos
     *
     * @param sql
     * @param args
     */
    public static void execTestUpdateScript(String sql, Object... args)
    {
        if (args == null)
        {
            jdbc.update(sql);
        } else
        {
            jdbc.update(sql, args);
        }
    }

    public static void createInitialOrders()
    {

        TestScript.execTestUpdateScript("DELETE FROM lab05");
        TestScript.execTestUpdateScript("DELETE FROM lab08");
        TestScript.execTestUpdateScript("DELETE FROM lab21");
        TestScript.execTestUpdateScript("DELETE FROM lab22");
        TestScript.execTestUpdateScript("DELETE FROM lab24");
        TestScript.execTestUpdateScript("DELETE FROM lab39");
        TestScript.execTestUpdateScript("DELETE FROM lab43 WHERE lab43c1 != 1");
        TestScript.execTestUpdateScript("DELETE FROM lab54");
        TestScript.execTestUpdateScript("DELETE FROM lab57");
        TestScript.execTestUpdateScript("DELETE FROM lab62");
        TestScript.execTestUpdateScript("DELETE FROM lab63");
        TestScript.execTestUpdateScript("DELETE FROM lab159");
        TestScript.execTestUpdateScript("DELETE FROM lab30");
        TestScript.execTestUpdateScript("DELETE FROM lab29");
        TestScript.execTestUpdateScript("DELETE FROM lab42", null);
        TestScript.execTestUpdateScript("DELETE FROM lab52", null);
        TestScript.execTestUpdateScript("DELETE FROM lab53", null);
        TestScript.execTestUpdateScript("DELETE FROM lab87", null);
        TestScript.execTestUpdateScript("DELETE FROM lab155", null);
        TestScript.execTestUpdateScript("DELETE FROM lab164", null);
        TestScript.execTestUpdateScript("DELETE FROM lab158", null);
        TestScript.execTestUpdateScript("DELETE FROM lab201", null);

        //Motivo de repeticion
        TestScript.execTestUpdateScript("INSERT INTO lab30(lab30c1,lab30c2,lab30c3,lab30c4,lab30c5,lab04c1,lab07c1) VALUES (1,'Confirmacion', 'Confirmacion del examen', 18, '2017-09-13 11:24:27.468', 1, 1)");
        //MUESTRA
        TestScript.execTestUpdateScript("INSERT INTO lab24 VALUES (1, 'SANGRE', 1, 3, 1, 'DESCRIPCION', 10, '2017-09-11 13:48:21.775', 1, 1, 1, '1', '1,2,3', 1)");
        //SEDES
        TestScript.execTestUpdateScript("INSERT INTO lab05 VALUES (1, 'G', 'GENERAL', '', '', NULL, NULL, '2017-09-11 13:48:21.775', '01', '', '', 1, 1)");
        TestScript.execTestUpdateScript("INSERT INTO lab05 VALUES (2, 'S2', 'SEDE 2', '', '', NULL, NULL, '2017-09-11 13:48:21.775', '02', '', '', 1, 1);");
        //RAZA
        TestScript.execTestUpdateScript("INSERT INTO lab08 VALUES (1, 'Blanco', '2017-09-11 13:48:21.775', null, 1, 1, '01');");
        //TIPO DE DOCUMENTO
        TestScript.execTestUpdateScript("INSERT INTO lab54 VALUES (1, 'CC', 'CEDULA DE CIUDADANIA', '2017-09-11 15:54:54.863', 2, 1)");
        TestScript.execTestUpdateScript("INSERT INTO lab54 VALUES (2, 'TI', 'TARJETA DE IDENTIDAD', '2017-09-11 15:55:06.596', 2, 1)");
        //AREAS
        TestScript.execTestUpdateScript("INSERT INTO lab43 (lab43c1, lab43c2, lab43c3, lab43c4, lab43c5, lab43c6, lab43c7, lab04c1, lab07c1, lab43c8) VALUES(2, 0, 'QUI', 'QUIMICA', '#000000', 0, 'now()', 1, 1, 0)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab43 (lab43c1, lab43c2, lab43c3, lab43c4, lab43c5, lab43c6, lab43c7, lab04c1, lab07c1, lab43c8) VALUES(3, 1, 'MICRO', 'MICROBIOLOGIA', '#000000', 0, 'now()', 1, 1, 0)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab43 (lab43c1, lab43c2, lab43c3, lab43c4, lab43c5, lab43c6, lab43c7, lab04c1, lab07c1, lab43c8) VALUES(4, 2, 'QUI', 'QUIMICA', '#000000', 0, 'now()', 1, 1, 0)", null);
        //PACIENTES
        TestScript.execTestUpdateScript("INSERT INTO lab21 (lab21c1,lab21c2,lab21c3,lab21c4,lab21c5,lab21c6,lab80c1,lab21c7,lab21c8,lab21c9,lab21c10,lab21c11,lab21c12,lab04c1,lab08c1,lab54c1) VALUES (1, '10IUEsRP64sIZgspdLsKYA==', 'NSjDTJu014Q=', '+5j1V2yNc8Q=', 'SdbsT2RnFj4=', 'K5/kmGNsFgs=', 7, '1970-04-26 18:37:00.051', 'a@b.c', '10', 50, NULL, '2017-09-12 16:38:58.075', 1, 1, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab21 (lab21c1,lab21c2,lab21c3,lab21c4,lab21c5,lab21c6,lab80c1,lab21c7,lab21c8,lab21c9,lab21c10,lab21c11,lab21c12,lab04c1,lab08c1,lab54c1) VALUES (2, '+Vt+MXn0jzrwl9nphyi0Qg==', 'ZXuuB0ALtx4=', 'FtkyjlkRT9s=', 'cZw3OxJ3H44=', 'X0NgViSQ9XM=', 7, '1970-04-26 18:37:00.051', 'a@b.c', '10', 50, NULL, '2017-09-08 12:23:52.153', 1, 1, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab21 (lab21c1,lab21c2,lab21c3,lab21c4,lab21c5,lab21c6,lab80c1,lab21c7,lab21c8,lab21c9,lab21c10,lab21c11,lab21c12,lab04c1,lab08c1,lab54c1) VALUES (3, '9ZIkjz4nWxG6pPzZcJAkxA==', 'Bde5xksvVDY=', 'DE+KoXPPz5E=', '66wiawuDLfc=', 'DE+KoXPPz5E=', 7, '2017-09-11 08:43:44.879', 'a@b.c', '0', 0, NULL, '2017-09-11 08:46:27.203', 1, 1, 2)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab21 (lab21c1,lab21c2,lab21c3,lab21c5,lab21c12,lab80c1,lab04c1) VALUES (0, 'bygy+9615m4=', 'kCneGglFisE=', 'kCneGglFisE=',now(), 9, 1);", null);
        //EXAMENES
        TestScript.execTestUpdateScript("INSERT INTO lab39(lab39c1, lab43c1, lab39c2, lab39c3, lab39c4   , lab24c1, lab39c6, lab39c7, lab39c8, lab39c9, lab39c11, lab39c24, lab39c25, lab39c26, lab39c27, lab39c28, lab39c29, lab39c30, lab39c31,  lab39c36                , lab04c1, lab07c1, lab39c37, lab39c38, lab39c39, lab39c40, lab39c16,lab39c17,lab39c18,lab39c19,lab39c20,lab39c23) "
                + " VALUES (   1   ,     2   , '102'  , 'LU', 'Leucocitos', 1      , 42     , 0      , 200    , 1      , 2       , 1       , 1       , 1       , 0       , 0       , 0       , 0       , 1       , '2017-09-13 11:24:27.468', 1      , 1      , 0       , 1       , 0       , 0, 1,1,1,1,1,1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39(lab39c1, lab43c1, lab39c2, lab39c3, lab39c4     , lab24c1, lab39c6, lab39c7, lab39c8, lab39c9, lab39c11, lab39c24, lab39c25, lab39c26, lab39c27, lab39c28, lab39c29, lab39c30, lab39c31,  lab39c36                , lab04c1, lab07c1, lab39c37, lab39c38, lab39c39, lab39c40, lab39c16,lab39c17,lab39c18,lab39c19,lab39c20,lab39c23) "
                + " VALUES (2      , 2       , '103'  , 'NI'  , 'Ninfositos', 1      ,42      , 0      , 200    , 1      , 2       ,  1      , 1       , 1       , 0       , 0       , 0       , 0       , 1       ,'2017-09-13 11:24:27.468' , 1      , 1      , 0       , 0       , 0       , 0, 1,1,1,1,1,1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 (lab39c1, lab43c1, lab39c2, lab39c3, lab39c4         , lab24c1, lab39c6, lab39c7, lab39c8, lab39c9, lab39c11, lab39c24, lab39c25, lab39c26, lab39c27, lab39c28, lab39c29, lab39c30, lab39c31,  lab39c36                , lab04c1, lab07c1, lab39c37, lab39c38, lab39c39, lab39c40,lab39c16,lab39c17,lab39c18,lab39c19,lab39c20,lab39c23)"
                + " VALUES  (3      , 2      , '104'  , 'GR'   , 'Globulos rojos', 1      , 42     , 0      , 200    , 1      , 2       , 1       , 1       , 1       , 0       , 0       , 0       , 0       , 1       ,'2017-09-13 11:24:27.468' , 1      , 1      , 0       , 0       , 0       , 0,1,1,1,1,1,1)", null);

        TestScript.execTestUpdateScript("INSERT INTO lab39 (lab39c1, lab43c1, lab39c2, lab39c3, lab39c4   , lab24c1, lab39c6, lab39c7, lab39c8, lab39c9, lab39c11, lab39c24, lab39c25, lab39c26, lab39c27, lab39c28, lab39c29, lab39c30, lab39c31,  lab39c36                , lab04c1, lab07c1, lab39c37, lab39c38, lab39c39, lab39c40,lab39c16,lab39c17,lab39c18,lab39c19,lab39c20,lab39c23)"
                + " VALUES  (9      , 2      , '01'   , 'PR1'  , 'PERFIL 1', NULL   , 42     , 0      , 200    , 1      , NULL    , 1       , 1       , 1       , 0       , 0       , 0       , 0       , 1       ,'2017-09-13 11:24:27.468' , 1      , 1      , 1       , 0       , 0       , 0, 1,1,1,1,1,1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 (lab39c1, lab43c1, lab39c2, lab39c3, lab39c4        , lab24c1, lab39c6, lab39c7, lab39c8, lab39c9, lab39c11, lab39c24, lab39c25, lab39c26, lab39c27, lab39c28, lab39c29, lab39c30, lab39c31,  lab39c36                , lab04c1, lab07c1, lab39c37, lab39c38, lab39c39, lab39c40, lab39c16,lab39c17,lab39c18,lab39c19,lab39c20,lab39c23)"
                + " VALUES  (4      , 2      , '100'  , 'TRI'  , 'TRIGLICERIDOS', 1      , 42     , 0      , 200    , 1      , 2       , 1       , 1       , 1       , 0       , 0       , 0       , 0       , 1       ,'2017-08-31 12:27:23.948' , 1      , 1      , 1       , 0       , 0       , 0, 1,1,1,1,1,1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39 (lab39c1, lab43c1, lab39c2, lab39c3, lab39c4     , lab24c1, lab39c6, lab39c7, lab39c8, lab39c9, lab39c11, lab39c24, lab39c25, lab39c26, lab39c27, lab39c28, lab39c29, lab39c30, lab39c31,  lab39c36                , lab04c1, lab07c1, lab39c37, lab39c38, lab39c39, lab39c40, lab39c16,lab39c17,lab39c18,lab39c19,lab39c20,lab39c23)"
                + " VALUES  (5      , 2      , '101'  , 'COL'  , 'COLESTEROL', 1      , 42     , 0      , 200    , 1      , 2       , 0       , 1       , 1       , 0       , 0       , 0       , 0       , 1       ,'2017-08-31 12:27:23.948' , 1      , 1      , 0       , 0       , 0       , 0, 1,1,1,1,1,1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39(lab39c1, lab43c1, lab39c2, lab39c3, lab39c4   , lab24c1, lab39c6, lab39c7, lab39c8, lab39c9, lab39c11, lab39c24, lab39c25, lab39c26, lab39c27, lab39c28, lab39c29, lab39c30, lab39c31,  lab39c36                , lab04c1, lab07c1, lab39c37, lab39c38, lab39c39, lab39c40,lab39c16,lab39c17,lab39c18,lab39c19,lab39c20,lab39c23) "
                + " VALUES (6      , 2      , '001' , 'GLU'  ,'GLUCOSA'  , 1      , 42     , 0       , 200    , 1      , 2       , 0       , 1       , 1       , 0       , 0       , 0       , 0       , 1       ,'2017-08-31 12:27:23.948' , 1      , 1      , 0       , 0       , 0       , 0, 1,1,1,1,1,1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39(lab39c1, lab43c1, lab39c2, lab39c3, lab39c4   , lab24c1, lab39c6, lab39c7, lab39c8, lab39c9, lab39c11, lab39c24, lab39c25, lab39c26, lab39c27, lab39c28, lab39c29, lab39c30, lab39c31,  lab39c36                , lab04c1, lab07c1, lab39c37, lab39c38, lab39c39, lab39c40, lab39c16,lab39c17,lab39c18,lab39c19,lab39c20,lab39c23)"
                + " VALUES (7      , 2      , '002'  , 'CEL'  , 'CELULAS' , 1      , 42     , 0      , 200    , 1      , 2       , 0       , 1       , 1       , 0       , 0       , 0       , 0       , 1       ,'2017-08-31 12:27:38.091' , 1      , 1      , 0       , 0       , 0       , 0, 1,1,1,1,1,1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39(lab39c1, lab43c1, lab39c2, lab39c3, lab39c4      , lab24c1, lab39c6, lab39c7, lab39c8, lab39c9, lab39c11, lab39c24, lab39c25, lab39c26, lab39c27, lab39c28, lab39c29, lab39c30, lab39c31,  lab39c36                , lab04c1, lab07c1, lab39c37, lab39c38, lab39c39, lab39c40, lab39c16,lab39c17,lab39c18,lab39c19,lab39c20,lab39c23)"
                + " VALUES (8      , 3      , '003'  , 'CP'   , 'CONFIDENCIAL', 1      , 7     , 0      , 200    , 1      , 2       , 1       , 1       , 1       , 1       , 0       , 0       , 0       , 1       ,'2017-09-13 11:23:52.872' , 1      , 1      , 0       , 0       , 0       , 0, 1,1,1,1,1,1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab39(lab39c1, lab43c1, lab39c2, lab39c3, lab39c4    , lab24c1, lab39c6, lab39c7, lab39c8, lab39c9, lab39c11, lab39c24, lab39c25, lab39c26, lab39c27, lab39c28, lab39c29, lab39c30, lab39c31,  lab39c36                , lab04c1, lab07c1, lab39c37, lab39c38, lab39c39, lab39c40,lab39c16,lab39c17,lab39c18,lab39c19,lab39c20,lab39c23)"
                + " VALUES (10     , 0      , '02'   , 'PQ1'  , 'PAQUETE 1', NULL   , 42     , 0      , NULL   , NULL   , NULL    , 1       , 1       , 1       , 0       , 0       , 0       , 0       , 1       ,'2017-09-13 11:30:20.47'  , 1      , 1      , 2       , 0       , 0       , 0, 1,1,1,1,1,1)", null);
        //ORDENES
        TestScript.execTestUpdateScript("INSERT INTO lab22(lab22c1,lab22c2,lab103c1,lab22c3,lab21c1,lab22c4,lab22c5,lab22c6,lab04c1,lab07c1,lab22c7,lab05c1,lab10c1,lab19c1,lab14c1,lab904c1,lab22c8,lab22c9,lab22c10) VALUES (201709080001, 20170908, 1, '2017-09-08 11:49:15.944', 1, 1, 100, '2017-09-08 12:23:17.979', 1, 1, '1', 1, 1, 1, 1, 1, 1, null, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab22(lab22c1,lab22c2,lab103c1,lab22c3,lab21c1,lab22c4,lab22c5,lab22c6,lab04c1,lab07c1,lab22c7,lab05c1,lab10c1,lab19c1,lab14c1,lab904c1,lab22c8,lab22c9,lab22c10)VALUES (201709080002, 20170908, 2, '2017-09-08 11:49:15.944', 3, 1, 100, '2017-09-08 12:25:09.449', 1, 1, '1', 2, 1, 1, 1, 1, 1, null, 1)", null);

        TestScript.execTestUpdateScript("INSERT INTO lab22(lab22c1,lab22c2,lab103c1,lab22c3,lab21c1,lab22c4,lab22c5,lab22c6,lab04c1,lab07c1,lab22c7,lab05c1,lab10c1,lab19c1,lab14c1,lab904c1,lab22c8,lab22c9,lab22c10) VALUES (201710040001, 20171004, 1, '2017-10-04 11:49:15.944', 2, 1, 100, '2017-10-04 12:23:17.979', 1, 1, '1', 1, 1, 1, 1, 1, 1, null, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab22(lab22c1,lab22c2,lab103c1,lab22c3,lab21c1,lab22c4,lab22c5,lab22c6,lab04c1,lab07c1,lab22c7,lab05c1,lab10c1,lab19c1,lab14c1,lab904c1,lab22c8,lab22c9,lab22c10) VALUES (201710040002, 20171004, 1, '2017-10-04 11:49:15.944', 1, 1, 100, '2017-10-04 12:25:09.449', 1, 1, '1', 2, 1, 1, 1, 1, 1, null, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab22(lab22c1,lab22c2,lab103c1,lab22c3,lab21c1,lab22c4,lab22c5,lab22c6,lab04c1,lab07c1,lab22c7,lab05c1,lab10c1,lab19c1,lab14c1,lab904c1,lab22c8,lab22c9,lab22c10) VALUES (201710040003, 20171004, 1, '2017-10-04 11:49:15.944', 1, 1, 100, '2017-10-04 12:25:09.449', 1, 1, '1', 2, 1, 1, 1, 1, 1, null, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab22(lab22c1,lab22c2,lab103c1,lab22c3,lab21c1,lab22c4,lab22c5,lab22c6,lab04c1,lab07c1,lab22c7,lab05c1,lab10c1,lab19c1,lab14c1,lab904c1,lab22c8,lab22c9,lab22c10) VALUES (201710040004, 20171004, 2, '2017-10-04 11:49:15.944', 2, 1, 100, '2017-10-04 12:25:09.449', 1, 1, '1', 2, 1, 1, 1, 1, 1, null, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab22(lab22c1,lab22c2,lab103c1,lab22c3,lab21c1,lab22c4,lab22c5,lab22c6,lab04c1,lab07c1,lab22c7,lab05c1,lab10c1,lab19c1,lab14c1,lab904c1,lab22c8,lab22c9,lab22c10) VALUES (201710040005, 20171004, 2, '2017-10-04 11:49:15.944', 1, 1, 100, '2017-10-04 12:25:09.449', 1, 1, '1', 2, 1, 1, 1, 1, 1, null, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab22(lab22c1,lab22c2,lab103c1,lab22c3,lab21c1,lab22c4,lab22c5,lab22c6,lab04c1,lab07c1,lab22c7,lab05c1,lab10c1,lab19c1,lab14c1,lab904c1,lab22c8,lab22c9,lab22c10) VALUES (201710040006, 20171004, 3, '2017-10-04 11:49:15.944', 3, 1, 100, '2017-10-04 12:25:09.449', 1, 1, '1', 2, 1, 1, 1, 1, 1, null, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab22(lab22c1,lab22c2,lab103c1,lab22c3,lab21c1,lab22c4,lab22c5,lab22c6,lab04c1,lab07c1,lab22c7,lab05c1,lab10c1,lab19c1,lab14c1,lab904c1,lab22c8,lab22c9,lab22c10) VALUES (201710040007, 20171004, 3, '2017-10-04 11:49:15.944', 3, 1, 100, '2017-10-04 12:25:09.449', 1, 1, '1', 2, 1, 1, 1, 1, 1, null, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab22(lab22c1,lab22c2,lab103c1,lab22c3,lab21c1,lab22c4,lab22c5,lab22c6,lab04c1,lab07c1,lab22c7,lab05c1,lab10c1,lab19c1,lab14c1,lab904c1,lab22c8,lab22c9,lab22c10) VALUES (201710040008, 20171004, 2, '2017-10-04 11:49:15.944', 2, 1, 100, '2017-10-04 12:25:09.449', 1, 1, '1', 2, 1, 1, 1, 1, 1, null, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab22(lab22c1,lab22c2,lab103c1,lab22c3,lab21c1,lab22c4,lab22c5,lab22c6,lab04c1,lab07c1,lab22c7,lab05c1,lab10c1,lab19c1,lab14c1,lab904c1,lab22c8,lab22c9,lab22c10) VALUES (201710040009, 20171004, 5, '2017-10-04 11:49:15.944', 1, 1, 100, '2017-10-04 12:25:09.449', 1, 1, '1', 2, 1, 1, 1, 1, 1, null, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab22(lab22c1,lab22c2,lab103c1,lab22c3,lab21c1,lab22c4,lab22c5,lab22c6,lab04c1,lab07c1,lab22c7,lab05c1,lab10c1,lab19c1,lab14c1,lab904c1,lab22c8,lab22c9,lab22c10) VALUES (201710040010, 20171004, 3, '2017-10-04 11:49:15.944', 2, 1, 100, '2017-10-04 12:25:09.449', 1, 1, '1', 2, 1, 1, 1, 1, 1, null, 1)", null);

        //Estado de la muestra
        TestScript.execTestUpdateScript("INSERT INTO lab159(lab22c1,lab24c1,lab159c1,lab04c1,lab159c2,lab05c1) VALUES (201709080001, 1, 2, 1, '2017-09-08 12:25:09.449', 1)", null);

        //RESULTADOS
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201709080001, 8, '2017-09-13 11:33:13.552855', 1, 0, 4, NULL, NULL, 1, 20170908, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201709080001, 6, '2017-09-13 11:34:10.914229', 1, 3, 4, 9, 10, 1, 20170908, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201709080001, 9, '2017-09-13 11:34:43.352058', 1, 3, 4, NULL, 10, 1, 20170908, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201709080001, 7, '2017-09-13 11:34:12.439341', 1, 3, 4, 9, 10, 1, 20170908, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201709080001, 10,'2017-09-13 11:42:29.666415', 1, 0, NULL, NULL, NULL, 1, 20170908, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201709080002, 8, '2017-09-13 11:44:18.923104', 1, 0, 2, NULL, NULL, 1, 20170908, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201709080002, 6, '2017-09-13 11:44:20.116507', 1, 3, 4, 9, NULL, 1, 20170908, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201709080002, 7, '2017-09-13 11:44:21.712427', 1, 3, 4, 9, NULL, 1, 20170908, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201709080002, 9, '2017-09-13 11:52:11.687382', 1, 0, 4, NULL, NULL, 1, 20170908, 1)", null);

        TestScript.execTestUpdateScript("INSERT INTO lab57(lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040001, 10, '2017-09-13 11:42:29.666415', 1, 1, 1, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57(lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040001, 9,  '2017-09-13 11:34:43.352058', 1, 3, 4, NULL, 10, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57(lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040001, 4,  '2017-09-13 11:33:13.552855', 1, 3, 4, 9, 10, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57(lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040001, 5,  '2017-09-13 11:34:10.914229', 1, 3, 4, 9, 10, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57(lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040001, 6,  '2017-09-13 11:34:12.439341', 1, 1, 1, 9, 10, 1, 20171004, 1)", null);

        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040002, 9, '2017-09-13 11:52:11.687382', 1, 3, 2, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040002, 4, '2017-09-13 11:44:21.712427', 1, 1, 1, 9, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040002, 5, '2017-09-13 11:44:18.923104', 1, 1, 1, 9, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040002, 6, '2017-09-13 11:44:20.116507', 1, 1, 1, 9, NULL, 1, 20171004, 1)", null);

        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040003, 9, '2017-09-13 11:34:43.352058', 1, 3, 4, NULL, 10, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040003, 4, '2017-09-13 11:44:21.712427', 1, 1, 2, 9, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040003, 5, '2017-09-13 11:44:18.923104', 1, 1, 2, 9, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040003, 6, '2017-09-13 11:44:20.116507', 1, 1, 2, 9, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040003, 8, '2017-09-13 11:34:12.439341', 1, 1, 2, 9, NULL, 1, 20171004, 1)", null);

        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040004, 8, '2017-09-13 11:34:12.439341', 1, 1, 2, NULL, NULL, 1, 20171004, 1)", null);

        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040005, 8, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040005, 4, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040005, 3, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040006, 3, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040006, 2, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040006, 1, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040006, 4, '2017-09-13 11:34:12.439341', 1, 1, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040007, 3, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040007, 2, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040007, 1, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040008, 4, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040008, 5, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040009, 9, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040009, 4, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040009, 2, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);
        TestScript.execTestUpdateScript("INSERT INTO lab57 (lab22c1, lab39c1, lab57c4,lab57c5, lab57c8, lab57c16, lab57c14, lab57c15,lab40c1, lab57c34, lab24c1) VALUES (201710040010, 6, '2017-09-13 11:34:12.439341', 1, 0, 3, NULL, NULL, 1, 20171004, 1)", null);

        //Repeticiones
        TestScript.execTestUpdateScript("INSERT INTO lab29(lab29c1,lab22c1,lab39c1,lab29c2,lab29c3,lab29c4,lab04c1_1,lab29c5,lab30c1,lab29c7,lab29c8,lab04c1_2,lab29c9) "
                + "VALUES (1,201709080001, 7, '25', '2017-09-13 11:24:27.468',0, 1, 'R',1,'Coment','2018-03-22 11:24:27.468',1,0) ");
        TestScript.execTestUpdateScript("INSERT INTO lab29(lab29c1,lab22c1,lab39c1,lab29c2,lab29c3,lab29c4,lab04c1_1,lab29c5,lab30c1,lab29c7,lab29c8,lab04c1_2,lab29c9) "
                + "VALUES (2,201709080001, 7, '30', '2017-09-13 11:24:27.468',0, 1, 'R',1,'Coment','2018-03-22 11:24:27.468',1,0) ");
        //Estadisticas rapidas
        TestScript.execTestUpdateScript("DROP TABLE IF EXISTS sta52017;");
        TestScript.execTestUpdateScript(""
                + "CREATE TABLE sta52017 ("
                + "    sta5c1    INTEGER NOT NULL,"
                + "    sta5c2    INTEGER NOT NULL,"
                + "    sta5c3    INTEGER NOT NULL,"
                + "    sta5c4    VARCHAR(16) NOT NULL,"
                + "    sta5c5    VARCHAR(64) NOT NULL,"
                + "    sta5c6    VARCHAR(16) NOT NULL,"
                + "    sta5c7    VARCHAR(64) NOT NULL,"
                + "    sta5c8    INTEGER NOT NULL,"
                + "    sta5c9    INTEGER NOT NULL,"
                + "    sta5c10   INTEGER NOT NULL,"
                + "    sta5c11   INTEGER NOT NULL"
                + ");"
                + "ALTER TABLE sta52017 ADD CONSTRAINT sta52017_pk PRIMARY KEY ( sta5c1,sta5c2,sta5c3 );");
    }
}

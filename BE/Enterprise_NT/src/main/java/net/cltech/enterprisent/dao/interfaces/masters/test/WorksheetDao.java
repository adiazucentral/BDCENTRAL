/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.test.Worksheet;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * Hojas de Trabajo.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 31/07/2017
 * @see Creación
 */
public interface WorksheetDao
{

    /**
     * Lista las hojas de trabajo desde la base de datos.
     *
     * @return Lista de hojas de trabajo.
     * @throws Exception Error en la base de datos.
     */
    default List<Worksheet> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab37c1, lab37c2, lab37c3, lab37c4, lab37c5, lab37c6, lab37c7, lab37.lab04c1, lab04c2, lab04c3, lab04c4, lab37.lab07c1 "
                    + "FROM lab37 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab37.lab04c1", (ResultSet rs, int i) ->
            {
                Worksheet worksheet = new Worksheet();
                worksheet.setId(rs.getInt("lab37c1"));
                worksheet.setName(rs.getString("lab37c2"));
                worksheet.setType(rs.getString("lab37c3") == null ? null : rs.getShort("lab37c3"));
                worksheet.setOrientation(rs.getString("lab37c4") == null ? null : rs.getShort("lab37c4"));
                worksheet.setExclusive(rs.getInt("lab37c5") == 1);
                worksheet.setMicrobiology(rs.getInt("lab37c6") == 1);
                /*Usuario*/
                worksheet.getUser().setId(rs.getInt("lab04c1"));
                worksheet.getUser().setName(rs.getString("lab04c2"));
                worksheet.getUser().setLastName(rs.getString("lab04c3"));
                worksheet.getUser().setUserName(rs.getString("lab04c4"));

                worksheet.setLastTransaction(rs.getTimestamp("lab37c7"));
                worksheet.setState(rs.getInt("lab07c1") == 1);

                return worksheet;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra una nueva Hoja de Trabajo en la base de datos.
     *
     * @param worksheet Instancia con los datos de la Hoja de Trabajo.
     *
     * @return Instancia con las datos de la Hoja de Trabajo.
     * @throws Exception Error en la base de datos.
     */
    default Worksheet create(Worksheet worksheet) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab37")
                .usingGeneratedKeyColumns("lab37c1");

        HashMap parameters = new HashMap();
        parameters.put("lab37c2", worksheet.getName().trim());
        parameters.put("lab37c3", worksheet.getType());
        parameters.put("lab37c4", worksheet.getOrientation());
        parameters.put("lab37c5", worksheet.isExclusive() ? 1 : 0);
        parameters.put("lab37c6", worksheet.isMicrobiology() ? 1 : 0);
        parameters.put("lab37c7", timestamp);
        parameters.put("lab04c1", worksheet.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        worksheet.setId(key.intValue());
        worksheet.setLastTransaction(timestamp);

        insertTests(worksheet);

        return worksheet;
    }

    /**
     * Obtener información de una Hoja de Trabajo por un campo especifico.
     *
     * @param id ID de la Hoja de Trabajo a ser consultada.
     * @param name Nombre de la Hoja de Trabajo a ser consultada.
     *
     * @return Instancia con las datos de la Hoja de Trabajo.
     * @throws Exception Error en la base de datos.
     */
    default Worksheet get(Integer id, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab37c1, lab37c2, lab37c3, lab37c4, lab37c5, lab37c6, lab37c7, lab37.lab04c1, lab04c2, lab04c3, lab04c4, lab37.lab07c1 "
                    + "FROM lab37 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab37.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab37c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab37c2) = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (name != null)
            {
                object = name.toUpperCase();
            }

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Worksheet worksheet = new Worksheet();
                worksheet.setId(rs.getInt("lab37c1"));
                worksheet.setName(rs.getString("lab37c2"));
                worksheet.setType(rs.getString("lab37c3") == null ? null : rs.getShort("lab37c3"));
                worksheet.setOrientation(rs.getString("lab37c4") == null ? null : rs.getShort("lab37c4"));
                worksheet.setExclusive(rs.getInt("lab37c5") == 1);
                worksheet.setMicrobiology(rs.getInt("lab37c6") == 1);
                /*Usuario*/
                worksheet.getUser().setId(rs.getInt("lab04c1"));
                worksheet.getUser().setName(rs.getString("lab04c2"));
                worksheet.getUser().setLastName(rs.getString("lab04c3"));
                worksheet.getUser().setUserName(rs.getString("lab04c4"));

                worksheet.setLastTransaction(rs.getTimestamp("lab37c7"));
                worksheet.setState(rs.getInt("lab07c1") == 1);

                readTests(worksheet);

                return worksheet;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de una Hoja de Trabajo en la base de datos.
     *
     * @param worksheet Instancia con las datos de la Hoja de Trabajo.
     *
     * @return Objeto de la Hoja de Trabajo modificada.
     * @throws Exception Error en la base de datos.
     */
    default Worksheet update(Worksheet worksheet) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab37 SET lab37c2 = ?, lab37c3 = ?, lab37c4 = ?, lab37c5 = ?, lab37c6 = ?, lab37c7 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab37c1 = ?",
                worksheet.getName(), worksheet.getType(), worksheet.getOrientation(), worksheet.isExclusive() ? 1 : 0, worksheet.isMicrobiology() ? 1 : 0, timestamp, worksheet.getUser().getId(), worksheet.isState() ? 1 : 0, worksheet.getId());

        worksheet.setLastTransaction(timestamp);

        insertTests(worksheet);

        return worksheet;
    }

    /**
     *
     * Elimina una hoja de trabajo de la base de datos.
     *
     * @param id ID de la hoja de trabajo.
     *
     * @throws Exception Error en base de datos.
     */
    default void delete(Integer id) throws Exception
    {

    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Asociar examenes a una hoja de trabajo.
     *
     * @param worksheet Instancia con los datos de la hoja de trabajo.
     * @throws java.lang.Exception
     */
    default void insertTests(Worksheet worksheet) throws Exception
    {
        deleteTests(worksheet.getId());
        for (TestBasic test : worksheet.getTests())
        {
            if (test.isSelected())
            {
                SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                        .withTableName("lab38")
                        .usingColumns("lab37c1", "lab39c1");

                HashMap parameters = new HashMap();
                parameters.put("lab37c1", worksheet.getId());
                parameters.put("lab39c1", test.getId());

                insert.execute(parameters);
            }
        }

    }

    /**
     * Obtener todos los examenes y seleccionar los asociados a una hoja de
     * trabajo.
     *
     * @param worksheet Instancia con los datos de la hoja de trabajo.
     */
    default void readTests(Worksheet worksheet)
    {
        try
        {
            worksheet.setTests(getJdbcTemplate().query(""
                    + "SELECT lab39.lab39c1, lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c5, lab38.lab37c1 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab38 ON lab38.lab39c1 = lab39.lab39c1 "
                    + "AND lab38.lab37c1 = ?"
                    + "",
                    new Object[]
                    {
                        worksheet.getId()
                    }, (ResultSet rs, int i) ->
            {
                TestBasic testBasic = new TestBasic();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));
                testBasic.setSelected(rs.getString("lab37c1") != null);
                return testBasic;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            worksheet.setTests(new ArrayList<>());
        }
    }

    default void deleteTests(Integer idWorksheet) throws Exception
    {
        String query = ""
                + " DELETE FROM lab38 WHERE lab37c1 = " + idWorksheet;
        getJdbcTemplate().execute(query);
    }

    /**
     * Obtener los examenes asociados a una hoja de trabajo.
     *
     * @param id Id de la hoja de trabajo
     * @return
     * @throws java.lang.Exception
     */
    default List<TestBasic> testsByWorkSheets(int id) throws Exception
    {
        return getJdbcTemplate().query(""
                + "SELECT lab39.lab39c1, lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c5, lab38.lab37c1 "
                + "FROM lab38 "
                + "INNER JOIN lab39 ON lab38.lab39c1 = lab39.lab39c1 "
                + "AND lab38.lab37c1 = ?",
                new Object[]
                {
                    id
                }, (ResultSet rs, int i) ->
        {
            TestBasic testBasic = new TestBasic();
            testBasic.setId(rs.getInt("lab39c1"));
            testBasic.setCode(rs.getString("lab39c2"));
            testBasic.setAbbr(rs.getString("lab39c3"));
            testBasic.setName(rs.getString("lab39c4"));
            testBasic.setSelected(true);
            return testBasic;
        });
    }
}

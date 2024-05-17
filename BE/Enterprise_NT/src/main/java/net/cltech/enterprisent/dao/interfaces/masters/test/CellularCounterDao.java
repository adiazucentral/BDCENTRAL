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
import net.cltech.enterprisent.domain.masters.test.CellularCounter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * Contador Hematologico.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 01/08/2017
 * @see Creación
 */
public interface CellularCounterDao
{

    /**
     * Lista los contadores hematologicos desde la base de datos.
     *
     * @return Lista de contadores hematologicos.
     * @throws Exception Error en la base de datos.
     */
    default List<CellularCounter> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab18c1, lab18c2, lab18c3, lab18c4, lab18c5, lab18c6, lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab18.lab04c1, lab04c2, lab04c3, lab04c4, lab18.lab07c1 "
                    + "FROM lab18 "
                    + "LEFT JOIN lab39 ON lab39.lab39c1 = lab18.lab39c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab18.lab04c1", (ResultSet rs, int i) ->
            {
                CellularCounter cellularCounter = new CellularCounter();
                cellularCounter.setId(rs.getInt("lab18c1"));
                cellularCounter.setKey(rs.getString("lab18c2"));
                cellularCounter.setText(rs.getString("lab18c3"));
                cellularCounter.setType(rs.getShort("lab18c4"));
                cellularCounter.setSum(rs.getInt("lab18c5") == 1);
                /*Usuario*/
                cellularCounter.getUser().setId(rs.getInt("lab04c1"));
                cellularCounter.getUser().setName(rs.getString("lab04c2"));
                cellularCounter.getUser().setLastName(rs.getString("lab04c3"));
                cellularCounter.getUser().setUserName(rs.getString("lab04c4"));

                /*Prueba*/
                cellularCounter.getTest().setId(rs.getInt("lab39c1"));
                cellularCounter.getTest().setCode(rs.getString("lab39c2"));
                cellularCounter.getTest().setAbbr(rs.getString("lab39c3"));
                cellularCounter.getTest().setName(rs.getString("lab39c4"));

                cellularCounter.setLastTransaction(rs.getTimestamp("lab18c6"));
                cellularCounter.setState(rs.getInt("lab07c1") == 1);

                return cellularCounter;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra un nuevo Contador Hematologico en la base de datos.
     *
     * @param cellularCounter Instancia con los datos del Contador Hematologico.
     *
     * @return Instancia con las datos de la Contador Hematologico.
     * @throws Exception Error en la base de datos.
     */
    default CellularCounter create(CellularCounter cellularCounter) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab18")
                .usingGeneratedKeyColumns("lab18c1");

        HashMap parameters = new HashMap();
        parameters.put("lab18c2", cellularCounter.getKey().trim());
        parameters.put("lab18c3", cellularCounter.getText().trim());
        parameters.put("lab18c4", cellularCounter.getType());
        parameters.put("lab18c5", cellularCounter.isSum() ? 1 : 0);
        parameters.put("lab18c6", timestamp);
        parameters.put("lab39c1", cellularCounter.getTest().getId());
        parameters.put("lab04c1", cellularCounter.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        cellularCounter.setId(key.intValue());
        cellularCounter.setLastTransaction(timestamp);

        return cellularCounter;
    }

    /**
     * Obtener información de un Contador Hematologico por un campo especifico.
     *
     * @param id ID del Contador Hematologico a ser consultado.
     * @param key Tecla del Contador Hematologico a ser consultado.
     *
     * @return Instancia con las datos de la Contador Hematologico.
     * @throws Exception Error en la base de datos.
     */
    default CellularCounter get(Integer id, String key) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab18c1, lab18c2, lab18c3, lab18c4, lab18c5, lab18c6, lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab18.lab04c1, lab04c2, lab04c3, lab04c4, lab18.lab07c1 "
                    + "FROM lab18 "
                    + "LEFT JOIN lab39 ON lab39.lab39c1 = lab18.lab39c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab18.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab18c1 = ? ";
            }
            if (key != null)
            {
                query = query + "WHERE UPPER(lab18c2) = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (key != null)
            {
                object = key.toUpperCase();
            }

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                CellularCounter cellularCounter = new CellularCounter();
                cellularCounter.setId(rs.getInt("lab18c1"));
                cellularCounter.setKey(rs.getString("lab18c2"));
                cellularCounter.setText(rs.getString("lab18c3"));
                cellularCounter.setType(rs.getShort("lab18c4"));
                cellularCounter.setSum(rs.getInt("lab18c5") == 1);
                /*Usuario*/
                cellularCounter.getUser().setId(rs.getInt("lab04c1"));
                cellularCounter.getUser().setName(rs.getString("lab04c2"));
                cellularCounter.getUser().setLastName(rs.getString("lab04c3"));
                cellularCounter.getUser().setUserName(rs.getString("lab04c4"));

                /*Prueba*/
                cellularCounter.getTest().setId(rs.getInt("lab39c1"));
                cellularCounter.getTest().setCode(rs.getString("lab39c2"));
                cellularCounter.getTest().setAbbr(rs.getString("lab39c3"));
                cellularCounter.getTest().setName(rs.getString("lab39c4"));

                cellularCounter.setLastTransaction(rs.getTimestamp("lab18c6"));
                cellularCounter.setState(rs.getInt("lab07c1") == 1);

                return cellularCounter;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtener información de un Contador Hematologico por un campo especifico.
     *
     * @param id ID del examen a ser consultado.
     *
     * @return Instancia con las datos de la Contador Hematologico.
     * @throws Exception Error en la base de datos.
     */
    default List<CellularCounter> getTest(Integer id) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab18c1, lab18c2, lab18c3, lab18c4, lab18c5, lab18c6, lab04c1 "
                    + "FROM lab18 "
                    + "WHERE lab39c1 = ? AND lab07c1 = 1", (ResultSet rs, int i) ->
            {
                CellularCounter cellularCounter = new CellularCounter();
                cellularCounter.setId(rs.getInt("lab18c1"));
                cellularCounter.setKey(rs.getString("lab18c2"));
                cellularCounter.setText(rs.getString("lab18c3"));
                cellularCounter.setType(rs.getShort("lab18c4"));
                cellularCounter.setSum(rs.getInt("lab18c5") == 1);
                cellularCounter.setLastTransaction(rs.getTimestamp("lab18c6"));
                cellularCounter.getUser().setId(rs.getInt("lab04c1"));

                return cellularCounter;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Actualiza la información de un Contador Hematologico en la base de datos.
     *
     * @param cellularCounter Instancia con los datos del Contador Hematologico.
     *
     * @return Objeto del Contador Hematologico modificada.
     * @throws Exception Error en la base de datos.
     */
    default CellularCounter update(CellularCounter cellularCounter) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab18 SET lab18c2 = ?, lab18c3 = ?, lab18c4 = ?, lab18c5 = ?, lab18c6 = ?, lab39c1 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab18c1 = ?",
                cellularCounter.getKey().trim(), cellularCounter.getText().trim(), cellularCounter.getType(), cellularCounter.isSum() ? 1 : 0, timestamp, cellularCounter.getTest().getId(), cellularCounter.getUser().getId(), cellularCounter.isState() ? 1 : 0, cellularCounter.getId());

        cellularCounter.setLastTransaction(timestamp);

        return cellularCounter;
    }

    /**
     *
     * Elimina un contador hematologico de la base de datos.
     *
     * @param id ID de la contador hematologico.
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.common;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.Holiday;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * festivos.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 30/08/2017
 * @see Creación
 */
public interface HolidayDao
{

    /**
     * Lista los festivos desde la base de datos.
     *
     * @return Lista de festivos.
     * @throws Exception Error en la base de datos.
     */
    default List<Holiday> list() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab81c1, lab81c2, lab81c3, lab81c4, lab81.lab04c1, lab04c2, lab04c3, lab04c4, lab81.lab07c1 "
                    + "FROM lab81 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab81.lab04c1", (ResultSet rs, int i) ->
            {
                Holiday holiday = new Holiday();
                holiday.setId(rs.getInt("lab81c1"));
                holiday.setName(rs.getString("lab81c2"));
                holiday.setDate(rs.getTimestamp("lab81c3"));
                /*Usuario*/
                holiday.getUser().setId(rs.getInt("lab04c1"));
                holiday.getUser().setName(rs.getString("lab04c2"));
                holiday.getUser().setLastName(rs.getString("lab04c3"));
                holiday.getUser().setUserName(rs.getString("lab04c4"));

                holiday.setLastTransaction(rs.getTimestamp("lab81c4"));
                holiday.setState(rs.getInt("lab07c1") == 1);

                return holiday;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    
    /**
     * Lista los festivos desde la base de datos.
     *
     * @return Lista de festivos.
     * @throws Exception Error en la base de datos.
     */
    default List<String> listBasic() throws Exception
    {
        try
        {
            
            return getConnection().query(""
                    + "SELECT lab81c3 "
                    + "FROM lab81 "
                    + "WHERE lab07c1 = 1 ", (ResultSet rs, int i) ->
            {
                
                return rs.getString("lab81c3");
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra uno nuevo festivo en la base de datos.
     *
     * @param holiday Instancia con los datos del festivo.
     *
     * @return Instancia con los datos del festivo.
     * @throws Exception Error en la base de datos.
     */
    default Holiday create(Holiday holiday) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab81")
                .usingGeneratedKeyColumns("lab81c1");

        HashMap parameters = new HashMap();
        parameters.put("lab81c2", holiday.getName().trim());
        parameters.put("lab81c3", holiday.getDate());
        parameters.put("lab81c4", timestamp);
        parameters.put("lab04c1", holiday.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        holiday.setId(key.intValue());
        holiday.setLastTransaction(timestamp);

        return holiday;
    }

    /**
     * Obtener información de un festivo por una campo especifico.
     *
     * @param id ID del festivo a ser consultado.
     * @param name Nombre del festivo a ser consultado.
     *
     * @return Instancia con los datos del festivo.
     * @throws Exception Error en la base de datos.
     */
    default Holiday get(Integer id, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab81c1, lab81c2, lab81c3, lab81c4, lab81.lab04c1, lab04c2, lab04c3, lab04c4, lab81.lab07c1 "
                    + "FROM lab81 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab81.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab81c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab81c2) = ? ";
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

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Holiday holiday = new Holiday();
                holiday.setId(rs.getInt("lab81c1"));
                holiday.setName(rs.getString("lab81c2"));
                holiday.setDate(rs.getTimestamp("lab81c3"));
                /*Usuario*/
                holiday.getUser().setId(rs.getInt("lab04c1"));
                holiday.getUser().setName(rs.getString("lab04c2"));
                holiday.getUser().setLastName(rs.getString("lab04c3"));
                holiday.getUser().setUserName(rs.getString("lab04c4"));

                holiday.setLastTransaction(rs.getTimestamp("lab81c4"));
                holiday.setState(rs.getInt("lab07c1") == 1);

                return holiday;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de una festivo en la base de datos.
     *
     * @param holiday Instancia con los datos de la festivo.
     *
     * @return Objeto de la festivo modificado.
     * @throws Exception Error en la base de datos.
     */
    default Holiday update(Holiday holiday) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getConnection().update("UPDATE lab81 SET lab81c2 = ?, lab81c3 = ?, lab81c4 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab81c1 = ?",
                holiday.getName().trim(), holiday.getDate(), timestamp, holiday.getUser().getId(), holiday.isState() ? 1 : 0, holiday.getId());

        holiday.setLastTransaction(timestamp);

        return holiday;
    }

    /**
     *
     * Elimina un festivo de la base de datos.
     *
     * @param id ID del festivo.
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
    public JdbcTemplate getConnection();
}

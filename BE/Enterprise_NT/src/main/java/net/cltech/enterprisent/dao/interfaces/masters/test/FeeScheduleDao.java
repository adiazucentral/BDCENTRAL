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
import net.cltech.enterprisent.domain.masters.test.FeeSchedule;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * Vigencias.
 *
 * @version 1.0.0
 * @author enavas
 * @since 09/08/2017
 * @see Creación
 */
public interface FeeScheduleDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista las vigencias desde la base de datos.
     *
     * @return Lista de vigencias.
     * @throws Exception Error en la base de datos.
     */
    default List<FeeSchedule> list() throws Exception
    {
        try
        {
            String query = ""
                    + "SELECT lab116.lab116c1 "
                    + ", lab116.lab116c2 "
                    + ", lab116.lab116c3 "
                    + ", lab116.lab116c4 "
                    + ", lab116.lab116c5 "
                    + ", lab116.lab07c1 "
                    + ", lab116.lab116c7 "
                    + ", lab116.lab04c1 "
                    + "FROM lab116 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab116.lab04c1";

            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                FeeSchedule feeSchedule = new FeeSchedule();

                feeSchedule.setId(rs.getInt("lab116c1"));
                feeSchedule.setName(rs.getString("lab116c2"));
                feeSchedule.setInitialDate(rs.getDate("lab116c3"));
                feeSchedule.setEndDate(rs.getDate("lab116c4"));
                feeSchedule.setAutomatically(rs.getInt("lab116c5") == 1);
                feeSchedule.setState(rs.getInt("lab07c1") == 1);
                /*Usuario*/
                feeSchedule.getUser().setId(rs.getInt("lab04c1"));
                feeSchedule.setLastTransaction(rs.getTimestamp("lab116c7"));
                return feeSchedule;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtener información de una vigencia por un campo especifico.
     *
     * @param id ID de la vigencia a ser consultada.
     * @param name Nombre de la vigencia a ser consultada.
     *
     * @return Instancia con las datos de la vigencia.
     * @throws Exception Error en la base de datos.
     */
    default FeeSchedule get(Integer id, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = ""
                    + "SELECT lab116.lab116c1 "
                    + ", lab116.lab116c2 "
                    + ", lab116.lab116c3 "
                    + ", lab116.lab116c4 "
                    + ", lab116.lab116c5 "
                    + ", lab116.lab07c1 "
                    + ", lab116.lab116c7 "
                    + ", lab116.lab04c1 "
                    + ", lab04c2 "
                    + ", lab04c3 "
                    + ", lab04c4 "                
                    + " FROM lab116 "
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab116.lab04c1";
            /*Where*/
            if (id != null)
            {
                query = query + " WHERE lab116.lab116c1 = ? ";
            }
            if (name != null)
            {
                query = query + " WHERE UPPER(lab116.lab116c2) = ? ";
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
                FeeSchedule feeSchedule = new FeeSchedule();
                feeSchedule.setId(rs.getInt("lab116c1"));
                feeSchedule.setName(rs.getString("lab116c2"));
                feeSchedule.setInitialDate(rs.getDate("lab116c3"));
                feeSchedule.setEndDate(rs.getDate("lab116c4"));
                feeSchedule.setAutomatically(rs.getInt("lab116c5") == 1);
                feeSchedule.setState(rs.getInt("lab07c1") == 1);
                /*Usuario*/
                feeSchedule.getUser().setId(rs.getInt("lab04c1"));
                feeSchedule.getUser().setName(rs.getString("lab04c2"));
                feeSchedule.getUser().setLastName(rs.getString("lab04c3"));
                feeSchedule.getUser().setUserName(rs.getString("lab04c4"));
                
                feeSchedule.setLastTransaction(rs.getTimestamp("lab116c7"));
                return feeSchedule;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Registra una nueva vigencia en la base de datos.
     *
     * @param feeSchedule Instancia con los datos de la vigencia.
     *
     * @return Instancia con las datos de la vigencia.
     * @throws Exception Error en la base de datos.
     */
    default FeeSchedule create(FeeSchedule feeSchedule) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab116")
                .usingGeneratedKeyColumns("lab116c1");

        HashMap parameters = new HashMap();
        parameters.put("lab116c2", feeSchedule.getName().trim());
        parameters.put("lab116c3", feeSchedule.getInitialDate());
        parameters.put("lab116c4", feeSchedule.getEndDate());
        parameters.put("lab116c5", feeSchedule.isAutomatically() ? 1 : 0);
        parameters.put("lab07c1", 0);
        parameters.put("lab116c7", timestamp);
        parameters.put("lab04c1", feeSchedule.getUser().getId());

        Number key = insert.executeAndReturnKey(parameters);
        feeSchedule.setId(key.intValue());
        feeSchedule.setLastTransaction(timestamp);

        return feeSchedule;
    }

    /**
     * Actualiza la información de una vigencia en la base de datos.
     *
     * @param feeSchedule Instancia con los datos de la vigencia.
     *
     * @return Instancia con las datos de la vigencia.
     * @throws Exception Error en la base de datos.
     */
    default FeeSchedule update(FeeSchedule feeSchedule) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        String query = ""
                + " UPDATE lab116"
                + " SET lab116c2=?"
                + " , lab116c3=?"
                + " , lab116c4=?"
                + " , lab116c5=?"
                + " , lab116c7=?"
                + " , lab04c1=?"
                + " WHERE lab116c1=?";

        getJdbcTemplate().update(query,
                feeSchedule.getName(),
                feeSchedule.getInitialDate(),
                feeSchedule.getEndDate(),
                feeSchedule.isAutomatically() ? 1 : 0,
                timestamp,
                feeSchedule.getUser().getId(),
                feeSchedule.getId());

        feeSchedule.setLastTransaction(timestamp);

        return feeSchedule;
    }

}

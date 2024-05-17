/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.pathology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.ProcessingTime;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios del maestro de horas de procesamiento de las muestras de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 13/07/2021
 * @see Creaciòn
*/
public interface ProcessingTimeDao 
{
    public JdbcTemplate getJdbcTemplatePat();
    
    default List<ProcessingTime> list() throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + "SELECT pat31c1, pat31c2, pat31c3, pat31c4, lab04c1a, pat31c5, lab04c1b "
                    + "FROM pat31 ", (ResultSet rs, int i) ->
            {
                ProcessingTime processingTime = new ProcessingTime();
                processingTime.setId(rs.getInt("pat31c1"));
                processingTime.setTime(rs.getString("pat31c2"));
                processingTime.setStatus(rs.getInt("pat31c3"));
                processingTime.getUserCreated().setId(rs.getInt("lab04c1a"));
                processingTime.getUserUpdated().setId(rs.getInt("lab04c1b"));
                processingTime.setCreatedAt(rs.getTimestamp("pat31c4"));
                if (rs.getTimestamp("pat31c5") != null) {
                    processingTime.setUpdatedAt(rs.getTimestamp("pat31c5"));
                }
                return processingTime;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    default ProcessingTime create(ProcessingTime processingTime) {

        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat31")
                .usingColumns("pat31c2", "pat31c3", "pat31c4","lab04c1a")
                .usingGeneratedKeyColumns("pat31c1");

        HashMap parameters = new HashMap();
        parameters.put("pat31c2", processingTime.getTime());
        parameters.put("pat31c3", processingTime.getStatus());
        parameters.put("lab04c1a", processingTime.getUserCreated().getId());
        parameters.put("pat31c4", timestamp);

        Number key = insert.executeAndReturnKey(parameters);
        processingTime.setId(key.intValue());
        processingTime.setCreatedAt(timestamp);
        return processingTime;
    }
   
    default ProcessingTime get(Integer id, String time) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat31c1, pat31c2, pat31c3, pat31c4, lab04c1a, pat31c5, lab04c1b "
                    + "FROM pat31 ";
            
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE pat31c1 = ? ";
            }
            if (time != null)
            {
                query = query + "WHERE pat31c2 = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (time != null)
            {
                object = time;
            }
            
            return getJdbcTemplatePat().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                ProcessingTime processingTime = new ProcessingTime();
                processingTime.setId(rs.getInt("pat31c1"));
                processingTime.setTime(rs.getString("pat31c2"));
                processingTime.setStatus(rs.getInt("pat31c3"));
                processingTime.getUserCreated().setId(rs.getInt("lab04c1a"));
                processingTime.getUserUpdated().setId(rs.getInt("lab04c1b"));
                processingTime.setCreatedAt(rs.getTimestamp("pat31c4"));
                if (rs.getTimestamp("pat31c5") != null) {
                    processingTime.setUpdatedAt(rs.getTimestamp("pat31c5"));
                }
                return processingTime;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default ProcessingTime update(ProcessingTime processingTime) throws Exception {

        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplatePat().update("UPDATE pat31 SET pat31c2 = ?, pat31c3 = ?, pat31c5 = ?, lab04c1b = ? "
                + "WHERE pat31c1 = ?",
                processingTime.getTime(), processingTime.getStatus(), timestamp, processingTime.getUserUpdated().getId(), processingTime.getId());

        processingTime.setUpdatedAt(timestamp);

        return processingTime;
    }
}

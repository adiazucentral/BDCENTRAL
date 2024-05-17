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
import net.cltech.enterprisent.domain.masters.pathology.EventPathology;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios del maestro de eventos de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 18/05/2021
 * @see Creaciòn
*/
public interface EventPathologyDao 
{
     public JdbcTemplate getJdbcTemplatePat();
    
    default List<EventPathology> list() throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + "SELECT pat21c1, pat21c2, pat21c3, pat21c4, pat21c5, pat21c6, lab04c1a, pat21c7, lab04c1b "
                    + "FROM pat21 ", (ResultSet rs, int i) ->
            {
                EventPathology event = new EventPathology();
                event.setId(rs.getInt("pat21c1"));
                event.setCode(rs.getString("pat21c2"));
                event.setName(rs.getString("pat21c3"));
                event.setColour(rs.getString("pat21c4"));
                event.setStatus(rs.getInt("pat21c5"));
                event.getUserCreated().setId(rs.getInt("lab04c1a"));
                event.getUserUpdated().setId(rs.getInt("lab04c1b"));
                event.setCreatedAt(rs.getTimestamp("pat21c6"));
                if (rs.getTimestamp("pat21c7") != null) {
                    event.setUpdatedAt(rs.getTimestamp("pat21c7"));
                }
                return event;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    default EventPathology create(EventPathology event) {

        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat21")
                .usingColumns("pat21c2", "pat21c3", "pat21c4", "pat21c5", "lab04c1a", "pat21c6")
                .usingGeneratedKeyColumns("pat21c1");

        HashMap parameters = new HashMap();
        parameters.put("pat21c2", event.getCode());
        parameters.put("pat21c3", event.getName().trim());
        parameters.put("pat21c5", event.getStatus());
        parameters.put("pat21c4", event.getColour());
        parameters.put("lab04c1a", event.getUserCreated().getId());
        parameters.put("pat21c6", timestamp);

        Number key = insert.executeAndReturnKey(parameters);
        event.setId(key.intValue());
        event.setCreatedAt(timestamp);
        return event;
    }
   
    default EventPathology get(Integer id, String code) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat21c1, pat21c2, pat21c3, pat21c4, pat21c5, pat21c6, lab04c1a, pat21c7, lab04c1b "
                    + "FROM pat21 ";
            
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE pat21c1 = ? ";
            }
            if (code != null)
            {
                query = query + "WHERE UPPER(pat21c2) = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (code != null)
            {
                object = code.toUpperCase();
            }

            return getJdbcTemplatePat().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                EventPathology event = new EventPathology();
                event.setId(rs.getInt("pat21c1"));
                event.setCode(rs.getString("pat21c2"));
                event.setName(rs.getString("pat21c3"));
                event.setColour(rs.getString("pat21c4"));
                event.setStatus(rs.getInt("pat21c5"));
                event.getUserCreated().setId(rs.getInt("lab04c1a"));
                event.getUserUpdated().setId(rs.getInt("lab04c1b"));
                event.setCreatedAt(rs.getTimestamp("pat21c6"));
                if (rs.getTimestamp("pat21c7") != null) {
                    event.setUpdatedAt(rs.getTimestamp("pat21c7"));
                }
                return event;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default EventPathology update(EventPathology event) throws Exception {

        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplatePat().update("UPDATE pat21 SET pat21c2 = ?, pat21c3 = ?, pat21c4 = ?, pat21c5 = ? ,lab04c1b = ?, pat21c7 = ? "
                + "WHERE pat21c1 = ?",
                event.getCode(), event.getName(), event.getColour(), event.getStatus(), event.getUserUpdated().getId(), timestamp, event.getId());

        event.setUpdatedAt(timestamp);

        return event;
    }
}

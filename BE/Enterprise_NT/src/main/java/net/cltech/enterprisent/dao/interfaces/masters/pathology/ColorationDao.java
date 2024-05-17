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
import net.cltech.enterprisent.domain.masters.pathology.Coloration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios del maestro de coloraciones de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 07/04/2021
 * @see Creaciòn
 */
public interface ColorationDao 
{
    public JdbcTemplate getJdbcTemplatePat();
    
    default List<Coloration> list() throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + "SELECT pat06c1, pat06c2, pat06c3, pat06c4, lab04c1a, pat06c5, lab04c1b, pat06c6 "
                    + "FROM pat06 ", (ResultSet rs, int i) ->
            {
                Coloration coloration = new Coloration();
                coloration.setId(rs.getInt("pat06c1"));
                coloration.setCode(rs.getString("pat06c2"));
                coloration.setName(rs.getString("pat06c3"));
                coloration.setStatus(rs.getInt("pat06c4"));
                coloration.getUserCreated().setId(rs.getInt("lab04c1a"));
                coloration.getUserUpdated().setId(rs.getInt("lab04c1b"));
                coloration.setCreatedAt(rs.getTimestamp("pat06c5"));
                if (rs.getTimestamp("pat06c6") != null) {
                    coloration.setUpdatedAt(rs.getTimestamp("pat06c6"));
                }
                return coloration;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    default Coloration create(Coloration coloration) {

        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat06")
                .usingColumns("pat06c2", "pat06c3", "pat06c4", "lab04c1a", "pat06c5")
                .usingGeneratedKeyColumns("pat06c1");

        HashMap parameters = new HashMap();
        parameters.put("pat06c2", coloration.getCode());
        parameters.put("pat06c3", coloration.getName().trim());
        parameters.put("pat06c4", coloration.getStatus());
        parameters.put("lab04c1a", coloration.getUserCreated().getId());
        parameters.put("pat06c5", timestamp);

        Number key = insert.executeAndReturnKey(parameters);
        coloration.setId(key.intValue());
        coloration.setCreatedAt(timestamp);
        return coloration;
    }
   
    default Coloration get(Integer id, String name, String code) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat06c1, pat06c2, pat06c3, pat06c4, lab04c1a, pat06c5, lab04c1b, pat06c6 "
                    + "FROM pat06 ";
            
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE pat06c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(pat06c3) = ? ";
            }
            if (code != null)
            {
                query = query + "WHERE UPPER(pat06c2) = ? ";
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
                Coloration coloration = new Coloration();
                coloration.setId(rs.getInt("pat06c1"));
                coloration.setCode(rs.getString("pat06c2"));
                coloration.setName(rs.getString("pat06c3"));
                coloration.setStatus(rs.getInt("pat06c4"));
                coloration.getUserCreated().setId(rs.getInt("lab04c1a"));
                coloration.getUserUpdated().setId(rs.getInt("lab04c1b"));
                coloration.setCreatedAt(rs.getTimestamp("pat06c5"));
                if (rs.getTimestamp("pat06c6") != null) {
                    coloration.setUpdatedAt(rs.getTimestamp("pat06c6"));
                }
                return coloration;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default Coloration update(Coloration coloration) throws Exception {

        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplatePat().update("UPDATE pat06 SET pat06c2 = ?, pat06c3 = ?, pat06c4 = ?, lab04c1b = ?, pat06c6 = ? "
                + "WHERE pat06c1 = ?",
                coloration.getCode(), coloration.getName(), coloration.getStatus(), coloration.getUserUpdated().getId(), timestamp, coloration.getId());

        coloration.setUpdatedAt(timestamp);

        return coloration;
    }
    
}

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
import net.cltech.enterprisent.domain.masters.pathology.Casete;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios del maestro de casetes de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 07/04/2021
 * @see Creaciòn
*/
public interface CaseteDao {
    
    public JdbcTemplate getJdbcTemplatePat();
    
    default List<Casete> list() throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + "SELECT pat03c1, pat03c2, pat03c3, pat03c4, pat03c5, lab04c1a, pat03c6, lab04c1b, pat03c7 "
                    + "FROM pat03 ", (ResultSet rs, int i) ->
            {
                Casete casete = new Casete();
                casete.setId(rs.getInt("pat03c1"));
                casete.setCode(rs.getString("pat03c2"));
                casete.setName(rs.getString("pat03c3"));
                casete.setStatus(rs.getInt("pat03c4"));
                casete.setColour(rs.getString("pat03c5"));
                casete.getUserCreated().setId(rs.getInt("lab04c1a"));
                casete.getUserUpdated().setId(rs.getInt("lab04c1b"));
                casete.setCreatedAt(rs.getTimestamp("pat03c6"));
                if (rs.getTimestamp("pat03c7") != null) {
                    casete.setUpdatedAt(rs.getTimestamp("pat03c7"));
                }
                return casete;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    default Casete create(Casete casete) {

        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat03")
                .usingColumns("pat03c2", "pat03c3", "pat03c4","pat03c5","lab04c1a", "pat03c6")
                .usingGeneratedKeyColumns("pat03c1");

        HashMap parameters = new HashMap();
        parameters.put("pat03c2", casete.getCode());
        parameters.put("pat03c3", casete.getName().trim());
        parameters.put("pat03c4", casete.getStatus());
        parameters.put("pat03c5", casete.getColour());
        parameters.put("lab04c1a", casete.getUserCreated().getId());
        parameters.put("pat03c6", timestamp);

        Number key = insert.executeAndReturnKey(parameters);
        casete.setId(key.intValue());
        casete.setCreatedAt(timestamp);
        return casete;
    }
   
    default Casete get(Integer id, String name, String code) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat03c1, pat03c2, pat03c3, pat03c4, pat03c5, lab04c1a, pat03c6, lab04c1b, pat03c7 "
                    + "FROM pat03 ";
            
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE pat03c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(pat03c3) = ? ";
            }
            if (code != null)
            {
                query = query + "WHERE UPPER(pat03c2) = ? ";
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
                Casete casete = new Casete();
                casete.setId(rs.getInt("pat03c1"));
                casete.setCode(rs.getString("pat03c2"));
                casete.setName(rs.getString("pat03c3"));
                casete.setStatus(rs.getInt("pat03c4"));
                casete.setColour(rs.getString("pat03c5"));
                casete.getUserCreated().setId(rs.getInt("lab04c1a"));
                casete.getUserUpdated().setId(rs.getInt("lab04c1b"));
                casete.setCreatedAt(rs.getTimestamp("pat03c6"));
                if (rs.getTimestamp("pat03c7") != null) {
                    casete.setUpdatedAt(rs.getTimestamp("pat03c7"));
                }
                return casete;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default Casete update(Casete casete) throws Exception {

        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplatePat().update("UPDATE pat03 SET pat03c2 = ?, pat03c3 = ?, pat03c4 = ?, pat03c5 = ? ,lab04c1b = ?, pat03c7 = ? "
                + "WHERE pat03c1 = ?",
                casete.getCode(), casete.getName(), casete.getStatus(), casete.getColour() ,casete.getUserUpdated().getId(), timestamp, casete.getId());

        casete.setUpdatedAt(timestamp);

        return casete;
    }
}

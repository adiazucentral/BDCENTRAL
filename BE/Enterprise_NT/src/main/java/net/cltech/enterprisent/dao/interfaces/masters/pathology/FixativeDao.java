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
import net.cltech.enterprisent.domain.masters.pathology.Fixative;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios del maestro de fijadores de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 07/04/2021
 * @see Creaciòn
 */
public interface FixativeDao {
    
    public JdbcTemplate getJdbcTemplatePat();
    
    default List<Fixative> list() throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + "SELECT pat10c1, pat10c2, pat10c3, pat10c4, lab04c1a, pat10c5, lab04c1b, pat10c6 "
                    + "FROM pat10 ", (ResultSet rs, int i) ->
            {
                Fixative fixative = new Fixative();
                fixative.setId(rs.getInt("pat10c1"));
                fixative.setCode(rs.getString("pat10c2"));
                fixative.setName(rs.getString("pat10c3"));
                fixative.setStatus(rs.getInt("pat10c4"));
                fixative.getUserCreated().setId(rs.getInt("lab04c1a"));
                fixative.getUserUpdated().setId(rs.getInt("lab04c1b"));
                fixative.setCreatedAt(rs.getTimestamp("pat10c5"));
                if (rs.getTimestamp("pat10c6") != null) {
                    fixative.setUpdatedAt(rs.getTimestamp("pat10c6"));
                }
                return fixative;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    default Fixative create(Fixative fixative) {

        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat10")
                .usingColumns("pat10c2", "pat10c3", "pat10c4", "lab04c1a", "pat10c5")
                .usingGeneratedKeyColumns("pat10c1");

        HashMap parameters = new HashMap();
        parameters.put("pat10c2", fixative.getCode());
        parameters.put("pat10c3", fixative.getName().trim());
        parameters.put("pat10c4", fixative.getStatus());
        parameters.put("lab04c1a", fixative.getUserCreated().getId());
        parameters.put("pat10c5", timestamp);

        Number key = insert.executeAndReturnKey(parameters);
        fixative.setId(key.intValue());
        fixative.setCreatedAt(timestamp);
        return fixative;
    }
   
    default Fixative get(Integer id, String name, String code) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat10c1, pat10c2, pat10c3, pat10c4, lab04c1a, pat10c5, lab04c1b, pat10c6 "
                    + "FROM pat10 ";
            
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE pat10c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(pat10c3) = ? ";
            }
            if (code != null)
            {
                query = query + "WHERE UPPER(pat10c2) = ? ";
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
                Fixative fixative = new Fixative();
                fixative.setId(rs.getInt("pat10c1"));
                fixative.setCode(rs.getString("pat10c2"));
                fixative.setName(rs.getString("pat10c3"));
                fixative.setStatus(rs.getInt("pat10c4"));
                fixative.getUserCreated().setId(rs.getInt("lab04c1a"));
                fixative.getUserUpdated().setId(rs.getInt("lab04c1b"));
                fixative.setCreatedAt(rs.getTimestamp("pat10c5"));
                if (rs.getTimestamp("pat10c6") != null) {
                    fixative.setUpdatedAt(rs.getTimestamp("pat10c6"));
                }
                return fixative;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default Fixative update(Fixative fixative) throws Exception {

        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplatePat().update("UPDATE pat10 SET pat10c2 = ?, pat10c3 = ?, pat10c4 = ?, lab04c1b = ?, pat10c6 = ? "
                + "WHERE pat10c1 = ?",
                fixative.getCode(), fixative.getName(), fixative.getStatus(), fixative.getUserUpdated().getId(), timestamp, fixative.getId());

        fixative.setUpdatedAt(timestamp);

        return fixative;
    }
}

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
import net.cltech.enterprisent.domain.masters.pathology.Field;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios del maestro de campos para las plantillas de mascroscopia
 *
 * @version 1.0.0
 * @author omendez
 * @see 09/06/2021
 * @see Creaciòn
 */
public interface FieldDao 
{
    public JdbcTemplate getJdbcTemplatePat();
    
    default List<Field> list() throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + "SELECT pat26c1, pat26c2, pat26c3, pat26c4, pat26c5, pat26c6, pat26c7, lab04c1a, pat26c8, lab04c1b "
                    + "FROM pat26 ", (ResultSet rs, int i) ->
            {
                Field field = new Field();
                field.setId(rs.getInt("pat26c1"));
                field.setName(rs.getString("pat26c2"));
                field.setType(rs.getInt("pat26c3"));
                field.setGrid(rs.getInt("pat26c4"));
                field.setRequired(rs.getInt("pat26c5"));
                field.setStatus(rs.getInt("pat26c6"));
                field.getUserCreated().setId(rs.getInt("lab04c1a"));
                field.getUserUpdated().setId(rs.getInt("lab04c1b"));
                field.setCreatedAt(rs.getTimestamp("pat26c7"));
                if (rs.getTimestamp("pat26c8") != null) {
                    field.setUpdatedAt(rs.getTimestamp("pat26c8"));
                }
                return field;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    default Field create(Field field) {

        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat26")
                .usingColumns("pat26c2", "pat26c3", "pat26c4", "pat26c5", "pat26c6", "pat26c7", "lab04c1a")
                .usingGeneratedKeyColumns("pat26c1");

        HashMap parameters = new HashMap();
        parameters.put("pat26c2", field.getName());
        parameters.put("pat26c3", field.getType());
        parameters.put("pat26c4", field.getGrid());
        parameters.put("pat26c5", field.getRequired());
        parameters.put("pat26c6", field.getStatus());
        parameters.put("lab04c1a", field.getUserCreated().getId());
        parameters.put("pat26c7", timestamp);

        Number key = insert.executeAndReturnKey(parameters);
        field.setId(key.intValue());
        field.setCreatedAt(timestamp);
        return field;
    }
   
    default Field get(Integer id, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat26c1, pat26c2, pat26c3, pat26c4, pat26c5, pat26c6, pat26c7, lab04c1a, pat26c8, lab04c1b "
                    + "FROM pat26 ";
            
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE pat26c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(pat26c2) = ? ";
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

            return getJdbcTemplatePat().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Field field = new Field();
                field.setId(rs.getInt("pat26c1"));
                field.setName(rs.getString("pat26c2"));
                field.setType(rs.getInt("pat26c3"));
                field.setGrid(rs.getInt("pat26c4"));
                field.setRequired(rs.getInt("pat26c5"));
                field.setStatus(rs.getInt("pat26c6"));
                field.getUserCreated().setId(rs.getInt("lab04c1a"));
                field.getUserUpdated().setId(rs.getInt("lab04c1b"));
                field.setCreatedAt(rs.getTimestamp("pat26c7"));
                if (rs.getTimestamp("pat26c8") != null) {
                    field.setUpdatedAt(rs.getTimestamp("pat26c8"));
                }
                return field;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default Field update(Field field) throws Exception {

        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplatePat().update("UPDATE pat26 SET pat26c2 = ?, pat26c3 = ?, pat26c4 = ?, pat26c5 = ?, pat26c6 = ?, pat26c8 = ?, lab04c1b = ? "
                + " WHERE pat26c1 = ? ",
                field.getName(), field.getType(), field.getGrid(), field.getRequired(), field.getStatus(), timestamp, field.getUserUpdated().getId(), field.getId());

        field.setUpdatedAt(timestamp);

        return field;
    }
    
}

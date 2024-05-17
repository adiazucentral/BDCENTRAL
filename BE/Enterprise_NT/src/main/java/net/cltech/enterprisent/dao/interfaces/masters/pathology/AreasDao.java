/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.pathology;


import net.cltech.enterprisent.domain.masters.pathology.Area;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios de el maestro de areas de patología
 *
 * @version 1.0.0
 * @author etoro
 * @see 08/10/2020
 * @see Creaciòn
 */
public interface AreasDao {

    public JdbcTemplate getJdbcTemplatePat();
    
    default List<Area> list() throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + "SELECT pat02c1, pat02c2, pat02c3, pat02c4, lab04c1a, pat02c5, lab04c1b, pat02c6 "
                    + "FROM pat02 ", (ResultSet rs, int i) ->
            {
                Area area = new Area();
                area.setId(rs.getInt("pat02c1"));
                area.setCode(rs.getString("pat02c2"));
                area.setName(rs.getString("pat02c3"));
                area.setStatus(rs.getInt("pat02c4"));
                area.getUserCreated().setId(rs.getInt("lab04c1a"));
                area.getUserUpdated().setId(rs.getInt("lab04c1b"));
                area.setCreatedAt(rs.getTimestamp("pat02c5"));
                if (rs.getTimestamp("pat02c6") != null) {
                    area.setUpdatedAt(rs.getTimestamp("pat02c6"));
                }
                return area;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    default Area create(Area area) {

        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat02")
                .usingColumns("pat02c2", "pat02c3", "pat02c4", "lab04c1a", "pat02c5")
                .usingGeneratedKeyColumns("pat02c1");

        HashMap parameters = new HashMap();
        parameters.put("pat02c2", area.getCode());
        parameters.put("pat02c3", area.getName().trim());
        parameters.put("pat02c4", area.getStatus());
        parameters.put("lab04c1a", area.getUserCreated().getId());
        parameters.put("pat02c5", timestamp);

        Number key = insert.executeAndReturnKey(parameters);
        area.setId(key.intValue());
        area.setCreatedAt(timestamp);
        return area;
    }
   
    default Area get(Integer id, String name, String code) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat02c1, pat02c2, pat02c3, pat02c4, lab04c1a, pat02c5, lab04c1b, pat02c6 "
                    + "FROM pat02 ";
            
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE pat02c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(pat02c3) = ? ";
            }
            if (code != null)
            {
                query = query + "WHERE UPPER(pat02c2) = ? ";
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
                Area area = new Area();
                area.setId(rs.getInt("pat02c1"));
                area.setCode(rs.getString("pat02c2"));
                area.setName(rs.getString("pat02c3"));
                area.setStatus(rs.getInt("pat02c4"));
                area.getUserCreated().setId(rs.getInt("lab04c1a"));
                area.getUserUpdated().setId(rs.getInt("lab04c1b"));
                area.setCreatedAt(rs.getTimestamp("pat02c5"));
                if (rs.getTimestamp("pat02c6") != null) {
                    area.setUpdatedAt(rs.getTimestamp("pat02c6"));
                }
                return area;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default Area update(Area area) throws Exception {

        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplatePat().update("UPDATE pat02 SET pat02c2 = ?, pat02c3 = ?, pat02c4 = ?, lab04c1b = ?, pat02c6 = ? "
                + "WHERE pat02c1 = ?",
                area.getCode(), area.getName(), area.getStatus(), area.getUserUpdated().getId(), timestamp, area.getId());

        area.setUpdatedAt(timestamp);

        return area;
    }

}

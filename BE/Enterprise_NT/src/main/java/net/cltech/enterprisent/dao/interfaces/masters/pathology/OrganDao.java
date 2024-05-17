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
import net.cltech.enterprisent.domain.masters.pathology.Organ;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios de el maestro de organos de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 20/10/2020
 * @see Creaciòn
 */
public interface OrganDao 
{
    public JdbcTemplate getJdbcTemplatePat();
    
    default List<Organ> list() throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + "SELECT pat07c1, pat07c2, pat07c3, pat07c4, pat07c5, lab04c1a, pat07c6, lab04c1b "
                    + "FROM pat07 ", (ResultSet rs, int i) ->
            {
                Organ organ = new Organ();
                organ.setId(rs.getInt("pat07c1"));
                organ.setCode(rs.getString("pat07c2"));
                organ.setName(rs.getString("pat07c3"));
                organ.setStatus(rs.getInt("pat07c4"));
                organ.getUserCreated().setId(rs.getInt("lab04c1a"));
                organ.getUserUpdated().setId(rs.getInt("lab04c1b"));
                organ.setCreatedAt(rs.getTimestamp("pat07c5"));
                if (rs.getTimestamp("pat07c6") != null) {
                    organ.setUpdatedAt(rs.getTimestamp("pat07c6"));
                }
                return organ;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    default Organ create(Organ organ) throws Exception {

        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat07")
                .usingColumns("pat07c2", "pat07c3", "pat07c4", "pat07c5", "lab04c1a")
                .usingGeneratedKeyColumns("pat07c1");

        HashMap parameters = new HashMap();
        parameters.put("pat07c2", organ.getCode());
        parameters.put("pat07c3", organ.getName().trim());
        parameters.put("pat07c4", organ.getStatus());
        parameters.put("lab04c1a", organ.getUserCreated().getId());
        parameters.put("pat07c5", timestamp);

        Number key = insert.executeAndReturnKey(parameters);
        organ.setId(key.intValue());
        organ.setCreatedAt(timestamp);
        return organ;
    }
   
    default Organ get(Integer id, String name, String code) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat07c1, pat07c2, pat07c3, pat07c4, pat07c5, lab04c1a, pat07c6, lab04c1b "
                    + "FROM pat07 ";
            
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE pat07c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(pat07c3) = ? ";
            }
            if (code != null)
            {
                query = query + "WHERE UPPER(pat07c2) = ? ";
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
                Organ organ = new Organ();
                organ.setId(rs.getInt("pat07c1"));
                organ.setCode(rs.getString("pat07c2"));
                organ.setName(rs.getString("pat07c3"));
                organ.setStatus(rs.getInt("pat07c4"));
                organ.getUserCreated().setId(rs.getInt("lab04c1a"));
                organ.getUserUpdated().setId(rs.getInt("lab04c1b"));
                organ.setCreatedAt(rs.getTimestamp("pat07c5"));
                if (rs.getTimestamp("pat07c6") != null) {
                    organ.setUpdatedAt(rs.getTimestamp("pat07c6"));
                }
                return organ;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
    /**
    * Obtiene un organo por especimen
    * 
    * @param specimenId Id del especimen.
    *
    * @return Organo.
    * @throws Exception Error en la base de datos.
    */
    default Organ findOrganBySpecimen(Integer specimenId) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat07.pat07c1, pat07c2, pat07c3, pat07c4, pat07c5, pat07.lab04c1a, pat07c6, pat07.lab04c1b  "
                    + " FROM pat07 "
                    + " JOIN pat09 ON pat09.pat07c1 = pat07.pat07c1 "
                    + " WHERE pat09.lab24c1 = ? ";

            List parameters = new ArrayList(0);
            
            if (specimenId != null)
            {
                parameters.add(specimenId);
            }
            
            return getJdbcTemplatePat().queryForObject(query,
                    parameters.toArray(), (ResultSet rs, int i) ->
            {
                Organ organ = new Organ();
                organ.setId(rs.getInt("pat07c1"));
                organ.setCode(rs.getString("pat07c2"));
                organ.setName(rs.getString("pat07c3"));
                organ.setStatus(rs.getInt("pat07c4"));
                organ.getUserCreated().setId(rs.getInt("lab04c1a"));
                organ.getUserUpdated().setId(rs.getInt("lab04c1b"));
                organ.setCreatedAt(rs.getTimestamp("pat07c5"));
                if (rs.getTimestamp("pat07c6") != null) {
                    organ.setUpdatedAt(rs.getTimestamp("pat07c6"));
                }
                return organ;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default Organ update(Organ organ) throws Exception {

        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplatePat().update("UPDATE pat07 SET pat07c2 = ?, pat07c3 = ?, pat07c4 = ?, pat07c6 = ?, lab04c1b = ? "
                + "WHERE pat07c1 = ?",
                organ.getCode(), organ.getName(), organ.getStatus(), timestamp , organ.getUserUpdated().getId(), organ.getId());
        organ.setUpdatedAt(timestamp);
        return organ;
    }
}

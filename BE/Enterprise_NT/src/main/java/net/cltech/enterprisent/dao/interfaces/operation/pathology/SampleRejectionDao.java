/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.pathology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import net.cltech.enterprisent.domain.operation.pathology.SampleRejection;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * Representa los métodos de acceso a base de datos para la información de las muestras rechazadas de patologia.
 *
 * @version 1.0.0
 * @author omendez
 * @since 26/02/2021
 * @see Creación
 */
public interface SampleRejectionDao {
    
    public JdbcTemplate getJdbcTemplatePat();
    
    default List<SampleRejection> list() throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + "SELECT pat16c1, lab22c1, pat16.pat11c1, lab30c1, pat16c2, pat16c3, pat16.lab04c1a, "
                    + " pat16c4, pat16.lab04c1b, pat11c2, pat11c3, pat11c4 "
                    + " FROM pat16 "
                    + " JOIN pat11 ON pat11.pat11c1 = pat16.pat11c1 ", (ResultSet rs, int i) ->
            {
                SampleRejection rejection = new SampleRejection();
                
                rejection.setId(rs.getInt("pat16c1"));
                rejection.setOrderNumber(rs.getLong("lab22c1"));
                rejection.getStudyType().setId(rs.getInt("pat11c1"));
                rejection.getStudyType().setCode(rs.getString("pat11c2"));
                rejection.getStudyType().setName(rs.getString("pat11c3"));
                rejection.getMotive().setId(rs.getInt("lab30c1"));
                rejection.setObservation(rs.getString("pat16c2"));
                rejection.getUserCreated().setId(rs.getInt("lab04c1a"));
                rejection.getUserUpdated().setId(rs.getInt("lab04c1b"));
                rejection.setCreatedAt(rs.getTimestamp("pat16c3"));
                if (rs.getTimestamp("pat16c4") != null) {
                    rejection.setUpdatedAt(rs.getTimestamp("pat16c4"));
                }
                return rejection;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    default SampleRejection get(Integer studyType, Long order) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat16c1, lab22c1, pat16.pat11c1, lab30c1, pat16c2, pat16c3, pat16.lab04c1a, "
                    + " pat16c4, pat16.lab04c1b, pat11c2, pat11c3, pat11c4 "
                    + " FROM pat16 "
                    + " JOIN pat11 ON pat11.pat11c1 = pat16.pat11c1 ";
            
            /*Where*/
            if (studyType!= null && order != null)
            {
                query = query + "WHERE pat16.pat11c1 = ? AND lab22c1 = ? ";
            }
            
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            List object = new ArrayList(0);
            if (studyType != null && order != null)
            {
                object.add(studyType);
                object.add(order);
            }
            
            return getJdbcTemplatePat().queryForObject(query, object.toArray(), (ResultSet rs, int i) ->
            {
                SampleRejection rejection = new SampleRejection();
                
                rejection.setId(rs.getInt("pat16c1"));
                rejection.setOrderNumber(rs.getLong("lab22c1"));
                rejection.getStudyType().setId(rs.getInt("pat11c1"));
                rejection.getStudyType().setCode(rs.getString("pat11c2"));
                rejection.getStudyType().setName(rs.getString("pat11c3"));
                rejection.getMotive().setId(rs.getInt("lab30c1"));
                rejection.setObservation(rs.getString("pat16c2"));
                rejection.getUserCreated().setId(rs.getInt("lab04c1a"));
                rejection.getUserUpdated().setId(rs.getInt("lab04c1b"));
                rejection.setCreatedAt(rs.getTimestamp("pat16c3"));
                if (rs.getTimestamp("pat16c4") != null) {
                    rejection.setUpdatedAt(rs.getTimestamp("pat16c4"));
                }
                return rejection;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
    /**
    * Crea un rechazo de muestras en el sistema
    *
    * @param rejection {@link net.cltech.enterprisent.domain.operation.pathology.SampleRejection}
    *
    * @return {@link net.cltech.enterprisent.domain.operation.pathology.SampleRejection}
    * @throws Exception Error en base de datos
    */
    default SampleRejection create(SampleRejection rejection) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat16")
                .usingGeneratedKeyColumns("pat16c1");
        HashMap<String, Object> parameters = new HashMap<>(0);
        parameters.put("lab22c1", rejection.getOrderNumber());
        parameters.put("pat11c1", rejection.getStudyType().getId());
        parameters.put("lab30c1", rejection.getMotive().getId());
        parameters.put("pat16c2", rejection.getObservation());
        parameters.put("lab04c1a", rejection.getUserCreated().getId());
        parameters.put("pat16c3", timestamp);
        
        Number id = insert.executeAndReturnKey(parameters);
        rejection.setId(id.intValue());
        rejection.setCreatedAt(timestamp);
        return rejection;
    }
    
    /**
     * Elimina las muestras rechazadas 
     *
     * @param rejectList lista de muestras rechazadas
     *
     * @return Lista de muestras actualizadas
     * @throws Exception
     */
    default List<SampleRejection> activeSamples(List<SampleRejection> rejectList) throws Exception
    {
        List<Object[]> parameters = new ArrayList<>(0);
        String query = "DELETE FROM pat16 WHERE lab22c1 = ? AND pat11c1 = ? ";

        rejectList.forEach((reject) -> {
            parameters.add(new Object[]
            {
                reject.getOrderNumber(),
                reject.getStudyType().getId(),
            });
        });
        getJdbcTemplatePat().batchUpdate(query, parameters);
        return rejectList;
    }
    
}

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
import net.cltech.enterprisent.domain.masters.pathology.Field;
import net.cltech.enterprisent.domain.operation.pathology.Macroscopy;
import net.cltech.enterprisent.tools.Constants;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios de las descripciones macroscopicas de los casos de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @see 16/06/2021
 * @see Creaciòn
 */
public interface MacroscopyDao 
{
    public JdbcTemplate getJdbcTemplatePat();
    
    default Macroscopy get(int idCase) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat25c1, pat01c1, lab04c1a, pat25c3, pat25c4, pat25.pat28c1, pat28c2, pat28c3, pat28c4, pat25c6, lab04c1c"
                    + " FROM pat25 "
                    + " LEFT JOIN pat28 ON pat28.pat28c1 = pat25.pat28c1 "
                    + " WHERE pat01c1 = ?";
            
            List object = new ArrayList(0);
            object.add(idCase);

            return getJdbcTemplatePat().queryForObject(query, object.toArray(), (ResultSet rs, int i) ->
            {
                Macroscopy macroscopy = new Macroscopy();
                
                macroscopy.setId(rs.getInt("pat25c1"));
                macroscopy.setCasePat(rs.getInt("pat01c1"));
                macroscopy.getPathologist().setId(rs.getInt("lab04c1a"));
                macroscopy.setCreatedAt(rs.getTimestamp("pat25c3"));
                macroscopy.setTranscription(rs.getInt("pat25c4"));
                macroscopy.getAudio().setId(rs.getInt("pat28c1"));
                macroscopy.getAudio().setExtension(rs.getString("pat28c3"));
                macroscopy.getAudio().setUrl(rs.getString("pat28c4"));
                macroscopy.setAuthorization(rs.getInt("pat25c6"));
                macroscopy.getAuthorizer().setId(rs.getInt("lab04c1c"));
                return macroscopy;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
    default Macroscopy create(Macroscopy macroscopy) 
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat25")
                .usingColumns("pat01c1", "lab04c1a", "pat25c3", "pat25c4", "pat28c1", "pat25c6", "lab04c1c")
                .usingGeneratedKeyColumns("pat25c1");

        HashMap parameters = new HashMap();
        parameters.put("pat01c1", macroscopy.getCasePat());
        parameters.put("lab04c1a", macroscopy.getPathologist().getId());
        parameters.put("pat25c3", timestamp);
        parameters.put("pat25c4", macroscopy.getTranscription());
        parameters.put("pat28c1", macroscopy.getAudio().getId());
        parameters.put("pat25c6", macroscopy.getAuthorization());
        parameters.put("lab04c1c", macroscopy.getAuthorizer() == null ? null : macroscopy.getAuthorizer().getId());

        Number key = insert.executeAndReturnKey(parameters);
        macroscopy.setId(key.intValue());
        macroscopy.setCreatedAt(timestamp);
        return macroscopy;
    }
    
    /**
    * Elimina los valores de una plantilla de una descripcion macroscopica de un caso
    *
    * @param idMacroscopy id de la descripcion
    *
    * @return registros afectados
    */
    default int deleteValues(int idMacroscopy)
    {
        return getJdbcTemplatePat().update("DELETE FROM pat29 WHERE pat25c1 = ? ", idMacroscopy);
    }
    
    /**
     * Inserta los valores de una plantilla de una descripcion macroscopica de un caso
     * @param idMacroscopy objeto con información de la relación
     *
     * @return numero de registros afectados
     * @throws Exception Error de base de datos
     */
    default int insertValues(List<Field> fields, int idMacroscopy) throws Exception
    {
        final List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat29")
                .usingColumns("pat25c1", "pat29c2", "pat27c1")
                .usingGeneratedKeyColumns("pat29c1");

        fields.stream().map((field)->
        {
            HashMap parameters = new HashMap();
            parameters.put("pat25c1", idMacroscopy);
            parameters.put("pat29c2", field.getValue());
            parameters.put("pat27c1", field.getTemplate());

            return parameters;
        }).forEachOrdered((parameters)
                ->
        {
            batchArray.add(parameters);
        });

        return insert.executeBatch(batchArray.toArray(new HashMap[fields.size()])).length;
    }
    
    default Macroscopy update(Macroscopy macroscopy) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String update = ""
                + "UPDATE pat25 "
                + "SET pat01c1 = ? , lab04c1a = ?, pat25c3 = ? "
                + ", pat25c4 = ?, pat28c1 = ?, pat25c6 = ?, lab04c1c = ? ";
        
        List object = new ArrayList(0);
        object.add(macroscopy.getCasePat());
        object.add(macroscopy.getPathologist().getId());
        object.add(timestamp);
        object.add(macroscopy.getTranscription());
        object.add(macroscopy.getAudio() != null ? macroscopy.getAudio().getId() : null);
        object.add(macroscopy.getAuthorization());
        object.add(macroscopy.getAuthorizer() == null ? null : macroscopy.getAuthorizer().getId());

        update += " WHERE pat25c1 = ? ";
        object.add(macroscopy.getId());
        int affectedRows = getJdbcTemplatePat().update(update, object.toArray());
        return affectedRows > 0 ? macroscopy : null;
    }
    
    default List<Macroscopy> getPendingTranscripts() throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + " SELECT pat25c1, pat25.pat01c1, pat01c2, lab22c1, pat11c2, pat11c3, pat25.lab04c1a, pat25c3, pat25c6 "
                    + " FROM pat25 "
                    + " LEFT JOIN pat01 ON pat01.pat01c1 = pat25.pat01c1 "
                    + " LEFT JOIN pat11 ON pat01.pat11c1 = pat11.pat11c1 "
                    + " WHERE pat25c4 = 1 AND pat01c4 = " + Constants.TRANSCRIPTION
                    ,(ResultSet rs, int i) ->
            {
                Macroscopy macroscopy = new Macroscopy();
                
                macroscopy.setId(rs.getInt("pat25c1"));
                macroscopy.setCasePat(rs.getInt("pat01c1"));
                macroscopy.setNumberCase(rs.getLong("pat01c2"));
                macroscopy.setNumberOrder(rs.getLong("lab22c1"));
                macroscopy.setAuthorization(rs.getInt("pat25c6"));
                macroscopy.setStudyTypeCode(rs.getString("pat11c2"));
                macroscopy.setStudyTypeName(rs.getString("pat11c3"));
                macroscopy.getPathologist().setId(rs.getInt("lab04c1a"));
                macroscopy.setCreatedAt(rs.getTimestamp("pat25c3"));
                return macroscopy;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    default Macroscopy transcript(Macroscopy macroscopy) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String update = ""
                + "UPDATE pat25 "
                + "SET pat25c4 = ? , lab04c1b = ?, pat25c5 = ? ";
        
        List object = new ArrayList(0);
        object.add(macroscopy.getDraft() == 1 ? 1 : 0);
        object.add(macroscopy.getTranscriber().getId());
        object.add(timestamp);

        update += " WHERE pat25c1 = ? ";
        object.add(macroscopy.getId());
        int affectedRows = getJdbcTemplatePat().update(update, object.toArray());
        return affectedRows > 0 ? macroscopy : null;
    }
    
    default Macroscopy authorization(Macroscopy macroscopy) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String update = ""
                + "UPDATE pat25 "
                + "SET pat25c6 = ?, pat25c7 = ? ";
        
        List object = new ArrayList(0);      
        object.add(macroscopy.getDraft() == 1 ? 1 : 0);
        object.add(timestamp);

        update += " WHERE pat25c1 = ? ";
        object.add(macroscopy.getId());
        int affectedRows = getJdbcTemplatePat().update(update, object.toArray());
        return affectedRows > 0 ? macroscopy : null;
    }
    
    default List<Macroscopy> getPendingAuthorizations(Integer idUser) throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + " SELECT pat25c1, pat25.pat01c1, pat01c2, lab22c1, pat11c2, pat11c3, pat25.lab04c1a, pat25c3, pat25c4, pat25.lab04c1b, pat25c5  "
                    + " FROM pat25 "
                    + " LEFT JOIN pat01 ON pat01.pat01c1 = pat25.pat01c1 "
                    + " LEFT JOIN pat11 ON pat01.pat11c1 = pat11.pat11c1 "
                    + " WHERE pat25c4 = 0 AND pat25c6 = 1 AND pat01c4 = " + Constants.TRANSCRIPTION + " AND pat25.lab04c1c = ? "
                    ,(ResultSet rs, int i) ->
            {
                Macroscopy macroscopy = new Macroscopy();
                
                macroscopy.setId(rs.getInt("pat25c1"));
                macroscopy.setCasePat(rs.getInt("pat01c1"));
                macroscopy.setNumberCase(rs.getLong("pat01c2"));
                macroscopy.setNumberOrder(rs.getLong("lab22c1"));
                macroscopy.setStudyTypeCode(rs.getString("pat11c2"));
                macroscopy.setStudyTypeName(rs.getString("pat11c3"));
                macroscopy.getPathologist().setId(rs.getInt("lab04c1a"));
                macroscopy.setCreatedAt(rs.getTimestamp("pat25c3"));
                macroscopy.getTranscriber().setId(rs.getInt("lab04c1b"));
                macroscopy.setTranscribedAt(rs.getTimestamp("pat25c5"));
                
                return macroscopy;
            }, idUser);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
}

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
import net.cltech.enterprisent.domain.masters.pathology.SpecimenProtocol;
import net.cltech.enterprisent.domain.masters.pathology.Sheet;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios de el maestro de configuración de protocolos de especimenes de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @see 20/04/2021
 * @see Creaciòn
 */
public interface SpecimenProtocolDao 
{
    public JdbcTemplate getJdbcTemplatePat();
    
    default List<SpecimenProtocol> list() throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + " SELECT pat09c1, lab24c1, pat09.pat07c1, pat09.pat03c1, pat09c2, pat09c3, pat09.lab04c1a, pat09c4, pat09.lab04c1b, " 
                    + " pat07c2, pat07c3, pat07c4, pat03c2, pat03c3, pat03c4, pat03c5, pat09c5 "
                    + " FROM pat09 "
                    + " LEFT JOIN pat07 ON pat07.pat07c1 = pat09.pat07c1 "
                    + " LEFT JOIN pat03 ON pat03.pat03c1 = pat09.pat03c1 ", (ResultSet rs, int i) ->
            {
                
                SpecimenProtocol protocol = new SpecimenProtocol();
                protocol.setId(rs.getInt("pat09c1"));
                protocol.getSpecimen().setId(rs.getInt("lab24c1"));
                protocol.getOrgan().setId(rs.getInt("pat07c1"));
                protocol.getOrgan().setCode(rs.getString("pat07c2"));
                protocol.getOrgan().setName(rs.getString("pat07c3"));
                protocol.getOrgan().setStatus(rs.getInt("pat07c4"));
                protocol.getCasete().setId(rs.getInt("pat03c1"));
                protocol.getCasete().setCode(rs.getString("pat03c2"));
                protocol.getCasete().setName(rs.getString("pat03c3"));
                protocol.getCasete().setStatus(rs.getInt("pat03c4"));
                protocol.getCasete().setColour(rs.getString("pat03c5"));
                protocol.setQuantity(rs.getInt("pat09c2"));
                protocol.setProcessingHours(rs.getInt("pat09c5"));
                protocol.getUserCreated().setId(rs.getInt("lab04c1a"));
                protocol.getUserUpdated().setId(rs.getInt("lab04c1b"));
                protocol.setCreatedAt(rs.getTimestamp("pat09c3"));
                if (rs.getTimestamp("pat09c4") != null) {
                    protocol.setUpdatedAt(rs.getTimestamp("pat09c4"));
                }
                return protocol;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    default SpecimenProtocol create(SpecimenProtocol protocol) throws Exception {

        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat09")
                .usingColumns("lab24c1", "pat07c1", "pat03c1", "pat09c2", "pat09c3", "lab04c1a", "pat09c5")
                .usingGeneratedKeyColumns("pat09c1");

        HashMap parameters = new HashMap();
        parameters.put("lab24c1", protocol.getSpecimen().getId());
        parameters.put("pat07c1", protocol.getOrgan().getId());
        parameters.put("pat03c1", protocol.getCasete().getId());
        parameters.put("pat09c2", protocol.getQuantity());
        parameters.put("pat09c3", timestamp);
        parameters.put("lab04c1a", protocol.getUserCreated().getId());
        parameters.put("pat09c5", protocol.getProcessingHours());

        Number key = insert.executeAndReturnKey(parameters);
        protocol.setId(key.intValue());
        protocol.setCreatedAt(timestamp);
        createSheetsByProtocol(protocol);
        return protocol;
    }
   
    default SpecimenProtocol get(Integer id, Integer specimen) throws Exception
    {
        try
        {

            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat09c1, lab24c1, pat09.pat07c1, pat09.pat03c1, pat09c2, pat09c3, pat09.lab04c1a, pat09c4, pat09.lab04c1b, "
                    + " pat07c2, pat07c3, pat07c4, pat03c2, pat03c3, pat03c4, pat03c5, pat09c5 "
                    + " FROM pat09 "
                    + " LEFT JOIN pat07 ON pat07.pat07c1 = pat09.pat07c1 "
                    + " LEFT JOIN pat03 ON pat03.pat03c1 = pat09.pat03c1 ";
            
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE pat09c1 = ? ";
            }
            
            if (specimen != null)
            {
                query = query + "WHERE lab24c1 = ? ";
            }
            
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            
            if (specimen != null)
            {
                object = specimen;
            }

            return getJdbcTemplatePat().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                SpecimenProtocol protocol = new SpecimenProtocol();
                protocol.setId(rs.getInt("pat09c1"));
                protocol.getSpecimen().setId(rs.getInt("lab24c1"));
                protocol.getOrgan().setId(rs.getInt("pat07c1"));
                protocol.getOrgan().setCode(rs.getString("pat07c2"));
                protocol.getOrgan().setName(rs.getString("pat07c3"));
                protocol.getOrgan().setStatus(rs.getInt("pat07c4"));
                protocol.getCasete().setId(rs.getInt("pat03c1"));
                protocol.getCasete().setCode(rs.getString("pat03c2"));
                protocol.getCasete().setName(rs.getString("pat03c3"));
                protocol.getCasete().setStatus(rs.getInt("pat03c4"));
                protocol.getCasete().setColour(rs.getString("pat03c5"));
                protocol.setQuantity(rs.getInt("pat09c2"));
                protocol.setProcessingHours(rs.getInt("pat09c5"));
                protocol.getUserCreated().setId(rs.getInt("lab04c1a"));
                protocol.getUserUpdated().setId(rs.getInt("lab04c1b"));
                protocol.setCreatedAt(rs.getTimestamp("pat09c3"));
                if (rs.getTimestamp("pat09c4") != null) {
                    protocol.setUpdatedAt(rs.getTimestamp("pat09c4"));
                }
                return protocol;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    

    default SpecimenProtocol update(SpecimenProtocol protocol) throws Exception {

        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplatePat().update("UPDATE pat09 SET lab24c1 = ?, pat07c1 = ?, pat03c1 = ?, pat09c2 = ?, pat09c4 = ?, lab04c1b = ?, pat09c5 = ?  "
                + "WHERE pat09c1 = ?",
                protocol.getSpecimen().getId(), protocol.getOrgan().getId(), protocol.getCasete().getId(), protocol.getQuantity(), timestamp , protocol.getUserUpdated().getId(), protocol.getProcessingHours(), protocol.getId());
        protocol.setUpdatedAt(timestamp);
        createSheetsByProtocol(protocol);
        return protocol;
    }
    
    /**
     * Nuevo registro de laminas por protocolo en la base de datos.
     *
     * @param protocol Instancia con los datos del protocolo.
     *
     * @return {@link net.cltech.enterprisent.domain.masters.pathology.SpecimenProtocol}
     * @throws Exception Error en la base de datos.
     */
    default int createSheetsByProtocol(SpecimenProtocol protocol) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();

        deleteSheetsByProtocol(protocol.getId());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .usingGeneratedKeyColumns("pat08c1")
                .withTableName("pat08");
        protocol.getSheets().stream().map((sheet) -> {
            HashMap parameters = new HashMap();
            parameters.put("pat09c1", protocol.getId());
            parameters.put("pat06c1", sheet.getColoration().getId());
            parameters.put("pat08c2", sheet.getQuantity());
            return parameters;
        }).forEachOrdered((parameters) -> {
            batchArray.add(parameters);
        });

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[protocol.getSheets().size()]));

        return inserted.length;

    }
    
    /**
    * Obtiene laminas asociadas al protocolo
    *
    * @param id id del protocolo
    *
    * @return Lista de laminas
    * @throws Exception Error en la base de datos.
    */
    default List<Sheet> listSheetsByProtocol(Integer id) throws Exception
    {
        try
        {
            String query = "SELECT pat08c1, pat09c1, pat08.pat06c1, pat08c2, pat06c2, pat06c3, pat06c4 "
                    + " FROM pat08 "
                    + " INNER JOIN pat06 ON pat06.pat06c1 = pat08.pat06c1 "
                    + " WHERE pat09c1 = ? ";
            
            return getJdbcTemplatePat().query(query, (ResultSet rs, int i) ->
            {
                Sheet sheet = new Sheet();
                sheet.setId(rs.getInt("pat08c1"));
                sheet.getColoration().setId(rs.getInt("pat06c1"));
                sheet.getColoration().setCode(rs.getString("pat06c2"));
                sheet.getColoration().setName(rs.getString("pat06c3"));
                sheet.getColoration().setStatus(rs.getInt("pat06c4"));
                sheet.setQuantity(rs.getInt("pat08c2"));
                return sheet;
            }, id);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Elimina asignación de laminas de un protocolo
     *
     * @param id id del protocolo
     *
     * @return número de registros afectados
     * @throws Exception Error BD
     */
    default int deleteSheetsByProtocol(Integer id) throws Exception
    {
        String query = " DELETE FROM pat08 WHERE pat09c1 = ?";

        return getJdbcTemplatePat().update(query, id);
    }
}

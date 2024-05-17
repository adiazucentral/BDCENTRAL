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
import net.cltech.enterprisent.domain.masters.pathology.MacroscopyTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios del maestro para las plantillas de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @see 09/06/2021
 * @see Creaciòn
 */
public interface MacroscopyTemplateDao 
{
     public JdbcTemplate getJdbcTemplatePat();
    
    /**
    * Lista los campos asignados a un especimen
    *
    * @param id id muestra
    *
    * @return lista de campos
    * @throws Exception Error en base de datos
    */
    default List<Field> fields(int id) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT pat26.pat26c1, pat26c2, pat27.pat27c1, pat27c2, pat26c3, pat26c4, pat26c5, pat26c6, pat26c7, pat26.lab04c1a, pat26c8, pat26.lab04c1b  ")
                    .append("FROM pat26 ")
                    .append(" LEFT JOIN pat27 ON pat27.pat26c1 = pat26.pat26c1 ")
                    .append("WHERE lab24c1 = ").append(id);
            return getJdbcTemplatePat().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                Field field = new Field();
                field.setId(rs.getInt("pat26c1"));
                field.setName(rs.getString("pat26c2"));
                field.setType(rs.getInt("pat26c3"));
                field.setGrid(rs.getInt("pat26c4"));
                field.setRequired(rs.getInt("pat26c5"));
                field.setStatus(rs.getInt("pat26c6"));
                field.setTemplate(rs.getInt("pat27c1"));
                field.setOrder(rs.getInt("pat27c2"));
                field.getUserCreated().setId(rs.getInt("lab04c1a"));
                field.getUserUpdated().setId(rs.getInt("lab04c1b"));
                field.setCreatedAt(rs.getTimestamp("pat26c7"));
                if (rs.getTimestamp("pat26c8") != null) {
                    field.setUpdatedAt(rs.getTimestamp("pat26c8"));
                }
                return field;
            });
        }
        catch (Exception e)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
    * Elimina los campos de una plantilla de un especimen
    *
    * @param idSpecimen id del especimen
    *
    * @return registros afectados
    */
    default int deleteFields(int idSpecimen)
    {
        return getJdbcTemplatePat().update("DELETE FROM pat27 WHERE lab24c1 = ? ", idSpecimen);
    }
    
    /**
     * Inserta los campos de una plantilla de un especimen
     * @param template objeto con información de la relación
     *
     * @return numero de registros afectados
     * @throws Exception Error de base de datos
     */
    default int insertFields(MacroscopyTemplate template) throws Exception
    {
        final List<HashMap> batchArray = new ArrayList<>();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat27")
                .usingColumns("lab24c1", "pat26c1", "pat27c2", "pat27c3", "lab04c1a")
                .usingGeneratedKeyColumns("pat27c1");

        template.getFields().stream().map((field)->
        {
            HashMap parameters = new HashMap();
            
            parameters.put("lab24c1", template.getSpecimen().getId());
            parameters.put("pat26c1", field.getId());
            parameters.put("pat27c2", field.getOrder());
            parameters.put("pat27c3", timestamp);
            parameters.put("lab04c1a", template.getUserCreated().getId());
  
            return parameters;
        }).forEachOrdered((parameters)
                ->
        {
            batchArray.add(parameters);
        });

        return insert.executeBatch(batchArray.toArray(new HashMap[template.getFields().size()])).length;
    }
    
    /**
    * Lista los campos asignados a un caso por especimen
    *
    * @param idSpecimen id especimen
    * @param idCase id del caso
    *
    * @return lista de campos
    * @throws Exception Error en base de datos
    */
    default List<Field> fieldsByCase(int idSpecimen, int idCase) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT pat26.pat26c1, pat26c2, pat29.pat29c2, pat27.pat27c1, pat27c2, pat26c3, pat26c4, pat26c5,  ")
                    .append(" pat26c6, pat26c7, pat26.lab04c1a, pat26c8, pat26.lab04c1b ")
                    .append("FROM pat26 ")
                    .append(" LEFT JOIN pat27 ON pat27.pat26c1 = pat26.pat26c1 ")
                    .append(" LEFT JOIN pat29 ON pat27.pat27c1 = pat29.pat27c1 ")
                    .append(" AND EXISTS (SELECT 1 FROM pat25 WHERE pat29.pat25c1 = pat25.pat25c1 AND pat25.pat01c1 = ").append(idCase).append(" ) ")
                    .append(" WHERE lab24c1 = ").append(idSpecimen);
            return getJdbcTemplatePat().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                Field field = new Field();
                field.setId(rs.getInt("pat26c1"));
                field.setName(rs.getString("pat26c2"));
                field.setType(rs.getInt("pat26c3"));
                field.setGrid(rs.getInt("pat26c4"));
                field.setRequired(rs.getInt("pat26c5"));
                field.setStatus(rs.getInt("pat26c6"));
                field.setTemplate(rs.getInt("pat27c1"));
                field.setValue(rs.getString("pat29c2"));
                field.setOrder(rs.getInt("pat27c2"));
                field.getUserCreated().setId(rs.getInt("lab04c1a"));
                field.getUserUpdated().setId(rs.getInt("lab04c1b"));
                field.setCreatedAt(rs.getTimestamp("pat26c7"));
                if (rs.getTimestamp("pat26c8") != null) {
                    field.setUpdatedAt(rs.getTimestamp("pat26c8"));
                }
                return field;
            });
        }
        catch (Exception e)
        {
            return new ArrayList<>(0);
        }
    }
}

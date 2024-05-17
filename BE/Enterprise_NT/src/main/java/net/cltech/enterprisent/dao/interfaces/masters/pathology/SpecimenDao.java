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
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.masters.pathology.Study;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios de el maestro de especimenes de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 20/10/2020
 * @see Creaciòn
 */
public interface SpecimenDao {

    public JdbcTemplate getJdbcTemplate();
    public JdbcTemplate getJdbcTemplatePat();
    
    /**
    * Obtiene la lista de especimenes de patologia.
    *
    * @return Retorna la lista de especimenes de patologia
    * @throws Exception Error en la base de datos.
    */
    public List<Specimen> list() throws Exception;
    
    /**
    * Lista las muestras asignadas a un especimen
    *
    * @param id id muestra padre
    *
    * @return lista de submuestras
    * @throws Exception Error en base de datos
    */
    default List<Integer> listSubSample(int id) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT pat04c1, lab24c1, pat04c2, pat04c3, lab04c1a, pat04c4, lab04c1a ")
                    .append("FROM pat04 ")
                    .append("WHERE lab24c1 = ").append(id);
            return getJdbcTemplatePat().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("pat04c2");
            });
        }
        catch (Exception e)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
    * Elimina las submuestras de un especimen
    *
    * @param idSpecimen id del especimen
    *
    * @return registros afectados
    */
    default int deleteSubSamples(int idSpecimen)
    {
        return getJdbcTemplatePat().update("DELETE FROM pat04 WHERE lab24c1 = ? ", idSpecimen);
    }
    
    /**
     * Inserta las submuestras de un especimen
     * @param specimen muestra con información de la relación
     *
     * @return numero de registros afectados
     * @throws Exception Error de base de datos
     */
    default int insertSubSample(Specimen specimen) throws Exception
    {
        final List<HashMap> batchArray = new ArrayList<>();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat04")
                .usingColumns("lab24c1", "pat04c2", "pat04c3", "lab04c1a")
                .usingGeneratedKeyColumns("pat04c1");

        specimen.getSubSamples().stream().map((subSample)->
        {
            HashMap parameters = new HashMap();
            parameters.put("lab24c1", specimen.getId());
            parameters.put("pat04c2", subSample.getId());
            parameters.put("pat04c3", timestamp);
            parameters.put("lab04c1a", specimen.getUserCreated().getId());
  
            return parameters;
        }).forEachOrdered((parameters)
                ->
        {
            batchArray.add(parameters);
        });

        return insert.executeBatch(batchArray.toArray(new HashMap[specimen.getSubSamples().size()])).length;
    }
    
    /**
    * Lista los examenes con muestras de patologia desde la base de datos.
    *
    * @return Lista de examenes con muestras de patologia.
    * @throws Exception Error en la base de datos.
    */
    public List<Study> studies() throws Exception;
    
    /**
    * Obtiene el listado de estudios de una muestra de patologia
    * 
    * @return 
    * @throws Exception Error al el listado de estudios
    */
    default List<Integer> getStudiesBySample(Integer sample) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab39c1 ")
                    .append("FROM pat15 ")
                    .append("WHERE pat13c1 = ? ");
            
            return getJdbcTemplatePat().query(query.toString(),
                    new Object[]
                    {
                        sample
                    }, (ResultSet rs, int i)->
            {
                return rs.getInt("lab39c1");
            });
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
    * Obtiene la informacion de un estudio
    * 
    * @return 
    * @throws Exception Error al obtener la informacion del estudio
    */
    default Study getStudieById(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab39c1, lab39c2, lab39c3, lab39c4, lab39.lab24c1, lab24c2 "
                    + " FROM lab39 "
                    + " LEFT JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 ";
            
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab39c1 = ? ";
            }

            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            
            return getJdbcTemplate().queryForObject(query, new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Study study = new Study();
                study.setId(rs.getInt("lab39c1"));
                study.setCode(rs.getString("lab39c2"));
                study.setName(rs.getString("lab39c4"));
                study.setAbbr(rs.getString("lab39c3"));
                study.setSample(rs.getInt("lab24c1"));
                study.setSampleName(rs.getString("lab24c2"));
                return study;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
}

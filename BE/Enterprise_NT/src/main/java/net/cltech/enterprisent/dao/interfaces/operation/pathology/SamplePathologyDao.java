/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.pathology;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.masters.pathology.Study;
import net.cltech.enterprisent.domain.operation.pathology.SamplePathology;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las muestras de los casos de patologia.
 *
 * @version 1.0.0
 * @author omendez
 * @since 12/04/2021
 * @see Creación
 */
public interface SamplePathologyDao 
{
    
    public JdbcTemplate getJdbcTemplatePat();
    
    /**
    * Obtiene las muestras de un caso con su contenido
    *
    * @param casePat Caso.
    * @return Lista de {@link net.cltech.enterprisent.domain.masters.pathology.Specimen}
    * @throws Exception Error en base de datos
    */
    default List<Specimen> getSpecimensByCase(Integer casePat) throws Exception
    {
        try
        {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT pat13c1, pat13.lab24c1, pat13.pat04c1 as subSample, pat13.pat07c1, pat07c2, pat07c3, pat07c4, pat13.pat04c1 " +
                    " FROM pat13 " +
                    " JOIN pat07 ON pat13.pat07c1 = pat07.pat07c1 " +
                    " LEFT JOIN pat04 ON pat13.pat04c1 = pat04.pat04c1 " +
                    " WHERE pat01c1 = ? ";
            return getJdbcTemplatePat().query(query, (ResultSet rs, int i)
            ->
            {
                Specimen specimen = new Specimen();
                specimen.setSample(rs.getInt("pat13c1"));
                specimen.setId(rs.getInt("lab24c1"));
                specimen.getOrgan().setId(rs.getInt("pat07c1"));
                specimen.getOrgan().setCode(rs.getString("pat07c2"));
                specimen.getOrgan().setName(rs.getString("pat07c3"));
                specimen.getOrgan().setId(rs.getInt("pat07c4"));
                specimen.setSubSample(rs.getInt("subSample"));
                
                return specimen;
            }, casePat);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList(0);
        }
    }
    
    /**
    * Obtiene el contenido de una muestra de patologia
    *
    * @param specimen Id Especimen.
    * @return Lista de { @link net.cltech.enterprisent.domain.operation.pathology.SamplePathology }
    * @throws Exception Error en base de datos
    */
    default List<SamplePathology> getSampleContent(Integer specimen) throws Exception
    {
        try
        {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT pat14c1, pat14c2, pat05.pat05c1, pat05c2, pat05c3, pat05c4, pat05c7,  pat14.pat10c1, pat10c2, pat10c3, pat10c4 " +
                    " FROM pat14 " +
                    " JOIN pat05 ON pat05.pat05c1 = pat14.pat05c1 " +
                    " JOIN pat10 ON pat10.pat10c1 = pat14.pat10c1 " +
                    " WHERE pat13c1 = ? ";
            return getJdbcTemplatePat().query(query, (ResultSet rs, int i)
                    ->
            {
                SamplePathology sample = new SamplePathology();
                
                sample.setId(rs.getInt("pat14c1"));
                sample.setQuantity(rs.getInt("pat14c2"));
                
                /*Contenedor*/
                sample.getContainer().setId(rs.getInt("pat05c1"));
                sample.getContainer().setName(rs.getString("pat05c2"));
                sample.getContainer().setStatus(rs.getInt("pat05c4"));                 
                String Imabas64 = "";
                byte[] ImaBytes = rs.getBytes("pat05c3");
                if (ImaBytes != null)
                {
                    Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
                }
                sample.getContainer().setImage(Imabas64);
                
                sample.getContainer().setPrint(rs.getInt("pat05c7"));
                
                /*Fijador*/
                sample.getFixative().setId(rs.getInt("pat10c1"));
                sample.getFixative().setCode(rs.getString("pat10c2"));
                sample.getFixative().setName(rs.getString("pat10c3"));
                sample.getFixative().setStatus(rs.getInt("pat10c4"));
                                
                return sample;
            }, specimen);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList(0);
        }
    }
   
    /**
    * Guarda las muestras de los casos
    *
    * @param casePat Id del caso
    * @param specimens Lista de {@link net.cltech.enterprisent.domain.masters.pathology.Specimen}
    *
    * @throws Exception Error en base de datos
    */
    default void saveSpecimens(Integer casePat, List<Specimen> specimens) throws Exception
    {
        deleteSamples(casePat);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat13")
                .usingGeneratedKeyColumns("pat13c1");
        
        HashMap<String, Object> parameters = new HashMap<>(0);
        
        for(Specimen specimen : specimens) {
            parameters = new HashMap<>(0);
            parameters.put("pat01c1", casePat);
            parameters.put("lab24c1", specimen.getId());
            parameters.put("pat04c1", specimen.getSubSample());
            parameters.put("pat07c1", specimen.getOrgan().getId());
            Number id = insert.executeAndReturnKey(parameters);
            saveContentSamples(id.intValue(), specimen.getSamples());
            saveStudies(id.intValue(), specimen.getStudies());
        }
    }
    
    /**
    * Guarda el contenido de las muestras de un caso de patologia en el sistema
    *
    * @param specimen Id del especimen
    * @param samples Lista de
    * {@link net.cltech.enterprisent.domain.operation.pathology.SampleCase}
    *
    * @throws Exception Error en base de datos
    */
    default void saveContentSamples(Integer specimen, List<SamplePathology> samples) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat14")
                .usingGeneratedKeyColumns("pat14c1");
        
        HashMap<String, Object> parameters = new HashMap<>(0);
        
        for(SamplePathology content : samples) {
            parameters = new HashMap<>(0);
            parameters.put("pat14c2", content.getQuantity());
            parameters.put("pat05c1", content.getContainer().getId());
            parameters.put("pat10c1", content.getFixative().getId());
            parameters.put("pat13c1", specimen);
            Number id = insert.executeAndReturnKey(parameters);
        }
    }
    
    /**
    * Guarda los estudios de las muestras de un caso de patologia en el sistema
    *
    * @param specimen Id del especimen
    * @param studies Lista de estudios
    *
    * @throws Exception Error en base de datos
    */
    default void saveStudies(Integer specimen, List<Study> studies) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat15");
        
        List<HashMap> batchArray = new ArrayList<>();
        for(Study study : studies) {
            HashMap parameters = new HashMap();
            parameters.put("pat13c1", specimen);
            parameters.put("lab39c1", study.getId());
            batchArray.add(parameters);
        }
        insert.executeBatch(batchArray.toArray(new HashMap[batchArray.size()]));
    }
    
    /**
    * Elimina las muetras de un caso
    *
    * @param casePat id del caso
    *
    * @return número de registros afectados
    * @throws Exception Error BD
    */
    default int deleteSamples(Integer casePat) throws Exception
    {
        String query = " DELETE FROM pat13 WHERE pat01c1 = ?";
        return getJdbcTemplatePat().update(query, casePat);
    }
    
    /**
    * Elimina los estudios de una lista de muestras
    *
    * @param samples ids
    *
    * @return número de registros afectados
    * @throws Exception Error BD
    */
    default int deleteSampleStudies(String samples) throws Exception
    {
        StringBuilder query = new StringBuilder();
        query.append(" DELETE FROM pat15").append(" WHERE pat13c1 IN (").append(samples).append(")");
        return getJdbcTemplatePat().update(query.toString());
    }
    
    /**
    * Elimina las muetras de un caso
    *
    * @param samples ids
    *
    * @return número de registros afectados
    * @throws Exception Error BD
    */
    default int deleteContentSamples(String samples) throws Exception
    {
        StringBuilder query = new StringBuilder();
        query.append(" DELETE FROM pat14").append(" WHERE pat13c1 IN (").append(samples).append(")");
        return getJdbcTemplatePat().update(query.toString());
    }
}

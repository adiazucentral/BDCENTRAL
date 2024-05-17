/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.demographic;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.DemographicTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información del maestro de examenes por demograficos
 *
 * @version 1.0.0
 * @author omendez
 * @since 31/01/2022
 * @see Creación
 */
public interface DemographicTestDao {
    
    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
    
    /**
    * Lista las relaciones desde la base de datos.
    *
    * @return Lista de relaciones.
    * @throws Exception Error en la base de datos.
    */
    default List<DemographicTest> list() throws Exception
    {
        try
        {
            String sql = "SELECT lab600c1, lab600c2, lab600c3, lab600c4, lab600c5, lab600c6, lab600c7, lab600c8, lab600.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + " FROM lab600 "
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab600.lab04c1";
            return getJdbcTemplate().query(sql, (ResultSet rs, int i) ->
            {
                DemographicTest demographic = new DemographicTest();
                
                demographic.setId(rs.getInt("lab600c1"));
                demographic.setIdDemographic1(rs.getInt("lab600c2"));
                demographic.setValueDemographic1(rs.getInt("lab600c3"));
                demographic.setIdDemographic2(rs.getInt("lab600c4"));
                demographic.setValueDemographic2(rs.getInt("lab600c5"));
                demographic.setIdDemographic3(rs.getInt("lab600c6"));
                demographic.setValueDemographic3(rs.getInt("lab600c7"));
                demographic.setTests(listTests(rs.getInt("lab600c1")));
                
                demographic.getUser().setId(rs.getInt("lab04c1"));
                demographic.getUser().setName(rs.getString("lab04c2"));
                demographic.getUser().setLastName(rs.getString("lab04c3"));
                demographic.getUser().setUserName(rs.getString("lab04c4"));
  
                demographic.setLastTransaction(rs.getTimestamp("lab600c8"));

                return demographic;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }
    
    /**
    * Obtiene pruebas asociadas a los demograficos
    *
    * @param id id de la relacion
    *
    * @return Lista de pruebas
    */
    default List<Integer> listTests(Integer id)
    {
        try
        {
            String query = "SELECT lab39c1"
                    + " FROM lab601 "
                    + " WHERE lab600c1 = ? ";

            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                return rs.getInt("lab39c1");
            }, id);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }
    
    /**
    * Nuevo registro en la base de datos.
    *
    * @param demographic Instancia con los datos de los demographics
    *
    * @return {@link net.cltech.enterprisent.domain.masters.demographic.DemographicTest;}
    * @throws Exception Error en la base de datos.
    */
    default DemographicTest create(DemographicTest demographic) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab600")
                .usingColumns("lab600c2", "lab600c3", "lab600c4", "lab600c5", "lab600c6", "lab600c7", "lab600c8", "lab04c1")
                .usingGeneratedKeyColumns("lab600c1");

        Date createdAt = new Date();
        HashMap params = new HashMap();
        params.put("lab600c2", demographic.getIdDemographic1());
        params.put("lab600c3", demographic.getValueDemographic1());
        params.put("lab600c4", demographic.getIdDemographic2());
        params.put("lab600c5", demographic.getValueDemographic2());
        params.put("lab600c6", demographic.getIdDemographic3());
        params.put("lab600c7", demographic.getValueDemographic3());
        params.put("lab600c8", new Timestamp(createdAt.getTime()));
        params.put("lab04c1", demographic.getUser().getId());

        Number key = insert.executeAndReturnKey(params);

        demographic.setId(key.intValue());
        demographic.setLastTransaction(createdAt);
        createTestsByDemos(demographic);

        return demographic;
    }
    
    /**
    * Actualiza la información en la base de datos.
    *
    *
    * @param demographics Instancia con los datos de la relación
    *
    * @return Relactipon actualizada
    *
    * @throws Exception Error en la base de datos.
    */
    default DemographicTest update(DemographicTest demographics) throws Exception
    {
        Date modificationDate = new Date();
        getJdbcTemplate().update(" UPDATE lab600 \n"
                + " SET lab600c2 = ?, lab600c3 = ?, lab600c4 = ?, lab600c5 = ?, lab600c6 = ?, lab600c7 = ?, lab600c8 = ?, lab04c1 = ?  \n"
                + " WHERE lab600c1 = ? ",
                demographics.getIdDemographic1(), demographics.getValueDemographic1(), demographics.getIdDemographic2(),
                demographics.getValueDemographic2(), demographics.getIdDemographic3(), demographics.getValueDemographic3(),
                new Timestamp(modificationDate.getTime()),
                demographics.getUser().getId(), demographics.getId());
        demographics.setLastTransaction(modificationDate);
        createTestsByDemos(demographics);
        return demographics;
    }
    
    /**
     * Nuevo registro de examenes por demograficos en la base de datos.
     *
     * @param demographics Instancia con los datos de los demograficos.
     *
     * @return {@link net.cltech.enterprisent.domain.masters.demographic.DemographicTest;}
     * @throws Exception Error en la base de datos.
     */
    default int createTestsByDemos(DemographicTest demographics) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();

        deleteTestsByDemos(demographics.getId());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab601");
        
        demographics.getTests().forEach( test -> {
            HashMap parameters = new HashMap();
            parameters.put("lab600c1", demographics.getId());
            parameters.put("lab39c1", test);
            batchArray.add(parameters);
        });
        
        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[demographics.getTests().size()]));
        
        return inserted.length;
    }
    
    /**
     * Elimina asignación de pruebas de los demograficos
     *
     * @param idRelation id de la relacion
     *
     * @return número de registros afectados
     * @throws Exception Error BD
     */
    default int deleteTestsByDemos(Integer idRelation) throws Exception
    {
        String query = " DELETE FROM lab601 WHERE lab600c1 = ?";
        return getJdbcTemplate().update(query, idRelation);
    }
    
}

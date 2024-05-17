/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.pathology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.masters.pathology.Organ;
import net.cltech.enterprisent.domain.masters.pathology.Pathologist;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios de el maestro de patologos
 *
 * @version 1.0.0
 * @author omendez
 * @see 16/04/2021
 * @see Creaciòn
 */
public interface PathologistDao 
{
    public JdbcTemplate getJdbcTemplatePat();
    public JdbcTemplate getJdbcTemplate();
    
    default List<User> listPathologists() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab04.lab04c1, lab04c2, lab04c3, lab04c4, lab04c10, lab04c11, lab04c16 "
                    + " FROM lab04 "
                    + " JOIN lab84 ON lab04.lab04c1 = lab84.lab04c1 "
                    + " JOIN lab82 on lab82.lab82c1 = lab84.lab82c1  "
                    + " WHERE lab82c2 = 'Patólogo';", (ResultSet rs, int i) ->
            {
                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                user.setIdentification(rs.getString("lab04c10"));
                user.setEmail(rs.getString("lab04c11"));
                /*Foto*/
                String photo64 = "";
                byte[] photoBytes = rs.getBytes("lab04c16");
                if (photoBytes != null)
                {
                    photo64 = Base64.getEncoder().encodeToString(photoBytes);
                }
                user.setPhoto(photo64);
                
                return user;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    default List<Organ> listOrgansByPathologists(Integer idPathologist) throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + " SELECT pat19c1, pat19.lab04c1a, pat19.pat07c1, pat19c2, pat19.lab04c1b, pat19c3, pat19.lab04c1c, pat07c2, pat07c3, pat07c4 "
                    + " FROM pat19 "
                    + " JOIN pat07 ON pat07.pat07c1 = pat19.pat07c1 "
                    + " WHERE pat19.lab04c1a = ? ",
                    new Object[]
                    {
                        idPathologist
                    },(ResultSet rs, int i) ->
            {
                Organ organ = new Organ();
                organ.setId(rs.getInt("pat07c1"));
                organ.setCode(rs.getString("pat07c2"));
                organ.setName(rs.getString("pat07c3"));
                organ.setStatus(rs.getInt("pat07c4"));
                organ.getUserCreated().setId(rs.getInt("lab04c1b"));
                organ.getUserUpdated().setId(rs.getInt("lab04c1c"));
                organ.setCreatedAt(rs.getTimestamp("pat19c2"));
                if (rs.getTimestamp("pat19c3") != null) {
                    organ.setUpdatedAt(rs.getTimestamp("pat19c3"));
                }
                return organ;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Obtenemos la cantidad de casos asignados al patologo
    *
    * @param idPathologist
    *
    * @return Cantidad de casos asignados
    * @throws java.lang.Exception
    */
    default Integer getAssignedCases(Integer idPathologist) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT count(*) AS quantity FROM pat01  ")
                    .append("WHERE lab04c1c = ").append(idPathologist)
                    .append(" GROUP BY lab04c1c");
            
            return getJdbcTemplatePat().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("quantity");
            });
        }
        catch (Exception e)
        {
            return 0;
        }
    }
    
     /**
    * Elimina los organos de un patologo
    *
    * @param idPathologist id del patologo
    *
    * @return registros afectados
    */
    default int deleteOrgans(int idPathologist)
    {
        return getJdbcTemplatePat().update("DELETE FROM pat19 WHERE lab04c1a = ? ", idPathologist);
    }
    
    /**
     * Inserta los organos de un patologo
     * @param pathologist muestra con información de la relación
     *
     * @return numero de registros afectados
     * @throws Exception Error de base de datos
     */
    default int insertOrgans(Pathologist pathologist) throws Exception
    {
        final List<HashMap> batchArray = new ArrayList<>();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat19")
                .usingColumns("lab04c1a", "pat07c1", "pat19c2", "lab04c1b")
                .usingGeneratedKeyColumns("pat19c1");

        pathologist.getOrgans().stream().map((organ)->
        {
            HashMap parameters = new HashMap();
            parameters.put("lab04c1a", pathologist.getPathologist().getId());
            parameters.put("pat07c1", organ.getId());
            parameters.put("pat19c2", timestamp);
            parameters.put("lab04c1b", pathologist.getUserCreated().getId());
  
            return parameters;
        }).forEachOrdered((parameters)
                ->
        {
            batchArray.add(parameters);
        });

        return insert.executeBatch(batchArray.toArray(new HashMap[pathologist.getOrgans().size()])).length;
    }
}

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
import javax.xml.bind.DatatypeConverter;
import net.cltech.enterprisent.domain.masters.pathology.ContainerPathology;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios de el maestro de contenedores de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 19/10/2020
 * @see Creaciòn
 */
public interface ContainerPathologyDao {
    
    public JdbcTemplate getJdbcTemplatePat();
    
    default List<ContainerPathology> list() throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + "SELECT pat05c1, pat05c2, pat05c3, pat05c4, pat05c5, lab04c1a, pat05c6, lab04c1b, pat05c7 "
                    + "FROM pat05 ", (ResultSet rs, int i) ->
            {
                ContainerPathology container = new ContainerPathology();
                container.setId(rs.getInt("pat05c1"));
                container.setName(rs.getString("pat05c2"));
                String Imabas64 = "";
                byte[] ImaBytes = rs.getBytes("pat05c3");
                if (ImaBytes != null) {
                    Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
                }
                container.setImage(Imabas64);
                container.setStatus(rs.getInt("pat05c4"));
                container.getUserCreated().setId(rs.getInt("lab04c1a"));
                container.getUserUpdated().setId(rs.getInt("lab04c1b"));
                container.setCreatedAt(rs.getTimestamp("pat05c5"));
                if (rs.getTimestamp("pat05c6") != null) {
                    container.setUpdatedAt(rs.getTimestamp("pat05c6"));
                }
                
                container.setPrint(rs.getInt("pat05c7"));
                
                return container;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    default ContainerPathology create(ContainerPathology container) 
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat05")
                .usingColumns("pat05c2", "pat05c3", "pat05c4", "pat05c5", "lab04c1a", "pat05c7")
                .usingGeneratedKeyColumns("pat05c1");

        HashMap parameters = new HashMap();
        parameters.put("pat05c2", container.getName().trim());
        byte[] imageByte = null;
        if(container.getImage() != null ) {
            imageByte = DatatypeConverter.parseBase64Binary(container.getImage());
        }
        parameters.put("pat05c3", imageByte);
        parameters.put("pat05c4", container.getStatus());
        parameters.put("pat05c5", timestamp);
        parameters.put("lab04c1a", container.getUserCreated().getId());
        parameters.put("pat05c7", container.getPrint());

        Number key = insert.executeAndReturnKey(parameters);
        container.setId(key.intValue());
        container.setCreatedAt(timestamp);
        return container;
    }
   
    default ContainerPathology get(Integer id, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat05c1, pat05c2, pat05c3, pat05c4, pat05c5, lab04c1a, pat05c6, lab04c1b, pat05c7 "
                    + "FROM pat05 ";
            
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE pat05c1 = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(pat05c2) = ? ";
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

            return getJdbcTemplatePat().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                ContainerPathology container = new ContainerPathology();
                container.setId(rs.getInt("pat05c1"));
                container.setName(rs.getString("pat05c2"));
                String Imabas64 = "";
                byte[] ImaBytes = rs.getBytes("pat05c3");
                if (ImaBytes != null) {
                    Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
                }
                container.setImage(Imabas64);
                container.setStatus(rs.getInt("pat05c4"));
                container.getUserCreated().setId(rs.getInt("lab04c1a"));
                container.getUserUpdated().setId(rs.getInt("lab04c1b"));
                container.setCreatedAt(rs.getTimestamp("pat05c5"));
                if (rs.getTimestamp("pat05c6") != null) {
                    container.setUpdatedAt(rs.getTimestamp("pat05c6"));
                }
                
                container.setPrint(rs.getInt("pat05c7"));
             
                return container;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    default ContainerPathology update(ContainerPathology container) throws Exception 
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        byte[] imageByte = null;
        if(container.getImage() != null ) {
            imageByte = DatatypeConverter.parseBase64Binary(container.getImage());
        }
        getJdbcTemplatePat().update("UPDATE pat05 SET pat05c2 = ?, pat05c3 = ?, pat05c4 = ?, pat05c6 = ?, lab04c1b = ?, pat05c7 = ? "
                + "WHERE pat05c1 = ?",
                container.getName(), imageByte, container.getStatus(), timestamp, container.getUserUpdated().getId(), container.getPrint() ,container.getId() );

        container.setUpdatedAt(timestamp);
        return container;
    }
    
}

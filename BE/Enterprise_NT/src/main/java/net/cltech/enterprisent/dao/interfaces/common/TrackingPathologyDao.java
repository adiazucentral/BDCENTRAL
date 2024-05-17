/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.common;

import java.sql.ResultSet;
import java.util.Base64;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.user.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los métodos de acceso a base de datos para la información de auditoria para patologia.
 *
 * @version 1.0.0
 * @author omendez
 * @since 01/03/2021
 * @see Creación
 */
public interface TrackingPathologyDao 
{
    public JdbcTemplate getJdbcTemplate();
    
    default AuthorizedUser get(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab04c1, lab04c2, lab04c3, lab04c4 FROM lab04 WHERE lab04c1 = ?";
            
            return getJdbcTemplate().queryForObject(query, 
                new Object[]{
                    id
                }, (ResultSet rs, int i) ->
            {
                
                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                return user;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
    default User getUser(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab04.lab04c1, lab04c2, lab04c3, lab04c4, lab04c10, lab04c11, lab04c16 "
                    + " FROM lab04 "
                    + "WHERE lab04.lab04c1 = ?";
            
            return getJdbcTemplate().queryForObject(query, 
                new Object[]{
                    id
                }, (ResultSet rs, int i) ->
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
            return null;
        }
    }
}

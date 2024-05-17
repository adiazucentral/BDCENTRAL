/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.dao.interfaces.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author omendez
 */
public interface ToolsDao {
    
    /**
    * Valida si una tabla existe en la base de datos
    *
     * @param connection Conexion a la base de datos
    * @param name Nombre de la tabla 
     * @return  
    * @throws SQLException Error en base de datos
    */
    default boolean tableExists(JdbcTemplate connection, String name) throws SQLException 
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT count(*) as count FROM information_schema.tables WHERE table_name =  '").append(name).append("'");
           
            return connection.queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("count") != 0;
            });
        }
        catch (DataAccessException e)
        {
            return false;
        }
    }
    
}

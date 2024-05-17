/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.dao.interfaces.masters.configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.cltech.outreach.domain.masters.configuration.UserType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Representa los metodos de acceso a base de datos para la informacion de los
 * tipos de usuar io de la consulta web
 *
 * @version 1.0.0
 * @author cmartin
 * @since 23/04/2018
 * @see Creación
 */
public interface UserTypeDao
{

    /**
     * Obtiene la conexion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista los tipos de usuario para la consulta web
     *
     * @return Lista de tipos de usuario
     */
    default List<UserType> list()
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab88c1, lab88c2, lab88c3, lab88c4, lab88c5, lab88c6, lab88c7 "
                    + "FROM lab88",
                    new RowMapper<UserType>()
            {
                @Override
                public UserType mapRow(ResultSet rs, int i) throws SQLException
                {
                    UserType userType = new UserType();
                    userType.setType(rs.getInt("lab88c1"));
                    userType.setMessage(rs.getString("lab88c2"));
                    userType.setQuantityOrder(rs.getInt("lab88c3"));
                    userType.setImage(rs.getString("lab88c4"));
                    userType.setVisible(rs.getInt("lab88c5") == 1);
                    userType.setConfidential(rs.getInt("lab88c6") == 1);
                    userType.setOnlyValidated(rs.getInt("lab88c7") == 1);
                    return userType;
                }
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Actualiza la información de los tipos de usuario
     * 
     * @param userTypes Tipos de usuario a ser actualizados
     * @return Cantidad de registros afectados
     */
    default int update(List<UserType> userTypes)
    {
        List<Object[]> parameters = new ArrayList<Object[]>();
        
        String query = "UPDATE lab88 SET lab88c2 = ?, lab88c3 = ?, lab88c4 = ?, lab88c5 = ?, lab88c6 = ? , lab88c7 = ?  WHERE lab88c1 = ?";
        for (UserType userType : userTypes)
        {
            parameters.add(new Object[]
            {
                userType.getMessage(),
                userType.getQuantityOrder(),
                userType.getImage(),
                userType.isVisible() ? 1 : 0,
                userType.isConfidential()? 1 : 0,
                userType.isOnlyValidated() ? 1 : 0,
                userType.getType()
            });
        }

        int[] update = getJdbcTemplate().batchUpdate(query, parameters);
        return update.length;
    }
}

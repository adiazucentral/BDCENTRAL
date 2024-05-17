/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.dao.interfaces.security;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.securitynt.domain.common.AuthenticationSession;
import net.cltech.securitynt.domain.masters.user.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de
 * sesiones activas.
 *
 * @version 1.0.0
 * @author equijano
 * @since 30/11/2018
 * @see Creación
 */
public interface SessionDao
{

    /**
     * Obtiene la conexion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Obtiene las sesiones activas.
     *
     * @return Lista de comentarios.
     */
    default List<AuthenticationSession> list()
    {
        try
        {

            String query = ""
                    + "SELECT lab100c1, lab100c2, lab100c3, "
                    + "lab04.lab04c1, lab04.lab04c2, lab04.lab04c3, lab04.lab04c4, "
                    + "lab05c1 "
                    + "FROM lab100 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab100.lab04c1 ";

            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                AuthenticationSession authenticationSession = new AuthenticationSession();
                authenticationSession.setIdSession(rs.getString("lab100c1"));
                authenticationSession.setIp(rs.getString("lab100c3"));
                authenticationSession.setBranch(rs.getInt("lab05c1"));
                /*Usuario*/
                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                authenticationSession.setUser(user);

                authenticationSession.setDateRegister(rs.getTimestamp("lab100c2"));
                return authenticationSession;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Registra una sesion activa
     *
     * @param authenticationSession
     * @return Registros afectados
     * @throws Exception Error en la base de datos.
     */
    default AuthenticationSession create(AuthenticationSession authenticationSession) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("lab100");

        HashMap parameters = new HashMap();
        parameters.put("lab100c1", authenticationSession.getIdSession()); //Orden o paciente
        parameters.put("lab100c2", timestamp); //Comentario
        parameters.put("lab100c3", authenticationSession.getIp()); //Tipo: 1 -> Orden, 2 -> Paciente        
        parameters.put("lab04c1", authenticationSession.getUser().getId());
        parameters.put("lab05c1", authenticationSession.getBranch());

        insert.execute(parameters);
        authenticationSession.setDateRegister(timestamp);
        return authenticationSession;
    }

    /**
     * Elimina una sesion activa
     *
     * @param idSession
     * @return
     * @throws Exception Error en la base de datos.
     */
    default int deleteBySession(String idSession) throws Exception
    {
        String deleteSql = "DELETE FROM lab100 WHERE lab100c1 = ?; ";
        return getJdbcTemplate().update(deleteSql, idSession);
    }

    /**
     * Elimina todas las sesiones activas
     *
     * @throws Exception Error en la base de datos.
     */
    default void deleteAll() throws Exception
    {
        String deleteSql = "DELETE FROM lab100";
        getJdbcTemplate().update(deleteSql);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.dao.interfaces.security;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import net.cltech.outreach.domain.common.AuthorizedUser;
import net.cltech.outreach.tools.Constants;
import net.cltech.outreach.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los metodos de acceso a base de datos para el inicio de sesion de
 * la consulta web
 *
 * @version 1.0.0
 * @author cmartin
 * @since 03/05/2018
 * @see Creación
 */
public interface AuthenticationDao {

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Realiza proceso de autenticacion para los medicos.
     *
     * @param username Usuario
     * @return Información del usuario autenticado
     * @throws Exception Error en base de datos
     */
    default AuthorizedUser authenticationPhysician(String username) throws Exception {
        try {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT lab19c1, lab19c2, lab19c3, lab19c17, lab19c18, lab19c19, lab19c21 "
                    + "FROM lab19 "
                    + "WHERE lab19c17 = ?",
                    new Object[]{
                        username
                    }, (ResultSet rs, int i)
                    -> {
                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab19c1"));
                user.setIdentification(Tools.decrypt(rs.getString("lab19c19")));
                user.setName(rs.getString("lab19c2"));
                user.setLastName(rs.getString("lab19c3"));
                user.setUserName(rs.getString("lab19c17"));
                user.setPassword(rs.getString("lab19c18"));
                user.setEmail(rs.getString("lab19c21"));
                user.setType(Constants.PHYSICIAN);
                return user;
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }
    
           /**
     * Realiza proceso de autenticacion para los clientes.
     *
     * @param username Usuario
     * @return Información del usuario autenticado
     * @throws Exception Error en base de datos
     */
    default AuthorizedUser authenticationRate(String username) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab904c1, ")
                    .append("lab904c2, ")
                    .append("lab904c3, ")
                    .append("lab904c9, ")
                    .append("lab07c1, ")
                    .append("lab904c39 ")
                    .append("FROM lab904 ")
                    .append("WHERE lab904c9 = '").append(username).append("'");
            
            return getJdbcTemplate().queryForObject(query.toString(), 
                    (ResultSet rs, int i) ->
            {
                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab904c1"));
                user.setIdentification(Tools.decrypt(rs.getString("lab904c2")));
                user.setName(rs.getString("lab904c3"));
                user.setUserName(rs.getString("lab904c9"));
                user.setPassword(rs.getString("lab904c39"));
                user.setEmail(rs.getString("lab904c9"));
                user.setType(Constants.RATE);
                return user;
            });
        } catch (Exception ex)
        {
            return null;
        }
    }
    

    /**
     * Realiza proceso de autenticacion para los denograficos.
     *
     * @param username Usuario
     * @return Información del usuario autenticado
     * @throws Exception Error en base de datos
     */
    default AuthorizedUser authenticationDemographic(String username) throws Exception {
        try {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT lab181c1, lab181c2, lab181c3, lab181c13 "
                    + "FROM lab181 "
                    + "WHERE lab181c2 = ?",
                    new Object[]{
                        username
                    }, (ResultSet rs, int i)
                    -> {
                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab181c1"));
                user.setIdentification(null);
                user.setName(null);
                user.setLastName(null);
                user.setUserName(rs.getString("lab181c2"));
                user.setPassword(rs.getString("lab181c3"));
                user.setEmail(rs.getString("lab181c13"));
                user.setType(Constants.DEMOGRAPHIC);
                return user;
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Realiza proceso de autenticacion para los pacientes.
     *
     * @param username Usuario
     * @param historyType
     * @return Información del usuario autenticado
     * @throws Exception Error en base de datos
     */
    default AuthorizedUser authenticationPatient(String username, int historyType) throws Exception {
        try {
            String where = "";
            if (historyType != -1) {
                where = " AND lab54c1 = " + historyType;
            }
            return getJdbcTemplate().queryForObject(""
                    + "SELECT lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21c8, lab21c18, lab21c19 "
                    + "FROM lab21 "
                    + "WHERE lab21c18 = ?" + where,
                    new Object[]{
                        username
                    }, (ResultSet rs, int i)
                    -> {
                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab21c1"));
                user.setIdentification(Tools.decrypt(rs.getString("lab21c2")));
                user.setName(Tools.decrypt(rs.getString("lab21c3")) + " " + Tools.decrypt(rs.getString("lab21c4")));
                user.setLastName(Tools.decrypt(rs.getString("lab21c5")) + " " + Tools.decrypt(rs.getString("lab21c6")));
                user.setUserName(Tools.decrypt(rs.getString("lab21c18")));
                user.setPassword(rs.getString("lab21c19"));
                user.setEmail(rs.getString("lab21c8"));
                user.setType(Constants.PATIENT);
                return user;
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Realiza proceso de autenticacion para los clientes.
     *
     * @param username Usuario
     * @return Información del usuario autenticado
     * @throws Exception Error en base de datos
     */
    default AuthorizedUser authenticationAccount(String username) throws Exception {
        try {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT lab14c1, lab14c2, lab14c3, lab14c21, lab14c24, lab14c25 "
                    + "FROM lab14 "
                    + "WHERE lab14c24 = ?",
                    new Object[]{
                        username
                    }, (ResultSet rs, int i)
                    -> {
                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab14c1"));
                user.setIdentification(Tools.decrypt(rs.getString("lab14c2")));
                user.setName(rs.getString("lab14c3"));
                user.setUserName(rs.getString("lab14c24"));
                user.setPassword(rs.getString("lab14c25"));
                user.setEmail(rs.getString("lab14c21"));
                user.setType(Constants.ACCOUNT);
                return user;
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Realiza proceso de autenticacion para los medicos.
     *
     * @param username Usuario
     * @return Información del usuario autenticado
     * @throws Exception Error en base de datos
     */
    default AuthorizedUser authenticationUserLIS(String username) throws Exception {
        try {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT lab04c1, lab04c2, lab04c3, lab04c4, lab04c5, lab04c10, lab04c17 "
                    + "FROM lab04 "
                    + "WHERE lab04c4 = ?",
                    new Object[]{
                        username
                    }, (ResultSet rs, int i)
                    -> {
                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab04c1"));
                user.setIdentification(Tools.decrypt(rs.getString("lab04c10")));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                user.setPassword(rs.getString("lab04c5"));
                user.setType(Constants.USERLIS);
                user.setConfidential(rs.getInt("lab04c17") == 1);
                return user;
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Actualiza contraseña de la cuenta
     *
     * @param id id de la cuenta
     * @param password nuevo password
     * @return registros afectados
     * @throws Exception Error al actualizar
     */
    default int updatePasswordAccount(int id, String password) throws Exception {
        String update = "UPDATE lab14 set lab14c25 = ? WHERE lab14c1 = ? ";
        if (password != null) {
            update += "AND lab14c25 IS NULL";
        }
        return getJdbcTemplate().update(update,
                new Object[]{
                    Tools.encrypt(password),
                    id
                });
    }

    /**
     * Actualiza contraseña de demografico
     *
     * @param id id de paciente
     * @param password nuevo password
     * @return registros afectados
     * @throws Exception Error al actualizar
     */
    default int updatePasswordDemo(int id, String password) throws Exception {
        String update = "UPDATE lab181 set lab181c3 = ? WHERE lab181c1 = ?";
        if (password != null) {
            update += " AND lab181c3 IS NULL";
        }
        return getJdbcTemplate().update(update,
                new Object[]{
                    password == null ? null : Tools.encrypt(password),
                    id
                });
    }

    /**
     * Actualiza contraseña de paciente
     *
     * @param id id de paciente
     * @param password nuevo password
     * @return registros afectados
     * @throws Exception Error al actualizar
     */
    default int updatePasswordPatient(int id, String password) throws Exception {
        String update = "UPDATE lab21 set lab21c19 = ?, lab21c21 = ? WHERE lab21c1 = ? ";

        return getJdbcTemplate().update(update,
                new Object[]{
                    Tools.encrypt(password),
                    new Timestamp(new Date().getTime()),
                    id
                });
    }

    /**
     * Actualiza contraseña de medico
     *
     * @param id id de medico
     * @param password nuevo password
     * @return registros afectados
     * @throws Exception Error al actualizar
     */
    default int updatePasswordPhysician(int id, String password) throws Exception {
        String update = "UPDATE lab19 set lab19c18 = ? WHERE lab19c1 = ?";
        if (password != null) {
            update += " AND lab19c18 IS NULL";
        }
        return getJdbcTemplate().update(update,
                new Object[]{
                    password == null ? null : Tools.encrypt(password),
                    id
                });
    }

      /**
     * Actualiza contraseña de medico
     *
     * @param id id de medico
     * @param password nuevo password
     * @return registros afectados
     * @throws Exception Error al actualizar
     */
    default int updatePasswordRate(int id, String password) throws Exception {
        String update = "UPDATE lab904 set lab904c39 = ? WHERE lab904c1 = ?";
        if (password != null) {
            update += " AND lab904c39 IS NULL";
        }
        return getJdbcTemplate().update(update,
                new Object[]{
                    password == null ? null : Tools.encrypt(password),
                    id
                });
    }
    
    /**
     * Realiza proceso de autenticacion para los medicos por medio del correo.
     *
     * @param email correo electronico
     * @return Información del usuario autenticado
     * @throws Exception Error en base de datos
     */
    default AuthorizedUser authenticationPhysicianEmail(String email) throws Exception {
        try {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT lab19c1, lab19c2, lab19c3, lab19c17, lab19c18, lab19c19, lab19c21 "
                    + "FROM lab19 "
                    + "WHERE lab19c21 = ?",
                    new Object[]{
                        email
                    }, (ResultSet rs, int i)
                    -> {
                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab19c1"));
                user.setIdentification(rs.getString("lab19c19"));
                user.setName(rs.getString("lab19c2"));
                user.setLastName(rs.getString("lab19c3"));
                user.setUserName(rs.getString("lab19c17"));
                user.setPassword(rs.getString("lab19c18"));
                user.setEmail(rs.getString("lab19c21"));
                user.setType(Constants.PHYSICIAN);
                return user;
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Busca a un paciente segun su correo electronico y número de historia
     *
     * @param email correo electronico
     * @param historyNumber
     *
     * @return Información del usuario autenticado
     * @throws Exception Error en base de datos
     */
    default List<AuthorizedUser> authenticationPatientEmail(String email, String historyNumber) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab21c1, ")
                    .append("lab21c2, ")
                    .append("lab21c3, ")
                    .append("lab21c4, ")
                    .append("lab21c5, ")
                    .append("lab21c6, ")
                    .append("lab21c8, ")
                    .append("lab21c18, ")
                    .append("lab21c19 ")
                    .append("FROM lab21 ")
                    .append("WHERE lab21c8 = '").append(email).append("'")
                    .append(" AND lab21c2 = '").append(Tools.encrypt(historyNumber)).append("'");

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    -> {
                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab21c1"));
                user.setIdentification(Tools.decrypt(rs.getString("lab21c2")));
                user.setName(Tools.decrypt(rs.getString("lab21c3")) + " " + Tools.decrypt(rs.getString("lab21c4")));
                user.setLastName(Tools.decrypt(rs.getString("lab21c5")) + " " + Tools.decrypt(rs.getString("lab21c6")));
                user.setEmail(rs.getString("lab21c8"));
                user.setUserName(Tools.decrypt(rs.getString("lab21c18")));
                user.setPassword(rs.getString("lab21c19"));
                user.setType(Constants.PATIENT);
                return user;
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Realiza proceso de autenticacion para los clientes por medio del correo.
     *
     * @param email correo electronico
     * @return Información del usuario autenticado
     * @throws Exception Error en base de datos
     */
    default AuthorizedUser authenticationAccountEmail(String email) throws Exception {
        try {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT lab14c1, lab14c2, lab14c3, lab14c21, lab14c24, lab14c25 "
                    + "FROM lab14 "
                    + "WHERE lab14c21 = ?",
                    new Object[]{
                        email
                    }, (ResultSet rs, int i)
                    -> {
                AuthorizedUser user = new AuthorizedUser();
                user.setId(rs.getInt("lab14c1"));
                user.setIdentification(rs.getString("lab14c2"));
                user.setName(rs.getString("lab14c3"));
                user.setUserName(rs.getString("lab14c24"));
                user.setPassword(rs.getString("lab14c25"));
                user.setEmail(rs.getString("lab14c21"));
                user.setType(Constants.ACCOUNT);
                return user;
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }
}

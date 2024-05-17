
package net.cltech.securitynt.dao.interfaces.masters.user;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import net.cltech.securitynt.domain.masters.demographic.OrderType;
import net.cltech.securitynt.domain.masters.user.RoleByUser;
import net.cltech.securitynt.domain.masters.user.User;
import net.cltech.securitynt.domain.masters.user.UserPassword;
import net.cltech.securitynt.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los metodos de acceso a base de datos para la informacion de la
 * base de datos
 * @author JDuarte
 * @version 1.0.0
 * @since 21/01/2020
 * @see Se agregaron metodos para el funcionamiento maestro usuario desde consulta web
 */
public interface UserWebDao {    
    /**
     * Obtener información de un usuario por un campo especifico.
     *
     * @param userName Usuario del usuario a ser consultada.
     *
     * @return Instancia con los datos del usuario.
     * @throws java.lang.Exception
     */
    default User findByUserName(String userName) throws Exception
    {
        try
        {
            String sql = "SELECT lab04c1,lab04c2,lab04c3,lab04c4,lab04c5, lab04c14, lab04.lab07c1,lab04.lab04c6,lab04c7,lab04c8,lab04c16,lab04c17, lab103c1, lab04c29 "
                    + " FROM lab04 "
                    + " WHERE LOWER(lab04c4) = ?";
            return getJdbcTemplate().queryForObject(sql, new Object[]
            {
                userName.toLowerCase()
            }, (ResultSet rs, int i) ->
            {
                User user = new User();

                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                user.setPassword(rs.getString("lab04c5"));
                user.setActivation(rs.getTimestamp("lab04c6"));
                user.setExpiration(rs.getTimestamp("lab04c7"));
                user.setMaxDiscount(rs.getDouble("lab04c14"));
                user.setPasswordExpiration(rs.getTimestamp("lab04c8"));
                user.setState(rs.getInt("lab07c1") == 1);
                user.setConfidential(rs.getInt("lab04c17") == 1);
                OrderType orderType = new OrderType();
                orderType.setId(rs.getInt("lab103c1"));
                user.setOrderType(orderType);
                user.setCountFail(rs.getInt("lab04c29"));
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

    /**
     * Obtener información de historico de contraseña de un usuario
     *
     * @param id
     * @return registros insertados
     * @throws java.lang.Exception
     */
    default User getPasswordHistory(int id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT u.lab04c1, u.lab04c2, u.lab04c3, u.lab04c4, u.lab04c6, u.lab04c7, u.lab04c8, u.lab04c9, u.lab04c10, u.lab04c11, u.lab04c12, u.lab04c13, u.lab04c14, u.lab04c15, u.lab04c16, u.lab04c17, u.lab04c18, u.lab04c19, u.lab04c20, u.lab04c21, u.lab04c22, u.lab04c23, u.lab04c24, u.lab07c1, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5, us.lab04c1 as lab04c1_1, us.lab04c2 as lab04c2_1, us.lab04c3 as lab04c3_1, us.lab04c4 as lab04c4_1, lab103.lab103c1, lab103.lab103c2, lab103.lab103c3, u.lab04c5, u.lab04c27, u.lab04c28  "
                    + "FROM lab04 as u "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = u.lab04c15 "
                    + "LEFT JOIN lab04 as us ON us.lab04c1 = u.lab04c1_1 "
                    + "LEFT JOIN lab103 ON lab103.lab103c1 = u.lab103c1 "
                    + "WHERE u.lab04c1 = ?  ";

            return getJdbcTemplate().queryForObject(query, (ResultSet rs, int i) ->
            {
                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                user.setPassword(rs.getString("lab04c5"));
                user.setPenultimatePassword(rs.getString("lab04c27"));
                user.setAntepenultimatePassword(rs.getString("lab04c28"));
                user.setState(rs.getInt("lab07c1") == 1);
                user.setActivation(rs.getTimestamp("lab04c6"));
                user.setExpiration(rs.getTimestamp("lab04c7"));
                user.setPasswordExpiration(rs.getTimestamp("lab04c8"));
                user.setLastTransaction(rs.getTimestamp("lab04c9"));
                user.setIdentification(rs.getString("lab04c10"));
                user.setEmail(rs.getString("lab04c11"));
                user.setSignatureCode(rs.getString("lab04c13"));
                user.setMaxDiscount(rs.getDouble("lab04c14"));
                user.getType().setId(rs.getInt("lab04c15"));
                user.setConfidential(rs.getInt("lab04c17") == 1);
                user.setPrintInReports(rs.getInt("lab04c18") == 1);
                user.setAddExams(rs.getInt("lab04c19") == 1);
                user.setSecondValidation(rs.getInt("lab04c20") == 1);
                user.setEditPatients(rs.getInt("lab04c21") == 1);
                user.setQuitValidation(rs.getInt("lab04c22") == 1);
                user.setCreatingItems(rs.getInt("lab04c23") == 1);
                user.setPrintResults(rs.getInt("lab04c24") == 1);

                //Imagenes
                //Firma
                String signature64 = "";
                byte[] signatureBytes = rs.getBytes("lab04c12");
                if (signatureBytes != null)
                {
                    signature64 = Base64.getEncoder().encodeToString(signatureBytes);
                }
                user.setSignature(signature64);
                /*Foto*/
                String photo64 = "";
                byte[] photoBytes = rs.getBytes("lab04c16");
                if (photoBytes != null)
                {
                    photo64 = Base64.getEncoder().encodeToString(photoBytes);
                }
                user.setPhoto(photo64);

                /*Tipo*/
                user.getType().setId(rs.getInt("lab80c1"));
                user.getType().setIdParent(rs.getInt("lab80c2"));
                user.getType().setCode(rs.getString("lab80c3"));
                user.getType().setEsCo(rs.getString("lab80c4"));
                user.getType().setEnUsa(rs.getString("lab80c5"));

                user.setLastTransaction(rs.getTimestamp("lab04c7"));

                /*Usuario*/
                user.getUser().setId(rs.getInt("lab04c1_1"));
                user.getUser().setName(rs.getString("lab04c2_1"));
                user.getUser().setLastName(rs.getString("lab04c3_1"));
                user.getUser().setUserName(rs.getString("lab04c4_1"));

                /*Tipo de Orden*/
                user.getOrderType().setId(rs.getInt("lab103c1"));
                user.getOrderType().setCode(rs.getString("lab103c2"));
                user.getOrderType().setName(rs.getString("lab103c3"));
                return user;
            }, id);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtener información de un usuario por un campo especifico.
     *
     * @param id ID del usuario a ser consultada.
     * @param username Usuario del usuario a ser consultada.
     * @param identification Identificacion del usuario a ser consultada.
     * @param signatureCode Codigo firma del usuario a ser consultada.
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    public User get(Integer id, String username, String identification, String signatureCode) throws Exception;

    /**
     * Obtener información de un usuario por un campo especifico.
     *
     * @param username nombre de usuario
     * @param password contraseña del usuario
     *
     * @return registros insertados
     * @throws java.lang.Exception
     */
    default User get(String username, String password) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT u.lab04c1, u.lab04c2, u.lab04c3, u.lab04c4, u.lab04c5, u.lab04c6, u.lab04c7, u.lab04c8, u.lab04c9, u.lab04c10, u.lab04c11, u.lab04c12, u.lab04c13, u.lab04c14, u.lab04c15, u.lab04c16, u.lab04c17, u.lab04c18, u.lab04c19, u.lab04c20, u.lab04c21, u.lab04c22, u.lab04c23, u.lab04c24, u.lab07c1, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5, us.lab04c1 as lab04c1_1, us.lab04c2 as lab04c2_1, us.lab04c3 as lab04c3_1, us.lab04c4 as lab04c4_1, lab103.lab103c1, lab103.lab103c2, lab103.lab103c3, u.lab04c27, u.lab04c28 "
                    + "FROM lab04 as u "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = u.lab04c15 "
                    + "LEFT JOIN lab04 as us ON us.lab04c1 = u.lab04c1_1 "
                    + "LEFT JOIN lab103 ON lab103.lab103c1 = u.lab103c1 ";
            /*Where*/
            if (password != null)
            {
                query = query + "WHERE u.lab04c5 = ? ";
            }
            if (username != null)
            {
                query += !query.contains("WHERE") ? "WHERE UPPER(u.lab04c4) = ? " : " AND UPPER(u.lab04c4) = ? ";
            }
            List<Object> objects = new ArrayList<>();
            if (password != null)
            {
                objects.add(Tools.encrypt(password));
            }
            if (username != null)
            {
                objects.add(username.toUpperCase());
            }

            return getJdbcTemplate().queryForObject(query,
                    objects.toArray(), (ResultSet rs, int i) ->
            {
                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                user.setPassword(rs.getString("lab04c5"));
                user.setPenultimatePassword(rs.getString("lab04c27"));
                user.setAntepenultimatePassword(rs.getString("lab04c28"));
                user.setState(rs.getInt("lab07c1") == 1);
                user.setActivation(rs.getTimestamp("lab04c6"));
                user.setExpiration(rs.getTimestamp("lab04c7"));
                user.setPasswordExpiration(rs.getTimestamp("lab04c8"));
                user.setLastTransaction(rs.getTimestamp("lab04c9"));
                user.setIdentification(rs.getString("lab04c10"));
                user.setEmail(rs.getString("lab04c11"));
                user.setSignatureCode(rs.getString("lab04c13"));
                user.setMaxDiscount(rs.getDouble("lab04c14"));
                user.getType().setId(rs.getInt("lab04c15"));
                user.setConfidential(rs.getInt("lab04c17") == 1);
                user.setPrintInReports(rs.getInt("lab04c18") == 1);
                user.setAddExams(rs.getInt("lab04c19") == 1);
                user.setSecondValidation(rs.getInt("lab04c20") == 1);
                user.setEditPatients(rs.getInt("lab04c21") == 1);
                user.setQuitValidation(rs.getInt("lab04c22") == 1);
                user.setCreatingItems(rs.getInt("lab04c23") == 1);
                user.setPrintResults(rs.getInt("lab04c24") == 1);

                //Imagenes
                //Firma
                String signature64 = "";
                byte[] signatureBytes = rs.getBytes("lab04c12");
                if (signatureBytes != null)
                {
                    signature64 = Base64.getEncoder().encodeToString(signatureBytes);
                }
                user.setSignature(signature64);
                /*Foto*/
                String photo64 = "";
                byte[] photoBytes = rs.getBytes("lab04c16");
                if (photoBytes != null)
                {
                    photo64 = Base64.getEncoder().encodeToString(photoBytes);
                }
                user.setPhoto(photo64);

                /*Tipo*/
                user.getType().setId(rs.getInt("lab80c1"));
                user.getType().setIdParent(rs.getInt("lab80c2"));
                user.getType().setCode(rs.getString("lab80c3"));
                user.getType().setEsCo(rs.getString("lab80c4"));
                user.getType().setEnUsa(rs.getString("lab80c5"));

                user.setLastTransaction(rs.getTimestamp("lab04c7"));

                /*Usuario*/
                user.getUser().setId(rs.getInt("lab04c1_1"));
                user.getUser().setName(rs.getString("lab04c2_1"));
                user.getUser().setLastName(rs.getString("lab04c3_1"));
                user.getUser().setUserName(rs.getString("lab04c4_1"));

                /*Tipo de Orden*/
                user.getOrderType().setId(rs.getInt("lab103c1"));
                user.getOrderType().setCode(rs.getString("lab103c2"));
                user.getOrderType().setName(rs.getString("lab103c3"));
                return user;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtener roles asociados a un usuario.
     *
     * @param user Instancia con los datos del usuario.
     */
    public void readRoles(User user);

    default boolean updatePassword(User user, int days)
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, days);

        if (user.getPassword() != null)
        {
            return getJdbcTemplate().update("UPDATE lab04 SET lab04c2 = ?, lab04c3 = ?, lab04c10 = ?, lab04c11 = ?, lab04c5 = ?, lab04c8 = ?, lab04c9 = ? , lab04c1_1 = ?, lab04c16 = ? "
                    + "WHERE lab04c4 = ?",
                    user.getName(), user.getLastName(), user.getIdentification(), user.getEmail(), Tools.encrypt(user.getPassword()), new Timestamp(calendar.getTime().getTime()), timestamp, user.getUser().getId(), user.getPhoto() == null ? null : DatatypeConverter.parseBase64Binary(user.getPhoto()), user.getUserName()
            ) == 1;
        } else
        {
            return getJdbcTemplate().update("UPDATE lab04 SET lab04c2 = ?, lab04c3 = ?, lab04c10 = ?, lab04c11 = ?, lab04c9 = ? , lab04c1_1 = ?, lab04c16 = ? "
                    + "WHERE lab04c4 = ?",
                    user.getName(), user.getLastName(), user.getIdentification(), user.getEmail(), timestamp, user.getUser().getId(), user.getPhoto() == null ? null : DatatypeConverter.parseBase64Binary(user.getPhoto()), user.getUserName()
            ) == 1;
        }
    }

    default boolean changePassword(UserPassword user, int days)
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, days);
        int response = getJdbcTemplate().update("UPDATE lab04 SET lab04c5 = ?, lab04c27 = ?, lab04c28 = ?, lab04c8 = ?, lab04c9 = ? "
                + "WHERE lab04c4 = ?",
                user.getPasswordNew(), user.getPasswordOld(), user.getPasswordOldSecond(), new Timestamp(calendar.getTime().getTime()), timestamp, user.getUserName().toLowerCase());
        return response == 1;
    }

    default boolean updateCountFail()
    {
        return getJdbcTemplate().update("UPDATE lab04 SET lab04c29 = 0 WHERE lab07c1 = 1 AND lab04c29 < 3") == 1;
    }

   
    
   
    

    /**
     * Roles de un usuario
     *
     * @param user entidad de usuario
     */
    default void rolesByUser(User user)
    {
        try
        {
            user.setRoles(getJdbcTemplate().query("SELECT lab84c1, lab84.lab04c1, lab82.lab82c1, lab82.lab82c2, lab82.lab82c3 FROM lab82 "
                    + "INNER JOIN lab84 ON lab84.lab82c1 = lab82.lab82c1 "
                    + "AND lab84.lab04c1 = ?"
                    + "",
                    new Object[]
                    {
                        user.getId()
                    }, (ResultSet rs, int i) ->
            {
                RoleByUser roleByUser = new RoleByUser();
                roleByUser.setAccess(rs.getInt("lab84c1") == 1);

                /*Usuario*/
                roleByUser.getUser().setId(rs.getInt("lab04c1"));

                /*Roles*/
                roleByUser.getRole().setId(rs.getInt("lab82c1"));
                roleByUser.getRole().setName(rs.getString("lab82c2"));
                roleByUser.getRole().setAdministrator(rs.getInt("lab82c3") == 1);

                return roleByUser;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            user.setRoles(new ArrayList<>());
        }
    }

    
    

    /**
     * Registra una nueva usuario en la base de datos.
     *
     * @param user Instancia con los datos del usuario.
     * @param days Dias de expiracion de la contraseña.
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    public User create(User user, int days) throws Exception;

    /**
     * Actualiza la información de un usuario en la base de datos.
     *
     * @param user Instancia con los datos del usuario.
     * @param days Dias de expiracion de la contraseña.
     *
     * @return Objeto del usuario modificada.
     * @throws Exception Error en la base de datos.
     */
    public User update(User user, int days) throws Exception;

    /**
     * Actualizar contador de intentos fallidos
     *
     * @param user id del usuario
     * @param initial
     */
    default void countFail(int user, boolean initial)
    {
        String sql = "UPDATE lab04 SET lab04c29 = lab04c29 + 1 WHERE lab04c1 = ? ";
        if (initial)
        {
            sql = "UPDATE lab04 SET lab04c29 = 0 WHERE lab04c1 = ? ";
        }
        getJdbcTemplate().update(sql, user);
    }

    public JdbcTemplate getJdbcTemplate();
    
    
    
    
    
    
}

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
import net.cltech.securitynt.domain.masters.user.BranchByUser;
import net.cltech.securitynt.domain.masters.user.RoleByUser;
import net.cltech.securitynt.domain.masters.user.User;
import net.cltech.securitynt.domain.masters.user.UserPassword;
import net.cltech.securitynt.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los metodos de acceso a base de datos para la informacion de la base de datos
 *
 * @version 1.0.0
 * @author eacuna
 * @since 17/04/2017
 * @see Creación
 *
 * @author cmartin
 * @version 1.0.0
 * @since 12/05/2017
 * @see Se agregaron metodos para el funcionamiento maestro usuario.
 */
public interface UserDao
{

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
            String sql = "SELECT lab04c1,"
                    + "lab04c2,"
                    + "lab04c3,"
                    + "lab04c4,"
                    + "lab04c5, "
                    + "lab04c14, "
                    + "lab04.lab07c1,"
                    + "lab04.lab04c6,"
                    + "lab04c7,"
                    + "lab04c8,"
                    + "lab04c16,"
                    + "lab04c17, "
                    + "lab103c1, "
                    + "lab04c29, "
                    + "lab04c23, "
                    + "lab04c15, "
                    + "lab04c33, "
                    + "lab04c36, "
                    + "lab04c39, "
                    + "lab04c26 "
                    + " FROM lab04 "
                    + " WHERE LOWER(lab04c4) = LOWER('" + userName + "')";
            return getJdbcTemplate().queryForObject(sql, 
                    (ResultSet rs, int i) ->
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
                user.getType().setId(rs.getInt("lab04c15"));
                user.setValidatedRecove(rs.getInt("lab04c33") == 1);
                user.setUserTypeLogin(4);
                user.setCreatingItems(rs.getInt("lab04c23") == 1);
                /*Foto*/
                String photo64 = "";
                byte[] photoBytes = rs.getBytes("lab04c16");
                if (photoBytes != null)
                {
                    photo64 = Base64.getEncoder().encodeToString(photoBytes);
                }
                user.setPhoto(photo64);
                
                user.setEditOrderCash(rs.getInt("lab04c36") == 1);
                user.setRemoveCashBox(rs.getInt("lab04c39") == 1);
                user.setDateLastLogin(rs.getTimestamp("lab04c26"));
                return user;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtener información de un usuario por un campo especifico.
     *
     * @param userName Usuario del usuario a ser consultada en consulta web.
     *
     * @return Instancia con los datos del usuario.
     * @throws java.lang.Exception
     */
    default User findByUserNameWeb(String userName) throws Exception
    {
        try
        {
            String sql = "SELECT lab181c1, lab181c2, lab181c3, lab181c4, lab181c5, lab181c6, lab181c9"
                    + " FROM lab181 "
                    + " WHERE LOWER(lab181c2) = LOWER(?)";
            return getJdbcTemplate().queryForObject(sql, new Object[]
            {
                userName.toLowerCase()
            }, (ResultSet rs, int i) ->
            {
                User user = new User();

                user.setId(rs.getInt("lab181c1"));
                user.setUserName(rs.getString("lab181c2"));
                user.setPassword(rs.getString("lab181c3"));
                user.setPasswordExpiration(rs.getTimestamp("lab181c4"));
                user.setDateLastLogin(rs.getTimestamp("lab181c5"));
                user.setMaxDiscount(rs.getDouble("lab181c6"));
                user.setCountFail(rs.getInt("lab181c6"));
                user.setState(rs.getInt("lab181c9") == 1);
                user.setUserTypeLogin(5);

                return user;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtener información de un usuario por un campo especifico.
     *
     * @param user
     */
    default void changeStateUser(User user)
    {
        getJdbcTemplate().update("UPDATE lab181 SET lab181c6 = 0, lab181c9 = 0 WHERE lab181c1 = ? ", user.getId());
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
        }
        catch (EmptyResultDataAccessException ex)
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
    default User getPasswordHistoryWeb(int id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT u.lab181c1, u.lab181c2, u.lab181c3, u.lab181c4, u.lab181c5, u.lab181c6, u.lab181c9, u.lab181c10, u.lab181c11, u.lab181c12"
                    + " FROM lab181 as u "
                    + "WHERE u.lab181c1 = ?  ";

            return getJdbcTemplate().queryForObject(query, (ResultSet rs, int i) ->
            {
                User user = new User();
                user.setId(rs.getInt("lab181c1"));
                user.setUserName(rs.getString("lab181c2"));
                user.setPassword(rs.getString("lab181c3"));
                user.setPenultimatePassword(rs.getString("lab181c10"));
                user.setAntepenultimatePassword(rs.getString("lab181c11"));
                user.setLastTransaction(rs.getTimestamp("lab181c12"));
                user.setPasswordExpiration(rs.getTimestamp("lab181c4"));
                user.setDateLastLogin(rs.getTimestamp("lab181c5"));
                user.setMaxDiscount(rs.getDouble("lab181c6"));
                user.setCountFail(rs.getInt("lab181c6"));
                user.setState(rs.getInt("lab181c9") == 1);


                /*Usuario*/
                user.getUser().setId(rs.getInt("lab181c1"));
                user.getUser().setUserName(rs.getString("lab181c2"));

                return user;
            }, id);
        }
        catch (EmptyResultDataAccessException ex)
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
     * @param id ID del usuario a ser consultada.
     * @param username Usuario del usuario a ser consultada.
     * @param identification Identificacion del usuario a ser consultada.
     * @param signatureCode Codigo firma del usuario a ser consultada.
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    public User getWeb(Integer id, String username, String identification, String signatureCode) throws Exception;

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

        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtener información de un usuario por un campo especifico.
     *
     * @param username nombre de usuario
     * @param password contraseña del usuario
     *
     * @return registros insertados
     * @throws java.lang.Exception
     */
    default User getWeb(String username, String password) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT u.lab181c1, u.lab181c2, u.lab181c3, u.lab181c4, u.lab181c5, u.lab181c6, u.lab181c9, u.lab181c10, u.lab181c11, u.lab181c12"
                    + " FROM lab181 as u ";
            /*Where*/
            if (password != null)
            {
                query = query + "WHERE u.lab181c3 = ? ";
            }
            if (username != null)
            {
                query += !query.contains("WHERE") ? "WHERE UPPER(u.lab181c2) = ? " : " AND UPPER(u.lab181c2) = ? ";
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
                user.setId(rs.getInt("lab181c1"));
                user.setUserName(rs.getString("lab181c2"));
                user.setPassword(rs.getString("lab181c3"));
                user.setPasswordExpiration(rs.getTimestamp("lab181c4"));
                user.setDateLastLogin(rs.getTimestamp("lab181c5"));
                user.setMaxDiscount(rs.getDouble("lab181c6"));
                user.setCountFail(rs.getInt("lab181c6"));
                user.setState(rs.getInt("lab181c9") == 1);

                /*Usuario*/
                user.getUser().setId(rs.getInt("lab181c1"));
                user.getUser().setUserName(rs.getString("lab181c2"));
                return user;
            });

        }
        catch (EmptyResultDataAccessException ex)
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
        }
        else
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
        int response = getJdbcTemplate().update("UPDATE lab04 SET lab04c5 = ?, lab04c27 = ?, lab04c28 = ?, lab04c8 = ?, lab04c9 = ?, lab04c33 = 0 "
                + "WHERE lab04c1 = ?",
                user.getPasswordNew(), user.getPasswordOld(), user.getPasswordOldSecond(), new Timestamp(calendar.getTime().getTime()), timestamp, user.getIdUser());
        return response == 1;
    }

    default boolean changePasswordWeb(UserPassword user, int days)
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, days);
        int response = getJdbcTemplate().update("UPDATE lab181 SET lab181c3 = ?, lab181c10 = ?, lab181c11 = ?, lab181c4 = ?, lab181c12 = ? "
                + "WHERE lab181c1 = ?",
                user.getPasswordNew(), user.getPasswordOld(), user.getPasswordOldSecond(), new Timestamp(calendar.getTime().getTime()), timestamp, user.getIdUser());

        return response == 1;
    }

    default boolean updateCountFail()
    {
        return getJdbcTemplate().update("UPDATE lab04 SET lab04c29 = 0 WHERE lab07c1 = 1 AND lab04c29 < 3") == 1;
    }

    default boolean updateCountFailWeb()
    {
        return getJdbcTemplate().update("UPDATE lab181 SET lab181c6 = 0 WHERE lab181c9 = 1 AND lab181c6 < 3") == 1;
    }

    /**
     * Asigna las sedes al usuario
     *
     * @param user entidad de usuario
     */
    default void readBranch(User user)
    {
        try
        {
            user.setBranches(getJdbcTemplate().query("SELECT lab93c1, lab93c2, lab93.lab04c1, lab05.lab05c1, lab05.lab05c2, lab05.lab05c4 FROM lab05 "
                    + "LEFT JOIN lab93 ON lab93.lab05c1 = lab05.lab05c1 "
                    + "AND lab93.lab04c1 = ?"
                    + "",
                    new Object[]
                    {
                        user.getId()
                    }, (ResultSet rs, int i) ->
            {
                BranchByUser branchByUser = new BranchByUser();
                branchByUser.setAccess(rs.getInt("lab93c1") == 1);
                branchByUser.setBatchPrint(rs.getInt("lab93c2") == 1);

                /*Usuario*/
                branchByUser.getUser().setId(rs.getInt("lab04c1"));

                /*Sedes*/
                branchByUser.getBranch().setId(rs.getInt("lab05c1"));
                branchByUser.getBranch().setAbbreviation(rs.getString("lab05c2"));
                branchByUser.getBranch().setName(rs.getString("lab05c4"));

                return branchByUser;
            }));
        }
        catch (EmptyResultDataAccessException ex)
        {
            user.setBranches(new ArrayList<>());
        }
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
        }
        catch (EmptyResultDataAccessException ex)
        {
            user.setRoles(new ArrayList<>());
        }
    }

    /**
     * Sedes de un usuario
     *
     * @param user entidad de usuario
     */
    default void rolesByBranch(User user)
    {
        try
        {
            user.setBranches(getJdbcTemplate().query("SELECT lab93c1, lab93c2, lab93.lab04c1, lab05.lab05c1, lab05.lab05c2, lab05.lab05c4 FROM lab05 "
                    + "INNER JOIN lab93 ON lab93.lab05c1 = lab05.lab05c1 "
                    + "AND lab93.lab04c1 = ?"
                    + "",
                    new Object[]
                    {
                        user.getId()
                    }, (ResultSet rs, int i) ->
            {
                BranchByUser branchByUser = new BranchByUser();
                branchByUser.setAccess(rs.getInt("lab93c1") == 1);
                branchByUser.setBatchPrint(rs.getInt("lab93c2") == 1);

                /*Usuario*/
                branchByUser.getUser().setId(rs.getInt("lab04c1"));

                /*Sedes*/
                branchByUser.getBranch().setId(rs.getInt("lab05c1"));
                branchByUser.getBranch().setAbbreviation(rs.getString("lab05c2"));
                branchByUser.getBranch().setName(rs.getString("lab05c4"));

                return branchByUser;
            }));
        }
        catch (EmptyResultDataAccessException ex)
        {
            user.setBranches(new ArrayList<>());
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

    /**
     * Actualizar contador de intentos fallidos en consulta web
     *
     * @param user id del usuario
     * @param initial
     */
    default void countFailWeb(int user, boolean initial)
    {
        String sql = "UPDATE lab181 SET lab181c6 = lab181c6 + 1 WHERE lab181c1 = ? ";
        if (initial)
        {
            sql = "UPDATE lab181 SET lab181c6 = 0 WHERE lab181c1 = ? ";
        }
        getJdbcTemplate().update(sql, user);
    }

    default void changeDateLastLogin(int user)
    {
        String sql = "UPDATE lab04 SET lab04c26 = ? WHERE lab04c1 = ? ";
        getJdbcTemplate().update(sql, new Timestamp(new Date().getTime()), user);
    }

    default void changeDateLastLoginWeb(int user)
    {
        String sql = "UPDATE lab181 SET lab181c5 = ? WHERE lab181c1 = ? ";
        getJdbcTemplate().update(sql, new Timestamp(new Date().getTime()), user);
    }
    
    default void updateDateEntry(int user)
    {
        String sql = "UPDATE lab21 SET lab21c21 = ? WHERE lab21c1 = ? ";
        getJdbcTemplate().update(sql, new Timestamp(new Date().getTime()), user);
    }

    public JdbcTemplate getJdbcTemplate();

    /**
     * Realiza proceso de autenticacion para los medicos.
     *
     * @param userName Usuario
     * @return Información del usuario autenticado
     * @throws Exception Error en base de datos
     */
    default User authenticationPhysician(String userName) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab19c1, ")
                    .append("lab19c2, ")
                    .append("lab19c3, ")
                    .append("lab19c17, ")
                    .append("lab19c18, ")
                    .append("lab19c19, ")
                    .append("lab07c1, ")
                    .append("lab19c21 ")
                    .append("FROM lab19 ")
                    .append("WHERE lab19c19 = '").append(userName).append("'");
            return getJdbcTemplate().queryForObject(query.toString(), 
                    (ResultSet rs, int i) ->
            {
                User user = new User();
                user.setId(rs.getInt("lab19c1"));
                user.setIdentification(rs.getString("lab19c19"));
                user.setName(rs.getString("lab19c2"));
                user.setLastName(rs.getString("lab19c3"));
                user.setUserName(rs.getString("lab19c17"));
                user.setPassword(rs.getString("lab19c18"));
                user.setEmail(rs.getString("lab19c21"));
                user.setState(rs.getInt("lab07c1") == 1);
                user.setUserTypeLogin(1);
                return user;
            });
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Realiza proceso de autenticacion para los pacientes.
     *
     * @param userName Usuario
     * @param historyType
     * @return Información del usuario autenticado
     * @throws Exception Error en base de datos
     */
    default User authenticationPatient(String userName, int historyType) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab21c1, ")
                    .append("lab21c2, ")
                    .append("lab21c3, ")
                    .append("lab21c4, ")
                    .append("lab21c5, ")
                    .append("lab21c6, ")
                    .append("lab21c8, ")
                    .append("lab21c18, ")
                    .append("lab21c19, ")
                    .append("lab21c21 ")
                    .append("FROM lab21 ")
                    .append("WHERE lab21c18 = '").append(userName).append("'");
            if (historyType != -1)
            {
                query.append(" AND lab54c1 = ").append(historyType);
            }
            
            return getJdbcTemplate().queryForObject(query.toString(),
                     (ResultSet rs, int i) ->
            {
                User user = new User();
                user.setId(rs.getInt("lab21c1"));
                user.setIdentification(Tools.decrypt(rs.getString("lab21c2")));
                user.setName(Tools.decrypt(rs.getString("lab21c3")) + " " + Tools.decrypt(rs.getString("lab21c4")));
                user.setLastName(Tools.decrypt(rs.getString("lab21c5")) + " " + Tools.decrypt(rs.getString("lab21c6")));
                user.setUserName(Tools.decrypt(rs.getString("lab21c18")));
                user.setPassword(rs.getString("lab21c19"));
                user.setEmail(rs.getString("lab21c8"));
                user.setUserTypeLogin(2);
                user.setDateEntryLogin(rs.getTimestamp("lab21c21"));
                // El usuario paciente al no tener un estado modificable desde la base de datos
                // su estado siempre sera True -> Activo
                user.setState(true);
                return user;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
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
    default User authenticationAccount(String username) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab14c1, ")
                    .append("lab14c2, ")
                    .append("lab14c3, ")
                    .append("lab14c21, ")
                    .append("lab14c24, ")
                    .append("lab07c1, ")
                    .append("lab14c25 ")
                    .append("FROM lab14 ")
                    .append("WHERE lab14c24 = '").append(username).append("'");
            
            return getJdbcTemplate().queryForObject(query.toString(), 
                    (ResultSet rs, int i) ->
            {
                User user = new User();
                user.setId(rs.getInt("lab14c1"));
                user.setIdentification(Tools.decrypt(rs.getString("lab14c2")));
                user.setName(rs.getString("lab14c3"));
                user.setUserName(rs.getString("lab14c24"));
                user.setPassword(rs.getString("lab14c25"));
                user.setEmail(rs.getString("lab14c21"));
                user.setState(rs.getInt("lab07c1") == 1);
                user.setUserTypeLogin(3);
                return user;
            });
        } catch (Exception ex)
        {
            return null;
        }
    }
}

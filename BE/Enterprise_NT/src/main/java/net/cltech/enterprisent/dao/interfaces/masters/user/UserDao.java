package net.cltech.enterprisent.dao.interfaces.masters.user;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.DatatypeConverter;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.test.ExcludeTest;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.masters.user.AreaByUser;
import net.cltech.enterprisent.domain.masters.user.BranchByUser;
import net.cltech.enterprisent.domain.masters.user.RoleByUser;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.masters.user.UserAnalyzer;
import net.cltech.enterprisent.domain.masters.user.UserByBranchByAreas;
import net.cltech.enterprisent.domain.masters.user.UserIntegration;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los metodos de acceso a base de datos para la informacion de la
 * base de datos
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
     * @throws Exception Error en la base de datos.
     */
    default User findByUserName(String userName) throws Exception
    {
        try
        {
            String sql = "SELECT lab04c1, lab04c2, lab04c3, lab04c4, lab04c5, lab04c14, lab04.lab07c1, lab04.lab04c6,lab04c7, lab04c8, lab04c16, lab04c17, lab103c1, lab04c36, lab04c37, lab04c38 , lab04c39 "
                    + " FROM lab04 "
                    + " WHERE LOWER(lab04c4) = ?";
            return getJdbcTemplate().queryForObject(sql, new Object[]
            {
                userName.toLowerCase()
            }, (ResultSet rs, int i)
                    ->
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
                user.setEditOrderCash(rs.getInt("lab04c36") == 1);
                user.setDemographicQuery(rs.getInt("lab04c37"));
                user.setDemographicItemQuery(rs.getInt("lab04c38"));
                user.setRemoveCashBox(rs.getInt("lab04c39") == 1);
                OrderType orderType = new OrderType();
                orderType.setId(rs.getInt("lab103c1"));
                user.setOrderType(orderType);
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
     * Lista de usuarios con id y fecha de ultimo ingreso al sistema, para
     * verifiacion de estado.
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    default List<User> listDeactivate() throws Exception
    {
        try
        {
            String sql = "SELECT lab04c1, lab04c26 "
                    + " FROM lab04 "
                    + " WHERE lab07c1 = 1";
            return getJdbcTemplate().query(sql, (ResultSet rs, int i)
                    ->
            {
                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setDateLastLogin(rs.getTimestamp("lab04c26"));
                return user;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Lista de usuarios simple
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    default List<User> listUser() throws Exception
    {
        try
        {
            String sql = "SELECT lab04c1, lab04c2, lab04c3, lab04c4, lab04c10, lab07c1, lab80c1, lab04c36, lab04c39 "
                    + " FROM lab04 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab04.lab04c15 ";
            return getJdbcTemplate().query(sql, (ResultSet rs, int i)
                    ->
            {
                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                user.setIdentification(rs.getString("lab04c10"));
                user.setState(rs.getInt("lab07c1") == 1);
                user.getType().setId(rs.getInt("lab80c1"));
                user.setEditOrderCash(rs.getInt("lab04c36") == 1);
                user.setRemoveCashBox(rs.getInt("lab04c39") == 1);
                return user;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Lista los usuarios desde la base de datos.
     *
     * @return Lista de usuarios.
     * @throws Exception Error en la base de datos.
     */
    public List<User> list() throws Exception;

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
    default User get(Integer id, String username, String identification, String signatureCode) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT u.lab04c1, "
                    + "u.lab04c2, u.lab04c3, "
                    + "u.lab04c4, u.lab04c6, "
                    + "u.lab04c7, u.lab04c8, "
                    + "u.lab04c9, u.lab04c10, "
                    + "u.lab04c11, u.lab04c12, "
                    + "u.lab04c13, u.lab04c14, "
                    + "u.lab04c15, u.lab04c16, "
                    + "u.lab04c17, u.lab04c18, "
                    + "u.lab04c19, u.lab04c20, "
                    + "u.lab04c21, u.lab04c22, "
                    + "u.lab04c23, u.lab04c24, "
                    + "u.lab04c25, u.lab04c30,  "
                    + "u.lab04c31, "
                    + "u.lab04c32, "
                    + "u.lab04c34, "
                    + "u.lab04c35, "
                    + "u.lab04c36, "
                    + "u.lab04c37, "
                    + "u.lab04c38, "
                    + "u.lab04c39, "
                    + "u.lab04c40, "
                    + "u.lab04c42, "
                    + "u.lab04c41, "
                    + "u.lab07c1, lab80c1, "
                    + "lab80c2, lab80c3, lab80c4, "
                    + "lab80c5, us.lab04c1 as lab04c1_1, "
                    + "us.lab04c2 as lab04c2_1, "
                    + "us.lab04c3 as lab04c3_1, "
                    + "us.lab04c4 as lab04c4_1, "
                    + "lab103.lab103c1, "
                    + "lab103.lab103c2, lab103.lab103c3 "
                    + "FROM lab04 as u "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = u.lab04c15 "
                    + "LEFT JOIN lab04 as us ON us.lab04c1 = u.lab04c1_1 "
                    + "LEFT JOIN lab103 ON lab103.lab103c1 = u.lab103c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE u.lab04c1 = ? ";
            }
            if (username != null)
            {
                query = query + "WHERE UPPER(u.lab04c4) = ? ";
            }
            if (identification != null)
            {
                query = query + "WHERE UPPER(u.lab04c10) = ? ";
            }
            if (signatureCode != null)
            {
                query = query + "WHERE UPPER(u.lab04c13) = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (username != null)
            {
                object = username.toUpperCase();
            }
            if (identification != null)
            {
                object = identification.toUpperCase();
            }
            if (signatureCode != null)
            {
                object = signatureCode.toUpperCase();
            }

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i)
                    ->
            {
                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
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
                user.setDestination(new Destination());
                user.getDestination().setId(rs.getString("lab04c25") == null ? 0 : rs.getInt("lab04c25"));
                user.setLaboratory(rs.getInt("lab04c30"));
                user.setDashboard(rs.getInt("lab04c31") == 1);
                user.setPreValidationRequired(rs.getInt("lab04c32") == 1);
                user.setPrintReportpreliminary(rs.getInt("lab04c34") == 1);
                user.setUpdatetestentry(rs.getInt("lab04c35") == 1);
                user.setEditOrderCash(rs.getInt("lab04c36") == 1);
                user.setDemographicQuery(rs.getInt("lab04c37"));
                user.setDemographicItemQuery(rs.getInt("lab04c38"));
                user.setRemoveCashBox(rs.getInt("lab04c39") == 1);
                user.setEditObservation(rs.getInt("lab04c40") == 1);
                user.setEditTestResult(rs.getInt("lab04c42") == 1);
                user.setEditPayments(rs.getInt("lab04c41") == 1);
                
                
                

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

                /*Usuario*/
                user.getUser().setId(rs.getInt("lab04c1_1"));
                user.getUser().setName(rs.getString("lab04c2_1"));
                user.getUser().setLastName(rs.getString("lab04c3_1"));
                user.getUser().setUserName(rs.getString("lab04c4_1"));

                /*Tipo de Orden*/
                user.getOrderType().setId(rs.getInt("lab103c1"));
                user.getOrderType().setCode(rs.getString("lab103c2"));
                user.getOrderType().setName(rs.getString("lab103c3"));

                readAreas(user);
                readBranch(user);
                readRoles(user);

                return user;
            });

        } catch (EmptyResultDataAccessException ex)
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
    default User get(String username, String password) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT u.lab04c1, u.lab04c2, u.lab04c3, u.lab04c4, u.lab04c6, u.lab04c7, u.lab04c8, u.lab04c9, u.lab04c10, u.lab04c11, u.lab04c12, u.lab04c13, u.lab04c14, u.lab04c15, u.lab04c16, u.lab04c17, u.lab04c18, u.lab04c19, u.lab04c20, u.lab04c21, u.lab04c22, u.lab04c23, u.lab04c24, u.lab07c1, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5, us.lab04c1 as lab04c1_1, us.lab04c2 as lab04c2_1, us.lab04c3 as lab04c3_1, us.lab04c4 as lab04c4_1, lab103.lab103c1, lab103.lab103c2, lab103.lab103c3 "
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
                    objects.toArray(), (ResultSet rs, int i)
                    ->
            {
                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
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

            return getJdbcTemplate().queryForObject(query, (ResultSet rs, int i)
                    ->
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
     * Obtener información de historico de contraseña de un usuario
     *
     * @param id
     * @return registros insertados
     * @throws java.lang.Exception
     */
    default User getBasicUser(int id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT u.lab04c1, u.lab04c2, u.lab04c3, u.lab04c4, u.lab04c10  "
                    + "FROM lab04 as u "
                    + "WHERE u.lab04c1 = ?  ";

            return getJdbcTemplate().queryForObject(query, (ResultSet rs, int i)
                    ->
            {
                User user = new User();
                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));
                user.setIdentification(rs.getString("lab04c10"));
                return user;

            }, id);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtener información de historico de contraseña de un usuario
     *
     * @param username
     * @param password
     * @return registros insertados
     * @throws java.lang.Exception
     */
    default User getPermitUpdate(String username, String password) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab04c4, lab04c35 "
                    + "FROM lab04 ";
            /*Where*/
            if (password != null)
            {
                query = query + "WHERE lab04c5 = ? AND lab07c1 = 1 ";
            }
            if (username != null)
            {
                query += !query.contains("WHERE") ? "WHERE UPPER(lab04c4) = ? " : " AND UPPER(lab04c4) = ? ";
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
                    objects.toArray(), (ResultSet rs, int i)
                    ->
            {
                User user = new User();
                user.setUserName(rs.getString("lab04c4"));
                user.setUpdatetestentry(rs.getInt("lab04c35") == 1);
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
     * @param username
     * @return registros insertados
     * @throws java.lang.Exception
     */
    default UserIntegration getUserByCode(String username) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab04c1, lab04c5 FROM lab04 ";
            if (username != null)
            {
                query += !query.contains("WHERE") ? "WHERE UPPER(lab04c4) = ? " : " AND UPPER(lab04c4) = ? ";
            }
         
            List<Object> objects = new ArrayList<>();
            
            objects.add(username.toUpperCase());
            
            return getJdbcTemplate().queryForObject(query,
                    objects.toArray(), (ResultSet rs, int i)
                    ->
            {
                UserIntegration user = new UserIntegration();
                user.setId(rs.getInt("lab04c1"));
                user.setPassword(Tools.decrypt(rs.getString("lab04c5")));
                return user;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

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
     *
     * Elimina un usuario de la base de datos.
     *
     * @param id ID del usuario.
     *
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;

    /**
     * Obtener roles asociados a un usuario.
     *
     * @param user Instancia con los datos del usuario.
     */
    public void readRoles(User user);

    /**
     * Inserta los examenes relacionados al usuario
     *
     * @param excludeList lista de examenes
     *
     * @return registros insertados
     */
    default int insertTest(List<ExcludeTest> excludeList)
    {
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab72");
        excludeList.stream().map((user)
                ->
        {
            HashMap parameters = new HashMap();
            parameters.put("lab39c1", user.getTest().getId());
            parameters.put("lab04c1", user.getUser().getId());
            return parameters;
        }).forEachOrdered((parameters)
                ->
        {
            batchArray.add(parameters);
        });
        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[excludeList.size()]));
        return inserted.length;
    }

    /**
     * Elimina los examenes relacionados al usuario
     *
     * @param user {@link net.cltech.enterprisent.domain.masters.user.User}
     */
    default void changeStateUser(User user)
    {

        getJdbcTemplate().update("UPDATE lab04 SET lab07c1 = ? WHERE lab04c1 = ? ", user.isState() ? 1 : 0, user.getId());
    }

    /**
     * Elimina los examenes relacionados al usuario
     *
     * @param id id usuario
     *
     * @return registros eliminados
     */
    default int deleteTest(Integer id)
    {
        String deleteSql = "DELETE FROM lab72 WHERE lab04c1 = ? ";
        return getJdbcTemplate().update(deleteSql, id);
    }

    /**
     * lista los examenes relacionados al usuario
     *
     * @param id Id del usuario
     *
     * @return lista de examenes.
     */
    default List<ExcludeTest> listTest(Integer id)
    {
        try
        {
            String query = "SELECT  lab39.lab39c1,lab39c2,lab39c3,lab39c4, lab72.lab04c1, lab04.lab04c2, lab04.lab04c3 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab72 ON lab39.lab39c1 = lab72.lab39c1 AND lab72.lab04c1 = ?  "
                    + "LEFT JOIN lab04 ON lab72.lab04c1 = lab04.lab04c1 "
                    + "WHERE lab39.lab07c1 = 1 ";

            return getJdbcTemplate().query(query, (ResultSet rs, int i)
                    ->
            {
                ExcludeTest bean = new ExcludeTest();
                bean.getTest().setSelected(rs.getString("lab04c1") != null);
                bean.getTest().setAbbr(rs.getString("lab39c3"));
                bean.getTest().setName(rs.getString("lab39c4"));
                bean.getTest().setCode(rs.getString("lab39c2"));
                bean.getTest().setId(rs.getInt("lab39c1"));
                AuthorizedUser user = new AuthorizedUser();
                user.setId(id);
                user.setName((rs.getString("lab04c2") == null ? "" : rs.getString("lab04c2")) + (rs.getString("lab04c3") == null ? "" : " " + rs.getString("lab04c3")));
                bean.setUser(user);
                return bean;
            }, id);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
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
                    }, (ResultSet rs, int i)
                    ->
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
        } catch (EmptyResultDataAccessException ex)
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
                    }, (ResultSet rs, int i)
                    ->
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
                    }, (ResultSet rs, int i)
                    ->
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
        } catch (EmptyResultDataAccessException ex)
        {
            user.setBranches(new ArrayList<>());
        }
    }

    /**
     * Actualiza el usuario que se va recuperar la contraseña
     *
     * @param password contraseña aleatoria para asignar al usuario
     * @param username Username del usuario
     * @param email email del usuario
     *
     */
    default int updatePasswordRecovery(String password, String username, String email) throws Exception
    {
        String update = "UPDATE lab04 set lab04c5 = ?, lab04c33  = ? WHERE lab04c4 = ? AND lab04c11 = ?";
        return getJdbcTemplate().update(update,
                new Object[]
                {
                    Tools.encrypt(password),
                    1,
                    username,
                    email
                });
    }

    /**
     * Realiza la consulta que trae los usuarios por sede y areas
     *
     * @param filter Objeto con informacion del filtro ha realizar
     *
     * @return Instancia la lista de usaurios.
     * @throws Exception Error en la base de datos.
     */
    default List<User> getByBranchAreas(UserByBranchByAreas filter) throws Exception
    {
        try
        {
            String sql = "SELECT lab04.lab04c1"
                    + ",lab04c2"
                    + ",lab04c3"
                    + ",lab04c4"
                    + ",lab04c5"
                    + ", lab04c14"
                    + ", lab04.lab07c1"
                    + ",lab04.lab04c6"
                    + ",lab04c7"
                    + ",lab04c8"
                    + ",lab04c17"
                    + ", lab103c1 "
                    + " FROM lab04 "
                    + " INNER JOIN lab93 ON lab93.lab04c1 = lab04.lab04c1 "
                    + " WHERE lab05c1 = ? AND (SELECT COUNT(*) FROM lab69 WHERE lab69.lab04c1 = lab04.lab04c1 AND lab43c1 IN (" + filter.getAreas().stream().map(a -> a.toString()).collect(Collectors.joining(",")) + ")) = ? GROUP BY lab04.lab04c1 ORDER BY lab04.lab04c1 ";
            return getJdbcTemplate().query(sql, new Object[]
            {
                filter.getIdbranch(),
                filter.getAreas().size()
            }, (ResultSet rs, int i)
                    ->
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
//                /*Foto*/
//                String photo64 = "";
//                byte[] photoBytes = rs.getBytes("lab04c16");
//                if (photoBytes != null)
//                {
//                    photo64 = Base64.getEncoder().encodeToString(photoBytes);
//                }
//                user.setPhoto(photo64);
                return user;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Registra una nueva usuario por tipo en la base de datos.
     *
     * @param user Instancia con los datos del usuario.
     * @param days Dias de expiracion de la contraseña.
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    default User createByType(User user, int days) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(user.getActivation());
        calendar.add(Calendar.DAY_OF_YEAR, days);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab04")
                .usingColumns("lab04c2", "lab04c3", "lab04c4", "lab04c5", "lab04c6", "lab04c7", "lab04c8", "lab04c9", "lab04c10", "lab04c15", "lab04c16", "lab04c17", "lab04c18", "lab04c19", "lab04c20", "lab04c21", "lab04c22", "lab04c23", "lab04c24", "lab04c1_1", "lab07c1", "lab04c25", "lab04c29", "lab04c30", "lab04c31", "lab04c36", "lab04c37", "lab04c38", "lab04c39")
                .usingGeneratedKeyColumns("lab04c1");

        HashMap parameters = new HashMap();
        parameters.put("lab04c2", user.getName().trim());
        parameters.put("lab04c3", "");
        parameters.put("lab04c4", user.getUserName().trim());
        parameters.put("lab04c5", user.getPassword() == null ? "" : Tools.encrypt(user.getPassword().trim()));
        parameters.put("lab07c1", 1);
        parameters.put("lab04c6", user.getActivation());
        parameters.put("lab04c7", user.getExpiration());
        parameters.put("lab04c8", user.getPasswordExpiration() == null ? new Timestamp(calendar.getTime().getTime()) :  user.getPasswordExpiration());
        parameters.put("lab04c9", timestamp);
        parameters.put("lab04c1_1", user.getUser().getId());
        parameters.put("lab04c10", user.getIdentification());
        parameters.put("lab04c15", user.getType().getId());
        if (user.getPhoto() != null)
        {
            byte[] photoByte = DatatypeConverter.parseBase64Binary(user.getPhoto());
            parameters.put("lab04c16", photoByte);
        } else
        {
            parameters.put("lab04c16", null);
        }
        parameters.put("lab04c17", 0);
        parameters.put("lab04c18", 0);
        parameters.put("lab04c19", 0);
        parameters.put("lab04c20", 0);
        parameters.put("lab04c21", 0);
        parameters.put("lab04c22", 0);
        parameters.put("lab04c23", 0);
        parameters.put("lab04c24", 0);
        parameters.put("lab103c1", 0);
        parameters.put("lab04c25", user.getDestination() == null ? 0 : user.getDestination().getId());
        parameters.put("lab04c29", 0);
        parameters.put("lab04c30", user.getLaboratory());
        parameters.put("lab04c31", user.isDashboard());
        parameters.put("lab04c32", user.isPreValidationRequired() ? 1 : 0);
        parameters.put("lab04c36", user.isEditOrderCash() ? 1 : 0);
        parameters.put("lab04c37", user.getDemographicQuery());
        parameters.put("lab04c38", user.getDemographicItemQuery());
        parameters.put("lab04c39", user.isRemoveCashBox()? 1 : 0);

        Number key = insert.executeAndReturnKey(parameters);
        user.setId(key.intValue());
        user.setLastTransaction(timestamp);
        insertBranchesIntegration(user);
        return user;
    }
    
    /**
     * Asociar sedes a un usuario.
     *
     * @param user Instancia con los datos del usuario.
     */
    default void insertBranchesIntegration(User user)
    {
        
            for (BranchByUser branch : user.getBranches())
            {
                SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                        .withTableName("lab93")
                        .usingColumns("lab93c1", "lab93c2", "lab04c1", "lab05c1");

                HashMap parameters = new HashMap();
                parameters.put("lab93c1", branch.isAccess() ? 1 : 0);
                parameters.put("lab93c2", branch.isBatchPrint() ? 1 : 0);
                parameters.put("lab04c1", user.getId());
                parameters.put("lab05c1", branch.getBranch().getId());

                insert.execute(parameters);
            }

        

    }

    /**
     * Actualiza la información de un usuario por tipo en la base de datos.
     *
     * @param user Instancia con los datos del usuario.
     * @param days Dias de expiracion de la contraseña.
     *
     * @return Objeto del usuario modificada.
     * @throws Exception Error en la base de datos.
     */
    default User updateByType(User user, int days) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, days);

        if (user.getPassword() != null)
        {
            getJdbcTemplate().update("UPDATE lab04 SET lab04c2 = ?, lab04c3 = '', lab04c4 = ?, lab04c5 = ?, lab04c6 = ?, lab04c7 = ?, lab04c8 = ?, lab04c9 = ?, lab04c10 = ?, lab04c15 = ?, lab04c16 = ?, lab04c25 = ?, lab04c30 = ?, lab04c31 = ?, lab07c1 = ?, lab04c1_1 = ?, lab04c32 = ?,  lab04c36 = ?,  lab04c37 = ?,  lab04c38 = ?, lab04c39 = ?  WHERE lab04c1 = ?", user.getName(), user.getUserName(), user.getPassword() == null ? "" : Tools.encrypt(user.getPassword()), user.getActivation(), user.getExpiration(), new Timestamp(calendar.getTime().getTime()), timestamp, user.getIdentification(), user.getType().getId(), user.getPhoto() == null ? null : DatatypeConverter.parseBase64Binary(user.getPhoto()), user.getDestination() == null ? null : user.getDestination().getId(), user.getLaboratory(), user.isDashboard() ? 1 : 0, user.isState() ? 1 : 0, user.getUser().getId(), user.isPreValidationRequired() ? 1 : 0, user.isEditOrderCash() ? 1 : 0, user.getDemographicQuery(), user.getDemographicItemQuery(), user.isRemoveCashBox() ? 1 : 0, user.getId());
        } else
        {
            getJdbcTemplate().update("UPDATE lab04 SET lab04c2 = ?, lab04c3 = '', lab04c4 = ?, lab04c6 = ?, lab04c7 = ?, lab04c9 = ?, lab04c10 = ?, lab04c15 = ?, lab04c16 = ?,lab04c25 = ?, lab04c30 = ?, lab04c31 = ?, lab07c1 = ?, lab04c1_1 = ?, lab04c32 = ?, lab04c36 = ?,  lab04c37 = ?,  lab04c38 = ?, lab04c39 = ?  WHERE lab04c1 = ?", user.getName(), user.getUserName(), user.getActivation(), user.getExpiration(), timestamp, user.getIdentification(), user.getType().getId(), user.getPhoto() == null ? null : DatatypeConverter.parseBase64Binary(user.getPhoto()), user.getDestination() == null ? null : user.getDestination().getId(), user.getLaboratory(), user.isDashboard() ? 1 : 0, user.isState() ? 1 : 0, user.getUser().getId(), user.isPreValidationRequired() ? 1 : 0, user.isEditOrderCash() ? 1 : 0, user.getDemographicQuery(), user.getDemographicItemQuery(), user.isRemoveCashBox() ? 1 : 0, user.getId());
        }
        user.setLastTransaction(timestamp);

        return user;
    }

    /**
     * Obtiene una lista con los usuarios de analizadores con destino en
     * microbiologia
     *
     * @return Lista de usuarios
     * @throws java.lang.Exception
     */
    default List<UserAnalyzer> getUsersAnalyzers() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab04.lab04c1 AS lab04c1, ")
                    .append("lab04.lab04c2 AS lab04c2, ")
                    .append("lab04.lab04c3 AS lab04c3, ")
                    .append("lab04.lab04c4 AS lab04c4, ")
                    .append("lab04.lab04c10 AS lab04c10, ")
                    .append("lab04.lab04c30 AS lab04c30, ")
                    .append("lab04.lab04c25 AS lab04c25, ")
                    .append("lab40.lab40c3 AS lab40c3 ")
                    .append("FROM lab04 ")
                    .append("LEFT JOIN lab40 ON lab04.lab04c30 = lab40.lab40c1 ")
                    .append("WHERE lab04c15 = 12 and lab04.lab07c1 = 1");
            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                UserAnalyzer user = new UserAnalyzer();
                user.setUserId(rs.getInt("lab04c1"));
                user.setUserName(rs.getString("lab04c4"));
                user.setNames(rs.getString("lab04c2"));
                user.setLastNames(rs.getString("lab04c3"));
                user.setIdentification(rs.getString("lab04c10"));
                user.setReferenceLaboratory(rs.getInt("lab04c30"));
                user.setNameReferenceLaboratory(rs.getString("lab40c3"));
                user.setDestination(rs.getString("lab04c25") == null ? 0 : rs.getInt("lab04c25"));
                return user;
            });
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtener area asociadas a un usuario.
     *
     * @param user Instancia con los datos del usuario.
     */
    default void readAreas(User user)
    {
        try
        {
            user.setAreas(getJdbcTemplate().query("SELECT lab43.lab07c1, lab69c1, lab69c2, lab69.lab04c1, lab43.lab43c1, lab43.lab43c2, lab43.lab43c3, lab43.lab43c4 FROM lab43 "
                    + "LEFT JOIN lab69 ON lab69.lab43c1 = lab43.lab43c1 AND lab69.lab04c1 = ? "
                    + "WHERE lab43.lab43c1 != 1",
                    new Object[]
                    {
                        user.getId()
                    }, (ResultSet rs, int i)
                    ->
            {
                AreaByUser areaByUser = new AreaByUser();
                areaByUser.setAccess(rs.getInt("lab69c1") == 1);
                areaByUser.setValidate(rs.getInt("lab69c2") == 1);

                /*Usuario*/
                areaByUser.getUser().setId(rs.getInt("lab04c1"));

                /*Area*/
                areaByUser.getArea().setId(rs.getInt("lab43c1"));
                areaByUser.getArea().setOrdering(rs.getShort("lab43c2"));
                areaByUser.getArea().setAbbreviation(rs.getString("lab43c3"));
                areaByUser.getArea().setName(rs.getString("lab43c4"));
                areaByUser.getArea().setState(rs.getInt("lab07c1") == 1);
                return areaByUser;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            user.setAreas(new ArrayList<>());
        }
    }

    public JdbcTemplate getJdbcTemplate();

}

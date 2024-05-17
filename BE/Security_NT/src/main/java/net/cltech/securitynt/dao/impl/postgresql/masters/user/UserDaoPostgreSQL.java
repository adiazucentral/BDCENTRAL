package net.cltech.securitynt.dao.impl.postgresql.masters.user;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;
import net.cltech.securitynt.dao.interfaces.masters.user.UserDao;
import net.cltech.securitynt.domain.masters.user.AreaByUser;
import net.cltech.securitynt.domain.masters.user.BranchByUser;
import net.cltech.securitynt.domain.masters.user.RoleByUser;
import net.cltech.securitynt.domain.masters.user.User;
import net.cltech.securitynt.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro
 * Usuario para PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 25/04/2017
 * @see Creación
 *
 * @author cmartin
 * @version 1.0.0
 * @since 12/05/2017
 * @see Se agregaron metodos para el funcionamiento maestro usuario.
 */
@Repository
public class UserDaoPostgreSQL implements UserDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public User get(Integer id, String username, String identification, String signatureCode) throws Exception
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

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
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
    
    
    @Override
    public User getWeb(Integer id, String username, String identification, String signatureCode) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT u.lab181c1, u.lab181c2, u.lab181c3, u.lab181c4, u.lab181c5, u.lab181c6, u.lab181c9, u.lab181c10, u.lab181c11, u.lab181c12"
                    + " FROM lab181 as u " ;                
                    
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE u.lab181c1 = ? ";
            }
            if (username != null)
            {
                query = query + "WHERE UPPER(u.lab181c2) = UPPER(?) ";
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
                object = username;
            }
            

            return jdbc.queryForObject(query,
                    new Object[]
                    {
                        object
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


                
                 /*Usuario*/
                user.getUser().setId(rs.getInt("lab181c1"));             
                user.getUser().setUserName(rs.getString("lab181c2"));
               

               

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
    @Override
    public void readRoles(User user)
    {
        try
        {
            user.setRoles(jdbc.query("SELECT lab84c1, lab84.lab04c1, lab82.lab82c1, lab82.lab82c2, lab82.lab82c3 FROM lab82 "
                    + "LEFT JOIN lab84 ON lab84.lab82c1 = lab82.lab82c1 "
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
     * Obtener area asociadas a un usuario.
     *
     * @param user Instancia con los datos del usuario.
     */
    private void readAreas(User user)
    {
        try
        {
            user.setAreas(jdbc.query("SELECT lab43.lab07c1, lab69c1, lab69c2, lab69.lab04c1, lab43.lab43c1, lab43.lab43c2, lab43.lab43c3, lab43.lab43c4 FROM lab43 "
                    + "LEFT JOIN lab69 ON lab69.lab43c1 = lab43.lab43c1 AND lab69.lab04c1 = ? "
                    + "WHERE lab43.lab43c1 != 1",
                    new Object[]
                    {
                        user.getId()
                    }, (ResultSet rs, int i) ->
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

    @Override
    public User create(User user, int days) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(user.getActivation());
        calendar.add(Calendar.DAY_OF_YEAR, days);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                .withTableName("lab04")
                .usingColumns("lab04c2", "lab04c3", "lab04c4", "lab04c5", "lab04c6", "lab04c7", "lab04c8", "lab04c9", "lab04c10", "lab04c11", "lab04c12", "lab04c13", "lab04c14", "lab04c15", "lab04c16", "lab04c17", "lab04c18", "lab04c19", "lab04c20", "lab04c21", "lab04c22", "lab04c23", "lab04c24", "lab04c1_1", "lab07c1")
                .usingGeneratedKeyColumns("lab04c1");

        HashMap parameters = new HashMap();
        parameters.put("lab04c2", user.getName().trim());
        parameters.put("lab04c3", user.getLastName().trim());
        parameters.put("lab04c4", user.getUserName().trim());
        parameters.put("lab04c5", Tools.encrypt(user.getPassword().trim()));
        parameters.put("lab07c1", 1);
        parameters.put("lab04c6", user.getActivation());
        parameters.put("lab04c7", user.getExpiration());
        parameters.put("lab04c8", new Timestamp(calendar.getTime().getTime()));
        parameters.put("lab04c9", timestamp);
        parameters.put("lab04c1_1", user.getUser().getId());
        parameters.put("lab04c10", user.getIdentification());
        parameters.put("lab04c11", user.getEmail());
        if (user.getSignature() != null)
        {
            byte[] signatureByte = DatatypeConverter.parseBase64Binary(user.getSignature());
            parameters.put("lab04c12", signatureByte);
        } else
        {
            parameters.put("lab04c12", null);
        }
        parameters.put("lab04c13", user.getSignatureCode());
        parameters.put("lab04c14", user.getMaxDiscount());
        parameters.put("lab04c15", user.getType().getId());
        if (user.getPhoto() != null)
        {
            byte[] photoByte = DatatypeConverter.parseBase64Binary(user.getPhoto());
            parameters.put("lab04c16", photoByte);
        } else
        {
            parameters.put("lab04c16", null);
        }
        parameters.put("lab04c17", user.isConfidential() ? 1 : 0);
        parameters.put("lab04c18", user.isPrintInReports() ? 1 : 0);
        parameters.put("lab04c19", user.isAddExams() ? 1 : 0);
        parameters.put("lab04c20", user.isSecondValidation() ? 1 : 0);
        parameters.put("lab04c21", user.isEditPatients() ? 1 : 0);
        parameters.put("lab04c22", user.isQuitValidation() ? 1 : 0);
        parameters.put("lab04c23", user.isCreatingItems() ? 1 : 0);
        parameters.put("lab04c24", user.isPrintResults() ? 1 : 0);
        parameters.put("lab103c1", user.getOrderType().getId());

        Number key = insert.executeAndReturnKey(parameters);
        user.setId(key.intValue());
        user.setLastTransaction(timestamp);

        insertAreas(user);
        insertRoles(user);
        insertBranches(user);

        return user;
    }

    /**
     * Asociar areas a un usuario.
     *
     * @param user Instancia con los datos del usuario.
     */
    private void insertAreas(User user) throws Exception
    {
        deleteAreas(user.getId());
        for (AreaByUser area : user.getAreas())
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                    .withTableName("lab69")
                    .usingColumns("lab69c1", "lab69c2", "lab04c1", "lab43c1");

            HashMap parameters = new HashMap();
            parameters.put("lab69c1", area.isAccess() ? 1 : 0);
            parameters.put("lab69c2", area.isValidate() ? 1 : 0);
            parameters.put("lab04c1", user.getId());
            parameters.put("lab43c1", area.getArea().getId());

            insert.execute(parameters);
        }

    }

    /**
     * Asociar roles a un usuario.
     *
     * @param user Instancia con los datos del usuario.
     */
    private void insertRoles(User user) throws Exception
    {
        deleteRoles(user.getId());
        for (RoleByUser rol : user.getRoles())
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
                    .withTableName("lab84")
                    .usingColumns("lab84c1", "lab04c1", "lab82c1");

            HashMap parameters = new HashMap();
            parameters.put("lab84c1", rol.isAccess() ? 1 : 0);
            parameters.put("lab04c1", user.getId());
            parameters.put("lab82c1", rol.getRole().getId());

            insert.execute(parameters);
        }

    }

    /**
     * Asociar sedes a un usuario.
     *
     * @param user Instancia con los datos del usuario.
     */
    private void insertBranches(User user) throws Exception
    {
        deleteBranchs(user.getId());
        for (BranchByUser branch : user.getBranches())
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbc)
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
     * Obtener sedes asociadas a un usuario.
     *
     * @param idUser Instancia con los datos del usuario.
     */
    public void deleteAreas(Integer idUser) throws Exception
    {
        String query = " DELETE FROM lab69 WHERE lab04c1 = " + idUser;
        jdbc.execute(query);
    }

    public void deleteRoles(Integer idUser) throws Exception
    {
        String query = " DELETE FROM lab84 WHERE lab04c1 = " + idUser;
        jdbc.execute(query);
    }

    public void deleteBranchs(Integer idUser) throws Exception
    {
        String query = ""
                + " DELETE FROM lab93 WHERE lab04c1 = " + idUser;
        jdbc.execute(query);
    }

    @Override
    public User update(User user, int days) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, days);

        if (user.getPassword() != null)
        {
            jdbc.update("UPDATE lab04 SET lab04c2 = ?, lab04c3 = ?, lab04c4 = ?, lab04c5 = ?, lab04c6 = ?, lab04c7 = ?, lab04c8 = ?, lab04c9 = ?, lab04c10 = ?, lab04c11 = ?, lab04c12 = ?, lab04c13 = ?, lab04c14 = ?, lab04c15 = ?, lab04c16 = ?, lab04c17 = ?, lab04c18 = ?, lab04c19 = ?, lab04c20 = ?, lab04c21 = ?, lab04c22 = ?, lab04c23 = ?, lab04c24 = ?, lab07c1 = ?, lab04c1_1 = ?, lab103c1 = ? "
                    + "WHERE lab04c1 = ?",
                    user.getName(), user.getLastName(), user.getUserName(), Tools.encrypt(user.getPassword()), user.getActivation(), user.getExpiration(), new Timestamp(calendar.getTime().getTime()), timestamp, user.getIdentification(), user.getEmail(), user.getSignature() == null ? null : DatatypeConverter.parseBase64Binary(user.getSignature()), user.getSignatureCode(), user.getMaxDiscount(), user.getType().getId(), user.getPhoto() == null ? null : DatatypeConverter.parseBase64Binary(user.getPhoto()), user.isConfidential() ? 1 : 0, user.isPrintInReports() ? 1 : 0, user.isAddExams() ? 1 : 0, user.isSecondValidation() ? 1 : 0, user.isEditPatients() ? 1 : 0, user.isQuitValidation() ? 1 : 0, user.isCreatingItems() ? 1 : 0, user.isPrintResults() ? 1 : 0, user.isState() ? 1 : 0, user.getUser().getId(), user.getOrderType().getId(), user.getId()
            );
        } else
        {
            jdbc.update("UPDATE lab04 SET lab04c2 = ?, lab04c3 = ?, lab04c4 = ?, lab04c6 = ?, lab04c7 = ?, lab04c9 = ?, lab04c10 = ?, lab04c11 = ?, lab04c12 = ?, lab04c13 = ?, lab04c14 = ?, lab04c15 = ?, lab04c16 = ?, lab04c17 = ?, lab04c18 = ?, lab04c19 = ?, lab04c20 = ?, lab04c21 = ?, lab04c22 = ?, lab04c23 = ?, lab04c24 = ?, lab07c1 = ?, lab04c1_1 = ?, lab103c1 = ? "
                    + "WHERE lab04c1 = ?",
                    user.getName(), user.getLastName(), user.getUserName(), user.getActivation(), user.getExpiration(), timestamp, user.getIdentification(), user.getEmail(), user.getSignature() == null ? null : DatatypeConverter.parseBase64Binary(user.getSignature()), user.getSignatureCode(), user.getMaxDiscount(), user.getType().getId(), user.getPhoto() == null ? null : DatatypeConverter.parseBase64Binary(user.getPhoto()), user.isConfidential() ? 1 : 0, user.isPrintInReports() ? 1 : 0, user.isAddExams() ? 1 : 0, user.isSecondValidation() ? 1 : 0, user.isEditPatients() ? 1 : 0, user.isQuitValidation() ? 1 : 0, user.isCreatingItems() ? 1 : 0, user.isPrintResults() ? 1 : 0, user.isState() ? 1 : 0, user.getUser().getId(), user.getOrderType().getId(), user.getId()
            );
        }
        user.setLastTransaction(timestamp);

        insertAreas(user);
        insertRoles(user);
        insertBranches(user);

        return user;
    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }

}

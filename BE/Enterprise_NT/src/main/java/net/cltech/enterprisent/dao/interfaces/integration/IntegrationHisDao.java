/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.integration;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.xml.bind.DatatypeConverter;
import net.cltech.enterprisent.domain.integration.his.UserHis;
import net.cltech.enterprisent.domain.integration.his.UserStatus;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a la base de datos para la integracion con
 * el his
 *
 * @version 1.0.0
 * @author omendez
 * @since 01/02/2021
 * @see Creación
 */
public interface IntegrationHisDao
{

    public JdbcTemplate getJdbcTemplate();

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param user Instancia con los datos del usuario.
     *
     * @return Instancia con los datos del usuario.
     */
    default UserHis createUser(UserHis user)
    {
        try
        {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 365);

            Timestamp timestamp = new Timestamp(new Date().getTime());
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab04")
                    .usingColumns("lab04c2", "lab04c3", "lab04c4", "lab04c5", "lab04c6", "lab04c7", "lab04c8", "lab07c1", "lab04c9", "lab04c11", "lab04c12", "lab04c15", "lab04c16", "lab04c17", "lab04c18", "lab04c19", "lab04c20", "lab04c21", "lab04c22", "lab04c23", "lab04c24", "lab04c29", "lab04c31")
                    .usingGeneratedKeyColumns("lab04c1");

            HashMap parameters = new HashMap();
            parameters.put("lab04c2", user.getNombres().trim());
            parameters.put("lab04c3", user.getApellidos().trim());
            parameters.put("lab04c4", user.getUsuario().trim());
            parameters.put("lab04c5", Tools.encrypt(user.getContraseña().trim()));
            parameters.put("lab04c6", timestamp);
            parameters.put("lab04c7", new Timestamp(calendar.getTime().getTime()));
            parameters.put("lab04c8", new Timestamp(calendar.getTime().getTime()));
            parameters.put("lab07c1", 1);
            parameters.put("lab04c9", timestamp);
            parameters.put("lab04c11", user.getCorreo());
            parameters.put("lab04c12", null);
            parameters.put("lab04c15", 11);
            if (user.getFoto() != null)
            {
                byte[] photoByte = DatatypeConverter.parseBase64Binary(user.getFoto());
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
            parameters.put("lab04c29", 0);
            parameters.put("lab04c31", 0);

            Number key = insert.executeAndReturnKey(parameters);
            user.setId(key.intValue());

            insertRoles(user);
            return user;
        } catch (Exception e)
        {
            return null;
        }
    }

    default UserHis updateUser(UserHis user) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab04 SET lab04c2 = ?, "
                + "lab04c3 = ?, "
                + "lab04c5 = ?, "
                + "lab04c11 = ?, "
                + "lab04c16 = ?, "
                + "lab04c4 = ?, "
                + "lab04c9 = ? "
                + "WHERE lab04c1 = ?",
                user.getNombres(),
                user.getApellidos(),
                user.getContraseña(),
                user.getCorreo(),
                "".equals(user.getFoto()) ? null : user.getFoto(),
                user.getUsuario(),
                timestamp,
                user.getId());
        return user;
    }

    /**
     * Asociar roles a un usuario.
     *
     * @param user Instancia con los datos del usuario.
     */
    default void insertRoles(UserHis user) throws Exception
    {
        deleteRoles(user.getId());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab84")
                .usingColumns("lab84c1", "lab04c1", "lab82c1");

        HashMap parameters = new HashMap();
        parameters.put("lab84c1", 1);
        parameters.put("lab04c1", user.getId());
        parameters.put("lab82c1", 1);
        insert.execute(parameters);
    }

    default void deleteRoles(Integer idUser) throws Exception
    {
        String query = " DELETE FROM lab84 WHERE lab04c1 = " + idUser;
        getJdbcTemplate().execute(query);
    }

    default void changeStateUser(UserStatus user)
    {
        getJdbcTemplate().update("UPDATE lab04 SET lab07c1 = ? WHERE lab04c4 = ? ", user.getEstado(), user.getUsuario());
    }

    default Integer getBranchId(String code) throws Exception
    {
        try
        {
            String query = "SELECT lab05c1 "
                    + "FROM lab05 "
                    + "WHERE lab05c10 = ?";

            return getJdbcTemplate().queryForObject(query, (ResultSet rs, int i) ->
            {
                Integer branchId = rs.getInt("lab05c1");
                return branchId;
            }, code);
        } catch (EmptyResultDataAccessException ex)
        {
            return 0;
        }
    }

    default void deleteBranchByUser(Integer idUser) throws Exception
    {
        String query = " DELETE FROM lab93 WHERE lab04c1 = " + idUser;
        getJdbcTemplate().execute(query);
    }

    /**
     * Asociar sedes a un usuario.
     *
     * @param userId
     * @param branchId
     *
     * @throws java.lang.Exception
     */
    default void insertBranches(int userId, int branchId) throws Exception
    {
        //deleteBranchs(userId);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab93");

        HashMap parameters = new HashMap();
        parameters.put("lab93c1", 1);
        parameters.put("lab93c2", 0);
        parameters.put("lab04c1", userId);
        parameters.put("lab05c1", branchId);
        insert.execute(parameters);
    }

    default Branch update(Branch branch) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab05 SET lab05c2 = ?, "
                + "lab05c4 = ?, "
                + "lab05c5 = ?, "
                + "lab05c6 = ?, "
                + "lab05c9 = ?, "
                + "lab04c1 = ? "
                + "WHERE lab05c1 = ?",
                branch.getAbbreviation(),
                branch.getName(),
                branch.getAddress(),
                branch.getPhone(),
                timestamp,
                branch.getUser().getId(),
                branch.getId());

        branch.setLastTransaction(timestamp);

        return branch;
    }
    
    
    

   

}

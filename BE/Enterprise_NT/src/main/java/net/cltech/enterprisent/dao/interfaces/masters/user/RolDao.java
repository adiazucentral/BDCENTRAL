package net.cltech.enterprisent.dao.interfaces.masters.user;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.user.Module;
import net.cltech.enterprisent.domain.masters.user.Role;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los metodos de acceso a base de datos
 *
 * @version 1.0.0
 * @author eacuna
 * @since 05/05/2017
 * @see Creación
 */
public interface RolDao
{

    /**
     * Nuevo registro en la base de datos.
     *
     * @param rol Instancia con los datos del rol.
     *
     * @return {@link net.cltech.enterprisent.domain.masters.user.Role}
     * @throws Exception Error en la base de datos.
     */
    default Role create(Role rol) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab82")
                .usingColumns("lab82c2", "lab82c3", "lab82c4", "lab04c1", "lab07c1")
                .usingGeneratedKeyColumns("lab82c1");

        Date modificationDate = new Date();
        HashMap params = new HashMap();
        params.put("lab82c2", rol.getName().trim());
        params.put("lab82c3", (rol.isAdministrator() ? 1 : 0));
        params.put("lab82c4", new Timestamp(modificationDate.getTime()));
        params.put("lab04c1", rol.getUser().getId());
        params.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(params);

        rol.setId(key.intValue());
        rol.setLastTransaction(modificationDate);
        createRoleModules(rol);

        return rol;
    }

    /**
     * Lista los roles desde la base de datos.
     *
     * @return Lista de roles.
     * @throws Exception Error en la base de datos.
     */
    default List<Role> list() throws Exception
    {
        try
        {
            String sql = "SELECT lab82c1,lab82c2,lab82c3,lab82c4,lab82.lab07c1, lab04.lab04c1,lab04c2,lab04c3,lab04c4 "
                    + " FROM lab82 "
                    + " INNER JOIN lab04 ON lab04.lab04c1 = lab82.lab04c1";
            return getJdbcTemplate().query(sql, (ResultSet rs, int i) ->
            {
                Role result = new Role();
                AuthorizedUser user = new AuthorizedUser();

                user.setId(rs.getInt("lab04c1"));
                user.setName(rs.getString("lab04c2"));
                user.setLastName(rs.getString("lab04c3"));
                user.setUserName(rs.getString("lab04c4"));

                result.setId(rs.getInt("lab82c1"));
                result.setName(rs.getString("lab82c2"));
                result.setAdministrator(rs.getInt("lab82c3") == 1);
                result.setState(rs.getInt("lab07c1") == 1);
                result.setLastTransaction(rs.getTimestamp("lab82c4"));
                result.setUser(user);

                return result;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Actualiza la información del requisito en la base de datos.
     *
     *
     * @param rol Instancia con los datos del rol.
     *
     * @return rol actualizado
     *
     * @throws Exception Error en la base de datos.
     */
    default Role update(Role rol) throws Exception
    {
        Date modificationDate = new Date();
        getJdbcTemplate().update(" UPDATE lab82 \n"
                + " SET lab82c2 = ?, lab82c3 = ?, lab82c4 = ?, lab07c1 = ?, lab04c1 = ? \n"
                + " WHERE lab82c1 = ? ",
                rol.getName().trim(), rol.isAdministrator() ? 1 : 0, new Timestamp(modificationDate.getTime()), rol.isState() ? 1 : 0,
                rol.getUser().getId(), rol.getId());
        rol.setLastTransaction(modificationDate);
        createRoleModules(rol);
        return rol;
    }

    /**
     * Nuevo registro de Modulos por Rol en la base de datos.
     *
     * @param rol Instancia con los datos del rol y modulos a insertar.
     *
     * @return {@link net.cltech.enterprisent.domain.masters.user.Role}
     * @throws Exception Error en la base de datos.
     */
    default int createRoleModules(Role rol) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();

        deleteRoleModules(rol.getId());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab86");
        for (Module module : rol.getModules())
        {
            HashMap parameters = new HashMap();
            parameters.put("lab85c1", module.getId());
            parameters.put("lab82c1", rol.getId());
            batchArray.add(parameters);
        }

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[rol.getModules().size()]));

        return inserted.length;

    }

    /**
     * Obtiene modulos asociados al rol.
     *
     * @param id id del rol
     *
     * @return Lista de Módulos
     */
    default List<Module> listRoleModules(Integer id)
    {

        try
        {
            String query = "  SELECT lab85.lab85c1 "
                    + ", lab86.lab82c1"
                    + ", lab85.lab85c2"
                    + ", lab85.lab85c3"
                    + ", lab82.lab82c2"
                    + ", lab82.lab82c3"
                    + ", lab82.lab82c4"
                    + ", lab82.lab07c1"
                    + " FROM lab85"
                    + " INNER JOIN lab86 ON lab85.lab85c1=lab86.lab85c1 "
                    + " INNER JOIN lab82 ON lab82.lab82c1=lab86.lab82c1 "
                    + " WHERE lab86.lab82c1 = ? ";

            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                Module module = new Module();
                module.setId(rs.getInt("lab85c1"));
                module.setIdFather(rs.getInt("lab85c2"));
                module.setName(rs.getString("lab85c3"));
                module.setAccess(rs.getInt("lab82c1") != 0);
                return module;
            },
                    id);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Elimina asignación de módulos de un Rol
     *
     * @param idRol id del ROL
     *
     * @return número de registros afectados
     * @throws Exception Error BD
     */
    default int deleteRoleModules(Integer idRol) throws Exception
    {
        String query = " DELETE FROM lab86 WHERE lab82c1 = ?";

        return getJdbcTemplate().update(query, idRol);
    }

    /**
     * Obtiene la conexion JDBC
     *
     * @return conexion a base de datos
     */
    public JdbcTemplate getJdbcTemplate();

}

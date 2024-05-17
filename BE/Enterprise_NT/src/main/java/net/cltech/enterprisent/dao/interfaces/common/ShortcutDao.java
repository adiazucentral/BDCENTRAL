package net.cltech.enterprisent.dao.interfaces.common;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.access.Shortcut;
import net.cltech.enterprisent.domain.masters.user.Module;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * accesos directos.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 19/09/2017
 * @see Creación
 */
public interface ShortcutDao
{

    /**
     * Lista accesos directos de un usuario
     *
     * @param user id del usuario
     *
     * @return Lista de Modulos.
     * @throws Exception Error en la base de datos.
     */
    default List<Module> list(int user) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab125c1, lab04c1, lab125.lab85c1, lab85c3 "
                    + "FROM lab125 "
                    + "INNER JOIN lab85 ON lab85.lab85c1 = lab125.lab85c1 "
                    + "WHERE lab125.lab04c1 = ?", (ResultSet rs, int i) ->
            {
                Module module = new Module();
                module.setIdFather(rs.getInt("lab125c1"));
                module.setId(rs.getInt("lab85c1"));
                module.setName(rs.getString("lab85c3"));
                return module;
            }, user);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra uno nuevo shortcut en la base de datos.
     *
     * @param shortcut Instancia con los datos del shortcut.
     *
     * @return Instancia con los datos.
     * @throws Exception Error en la base de datos.
     */
    default int create(Shortcut shortcut) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab125");

        HashMap parameters = new HashMap();
        parameters.put("lab125c1", shortcut.getModule());
        parameters.put("lab04c1", shortcut.getUser());
        parameters.put("lab85c1", shortcut.getForm());

        return insert.execute(parameters);
    }

    /**
     *
     * Elimina un shortcut de la base de datos.
     *
     *
     * @param shortcut instancia objeto con información
     *
     * @return registros afectados
     * @throws Exception Error en base de datos.
     */
    default int delete(Shortcut shortcut) throws Exception
    {
        return getJdbcTemplate().update("DELETE FROM lab125 WHERE lab04c1 = ?  AND lab85c1 = ?",
                shortcut.getUser(), shortcut.getForm());
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}

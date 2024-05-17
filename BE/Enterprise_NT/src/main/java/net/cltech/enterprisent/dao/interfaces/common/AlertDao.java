package net.cltech.enterprisent.dao.interfaces.common;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.common.Alert;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * alertas.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 19/09/2017
 * @see Creación
 */
public interface AlertDao
{

    /**
     * Lista alertas
     *
     *
     * @return Lista de Modulos.
     * @throws Exception Error en la base de datos.
     */
    default List<Alert> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab12c1,lab12c2,lab12c3,lab12c4,lab12c5,lab12c6 "
                    + "FROM lab12 ", (ResultSet rs, int i) ->
            {
                Alert alert = new Alert();
                alert.setName(rs.getString("lab12c1"));
                alert.setDescription(rs.getString("lab12c2"));
                alert.setType(rs.getInt("lab12c3"));
                alert.setItem(rs.getString("lab12c4"));
                alert.setForm(rs.getString("lab12c5"));
                alert.setState(rs.getInt("lab12c6"));
                return alert;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra uno nuevo alert en la base de datos.
     *
     * @param alert Instancia con los datos del alert.
     *
     * @return Instancia con los datos.
     * @throws Exception Error en la base de datos.
     */
    default int create(Alert alert) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab12");

        HashMap parameters = new HashMap();
        parameters.put("lab12c1", alert.getName());
        parameters.put("lab12c2", alert.getDescription());
        parameters.put("lab12c3", alert.getType());
        parameters.put("lab12c4", alert.getItem());
        parameters.put("lab12c5", alert.getForm());
        parameters.put("lab12c6", alert.getState());

        return insert.execute(parameters);
    }

    /**
     *
     * Elimina un alert de la base de datos.
     *
     *
     * @param alert instancia objeto con información
     *
     * @return registros afectados
     * @throws Exception Error en base de datos.
     */
    default int delete(Alert alert) throws Exception
    {
        return getJdbcTemplate().update("DELETE FROM lab12 WHERE lab12c1 = ? ",
                alert.getName());
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}

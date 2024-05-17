package net.cltech.enterprisent.dao.interfaces.masters.microbiology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.Task;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * Tareas.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 09/06/2017
 * @see Creación
 */
public interface TaskDao
{

    /**
     * Lista las tareas desde la base de datos.
     *
     * @return Lista de sitios anatomicos.
     * @throws Exception Error en la base de datos.
     */
    default List<Task> list() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab169c1, lab169c2, lab169c3, lab169.lab04c1, lab04c2, lab04c3, lab04c4, lab169.lab07c1 "
                    + "FROM lab169 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab169.lab04c1", (ResultSet rs, int i) ->
            {
                Task task = new Task();
                task.setId(rs.getInt("lab169c1"));
                task.setDescription(rs.getString("lab169c2"));
                /*Usuario*/
                task.getUser().setId(rs.getInt("lab04c1"));
                task.getUser().setName(rs.getString("lab04c2"));
                task.getUser().setLastName(rs.getString("lab04c3"));
                task.getUser().setUserName(rs.getString("lab04c4"));

                task.setLastTransaction(rs.getTimestamp("lab169c3"));
                task.setState(rs.getInt("lab07c1") == 1);

                return task;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra una nueva tarea en la base de datos.
     *
     * @param task Instancia con los datos de la tarea.
     *
     * @return Instancia con los datos de la tarea.
     * @throws Exception Error en la base de datos.
     */
    default Task create(Task task) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab169")
                .usingGeneratedKeyColumns("lab169c1");

        HashMap parameters = new HashMap();
        parameters.put("lab169c2", task.getDescription().trim());
        parameters.put("lab169c3", timestamp);
        parameters.put("lab04c1", task.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        task.setId(key.intValue());
        task.setLastTransaction(timestamp);

        return task;
    }

    /**
     * Obtener información de una tarea por una campo especifico.
     *
     * @param id ID de la tarea a ser consultada.
     * @param description Descripcion de la tarea a ser consultada.
     *
     * @return Instancia con los datos de la tarea.
     * @throws Exception Error en la base de datos.
     */
    default Task get(Integer id, String description) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab169c1, lab169c2, lab169c3, lab169.lab04c1, lab04c2, lab04c3, lab04c4, lab169.lab07c1 "
                    + "FROM lab169 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab169.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab169c1 = ? ";
            }
            if (description != null)
            {
                query = query + "WHERE UPPER(lab169c2) = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (description != null)
            {
                object = description.toUpperCase();
            }

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Task task = new Task();
                task.setId(rs.getInt("lab169c1"));
                task.setDescription(rs.getString("lab169c2"));
                /*Usuario*/
                task.getUser().setId(rs.getInt("lab04c1"));
                task.getUser().setName(rs.getString("lab04c2"));
                task.getUser().setLastName(rs.getString("lab04c3"));
                task.getUser().setUserName(rs.getString("lab04c4"));

                task.setLastTransaction(rs.getTimestamp("lab169c3"));
                task.setState(rs.getInt("lab07c1") == 1);

                return task;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de una tarea en la base de datos.
     *
     * @param task Instancia con los datos de la tarea.
     *
     * @return Objeto de la tarea modificada.
     * @throws Exception Error en la base de datos.
     */
    default Task update(Task task) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getConnection().update("UPDATE lab169 SET lab169c2 = ?, lab169c3 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab169c1 = ?",
                task.getDescription(), timestamp, task.getUser().getId(), task.isState() ? 1 : 0, task.getId());

        task.setLastTransaction(timestamp);

        return task;
    }

    /**
     *
     * Elimina una tarea de la base de datos.
     *
     * @param id ID de la tarea.
     *
     * @throws Exception Error en base de datos.
     */
    default void delete(Integer id) throws Exception
    {

    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();
}

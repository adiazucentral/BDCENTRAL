package net.cltech.enterprisent.dao.interfaces.masters.opportunity;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.opportunity.Bind;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * Clases.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 15/02/2018
 * @see Creación
 */
public interface BindDao
{

    /**
     * Lista las clases desde la base de datos.
     *
     * @return Lista de clases.
     * @throws Exception Error en la base de datos.
     */
    default public List<Bind> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab06c1, lab06c2, lab06c3, lab06c4, lab06c5, lab06.lab07c1,"
                    + " lab06.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab06 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab06.lab04c1", (ResultSet rs, int i) ->
            {
                Bind bind = new Bind();
                bind.setId(rs.getInt("lab06c1"));
                bind.setName(rs.getString("lab06c2"));
                bind.setMinimum(rs.getLong("lab06c3"));
                bind.setMaximum(rs.getLong("lab06c4"));
                bind.setLastTransaction(rs.getTimestamp("lab06c5"));
                /*Usuario*/
                bind.getUser().setId(rs.getInt("lab04c1"));
                bind.getUser().setName(rs.getString("lab04c2"));
                bind.getUser().setLastName(rs.getString("lab04c3"));
                bind.getUser().setUserName(rs.getString("lab04c4"));

                bind.setState(rs.getInt("lab07c1") == 1);

                return bind;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra una nueva clase en la base de datos.
     *
     * @param bind Instancia con los datos de la clase.
     *
     * @return Instancia con los datos de lka clase.
     * @throws Exception Error en la base de datos.
     */
    default public Bind create(Bind bind) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab06")
                .usingGeneratedKeyColumns("lab06c1");

        HashMap parameters = new HashMap();
        parameters.put("lab06c2", bind.getName());
        parameters.put("lab06c3", bind.getMinimum());
        parameters.put("lab06c4", bind.getMaximum());
        parameters.put("lab06c5", timestamp);
        parameters.put("lab04c1", bind.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        bind.setId(key.intValue());
        bind.setLastTransaction(timestamp);

        return bind;
    }

    /**
     * Obtener información de una clase por un campo especifico.
     *
     * @param id ID de la clase a ser consultada.
     * @param name Nombre de la clase a ser consultada.
     * @return Instancia con los datos del clase.
     * @throws Exception Error en la base de datos.
     */
    default public Bind get(Integer id, String name) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab06c1, lab06c2, lab06c3, lab06c4, lab06c5, lab06.lab07c1,"
                    + " lab06.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab06 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab06.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab06c1 = ? ";
            }

            if (name != null)
            {
                query = query + "WHERE UPPER(lab06c2) = ? ";
            }

            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }

            if (name != null)
            {
                object = name.toUpperCase();
            }

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Bind bind = new Bind();
                bind.setId(rs.getInt("lab06c1"));
                bind.setName(rs.getString("lab06c2"));
                bind.setMinimum(rs.getLong("lab06c3"));
                bind.setMaximum(rs.getLong("lab06c4"));

                bind.setLastTransaction(rs.getTimestamp("lab06c5"));
                /*Usuario*/
                bind.getUser().setId(rs.getInt("lab04c1"));
                bind.getUser().setName(rs.getString("lab04c2"));
                bind.getUser().setLastName(rs.getString("lab04c3"));
                bind.getUser().setUserName(rs.getString("lab04c4"));

                bind.setState(rs.getInt("lab07c1") == 1);

                return bind;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de una clase en la base de datos.
     *
     * @param bind Instancia con los datos de la clase.
     *
     * @return Objeto de la clase modificada.
     * @throws Exception Error en la base de datos.
     */
    default public Bind update(Bind bind) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab06 SET lab06c2 = ?, lab06c3 = ?, lab06c4 = ?, lab06c5 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab06c1 = ?",
                bind.getName(), bind.getMinimum(), bind.getMaximum(), timestamp, bind.getUser().getId(), bind.isState() ? 1 : 0, bind.getId());

        bind.setLastTransaction(timestamp);

        return bind;
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}

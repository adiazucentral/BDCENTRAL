package net.cltech.enterprisent.dao.interfaces.masters.demographic;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Race;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 09/05/2017
 * @see Creación
 */
public interface RaceDao
{

    /**
     * Lista razas desde la base de datos.
     *
     * @return Lista de razas.
     * @throws Exception Error en la base de datos.
     */
    default List<Race> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab08c1, lab08c2, lab08c3, lab08c3, lab08c4, lab08c5, lab08c6, lab08.lab07c1, lab08.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab08 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab08.lab04c1", (ResultSet rs, int i) ->
            {
                Race bean = new Race();
                bean.setId(rs.getInt("lab08c1"));
                bean.setName(rs.getString("lab08c2"));
                bean.setCode(rs.getString("lab08c5"));
                bean.setEmail(rs.getString("lab08c6"));
                bean.setValue(rs.getFloat("lab08c4"));
                bean.setLastTransaction(rs.getTimestamp("lab08c3"));
                bean.setState(rs.getInt("lab07c1") == 1);

                /*Usuario*/
                bean.getUser().setId(rs.getInt("lab04c1"));
                bean.getUser().setName(rs.getString("lab04c2"));
                bean.getUser().setLastName(rs.getString("lab04c3"));
                bean.getUser().setUserName(rs.getString("lab04c4"));

                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra una nueva raza en la base de datos.
     *
     * @param race Instancia con los datos de la raza.
     *
     * @return Instancia con los datos de la raza.
     * @throws Exception Error en la base de datos.
     */
    default Race create(Race race) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab08")
                .usingGeneratedKeyColumns("lab08c1");

        HashMap parameters = new HashMap();
        parameters.put("lab08c2", race.getName().trim());
        parameters.put("lab08c5", race.getCode().trim());
        parameters.put("lab08c3", timestamp);
        parameters.put("lab08c4", race.getValue());
        parameters.put("lab08c6", race.getEmail()); 
        parameters.put("lab04c1", race.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        race.setId(key.intValue());
        race.setLastTransaction(timestamp);

        return race;
    }

    /**
     * Actualiza la información de una raza en la base de datos.
     *
     * @param race Instancia con los datos de la raza.
     *
     * @return Objeto de la raza modificada.
     * @throws Exception Error en la base de datos.
     */
    default Race update(Race race) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab08 SET lab08c2 = ?, lab08c3 = ?, lab08c4 = ?, lab04c1 = ?, lab07c1 = ?, lab08c5 = ? , lab08c6 = ? "
                + "WHERE lab08c1 = ?",
                race.getName(), timestamp, race.getValue(), race.getUser().getId(), race.isState() ? 1 : 0, race.getCode(), race.getEmail(), race.getId());

        race.setLastTransaction(timestamp);
        return race;
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

}

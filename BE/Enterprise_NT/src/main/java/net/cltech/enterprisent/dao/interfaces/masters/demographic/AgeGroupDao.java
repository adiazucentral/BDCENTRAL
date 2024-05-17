package net.cltech.enterprisent.dao.interfaces.masters.demographic;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.AgeGroup;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 31/01/2018
 * @see Creación
 */
public interface AgeGroupDao
{

    /**
     * Lista grupo etario desde la base de datos.
     *
     * @return Lista de grupo etario.
     * @throws Exception Error en la base de datos.
     */
    default List<AgeGroup> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab13c1, lab13c2, lab13c3, lab13c4, lab13c5, lab13c5, lab13c6,lab13c7,lab13c8, lab13.lab07c1"
                    + ", lab13.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + ", lab80c1, lab80c2, lab80c3, lab80c4, lab80c5 "
                    + "FROM lab13 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab13.lab04c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab13.lab13c4 "
                    + " ORDER BY lab13.lab13c1 DESC", (ResultSet rs, int i) ->
            {
                AgeGroup bean = new AgeGroup();
                bean.setId(rs.getInt("lab13c1"));
                bean.setCode(rs.getString("lab13c2"));
                bean.setName(rs.getString("lab13c3"));
                bean.setUnitAge(rs.getShort("lab13c5"));
                bean.setAgeMin(rs.getInt("lab13c6"));
                bean.setAgeMax(rs.getInt("lab13c7"));
                bean.setLastTransaction(rs.getTimestamp("lab13c8"));
                bean.setState(rs.getInt("lab07c1") == 1);
                /*Genero*/
                bean.setGender(new Item(rs.getInt("lab80c1")));
                bean.getGender().setIdParent(rs.getInt("lab80c2"));
                bean.getGender().setCode(rs.getString("lab80c3"));
                bean.getGender().setEsCo(rs.getString("lab80c4"));
                bean.getGender().setEnUsa(rs.getString("lab80c5"));
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
     * Registra una nueva grupo etario en la base de datos.
     *
     * @param create Instancia con los datos de la grupo etario.
     *
     * @return Instancia con los datos de la grupo etario.
     * @throws Exception Error en la base de datos.
     */
    default AgeGroup create(AgeGroup create) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab13")
                .usingGeneratedKeyColumns("lab13c1");

        HashMap parameters = new HashMap();
        parameters.put("lab13c2", create.getCode().trim());
        parameters.put("lab13c3", create.getName().trim());
        parameters.put("lab13c4", create.getGender().getId());
        parameters.put("lab13c5", create.getUnitAge());
        parameters.put("lab13c6", create.getAgeMin());
        parameters.put("lab13c7", create.getAgeMax());
        parameters.put("lab13c8", timestamp);
        parameters.put("lab04c1", create.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        create.setId(key.intValue());
        create.setLastTransaction(timestamp);

        return create;
    }

    /**
     * Actualiza la información de una grupo etario en la base de datos.
     *
     * @param update Instancia con los datos de la grupo etario.
     *
     * @return Objeto de la grupo etario modificada.
     * @throws Exception Error en la base de datos.
     */
    default AgeGroup update(AgeGroup update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab13 SET lab13c2 = ? , lab13c3 = ?, lab13c4 = ?,lab13c5 = ?,lab13c6 = ?,lab13c7 = ?,lab13c8 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab13c1 = ?",
                update.getCode(), update.getName(), update.getGender().getId(), update.getUnitAge(), update.getAgeMin(), update.getAgeMax(), timestamp, update.getUser().getId(), update.isState() ? 1 : 0, update.getId());

        update.setLastTransaction(timestamp);
        return update;
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

}

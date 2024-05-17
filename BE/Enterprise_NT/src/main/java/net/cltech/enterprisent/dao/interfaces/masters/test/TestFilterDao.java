package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.test.TestFilter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * Filtro Examenes.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 25/10/2017
 * @see Creación
 */
public interface TestFilterDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista las filtro examenes desde la base de datos.
     *
     * @return Lista de filtro examenes.
     * @throws Exception Error en la base de datos.
     */
    default List<TestFilter> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab65c1, lab65c2, lab65c3, lab65c4, lab65.lab04c1, lab04c2, lab04c3, lab04c4, lab65.lab07c1 "
                    + "FROM lab65 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab65.lab04c1", (ResultSet rs, int i) ->
            {
                TestFilter filter = new TestFilter();
                filter.setId(rs.getInt("lab65c1"));
                filter.setCode(rs.getString("lab65c2"));
                filter.setName(rs.getString("lab65c3"));
                /*Usuario*/
                filter.getUser().setId(rs.getInt("lab04c1"));
                filter.getUser().setName(rs.getString("lab04c2"));
                filter.getUser().setLastName(rs.getString("lab04c3"));
                filter.getUser().setUserName(rs.getString("lab04c4"));

                filter.setLastTransaction(rs.getTimestamp("lab65c4"));
                filter.setState(rs.getInt("lab07c1") == 1);

                return filter;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    
    /**
     * Lista las filtro examenes desde la base de datos.
     *
     * @return Lista de filtro examenes.
     * @throws Exception Error en la base de datos.
     */
    default List<TestFilter> listTestGroup() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab65.lab65c1, lab65.lab65c2, lab65.lab65c3, lab02.lab39c1 "
                    + "FROM lab02 "
                    + "INNER JOIN lab65 ON lab02.lab65c1 = lab65.lab65c1 ", (ResultSet rs, int i) ->
            {
                TestFilter filter = new TestFilter();
                filter.setId(rs.getInt("lab65c1"));
                filter.setCode(rs.getString("lab65c2"));
                filter.setName(rs.getString("lab65c3"));
                filter.setIdTest(rs.getInt("lab39c1"));

                return filter;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra una nueva Hoja de Trabajo en la base de datos.
     *
     * @param filter Instancia con los datos de la Hoja de Trabajo.
     *
     * @return Instancia con las datos de la Hoja de Trabajo.
     * @throws Exception Error en la base de datos.
     */
    default TestFilter create(TestFilter filter) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab65")
                .usingGeneratedKeyColumns("lab65c1");

        HashMap parameters = new HashMap();
        parameters.put("lab65c3", filter.getName().trim());
        parameters.put("lab65c2", filter.getCode());
        parameters.put("lab65c4", timestamp);
        parameters.put("lab04c1", filter.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        filter.setId(key.intValue());
        filter.setLastTransaction(timestamp);

        insertTests(filter);

        return filter;
    }

    /**
     * Actualiza la información de una Hoja de Trabajo en la base de datos.
     *
     * @param filter Instancia con las datos de la Hoja de Trabajo.
     *
     * @return Objeto de la Hoja de Trabajo modificada.
     * @throws Exception Error en la base de datos.
     */
    default TestFilter update(TestFilter filter) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab65 SET lab65c2 = ?, lab65c3 = ?, lab65c4 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab65c1 = ?",
                filter.getCode(), filter.getName(), timestamp, filter.getUser().getId(), filter.getState() == null ? 0 : filter.getState() ? 1 : 0, filter.getId());

        filter.setLastTransaction(timestamp);

        insertTests(filter);

        return filter;
    }

    /**
     * Asociar examenes a una hoja de trabajo.
     *
     * @param filter Instancia con los datos de la hoja de trabajo.
     * @throws Exception Error en la base de datos.
     */
    default void insertTests(TestFilter filter) throws Exception
    {
        deleteTests(filter.getId());
        for (TestBasic test : filter.getTests())
        {

            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab02")
                    .usingColumns("lab65c1", "lab39c1");

            HashMap parameters = new HashMap();
            parameters.put("lab65c1", filter.getId());
            parameters.put("lab39c1", test.getId());

            insert.execute(parameters);

        }

    }

    /**
     * Obtener examenes asociados al filtro.
     *
     * @param id
     *
     * @return Lista de exámenes del grupo
     */
    default List<TestBasic> readTests(int id)
    {
        try
        {
            return getJdbcTemplate().query("SELECT lab39.lab39c1, lab39.lab43c1,  lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c5, lab02.lab65c1 FROM lab39 "
                    + "LEFT JOIN lab02 ON lab02.lab39c1 = lab39.lab39c1 "
                    + "AND lab02.lab65c1 = ?"
                    + "",
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                TestBasic testBasic = new TestBasic();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));
                testBasic.setSelected(rs.getString("lab65c1") != null);

                
                Area area = new Area();
                area.setId(rs.getInt("lab43c1"));
               
                testBasic.setArea(area);
               
                return testBasic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Borra los examenes de un grupo
     *
     * @param idTestFilter Id del grupo
     * @throws Exception Error en la base de datos.
     */
    default void deleteTests(Integer idTestFilter) throws Exception
    {
        String query = ""
                + " DELETE FROM lab02 WHERE lab65c1 = " + idTestFilter;
        getJdbcTemplate().execute(query);
    }
}

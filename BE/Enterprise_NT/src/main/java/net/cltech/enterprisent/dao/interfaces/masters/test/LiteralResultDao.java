package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.test.LiteralByTest;
import net.cltech.enterprisent.domain.masters.test.LiteralResult;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 22/05/2017
 * @see Creación
 */
public interface LiteralResultDao
{

    /**
     * Lista resultados literales desde la base de datos.
     *
     * @return Lista de resultados literales.
     * @throws Exception Error en la base de datos.
     */
    public List<LiteralResult> list() throws Exception;

    /**
     * Registra resultados literales en la base de datos.
     *
     * @param create Instancia con los datos de resultados literales.
     *
     * @return Instancia con los datos de resultados literales.
     * @throws Exception Error en la base de datos.
     */
    public LiteralResult create(LiteralResult create) throws Exception;

    /**
     * Obtener información de resultados literales por nombre.
     *
     * @param name Nombre de resultados literales a ser consultada.
     *
     * @return Instancia con los datos de resultados literales.
     * @throws Exception Error en la base de datos.
     */
    public LiteralResult filterByName(String name) throws Exception;

    /**
     * Obtener información de resultados literales por nombre.
     *
     * @param id id de resultados literales.
     *
     * @return Instancia con los datos de resultados literales.
     * @throws Exception Error en la base de datos.
     */
    public LiteralResult filterById(Integer id) throws Exception;

    /**
     * Actualiza la información de resultados literales en la base de datos.
     *
     * @param update Instancia con los datos de resultados literales.
     *
     * @return Objeto de resultados literales modificada.
     * @throws Exception Error en la base de datos.
     */
    public LiteralResult update(LiteralResult update) throws Exception;

    /**
     * Obtiene lista de resultados por examen
     *
     * @param id del examen
     *
     * @return Lista de resultados
     * @throws Exception
     */
    default List<LiteralByTest> filterByTest(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab49.lab39c1,lab50.lab50c1, lab50c2, lab50c3, lab50.lab07c1, lab39c4, lab50c4 "
                    + "FROM lab50 "
                    + "LEFT JOIN lab49 ON lab49.lab50c1 = lab50.lab50c1 AND lab49.lab39c1 = ? "
                    + "LEFT JOIN lab39 ON lab39.lab39c1 = lab49.lab39c1 "
                    + "WHERE lab50.lab07c1 = 1";

            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                LiteralByTest bean = new LiteralByTest();
                bean.getLiteralResult().setId(rs.getInt("lab50c1"));
                bean.getLiteralResult().setName(rs.getString("lab50c2"));
                bean.getLiteralResult().setNameEnglish(rs.getString("lab50c4"));
                bean.setAssign(id.equals(rs.getInt("lab39c1")));
                bean.setName(rs.getString("lab39c4"));
                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Inserta los resultado literales de un examen
     *
     * @param literals resultados literales
     *
     * @return numero de datos insertados
     * @throws Exception Excepcion generada en la base de datos
     */
    default int insertLiteralResults(List<LiteralByTest> literals) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab49");
        for (LiteralByTest literal : literals)
        {
            HashMap parameters = new HashMap();
            parameters.put("lab50c1", literal.getLiteralResult().getId());
            parameters.put("lab39c1", literal.getId());
            batchArray.add(parameters);
        }

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[literals.size()]));

        return inserted.length;
    }

    /**
     * Elimina resultado asignados a un examen
     *
     *
     * @param id id del examen
     *
     * @return numero de registros eliminados
     * @throws Exception
     */
    default int deleteResultAssign(Integer id) throws Exception
    {
        String deleteSql = "DELETE FROM lab49 WHERE lab39c1 = ?";
        Object[] params =
        {
            id
        };

        return getJdbcTemplate().update(deleteSql, params);

    }

    /**
     * Consulta los resultados literales activos de todos los exámenes, para el
     * reporte de resultados
     *
     * @return Lista de resultados literales
     * @throws Exception
     */
    default List<LiteralResult> listWithTestId() throws Exception
    {
        try
        {
            String select = ""
                    + "SELECT lab50.lab50c1, lab50c2, lab49.lab39c1, lab50c4 "
                    + "FROM   lab50 "
                    + "INNER  JOIN lab49 ON lab49.lab50c1 = lab50.lab50c1 "
                    + "WHERE  lab50.lab07c1 = 1 "
                    + "ORDER  BY lab39c1, lab50c2 ";

            RowMapper mapper = (RowMapper<LiteralResult>) (ResultSet rs, int i) ->
            {
                LiteralResult bean = new LiteralResult();
                bean.setId(rs.getInt("lab50c1"));
                bean.setName(rs.getString("lab50c2"));
                bean.setTestId(rs.getInt("lab39c1"));
                bean.setNameEnglish(rs.getString("lab50c4"));
                return bean;
            };
            return getJdbcTemplate().query(select, mapper);
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }

    /**
     * Consulta los resultados literales activos por orden
     *
     * @param order numero de la orden
     * @return Lista de resultados literales
     * @throws Exception
     */
    default List<LiteralResult> listByOrder(long order) throws Exception
    {
        try
        {
            String select = "SELECT lab50.lab50c1, lab50.lab50c2, lab57.lab39c1 "
                    + "FROM lab57 "
                    + "INNER JOIN lab49 ON lab49.lab39c1 = lab57.lab39c1 "
                    + "INNER JOIN lab50 ON lab50.lab50c1 = lab49.lab50c1 "
                    + "WHERE lab57.lab22c1 = ? "
                    + "ORDER BY lab39c1 ";

            RowMapper mapper = (RowMapper<LiteralResult>) (ResultSet rs, int i) ->
            {
                LiteralResult bean = new LiteralResult();
                bean.setId(rs.getInt("lab50c1"));
                bean.setName(rs.getString("lab50c2"));
                bean.setTestId(rs.getInt("lab39c1"));
                return bean;
            };
            return getJdbcTemplate().query(select, mapper, order);
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

}

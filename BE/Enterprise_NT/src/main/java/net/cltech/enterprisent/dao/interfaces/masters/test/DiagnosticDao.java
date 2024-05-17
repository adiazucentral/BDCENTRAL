package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.domain.masters.test.Diagnostic;
import net.cltech.enterprisent.domain.masters.test.Test;
import net.cltech.enterprisent.domain.operation.orders.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * Cotizaciones
 *
 * @author Jrodriguez
 * @version 1.0.0
 * @since 01/11/2018
 * @see Creación
 */
public interface DiagnosticDao
{

    /**
     * Lista los diagnosticos desde la base de datos
     *
     * @return Lista de diagnosticos
     * @throws Exception Error en base de datos
     */
    public List<Diagnostic> list() throws Exception;

    /**
     * Obtener informacion de un diagnostico en la base de datos
     *
     * @param id del diagnostico consultado.
     * @param code codigo del diagnostico consultado.
     * @param name nombre del diagnostico consultado.
     * @param type tipo del diagnostico consultado.
     * @param state estado del diagnostico consultado.
     * @return Lista de diagnosticos
     * @throws Exception Error en base de datos
     */
    public List<Diagnostic> get(Integer id, String code, String name, Integer type, Boolean state) throws Exception;

    /**
     * Registra un nuevo diagnostico en la base de datos
     *
     * @param diagnostic Instancia con los datos del diagnostico.
     * @return Instancia con los datos del diagnostico.
     * @throws Exception Error en base de datos
     */
    public Diagnostic create(Diagnostic diagnostic) throws Exception;

    /**
     * Actualiza la informacion de un diagnostico en la base de datos
     *
     * @param diagnostic Instancia con los datos del diagnostico.
     * @return Instancia con los datos del diagnostico actualizado.
     * @throws Exception Error en base de datos
     */
    public Diagnostic update(Diagnostic diagnostic) throws Exception;

    /**
     * Registra una Lista de diagnosticos en la base de datos
     *
     * @param diagnostics Instancia con los datos de los diagnosticos.
     * @return numero de registros insertados
     * @throws Exception Error en base de datos
     */
    public int createAll(List<Diagnostic> diagnostics) throws Exception;

    /**
     * Elimina diagnosticos asignados a un exámen
     *
     * @param idTest id del exámen
     * @return numero de registros eliminados
     * @throws Exception
     */
    default int deleteTestDiagnosis(Integer idTest) throws Exception
    {
        String deleteSql = "DELETE FROM lab26 WHERE lab39c1 = ?";
        Object[] params =
        {
            idTest
        };

        return getJdbcTemplate().update(deleteSql, params);

    }

    /**
     * Elimina las pruebas asignadas a un diagnostico
     *
     * @param idDiagnostic id del diagnostico
     * @return numero de registros eliminados
     * @throws Exception
     */
    default int deleteTestByDiagnostic(Integer idDiagnostic) throws Exception
    {
        String deleteSql = "DELETE FROM lab26 WHERE lab20c1 = ?";
        Object[] params =
        {
            idDiagnostic
        };

        return getJdbcTemplate().update(deleteSql, params);

    }

    /**
     * Inserta/Asigna Diagnosticos a un examen
     *
     * @param diagnostics lista
     * @param idTest id del examen
     *
     * @return numero de registros insertados
     * @throws Exception
     */
    default int insertTestDiagnosis(List<Diagnostic> diagnostics, int idTest) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab26");
        for (Diagnostic diagnosis : diagnostics)
        {
            HashMap parameters = new HashMap();
            parameters.put("lab20c1", diagnosis.getId());
            parameters.put("lab39c1", idTest);
            batchArray.add(parameters);
        }

        return insert.executeBatch(batchArray.toArray(new HashMap[diagnostics.size()])).length;
    }

    /**
     * Inserta/Asigna Examenes a un diagnostico
     *
     * @param idDiagnostic id del diagnostico
     * @param tests lista de examenes
     *
     * @return numero de registros insertados
     * @throws Exception
     */
    default int insertDiagnosticTest(List<Test> tests, int idDiagnostic) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab26");
        for (Test test : tests)
        {
            HashMap parameters = new HashMap();
            parameters.put("lab20c1", idDiagnostic);
            parameters.put("lab39c1", test.getId());
            batchArray.add(parameters);
        }

        return insert.executeBatch(batchArray.toArray(new HashMap[tests.size()])).length;
    }

    /**
     * Lista de diagnosticos, se marcan aquellos asignados al examen
     *
     * @param idTest id del examen
     * @return Lista de diagnosticos
     * @throws Exception Error en base de datos
     */
    default List<Diagnostic> listTestDiagnosis(int idTest) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab20.lab20c1, lab20c2, lab20c3, lab39c1 "
                    + "FROM lab20 "
                    + "LEFT JOIN lab26 ON lab26.lab20c1 = lab20.lab20c1 AND lab26.lab39c1 = ? "
                    + "WHERE lab20.lab07c1 = 1 ",
                    new Object[]
                    {
                        idTest
                    }, (ResultSet rs, int i) ->
            {
                Diagnostic relation = new Diagnostic();
                relation.setId(rs.getInt("lab20c1"));
                relation.setCode(rs.getString("lab20c2"));
                relation.setName(rs.getString("lab20c3"));
                relation.setSelected(rs.getString("lab39c1") != null);
                return relation;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista de examanes, se marcan aquellos asignados al diagnostico
     *
     * @param diagnostic id del diagnostico
     * @return Lista de diagnosticos
     * @throws Exception Error en base de datos
     */
    default List<Test> listDiagnosticTest(int diagnostic) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab39.lab39c1, "
                    + "lab39c2, "
                    + "lab39c4, "
                    + "lab43c1, "
                    + "lab26.lab20c1 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab26 ON lab26.lab39c1 = lab39.lab39c1 AND lab26.lab20c1 = ? "
                    + "LEFT JOIN lab20 ON lab26.lab20c1 = lab20.lab20c1 AND lab20.lab07c1 = 1 ",
                    (ResultSet rs, int numRow) ->
            {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setName(rs.getString("lab39c4"));
                test.setCode(rs.getString("lab39c2"));
                if (rs.getString("lab43c1") != null)
                {
                    test.setArea(new Area(rs.getInt("lab43c1")));
                }
                test.setSelected(rs.getString("lab20c1") != null);
                return test;
            }, diagnostic);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Obtiene lista de pruebas asociadas a un diagnostico
     *
     * @param id Id del diagnostico
     * @return Lista de pruebas asociadas a un diagnostico
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.Payment}
     * @throws Exception Error en base de datos
     */
    default List<Test> getListTest(Integer id) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab26.lab39c1, "
                    + "lab39c2, "
                    + "lab39c3, "
                    + "lab39c4 "
                    + "FROM lab26 "
                    + "INNER JOIN lab39 ON lab26.lab39c1 = lab39.lab39c1 "
                    + "WHERE lab20c1 = ?",
                    (ResultSet rs, int numRow) ->
            {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                return test;
            }, id);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene lista de pruebas asociadas a un diagnostico
     *
     * @param tests Id de los examenes separados por comas.
     * @param diagnostics Id de los diagnosticos separados por comas.
     *
     * @return Lista de pruebas asociadas a los diagnosticos
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.Payment}
     * @throws Exception Error en base de datos
     */
    default List<Test> listTestByDiagnostic(String tests, String diagnostics) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab26.lab39c1, lab26.lab20c1 "
                    + "FROM lab26 "
                    + "WHERE lab39c1 IN (" + tests + ") AND lab20c1 IN (" + diagnostics + ") ",
                    (ResultSet rs, int numRow) ->
            {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setDiagnostic(rs.getInt("lab20c1"));
                return test;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra una Lista de diagnosticos en la base de datos por orden
     *
     * @param order
     * @return numero de registros insertados
     * @throws Exception Error en base de datos
     */
    public int createAllByOrder(Order order) throws Exception;

    /**
     * Obtiene lista de pruebas asociadas a un diagnostico
     *
     * @param order Numero de la orden
     * @return Lista de pruebas asociadas a un diagnostico
     * {@link net.cltech.enterprisent.domain.operation.orders.billing.Payment}
     * @throws Exception Error en base de datos
     */
    default List<Diagnostic> ListDiagnostics(long order) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab20.lab20c1, lab20.lab20c2, lab20.lab20c3 "
                    + "FROM lab180 "
                    + "INNER JOIN lab20 ON lab180.lab20c1 = lab20.lab20c1 "
                    + "WHERE lab180.lab22c1 = ? ",
                    (ResultSet rs, int numRow) ->
            {
                Diagnostic diagnostic = new Diagnostic();
                diagnostic.setId(rs.getInt("lab20c1"));
                diagnostic.setCode(rs.getString("lab20c2"));
                diagnostic.setName(rs.getString("lab20c3"));
                return diagnostic;
            }, order);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Actualizar una Lista de diagnosticos en la base de datos por orden
     *
     * @param order Numero de la orden
     * @return numero de registros insertados
     * @throws Exception Error en base de datos
     */
    public int updateAllByOrder(Order order) throws Exception;
}

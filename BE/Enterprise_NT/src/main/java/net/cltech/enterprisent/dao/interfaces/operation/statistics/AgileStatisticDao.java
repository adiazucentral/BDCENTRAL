package net.cltech.enterprisent.dao.interfaces.operation.statistics;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.operation.statistic.AgileStatisticTest;
import net.cltech.enterprisent.tools.Constants;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de estadisticas.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 04/04/2018
 * @see Creación
 */
public interface AgileStatisticDao
{

    /**
     * Obtiene la conexion a la base de datos de estadisticas.
     *
     * @return jdbc
     */
    public JdbcTemplate getConnectionStat();

    /**
     * Obtiene los años en los que se han registrados estadisticas especiales.
     *
     * @return Lista de años.
     */
    public List<String> getExistingYears();

    /**
     * Consulta la información de estadisticas rapidas: Sede - Prueba.
     *
     * @param idBranch Id de la sede.
     * @param date Fecha en formato yyyymmdd.
     * @param idTest Id del examem
     * @return Si ya existe creado el campo.
     * @throws Exception Error en la base de datos.
     */
    default AgileStatisticTest getStatisticsTestBranch(Integer date, int idBranch, int idTest) throws Exception
    {
        try
        {
            return getConnectionStat().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT sta5c1, sta5c2, sta5c3, sta5c4, sta5c5, sta5c6, sta5c7, sta5c8, sta5c9, sta5c10, sta5c11 "
                    + "FROM sta5" + date.toString().substring(0, 4)
                    + "WHERE sta5c1 = ? AND sta5c2 = ? AND sta5c3 = ?",
                    new Object[]
                    {
                        date, idBranch, idTest
                    }, (ResultSet rs, int i) ->
            {
                AgileStatisticTest statisticTest = new AgileStatisticTest();
                statisticTest.setDate(rs.getInt("sta5c1"));
                statisticTest.setIdBranch(rs.getInt("sta5c2"));
                statisticTest.setIdTest(rs.getInt("sta5c3"));
                statisticTest.setCodeBranch(rs.getString("sta5c4"));
                statisticTest.setNameBranch(rs.getString("sta5c5"));
                statisticTest.setCodeTest(rs.getString("sta5c6"));
                statisticTest.setNameTest(rs.getString("sta5c7"));
                statisticTest.setEntry(rs.getInt("sta5c8"));
                statisticTest.setValidate(rs.getInt("sta5c9"));
                statisticTest.setPrint(rs.getInt("sta5c10"));
                statisticTest.setPathology(rs.getInt("sta5c11"));
                return statisticTest;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new AgileStatisticTest(date, idBranch, "", "", idTest, "", "", 0, 0, 0, 0);
        }
    }

    /**
     * Consulta la información de estadisticas rapidas: Sede - Prueba por años.
     *
     * @param years Lista de años.
     *
     * @return Lista de Estadisticas especiales: Sede - Examen.
     * @throws Exception Error en la base de datos.
     */
    default List<AgileStatisticTest> listStatisticsTest(List<String> years) throws Exception
    {
        List<AgileStatisticTest> agileStatisticTests = new ArrayList<>();
        for (String year : years)
        {
            try
            {
                agileStatisticTests.addAll(getConnectionStat().query("" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT sta5c1, sta5c2, sta5c3, sta5c4, sta5c5, sta5c6, sta5c7, sta5c8, sta5c9, sta5c10, sta5c11 "
                        + "FROM sta5" + year, (ResultSet rs, int i) ->
                {
                    AgileStatisticTest statisticTest = new AgileStatisticTest();
                    statisticTest.setDate(rs.getInt("sta5c1"));
                    statisticTest.setIdBranch(rs.getInt("sta5c2"));
                    statisticTest.setIdTest(rs.getInt("sta5c3"));
                    statisticTest.setCodeBranch(rs.getString("sta5c4"));
                    statisticTest.setNameBranch(rs.getString("sta5c5"));
                    statisticTest.setCodeTest(rs.getString("sta5c6"));
                    statisticTest.setNameTest(rs.getString("sta5c7"));
                    statisticTest.setEntry(rs.getInt("sta5c8"));
                    statisticTest.setValidate(rs.getInt("sta5c9"));
                    statisticTest.setPrint(rs.getInt("sta5c10"));
                    statisticTest.setPathology(rs.getInt("sta5c11"));
                    return statisticTest;
                }));
            }
            catch (EmptyResultDataAccessException ex)
            {
                agileStatisticTests.addAll(new ArrayList<>());
            }
        }
        return agileStatisticTests;
    }

    /**
     * Consulta la información de estadisticas rapidas: Sede - Prueba.
     *
     * @param init Fecha Inicial en formato yyyymmdd.
     * @param end Fecha Final en formato yyyymmdd.
     * @param years Lista de años.
     *
     * @return Lista de Estadisticas especiales: Sede - Examen.
     * @throws Exception Error en la base de datos.
     */
    default List<AgileStatisticTest> listStatisticsTest(Integer init, Integer end, List<String> years) throws Exception
    {
        List<AgileStatisticTest> agileStatisticTests = new ArrayList<>();
        for (String year : years)
        {
            try
            {
                agileStatisticTests.addAll(getConnectionStat().query("" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT sta5c1, sta5c2, sta5c3, sta5c4, sta5c5, sta5c6, sta5c7, sta5c8, sta5c9, sta5c10, sta5c11 "
                        + " FROM sta5" + year
                        + " WHERE sta5c1 BETWEEN ? AND ?",
                        new Object[]
                        {
                            init.toString().length() == 3 ? Integer.valueOf(year + "0" + init) : Integer.valueOf(year + init), end.toString().length() == 3 ? Integer.valueOf(year + "0" + end) : Integer.valueOf(year + end)
                        }, (ResultSet rs, int i) ->
                {
                    AgileStatisticTest statisticTest = new AgileStatisticTest();
                    statisticTest.setDate(rs.getInt("sta5c1"));
                    statisticTest.setIdBranch(rs.getInt("sta5c2"));
                    statisticTest.setIdTest(rs.getInt("sta5c3"));
                    statisticTest.setCodeBranch(rs.getString("sta5c4"));
                    statisticTest.setNameBranch(rs.getString("sta5c5"));
                    statisticTest.setCodeTest(rs.getString("sta5c6"));
                    statisticTest.setNameTest(rs.getString("sta5c7"));
                    statisticTest.setEntry(rs.getInt("sta5c8"));
                    statisticTest.setValidate(rs.getInt("sta5c9"));
                    statisticTest.setPrint(rs.getInt("sta5c10"));
                    statisticTest.setPathology(rs.getInt("sta5c11"));
                    return statisticTest;
                }));
            }
            catch (EmptyResultDataAccessException ex)
            {
                agileStatisticTests.addAll(new ArrayList<>());
            }
        }
        return agileStatisticTests;
    }

    /**
     * Consulta la información de estadisticas rapidas: Sede.
     *
     * @param years Lista de años.
     *
     * @return Lista de Estadisticas especiales: Sede.
     * @throws Exception Error en la base de datos.
     */
    default List<AgileStatisticTest> listStatisticsBranch(List<String> years) throws Exception
    {
        List<AgileStatisticTest> agileStatisticTests = new ArrayList<>();
        for (String year : years)
        {
            try
            {
                agileStatisticTests.addAll(getConnectionStat().query("" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT sta7c1, sta7c2, sta7c3, sta7c4, sta7c5, sta7c6, sta7c7, sta7c8, sta7c9 "
                        + "FROM sta7" + year, (ResultSet rs, int i) ->
                {
                    AgileStatisticTest statisticTest = new AgileStatisticTest();
                    statisticTest.setDate(rs.getInt("sta7c1"));
                    statisticTest.setIdBranch(rs.getInt("sta7c2"));
                    statisticTest.setCodeBranch(rs.getString("sta7c3"));
                    statisticTest.setNameBranch(rs.getString("sta7c4"));
                    statisticTest.setOrderEntry(rs.getInt("sta7c5"));
                    statisticTest.setEntry(rs.getInt("sta7c6"));
                    statisticTest.setValidate(rs.getInt("sta7c7"));
                    statisticTest.setPrint(rs.getInt("sta7c8"));
                    statisticTest.setPathology(rs.getInt("sta7c9"));
                    return statisticTest;
                }));
            }
            catch (EmptyResultDataAccessException ex)
            {
                agileStatisticTests.addAll(new ArrayList<>());
            }
        }
        return agileStatisticTests;
    }

    /**
     * Consulta la información de estadisticas rapidas: Sede - Prueba.
     *
     * @param init Fecha Inicial en formato yyyymmdd.
     * @param end Fecha Final en formato yyyymmdd.
     * @param years Lista de años.
     *
     * @return Lista de Estadisticas especiales: Sede - Examen.
     * @throws Exception Error en la base de datos.
     */
    default List<AgileStatisticTest> listStatisticsBranch(Integer init, Integer end, List<String> years) throws Exception
    {
        List<AgileStatisticTest> agileStatisticTests = new ArrayList<>();
        for (String year : years)
        {
            try
            {
                agileStatisticTests.addAll(getConnectionStat().query("" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT sta7c1, sta7c2, sta7c3, sta7c4, sta7c5, sta7c6, sta7c7, sta7c8, sta7c9 "
                        + " FROM sta7" + year
                        + " WHERE sta7c1 BETWEEN ? AND ?",
                        new Object[]
                        {
                            init.toString().length() == 3 ? Integer.valueOf(year + "0" + init) : Integer.valueOf(year + init), end.toString().length() == 3 ? Integer.valueOf(year + "0" + end) : Integer.valueOf(year + end)
                        }, (ResultSet rs, int i) ->
                {
                    AgileStatisticTest statisticTest = new AgileStatisticTest();
                    statisticTest.setDate(rs.getInt("sta7c1"));
                    statisticTest.setIdBranch(rs.getInt("sta7c2"));
                    statisticTest.setCodeBranch(rs.getString("sta7c3"));
                    statisticTest.setNameBranch(rs.getString("sta7c4"));
                    statisticTest.setOrderEntry(rs.getInt("sta7c5"));
                    statisticTest.setEntry(rs.getInt("sta7c6"));
                    statisticTest.setValidate(rs.getInt("sta7c7"));
                    statisticTest.setPrint(rs.getInt("sta7c8"));
                    statisticTest.setPathology(rs.getInt("sta7c9"));
                    return statisticTest;
                }));
            }
            catch (EmptyResultDataAccessException ex)
            {
                agileStatisticTests.addAll(new ArrayList<>());
            }
        }
        return agileStatisticTests;
    }

    /**
     * Identificar si ya se encuentra creado el registro de valores.
     *
     * @param idBranch Id de la sede.
     * @param date Fecha en formato mmdd.
     * @param idTest Id de la prueba.
     * @return Si ya existe creado el campo.
     * @throws Exception Error en la base de datos.
     */
    default boolean validateStatisticsTestBranch(Integer date, int idBranch, int idTest) throws Exception
    {
        try
        {
            return getConnectionStat().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT sta5c1 "
                    + "FROM sta5" + date.toString().substring(0, 4)
                    + " WHERE sta5c1 = ? AND sta5c2 = ? AND sta5c3 = ?",
                    new Object[]
                    {
                        date, idBranch, idTest
                    }, (ResultSet rs, int i) -> true);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return false;
        }
    }

    /**
     * Identificar si ya se encuentra creado el registro de valores.
     *
     * @param idBranch Id de la sede.
     * @param date Fecha en formato mmdd.
     * @return Si ya existe creado el campo.
     * @throws Exception Error en la base de datos.
     */
    default boolean validateStatisticsBranch(Integer date, int idBranch) throws Exception
    {
        try
        {
            return getConnectionStat().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT sta7c1 "
                    + "FROM sta7" + date.toString().substring(0, 4)
                    + " WHERE sta7c1 = ? AND sta7c2 = ? ",
                    new Object[]
                    {
                        date, idBranch
                    }, (ResultSet rs, int i) -> true);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return false;
        }
    }

    /**
     * Ingresa la información inicial de estadisticas rapidas: Sede - Prueba.
     *
     * @param date Fecha en formato yyyymmdd.
     * @param idBranch Id de la sede.
     * @param idTest Id del examen.
     * @param codeBranch Codigo de la sede.
     * @param nameBranch Nombre de la sede.
     * @param codeTest Codigo del examen.
     * @param nameTest Nombre del examen.
     *
     * @return Indica si el ingreso en la base de datos fue exitoso.
     * @throws Exception Error en la base de datos.
     */
    default boolean createTestBranch(Integer date, int idBranch, int idTest, String codeBranch, String nameBranch, String codeTest, String nameTest) throws Exception
    {
        try
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnectionStat())
                    .withTableName("sta5" + date.toString().substring(0, 4));

            HashMap parameters = new HashMap();
            parameters.put("sta5c1", date);
            parameters.put("sta5c2", idBranch);
            parameters.put("sta5c3", idTest);
            parameters.put("sta5c4", codeBranch);
            parameters.put("sta5c5", nameBranch);
            parameters.put("sta5c6", codeTest);
            parameters.put("sta5c7", nameTest);
            parameters.put("sta5c8", 0);
            parameters.put("sta5c9", 0);
            parameters.put("sta5c10", 0);
            parameters.put("sta5c11", 0);

            insert.execute(parameters);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Ingresa la información inicial de estadisticas rapidas: Sede.
     *
     * @param date Fecha en formato yyyymmdd.
     * @param idBranch Id de la sede.
     * @param codeBranch Codigo de la sede.
     * @param nameBranch Nombre de la sede.
     *
     * @return Indica si el ingreso en la base de datos fue exitoso.
     * @throws Exception Error en la base de datos.
     */
    default boolean createBranch(Integer date, int idBranch, String codeBranch, String nameBranch) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnectionStat())
                .withTableName("sta7" + date.toString().substring(0, 4));

        HashMap parameters = new HashMap();
        parameters.put("sta7c1", date);
        parameters.put("sta7c2", idBranch);
        parameters.put("sta7c3", codeBranch);
        parameters.put("sta7c4", nameBranch);
        parameters.put("sta7c5", 0);
        parameters.put("sta7c6", 0);
        parameters.put("sta7c7", 0);
        parameters.put("sta7c8", 0);
        parameters.put("sta7c9", 0);

        insert.execute(parameters);
        return true;
    }

    /**
     * Actualiza la información de estadisticas rapidas: Sede - Prueba.
     *
     * @param date Fecha en formato yyyymmdd.
     * @param idBranch Id de la sede.
     * @param idTest Id del examen.
     * @param add Indica si se agregan o disminuyen las cantidades de los campos
     * @param type Indica que campo se actualizara: 1 -> Ingresados, 2 -> Validados, 3 -> Impresos, 4 -> Patologicos.
     *
     * @throws Exception Error en la base de datos.
     */
    default void updateTestBranch(Integer date, int idBranch, int idTest, boolean add, int type) throws Exception
    {
        if (add)
        {
            switch (type)
            {
                case Constants.STATISTICS_TEST_ENTRY:
                    getConnectionStat().update("UPDATE sta5" + date.toString().substring(0, 4) + " SET sta5c8 = sta5c8 + 1 WHERE sta5c1 = ? AND sta5c2 = ? AND sta5c3 = ?",
                            date, idBranch, idTest);
                    break;
                case Constants.STATISTICS_TEST_VALIDATE:
                    getConnectionStat().update("UPDATE sta5" + date.toString().substring(0, 4) + "  SET sta5c9 = sta5c9 + 1 WHERE sta5c1 = ? AND sta5c2 = ? AND sta5c3 = ?",
                            date, idBranch, idTest);
                    break;
                case Constants.STATISTICS_TEST_PRINT:
                    getConnectionStat().update("UPDATE sta5" + date.toString().substring(0, 4) + "  SET sta5c10 = sta5c10 + 1 WHERE sta5c1 = ? AND sta5c2 = ? AND sta5c3 = ?",
                            date, idBranch, idTest);
                    break;
                case Constants.STATISTICS_TEST_PATHOLOGY:
                    getConnectionStat().update("UPDATE sta5" + date.toString().substring(0, 4) + "  SET sta5c11 = sta5c11 + 1 WHERE sta5c1 = ? AND sta5c2 = ? AND sta5c3 = ?",
                            date, idBranch, idTest);
                    break;
                default:
                    break;
            }
        }
        else
        {
            switch (type)
            {
                case Constants.STATISTICS_TEST_ENTRY:
                    getConnectionStat().update("UPDATE sta5" + date.toString().substring(0, 4) + " SET sta5c8 = sta5c8 - 1 WHERE sta5c1 = ? AND sta5c2 = ? AND sta5c3 = ?",
                            date, idBranch, idTest);
                    break;
                case Constants.STATISTICS_TEST_VALIDATE:
                    getConnectionStat().update("UPDATE sta5" + date.toString().substring(0, 4) + " SET sta5c9 = sta5c9 - 1 WHERE sta5c1 = ? AND sta5c2 = ? AND sta5c3 = ?",
                            date, idBranch, idTest);
                    break;
                case Constants.STATISTICS_TEST_PRINT:
                    getConnectionStat().update("UPDATE sta5" + date.toString().substring(0, 4) + " SET sta5c10 = sta5c10 - 1 WHERE sta5c1 = ? AND sta5c2 = ? AND sta5c3 = ?",
                            date, idBranch, idTest);
                    break;
                case Constants.STATISTICS_TEST_PATHOLOGY:
                    getConnectionStat().update("UPDATE sta5" + date.toString().substring(0, 4) + " SET sta5c11 = sta5c11 - 1 WHERE sta5c1 = ? AND sta5c2 = ? AND sta5c3 = ?",
                            date, idBranch, idTest);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Actualiza la información de estadisticas rapidas: Sede.
     *
     * @param date Fecha en formato yyyymmdd.
     * @param idBranch Id de la sede.
     * @param add Indica si se agregan o disminuyen las cantidades de los campos
     * @param type Indica que campo se actualizara: 1 -> Ingresados, 2 -> Validados, 3 -> Impresos, 4 -> Patologicos.
     *
     * @throws Exception Error en la base de datos.
     */
    default void updateBranch(Integer date, int idBranch, boolean add, int type) throws Exception
    {
        int valueSta7 = 0;

        if (add)
        {
            switch (type)
            {
                case Constants.STATISTICS_ORDER_ENTRY:
                    valueSta7 = getValuesSta7("sta7c5", date, idBranch);
                    
                    if (valueSta7 >= 0)
                    {
                        valueSta7 += 1;
                        getConnectionStat().update("UPDATE sta7" + date.toString().substring(0, 4) + " SET sta7c5 = ? WHERE sta7c1 = ? AND sta7c2 = ?",
                            valueSta7, date, idBranch);
                    }
                    
                    break;
                case Constants.STATISTICS_TEST_ENTRY:
                    valueSta7 = getValuesSta7("sta7c6", date, idBranch);
                    
                    if (valueSta7 >= 0)
                    {
                        valueSta7 += 1;
                        getConnectionStat().update("UPDATE sta7" + date.toString().substring(0, 4) + " SET sta7c6 = ? WHERE sta7c1 = ? AND sta7c2 = ?",
                            valueSta7, date, idBranch);
                    }
                    
                    break;
                case Constants.STATISTICS_TEST_VALIDATE:
                    valueSta7 = getValuesSta7("sta7c7", date, idBranch);
                    
                    if (valueSta7 >= 0)
                    {
                        valueSta7 += 1;
                        getConnectionStat().update("UPDATE sta7" + date.toString().substring(0, 4) + "  SET sta7c7 = ? WHERE sta7c1 = ? AND sta7c2 = ?",
                            valueSta7, date, idBranch);
                    }
                    
                    break;
                case Constants.STATISTICS_TEST_PRINT:
                    valueSta7 = getValuesSta7("sta7c8", date, idBranch);
                    
                    if (valueSta7 >= 0)
                    {
                        valueSta7 += 1;
                        getConnectionStat().update("UPDATE sta7" + date.toString().substring(0, 4) + "  SET sta7c8 = ? WHERE sta7c1 = ? AND sta7c2 = ?",
                            valueSta7, date, idBranch);
                    }
                    
                    break;
                case Constants.STATISTICS_TEST_PATHOLOGY:
                    valueSta7 = getValuesSta7("sta7c9", date, idBranch);
                    
                    if (valueSta7 >= 0)
                    {
                        valueSta7 += 1;
                        getConnectionStat().update("UPDATE sta7" + date.toString().substring(0, 4) + "  SET sta7c9 = ? WHERE sta7c1 = ? AND sta7c2 = ?",
                            valueSta7, date, idBranch);
                    }
                    
                    break;
                default:
                    break;
            }
        }
        else
        {
            switch (type)
            {
                case Constants.STATISTICS_ORDER_ENTRY:
                    valueSta7 = getValuesSta7("sta7c5", date, idBranch);
                    
                    if (valueSta7 > 0)
                    {
                        valueSta7 -= 1;
                        getConnectionStat().update("UPDATE sta7" + date.toString().substring(0, 4) + " SET sta7c5 = ? WHERE sta7c1 = ? AND sta7c2 = ?",
                                valueSta7, date, idBranch);
                    }

                    break;
                case Constants.STATISTICS_TEST_ENTRY:
                    valueSta7 = getValuesSta7("sta7c6", date, idBranch);
                    
                    if (valueSta7 > 0)
                    {
                        valueSta7 -= 1;
                        getConnectionStat().update("UPDATE sta7" + date.toString().substring(0, 4) + " SET sta7c6 = ? WHERE sta7c1 = ? AND sta7c2 = ?",
                            valueSta7, date, idBranch);
                    }
                    
                    break;
                case Constants.STATISTICS_TEST_VALIDATE:
                    valueSta7 = getValuesSta7("sta7c7", date, idBranch);
                    
                    if (valueSta7 > 0)
                    {
                        valueSta7 -= 1;
                        getConnectionStat().update("UPDATE sta7" + date.toString().substring(0, 4) + "  SET sta7c7 = ? WHERE sta7c1 = ? AND sta7c2 = ?",
                            valueSta7, date, idBranch);
                    }
                    
                    break;
                case Constants.STATISTICS_TEST_PRINT:
                    valueSta7 = getValuesSta7("sta7c8", date, idBranch);
                    
                    if (valueSta7 > 0)
                    {
                        valueSta7 -= 1;
                        getConnectionStat().update("UPDATE sta7" + date.toString().substring(0, 4) + "  SET sta7c8 = ? WHERE sta7c1 = ? AND sta7c2 = ?",
                            valueSta7, date, idBranch);
                    }
                    
                    break;
                case Constants.STATISTICS_TEST_PATHOLOGY:
                    valueSta7 = getValuesSta7("sta7c9", date, idBranch);
                    
                    if (valueSta7 > 0)
                    {
                        valueSta7 -= 1;
                        getConnectionStat().update("UPDATE sta7" + date.toString().substring(0, 4) + "  SET sta7c9 = ? WHERE sta7c1 = ? AND sta7c2 = ?",
                            valueSta7, date, idBranch);
                    }
                    
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Identificar si ya se encuentra creado el registro de valores.
     *
     * @param idBranch Id de la sede.
     * @param date Fecha en formato mmdd.
     * @param idArea id area
     * @return Si ya existe creado el campo.
     * @throws Exception Error en la base de datos.
     */
    default boolean existsAreaByBranch(Integer date, int idBranch, int idArea) throws Exception
    {
        try
        {
            return getConnectionStat().queryForObject("" + ISOLATION_READ_UNCOMMITTED 
                    + "SELECT 1 "
                    + "FROM sta6" + date.toString().substring(0, 4)
                    + " WHERE sta6c1 = ? AND sta6c2 = ? AND sta6c3 = ?",
                    new Object[]
                    {
                        date, idBranch, idArea
                    }, (ResultSet rs, int i) -> true);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return false;
        }
    }

    /**
     * Ingresa la información inicial de estadisticas rapidas: Sede - Prueba.
     *
     * @param date Fecha en formato yyyymmdd.
     * @param idBranch Id de la sede.
     * @param idArea Id area
     * @param codeBranch Codigo de la sede.
     * @param nameBranch Nombre de la sede.
     * @param codeArea Codigo area
     * @param nameArea nombre area
     *
     * @return Indica si el ingreso en la base de datos fue exitoso.
     * @throws Exception Error en la base de datos.
     */
    default boolean insertAreaBranch(Integer date, int idBranch, int idArea, String codeBranch, String nameBranch, String codeArea, String nameArea, long insertAmmount) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnectionStat())
                .withTableName("sta6" + date.toString().substring(0, 4));

        HashMap parameters = new HashMap();
        parameters.put("sta6c1", date);
        parameters.put("sta6c2", idBranch);
        parameters.put("sta6c3", idArea);
        parameters.put("sta6c4", codeBranch);
        parameters.put("sta6c5", nameBranch);
        parameters.put("sta6c6", codeArea);
        parameters.put("sta6c7", nameArea);
        parameters.put("sta6c8", insertAmmount);
        parameters.put("sta6c9", 0);
        parameters.put("sta6c10", 0);
        parameters.put("sta6c11", 0);

        insert.execute(parameters);
        return true;
    }

    /**
     * Actualiza la información de estadisticas rapidas: Sede - Area.
     *
     * @param date Fecha en formato yyyymmdd.
     * @param idBranch Id de la sede.
     * @param idArea Id area
     * @param add Indica si se agregan o disminuyen las cantidades de los campos
     * @param type Indica que campo se actualizara: 1 -> Ingresados, 2 -> Validados, 3 -> Impresos, 4 -> Patologicos.
     * @param ammount Cantidad que va a sumar o restar
     *
     * @throws Exception Error en la base de datos.
     */
    default void updateAreaBranch(Integer date, int idBranch, int idArea, boolean add, int type, long ammount) throws Exception
    {
        if (add)
        {
            switch (type)
            {
                case Constants.STATISTICS_TEST_ENTRY:
                    getConnectionStat().update("UPDATE sta6" + date.toString().substring(0, 4) + " SET sta6c8 = sta6c8 + ? WHERE sta6c1 = ? AND sta6c2 = ? AND sta6c3 = ?",
                            ammount, date, idBranch, idArea);
                    break;
                case Constants.STATISTICS_TEST_VALIDATE:
                    getConnectionStat().update("UPDATE sta6" + date.toString().substring(0, 4) + "  SET sta6c9 = sta6c9 + ? WHERE sta6c1 = ? AND sta6c2 = ? AND sta6c3 = ?",
                            ammount, date, idBranch, idArea);
                    break;
                case Constants.STATISTICS_TEST_PRINT:
                    getConnectionStat().update("UPDATE sta6" + date.toString().substring(0, 4) + "  SET sta6c10 = sta6c10 + ? WHERE sta6c1 = ? AND sta6c2 = ? AND sta6c3 = ?",
                            ammount, date, idBranch, idArea);
                    break;
                case Constants.STATISTICS_TEST_PATHOLOGY:
                    getConnectionStat().update("UPDATE sta6" + date.toString().substring(0, 4) + "  SET sta6c11 = sta6c11 + ? WHERE sta6c1 = ? AND sta6c2 = ? AND sta6c3 = ?",
                            ammount, date, idBranch, idArea);
                    break;
                default:
                    break;
            }
        }
        else
        {
            switch (type)
            {
                case Constants.STATISTICS_TEST_ENTRY:
                    getConnectionStat().update("UPDATE sta6" + date.toString().substring(0, 4) + " SET sta6c8 = sta6c8 - ? WHERE sta6c1 = ? AND sta6c2 = ? AND sta6c3 = ?",
                            ammount, date, idBranch, idArea);
                    break;
                case Constants.STATISTICS_TEST_VALIDATE:
                    getConnectionStat().update("UPDATE sta6" + date.toString().substring(0, 4) + " SET sta6c9 = sta6c9 - ? WHERE sta6c1 = ? AND sta6c2 = ? AND sta6c3 = ?",
                            ammount, date, idBranch, idArea);
                    break;
                case Constants.STATISTICS_TEST_PRINT:
                    getConnectionStat().update("UPDATE sta6" + date.toString().substring(0, 4) + " SET sta6c10 = sta6c10 - ? WHERE sta6c1 = ? AND sta6c2 = ? AND sta6c3 = ?",
                            ammount, date, idBranch, idArea);
                    break;
                case Constants.STATISTICS_TEST_PATHOLOGY:
                    getConnectionStat().update("UPDATE sta6" + date.toString().substring(0, 4) + " SET sta6c11 = sta6c11 - ? WHERE sta6c1 = ? AND sta6c2 = ? AND sta6c3 = ?",
                            ammount, date, idBranch, idArea);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Consulta la información de estadisticas rapidas: Sede - Area por años.
     *
     * @param years Lista de años.
     *
     * @return Lista de Estadisticas especiales: Sede - Examen.
     * @throws Exception Error en la base de datos.
     */
    default List<AgileStatisticTest> listBranchArea(List<String> years) throws Exception
    {
        List<AgileStatisticTest> agileStatisticTests = new ArrayList<>();
        for (String year : years)
        {
            try
            {
                agileStatisticTests.addAll(getConnectionStat().query("" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT sta6c1, sta6c2, sta6c3, sta6c4, sta6c5, sta6c6, sta6c7, sta6c8, sta6c9, sta6c10, sta6c11 "
                        + "FROM sta6" + year, (ResultSet rs, int i) ->
                {
                    AgileStatisticTest statisticTest = new AgileStatisticTest();
                    statisticTest.setDate(rs.getInt("sta6c1"));
                    statisticTest.setIdBranch(rs.getInt("sta6c2"));
                    statisticTest.setIdArea(rs.getInt("sta6c3"));
                    statisticTest.setCodeBranch(rs.getString("sta6c4"));
                    statisticTest.setNameBranch(rs.getString("sta6c5"));
                    statisticTest.setCodeArea(rs.getString("sta6c6"));
                    statisticTest.setNameArea(rs.getString("sta6c7"));
                    statisticTest.setEntry(rs.getInt("sta6c8"));
                    statisticTest.setValidate(rs.getInt("sta6c9"));
                    statisticTest.setPrint(rs.getInt("sta6c10"));
                    statisticTest.setPathology(rs.getInt("sta6c11"));
                    return statisticTest;
                }));
            }
            catch (EmptyResultDataAccessException ex)
            {
                agileStatisticTests.addAll(new ArrayList<>());
            }
        }
        return agileStatisticTests;
    }

    /**
     * Consulta la información de estadisticas rapidas: Sede - Area.
     *
     * @param init Fecha Inicial en formato yyyymmdd.
     * @param end Fecha Final en formato yyyymmdd.
     * @param years Lista de años.
     *
     * @return Lista de Estadisticas especiales.
     * @throws Exception Error en la base de datos.
     */
    default List<AgileStatisticTest> listBranchArea(Integer init, Integer end, List<String> years) throws Exception
    {
        List<AgileStatisticTest> agileStatisticTests = new ArrayList<>();
        for (String year : years)
        {
            try
            {
                agileStatisticTests.addAll(getConnectionStat().query("" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT sta6c1, sta6c2, sta6c3, sta6c4, sta6c5, sta6c6, sta6c7, sta6c8, sta6c9, sta6c10, sta6c11 "
                        + " FROM sta6" + year
                        + " WHERE sta6c1 BETWEEN ? AND ?",
                        new Object[]
                        {
                            init.toString().length() == 3 ? Integer.valueOf(year + "0" + init) : Integer.valueOf(year + init), end.toString().length() == 3 ? Integer.valueOf(year + "0" + end) : Integer.valueOf(year + end)
                        }, (ResultSet rs, int i) ->
                {
                    AgileStatisticTest statisticTest = new AgileStatisticTest();
                    statisticTest.setDate(rs.getInt("sta6c1"));
                    statisticTest.setIdBranch(rs.getInt("sta6c2"));
                    statisticTest.setIdArea(rs.getInt("sta6c3"));
                    statisticTest.setCodeBranch(rs.getString("sta6c4"));
                    statisticTest.setNameArea(rs.getString("sta6c5"));
                    statisticTest.setCodeArea(rs.getString("sta6c6"));
                    statisticTest.setNameArea(rs.getString("sta6c7"));
                    statisticTest.setEntry(rs.getInt("sta6c8"));
                    statisticTest.setValidate(rs.getInt("sta6c9"));
                    statisticTest.setPrint(rs.getInt("sta6c10"));
                    statisticTest.setPathology(rs.getInt("sta6c11"));
                    return statisticTest;
                }));
            }
            catch (EmptyResultDataAccessException ex)
            {
                agileStatisticTests.addAll(new ArrayList<>());
            }
        }
        return agileStatisticTests;
    }

    default int getValuesSta7(String campoSta, Integer date, int idBranch) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT ").append(campoSta)
                    .append(" FROM sta7").append(date.toString().substring(0, 4))
                    .append(" WHERE sta7c1 = ").append(date)
                    .append(" AND sta7c2 = ").append(idBranch);
            return getConnectionStat().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("sta7c5");
            });
        }
        catch (Exception e)
        {
            e.getCause().toString();
            return -1;
        }
    }
}

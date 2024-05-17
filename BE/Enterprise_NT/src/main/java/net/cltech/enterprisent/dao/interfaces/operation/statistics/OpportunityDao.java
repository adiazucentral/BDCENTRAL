package net.cltech.enterprisent.dao.interfaces.operation.statistics;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.test.TestByService;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import net.cltech.enterprisent.domain.operation.results.ReferenceValues;
import net.cltech.enterprisent.domain.operation.statistic.StatisticDemographic;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOpportunity;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOrder;
import net.cltech.enterprisent.domain.operation.statistic.StatisticResult;
import net.cltech.enterprisent.start.StartApp;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.SQLTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.results.ResultsLog;
import net.cltech.enterprisent.tools.log.stadistics.StadisticsLog;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de
 * estadisticas tiempos de oportunidad.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 05/02/2018
 * @see Creación
 */
public interface OpportunityDao
{

    /**
     * Obtiene la conexion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnectionStat();

    /**
     * Obtiene la conexion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

    /**
     * Obtiene el dao de tools
     *
     * @return Instancia de toolsDao
     */
    public ToolsDao getToolsDao();

    /**
     * Agrega nuevo registro para los tiempos de oportunidad
     *
     * @param opportunity información del paciente
     *
     * @return registros afectados
     * @throws Exception
     */
    default int addOpportunityTime(StatisticOpportunity opportunity) throws Exception
    {
        HashMap params = new HashMap();
        params.put("sta2c1", opportunity.getOrderNumber());
        params.put("sta3c1", opportunity.getId());
        params.put("sta4c4", opportunity.getEntryDate());
        params.put("sta4c6", opportunity.getEntryUser());
        params.put("sta4c12", opportunity.getVerifyDate());
        params.put("sta4c13", opportunity.getVerifyElapsedTime());
        params.put("sta4c14", opportunity.getVerifyUser());

        return new SimpleJdbcInsert(getConnectionStat())
                .withTableName("sta4")
                .execute(params);
    }

    /**
     * Consulta registro en la tabla de tiempos
     *
     * @param orderNumber numero de la orden
     * @param testId id del examen
     * @return true si encuentra 1 registro
     */
    default boolean opportunityExists(long orderNumber, int testId)
    {
        try
        {
            List<Object> params = new ArrayList<>();
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT 1 FROM sta4 WHERE sta2c1 = ? AND sta3c1 = ?";
            params.add(orderNumber);
            params.add(testId);

            return getConnectionStat().queryForObject(query, Integer.class, params.toArray()) == 1;

        } catch (EmptyResultDataAccessException ex)
        {
            return false;
        }
    }

    /**
     * Lista de tiempos por servicios de exámenes
     *
     * @return Lista de Examenes por Servicio
     * @throws Exception
     */
    default List<TestByService> listTestByService() throws Exception
    {
        try
        {
            return getConnection().query("" + ISOLATION_READ_UNCOMMITTED
                    + " SELECT lab10c1, lab171c1, lab171c2, lab171c3, lab171c4"
                    + " FROM lab171 "
                    + " WHERE lab171c3 = 0", (ResultSet rs, int i) ->
            {
                TestByService testByService = new TestByService();
                testByService.getTest().setId(rs.getInt("lab171c1"));
                testByService.getService().setId(rs.getInt("lab10c1"));
                testByService.setExpectedTime(rs.getInt("lab171c2"));
                testByService.setMaximumTime(rs.getInt("lab171c4"));
                return testByService;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra orden en tabla de estadisticas
     *
     * @param order información de la orden
     *
     * @return registros afectados
     * @throws Exception Error
     */
    default int createOrder(StatisticOrder order) throws Exception
    {
        StartApp.logger.log(Level.INFO, "Entra DAO  {0}", order.getOrderNumber().toString());
        HashMap params = new HashMap();
        params.put("sta2c1", order.getOrderNumber());
        params.put("sta1c1", order.getId());
        params.put("sta2c2", order.getOrderType());
        params.put("sta2c3", order.getOrderTypeCode());
        params.put("sta2c4", order.getOrderTypeName());
        params.put("sta2c5", order.getBranch());
        params.put("sta2c6", order.getBranchCode());
        params.put("sta2c7", order.getBranchName());
        params.put("sta2c8", order.getService());
        params.put("sta2c9", order.getServiceCode());
        params.put("sta2c10", order.getServiceName());
        params.put("sta2c11", order.getPhysician());
        params.put("sta2c12", order.getPhysicianCode());
        params.put("sta2c13", order.getPhysicianName());
        params.put("sta2c14", order.getAccount());
        params.put("sta2c15", order.getAccountCode());
        params.put("sta2c16", order.getAccountName());
        params.put("sta2c17", order.getRate());
        params.put("sta2c18", order.getRateCode());
        params.put("sta2c19", order.getRateName());
        params.put("sta2c20", order.getDate());
        params.put("sta2c21", new Timestamp(order.getDateTime().getTime()));
        params.put("sta2c22", order.getAge());
        params.put("sta2c23", order.getUnit());
        params.put("sta2c24", order.getWeight());
        params.put("sta2c25", order.getState());
        params.put("sta2c26", order.getAgeGroup());
        params.put("sta2c27", order.getAgeGroupCode());
        params.put("sta2c28", order.getAgeGroupName());
        params.put("sta2c29", order.getExternalId());

        for (StatisticDemographic demographic : order.getDemographics())
        {
            if (demographic.isEncoded())
            {
                params.put("sta_demo_" + demographic.getIdDemographic(), demographic.getCodifiedId());
                params.put("sta_demo_" + demographic.getIdDemographic() + "_code", demographic.getCodifiedCode());
                params.put("sta_demo_" + demographic.getIdDemographic() + "_name", demographic.getCodifiedName());
            } else
            {
                params.put("sta_demo_" + demographic.getIdDemographic(), demographic.getValue());
            }
        }
        return new SimpleJdbcInsert(getConnectionStat())
                .withTableName("sta2")
                .execute(params);

    }

    /**
     * Elimina orden de tabla estadisticas
     *
     * @param order numero de orden
     *
     * @return Numero de registros afectados
     * @throws Exception Error
     */
    default int deleteOrder(long order) throws Exception
    {
        return getConnectionStat().update("DELETE FROM sta2 WHERE sta2c1 = ? ",
                order);
    }

    /**
     * Elimina un paciente de la tabla de estadisticas
     *
     * @param patient id paciente
     *
     * @return Numero de registros afectados
     * @throws Exception Error
     */
    default int deletePatient(Integer patient) throws Exception
    {
        return getConnectionStat().update("DELETE FROM sta1 WHERE sta1c1 = ? ",
                patient);
    }

    /**
     * Registro nuevo de un examen
     *
     * @param result información del resultado
     *
     * @return Registros afectados
     * @throws Exception Error
     */
    default int createResult(StatisticResult result) throws Exception
    {
        HashMap params = new HashMap();
        params.put("sta3c1", result.getId());
        params.put("sta3c2", result.getCode());
        params.put("sta3c3", result.getName());
        params.put("sta3c4", result.getSectionId());
        params.put("sta3c5", result.getSectionCode());
        params.put("sta3c6", result.getSectionName());
        params.put("sta3c7", result.getLaboratoryId());
        params.put("sta3c8", result.getLaboratoryCode());
        params.put("sta3c9", result.getLaboratoryName());
        params.put("sta3c10", result.getTestState());
        params.put("sta3c11", result.getSampleState());
        params.put("sta3c12", result.getPathology());
        params.put("sta3c13", result.getRepeats());
        params.put("sta3c14", result.getModifications());
        params.put("sta3c15", result.getValidationDate());
        params.put("sta3c16", result.getLevelComplex());
        params.put("sta3c17", result.getRate());
        params.put("sta3c18", result.getRateCode());
        params.put("sta3c19", result.getRateName());
        params.put("sta3c20", result.getPriceService());
        params.put("sta3c21", result.getPricePatient());
        params.put("sta3c22", result.getPriceAccount());
        params.put("sta3c23", result.getTypeStatistic());
        params.put("sta3c27", result.getProfile());
        params.put("sta2c1", result.getOrderNumber());
        params.put("sta1c1", result.getPatientId());

        return new SimpleJdbcInsert(getConnectionStat())
                .withTableName("sta3")
                .execute(params);

    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param searchType Indica si la consulta se hace por Fecha de Ingreso(0) o
     * Fecha de Verificación(1)
     * @param demographics Lista de demograficos.
     * @param prices Filtra exámenes de facturación
     * @param repeated Filtra exámenes con repeticiones
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<StatisticOrder> list(Long vInitial, Long vFinal, int searchType, final List<Demographic> demographics, boolean prices, boolean repeated) throws Exception
    {
        try
        {
            List<Object> params = new ArrayList<>();
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT sta2.sta2c1, sta2c2, sta2c3, sta2c4, sta2c5, sta2c6, sta2c7, sta2c8, sta2c9, sta2c10, sta2c11, sta2c12, "
                    + "sta2c13, sta2c14, sta2c15, sta2c16, sta2c17, sta2c18, sta2c19, sta2c20, sta2c21, sta2c22, sta2c23, sta2c24, "
                    + "sta1.sta1c1, sta1.sta1c2, sta1.sta1c3, sta1.sta1c4, sta1.sta1c5, sta1.sta1c6, sta1.sta1c7, sta1.sta1c8, "
                    + "sta1.sta1c9, sta1.sta1c10, "
                    + "sta3c1, sta3c2, sta3c3, sta3c4, sta3c5, sta3c6, sta3c7, sta3c8, sta3c9, sta3c10, sta3c11, sta3c12, sta3c13, sta3c14, sta3c15, sta3c16, "
                    + "sta3c17, sta3c18, sta3c19, sta3c20, sta3c21, sta3c22 ";
            String from = " "
                    + "FROM sta2 "
                    + "INNER JOIN sta1 ON sta1.sta1c1 = sta2.sta1c1 "
                    + "LEFT JOIN sta3 ON sta3.sta2c1 = sta2.sta2c1 ";
            String where;

            switch (searchType)
            {
                case 0:
                    where = " WHERE sta2.sta2c20 BETWEEN ? AND ?";
                    break;
                case 1:

                    where = " WHERE sta3.sta3c15 BETWEEN ? AND ?";
                    break;
                default:
                    return new ArrayList<>();
            }
            params.add(vInitial);
            params.add(vFinal);
            if (repeated)
            {
                where += " AND sta3c13 > 0";
            }
            if (prices)
            {
                where += " AND sta3c23 IN (0,2)";
            }

            for (Demographic demographic : demographics)
            {
                if (demographic.getOrigin().equals("O"))
                {
                    if (demographic.isEncoded())
                    {
                        query = query + ", sta2.sta_demo_" + demographic.getId();
                        query = query + ", sta2.sta_demo_" + demographic.getId() + "_code";
                        query = query + ", sta2.sta_demo_" + demographic.getId() + "_name";
                    } else
                    {
                        query = query + ", sta2.sta_demo_" + demographic.getId();
                    }
                } else
                {
                    if (demographic.getOrigin().equals("H"))
                    {
                        if (demographic.isEncoded())
                        {
                            query = query + ", sta1.sta_demo_" + demographic.getId();
                            query = query + ", sta1.sta_demo_" + demographic.getId() + "_code";
                            query = query + ", sta1.sta_demo_" + demographic.getId() + "_name";
                        } else
                        {
                            query = query + ", sta1.sta_demo_" + demographic.getId();
                        }
                    }
                }
            }
            List<StatisticOrder> orders = new ArrayList<>();
            RowMapper mapper = (RowMapper<StatisticOrder>) (ResultSet rs, int i) ->
            {
                StatisticOrder order = new StatisticOrder();
                //ORDEN
                order.setOrderNumber(rs.getLong("sta2c1"));
                if (orders.contains(order))
                {
                    if (rs.getString("sta3c1") != null)
                    {
                        StatisticResult test = new StatisticResult();
                        test.setId(rs.getInt("sta3c1"));
                        test.setOrderNumber(rs.getLong("sta2c1"));
                        test.setCode(rs.getString("sta3c2"));
                        test.setName(rs.getString("sta3c3"));
                        test.setSectionId(rs.getInt("sta3c4"));
                        test.setSectionCode(rs.getString("sta3c5"));
                        test.setSectionName(rs.getString("sta3c6"));
                        test.setLaboratoryId(rs.getInt("sta3c7"));
                        test.setLaboratoryCode(rs.getString("sta3c8"));
                        test.setLaboratoryName(rs.getString("sta3c9"));
                        test.setTestState(rs.getInt("sta3c10"));
                        test.setSampleState(rs.getInt("sta3c11"));
                        test.setPathology(rs.getInt("sta3c12"));
                        test.setRepeats(rs.getInt("sta3c13"));
                        test.setModifications(rs.getInt("sta3c14"));
                        test.setValidationDate(rs.getInt("sta3c15"));
                        test.setLevelComplex(rs.getInt("sta3c16"));
                        //Precios
                        test.setRate(rs.getInt("sta3c17"));
                        test.setRateCode(rs.getString("sta3c18"));
                        test.setRateName(rs.getString("sta3c19"));
                        test.setPriceService(rs.getBigDecimal("sta3c20"));
                        test.setPricePatient(rs.getBigDecimal("sta3c21"));
                        test.setPriceAccount(rs.getBigDecimal("sta3c22"));
                        orders.get(orders.indexOf(order)).getResults().add(test);
                    }
                } else
                {
                    order.setOrderType(rs.getInt("sta2c2"));
                    order.setOrderTypeCode(rs.getString("sta2c3"));
                    order.setOrderTypeName(rs.getString("sta2c4"));
                    order.setBranch(rs.getInt("sta2c5"));
                    order.setBranchCode(rs.getString("sta2c6"));
                    order.setBranchName(rs.getString("sta2c7"));
                    order.setService(rs.getInt("sta2c8"));
                    order.setServiceCode(rs.getString("sta2c9"));
                    order.setServiceName(rs.getString("sta2c10"));
                    order.setPhysician(rs.getInt("sta2c11"));
                    order.setPhysicianCode(rs.getString("sta2c12"));
                    order.setPhysicianName(rs.getString("sta2c13"));
                    order.setAccount(rs.getInt("sta2c14"));
                    order.setAccountCode(rs.getString("sta2c15"));
                    order.setAccountName(rs.getString("sta2c16"));
                    order.setRate(rs.getInt("sta2c17"));
                    order.setRateCode(rs.getString("sta2c18"));
                    order.setRateName(rs.getString("sta2c19"));
                    order.setDate(rs.getInt("sta2c20"));
                    order.setDateTime(rs.getTimestamp("sta2c21"));
                    order.setAge(rs.getInt("sta2c22"));
                    order.setUnit(rs.getInt("sta2c23"));
                    order.setWeight(rs.getBigDecimal("sta2c24"));
                    //PACIENTE
                    order.getPatient().setId(rs.getInt("sta1c1"));
                    order.getPatient().setGender(rs.getInt("sta1c2"));
                    order.getPatient().setGenderSpanish(rs.getString("sta1c3"));
                    order.getPatient().setGenderEnglish(rs.getString("sta1c4"));
                    order.getPatient().setDocumentType(rs.getInt("sta1c5"));
                    order.getPatient().setDocumentTypeCode(rs.getString("sta1c6"));
                    order.getPatient().setDocumentTypeName(rs.getString("sta1c7"));
                    order.getPatient().setRace(rs.getInt("sta1c8"));
                    order.getPatient().setRaceCode(rs.getString("sta1c9"));
                    order.getPatient().setRaceName(rs.getString("sta1c10"));

                    StatisticDemographic demoValue = null;
                    for (Demographic demographic : demographics)
                    {
                        demoValue = new StatisticDemographic();
                        demoValue.setIdDemographic(demographic.getId());
                        demoValue.setDemographic(demographic.getName());
                        demoValue.setEncoded(demographic.isEncoded());
                        if (demographic.isEncoded())
                        {
                            if (rs.getString("sta_demo_" + demographic.getId()) != null)
                            {
                                demoValue.setIdOrder(rs.getLong("sta2c1"));
                                demoValue.setCodifiedId(rs.getInt("sta_demo_" + demographic.getId()));
                                demoValue.setCodifiedCode(rs.getString("sta_demo_" + demographic.getId() + "_code"));
                                demoValue.setCodifiedName(rs.getString("sta_demo_" + demographic.getId() + "_name"));
                            }
                        } else
                        {
                            demoValue.setNotCodifiedValue(rs.getString("sta_demo_" + demographic.getId()));
                        }

                        if (demographic.getOrigin().equals("O"))
                        {
                            order.getDemographics().add(demoValue);
                        } else
                        {
                            order.getPatient().getDemographics().add(demoValue);
                        }
                    }

                    if (rs.getString("sta3c1") != null)
                    {
                        StatisticResult test = new StatisticResult();
                        test.setId(rs.getInt("sta3c1"));
                        test.setOrderNumber(rs.getLong("sta2c1"));
                        test.setCode(rs.getString("sta3c2"));
                        test.setName(rs.getString("sta3c3"));
                        test.setSectionId(rs.getInt("sta3c4"));
                        test.setSectionCode(rs.getString("sta3c5"));
                        test.setSectionName(rs.getString("sta3c6"));
                        test.setLaboratoryId(rs.getInt("sta3c7"));
                        test.setLaboratoryCode(rs.getString("sta3c8"));
                        test.setLaboratoryName(rs.getString("sta3c9"));
                        test.setTestState(rs.getInt("sta3c10"));
                        test.setSampleState(rs.getInt("sta3c11"));
                        test.setPathology(rs.getInt("sta3c12"));
                        test.setRepeats(rs.getInt("sta3c13"));
                        test.setModifications(rs.getInt("sta3c14"));
                        test.setValidationDate(rs.getInt("sta3c15"));
                        test.setLevelComplex(rs.getInt("sta3c16"));
                        //Precios
                        test.setRate(rs.getInt("sta3c17"));
                        test.setRateCode(rs.getString("sta3c18"));
                        test.setRateName(rs.getString("sta3c19"));
                        test.setPriceService(rs.getBigDecimal("sta3c20"));
                        test.setPricePatient(rs.getBigDecimal("sta3c21"));
                        test.setPriceAccount(rs.getBigDecimal("sta3c22"));

                        order.getResults().add(test);
                    }

                    orders.add(order);
                }
                return order;
            };
            getConnectionStat().query(query + from + where, mapper, params.toArray());
            return orders;
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param searchType Indica si la consulta se hace por Fecha de Ingreso(0) o
     * Fecha de Verificación(1), Rango de ordenes (2)
     * @param demographics Lista de demograficos.
     * @param prices Filtra exámenes de facturación
     * @param repeated Filtra exámenes con repeticiones
     * @param opportunity Indica la consulta de oportunidad
     * @param filterDemographics
     * @param listTests
     * @param isGroupProfile
     * @param testFilterType 
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<StatisticOrder> list(Long vInitial, Long vFinal, int searchType, final List<Demographic> demographics, boolean prices, boolean repeated, Integer opportunity, List<FilterDemographic> filterDemographics, List<Integer> listTests, boolean isGroupProfile, int testFilterType) throws Exception
    {
        try
        {

            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String sta2;
            String sta3;
            String sta4;
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<StatisticOrder> orders = new ArrayList<>();
            for (Integer year : years)
            {

                sta2 = year.equals(currentYear) ? "sta2" : "sta2_" + year;
                sta3 = year.equals(currentYear) ? "sta3" : "sta3_" + year;
                sta4 = year.equals(currentYear) ? "sta4" : "sta4_" + year;

                boolean tableExists = getToolsDao().tableExists(getConnectionStat(), sta2);
                tableExists = tableExists ? getToolsDao().tableExists(getConnectionStat(), sta3) : tableExists;
                tableExists = tableExists ? getToolsDao().tableExists(getConnectionStat(), sta4) : tableExists;

                if (tableExists)
                {
                    List<Object> params = new LinkedList<>();

                    StringBuilder query = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append(
                            "SELECT sta2.sta2c1, "
                            + "sta2c2, "
                            + "sta2c3, "
                            + "sta2c4, "
                            + "sta2c5, "
                            + "sta2c6, "
                            + "sta2c7, "
                            + "sta2c8, "
                            + "sta2c9, "
                            + "sta2c10, "
                            + "sta2c11, "
                            + "sta2c12, "
                            + "sta2c13, "
                            + "sta2c14, "
                            + "sta2c15, "
                            + "sta2c16, "
                            + "sta2c17, "
                            + "sta2c18, "
                            + "sta2c19, "
                            + "sta2c22, "
                            + "sta2c23, "
                            + "sta2c26, "
                            + "sta2c27, "
                            + "sta2c28, "
                            + "sta2c29, "
                            + "sta1.sta1c1, "
                            + "sta1.sta1c2, "
                            + "sta1.sta1c3, "
                            + "sta1.sta1c4, "
                            + "sta1.sta1c5, "
                            + "sta1.sta1c6, "
                            + "sta1.sta1c7, "
                            + "sta1.sta1c11, "
                            + "sta3.sta3c1, "
                            + "sta3c2, "
                            + "sta3c3, "
                            + "sta3c4, "
                            + "sta3c5, "
                            + "sta3c6, "
                            + "sta3c7, "
                            + "sta3c8, "
                            + "sta3c9, "
                            + "sta3c10, "
                            + "sta3c11, "
                            + "sta3c12, "
                            + "sta3c23, "
                            + "sta3c24, "
                            + "sta3c25, "
                            + "sta3c26, "
                            + "sta3c27, "
                            + "sta4c4, "
                            + "sta4c5, "
                            + "sta4c6, "
                            + "sta4c7, "
                            + "sta4c8, "
                            + "sta4c9, "
                            + "sta4c10, "
                            + "sta4c11, "
                            + "sta4c12, "
                            + "sta4c13, "
                            + "sta4c14, "
                            + "sta4c15, "
                            + "sta4c16, "
                            + "sta4c17, "
                            + "sta4c18, "
                            + "sta4c19, "
                            + "sta4c20, "
                            + "sta4c21, "
                            + "sta4c22, "
                            + "sta4c23, "
                            + "sta4c24, "
                            + "sta4c25, "
                            + "sta4c26, "
                            + "sta4c27, "
                            + "sta4c7, "
                            + "sta4c8, "
                            + "sta4c11 "
                    );
                    StringBuilder from = new StringBuilder();
                    from.append(" FROM ").append(sta2)
                            .append(" as sta2 INNER JOIN sta1 ON sta1.sta1c1 = sta2.sta1c1 LEFT JOIN ")
                            .append(sta3)
                            .append(" as sta3 ON sta3.sta2c1 = sta2.sta2c1 LEFT JOIN ")
                            .append(sta4)
                            .append(" as sta4 ON sta4.sta2c1 = sta3.sta2c1 AND sta4.sta3c1 = sta3.sta3c1 ");

                    StringBuilder where = new StringBuilder();
                    switch (searchType)
                    {
                        case 0:
                            where.append(" WHERE sta2.sta2c20 BETWEEN ? AND ?");
                            break;
                        case 1:
                            where.append(" WHERE sta3.sta3c15 BETWEEN ? AND ?");
                            break;
                        case 2:
                            where.append(" WHERE sta2.sta2c1 BETWEEN ? AND ?");
                            break;
                        case 3:
                            where.append(" WHERE sta4.sta4c12 BETWEEN ? AND ?");
                            params.addAll(Tools.rangeDates(vInitial.intValue(), vFinal.intValue()));
                            break;
                        default:
                            return new ArrayList<>();
                    }
                    
                    //Filtro por examenes confidenciales y areas
                    switch (testFilterType)
                    {
                        case 1:
                            where.append(" AND sta3.sta3c4 IN (").append(listTests.stream().map(area -> area.toString()).collect(Collectors.joining(","))).append(") ");
                            break;
                        case 2:
                            String listtest = listTests.stream().map(test -> test.toString()).collect(Collectors.joining(","));
                            where.append(" AND ((sta3.sta3c27 IN (").append(listtest).append(")) or sta3.sta3c1 IN (").append(listtest).append(")) ");
                            break;
                        case 3:
                            String listtestconfidencial = listTests.stream().map(test -> test.toString()).collect(Collectors.joining(","));
                            where.append(" AND ((sta3.sta3c27 IN (").append(listtestconfidencial).append(")) or sta3.sta3c1 IN (").append(listtestconfidencial).append(")) ");
                            break;
                        default:
                            break;
                    }
                    
                    
                    if (params.isEmpty())
                    {
                        params.add(vInitial);
                        params.add(vFinal);
                    }

                    //where.append(" AND sta2c25 = 1 AND sta3c10 = ").append(LISEnum.ResultTestState.VALIDATED.getValue());
                    where.append(" AND sta2c25 = 1");
                    
                    if (!isGroupProfile)
                    {
                     
                        if (!listTests.isEmpty())
                        {
                            //where.append(" AND (sta3c10 = 0 OR sta3c10 >= ").append(LISEnum.ResultTestState.VALIDATED.getValue()).append(")");
                            String result = Arrays.toString(listTests.toArray());
                            result = result.substring(1, result.length() - 1).trim();
                            where.append(" AND (sta3.sta3c1 IN (").append(result).append(") OR sta3c27 IN (").append(result).append("))");
                        } else
                        {
                            where.append(" AND sta3c23 = 1");
                            where.append(" AND sta3c10 >= ").append(LISEnum.ResultTestState.VALIDATED.getValue());
                        }
                    }
                    SQLTools.buildSQLDemographicFilterStatistics(filterDemographics, params, query, where);

                    demographics.stream().filter(demographic -> (demographic.getId() > 0) && demographic.getOrigin() != null).forEachOrdered(demographic ->
                    {
                        query.append(", sta_demo_").append(demographic.getId());
                        if (demographic.isEncoded())
                        {
                            query.append(", sta_demo_").append(demographic.getId()).append("_code");
                            query.append(", sta_demo_").append(demographic.getId()).append("_name");
                        }
                    });

                    RowMapper mapper = (RowMapper<StatisticOrder>) (ResultSet rs, int i) ->
                    {

                        StatisticOrder order = new StatisticOrder();
                        StatisticResult test = null;
                        Date currentDate = new Date();
                        if (rs.getString("sta3c1") != null)
                        {
                            test = new StatisticResult();
                            test.setId(rs.getInt("sta3c1"));
                            test.setOrderNumber(rs.getLong("sta2c1"));
                            test.setCode(rs.getString("sta3c2"));
                            test.setName(rs.getString("sta3c3"));
                            test.setSectionId(rs.getInt("sta3c4"));
                            test.setSectionCode(rs.getString("sta3c5"));
                            test.setSectionName(rs.getString("sta3c6"));
                            test.setLaboratoryId(rs.getInt("sta3c7"));
                            test.setLaboratoryCode(rs.getString("sta3c8"));
                            test.setLaboratoryName(rs.getString("sta3c9"));
                            test.setTestState(rs.getInt("sta3c10"));
                            test.setSampleState(rs.getInt("sta3c11"));
                            test.setPathology(rs.getInt("sta3c12"));
                            test.setMultiplyBy(rs.getInt("sta3c24"));
                            test.setBasic(rs.getInt("sta3c25"));
                            test.setProfile(rs.getInt("sta3c27"));
                            test.setApplyStadistic(rs.getInt("sta3c23"));
                            //Tiempos
                            if (opportunity != null)
                            {
                                test.setOpportunityTimes(new StatisticOpportunity(rs.getInt("sta3c1"), rs.getLong("sta2c1")));
                                test.getOpportunityTimes().setEntryDate(rs.getTimestamp("sta4c4"));
                                test.getOpportunityTimes().setEntryElapsedTime(rs.getLong("sta4c5"));
                                test.getOpportunityTimes().setEntryUser(rs.getInt("sta4c6"));

                                test.getOpportunityTimes().setTakeDate(rs.getTimestamp("sta4c7"));
                                test.getOpportunityTimes().setTakeElapsedTime(rs.getTimestamp("sta4c7") == null ? null : rs.getLong("sta4c8"));
                                test.getOpportunityTimes().setTakeUser(rs.getTimestamp("sta4c7") == null ? null : rs.getInt("sta4c11"));
                                test.getOpportunityTimes().setTransportDate(rs.getTimestamp("sta4c9"));
                                test.getOpportunityTimes().setPrintSampleDate(rs.getTimestamp("sta4c27"));

                                test.getOpportunityTimes().setVerifyDate(rs.getTimestamp("sta4c12"));
                                test.getOpportunityTimes().setVerifyElapsedTime(rs.getTimestamp("sta4c12") == null ? null : rs.getLong("sta4c13"));
                                test.getOpportunityTimes().setVerifyUser(rs.getTimestamp("sta4c12") == null ? null : rs.getInt("sta4c14"));

                                test.getOpportunityTimes().setResultDate(rs.getTimestamp("sta4c15"));
                                test.getOpportunityTimes().setResultElapsedTime(rs.getTimestamp("sta4c15") == null ? null : rs.getLong("sta4c16"));
                                test.getOpportunityTimes().setResultUser(rs.getTimestamp("sta4c15") == null ? null : rs.getInt("sta4c17"));

                                test.getOpportunityTimes().setValidDate(rs.getTimestamp("sta4c21"));
                                test.getOpportunityTimes().setValidElapsedTime(rs.getTimestamp("sta4c21") == null ? null : rs.getLong("sta4c22"));
                                test.getOpportunityTimes().setValidUser(rs.getTimestamp("sta4c21") == null ? null : rs.getInt("sta4c23"));

                                test.getOpportunityTimes().setPrintDate(rs.getTimestamp("sta4c24"));
                                test.getOpportunityTimes().setPrintElapsedTime(rs.getTimestamp("sta4c24") == null ? null : rs.getLong("sta4c25"));
                                test.getOpportunityTimes().setPrintUser(rs.getTimestamp("sta4c24") == null ? null : rs.getInt("sta4c26"));

                                test.getOpportunityTimes().setCurrentDate(currentDate);

                                //Calcula el tiempo observado en dias (Tiempo de validacion - tiempo de verificacion)
                                long days = getExpectedTimeDays(rs.getTimestamp("sta4c21"), rs.getTimestamp("sta4c12"));
                                test.getOpportunityTimes().setObservedTimeDays(days);
                            }
                        }
                        //ORDEN
                        order.setOrderNumber(rs.getLong("sta2c1"));
                        if (orders.contains(order))
                        {
                            order = orders.get(orders.indexOf(order));
                        } else
                        {
                            order.setOrderType(rs.getInt("sta2c2"));
                            order.setOrderTypeCode(rs.getString("sta2c3"));
                            order.setOrderTypeName(rs.getString("sta2c4"));
                            order.setBranch(rs.getInt("sta2c5"));
                            order.setBranchCode(rs.getString("sta2c6"));
                            order.setBranchName(rs.getString("sta2c7"));
                            order.setService(rs.getInt("sta2c8"));
                            order.setServiceCode(rs.getString("sta2c9"));
                            order.setServiceName(rs.getString("sta2c10"));
                            order.setPhysician(rs.getInt("sta2c11"));
                            order.setPhysicianCode(rs.getString("sta2c12"));
                            order.setPhysicianName(rs.getString("sta2c13"));
                            order.setAccount(rs.getInt("sta2c14"));
                            order.setAccountCode(rs.getString("sta2c15"));
                            order.setAccountName(rs.getString("sta2c16"));
                            order.setRate(rs.getInt("sta2c17"));
                            order.setRateCode(rs.getString("sta2c18"));
                            order.setRateName(rs.getString("sta2c19"));
                            order.setAge(rs.getInt("sta2c22"));
                            order.setUnit(rs.getInt("sta2c23"));
                            order.setAgeGroup(rs.getString("sta2c26") == null ? null : rs.getInt("sta2c26"));
                            order.setAgeGroupCode(rs.getString("sta2c27"));
                            order.setAgeGroupName(rs.getString("sta2c28"));
                            order.setExternalId(rs.getString("sta2c29"));
                            //PACIENTE
                            order.getPatient().setId(rs.getInt("sta1c1"));
                            order.getPatient().setGender(rs.getInt("sta1c2"));
                            order.getPatient().setGenderSpanish(rs.getString("sta1c3"));
                            order.getPatient().setGenderEnglish(rs.getString("sta1c4"));
                            order.getPatient().setDocumentType(rs.getInt("sta1c5"));
                            order.getPatient().setDocumentTypeCode(rs.getString("sta1c6"));
                            order.getPatient().setDocumentTypeName(rs.getString("sta1c7"));
                            order.getPatient().setDocumentTypeName(rs.getString("sta1c7"));
                            order.getPatient().setPatientId(Tools.decrypt(rs.getString("sta1c11")));

                            StatisticDemographic demoValue = null;

                            for (Demographic demographic : demographics)
                            {
                                demoValue = new StatisticDemographic();
                                demoValue.setIdDemographic(demographic.getId());
                                demoValue.setDemographic(demographic.getName());
                                demoValue.setEncoded(demographic.isEncoded());
                                if (demographic.isEncoded())
                                {
                                    if (rs.getString("sta_demo_" + demographic.getId()) != null)
                                    {
                                        demoValue.setCodifiedId(rs.getInt("sta_demo_" + demographic.getId()));
                                        demoValue.setCodifiedCode(rs.getString("sta_demo_" + demographic.getId() + "_code"));
                                        demoValue.setCodifiedName(rs.getString("sta_demo_" + demographic.getId() + "_name"));
                                    }
                                } else
                                {
                                    demoValue.setNotCodifiedValue(rs.getString("sta_demo_" + demographic.getId()));
                                }
                                if (demographic.getOrigin().equals("O"))
                                {
                                    order.getDemographics().add(demoValue);
                                } else
                                {
                                    order.getPatient().getDemographics().add(demoValue);
                                }
                            }

                            orders.add(order);
                        }
                        if (test != null)
                        {
                            order.getResults().add(test);
                        }
                        return order;
                    };
                    getConnectionStat().query(query.toString() + " " + from.toString() + " " + where.toString(), mapper, params.toArray());
                    return orders;

                }
            }
            return orders;
        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    default long getExpectedTimeDays(Timestamp timestampValidate, Timestamp timestampVerify)
    {
        long day = 0;
        if (timestampValidate != null && timestampVerify != null)
        {
            long startTime = timestampVerify.getTime();
            long endTime = timestampValidate.getTime();
            long daysFrom = (long) Math.floor(startTime / (1000 * 60 * 60 * 24)); // convertimos a dias, para que no afecten cambios de hora 
            long daysUntil = (long) Math.floor(endTime / (1000 * 60 * 60 * 24)); // convertimos a dias, para que no afecten cambios de hora
            day = daysUntil - daysFrom;
        }
        return day;
    }

    /**
     * Consulta registro en la tabla de tiempos
     *
     * @param serviceId id de servicio
     * @param testId id del examen
     * @return true si encuentra 1 registro
     */
    default Integer getTimeMaxByServiceAndByIdTest(int testId, int serviceId)
    {
        try
        {
            List<Object> params = new ArrayList<>();
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab171c4 FROM lab171 WHERE lab171c3 = 0 AND lab171c1 = ? AND lab10c1 = ?";
            params.add(testId);
            params.add(serviceId);
            return getConnection().queryForObject(query, Integer.class, params.toArray());
        } catch (EmptyResultDataAccessException ex)
        {
            return -1;
        }
    }

    /**
     * Consulta registro en la tabla de tiempos
     *
     * @param orderNumber id del examen
     * @return true si encuentra 1 registro
     */
    default String getLaboratoryName(long orderNumber, int idTest) throws Exception
    {
        try
        {
            List<Object> params = new ArrayList<>();
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab40.lab40c3 FROM lab57"
                    + " LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1"
                    + " WHERE lab22c1 = ? AND lab39c1 = ?";
            params.add(orderNumber);
            params.add(idTest);
            return getConnection().queryForObject(query, String.class, params.toArray());
        } catch (EmptyResultDataAccessException ex)
        {
            return "";
        }
    }
    
    /**
     * Obtiene la lista de valores de referencia de un rango de ordenes.
     *
     * @param orderIds Id´s de ordenes
     *
     * @return Lista de valores de referencia
     * @throws Exception Error en la base de datos.
     */
    default List<ReferenceValues> getReferenceValues(List<Long> orderIds) throws Exception
    {
        try
        {
            List<ReferenceValues> list = new LinkedList<>();
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(orderIds.stream().mapToLong(order -> order).min().getAsLong()), String.valueOf(orderIds.stream().mapToLong(order -> order).max().getAsLong()));
            String lab57;

            int currentYear = DateTools.dateToNumberYear(new Date());
            
            for (Integer year : years)
            {
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                
                StringBuilder query = new StringBuilder();
                
                query.append(ISOLATION_READ_UNCOMMITTED)
                        .append( " SELECT lab57.lab22c1")
                        .append(", lab57.lab39c1")
                        .append(", lab48c12")
                        .append(", lab48c13")
                        .append(", lab50n.lab50c2 as lab50c2n")
                        .append(", lab50p.lab50c2 as lab50c2p");
                
                StringBuilder from = new StringBuilder();
                
                from.append(" FROM ").append(lab57).append(" as lab57 ");
                from.append(" LEFT JOIN lab50 lab50n ON lab50n.lab50c1 = lab57.lab50c1_3 ");
                from.append(" LEFT JOIN lab50 lab50p ON lab50p.lab50c1 = lab57.lab50c1_1 ");
                                       
                from.append(" WHERE lab57.lab22c1 in (").append(orderIds.stream().map(o -> o.toString()).collect(Collectors.joining(","))).append(")");


                RowMapper mapper = (RowMapper<ReferenceValues>) (ResultSet rs, int i) ->
                {
                    ReferenceValues reference = new ReferenceValues();
                    
                    reference.setOrder(rs.getLong("lab22c1"));
                    reference.setTestId(rs.getInt("lab39c1"));
                            
                    BigDecimal refMin = rs.getBigDecimal("lab48c12");
                    BigDecimal refMax = rs.getBigDecimal("lab48c13");
                    
                    reference.setRefLiteral(rs.getString("lab50c2n"));
                    
                    if (refMin != null && refMax!= null)
                    {
                        reference.setRefMin(refMin);
                        reference.setRefMax(refMax);
                        
                        String bigDecimalRefMin = String.valueOf(refMin.doubleValue());
                        String bigDecimalRefMax = String.valueOf(refMax.doubleValue());
                        reference.setRefInterval(bigDecimalRefMin + " - " + bigDecimalRefMax);
                    } else
                    {
                        reference.setRefMin(BigDecimal.ZERO);
                        reference.setRefMax(BigDecimal.ZERO);
                        reference.setRefInterval(reference.getRefLiteral());
                    }
                    
                    list.add(reference);
                    return reference;
                };
                getConnection().query(query.toString() + from.toString() , mapper);
            }
            return list;
            
        } catch (EmptyResultDataAccessException ex)
        {
            ResultsLog.error(ex);
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Consulta registro en la tabla de tiempos
     *
     * @param orderNumber numero de la orden
     * @return true si encuentra 1 registro
     */
    default List<StatisticOpportunity> opportunityExistsByOrder(long orderNumber) throws Exception
    {
        try
        {
            List<StatisticOpportunity> list = new LinkedList<>();
            
            Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String sta4 = year.equals(currentYear) ? "sta4" : "sta4_" + year;
            
            StringBuilder query = new StringBuilder();
                
            query.append(ISOLATION_READ_UNCOMMITTED).append( " SELECT sta2c1, sta3c1 ");
                
                StringBuilder from = new StringBuilder();
                
                from.append(" FROM ").append(sta4).append(" as sta4 ");
                                       
                from.append(" WHERE sta2c1 = ").append(orderNumber);

                RowMapper mapper = (RowMapper<StatisticOpportunity>) (ResultSet rs, int i) ->
                {
                    StatisticOpportunity opportunity = new StatisticOpportunity();
                    
                    opportunity.setOrderNumber(rs.getLong("sta2c1"));
                    opportunity.setId(rs.getInt("sta3c1"));

                    list.add(opportunity);
                    return opportunity;
                };
                getConnectionStat().query(query.toString() + from.toString() , mapper);
                
            return list;
            
        } catch (EmptyResultDataAccessException ex)
        {
            ResultsLog.error(ex);
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Agrega nuevo registro para los tiempos de oportunidad
     *
     * @param opportunity información de los tiempos de oportunidad
     * @param orderNumber Numero de la orden
     *
     * @return registros afectados
     * @throws Exception
     */
    default int addOpportunityTimeBatch(List<StatisticOpportunity> list, Long orderNumber) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String sta4 = year.equals(currentYear) ? "sta4" : "sta4_" + year;
            
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnectionStat())
                .withTableName(sta4);
        
        list.stream().map((opportunity) -> {
            HashMap params = new HashMap();
            params.put("sta2c1", opportunity.getOrderNumber());
            params.put("sta3c1", opportunity.getId());
            params.put("sta4c4", opportunity.getEntryDate());
            params.put("sta4c6", opportunity.getEntryUser());
            params.put("sta4c12", opportunity.getVerifyDate());
            params.put("sta4c13", opportunity.getVerifyElapsedTime());
            params.put("sta4c14", opportunity.getVerifyUser());
            return params;
        }).forEachOrdered((parameters) -> {
            batchArray.add(parameters);
        });

        insert.executeBatch(batchArray.toArray(new HashMap[list.size()]));
        return 1;
    }
}



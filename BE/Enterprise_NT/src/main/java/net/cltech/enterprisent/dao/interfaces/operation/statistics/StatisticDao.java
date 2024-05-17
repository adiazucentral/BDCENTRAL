package net.cltech.enterprisent.dao.interfaces.operation.statistics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.operation.filters.StatisticFilter;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import net.cltech.enterprisent.domain.operation.microbiology.ResultMicrobiology;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.statistic.StatisticCashBox;
import net.cltech.enterprisent.domain.operation.statistic.StatisticDemographic;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOpportunity;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOrder;
import net.cltech.enterprisent.domain.operation.statistic.StatisticPatient;
import net.cltech.enterprisent.domain.operation.statistic.StatisticResult;
import net.cltech.enterprisent.domain.operation.widgets.WidgetEntry;
import net.cltech.enterprisent.service.interfaces.operation.orders.CashBoxService;
import net.cltech.enterprisent.start.StartApp;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.SQLTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.stadistics.StadisticsLog;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de
 * estadisticas.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 11/12/2017
 * @see Creación
 */
public interface StatisticDao
{

    /**
     * Obtiene el dao de caja
     *
     * @return Instancia de cada
     */
    public CashBoxService getcashBoxService();

    /**
     * Obtiene el dao de tools
     *
     * @return Instancia de toolsDao
     */
    public ToolsDao getToolsDao();

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
     * Registra nuevo paciente en la tabla de estadisticas
     *
     * @param patient información del paciente
     *
     * @return registros afectados
     * @throws Exception
     */
    default int createPatient(StatisticPatient patient) throws Exception
    {
        HashMap params = new HashMap();
        params.put("sta1c1", patient.getId());
        params.put("sta1c2", patient.getGender());
        params.put("sta1c5", patient.getDocumentType());
        params.put("sta1c6", patient.getDocumentTypeCode());
        params.put("sta1c7", patient.getDocumentTypeName());
        params.put("sta1c8", patient.getRace());
        params.put("sta1c9", patient.getRaceCode());
        params.put("sta1c10", patient.getRaceName());
        params.put("sta1c11", patient.getPatientId());
        for (StatisticDemographic demographic : patient.getDemographics())
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
                .withTableName("sta1")
                .execute(params);

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
    
     default int disableOrder(Long order, LISEnum.ResultOrderState state) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(order));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String sta2 = year.equals(currentYear) ? "sta2" : "sta2_" + year;
        String query = "UPDATE " + sta2 + " SET sta2c25 = ?  WHERE sta2c1 = ?";
        getConnectionStat().update(query, state.getValue(), order);
        return 1;
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
        try
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
            params.put("sta3c24", result.getMultiplyBy());
            params.put("sta3c25", result.getBasic());
            params.put("sta3c26", result.getTypeStatisticBilling());
            params.put("sta3c27", result.getProfile());
            params.put("sta2c1", result.getOrderNumber());

            return new SimpleJdbcInsert(getConnectionStat())
                    .withTableName("sta3")
                    .execute(params);
        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.info("[EnterpriseNT - App] : Error creando resultados en estadisticas ");
            StadisticsLog.error(ex);
            return 0;
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
     * @param testState
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<StatisticOrder> list(Long vInitial, Long vFinal, int searchType, final List<Demographic> demographics, boolean prices, boolean repeated, Integer opportunity, List<FilterDemographic> filterDemographics, Integer testState, boolean isGroupProfiles) throws Exception
    {
        try
        {
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String sta2;
            String sta3;
            String sta4;
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<StatisticOrder> orders = new LinkedList<>();
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
                    List<Object> params = new ArrayList<>();
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
                            + "sta2c20, "
                            + "sta2c21, "
                            + "sta2c22, "
                            + "sta2c23, "
                            + "sta2c24, "
                            + "sta2c26, "
                            + "sta2c27, "
                            + "sta2c28, "
                            + "sta1.sta1c1, "
                            + "sta1.sta1c2, "
                            + "sta1.sta1c3, "
                            + "sta1.sta1c4, "
                            + "sta1.sta1c5, "
                            + "sta1.sta1c6, "
                            + "sta1.sta1c7, "
                            + "sta1.sta1c8, "
                            + "sta1.sta1c9, "
                            + "sta1.sta1c10, "
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
                            + "sta3c13, "
                            + "sta3c14, "
                            + "sta3c15, "
                            + "sta3c16, "
                            + "sta3c17, "
                            + "sta3c18, "
                            + "sta3c19, "
                            + "sta3c20, "
                            + "sta3c21, "
                            + "sta3c22, "
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
                            + "sta4c26 "
                    );

                    String from = " FROM " + sta2 + " AS sta2 "
                            + "INNER JOIN sta1 ON sta1.sta1c1 = sta2.sta1c1 "
                            + "LEFT JOIN " + sta3 + " AS sta3 ON sta3.sta2c1 = sta2.sta2c1 "
                            + "LEFT JOIN " + sta4 + " AS sta4 ON sta4.sta2c1 = sta3.sta2c1 AND sta4.sta3c1 = sta3.sta3c1 ";

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
                    if (params.isEmpty())
                    {
                        params.add(vInitial);
                        params.add(vFinal);
                    }
                    if (repeated)
                    {
                        where.append(" AND sta3c13 > 0");
                    }
                    if (prices)
                    {
                        where.append(" AND sta3c26 = 1");
                    } else if (!isGroupProfiles)
                    {
                        where.append(" AND sta3c23 = 1");
                    }

                    switch (testState)
                    {
                        case StatisticFilter.FILTER_TEST_ALL:
                            where.append(" AND sta3c10 >= ").append(LISEnum.ResultTestState.ORDERED.getValue());
                            break;
                        case StatisticFilter.FILTER_TEST_VALID_PRINTED:
                            where.append(" AND sta3c10 >= ").append(LISEnum.ResultTestState.VALIDATED.getValue());

                            break;
                        case StatisticFilter.FILTER_TEST_PRINTED:
                            where.append(" AND sta3c10 = ").append(LISEnum.ResultTestState.DELIVERED.getValue());

                            break;
                        case StatisticFilter.FILTER_TEST_NOT_VALID:
                            where.append(" AND sta3c10 < ").append(LISEnum.ResultTestState.VALIDATED.getValue());

                            break;
                        case StatisticFilter.FILTER_TEST_RESULT:
                            where.append(" AND sta3c10 = ").append(LISEnum.ResultTestState.REPORTED.getValue());

                            break;
                        case StatisticFilter.FILTER_TEST_VALID:
                            where.append(" AND sta3c10 = ").append(LISEnum.ResultTestState.VALIDATED.getValue());

                            break;
                        default:
                            break;
                    }

                    where.append(" AND sta2c25 = 1 ");

                    SQLTools.buildSQLDemographicFilterStatistics(filterDemographics, params, query, where);

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
                            test.setRepeats(rs.getInt("sta3c13"));
                            test.setModifications(rs.getInt("sta3c14"));
                            test.setValidationDate(rs.getInt("sta3c15"));
                            test.setLevelComplex(rs.getInt("sta3c16"));
                            test.setMultiplyBy(rs.getInt("sta3c24"));
                            test.setProfile(rs.getInt("sta3c27"));
                            test.setApplyStadistic(rs.getInt("sta3c23"));
                            //Precios
                            test.setRate(rs.getInt("sta3c17"));
                            test.setRateCode(rs.getString("sta3c18"));
                            test.setRateName(rs.getString("sta3c19"));
                            test.setPriceService(rs.getBigDecimal("sta3c20"));
                            test.setPricePatient(rs.getBigDecimal("sta3c21"));
                            test.setPriceAccount(rs.getBigDecimal("sta3c22"));
                            test.setBasic(rs.getInt("sta3c25"));

                            //Tiempos
                            if (opportunity != null)
                            {
                                test.setOpportunityTimes(new StatisticOpportunity(rs.getInt("sta3c1"), rs.getLong("sta2c1")));
                                test.getOpportunityTimes().setEntryDate(rs.getTimestamp("sta4c4"));
                                test.getOpportunityTimes().setEntryElapsedTime(rs.getLong("sta4c5"));
                                test.getOpportunityTimes().setEntryUser(rs.getInt("sta4c6"));

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
                            order.setDate(rs.getInt("sta2c20"));
                            order.setDateTime(rs.getTimestamp("sta2c21"));
                            order.setAge(rs.getInt("sta2c22"));
                            order.setUnit(rs.getInt("sta2c23"));
                            order.setWeight(rs.getBigDecimal("sta2c24"));
                            order.setAgeGroup(rs.getString("sta2c26") == null ? null : rs.getInt("sta2c26"));
                            order.setAgeGroupCode(rs.getString("sta2c27"));
                            order.setAgeGroupName(rs.getString("sta2c28"));
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
                            order.getPatient().setPatientId(Tools.decrypt(rs.getString("sta1c11")));

                            StatisticDemographic demoValue = null;
                            for (FilterDemographic demographic : filterDemographics)
                            {
                                if (demographic.getDemographic() != null)
                                {
                                    if (demographic.getDemographic() > 0)
                                    {
                                        if (demographic.getOrigin() != null)
                                        {
                                            demoValue = new StatisticDemographic();
                                            demoValue.setIdDemographic(demographic.getDemographic());

                                            if (rs.getString("sta_demo_" + demographic.getDemographic()) != null)
                                            {
                                                demoValue.setIdOrder(rs.getLong("sta2c1"));
                                                demoValue.setCodifiedId(rs.getInt("sta_demo_" + demographic.getDemographic()));
                                                demoValue.setCodifiedCode(rs.getString("sta_demo_" + demographic.getDemographic() + "_code"));
                                                demoValue.setCodifiedName(rs.getString("sta_demo_" + demographic.getDemographic() + "_name"));
                                            }

                                            if (demographic.getOrigin().equals("O"))
                                            {
                                                order.getDemographics().add(demoValue);
                                            } else
                                            {
                                                order.getPatient().getDemographics().add(demoValue);
                                            }
                                        }
                                    }
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
                    //listOrders.addAll(orders);

                    getConnectionStat().query(query + from + where, mapper, params.toArray());
                }
            }
            return orders;
        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos, independientememte si tiene caja.
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
     * @param testState
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<StatisticOrder> listBox(Long vInitial, Long vFinal, int searchType, final List<Demographic> demographics, boolean prices, boolean repeated, Integer opportunity, List<FilterDemographic> filterDemographics, Integer testState) throws Exception
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

                    List<Object> params = new ArrayList<>();

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
                            + "sta2c20, "
                            + "sta2c21, "
                            + "sta2c22, "
                            + "sta2c23, "
                            + "sta2c24, "
                            + "sta2c26, "
                            + "sta2c27, "
                            + "sta2c28, "
                            + "sta1.sta1c1, "
                            + "sta1.sta1c2, "
                            + "sta1.sta1c3, "
                            + "sta1.sta1c4, "
                            + "sta1.sta1c5, "
                            + "sta1.sta1c6, "
                            + "sta1.sta1c7, "
                            + "sta1.sta1c8, "
                            + "sta1.sta1c9, "
                            + "sta1.sta1c10, "
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
                            + "sta3c13, "
                            + "sta3c14, "
                            + "sta3c15, "
                            + "sta3c16, "
                            + "sta3c17, "
                            + "sta3c18, "
                            + "sta3c19, "
                            + "sta3c20, "
                            + "sta3c21, "
                            + "sta3c22, "
                            + "sta3c24, "
                            + "sta3c25, "
                            + "sta3c26, "
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
                            + "sta4c26 "
                    );

                    String from = " FROM " + sta2 + " AS sta2 "
                            + "INNER JOIN sta1 ON sta1.sta1c1 = sta2.sta1c1 "
                            + "LEFT JOIN " + sta3 + " AS sta3 ON sta3.sta2c1 = sta2.sta2c1 "
                            + "LEFT JOIN " + sta4 + " AS sta4 ON sta4.sta2c1 = sta3.sta2c1 AND sta4.sta3c1 = sta3.sta3c1 ";

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
                    if (params.isEmpty())
                    {
                        params.add(vInitial);
                        params.add(vFinal);
                    }
                    if (repeated)
                    {
                        where.append(" AND sta3c13 > 0");
                    }
                    if (prices)
                    {
                        where.append(" AND sta3c26 = 1");
                    } else
                    {
                        where.append(" AND sta3c23 = 1");
                    }

                    switch (testState)
                    {
                        case StatisticFilter.FILTER_TEST_ALL:
                            where.append(" AND sta3c10 >= ").append(LISEnum.ResultTestState.ORDERED.getValue());
                            break;
                        case StatisticFilter.FILTER_TEST_VALID_PRINTED:
                            where.append(" AND sta3c10 >= ").append(LISEnum.ResultTestState.VALIDATED.getValue());

                            break;
                        case StatisticFilter.FILTER_TEST_PRINTED:
                            where.append(" AND sta3c10 = ").append(LISEnum.ResultTestState.DELIVERED.getValue());

                            break;
                        case StatisticFilter.FILTER_TEST_NOT_VALID:
                            where.append(" AND sta3c10 < ").append(LISEnum.ResultTestState.VALIDATED.getValue());

                            break;
                        case StatisticFilter.FILTER_TEST_RESULT:
                            where.append(" AND sta3c10 = ").append(LISEnum.ResultTestState.REPORTED.getValue());

                            break;
                        case StatisticFilter.FILTER_TEST_VALID:
                            where.append(" AND sta3c10 = ").append(LISEnum.ResultTestState.VALIDATED.getValue());

                            break;
                        default:
                            break;
                    }

                    where.append(" AND sta2c25 = 1 ");

                    SQLTools.buildSQLDemographicFilterStatistics(filterDemographics, params, query, where);

                    RowMapper mapper = (RowMapper<StatisticOrder>) (ResultSet rs, int i) ->
                    {

                        StatisticOrder order = new StatisticOrder();
                        StatisticResult test = null;
                        Date currentDate = new Date();
                        boolean chasBoxExist = false;
                        order.setOrderNumber(rs.getLong("sta2c1"));
                        try
                        {
                            chasBoxExist = getcashBoxService().cashBoxExists(order.getOrderNumber());
                        } catch (Exception ex)
                        {
                            Logger.getLogger(StatisticDao.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
                            test.setRepeats(rs.getInt("sta3c13"));
                            test.setModifications(rs.getInt("sta3c14"));
                            test.setValidationDate(rs.getInt("sta3c15"));
                            test.setLevelComplex(rs.getInt("sta3c16"));
                            test.setMultiplyBy(rs.getInt("sta3c24"));
                            //Precios
                            test.setRate(rs.getInt("sta3c17"));
                            test.setRateCode(rs.getString("sta3c18"));
                            test.setRateName(rs.getString("sta3c19"));
                            test.setPriceService(rs.getBigDecimal("sta3c20"));
                            test.setPricePatient(rs.getBigDecimal("sta3c21"));
                            test.setPriceAccount(rs.getBigDecimal("sta3c22"));
                            test.setBasic(rs.getInt("sta3c25"));
                            try
                            {
                                test.setCashbox(getcashBoxService().getCashBoxFound(order.getOrderNumber(), test.getId()));
                            } catch (Exception ex)
                            {
                                Logger.getLogger(StatisticDao.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            //Tiempos
                            if (opportunity != null)
                            {
                                test.setOpportunityTimes(new StatisticOpportunity(rs.getInt("sta3c1"), rs.getLong("sta2c1")));
                                test.getOpportunityTimes().setEntryDate(rs.getTimestamp("sta4c4"));
                                test.getOpportunityTimes().setEntryElapsedTime(rs.getLong("sta4c5"));
                                test.getOpportunityTimes().setEntryUser(rs.getInt("sta4c6"));

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
                            }

                        }
                        //ORDEN

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
                            order.setDate(rs.getInt("sta2c20"));
                            order.setDateTime(rs.getTimestamp("sta2c21"));
                            order.setAge(rs.getInt("sta2c22"));
                            order.setUnit(rs.getInt("sta2c23"));
                            order.setWeight(rs.getBigDecimal("sta2c24"));
                            order.setAgeGroup(rs.getString("sta2c26") == null ? null : rs.getInt("sta2c26"));
                            order.setAgeGroupCode(rs.getString("sta2c27"));
                            order.setAgeGroupName(rs.getString("sta2c28"));
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
                            //caja
                            if (chasBoxExist)
                            {
                                try
                                {
                                    order.setCashBox(getcashBoxService().getCashBoxHeader(order.getOrderNumber()));
                                } catch (Exception ex)
                                {
                                    Logger.getLogger(StatisticDao.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            StatisticDemographic demoValue = null;
                            for (FilterDemographic demographic : filterDemographics)
                            {
                                if (demographic.getDemographic() != null)
                                {
                                    if (demographic.getDemographic() > 0)
                                    {
                                        if (demographic.getOrigin() != null)
                                        {
                                            demoValue = new StatisticDemographic();
                                            demoValue.setIdDemographic(demographic.getDemographic());

                                            if (rs.getString("sta_demo_" + demographic.getDemographic()) != null)
                                            {
                                                demoValue.setIdOrder(rs.getLong("sta2c1"));
                                                demoValue.setCodifiedId(rs.getInt("sta_demo_" + demographic.getDemographic()));
                                                demoValue.setCodifiedCode(rs.getString("sta_demo_" + demographic.getDemographic() + "_code"));
                                                demoValue.setCodifiedName(rs.getString("sta_demo_" + demographic.getDemographic() + "_name"));
                                            }

                                            if (demographic.getOrigin().equals("O"))
                                            {
                                                order.getDemographics().add(demoValue);
                                            } else
                                            {
                                                order.getPatient().getDemographics().add(demoValue);
                                            }
                                        }
                                    }
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
                    //listOrders.addAll(orders);

                    getConnectionStat().query(query + from + where, mapper, params.toArray());
                }
            }
            return orders;
        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista pacientes por rango de numero de orden desde base de datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<StatisticPatient> listPatient(Long vInitial, Long vFinal) throws Exception
    {
        try
        {
            List<StatisticPatient> listOrders = new LinkedList<>();
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;

            int currentYear = DateTools.dateToNumberYear(new Date());

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

                List<Object> params = new ArrayList<>();
                String query = ISOLATION_READ_UNCOMMITTED + "SELECT DISTINCT lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6 "
                        + "FROM lab21 "
                        + "INNER JOIN " + lab22 + " as lab22  ON lab21.lab21c1 = lab22.lab21c1 AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) "
                        + "WHERE lab22c1 BETWEEN ? AND ?";
                params.add(vInitial);
                params.add(vFinal);

                RowMapper mapper = (RowMapper<StatisticPatient>) (ResultSet rs, int i) ->
                {
                    StatisticPatient patient = new StatisticPatient();
                    //PACIENTE
                    patient.setId(rs.getInt("lab21c1"));
                    patient.setPatientId(rs.getString("lab21c2"));
                    patient.setName1(rs.getString("lab21c3"));
                    patient.setName2(rs.getString("lab21c4"));
                    patient.setLastName(rs.getString("lab21c5"));
                    patient.setSurName(rs.getString("lab21c6"));
                    listOrders.add(patient);
                    return patient;
                };
                getConnection().query(query, mapper, params.toArray());
            }
            return listOrders;

        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la informacion de una orden para alimentar las tablas
     * estadisticas
     *
     * @param orderNumber numero de orden
     * @param demographics lista de demograficos
     *
     * @return Orden.
     * @throws Exception Error en la base de datos.
     */
    default StatisticOrder getOrder(Long orderNumber, final List<Demographic> demographics) throws Exception
    {
        try
        {

            Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22.lab22c1, lab22c2, lab22c3,  lab22.lab07c1, lab22c7, lab22c9, "
                    + "lab21c10, lab21c7, lab21.lab21c1, "
                    + "lab103.lab103c1, lab103c2, lab103c3, lab103c4,  "
                    + "lab05.lab05c1, lab05c10, lab05c4, "
                    + "lab10.lab10c1, lab10c2, lab10c7,  "
                    + "lab14.lab14c1, lab14c2, lab14c3,  "
                    + "lab19.lab19c1, lab19c2, lab19c22, "
                    + "lab904.lab904c1, lab904c2, lab904c3, "
                    + "lab21.lab80c1 ";
            String from = " "
                    + "FROM  " + lab22 + " as lab22 "
                    + "LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  "
                    + "LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  "
                    + "LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  "
                    + "LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  "
                    + "LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  "
                    + "LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ";
            String where = " "
                    + "WHERE lab22.lab22c1 = ?  ";

            for (Demographic demographic : demographics)
            {
                if (demographic.getOrigin().equals("O"))
                {
                    if (demographic.isEncoded())
                    {
                        query = query + ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                        query = query + ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                        query = query + ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                        from = from + " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab22.lab_demo_" + demographic.getId() + " = demo" + demographic.getId() + ".lab63c1";
                    } else
                    {
                        query = query + ", Lab22.lab_demo_" + demographic.getId();
                    }
                }
            }
            return getConnection().queryForObject(query + from + where, (ResultSet rs, int i) ->
            {
                StatisticOrder order = new StatisticOrder();
                order.setOrderNumber(rs.getLong("lab22c1"));

                order.setDate(rs.getInt("lab22c2"));
                order.setDateTime(rs.getTimestamp("lab22c3"));
                order.setWeight(rs.getBigDecimal("lab21c10"));
                order.setState(rs.getInt("lab07c1"));
                order.setPatient(new StatisticPatient(rs.getInt("lab21c1")));
                order.getPatient().setDob(rs.getTimestamp("lab21c7"));
                order.getPatient().setGender(rs.getInt("lab80c1"));
                order.setExternalId(rs.getString("lab22c7"));

                //TIPO DE ORDEN
                order.setOrderType(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                order.setOrderTypeCode(rs.getString("lab103c2"));
                order.setOrderTypeName(rs.getString("lab103c3"));
                //PACIENTE
                order.setId(rs.getInt("lab21c1"));
                //SEDE
                order.setBranch(rs.getInt("lab05c1"));
                order.setBranchCode(rs.getString("lab05c10") == null ? "" : rs.getString("lab05c10"));
                order.setBranchName(rs.getString("lab05c4") == null ? "" : rs.getString("lab05c4"));
                //SERVICIO
                order.setService(rs.getInt("lab10c1"));
                order.setServiceCode(rs.getString("lab10c7"));
                order.setServiceName(rs.getString("lab10c2"));
                //EMPRESA
                order.setAccount(rs.getInt("lab14c1"));
                order.setAccountCode(rs.getString("lab14c2"));
                order.setAccountName(rs.getString("lab14c3"));
                //MEDICO
                order.setPhysician(rs.getInt("lab19c1"));
                order.setPhysicianCode(rs.getString("lab19c22"));
                order.setPhysicianName(rs.getString("lab19c2"));
                //TARIFA
                order.setRate(rs.getInt("lab904c1"));
                order.setRateCode(rs.getString("lab904c2"));
                order.setRateName(rs.getString("lab904c3"));
                order.setDemographics(new ArrayList<>());
                for (Demographic demographic : demographics)
                {
                    StatisticDemographic demoValue = new StatisticDemographic();
                    demoValue.setIdDemographic(demographic.getId());
                    demoValue.setDemographic(demographic.getName());
                    demoValue.setEncoded(demographic.isEncoded());
                    if (demographic.isEncoded())
                    {
                        if (rs.getString("demo" + demographic.getId() + "_id") != null)
                        {
                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                        }
                    } else
                    {
                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                    }
                    order.getDemographics().add(demoValue);
                }
                order.setResults(listOrderTest(order.getOrderNumber(), null));

                return order;
            }, orderNumber);

        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return null;
        }
    }

    /**
     * Lista de la información de las ordenes enviadas
     *
     * @param orderNumbers lista de ordenes enviadas
     * @param demographics lista de demograficos
     *
     * @return Lista de Ordenes
     * @throws Exception Error en la base de datos.
     */
    default List<StatisticOrder> listOrders(Long orderNumbers, final List<Demographic> demographics) throws Exception
    {
        try
        {

            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(orderNumbers), String.valueOf(orderNumbers));
            String lab22;

            int currentYear = DateTools.dateToNumberYear(new Date());
            lab22 = years.get(0).equals(currentYear) ? "lab22" : "lab22_" + years.get(0);

            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22.lab22c1, lab22c2, lab22c3,  lab22.lab07c1 ";

            String from = " "
                    + "FROM " + lab22 + " as lab22 ";

            String where = " "
                    + "WHERE lab22.lab07c1 = 1 AND (lab22c19 = 0 or lab22c19 is null) AND lab22.lab22c1 in (" + orderNumbers + ")";

            return getConnection().query(query + from + where, (ResultSet rs, int i) ->
            {
                StatisticOrder order = new StatisticOrder();
                order.setOrderNumber(rs.getLong("lab22c1"));

                order.setDate(rs.getInt("lab22c2"));
                order.setDateTime(rs.getTimestamp("lab22c3"));
                order.setState(rs.getInt("lab07c1"));

                return order;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return new ArrayList<>();
        }
    }

    /**
     * Lista las pruebas de una orden para estadisticas.
     *
     * @param orderNumber Numero de la orden
     * @param test Id del examen
     *
     * @return Lista de examenes
     */
    default List<StatisticResult> listOrderTest(long orderNumber, Integer test)
    {
        List<Object> params = new ArrayList<>();

        Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
        String lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;
        String lab144 = year.equals(currentYear) ? "lab144" : "lab144_" + year;

        String query = ISOLATION_READ_UNCOMMITTED + "SELECT "
                + "lab57.lab22c1, lab57c9,  lab57.lab57c14, lab57.lab57c15, lab57c8, lab57c16, lab57c18, lab57c19, lab57c22, lab57c23, lab57c24, lab57c33, "
                + "lab57c4, lab57c5,  "
                + "lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c5, lab39.lab39c19, lab39.lab39c20, lab39.lab39c21, lab39.lab39c22,lab39.lab39c47, lab39.lab39c37,  "
                + "p.lab39c19 as stadicticprofil,  p.lab39c20 as billingprofil, "
                + "package.lab39c19 as stadicticpackage,  package.lab39c20 as billingpackage, "
                + "lab43.lab43c1, lab43c3, lab43.lab43c4, "
                + "lab57.lab40c1, lab40.lab40c2, lab40.lab40c3, "
                + "lab900.lab900c2, lab900.lab900c3, lab900.lab900c4, "
                + "lab904.lab904c1, lab904.lab904c2, lab904.lab904c3, "
                + "lab144c2,lab144.lab04c1 as verifyUser "
                + "FROM  " + lab57 + " as lab57 "
                + "INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                + "LEFT JOIN lab39 p ON p.lab39c1 = lab57.lab57c14 "
                + "LEFT JOIN lab39 package ON package.lab39c1 = lab57.lab57c15 "
                + "LEFT JOIN " + lab900 + " as lab900 ON lab900.lab22c1 = lab57.lab22c1 AND lab900.lab39c1 = lab57.lab39c1 "
                + "LEFT JOIN lab904 ON lab904.lab904c1 = lab900.lab904c1 "
                + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                + "LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 "
                + "LEFT JOIN " + lab144 + " as lab144 ON lab144.lab22c1 = lab57.lab22c1 AND lab144.lab24c1 = lab39.lab24c1 AND lab144c3 = ? "
                + "WHERE lab57.lab22c1 = ? ";
        params.add(LISEnum.ResultSampleState.CHECKED.getValue());
        params.add(orderNumber);
        if (test != null)
        {
            query += " AND lab57.lab39c1 = ?";
            params.add(test);
        }
        try
        {
            return getConnection().query(query, params.toArray(), (ResultSet rs, int i) ->
            {
                StatisticResult test1 = new StatisticResult();
                test1.setOrderNumber(orderNumber);
                test1.setId(rs.getInt("lab39c1"));
                test1.setCode(rs.getString("lab39c2"));
                test1.setName(rs.getString("lab39c21") == null || rs.getString("lab39c21").isEmpty() ? rs.getString("lab39c4") : rs.getString("lab39c21"));
                test1.setLaboratoryId(rs.getInt("lab40c1"));
                test1.setLaboratoryCode(rs.getString("lab40c2"));
                test1.setLaboratoryName(rs.getString("lab40c3"));
                test1.setSectionId(rs.getString("lab43c1") == null ? null : rs.getInt("lab43c1"));
                test1.setSectionCode(rs.getString("lab43c3"));
                test1.setSectionName(rs.getString("lab43c4"));
                test1.setSampleState(rs.getInt("lab57c16"));
                test1.setTestState(rs.getInt("lab57c8"));
                test1.setPathology(rs.getInt("lab57c9"));
                test1.setRepeats(rs.getInt("lab57c33"));
                test1.setModifications(rs.getInt("lab57c24"));
                test1.setLevelComplex(rs.getInt("lab39c5"));
                //test1.setTypeStatistic(rs.getInt("lab39c19") == 1 && rs.getInt("lab39c20") == 1 ? (short) 3 : rs.getInt("lab39c19") == 1 ? (short) 1 : rs.getInt("lab39c20") == 1 ? (short) 2 : 0);
                test1.setProfile(rs.getInt("lab57c14"));
                test1.setIdpackage(rs.getInt("lab57c15"));
                test1.setTypeTest(rs.getInt("lab39c37"));

                test1.setTypeStatistic(rs.getShort("lab39c19"));
                test1.setTypeStatisticBilling(rs.getShort("lab39c20"));

                test1.setProfileStaditics(rs.getShort("stadicticprofil"));
                test1.setProfileBilling(rs.getShort("billingprofil"));

                test1.setPackageStaditics(rs.getShort("stadicticpackage"));
                test1.setPackageBilling(rs.getShort("billingpackage"));

                //Condicional estadistica para examen
                if (test1.getTypeTest() == 0)
                {
                    test1.setTypeStatistic(test1.getPackageStaditics() == 1 || test1.getProfileStaditics() == 1 ? 0 : test1.getTypeStatistic());
                    test1.setTypeStatisticBilling(test1.getPackageBilling() == 1 || test1.getProfileBilling() == 1 ? 0 : test1.getTypeStatisticBilling());
                } // Condicional estadistica para perfil
                else if (test1.getTypeTest() == 1)
                {
                    test1.setTypeStatistic(test1.getPackageStaditics() == 1 ? 0 : test1.getTypeStatistic());
                    test1.setTypeStatisticBilling(test1.getPackageBilling() == 1 ? 0 : test1.getTypeStatisticBilling());
                }

                test1.setMultiplyBy(rs.getInt("lab39c22"));
                /*PRECIOS*/
                test1.setPriceService(rs.getBigDecimal("lab900c2"));
                test1.setPricePatient(rs.getBigDecimal("lab900c3"));
                test1.setPriceAccount(rs.getBigDecimal("lab900c4"));
                test1.setRate(rs.getInt("lab904c1"));
                test1.setRateCode(rs.getString("lab904c2"));
                test1.setRateName(rs.getString("lab904c3"));
                Date date = rs.getTimestamp("lab57c18");
                if (date != null)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    test1.setValidationDate(Integer.parseInt(sdf.format(date)));
                }
                /*ESTADOS*/
                test1.setOpportunityTimes(new StatisticOpportunity());
                test1.getOpportunityTimes().setEntryDate(rs.getTimestamp("lab57c4"));
                test1.getOpportunityTimes().setEntryUser(rs.getInt("lab57c5"));
                test1.getOpportunityTimes().setVerifyDate(rs.getTimestamp("lab144c2"));
                test1.getOpportunityTimes().setVerifyUser(rs.getInt("verifyUser"));
                test1.getOpportunityTimes().setValidDate(rs.getTimestamp("lab57c18"));
                test1.getOpportunityTimes().setValidUser(rs.getInt("lab57c19"));
                test1.getOpportunityTimes().setPrintDate(rs.getTimestamp("lab57c22"));
                test1.getOpportunityTimes().setPrintUser(rs.getInt("lab57c23"));
                test1.getOpportunityTimes().setId(test1.getId());
                test1.getOpportunityTimes().setOrderNumber(test1.getOrderNumber());
                test1.setBasic(rs.getInt("lab39c47"));

                return test1;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return new ArrayList<>();
        }
    }

    /**
     * Elimina examenes de una orden
     *
     * @param order numero de la orden
     *
     * @return Registros afectados
     * @throws Exception Error
     */
    default int deleteTests(Long order) throws Exception
    {

        List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(order), String.valueOf(order));
        String sta3;

        int currentYear = DateTools.dateToNumberYear(new Date());
        sta3 = years.get(0).equals(currentYear) ? "sta3" : "sta3_" + years.get(0);

        return getConnectionStat().update("DELETE FROM " + sta3 + " WHERE sta2c1 = ? ",
                order);
    }

    /**
     * Elimina examenes de una orden
     *
     * @param order Numero de la orden
     * @param test Id examen
     *
     * @return Registros afectados
     * @throws Exception Error
     */
    default int deleteTests(Long order, Integer test) throws Exception
    {
        List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(order), String.valueOf(order));
        String sta3;

        int currentYear = DateTools.dateToNumberYear(new Date());
        sta3 = years.get(0).equals(currentYear) ? "sta3" : "sta3_" + years.get(0);

        return getConnectionStat().update("DELETE FROM " + sta3 + " WHERE sta2c1 = ? AND sta3c1 = ? ",
                order, test);
    }

    /**
     * Adiciona demografico nuevo a la tabla de estadisticas del paciente
     *
     * @param demographic Demografico.
     *
     * @throws Exception Error en la base de datos.
     */
    public void addDemographicToPatient(Demographic demographic) throws Exception;

    /**
     * Adiciona demografico nuevo a la tabla de estadisticas de la orden
     *
     * @param demographic Demografico.
     *
     * @throws Exception Error en la base de datos.
     */
    public void addDemographicToOrder(Demographic demographic, int yearsQuery) throws Exception;

    /**
     * Obtiene paciente para estadisticas
     *
     * @param patientId Id del paciente.
     * @param demographics Lista de demograficos.
     *
     * @return Paciente
     * @throws Exception Error en la base de datos.
     */
    default StatisticPatient getPatient(Integer patientId, final List<Demographic> demographics) throws Exception
    {
        try
        {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT  "
                    + "lab21.lab21c1, lab21c2, lab21c7, lab21c10, "
                    + "lab80.lab80c1 , lab80c4 , lab80c5,"
                    + "lab08.lab08c1 , lab08c2, lab08c5,"
                    + "lab54.lab54c1 , lab54c2, lab54c3 ";
            String from = " "
                    + "FROM lab21 "
                    + "LEFT JOIN lab80  ON lab80.lab80c1 = lab21.lab80c1  "
                    + "LEFT JOIN lab08  ON lab08.lab08c1 = lab21.lab08c1  "
                    + "LEFT JOIN lab54  ON lab54.lab54c1 = lab21.lab54c1  ";
            String where = " "
                    + "WHERE lab21.lab21c1 = ? ";

            for (Demographic demographic : demographics)
            {
                if (demographic.getOrigin().equals("O"))
                {
                    if (demographic.isEncoded())
                    {
                        query = query + ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                        query = query + ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                        query = query + ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                        from = from + " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab22.lab_demo_" + demographic.getId() + " = demo" + demographic.getId() + ".lab63c1";
                    } else
                    {
                        query = query + ", Lab22.lab_demo_" + demographic.getId();
                    }
                } else
                {
                    if (demographic.getOrigin().equals("H"))
                    {
                        if (demographic.isEncoded())
                        {
                            query = query + ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                            query = query + ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                            query = query + ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                            from = from + " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab21.lab_demo_" + demographic.getId() + " = demo" + demographic.getId() + ".lab63c1";
                        } else
                        {
                            query = query + ", Lab21.lab_demo_" + demographic.getId();
                        }
                    }
                }
            }
            return getConnection().queryForObject(query + from + where, (ResultSet rs, int i) ->
            {

                StatisticPatient patient = new StatisticPatient();
                //PACIENTE
                patient.setId(rs.getInt("lab21c1"));
                patient.setDob(rs.getTimestamp("lab21c7"));
                patient.setPatientId(rs.getString("lab21c2"));
                //PACIENTE - SEXO
                patient.setGender(rs.getInt("lab80c1"));
                patient.setGenderSpanish(rs.getString("lab80c4"));
                patient.setGenderEnglish(rs.getString("lab80c5"));
                //PACIENTE - RAZA
                patient.setRace(rs.getInt("lab08c1"));
                patient.setRaceCode(rs.getString("lab08c5"));
                patient.setRaceName(rs.getString("lab08c2"));
                //PACIENTE - TIPO DE DOCUMENTO
                patient.setDocumentType(rs.getInt("lab54c1"));
                patient.setDocumentTypeCode(rs.getString("lab54c2"));
                patient.setDocumentTypeName(rs.getString("lab54c3"));

                patient.setDemographics(new ArrayList<>());
                for (Demographic demographic : demographics)
                {
                    StatisticDemographic demoValue = new StatisticDemographic();
                    demoValue.setIdDemographic(demographic.getId());
                    demoValue.setDemographic(demographic.getName());
                    demoValue.setEncoded(demographic.isEncoded());
                    if (demographic.isEncoded())
                    {
                        if (rs.getString("demo" + demographic.getId() + "_id") != null)
                        {
                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                        }
                    } else
                    {
                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                    }
                    patient.getDemographics().add(demoValue);

                }

                return patient;

            }, patientId);

        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return null;
        }
    }

    /**
     * Actualiza el estado de las ordenes enviadas
     *
     * @param orders lista de ordenes
     * @param state estado a actualizar
     *
     * @return Registros afectados
     * @throws Exception Error en la base de datos.
     */
    default int disableOrders(List<Long> orders, LISEnum.ResultOrderState state) throws Exception
    {
        List<Object[]> parameters = new ArrayList<>();

        String query = "UPDATE sta2 SET sta2c25 = ?  WHERE sta2c1 = ?";

        for (Long order : orders)
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String sta2 = year.equals(currentYear) ? "sta2" : "sta2_" + year;
            parameters.add(new Object[]
            {
                sta2,
                state.getValue(),
                order
            });

        }
        return getConnectionStat().batchUpdate(query, parameters).length;

    }

    /**
     * Actualiza el estado de las ordenes activadas para estadisticas
     *
     * @param orders lista de ordenes.
     *
     * @return Registros afectados.
     * @throws Exception Error en la base de datos.
     */
    default int enableOrders(List<StatisticOrder> orders) throws Exception
    {
        List<Object[]> parameters = new ArrayList<>();

        String query = "UPDATE sta2 SET sta2c25 = ?  WHERE sta2c1 = ?";

        orders.forEach((order) ->
        {
            parameters.add(new Object[]
            {
                order.getState(),
                order.getOrderNumber()
            });
        });
        return getConnectionStat().batchUpdate(query, parameters).length;

    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param demographics Lista de demograficos.
     * @param samples Lista de Muestras.
     * @param filterDemographics
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<StatisticOrder> listOrderSampleRejected(Long vInitial, Long vFinal, final List<Demographic> demographics, List<Integer> samples, List<FilterDemographic> filterDemographics) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            List<StatisticOrder> orders = new ArrayList<>();
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;
            String lab144;
            String lab60;
            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab144 = year.equals(currentYear) ? "lab144" : "lab144_" + year;
                lab60 = year.equals(currentYear) ? "lab60" : "lab60_" + year;
                boolean tableExists = getToolsDao().tableExists(getConnection(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getConnection(), lab144) : tableExists;
                tableExists = tableExists ? getToolsDao().tableExists(getConnection(), lab60) : tableExists;
                if (tableExists)
                {

                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append(
                            "SELECT lab144.lab144c4, lab22.lab22c1, lab22c2, lab22c3, lab22c4, lab22c5, lab22.lab07c1, lab22c7, lab22c9, "
                            + "lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103.lab07c1 AS lab103lab07c1,  "
                            + "lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c5 AS lab21lab08lab08c5, lab21lab54.lab54c1 AS lab21lab54lab54c1, lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3, "
                            + "lab24.lab24c1, lab24c2, lab24c4, lab24c9, lab24.lab24c10, "
                            + "lab30.lab30c1, lab30c2, "
                            + "lab05.lab05c1, lab05c10, lab05c4, "
                            + "lab10.lab10c1, lab10c2, lab10c7,  "
                            + "lab14.lab14c1, lab14c2, lab14c3,  "
                            + "lab19.lab19c1, lab19c2, lab19c3, lab19c22, "
                            + "lab22.lab04c1, lab04c2, lab04c3, lab04c4, "
                            + "lab904.lab904c1, lab904c2, lab904c3, "
                            + "lab60c3 "
                    );

                    StringBuilder from = new StringBuilder();
                    from.append(
                            " FROM " + lab144 + " AS lab144 "
                            + "INNER JOIN " + lab22 + " AS lab22 ON lab22.lab22c1 = lab144.lab22c1 "
                            + "INNER JOIN lab24 ON lab24.lab24c1 = lab144.lab24c1 "
                            + "INNER JOIN lab30 ON lab30.lab30c1 = lab144.lab30c1 "
                            + "LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 "
                            + "LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 "
                            + "LEFT JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1 "
                            + "LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1 "
                            + "LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1 "
                            + "LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 "
                            + "LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1 "
                            + "LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1 "
                            + "LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1 "
                            + "LEFT JOIN lab04 ON lab04.lab04c1 = lab22.lab04c1 "
                            + "LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1 "
                            + "LEFT JOIN " + lab60 + " AS lab60 ON lab60.lab60c2 = lab22.lab22c1 "
                    );

                    StringBuilder where = new StringBuilder();

                    List<Object> params = new ArrayList<>();
                    if (samples == null || samples.isEmpty())
                    {
                        where.append(" WHERE lab144.lab144c3 = 0 ")
                                .append(" AND lab22c2 BETWEEN ? AND ?");
                    } else
                    {
                        where.append(" WHERE lab144.lab144c3 = 0 ")
                                .append(" AND lab144.lab24c1 IN(").append(samples.stream().map(sample -> sample.toString()).collect(Collectors.joining(","))).append(") ")
                                .append(" AND lab22c2 BETWEEN ? AND ?");
                    }

                    where.append(" AND (lab22c19 = 0 or lab22c19 is null) ");
                    params.add(vInitial);
                    params.add(vFinal);

                    SQLTools.buildSQLDemographicFilterStatisticsLab(filterDemographics, params, query, from, where);

                    RowMapper mapper = (RowMapper<StatisticOrder>) (ResultSet rs, int i) ->
                    {
                        StatisticOrder order = new StatisticOrder();
                        order.setOrderNumber(rs.getLong("lab22c1"));
                        if (orders.contains(order))
                        {
                            //MUESTRA
                            StatisticResult sample = new StatisticResult();
                            sample.setSample(rs.getString("lab24c1") == null ? null : rs.getInt("lab24c1"));
                            sample.setSampleName(rs.getString("lab24c2"));
                            sample.setSampleCode(rs.getString("lab24c9"));
                            sample.setIdMotive(rs.getInt("lab30c1"));
                            sample.setNameMotive(rs.getString("lab30c2"));
                            sample.setCommentMotive(rs.getString("lab144c4"));

                            orders.get(orders.indexOf(order)).getResults().add(sample);
                        } else
                        {
                            order.setDate(rs.getInt("lab22c2"));
                            order.setDateTime(rs.getTimestamp("lab22c3"));
                            order.setState(rs.getInt("lab07c1"));
                            //TIPO DE ORDEN
                            order.setOrderType(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                            order.setOrderTypeCode(rs.getString("lab103c2"));
                            order.setOrderTypeName(rs.getString("lab103c3"));
                            //PACIENTE
                            order.setId(rs.getInt("lab21c1"));
                            order.getPatient().setId(rs.getInt("lab21c1"));
                            order.getPatient().setOrderNumber(rs.getLong("lab22c1"));
                            order.getPatient().setDob(rs.getTimestamp("lab21c7"));
                            order.setAge(rs.getTimestamp("lab21c7") == null ? null : (int) DateTools.getAgeInYears(rs.getTimestamp("lab21c7").toLocalDateTime().toLocalDate(), LocalDate.now()));
                            order.setWeight(rs.getBigDecimal("lab21c10"));
                            order.getPatient().setOrderNumber(order.getOrderNumber());
                            //PACIENTE - SEXO
                            order.getPatient().setGender(rs.getInt("lab21lab80lab80c1"));
                            order.getPatient().setGenderSpanish(rs.getString("lab21lab80lab80c4"));
                            order.getPatient().setGenderEnglish(rs.getString("lab21lab80lab80c5"));
                            //PACIENTE - RAZA
                            order.getPatient().setRace(rs.getInt("lab21lab08lab08c1"));
                            order.getPatient().setRaceName(rs.getString("lab21lab08lab08c2"));
                            order.getPatient().setRaceCode(rs.getString("lab21lab08lab08c5"));
                            //PACIENTE - TIPO DE DOCUMENTO
                            order.getPatient().setDocumentType(rs.getInt("lab21lab54lab54c1"));
                            order.getPatient().setDocumentTypeCode(rs.getString("lab21lab54lab54c2"));
                            order.getPatient().setDocumentTypeName(rs.getString("lab21lab54lab54c3"));
                            //SEDE
                            order.setBranch(rs.getInt("lab05c1"));
                            order.setBranchCode(rs.getString("lab05c10"));
                            order.setBranchName(rs.getString("lab05c4"));
                            //SERVICIO
                            order.setService(rs.getInt("lab10c1"));
                            order.setServiceCode(rs.getString("lab10c7"));
                            order.setServiceName(rs.getString("lab10c2"));
                            //EMPRESA
                            order.setAccount(rs.getInt("lab14c1"));
                            order.setAccountCode(rs.getString("lab14c2"));
                            order.setAccountName(rs.getString("lab14c3"));
                            //MEDICO
                            order.setPhysician(rs.getInt("lab19c1"));
                            order.setPhysicianName(rs.getString("lab19c3") + " " + rs.getString("lab19c2"));
                            order.setPhysicianCode(rs.getString("lab19c22"));
                            //TARIFA
                            order.setRate(rs.getInt("lab904c1"));
                            order.setRateCode(rs.getString("lab904c2"));
                            order.setRateName(rs.getString("lab904c3"));
                            //MUESTRA
                            List<StatisticResult> samples1 = new ArrayList<>();
                            StatisticResult sample = new StatisticResult();
                            sample.setSample(rs.getString("lab24c1") == null ? null : rs.getInt("lab24c1"));
                            sample.setSampleName(rs.getString("lab24c2"));
                            sample.setSampleCode(rs.getString("lab24c9"));
                            sample.setIdMotive(rs.getInt("lab30c1"));
                            sample.setNameMotive(rs.getString("lab30c2"));
                            sample.setCommentMotive(rs.getString("lab144c4"));
                            samples1.add(sample);
                            order.setResults(samples1);
                            StatisticDemographic demoValue = null;

                            for (FilterDemographic demographic : filterDemographics)
                            {
                                if (demographic.getDemographic() != null)
                                {
                                    if (demographic.getDemographic() > 0)
                                    {
                                        if (demographic.getOrigin() != null)
                                        {
                                            demoValue = new StatisticDemographic();
                                            demoValue.setIdOrder(rs.getLong("lab22c1"));
                                            demoValue.setIdDemographic(demographic.getDemographic());
                                            demoValue.setDemographic(demographic.getName());
                                            demoValue.setEncoded(demographic.isEncoded());
                                            if (demographic.isEncoded())
                                            {
                                                if (rs.getString("demo" + demographic.getDemographic() + "_id") != null)
                                                {
                                                    demoValue.setCodifiedId(rs.getInt("demo" + demographic.getDemographic() + "_id"));
                                                    demoValue.setCodifiedCode(rs.getString("demo" + demographic.getDemographic() + "_code"));
                                                    demoValue.setCodifiedName(rs.getString("demo" + demographic.getDemographic() + "_name"));
                                                }
                                            } else
                                            {
                                                demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getDemographic()));
                                            }
                                            if (demographic.getOrigin().equals("O"))
                                            {
                                                order.getDemographics().add(demoValue);
                                            } else
                                            {
                                                order.getPatient().getDemographics().add(demoValue);
                                            }
                                        }
                                    }
                                }
                            }
                            orders.add(order);
                        }
                        return order;
                    };
                    getConnection().query(query.toString() + from + where, mapper, params.toArray());
                }
            }
            return orders;
        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos con resultados de microbiologia.
     *
     * @param rangeType Tipo de busqueda: 0 -> Fecha Ingreso, 2 -> Numero de
     * Ordenes.
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param demographics Lista de demograficos.
     * @param samples Lista de Muestras.
     * @param tests Lista de Examenes.
     * @param microorganisms Lista de Microorganismos.
     * @param antibiotics Lista de Antibioticos.
     * @param filterDemographics
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<StatisticOrder> listOrderMicrobiology(Integer rangeType, Long vInitial, Long vFinal, final List<Demographic> demographics, List<Integer> samples, List<Integer> tests, List<Integer> microorganisms, List<Integer> antibiotics, List<FilterDemographic> filterDemographics) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();

            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append(
                    "SELECT lab204.lab204c1, lab22.lab22c1, lab22c2, lab22c3, lab22.lab07c1, lab22c7, "
                    + "lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103.lab07c1 AS lab103lab07c1, "
                    + "lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c5 AS lab21lab08lab08c5, lab21lab54.lab54c1 AS lab21lab54lab54c1, lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3, "
                    + "lab24.lab24c1, lab24.lab24c2, lab24.lab24c4, lab24.lab24c9, lab24.lab24c10, "
                    + "lab24_1.lab24c2 AS lab24c2_1, lab24_1.lab24c9 AS lab24c9_1, "
                    + "lab158c3, "
                    + "lab05.lab05c1, lab05c10, lab05c4, "
                    + "lab10.lab10c1, lab10c2, lab10c7,  "
                    + "lab14.lab14c1, lab14c2, lab14c3,  "
                    + "lab19.lab19c1, lab19c2, lab19c3, lab19c22, "
                    + "lab904.lab904c1, lab904c2, lab904c3, "
                    + "lab39.lab39c1, lab39c2, lab39c3, lab39c4, "
                    + "lab76.lab76c1, lab76c2, "
                    + "lab79.lab79c1, lab79.lab79c2, "
                    + "lab205.lab205c1, lab205.lab205c2, lab205.lab205c3, lab205.lab205c4, lab205.lab205c5, lab205.lab205c6, lab205.lab205c7, lab205.lab205c8, lab205.lab205c9, lab205.lab205c10, lab205.lab205c11, "
                    + "lab04_1.lab04c1 AS lab04c1_1, lab04_1.lab04c2 AS lab04c2_1, lab04_1.lab04c3 AS lab04c3_1, lab04_1.lab04c4 AS lab04c4_1, "
                    + "lab04_2.lab04c1 AS lab04c1_2, lab04_2.lab04c2 AS lab04c2_2, lab04_2.lab04c3 AS lab04c3_2, lab04_2.lab04c4 AS lab04c4_2, "
                    + "lab04_3.lab04c1 AS lab04c1_3, lab04_3.lab04c2 AS lab04c2_3, lab04_3.lab04c3 AS lab04c3_3, lab04_3.lab04c4 AS lab04c4_3 "
            );

            StringBuilder from = new StringBuilder();

            from.append(" FROM lab205 "
                    + "INNER JOIN lab204 ON lab204.lab204c1 = lab205.lab204c1 "
                    + "INNER JOIN lab39 ON lab39.lab39c1 = lab204.lab39c1 "
                    + "INNER JOIN lab76 ON lab76.lab76c1 = lab204.lab76c1 "
                    + "LEFT JOIN lab79 ON lab79.lab79c1 = lab205.lab79c1 "
                    + "LEFT JOIN lab04 AS lab04_1 ON lab04_1.lab04c1 = lab205.lab04c1_1 "
                    + "LEFT JOIN lab04 AS lab04_2 ON lab04_2.lab04c1 = lab205.lab04c1_2 "
                    + "LEFT JOIN lab04 AS lab04_3 ON lab04_3.lab04c1 = lab205.lab04c1_3 "
                    + "INNER JOIN lab22 ON lab22.lab22c1 = lab204.lab22c1 AND lab22.lab07C1 = 1 AND (lab22c19 = 0 or lab22c19 is null)"
                    + "INNER JOIN lab57 ON lab57.lab22c1 = lab22.lab22c1 AND lab57.lab39c1 = lab39.lab39c1 "
                    + "LEFT JOIN lab24 AS lab24_1 ON lab24_1.lab24c1 = lab57.lab24c1_1 "
                    + "LEFT JOIN lab158 ON lab158.lab158c1 = lab57.lab158c1 "
                    + "INNER JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + "LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 "
                    + "LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 "
                    + "LEFT JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1 "
                    + "LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1 "
                    + "LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1 "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 "
                    + "LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1 "
                    + "LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1 "
                    + "LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1 "
                    + "LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  "
            );

            List<Object> params = new ArrayList<>();
            StringBuilder where = new StringBuilder();

            if (rangeType == 1)
            {
                where.append(" WHERE lab22.lab22c1 BETWEEN ? AND ? ");
                params.add(vInitial);
                params.add(vFinal);
            } else
            {
                where.append(" WHERE lab22.lab22c2 BETWEEN ? AND ? ");
                params.add(vInitial);
                params.add(vFinal);
            }

            if (samples != null && !samples.isEmpty())
            {
                where.append(" AND lab24.lab24c1 IN(").append(samples.stream().map(sample -> sample.toString()).collect(Collectors.joining(","))).append(") ");
            }

            if (tests != null && !tests.isEmpty())
            {
                where.append(" AND lab39.lab39c1 IN(").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(") ");
            }

            if (microorganisms != null && !microorganisms.isEmpty())
            {
                where.append(" AND lab76.lab76c1 IN(").append(microorganisms.stream().map(microorganism -> microorganism.toString()).collect(Collectors.joining(","))).append(") ");
            }

            if (antibiotics != null && !antibiotics.isEmpty())
            {
                where.append(" AND lab79.lab79c1 IN(").append(antibiotics.stream().map(antibiotic -> antibiotic.toString()).collect(Collectors.joining(","))).append(") ");
            }

            SQLTools.buildSQLDemographicFilterStatisticsLab(filterDemographics, params, query, from, where);

            List<StatisticOrder> orders = new ArrayList<>();
            RowMapper mapper = (RowMapper<StatisticOrder>) (ResultSet rs, int i) ->
            {
                StatisticOrder order = new StatisticOrder();
                order.setOrderNumber(rs.getLong("lab22c1"));
                if (orders.contains(order))
                {
                    getResult(rs, orders.get(orders.indexOf(order)));
                } else
                {
                    order.setDate(rs.getInt("lab22c2"));
                    order.setDateTime(rs.getTimestamp("lab22c3"));
                    order.setState(rs.getInt("lab07c1"));
                    //TIPO DE ORDEN
                    order.setOrderType(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                    order.setOrderTypeCode(rs.getString("lab103c2"));
                    order.setOrderTypeName(rs.getString("lab103c3"));
                    //PACIENTE
                    order.setId(rs.getInt("lab21c1"));
                    order.getPatient().setId(rs.getInt("lab21c1"));
                    order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    order.getPatient().setOrderNumber(rs.getLong("lab22c1"));
                    order.getPatient().setDob(rs.getTimestamp("lab21c7"));
                    order.setWeight(rs.getBigDecimal("lab21c10"));
                    order.getPatient().setOrderNumber(order.getOrderNumber());
                    //PACIENTE - SEXO
                    order.getPatient().setGender(rs.getInt("lab21lab80lab80c1"));
                    order.getPatient().setGenderSpanish(rs.getString("lab21lab80lab80c4"));
                    order.getPatient().setGenderEnglish(rs.getString("lab21lab80lab80c5"));
                    //PACIENTE - RAZA
                    order.getPatient().setRace(rs.getInt("lab21lab08lab08c1"));
                    order.getPatient().setRaceName(rs.getString("lab21lab08lab08c2"));
                    order.getPatient().setRaceCode(rs.getString("lab21lab08lab08c5"));
                    //PACIENTE - TIPO DE DOCUMENTO
                    order.getPatient().setDocumentType(rs.getInt("lab21lab54lab54c1"));
                    order.getPatient().setDocumentTypeCode(rs.getString("lab21lab54lab54c2"));
                    order.getPatient().setDocumentTypeName(rs.getString("lab21lab54lab54c3"));
                    //SEDE
                    order.setBranch(rs.getInt("lab05c1"));
                    order.setBranchCode(rs.getString("lab05c10"));
                    order.setBranchName(rs.getString("lab05c4"));
                    //SERVICIO
                    order.setService(rs.getInt("lab10c1"));
                    order.setServiceCode(rs.getString("lab10c7"));
                    order.setServiceName(rs.getString("lab10c2"));
                    //EMPRESA
                    order.setAccount(rs.getInt("lab14c1"));
                    order.setAccountCode(rs.getString("lab14c2"));
                    order.setAccountName(rs.getString("lab14c3"));
                    //MEDICO
                    order.setPhysician(rs.getInt("lab19c1"));
                    order.setPhysicianName(rs.getString("lab19c3") + " " + rs.getString("lab19c2"));
                    order.setPhysicianCode(rs.getString("lab19c22"));
                    //TARIFA
                    order.setRate(rs.getInt("lab904c1"));
                    order.setRateCode(rs.getString("lab904c2"));
                    order.setRateName(rs.getString("lab904c3"));

                    //RESULTADOS
                    getResult(rs, order);

                    StatisticDemographic demoValue = null;
                    for (FilterDemographic demographic : filterDemographics)
                    {
                        if (demographic.getDemographic() != null)
                        {
                            if (demographic.getDemographic() > 0)
                            {
                                if (demographic.getOrigin() != null)
                                {
                                    demoValue = new StatisticDemographic();
                                    demoValue.setIdOrder(rs.getLong("lab22c1"));
                                    demoValue.setIdDemographic(demographic.getDemographic());
                                    demoValue.setDemographic(demographic.getName());
                                    demoValue.setEncoded(demographic.isEncoded());
                                    if (demographic.isEncoded())
                                    {
                                        if (rs.getString("demo" + demographic.getDemographic() + "_id") != null)
                                        {
                                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getDemographic() + "_id"));
                                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getDemographic() + "_code"));
                                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getDemographic() + "_name"));
                                        }
                                    } else
                                    {
                                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getDemographic()));
                                    }
                                    if (demographic.getOrigin().equals("O"))
                                    {
                                        order.getDemographics().add(demoValue);
                                    } else
                                    {
                                        order.getPatient().getDemographics().add(demoValue);
                                    }
                                }
                            }
                        }
                    }
                    orders.add(order);
                }
                return order;
            };

            getConnection().query(query.toString() + from + where, mapper, params.toArray());
            return orders;
        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    default void getResult(ResultSet rs, StatisticOrder order) throws SQLException
    {
        StatisticResult result = new StatisticResult();
        result.setId(rs.getInt("lab39c1"));

        if (order.getResults().contains(result))
        {
            getMicroorganism(rs, order.getResults().get(order.getResults().indexOf(result)));
        } else
        {
            result.setCode(rs.getString("lab39c2"));
            result.setName(rs.getString("lab39c4"));
            result.setSample(rs.getString("lab24c1") == null ? null : rs.getInt("lab24c1"));
            result.setSampleName(rs.getString("lab24c2"));
            result.setSampleCode(rs.getString("lab24c9"));
            result.setSubSampleCode(rs.getString("lab24c9_1") == null ? null : rs.getString("lab24c9_1"));
            result.setAnatomicalSiteCode(rs.getString("lab158c3") == null ? null : rs.getString("lab158c3"));

            getMicroorganism(rs, result);
            order.getResults().add(result);
        }
    }

    default void getMicroorganism(ResultSet rs, StatisticResult result) throws SQLException
    {
        Microorganism microorganism = new Microorganism();
        microorganism.setIdMicrobialDetection(rs.getInt("lab204c1"));

        if (result.getMicroorganisms().stream().anyMatch(m -> Objects.equals(m.getIdMicrobialDetection(), microorganism.getIdMicrobialDetection())))
        {
            Microorganism micro = result.getMicroorganisms().stream().filter(m -> Objects.equals(m.getIdMicrobialDetection(), microorganism.getIdMicrobialDetection())).findAny().orElse(null);
            micro.getResultsMicrobiology().add(getResultMicrobiology(rs));
        } else
        {
            microorganism.setId(rs.getInt("lab76c1"));
            microorganism.setName(rs.getString("lab76c2"));

            microorganism.getResultsMicrobiology().add(getResultMicrobiology(rs));
            result.getMicroorganisms().add(microorganism);
        }
    }

    default ResultMicrobiology getResultMicrobiology(ResultSet rs) throws SQLException
    {
        ResultMicrobiology resultMicrobiology = new ResultMicrobiology();
        resultMicrobiology.setIdAntibiotic(rs.getInt("lab79c1"));
        resultMicrobiology.setNameAntibiotic(rs.getString("lab79c2"));
        resultMicrobiology.setCmi(rs.getString("lab205c1"));
        resultMicrobiology.setInterpretationCMI(rs.getString("lab205c2"));
        resultMicrobiology.setCmiM(rs.getString("lab205c3"));
        resultMicrobiology.setInterpretationCMIM(rs.getString("lab205c4"));
        resultMicrobiology.setCmiMPrint(rs.getInt("lab205c5") == 1);
        resultMicrobiology.setDisk(rs.getString("lab205c6"));
        resultMicrobiology.setInterpretationDisk(rs.getString("lab205c7"));
        resultMicrobiology.setDiskPrint(rs.getInt("lab205c8") == 1);
        resultMicrobiology.setSelected(rs.getTimestamp("lab205c9") != null);
        /*CMI*/
        resultMicrobiology.setDateCMI(rs.getTimestamp("lab205c9"));
        if (rs.getString("lab04c1_1") != null)
        {
            resultMicrobiology.getUserCMI().setId(rs.getInt("lab04c1_1"));
            resultMicrobiology.getUserCMI().setName(rs.getString("lab04c2_1"));
            resultMicrobiology.getUserCMI().setLastName(rs.getString("lab04c3_1"));
            resultMicrobiology.getUserCMI().setUserName(rs.getString("lab04c4_1"));
        } else
        {
            resultMicrobiology.setUserCMI(null);
        }

        /*CMI Manual*/
        resultMicrobiology.setDateCMIM(rs.getTimestamp("lab205c10"));
        if (rs.getString("lab04c1_2") != null)
        {
            resultMicrobiology.getUserCMIM().setId(rs.getInt("lab04c1_2"));
            resultMicrobiology.getUserCMIM().setName(rs.getString("lab04c2_2"));
            resultMicrobiology.getUserCMIM().setLastName(rs.getString("lab04c3_2"));
            resultMicrobiology.getUserCMIM().setUserName(rs.getString("lab04c4_2"));
        } else
        {
            resultMicrobiology.setUserCMIM(null);
        }

        /*Disco*/
        resultMicrobiology.setDateDisk(rs.getTimestamp("lab205c11"));
        if (rs.getString("lab04c1_3") != null)
        {
            resultMicrobiology.getUserDisk().setId(rs.getInt("lab04c1_3"));
            resultMicrobiology.getUserDisk().setName(rs.getString("lab04c2_3"));
            resultMicrobiology.getUserDisk().setLastName(rs.getString("lab04c3_3"));
            resultMicrobiology.getUserDisk().setUserName(rs.getString("lab04c4_3"));
        } else
        {
            resultMicrobiology.setUserDisk(null);
        }

        return resultMicrobiology;
    }

    /**
     * Consulta tiempos en los cambios de estados del exámen
     *
     * @param orderNumber numero de orden
     * @param test id examne (opcional)
     * @param sample id muestra (opcional)
     * @return Lista de resultados con la informacion de StatisticOpportunity
     * @throws java.lang.Exception
     */
    default List<StatisticResult> listTestTimes(long orderNumber, Integer test, Integer sample) throws Exception
    {
        List<Object> params = new ArrayList<>();

        Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
        String lab144 = year.equals(currentYear) ? "lab144" : "lab144_" + year;


        String query = ISOLATION_READ_UNCOMMITTED + "SELECT "
                + "lab57.lab22c1, lab39.lab39c1,lab57c8, lab57c16, lab57c18, "
                + "lab57c4, lab57c5,lab57c2,lab57c3,lab57c18,lab57c19,lab57c20,lab57c21,lab57c22,lab57c23,lab57c37,lab57c38, lab57c39, lab57c40, lab57c57, lab57c59, lab57c60 "
                + "FROM  " + lab57 + " as lab57 "
                + "INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ";
                
        String where = "WHERE lab57.lab22c1 = ? ";
       
        params.add(orderNumber);
        if (test != null)
        {
            where += " AND lab57.lab39c1 = ?";
            params.add(test);
        }
        if (sample != null)
        {
            where += " AND lab39.lab24c1 = ?";
            params.add(sample);
        }
        try
        {
            return getConnection().query(query + where, params.toArray(), (ResultSet rs, int i) ->
            {
                StatisticResult test1 = new StatisticResult();
                test1.setOrderNumber(orderNumber);
                test1.setId(rs.getInt("lab39c1"));
                /*ESTADOS*/
                test1.setOpportunityTimes(new StatisticOpportunity());
                test1.getOpportunityTimes().setEntryDate(rs.getTimestamp("lab57c4"));
                test1.getOpportunityTimes().setEntryUser(rs.getInt("lab57c5"));
                test1.getOpportunityTimes().setTakeDate(rs.getTimestamp("lab57c39"));
                test1.getOpportunityTimes().setTakeUser(rs.getInt("lab57c40"));
                test1.getOpportunityTimes().setTransportDate(rs.getTimestamp("lab57c57"));
                test1.getOpportunityTimes().setPrintSampleDate(rs.getTimestamp("lab57c59"));
                test1.getOpportunityTimes().setPrintSampleUser(rs.getInt("lab57c60"));
                test1.getOpportunityTimes().setVerifyDate(rs.getTimestamp("lab57c37"));
                test1.getOpportunityTimes().setVerifyUser(rs.getInt("lab57c38"));
                test1.getOpportunityTimes().setResultDate(rs.getTimestamp("lab57c2"));
                test1.getOpportunityTimes().setResultUser(rs.getInt("lab57c3"));
                test1.getOpportunityTimes().setValidDate(rs.getTimestamp("lab57c18"));
                test1.getOpportunityTimes().setValidUser(rs.getInt("lab57c19"));
                test1.getOpportunityTimes().setPrintDate(rs.getTimestamp("lab57c22"));
                test1.getOpportunityTimes().setPrintUser(rs.getInt("lab57c23"));
                test1.getOpportunityTimes().setId(test1.getId());
                test1.getOpportunityTimes().setOrderNumber(test1.getOrderNumber());
                return test1;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return new ArrayList<>();
        }
    }

    /**
     * Actualiza tiempos de oportunidad
     *
     * @param tests Lista de examenes
     * @return Registros afectados
     * @throws java.lang.Exception Error en sql
     */
    default int updateTimes(List<StatisticResult> tests) throws Exception
    {
        tests.forEach((test) ->
        {
            // Año de la orden
            Integer year = Tools.YearOfOrder(String.valueOf(test.getOrderNumber()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String sta4 = year.equals(currentYear) ? "sta4" : "sta4_" + year;
            getConnectionStat().update("UPDATE sta4 SET sta4c7 = ?, sta4c11 = ?, sta4c9 = ?, sta4c27 = ?, sta4c28 = ?, sta4c12 = ? , sta4c13 = ? , sta4c14 = ? , sta4c15 = ? , sta4c16 = ? , sta4c17 = ? , sta4c21 = ?, sta4c22 = ? , sta4c23 = ? , sta4c24 = ? , sta4c25 = ? , sta4c26 = ? WHERE sta2c1 = ? AND sta3c1 = ?  ",
                    test.getOpportunityTimes().getTakeDate(),
                    test.getOpportunityTimes().getTakeUser(),
                    test.getOpportunityTimes().getTransportDate(),
                    test.getOpportunityTimes().getPrintSampleDate(),
                    test.getOpportunityTimes().getPrintSampleUser(),
                    test.getOpportunityTimes().getVerifyDate(),
                    test.getOpportunityTimes().getVerifyElapsedTime(),
                    test.getOpportunityTimes().getVerifyUser(),
                    test.getOpportunityTimes().getResultDate(),
                    test.getOpportunityTimes().getResultElapsedTime(),
                    test.getOpportunityTimes().getResultUser(),
                    test.getOpportunityTimes().getValidDate(),
                    test.getOpportunityTimes().getValidElapsedTime(),
                    test.getOpportunityTimes().getValidUser(),
                    test.getOpportunityTimes().getPrintDate(),
                    test.getOpportunityTimes().getPrintElapsedTime(),
                    test.getOpportunityTimes().getPrintUser(),
                    test.getOrderNumber(),
                    test.getId()
            );
        });
        return 1;
    }

    /**
     * Lista para reporte de muestra por destino
     *
     * @param vInitial fecha inicial
     * @param vFinal fecha final
     * @param samples id´s muestras a filtrar
     * @param sections id´s secciones a filtrar
     * @param demographics lista de demograficos a conusltar
     * @return Lista de ordenes con la informacion de la muestra y destinos
     * @throws Exception Error en base de datos
     * @param filterDemographics
     */
    default List<Order> sampleByDestiny(Long vInitial, Long vFinal, List<Integer> samples, List<Integer> sections, List<Demographic> demographics, List<FilterDemographic> filterDemographics) throws Exception
    {
        try
        {
            List<Order> orders = new ArrayList<>();

            List<Object> params = new ArrayList<>();

            StringBuilder query = new StringBuilder();

            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append(
                    "SELECT  lab22.lab22c1, lab10.lab10c1, lab19c1, lab14c1, lab904c1  "
                    + ",lab21.lab21c1, lab21c2,lab21c3,lab21c4,lab21c5,lab21c6,lab54c1 "
                    + ",lab22.lab103c1, lab103c2, lab103c3, lab103c4 "
                    + ",lab22.lab05c1, lab05c10, lab05c4 "
                    + ",lab159.lab24c1, lab24c2, lab24c9 "
                    + ",lab159c1 "
                    + ",lab42.lab53c1 "
                    + ",lab25.lab42c1 "
                    + ",lab10c2 "
            );

            StringBuilder from = new StringBuilder();
            from.append(
                    " FROM lab159 "
                    + "INNER JOIN lab24 ON lab24.lab24c1 = lab159.lab24c1 "
                    + "INNER JOIN lab22 ON lab22.lab22c1 = lab159.lab22c1 "
                    + "INNER JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 "
                    + "INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 "
                    + "INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                    + "INNER JOIN lab52 ON ((lab159.lab52c1 = lab52.lab52c1) OR ((lab52.lab103c1 = 0 OR lab52.lab103c1 = lab22.lab103c1) AND lab52.lab05c1= lab22.lab05c1 AND lab159.lab24c1 = lab52.lab24c1 AND lab159.lab52c1 IS NULL AND lab52.lab07c1 = 1)) "
                    + "INNER JOIN lab42 ON lab52.lab52c1 = lab42.lab52c1 "
                    + "LEFT JOIN lab25 ON lab22.lab22c1 = lab25.lab22c1 AND lab25.lab42c1 = lab42.lab42c1 "
                    + "LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1 "
            );

            StringBuilder where = new StringBuilder();

            where.append(" WHERE  lab22c2 BETWEEN ? AND ?  AND (lab22c19 = 0 or lab22c19 is null) ");

            if (samples != null && !samples.isEmpty())
            {
                where.append(" AND lab24.lab24c1 IN (").append(samples.stream().map(s -> s.toString()).collect(Collectors.joining(","))).append(")");
            }
            if (sections != null && !sections.isEmpty())
            {
                from.append(" INNER JOIN lab57 ON lab57.lab22c1 = lab159.lab22c1 ")
                        .append(" INNER JOIN lab39 ON lab57.lab39c1 = lab39.lab39c1 ");
                where.append(" AND lab39.lab43c1 IN (").append(sections.stream().map(s -> s.toString()).collect(Collectors.joining(","))).append(")");
            }

            SQLTools.buildSQLDemographicFilterStatisticsLab(filterDemographics, params, query, from, where);

            params.add(vInitial);
            params.add(vFinal);
            RowCallbackHandler rch = (ResultSet rs) ->
            {
                Order order = new Order(rs.getLong("lab22c1"));
                Sample sample = new Sample(rs.getInt("lab24c1"));
                Destination destination = new Destination(rs.getInt("lab53c1"));

                if (!orders.contains(order))
                {
                    order.setType(new OrderType(rs.getInt("lab103c1")));
                    order.getType().setCode(rs.getString("lab103c2"));
                    order.getType().setName(rs.getString("lab103c3"));
                    order.getType().setColor(rs.getString("lab103c4"));

                    Branch branch = new Branch();
                    branch.setId(rs.getInt("lab05c1"));
                    branch.setCode(rs.getString("lab05c10"));
                    branch.setName(rs.getString("lab05c4"));
                    order.setBranch(branch);
                    if (rs.getString("lab10c1") != null)
                    {
                        order.setService(new ServiceLaboratory(rs.getInt("lab10c1")));
                        order.getService().setName(rs.getString("lab10c2"));
                    }
                    if (rs.getString("lab19c1") != null)
                    {
                        order.setPhysician(new Physician(rs.getInt("lab19c1")));
                    }

                    if (rs.getString("lab14c1") != null)
                    {
                        order.setAccount(new Account(rs.getInt("lab14c1")));
                    }

                    if (rs.getString("lab904c1") != null)
                    {
                        order.setRate(new Rate(rs.getInt("lab904c1")));
                    }

                    DemographicValue demoValue = null;
                    order.setDemographics(new ArrayList());

                    order.setPatient(new Patient(rs.getInt("lab21c1")));
                    order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    order.getPatient().getDocumentType().setId(rs.getInt("lab54c1"));
                    order.getPatient().setDemographics(new ArrayList());

                    for (FilterDemographic demographic : filterDemographics)
                    {
                        if (demographic.getDemographic() != null)
                        {
                            if (demographic.getDemographic() > 0)
                            {
                                if (demographic.getOrigin() != null)
                                {
                                    demoValue = new DemographicValue();
                                    demoValue.setIdDemographic(demographic.getDemographic());
                                    demoValue.setDemographic(demographic.getName());
                                    demoValue.setEncoded(demographic.isEncoded());
                                    if (demographic.isEncoded())
                                    {
                                        if (rs.getString("demo" + demographic.getDemographic() + "_id") != null)
                                        {
                                            demoValue.setCodifiedId(rs.getInt("demo" + demographic.getDemographic() + "_id"));
                                            demoValue.setCodifiedCode(rs.getString("demo" + demographic.getDemographic() + "_code"));
                                            demoValue.setCodifiedName(rs.getString("demo" + demographic.getDemographic() + "_name"));
                                        }
                                    } else
                                    {
                                        demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getDemographic()));
                                    }
                                    if (demographic.getOrigin().equals("O"))
                                    {
                                        order.getDemographics().add(demoValue);
                                    } else
                                    {
                                        order.getPatient().getDemographics().add(demoValue);
                                    }
                                }
                            }
                        }
                    }

                    order.setSamples(new ArrayList<>());
                    orders.add(order);
                } else
                {
                    order = orders.get(orders.indexOf(order));
                }

                if (!order.getSamples().contains(sample))
                {
                    Boolean sampleState = rs.getString("lab159c1").equals(String.valueOf(LISEnum.ResultSampleState.CHECKED.getValue()))
                            || rs.getString("lab159c1").equals(String.valueOf(LISEnum.ResultSampleState.REJECTED.getValue()));
                    sample.setCodesample(rs.getString("lab24c9"));
                    sample.setName(rs.getString("lab24c2"));
                    sample.setDestinatios(new ArrayList<>());
                    sample.setCheck(sampleState);
                    order.getSamples().add(sample);
                } else
                {
                    sample = order.getSamples().get(order.getSamples().indexOf(sample));
                }

                if (!sample.getDestinatios().contains(destination))
                {
                    destination.setVerified(rs.getString("lab42c1") != null);
                    sample.getDestinatios().add(destination);
                }
            };
            getConnection().query(query.toString() + from + where, rch, params.toArray());

            return orders;
        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Cantidad de muestras por estado
     *
     * @param state
     * @param dateFirt
     * @param dateLast
     * @return int
     * @throws Exception
     */
    default int countSampleByState(int state, Timestamp dateFirt, Timestamp dateLast) throws Exception
    {
        try
        {
            String sql = ISOLATION_READ_UNCOMMITTED + "SELECT COUNT(lab144.lab144c1) AS cant FROM lab144 WHERE lab144c2 BETWEEN ? AND ? AND lab144c3 = ?";

            List<Object> params = new ArrayList<>();
            params.add(dateFirt);
            params.add(dateLast);
            params.add(state);
            return getConnection().queryForObject(sql, params.toArray(), (ResultSet rs, int i) ->
            {
                return rs.getInt("cant");
            });
        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return 0;
        }
    }

    /**
     * Cantidad de muestras por estado
     *
     * @param states
     * @param dateFirt
     * @param dateLast
     * @param widgetReceived
     * @return int
     * @throws Exception
     */
    default void countSampleByStateAll(List<Integer> states, Timestamp dateFirt, Timestamp dateLast, WidgetEntry widgetReceived) throws Exception
    {
        try
        {
            String sql = ISOLATION_READ_UNCOMMITTED + "SELECT COUNT(lab144.lab144c1) AS cant FROM lab144 WHERE lab144c2 BETWEEN ? AND ? AND lab144c3 = ?";
            StringBuilder cadenaSQL = new StringBuilder();
            StringBuilder sentences = new StringBuilder();
            cadenaSQL.append(ISOLATION_READ_UNCOMMITTED).append("SELECT ");
            states.forEach((state) ->
            {
                sentences.append("sum(case when lab144c3 = ").append(state).append(" then 1 else 0 end) AS estado").append(state).append(", ");
            });
            cadenaSQL.append(sentences.substring(0, sentences.lastIndexOf(",")))
                    .append(" FROM lab144 WHERE lab144c2 BETWEEN ? AND ? AND lab144c3 IN (?,?)");
            List<Object> params = new ArrayList<>();
            params.add(dateFirt);
            params.add(dateLast);
            //params.add(states.toString().replace("[", "(").replace("]", ")"));
            params.add(states.get(0));
            params.add(states.get(1));
            getConnection().query(cadenaSQL.toString(), params.toArray(), (ResultSet rs, int i) ->
            {
                widgetReceived.setSampleEntry(rs.getInt("estado2"));
                widgetReceived.setSampleVerified(rs.getInt("estado4"));
                return rs.getInt("estado2");
            });
        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
        }
    }

    /**
     * Cantidad de muestras por examen
     *
     * @param state
     * @param date
     * @return int
     * @throws Exception
     */
    default int countResultsByState(int state, int date) throws Exception
    {
        try
        {
            String sql = ISOLATION_READ_UNCOMMITTED + "SELECT COUNT(sta3.sta3c1) AS cant "
                    + "FROM sta2 "
                    + "INNER JOIN sta3 ON sta2.sta2c1 = sta3.sta2c1 "
                    + "WHERE sta2c20 = ? AND sta3c10 = ?";

            List<Object> params = new ArrayList<>();
            params.add(date);
            params.add(state);
            return getConnectionStat().queryForObject(sql, params.toArray(), (ResultSet rs, int i) ->
            {
                return rs.getInt("cant");
            });
        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return 0;
        }
    }

    /**
     * Cantidad de muestras por examen
     *
     * @param states
     * @param date
     * @param widgetReceived
     * @throws Exception
     */
    default void countResultsByStateAll(List<Integer> states, int date, WidgetEntry widgetReceived) throws Exception
    {
        try
        {
            String sql = ISOLATION_READ_UNCOMMITTED + "SELECT COUNT(sta3.sta3c1) AS cant "
                    + "FROM sta2 "
                    + "INNER JOIN sta3 ON sta2.sta2c1 = sta3.sta2c1 "
                    + "WHERE sta2c20 = ? AND sta3c10 = ?";

            StringBuilder cadenaSQL = new StringBuilder();
            StringBuilder sentences = new StringBuilder();
            cadenaSQL.append(ISOLATION_READ_UNCOMMITTED).append("SELECT ");
            states.forEach((state) ->
            {
                sentences.append("sum(case when sta3c10 = ").append(state).append(" then 1 else 0 end) AS estado").append(state).append(", ");
            });
            cadenaSQL.append(sentences.substring(0, sentences.lastIndexOf(",")))
                    .append(" FROM sta2")
                    .append(" INNER JOIN sta3 ON sta2.sta2c1 = sta3.sta2c1")
                    .append(" WHERE sta2c20 = ? AND sta3c10 IN (?,?,?)");

            List<Object> params = new ArrayList<>();
            params.add(date);
            params.add(states.get(0));
            params.add(states.get(1));
            params.add(states.get(2));
            getConnectionStat().queryForObject(cadenaSQL.toString(), params.toArray(), (ResultSet rs, int i) ->
            {
                widgetReceived.setSampleByTestEntry(rs.getInt("estado0"));
                widgetReceived.setSampleByTestValidated(rs.getInt("estado4"));
                widgetReceived.setSampleByTestPrinted(rs.getInt("estado5"));
                return rs.getInt("estado0");
            });
        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
        }
    }

    /**
     * Cantidad de ordenes registradas los ultimos cinco dias a partir del dia
     * de hoy
     *
     * @param dates
     * @throws Exception
     */
    default void countOrdersByDate(HashMap<Integer, Integer> dates) throws Exception
    {
        try
        {
            String sql = ISOLATION_READ_UNCOMMITTED + "SELECT COUNT(sta2.sta2c1) AS cant, sta2c20 "
                    + "FROM sta2 "
                    + "WHERE sta2c20 IN (" + new ArrayList<>(dates.keySet()).stream().map(date -> date.toString()).collect(Collectors.joining(",")) + ") "
                    + "GROUP BY sta2c20";
            getConnectionStat().query(sql, (ResultSet rs, int i) ->
            {
                dates.replace(rs.getInt("sta2c20"), rs.getInt("cant"));
                return rs.getInt("cant");
            });
        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
        }
    }

    /**
     * Cambia el estado del examen.
     *
     * @param orderId Número de orden de laboratorio
     * @param testId Identificador del examen
     * @param state
     *
     * @throws Exception Error en la base de datos.
     */
    default void changeStateTest(long orderId, int testId, int state) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(orderId));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String sta3 = year.equals(currentYear) ? "sta3" : "sta3_" + year;

            String update = ""
                    + "UPDATE    " + sta3
                    + " SET      sta3c10 = ?, sta3c15 = ?" // estado del examen
                    + " WHERE sta3c1 = ? "
                    + " AND sta2c1 = ? ";

            getConnectionStat().update(update, new Object[]
            {
                state, state < LISEnum.ResultTestState.VALIDATED.getValue() ? null : Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date())), testId, orderId
            });

        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
        }
    }

    /**
     * obtener el estado actual del examen de una orden.
     *
     * @param orderId Número de orden de laboratorio
     * @param testId Identificador del examen
     * @return
     *
     * @throws Exception Error en la base de datos.
     */
    default int getTestState(long orderId, int testId) throws Exception
    {
        try
        {
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT  sta3c10 "
                    + " FROM   sta3"
                    + " WHERE sta3c1 = ? "
                    + " AND sta2c1 = ? ";

            int orderPathology = getConnectionStat().queryForObject(select, new Object[]
            {
                testId, orderId
            }, Integer.class);

            return orderPathology;
            //TODO: Actualizar el historico para el paciente: Lab17
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return -1;
        }
    }

    /**
     * Cambio en el estado de la muestra en Estadisticas - Resultados
     *
     * @param state
     * @param order
     * @param tests
     */
    default void changeSampleStatus(Integer state, Long order, String tests)
    {
        try
        {
            getConnectionStat().update("UPDATE sta3 SET sta3c11 = ? "
                    + "WHERE sta2c1 = ? "
                    + "AND sta3c1 IN (" + tests + ")",
                    state,
                    order);
        } catch (Exception e)
        {
            StadisticsLog.error(e);
            e.getMessage();
        }
    }

    /**
     * Informacion de la caja para una orden
     *
     * @param orders Numeros de ordenes a buscar la caja
     * @return lista de cajas de las ordenes enviadas
     * 
     */
    default List<StatisticCashBox> listCashBox(List<Long> orders)
    {
        try
        {
            List<StatisticCashBox> list = new LinkedList<>();
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab902.lab22c1, lab902c6, lab902c3, lab902c5, lab902c8, lab902c9 "
                    + "FROM lab902 "
                    + " WHERE lab902.lab22c1 IN(" + orders.stream().map(t -> t.toString()).collect(Collectors.joining(",")) + ")";

            RowMapper mapper = (RowMapper<StatisticCashBox>) (ResultSet rs, int i) ->
            {
                StatisticCashBox cashbox = new StatisticCashBox();
                
                cashbox.setOrderNumber(rs.getLong("lab22c1"));
                cashbox.setCopay(rs.getDouble("lab902c6"));
                cashbox.setCopay(rs.getDouble("lab902c6"));
                cashbox.setDiscounts(rs.getDouble("lab902c3"));
                cashbox.setTaxe(rs.getDouble("lab902c5"));
                cashbox.setPayment(rs.getDouble("lab902c8"));
                cashbox.setBalance(rs.getDouble("lab902c9"));
                list.add(cashbox);
                return cashbox;
            };
            getConnection().query(query, mapper);
            return list;

        } catch (EmptyResultDataAccessException ex)
        {
            StadisticsLog.error(ex);
            return new ArrayList<>(0);
        }
    }
            
    default int createBatchResult(List<StatisticResult> list, Long orderNumber) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String sta3 = year.equals(currentYear) ? "sta3" : "sta3_" + year;
            
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnectionStat())
                .withTableName(sta3);

        list.stream().map((result) -> {
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
            params.put("sta3c24", result.getMultiplyBy());
            params.put("sta3c25", result.getBasic());
            params.put("sta3c26", result.getTypeStatisticBilling());
            params.put("sta3c27", result.getProfile());
            params.put("sta2c1", result.getOrderNumber());
            return params;
        }).forEachOrdered((parameters) -> {
            batchArray.add(parameters);
        });

        insert.executeBatch(batchArray.toArray(new HashMap[list.size()]));
        return 1;
    }
}

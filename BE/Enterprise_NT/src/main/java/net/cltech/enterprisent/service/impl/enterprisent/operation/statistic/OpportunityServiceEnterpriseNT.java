package net.cltech.enterprisent.service.impl.enterprisent.operation.statistic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.operation.statistics.OpportunityDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.opportunity.Bind;
import net.cltech.enterprisent.domain.masters.test.HomologationCode;
import net.cltech.enterprisent.domain.masters.test.TestByService;
import net.cltech.enterprisent.domain.operation.filters.StatisticFilter;
import net.cltech.enterprisent.domain.operation.results.ReferenceValues;
import net.cltech.enterprisent.domain.operation.statistic.Histogram;
import net.cltech.enterprisent.domain.operation.statistic.HistogramData;
import net.cltech.enterprisent.domain.operation.statistic.StaticticAveragetimes;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOpportunity;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOrder;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOrderAverageTimes;
import net.cltech.enterprisent.domain.operation.statistic.StatisticResult;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.opportunity.BindService;
import net.cltech.enterprisent.service.interfaces.masters.test.CentralSystemService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.OpportunityService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.StatisticService;
import net.cltech.enterprisent.tools.ConfigurationConstants;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.log.stadistics.StadisticsLog;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.FastMath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de estadisticas tiempos oportunidad para Enterprise NT
 *
 * @version 1.0.0
 * @author eacuna
 * @since 06/02/2018
 * @see Creacion
 */
@Service
public class OpportunityServiceEnterpriseNT implements OpportunityService
{

    @Autowired
    private OpportunityDao dao;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private BindService bindService;
    @Autowired
    private DemographicService serviceDemographic;
    @Autowired
    private ConfigurationDao daoConfig;
    @Autowired
    private CentralSystemService centralSystemService;
    @Autowired
    private ConfigurationService configurationService;

    @Override
    public void saveTest(StatisticResult test)
    {
        try
        {
            StadisticsLog.info("[EnterpriseNT - App] : entra creando registro tiempos de oportunidad ");
            if (!dao.opportunityExists(test.getOrderNumber(), test.getId()))
            {
                if (test.getOpportunityTimes().getVerifyDate() != null)
                {
                    LocalDateTime init = DateTools.localDateTime(test.getOpportunityTimes().getEntryDate());
                    LocalDateTime end = DateTools.localDateTime(test.getOpportunityTimes().getVerifyDate());
                    test.getOpportunityTimes().setVerifyElapsedTime(DateTools.getElapsedMinutes(init, end));
                }
                dao.addOpportunityTime(test.getOpportunityTimes());
            }
        } catch (Exception ex)
        {
            StadisticsLog.info("[EnterpriseNT - App] : Error creando registro tiempos de oportunidad ");
            StadisticsLog.error(ex);

        }
    }

    @Override
    public List<StatisticOrder> controlList(StatisticFilter filter) throws Exception
    {
        List<TestByService> testByService;
        filter.setTestState(4);
        List<StatisticOrder> orders = statisticService.listFilters(filter, true, false, false, 1);
        if (!orders.isEmpty())
        {
            testByService = dao.listTestByService();
            orders.forEach((order) ->
            {
                setTestOpportunityTimes(order, testByService);
            });
        }
        return orders;
    }

    @Override
    public List<StatisticOrder> opportunityIndicators(StatisticFilter filter) throws Exception
    {
        List<TestByService> testByService;
        filter.setTestState(1);
        List<StatisticOrder> orders = statisticService.listFilters(filter, false, false, false, 1);
        if (!orders.isEmpty())
        {
            testByService = dao.listTestByService();
            orders.forEach((order) ->
            {
                setTestOpportunityTimes(order, testByService);
            });
        }
        return orders;

    }

    @Override
    public List<StatisticOrder> opportunityTimes(StatisticFilter search, boolean averageTime) throws Exception
    {

        int sistemaCentralListados = 0;
        if (daoConfig.get("SistemaCentralListados").getValue() != null && !daoConfig.get("SistemaCentralListados").getValue().isEmpty())
        {
            sistemaCentralListados = Integer.parseInt(daoConfig.get("SistemaCentralListados").getValue());
        }

        final List<HomologationCode> homologationcodes = centralSystemService.gethomologationcodes(sistemaCentralListados);

        search.setStartDate(1);
        search.setTestState(3);

        List<StatisticOrder> orders = dao.list(search.getInit(), search.getEnd(), search.getRangeType(), serviceDemographic.list(true), false, false, 1, search.getDemographics(), search.getTests() != null && !search.getTests().isEmpty() ? search.getTests() : new ArrayList<>(), search.isGroupProfiles(), search.getTestFilterType());

        orders = orders.stream()
                .filter(filter -> filter.getPatient().getId() != null && filter.getPatient().getId() != 0)
                .filter(filter -> !filter.getResults().isEmpty())
                .map(order -> order.setResults(setHomologationCodes(order.getResults(), homologationcodes)))
                .collect(Collectors.toList());

        if (search.getOpportunityTime() != null || search.isGroupProfiles())
        {
            if ((search.getTests() != null && !search.getTests().isEmpty()) || search.isGroupProfiles())
            {
                orders = validateProfileOrders(orders, search.getTests() != null && !search.getTests().isEmpty() ? search.getTests() : new ArrayList<>(), search.isGroupProfiles(), 0);
            }

            orders = orders.stream()
                    .map(order -> order.setResults(validTimeFilter(order.getResults(), search.getOpportunityTime(), averageTime, search.getStartDate() != 1)))
                    .filter(order -> !order.getResults().isEmpty())
                    .collect(Collectors.toList());

            List<Long> idOrders = orders.stream().map(StatisticOrder::getOrderNumber).collect(Collectors.toList());

            if (idOrders.size() > 0)
            {
                final List<ReferenceValues> referenceValues = dao.getReferenceValues(idOrders);
                orders.forEach(order -> order.getResults().stream().forEach(r -> r.setReferenceValues(filterReferenceValues(r.getOrderNumber(), r.getId(), referenceValues))));
            }
        }

        orders.stream().filter(order -> !order.getResults().isEmpty()).collect(Collectors.toList());
        String laboratoryName = configurationService.getValue(ConfigurationConstants.NAME_LABORATORY_TOP);
        if (laboratoryName.equals("True"))
        {
            orders.forEach(order ->
            {
                order.getResults().stream().forEachOrdered(result ->
                {
                    result.setExpectedTimeDays(getExpectedDay(result.getId(), order.getService()));
                    result.setLaboratoryRemisionName(getLaboratoryRemisionName(order.getOrderNumber(), result.getId()));
                });
            });

            orders.forEach(order ->
            {
                Collections.sort(order.getResults(), (StatisticResult o1, StatisticResult o2) -> o1.getLaboratoryRemisionName().compareTo(o2.getLaboratoryRemisionName()));
            });
        }
        /* orders.forEach(order ->
        {
            order.getResults().stream().forEachOrdered(result -> result.setExpectedTimeDays(getExpectedDay(result.getId(), order.getService())));
        });

        orders.forEach(order ->
        {
            order.getResults().stream().forEachOrdered(result -> result.setExpectedTimeDays(getExpectedDay(result.getId(), order.getService())));
        });

        orders = orders.stream()
                .map((StatisticOrder t) -> t.setResults(filterOrderTests(t, search)))
                .filter(order -> !order.getResults().isEmpty())
                .collect(Collectors.toList());

        orders.forEach(order ->
        {
            order.getResults().stream().forEachOrdered(result ->
            {
                result.setLaboratoryRemisionName(getLaboratoryRemisionName(order.getOrderNumber(), result.getId()));
            });
        });

        orders.forEach(order ->
        {
            Collections.sort(order.getResults(), (StatisticResult o1, StatisticResult o2) -> o1.getLaboratoryRemisionName().compareTo(o2.getLaboratoryRemisionName()));
        });*/
        // orders = validateOpportunityTimes(orders);
        return orders;
    }

    /**
     * Se encarga de agrupar los examenes por perfil
     *
     * @param orders //Lista de ordenes
     * @return //Devuelve un perfil con sus respectivos tiempos en cada proceso
     * teniendo en cuenta cada examen.
     */
    private List<StatisticOrder> validateProfileOrders(List<StatisticOrder> orders, List<Integer> listTests, boolean isGroupProfile, List<Integer> testState)
    {
        try
        {
            List<StatisticOrder> ordersResults = orders;
            for (StatisticOrder order : ordersResults)
            {
                StatisticResult testSend;
                List<StatisticResult> testsResults = new ArrayList<>();
                HashMap<Integer, StatisticResult> listProfeile = new HashMap<>();
                for (StatisticResult result : order.getResults())
                {
                    if (result.getProfile() != null && (result.getProfile() > 0 || listTests.contains(result.getId())))
                    {
                        int idProfile = result.getProfile() > 0 ? result.getProfile() : result.getId();
                        if (listProfeile.containsKey(idProfile))
                        {

                            testSend = validateResulSend(listProfeile.get(idProfile), result, idProfile == result.getId());
                            listProfeile.replace(idProfile, testSend);
                        } else
                        {
                            listProfeile.put(idProfile, result);
                        }
                    } else if (isGroupProfile)
                    {
                        if (listProfeile.containsKey(result.getId()))
                        {
                            //testSend = validateResulSend(listProfeile.get(idProfile), result, result.getProfile().equals(result.getId()));
                            testSend = validateResulSend(listProfeile.get(result.getId()), result, Objects.equals(listProfeile.get(result.getId()).getProfile(), result.getId()));
                            listProfeile.replace(result.getId(), testSend);
                        } else
                        {
                            listProfeile.put(result.getId(), result);
                        }
                    } else
                    {
                        testsResults.add(result);
                    }
                }
                if (!listProfeile.isEmpty())
                {
                    testsResults.addAll(listProfeile.values());
                }
                if (!testsResults.isEmpty() && testState == null)
                {
                    testsResults = testsResults.stream()
                            .filter(test -> test.getApplyStadistic() > 0 && test.getTestState() >= 3)
                            .collect(Collectors.toList());
                    order.setResults(testsResults);
                } else if (!testsResults.isEmpty() && testState != null)
                {
                    testsResults = testsResults.stream()
                            .filter(test -> test.getApplyStadistic() > 0 && testState.contains(test.getTestState()))
                            .collect(Collectors.toList());
                    order.setResults(testsResults);
                }
            }

            return ordersResults;
        } catch (Exception e)
        {
            StadisticsLog.error(e);
            return new ArrayList<>();
        }
    }

    @Override
    public StatisticOrderAverageTimes averageTimes(StatisticFilter search) throws Exception
    {
        List<StatisticOrder> orders = dao.list(search.getInit(), search.getEnd(), search.getRangeType(), serviceDemographic.list(true), false, false, 1, search.getDemographics(), search.getTests() != null && !search.getTests().isEmpty() ? search.getTests() : new ArrayList<>(), search.isGroupProfiles(), search.getTestFilterType());

        orders = orders.stream()
                .filter(filter -> filter.getPatient().getId() != null && filter.getPatient().getId() != 0)
                .filter(filter -> !filter.getResults().isEmpty())
                .collect(Collectors.toList());

        if (search.getOpportunityTime() != null || search.isGroupProfiles())
        {
            if ((search.getTests() != null && !search.getTests().isEmpty()) || search.isGroupProfiles())
            {
                List<Integer> tests = search.getTests() != null && !search.getTests().isEmpty() ? search.getTests() : new ArrayList<>();
                orders = validateProfileOrders(orders, tests, search.isGroupProfiles(), search.getListTestState());
            }
        }

        orders = orders.stream()
                .map((StatisticOrder t) -> t.setResults(filterOrderTests(t, search)))
                .filter(order -> !order.getResults().isEmpty())
                .collect(Collectors.toList());

        if (orders.isEmpty())
        {
            return null;
        } else
        {
            return validateOpportunityTimes(orders, search.getTypeGrouping());
        }
    }

    /**
     * Establece la lista de codigos de homologacion de un examen
     *
     * @param test examen con estados de muestra y resultado
     *
     * @return Examen con la lista de codigos de homologacion
     */
    private List<StatisticResult> setHomologationCodes(List<StatisticResult> tests, List<HomologationCode> homologationcodes)
    {
        tests.forEach((result) ->
        {

            List<HomologationCode> code = homologationcodes.stream()
                    .filter(filter -> Objects.equals(filter.getId(), result.getId()))
                    .collect(Collectors.toList());

            if (code.size() > 0)
            {
                result.setHomologationCode(code.get(0).getCodes());
            }
        });
        return tests;
    }

    /**
     * Se encarga de agrupar los examenes por perfil
     *
     * @param orders //Lista de ordenes
     * @return //Devuelve un perfil con sus respectivos tiempos en cada proceso
     * teniendo en cuenta cada examen.
     */
    private List<StatisticOrder> validateProfileOrders(List<StatisticOrder> orders, List<Integer> listTests, boolean isGroupProfile, int testState)
    {
        try
        {
            StadisticsLog.info("Entra en validateProfile");
            StadisticsLog.info("listTests: " + listTests.size() + " bandera: " + isGroupProfile);
            List<StatisticOrder> ordersResults = orders;
            for (StatisticOrder order : ordersResults)
            {
                StatisticResult testSend;
                List<StatisticResult> testsResults = new ArrayList<>();
                HashMap<Integer, StatisticResult> listProfeile = new HashMap<>();
                for (StatisticResult result : order.getResults())
                {
                    if (result.getProfile() != null && (result.getProfile() > 0 || listTests.contains(result.getId())))
                    {
                        int idProfile = result.getProfile() > 0 ? result.getProfile() : result.getId();
                        if (listProfeile.containsKey(idProfile))
                        {
                            //testSend = validateResulSend(listProfeile.get(idProfile), result, result.getProfile().equals(result.getId()));
                            testSend = validateResulSend(listProfeile.get(idProfile), result, idProfile == result.getId());
                            listProfeile.replace(idProfile, testSend);
                        } else
                        {
                            listProfeile.put(idProfile, result);
                        }
                    } else if (isGroupProfile)
                    {
                        if (listProfeile.containsKey(result.getId()))
                        {
                            //testSend = validateResulSend(listProfeile.get(idProfile), result, result.getProfile().equals(result.getId()));
                            testSend = validateResulSend(listProfeile.get(result.getId()), result, Objects.equals(listProfeile.get(result.getId()).getProfile(), result.getId()));
                            listProfeile.replace(result.getId(), testSend);
                        } else
                        {
                            listProfeile.put(result.getId(), result);
                        }
                    } else
                    {
                        testsResults.add(result);
                    }
                }
                if (!listProfeile.isEmpty())
                {
                    testsResults.addAll(listProfeile.values());
                }
                if (!testsResults.isEmpty() && testState == 0)
                {
                    testsResults = testsResults.stream()
                            .filter(test -> test.getApplyStadistic() > 0 && test.getTestState() >= 3)
                            .collect(Collectors.toList());
                    order.setResults(testsResults);
                } else if (!testsResults.isEmpty() && testState != 0)
                {
                    testsResults = testsResults.stream()
                            .filter(test -> test.getApplyStadistic() > 0 && test.getTestState() == testState)
                            .collect(Collectors.toList());
                    order.setResults(testsResults);
                }
            }

            return ordersResults;
        } catch (Exception e)
        {
            StadisticsLog.error(e);
            return new ArrayList<>();
        }
    }

    /**
     * Se encarga de reemplazar un examen con respecto a un perfil
     *
     * @param init
     * @param end
     * @param profileInit
     * @return //El resultado mas actualizado para estadisticas
     */
    private StatisticResult validateResulSend(StatisticResult init, StatisticResult end, boolean profileInit)
    {
        try
        {
            StatisticResult resultSend = null;

            if (profileInit)
            {
                resultSend = end;
                if (init.getTestState() < 3)
                {
                    resultSend.setTestState(init.getTestState());
                } else
                {
                    resultSend.setTestState(3);
                }

            } else
            {
                resultSend = init;
                if (resultSend.getTestState() == 0)
                {
                    resultSend.setTestState(3);
                } else if (end.getTestState() < 3)
                {
                    resultSend.setTestState(1);
                }
            }
            //Entrada
            if (init.getOpportunityTimes().getEntryDate() != null && end.getOpportunityTimes().getEntryDate() != null)
            {
                if (init.getOpportunityTimes().getEntryDate().after(end.getOpportunityTimes().getEntryDate()))
                {
                    resultSend.getOpportunityTimes().setEntryDate(init.getOpportunityTimes().getEntryDate());
                    resultSend.getOpportunityTimes().setEntryElapsedTime(init.getOpportunityTimes().getEntryElapsedTime());
                    resultSend.getOpportunityTimes().setEntryUser(init.getOpportunityTimes().getEntryUser());
                } else
                {
                    resultSend.getOpportunityTimes().setEntryDate(end.getOpportunityTimes().getEntryDate());
                    resultSend.getOpportunityTimes().setEntryElapsedTime(end.getOpportunityTimes().getEntryElapsedTime());
                    resultSend.getOpportunityTimes().setEntryUser(end.getOpportunityTimes().getEntryUser());
                }
            } else if (init.getOpportunityTimes().getEntryDate() != null)
            {
                resultSend.getOpportunityTimes().setEntryDate(init.getOpportunityTimes().getEntryDate());
                resultSend.getOpportunityTimes().setEntryElapsedTime(init.getOpportunityTimes().getEntryElapsedTime());
                resultSend.getOpportunityTimes().setEntryUser(init.getOpportunityTimes().getEntryUser());
            } else if (end.getOpportunityTimes().getEntryDate() != null)
            {
                resultSend.getOpportunityTimes().setEntryDate(end.getOpportunityTimes().getEntryDate());
                resultSend.getOpportunityTimes().setEntryElapsedTime(end.getOpportunityTimes().getEntryElapsedTime());
                resultSend.getOpportunityTimes().setEntryUser(end.getOpportunityTimes().getEntryUser());
            }

            //toma de la muestra
            if (init.getOpportunityTimes().getTakeDate() != null && end.getOpportunityTimes().getTakeDate() != null)
            {
                if (init.getOpportunityTimes().getTakeDate().after(end.getOpportunityTimes().getTakeDate()))
                {
                    resultSend.getOpportunityTimes().setTakeDate(init.getOpportunityTimes().getTakeDate());
                    resultSend.getOpportunityTimes().setTakeElapsedTime(init.getOpportunityTimes().getTakeElapsedTime());
                    resultSend.getOpportunityTimes().setTakeUser(init.getOpportunityTimes().getTakeUser());
                } else
                {
                    resultSend.getOpportunityTimes().setTakeDate(end.getOpportunityTimes().getTakeDate());
                    resultSend.getOpportunityTimes().setTakeElapsedTime(end.getOpportunityTimes().getTakeElapsedTime());
                    resultSend.getOpportunityTimes().setTakeUser(end.getOpportunityTimes().getTakeUser());
                }
            } else if (init.getOpportunityTimes().getTakeDate() != null)
            {
                resultSend.getOpportunityTimes().setTakeDate(init.getOpportunityTimes().getTakeDate());
                resultSend.getOpportunityTimes().setTakeElapsedTime(init.getOpportunityTimes().getTakeElapsedTime());
                resultSend.getOpportunityTimes().setTakeUser(init.getOpportunityTimes().getTakeUser());
            } else if (end.getOpportunityTimes().getTakeDate() != null)
            {
                resultSend.getOpportunityTimes().setTakeDate(end.getOpportunityTimes().getTakeDate());
                resultSend.getOpportunityTimes().setTakeElapsedTime(end.getOpportunityTimes().getTakeElapsedTime());
                resultSend.getOpportunityTimes().setTakeUser(end.getOpportunityTimes().getTakeUser());
            }

            //tranporte
            if (init.getOpportunityTimes().getTransportDate() != null && end.getOpportunityTimes().getTransportDate() != null)
            {
                if (init.getOpportunityTimes().getTransportDate().after(end.getOpportunityTimes().getTransportDate()))
                {
                    resultSend.getOpportunityTimes().setTransportDate(init.getOpportunityTimes().getTransportDate());
                } else
                {
                    resultSend.getOpportunityTimes().setTransportDate(end.getOpportunityTimes().getTransportDate());
                }
            } else if (init.getOpportunityTimes().getTransportDate() != null)
            {
                resultSend.getOpportunityTimes().setTransportDate(init.getOpportunityTimes().getTransportDate());
            } else if (end.getOpportunityTimes().getTransportDate() != null)
            {
                resultSend.getOpportunityTimes().setTransportDate(end.getOpportunityTimes().getTransportDate());
            }

            //impresion de muestra
            if (init.getOpportunityTimes().getPrintSampleDate() != null && end.getOpportunityTimes().getPrintSampleDate() != null)
            {
                if (init.getOpportunityTimes().getPrintSampleDate().after(end.getOpportunityTimes().getPrintSampleDate()))
                {
                    resultSend.getOpportunityTimes().setPrintSampleDate(init.getOpportunityTimes().getPrintSampleDate());
                } else
                {
                    resultSend.getOpportunityTimes().setPrintSampleDate(end.getOpportunityTimes().getPrintSampleDate());
                }
            } else if (init.getOpportunityTimes().getPrintSampleDate() != null)
            {
                resultSend.getOpportunityTimes().setPrintSampleDate(init.getOpportunityTimes().getPrintSampleDate());
            } else if (end.getOpportunityTimes().getPrintSampleDate() != null)
            {
                resultSend.getOpportunityTimes().setPrintSampleDate(end.getOpportunityTimes().getPrintSampleDate());
            }

            //Verificacion
            if (init.getOpportunityTimes().getVerifyDate() != null && end.getOpportunityTimes().getVerifyDate() != null)
            {
                if (init.getOpportunityTimes().getVerifyDate().after(end.getOpportunityTimes().getVerifyDate()))
                {
                    resultSend.getOpportunityTimes().setVerifyDate(init.getOpportunityTimes().getVerifyDate());
                    //resultSend.getOpportunityTimes().setVerifyElapsedTime(init.getOpportunityTimes().getVerifyElapsedTime());
                    resultSend.getOpportunityTimes().setVerifyElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getEntryDate(), init.getOpportunityTimes().getVerifyDate()));
                    resultSend.getOpportunityTimes().setVerifyUser(init.getOpportunityTimes().getVerifyUser());
                } else
                {
                    resultSend.getOpportunityTimes().setVerifyDate(end.getOpportunityTimes().getVerifyDate());
                    //resultSend.getOpportunityTimes().setVerifyElapsedTime(end.getOpportunityTimes().getVerifyElapsedTime());
                    resultSend.getOpportunityTimes().setVerifyElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getEntryDate(), end.getOpportunityTimes().getVerifyDate()));
                    resultSend.getOpportunityTimes().setVerifyUser(end.getOpportunityTimes().getVerifyUser());
                }
            } else if (init.getOpportunityTimes().getVerifyDate() != null)
            {
                resultSend.getOpportunityTimes().setVerifyDate(init.getOpportunityTimes().getVerifyDate());
                //resultSend.getOpportunityTimes().setVerifyElapsedTime(init.getOpportunityTimes().getVerifyElapsedTime());
                resultSend.getOpportunityTimes().setVerifyElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getEntryDate(), init.getOpportunityTimes().getVerifyDate()));
                resultSend.getOpportunityTimes().setVerifyUser(init.getOpportunityTimes().getVerifyUser());
            } else if (end.getOpportunityTimes().getVerifyDate() != null)
            {
                resultSend.getOpportunityTimes().setVerifyDate(end.getOpportunityTimes().getVerifyDate());
                //resultSend.getOpportunityTimes().setVerifyElapsedTime(end.getOpportunityTimes().getVerifyElapsedTime());
                resultSend.getOpportunityTimes().setVerifyElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getEntryDate(), end.getOpportunityTimes().getVerifyDate()));
                resultSend.getOpportunityTimes().setVerifyUser(end.getOpportunityTimes().getVerifyUser());
            }

            //Resultados
            if (init.getOpportunityTimes().getResultDate() != null && end.getOpportunityTimes().getResultDate() != null)
            {
                if (init.getOpportunityTimes().getResultDate().after(end.getOpportunityTimes().getResultDate()))
                {
                    resultSend.getOpportunityTimes().setResultDate(init.getOpportunityTimes().getResultDate());
                    //resultSend.getOpportunityTimes().setResultElapsedTime(init.getOpportunityTimes().getResultElapsedTime());
                    resultSend.getOpportunityTimes().setResultElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getVerifyDate(), init.getOpportunityTimes().getResultDate()));
                    resultSend.getOpportunityTimes().setResultUser(init.getOpportunityTimes().getResultUser());
                } else
                {
                    resultSend.getOpportunityTimes().setResultDate(end.getOpportunityTimes().getResultDate());
                    //resultSend.getOpportunityTimes().setResultElapsedTime(end.getOpportunityTimes().getResultElapsedTime());
                    resultSend.getOpportunityTimes().setResultElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getVerifyDate(), end.getOpportunityTimes().getResultDate()));
                    resultSend.getOpportunityTimes().setResultUser(end.getOpportunityTimes().getResultUser());
                }
            } else if (init.getOpportunityTimes().getResultDate() != null)
            {
                resultSend.getOpportunityTimes().setResultDate(init.getOpportunityTimes().getResultDate());
                //resultSend.getOpportunityTimes().setResultElapsedTime(init.getOpportunityTimes().getResultElapsedTime());
                resultSend.getOpportunityTimes().setResultElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getVerifyDate(), init.getOpportunityTimes().getResultDate()));
                resultSend.getOpportunityTimes().setResultUser(init.getOpportunityTimes().getResultUser());
            } else if (end.getOpportunityTimes().getResultDate() != null)
            {
                resultSend.getOpportunityTimes().setResultDate(end.getOpportunityTimes().getResultDate());
                //resultSend.getOpportunityTimes().setResultElapsedTime(end.getOpportunityTimes().getResultElapsedTime());
                resultSend.getOpportunityTimes().setResultElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getVerifyDate(), end.getOpportunityTimes().getResultDate()));
                resultSend.getOpportunityTimes().setResultUser(end.getOpportunityTimes().getResultUser());
            }

            //Validacion
            if (init.getOpportunityTimes().getValidDate() != null && end.getOpportunityTimes().getValidDate() != null)
            {
                if (init.getOpportunityTimes().getValidDate().after(end.getOpportunityTimes().getValidDate()))
                {
                    resultSend.getOpportunityTimes().setValidDate(init.getOpportunityTimes().getValidDate());
                    //resultSend.getOpportunityTimes().setValidElapsedTime(init.getOpportunityTimes().getValidElapsedTime());
                    resultSend.getOpportunityTimes().setValidElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getResultDate(), init.getOpportunityTimes().getValidDate()));
                    resultSend.getOpportunityTimes().setValidUser(init.getOpportunityTimes().getValidUser());
                } else
                {
                    resultSend.getOpportunityTimes().setValidDate(end.getOpportunityTimes().getValidDate());
                    //resultSend.getOpportunityTimes().setValidElapsedTime(end.getOpportunityTimes().getValidElapsedTime());
                    resultSend.getOpportunityTimes().setValidElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getResultDate(), end.getOpportunityTimes().getValidDate()));
                    resultSend.getOpportunityTimes().setValidUser(end.getOpportunityTimes().getValidUser());
                }
            } else if (init.getOpportunityTimes().getValidDate() != null)
            {
                resultSend.getOpportunityTimes().setValidDate(init.getOpportunityTimes().getValidDate());
                //resultSend.getOpportunityTimes().setValidElapsedTime(init.getOpportunityTimes().getValidElapsedTime());
                resultSend.getOpportunityTimes().setValidElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getResultDate(), init.getOpportunityTimes().getValidDate()));
                resultSend.getOpportunityTimes().setValidUser(init.getOpportunityTimes().getValidUser());
            } else if (end.getOpportunityTimes().getValidDate() != null)
            {
                resultSend.getOpportunityTimes().setValidDate(end.getOpportunityTimes().getValidDate());
                resultSend.getOpportunityTimes().setValidElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getResultDate(), end.getOpportunityTimes().getValidDate()));
                resultSend.getOpportunityTimes().setValidUser(end.getOpportunityTimes().getValidUser());
            }

            //Impresion
            if (init.getOpportunityTimes().getPrintDate() != null && end.getOpportunityTimes().getPrintDate() != null)
            {
                if (init.getOpportunityTimes().getPrintDate().after(end.getOpportunityTimes().getPrintDate()))
                {
                    resultSend.getOpportunityTimes().setPrintDate(init.getOpportunityTimes().getPrintDate());
                    //resultSend.getOpportunityTimes().setPrintElapsedTime(init.getOpportunityTimes().getPrintElapsedTime());
                    resultSend.getOpportunityTimes().setPrintElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getValidDate(), init.getOpportunityTimes().getPrintDate()));
                    resultSend.getOpportunityTimes().setPrintUser(init.getOpportunityTimes().getPrintUser());
                } else
                {
                    resultSend.getOpportunityTimes().setPrintDate(end.getOpportunityTimes().getPrintDate());
                    //resultSend.getOpportunityTimes().setPrintElapsedTime(end.getOpportunityTimes().getPrintElapsedTime());
                    resultSend.getOpportunityTimes().setPrintElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getValidDate(), end.getOpportunityTimes().getPrintDate()));
                    resultSend.getOpportunityTimes().setPrintUser(end.getOpportunityTimes().getPrintUser());
                }
            } else if (init.getOpportunityTimes().getPrintDate() != null)
            {
                resultSend.getOpportunityTimes().setPrintDate(init.getOpportunityTimes().getPrintDate());
                //resultSend.getOpportunityTimes().setPrintElapsedTime(init.getOpportunityTimes().getPrintElapsedTime());
                resultSend.getOpportunityTimes().setPrintElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getValidDate(), init.getOpportunityTimes().getPrintDate()));
                resultSend.getOpportunityTimes().setPrintUser(init.getOpportunityTimes().getPrintUser());
            } else if (end.getOpportunityTimes().getPrintDate() != null)
            {
                resultSend.getOpportunityTimes().setPrintDate(end.getOpportunityTimes().getPrintDate());
                // resultSend.getOpportunityTimes().setPrintElapsedTime(end.getOpportunityTimes().getPrintElapsedTime());
                resultSend.getOpportunityTimes().setPrintElapsedTime(getDifferenceBetwenDates(resultSend.getOpportunityTimes().getValidDate(), end.getOpportunityTimes().getPrintDate()));
                resultSend.getOpportunityTimes().setPrintUser(end.getOpportunityTimes().getPrintUser());
            }

            //Fecha de actualizacion
            if (init.getOpportunityTimes().getCurrentDate() != null && end.getOpportunityTimes().getCurrentDate() != null)
            {
                if (init.getOpportunityTimes().getCurrentDate().after(end.getOpportunityTimes().getCurrentDate()))
                {
                    resultSend.getOpportunityTimes().setCurrentDate(init.getOpportunityTimes().getCurrentDate());
                } else
                {
                    resultSend.getOpportunityTimes().setCurrentDate(end.getOpportunityTimes().getCurrentDate());
                }
            } else if (init.getOpportunityTimes().getCurrentDate() != null)
            {
                resultSend.getOpportunityTimes().setCurrentDate(init.getOpportunityTimes().getCurrentDate());
            } else if (end.getOpportunityTimes().getCurrentDate() != null)
            {
                resultSend.getOpportunityTimes().setCurrentDate(end.getOpportunityTimes().getCurrentDate());
            }

            return resultSend;
        } catch (Exception e)
        {
            StadisticsLog.error(e);
            return new StatisticResult();
        }
    }

    private StatisticOrderAverageTimes validateOpportunityTimes(List<StatisticOrder> orders, Integer groupingType)
    {
        try
        {
            List<StatisticOrder> ordersSend = orders;
            HashMap<String, StaticticAveragetimes> generalOpportunityArea = new HashMap<>();
            HashMap<String, StaticticAveragetimes> generalOpportunityTest = new HashMap<>();
            HashMap<String, StaticticAveragetimes> generalOpportunityServices = new HashMap<>();
            HashMap<String, StaticticAveragetimes> generalOpportunityUser = new HashMap<>();
            HashMap<String, HashMap<String, StaticticAveragetimes>> generalOpportunityUserArea = new HashMap<>();
            HashMap<String, HashMap<String, StaticticAveragetimes>> generalOpportunityUserTest = new HashMap<>();
            ordersSend.forEach((statisticOrder) ->
            {
                statisticOrder.getResults().forEach((result) ->
                {
                    StaticticAveragetimes opportunity;
                    //Area
                    if (groupingType == 0 || groupingType == 3)
                    {
                        if (result.getSectionName() != null && !result.getSectionName().isEmpty())
                        {
                            if (generalOpportunityArea.containsKey(result.getSectionName()))
                            {
                                opportunity = validateResulSendOpportunity(generalOpportunityArea.get(result.getSectionName()), result.getOpportunityTimes());
                                opportunity.setNameArea(result.getSectionName());
                                generalOpportunityArea.replace(result.getSectionName(), opportunity);
                            } else
                            {
                                result.getOpportunityTimes().setNameArea(result.getSectionName());
                                opportunity = validateResulSendOpportunity(new StaticticAveragetimes(), result.getOpportunityTimes());
                                opportunity.setNameArea(result.getSectionName());
                                generalOpportunityArea.put(result.getSectionName(), opportunity);
                            }
                        }
                    }
                    if (groupingType == 0 || groupingType == 1)
                    {
                        //Prueba
                        if (result.getName() != null && !result.getName().isEmpty())
                        {
                            if (generalOpportunityTest.containsKey(result.getName()))
                            {
                                opportunity = validateResulSendOpportunity(generalOpportunityTest.get(result.getName()), result.getOpportunityTimes());
                                opportunity.setNameTest(result.getName());
                                generalOpportunityTest.replace(result.getName(), opportunity);
                            } else
                            {
                                result.getOpportunityTimes().setNameTest(result.getName());
                                opportunity = validateResulSendOpportunity(new StaticticAveragetimes(), result.getOpportunityTimes());
                                opportunity.setNameTest(result.getName());
                                generalOpportunityTest.put(result.getName(), opportunity);
                            }
                        }
                    }

                    if (groupingType == 0 || groupingType == 4)
                    {
                        //Servicio
                        if (statisticOrder.getServiceName() != null && !statisticOrder.getServiceName().isEmpty())
                        {
                            if (generalOpportunityServices.containsKey(statisticOrder.getServiceName()))
                            {
                                opportunity = validateResulSendOpportunity(generalOpportunityServices.get(statisticOrder.getServiceName()), result.getOpportunityTimes());
                                opportunity.setNameService(statisticOrder.getServiceName());
                                generalOpportunityServices.replace(statisticOrder.getServiceName(), opportunity);
                            } else
                            {
                                result.getOpportunityTimes().setNameService(statisticOrder.getServiceName());
                                opportunity = validateResulSendOpportunity(new StaticticAveragetimes(), result.getOpportunityTimes());
                                opportunity.setNameService(statisticOrder.getServiceName());
                                generalOpportunityServices.put(statisticOrder.getServiceName(), opportunity);
                            }
                        }
                    }

                    if (groupingType == 0 || groupingType == 5)
                    {
                        //Usuario
                        if (result.getOpportunityTimes().getValidUser() != null && result.getOpportunityTimes().getValidUser() > 0)
                        {
                            if (generalOpportunityUser.containsKey(String.valueOf(result.getOpportunityTimes().getValidUser())))
                            {
                                opportunity = validateResulSendOpportunity(generalOpportunityUser.get(String.valueOf(result.getOpportunityTimes().getValidUser())), result.getOpportunityTimes());
                                opportunity.setNameUser(String.valueOf(result.getOpportunityTimes().getValidUser()));
                                generalOpportunityUser.replace(String.valueOf(result.getOpportunityTimes().getValidUser()), opportunity);
                            } else
                            {
                                result.getOpportunityTimes().setNameUser(String.valueOf(result.getOpportunityTimes().getValidUser()));
                                opportunity = validateResulSendOpportunity(new StaticticAveragetimes(), result.getOpportunityTimes());
                                opportunity.setNameUser(String.valueOf(result.getOpportunityTimes().getValidUser()));
                                generalOpportunityUser.put(String.valueOf(result.getOpportunityTimes().getValidUser()), opportunity);
                            }
                        }
                    }

                    if (groupingType == 0 || groupingType == 8)
                    {
                        //Usuario/Area
                        if (result.getOpportunityTimes().getValidUser() != null && result.getOpportunityTimes().getValidUser() > 0)
                        {
                            if (generalOpportunityUserArea.containsKey(String.valueOf(result.getOpportunityTimes().getValidUser())))
                            {
                                if (result.getSectionName() != null && !result.getSectionName().isEmpty())
                                {
                                    if (generalOpportunityUserArea.get(String.valueOf(result.getOpportunityTimes().getValidUser())).containsKey(result.getSectionName()))
                                    {
                                        opportunity = validateResulSendOpportunity(generalOpportunityUserArea.get(String.valueOf(result.getOpportunityTimes().getValidUser())).get(result.getSectionName()), result.getOpportunityTimes());
                                        opportunity.setNameArea(result.getSectionName());
                                        generalOpportunityUserArea.get(String.valueOf(result.getOpportunityTimes().getValidUser())).replace(result.getSectionName(), opportunity);
                                    } else
                                    {
                                        result.getOpportunityTimes().setNameArea(result.getSectionName());
                                        opportunity = validateResulSendOpportunity(new StaticticAveragetimes(), result.getOpportunityTimes());
                                        opportunity.setNameArea(result.getSectionName());
                                        generalOpportunityUserArea.get(String.valueOf(result.getOpportunityTimes().getValidUser())).put(result.getSectionName(), opportunity);
                                    }
                                }
                            } else
                            {
                                generalOpportunityUserArea.put(String.valueOf(result.getOpportunityTimes().getValidUser()), new HashMap<>());
                                if (result.getSectionName() != null && !result.getSectionName().isEmpty())
                                {
                                    result.getOpportunityTimes().setNameArea(result.getSectionName());
                                    opportunity = validateResulSendOpportunity(new StaticticAveragetimes(), result.getOpportunityTimes());
                                    opportunity.setNameArea(result.getSectionName());
                                    generalOpportunityUserArea.get(String.valueOf(result.getOpportunityTimes().getValidUser())).put(result.getSectionName(), opportunity);
                                }
                            }
                        }
                    }

                    if (groupingType == 0 || groupingType == 6)
                    {
                        //Usuario/Examen
                        if (result.getOpportunityTimes().getValidUser() != null && result.getOpportunityTimes().getValidUser() > 0)
                        {
                            if (generalOpportunityUserTest.containsKey(String.valueOf(result.getOpportunityTimes().getValidUser())))
                            {
                                if (result.getName() != null && !result.getName().isEmpty())
                                {
                                    if (generalOpportunityUserTest.get(String.valueOf(result.getOpportunityTimes().getValidUser())).containsKey(result.getName()))
                                    {
                                        opportunity = validateResulSendOpportunity(generalOpportunityUserTest.get(String.valueOf(result.getOpportunityTimes().getValidUser())).get(result.getName()), result.getOpportunityTimes());
                                        opportunity.setNameTest(result.getName());
                                        generalOpportunityUserTest.get(String.valueOf(result.getOpportunityTimes().getValidUser())).replace(result.getName(), opportunity);
                                    } else
                                    {
                                        result.getOpportunityTimes().setNameTest(result.getName());
                                        opportunity = validateResulSendOpportunity(new StaticticAveragetimes(), result.getOpportunityTimes());
                                        opportunity.setNameTest(result.getName());
                                        generalOpportunityUserTest.get(String.valueOf(result.getOpportunityTimes().getValidUser())).put(result.getName(), opportunity);
                                    }
                                }
                            } else
                            {
                                generalOpportunityUserTest.put(String.valueOf(result.getOpportunityTimes().getValidUser()), new HashMap<>());
                                if (result.getName() != null && !result.getName().isEmpty())
                                {
                                    //result.getOpportunityTimes().setNameTest(result.getName());
                                    opportunity = validateResulSendOpportunity(new StaticticAveragetimes(), result.getOpportunityTimes());
                                    opportunity.setNameTest(result.getName());
                                    generalOpportunityUserTest.get(String.valueOf(result.getOpportunityTimes().getValidUser())).put(result.getName(), opportunity);
                                }
                            }
                        }
                    }
                });

            });
            HashMap<String, List<StaticticAveragetimes>> generalOpportunityUserAreaAux = new HashMap<>();
            HashMap<String, List<StaticticAveragetimes>> generalOpportunityUserTestAux = new HashMap<>();
            generalOpportunityUserArea.keySet().forEach((string) ->
            {
                generalOpportunityUserAreaAux.put(string, new ArrayList<>(generalOpportunityUserArea.get(string).values()));
            });
            generalOpportunityUserTest.keySet().forEach((string) ->
            {
                generalOpportunityUserTestAux.put(string, new ArrayList<>(generalOpportunityUserTest.get(string).values()));
            });

            StatisticOrderAverageTimes statisticOrderAverageTimes = new StatisticOrderAverageTimes();
            statisticOrderAverageTimes.setGeneralOpportunityArea(new ArrayList<>(generalOpportunityArea.values()));
            statisticOrderAverageTimes.setGeneralOpportunityTest(new ArrayList<>(generalOpportunityTest.values()));
            statisticOrderAverageTimes.setGeneralOpportunityServices(new ArrayList<>(generalOpportunityServices.values()));
            statisticOrderAverageTimes.setGeneralOpportunityUser(new ArrayList<>(generalOpportunityUser.values()));
            statisticOrderAverageTimes.setGeneralOpportunityUserArea(generalOpportunityUserAreaAux);
            statisticOrderAverageTimes.setGeneralOpportunityUserTest(generalOpportunityUserTestAux);

            return statisticOrderAverageTimes;
        } catch (Exception e)
        {
            StadisticsLog.error(e.getMessage());
            return null;
        }
    }

    public long getDifferenceBetwenDates(Date dateInicio, Date dateFinal)
    {
        if (dateInicio == null || dateFinal == null)
        {
            return 0;
        }
        long milliseconds = dateFinal.getTime() - dateInicio.getTime();
        return (long) (TimeUnit.MILLISECONDS.toMinutes(milliseconds));
    }

    private StaticticAveragetimes validateResulSendOpportunity(StaticticAveragetimes init, StatisticOpportunity end)
    {
        try
        {
            StaticticAveragetimes resultSend = new StaticticAveragetimes();
            resultSend.setTotalTest(init.getTotalTest() + 1);

            //ingreso-toma
            if (end.getTakeDate() != null)
            {
                resultSend.setTimeEntryTake(init.getTimeEntryTake() + getDifferenceBetwenDates(end.getEntryDate(), end.getTakeDate()));
            } else
            {
                resultSend.setTimeEntryTake(init.getTimeEntryTake());
            }
            //toma-transporte
            //transporte-verificacion
            if (end.getTransportDate() != null)
            {
                resultSend.setTimeTakeTransport(init.getTimeTakeTransport() + getDifferenceBetwenDates(end.getTakeDate(), end.getTransportDate()));
                resultSend.setTimeTransportVerific(init.getTimeTransportVerific() + getDifferenceBetwenDates(end.getTransportDate(), end.getVerifyDate()));
                resultSend.setTotalTestTransport(init.getTotalTestTransport() + 1);
            } else
            {
                resultSend.setTimeTakeTransport(init.getTimeTakeTransport());
                resultSend.setTimeTransportVerific(init.getTimeTransportVerific());
                resultSend.setTotalTestTransport(init.getTotalTestTransport());
            }

            //Verific-Result
            resultSend.setTimeVerificResult(init.getTimeVerificResult() + getDifferenceBetwenDates(end.getVerifyDate(), end.getResultDate()));

            //Result-Validate
            resultSend.setTimeResultValidate(init.getTimeResultValidate() + getDifferenceBetwenDates(end.getResultDate(), end.getValidDate()));

            if (end.getPrintDate() != null)
            {
                //Validate-Print
                resultSend.setTimeValidatePrint(init.getTimeValidatePrint() + getDifferenceBetwenDates(end.getValidDate(), end.getPrintDate()));
                //Verific - Print
                resultSend.setTimeVerificPrint(init.getTimeVerificPrint() + getDifferenceBetwenDates(end.getVerifyDate(), end.getPrintDate()));
                resultSend.setTotalTestPrint(init.getTotalTestPrint() + 1);
            } else
            {

                resultSend.setTimeValidatePrint(init.getTimeValidatePrint());
                resultSend.setTimeVerificPrint(init.getTimeVerificPrint());
                resultSend.setTotalTestPrint(init.getTotalTestPrint());
            }

            //Entry-Verific
            resultSend.setTimeEntryVerific(init.getTimeEntryVerific() + getDifferenceBetwenDates(end.getEntryDate(), end.getVerifyDate()));

            //Verific-Validate
            resultSend.setTimeVerificValidate(init.getTimeVerificValidate() + getDifferenceBetwenDates(end.getVerifyDate(), end.getValidDate()));

            return resultSend;
        } catch (Exception e)
        {
            StadisticsLog.error(e.getMessage());
            return init;
        }
    }

    /**
     * Filtra examenes de acuerdo a las opciones enviadas
     *
     * @param filter Orden con examenes a filtrar
     * @param search lista de examenes filtrados
     *
     * @return Resultados
     */
    private List<StatisticResult> filterOrderTests(StatisticOrder filter, StatisticFilter search)
    {
        List<StatisticResult> filteredTests;

        filteredTests
                = filter.getResults().stream()
                        .filter(test -> search.getAreas() == null || search.getAreas().isEmpty() || search.getAreas().contains(test.getSectionId()))
                        .filter(test -> search.getLaboratories() == null || search.getLaboratories().isEmpty() || search.getLaboratories().contains(test.getLaboratoryId()))
                        .filter(test -> search.getTests() == null || search.getTests().isEmpty() || search.getTests().contains(test.getId()))
                        .filter(test -> search.getLevels() == null || search.getLevels().isEmpty() || search.getLevels().contains(test.getLevelComplex()))
                        .collect(Collectors.toList());
        return filteredTests;
    }

    @Override
    public Histogram histogram(StatisticFilter filter) throws Exception
    {
        filter.setTestState(StatisticFilter.FILTER_TEST_ALL);//cambiar filtro por validadas (2) StatisticFilter.FILTER_TEST_VALID
        List<StatisticOrder> orders = statisticService.listFilters(filter, true, false, false, 1).stream()
                .map(order -> order.setResults(validTimeFilter(order.getResults(), filter.getOpportunityTime(), true, filter.getStartDate() != 1)))
                .filter(order -> !order.getResults().isEmpty())
                .collect(Collectors.toList());

        Histogram histogram = new Histogram();
        histogram.setBinds(bindService.list(true));
        if (histogram.getBinds().isEmpty())
        {
            throw new EnterpriseNTException(Arrays.asList("0|No binds configured"));
        }
        histogram.setDetail(histogramData(orders, histogram, filter.getStartDate() != 1));
        if (!histogram.getDetail().isEmpty())
        {
            calculateStatisticsValues(histogram);
        } else
        {
            return null;
        }

        return histogram;
    }

    /**
     * Establece los datos
     *
     * @param orders
     * @param histogram
     * @return
     */
    public List<HistogramData> histogramData(List<StatisticOrder> orders, Histogram histogram, boolean startWithEntry)
    {
        List<HistogramData> dataList = new ArrayList<>();
        orders.forEach((StatisticOrder order) ->
        {
            order.getResults().stream().map((result) ->
            {
                HistogramData data = new HistogramData();
                data.setOrderNumber(order.getOrderNumber());
                data.setId(result.getId());
                data.setCode(result.getCode());
                data.setName(result.getName());
                if (startWithEntry)
                {
                    data.setVerifyUser(result.getOpportunityTimes().getEntryUser());
                    data.setVerifyDate(result.getOpportunityTimes().getEntryDate());

                } else
                {
                    data.setVerifyUser(result.getOpportunityTimes().getVerifyUser());
                    data.setVerifyDate(result.getOpportunityTimes().getVerifyDate());
                }
                data.setValidUser(result.getOpportunityTimes().getValidUser());
                data.setValidDate(result.getOpportunityTimes().getValidDate());
                data.setTotalTime(result.getOpportunityTimes().getTotalTime());
                System.out.println(data.getOrderNumber() + "-" + "-" + data.getId() + "-" + result.getOpportunityTimes().getTotalTime());
                return data;
            }).filter(data -> data.getTotalTime() != null).map((data) ->
            {
                data.setCodeService(order.getServiceCode());
                return data;
            }).map((data) ->
            {
                data.setNameService(order.getServiceName());
                return data;
            }).map((data) ->
            {
                data.setBind(getCorrespondingBind(data, histogram.getBinds()));
                return data;
            }).filter((data) -> (data.getBind() != null)).map((data) ->
            {
                dataList.add(data);
                return data;
            }).forEachOrdered((data) ->
            {
                Bind dataBind = histogram.getBinds().get(histogram.getBinds().indexOf(data.getBind()));
                dataBind.setFrecuency(dataBind.getFrecuency() == null ? 1 : dataBind.getFrecuency() + 1);

            });
        });
        return dataList;

    }

    /**
     * Realiza los calculos estadisticos y los establece en el objeto Histogram
     *
     * @param histogram objeto con los datos estadisticos
     */
    public void calculateStatisticsValues(Histogram histogram)
    {
        double[] values = histogram.getDetail().stream()
                .mapToDouble(data -> data.getTotalTime())
                .toArray();

        histogram.setMean(StatUtils.mean(values));
        histogram.setMedian(StatUtils.percentile(values, 50));
        histogram.setPercentile25(StatUtils.percentile(values, 25));
        histogram.setPercentile75(StatUtils.percentile(values, 75));
        histogram.setStandardDeviation(FastMath.sqrt(StatUtils.variance(values)));
        histogram.setMode(StatUtils.mode(values));

    }

    /**
     * Establece la clase(bind) al dato del histograma de acuerto al tiempo
     * total
     *
     * @param data Dato del histograma al se se le establece la clase
     * @param binds
     */
    private Bind getCorrespondingBind(HistogramData data, List<Bind> binds)
    {
        return binds.stream()
                .filter(bind -> data.getTotalTime() >= bind.getMinimum() && (bind.getMaximum() == null || data.getTotalTime() < bind.getMaximum()))
                .findFirst()
                .orElse(null);

    }

    /**
     * Establece los tiempos esperados y maximos por examen y servicio
     *
     * @param order informacion de la orden y examenes
     * @param testByService lista de tiempos configurados
     */
    private void setTestOpportunityTimes(StatisticOrder order, List<TestByService> testByService)
    {
        order.getResults().forEach((result) ->
        {
            TestByService times = new TestByService(order.getService(), result.getId());
            if (testByService.contains(times) && result.getOpportunityTimes().getVerifyDate() != null)
            {
                times = testByService.get(testByService.indexOf(times));
                LocalDateTime init = DateTools.localDateTime(result.getOpportunityTimes().getVerifyDate());
                LocalDateTime end = DateTools.localDateTime(result.getOpportunityTimes().getCurrentDate());
                if (result.getOpportunityTimes().getValidDate() != null)
                {
                    end = DateTools.localDateTime(result.getOpportunityTimes().getValidDate());
                }
                Long minutes = DateTools.getElapsedMinutes(init, end);
                result.getOpportunityTimes().setElapsedTime(minutes);
                result.getOpportunityTimes().setExpectedTime((long) times.getExpectedTime());
                result.getOpportunityTimes().setMaxTime((long) times.getMaximumTime());
            }
        });
    }

    /**
     * Filtro de examanes por tiempo de validaci�n
     *
     * @param results Lista de resultados
     * @param maxTime Tiempo maximo
     * @param averageTime Indican si se manejan promedios
     * @param startWithEntry True si se calcula el total con respecto a la fecha
     * de ingreso, de lo contrario toma la fecha de vevrificaci�n.
     * @return Lista de Resultdos
     */
    public List<StatisticResult> validTimeFilter(List<StatisticResult> results, Integer maxTime, boolean averageTime, boolean startWithEntry)
    {
        return results.stream()
                .map(test -> test.setOpportunityTimes(sumTimes(test.getOpportunityTimes(), startWithEntry)))
                .filter(test -> averageTime || test.getOpportunityTimes().getEntryValidTime() != null)
                .filter(test -> maxTime == null || test.getOpportunityTimes().getEntryValidTime().compareTo(Long.parseLong(maxTime.toString())) > 0)
                .collect(Collectors.toList());
    }

    /**
     * Suma los tiempos para calcular el tiempo hasta la validacion
     *
     * @param opportunity Tiempos de Oportunidad con estados.
     * @return La suma de los tiempos de oportunidad
     */
    private StatisticOpportunity sumTimes(StatisticOpportunity opportunity, boolean startWithEntry)
    {

        if (opportunity.getVerifyElapsedTime() != null && opportunity.getResultElapsedTime() != null)
        {
            opportunity.setEntryResultTime(opportunity.getVerifyElapsedTime() + opportunity.getResultElapsedTime());
        } else
        {
            opportunity.setEntryResultTime(null);
        }

        if (opportunity.getVerifyElapsedTime() != null && opportunity.getResultElapsedTime() != null && opportunity.getValidElapsedTime() != null)
        {
            opportunity.setEntryValidTime(opportunity.getVerifyElapsedTime() + opportunity.getResultElapsedTime() + opportunity.getValidElapsedTime());
        } else
        {
            opportunity.setEntryValidTime(null);
        }

        if (opportunity.getVerifyElapsedTime() != null && opportunity.getResultElapsedTime() != null && opportunity.getValidElapsedTime() != null)
        {

            opportunity.setTotalTime((startWithEntry ? opportunity.getVerifyElapsedTime() : 0) + opportunity.getResultElapsedTime() + opportunity.getValidElapsedTime());
        } else
        {
            opportunity.setTotalTime(null);
        }
        return opportunity;
    }

    private Long getExpectedDay(Integer idTest, Integer idService)
    {
        long count = 0;

        int timeMax = dao.getTimeMaxByServiceAndByIdTest(idTest, idService);
        StadisticsLog.info("timeMax : " + timeMax);
        if (timeMax != -1)
        {
            count = DateTools.calculationOfTimes(timeMax, true, false, false);
        }
        return count;
    }

    private String getLaboratoryRemisionName(Long idOrder, Integer idTest)
    {
        StadisticsLog.info("[EnterpriseNT - App] : Id Order : " + idOrder + " Id Test : " + idTest);
        String laboratoryName = "";
        try
        {
            laboratoryName = dao.getLaboratoryName(idOrder, idTest);
            StadisticsLog.info("[EnterpriseNT - App] : Laboratory Name : " + laboratoryName);
        } catch (Exception e)
        {
        }
        return laboratoryName;
    }

    /**
     * Filtra los valores de referencia
     *
     * @param order orden
     * @param idTest
     * @param list Lista de valores de referencia
     * @return Lista de Resultdos
     */
    public ReferenceValues filterReferenceValues(Long order, Integer idTest, List<ReferenceValues> list)
    {
        return list.stream().filter(r -> order.equals(r.getOrder()) && idTest.equals(r.getTestId())).findFirst().orElse(null);
    }
    
    @Override
    public void saveTestBatch(List<StatisticResult> tests, Long orderNumber)
    {
        try
        {
            StadisticsLog.info("[EnterpriseNT - App] : entra creando registro tiempos de oportunidad batch ");
            
            List<StatisticOpportunity> opportunity = dao.opportunityExistsByOrder(orderNumber);
            List<StatisticOpportunity> listToAdd = new LinkedList<>();
             
            tests.forEach( test -> {
                StatisticOpportunity validate = opportunity.stream().filter(op -> test.getId().equals(op.getId()) && test.getOrderNumber().equals(op.getOrderNumber())).findFirst().orElse(null);
                if(validate == null) {
                    if (test.getOpportunityTimes().getVerifyDate() != null)
                    {
                        LocalDateTime init = DateTools.localDateTime(test.getOpportunityTimes().getEntryDate());
                        LocalDateTime end = DateTools.localDateTime(test.getOpportunityTimes().getVerifyDate());
                        test.getOpportunityTimes().setVerifyElapsedTime(DateTools.getElapsedMinutes(init, end));
                    }
                    listToAdd.add(test.getOpportunityTimes());
                }
                
            });
            if( listToAdd.size() > 0 ) {
                dao.addOpportunityTimeBatch(listToAdd, orderNumber);
            }
        } catch (Exception ex)
        {
            StadisticsLog.info("[EnterpriseNT - App] : Error creando registro tiempos de oportunidad batch ");
            StadisticsLog.error(ex);
        }
    }
}

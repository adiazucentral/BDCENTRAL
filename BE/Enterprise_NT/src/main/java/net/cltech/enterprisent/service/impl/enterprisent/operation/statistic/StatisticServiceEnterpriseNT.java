package net.cltech.enterprisent.service.impl.enterprisent.operation.statistic;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.operation.list.OrderListDao;
import net.cltech.enterprisent.dao.interfaces.operation.statistics.StatisticDao;
import net.cltech.enterprisent.dao.interfaces.operation.tracking.AuditDao;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.AgeGroup;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.operation.filters.StatisticFilter;
import net.cltech.enterprisent.domain.operation.microbiology.WhonetPlain;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.billing.CashBoxHeader;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.statistic.SampleByDestinationCount;
import net.cltech.enterprisent.domain.operation.statistic.SampleByDestinationHeader;
import net.cltech.enterprisent.domain.operation.statistic.StatisticCashBox;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOrder;
import net.cltech.enterprisent.domain.operation.statistic.StatisticPatient;
import net.cltech.enterprisent.domain.operation.statistic.StatisticResult;
import net.cltech.enterprisent.service.impl.enterprisent.operation.results.CheckResultServiceEnterpriseNT;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.AgeGroupService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
import net.cltech.enterprisent.service.interfaces.operation.orders.CashBoxService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.OpportunityService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.StatisticService;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Log;
import net.cltech.enterprisent.tools.StreamFilters;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.enums.ListEnum;
import net.cltech.enterprisent.tools.log.stadistics.StadisticsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de estadisticas para Enterprise NT
 *
 * @version 1.0.0
 * @author eacuna
 * @since 12/12/2017
 * @see Creacion
 */
@Service
public class StatisticServiceEnterpriseNT implements StatisticService
{

    @Autowired
    private StatisticDao dao;
    @Autowired
    private DemographicService demographicService;
    @Autowired
    private DemographicService serviceDemographic;
    @Autowired
    private OrderListDao listDao;
    @Autowired
    private ResultsService serviceResult;
    @Autowired
    private AuditDao auditDao;
    @Autowired
    private AgeGroupService ageGroupService;
    @Autowired
    private OpportunityService opportunityServices;
    @Autowired
    private ConfigurationService configServices;
    @Autowired
    private CashBoxService cashBoxService;
    @Autowired
    private LaboratorysByBranchesService laboratorysByBranchesService;
    @Autowired
    private HttpServletRequest request;


    @Override
    public void saveOrder(Long order)
    {
        try
        {
            StadisticsLog.info("Entra insertar orden estadistica {0}" + order.toString());
            dao.deleteOrder(order);
            StatisticOrder statisticOrder = dao.getOrder(order, demographicService.listBySource("O"));
            if (statisticOrder != null)
            {
                if (statisticOrder.getPatient() != null && statisticOrder.getPatient().getId() != null && statisticOrder.getPatient().getId() != 0)
                {
                    setAgeGroup(statisticOrder);
                }
                dao.createOrder(statisticOrder);
                saveTest(statisticOrder);
            }
        } catch (Exception ex)
        {
            StadisticsLog.info("Error registrando orden estadisticas");
            StadisticsLog.error(ex);
        }

    }

    @Override
    public void savePatient(int patient)
    {
        try
        {
            StadisticsLog.info("[EnterpriseNT - App] : Entra insertar paciente ");
            dao.deletePatient(patient);
            dao.createPatient(dao.getPatient(patient, demographicService.listBySource("H")));
        } catch (Exception ex)
        {
            StadisticsLog.info("Error registrando paciente estadisticas");
            StadisticsLog.error(ex);
        }
    }

    /**
     * Alimenta la información de examenes de una orden
     *
     * @param order
     */
    private void saveTest(StatisticOrder order)
    {
        try
        {
            StadisticsLog.info("[EnterpriseNT - App] : Entra insertar estadisticas examenes " + order.getOrderNumber());
            dao.deleteTests(order.getOrderNumber());
            for (StatisticResult result : order.getResults())
            {
                dao.createResult(result);
                opportunityServices.saveTest(result);
            }
        } catch (Exception ex)
        {
            StadisticsLog.info("[EnterpriseNT - App] : Error registrando examenes estadisticas " + order);
            StadisticsLog.error(ex);
        }
    }

    @Override
    public void saveTest(Long order, Integer test)
    {
        try
        {
            StadisticsLog.info("[EnterpriseNT - App] : Entra insertar estadisticas examen " + order);
            dao.deleteTests(order, test);
            for (StatisticResult result : dao.listOrderTest(order, test))
            {
                dao.createResult(result);
                opportunityServices.saveTest(result);
            }
        } catch (Exception ex)
        {
            StadisticsLog.info("[EnterpriseNT - App] : Error registrando examen estadisticas " + order);
            StadisticsLog.error(ex);
        }
    }

    @Override
    public void disableOrders(List<Long> orders, LISEnum.ResultOrderState state)
    {
        try
        {
            StadisticsLog.info("[EnterpriseNT - App] : Entra actualizar estado de la orden ");
            dao.disableOrders(orders, state);
        } catch (Exception ex)
        {
            StadisticsLog.info("[EnterpriseNT - App] : Error inactivando ordenes estadisticas ");
            StadisticsLog.error(ex);
        }
    }

    @Override
    public List<StatisticOrder> listFilters(StatisticFilter search, boolean infoPatient, boolean prices, boolean repeated, Integer opportunity) throws Exception
    {

        List<StatisticOrder> list = dao.list(search.getInit(), search.getEnd(), search.getRangeType(), serviceDemographic.list(true), prices, false, opportunity, search.getDemographics(), search.getTestState(), search.isGroupProfiles());
        List<StatisticOrder> filters = list.stream()
                .filter(filter -> filter.getPatient().getId() != null && filter.getPatient().getId() != 0)
                .filter(filter -> search.getDemographics() == null || search.getDemographics().isEmpty() || StreamFilters.containsDemographicStatistic(filter, search.getDemographics()))
                .map((StatisticOrder t) -> t.setResults(filterOrderTests(t, search)))
                .filter(filter -> !filter.getResults().isEmpty())
                .collect(Collectors.toList());

        //Agrupacion de examenes por perfil
        if (search.isGroupProfiles())
        {
            filters = validateProfileOrders(filters, search.getTests() != null && !search.getTests().isEmpty() ? search.getTests() : new ArrayList<>(), search.isGroupProfiles());

        }

        if (infoPatient && !filters.isEmpty())
        {
            List<StatisticPatient> patients = dao.listPatient(filters.stream().mapToLong(order -> order.getOrderNumber()).min().getAsLong(), filters.stream().mapToLong(order -> order.getOrderNumber()).max().getAsLong());
            filters.stream().filter((order) -> (patients.contains(order.getPatient()))).forEachOrdered((order) ->
            {
                StatisticPatient patient = patients.get(patients.indexOf(order.getPatient()));
                order.getPatient().setPatientId(Tools.decrypt(patient.getPatientId()));
                order.getPatient().setName1(Tools.decrypt(patient.getName1()));
                order.getPatient().setName2(Tools.decrypt(patient.getName2()));
                order.getPatient().setLastName(Tools.decrypt(patient.getLastName()));
                order.getPatient().setSurName(Tools.decrypt(patient.getSurName()));
            });
        }
        
        if(search.getFilterType() != null && search.getFilterType() == 5 && !filters.isEmpty()) {
            
            List<Long> orders = filters.stream().map(order -> order.getOrderNumber()).distinct().collect(Collectors.toList());

            List<StatisticCashBox> cashbox = dao.listCashBox(orders);
            filters.forEach( order -> {
                StatisticCashBox cash = cashbox.stream().filter( c -> c.getOrderNumber().equals(order.getOrderNumber())).findAny().orElse(null);
                if(cash != null) {
                    order.setCopay(cash.getCopay());
                    order.setDiscounts(cash.getDiscounts());
                    order.setPayment(cash.getPayment());
                    order.setTaxe(cash.getTaxe());
                    order.setBalance(cash.getBalance());
                }
            });
        }
        return filters;
    }

    /**
     * Se encarga de agrupar los examenes por perfil
     *
     * @param orders //Lista de ordenes
     * @return //Devuelve un perfil con sus respectivos tiempos en cada proceso
     * teniendo en cuenta cada examen.
     */
    private List<StatisticOrder> validateProfileOrders(List<StatisticOrder> orders, List<Integer> listTests, boolean isGroupProfile)
    {
        try
        {
            StadisticsLog.info("Entra en validateProfile");
            StadisticsLog.info("listTests: " + listTests.size() + " bandera: " + isGroupProfile);
            List<StatisticOrder> ordersResults = orders;
            for (StatisticOrder order : ordersResults)
            {
                StadisticsLog.info("ORDER : " + order.getOrderNumber() + " SIZE RESULT : " + order.getResults().size());
                StatisticResult testSend;
                List<StatisticResult> testsResults = new ArrayList<>();
                HashMap<Integer, StatisticResult> listProfeile = new HashMap<>();
                for (StatisticResult result : order.getResults())
                {
                    StadisticsLog.info("result : " + result.toString() );
                    int idProfile;
                    if (result.getProfile() != null && (result.getProfile() > 0 || listTests.contains(result.getId())))
                    {
                        
                         idProfile = result.getProfile() > 0 ? result.getProfile() : result.getId();
                         StadisticsLog.info("idProfile 1: " + idProfile );
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
                        idProfile = result.getProfile() != null && result.getProfile() > 0 ? result.getProfile() : result.getId();
                        StadisticsLog.info("idProfile 2: " + idProfile );
                        if (listProfeile.containsKey(idProfile))
                        {
                            //testSend = validateResulSend(listProfeile.get(idProfile), result, result.getProfile().equals(result.getId()));
                            testSend = validateResulSend(listProfeile.get(result.getId()), result, Objects.equals(listProfeile.get(result.getId()).getProfile(), result.getId()));
                            listProfeile.replace(idProfile, testSend);
                        } else
                        {
                            listProfeile.put(idProfile, result);
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
                if (!testsResults.isEmpty())
                {
                    testsResults = testsResults.stream()
                            .filter(filter -> filter.getApplyStadistic() > 0 && filter.getTestState() >= 3)
                            .collect(Collectors.toList());
                    order.setResults(testsResults);
                }
                StadisticsLog.info("Final de ORDER : " + order.getOrderNumber() + " SIZE RESULT : " + order.getResults().size());
                for (StatisticResult result : order.getResults())
                {
                     StadisticsLog.info("Value examen" + result.toString());
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
            StatisticResult resultSend;
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

            //Verificacion
            if (init.getOpportunityTimes().getVerifyDate() != null && end.getOpportunityTimes().getVerifyDate() != null)
            {
                if (init.getOpportunityTimes().getVerifyDate().after(end.getOpportunityTimes().getVerifyDate()))
                {
                    resultSend.getOpportunityTimes().setVerifyDate(init.getOpportunityTimes().getVerifyDate());
                    resultSend.getOpportunityTimes().setVerifyElapsedTime(init.getOpportunityTimes().getVerifyElapsedTime());
                    resultSend.getOpportunityTimes().setVerifyUser(init.getOpportunityTimes().getVerifyUser());
                } else
                {
                    resultSend.getOpportunityTimes().setVerifyDate(end.getOpportunityTimes().getVerifyDate());
                    resultSend.getOpportunityTimes().setVerifyElapsedTime(end.getOpportunityTimes().getVerifyElapsedTime());
                    resultSend.getOpportunityTimes().setVerifyUser(end.getOpportunityTimes().getVerifyUser());
                }
            } else if (init.getOpportunityTimes().getVerifyDate() != null)
            {
                resultSend.getOpportunityTimes().setVerifyDate(init.getOpportunityTimes().getVerifyDate());
                resultSend.getOpportunityTimes().setVerifyElapsedTime(init.getOpportunityTimes().getVerifyElapsedTime());
                resultSend.getOpportunityTimes().setVerifyUser(init.getOpportunityTimes().getVerifyUser());
            } else if (end.getOpportunityTimes().getVerifyDate() != null)
            {
                resultSend.getOpportunityTimes().setVerifyDate(end.getOpportunityTimes().getVerifyDate());
                resultSend.getOpportunityTimes().setVerifyElapsedTime(end.getOpportunityTimes().getVerifyElapsedTime());
                resultSend.getOpportunityTimes().setVerifyUser(end.getOpportunityTimes().getVerifyUser());
            }

            //Resultados
            if (init.getOpportunityTimes().getResultDate() != null && end.getOpportunityTimes().getResultDate() != null)
            {
                if (init.getOpportunityTimes().getResultDate().after(end.getOpportunityTimes().getResultDate()))
                {
                    resultSend.getOpportunityTimes().setResultDate(init.getOpportunityTimes().getResultDate());
                    resultSend.getOpportunityTimes().setResultElapsedTime(init.getOpportunityTimes().getResultElapsedTime());
                    resultSend.getOpportunityTimes().setResultUser(init.getOpportunityTimes().getResultUser());
                } else
                {
                    resultSend.getOpportunityTimes().setResultDate(end.getOpportunityTimes().getResultDate());
                    resultSend.getOpportunityTimes().setResultElapsedTime(end.getOpportunityTimes().getResultElapsedTime());
                    resultSend.getOpportunityTimes().setResultUser(end.getOpportunityTimes().getResultUser());
                }
            } else if (init.getOpportunityTimes().getResultDate() != null)
            {
                resultSend.getOpportunityTimes().setResultDate(init.getOpportunityTimes().getResultDate());
                resultSend.getOpportunityTimes().setResultElapsedTime(init.getOpportunityTimes().getResultElapsedTime());
                resultSend.getOpportunityTimes().setResultUser(init.getOpportunityTimes().getResultUser());
            } else if (end.getOpportunityTimes().getResultDate() != null)
            {
                resultSend.getOpportunityTimes().setResultDate(end.getOpportunityTimes().getResultDate());
                resultSend.getOpportunityTimes().setResultElapsedTime(end.getOpportunityTimes().getResultElapsedTime());
                resultSend.getOpportunityTimes().setResultUser(end.getOpportunityTimes().getResultUser());
            }

            //Validacion
            if (init.getOpportunityTimes().getValidDate() != null && end.getOpportunityTimes().getValidDate() != null)
            {
                if (init.getOpportunityTimes().getValidDate().after(end.getOpportunityTimes().getValidDate()))
                {
                    resultSend.getOpportunityTimes().setValidDate(init.getOpportunityTimes().getValidDate());
                    resultSend.getOpportunityTimes().setValidElapsedTime(init.getOpportunityTimes().getValidElapsedTime());
                    resultSend.getOpportunityTimes().setValidUser(init.getOpportunityTimes().getValidUser());
                } else
                {
                    resultSend.getOpportunityTimes().setValidDate(end.getOpportunityTimes().getValidDate());
                    resultSend.getOpportunityTimes().setValidElapsedTime(end.getOpportunityTimes().getValidElapsedTime());
                    resultSend.getOpportunityTimes().setValidUser(end.getOpportunityTimes().getValidUser());
                }
            } else if (init.getOpportunityTimes().getValidDate() != null)
            {
                resultSend.getOpportunityTimes().setValidDate(init.getOpportunityTimes().getValidDate());
                resultSend.getOpportunityTimes().setValidElapsedTime(init.getOpportunityTimes().getValidElapsedTime());
                resultSend.getOpportunityTimes().setValidUser(init.getOpportunityTimes().getValidUser());
            } else if (end.getOpportunityTimes().getValidDate() != null)
            {
                resultSend.getOpportunityTimes().setValidDate(end.getOpportunityTimes().getValidDate());
                resultSend.getOpportunityTimes().setValidElapsedTime(end.getOpportunityTimes().getValidElapsedTime());
                resultSend.getOpportunityTimes().setValidUser(end.getOpportunityTimes().getValidUser());
            }

            //Impresion
            if (init.getOpportunityTimes().getPrintDate() != null && end.getOpportunityTimes().getPrintDate() != null)
            {
                if (init.getOpportunityTimes().getPrintDate().after(end.getOpportunityTimes().getPrintDate()))
                {
                    resultSend.getOpportunityTimes().setPrintDate(init.getOpportunityTimes().getPrintDate());
                    resultSend.getOpportunityTimes().setPrintElapsedTime(init.getOpportunityTimes().getPrintElapsedTime());
                    resultSend.getOpportunityTimes().setPrintUser(init.getOpportunityTimes().getPrintUser());
                } else
                {
                    resultSend.getOpportunityTimes().setPrintDate(end.getOpportunityTimes().getPrintDate());
                    resultSend.getOpportunityTimes().setPrintElapsedTime(end.getOpportunityTimes().getPrintElapsedTime());
                    resultSend.getOpportunityTimes().setPrintUser(end.getOpportunityTimes().getPrintUser());
                }
            } else if (init.getOpportunityTimes().getPrintDate() != null)
            {
                resultSend.getOpportunityTimes().setPrintDate(init.getOpportunityTimes().getPrintDate());
                resultSend.getOpportunityTimes().setPrintElapsedTime(init.getOpportunityTimes().getPrintElapsedTime());
                resultSend.getOpportunityTimes().setPrintUser(init.getOpportunityTimes().getPrintUser());
            } else if (end.getOpportunityTimes().getPrintDate() != null)
            {
                resultSend.getOpportunityTimes().setPrintDate(end.getOpportunityTimes().getPrintDate());
                resultSend.getOpportunityTimes().setPrintElapsedTime(end.getOpportunityTimes().getPrintElapsedTime());
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

    @Override
    public List<StatisticOrder> listFiltersBox(StatisticFilter search, boolean infoPatient, boolean prices, boolean repeated, Integer opportunity) throws Exception
    {

        List<StatisticOrder> list = dao.listBox(search.getInit(), search.getEnd(), search.getRangeType(), serviceDemographic.list(true), prices, false, opportunity, search.getDemographics(), search.getTestState());
        List<StatisticOrder> filters = list.stream()
                .filter(filter -> filter.getPatient().getId() != null && filter.getPatient().getId() != 0)
                .filter(filter -> search.getDemographics() == null || search.getDemographics().isEmpty() || StreamFilters.containsDemographicStatistic(filter, search.getDemographics()))
                .map((StatisticOrder t) -> t.setResults(filterOrderTests(t, search)))
                .filter(filter -> !filter.getResults().isEmpty())
                .collect(Collectors.toList());

        if (infoPatient && !filters.isEmpty())
        {
            List<StatisticPatient> patients = dao.listPatient(filters.stream().mapToLong(order -> order.getOrderNumber()).min().getAsLong(), filters.stream().mapToLong(order -> order.getOrderNumber()).max().getAsLong());
            filters.stream().filter((order) -> (patients.contains(order.getPatient()))).forEachOrdered((order) ->
            {
                StatisticPatient patient = patients.get(patients.indexOf(order.getPatient()));
                order.getPatient().setPatientId(Tools.decrypt(patient.getPatientId()));
                order.getPatient().setName1(Tools.decrypt(patient.getName1()));
                order.getPatient().setName2(Tools.decrypt(patient.getName2()));
                order.getPatient().setLastName(Tools.decrypt(patient.getLastName()));
                order.getPatient().setSurName(Tools.decrypt(patient.getSurName()));
            });
        }
        return filters;
    }

    @Override
    public List<Order> listFiltersStatisticsSpecial(StatisticFilter search) throws Exception
    {
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        List<Order> list = listDao.list(search.getInit(), search.getEnd(), 3, serviceDemographic.list(true), null, Arrays.asList(search.getTest()), 0, 0, laboratorys, idbranch);

        List<Order> orders = list.stream()
                .filter(order -> order.getState() != LISEnum.ResultOrderState.CANCELED.getValue())
                .filter(order -> order.getPatient().getId() != 0)
                .filter(order -> search.getGender() == ListEnum.Gender.BOTH.getValue() || order.getPatient().getSex().getId().equals(search.getGender()))
                .filter(order -> DateTools.getAgeInYears(order.getPatient().getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()) >= search.getAgeMin() && DateTools.getAgeInYears(order.getPatient().getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()) <= search.getAgeMax())
                .filter(order -> search.getDemographics() == null || StreamFilters.containsDemographic(order, search.getDemographics()))
                .collect(Collectors.toList());

        List<ResultTest> tests;
        switch (search.getResultType())
        {
            case 0:
            case 1:
                tests = getTestResults(Arrays.asList(search.getTest()), orders.stream().map(order -> order.getOrderNumber()).collect(Collectors.toList()))
                        .stream()
                        .filter(test -> test.getResult() != null && !test.getResult().isEmpty())
                        .filter(test -> filterTests(test, search))
                        .collect(Collectors.toList());
                break;
            case 2:
                tests = getTestResults(Arrays.asList(search.getTest()), orders.stream().map(order -> order.getOrderNumber()).collect(Collectors.toList()))
                        .stream()
                        .filter(test -> test.getResultComment().getComment() != null && !test.getResultComment().getComment().isEmpty())
                        .filter(test -> filterTests(test, search))
                        .collect(Collectors.toList());
                break;
            default:
                tests = new ArrayList<>();
                break;
        }

        final List<ResultTest> resultsFinal = tests;
        orders = orders.stream()
                .map(order -> order.setResultTest(getOrderTests(order.getOrderNumber(), resultsFinal, search)))
                .filter(order -> !order.getResultTest().isEmpty())
                .collect(Collectors.toList());

        return serviceResult.addAdditional(orders);
    }

    @Override
    public List<StatisticOrder> listFiltersStatisticsMicrobiology(StatisticFilter search) throws Exception
    {
        List<StatisticOrder> list = dao.listOrderMicrobiology(search.getRangeType(), search.getInit(), search.getEnd(), serviceDemographic.list(true), search.getSamples(), search.getTests(), search.getMicroorganisms(), search.getAntibiotics(), search.getDemographics());
        return list.stream()
                .filter(order -> order.getState() != LISEnum.ResultOrderState.CANCELED.getValue())
                .filter(order -> order.getPatient().getId() != 0)
                .filter(order -> StreamFilters.containsDemographicStatistic(order, search.getDemographics()))
                .collect(Collectors.toList());
    }

    /**
     * Filtra examenes de acuerdo a las opciones enviadas
     *
     * @param order
     *
     * @return Resultados
     */
    private List<CashBoxHeader> getCashBox(Long order)
    {
        try
        {
            return cashBoxService.getCashBoxHeader(order);
        } catch (Exception e)
        {
            return new ArrayList<>();
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
        filteredTests = filter.getResults();
        filteredTests
                = filteredTests.stream()
                        .filter(test -> search.getAreas() == null || search.getAreas().isEmpty() || search.getAreas().contains(test.getSectionId()))
                        .filter(test -> search.getLaboratories() == null || search.getLaboratories().isEmpty() || search.getLaboratories().contains(test.getLaboratoryId()))
                        .filter(test -> search.getTests() == null || search.getTests().isEmpty() || search.getTests().contains(test.getId()))
                        .filter(test -> search.getLevels() == null || search.getLevels().isEmpty() || search.getLevels().contains(test.getLevelComplex()))
                        .collect(Collectors.toList());
        return filteredTests;
    }

    @Override
    public void enableOrders(Long order)
    {
        try
        {

            List<StatisticOrder> statisticOrders = dao.listOrders(order, demographicService.listBySource("O"));
            if (!statisticOrders.isEmpty())
            {
                dao.enableOrders(statisticOrders);
            }
        } catch (Exception ex)
        {
            StadisticsLog.info("[EnterpriseNT - App] : Error inactivando ordenes estadisticas ");
            StadisticsLog.error(ex);
        }
    }

    /**
     * Filtra examenes de la orden
     *
     * @param order numero de orden
     * @param allTests lista de examenes de consulta
     *
     * @return Examenes que corresponden a la orden
     */
    private List<ResultTest> getOrderTests(long order, List<ResultTest> allTests, StatisticFilter search)
    {
        List<ResultTest> orderResults = allTests.stream()
                .filter(test -> test.getTestId() == search.getTest())
                .filter(test -> test.getOrder() == order)
                .collect(Collectors.toList());

        return orderResults.stream()
                .filter(test -> filterTests(test, search))
                .collect(Collectors.toList());
    }

    /**
     * Filtra examenes por area, examen o confidenciales
     *
     * @param filter Orden con examenes a filtrar
     * @param filter lista de examenes filtrados
     *
     * @return
     */
    private boolean filterTests(ResultTest tests, StatisticFilter filter)
    {
        switch (filter.getResultType())
        {
            case 0:
                if (!(tests.getResult() != null && tests.getResultType() == LISEnum.ResultTestResultType.NUMERIC.getValue() && isNumericResult(tests.getResult()) && new BigDecimal(tests.getResult()).compareTo(filter.getRefMin()) != 2 && new BigDecimal(tests.getResult()).compareTo(filter.getRefMax()) != 1))
                {
                    return false;
                }
                break;
            case 1:
                if (!(tests.getResultType() == LISEnum.ResultTestResultType.TEXT.getValue() && tests.getResult() != null && !(tests.getResult().isEmpty()) && tests.getResult().equalsIgnoreCase(filter.getResult())))
                {
                    return false;
                }
                break;
            case 2:
                if (!(tests.getResult() != null && !(tests.getResult().isEmpty()) && tests.getResultComment().getComment().contains(filter.getComment())))
                {
                    return false;
                }
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean isNumericResult(String result)
    {
        try
        {
            Float.parseFloat(result);
            return true;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * Obtiene examenes de una orden
     *
     * @param order numero de la orden
     *
     * @return lista de examenes con información del resultado
     */
    private List<ResultTest> getTestResults(List<Integer> tests, List<Long> orders)
    {
        try
        {
            ResultFilter resultFilter = new ResultFilter();
            resultFilter.setTestsId(tests);
            resultFilter.setFilterId(5);
            return serviceResult.getTests(resultFilter, orders);
        } catch (Exception ex)
        {
            Log.error(CheckResultServiceEnterpriseNT.class, ex);
        }
        return new ArrayList<>();
    }

    @Override
    public List<StatisticOrder> listFiltersSampleReject(StatisticFilter search) throws Exception
    {
        List<StatisticOrder> list = dao.listOrderSampleRejected(search.getInit(), search.getEnd(), serviceDemographic.list(true), search.getSamples(), search.getDemographics());
        return list.stream()
                .filter(order -> order.getState() != LISEnum.ResultOrderState.CANCELED.getValue())
                .filter(order -> order.getPatient().getId() != 0)
                .filter(order -> StreamFilters.containsDemographicStatistic(order, search.getDemographics()))
                .collect(Collectors.toList());
    }

    @Override
    public List<StatisticOrder> statisticRepeated(StatisticFilter search) throws Exception
    {
        List<StatisticOrder> orders = listFilters(search, false, false, true, null);
        String orderNumbers = orders.stream().map(order -> order.getOrderNumber().toString()).collect(Collectors.joining(","));
        List<Motive> motives = auditDao.listMotives(orderNumbers, LISEnum.ResultReason.REPEAT);
        orders = orders.stream()
                .map(order -> fillResultMotives(order, motives))
                .collect(Collectors.toList());
        return orders;
    }

    /**
     * Alimenta los motivos de repeticion de los examenes de la orden
     *
     * @param order Entidad estadistica de la orden
     * @param motives Lista de motivos
     * @return
     */
    private StatisticOrder fillResultMotives(StatisticOrder order, List<Motive> motives)
    {
        order.getResults().forEach((result) ->
        {
            result.setRepeatReasons(
                    motives.stream()
                            .filter(motive -> motive.getOrder().equals(order.getOrderNumber()))
                            .filter(motive -> motive.getTest().equals(result.getId()))
                            .collect(Collectors.toList())
            );
        });

        return order;

    }

    /**
     * Establece la edad del paciente con respecto a la orden<br>
     * Unidad en días(1), meses(2), años(3)
     *
     * @param order información de la orden
     */
    private StatisticOrder setWhonetAge(StatisticOrder order)
    {
        long days = DateTools.getAgeInDays(DateTools.localDate(order.getPatient().getDob()), DateTools.localDate(order.getDateTime()));
        int unit = 1;
        if (days >= 365)
        {
            days = days / 365;
            unit = 3;
        } else if (days >= 31)
        {
            days = days / 31;
            unit = 2;
        }
        //Calculo de Edad y unidad de la edad
        order.setAge((int) days);
        order.setUnit(unit);
        return order;
    }

    /**
     * Establece unidad, edad del paciente y asigna grupo etario.
     *
     * @param order informacion de la orden
     */
    private void setAgeGroup(StatisticOrder order)
    {
        try
        {
            long ages = 0;
            int unit = 1;
            //Calculo de Edad y unidad de la edad

            ages = DateTools.getAgeInYears(DateTools.localDate(order.getPatient().getDob()), DateTools.localDate(order.getDateTime()));
            if (ages <= 0)
            {
                ages = DateTools.getAgeInMonths(DateTools.localDate(order.getPatient().getDob()), DateTools.localDate(order.getDateTime()));
                unit = 3;
                if (ages <= 0)
                {
                    ages = DateTools.getAgeInDays(DateTools.localDate(order.getPatient().getDob()), DateTools.localDate(order.getDateTime()));
                    unit = 2;
                }
            }

            order.setAge((int) ages);
            order.setUnit(unit);
            //Asignación grupo etario
            AgeGroup ageGroup = ageGroupService.filterByState(true)
                    .stream()
                    .filter(group -> group.getGender().getId().equals(ListEnum.Gender.BOTH.getValue()) || group.getGender().getId().equals(order.getPatient().getGender()))
                    .filter(group -> group.getUnitAge().intValue() == order.getUnit().intValue())
                    .filter(group -> order.getAge().compareTo(group.getAgeMin()) != -1)//<=
                    .filter(group -> order.getAge().compareTo(group.getAgeMax()) != 1)//>=
                    .findFirst()
                    .orElse(null);
            if (ageGroup != null)
            {
                order.setAgeGroup(ageGroup.getId());
                order.setAgeGroupCode(ageGroup.getCode());
                order.setAgeGroupName(ageGroup.getName());
            }
        } catch (Exception ex)
        {
            StadisticsLog.info("[EnterpriseNT - App] : Error asignando grupo etario ");
            StadisticsLog.error(ex);
        }
    }

    @Override
    public void sampleStateChanged(Integer state, Long order, Integer sample)
    {
        try
        {
            StadisticsLog.info("[EnterpriseNT - App] : entra actualizar tiempos de oportunidad (verificacion) ");
            List<StatisticResult> tests = dao.listTestTimes(order, null, sample).stream()
                    .map(test -> setElapsedTimes(test))
                    .collect(Collectors.toList());
            dao.updateTimes(tests);
        } catch (Exception ex)
        {
            StadisticsLog.info("[EnterpriseNT - App] : Error registrando oportunidad (verificacion) ");
            StadisticsLog.error(ex);
        }
    }

    @Override
    public void testStateChanged(Integer state, Long order, Integer testId)
    {
        try
        {
            List<StatisticResult> tests = dao.listTestTimes(order, testId, null).stream()
                    .map(test -> setElapsedTimes(test))
                    .collect(Collectors.toList());
            dao.updateTimes(tests);

        } catch (Exception ex)
        {
            StadisticsLog.info("[EnterpriseNT - App] : Error registrando oportunidad (examen) ");
            StadisticsLog.error(ex);
        }
    }

    private StatisticResult setElapsedTimes(StatisticResult result)
    {
        Long elapsedMinutes = null;
        if (result.getOpportunityTimes().getVerifyDate() != null && result.getOpportunityTimes().getEntryDate() != null)
        {
            elapsedMinutes = DateTools.getElapsedMinutes(result.getOpportunityTimes().getEntryDate(), result.getOpportunityTimes().getVerifyDate());
            result.getOpportunityTimes().setVerifyElapsedTime(elapsedMinutes);
        }
        if (result.getOpportunityTimes().getResultDate() != null && result.getOpportunityTimes().getVerifyDate() != null)
        {
            elapsedMinutes = DateTools.getElapsedMinutes(result.getOpportunityTimes().getVerifyDate(), result.getOpportunityTimes().getResultDate());
            result.getOpportunityTimes().setResultElapsedTime(elapsedMinutes);
        }
        if (result.getOpportunityTimes().getValidDate() != null && result.getOpportunityTimes().getResultDate() != null)
        {
            elapsedMinutes = DateTools.getElapsedMinutes(result.getOpportunityTimes().getResultDate(), result.getOpportunityTimes().getValidDate());
            result.getOpportunityTimes().setValidElapsedTime(elapsedMinutes);
        }
        if (result.getOpportunityTimes().getPrintDate() != null && result.getOpportunityTimes().getValidDate() != null)
        {
            elapsedMinutes = DateTools.getElapsedMinutes(result.getOpportunityTimes().getValidDate(), result.getOpportunityTimes().getPrintDate());
            result.getOpportunityTimes().setPrintElapsedTime(elapsedMinutes);
        }

        return result;
    }

    @Override
    public SampleByDestinationHeader sampleByDestiny(StatisticFilter filter) throws Exception
    {
        SampleByDestinationHeader header = new SampleByDestinationHeader();
        List<Order> orders = dao.sampleByDestiny(filter.getInit(), filter.getEnd(), filter.getSamples(), filter.getAreas(), demographicService.list(true), filter.getDemographics());
        if (!orders.isEmpty())
        {
            List<SampleByDestinationCount> countList = new ArrayList<>();
            SampleByDestinationCount sampleCount;
            for (Order order : orders)
            {
                for (Sample sample : order.getSamples())
                {
                    sampleCount = new SampleByDestinationCount(sample.getId(), order.getType().getId(), order.getBranch().getId());
                    if (!countList.contains(sampleCount))
                    {
                        sampleCount.setTotal(1);
                        sampleCount.setVerify(!sample.isCheck() ? 1 : 0);
                        sampleCount.setDestinations(new ArrayList<>());
                        sampleCount.setSampleName(sample.getName());
                        sampleCount.setOrderTypeCode(order.getType().getCode());
                        sampleCount.setOrderTypeName(order.getType().getName());
                        sampleCount.setBranchCode(order.getBranch().getCode());
                        sampleCount.setBranchName(order.getBranch().getName());

                        countList.add(sampleCount);

                    } else
                    {
                        sampleCount = countList.get(countList.indexOf(sampleCount));
                        sampleCount.setTotal(sampleCount.getTotal() + 1);
                        sampleCount.setVerify(!sample.isCheck() ? sampleCount.getVerify() + 1 : sampleCount.getVerify());
                    }
                    for (Destination filterDestination : filter.getSamplesDestiny().stream().map(id -> new Destination(id)).collect(Collectors.toList()))
                    {
                        if (!sampleCount.getDestinations().contains(filterDestination))
                        {
                            filterDestination.setUser(null);
                            sampleCount.getDestinations().add(filterDestination);
                        } else
                        {
                            filterDestination = sampleCount.getDestinations().get(sampleCount.getDestinations().indexOf(filterDestination));
                        }

                        if (sample.getDestinatios().contains(filterDestination))
                        {
                            if (filterDestination.getCheckAmount() == null)
                            {
                                filterDestination.setCheckAmount(0);
                            }
                            if (filterDestination.getUncheckAmount() == null)
                            {
                                filterDestination.setUncheckAmount(0);
                            }
                            Destination sampleDestination = sample.getDestinatios().get(sample.getDestinatios().indexOf(filterDestination));
                            filterDestination.setCheckAmount(sampleDestination.getVerified() ? filterDestination.getCheckAmount() + 1 : filterDestination.getCheckAmount());
                            filterDestination.setUncheckAmount(!sampleDestination.getVerified() ? filterDestination.getUncheckAmount() + 1 : filterDestination.getUncheckAmount());
                        }

                    }

                }
            }
            header.setDataGroup(countList);
            header.setDataDetail(orders);
            return header;

        }

        return null;
    }

    @Override
    public List<WhonetPlain> listMicrobiologyWhonet(StatisticFilter search) throws Exception
    {
        String thm = configServices.get(Configuration.KEY_WHONET_THM).getValue();
        String edta = configServices.get(Configuration.KEY_WHONET_EDTA).getValue();
        String apb = configServices.get(Configuration.KEY_WHONET_APB).getValue();
        String specType = configServices.get(Configuration.KEY_WHONET_TYPE).getValue();

        List<WhonetPlain> whonet = new ArrayList<>();
        listFiltersStatisticsMicrobiology(search)
                .forEach((StatisticOrder order) ->
                {
                    setWhonetAge(order);
                    order.getResults().forEach(test ->
                    {
                        test.getMicroorganisms().forEach(micro ->
                        {
                            micro.getResultsMicrobiology().forEach(abx ->
                            {
                                String sex = order.getPatient().getGender().equals(ListEnum.Gender.FEMALE.getValue()) ? "F" : order.getPatient().getGender().equals(ListEnum.Gender.MALE.getValue()) ? "M" : "U";
                                String specTypeValue = specType.equals(Configuration.WHONET_TYPE_ANATOMICAL) ? test.getAnatomicalSiteCode() : test.getSubSampleCode();

                                WhonetPlain record = new WhonetPlain();
                                record.setDocumentType(order.getPatient().getDocumentType());
                                record.setDocumentTypeCode(order.getPatient().getDocumentTypeCode());
                                record.setDocumentTypeName(order.getPatient().getDocumentTypeName());
                                record.setPatientId(order.getPatient().getPatientId());
                                record.setLastName(order.getPatient().getLastName());
                                record.setSurName(order.getPatient().getSurName());
                                record.setName1(order.getPatient().getName1());
                                record.setName2(order.getPatient().getName2());
                                record.setGender(sex);
                                record.setDob(order.getPatient().getDob());
                                record.setAge(order.getAge());
                                record.setAgeUnit(order.getUnit());
                                record.setDepartment(order.getBranchName());
                                record.setWardType(order.getServiceName());
                                record.setSpecNum(order.getOrderNumber().toString());
                                record.setSpecDate(order.getDateTime());
                                record.setSpecType(specTypeValue);
                                record.setOrganism(micro.getName());
                                record.setAbx(abx.getNameAntibiotic());
                                record.setMic(abx.getCmi());
                                record.setThm(thm.equals(abx.getIdAntibiotic().toString()) ? abx.getInterpretationCMI() : "");
                                record.setEdta(edta.equals(abx.getIdAntibiotic().toString()) ? abx.getInterpretationCMI() : "");
                                record.setApb(apb.equals(abx.getIdAntibiotic().toString()) ? abx.getInterpretationCMI() : "");
                                whonet.add(record);

                            });
                        });
                    });
                });
        return whonet;
    }

    @Override
    public void updateSampleStatus(Integer state, Long order, String tests) throws Exception
    {
        try
        {
            if (!tests.isEmpty())
            {
                tests = tests.substring(0, tests.length() - 1);
                dao.changeSampleStatus(state, order, tests);
            }
        } catch (Exception e)
        {
            e.getMessage();
        }
    }
    
    @Override
    public void saveOrderUpdate(Long order)
    {
        try
        {
            StadisticsLog.info("Entra insertar orden estadistica update {0}" + order.toString());
            dao.deleteOrder(order);
            StatisticOrder statisticOrder = dao.getOrder(order, demographicService.listBySource("O"));
            if (statisticOrder != null)
            {
                if (statisticOrder.getPatient() != null && statisticOrder.getPatient().getId() != null && statisticOrder.getPatient().getId() != 0)
                {
                    setAgeGroup(statisticOrder);
                }
                dao.createOrder(statisticOrder);
                saveTestUpdate(statisticOrder);
            }
        } catch (Exception ex)
        {
            StadisticsLog.info("Error registrando orden estadisticas");
            StadisticsLog.error(ex);
        }
    }
    
    @Override
    public void disableOrder(Long order, LISEnum.ResultOrderState state)
    {
        try
        {
            StadisticsLog.info("[EnterpriseNT - App] : Entra actualizar estado de la orden ");
            dao.disableOrder(order, state);
        } catch (Exception ex)
        {
            StadisticsLog.info("[EnterpriseNT - App] : Error inactivando ordenes estadisticas ");
            StadisticsLog.error(ex);
        }
    }
    
     /**
     * Alimenta la información de examenes de una orden
     *
     * @param order
     */
    private void saveTestUpdate(StatisticOrder order)
    {
        try
        {
            StadisticsLog.info("[EnterpriseNT - App] : Entra insertar estadisticas examenes " + order.getOrderNumber());
            dao.deleteTests(order.getOrderNumber());
            dao.createBatchResult(order.getResults(), order.getOrderNumber());
            opportunityServices.saveTestBatch(order.getResults(), order.getOrderNumber());
        } catch (Exception ex)
        {
            StadisticsLog.info("[EnterpriseNT - App] : Error registrando examenes estadisticas " + order);
            StadisticsLog.error(ex);
        }
    }
}

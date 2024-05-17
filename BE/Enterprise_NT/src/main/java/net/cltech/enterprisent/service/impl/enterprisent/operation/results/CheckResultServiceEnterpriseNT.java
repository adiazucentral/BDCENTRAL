package net.cltech.enterprisent.service.impl.enterprisent.operation.results;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.masters.interview.InterviewDao;
import net.cltech.enterprisent.dao.interfaces.operation.list.OrderListDao;
import net.cltech.enterprisent.domain.masters.interview.Interview;
import net.cltech.enterprisent.domain.masters.interview.PanicInterview;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.filters.OrderSearchFilter;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.orders.TestList;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderSearchService;
import net.cltech.enterprisent.service.interfaces.operation.results.CheckResultService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Log;
import net.cltech.enterprisent.tools.StreamFilters;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de hojas de trabajo para Enterprise NT
 *
 * @version 1.0.0
 * @author eacuna
 * @since 09/10/2017
 * @see Creacion
 */
@Service
public class CheckResultServiceEnterpriseNT implements CheckResultService
{

    @Autowired
    private OrderListDao dao;
    @Autowired
    private DemographicService serviceDemographic;
    @Autowired
    private ConfigurationDao daoConfig;
    @Autowired
    private ResultsService serviceResult;
    @Autowired
    OrderSearchService orderSearchService;
    @Autowired
    private InterviewDao surveyDao;
    @Autowired
    private LaboratorysByBranchesService laboratorysByBranchesService;
    @Autowired
    private HttpServletRequest request;


    @Override
    public List<OrderList> listPending(Filter search) throws Exception
    {
        getPendingStates(search);

        int sistemaCentralListados = 0;
        boolean account = daoConfig.get("ManejoCliente").getValue().equalsIgnoreCase("true");
        boolean physician = daoConfig.get("ManejoMedico").getValue().equalsIgnoreCase("true");
        boolean rate = daoConfig.get("ManejoTarifa").getValue().equalsIgnoreCase("true");
        boolean service = daoConfig.get("ManejoServicio").getValue().equalsIgnoreCase("true");
        boolean race = daoConfig.get("ManejoRaza").getValue().equalsIgnoreCase("true");
        boolean documenttype = daoConfig.get("ManejoTipoDocumento").getValue().equalsIgnoreCase("true");
        boolean consultarSistemaCentral = daoConfig.get("ConsultarSistemaCentral").getValue().equalsIgnoreCase("true");
        if (consultarSistemaCentral)
        {
            sistemaCentralListados = Integer.parseInt(daoConfig.get("SistemaCentralListados").getValue());
        }
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        List<OrderList> list = dao.listBasic(search, serviceDemographic.list(true), account, physician, rate, service, race, documenttype, consultarSistemaCentral, sistemaCentralListados, false, laboratorys, idbranch);
        List<OrderList> filters = list.stream()
                .filter(filter -> search.getDemographics().isEmpty() || StreamFilters.containsDemographicList(filter, search.getDemographics()))
                .map((OrderList t) -> t.setTests(filterExcludetestbyprofilePending(t.getTests(), search.isGroupProfiles())))
                .filter(filter -> search.isBasic() || !filter.getTests().isEmpty())
                .collect(Collectors.toList());
        return filters;
    }

    /**
     * Filtra examenes por laboratorio y establece laboratorio origen
     *
     * @param filter Orden a la cual se le realizara el filtro
     * @param search lista laboratorio a filtrar
     *
     * @return Lista de examenes filtrados
     */
    private List<TestList> filterExcludetestbyprofilePending(List<TestList> filter, boolean isGroupProfiles)
    {
        try
        {
            if (isGroupProfiles)
            {

                List<TestList> profiles = new LinkedList<>();
                profiles = filter.stream().filter(t -> t.getTestType() != 0).collect(Collectors.toList());
                Set<Integer> filteredProfile = profiles.stream().map(TestList::getId).collect(Collectors.toSet());

                if (filteredProfile.size() > 0)
                {
                    filteredProfile.forEach(t ->
                    {
                        List<TestList> filteredTests = filter.stream()
                                .filter(p -> !(p.getResult().getState() == 4 && !p.getResult().isPrint()) && p.getResult().getSampleState() != 1)
                                .filter(c -> java.util.Objects.equals(c.getProfile(), t))
                                .collect(Collectors.toList());

                        if (filteredTests.isEmpty())
                        {
                            filter.removeIf(test -> java.util.Objects.equals(test.getId(), t));
                        }
                    });
                }

                List<TestList> filteredTests = filter.stream()
                        .filter(t -> !(t.getResult().getState() == 4 && !t.getResult().isPrint()) && t.getResult().getSampleState() != 1)
                        .filter(c -> !filteredProfile.contains(c.getProfile()))
                        .map(test ->
                        {
                            if (test.getTestType() == 1)
                            {
                                int min = filter.stream()
                                        .filter(t -> Objects.equals(t.getProfile(), test.getId()))
                                        .mapToInt(i -> i.getResult().getState()).min().orElseThrow(NoSuchElementException::new);
                                test.getResult().setState(min);
                            }

                            return test;
                        })
                        .collect(Collectors.toList());

                return filteredTests;

            } else
            {
                return filter;
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Filtra examenes por laboratorio y establece laboratorio origen
     *
     * @param filter Orden a la cual se le realizara el filtro
     * @param search lista laboratorio a filtrar
     *
     * @return Lista de examenes filtrados
     */
    private List<TestList> filterExcludetestbyprofilelist(List<TestList> filter, boolean isGroupProfiles)
    {
        try
        {
            if (isGroupProfiles)
            {

                List<TestList> profiles = new LinkedList<>();
                profiles = filter.stream().filter(t -> t.getTestType() != 0).collect(Collectors.toList());
                Set<Integer> filteredProfile = profiles.stream().map(TestList::getId).collect(Collectors.toSet());

                if (filteredProfile.size() > 0)
                {
                    filteredProfile.forEach(t ->
                    {
                        List<TestList> filteredTests = filter.stream()
                                .filter(c -> java.util.Objects.equals(c.getProfile(), t))
                                .collect(Collectors.toList());

                        if (filteredTests.isEmpty())
                        {
                            filter.removeIf(test -> java.util.Objects.equals(test.getId(), t));
                        }
                    });
                }

                List<TestList> filteredTests = filter.stream()
                        .filter(c -> !filteredProfile.contains(c.getProfile()))
                        .map(test ->
                        {
                            if (test.getTestType() == 1)
                            {
                                int min = filter.stream().filter(t -> Objects.equals(t.getProfile(), test.getId())).mapToInt(i -> i.getResult().getState()).min().orElseThrow(NoSuchElementException::new);
                                test.getResult().setState(min);
                            }

                            return test;
                        })
                        .collect(Collectors.toList());

                return filteredTests;

            } else
            {
                return filter;
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<OrderList> list(Filter search) throws Exception
    {
        getStatesManagement(search);
        boolean account = daoConfig.get("ManejoCliente").getValue().equalsIgnoreCase("true");
        boolean physician = daoConfig.get("ManejoMedico").getValue().equalsIgnoreCase("true");
        boolean rate = daoConfig.get("ManejoTarifa").getValue().equalsIgnoreCase("true");
        boolean service = daoConfig.get("ManejoServicio").getValue().equalsIgnoreCase("true");
        boolean race = daoConfig.get("ManejoRaza").getValue().equalsIgnoreCase("true");
        boolean documenttype = daoConfig.get("ManejoTipoDocumento").getValue().equalsIgnoreCase("true");
        boolean consultarSistemaCentral = daoConfig.get("ConsultarSistemaCentral").getValue().equalsIgnoreCase("true");
        int sistemaCentralListados = 0;
        if (consultarSistemaCentral)
        {
            sistemaCentralListados = Integer.parseInt(daoConfig.get("SistemaCentralListados").getValue());
        }
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        
        List<OrderList> list = dao.listBasic(search, serviceDemographic.list(true), account, physician, rate, service, race, documenttype, consultarSistemaCentral, sistemaCentralListados, true, laboratorys, idbranch);
        List<OrderList> filters = list.stream()
                .filter(filter -> search.getDemographics().isEmpty() || StreamFilters.containsDemographicList(filter, search.getDemographics()))
                .map((OrderList t) -> t.setTests(filterExcludetestbyprofilelist(t.getTests(), search.isGroupProfiles())))
                .filter(filter -> search.isBasic() || !filter.getTests().isEmpty())
                .collect(Collectors.toList());
        return filters;
    }

    /**
     * Obtiene examenes de una orden
     *
     * @param order numero de la orden
     *
     * @return lista de examenes con información del resultado
     */
    private List<ResultTest> getTestResults(List<Long> orders)
    {
        try
        {

            return serviceResult.getTests(orders, 0);
        } catch (Exception ex)
        {
            Log.error(CheckResultServiceEnterpriseNT.class, ex);
        }
        return new ArrayList<>();
    }

    /**
     * Filtra examenes de la orden
     *
     * @param order numero de orden
     * @param allTests lista de examenes de consulta
     *
     * @return examenes que corresponden a la orden
     */
    private List<ResultTest> getOrderTests(long order, List<ResultTest> allTests)
    {
        return allTests = allTests.stream()
                .filter(test -> test.getOrder() == order)
                .map(test -> setCurrentState(test))
                .collect(Collectors.toList());
    }

    /**
     * Establece el estado actual
     *
     * @param test examen con estados de muestra y resultado
     *
     * @return Examen con el estado actual.
     */
    private ResultTest setCurrentState(ResultTest test)
    {
        if (test.getState() == LISEnum.ResultTestState.DELIVERED.getValue())
        {
            test.setCurrentState(LISEnum.ResultTestCommonState.PRINTED.getValue());
        } else if (test.getState() == LISEnum.ResultTestState.VALIDATED.getValue())
        {
            test.setCurrentState(LISEnum.ResultTestCommonState.VALIDATED.getValue());
        } else if (test.getState() == LISEnum.ResultTestState.REPORTED.getValue())
        {
            test.setCurrentState(LISEnum.ResultTestCommonState.REPORTED.getValue());
        } else if (test.getSampleState() == LISEnum.ResultSampleState.CHECKED.getValue())
        {
            test.setCurrentState(LISEnum.ResultTestCommonState.CHECKED.getValue());
        } else if (test.getSampleState() == LISEnum.ResultSampleState.COLLECTED.getValue())
        {
            test.setCurrentState(LISEnum.ResultTestCommonState.TAKED.getValue());
        } else
        {
            test.setCurrentState(LISEnum.ResultTestCommonState.ORDERED.getValue());
        }
        return test;
    }

    /**
     * Indica si el examen cumple con los estados pendientes enviados
     *
     * @param test Examen a evaluar
     * @param filterStates lista de estados
     *
     * @return true si cumple con todos los estados
     */
    private Filter getPendingStates(Filter filter)
    {
        filter.setSampleState(new ArrayList<>());
        filter.setTestState(new ArrayList<>());
        for (Integer state : filter.getFilterState())
        {
            switch (state)
            {
                case 0:
                    filter.getSampleState().add(LISEnum.ResultSampleState.ORDERED.getValue());
                    filter.getSampleState().add(LISEnum.ResultSampleState.COLLECTED.getValue());
                    filter.getSampleState().add(LISEnum.ResultSampleState.CHECKED.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.ORDERED.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.RERUN.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.REPORTED.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.PREVIEW.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.VALIDATED.getValue());
                case 1://Pendientes de toma
                    filter.getSampleState().add(LISEnum.ResultSampleState.ORDERED.getValue());
                    break;
                case 2://Pendiente verificación
                    filter.getSampleState().add(LISEnum.ResultSampleState.ORDERED.getValue());
                    filter.getSampleState().add(LISEnum.ResultSampleState.COLLECTED.getValue());
                    filter.getSampleState().add(LISEnum.ResultSampleState.NEW_SAMPLE.getValue());
                    filter.getSampleState().add(LISEnum.ResultSampleState.PENDING.getValue());
                    filter.getSampleState().add(LISEnum.ResultSampleState.REJECTED.getValue());

                    break;
                case 3://Pendiente Resultado
                    filter.getSampleState().add(LISEnum.ResultSampleState.CHECKED.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.ORDERED.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.RERUN.getValue());
                    break;
                case 4://Pendiente Validación
                    filter.getTestState().add(LISEnum.ResultTestState.REPORTED.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.PREVIEW.getValue());
                    break;
                case 5://Pendiente Impresion
                    filter.getTestState().add(LISEnum.ResultTestState.VALIDATED.getValue());
                    break;

            }

        }
        return filter;
    }

    /**
     * Obtiene el estado actual del examen
     *
     * @param filter Filtros
     *
     * @return Filtro con estados correspondientes al examen
     */
    private Filter getCurrentStates(Filter filter)
    {
        filter.setSampleState(new ArrayList<>());
        filter.setTestState(new ArrayList<>());
        filter.setFilterState(filter.getFilterState() == null ? new ArrayList<>() : filter.getFilterState());
        for (Integer state : filter.getFilterState())
        {
            switch (state)
            {
                case 1:
                    filter.getSampleState().add(LISEnum.ResultSampleState.COLLECTED.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.ORDERED.getValue());
                    break;
                case 2:
                    filter.getSampleState().add(LISEnum.ResultSampleState.CHECKED.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.ORDERED.getValue());
                    break;
                case 3:
                    filter.getTestState().add(LISEnum.ResultTestState.REPORTED.getValue());
                    break;
                case 4:
                    filter.getTestState().add(LISEnum.ResultTestState.VALIDATED.getValue());
                    break;
                case 5:
                    filter.getTestState().add(LISEnum.ResultTestState.DELIVERED.getValue());
                    break;

            }

        }
        return filter;
    }

    /**
     * Indica si el examen cumple con los estados enviados
     *
     * @param test Examen a evaluar
     * @param filterStates lista de estados
     *
     * @return true si cumple con todos los estados
     */
    private boolean matchStates(ResultTest test, List<Integer> filterStates)
    {
        filterStates = filterStates == null ? new ArrayList<>() : filterStates;
        return filterStates.isEmpty() || filterStates.contains(test.getState());
    }

    /**
     * Indica si el examen cumple con los estados de la muestra
     *
     * @param test Examen a evaluar
     * @param filterSampleStates lista de estados
     *
     * @return true si cumple con todos los estados
     */
    private boolean matchSampleStates(ResultTest test, List<Integer> filterSampleStates)
    {
        filterSampleStates = filterSampleStates == null ? new ArrayList<>() : filterSampleStates;
        return filterSampleStates.isEmpty() || filterSampleStates.contains(test.getSampleState());
    }

    /**
     * Valida si el resultado cumple con los estados enviados
     *
     * @param test examen
     * @param resultStates lista de estados (1 - Modificado, 2 - Repetición, 3 -
     * Patologíco, 4 - Pánico, 5 - Delta Check )
     *
     * @return true si cumple con filtros
     */
    private boolean matchResult(ResultTest test, List<Integer> resultStates)
    {
        resultStates = resultStates == null ? new ArrayList<>() : resultStates;
        if (!resultStates.isEmpty())
        {
            return resultStates.stream()
                    .allMatch(state -> state == 1 ? test.getModificationAmmount() > 0
                    : state == 2 ? test.getRepeatAmmount() > 0
                            : state == 3 ? Arrays.asList(1, 2, 3).contains(test.getPathology())
                                    : state == 4 ? Arrays.asList(4, 5, 6).contains(test.getPathology())
                                            : false);
        } else
        {
            return true;
        }

    }

    /**
     * Filtra examenes por area, examen o confidenciales
     *
     * @param filter Orden con examenes a filtrar
     * @param filter lista de examenes filtrados
     *
     * @return true si cumple con los filtros
     */
    private boolean filterTests(ResultTest tests, Filter filter)
    {

        switch (filter.getTestFilterType())
        {
            case 1:
                if (!filter.getTests().contains(tests.getAreaId()))
                {
                    return false;
                }
                break;
            case 2:
            case 3:
                if (!filter.getTests().contains(tests.getTestId()))
                {
                    return false;
                }
                break;
            default:

                break;
        }

//        return filter.getTestFilterType() == 3 ? tests.isConfidential() : !tests.isConfidential();
        return true;
    }

    @Override
    public List<ResultTest> listByRecord(String record, int document, int test) throws Exception
    {

        OrderSearchFilter filter = new OrderSearchFilter();
        filter.setRecord(record);
        filter.setDocumentType(document);

        List<OrderList> orders = orderSearchService.getOrdersbyPatient(filter);

        if (!orders.isEmpty())
        {
            List<Long> orderslist = orders.stream().map(OrderList::getOrderNumber).collect(Collectors.toList());
            List<ResultTest> resultTests = serviceResult.getTests(orderslist, test)
                    .stream()
                    .filter(result -> Objects.equals(result.getTestId(), test) && result.getPatient().getPatientId().equals(record))
                    .map(result -> setCurrentState(result))
                    .collect(Collectors.toList());

            return resultTests; //serviceResult.addTestAdditional(resultTests);
        }

        return new ArrayList<>();

    }

    @Override
    public List<PanicInterview> getPanicInterview(Filter filter) throws Exception
    {
        getCurrentStates(filter);

        boolean account = daoConfig.get("ManejoCliente").getValue().equalsIgnoreCase("true");
        boolean physician = daoConfig.get("ManejoMedico").getValue().equalsIgnoreCase("true");
        boolean rate = daoConfig.get("ManejoTarifa").getValue().equalsIgnoreCase("true");
        boolean service = daoConfig.get("ManejoServicio").getValue().equalsIgnoreCase("true");
        boolean race = daoConfig.get("ManejoRaza").getValue().equalsIgnoreCase("true");
        boolean documenttype = daoConfig.get("ManejoTipoDocumento").getValue().equalsIgnoreCase("true");

        List<Long> orders = dao.getOrdersbyFilter(filter, serviceDemographic.list(true), account, physician, rate, service, race, documenttype);

        if (orders.size() > 0)
        {
            if (filter.getAttended() == 1)
            {
                return dao.getPanicInterview(orders);
            } else
            {
                List<Interview> surveys = surveyDao.list().stream().filter(x -> x.isPanic() && x.isState()).collect(Collectors.toList());
                if (surveys.size() > 0)
                {
                    return dao.getPanicUnattended(orders, surveys.get(0));
                } else
                {
                    return null;
                }
            }
        } else
        {
            return null;
        }
    }

    @Override
    public List<ResultTest> getCriticalValues(Filter filter) throws Exception
    {
        filter.setFilterState(new ArrayList(Arrays.asList(4)));
        getCurrentStates(filter);
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        
        List<Order> orders = dao.list(filter.getInit(), filter.getEnd(), filter.getRangeType(), serviceDemographic.list(true), null, filter.getTestFilterType() == 2 ? filter.getTests() : null, 0, 0, laboratorys, idbranch)
                .stream()
                .filter(order -> order.getState() != LISEnum.ResultOrderState.CANCELED.getValue())
                .filter(order -> order.getPatient().getId() != 0)
                .filter(order -> filter.getOrderType() == 0 || order.getType().getId().equals(filter.getOrderType()))
                .filter(order -> StreamFilters.containsDemographic(order, filter.getDemographics()))
                .collect(Collectors.toList());

        if (orders.size() > 0)
        {
            return getTestResults(orders.stream().map(order -> order.getOrderNumber()).collect(Collectors.toList()))
                    .stream()
                    .filter(test -> matchStates(test, filter.getTestState()))
                    .filter(test -> test.getSampleState() == LISEnum.ResultSampleState.CHECKED.getValue())
                    .filter(test -> test.getPathology() >= LISEnum.ResultTestPathology.CRITICAL.getValue())
                    .filter(test -> filterTests(test, filter))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    
    /**
     * Obtiene el estado actual del examen
     *
     * @param filter Filtros
     *
     * @return Filtro con estados correspondientes al examen
     */
    private Filter getStatesManagement(Filter filter)
    {
        filter.setSampleState(new ArrayList<>());
        filter.setTestState(new ArrayList<>());
        filter.setFilterState(filter.getFilterState() == null ? new ArrayList<>() : filter.getFilterState());
        for (Integer state : filter.getFilterState())
        {
            switch (state)
            {
                case 0:
                    filter.getSampleState().add(LISEnum.ResultSampleState.ORDERED.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.ORDERED.getValue());
                    break;
                case 1:
                    filter.getSampleState().add(LISEnum.ResultSampleState.ORDERED.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.RERUN.getValue());
                    break;
                case 2:
                    filter.getSampleState().add(LISEnum.ResultSampleState.CHECKED.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.REPORTED.getValue());
                    break;
                case 3:
                    filter.getSampleState().add(LISEnum.ResultSampleState.CHECKED.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.PREVIEW.getValue());
                    break;
                case 4:
                    filter.getSampleState().add(LISEnum.ResultSampleState.CHECKED.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.VALIDATED.getValue());
                    break;
                case 5:
                    filter.getSampleState().add(LISEnum.ResultSampleState.CHECKED.getValue());
                    filter.getTestState().add(LISEnum.ResultTestState.DELIVERED.getValue());
                    break;
            }

        }
        return filter;
    }
}

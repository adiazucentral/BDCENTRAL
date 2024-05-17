package net.cltech.enterprisent.service.impl.enterprisent.operation.orders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.domain.operation.filters.OrderSearchFilter;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.service.impl.enterprisent.operation.results.CheckResultServiceEnterpriseNT;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderSearchService;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Log;
import net.cltech.enterprisent.tools.SQLTools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de servicios de busqueda de ordenes para Enterprise NT
 *
 * @version 1.0.0
 * @author eacuna
 * @since 29/11/2017
 * @see Creacion
 */
@Service
public class OrderSearchServiceEnterpriseNT implements OrderSearchService {

    @Autowired
    private PatientService patientService;
    @Autowired
    private OrdersDao orderDao;
    @Autowired
    private ResultsService serviceResult;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private LaboratorysByBranchesService laboratorysByBranchesService;
    @Autowired
    private HttpServletRequest request;

    

    @Override
    public List<Patient> listByRecord(String record, int documentType, int init, int end) throws Exception {
        Patient patient = patientService.get(record, documentType, 0);
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        if (patient != null) {
            OrderSearchFilter filter = new OrderSearchFilter(OrderSearchFilter.RANGE_TYPE_DATE, (long) init, (long) end);
            List<Object> params = new ArrayList<>();
            String where = buildSqlFilter(filter, params);
            int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
            return groupOrdersByPatient(orderDao.list(where, params, null, patient.getId(), null, yearsQuery, laboratorys, idbranch));
        }
        return new ArrayList<>();

    }

    @Override
    public List<Patient> listByLastName(String name, String name1, String lastname, String surname, int gender, int init, int end) throws Exception {
        List<Patient> patients = patientService.listByLastName(name,name1,lastname, surname, gender, null);
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);
        if (!patients.isEmpty()) {
            OrderSearchFilter filter = new OrderSearchFilter(OrderSearchFilter.RANGE_TYPE_DATE, (long) init, (long) end);
            List<Object> params = new ArrayList<>();
            String where = buildSqlFilter(filter, params);
            String patientIds = patients.stream().map(patient -> patient.getId().toString()).collect(Collectors.joining(","));
            int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
            return groupOrdersByPatient(orderDao.list(where, params, null, null, patientIds, yearsQuery, laboratorys, idbranch));
        }
        return new ArrayList<>();
    }

    @Override
    public List<Patient> listByDates(int init, int end) throws Exception {
        OrderSearchFilter filter = new OrderSearchFilter(OrderSearchFilter.RANGE_TYPE_DATE, (long) init, (long) end);
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        List<Object> params = new ArrayList<>();
        String where = buildSqlFilter(filter, params);
        int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
        return groupOrdersByPatient(orderDao.list(where, params, null, null, null, yearsQuery, laboratorys, idbranch));
    }

    @Override
    public List<Patient> listByOrder(long order) throws Exception {
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
        return groupOrdersByPatient(orderDao.list(null, null, order, null, null, yearsQuery, laboratorys, idbranch));
    }

    /**
     * Agrupa ordenes por paciente
     *
     * @param orders lista de ordenes
     *
     * @return Lista de pacientes
     */
    private List<Patient> groupOrdersByPatient(List<Order> orders) {
        List<Patient> patients = new ArrayList<>();
        Map<Patient, ArrayList<Order>> ordersByPatient
                = orders.stream()
                        .collect(groupingBy(Order::getPatient,
                                toCollection(ArrayList::new)));

        for (Entry<Patient, ArrayList<Order>> entry : ordersByPatient.entrySet()) {
            List<Order> ordersPatient = entry.getValue().stream().map(order -> order.setPatient(null)).collect(Collectors.toList());
            patients.add(entry.getKey().setOrders(ordersPatient));
        }
        return patients;
    }

    @Override
    public List<Order> listByFilter(OrderSearchFilter filter) throws Exception {
        List<Object> params = new ArrayList<>();
        String where = buildSqlFilter(filter, params);
        Patient patient = null;
        String patientIds = null;
        if (filter.getRecord() != null) {
            patient = patientService.get(filter.getRecord(), filter.getDocumentType() == null ? 0 : filter.getDocumentType(), 0);
            if (patient == null) {
                return new ArrayList<>();
            }
        }

        if (filter.getLastName() != null && filter.getGender() != null) {
            List<Patient> patients = patientService.listByLastName(null, null,filter.getLastName(), null, filter.getGender(), null);
            if (patients.isEmpty()) {
                return new ArrayList<>();
            }
            patientIds = patients.stream().map(pat -> pat.getId().toString()).collect(Collectors.joining(","));

        }

        int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        List<Order> orders = orderDao.list(where, params, null, patient == null ? null : patient.getId(), patientIds == null ? null : patientIds, yearsQuery, laboratorys, idbranch);
        final List<ResultTest> tests = getTestResults(orders)
                .stream()
                .filter(test -> filter.getSections() == null || filter.getSections().isEmpty() || filter.getSections().contains(test.getAreaId()))
                .collect(Collectors.toList());

        return orders.stream()
                .map(order -> order.setResultTest(getOrderTests(order.getOrderNumber(), tests)))
                .filter(order -> !order.getResultTest().isEmpty())
                .collect(Collectors.toList());

    }

    @Override
    public List<OrderList> getOrdersbyPatient(OrderSearchFilter filter) throws Exception {
        List<Object> params = new ArrayList<>();
        String where = buildSqlFilter(filter, params);
        int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        List<OrderList> orders = orderDao.getOrdersbyPatient(filter, where, params, yearsQuery, laboratorys, idbranch);

        if (filter.getLastName() != null) {
            orders = orders.stream()
                    .filter(order -> order.getPatient().getLastName() == null || order.getPatient().getLastName().trim().isEmpty() || order.getPatient().getLastName().toLowerCase().startsWith(filter.getLastName().toLowerCase()))
                    .collect(Collectors.toList());
        }

        return orders;

    }

    @Override
    public List<OrderList> getOrdersbyPatientStorage(OrderSearchFilter filter) throws Exception {
        List<Object> params = new ArrayList<>();
        String where = buildSqlFilter(filter, params);
        int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
        List<OrderList> orders = orderDao.getOrdersbyPatientStorage(filter, where, params, yearsQuery);

        if (filter.getLastName() != null) {
            orders = orders.stream()
                    .filter(order -> order.getPatient().getLastName() == null || order.getPatient().getLastName().trim().isEmpty() || order.getPatient().getLastName().toLowerCase().startsWith(filter.getLastName().toLowerCase()))
                    .collect(Collectors.toList());
        }

        return orders;

    }

    @Override
    public List<Order> ordersWithPatient(OrderSearchFilter filter) throws Exception {
        List<Object> params = new ArrayList<>();
        if (filter.getRangeType().equals(1)) {
            String where = buildSqlFilter(filter, params);
            
            int idbranch = JWT.decode(request).getBranch();
            List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);
            
            return orderDao.ordersWithPatient(where, params, filter,laboratorys, idbranch );
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
    private List<ResultTest> getOrderTests(long order, List<ResultTest> allTests) {
        return allTests.stream()
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
    private ResultTest setCurrentState(ResultTest test) {
        if (test.getState() == LISEnum.ResultTestState.DELIVERED.getValue()) {
            test.setCurrentState(LISEnum.ResultTestCommonState.PRINTED.getValue());
        } else if (test.getState() == LISEnum.ResultTestState.VALIDATED.getValue()) {
            test.setCurrentState(LISEnum.ResultTestCommonState.VALIDATED.getValue());
        } else if (test.getState() == LISEnum.ResultTestState.REPORTED.getValue()) {
            test.setCurrentState(LISEnum.ResultTestCommonState.REPORTED.getValue());
        } else if (test.getSampleState() == LISEnum.ResultSampleState.CHECKED.getValue()) {
            test.setCurrentState(LISEnum.ResultTestCommonState.CHECKED.getValue());
        } else if (test.getSampleState() == LISEnum.ResultSampleState.COLLECTED.getValue()) {
            test.setCurrentState(LISEnum.ResultTestCommonState.TAKED.getValue());
        } else {
            test.setCurrentState(LISEnum.ResultTestCommonState.ORDERED.getValue());
        }
        return test;
    }

    /**
     * Construir el filtro SQL
     *
     * @param filter Filtros de la orden y el examen
     * @param params Parametros
     *
     * @return Where de la consulta SQL
     */
    private String buildSqlFilter(OrderSearchFilter filter, List<Object> params) {
        String where = "";

        //Filtro por rangos
        if (Objects.equals(filter.getRangeType(), OrderSearchFilter.RANGE_TYPE_DATE)) {
            if (Objects.nonNull(filter.getEnd()) && filter.getEnd() != 0) {
                where += " AND lab22c2 BETWEEN  ? and ? ";
                params.add(filter.getInit());
                params.add(filter.getEnd());

            } else if (filter.getInit() != 0) {
                where += " AND lab22c2 = ? ";
                params.add(filter.getInit());
            }
        } else if (Objects.equals(filter.getRangeType(), OrderSearchFilter.RANGE_TYPE_ORDER)) {
            where += " AND lab22c1 BETWEEN  ? and ? ";
            params.add(filter.getInit());
            params.add(filter.getEnd());
        }

        if (filter.getDemographics() != null && !filter.getDemographics().isEmpty()) {
            for (FilterDemographic demographic : filter.getDemographics()) {
                where += SQLTools.buildSQLDemographicFilter(demographic, params);
            }
        }
        return where;

    }

    /**
     * Obtiene examenes de una orden
     *
     * @param order numero de la orden
     *
     * @return lista de examenes con información del resultado
     */
    private List<ResultTest> getTestResults(List<Order> orders) {
        try {
            List<Long> orderNumbers = orders
                    .stream()
                    .map(Order::getOrderNumber)
                    .collect(Collectors.toList());
            return serviceResult.getTests(orderNumbers, 0);
        } catch (Exception ex) {
            Log.error(CheckResultServiceEnterpriseNT.class, ex);
        }
        return new ArrayList<>();
    }

}

package net.cltech.enterprisent.service.impl.enterprisent.operation.orders;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.operation.list.OrderListDao;
import net.cltech.enterprisent.dao.interfaces.operation.microbiology.MicrobiologyDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.SpecialDeleteDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultTestDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardOpportunityTime;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.common.Reason;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderBasic;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.results.ResultTestRepetition;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationDashBoardService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationIngresoService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMiddlewareService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.orders.SpecialDeleteService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.AgileStatisticService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.StatisticService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de los borrados especiales para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @since 25/10/2017
 * @see Creacion
 */
@Service
public class SpecialDeleteServiceEnterpriseNT implements SpecialDeleteService {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SpecialDeleteDao dao;
    @Autowired
    private OrdersDao orderDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderListDao listDao;
    @Autowired
    private ResultsService serviceResult;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private AgileStatisticService agileStatisticService;
    @Autowired
    private IntegrationDashBoardService dashBoardService;
    @Autowired
    private IntegrationIngresoService serviceIngreso;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private IntegrationMiddlewareService integrationMiddlewareService;
    @Autowired
    private MicrobiologyDao microbiologyDao;
    @Autowired
    private ResultTestDao resultTestDao;

    @Override
    public List<Order> specialDelete(Reason reason) throws Exception {
        AuthorizedUser user = JWT.decode(request);
        Timestamp timestamp = new Timestamp(new Date().getTime());

        List<Order> orders = reason.getDeleteType() == 1 ? listDao.listdeletedorder(reason.getInit(), reason.getEnd()) : listDao.listdeleted(reason.getInit(), reason.getEnd());
        List<String> errors = validateMotive(reason);

        if (errors.isEmpty()) {
            for (Order order : orders) {
                order.setReason(reason);
                order.getReason().setUser(user);
                order.getReason().setDeleteDate(timestamp);
            }

            switch (reason.getDeleteType()) {
                case 1:
                    orders = orders.stream()                           
                            .collect(Collectors.toList());

                    if (!orders.isEmpty()) {
                        orders = orderDao.updateOrderState(orders, LISEnum.ResultOrderState.CANCELED);
                        //Estadisticas
                        final List<Long> orderList = orders.stream().map(Order::getOrderNumber).collect(Collectors.toList());
                        CompletableFuture.runAsync(() -> statisticService.disableOrders(orderList, LISEnum.ResultOrderState.CANCELED));

                        List<AuditOperation> audit = new ArrayList<>();
                        for (Long orderNumber : orderList) {
                            Order order = orderService.get(orderNumber).clean();

                            audit.add(new AuditOperation(order.getOrderNumber(), null, null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_ORDER, "", reason.getMotive().getId(), reason.getComment(), null, null));
                            agileStatisticService.updateOrderBranch(order.getOrderNumber(), false);
                            dashBoardService.dashBoardOpportunityTime(orderNumber, null, DashBoardOpportunityTime.ACTION_DELETE);

                            CompletableFuture.runAsync(()
                                    -> {
                                try {
                                    String idTest = "";
                                    for (ResultTest test : order.getResultTest()) {
                                        agileStatisticService.updateTestBranch(order.getOrderNumber(), test.getTestId(), false, null);
                                        idTest += test.getTestId() + "|";
                                    }

                                    idTest = idTest.substring(0, idTest.length() - 1);
                                    //Check-IN Segment
                                    serviceIngreso.getMessageTest(order.getOrderNumber(), "elimination", null, idTest, order.getResultTest());
                                } catch (Exception ex) {
                                    IntegrationHisLog.info("ERROR ELIMINACION DE EXAMENES " + ex);
                                }

                            });
                        }
                        trackingService.registerOperationTracking(audit);
                    }
                    break;
                case 2:
                    orders = orders.stream()                          
                            .map((Order t) -> t.setTests(filterOrderTests(t, reason)))
                            .filter(order -> !order.getTests().isEmpty())
                            .collect(Collectors.toList());

                    if (!orders.isEmpty()) {
                        List<AuditOperation> audit = new ArrayList<>();
                        for (Order order : orders) {
                            for (Test test : order.getTests()) {
                                ResultTest resultTest = new ResultTest();
                                resultTest.setOrder(order.getOrderNumber());
                                resultTest.setTestId(test.getId());
                                resultTest.setNewState(LISEnum.ResultTestState.RERUN.getValue());
                                resultTest.setResultDate(test.getResult().getDateResult());
                                resultTest.setUserId(user.getId());
                                ResultTestRepetition resultRepetition = new ResultTestRepetition();
                                resultRepetition.setType("R".charAt(0));
                                resultRepetition.setReasonId(0);
                                resultRepetition.setReasonComment("");
                                resultTest.setResultRepetition(resultRepetition);

                                serviceResult.reportedTest(resultTest);
                                //Trazabilidad del examen
                                //Order o = orderService.getAudit(order.getOrderNumber()).clean();
                                audit.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_ORDER, Tools.jsonObject(LISEnum.ResultTestState.RERUN.getValue()), reason.getMotive().getId(), reason.getComment(), null, null));
                            }

                        }
                        trackingService.registerOperationTracking(audit);
                    }
                    break;
                case 3:
                    orders = orders.stream()
                            .map((Order t) -> t.setTests(filterOrderTests(t, reason)))
                            .filter(order -> !order.getTests().isEmpty())
                            .collect(Collectors.toList());
                    if (!orders.isEmpty()) {
                        for (Order order : orders) {
                            dashBoardService.dashBoardOpportunityTime(order.getOrderNumber(), order.getTests().stream().map(test -> test.getId()).collect(Collectors.toList()), DashBoardOpportunityTime.ACTION_DELETE);
                        }
                        dao.deleteTest(orders);
                        
                        List<AuditOperation> audit = new ArrayList<>();
                        for (Order order : orders) {
                            Order o = orderService.getAudit(order.getOrderNumber()).clean();
                            audit.add(new AuditOperation(o.getOrderNumber(), null, null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_ORDER, "", reason.getMotive().getId(), reason.getComment(), null, null));
                        }
                        trackingService.registerOperationTracking(audit);
                    }
                      
                    break;
                case 4:
                case 5:
                    for (Order order : orders) {
                        if (validateOrderTests(order, reason)) {
                            errors.add("3|tests|" + order.getOrderNumber());
                        }
                    }

                    if (errors.isEmpty()) {
                        orders = orders.stream()                              
                                .map((Order t) -> t.setTests(filterOrderTests(t, reason)))
                                .filter(order -> !order.getTests().isEmpty())
                                .collect(Collectors.toList());

                        if (!orders.isEmpty()) {
                            for (Order order : orders) {
                                for (Test test : order.getTests()) {
                                    agileStatisticService.updateTestBranch(order.getOrderNumber(), test.getId(), false, null);
                                }

                                dashBoardService.dashBoardOpportunityTime(order.getOrderNumber(), order.getTests().stream().map(test -> test.getId()).collect(Collectors.toList()), DashBoardOpportunityTime.ACTION_DELETE);
                            }
                            dao.deleteTest(orders);

                            List<AuditOperation> audit = new ArrayList<>();
                            for (Order order : orders) {
                                Order o = orderService.getAudit(order.getOrderNumber()).clean();
                                audit.add(new AuditOperation(o.getOrderNumber(), null, null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_ORDER, "", reason.getMotive().getId(), reason.getComment(), null, null));
                            }
                            trackingService.registerOperationTracking(audit);
                        }
                    } else {
                        throw new EnterpriseNTException(errors);
                    }
                    break;
            }
            return orders;
        } else {
            throw new EnterpriseNTException(errors);
        }
    }

    /**
     * Filtra examenes de acuerdo a las opciones enviadas
     *
     * @param order Orden con examenes a filtrar
     * @param search lista de examenes filtrados
     *
     * @return
     */
    private List<Test> filterOrderTests(Order order, Reason reason) {
        List<Integer> tests = reason.getTests().stream().map(test -> test.getId()).collect(Collectors.toList());
        List<Test> filteredTests;
        switch (reason.getDeleteType()) {
            case 2:
                filteredTests = order.getTests().stream()
                        .filter(test -> test.getTestType() == 0)
                        .filter(test -> test.getResult().getState() >= LISEnum.ResultTestState.REPORTED.getValue())
                        .filter(test -> tests.contains(test.getId()))
                        .collect(Collectors.toList());
                break;
            case 3:
                filteredTests = order.getTests().stream()
                        .filter(test -> test.getTestType() == 0)
                        .filter(test -> tests.contains(test.getId()))
                        .collect(Collectors.toList());
                break;
            case 4:
                filteredTests = order.getTests().stream()
                        .filter(test -> test.getTestType() == 0)
                        .filter(test -> tests.contains(test.getId()))
                        .filter(test -> test.getResult().getState() < LISEnum.ResultTestState.REPORTED.getValue())
                        .collect(Collectors.toList());
                break;
            case 5:
                filteredTests = order.getTests().stream()
                        .filter(test -> test.getTestType() == 0)
                        .filter(test -> tests.contains(test.getId()))
                        .filter(test -> test.getResult().getState() == LISEnum.ResultTestState.VALIDATED.getValue())
                        .collect(Collectors.toList());
                break;
            default:
                filteredTests = new ArrayList<>();
                break;
        }
        return filteredTests;
    }

    /**
     * Filtra examenes de acuerdo a las opciones enviadas
     *
     * @param order Orden con examenes a filtrar
     * @param search lista de examenes filtrados
     *
     * @return
     */
    private boolean validateOrderTests(Order order, Reason reason) {
        List<Integer> tests = order.getTests().stream().map(test -> test.getId()).collect(Collectors.toList());
        return reason.getTests().stream().map(test -> test.getId()).collect(Collectors.toList()).containsAll(tests);
    }

    /**
     * Filtra examenes de acuerdo a las opciones enviadas
     *
     * @param order Orden con examenes a filtrar
     * @param search lista de examenes filtrados
     *
     * @return
     */
    private List<String> validateMotive(Reason reason) {
        List<String> errors = new ArrayList<>();
        if (reason == null) {
            errors.add("0|reason");
            return errors;
        }

        if (reason.getMotive() == null || reason.getMotive().getId() == null || reason.getMotive().getId() == 0) {
            errors.add("0|motive");
        }
        return errors;
    }

    @Override
    public List<OrderBasic> specialDeleteTest(Reason reason) throws Exception {
       List<OrderBasic> orders = listDao.listdeleted(reason.getInit(), reason.getEnd(), reason.getDeleteType(), reason.getTestsList());
       return orders;
    }
    
    @Override
    public Integer specialDeletebyorder(Reason reason) throws Exception {
        AuthorizedUser user = JWT.decode(request);
        List<AuditOperation> audit = new ArrayList<>();
        switch (reason.getDeleteType()) {
            case 1:
                orderDao.updateOrderStateByOrder(reason.getOrderNumber(), LISEnum.ResultOrderState.CANCELED);
                statisticService.disableOrder(reason.getOrderNumber(), LISEnum.ResultOrderState.CANCELED);
                agileStatisticService.updateOrderBranch(reason.getOrderNumber(), false);
                if (configurationService.getValue("IntegracionDashBoard").equalsIgnoreCase("true"))
                {
                    dashBoardService.dashBoardOpportunityTime(reason.getOrderNumber(), null, DashBoardOpportunityTime.ACTION_DELETE);
                }
                audit.add(new AuditOperation(reason.getOrderNumber(), null, null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_ORDER, "", reason.getMotive().getId(), reason.getComment(), null, null));
                trackingService.registerOperationTracking(audit);
                break;
            case 2:
                for (Test test : reason.getTests()) {
                    ResultTest resultTest = new ResultTest();
                    resultTest.setOrder(reason.getOrderNumber());
                    resultTest.setTestId(test.getId());
                    resultTest.setNewState(LISEnum.ResultTestState.RERUN.getValue());
                    resultTest.setResultDate(test.getResult().getDateResult());
                    resultTest.setUserId(user.getId());
                    ResultTestRepetition resultRepetition = new ResultTestRepetition();
                    resultRepetition.setType("R".charAt(0));
                    resultRepetition.setReasonId(0);
                    resultRepetition.setReasonComment("");
                    resultTest.setResultRepetition(resultRepetition);
                    serviceResult.reportedTest(resultTest);
                    //Trazabilidad del examen
                    audit.add(new AuditOperation(reason.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_ORDER, Tools.jsonObject(LISEnum.ResultTestState.RERUN.getValue()), reason.getMotive().getId(), reason.getComment(), null, null));
                }
                trackingService.registerOperationTracking(audit);
                break;
            default: 
                dao.deleteTestbyOrder(reason);
                if (configurationService.getValue("IntegracionDashBoard").equalsIgnoreCase("true"))
                {
                    dashBoardService.dashBoardOpportunityTime(reason.getOrderNumber(), reason.getTests().stream().map(test -> test.getId()).collect(Collectors.toList()), DashBoardOpportunityTime.ACTION_DELETE);
                }
                integrationMiddlewareService.sendOrderASTM(reason.getOrderNumber(), new ArrayList<>(), null, Constants.ANY, null, null,  reason.getTests(), 1, false);
                for (Test test : reason.getTests()) {
                    audit.add(new AuditOperation(reason.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_TEST, "", null, null, null, null));
                    microbiologyDao.deleteResultTemplate(reason.getOrderNumber(), test.getId());

                    boolean deletecomment = resultTestDao.deleteResultComment(reason.getOrderNumber(), test.getId());
                    if (deletecomment)
                    {
                        audit.add(new AuditOperation(reason.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_RESULTCOMMENT, "", null, null, null, null));
                    }
                }
                trackingService.registerOperationTracking(audit);
                break;
        }
        return 1;
        
    }
}

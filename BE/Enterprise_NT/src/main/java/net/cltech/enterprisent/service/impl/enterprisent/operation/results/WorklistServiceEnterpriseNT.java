package net.cltech.enterprisent.service.impl.enterprisent.operation.results;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.operation.list.OrderListDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.WorklistDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.test.Worksheet;
import net.cltech.enterprisent.domain.operation.common.Common;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.results.worklist.WorklistFilter;
import net.cltech.enterprisent.domain.operation.results.worklist.WorklistResult;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
import net.cltech.enterprisent.service.interfaces.masters.test.WorksheetService;
import net.cltech.enterprisent.service.interfaces.operation.result.WorklistService;
import net.cltech.enterprisent.tools.JWT;
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
public class WorklistServiceEnterpriseNT implements WorklistService
{

    @Autowired
    private OrderListDao dao;
    @Autowired
    private WorklistDao daoWorklist;
    @Autowired
    private DemographicService serviceDemographic;
    @Autowired
    private WorksheetService serviceWorksheet;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ConfigurationService configurationServices;
    @Autowired
    private LaboratorysByBranchesService laboratorysByBranchesService;



    @Override
    public WorklistResult list(WorklistFilter filter) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        Integer workListId = filter.getWorkLists().get(0);
        Worksheet worksheet = serviceWorksheet.get(workListId, null);
        worksheet.setTests(worksheet.getTests().stream().filter(test -> test.isSelected()).collect(Collectors.toList()));

        List<Integer> worksheetTests = worksheet.getTests()
                .stream()
                .map(TestBasic::getId)
                .collect(Collectors.toList());
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        List<Order> worksheetOrders = dao.listw(filter.getInit(), filter.getEnd(), filter.getRangeType(), serviceDemographic.list(true), null, null, 0, 0, laboratorys, idbranch );
        worksheetOrders = worksheetOrders
                .stream()
                .filter(order -> order.getPatient().getId() != 0)
                .filter(order -> filter.getOrderType() == 0 || order.getType().getId() == filter.getOrderType())
                .map((Order t) -> t.setTests(filterTestsByWorksheet(t, filter, worksheetTests, worksheet.isExclusive())))
                .filter(order -> !order.getTests().isEmpty())
                .map(order -> order.setTests(getWorksheetTests(worksheet.getTests(), order.getTests())))
                .collect(Collectors.toList());
        if (!worksheetOrders.isEmpty())
        {
            Integer consecutive = daoWorklist.getConsecutive(workListId);
            
            List<Order> orders = worksheetOrders.stream().map( 
                    (Order o) -> o.setTests(filterTestDistinct(o)) 
            ).collect(Collectors.toList());
                    
            daoWorklist.insertTestGroup(orders, workListId, ++consecutive, user.getId());
            return new WorklistResult(consecutive, worksheetOrders);
        }
        return null;

    }

    @Override
    public WorklistResult previous(int worksheet, int group) throws Exception
    {
        Worksheet work = serviceWorksheet.get(worksheet, null);
        work.setTests(work.getTests().stream().filter(test -> test.isSelected()).collect(Collectors.toList()));
        int yearsQuery = Integer.parseInt(configurationServices.getValue("AniosConsultas"));
        List<Order> orders = daoWorklist.list(group, worksheet, serviceDemographic.list(true), yearsQuery);
        if (!orders.isEmpty())
        {
            orders = orders.stream().map(order -> order.setTests(getWorksheetTests(work.getTests(), order.getTests()))).collect(Collectors.toList());
            return new WorklistResult(group, orders);
        }
        return null;
    }

    @Override
    public int reset(int worksheet) throws Exception
    {
        return daoWorklist.deleteWorksheetSecuence(worksheet);
    }

    /**
     * Filtra examenes que correspondan a la hoja de trabajo y no se encuetren
     * en una ya realizada
     *
     * @param order          orden con examenes para filtrar
     * @param filter         filtro con opciones de filtros para examenes
     *                       (Pendientes de resultado)
     * @param worksheetTests Lista de examenes de la hoja de trabajo
     * @param excluding      filtro excluyente true -> Todos los examenes de la
     *                       hoja de trabajo deben estar en la orden
     * <b> false-> La orden contine algun examen de la hoja de trabajo
     *
     * @return Lista de examenes que corresponden a los filtros enviados
     */
    private List<Test> filterTestsByWorksheet(Order order, WorklistFilter filter, List<Integer> worksheetTests, boolean excluding)
    {

        List<Test> testToCompare = order.getTests().stream()
                .filter(test -> !filter.isResultPending() || test.getResult().getState() <= LISEnum.ResultTestState.REPORTED.getValue())//pendientes por resultado
                .filter(test -> test.getWorklist() == null) //examenes que no se encuentre en una hoja de trabajo generada
                .collect(Collectors.toList());
        final boolean containsAll = excluding ? testToCompare.stream().map(Test::getId).collect(Collectors.toList()).containsAll(worksheetTests) : true;

        return testToCompare.stream()
                .filter(test -> !excluding || containsAll)
                .filter(test -> worksheetTests.contains(test.getId()))//exámenes en la hoja de trabajo configurada
                .collect(Collectors.toList());

    }

    /**
     * Obtiene los examenes que pertenezcan a la hoja de trabajo
     *
     * @param worksheet
     * @param order
     *
     * @return
     */
    public List<Test> getWorksheetTests(List<TestBasic> worksheet, List<Test> order)
    {
        //Lista examenes de la orden que corresponden a la hoja de trabajo
        final List<Test> orderTests = order.stream()
                .filter(test -> worksheet.contains(new TestBasic(test.getId())))
                .map(test -> test.setSelected(true))
                .collect(Collectors.toList());
        
        //Lista de examenes de la hoja de trabajo que no estan en la orden
        List<Test> resultTests = worksheet.stream()
                .filter(test -> !orderTests.contains(new Test(test.getId())))
                .map(testbasic -> new Test(testbasic))
                .map(test -> test.setSelected(false))
                .map(test -> test.setWorksheet(true))
                .collect(Collectors.toList());

        resultTests.addAll(orderTests);
        return resultTests;

    }

    @Override
    public List<Common> listSecuence(int worksheet) throws Exception
    {
        return daoWorklist.listSecuences(worksheet);
    }
    
    /**
     * Filtra examenes que correspondan a la hoja de trabajo y que no se repitan por doble almacenamiento en gradilla
     *
     * @param order orden con examenes para filtrar
     *
     * @return Lista de examenes filtrados que corresponden a los filtros enviados
     */
    private List<Test> filterTestDistinct(Order order)
    {
        return order.getTests().stream().distinct().collect(Collectors.toList());
    }

}

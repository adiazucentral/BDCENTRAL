package net.cltech.enterprisent.service.impl.enterprisent.integration;

import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.masters.user.UserDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultTestDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.databank.TemplateDatabank;
import net.cltech.enterprisent.domain.integration.databank.TestCross;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.test.GeneralTemplateOption;
import net.cltech.enterprisent.domain.masters.user.UserIntegration;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.results.ResultTestComment;
import net.cltech.enterprisent.domain.operation.results.ResultTestRepetition;
import net.cltech.enterprisent.domain.operation.results.ResultTestValidate;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationDatabankService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
import net.cltech.enterprisent.service.interfaces.operation.microbiology.MicrobiologyService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.service.interfaces.operation.tracking.SampleTrackingService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de la integracion con databank
 *
 * @version 1.0.0
 * @author adiaz
 * @since 20/02/2020
 * @see Creacion
 */
@Service
public class IntegrationDatabankServiceEnterpriseNT implements IntegrationDatabankService
{
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ResultTestDao resultTestDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private SampleTrackingService sampleTrackingService;
    @Autowired
    private ResultsService resultService;
    @Autowired
    private TestService testService;
    @Autowired
    private ConfigurationService configurationServices;
    @Autowired
    private DemographicService demographicService;
    @Autowired
    private ResultsService resultsService;
    @Autowired
    private MicrobiologyService microbiologyService;
    @Autowired
    private UserDao userDao;
      @Autowired
    private LaboratorysByBranchesService laboratorysByBranchesService;
    
    @Override
    public void results(List<ResultTest> resultTest) throws Exception
    {
        
        AuthorizedUser session = JWT.decode(request);
        List<ResultTest> resultTestList = resultTestDao.list(resultTest.get(0).getOrder()); 
        for (ResultTest resultTestItem : resultTest)
        {
            int testid = 0;
            
            if (resultTestItem.getTestId() == 0)
            {
                
                net.cltech.enterprisent.domain.masters.test.Test test = new net.cltech.enterprisent.domain.masters.test.Test();
                test = testService.get(null, resultTestItem.getTestCode(), null, null);
                
                if(test != null) {
                    resultTestItem.setTestId(test.getId());
                    testid = resultTestItem.getTestId();
                    
                    if(testid != 0) {
                
                        ResultTest ResultTestOld = new ResultTest(); 
                        
                        for (ResultTest result : resultTestList)
                        {
                            if(result.getTestId() == testid) {
                                ResultTestOld = result;
                                break;
                            }
                        }
                        if(ResultTestOld.getOrder() != 0)
                        {
                            UserIntegration user = userDao.getUserByCode(resultTestItem.getUser().getUserName());
                            Integer userdatabank = user == null ? session.getId() : user.getId();
                            
                            
                            if (ResultTestOld.getResult() == null ? resultTestItem.getResult() != null : !ResultTestOld.getResult().equals(resultTestItem.getResult())){
                                if(ResultTestOld.getState() <  LISEnum.ResultTestState.REPORTED.getValue()){
                                    ResultTestOld.setResult(resultTestItem.getResult());
                                }

                                ResultTestOld.setResultChanged(true);
                            }

                            else {
                                ResultTestOld.setResultChanged(false);
                            }

                            if(ResultTestOld.getSampleState() != LISEnum.ResultSampleState.CHECKED.getValue()){
                                session.setBranch(parseInt(configurationServices.get("SedeVerificacionDatabank").getValue()));
                                boolean sample = sampleTrackingService.sampleTracking(ResultTestOld.getOrder(), ResultTestOld.getSampleCode(), LISEnum.ResultSampleState.CHECKED.getValue(), session);
                            }
                            
                            //Plantilla de resultados
                            if(resultTestItem.getTemplates().size() > 0 ) {
                                GeneralTemplateOption general = microbiologyService.getGeneralTemplate(ResultTestOld.getOrder(), testid);
                                if(general != null) {
                                    general.getOptionTemplates().forEach( template -> {
                                        TemplateDatabank temp = resultTestItem.getTemplates().stream().filter( t -> template.getOption().toLowerCase().equals(t.getName().toLowerCase())).findAny().orElse(null);
                                        if( temp != null ) {
                                            if(template.getResult() == null || template.getResult().isEmpty()) {
                                                template.setResult(temp.getResult());
                                            } else {
                                                template.setResult(template.getResult().concat("\n").concat(temp.getResult()));
                                            }
                                        }
                                    });
                                    microbiologyService.insertResultTemplate(general.getOptionTemplates(), ResultTestOld.getOrder(), testid, userdatabank);
                                }
                            }

                            ResultTestOld.setUserId(session.getId());
                            if(ResultTestOld.getState() < LISEnum.ResultTestState.REPORTED.getValue()){
                                ResultTestOld.setNewState(LISEnum.ResultTestState.REPORTED.getValue());
                                resultService.reportedTest(ResultTestOld, userdatabank);
                                validResultTest(ResultTestOld.getOrder(),testid,  userdatabank);
                            }
                            else if(ResultTestOld.getState() ==  LISEnum.ResultTestState.REPORTED.getValue() && ResultTestOld.getResultChanged()){

                                ResultTestOld.setNewState(LISEnum.ResultTestState.RERUN.getValue());           

                                ResultTestRepetition resultTestRepetition = new ResultTestRepetition();
                                resultTestRepetition.setType('R');
                                resultTestRepetition.setReasonId(7);
                                ResultTestOld.setResultRepetition(resultTestRepetition);
                                resultService.reportedTest(ResultTestOld, userdatabank);

                                ResultTest newResult = ResultTestOld;
                                newResult.setResultRepetition(null);
                                newResult.setResult(resultTestItem.getResult());
                                newResult.setNewState(LISEnum.ResultTestState.REPORTED.getValue());

                                resultService.reportedTest(newResult, userdatabank);
                                validResultTest(ResultTestOld.getOrder(),testid, userdatabank);
                            }
                        }
                    }
                } else {
                    validateFields(resultTest);
                }
            }
            else
            {
                testid = resultTestItem.getTestId();
            }
        }
    }
    
    private void validateFields(List<ResultTest> resultTest) throws Exception
    {
        AuthorizedUser session = JWT.decode(request);
        String codeTestCross = configurationServices.get("CodigoPruebaCruzada").getValue();
        for (ResultTest resultTestItem : resultTest)
        {
            if(resultTestItem.getTestCode().equals("crossmatched")) {
                UserIntegration user = userDao.getUserByCode(resultTest.get(0).getTestCrossArrayList().get(0).getUsertest().getUserName());
                Integer userdatabank = user == null ? session.getId() : user.getId();
                
                validateOrder(resultTestItem.getOrder(), codeTestCross, resultTest.get(0).getTestCrossArrayList(), userdatabank );
            }
        }
    }
    
    private void validateOrder(long order, String codeTestCross, List<TestCross> testCrossList, Integer userdatabank) throws Exception
    {
        
        final List<Demographic> demos = demographicService.list().stream().filter((Demographic t) -> t.isState()).collect(Collectors.toList());
        List<Demographic> demographicsFilter = new ArrayList<>();
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        List<Order> found = ordersDao.orderReport(order, order, demos, demographicsFilter, 1, laboratorys, idbranch);

        if(found.size() > 0) {
            final List<Test> tests = found.get(0).getTests().stream().filter((Test t) -> t.getCode().equals(codeTestCross)).collect(Collectors.toList());
            if(tests.size() == 0) {
                addTest(found.get(0), codeTestCross, testCrossList, userdatabank);
            }else{
                Test testCross = new Test();
                testCross.setId(tests.get(0).getId());
                testCross.setCode(tests.get(0).getCode());       
                testCross.setName(tests.get(0).getName());
                testCross.setTestType(tests.get(0).getTestType());
                reportResultTestCross(found.get(0), testCross, testCrossList, userdatabank);
            }
        }
    }
    
    private void addTest(Order found, String codeTestCross, List<TestCross> testCrossList, Integer userdatabank) throws Exception
    {
        net.cltech.enterprisent.domain.masters.test.Test test = new net.cltech.enterprisent.domain.masters.test.Test();
        test = testService.get(null,codeTestCross, null, null);
        Test testCross = new Test();
        testCross.setId(test.getId());
        testCross.setCode(test.getCode());       
        testCross.setName(test.getName());
        testCross.setTestType(test.getTestType());
        found.getTests().add(testCross);
        
        AuthorizedUser session = JWT.decode(request);
        
        resultsService.addRemoveTest(found, LISEnum.ResultSampleState.ORDERED.getValue(), session);
        session.setBranch(parseInt(configurationServices.get("SedeVerificacionDatabank").getValue()));
        boolean sample = sampleTrackingService.sampleTracking(found.getOrderNumber(), test.getSample().getCodesample(), LISEnum.ResultSampleState.CHECKED.getValue(), session);
        reportResultTestCross(found, testCross, testCrossList, userdatabank);
    }
    
    private void reportResultTestCross(Order order, Test testCross, List<TestCross> testCrossList, Integer userdatabank) throws Exception
    {
        //Concatenar nuevo resultado con el anterior.
        ResultFilter filter = new ResultFilter();
        List<Integer> listTest = new ArrayList<>();
        List<ResultTest> listTestResult = new ArrayList<>();
        listTest.add(testCross.getId());
        filter.setFilterId(1);
        filter.setFirstOrder(order.getOrderNumber());
        filter.setLastOrder(order.getOrderNumber());
        filter.setTestList(listTest);
        AuthorizedUser session = JWT.decode(request);
        filter.setUser(session);

        listTestResult = resultService.getTests(filter, null);
        if (listTestResult.size() > 0)
        {
            String segment = "";
               
            for (TestCross testCrossL : testCrossList)
            {
                GeneralTemplateOption general = microbiologyService.getGeneralTemplate(order.getOrderNumber(), testCross.getId());
                 if(general != null) {
                    general.getOptionTemplates().forEach( template -> {
                        if(null != template.getOption())switch (template.getOption()) {
                            case "PRUEBA DE COMPATIBILIDAD:":
                                template.setResult(testCrossL.getResult());
                                break;
                            case "No. BOLSA:":
                                template.setResult(testCrossL.getBagExternalNumber());
                                break;
                            case "SELLO DE CALIDAD:":
                                template.setResult(testCrossL.getSeal());
                                break;
                            default:
                                break;
                        }
                    }); 
                    
                    microbiologyService.insertResultTemplate(general.getOptionTemplates(), order.getOrderNumber(), testCross.getId(), userdatabank);
                 }
            }
            
            // Obtenemos el comentario viejo y le adicionamos los nuevos parametros
            ResultTestComment resultCommentTwo = resultService.getTests(filter, null).get(0).getResultComment();
            String oldComment = listTestResult.get(0).getResultComment().getComment() == null ? "" : listTestResult.get(0).getResultComment().getComment();
            String newComment = oldComment + " " + segment;
            resultCommentTwo.setCommentChanged(true);
            // Adicionamos al objeto el comentario para ese resultado
            resultCommentTwo.setComment(newComment);
            listTestResult.stream().forEach((resultOne) ->
            {
                resultOne.setResultChanged(true);
                resultOne.setNewState(2);
            });
            listTestResult.get(0).setResult("MEMO");
            // Registramos en auditoria la trazabilidad del comentario
            listTestResult.get(0).setResultComment(resultCommentTwo);
            resultService.reportedTest(listTestResult.get(0),userdatabank);
            validResultTest(order.getOrderNumber(),testCross.getId(), userdatabank);
        }
    }
    
    private void validResultTest(long Order, int testid, int userdatabank) throws Exception
    {
        AuthorizedUser session = JWT.decode(request);
        session.setId(userdatabank);
        
        ResultTestValidate resultTestValidate = new ResultTestValidate();
        Item itemOne = new Item();
        resultTestValidate.setFinalValidate(false);
        resultTestValidate.setOrderId(Order);
        List<ResultTest> listResultTest = new ArrayList<>();
        listResultTest = resultTestDao.listByIdTest(Order, testid);
        listResultTest.get(0).setNewState(4);
        listResultTest.get(0).setGrantValidate(true);
        resultTestValidate.setTests(listResultTest);
        itemOne = resultTestDao.getGenderTest(testid);
        resultTestValidate.setSex(itemOne);

        resultsService.validatedTests(resultTestValidate, session, false);
    }
    
    
    

}

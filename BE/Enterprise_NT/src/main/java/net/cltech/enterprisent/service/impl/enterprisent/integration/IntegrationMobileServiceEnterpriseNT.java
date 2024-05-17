package net.cltech.enterprisent.service.impl.enterprisent.integration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.controllers.tools.JWTPatient;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationMobileDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.mobile.AuthenticationData;
import net.cltech.enterprisent.domain.integration.mobile.AuthorizedPatient;
import net.cltech.enterprisent.domain.integration.mobile.ChangePassword;
import net.cltech.enterprisent.domain.integration.mobile.LisPatient;
import net.cltech.enterprisent.domain.integration.mobile.OrderEt;
import net.cltech.enterprisent.domain.integration.mobile.PatientEmailUpdate;
import net.cltech.enterprisent.domain.integration.mobile.RestorePassword;
import net.cltech.enterprisent.domain.integration.mobile.TestEt;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.common.FilterOrderHeader;
import net.cltech.enterprisent.domain.operation.common.PrintOrder;
import net.cltech.enterprisent.domain.operation.common.PrintOrderInfo;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.reports.JsonToBufferNT;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.service.impl.enterprisent.operation.results.CheckResultServiceEnterpriseNT;
import net.cltech.enterprisent.service.impl.enterprisent.operation.results.ResultServiceEnterpriseNT;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMobileService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.reports.ReportService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.tools.Log;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios generales de integración con app móvil Enterprise NT
 *
 * @version 1.0.0
 * @author JDuarte
 * @see 19/08/2020
 * @see Creaciòn
 */
@Service
public class IntegrationMobileServiceEnterpriseNT implements IntegrationMobileService
{

    @Autowired
    private IntegrationMobileDao mobilDao;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ResultsService resultsService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private ResultServiceEnterpriseNT resultServiceEnterpriseNT;
    @Autowired
    private DemographicService demographicService;
    @Autowired
    private OrderService orderService;

    @Override
    public AuthorizedPatient getEtUserAuthentication(String userName, String password) throws Exception
    {

        try
        {

            AuthorizedPatient authorizedPatient = new AuthorizedPatient();

            int valueIsEmpty = 0;

            String havePassword = mobilDao.havePassword(userName);

            if ((havePassword == null) || (havePassword.equals("")))
            {
                valueIsEmpty = 1;
            }

            if (valueIsEmpty > 0)
            {
                Integer idPatient;

                idPatient = mobilDao.getIdPatient(userName);

                Integer valueNew = idPatient * 345;

                String passwordNew = valueNew.toString();

                if (passwordNew.equals(password))
                {

                    authorizedPatient = mobilDao.getEtUserAuthenticationPasswordNull(userName, idPatient);

                } else
                {
                    throw new EnterpriseNTException("Error password");
                }

            } else
            {
                authorizedPatient = mobilDao.getEtUserAuthentication(userName, password);
            }
            return authorizedPatient;

        } catch (Exception e)
        {
            throw new EnterpriseNTException("Error in login");
        }
    }

    @Override
    public ChangePassword changePassword(ChangePassword userPassword) throws Exception
    {

        try
        {

            AuthorizedPatient session = JWTPatient.decode(request);

            ChangePassword bean = new ChangePassword();

            userPassword.setPassword(Tools.encrypt(userPassword.getPassword()));

            boolean answerCorrect = mobilDao.changePassword(userPassword.getId(), userPassword.getPassword());

            if (answerCorrect)
            {
                bean.setId(session.getId());
                bean.setPassword(Tools.decrypt(userPassword.getPassword()));
                bean.setCorrect(answerCorrect);

            }

            return bean;

        } catch (Exception e)
        {

            throw new EnterpriseNTException("Error in System");

        }
    }

    /**
     * Retorna el objeto de paciente de LIS
     *
     * @param patientId historia clinica del paciente
     *
     * @return Instancia con los datos del paciente del LIS.
     * @throws java.lang.Exception
     */
    @Override
    public LisPatient getPatientByPatientId(String patientId) throws Exception
    {
       
            List<LisPatient> patient = mobilDao.getPatientByPatientId(patientId);
            patient = patient.stream()
                    .filter( distinctByKey(p -> p.getId()) )
                    .collect( Collectors.toList());
            
            if(patient.size() > 0)
            {
                int valueIsEmpty = 0;

                String havePassword = mobilDao.havePassword(patient.get(0).getEmail());

                if ((havePassword == null) || (havePassword.equals("")))
                {
                    valueIsEmpty = 1;
                }

                if (valueIsEmpty > 0)
                {
                    patient.get(0).setPasswordTemp(true);

                } else
                {
                    patient.get(0).setPasswordTemp(false);
                }
            }
            else {
                List<String> errors = new ArrayList<>();
                errors.add("0|patiend not found");
                throw new EnterpriseNTException(errors);
            }
            return patient.get(0);
       
    }
    
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) 
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public List<OrderEt> getEtOrders(int limit) throws Exception
    {

        try
        {

            List<OrderEt> bean = new ArrayList<>();

            AuthorizedPatient session = JWTPatient.decode(request);

            HttpServletRequest req = (HttpServletRequest) request;
            //Validamos que la cabecera tenga el token de autenticación.
            String token = req.getHeader("Authorization");

            if (!token.isEmpty())
            {

                bean = mobilDao.getEtOrders(limit, session.getId());

            }

            return bean;

        } catch (Exception e)
        {

            throw new EnterpriseNTException("Error in System");

        }

    }

    private List<String> validate(ChangePassword userPassword) throws Exception
    {

        List<String> errors = new ArrayList<>();

        HttpServletRequest req = (HttpServletRequest) request;
        //Validamos que la cabecera tenga el token de autenticación.
        String token = req.getHeader("Authorization");

        if (userPassword.getPassword().isEmpty())
        {
            errors.add("0|Password Empty ");

        }

        if (token.isEmpty())
        {
            errors.add("1|Need token ");

        }

        return errors;
    }

    /**
     * Lista las ordenes de un paciente para la app Móvil.
     *
     * @return lista de Objeto TestEt, Obtiene los examenes que estan marcados
     * para ver en ingreso de ordenes
     *
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<TestEt> getEtTest() throws Exception
    {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");

        if (!token.isEmpty())
        {
            List<TestEt> testsEt = mobilDao.getEtTests();
            if (!testsEt.isEmpty())
            {
                for (TestEt testEt : testsEt)
                {
                    testEt.setRequirement(mobilDao.getRequirements(testEt.getId()));
                }
            }
            return testsEt;
        }
        return new ArrayList<>();
    }

    public RestorePassword restorPatientPassword(LisPatient lisPatient) throws Exception
    {

        try
        {
            RestorePassword bean = new RestorePassword();

            boolean answerCorrect = mobilDao.restorPatientPassword(lisPatient.getPatientId());

            if (answerCorrect)
            {
                bean.setSuccess(answerCorrect);
                bean.setState(1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dateNow = new Date();
                String currentTime = sdf.format(dateNow);
                Date i = sdf.parse(String.valueOf(currentTime));
                bean.setDate(i);

            }

            return bean;

        } catch (Exception e)
        {

            throw new EnterpriseNTException("Error in System");

        }

    }

    /**
     * Recibe un id de una orden y retorna un dato en pdf para la ap Móvil
     *
     * @param order
     *
     * @return Dato byte[] para archivo pdf
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public byte[] getPDFOrder(long order) throws Exception
    {
        try
        {
            byte[] bytes = new byte[0];
                        
            AuthorizedPatient session = JWTPatient.decode(request);
          
            AuthenticationData user = mobilDao.get(session.getId());
            if (user != null)
            {
               
                List<Order> listOrder;
                List<ResultTest> resultsList = new ArrayList<>();
                String toBaseSixtyFour;

                if (order != 0)
                {
                    Filter search = new Filter();
                    search.setRangeType(1);
                    search.setInit(order);
                    search.setEnd(order);
                    
                    // Obtenemos el listado de una orden
                    listOrder = reportService.orderHeader(search);
                    
                    FilterOrderHeader filter = new FilterOrderHeader();
                                        
                    List<PrintOrder> list = new ArrayList<>();
                    PrintOrder pintOrder = new PrintOrder();
                    
                    List<PrintOrderInfo> listPrintOrder = new ArrayList<>();
                    PrintOrderInfo orderInfo = new PrintOrderInfo();
                    listPrintOrder.add(orderInfo);
                    
                    orderInfo.setOrder(listOrder.get(0));
                    
                    pintOrder.setPhysician(null);
                    pintOrder.setListOrders(listPrintOrder);
                    
                    list.add(pintOrder);
                    
                    filter.setPrintOrder(list);
                    filter.setTypeReport(0);
                    filter.setAttached(true);
                    
                    PrintOrder finalReport = reportService.finalReport(filter);
                    
                    finalReport.getListOrders().forEach( orderprint -> {
                        List<Long> listOrders = new ArrayList<>();
                        listOrders.add(orderprint.getOrder().getOrderNumber());                        
                        for (ResultTest resultTest : getTestResultsAppMovil(listOrders, "", false))
                        {
                            if (resultTest != null)
                            {
                                if (resultTest.getState() == 4 || resultTest.getState() == 5)
                                {
                                    // Agregamos dichos resultados
                                    resultsList.add(resultTest);
                                }
                            }
                        }
                    });
                    
                    finalReport.getListOrders().get(0).getOrder().setResultTest(resultsList);
                    
                    if(finalReport.getListOrders().size() > 0 && finalReport.getListOrders().get(0).getOrder().getResultTest().size() > 0) 
                    {
                        Date objDate = new Date();
                        String strDateFormat = configurationService.getValue("FormatoFecha");
                        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);

                        FilterOrderHeader filterOrder = new FilterOrderHeader();
                        
                        String nameTemplate = getNameReport(finalReport.getListOrders().get(0).getOrder().getOrderNumber());
                  
                        List<User> userlist = orderService.getUserValidate(finalReport.getListOrders().get(0).getOrder().getOrderNumber());
                        finalReport.getListOrders().get(0).getOrder().setTemplateorder(nameTemplate);
                        
                        filterOrder.setOrder(Tools.jsonObject(finalReport.getListOrders().get(0).getOrder()));
                      
                        String urlLanguage = configurationService.getValue("UrlNodeJs") + "api/language";
                        HashMap<String, String> resp  = integrationService.get(HashMap.class, urlLanguage);
                        String language = jsonToString(resp);
                        filterOrder.setLabelsreport(language);
                        
                        filterOrder.setAttachments(Tools.jsonObject(finalReport.getListOrders().get(0).getAttachments()));
                        filterOrder.setUserlist(Tools.jsonObject(userlist));

                        filterOrder.setVariables(
                                "{\"username\":\"" + user.getUser() 
                                + "\",\"titleReport\":\"Informe final\",\"date\":\"" + objSDF.format(objDate) 
                                + "\",\"formatDate\":\"DD/MM/YYYY\",\"templateorder\":\""+ nameTemplate 
                                + "\",\"typePrint\":0}");
                        
                       
                        filterOrder.setPrintingMedium(2);
                        filterOrder.setPrintingType(1);
                        filterOrder.setTypeReport(1);
                        
                        String urlImpressionServ = configurationService.getValue("UrlNodeJs") + "api/printReportOrders";
                        JsonToBufferNT bufferExtern = integrationService.post(Tools.jsonObject(filterOrder), JsonToBufferNT.class, urlImpressionServ);
                        toBaseSixtyFour = Base64.getEncoder().encodeToString(bufferExtern.getData());
                     
                        if (!toBaseSixtyFour.isEmpty())
                        {
                           
                            //Cambio de estado de Finales y re impreso
                            changeStateTest(filterOrder, listOrder.get(0), session.getId(), session.getName1() + " " + session.getLastName());

                            bytes = Base64.getDecoder().decode(toBaseSixtyFour);
                           
                            return bytes;

                        } else
                        {
                            return bytes;
                        }
                    } else
                    {
                        return bytes;
                    }
                } else
                {
                    return bytes;
                }
            } else
            {
                return bytes;
            }

        } catch (Exception e)
        {

            byte[] bytesError = new byte[0];

            return bytesError;

        }
    }
    
    
    public String getNameReport(long order) throws Exception 
    {
        String name = "";
        String nameTemplate = nameReport();

        Demographic demographicTemplate = getDemographic();

        Order infoOrder = orderService.get(order);
        List<Demographic> list = demographicService.demographicsList();

        if(demographicTemplate != null) {
            if (demographicTemplate.getId() > 0 && demographicTemplate.isEncoded()) {
                DemographicValue demographicOrder = infoOrder.getDemographics().stream().filter( demo -> demo.getIdDemographic() == demographicTemplate.getId()).findFirst().orElse(null);
                name = nameTemplate + "_" + demographicOrder.getCodifiedCode() + ".mrt"; 
            } else if(demographicTemplate.getId() < 0 && demographicTemplate.isEncoded()) {
                String auxName = "";
                switch(demographicTemplate.getId()) {
                    case -1:
                        auxName = infoOrder.getAccount().getNit();
                        break;
                    case -2:
                        auxName = infoOrder.getPhysician().getCode();
                        break;
                    case -3:
                        auxName = infoOrder.getRate().getCode();
                        break;
                    case -4:
                        auxName = infoOrder.getType().getCode(); 
                        break;
                    case -5:
                        auxName = infoOrder.getBranch().getCode();
                        break;
                    case -6:
                        auxName = infoOrder.getService().getCode();
                        break;
                }

                name = nameTemplate + "_" + auxName +".mrt"; 
            } else {
               name =  nameTemplate + ".mrt";
            }

            String urlListReports = configurationService.getValue("UrlNodeJs") + "api/getlistReportFile";
            List<HashMap<String, String>> listReport  = integrationService.post("", List.class, urlListReports);

            if(listReport.size() == 0) {
                name = "reports.mrt";
            }                                    
        } else {
            name = "reports.mrt";
        }
        return name;
    }
    
    public String jsonToString(Map<String, String> map) {
        String mapAsString = map.keySet().stream()
          .map(key -> "\"" + key + "\":" + "\"" + map.get(key)+ "\"")
          .collect(Collectors.joining(", ", "{", "}"));
        return mapAsString;
    }
    
    public Demographic getDemographic() throws Exception {
        Integer demographicTitleReport = Integer.parseInt(configurationService.getValue("DemograficoTituloInforme"));
        Demographic template = null;
        if(demographicTitleReport != 0) {
            List<Demographic> list = demographicService.demographicsList();
            template = list.stream().filter( demographic -> demographic.getId().equals(demographicTitleReport)).findFirst().orElse(null);
        }
        return template;
    }

    public String nameReport() throws Exception {
        Integer demographicTitleReport = Integer.parseInt(configurationService.getValue("DemograficoTituloInforme"));
        String name = "";
        if(demographicTitleReport != 0) {
            List<Demographic> list = demographicService.demographicsList();
            Demographic template = list.stream().filter( demographic -> demographic.getId().equals(demographicTitleReport)).findFirst().orElse(null);
            name = "reports_" + template.getName();
            String referenceName = ""; 
            if(demographicTitleReport < 0) {
                switch(demographicTitleReport) {
                    case -1:
                        referenceName = "Cliente"; 
                        break;

                    case -2:
                        referenceName = "Médico"; 
                        break;
                        
                    case -3:
                        referenceName = "Tarifa"; 
                        break;
                        
                    case -4:
                        referenceName = "Tipo de orden"; 
                        break;
                        
                    case -5:
                        referenceName = "Sede"; 
                        break;
                        
                    case -6:
                        referenceName = "Servicio"; 
                        break;
                        
                    case -7:
                        referenceName = "Raza"; 
                        break;
                }
                name = "reports_" + referenceName;
            }
        } else {
            name = "reports";
        }
        
        return name;
    }

    /**
     * Obtiene examenes de una orden
     *
     * @param order Número de la orden
     * @param idOrdenHis Número de la orden externa
     * @return lista de examenes con información del resultado
     * {@link net.cltech.enterprisent.domain.operation.results.ResultTest}
     */
    private List<ResultTest> getTestResults(List<Long> orders, String idOrdenHis, boolean isConfidential)
    {
        try
        {
            long first = System.currentTimeMillis();
            List<ResultTest> result = resultsService.getTestsUnionDaughter(orders, idOrdenHis, isConfidential);
            System.out.println("Tiempo de respuesta examenes con union - " + (System.currentTimeMillis() - first));
            return result;
        } catch (Exception ex)
        {
            Log.error(CheckResultServiceEnterpriseNT.class, ex);
        }
        return new ArrayList<>();
    }

    /**
     * Metodo para cambiar el estado de los examenes
     *
     * @param search
     * @return
     * @throws Exception
     */
    private void changeStateTest(FilterOrderHeader filterOrder, Order orderComplete, int iduser, String receivesPerson) throws Exception
    {
        Item item = new Item();
        switch (filterOrder.getPrintingMedium())
        {
            case 1:
                item.setId(59);
                break;
            case 2:
                item.setId(62);
                break;
            case 3:
                item.setId(60);
                break;
            case 4:
                item.setId(61);
                break;
            default:
                break;
        }
        orderComplete.setDeliveryType(item);
        if (filterOrder.getTypeReport() == 0)
        {
            orderComplete.setResultTest(orderComplete.getResultTest().stream().filter((ResultTest test) -> test.getState() == LISEnum.ResultTestState.VALIDATED.getValue()).collect(Collectors.toList()));
            resultServiceEnterpriseNT.reportTests(orderComplete, iduser, receivesPerson, filterOrder.getSendAutomaticResult());
        } else if (filterOrder.getTypeReport() == 1)
        {
            resultServiceEnterpriseNT.reportTests(orderComplete, iduser, receivesPerson, filterOrder.getSendAutomaticResult());
        }
    }

    /**
     * Obtiene examenes de una orden para la app Móvil
     *
     * @param order Número de la orden
     * @param idOrdenHis Número de la orden externa
     * @return lista de examenes con información del resultado
     * {@link net.cltech.enterprisent.domain.operation.results.ResultTest}
     */
    private List<ResultTest> getTestResultsAppMovil(List<Long> orders, String idOrdenHis, boolean isConfidential)
    {
        try
        {
            long first = System.currentTimeMillis();
            List<ResultTest> result = resultsService.getTestsUnionDaughterApp(orders, idOrdenHis, isConfidential);
            System.out.println("Tiempo de respuesta examenes con union - " + (System.currentTimeMillis() - first));
            return result;
        } catch (Exception ex)
        {
            Log.error(CheckResultServiceEnterpriseNT.class, ex);
        }
        return new ArrayList<>();
    }
    
    /**
     * Actualiza el correo electronico de un paciente según el id del paciente que se envie
     *
     * @param patient
     * @return Cantidad de registros afectados
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public int patientEmailUpdate(PatientEmailUpdate patient) throws Exception
    {
        try
        {
            return mobilDao.patientEmailUpdate(patient);
        }
        catch (Exception e)
        {
            return -1;
        }
    }
}

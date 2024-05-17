package net.cltech.enterprisent.service.impl.enterprisent.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationEpidemiologyDao;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationMinsaDao;
import net.cltech.enterprisent.dao.interfaces.masters.interview.QuestionDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.CentralSystemDao;
import net.cltech.enterprisent.dao.interfaces.operation.tracking.SampleTrackingDao;
import net.cltech.enterprisent.domain.DTO.integration.minsa.results.OrderHisPendingResults;
import net.cltech.enterprisent.domain.DTO.integration.minsa.results.OrderPendingResults;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.exception.WebException;
import net.cltech.enterprisent.domain.integration.epidemiology.EpidemiologicalEvents;
import net.cltech.enterprisent.domain.integration.generalMinsa.GeneralAnswer;
import net.cltech.enterprisent.domain.integration.generalMinsa.GeneralDemographic;
import net.cltech.enterprisent.domain.integration.generalMinsa.GeneralOrder;
import net.cltech.enterprisent.domain.integration.generalMinsa.GeneralQuestion;
import net.cltech.enterprisent.domain.integration.generalMinsa.GeneralResult;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.interview.Answer;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Result;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.service.interfaces.common.ListService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationIngresoService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMinsaService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.BranchService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicItemService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DocumentTypeService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.OrderTypeService;
import net.cltech.enterprisent.service.interfaces.masters.interview.InterviewService;
import net.cltech.enterprisent.service.interfaces.masters.interview.QuestionService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import net.cltech.enterprisent.tools.log.integration.SecurityLog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Implementacion de integración para Enterprise NT con Minsa.
 *
 * @version 1.0.0
 * @author bbonilla
 * @since 29/04/2022
 * @see Creación
 *
 * @author hpoveda
 * @since 29/04/2022
 * @see Creación
 */
@Service
public class IntegrationMinsaServiceEnterpriseNT implements IntegrationMinsaService
{

    @Autowired
    private IntegrationMinsaDao integrationMinsaDao;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private ListService listService;
    @Autowired
    private CentralSystemDao centralSystemDao;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private TestService testService;
    @Autowired
    private DemographicService demographicService;
    @Autowired
    private DemographicItemService demographicItemService;
    @Autowired
    private IntegrationIngresoService serviceIngreso;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ResultsService resultsService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private SampleTrackingDao sampleTrackingDao;
    @Autowired
    private InterviewService interviewService;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private IntegrationEpidemiologyDao integrationEpidemiologyDao;

    @Override
    public List<OrderPendingResults> pendingResultsOrdersEpi(int centralSytem, int days)
    {
        try
        {
            Integer orderdigits = configurationService.getIntValue("DigitosOrden");
            List<Demographic> demosList = demographicService.list()
                    .stream()
                    .filter(demo -> demo.isState())
                    .collect(Collectors.toList());
            return integrationMinsaDao.pendingResultsOrdersEpi(centralSytem, demosList, days, orderdigits);
        } catch (Exception ex)
        {
            IntegrationHisLog.error(ex);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean insertOrderHis(List<OrderHisPendingResults> hisPendingResults)
    {

        try
        {
            return integrationMinsaDao.insertOrderHis(hisPendingResults);
        } catch (Exception ex)
        {
            IntegrationHisLog.error(ex);
            return false;
        }
    }

    /**
     * Obtiene la lista de pruebas asociadas al area epidemiologica
     *
     * @return Lista de pruebas
     * @throws Exception Error al obtener la lista de pruebas
     */
    @Override
    public List<EpidemiologicalEvents> epidemiologicalEvents() throws Exception
    {
        try
        {
            String area = configurationService.getValue("AreaEpidemiologica");

            if (!area.isEmpty())
            {

                final Integer idArea = Integer.parseInt(area);

                List<EpidemiologicalEvents> tests = integrationEpidemiologyDao.getTestsByArea(idArea);

                tests.forEach(test ->
                {
                    try
                    {
                        if (test.getInterview().getId() != 0)
                        {
                            List<Question> questions = interviewService.listQuestion(test.getInterview().getId());
                            questions.forEach(question ->
                            {
                                if (!question.isOpen())
                                {
                                    questionDao.readAnswer(question);
                                }
                            });
                            test.getInterview().setQuestions(questions);
                        }
                    } catch (Exception ex)
                    {
                        Logger.getLogger(IntegrationServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                return tests;

            } else
            {
                return null;
            }
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<User> listUser() throws Exception
    {
        List<User> users = userService.list(true);
        users.forEach((user) ->
        {
            //Ocultar información innesaria para la integración
            users.set(users.indexOf(user), new User(user.getId(), user.getName(), user.getLastName(), user.getUserName(), user.getPhoto()));
        });
        return users;
    }

    @Override
    public List<User> listUserSimple() throws Exception
    {
        List<User> users = userService.SimpleUserList();
        return users;
    }

    @Override
    public List<Branch> listBranches() throws Exception
    {
        List<Branch> branches = branchService.list(true);
        branches.forEach((branch) ->
        {
            branches.set(branches.indexOf(branch), new Branch(branch.getId(), branch.getCode(), branch.getName()));
        });
        return branches;
    }

    @Override
    public void processResponseError(Response response) throws IOException, EnterpriseNTException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String responseAsString = response.body().string();
        try
        {
            throw new EnterpriseNTException(mapper.readValue(responseAsString, WebException.class).getErrorFields());
        } catch (IOException ex)
        {
            ex.printStackTrace();
            SecurityLog.error(ex);
            throw new EnterpriseNTException(responseAsString);
        }
    }

    @Override
    public <T> T post(String data, Class<T> valueTypeReturn, String url) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data);
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Request requestRest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", getToken())
                .post(body)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(requestRest).execute();
        if (response.code() == HttpStatus.OK.value())
        {
            return mapper.readValue(response.body().string(), mapper.getTypeFactory().constructType(valueTypeReturn));
        } else if (response.code() == HttpStatus.NO_CONTENT.value())
        {
            return null;
        } else
        {
            processResponseError(response);
        }
        return null;
    }

    @Override
    public <T> T put(String data, Class<T> valueTypeReturn, String url) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data);
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Request requestRest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", getToken())
                .put(body)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(requestRest).execute();

        if (response.code() == HttpStatus.OK.value())
        {
            return mapper.readValue(response.body().string(), mapper.getTypeFactory().constructType(valueTypeReturn));
        } else if (response.code() == HttpStatus.NO_CONTENT.value())
        {
            return null;
        } else
        {
            processResponseError(response);
        }
        return null;

    }

    @Override
    public <T> void putVoid(String data, String url) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data);
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Request requestRest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", getToken())
                .put(body)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(requestRest).execute();
        if (response.code() != HttpStatus.OK.value() || response.code() == HttpStatus.NO_CONTENT.value())
        {
            processResponseError(response);
        }
    }

    @Override
    public <T> T get(Class<T> valueTypeReturn, String url) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Request requestRest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", getToken())
                .get()
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(requestRest).execute();
        if (response.code() == HttpStatus.OK.value())
        {
            return mapper.readValue(response.body().string(), mapper.getTypeFactory().constructType(valueTypeReturn));
        } else if (response.code() == HttpStatus.NO_CONTENT.value())
        {
            return null;
        } else
        {
            processResponseError(response);
        }
        return null;
    }

    @Override
    public <T> List<T> getList(Class<T> valueTypeReturn, String url) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Request requestRest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", getToken())
                .get()
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(requestRest).execute();
        if (response.code() == HttpStatus.OK.value())
        {
            return mapper.readValue(response.body().string(), mapper.getTypeFactory().constructCollectionType(List.class, valueTypeReturn));
        } else if (response.code() == HttpStatus.NO_CONTENT.value())
        {
            return new ArrayList<>();
        } else
        {
            processResponseError(response);
        }
        return new ArrayList<>();
    }

    @Override
    public void delete(String url, String token) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Request requestRest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token == null ? getToken() : token)
                .delete()
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(requestRest).execute();
        if (response.code() != HttpStatus.OK.value() || response.code() == HttpStatus.NO_CONTENT.value())
        {
            processResponseError(response);
        }
    }

    private String getToken()
    {
        String token = "";
        try
        {
            token = request.getHeader("Authorization");
            return token == null ? "" : token;
        } catch (Exception ex)
        {
        }
        return token;
    }

    @Override
    public void reportResult(List<GeneralOrder> orders) throws Exception
    {
        try
        {
            orders.forEach(order ->
            {
                try
                {

                    AuthorizedUser tokenUser = JWT.decode(request);
                    Order orderNT = new Order();
                    int centralSystemConf = configurationService.getIntValue("SistemaCentralIntGeneral");

                    orderNT.setExternalId(order.getOrder());
                    orderNT.setCreatedDateShort(DateTools.dateToNumber(new Date()));

                    // Consultamos el tipo de orden por el codigo
                    OrderType orderType = orderTypeService.filterByCode(order.getType());
                    orderNT.setType(orderType);
                    // Consultamos la sede
                    Branch orderBranch = branchService.get(tokenUser.getBranch(), null, null, null);
                    orderNT.setBranch(orderBranch);
                    orderNT.setHomebound(false);
                    orderNT.setMiles(0);
                    orderNT.setTurn("");
                    orderNT.setInconsistency(false);

                    // Paciente:
                    Patient patientNT = patientService.get(order.getPatient().getIdentification());
                    if (patientNT == null)
                    {
                        patientNT = new Patient();
                        patientNT.setPatientId(order.getPatient().getIdentification());
                    }
                    // Nombres:
                    ArrayList<String> names = new ArrayList<>(Arrays.asList(order.getPatient().getNames().split(" ")));
                    if (names.size() > 1)
                    {
                        patientNT.setName1(names.get(0));
                        names.remove(0);
                        patientNT.setName2(String.join(" ", names));
                    } else
                    {
                        patientNT.setName1(names.get(0));
                    }
                    // Apellidos:
                    names = new ArrayList<>(Arrays.asList(order.getPatient().getLastName().split(" ")));
                    if (names.size() > 1)
                    {
                        patientNT.setLastName(names.get(0));
                        names.remove(0);
                        patientNT.setSurName(String.join(" ", names));
                    } else
                    {
                        patientNT.setLastName(names.get(0));
                    }
                    // Genero del paciente:
                    List<Item> items = listService.list(6);
                    Item gender = items.stream()
                            .filter(item -> item.getCode().equalsIgnoreCase(order.getPatient().getSex()))
                            .findAny().orElse(null);
                    patientNT.setSex(gender);
                    patientNT.setBirthday(order.getPatient().getBirthDay());

                    // Demográficos del paciente
                    List<DemographicValue> listDemopatient = new ArrayList<>();
                    for (GeneralDemographic demographic : order.getPatient().getDemographics())
                    {
                        if (demographic.getId().equals(Constants.DOCUMENT))
                        {
                            Integer idItemDemo = centralSystemDao.getIdItemDemoByHomologationCode(centralSystemConf, Constants.DOCUMENT_TYPE, demographic.getValue());
                            if (idItemDemo != null)
                            {
                                DocumentType document = documentTypeService.get(idItemDemo, null, null);
                                patientNT.setDocumentType(document);
                            }
                        } else if (demographic.getId().equals(Constants.ADRESS))
                        {
                            patientNT.setAddress(demographic.getValue());
                        } else if (demographic.getId().equals(Constants.PHONE))
                        {
                            patientNT.setPhone(demographic.getValue());
                        } else
                        {
                            DemographicValue dinamic = new DemographicValue();
                            if (demographic.getId().equals(Constants.CITY))
                            {
                                dinamic = getDynamicDemographic(centralSystemConf, Constants.CITY_ID, demographic.getValue());
                            } else if (demographic.getId().equals(Constants.DEPARTMENT))
                            {
                                dinamic = getDynamicDemographic(centralSystemConf, Constants.DEPARTMENT_ID, demographic.getValue());
                            } else if (demographic.getId().equals(Constants.PHONE2))
                            {
                                dinamic = getDynamicDemographic(centralSystemConf, Constants.PHONE2_ID, demographic.getValue());
                            } else if (demographic.getId().equals(Constants.LOCALITY))
                            {
                                dinamic = getDynamicDemographic(centralSystemConf, Constants.LOCALITY_ID, demographic.getValue());
                            } else if (demographic.getId().equals(Constants.TEST_CODE))
                            {
                                dinamic = getDynamicDemographic(centralSystemConf, Constants.TEST_CODE_ID, demographic.getValue());
                            }

                            if (dinamic.getDemographic() != null && dinamic.getIdDemographic() > 0)
                            {
                                listDemopatient.add(dinamic);
                            }
                        }
                    }

                    // Demograficos de la orden:
                    List<DemographicValue> listDemoOrder = new ArrayList<>();
                    for (GeneralDemographic demographic : order.getDemographics())
                    {
                        DemographicValue dinamicDemo = new DemographicValue();
                        if (demographic.getId().equals(Constants.SOURCE))
                        {
                            dinamicDemo = getDynamicDemographic(centralSystemConf, Constants.SOURCE_ID, demographic.getValue());
                        } else if (demographic.getId().equals(Constants.SYMPTOM_DATE))
                        {
                            dinamicDemo = getDynamicDemographic(centralSystemConf, Constants.SYMPTOM_DATE_ID, demographic.getValue());
                        } else if (demographic.getId().equals(Constants.REPORTER))
                        {
                            dinamicDemo = getDynamicDemographic(centralSystemConf, Constants.REPORTER_ID, demographic.getValue());
                        } else if (demographic.getId().equals(Constants.SAMPLE_TYPE))
                        {
                            dinamicDemo = getDynamicDemographic(centralSystemConf, Constants.SAMPLE_TYPE_ID, demographic.getValue());
                        } else if (demographic.getId().equals(Constants.ADRESS))
                        {

                            patientNT.setAddress(demographic.getValue());
                        } else if (demographic.getId().equals(Constants.PHONE))
                        {
                            patientNT.setPhone(demographic.getValue());
                        } else
                        {
                            DemographicValue dinamic = new DemographicValue();
                            if (demographic.getId().equals(Constants.CITY))
                            {
                                dinamic = getDynamicDemographic(centralSystemConf, Constants.DISTRITO_ID, demographic.getValue());
                            } else if (demographic.getId().equals(Constants.DEPARTMENT))
                            {
                                dinamic = getDynamicDemographic(centralSystemConf, Constants.REGION_ID, demographic.getValue());
                            } else if (demographic.getId().equals(Constants.PHONE2))
                            {
                                dinamic = getDynamicDemographic(centralSystemConf, Constants.PHONE2_ID, demographic.getValue());
                            } else if (demographic.getId().equals(Constants.LOCALITY))
                            {
                                dinamic = getDynamicDemographic(centralSystemConf, Constants.CORREGIMENTO_ID, demographic.getValue());
                            } else if (demographic.getId().equals(Constants.TEST_CODE))
                            {
                                dinamic = getDynamicDemographic(centralSystemConf, Constants.TEST_CODE_ID, demographic.getValue());
                            }

                            if (dinamic.getDemographic() != null && dinamic.getIdDemographic() > 0)
                            {
                                listDemopatient.add(dinamic);
                            }
                        }

                        if (dinamicDemo.getDemographic() != null && dinamicDemo.getIdDemographic() > 0)
                        {
                            listDemoOrder.add(dinamicDemo);
                        }
                    }
                    DemographicValue demoRegist = new DemographicValue();
                    demoRegist = getDynamicDemographic(centralSystemConf, Constants.TOMA_DATE_ID, new SimpleDateFormat("dd/MM/yyyy").format(order.getRegist()));
                    listDemoOrder.add(demoRegist);
                    DemographicValue demoRegistNumeroInterno = new DemographicValue();
                    demoRegistNumeroInterno = getDynamicDemographic(centralSystemConf, Constants.NUMERO_INTERNO_ORDER, order.getOrder());
                    listDemoOrder.add(demoRegistNumeroInterno);

                    DemographicValue demoRegistDateResult = new DemographicValue();
                    demoRegistDateResult = getDynamicDemographic(centralSystemConf, Constants.RESULT_DATE_ID, new SimpleDateFormat("dd/MM/yyyy").format(order.getResult().get(0).getRegist()));
                    listDemoOrder.add(demoRegistDateResult);
                    // Cargamos Demografcios de paciente 
                    patientNT.setDemographics(listDemopatient);
                    // Cargamos el paciente a la orden
                    orderNT.setPatient(patientNT);
                    // Cargamos Demografcios de order
                    orderNT.setDemographics(listDemoOrder);
                    // Examenes de la orden
                    List<Test> tests = new ArrayList<>();
                    List<Integer> testsResult = new ArrayList<>();
                    HashMap<String, Timestamp> dateResultforTest = new HashMap<>();
                    for (GeneralResult generalResult : order.getResult())
                    {
                        net.cltech.enterprisent.domain.masters.test.Test testAux = testService.get(null, generalResult.getTestCode(), null, null);
                        if (testAux != null)
                        {
                            dateResultforTest.put(generalResult.getTestCode(), generalResult.getRegist());
                            Test test = new Test();
                            test.setId(testAux.getId());
                            test.setArea(testAux.getArea());
                            test.setCode(testAux.getCode());
                            test.setAbbr(testAux.getAbbr());
                            test.setName(testAux.getName());
                            test.setTestType(testAux.getTestType());
                            test.setUnit(testAux.getUnit());
                            test.setSample(testAux.getSample());
                            test.setResult(new Result());
                            test.setPanel(new Test());
                            test.setPack(new Test());
                            test.setTests(new ArrayList<>());
                            test.setConfidential(false);
                            test.setLaboratory(new Laboratory());
                            test.setPrint(testAux.getPrintOrder());
                            test.setResultType(testAux.getResultType().intValue());
                            tests.add(test);
                            testsResult.add(test.getId());
                        }
                    }

                    // Si la orden no contiene examenes no se podra crear la misma
                    if (!tests.isEmpty())
                    {

                        orderNT.setTests(tests);
                        // Creación O Actualización de la orden:
                        List<Order> ordersHis = serviceIngreso.getOrdersByOrderHISMinsa(order.getOrder(), tokenUser.getBranch());
                        Long orderNumber;
                        if (ordersHis.isEmpty())
                        {
                            Order createdOrder = orderService.create(orderNT, tokenUser.getId(), JWT.decode(request).getBranch());
                            orderNumber = createdOrder.getOrderNumber();
                        } else
                        {
                            orderNumber = ordersHis.get(0).getOrderNumber();
                            ordersHis.get(0).setTests(tests);
                            resultsService.addRemoveTest(ordersHis.get(0), 4, tokenUser);
                        }

                        ResultFilter filter = new ResultFilter();
                        filter.setFirstOrder(orderNumber);
                        filter.setLastOrder(orderNumber);
                        filter.setFilterId(1);
                        filter.setTestList(testsResult);

                        List<ResultTest> resultTestList = resultsService.getTests(filter, null);

                        // Insercion de resultados y asociasion entre ordenes, examenes y entrevista
                        for (ResultTest resultTest : resultTestList)
                        {
                            List<Question> questions = new ArrayList<>();

                            //ResultTest resultTest = new ResultTest();
                            GeneralResult testToResult = order.getResult().stream()
                                    .filter(testOrder -> testOrder.getTestCode().equalsIgnoreCase(resultTest.getTestCode()))
                                    .findAny().orElse(null);
                            resultTest.setUserId(tokenUser.getId());
                            resultTest.setResult(testToResult.getResult());
                            resultTest.setNewState(LISEnum.ResultTestState.REPORTED.getValue());
                            resultTest.getResultComment().setCommentChanged(true);
                            resultTest.getResultComment().setComment(testToResult.getComment());
                            resultTest.setResultChanged(true);
                            resultTest.setDelta(false);
                            resultTest.setResultDate(dateResultforTest.get(resultTest.getTestCode()));
                            resultsService.reportedTest(resultTest);
                            resultTest.setResultDate(dateResultforTest.get(resultTest.getTestCode()));
                            List<ResultTest> resultTests = new ArrayList<>();
                            resultTests.add(resultTest);
                            if (testToResult.getInterview() != null && !testToResult.getInterview().getQuestions().isEmpty())
                            {
                                for (GeneralQuestion gQuestion : testToResult.getInterview().getQuestions())
                                {
                                    Question question = questionService.get(gQuestion.getId(), null, null);
                                    question.setInterviewAnswer(gQuestion.getInterviewAnswer());
                                    List<Answer> answers = new ArrayList<>();
                                    if (!gQuestion.getAnswers().isEmpty())
                                    {
                                        for (GeneralAnswer gAnswer : gQuestion.getAnswers())
                                        {
                                            Answer answer = questionService.getAnswer(null, gAnswer.getName());
                                            answer.setSelected(true);
                                            if (answer.getId() > 0)
                                            {
                                                answers.add(answer);
                                            }
                                        }
                                        question.setAnswers(answers);
                                    }

                                    if (question.getId() > 0)
                                    {
                                        questions.add(question);
                                    }
                                }
                            }
                            // Insertamos la entrevista junto con la orden y el examen:
                            sampleTrackingDao.insertResultInterview(questions, orderNumber, tokenUser);
                            // Seguido de esto validamos los examenes:
                            resultsService.validatedTest(orderNumber, resultTest.getTestId(), tokenUser.getId());
                            if (!Objects.equal(dateResultforTest.get(resultTest.getTestCode()), null))
                            {
                                IntegrationHisLog.info("INGRESA HACER UPDATE " + dateResultforTest.get(resultTest.getTestCode()));
                                resultsService.updateFechaIngresoDate(orderNumber, resultTest.getTestId(), dateResultforTest.get(resultTest.getTestCode()));
                            }

                        }
                    } else
                    {
                        SecurityLog.error("Examenes no existen");
                    }
                } catch (Exception e)
                {
                    SecurityLog.error(e);
                    try
                    {
                        System.out.println("error " + Tools.jsonObject(e));
                    } catch (JsonProcessingException ex)
                    {
                        Logger.getLogger(IntegrationServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } catch (Exception e)
        {
            SecurityLog.error(e);
            System.out.println("error " + Tools.jsonObject(e));
        }

    }

    /**
     * Consulta y retorna el demografico dinamico completamente cargado
     *
     * @return Demográfico dinamico
     */
    private DemographicValue getDynamicDemographic(int centralSystem, int idDemo, String demoItemValue)
    {
        try
        {
            DemographicValue dinamic = new DemographicValue();

            Integer idItemDemo = centralSystemDao.getIdItemDemoByHomologationCode(centralSystem, idDemo, demoItemValue);
            Demographic demo = demographicService.get(idDemo, null, null);
            if (idItemDemo != null)
            {
                List<DemographicItem> demoItem = demographicItemService.get(idItemDemo, null, null, null, true);
                dinamic.setIdDemographic(demo.getId());
                dinamic.setDemographic(demo.getName());
                dinamic.setEncoded(true);
                dinamic.setCodifiedId(demoItem.get(0).getId());
                dinamic.setCodifiedCode(demoItem.get(0).getCode());
                dinamic.setCodifiedName(demoItem.get(0).getName());
            } else
            {
                dinamic.setIdDemographic(demo.getId());
                dinamic.setDemographic(demo.getName());
                dinamic.setEncoded(false);
                dinamic.setNotCodifiedValue(demoItemValue);
            }
            return dinamic;
        } catch (Exception e)
        {
            return new DemographicValue();
        }
    }

}

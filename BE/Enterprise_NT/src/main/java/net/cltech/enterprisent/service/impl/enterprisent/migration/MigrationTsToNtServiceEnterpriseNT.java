/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.migration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationIngresoDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.CentralSystemDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.SampleDao;
import net.cltech.enterprisent.dao.interfaces.masters.user.UserDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.DemographicNT;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.OrderIngreso;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.OrderNT;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.PatientNT;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.RequestMigracion;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.ResponseNT;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.ResponseOrderNT;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.SampleNT;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.TestNT;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.ingreso.RequestItemCentralCode;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Result;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.service.interfaces.common.ListService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationIngresoService;
import net.cltech.enterprisent.service.interfaces.masters.billing.RateService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.AccountService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.BranchService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicItemService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DocumentTypeService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.OrderTypeService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.PhysicianService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.ServiceService;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
import net.cltech.enterprisent.service.interfaces.migration.MigrationTsToNtService;
import net.cltech.enterprisent.service.interfaces.operation.common.CommentService;
import net.cltech.enterprisent.service.interfaces.operation.microbiology.MicrobiologyService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import net.cltech.enterprisent.tools.mappers.MigrationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hpoveda
 */
@Service
public class MigrationTsToNtServiceEnterpriseNT implements MigrationTsToNtService
{

    @Autowired
    private BranchService branchService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private MicrobiologyService microbiologyService;
    @Autowired
    private OrdersDao orderDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ResultsService resultService;

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
    private SampleDao sampleDao;
    @Autowired
    private PhysicianService physicianService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RateService rateService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private IntegrationIngresoDao integrationIngresoDao;
    @Autowired
    private ConfigurationDao daoConfig;
    @Autowired
    private LaboratorysByBranchesService laboratorysByBranchesService;
    @Autowired
    private HttpServletRequest request;


     @Override
    public ResponseNT createNT(OrderIngreso orderIngreso) throws Exception
    {
        long time1 = System.currentTimeMillis();
        OrderNT order = orderIngreso.getOrder();
        ResponseNT responseNT = new ResponseNT();
        OrderCreationLog.info("ORDER FOR INTERFACE INGRESO" + order.getOrder());
        int centralSystemConf = order.getCentralSytemId();
        responseNT.setOrder(Long.parseLong(order.getOrder()));
        Order orderNT = MigrationMapper.toDtoOrder(order);

        orderNT.setCreateUser(userDao.findByUserName(order.getUser()));
        AuthorizedUser tokenUser = new AuthorizedUser(orderNT.getCreateUser().getId());
        tokenUser.setBranch(orderNT.getBranch().getId());
        //       orderNT.setExternalId(order.getOrder());
        orderNT.setCreatedDateShort(DateTools.dateToNumber(new Date()));
        // Consultamos el tipo de orden por el codigo
        orderNT.setType(orderTypeService.filterByCode(order.getType()));
        // CREACION PACIENTE
        Patient patientNT = new Patient(order.getPatient().getRecord());

        // Demográficos del paciente
        List<DemographicValue> listDemosPatient = new LinkedList<>();
        //DEMO CODIFICADOS Y HOMOLOGADOS PACIENTE
        for (DemographicNT demographic : order.getPatient().getDemographics())
        {
            nameValues(demographic);

            switch (demographic.getId())
            {
                case Constants.DOCUMENT_TYPE:
                    Integer idItemDemoPatient = demographicHomologado(demographic, centralSystemConf);
                    DocumentType document = documentTypeService.get(idItemDemoPatient, null, !demographic.getCode().isEmpty() && Objects.equals(idItemDemoPatient, null) ? demographic.getCode() : null);
                    if (document != null)//Al llamar patientService.get en este bloque permitira actualizar los demograficos
                    {
                        patientNT.setDocumentType(document);
                        patientNT = patientService.get(patientNT.getPatientId(), patientNT.getDocumentType().getId()) == null
                                ? patientNT : patientService.get(patientNT.getPatientId(), patientNT.getDocumentType().getId());
                    } else
                    {
                        responseNT.setOrder(0l);
                        responseNT.setError("ERROR FOR TYPE DOCUMENT IN ORDER EXTERNAL :" + orderNT.getExternalId());
                        OrderCreationLog.info("ERROR FOR TYPE DOCUMENT IN ORDER EXTERNAL :" + orderNT.getExternalId());
                        return responseNT;
                    }
                    break;
                case Constants.PATIENT_ADDRESS:
                    patientNT.setAddress(demographic.getCode());
                    break;
                case Constants.PATIENT_PHONE:
                    patientNT.setPhone(demographic.getCode());
                    break;
                case Constants.PATIENT_EMAIL:
                    patientNT.setEmail(demographic.getCode());
                    break;
                default:
                    if (!demographic.getEncode())
                    {
                        listDemosPatient.add(new DemographicValue(demographic));
                    } else
                    {
                        DemographicValue demoEcode = getEncodeDemographic(centralSystemConf, demographic.getId(), demographic.getCode());
                        listDemosPatient.add(demoEcode);
                    }
                    break;
            }
        }

        //INSERTA LOS NUEVO DATOS
        if (patientNT.getSex() == null)
        {
            Item gender = listService.list(6).stream()
                    .filter(item -> item.getCode().equals(String.valueOf(order.getPatient().getGender())))
                    .findFirst().orElse(null);
            patientNT.setSex(gender); // Nombres:
        }
        else if(patientNT.getSex().getId() == null){
            Item gender = listService.list(6).stream()
                    .filter(item -> item.getCode().equals(String.valueOf(order.getPatient().getGender())))
                    .findFirst().orElse(null);
            patientNT.setSex(gender); // Nombres:
        }

      
        
        //Nombres
        patientNT.setName1(order.getPatient().getName1() != null ? order.getPatient().getName1() : "");
        patientNT.setName2(order.getPatient().getName2() != null ? order.getPatient().getName2() : "");
        //Apellidos
        patientNT.setLastName(order.getPatient().getLastName() != null ? order.getPatient().getLastName() : "");
        patientNT.setSurName(order.getPatient().getSecondLastName() != null ? order.getPatient().getSecondLastName() : "");

        //formatear fecha 
        if (patientNT.getBirthDayFormat() == null)
        {
            SimpleDateFormat dateParser = new SimpleDateFormat("dd/MM/yy");
            try
            {
                patientNT.setBirthday(dateParser.parse(order.getPatient().getBirthDate()));
            } catch (ParseException e)
            {
                OrderCreationLog.info("Birthdate " + order.getPatient().getBirthDate() + " not valid. Set null birthdate");
                responseNT.setError("ORDER NOT CREATE FOR EXEPTION  " + e);
            }

        }

        //INSERTA DEMOGRAFICO AL PACIENTE DE LA ORDEN 
        patientNT.setDemographics(listDemosPatient);
        orderNT.setPatient(patientNT);
        // DEMOGRAFICOS ORDEN :
        List<DemographicValue> listDemosOrder = new LinkedList<>();
        //DEMO CODIFICADOS Y HOMOLOGADOS ORDEN
        for (DemographicNT demographic : order.getDemographics())
        {
            nameValues(demographic);
            switch (demographic.getId())
            {
                case Constants.BRANCH:
                    Integer idItemDemo = demographicHomologado(demographic, centralSystemConf);
                    if (idItemDemo != null)
                    {
                        Branch branch = branchService.get(idItemDemo, null, null, null);
                        orderNT.setBranch(branch);
                    } else
                    {
                        responseNT.setOrder(0l);
                        responseNT.setError("ERROR FOR BRACH IN ORDER EXTERNAL :" + orderNT.getExternalId());
                        OrderCreationLog.info("ERROR FOR BRACH IN ORDER EXTERNAL :" + orderNT.getExternalId());
                        return responseNT;
                    }
                    break;
                case Constants.PHYSICIAN:
                    idItemDemo = demographicHomologado(demographic, centralSystemConf);
                    if (idItemDemo != null)
                    {
                        Physician physician = physicianService.filterById(idItemDemo);
                        orderNT.setPhysician(physician);
                    }
                    break;
                case Constants.ACCOUNT:
                    idItemDemo = demographicHomologado(demographic, centralSystemConf);
                    if (idItemDemo != null)
                    {
                        Account account = accountService.get(idItemDemo, null, null, null, null);
                        orderNT.setAccount(account);
                    }
                    break;
                case Constants.RATE:
                    idItemDemo = demographicHomologado(demographic, centralSystemConf);
                    if (idItemDemo != null)
                    {
                        Rate rate = rateService.get(idItemDemo, null, null);
                        orderNT.setRate(rate);
                    }
                    break;
                case Constants.SERVICE:
                    idItemDemo = demographicHomologado(demographic, centralSystemConf);
                    if (idItemDemo != null)
                    {
                        ServiceLaboratory serviceLaboratory = serviceService.filterById(idItemDemo);
                        orderNT.setService(serviceLaboratory);
                    }
                    break;

                default:
                    if (!demographic.getEncode())
                    {
                        listDemosOrder.add(new DemographicValue(demographic));
                    } else
                    {
                        DemographicValue demoEcode = getEncodeDemographic(centralSystemConf, demographic.getId(), demographic.getCode());
                        listDemosOrder.add(demoEcode);
                    }
                    break;
            }
        }
        //INSERTA DEMOS DE ORDEN
        orderNT.setDemographics(listDemosOrder);
        // Examenes de la orden
        List<Test> tests = new LinkedList<>();
        List<Sample> samples = new LinkedList<>();
        List<Integer> samplesIds = new LinkedList<>();

        //HashMap PARA ACTAULIZAR LAB75C49 EN INGRESO DE ORDENES HIS
        //key => idtest ,value => objeto para realizar el update
        HashMap<Integer, RequestItemCentralCode> codeHisIdtestsForUpdate = new HashMap<Integer, RequestItemCentralCode>();

        for (TestNT testNT : order.getTests())
        {
            Integer idTest = centralSystemDao.getHomologationIdTest(centralSystemConf, testNT.getCode());
            if (idTest != null)
            {

                codeHisIdtestsForUpdate.put(idTest, new RequestItemCentralCode(idTest, testNT.getKeyHIS()));
                net.cltech.enterprisent.domain.masters.test.Test testAux = testService.get(idTest, null, null, null);
                if (testAux != null)
                {
                    Test test = new Test();
                    test.setCodeCups(testNT.getKeyHIS());
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
                    if (!samplesIds.contains(testAux.getSample().getId()))
                    {
                        samplesIds.add(testAux.getSample().getId());
                        //lista de samples
                        Sample sample = sampleDao.getSampleByTest(test.getId());
                        samples.add(sample);
                    }

                }
            }
        }
        // Si la orden no contiene examenes no se podra crear la misma
        if (!tests.isEmpty())
        {
            orderNT.setSamples(samples);
            orderNT.setTests(tests);
            OrderCreationLog.info("ManejoTarifa " + daoConfig.get("ManejoTarifa").getValue());
            if("True".equals(daoConfig.get("ManejoTarifa").getValue()))
            {
                //Validad tarifa por defecto
                int idTarifa = Integer.parseInt(daoConfig.get("TarifaPorDefecto").getValue());
                if (orderNT.getRate().getId() == null && idTarifa != 0)
                {
                    orderNT.setRate(rateService.get(idTarifa, null, null));
                }
            }
            
            Order createdOrder = new Order();
            
            //mirar objeto
            if(!orderIngreso.getOptions().getUpdateGroupBy())
            {
                //creacion de la orden
                createdOrder = orderService.createIngreso(orderNT, tokenUser.getId());
                OrderCreationLog.info("ORDER  IN NUMBER " + createdOrder.getOrderNumber() + " IS CREATE");
            }
            else
            {
                //consulta orden HIS
                Long numberOrder = orderDao.getOrderLisHis(orderNT.getExternalId()).size() > 0 ? orderDao.getOrderLisHisByDays(orderNT.getExternalId(), orderIngreso.getDaysUpdate()).get(0) : null;
                
                //Validacion para creacion o actualizacion de la orden
                if (numberOrder != null && numberOrder > 0)
                {
                    //Actualizacion de la orden
                    try
                    {
                        orderNT.setOrderNumber(numberOrder);
                        createdOrder = orderService.update(orderNT, tokenUser.getId(), orderNT.getBranch().getId(),0);
                        OrderCreationLog.info("ORDER  IN NUMBER " + createdOrder.getOrderNumber() + " IS UPDATE");

                    } catch (Exception e)
                    {
                        createdOrder.setOrderNumber(0l);
                        responseNT.setError("ERROR UPDATE ORDERHIS :" + orderNT.getExternalId());
                        OrderCreationLog.info("ERROR UPDATE ORDERHIS :" + orderNT.getExternalId());
                        OrderCreationLog.info(e.toString());

                    }

                } else
                {
                    //creacion de la orden
                    createdOrder = orderService.createIngreso(orderNT, tokenUser.getId());
                    OrderCreationLog.info("ORDER  IN NUMBER " + createdOrder.getOrderNumber() + " IS CREATE");
                }
            }

            long orderNumber = createdOrder.getOrderNumber();

            if (orderNumber != 0l)
            {
                // ACTUAILZA CODIGO HIS EN LA ORDEN CREADA POR EXAMEN
                createdOrder.getTests()
                        .stream()
                        .filter(test -> codeHisIdtestsForUpdate.keySet().contains(test.getId()))
                        .forEach(test ->
                        {
                            OrderCreationLog.info("UPDATE CODE HIS  : " + codeHisIdtestsForUpdate.get(test.getId()).getCentralCode() + " TEST :" + test.getId());
                            try
                            {
                                integrationIngresoDao.updateCentralCode(codeHisIdtestsForUpdate.get(test.getId()), orderNumber, 1);
                            } catch (Exception ex)
                            {
                                OrderCreationLog.info("ERROR UPDATE  CODE HIS AL TEST : " + test.getId() + " CON ERROR :" + ex);
                            }
                        });
                responseNT.setOrder(orderNumber);
            }

        } else
        {
            OrderCreationLog.info("NO EXISTEN EXAMENES");
            responseNT.setError("Examenes no existen");
        }
        long time2 = System.currentTimeMillis();
        OrderCreationLog.info("TIME TO CREATE ORDER : " + (time2 - time1) + " ms");
        return responseNT;
    }

    private void nameValues(DemographicNT demographic)
    {
        if (!demographic.getEncode() || demographic.getName().isEmpty())
        {
            demographic.setName(demographic.getCode());
        }
    }

    private Integer demographicHomologado(DemographicNT demographicNT, int centralSystemConf)
    {
        try
        {
            return centralSystemDao.getIdItemDemoByHomologationCode(centralSystemConf, demographicNT.getId(), demographicNT.getCode());
        } catch (Exception ex)
        {
            return null;
        }
    }

    /**
     * Consulta y retorna el demografico codificados completamente cargado
     *
     * @return Demográfico dinamico
     */
    private DemographicValue getEncodeDemographic(int centralSystem, int idDemo, String demoItemValue)
    {
        try
        {
            DemographicValue demoEncode = new DemographicValue();

            Integer idItemDemo = centralSystemDao.getIdItemDemoByHomologationCode(centralSystem, idDemo, demoItemValue);
            Demographic demo = demographicService.get(idDemo, null, null);
            if (idItemDemo != null)
            {
                List<DemographicItem> demoItem = demographicItemService.get(idItemDemo, null, null, null, true);
                demoEncode.setIdDemographic(demo.getId());
                demoEncode.setDemographic(demo.getName());
                demoEncode.setEncoded(demo.isEncoded());
                demoEncode.setCodifiedId(demoItem.get(0).getId());
                demoEncode.setCodifiedCode(demoItem.get(0).getCode());
                demoEncode.setCodifiedName(demoItem.get(0).getName());
            } else
            {
                IntegrationHisLog.info("DEMO ID " + demo.getId() + " NO HOMOLOGADO EN  CENTRAL ID " + centralSystem);
                demoEncode.setIdDemographic(demo.getId());
                demoEncode.setDemographic(demo.getName());
                demoEncode.setEncoded(demo.isEncoded());
                demoEncode.setNotCodifiedValue(demoItemValue);
            }
            return demoEncode;
        } catch (Exception e)
        {
            return new DemographicValue();
        }
    }

    @Override
    public ResponseOrderNT getOrderNT(RequestMigracion requestMigracion) throws Exception
    {
        ResponseOrderNT responseOrderNT = new ResponseOrderNT();
        Order order = get(Long.parseLong(requestMigracion.getOrder()));
        if (order != null)
        {
            responseOrderNT.setDigitosOrden(configurationService.getValue("DigitosOrden"));
            responseOrderNT.setSeparadorMuestra(configurationService.getValue("SeparadorMuestra"));
            List<String> myListDemos = new ArrayList<>(Arrays.asList(requestMigracion.getDemos().split(",")));
            List<SampleNT> samples = new ArrayList<>();
            for (ResultTest resultTest : order.getResultTest())
            {
                net.cltech.enterprisent.domain.masters.test.Test testAux = testService.get(null, resultTest.getTestCode(), null, null);
                if (testAux != null)
                {
                    Sample sample = sampleDao.getSampleByTest(testAux.getId());
                    SampleNT sampleNT = MigrationMapper.toDtoSampleNT(sample);
                    sampleNT.getTest().add(new TestNT(testAux));
                    if (!samples.contains(sampleNT))
                    {
                        samples.add(sampleNT);
                    }
                }
            }
            responseOrderNT.setType(order.getType().getName());
            responseOrderNT.setSampleNT(samples);
            PatientNT patientNT = MigrationMapper.toDtoOrderNT(order).getPatient();
            List<DemographicNT> demographicNTs = new ArrayList<>();
            for (DemographicNT demographicNT : patientNT.getDemographics())
            {
                if (myListDemos.contains(String.valueOf(demographicNT.getId())))
                {
                    demographicNTs.add(demographicNT);
                }
            }

            patientNT.setDemographics(demographicNTs);
            responseOrderNT.setPatientNT(patientNT);
        } else
        {
            responseOrderNT.setType("ORDER " + requestMigracion.getOrder() + " DOES NOT EXIST");
        }
        return responseOrderNT;
    }

    @Override
    public Order get(long orderNumber) throws Exception
    {
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);
        
        List<Demographic> demos = demographicService.list().stream().filter((Demographic t) -> t.getOrigin().equals("O") && t.isState()).collect(Collectors.toList());
        Order order = orderDao.get(orderNumber, demos, laboratorys, idbranch);
        
        if (order != null)
        {
            order.setPatient(patientService.get(order.getPatient().getId()));
            List<ResultTest> allTests = resultService.get(orderNumber);
            List<ResultTest> tests = allTests
                    .stream()
                    .filter(t -> t.isEntry())
                    .map(t -> t.setValidatedChilds(childsWithValidation(t, allTests)))
                    .collect(Collectors.toList());
            String result = tests.stream()
                    .map(n -> String.valueOf(n.getTestCode()))
                    .collect(Collectors.joining("-", "{", "}"));
            OrderCreationLog.error(result);
            order.setResultTest(tests);
            order.setComments(commentService.listCommentOrder(order.getOrderNumber(), null));
            order.getPatient().setDiagnostic(commentService.listCommentOrder(null, order.getPatient().getId()));
            for (ResultTest resultTest : order.getResultTest())
            {
                if (resultTest.getHasTemplate())
                {
                    resultTest.setOptionsTemplate(microbiologyService.getGeneralTemplate(order.getOrderNumber(), resultTest.getTestId()).getOptionTemplates());
                }
            }
            return order;
        } else
        {
            return null;
        }
    }

    @Override
    public Order getEntry(long orderNumber) throws Exception
    {
        Order order = get(orderNumber);
        if (order != null)
        {
            order.setResultTest(order.getResultTest().stream().filter(r -> r.getPackageId() == 0).collect(Collectors.toList()));

//            order.setBillingAccount(orderDao.getBillingAccountByOrder(orderNumber));
        }
        return order;
    }

    public int childsWithValidation(ResultTest test, List<ResultTest> allTests)
    {
        return allTests.stream()
                .filter(t -> t.getPackageId() == test.getTestId() || t.getProfileId() == test.getTestId())
                .filter(t -> t.getState() >= LISEnum.ResultTestState.VALIDATED.getValue())
                .collect(Collectors.toList())
                .size();
    }
}

package net.cltech.enterprisent.service.impl.enterprisent.integration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationIngresoDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.CentralSystemDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.SampleDao;
import net.cltech.enterprisent.dao.interfaces.operation.list.OrderListDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.domain.DTO.integration.order.DemographicDto;
import net.cltech.enterprisent.domain.DTO.integration.order.MapperIngresoOrder;
import net.cltech.enterprisent.domain.DTO.integration.order.OrderDto;
import net.cltech.enterprisent.domain.DTO.integration.order.ServiceResponse;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.HL7Socket.LoggerInterface;
import net.cltech.enterprisent.domain.integration.HL7Socket.SimpleMLLPBasedTCPClient;
import net.cltech.enterprisent.domain.integration.ingreso.RequestBarCode;
import net.cltech.enterprisent.domain.integration.ingreso.RequestCentralCode;
import net.cltech.enterprisent.domain.integration.ingreso.RequestDemographicAuto;
import net.cltech.enterprisent.domain.integration.ingreso.RequestDemographicIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.RequestHomologationDemographicIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.RequestHomologationTestIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.RequestItemBarCode;
import net.cltech.enterprisent.domain.integration.ingreso.RequestItemCentralCode;
import net.cltech.enterprisent.domain.integration.ingreso.RequestStateTest;
import net.cltech.enterprisent.domain.integration.ingreso.RequestTest;
import net.cltech.enterprisent.domain.integration.ingreso.RequestTestIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.RequestTestStatus;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsDemographicIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsHomologationDemographicIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsHomologationTestIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsItemSample;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsSample;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsSonControl;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsTestIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.ResponseBarCode;
import net.cltech.enterprisent.domain.integration.ingreso.ResponseItemBarCode;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.demographic.StandardizationDemographic;
import net.cltech.enterprisent.domain.masters.test.Profile;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.Standardization;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.orders.CommentOrder;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
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
import net.cltech.enterprisent.service.interfaces.masters.demographic.RaceService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.ServiceService;
import net.cltech.enterprisent.service.interfaces.masters.test.CentralSystemService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de Integracion de ingreso para Enterprise NT
 *
 * @author BValero
 * @version 1.0.0
 * @see Creacion
 * @since 22/01/2020
 */
@Service
public class IntegrationIngresoServiceEnterpriseNT implements IntegrationIngresoService
{

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CentralSystemDao dao;
    @Autowired
    private DemographicItemService demographicItemService;
    /*Demograficos fijos*/
    @Autowired
    private AccountService accountService;
    @Autowired
    private PhysicianService physicianService;
    @Autowired
    private RateService rateService;
    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private RaceService raceService;
    @Autowired
    private DocumentTypeService documentService;
    @Autowired
    private CentralSystemService centralSystemService;
    @Autowired
    private DemographicService demographicService;
    @Autowired
    private IntegrationIngresoDao integrationIngresoDao;
    /* Otros servicios requerios */
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private SampleDao sampleDao;
    @Autowired
    private TestService testService;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ListService listService;
    private Timer checkResultsWorker;
    private MapperIngresoOrder mapper;
    @Autowired
    private IntegrationIngresoService serviceIngreso;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderListDao orderListDao;

    private static final char END_OF_BLOCK = 28;//'\u001c';
    private static final char START_OF_BLOCK = 11;//'\u000b';
    private static final char CARRIAGE_RETURN = 13;//\ufe0c
    LoggerInterface log = new LoggerInterface();
    
    @Override
    public ResponsHomologationDemographicIngreso demographicHomologation(RequestHomologationDemographicIngreso homo) throws Exception
    {
        ResponsHomologationDemographicIngreso res = new ResponsHomologationDemographicIngreso();
        res.setIdSystem(homo.getIdSystem());
        res.setDemographic(homo.getDemographic().stream()
                .map(demografico -> Homologar(homo.getIdSystem(), demografico.getIdDemographic(), demografico.getIdItemDemographicHis(), homo.getField()))
                .collect(Collectors.toList()));
        return res;
    }

    private ResponsDemographicIngreso Homologar(int idCentralSystem, int idDemographic, String idItemDemographicHis, String field)
    {
        ResponsDemographicIngreso homologado = new ResponsDemographicIngreso();
        try
        {
            List<StandardizationDemographic> demographics = dao.demographicsItemList(idCentralSystem, idDemographic);
            switch (idDemographic)
            {
                case Constants.ACCOUNT:
                    homologado = accountService.list(true).stream()
                            .map(account -> new ResponsDemographicIngreso(idDemographic, getCode(demographics, account.getId(), idItemDemographicHis), account.getId(), account.getName()))
                            .filter(filter -> filter.getIdItemDemographicHis() != null)
                            .findAny().orElse(null);
                    break;
                case Constants.PHYSICIAN:
                    homologado = physicianService.filterByState(true).stream()
                            .map(physician -> new ResponsDemographicIngreso(idDemographic, getCode(demographics, physician.getId(), idItemDemographicHis), physician.getId(), physician.getName()))
                            .filter(filter -> filter.getIdItemDemographicHis() != null)
                            .findAny().orElse(null);
                    break;
                case Constants.RATE:
                    homologado = rateService.list(true).stream()
                            .map(rate -> new ResponsDemographicIngreso(idDemographic, getCode(demographics, rate.getId(), idItemDemographicHis), rate.getId(), rate.getName()))
                            .filter(filter -> filter.getIdItemDemographicHis() != null)
                            .findAny().orElse(null);
                    break;
                case Constants.ORDERTYPE:
                    homologado = orderTypeService.filterByState(true).stream()
                            .map(orderType -> new ResponsDemographicIngreso(idDemographic, getCode(demographics, orderType.getId(), idItemDemographicHis), orderType.getId(), orderType.getName()))
                            .filter(filter -> filter.getIdItemDemographicHis() != null)
                            .findAny().orElse(null);
                    break;
                case Constants.BRANCH:
                    homologado = branchService.list(true).stream()
                            .map(branch -> new ResponsDemographicIngreso(idDemographic, getCode(demographics, branch.getId(), idItemDemographicHis), branch.getId(), branch.getName()))
                            .filter(filter -> filter.getIdItemDemographicHis() != null)
                            .findAny().orElse(null);
                    break;
                case Constants.SERVICE:
                    homologado = serviceService.filterByState(true).stream()
                            .map(service -> new ResponsDemographicIngreso(idDemographic, getCode(demographics, service.getId(), idItemDemographicHis), service.getId(), service.getName()))
                            .filter(filter -> filter.getIdItemDemographicHis() != null)
                            .findAny().orElse(null);
                    break;
                case Constants.RACE:
                    homologado = raceService.filterByState(true).stream()
                            .map(race -> new ResponsDemographicIngreso(idDemographic, getCode(demographics, race.getId(), idItemDemographicHis), race.getId(), race.getName()))
                            .filter(filter -> filter.getIdItemDemographicHis() != null)
                            .findAny().orElse(null);
                    break;
                case Constants.DOCUMENT_TYPE:
                    homologado = documentService.list(true).stream()
                            .map(documentType -> new ResponsDemographicIngreso(idDemographic, getCode(demographics, documentType.getId(), idItemDemographicHis), documentType.getId(), documentType.getName()))
                            .filter(filter -> filter.getIdItemDemographicHis() != null)
                            .findAny().orElse(null);
                    break;
                case Constants.PATIENT_SEX:
                    homologado = listService.list(6).stream()
                            .map(sex -> new ResponsDemographicIngreso(idDemographic, getCode(demographics, sex.getId(), idItemDemographicHis), sex.getId(), sex.getEsCo()))
                            .filter(filter -> filter.getIdItemDemographicHis() != null)
                            .findAny().orElse(null);
                    break;
                default:
                    homologado = demographicItemService.get(null, null, null, idDemographic, null).stream()
                            .filter(filter -> filter.isState())
                            .map(demographicItem -> new ResponsDemographicIngreso(idDemographic, getCode(demographics, demographicItem.getId(), idItemDemographicHis), demographicItem.getId(), demographicItem.getName()))
                            .filter(filter -> filter.getIdItemDemographicHis() != null)
                            .findAny().orElse(null);
                    break;
            }
            return homologado;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene una peticion de homologacion de examenes y retorna una lista de
     * examenes homologados en respuesta a dicha peticion
     *
     * @return Objeto de respuesta - ResponsTestIngreso
     * @throws Exception Error de base de datos
     */
    @Override
    public ResponsHomologationTestIngreso testHomologation(RequestHomologationTestIngreso requestHomologationTestIngreso) throws Exception
    {
        ResponsHomologationTestIngreso respons = new ResponsHomologationTestIngreso();
        List<ResponsTestIngreso> listTest = new LinkedList<>();
        List<Standardization> listStand = centralSystemService.standardizationList(requestHomologationTestIngreso.getIdSystem(), false);
        if (listStand != null && !listStand.isEmpty())
        {
            for (RequestTestIngreso test : requestHomologationTestIngreso.getTest())
            {
                ResponsTestIngreso obj = new ResponsTestIngreso();
                obj.setIdItemTestHis(test.getIdTestHis());
                obj.setnName(test.getNameTest());
                for (Standardization st : listStand)
                {
                    for (String c : st.getCodes())
                    {
                        if(c != null && c.equalsIgnoreCase(test.getIdTestHis())){
                            obj.setIdItemTestLis(st.getId());
                            obj.setCodeTestLis(st.getCode());
                        }
                    }
                }
              
                listTest.add(obj);
                
            }
        }
        respons.setTest(listTest);
        respons.setIdSystem(requestHomologationTestIngreso.getIdSystem());
        return respons;
    }

    /**
     * Obtiene los codigos de homologación de demografico item
     *
     * @param demographics Lista de Demograficos items con codigo de
     * homologación
     * @param id Id Demografico Item
     * @param idDemographicHIS
     * @return lista de codigos de homologación
     */
    public String getCode(List<StandardizationDemographic> demographics, Integer id, String idDemographicHIS)
    {
        StandardizationDemographic obj = new StandardizationDemographic();
        List<String> listCentralCode = new ArrayList<>();
        listCentralCode.add(idDemographicHIS);
        obj.setCentralCode(listCentralCode);
        if (Objects.equals(demographics.get(demographics.indexOf(obj)).getDemographicItem().getId(), id))
        {
            return demographics.get(demographics.indexOf(obj)).getCentralCode().get(0).toUpperCase();
        } else
        {
            return null;
        }
    }

    /**
     * Obtiene una peticion de homologacion de demografico y se encarga de
     * verificar que no exista, en dado caso que este demografico no exista, lo
     * crea y lo homologa.
     *
     * @param requestDemographic
     * @return True si lo creo satisfactoriamente, y false si seguramente ya
     * estaba creado
     * @throws Exception Error de base de datos
     */
    @Override
    public boolean autoCreationDemographic(RequestDemographicAuto requestDemographic) throws Exception
    {
        /**
         * FALTANTE: UNIFICAR METODOS DE CREACION DE ITEM DEMOGRAFICO (FIJOS Y
         * DINAMICOS)
         */
        ResponsHomologationDemographicIngreso res = new ResponsHomologationDemographicIngreso();
        RequestHomologationDemographicIngreso demoG = new RequestHomologationDemographicIngreso();
        RequestDemographicIngreso demo = new RequestDemographicIngreso();
        List<RequestDemographicIngreso> lista = new ArrayList<>();
        StandardizationDemographic homologacion = new StandardizationDemographic();
        Demographic objAux = new Demographic();
        AuthorizedUser session = JWT.decode(request);
        demo.setIdDemographic(requestDemographic.getIdDemographic());
        demo.setIdItemDemographicHis(requestDemographic.getIdItemDemographicHis());
        lista.add(demo);
        demoG.setIdSystem(requestDemographic.getIdSystem());
        demoG.setDemographic(lista);
        try
        {
            res = demographicHomologation(demoG);
            if (res.getDemographic().get(0) == null)
            {
                List<Demographic> demoAll = demographicService.demographicsList();
                Demographic objget = new Demographic(requestDemographic.getIdDemographic(), null, true);
                objAux = demoAll.get(demoAll.indexOf(objget));
                //Creme el demografico  
                DemographicItem demoItem = new DemographicItem();
                demoItem.setCode(requestDemographic.getIdItemDemographicHis());
                // Si este campo descItemDemographic llega vacio enviaremos el codigo del HIS
                demoItem.setName(requestDemographic.getDescItemDemographic().isEmpty() ? requestDemographic.getIdItemDemographicHis() : requestDemographic.getDescItemDemographic());
                demoItem.setState(true);
                demoItem.setDescription("");
                demoItem.setDemographic(requestDemographic.getIdDemographic());
                demoItem.setDefaultItem(true);
                demoItem.setDemographicName(objAux.getName());
                demoItem.setUser(new AuthorizedUser(session.getId()));
                //Lista de central code (IdHIS)(CUPS)
                List<String> ls = new ArrayList<>();
                ls.add(requestDemographic.getIdItemDemographicHis());
                //Objeto Aux
                DemographicItem aux = new DemographicItem();
                //Se crea el demografico
                aux = demographicItemService.create(demoItem);
                //Se homologa el demografico
                homologacion.setId(requestDemographic.getIdSystem());
                homologacion.setDemographicItem(aux);
                homologacion.setDemographic(new Demographic(requestDemographic.getIdDemographic(), "", true));
                homologacion.setCentralCode(ls);
                homologacion.setUser(new AuthorizedUser(session.getId()));
                //Se genera la homologacion
                centralSystemService.insertStandardizationDemographic(homologacion);
                return true;
            } else
            {
                //Consultamos el demográfico por su nombre
                DemographicItem aux = demographicItemService.get(null, null, requestDemographic.getDescItemDemographic(), null, null).get(0);
                // Si este existe, lo homologamos con lo que se espera que deba estar homologado
                if (aux != null)
                {
                    boolean alreadyApproved = centralSystemService.standardizationCodeExists(requestDemographic.getIdSystem(), requestDemographic.getIdItemDemographicHis(), requestDemographic.getIdDemographic(), aux.getId());
                    if (!alreadyApproved)
                    {
                        //Lista de central code (IdHIS)(CUPS)
                        List<String> ls = new ArrayList<>();
                        ls.add(requestDemographic.getIdItemDemographicHis());
                        //Se homologa el demografico
                        homologacion.setId(requestDemographic.getIdSystem());
                        homologacion.setDemographicItem(aux);
                        homologacion.setDemographic(new Demographic(requestDemographic.getIdDemographic(), "", true));
                        homologacion.setCentralCode(ls);
                        homologacion.setUser(new AuthorizedUser(session.getId()));
                        //Se genera la homologacion
                        centralSystemService.insertStandardizationDemographic(homologacion);
                        return true;
                    } else
                    {
                        return false;
                    }
                } else
                {
                    return false;
                }
            }
        } catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public int updateCentralCode(RequestCentralCode requestItemCentralCode) throws Exception
    {
        try
        {
            int affectedResults = 0;
            List<String> errors = new LinkedList<>();
            int typeTest;
            //Actualiza una orden
            if (requestItemCentralCode.getIdOrder() != 0)
            {
                IntegrationHisLog.info("ENTRO PRIMER CON CODIGO " + requestItemCentralCode.getIdOrder());
                if (checkResultsWorker == null)
                {
                    checkResultsWorker = new Timer(true);
                    checkResultsWorker.schedule(new CheckResultsWorker(), 5000, 5000);
                }
                //affectedResults += integrationIngresoDao.updateTestCups(requestItemCentralCode.getIdOrder());
                for (RequestItemCentralCode itemAux : requestItemCentralCode.getItemOrder())
                {
                    
                    typeTest = integrationIngresoDao.identifyTestType(itemAux.getIdTest());
                    IntegrationHisLog.info("TIPO DE TEST  " + typeTest + " PARA EL TSST CON ID " + itemAux.getIdTest());
                    if (typeTest == 0)
                    {
                        affectedResults += integrationIngresoDao.updateCentralCode(itemAux, requestItemCentralCode.getIdOrder(), typeTest);
                    } else if (typeTest == 1)
                    {
                        List<net.cltech.enterprisent.domain.masters.test.Test> childsPanel = testService.getChilds(itemAux.getIdTest());
                        RequestItemCentralCode itemToSave = new RequestItemCentralCode();
                        for (net.cltech.enterprisent.domain.masters.test.Test test : childsPanel)
                        {
                            itemToSave.setCentralCode(itemAux.getCentralCode());
                            itemToSave.setIdTest(test.getId());
                            affectedResults += integrationIngresoDao.updateCentralCode(itemToSave, requestItemCentralCode.getIdOrder(), typeTest);
                        }
                        affectedResults += integrationIngresoDao.updateCentralCode(itemAux, requestItemCentralCode.getIdOrder(), typeTest);
                    }
                }

            } else //Actualiza una lista de ordenes
            {
                IntegrationHisLog.info("ENTRO SEGUNDO  CODIGO ");
                int cantidad = 0;
                if (requestItemCentralCode.getOrders() != null && !requestItemCentralCode.getOrders().isEmpty())
                {
                    for (Long order : requestItemCentralCode.getOrders())
                    {
                        cantidad++;
                        for (RequestItemCentralCode itemAux : requestItemCentralCode.getItemOrder())
                        {
                            if(itemAux.getIdTest() != 0){
                                String originalCode = itemAux.getCentralCode();
                                String newCentralcode = itemAux.getCentralCode() + "-" + cantidad;
                                itemAux.setCentralCode(newCentralcode);
                                typeTest = integrationIngresoDao.identifyTestType(itemAux.getIdTest());

                                if (typeTest < 3)
                                {
                                    if (typeTest == 0)
                                    {
                                        affectedResults += integrationIngresoDao.updateCentralCode(itemAux, order, typeTest);
                                    } else if (typeTest == 1)
                                    {
                                        List<net.cltech.enterprisent.domain.masters.test.Test> childsPanel = testService.getChilds(itemAux.getIdTest());
                                        RequestItemCentralCode itemToSave = new RequestItemCentralCode();
                                        for (net.cltech.enterprisent.domain.masters.test.Test test : childsPanel)
                                        {
                                            itemToSave.setCentralCode(itemAux.getCentralCode());
                                            itemToSave.setIdTest(test.getId());
                                            affectedResults += integrationIngresoDao.updateCentralCode(itemToSave, order, typeTest);
                                        }
                                        affectedResults += integrationIngresoDao.updateCentralCode(itemAux, order, typeTest);
                                    }

                                    IntegrationHisLog.info("ENTRO TIPO DE TEST 3 orden" + order + typeTest);
                                    //affectedResults += integrationIngresoDao.updateCentralCode(itemAux, order, typeTest);
                                    itemAux.setCentralCode(originalCode);
                                } else
                                {
                                    errors.add("0|unexpected value");
                                }
                                if (errors.size() > 0)
                                {
                                    throw new EnterpriseNTException(errors);
                                }
                            }
                        }
                    }
                }
            }
            return affectedResults;
        } catch (Exception e)
        {
            OrderCreationLog.error(e);
            return 0;
        }
    }

    /**
     * Con el objeto que viene entrando obtenemos los parametros necesarios para
     * hacer una consulta detallada de las muestras por id de orden y id de
     * prueba
     *
     * @param requestCentralCode
     * @return
     * @throws Exception
     */
    @Override
    public ResponsSample getSampleByIdOrderAndIdTest(RequestCentralCode requestCentralCode) throws Exception
    {
        // Creo los objetos requeridos para el objeto de envio
        ResponsSample sample = new ResponsSample();
        List<ResponsItemSample> listSamplesRes = new ArrayList<>();
        String abreviaturaByExam = "";
        List<Sample> listMuestraOrden = new ArrayList<>();
        List<net.cltech.enterprisent.domain.masters.test.Test> listTestAux = new ArrayList<>();
        int muestraAux = 0;
        // Obtenemos los examenes asignados a un perfil
        int theSameList = 0;
        for (RequestItemCentralCode itemTests : requestCentralCode.getItemOrder())
        {
            abreviaturaByExam += "." + integrationIngresoDao.getAbbreviationByIdTest(itemTests.getIdTest()).replace(".", "");
        }
        if (requestCentralCode.getItemOrder() != null)
        {
            for (int i = 0; i < requestCentralCode.getItemOrder().size(); i++)
            {
                theSameList = requestCentralCode.getItemOrder().size();
                for (RequestItemCentralCode requestItem : requestCentralCode.getItemOrder())
                {
                    net.cltech.enterprisent.domain.masters.test.Test possibleProfile = testService.get(requestItem.getIdTest(), null, null, null);
                    if (possibleProfile.getSample().getId() == null)
                    {
                        List<Profile> profileWithExams = testService.getProfiles().stream().filter(profile -> Objects.equals(profile.getProfileId(), requestItem.getIdTest())).collect(Collectors.toList());
                        // Si la lista no esta vacia significa que ese id de examen realmente representa un examen:
                        if (!profileWithExams.isEmpty())
                        {
                            requestCentralCode.getItemOrder().removeIf(item -> item.getIdTest() == requestItem.getIdTest());
                            profileWithExams.removeIf(profile -> profile.getTestType() == 1);
                            String idsMatchesSample = "";
                            String idsTests = "";
//                            for(Profile objProfile : profileWithExams)
//                            {
//                                RequestItemCentralCode objItem = new RequestItemCentralCode();
//                                objItem.setIdTest(objProfile.getTestId());
//                                requestCentralCode.getItemOrder().add(objItem);   
//                            }
                            for (int j = 0; j < profileWithExams.size(); j++)
                            {
                                net.cltech.enterprisent.domain.masters.test.Test testAux = testService.get(profileWithExams.get(j).getTestId(), null, null, null);
                                String idMatchesSample = String.valueOf(testAux.getSample().getId());
                                idsMatchesSample += "," + idMatchesSample;
                                idsTests += "," + testAux.getId();
                                profileWithExams.removeIf(fintTest -> Objects.equals(fintTest.getTestId(), testAux.getId()));
                                j--;
                                RequestItemCentralCode objItem = new RequestItemCentralCode();
                                objItem.setIdTest(testAux.getId());
                                requestCentralCode.getItemOrder().add(objItem);
                            }
                            idsMatchesSample = idsMatchesSample.substring(1);
                            idsTests = idsTests.substring(1);
                            String arrMatchesSample[] = idsMatchesSample.split(",");
                            String arrIdsTests[] = idsTests.split(",");
                            for (int h = 0; h < arrIdsTests.length; h++)
                            {
                                net.cltech.enterprisent.domain.masters.test.Test testAuxTwo = testService.get(possibleProfile.getId(), null, null, null);
                                testAuxTwo.setProfileTest(Integer.parseInt(arrIdsTests[h]));
                                testAuxTwo.getSample().setId(Integer.parseInt(arrMatchesSample[h]));
                                listTestAux.add(testAuxTwo);
                            }
                            i--;
                            break;
                        }
                    } else if (listTestAux.stream().filter(findTests -> Objects.equals(findTests.getId(), possibleProfile.getId())).findFirst().orElse(null) == null && listTestAux.stream().filter(findTestsTwo -> Objects.equals(findTestsTwo.getProfileTest(), possibleProfile.getId())).findFirst().orElse(null) == null)
                    {
                        listTestAux.add(possibleProfile);
                    }
                }
                // Si la lista de items que entro es la misma que salio
                // Esto significaria que no habrían perfiles dentro de esta
                // Pero si su tamaño es mayor, un perfil habria sido remplazado por sus respectivos analitos
                if (theSameList == requestCentralCode.getItemOrder().size())
                {
                    break;
                }
            }
        }
        for (RequestItemCentralCode itemTests : requestCentralCode.getItemOrder())
        {
            Sample sampleAux = sampleDao.getSampleByTest(itemTests.getIdTest());
            muestraAux = muestraAux == 0 ? 0 : muestraAux;
            if (muestraAux != sampleAux.getId())
            {
                if (!listMuestraOrden.contains(sampleAux))
                {
                    listMuestraOrden.add(sampleAux);
                    muestraAux = sampleAux.getId();
                }
            } else
            {
                muestraAux = 0;
            }
        }
        String abreviaturaByArea = "";
        for (Sample samItem : listMuestraOrden)
        {
            ResponsItemSample responItem = new ResponsItemSample();
            List<ResponsItemSample> listSamplesAux = new ArrayList<>();
            for (net.cltech.enterprisent.domain.masters.test.Test itemTestsTwo : listTestAux)
            {
                if (Objects.equals(itemTestsTwo.getSample().getId(), samItem.getId()))
                {
                    List<ResponsItemSample> listSamplesAuxTwo = integrationIngresoDao.getSampleByIdOrderAndIdTest(requestCentralCode.getIdOrder(), samItem.getId(), itemTestsTwo.getId());
                    listSamplesAux.addAll(listSamplesAuxTwo);
                }
            }
            if (listSamplesAux != null && !listSamplesAux.isEmpty())
            {
                for (ResponsItemSample rs : listSamplesAux)
                {
                    abreviaturaByArea += abreviaturaByArea.contains(rs.getAbreviaturaExamen()) ? "" : "." + rs.getAbreviaturaExamen().replace(".", "");
                }
                responItem.setIdSample(listSamplesAux.get(0).getIdSample());
                responItem.setLab24c9(listSamplesAux.get(0).getLab24c9());
                responItem.setLab24c2(listSamplesAux.get(0).getLab24c2());
                responItem.setLab56c2(listSamplesAux.get(0).getLab56c2());
                responItem.setLab24c3(listSamplesAux.get(0).getLab24c3());
                responItem.setLab43c4(abreviaturaByArea.substring(1));
                responItem.setExamenesEtiquetaAdicional(abreviaturaByExam.substring(1));
                listSamplesRes.add(responItem);
            }
            abreviaturaByArea = "";
        }
        sample.setIdOrder(requestCentralCode.getIdOrder());
        sample.setItemSamples(listSamplesRes);
        return sample;
    }

    /**
     * Obtendra la informacion necesaria para imprimir el codigo de barras
     *
     * @return
     * @throws Exception error al cargar algn componente del objeto
     */
    @Override
    public ResponseBarCode getBarcode(RequestBarCode requestBarCode) throws Exception
    {
        try
        {
            ResponseBarCode infoBarCode = new ResponseBarCode();
            List<ResponseItemBarCode> listItemBarCode = new ArrayList<>();
            infoBarCode.setLab22C1(requestBarCode.getLab22C1());
            infoBarCode.setLab21C2(requestBarCode.getLab21C2());
            infoBarCode.setLab22C12(requestBarCode.getLab22C12());
            infoBarCode.setLab21C4C5(requestBarCode.getLab21C4C5());
            infoBarCode.setLab21C6(requestBarCode.getLab21C6());
            infoBarCode.setLab22C14(requestBarCode.getLab22C14());
            infoBarCode.setLab21C10(requestBarCode.getLab21C10());
            infoBarCode.setEdad(requestBarCode.getEdad() + " Años");
            infoBarCode.setClave("123");
            infoBarCode.setHomonimo("");
            infoBarCode.setInicialesPaciente(Integer.parseInt(configurationService.get("InicialesPaciente").getValue()));
            infoBarCode.setHis(Integer.parseInt(configurationService.get("HIS").getValue()));
            // Cargo el item del objeto de respuesta
            for (RequestItemBarCode requestItemBarCode : requestBarCode.getDataSample())
            {
                ResponseItemBarCode responseItemBarCode = new ResponseItemBarCode();
                responseItemBarCode.setLab24C9(requestItemBarCode.getLab24C9());
                responseItemBarCode.setLab24C2(requestItemBarCode.getLab24C2());
                responseItemBarCode.setLab56C2(requestItemBarCode.getLab56C2());
                responseItemBarCode.setLab24C4(Integer.parseInt(configurationService.get("PrintPointBarCode").getValue()));
                responseItemBarCode.setLab43C4(requestItemBarCode.getLab43C4());
                responseItemBarCode.setLab24C3(requestItemBarCode.getLab24C3());
                responseItemBarCode.setExamenesEtiquetaAdicional(requestItemBarCode.getExamenesEtiquetaAdicional());
                listItemBarCode.add(responseItemBarCode);
            }
            infoBarCode.setDataSample(listItemBarCode);
            return infoBarCode;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Resultados de mensaje transmitido al cliente
     *
     * @param idOrder
     * @param executedFrom
     * @return respuesta del mensaje
     * @throws Exception Error en conexion al socket.
     */
    @Override
    @SuppressWarnings("UnusedAssignment")
    public String getMessageTest(long idOrder, String executedFrom, Integer muestra, String examenes, List<ResultTest> deletetest) throws Exception
    {
        try
        {
            IntegrationHisLog.info("ENTRA A ENVIAR MENSAJES");
            String serverHis = "";
            String codHis = "";
            String executedFromClone = "";
            String ambito = "";
            int countOBRS = 0;
            String[] fragmentedMessage;
            String[] separatedBy;
            int portHis = 0;
            int centralSystemHis = 0;
            List<String> OBRS = new ArrayList<>();
            List<String> tests = examenes == null || examenes.isEmpty() ? new ArrayList<>() : Arrays.asList(examenes.split("\\|"));
            serverHis = configurationService.get("serverHIS").getValue();
            portHis = Integer.parseInt(configurationService.get("portHIS").getValue());
            centralSystemHis = Integer.parseInt(configurationService.get("centralSystemHIS").getValue());
            //Se obtienen los datos de la orden si tienen el estado 2
            ResponsSonControl objControl = new ResponsSonControl();
            objControl = integrationIngresoDao.ordersSonControl(idOrder);
            int state = 0;
            if (objControl != null)
            {
                String ruta = "";
                ruta = configurationService.get("rutaLogHIS").getValue();
                SimpleMLLPBasedTCPClient obj = new SimpleMLLPBasedTCPClient(ruta);
                // Obtenemos el ambito
                fragmentedMessage = objControl.getMensaje().split("[\n\r]");
                separatedBy = fragmentedMessage[2].split("\\|");
                ambito = separatedBy[15];
                IntegrationHisLog.info("AMBITO DE LA ORDEN " + ambito);
                // Obtenemos la cantidad de estudios totales
                for (String line : fragmentedMessage)
                {
                    if (line.split("\\|")[0].equalsIgnoreCase("OBR"))
                    {
                        countOBRS++;
                    }
                }
                //Si es diferente de null se buscan los examenes segun el evento donde se ejecuto
                //Si es rechazho o verificacion se buscan los examenes segun la muestra y se busca su codigo de homologacion
                //Si es desde eliminacion solo se homologan los ids de los examenes eliminados
                //Finalmente se concatenan los codigos de homologacion de los examenes con un | y envian a la clase que genera el mensaje
                if (muestra != null)
                {
                    //buscar los idHIS de los examenes relacionados a esa muestra
                    codHis = homologationCodesForSample(muestra, centralSystemHis);
                    IntegrationHisLog.info("Codigos his muestra" + codHis);
                }
                else {
                    codHis = homologationCodesForIdTest(examenes, centralSystemHis);
                    IntegrationHisLog.info("Codigos his examen" + codHis);
                }
                
                if (!codHis.equals(""))
                {
                    Order orderNT = orderService.getEntry(idOrder);
                    
                    if (orderNT != null)
                    {
                        IntegrationHisLog.info("numero orden" + orderNT.getOrderNumber());
                        List<ResultTest> testToSend = new ArrayList<>();
                        IntegrationHisLog.info("exameens " + tests);
                        
                        if (!executedFrom.equalsIgnoreCase("elimination")) // HD
                        {
                            if("Homebound".equals(tests.get(0))){
                                IntegrationHisLog.info("Envio desde homebound");
                                testToSend = orderNT.getResultTest().stream().filter(t -> t.getSampleId() == muestra).collect(Collectors.toList());
                                
                            }
                            else{
                                for (int i = 0; i < tests.size(); i++) {
                                    IntegrationHisLog.info("exameens1" + tests.get(i));
                                    int test = Integer.parseInt(tests.get(i));
                                    ResultTest resultTest = orderNT.getResultTest().stream().filter(t -> t.getTestId() == test).findAny().orElse(null) ;
                                    if(resultTest != null){
                                        testToSend.add(resultTest);
                                    }
                                }
                            }
                        }
                        else
                        {
                            testToSend = deletetest;
                        }
                        
                        IntegrationHisLog.info("testToSend" + Tools.jsonObject(testToSend));

                        for (ResultTest resultTest : testToSend)
                        {
                            if (!executedFrom.equalsIgnoreCase("elimination")) // HD
                            {
                                IntegrationHisLog.info("testToSend parameters " + resultTest.getTestId() + " " + orderNT.getOrderNumber());
                                // Obtenemos la cantidad para ese estudio en esa orden
                                String codeAndQuantity = integrationIngresoDao.getHomologationCodeAndQuantity(resultTest.getTestId(), orderNT.getOrderNumber());
                                IntegrationHisLog.info("codeAndQuantity" + codeAndQuantity);
                                
                                if(codeAndQuantity != null && !codeAndQuantity.isEmpty())
                                {
                                    String[] separate = codeAndQuantity.split("-");
                                    IntegrationHisLog.info("separate" + Tools.jsonObject(separate));
                                    String codHisCopy = codHis;
                                    IntegrationHisLog.info("codHisCopy" + codHisCopy);

                                    List<String> codHisList = Arrays.asList(codHisCopy.split("\\|"));
                                
                                    if (!codeAndQuantity.isEmpty())
                                    {
                                        IntegrationHisLog.info("codeAndQuantity" + codeAndQuantity);
                                        codHisCopy = codHisList.stream().filter(searchCode -> Objects.equals(searchCode, separate[0])).findAny().orElse("");
                                        IntegrationHisLog.info("codHisCopy" + codHisCopy);
                                        objControl.setCantidad(separate[1]);
                                        IntegrationHisLog.info("separate" + Tools.jsonObject(separate));
                                    }
                                    if (codHisCopy.isEmpty())
                                    {
                                        IntegrationHisLog.info("[ERROR] El codigo: " + separate[0] + " No fue encontrado dentro del mensaje HL7");
                                    } else //if (!objControl.getPerfiles().contains(codHisCopy))
                                    {
                                        IntegrationHisLog.info("[INICIO] - Proceso de creación del ORM_OUT");
                                        // Si este estudio es un perfil lo guardamos dentro de la tabla de control
                                        IntegrationHisLog.info("test type" + resultTest.getTestType());
                                        if (resultTest.getTestType() == 1)
                                        {
                                            String profiles = objControl.getPerfiles().isEmpty() ? codHisCopy + "|" : objControl.getPerfiles() + codHisCopy + "|";
                                            integrationIngresoDao.updateOrderProfiles(objControl.getIdOrder(), profiles);
                                        }
                                        //Se genera el mensaje y se envia a santafe
                                        try
                                        {
                                            IntegrationHisLog.info("ENTRA A ENVIAR MENSAJES DE LA ORDEN " + executedFrom);
                                            if (executedFrom.equalsIgnoreCase("received")) // SC
                                            {
                                                state = 2;
                                            } else if (executedFrom.equalsIgnoreCase("check")) // IP
                                            {
                                                state = 3;
                                            } else if (executedFrom.equalsIgnoreCase("pending")) // HD
                                            {
                                                state = 4;
                                            } else if (executedFrom.equalsIgnoreCase("retakes")) // HD
                                            {
                                                state = 5;
                                            } else if (executedFrom.equalsIgnoreCase("rejection")) // HD
                                            {
                                                state = 6;
                                            } else if (executedFrom.equalsIgnoreCase("elimination")) // HD
                                            {
                                                state = 7;
                                            }else if (executedFrom.equalsIgnoreCase("result")) // CM
                                            {
                                                state = 8;
                                            }
                                            IntegrationHisLog.info("ESTADO ENVIADO state" + state);
                                            IntegrationHisLog.info("ESTADO ENVIADO objControl.getEstado()" + objControl.getEstado());
                                            // Se lleva a cabo esta validación con de notificar al HIS con los estados SC e IP
                                            // Si por algun motivo el mensaje de notificacion SC no fue enviado desde Homebound
                                            if (state == 2)
                                            {
                                                countOBRS *= 2;
                                                // SC
                                                executedFromClone = "received";
                                                objControl.setState(2);
                                                IntegrationHisLog.info("1.IF ENTRA A ENVIAR MENSAJES");
                                                OBRS = sendMessage(objControl, serverHis, portHis, executedFromClone, codHisCopy, idOrder, objControl.getCuenta());
                                                IntegrationHisLog.info("OBRS " + OBRS);
                                                IntegrationHisLog.info("1.IF TERMINA WHILE DE ENVIO");
                                                // Obtenemos la cantidad de estudios enviados para esa orden
                                                updateStatusBySubmittedTest(objControl, OBRS, countOBRS);
                                                OBRS = new ArrayList<>();
                                            } else if (state == 3)
                                            {
                                                countOBRS *= 3;
                                                // SC
                                                executedFromClone = "received";
                                                objControl.setState(2);
                                                // Obtenemos la cantidad de estudios enviados para esa orden
                                                IntegrationHisLog.info("2.IF ENTRA A ENVIAR MENSAJES received");

                                                IntegrationHisLog.info("OBRS " + OBRS);
                                                OBRS = sendMessage(objControl, serverHis, portHis, executedFromClone, codHisCopy, idOrder, objControl.getCuenta());

                                                IntegrationHisLog.info("2.IF TERMINA WHILE DE ENVIO received");
                                                // Actualizamos cantidad de estudios enviados
                                                updateStatusBySubmittedTest(objControl, OBRS, countOBRS);
                                                OBRS = new ArrayList<>();
                                                // IP
                                                executedFromClone = "check";
                                                objControl.setState(3);
                                                // Obtenemos la cantidad de estudios enviados para esa orden
                                                IntegrationHisLog.info("2.IF EMPIEZA WHILE DE ENVIO check");

                                                IntegrationHisLog.info("OBRS " + OBRS);
                                                OBRS = sendMessage(objControl, serverHis, portHis, executedFromClone, codHisCopy,idOrder, objControl.getCuenta());

                                                IntegrationHisLog.info("2.IF TERMINA WHILE DE ENVIO check");
                                                // Actualizamos cantidad de estudios enviados
                                                updateStatusBySubmittedTest(objControl, OBRS, countOBRS);
                                                OBRS = new ArrayList<>();
                                            }
                                            else {
                                                objControl.setState(state);
                                                // Obtenemos la cantidad de estudios enviados para esa orden
                                                IntegrationHisLog.info("7.IF EMPIEZA WHILE DE ENVIO");


                                                OBRS = sendMessage(objControl, serverHis, portHis, executedFrom, codHisCopy, idOrder, objControl.getCuenta());
                                                IntegrationHisLog.info("OBRS " + OBRS);
                                                IntegrationHisLog.info("7.IF termina WHILE DE ENVIO");
                                                // Actualizamos cantidad de estudios enviados
                                                updateStatusBySubmittedTest(objControl, OBRS, countOBRS);
                                                OBRS = new ArrayList<>();
                                            }
                                        } catch (Exception ex)
                                        {
                                            IntegrationHisLog.error("Error al enviar mensaje HIS" + ex);
                                            return ex.getMessage();
                                        }
                                    }
                                }
                                else{
                                    IntegrationHisLog.error("EXAMEN SIN CDIGO DE HOMOLOGACION ASIGNADO" + resultTest.getTestId() + " ORDEN " + orderNT.getOrderNumber());
                                }
                            }
                            else{
                                state = 7;
                                objControl.setState(state);
                                // Obtenemos la cantidad de estudios enviados para esa orden
                                IntegrationHisLog.info("7.IF EMPIEZA WHILE DE ENVIO");
                                    
                                OBRS = sendMessage(objControl, serverHis, portHis, executedFrom, codHis, idOrder, objControl.getCuenta());
                                IntegrationHisLog.info("OBRS " + OBRS);
                                
                                IntegrationHisLog.info("7.IF termina WHILE DE ENVIO");
                                // Actualizamos cantidad de estudios enviados
                                updateStatusBySubmittedTest(objControl, OBRS, countOBRS);
                                OBRS = new ArrayList<>();
                            }
                        }
                        return "true";
                    } else
                    {
                        IntegrationHisLog.error("No se encontro ninguna orden asociada");
                        return "No se encontro ninguna orden asociada";
                    }
                } else
                {
                    IntegrationHisLog.error("No hay examenes homologados");
                    return "No hay examenes homologados";
                }
            } else
            {
                IntegrationHisLog.error("La orden no existe en la tabla de control");
                return "La orden no existe en la tabla de control";
            }
        } catch (Exception e)
        {
            IntegrationHisLog.error("Error al enviar mensaje HIS" + e);
            e.getMessage();
            return "";
        }
    }
    
    public List<String> sendMessage(ResponsSonControl objControl, String server, int port, String executedFrom, String codHis, long order, String cuenta) throws Exception
    {
        // Creacion del socket enviando server y puerto
        IntegrationHisLog.info("********************************************************************************************");
        IntegrationHisLog.info("********************************************************************************************");
        IntegrationHisLog.info("--------------------------------------------------------------------------------------------");
        IntegrationHisLog.info("Conectado al server: " + server + " puerto: " + port);
        IntegrationHisLog.info("--------------------------------------------------------------------------------------------");
        StringBuilder testHL7MessageToTransmit = new StringBuilder();
        StringBuilder messageOrder = new StringBuilder();
        messageOrder.append(objControl.getMensaje());
        //actualizar fecha del mensaje
        Calendar c = Calendar.getInstance();
        String anio = Integer.toString(c.get(Calendar.YEAR));
        String mes = Integer.toString(c.get(Calendar.MONTH) + 1);
        mes = Integer.parseInt(mes) < 10 ? 0 + mes : mes;
        String day = Integer.toString(c.get(Calendar.DATE));
        day = Integer.parseInt(day) < 10 ? 0 + day : day;
        String hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
        hour = Integer.parseInt(hour) < 10 ? 0 + hour : hour;
        String minute = Integer.toString(c.get(Calendar.MINUTE));
        minute = Integer.parseInt(minute) < 10 ? 0 + minute : minute;
        String second = Integer.toString(c.get(Calendar.SECOND));
        second = Integer.parseInt(second) < 10 ? 0 + second : second;
        String fechahora = anio + mes + day + hour + minute + second;
        //Ahora se obtiene el mensaje y se actualiza la fecha
        String[] msg = messageOrder.toString().split("[\n\r]");
        int val = 0;
        String[] ORC;
        String[] MSH;
        String[] PID;
        String[] PV1AUX;
        String estado = "";
        //List<String> OBRS = new ArrayList<>();
        
        List<RequestStateTest> OBRS = new ArrayList<>();
        List<String> OBRSSEND = new ArrayList<>();
        StringBuilder OBRSAVE = new StringBuilder();
        Integer count = 0;
        if (executedFrom.equalsIgnoreCase("received"))
        {
            estado = "SC";
        }
        else if (executedFrom.equalsIgnoreCase("check"))
        {
            estado = "IP";
        }
        else if (executedFrom.equalsIgnoreCase("pending"))
        {
            estado = "HD";
        }
        else if (executedFrom.equalsIgnoreCase("retakes"))
        {
            estado = "HD";
        }
        else if (executedFrom.equalsIgnoreCase("rejection"))
        {
            estado = "HD";
        }
        else if (executedFrom.equalsIgnoreCase("result"))
        {
            estado = "CM";
        }
        else if (executedFrom.equalsIgnoreCase("elimination"))
        {
            estado = "CA";
        }
        
        IntegrationHisLog.info("Estado a enviar: " + estado);
        IntegrationHisLog.info("--------------------------------------------------------------------------------------------");
        if (codHis.isEmpty())
        {
            IntegrationHisLog.info("No se encontro ningun codigo de homologacion para esa muestra o examen");
            // Close the socket and its streams
            IntegrationHisLog.info("Cerrando conexion al server: " + server + " puerto: " + port);
            IntegrationHisLog.info("********************************************************************************************");
            IntegrationHisLog.info("********************************************************************************************");
            return new ArrayList<>();
        }
        String[] examenes = codHis.split("\\|");
        testHL7MessageToTransmit.append(START_OF_BLOCK);
        
        for (String line : msg)
        {
            if (!line.equals(""))
            {
                if (line.split("\\|")[0].equals("ORC") && val == 0)
                {
                    ORC = line.split("\\|");
                    ORC[1] = "SC";
                    ORC[5] = estado;
                    testHL7MessageToTransmit.append(String.join("|", ORC));
                    testHL7MessageToTransmit.append(CARRIAGE_RETURN);
                    val++;
                }
                else if (line.split("\\|")[0].equals("MSH"))
                {
                    MSH = line.split("\\|");
                    MSH[2] = "LAB";
                    MSH[4] = "HIS";
                    MSH[6] = fechahora;
                    testHL7MessageToTransmit.append(String.join("|", MSH));
                    testHL7MessageToTransmit.append(CARRIAGE_RETURN);
                }
                else if (line.split("\\|")[0].equals("OBR"))
                {
                    String[] lineAux = line.split("\\|");
                    count++;
                    
                    RequestStateTest requestStateTest = new RequestStateTest();
                   
                    
                    if (testInOrc(line.split("\\|")[4].split("\\^")[0], examenes, estado, order, requestStateTest, cuenta))
                    {
                        lineAux[1] = count.toString();
                        lineAux[5] = objControl.getCantidad();
                        line = "";
                        line = String.join("|", lineAux);
                        
                        requestStateTest.setOBR(line);
                        OBRS.add(requestStateTest);
                    }
                    // Asignamos la cantidad a la que corresponde este estudio
                    lineAux[1] = count.toString();
                    lineAux[5] = objControl.getCantidad();
                    line = "";
                    line = String.join("|", lineAux);
                    OBRSAVE.append(line);
                    OBRSAVE.append(CARRIAGE_RETURN);
                }
                else if (!line.split("\\|")[0].equals("ORC") && !line.split("\\|")[0].equals("OBR") && !line.split("\\|")[0].equals("MSH") && !line.split("\\|")[0].equals("PV2") && !line.split("\\|")[0].equals("IN1"))
                {
                    if (line.split("\\|")[0].equals("PID"))
                    {
                        PID = line.split("\\|");
                        PID[4] = "";
                        testHL7MessageToTransmit.append(String.join("|", PID));
                        testHL7MessageToTransmit.append(CARRIAGE_RETURN);
                    }
                    else if (line.split("\\|")[0].equals("PV1"))
                    {
                        PV1AUX = line.split("\\|");
                        PV1AUX[2] = PV1AUX[15];
                        PV1AUX[5] = PV1AUX[19];
                        testHL7MessageToTransmit.append(String.join("|", PV1AUX));
                        testHL7MessageToTransmit.append(CARRIAGE_RETURN);
                    }
                    else
                    {
                        testHL7MessageToTransmit.append(line);
                        testHL7MessageToTransmit.append(CARRIAGE_RETURN);
                    }
                }
            }
        }
        
        IntegrationHisLog.info("Tamaño del OBR: " + OBRS.size());
        if (OBRS.size() > 0)
        {
            IntegrationHisLog.info("ENTRA A OBRS: " + Tools.jsonObject(OBRS));
            //Se realiza el envio por examen
            for (RequestStateTest OBR : OBRS)
            {
                
                StringBuilder HeadORM_OUT = new StringBuilder(testHL7MessageToTransmit.toString());
                HeadORM_OUT.append(OBR.getOBR());
                HeadORM_OUT.append(END_OF_BLOCK);
                HeadORM_OUT.append(CARRIAGE_RETURN);
                
                IntegrationHisLog.info("Mensaje enviado: ");
                IntegrationHisLog.info(HeadORM_OUT.toString());
                
                //integrationIngresoDao.createStatusByTest(OBR.getStatustest());
                
                
                    try(Socket socket = new Socket(server, port); InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream())
                    {
                        OBRSSEND.add(OBR.getOBR());
                        // Send the MLLP-wrapped HL7 message to the server
                        out.write(HeadORM_OUT.toString().getBytes("UTF-8"));
                        byte[] byteBuffer = new byte[200];
                        in.read(byteBuffer);
                        String ACK = new String(byteBuffer);
                        IntegrationHisLog.info("Respuesta recibida: ");
                        IntegrationHisLog.info(ACK);
                        IntegrationHisLog.info("--------------------------------------------------------------------------------------------");
                        out.flush();
                        out.close();
                        in.close();
                        socket.close();

                        IntegrationHisLog.info("cierra socket");
                        /*String[] validACK = ACK.split("\\|");
                        
                        if(validACK.length <= 17){
                            if("AE".equals(validACK[15])){
                                IntegrationHisLog.info("CON ERROR MENSAJ EHI: ");
                                OBR.getStatustest().setSendWithError(OBR.getStatustest().getSendWithError() + 1);
                            }
                        }*/
                        
                        
                        IntegrationHisLog.info("accion tabla de control " + OBR.getStatustest().getAction());
                        if(OBR.getStatustest().getAction() == 0){
                            
                            integrationIngresoDao.createStatusByTest(OBR.getStatustest());
                            IntegrationHisLog.info("inserta" +  Tools.jsonObject(OBR.getStatustest()));
                        }
                        else {
                            integrationIngresoDao.updateStateTest(OBR.getStatustest().getOrder(), OBR.getStatustest().getHomologationCode(), 0 ,OBR.getStatustest().getSendWithError() );
                            IntegrationHisLog.info("actualiza" + OBR.getStatustest().getOrder() + " " + OBR.getStatustest().getHomologationCode());
                        }
                        
                        /*if(OBR.getStatustest().getSendWithError() == 0){
                            break;
                        }*/
                    }
                    catch(IOException ex)
                    {
                        IntegrationHisLog.error("--------------------------------------------------------------------------------------------");
                        IntegrationHisLog.error("[EXCEPTION] " + ex);
                        IntegrationHisLog.error("--------------------------------------------------------------------------------------------");
                        // Close the socket and its streams
                        IntegrationHisLog.error("Cerrando conexion al server: " + server + " puerto: " + port);
                        IntegrationHisLog.error("********************************************************************************************");
                        IntegrationHisLog.error("********************************************************************************************");
                        return OBRSSEND;
                    }
                
            }
            IntegrationHisLog.info("TERMINA OBRS: ");
        }
        else
        {
            IntegrationHisLog.info("El examen a enviar: " + codHis + "No se encuentra en los OBR del mensaje original: ");
            IntegrationHisLog.info(OBRSAVE.toString());
        }
        IntegrationHisLog.info("--------------------------------------------------------------------------------------------");
        // Close the socket and its streams
        IntegrationHisLog.info("Cerrando conexion al server: " + server + " puerto: " + port);
        IntegrationHisLog.info("********************************************************************************************");
        IntegrationHisLog.info("********************************************************************************************");
        return OBRSSEND;
    }

    public Boolean testInOrc(String inputStr, String[] items, String state, Long ordernumber, RequestStateTest requestStateTest, String cuenta)
    {
        inputStr = inputStr.trim();
        for (String item : items)
        {
            if (inputStr.equalsIgnoreCase(item.trim()))
            {
                boolean validstatetest = true;
                RequestTestStatus order = new RequestTestStatus();
                
                order.setOrder(ordernumber);
                order.setAccountNumber(cuenta);
                order.setStatus(state);
                order.setHomologationCode(item.trim());
                order.setNumberSending(1);
                order.setSendWithError(0);
                
                requestStateTest.setStatustest(order);
                
                try {
                    IntegrationHisLog.info("etado busqueda de examenes" + state);
                    //lista de estados
                    if("SC".equals(state)){

                        IntegrationHisLog.info("orden " + ordernumber + "examen " + item );
                        List<RequestTestStatus> liststate = integrationIngresoDao.listStatusByTest(ordernumber, item);
                        
                        int filterstate = liststate.stream().filter((RequestTestStatus t) -> t.getStatus().equals("SC") && t.getSendWithError() == 0).collect(Collectors.toList()).size();
                        order.setAction(liststate.stream().filter((RequestTestStatus t) -> t.getStatus().equals("SC")).collect(Collectors.toList()).size() > 0 ? 1 : 0);

                        return filterstate == 0;
                    }
                    else if("IP".equals(state)){
                        IntegrationHisLog.info("orden IP " + ordernumber + "examen " + item );
                        List<RequestTestStatus> liststate = integrationIngresoDao.listStatusByTest(ordernumber, item);

                        int filterstateSC = liststate.stream().filter((RequestTestStatus t) -> t.getStatus().equals("SC") && t.getSendWithError() == 0).collect(Collectors.toList()).size();
                        IntegrationHisLog.info("filterstateSC IP " + filterstateSC);
                        int filterstateIP = liststate.stream().filter((RequestTestStatus t) -> t.getStatus().equals("IP") && t.getSendWithError() == 0).collect(Collectors.toList()).size();
                        IntegrationHisLog.info("filterstateSC IP " + filterstateIP);
                        order.setAction(liststate.stream().filter((RequestTestStatus t) -> t.getStatus().equals("IP")).collect(Collectors.toList()).size() > 0 ? 1 : 0);
                        return filterstateSC > 0 && filterstateIP == 0;
                    }
                    else {
                        IntegrationHisLog.info("ACCION  NADA");
                        order.setAction(0);
                        return true;
                    }
                }
                catch(Exception e){
                
                }
                return validstatetest;
            }
        }
        return false;
    }


    /**
     * Obtendra los codigos de homologación de todos los examenes relacionados
     * con una muestra
     *
     * @param idSample
     * @param centralSystem
     * @return
     * @throws Exception error al cargar algn componente del objeto
     */
    public String homologationCodesForSample(int idSample, int centralSystem) throws Exception
    {
        try
        {
            String concatCodHis = "";
            List<String> listCodHIS = integrationIngresoDao.homologationCodesForSample(idSample, centralSystem);
            concatCodHis = listCodHIS.stream().map((codHis) -> codHis + "|").reduce(concatCodHis, String::concat);
            return concatCodHis;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtendra los codigos de homologación de todos los examenes por id
     *
     * @param idTest
     * @param centralSystem
     * @return
     * @throws Exception error al cargar algn componente del objeto
     */
    public String homologationCodesForIdTest(String idTest, int centralSystem) throws Exception
    {
        try
        {
            String concatCodHis = "";
            List<String> listCodHIS = integrationIngresoDao.homologationCodesForIdTest(idTest.replace("|", ","), centralSystem);
            concatCodHis = listCodHIS.stream().map((codHis) -> codHis + "|").reduce(concatCodHis, String::concat);
            return concatCodHis;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene todas las ordenes en el LIS que correspondan a ese id del HIS
     *
     * @param externalId
     * @return Lista de ordenes
     * @throws Exception Error al conectar con la base de datos
     */
    @Override
    public List<Order> getOrdersByOrderHIS(String externalId) throws Exception
    {
        try
        {
            List<Long> listIdOrders = ordersDao.getOrdersLisByIdHis(externalId);
            List<Order> listOrders = new ArrayList<>();
            if (listIdOrders.size() > 0)
            {
                for (Long idOrder : listIdOrders)
                {
                    Order order = orderService.get(idOrder);
                    if (order.isActive())
                    {
                        List<Test> listTest = integrationIngresoDao.getListTest(idOrder);
                        if (listTest.size() > 0)
                        {
                            order.setTests(listTest);
                        }
                        if (order != null)
                        {
                            listOrders.add(order);
                        }
                    }
                }
            }
            return listOrders;
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene todas las ordenes en el LIS que correspondan a ese id del HIS
     *
     * @param externalId
     * @return Lista de ordenes
     * @throws Exception Error al conectar con la base de datos
     */
    @Override
    public Boolean getValidOrdersByHIS(String externalId, String branch) throws Exception
    {
        try
        {
            List<Long> listIdOrders = ordersDao.getOrdersLisByIdHis(externalId);
            return listIdOrders.size() > 0;
        } catch (Exception e)
        {
            e.getMessage();
            return false;
        }
    }

    /**
     * Obtiene todas las ordenes en el LIS que correspondan a ese id del HIS
     *
     * @param system
     * @return Lista de ordenes
     * @throws Exception Error al conectar con la base de datos
     */
    @Override
    public ResponsHomologationDemographicIngreso getDemographicCentralSystem(Integer system) throws Exception
    {
        try
        {
            ResponsHomologationDemographicIngreso demo = new ResponsHomologationDemographicIngreso();
            demo.setIdSystem(system);
            List<ResponsDemographicIngreso> demographics = new ArrayList();
            demographics = integrationIngresoDao.getDemographicCentralSystem(system);
            demo.setDemographic(demographics);
            return demo;
        } catch (Exception e)
        {
            e.getMessage();
            return null;
        }
    }

    public void updateStatusBySubmittedTest(ResponsSonControl objControl, List<String> OBRS, int countOBRS) throws Exception
    {
        int obrsSent = integrationIngresoDao.getStudiesSent(objControl.getIdOrder());
        if (!OBRS.isEmpty())
        {
            if (obrsSent == 0 && OBRS.size() == 1)
            {
                obrsSent++;
                if (obrsSent == countOBRS)
                {
                    integrationIngresoDao.updateStateOrder(objControl.getIdOrder(), objControl.getState());
                    integrationIngresoDao.updateStudiesSent(objControl.getIdOrder(), 0);
                } else
                {
                    integrationIngresoDao.updateStudiesSent(objControl.getIdOrder(), obrsSent);
                }
            } else if (obrsSent == 0 && OBRS.size() > 1)
            {
                obrsSent += OBRS.size();
                if (obrsSent == countOBRS)
                {
                    integrationIngresoDao.updateStateOrder(objControl.getIdOrder(), objControl.getState());
                    integrationIngresoDao.updateStudiesSent(objControl.getIdOrder(), 0);
                } else
                {
                    integrationIngresoDao.updateStudiesSent(objControl.getIdOrder(), obrsSent);
                }
            } else if (obrsSent > 0 && OBRS.size() == 1)
            {
                obrsSent++;
                if (obrsSent == countOBRS)
                {
                    integrationIngresoDao.updateStateOrder(objControl.getIdOrder(), objControl.getState());
                    integrationIngresoDao.updateStudiesSent(objControl.getIdOrder(), 0);
                } else
                {
                    integrationIngresoDao.updateStudiesSent(objControl.getIdOrder(), obrsSent);
                }
            } else if (obrsSent > 0 && OBRS.size() > 1)
            {
                obrsSent += OBRS.size();
                if (obrsSent == countOBRS)
                {
                    integrationIngresoDao.updateStateOrder(objControl.getIdOrder(), objControl.getState());
                    integrationIngresoDao.updateStudiesSent(objControl.getIdOrder(), 0);
                } else
                {
                    integrationIngresoDao.updateStudiesSent(objControl.getIdOrder(), obrsSent);
                }
            }
        }
    }

    @Override
    public RequestTest getTestsByOrderHIS(int idTest, int idBranch, int orderType) throws Exception
    {
        try
        {
            return integrationIngresoDao.getTestsByOrderHIS(idTest, idBranch, orderType);
        } catch (Exception e)
        {
            e.getMessage();
            return null;
        }
    }

    /**
     * Crea un codigo aleatorio para un nuevo item demografico
     *
     * @param nameDemographicItem
     * @param idDemographic
     * @return Codigo de homologación
     * @throws Exception Error al conectar con la base de datos
     */
    @Override
    public String createRandomCode(String nameDemographicItem, int idDemographic) throws Exception
    {
        try
        {
            DemographicItem demo = integrationIngresoDao.obtainReferringPhysician(nameDemographicItem);
            String newHomologationCode = "";
            if (demo == null)
            {
                String[] dividedName = new String[2];
                dividedName[0] = nameDemographicItem.split(" ")[0];
                if (nameDemographicItem.split(" ").length == 1)
                {
                    // Si el nombre del item demografico viene solo con una palabra
                    // tomamos el primer y ultimo caracter de la unica palabra enviada
                    dividedName[1] = dividedName[0].substring((dividedName[0].length() - 1), dividedName[0].length());
                } else
                {
                    dividedName[1] = nameDemographicItem.split(" ")[1];
                }
                boolean existCode = true;
                Integer newCodeInt = 0;
                String newCodeS = "0000";
                while (existCode)
                {
                    newCodeInt++;
                    newHomologationCode = "";
                    String initialOne = StringUtils.stripAccents(dividedName[0].substring(0, 1).toUpperCase());
                    String initialTwo = StringUtils.stripAccents(dividedName[1].substring(0, 1).toUpperCase());
                    newHomologationCode = initialOne + initialTwo + newCodeS.substring(0, newCodeS.length() - newCodeInt.toString().length()) + newCodeInt.toString();
                    existCode = !integrationIngresoDao.obtainNameApprovedReferringPhysician(idDemographic, newHomologationCode).equals("");
                }
            } else
            {
                newHomologationCode = demo.getCode();
            }
            return newHomologationCode;
        } catch (Exception e)
        {
            return "";
        }
    }

    @Override
    public int updateCentralCodeMyM(RequestCentralCode requestItemCentralCode) throws Exception
    {
        int affectedResults = 0;
        List<String> errors = new ArrayList<>();
        int typeTest = 0;
        // 0 - Valor inesperado
        if (requestItemCentralCode.getIdOrder() != 0)
        {
            for (RequestItemCentralCode item : requestItemCentralCode.getItemOrder())
            {
                typeTest = 0;
                typeTest = integrationIngresoDao.identifyTestType(item.getIdTest());
                if (typeTest < 3)
                {
                    affectedResults += integrationIngresoDao.updateCentralCode(item, requestItemCentralCode.getIdOrder(), typeTest);
                } else
                {
                    errors.add("0|unexpected value");
                }
                if (errors.size() > 0)
                {
                    throw new EnterpriseNTException(errors);
                }
            }
        } else
        {
            for (Long order : requestItemCentralCode.getOrders())
            {
                for (RequestItemCentralCode itemAux : requestItemCentralCode.getItemOrder())
                {
                    typeTest = 0;
                    typeTest = integrationIngresoDao.identifyTestType(itemAux.getIdTest());
                    if (typeTest < 3)
                    {
                        affectedResults += integrationIngresoDao.updateCentralCode(itemAux, order, typeTest);
                    } else
                    {
                        errors.add("0|unexpected value");
                    }
                    if (errors.size() > 0)
                    {
                        throw new EnterpriseNTException(errors);
                    }
                }
            }
        }
        return affectedResults;
    }

    @Override
    public List<Order> getOrdersByOrderHISMinsa(String externalId, int branchId) throws Exception
    {
        try
        {
            // Consultamos la sede por su id:
            Branch branch = branchService.get(branchId, null, null, null);
            List<Long> listIdOrders = ordersDao.getOrdersLisByIdHis(externalId, branch.getCode());
            List<Order> listOrders = new ArrayList<>();
            if (listIdOrders.size() > 0)
            {
                for (Long idOrder : listIdOrders)
                {
                    Order order = orderService.get(idOrder);
                    if (order.isActive())
                    {
                        List<Test> listTest = orderListDao.getListTest(idOrder);
                        if (listTest.size() > 0)
                        {
                            order.setTests(listTest);
                        }
                        if (order != null)
                        {
                            listOrders.add(order);
                        }
                    }
                }
            }
            return listOrders;
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * clase para la verificacion de archivos de resultados
     */
    class CheckResultsWorker extends TimerTask
    {

        @Override
        public void run()
        {
            try
            {
                int affectedResults = 0;
                affectedResults += integrationIngresoDao.updateTestCups(0);
            } catch (Exception ex)
            {
                OrderCreationLog.error(ex);
            }
        }
    }

    @Override
    public ServiceResponse create(OrderDto orderDto)
    {
        User user;
        ServiceResponse serviceResponse = new ServiceResponse(orderDto.getOrder());
        try
        {
            user = userService.get(null, orderDto.getUser(), null, null);
        } catch (Exception ex)
        {
            serviceResponse.setMessage("User " + orderDto.getUser() + " not valid, not found.");
            return serviceResponse;
        }
        Order order = new Order();
        //Numero de la Orden 
        order.setOrderNumber(orderDto.getOrder());
        // Agrega commentario 
        CommentOrder commentOrder = new CommentOrder(orderDto.getComment());
        order.getComments().add(commentOrder);
        DemographicDto branch = new DemographicDto();
        order.setBranch(new Branch(orderDto.getDemographics().stream()
                .filter(demo -> demo.getName().equalsIgnoreCase("SEDE"))
                .findFirst().orElse(branch).getId()));
        DemographicDto serviceLaboratory = new DemographicDto();
        order.setService(new ServiceLaboratory(orderDto.getDemographics().stream().filter(demographic -> demographic.getName().equalsIgnoreCase("SERVICIO")).findFirst().orElse(serviceLaboratory).getId()));
        DemographicDto physician = new DemographicDto();
        order.setPhysician(new Physician(orderDto.getDemographics().stream().filter(demographic -> demographic.getName().equalsIgnoreCase("MEDICO")).findFirst().orElse(physician).getId()));
        DemographicDto account = new DemographicDto();
        order.setAccount(new Account(orderDto.getDemographics().stream().filter(demographic -> demographic.getName().equalsIgnoreCase("CLIENTE")).findFirst().orElse(account).getId()));
        DemographicDto rate = new DemographicDto();
        order.setRate(new Rate(orderDto.getDemographics().stream().filter(demographic -> demographic.getName().equalsIgnoreCase("TARIFA")).findFirst().orElse(rate).getId()));
        List<DemographicValue> orderDemographics = new LinkedList<>();
        orderDto.getDemographics().stream().map(demographic ->
        {
            if (demographic.isEncoded())
            {
                RequestDemographicAuto requestDemographicAuto = new RequestDemographicAuto(orderDto.getCentralSystemId(), demographic.getId(), demographic.getCode(), demographic.getName());
                try
                {
                    serviceIngreso.autoCreationDemographic(requestDemographicAuto);
                } catch (Exception ex)
                {
                    Logger.getLogger(IntegrationIngresoServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return demographic;
        }).forEachOrdered(demographic ->
        {
            DemographicValue demographicValue = new DemographicValue();
            demographicValue.setIdDemographic(demographic.getId());
            demographicValue.setCodifiedName(demographic.getName());
            demographicValue.setEncoded(demographic.isEncoded());
            demographicValue.setCodifiedCode(demographic.getCode());
            orderDemographics.add(demographicValue);
        });
        List<Test> tests = new LinkedList<>();
        List<Test> toDelete = new LinkedList<>();
        orderDto.getTests().forEach(test ->
        {
            if (test.getActionCode().equals("C"))
            {
                toDelete.add(mapper.toTest(test));
            }
            if (test.getActionCode().equals("A"))
            {
                tests.add(mapper.toTest(test));
            }
        });
        Date birthDate = null;
        try
        {
            birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(orderDto.getPatient().getBirthdate());
        } catch (ParseException ex)
        {
            IntegrationHisLog.info("Patient birthdate " + orderDto.getPatient().getBirthdate() + " not valid. Using actual date.");
        }
        Patient patient = new Patient();
        patient.setPatientId(orderDto.getPatient().getRecord());
        patient.setName1(orderDto.getPatient().getName());
        patient.setLastName(orderDto.getPatient().getName());
        patient.setSurName(orderDto.getPatient().getSecondLastName());
        patient.setSex(new Item(orderDto.getPatient().getGender()));
        patient.setBirthday(birthDate);
        patient.setDocumentType(orderDto.getPatient().getDocumentType());
        order.setPatient(patient);
        List<DemographicValue> patientDemographics = new LinkedList<>();
        orderDto.getPatient().getDemographics().stream().map(demographic ->
        {
            if (demographic.getName().equals("TIPO DE DOCUMENTO"))
            {
                order.getPatient().setDocumentType(new DocumentType(demographic.getId(), demographic.getCode(), demographic.getName(), true, null));
            }
            return demographic;
        }).forEach(demographic ->
        {
            DemographicValue demographicValue = new DemographicValue();
            demographicValue.setIdDemographic(demographic.getId());
            demographicValue.setCodifiedName(demographic.getName());
            demographicValue.setEncoded(demographic.isEncoded());
            demographicValue.setCodifiedCode(demographic.getCode());
            patientDemographics.add(demographicValue);
        });
        order.getPatient().setDemographics(patientDemographics);
        order.setDemographics(orderDemographics);
        order.setDeleteTests(toDelete);
        order.setTests(tests);
        try
        {
            orderService.create(order, user.getId(), order.getBranch().getId());
        } catch (Exception ex)
        {
            serviceResponse.setMessage("Order " + orderDto.getOrder() + " do not created, internal error.");
            return serviceResponse;
        }
        serviceResponse.setMessage("Order " + orderDto.getOrder() + " has been created succesfully.");
        return serviceResponse;
    }
}

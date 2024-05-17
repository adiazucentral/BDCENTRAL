package net.cltech.enterprisent.service.impl.enterprisent.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.integration.HomeBoundDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.dao.interfaces.operation.tracking.SampleTrackingDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.homebound.AccountHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.Appointment;
import net.cltech.enterprisent.domain.integration.homebound.BasicHomeboundPatient;
import net.cltech.enterprisent.domain.integration.homebound.BillingTestHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.ContainerHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.DemographicHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.DocumentTypeHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.GenderHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.ListOrders;
import net.cltech.enterprisent.domain.integration.homebound.PatientHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.PaymentTypeHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.ProfileHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.QuestionHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.RateHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.SampleHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.SampleHospitalHomebound;
import net.cltech.enterprisent.domain.integration.homebound.TestHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.Track;
import net.cltech.enterprisent.domain.integration.homebound.TransportSampleHomebound;
import net.cltech.enterprisent.domain.masters.billing.PaymentType;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.orders.CommentOrder;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderTestDetail;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.orders.TestPrice;
import net.cltech.enterprisent.domain.operation.tracking.SampleTracking;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationHomeBoundService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationSklService;
import net.cltech.enterprisent.service.interfaces.masters.billing.PaymentTypeService;
import net.cltech.enterprisent.service.interfaces.masters.billing.RateService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.AccountService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.BranchService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DocumentTypeService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.OrderTypeService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.PhysicianService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.ServiceService;
import net.cltech.enterprisent.service.interfaces.masters.test.SampleService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.common.CommentService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.StatisticService;
import net.cltech.enterprisent.service.interfaces.operation.tracking.SampleTrackingService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.StreamFilters;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.events.EventsLog;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de integración para Enterprise NT.
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 17/02/2020
 * @see Creacion
 */
@Service
public class IntegrationHomboundServiceEnterpriseNT implements IntegrationHomeBoundService
{

    @Autowired
    private HomeBoundDao homeBoundDao;
    @Autowired
    private DemographicService serviceDemographic;
    @Autowired
    private SampleTrackingDao sampleTrackingDao;
    @Autowired
    private SampleTrackingService sampleTrackingService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RateService rateService;
    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private PaymentTypeService paymentTypeService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private PhysicianService physicianService;
    @Autowired
    private TestService testService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private SampleService sampleService;
    @Autowired
    private ToolsDao toolsDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private IntegrationSklService integrationSklService;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private ConfigurationDao daoConfig;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private OrdersDao ordersDao;

    @Override
    public List<TestHomeBound> testHomeBound() throws Exception
    {
        return homeBoundDao.listTestHomeBound();
    }

    @Override
    public List<ProfileHomeBound> userPermissionHomeBound() throws Exception
    {
        List<ProfileHomeBound> testInformation = new ArrayList<>();
        testInformation = homeBoundDao.listProfileHomeBound();
        for (ProfileHomeBound test : testInformation)
        {

            if (test.getType() != 0)
            {
                test.setTests(homeBoundDao.getChildsByProfile(test.getId()));
            }

        }

        return testInformation;
    }

    @Override
    public List<Order> listFilters(ListOrders listOrders) throws Exception
    {

        try
        {

            AuthorizedUser session = JWT.decode(request);
            boolean account = daoConfig.get("ManejoCliente").getValue().equalsIgnoreCase("true");
            boolean physician = daoConfig.get("ManejoMedico").getValue().equalsIgnoreCase("true");
            boolean rate = daoConfig.get("ManejoTarifa").getValue().equalsIgnoreCase("true");
            boolean service = daoConfig.get("ManejoServicio").getValue().equalsIgnoreCase("true");
            boolean race = daoConfig.get("ManejoRaza").getValue().equalsIgnoreCase("true");
            boolean documenttype = daoConfig.get("ManejoTipoDocumento").getValue().equalsIgnoreCase("true");

            List<Demographic> demographics = new LinkedList<>();
            if (listOrders.getFilterdemographics() == null || listOrders.getFilterdemographics().isEmpty())
            {
                //demographics =serviceDemographic.list(true);
            } else
            {
                demographics = serviceDemographic.getDemographicIds(listOrders.getFilterdemographics());
            }

            List<Order> list = homeBoundDao.listOrders(listOrders.getOrders(), demographics, session.getBranch(), session.getId(), listOrders.getTests(), account, physician, rate, service, race, documenttype);

            if (listOrders.getDemographics().size() > 0)
            {
                list = list.stream()
                        .filter(filter -> listOrders.getDemographics().isEmpty() || StreamFilters.containsDemographicPatient(filter, listOrders.getDemographics()))
                        .collect(Collectors.toList());
                if (list.size() > 0)
                {
                    list.subList(1, list.size()).clear();
                }
            }

            list.forEach((order) ->
            {
                order.getTests().stream().forEach(testdetail ->
                {
                    if (testdetail.getTestType() > 0)
                    {
                        List<Test> testProfile = order.getTests().stream().filter(test -> (Objects.equals(test.getProfile(), testdetail.getId()))).sorted(Comparator.comparing(Test::getSampleState)).collect(Collectors.toList());
                        testdetail.setSampleState(testProfile.get(0).getSampleState());
                    }
                });
                List<Sample> samples = order.getTests().stream().filter(test -> (test.getTestType() == 0 && test.getProfile() == 0) || (test.getTestType() > 0 && test.getSample().getId() != null) || (test.getTestType() == 0 && test.getProfile() != 0 && test.getDependentTest() == 0)).map(test -> test.getSample()).distinct().collect(Collectors.toList());

                samples.stream().forEach(sample ->
                {
                    sample.setTests(getTestsSample(order.getTests(), sample, order.getOrderNumber()));
                });
                order.setSamples(samples);
                order.setTests(null);
            });

            List<Order> filters = list.stream()
                    .collect(Collectors.toList());

            return filters;
        } catch (Exception e)
        {
            EventsLog.error(e);
            return null;
        }

    }

    @Override
    public List<Order> listBasicFilters(ListOrders listOrders) throws Exception
    {
        AuthorizedUser session = JWT.decode(request);
        boolean account = daoConfig.get("ManejoCliente").getValue().equalsIgnoreCase("true");
        boolean physician = daoConfig.get("ManejoMedico").getValue().equalsIgnoreCase("true");
        boolean rate = daoConfig.get("ManejoTarifa").getValue().equalsIgnoreCase("true");
        boolean service = daoConfig.get("ManejoServicio").getValue().equalsIgnoreCase("true");
        boolean race = daoConfig.get("ManejoRaza").getValue().equalsIgnoreCase("true");
        boolean documenttype = daoConfig.get("ManejoTipoDocumento").getValue().equalsIgnoreCase("true");

        List<Demographic> demographics = new LinkedList<>();
        if (listOrders.getFilterdemographics() == null || listOrders.getFilterdemographics().isEmpty())
        {
            //demographics =serviceDemographic.list(true);
        } else
        {
            demographics = serviceDemographic.getDemographicIds(listOrders.getFilterdemographics());
        }

        List<Order> list = homeBoundDao.listBasicOrders(listOrders.getOrders(), demographics, session.getBranch(), session.getId(), listOrders.getTests(), account, physician, rate, service, race, documenttype, listOrders.getSamplestatelis());

        if (listOrders.getDemographics().size() > 0)
        {
            list = list.stream()
                    .filter(filter -> listOrders.getDemographics().isEmpty() || StreamFilters.containsDemographicPatient(filter, listOrders.getDemographics()))
                    .collect(Collectors.toList());
            if (list.size() > 0)
            {
                list.subList(1, list.size()).clear();
            }
        }

        List<Order> filters = list.stream()
                .collect(Collectors.toList());

        return filters;

    }

    /**
     * Envia mensaje a homebound de verificacion de muestra
     *
     * @param order
     * @param urlHomebound
     * @return
     */
    @Override
    public Boolean verificSample(Long order, Integer sample, String urlHomebound)
    {

        final String url = urlHomebound + "/api/hospitalarysample/statechange";

        SampleHospitalHomebound sampleHomebound = new SampleHospitalHomebound();
        sampleHomebound.setOrder(order);
        sampleHomebound.setSample(sample);
        sampleHomebound.setState(7);

        try
        {
            integrationService.putVoid(Tools.jsonObject(sampleHomebound), url);
        } catch (JsonProcessingException ex)
        {
            Logger.getLogger(IntegrationHomboundServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex)
        {
            Logger.getLogger(IntegrationHomboundServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    @Override
    public Boolean retakeSample(Long order, Integer sample) throws Exception
    {
        final String url = configurationService.getValue("UrlHomebound") + "/api/hospitalarysample/statechange";

        SampleHospitalHomebound sampleHomebound = new SampleHospitalHomebound();
        sampleHomebound.setOrder(order);
        sampleHomebound.setSample(sample);
        sampleHomebound.setState(8);

        try
        {
            integrationService.putVoid(Tools.jsonObject(sampleHomebound), url);
        } catch (JsonProcessingException ex)
        {
            Logger.getLogger(IntegrationHomboundServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex)
        {
            Logger.getLogger(IntegrationHomboundServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    /**
     * Obtiene los exámenes de una muestra
     *
     * @param tests lista de examenes de la orden
     * @param sample muestra
     * @param idOrder numero de la orden
     *
     * @return
     */
    private List<Test> getTestsSample(List<Test> tests, Sample sample, long idOrder)
    {
        sample.setSampleTrackings(sampleTrackingDao.getSampleTracking(sample.getId(), idOrder));

        List<Test> testsFilter = tests.stream().filter(test -> test.getSample() != null && test.getSample().getId() != null
                && test.getSample().getId().equals(sample.getId())).collect(Collectors.toList());

        testsFilter = testsFilter.stream().filter(test -> (test.getTestType() == 0 && test.getProfile() == 0)
                || (test.getTestType() > 0 && test.getSample().getId() != null)
                || (test.getTestType() == 0 && test.getProfile() != 0 && test.getDependentTest() == 0)).collect(Collectors.toList());

        SampleTracking tracking = sample.getSampleTrackings().stream().filter(s -> s.getState() == LISEnum.ResultSampleState.CHECKED.getValue()).findAny().orElse(null);
        testsFilter.forEach(test ->
        {
            test.getResult().setDateSample(tracking == null ? null : tracking.getDate());
            test.getResult().setUserSample(tracking == null ? null : tracking.getUser());
            test.setSample(null);
        });
        sample.setSampleTrackings(null);
        return testsFilter;
    }

    private List<Order> setComments(List<Order> orders) throws Exception
    {
        List<Order> ordersComment = new ArrayList<>();
        for (Order order : orders)
        {
            order.setComments(commentService.listCommentOrder(order.getOrderNumber(), null).stream().filter(comment -> comment.isPrint()).collect(Collectors.toList()));
            if (order.getPatient() != null && order.getPatient().getId() != null)
            {
                order.getPatient().setDiagnostic(commentService.listCommentOrder(null, order.getPatient().getId()));
            }
            ordersComment.add(order);
        }
        return ordersComment;
    }

    /**
     * Obtiene una lista de los tipos de documentos de NT y los devuelve con el
     * formato correcto a HomeBound
     *
     *
     * @return Lista de tipos de documentos.
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<DocumentTypeHomeBound> listDocumentTypes() throws Exception
    {
        try
        {
            List<DocumentTypeHomeBound> documentTypes = new ArrayList<>();
            List<DocumentType> list = documentTypeService.list();
            if (list.size() > 0)
            {
                for (DocumentType documentType : list)
                {
                    DocumentTypeHomeBound hmbDocument = new DocumentTypeHomeBound();
                    hmbDocument.setId(documentType.getId());
                    hmbDocument.setName(documentType.getName());
                    hmbDocument.setCode(documentType.getAbbr());

                    documentTypes.add(hmbDocument);
                }
            }

            return documentTypes;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene una lista de los clientes y los envia con el formato correcto a
     * HomeBound
     *
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<AccountHomeBound> listAccounts() throws Exception
    {
        try
        {
            List<AccountHomeBound> accounts = new ArrayList<>();
            List<Account> list = accountService.list();
            if (list.size() > 0)
            {
                for (Account account : list)
                {
                    AccountHomeBound hmbAccount = new AccountHomeBound();
                    hmbAccount.setId(account.getId());
                    hmbAccount.setName(account.getName());
                    hmbAccount.setCode(account.getNit());

                    accounts.add(hmbAccount);
                }
            }

            return accounts;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene una lista de generos de NT para ser enviados a Homebound
     *
     * @return Lista de generos
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<GenderHomeBound> listGenders() throws Exception
    {
        try
        {
            return homeBoundDao.listGenresLanguage();
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene una lista de los tipos de pagos para ser enviados a Homebound
     *
     * @return Lista de formas de pagos
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<PaymentTypeHomeBound> listPaymentTypes() throws Exception
    {
        try
        {
            List<PaymentTypeHomeBound> paymentTypes = new ArrayList<>();
            List<PaymentType> list = paymentTypeService.list();
            if (list.size() > 0)
            {
                for (PaymentType paymentType : list)
                {
                    PaymentTypeHomeBound hmbPaymentType = new PaymentTypeHomeBound();
                    hmbPaymentType.setId(paymentType.getId());
                    hmbPaymentType.setName(paymentType.getName());

                    paymentTypes.add(hmbPaymentType);
                }
            }
            return paymentTypes;
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public PatientHomeBound getPatientHomeBound(int documentType, String patientId, String lang) throws Exception
    {
        try
        {
            Patient patientNT = patientService.get(patientId, documentType, 0);
            if (patientNT != null)
            {
                PatientHomeBound patient = new PatientHomeBound();
                DocumentTypeHomeBound document = new DocumentTypeHomeBound();
                GenderHomeBound gender = new GenderHomeBound();
                List<DemographicHomeBound> demos = new ArrayList<>();

                patient.setId(patientNT.getId());
                document.setId(patientNT.getDocumentType().getId());
                document.setCode(patientNT.getDocumentType().getAbbr());
                document.setName(patientNT.getDocumentType().getName());
                patient.setDocumentType(document);
                patient.setPatientId(patientNT.getPatientId());
                patient.setLastName(patientNT.getLastName());
                patient.setSurName(patientNT.getSurName());
                patient.setName1(patientNT.getName1());
                patient.setName2(patientNT.getName2());
                patient.setBirthday(patientNT.getBirthday());
                gender.setId(patientNT.getSex().getId());
                gender.setCode(patientNT.getSex().getCode());
                patient.setEmail(patientNT.getEmail());
                // Validación de idioma en el que se retornara el nombre del genero
                if (lang.equalsIgnoreCase("es"))
                {
                    gender.setName(patientNT.getSex().getEsCo());
                } else
                {
                    gender.setName(patientNT.getSex().getEnUsa());
                }
                patient.setSex(gender);
                patient.setDiagnosis(integrationSklService.listCommentPatient(patient.getId()));
                // Cargamos los demograficos
                //Email
                DemographicValue emailDemo = new DemographicValue();
                emailDemo.setIdDemographic(Constants.PATIENT_EMAIL);
                emailDemo.setDemographic(patientNT.getEmail());
                emailDemo.setEncoded(false);
                patientNT.getDemographics().add(emailDemo);
                patientNT.getDemographics().stream().map((demo) ->
                {
                    DemographicHomeBound demoAux = new DemographicHomeBound();
                    demoAux.setId(demo.getIdDemographic());
                    demoAux.setItem(demo.getCodifiedId());
                    demoAux.setCode(demo.getCodifiedCode());
                    if (!demo.getCodifiedName().isEmpty())
                    {
                        demoAux.setValue(demo.getCodifiedName());
                    } else
                    {
                        demoAux.setValue(demo.getDemographic());
                    }
                    return demoAux;
                }).forEachOrdered((demoAux) ->
                {
                    demos.add(demoAux);
                });
                patient.setDemographics(demos);
                patient.setPasswordTemp(false);
                patient.setPassword(patientNT.getPasswordWebQuery());
                return patient;
            } else
            {
                return null;
            }
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene una lista de tipos de orndenes para ser enviados a Homebound
     *
     * @return Paciente con el formato requerido por Homebound
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<OrderType> listOrderTypes() throws Exception
    {
        try
        {
            return orderTypeService.list();
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public Appointment createAppointment(Appointment appointment) throws Exception
    {
        AuthorizedUser userToken = JWT.decode(request);
        Order order = new Order();
        Patient patient = patientService.get(appointment.getPatient().getId());
        order.setPatient(patient);

        OrderType orderType = new OrderType();
        orderType = orderTypeService.filterByCode(appointment.getType());
        order.setType(orderType);

        Branch branch = branchService.get(appointment.getBranch().getId(), null, null, null);
        order.setBranch(branch);

        ServiceLaboratory service = serviceService.filterById(appointment.getIdService());
        order.setService(service);

        List<Test> testsList = new ArrayList<>();

        for (int i = 0; i < appointment.getBillingTests().size(); i++)
        {
            Test testSingle = new Test();
            testSingle.setId(appointment.getBillingTests().get(i).getId());
            testSingle.setAbbr(appointment.getBillingTests().get(i).getAbbr());
            testSingle.setName(appointment.getBillingTests().get(i).getName());
            testSingle.setCode(appointment.getBillingTests().get(i).getCode());

            if (configurationService.getValue("ManejoTarifa").toLowerCase().equals("true"))
            {
                Rate rate = new Rate();
                rate.setId(appointment.getBillingTests().get(i).getRate().getId());
                rate.setName(appointment.getBillingTests().get(i).getRate().getName());
                rate.setCode(appointment.getBillingTests().get(i).getRate().getCode());
                testSingle.setRate(rate);
                testSingle.setPrice(appointment.getBillingTests().get(i).getPrice());
            }

            net.cltech.enterprisent.domain.masters.test.Test test = testService.get(appointment.getBillingTests().get(i).getId(), null, null, null);
            testSingle.setTestType(test.getTestType());

            testsList.add(testSingle);
        }
        order.setTests(testsList);

        List<DemographicValue> demographicList = new ArrayList<>();
        for (DemographicHomeBound demo : appointment.getDemographics())
        {
            DemographicValue demographicSingle = new DemographicValue();
            demographicSingle.setIdDemographic(demo.getId());
            demographicSingle.setEncoded(demo.isCoded());
            if (demo.getType() == 1)
            {
                demographicSingle.setEncoded(true);
                demographicSingle.setCodifiedId(demo.getItem());
            } else
            {
                demographicSingle.setEncoded(false);
                demographicSingle.setNotCodifiedValue(demo.getValue());
            }
            demographicList.add(demographicSingle);
        }
        order.setDemographics(demographicList);

        if (configurationService.getValue("ManejoMedico").toLowerCase().equals("true"))
        {
            Physician physician = physicianService.filterById(appointment.getPhysician().getId());
            order.setPhysician(physician);
        }

        if (configurationService.getValue("ManejoCliente").toLowerCase().equals("true"))
        {
            Account account = accountService.get(appointment.getAccount().getId(), null, null, null, null);
            order.setAccount(account);
        }

        if (configurationService.getValue("ManejoTarifa").toLowerCase().equals("true"))
        {
            Rate rate = rateService.get(appointment.getRate().getId(), null, null);
            order.setRate(rate);
        }

        order.setCreatedDateShort(appointment.getDate());
       // order.setAppointment(01);
        
        if (appointment.getOrderNumber() != null)
        {
            order.setOrderNumber(appointment.getOrderNumber());
        } else
        {
            Branch branchInformation = branchService.getBasic(appointment.getBranch().getId(), null,null,null);
            int nextValue = homeBoundDao.getSecuenceOrder(appointment.getDate(),appointment.getBranch().getId());
            //int nextValue = toolsDao.nextVal(Constants.SEQUENCE_APPOINTMENT);
            
            //100 -> remplazar por la contida de citas
            nextValue = nextValue == 0 ? branchInformation.getMaximum() - branchInformation.getNumberAppointments() : branchInformation.getMaximum() - branchInformation.getNumberAppointments() + nextValue;
                        
            if (nextValue <= branchInformation.getMaximum())
            {
                int orderDigits = Integer.parseInt(configurationService.get("DigitosOrden").getValue().trim());
                int date = order.getCreatedDateShort();
                order.setOrderNumber(Tools.getCompleteOrderNumber(date, orderDigits, nextValue));
            } else
            {
                List<String> errorFields = new ArrayList<>(0);
                errorFields.add("1|Rango de ordenes inavalidos para citas");
                throw new EnterpriseNTException(errorFields);
            }
        }

        Order orderAppointment = orderService.create(order, userToken.getId(), appointment.getBranch().getId());
        appointment.setOrderNumber(orderAppointment.getOrderNumber());

        return appointment;
    }

    /**
     * Obtiene un paciente por su identificador en la base de datos para ser
     * enviados a Homebound
     *
     * @param id
     * @return Paciente con el formato requerido por Homebound
     * @throws Exception Error en la base de datos.
     */
    @Override
    public PatientHomeBound getPatientByIdHomeBound(int id) throws Exception
    {
        try
        {
            Patient patientNT = patientService.get(id);
            PatientHomeBound patient = new PatientHomeBound();
            if (patientNT != null)
            {
                DocumentTypeHomeBound document = new DocumentTypeHomeBound();
                GenderHomeBound gender = new GenderHomeBound();
                List<DemographicHomeBound> demos = new ArrayList<>();

                patient.setId(patientNT.getId());
                document.setId(patientNT.getDocumentType().getId());
                document.setCode(patientNT.getDocumentType().getAbbr());
                document.setName(patientNT.getDocumentType().getName());
                patient.setDocumentType(document);
                patient.setPatientId(patientNT.getPatientId());
                patient.setLastName(patientNT.getLastName());
                patient.setSurName(patientNT.getSurName());
                patient.setName1(patientNT.getName1());
                patient.setName2(patientNT.getName2());
                patient.setBirthday(patientNT.getBirthday());
                gender.setCode(patientNT.getSex().getCode());
                gender.setName(patientNT.getSex().getEsCo());
                gender.setNameEn(patientNT.getSex().getEnUsa());
                patient.setSex(gender);
                patient.setEmail(patientNT.getEmail());
                patient.setPhotoBase64(patientNT.getPhoto());
                patientNT.getDemographics().stream().map((demo) ->
                {
                    DemographicHomeBound demoAux = new DemographicHomeBound();
                    demoAux.setId(demo.getIdDemographic());
                    demoAux.setItem(demo.getCodifiedId());
                    demoAux.setCode(demo.getCodifiedCode());
                    if (!demo.getCodifiedName().isEmpty())
                    {
                        demoAux.setValue(demo.getCodifiedName());
                    } else
                    {
                        demoAux.setValue(demo.getDemographic());
                    }
                    return demoAux;
                }).forEachOrdered((demoAux) ->
                {
                    demos.add(demoAux);
                });
                patient.setDemographics(demos);
                patient.setPasswordTemp(false);
                patient.setPassword(patientNT.getPasswordWebQuery());
            }
            return patient;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Crea un paciente en la base de datos desde un servicio externo que
     * consume este servicio de NT
     *
     * @param patientHomebound
     * @return Paciente creado desde homebound
     * @throws Exception Error en la base de datos.
     */
    @Override
    public PatientHomeBound createPatientHomeBound(PatientHomeBound patientHomebound) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        Patient patientNT = new Patient();
        DocumentType document = new DocumentType();
        Item gender = new Item();
        CommentOrder diagnostic = new CommentOrder();

        List<DemographicValue> demos = new ArrayList<>();
        if (patientHomebound != null)
        {
            patientNT.setId(patientHomebound.getId());
            document.setId(patientHomebound.getDocumentType().getId());
            document.setAbbr(patientHomebound.getDocumentType().getCode());
            document.setName(patientHomebound.getDocumentType().getName());
            patientNT.setDocumentType(document);
            patientNT.setPatientId(patientHomebound.getPatientId());
            patientNT.setLastName(patientHomebound.getLastName());
            patientNT.setSurName(patientHomebound.getSurName());
            patientNT.setName1(patientHomebound.getName1());
            patientNT.setName2(patientHomebound.getName2());
            patientNT.setBirthday(patientHomebound.getBirthday());
            gender.setId(patientHomebound.getSex().getId());
            gender.setEsCo(patientHomebound.getSex().getName());
            gender.setEnUsa(patientHomebound.getSex().getNameEn());
            patientNT.setSex(gender);
            patientNT.setEmail(patientHomebound.getEmail());
            patientNT.setPhoto(patientHomebound.getPhotoBase64());

            for (DemographicHomeBound demographic : patientHomebound.getDemographics())
            {
                DemographicValue demo = new DemographicValue();
                demo.setIdDemographic(demographic.getId());
                demo.setDemographic(demographic.getName());
                demo.setEncoded(demographic.getType() == 1);
                demo.setNotCodifiedValue(demographic.getValue());
                demo.setCodifiedId(demographic.getItem());
                demo.setCodifiedName(demographic.getValue());
                demo.setCodifiedCode(demographic.getCode());
                demos.add(demo);
            }
            patientNT.setDemographics(demos);
        }

        Patient createdPatient = patientService.create(patientNT, user.getId());
        if (createdPatient != null)
        {
            patientHomebound.setId(createdPatient.getId());
            patientHomebound.setPatientId(createdPatient.getPatientId());

            if (patientHomebound.getDiagnosis() != null && patientHomebound.getDiagnosis().isEmpty() == false)
            {
                List<CommentOrder> commentsInsert = new ArrayList<>();

                CommentOrder commentOrder = new CommentOrder();
                commentOrder.setIdRecord(Long.valueOf(createdPatient.getId()));
                commentOrder.setComment(patientHomebound.getDiagnosis());
                commentOrder.setType((short) 2);
                commentOrder.setUser(user);

                commentsInsert.add(diagnostic);
                commentDao.insertCommentOrder(commentsInsert);
            }
            return patientHomebound;
        } else
        {
            List<String> errorFields = new ArrayList<>(0);
            errorFields.add("Check that none of the following errors occur");
            errorFields.add("0|Patient already exists");
            errorFields.add("1|Document Type is required");
            errorFields.add("2|Patient Id is not defined");
            throw new EnterpriseNTException(errorFields);
        }
    }

    /**
     * Actualiza un paciente en la base de datos desde un servicio externo que
     * consume este servicio de NT
     *
     * @param patientHomebound
     * @return Paciente creado desde homebound
     * @throws Exception Error en la base de datos.
     */
    @Override
    public PatientHomeBound updatePatientHomeBound(PatientHomeBound patientHomebound) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        Patient patientNT = new Patient();
        DocumentType document = new DocumentType();
        Item gender = new Item();

        if (patientHomebound != null)
        {
            patientNT = patientService.get(patientHomebound.getId());

            document.setId(patientHomebound.getDocumentType().getId());
            document.setAbbr(patientHomebound.getDocumentType().getCode());
            document.setName(patientHomebound.getDocumentType().getName());
            patientNT.setDocumentType(document);

            patientNT.setPatientId(patientHomebound.getPatientId());
            patientNT.setLastName(patientHomebound.getLastName());
            patientNT.setSurName(patientHomebound.getSurName());
            patientNT.setName1(patientHomebound.getName1());
            patientNT.setName2(patientHomebound.getName2());
            patientNT.setBirthday(patientHomebound.getBirthday());

            gender.setId(patientHomebound.getSex().getId());
            gender.setCode(patientHomebound.getSex().getCode());
            gender.setEsCo(patientHomebound.getSex().getName());
            gender.setEnUsa(patientHomebound.getSex().getNameEn());
            patientNT.setSex(gender);
            patientNT.setEmail(patientHomebound.getEmail());
            patientNT.setPhoto(patientHomebound.getPhotoBase64());
            patientNT.setDemographicsHomebound(patientHomebound.getDemographics());

            //diagnostic.setComment(patientHomebound.getDiagnosis());
            //listDiagnostic.add(diagnostic);
            //patientNT.setDiagnostic(listDiagnostic);
            patientNT.setHomebound(true);
        }
        Patient updatePatient = patientService.update(patientNT, user.getId());
        if (updatePatient != null)
        {
            return patientHomebound;
        } else
        {
            List<String> errorFields = new ArrayList<>(0);
            errorFields.add("0|Patient no exists");
            throw new EnterpriseNTException(errorFields);
        }
    }

    @Override
    public BillingTestHomeBound getRatetByIdHomeBound(Integer rate, Integer test) throws Exception
    {

        //Inicializa el objeto de respuesta
        OrderTestDetail detail = new OrderTestDetail();
        detail.setId(test);
        //Consultar las muestras y recipientes del examen
        List<Sample> samples = new ArrayList<>(0);
        List<net.cltech.enterprisent.domain.masters.test.Test> childs = null;

        BillingTestHomeBound billint = new BillingTestHomeBound();

        net.cltech.enterprisent.domain.masters.test.Test testDetail = testService.get(test, null, null, null);
        switch (testDetail.getTestType())
        {
            case 0:
                //Examen

                if (testDetail.getSample() != null)
                {
                    samples.add(testDetail.getSample());
                }
                break;
            case 1:
                //Perfil
                childs = testService.getChilds(test);
                for (net.cltech.enterprisent.domain.masters.test.Test child : childs)
                {

                    if (child.getSample() != null)
                    {
                        if (!samples.contains(child.getSample()))
                        {
                            samples.add(child.getSample());
                        }
                    }
                }
                break;
            case 2:
                //Paquete
                childs = testService.getChilds(test);
                List<net.cltech.enterprisent.domain.masters.test.Test> subChilds = null;
                for (net.cltech.enterprisent.domain.masters.test.Test child : childs)
                {
                    if (child.getTestType() == 1)
                    {
                        //Si es un perfil
                        subChilds = testService.getChilds(child.getId());
                        for (net.cltech.enterprisent.domain.masters.test.Test subChild : subChilds)
                        {
                            if (subChild.getSample() != null)
                            {
                                if (!samples.contains(subChild.getSample()))
                                {
                                    samples.add(subChild.getSample());
                                }
                            }
                        }
                    } else
                    {
                        if (child.getSample() != null)
                        {
                            if (!samples.contains(child.getSample()))
                            {
                                samples.add(child.getSample());
                            }
                        }
                    }
                }
                break;

        }
        //Establece las muestras encontradas
        detail.setSamples(samples);
        //Consultar el precio del examen
        if (rate != -1)
        {
            TestPrice price = orderService.getPriceTest(test, rate);
            detail.setPrice(price.getServicePrice());
            detail.setPatientPrice(price.getPatientPrice());
            detail.setInsurancePrice(price.getInsurancePrice());

            billint.setPrice(price.getPatientPrice());

            Rate rateDetail = rateService.get(rate, null, null);
            RateHomeBound ratehomebound = new RateHomeBound();
            ratehomebound.setId(rateDetail.getId());
            ratehomebound.setCode(rateDetail.getCode());
            ratehomebound.setName(rateDetail.getName());

            billint.setRate(ratehomebound);

        }

        billint.setId(testDetail.getId());
        billint.setCode(testDetail.getCode());
        billint.setAbbr(testDetail.getAbbr());
        billint.setName(testDetail.getName());

        SampleHomeBound samplehomebound = new SampleHomeBound();
        samplehomebound.setName(detail.getSamples().get(0).getName());
        samplehomebound.setId(detail.getSamples().get(0).getId());
        samplehomebound.setCode(detail.getSamples().get(0).getCodesample());

        ContainerHomeBound container = new ContainerHomeBound();
        container.setName(detail.getSamples().get(0).getContainer().getName());
        container.setPhotoBase64(detail.getSamples().get(0).getContainer().getImage());

        samplehomebound.setContainer(container);

        billint.setSample(samplehomebound);
        return billint;

    }

    /**
     * Realiza la actualizacion de la toma de muestra segun los datos enviados
     * por Homebound
     *
     * @param appointment
     * @return Paciente creado desde homebound
     * @throws Exception Error en la base de datos.
     */
    @Override
    public Appointment updateSampleTake(Appointment appointment) throws Exception
    {
        try
        {
            boolean isTaken = true;
            if (appointment.getSamples() != null && appointment.getSamples().size() > 0)
            {
                for (SampleHomeBound sampleHomebound : appointment.getSamples())
                {
                    Sample samplesNT = sampleService.get(sampleHomebound.getId(), null, null, null, null).get(0);
                    boolean auxBool = sampleTrackingService.sampleTracking(appointment.getOrderNumber(), samplesNT.getCodesample(), LISEnum.ResultSampleState.COLLECTED.getValue(), null, false, false);
                    // En el momento que no se pueda realizar la toma de una muestra se sale
                    if (!auxBool)
                    {
                        isTaken = false;
                        break;
                    }
                }
            }
            // Si la respuesta es FALSE seguramente no se pudo 
            // realizar la toma de una muestra de la lista
            if (isTaken)
            {
                return appointment;
            } else
            {
                return null;
            }
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Realiza la actualizacion de la entrevista segun los datos enviados por
     * Homebound
     *
     * @param appointment
     * @return Paciente creado desde homebound
     * @throws Exception Error en la base de datos.
     */
    @Override
    public boolean updateInterview(Appointment appointment) throws Exception
    {
        try
        {
            boolean inpatientInterview = false;
            if (appointment.getInterview() != null && appointment.getInterview().size() > 0)
            {
                AuthorizedUser session = JWT.decode(request);
                List<AuditOperation> audit = new ArrayList<>();
                inpatientInterview = homeBoundDao.insertResultInterview(appointment.getInterview(), appointment.getOrderNumber(), session) > 0;
                audit.add(new AuditOperation(appointment.getOrderNumber(), null, null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_INTERVIEW, Tools.jsonObject(appointment.getInterview()), null, null, null, null));
                trackingService.registerOperationTracking(audit);
            }

            return inpatientInterview;
        } catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public void cancelOrderHomeBound(long order) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        orderService.cancel(order, user.getId());
    }

    @Override
    public long updateAppointmentHomeBound(Appointment appointment) throws Exception
    {
        try
        {    
            AuthorizedUser user = JWT.decode(request);

            orderService.cancel(appointment.getOrderNumber(), user.getId());

            appointment.setOrderNumber(null);

            Appointment objectAppointment = createAppointment(appointment);

            return objectAppointment.getOrderNumber();

        } catch (Exception e)
        {
            IntegrationHisLog.error(e);
            return 0;
        }

    }

    @Override
    public List<Appointment> getAppointments(List<Long> orders) throws Exception
    {
        try
        {
            List<Appointment> listAppointments = new ArrayList<>();
            Appointment singleAppointment = new Appointment();
            List<BillingTestHomeBound> testsList = new ArrayList<>();
            List<DemographicValue> listDemographicValue = new ArrayList<>();

            for (int i = 0; i < orders.size(); i++)
            {

                List<SampleHomeBound> samples = homeBoundDao.getSamplesByIdOrder(orders.get(i));
                if (samples.size() > 0)
                {
                    List<DemographicHomeBound> listDemographic = new ArrayList<>();

                    singleAppointment = homeBoundDao.getAppointment(orders.get(i));

                    testsList = homeBoundDao.getTestsList(orders.get(i));
                    singleAppointment.setBillingTests(testsList);
                    listDemographicValue = orderService.getAsDemographicList(orders.get(i),0);

                    List<DemographicHomeBound> demosh = new ArrayList<>();
                    Patient patient = patientService.get(singleAppointment.getPatient().getId());
                    for (DemographicValue demo : patient.getDemographics())
                    {
                        DemographicHomeBound demoAuxh = new DemographicHomeBound();
                        demoAuxh.setId(demo.getIdDemographic());
                        demoAuxh.setName(demo.getDemographic());
                        demoAuxh.setCoded(demo.isEncoded());
                        demoAuxh.setSource("H");
                        if (demo.isEncoded())
                        {
                            demoAuxh.setItem(demo.getCodifiedId());
                            demoAuxh.setCode(demo.getCodifiedCode());
                            demoAuxh.setValue(demo.getCodifiedName());
                            demoAuxh.setType(1);
                        } else
                        {
                            demoAuxh.setValue(demo.getNotCodifiedValue());
                            demoAuxh.setType(0);
                        }
                        demosh.add(demoAuxh);
                        listDemographic.add(demoAuxh);
                    }

                    for (DemographicValue demo1 : listDemographicValue)
                    {
                        DemographicHomeBound demoAuxo = new DemographicHomeBound();
                        demoAuxo.setId(demo1.getIdDemographic());
                        demoAuxo.setName(demo1.getDemographic());
                        demoAuxo.setCoded(demo1.isEncoded());
                        demoAuxo.setSource("O");

                        if (demo1.isEncoded())
                        {
                            demoAuxo.setItem(demo1.getCodifiedId());
                            demoAuxo.setCode(demo1.getCodifiedCode());
                            demoAuxo.setValue(demo1.getCodifiedName());
                            demoAuxo.setType(1);
                        } else
                        {
                            demoAuxo.setValue(demo1.getNotCodifiedValue());
                            demoAuxo.setType(0);
                        }

                        listDemographic.add(demoAuxo);
                    }

                    singleAppointment.setDemographics(listDemographic);
                    singleAppointment.getPatient().setDemographics(demosh);

                    singleAppointment.getPatient().setDiagnosis(integrationSklService.listCommentPatient(singleAppointment.getPatient().getId()));

                    List<TestHomeBound> tests = homeBoundDao.listTestHomeBoundByIdOrder(orders.get(i));

                    if (tests.size() > 0)
                    {
                        singleAppointment.setAllTests(homeBoundDao.listTestHomeBoundByIdOrder(orders.get(i)));
                    }

                    singleAppointment.setSamples(homeBoundDao.getSamplesByIdOrder(orders.get(i)));

                    listAppointments.add(singleAppointment);
                }
            }
            return listAppointments;

        } catch (Exception e)
        {

            return null;
        }
    }

    @Override
    public List<BasicHomeboundPatient> getPatientByOrderId(List<Long> orders) throws Exception
    {
        try
        {
            return homeBoundDao.getPatientBasicInfo(orders);
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<QuestionHomeBound> getInterviewByOrderId(long orderId) throws Exception
    {
        try
        {
            Order order = sampleTrackingService.getOrder(orderId, false);
            return homeBoundDao.getInterviewByOrderId(order);
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<DemographicHomeBound> getItemDemographicsByDemoId(int demographicId) throws Exception
    {
        try
        {
            return homeBoundDao.getItemDemographicsByDemoId(demographicId);
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<DemographicHomeBound> getAllDemographics() throws Exception
    {
        try
        {
            return homeBoundDao.getAllDemographics();
        } catch (Exception e)
        {
            return null;
        }
    }
    
    @Override
    public Track getTracking(Long orderNumber) throws Exception
    {
        try
        {
            return homeBoundDao.getTracking(orderNumber);
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public boolean updateAppointment(Appointment appointment) throws Exception
    {
        Order order = new Order();
        AuthorizedUser userBranch = JWT.decode(request);
        updatePatientHomeBound(appointment.getPatient());

        // Número de la orden
        if (appointment.getOrderNumber() != null)
        {
            order.setOrderNumber(appointment.getOrderNumber());
        } else
        {
            int nextValue = toolsDao.nextVal(Constants.SEQUENCE_APPOINTMENT);
            if (nextValue != -1 && nextValue != -2)
            {
                int orderDigits = Integer.parseInt(configurationService.get("DigitosOrden").getValue().trim());
                int date = order.getCreatedDateShort();
                order.setOrderNumber(Tools.getCompleteOrderNumber(date, orderDigits, nextValue));
            } else
            {
                List<String> errorFields = new ArrayList<>(0);
                errorFields.add("1|Rango de ordenes inavalidos para citas");
                throw new EnterpriseNTException(errorFields);
            }
        }

        OrderType orderType;
        orderType = orderTypeService.filterByCode(appointment.getType());
        order.setType(orderType);

        Branch branch = branchService.get(userBranch.getBranch(), null, null, null);
        order.setBranch(branch);

        ServiceLaboratory service = serviceService.filterById(appointment.getIdService());
        order.setService(service);

        List<Test> testsList = new ArrayList<>();

        for (BillingTestHomeBound test : appointment.getBillingTests())
        {
            Test testSingle = new Test();
            testSingle.setId(test.getId());
            testSingle.setAbbr(test.getAbbr());
            testSingle.setName(test.getName());
            testSingle.setCode(test.getCode());

            if (configurationService.getValue("ManejoTarifa").toLowerCase().equals("true"))
            {
                Rate rate = new Rate();
                rate.setId(test.getRate().getId());
                rate.setName(test.getRate().getName());
                rate.setCode(test.getRate().getCode());
                testSingle.setRate(rate);
                testSingle.setPrice(test.getPrice());
            }

            net.cltech.enterprisent.domain.masters.test.Test testAux = testService.get(test.getId(), null, null, null);
            testSingle.setTestType(testAux.getTestType());

            testsList.add(testSingle);
        }
        order.setTests(testsList);

        List<DemographicValue> demographicList = new ArrayList<>();
        for (DemographicHomeBound demo : appointment.getDemographics())
        {
            if( demo.getId() > 0 ) {
                DemographicValue demographicSingle = new DemographicValue();
                demographicSingle.setIdDemographic(demo.getId());
                demographicSingle.setEncoded(demo.isCoded());
                if (demo.getType() == 1)
                {
                    demographicSingle.setEncoded(true);
                    demographicSingle.setCodifiedId(demo.getItem());
                } else
                {
                    demographicSingle.setEncoded(false);
                    demographicSingle.setNotCodifiedValue(demo.getValue());
                }
                demographicList.add(demographicSingle);
            }
        }
        order.setDemographics(demographicList);

        if (configurationService.getValue("ManejoMedico").toLowerCase().equals("true"))
        {
            Physician physician = physicianService.filterById(appointment.getPhysician().getId());
            order.setPhysician(physician);
        }

        if (configurationService.getValue("ManejoCliente").toLowerCase().equals("true"))
        {
            Account account = accountService.get(appointment.getAccount().getId(), null, null, null, null);
            order.setAccount(account);
        }

        if (configurationService.getValue("ManejoTarifa").toLowerCase().equals("true"))
        {
            Rate rate = rateService.get(appointment.getRate().getId(), null, null);
            order.setRate(rate);
        }
        order.setCreatedDateShort(appointment.getDate());
        order.getPatient().setUpdatePatient(false);
        order.getPatient().setPatientId(appointment.getPatient().getPatientId());
        order.getPatient().getDocumentType().setId(appointment.getPatient().getDocumentType().getId());
        Order orderAppointment = orderService.update(order, userBranch.getId(), userBranch.getBranch(),0);
        return orderAppointment != null;

    }

    @Override
    public boolean updateAppointmentDate(long orderId) throws Exception
    {
        try
        {
            return homeBoundDao.updateAppointmentDate(orderId);
        } catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public boolean updateTransportSample(List<TransportSampleHomebound> transportSampleHomebound) throws Exception
    {
        try
        {
            List<AuditOperation> auditList = new ArrayList<>(0);
            homeBoundDao.updateTransportSample(transportSampleHomebound);
            for (int i = 0; i < transportSampleHomebound.size(); i++)
            {
                for (int j = 0; j < transportSampleHomebound.get(i).getSamples().size(); j++)
                {
                    statisticService.sampleStateChanged(1, transportSampleHomebound.get(i).getOrder(), transportSampleHomebound.get(i).getSamples().get(j));
                }

                List<Integer> tests = ordersDao.getTestSampleTake(transportSampleHomebound.get(i).getOrder(), transportSampleHomebound.get(i).getSamples());
                for (int j = 0; j < tests.size(); j++)
                {
                    auditList.add(new AuditOperation(transportSampleHomebound.get(i).getOrder(), tests.get(j), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(LISEnum.SampleStateHomebound.TRANSPORT.getValue()), null, null, null, null));
                }
            }
            try
            {
                trackingService.registerOperationTracking(auditList, transportSampleHomebound.get(0).getUser());
                IntegrationHisLog.info("registro auditoria transporte");
            } catch (Exception e)
            {
                IntegrationHisLog.error(e);
            }
            return true;
        } catch (Exception e)
        {
            IntegrationHisLog.error(e);
            return false;
        }
    }

    @Override
    public List<RateHomeBound> getRatesByAccountId(int accountId) throws Exception
    {
        try
        {
            return accountService.getRates(accountId).stream().filter(t -> t.isApply())
                    .map(rateAccount -> new RateHomeBound(rateAccount.getRate().getId(), rateAccount.getRate().getCode(), rateAccount.getRate().getName()))
                    .collect(Collectors.toList());
        } catch (Exception e)
        {
            return null;
        }
    }
}

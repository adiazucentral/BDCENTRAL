package net.cltech.enterprisent.service.impl.enterprisent.operation.orders;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicItemDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.DiagnosticDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.SampleDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.TestDao;
import net.cltech.enterprisent.dao.interfaces.operation.microbiology.MicrobiologyDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.BillingTestDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultTestDao;
import net.cltech.enterprisent.dao.interfaces.operation.tracking.SampleTrackingDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardOpportunityTime;
import net.cltech.enterprisent.domain.integration.orderForExternal.OrderForExternal;
import net.cltech.enterprisent.domain.integration.orderForExternal.PatientForExternal;
import net.cltech.enterprisent.domain.integration.orderForExternal.TestForExternal;
import net.cltech.enterprisent.domain.integration.sendOrderExternalLIS.IdsPatientOrderTest;
import net.cltech.enterprisent.domain.integration.siga.SigaTurn;
import net.cltech.enterprisent.domain.integration.siga.SigaTurnMovement;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.BranchDemographic;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicBranch;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.domain.masters.demographic.Race;
import net.cltech.enterprisent.domain.masters.demographic.RatesOfAccount;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.masters.test.Requirement;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.billing.FilterTestPrice;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.FilterSubsequentPayments;
import net.cltech.enterprisent.domain.operation.orders.InconsistentOrder;
import net.cltech.enterprisent.domain.operation.orders.LastOrderPatient;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderNumSearch;
import net.cltech.enterprisent.domain.operation.orders.OrderSearch;
import net.cltech.enterprisent.domain.operation.orders.OrderTestDetail;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Recalled;
import net.cltech.enterprisent.domain.operation.orders.Result;
import net.cltech.enterprisent.domain.operation.orders.ShiftOrder;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.orders.TestPrice;
import net.cltech.enterprisent.domain.operation.orders.TestValidity;
import net.cltech.enterprisent.domain.operation.orders.TicketTest;
import net.cltech.enterprisent.domain.operation.orders.billing.BillingTest;
import net.cltech.enterprisent.domain.operation.orders.billing.CashBox;
import net.cltech.enterprisent.domain.operation.results.HistoricalResult;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.tracking.PackageTracking;
import net.cltech.enterprisent.domain.operation.tracking.SampleState;
import net.cltech.enterprisent.domain.operation.tracking.TestToRecall;
import net.cltech.enterprisent.service.interfaces.common.ListService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationDashBoardService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationIngresoService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMiddlewareService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationSigaService;
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
import net.cltech.enterprisent.service.interfaces.masters.test.DiagnosticService;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.appointment.AppointmentService;
import net.cltech.enterprisent.service.interfaces.operation.common.CommentService;
import net.cltech.enterprisent.service.interfaces.operation.list.OrderListService;
import net.cltech.enterprisent.service.interfaces.operation.microbiology.MicrobiologyService;
import net.cltech.enterprisent.service.interfaces.operation.orders.CashBoxService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.service.interfaces.operation.orders.RecalledService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.AgileStatisticService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.StatisticService;
import net.cltech.enterprisent.service.interfaces.operation.tracking.SampleTrackingService;
import net.cltech.enterprisent.service.interfaces.tools.events.EventsOrderService;
import net.cltech.enterprisent.tools.ConfigurationConstants;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.enums.ListEnum;
import net.cltech.enterprisent.tools.log.integration.ExternalBillingLog;
import net.cltech.enterprisent.tools.log.integration.MiddlewareLog;
import net.cltech.enterprisent.tools.log.integration.SecurityLog;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion de servicios de ordenes para Enterprise NT
 *
 * @version 1.0.0
 * @author dcortes
 * @since 18/07/2017
 * @see Creacion
 */
@Service
public class OrderServiceEnterpriseNT implements OrderService
{

    @Autowired
    private CashBoxService cashBoxService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PatientService patientService;
    @Autowired
    private OrdersDao orderDao;
    @Autowired
    private ResultTestDao resultTestDao;
    @Autowired
    private OrderListService orderListService;
    @Autowired
    private ConfigurationService configurationServices;
    @Autowired
    private RaceService raceService;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private ListService listService;
    @Autowired
    private DemographicService demographicService;
    @Autowired
    private DemographicItemService demographicItemService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RateService rateService;
    @Autowired
    private PhysicianService physicianService;
    @Autowired
    private ToolsDao toolsDao;
    @Autowired
    private ResultsService resultService;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private TestService testService;
    @Autowired
    private ResultDao resultsDao;
    @Autowired
    private SampleDao sampleDao;
    @Autowired
    private SampleTrackingDao sampleTrackingDao;
    @Autowired
    private SampleTrackingService sampleTrackingService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private AgileStatisticService agileStatisticService;
    @Autowired
    private BillingTestDao billingTestDao;
    @Autowired
    private CommentService commentService;
    @Autowired
    private MicrobiologyService microbiologyService;
    @Autowired
    private IntegrationDashBoardService dashBoardService;
    @Autowired
    private DiagnosticDao diagnosticDao;
    @Autowired
    private DiagnosticService diagnosticService;
    @Autowired
    private IntegrationMiddlewareService integrationMiddlewareService;
    @Autowired
    private EventsOrderService eventsOrderService;
    @Autowired
    private RecalledService recalledService;
    @Autowired
    private IntegrationIngresoService serviceIngreso;
    @Autowired
    private DemographicItemDao demographicItemDao;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private IntegrationSigaService integrationSigaService;
    @Autowired
    private MicrobiologyDao microbiologyDao;
    @Autowired
    private TestDao testDao;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private LaboratorysByBranchesService laboratorysByBranchesService;


 

    @Override
    public Order get(long orderNumber) throws Exception
    {
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);
        
        List<Demographic> demos = demographicService.list().stream().filter((Demographic t) -> t.getOrigin().equals("O") && t.isState()).collect(Collectors.toList());
        Order order = orderDao.get(orderNumber, demos, laboratorys, idbranch );
        if (order != null)
        {
            order.setPatient(patientService.get(order.getPatient().getId()));
            List<ResultTest> allTests = resultService.get(orderNumber);
            List<ResultTest> tests = allTests
                    .stream()
                    .filter(t -> t.isEntry())
                    .map(t -> t.setValidatedChilds(childsWithValidation(t, allTests)))
                    .collect(Collectors.toList());

            order.setListDiagnostic(diagnosticService.ListDiagnostics(orderNumber));
            //Agrega la caja a los examenes
            List<BillingTest> billing = billingTestDao.get(orderNumber);
            if (!billing.isEmpty())
            {
                ResultTest ts = null;
                Order orderB = new Order();
                orderB.setOrderNumber(orderNumber);
                for (BillingTest billingTest : billing)
                {
                    ts = tests.stream().filter(t -> t.getTestId() == billingTest.getTest().getId()).findFirst().orElse(null);
                    if (ts != null)
                    {
                        billingTest.setOrder(orderB);
                        ts.setBilling(billingTest);
                    }
                }
            }

            //Se consultan los medicos auxiliares
            if ("True".equals(configurationServices.get("MedicosAuxiliares").getValue()))
            {
                order.setAuxiliaryPhysicians(orderDao.getAuxiliaryPhysicians(order.getOrderNumber()));
            }

            order.setResultTest(tests);
            order.setSamples(getSamples(tests));
            order.setComments(commentService.listCommentOrder(order.getOrderNumber(), null));
            order.getPatient().setDiagnostic(commentService.listCommentOrder(null, order.getPatient().getId()));
            for (ResultTest resultTest : order.getResultTest())
            {
                if ("True".equals(configurationServices.get("AgregarExamenesServicios").getValue()) && resultTest.getTestType() > 0)
                {
                    if (allTests.stream().filter(t -> t.getProfileId() == resultTest.getTestId() && t.isPrintsample()).collect(Collectors.toList()).size() > 0)
                    {
                        resultTest.setPrintsample(true);
                    }
                }
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
    public Order get(long orderNumber, int appointment) throws Exception
    {
        
        int idbranch = JWT.decode(request).getBranch();
        
        List<Demographic> demos = demographicService.list().stream().filter((Demographic t) -> t.getOrigin().equals("O") && t.isState()).collect(Collectors.toList());
        Order order = orderDao.getAppointment(orderNumber, demos, 1, idbranch);
        
        if (order != null)
        {
            order.setPatient(patientService.get(order.getPatient().getId()));
            List<ResultTest> allTests = resultService.get(orderNumber);
            List<ResultTest> tests = allTests
                    .stream()
                    .filter(t -> t.isEntry())
                    .map(t -> t.setValidatedChilds(childsWithValidation(t, allTests)))
                    .collect(Collectors.toList());

            order.setListDiagnostic(diagnosticService.ListDiagnostics(orderNumber));
            //Agrega la caja a los examenes
            List<BillingTest> billing = billingTestDao.get(orderNumber);
            if (!billing.isEmpty())
            {
                ResultTest ts = null;
                Order orderB = new Order();
                orderB.setOrderNumber(orderNumber);
                for (BillingTest billingTest : billing)
                {
                    ts = tests.stream().filter(t -> t.getTestId() == billingTest.getTest().getId()).findFirst().orElse(null);
                    if (ts != null)
                    {
                        billingTest.setOrder(orderB);
                        ts.setBilling(billingTest);
                    }
                }
            }

            //Se consultan los medicos auxiliares
            if ("True".equals(configurationServices.get("MedicosAuxiliares").getValue()))
            {
                order.setAuxiliaryPhysicians(orderDao.getAuxiliaryPhysicians(order.getOrderNumber()));
            }

            order.setResultTest(tests);
            order.setSamples(getSamples(tests));
            order.setComments(commentService.listCommentOrder(order.getOrderNumber(), null));
            order.getPatient().setDiagnostic(commentService.listCommentOrder(null, order.getPatient().getId()));
            for (ResultTest resultTest : order.getResultTest())
            {
                if ("True".equals(configurationServices.get("AgregarExamenesServicios").getValue()) && resultTest.getTestType() > 0)
                {
                    if (allTests.stream().filter(t -> t.getProfileId() == resultTest.getTestId() && t.isPrintsample()).collect(Collectors.toList()).size() > 0)
                    {
                        resultTest.setPrintsample(true);
                    }
                }
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
    public Order getOrder(long orderNumber) throws Exception
    {
        List<Demographic> demos = demographicService.list().stream().filter((Demographic t) -> t.getOrigin().equals("O") && t.isState()).collect(Collectors.toList());
        Order order = orderDao.get(orderNumber, demos);
        if (order != null)
        {
            order.setPatient(patientService.get(order.getPatient().getId()));
            List<ResultTest> allTests = resultService.get(orderNumber);
            List<ResultTest> tests = allTests
                    .stream()
                    .filter(t -> t.isEntry() || t.getDeleteProfile() == 1)
                    .map(t -> t.setValidatedChilds(childsWithValidation(t, allTests)))
                    .collect(Collectors.toList());

            
            //Agrega la caja a los examenes
            List<BillingTest> billing = billingTestDao.get(orderNumber);
            if (!billing.isEmpty())
            {
                ResultTest ts = null;
                Order orderB = new Order();
                orderB.setOrderNumber(orderNumber);
                for (BillingTest billingTest : billing)
                {
                    ts = tests.stream().filter(t -> t.getTestId() == billingTest.getTest().getId()).findFirst().orElse(null);
                    if (ts != null)
                    {
                        billingTest.setOrder(orderB);
                        ts.setBilling(billingTest);
                    }
                }
            }
            order.setResultTest(tests);
            order.setSamples(getSamples(tests));
            return order;
        } else
        {
            return null;
        }
    }
    
    public Order getEmailDemo(long orderNumber) throws Exception
    {
        List<Demographic> demos = demographicService.list().stream().filter((Demographic t) -> t.getOrigin().equals("O") && t.isState()).collect(Collectors.toList());
        Order order = orderDao.getEmailDemo(orderNumber, demos);

        if (order != null)
        {
            order.setPatient(patientService.getEmail(order.getPatient().getId()));
            //Se consultan los medicos auxiliares
            if ("True".equals(configurationServices.get("MedicosAuxiliares").getValue()))
            {
                order.setAuxiliaryPhysicians(orderDao.getAuxiliaryPhysiciansReport(orderNumber));
            }
            return order;
        } else
        {
            return null;
        }
    }

    @Override
    public Order getConfigPrint(long orderNumber) throws Exception
    {
        Order order = orderDao.getConfigPrint(orderNumber);
        return order;
    }

    @Override
    public OrderForExternal getforBuilder(IdsPatientOrderTest ids) throws Exception
    {
                
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);
                
        List<Demographic> demos = demographicService.list().stream().filter((Demographic t) -> t.getOrigin().equals("O") && t.isState()).collect(Collectors.toList());
        Order order = orderDao.get(ids.getOrderNumber(), demos, laboratorys, idbranch);
        if (order != null)
        {
            order.setComments(commentService.listCommentOrder(order.getOrderNumber(), null));
            OrderForExternal orderForExternal = new OrderForExternal(order);
            orderForExternal.setPatient(new PatientForExternal(patientService.get(((int) ids.getIdPatient()))));
            List<TestForExternal> tests = orderForExternal.getTests();
            ids.getIdTests().forEach(test ->
            {
                try
                {
                    tests.add(testDao.getTestForExternal(test));
                } catch (Exception ex)
                {
                    Logger.getLogger(OrderServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            orderForExternal.setTests(tests);
            return orderForExternal;
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

            order.setBillingAccount(orderDao.getBillingAccountByOrder(orderNumber));
        }
        return order;
    }
    
    @Override
    public Order getEntry(long orderNumber, int appointment) throws Exception
    {
        Order order = get(orderNumber, appointment);
        if (order != null)
        {
            order.setResultTest(order.getResultTest().stream().filter(r -> r.getPackageId() == 0).collect(Collectors.toList()));

            order.setBillingAccount(orderDao.getBillingAccountByOrder(orderNumber));
        }
        return order;
    }
    
    @Override
    public Order getOrderEditTest(long orderNumber) throws Exception
    {
        Order order = getOrder(orderNumber);
        if (order != null)
        {
            order.setResultTest(order.getResultTest().stream().filter(r -> r.getPackageId() == 0 || r.getDeleteProfile() == 1 ).collect(Collectors.toList()));

            order.setBillingAccount(orderDao.getBillingAccountByOrder(orderNumber));
        }
        return order;
    }
    
    

    @Override
    public Order getEmail(long orderNumber) throws Exception
    {
        Order order = getEmailDemo(orderNumber);
        return order;
    }

    @Override
    public Order getAudit(long orderNumber) throws Exception
    {
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);
               
        List<Demographic> demos = demographicService.list().stream().filter((Demographic t) -> t.getOrigin().equals("O") && t.isState()).collect(Collectors.toList());
        Order order = orderDao.get(orderNumber, demos,  laboratorys, idbranch);
        if (order != null)
        {
            order.setPatient(patientService.get(order.getPatient().getId()));
            List<ResultTest> allTests = resultService.get(orderNumber);
            List<ResultTest> tests = allTests
                    .stream()
                    .filter(t -> t.getTestType() == 0)
                    .collect(Collectors.toList());

            order.setListDiagnostic(diagnosticService.ListDiagnostics(orderNumber));

            order.setResultTest(tests);
            order.setSamples(getSamplesAudit(tests));
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
    public List<Order> get(int dateShort) throws Exception
    {
        List<Demographic> demos = demographicService.list().stream().filter((Demographic t) -> t.getOrigin().equals("O") && t.isState()).collect(Collectors.toList());
        return orderDao.get(dateShort, demos);
    }

    @Override
    public List<Order> getByLastName(String lastName) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Order> getByName(String name) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Order> getByPatientId(String patientId) throws Exception
    {
        String encryptedPatientId = Tools.encrypt(patientId);
        List<Demographic> demos = demographicService.list().stream().filter((Demographic t) -> t.getOrigin().equals("O") && t.isState()).collect(Collectors.toList());
        return orderDao.getByPatientId(encryptedPatientId, demos);
    }

    @Override
    public List<DemographicValue> getAsDemographicList(long orderNumber, int appointment) throws Exception
    {
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);
        
        List<Demographic> demos = demographicService.list().stream().filter((Demographic t) -> t.getOrigin().equals("O") && t.isState()).collect(Collectors.toList());
        
        Order order = appointment == 0 ? orderDao.get(orderNumber, demos, laboratorys, idbranch): orderDao.getAppointment(orderNumber, demos, 0, idbranch);

        //Se consultan los medicos auxiliares
        if ("True".equals(configurationServices.get("MedicosAuxiliares").getValue()))
        {
            order.setAuxiliaryPhysicians(orderDao.getAuxiliaryPhysicians(order.getOrderNumber()));
        }

        if (order != null)
        {
            List<DemographicValue> demosValues = new ArrayList<>(0);
            DemographicValue demo = null;
            //ORDEN
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.ORDER_ID);
            demo.setDemographic("Order");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(String.valueOf(order.getOrderNumber()));
            demosValues.add(demo);

            //Fecha Registro
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.ORDER_DATE);
            demo.setDemographic("Register Date");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(new SimpleDateFormat(configurationServices.get("FormatoFecha").getValue() + " HH:mm:ss").format(order.getCreatedDate()));
            demosValues.add(demo);

            //Tipo de Orden
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.ORDERTYPE);
            demo.setDemographic("Order Type");
            demo.setEncoded(true);
            demo.setCodifiedId(order.getType().getId());
            demo.setCodifiedCode(order.getType().getCode());
            demo.setCodifiedName(order.getType().getName());
            demosValues.add(demo);

            //Sede
            if (Boolean.parseBoolean(configurationServices.get("TrabajoPorSede").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.BRANCH);
                demo.setDemographic("Branch");
                demo.setEncoded(true);
                demo.setCodifiedId(order.getBranch() == null ? null : order.getBranch().getId());
                demo.setCodifiedCode(order.getBranch() == null ? null : order.getBranch().getCode());
                demo.setCodifiedName(order.getBranch() == null ? null : order.getBranch().getName());
                demosValues.add(demo);
            }

            //Servicio
            if (Boolean.parseBoolean(configurationServices.get("ManejoServicio").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.SERVICE);
                demo.setDemographic("Service");
                demo.setEncoded(true);
                demo.setCodifiedId(order.getService() == null ? null : order.getService().getId());
                demo.setCodifiedCode(order.getService() == null ? null : order.getService().getCode());
                demo.setCodifiedName(order.getService() == null ? null : order.getService().getName());
                demosValues.add(demo);
            }

            //Cliente
            if (Boolean.parseBoolean(configurationServices.get("ManejoCliente").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.ACCOUNT);
                demo.setDemographic("Account");
                demo.setEncoded(true);
                demo.setCodifiedId(order.getAccount() == null ? null : order.getAccount().getId());
                //TODO Cambio por codigo
                demo.setCodifiedCode(order.getAccount() == null ? null : order.getAccount().getName());
                demo.setCodifiedName(order.getAccount() == null ? null : order.getAccount().getName());
                demosValues.add(demo);
            }

            //Tarifa
            if (Boolean.parseBoolean(configurationServices.get(ConfigurationConstants.KEY_RATE_ACTIVE).getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.RATE);
                demo.setDemographic("Rate");
                demo.setEncoded(true);
                demo.setCodifiedId(order.getRate() == null ? null : order.getRate().getId());
                demo.setCodifiedCode(order.getRate() == null ? null : order.getRate().getCode());
                demo.setCodifiedName(order.getRate() == null ? null : order.getRate().getName());
                demosValues.add(demo);
            }

            //Medico
            if (Boolean.parseBoolean(configurationServices.get("ManejoMedico").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.PHYSICIAN);
                demo.setDemographic("Physian");
                demo.setEncoded(true);
                demo.setCodifiedId(order.getPhysician() == null ? null : order.getPhysician().getId());
                demo.setCodifiedCode(order.getPhysician() == null ? null : order.getPhysician().getCode());
                demo.setCodifiedName(order.getPhysician() == null ? null : order.getPhysician().getName());
                demosValues.add(demo);

                int cantPhysician = Integer.parseInt(configurationServices.get("TotalMedicosAuxiliares").getValue());
                for (int i = 1; i <= cantPhysician; i += 1)
                {
                    int intTest = 200 + i;
                    intTest = intTest * -1;

                    demo = new DemographicValue();
                    demo.setIdDemographic(intTest);
                    demo.setEncoded(true);

                    Integer idDemo;
                    String codeDemo;
                    String nameDemo;

                    if ((i - 1) < order.getAuxiliaryPhysicians().size())
                    {
                        Physician physicianAux = order.getAuxiliaryPhysicians().get(i - 1);
                        idDemo = physicianAux == null ? null : physicianAux.getId();
                        codeDemo = physicianAux == null ? null : physicianAux.getCode();
                        nameDemo = physicianAux == null ? null : physicianAux.getName();
                    } else
                    {
                        idDemo = null;
                        codeDemo = null;
                        nameDemo = null;
                    }

                    demo.setCodifiedId(idDemo);
                    demo.setCodifiedCode(codeDemo);
                    demo.setCodifiedName(nameDemo);

                    demosValues.add(demo);
                }
            }

            if (order.getAllDemographics() != null && !order.getAllDemographics().isEmpty())
            {
                demosValues.addAll(order.getAllDemographics());
            }
            return demosValues;
        }
        return null;
    }

    @Transactional(transactionManager = "transactionManager", isolation = Isolation.READ_COMMITTED, rollbackFor
            =
            {
                EnterpriseNTException.class, Exception.class
            })
    @Override
    public synchronized Order create(Order order, int user, int branch) throws Exception
    {
        try
        {
            //Valida que la orden tenga asociado paciente
            if (order.getPatient() == null)
            {
                List<String> errors = new ArrayList<>(0);
                errors.add("1|Order not valid must contains patient");
                OrderCreationLog.error(errors.get(0));
                throw new EnterpriseNTException(errors);
            }
            //Revisa si el numero de orden viene null entonces se autogenera
            boolean autonumeric = false;
            int orderDigits = Integer.parseInt(configurationServices.get("DigitosOrden").getValue().trim());
            
            // Obtener la fecha actual
            LocalDate fechaActual = LocalDate.now();
            // Definir el formato deseado
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyyMMdd");
            // Formatear la fecha en el formato deseado
            int fechaEntero = Integer.parseInt(fechaActual.format(formato));
            
            
            
            if(order.getHasAppointment() == 1 || fechaEntero != order.getCreatedDateShort()){
                
               
                        
                Branch branchInformation = branchService.getBasic(order.getBranch().getId(), null,null,null);
                
                long initiappointment = Tools.getCompleteOrderNumber(order.getCreatedDateShort(), orderDigits, branchInformation.getMinimum());
                 
                int nextValue = appointmentService.getSecuenceOrder(order.getCreatedDateShort(),order.getBranch().getId(), initiappointment);
               
                nextValue = nextValue == 0 ? branchInformation.getMaximum() - branchInformation.getNumberAppointments() : branchInformation.getMaximum() - branchInformation.getNumberAppointments() + nextValue;

                if (nextValue <= branchInformation.getMaximum())
                {
                   int date = order.getCreatedDateShort();
                   order.setOrderNumber(Tools.getCompleteOrderNumber(date, orderDigits, nextValue));
                } else
                {
                    List<String> errorFields = new ArrayList<>(0);
                    errorFields.add("1|Rango de ordenes inavalidos para citas");
                    throw new EnterpriseNTException(errorFields);
                }
            
            }
                
                
            if (order.getOrderNumber() == null || order.getOrderNumber() == 0)
            {
                //Debe genera autonumerico
                autonumeric = true;
            } else if (String.valueOf(order.getOrderNumber()).length() < (8 + orderDigits))
            {
                //El numero de orden enviado no es correcto
                List<String> errors = new ArrayList<>(0);
                errors.add("1|Order " + order.getOrderNumber() + " not valid");
                OrderCreationLog.error(errors.get(0));
                throw new EnterpriseNTException(errors);
            } else
            {
                //No se debe generar autonumerico porque la orden ya tiene numero asignado
                autonumeric = false;
            }

            Patient patient = order.getPatient();
            order.getPatient().setUpdatePatient(order.getPatient().getUpdatePatient() == null ? true : order.getPatient().getUpdatePatient());
            //Realiza el registro o actualizacion del paciente
            patient = patientService.save(order.getPatient(), user, order.getPatient().getUpdatePatient());
            OrderCreationLog.info("The patient has been created or updated correctly " + order.getExternalId());

            //Establece el paciente registrado en base de datos al objeto orden
            order.setPatient(patientService.get(patient.getId()));
            Date createdDate = new Date();
            order.setCreatedDate(createdDate);
            order.setLastUpdateDate(createdDate);
            User userBean = new User();
            userBean.setId(user);
            order.setLastUpdateUser(userBean);
            order.setCreateUser(userBean);
            Order orderCreated = null;
            AuthorizedUser session = JWT.decode(request);

            // OBtenemos los demograficos que no son requeridos para esa sede pero son obligatorios
            List<DemographicBranch> listRequeridDemographis = getDemographicsExcluded(session.getBranch());

            try
            {
                //Si es autonumerico invoca el metodo para genera el autonumerico
                if (autonumeric)
                {
                    order.setOrderNumber(getAutonumericNumber(order));
                }
                OrderCreationLog.info("ORDER CREATE " + order.getOrderNumber());
                orderCreated = orderDao.create(order, listRequeridDemographis);
                OrderCreationLog.info("The order has been successfully created" + order.getExternalId());
                OrderCreationLog.info("The order has been successfully created 2 " + (orderCreated != null ? orderCreated.getOrderNumber() : null));
            } catch (DuplicateKeyException ex)
            {
                String error;
                error = "1|order duplicate - " + order.getOrderNumber();
                OrderCreationLog.error(ex);
                throw new EnterpriseNTException(Arrays.asList(error));
            }
            if (orderCreated != null)
            {
                if (order.getListDiagnostic() != null && order.getListDiagnostic().isEmpty() == false)
                {
                    if (order.getListDiagnostic().size() > 0)
                    {
                        diagnosticDao.createAllByOrder(orderCreated);
                        OrderCreationLog.info("The order has been successfully created" + order.getExternalId());
                    }
                }

                boolean check = !configurationServices.get("Trazabilidad").getValue().equals("1");

                List<Integer> tests = saveTests(order, user, createdDate, orderCreated.getBranch().getId(), order.getType().getCode().equals("S") ? 1 : 2, check, false);
                List<Test> listTest = new ArrayList<>();

                for (Integer itemTest : tests)
                {
                    net.cltech.enterprisent.domain.masters.test.Test objTest = testService.get(itemTest, null, null, null);
                    Test objAux = new Test();
                    objAux.setId(objTest.getId());
                    objAux.setCode(objTest.getCode());
                    objAux.setName(objTest.getName());
                    objAux.setTestType(objTest.getTestType());
                    objAux.setPrint(objTest.getPrintOrder());
                    objAux.setAbbr(objTest.getAbbr());
                    objAux.setArea(objTest.getArea());
                    objAux.setSample(objTest.getSample());

                    //Buscar la tarifa del examen para el ingreso de ordenes sin historia
                    Test aux = order.getTests().stream().filter(test -> Objects.equals(test.getId(), itemTest)).findAny().orElse(null);
                    if (aux != null)
                    {
                        objAux.setHasprofile(false);
                        objAux.setRate(aux.getRate());
                    } else
                    {
                        objAux.setHasprofile(true);
                    }

                    listTest.add(objAux);
                }
                OrderCreationLog.info("The tests have been successfully associated with the order");
                savePrices(order);
                order.setTests(listTest);

                long orderNumber = orderCreated.getOrderNumber();
                statisticService.saveOrder(orderNumber);
                OrderCreationLog.info("The order was recorded in statistics " + orderNumber);
                agileStatisticService.updateOrderBranch(orderNumber, true);

                if (!check)
                {
                    sampleTrackingService.sampleOrdered(orderNumber, session, LISEnum.ResultSampleState.CHECKED.getValue(), null);
                } else
                {
                    sampleTrackingService.sampleOrdered(orderNumber, session, !check ? LISEnum.ResultSampleState.CHECKED.getValue() : LISEnum.ResultSampleState.ORDERED.getValue(), LISEnum.OriginModule.ORIGIN.getValue(), null);
                }
                OrderCreationLog.info("The traceability of the sample was inserted in the correct order " + orderNumber);
                //Agregar orden padre - hijo para rellamado
                if (order.getType().getId() == ORDER_TYPE_RECALLED)
                {
                    Order daughterOrder = new Order();
                    daughterOrder.setOrderNumber(orderNumber);
                    Order fatherOrder = new Order();
                    fatherOrder.setOrderNumber(order.getFatherOrder());
                    recalledService.create(new Recalled(fatherOrder, daughterOrder));
                    OrderCreationLog.info("The son order was added to the parent order correctly");
                }
                //Trazabilidad de la orden
                List<AuditOperation> auditList = new ArrayList<>(0);
                auditList.add(new AuditOperation(orderNumber, null, null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_ORDER, Tools.jsonObject(getAsDemographicList(order.getOrderNumber(), order.getHasAppointment())), null, null, null, null));
                trackingService.registerOperationTracking(auditList);

                OrderCreationLog.info("Traceability of the order was successfully recorded");
                MiddlewareLog.info("entro  AST MW ORDER  : " + orderNumber);
                //integrationMiddlewareService.sendOrderASTM(orderNumber, null, null, Constants.ENTRY, null, null, order.getDeleteTests(), branch, false);
                //dashBoardService.dashBoardOpportunityTime(orderNumber, tests, DashBoardOpportunityTime.ACTION_INSERT);
            
                //Se crean los valores de la caja en cero para que despues el usuario pueda editar campos 
                //como descuento, copago y cuota moderadora
                if ("True".equals(configurationServices.get("CajaAutomatica").getValue()))
                {
                    CashBox cash = order.getCashbox();
                    if (cash != null)
                    {
                        cash.setOrder(orderNumber);
                        cash.getHeader().setOrder(orderNumber);
                        cash.getHeader().setRate(orderCreated.getRate());
                        cash.getHeader().setCopayType(0);
                    } else
                    {
                        cash = new CashBox();
                        cash.setOrder(orderNumber);
                        cash.getHeader().setOrder(orderNumber);
                        cash.getHeader().setRate(orderCreated.getRate());
                        cash.getHeader().setSubTotal(new BigDecimal(0.0));
                        cash.getHeader().setDiscountValue(new BigDecimal(0.0));
                        cash.getHeader().setDiscountPercent(new BigDecimal(0.0));
                        cash.getHeader().setTaxValue(new BigDecimal(0.0));
                        cash.getHeader().setCopay(new BigDecimal(0.0));
                        cash.getHeader().setFee(new BigDecimal(0.0));
                        cash.getHeader().setTotalPaid(new BigDecimal(0.0));
                        cash.getHeader().setBalance(new BigDecimal(0.0));
                        cash.getHeader().setDiscountValueRate(new BigDecimal(0.0));
                        cash.getHeader().setDiscountPercentRate(new BigDecimal(0.0));
                        cash.getHeader().setCopayType(0);
                        cash.getHeader().setCharge(new BigDecimal(0.0));
                    }
                    cashBoxService.save(cash);
                }

                //Se insertan los medicos auxiliares
                if ("True".equals(configurationServices.get("MedicosAuxiliares").getValue()))
                {
                    if (order.getAuxiliaryPhysicians().size() > 0)
                    {
                        orderDao.insertAuxiliaryPhysicians(order.getAuxiliaryPhysicians(), orderNumber);
                    }
                }

                OrderCreationLog.info("--------------------------------------------------------------------");
                OrderCreationLog.info("The order was SUCCESSFULLY CREATED in the LIS");
                OrderCreationLog.info("--------------------------------------------------------------------");
                // Hilo para Toma de muestra Hospitalaria
                List<Sample> listSamples = sampleTrackingService.getOrder(order.getOrderNumber(), false) != null ? sampleTrackingService.getOrder(order.getOrderNumber(), false).getSamples() : new ArrayList<>();

                order.setSamples(listSamples);
                if (order.getService() != null && order.getService().getId() != null && order.getService().getId() > 0)
                {

                    CompletableFuture.runAsync(()
                            ->
                    {
                        try
                        {
                            ServiceLaboratory orderService = serviceService.filterById(order.getService().getId());

                            if (orderService.getHospitalSampling())
                            {
                                for (Sample sample : order.getSamples())
                                {
                                    List<Integer> idsTestBySample = sample.getTests().stream()
                                            .map(test -> test.getId()).collect(Collectors.toList());
                                    dashBoardService.dashBoardHospitalSampling(orderNumber, sample.getId(), idsTestBySample, DashBoardOpportunityTime.ACTION_INSERT_HOSPITAL_SAMPLING);
                                    OrderCreationLog.info("Finalizo tableros");
                                }
                            }
                        } catch (Exception ex)
                        {
                            OrderCreationLog.error(ex.getMessage());
                            Logger.getLogger(OrderServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);

                        }
                    });
                }
                cashBoxService.createBoxInExternalBilling(new CashBox(), 0, orderNumber);
            }
            
            if(order.getHasAppointment() == 1){
                appointmentService.createAppointment(order);
            }

            // Envió de la orden al API de Facturación Externa:
            return orderCreated;
        } catch (EnterpriseNTException exeptionNT)
        {
            if (exeptionNT.getErrorFields().isEmpty())
            {
                OrderCreationLog.error(exeptionNT);
                throw new EnterpriseNTException(Arrays.asList("Check the log for more information about the error that was just generated"));
            } else
            {
                exeptionNT.getErrorFields().stream().forEach(ex
                        ->
                {
                    OrderCreationLog.error(ex);
                });
                throw new EnterpriseNTException(exeptionNT.getErrorFields());
            }
        } catch (SQLException sQLException)
        {
            String addMessagge = order.getOrderNumber() != null ? order.getOrderNumber().toString() + " create order excepcion - " : "";
            OrderCreationLog.error(sQLException.getMessage() + " " + addMessagge);
            throw new SQLException(addMessagge + sQLException);

        } catch (Exception exeption)
        {
            String addMessagge = order.getOrderNumber() != null ? order.getOrderNumber().toString() + " create order excepcion - " : "";
            OrderCreationLog.error(exeption.getMessage() + " " + addMessagge);
            throw new Exception(addMessagge + exeption);
        }
    }

    @Transactional(transactionManager = "transactionManager", isolation = Isolation.READ_COMMITTED)
    @Override
    public Order update(Order order, int user, int branch, int appointment) throws Exception
    {
        List<DemographicValue> demographicold = getAsDemographicList(order.getOrderNumber(), appointment);
        //Valida que la orden tenga asociado paciente
        if (order.getPatient() == null)
        {
            List<String> errors = new ArrayList<>(0);
            errors.add("1|Order not valid must contains patient");
            throw new EnterpriseNTException(errors);
        }
        //Consultar los examnes que estan asociados a la orden antes de actualizarla
        List<Test> listTestBefore = resultService.allTestByOrder(order.getOrderNumber(), "A", null, null);
        //TODO: Revisar auditoria de la orden
        //Realiza la actualizacion del paciente
        Patient patient = new Patient();

        order.getPatient().setUpdatePatient(order.getPatient().getUpdatePatient() == null ? true : order.getPatient().getUpdatePatient());
        patient = patientService.save(order.getPatient(), user, order.getPatient().getUpdatePatient());
        
        //Establece el paciente registrado en base de datos al objeto orden
        order.setPatient(patient);
        order.setLastUpdateDate(new Date());
        User userBean = new User();
        userBean.setId(user);
        order.setLastUpdateUser(userBean);
        boolean check = !configurationServices.get("Trazabilidad").getValue().equals("1");

        Order orderUpdate = orderDao.update(order);
        orderUpdate.setPatient(patient);

        List<Integer> tests = saveTests(order, user, order.getLastUpdateDate(), orderUpdate.getBranch().getId(), order.getType().getCode().equals("S") ? 1 : 2, check, false);

        List<Test> listTest = new ArrayList<>();

        for (Integer itemTest : tests)
        {
            net.cltech.enterprisent.domain.masters.test.Test objTest = testService.get(itemTest, null, null, null);
            Test objAux = new Test();
            objAux.setId(objTest.getId());
            objAux.setCode(objTest.getCode());
            objAux.setName(objTest.getName());
            objAux.setTestType(objTest.getTestType());
            objAux.setPrint(objTest.getPrintOrder());
            objAux.setAbbr(objTest.getAbbr());
            objAux.setArea(objTest.getArea());
            objAux.setSample(objTest.getSample());
            listTest.add(objAux);
        }

        savePrices(order);
        order.setTests(listTest);

        long orderNumber = orderUpdate.getOrderNumber();
        statisticService.saveOrderUpdate(orderNumber);
               
        AuthorizedUser session = new AuthorizedUser();
        session.setBranch(branch);
        session.setId(user);
        

        if (tests.size() > 0)
        {

            if (!check)
            {
                sampleTrackingService.sampleOrdered(orderNumber, session, LISEnum.ResultSampleState.CHECKED.getValue(), tests);
            } else
            {
                sampleTrackingService.sampleOrdered(orderNumber, session, !check ? LISEnum.ResultSampleState.CHECKED.getValue() : LISEnum.ResultSampleState.ORDERED.getValue(), LISEnum.OriginModule.ORIGIN.getValue(), tests);
            }
        }

        List<DemographicValue> demographicnew = getAsDemographicList(order.getOrderNumber(), appointment);

        //Trazabilidad de la orden
        List<AuditOperation> auditList = new ArrayList<>(0);
        List<DemographicValue> demographicupdate = Tools.compareDemographics(demographicold, demographicnew);
        if (demographicupdate.size() > 0)
        {
            auditList.add(new AuditOperation(order.getOrderNumber(), null, null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_ORDER, Tools.jsonObject(demographicupdate), order.getIdMotive(), order.getCommentary(), null, null));
            trackingService.registerOperationTracking(auditList);
        }

        CashBox cash = order.getCashbox();
        if (cash != null)
        {
            cash.setOrder(orderNumber);
            cash.getHeader().setRate(orderUpdate.getRate());
            cashBoxService.save(cash);
        }

        ExternalBillingLog.info("[envio actualizacion de la orden]:");
        cashBoxService.createBoxInExternalBilling(new CashBox(), 1, orderNumber);

        //Envia orden al middleware apenas se crea la orden 
        OrderCreationLog.info("Traceability of the order was successfully recorded");
        MiddlewareLog.info("ORDER SEND MIDDLEARE");
        //integrationMiddlewareService.sendOrderASTM(orderNumber, null, null, Constants.ENTRY, null, null, order.getDeleteTests(), branch, false);

        //Se actualizan los medicos auxiliares
        if ("True".equals(configurationServices.get("MedicosAuxiliares").getValue()))
        {
            if (order.getAuxiliaryPhysicians().size() > 0)
            {
                orderDao.deleteAuxiliaryPhysicians(order.getOrderNumber());
                orderDao.insertAuxiliaryPhysicians(order.getAuxiliaryPhysicians(), orderNumber);
            } else
            {
                orderDao.deleteAuxiliaryPhysicians(order.getOrderNumber());
            }
        }

        //Hilo para enviar orden al middleware
        if (Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("modificarOrden").getValue()) == true)
        {
            //Hilo para eventos
            CompletableFuture.runAsync(()
                    ->
            {
                eventsOrderService.update(order);
            });
        }

        return orderUpdate;
    }

    private long getAutonumericNumber(Order order) throws Exception
    {
        int nextValue = toolsDao.nextVal(getNameSequence(order));

        if (nextValue != -1 && nextValue != -2)
        {
            int orderDigits = Integer.parseInt(configurationServices.get("DigitosOrden").getValue().trim());
            int date = order.getCreatedDateShort();
            return Tools.getCompleteOrderNumber(date, orderDigits, nextValue);
        } else if (nextValue == -2)
        {
            //No se tiene numeracion asignada
            List<String> errors = new ArrayList<>(0);
            errors.add("5|The numeration limit of the order was passed.");
            throw new EnterpriseNTException(errors);
        } else
        {
            if (order.getType().getId() == ORDER_TYPE_RECALLED)
            {
                //No se tiene numeracion asignada
                List<String> errors = new ArrayList<>(0);
                errors.add("6|Order numbering of recalled has not found.");
                throw new EnterpriseNTException(errors);
            } else
            {
                //No se tiene numeracion asignada
                List<String> errors = new ArrayList<>(0);
                errors.add("4|Order numbering has not found.");
                throw new EnterpriseNTException(errors);
            }
        }
    }

    public String getNameSequence(Order order) throws Exception
    {
        int idBranchOrService = -1;
        String orderTypeNumber = configurationServices.get(ConfigurationConstants.KEY_NUMERATION).getValue();

        if (orderTypeNumber.equals(ConfigurationConstants.NUMERATION_GENERAL) && order.getType().getId() != ORDER_TYPE_RECALLED)
        {
            if (!toolsDao.validateSequence(ConfigurationConstants.NUMERATION_GENERAL))
            {
                int minimun = 1;
                int maximun = Integer.parseInt(String.join("", Collections.nCopies(Integer.parseInt(configurationServices.get("DigitosOrden").getValue().trim()), "9")));
                toolsDao.createSequence(Constants.RESOLUTION_SEQUENCE, minimun, 1, maximun);
            }
            return Constants.SEQUENCE_GENERAL;
        } else if (orderTypeNumber.equals(ConfigurationConstants.NUMERATION_BRANCH) && order.getType().getId() != ORDER_TYPE_RECALLED)
        {
            if (order.getBranch() != null && order.getBranch().getId() != null)
            {
                idBranchOrService = order.getBranch().getId();
            } else
            {
                //Se numera por sede pero la orden no se le envia la sede
                List<String> errors = new ArrayList<>(0);
                errors.add("3|Order must be containts branch");
                throw new EnterpriseNTException(errors);
            }
        } else if (orderTypeNumber.equals(ConfigurationConstants.NUMERATION_SERVICE) && order.getType().getId() != ORDER_TYPE_RECALLED)
        {
            if (order.getService() != null)
            {
                idBranchOrService = order.getService().getId();
            } else
            {
                //Se numera por servicio pero la orden no se le envia el servicio
                List<String> errors = new ArrayList<>(0);
                errors.add("3|Order must be containts service");
                throw new EnterpriseNTException(errors);
            }
        }
        if (order.getType().getId() == ORDER_TYPE_RECALLED)
        {
            return Constants.SEQUENCE_RECALLED;
        } else
        {
            return Constants.SEQUENCE + idBranchOrService;
        }
    }

    @Override
    public List<Demographic> getDemographics(String origin) throws Exception
    {
        try
        {
            AuthorizedUser user = JWT.decode(request);
            List<Demographic> demos = new ArrayList<>(0);
            Demographic demo = null;
            List<BranchDemographic> demoItemsByBranch = new ArrayList<>();
            List<Demographic> Demofixed = orderDao.listOderingfixed();
            Map<String, Short> map = new HashMap<>();

            for (Demographic demographic : Demofixed)
            {

                if (demographic.getId() == Constants.DOCUMENT_TYPE)
                {
                    map.put("typeDocumentValue", demographic.getOrdering());
                }

                if (Boolean.parseBoolean(configurationServices.get("MedicosAuxiliares").getValue()))
                {
                    int cantPhysician = Integer.parseInt(configurationServices.get("TotalMedicosAuxiliares").getValue());
                    for (int i = 1; i <= cantPhysician; i += 1)
                    {
                        int intTest = 200 + i;
                        intTest = intTest * -1;
                        if (demographic.getId() == intTest)
                        {
                            map.put(String.valueOf(intTest), demographic.getOrdering());
                        }
                    }
                }

                if (demographic.getId() == Constants.PATIENT_ID)
                {
                    map.put("patientIDValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.PATIENT_LAST_NAME)
                {
                    map.put("patientLastNameValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.PATIENT_SURNAME)
                {
                    map.put("patientSurnameValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.PATIENT_NAME)
                {
                    map.put("patientNameValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.PATIENT_SECOND_NAME)
                {
                    map.put("patientSecondNameValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.PATIENT_SEX)
                {
                    map.put("patientSexValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.PATIENT_BIRTHDAY)
                {

                    map.put("patientBirthdayValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.PATIENT_AGE)
                {

                    map.put("patientAgeValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.PATIENT_EMAIL)
                {

                    map.put("patientEmailValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.PATIENT_PHONE)
                {

                    map.put("patientphoneValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.WEIGHT)
                {

                    map.put("WeightValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.SIZE)
                {

                    map.put("patientSizeValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.PATIENT_ADDRESS)
                {

                    map.put("patientAddresValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.RACE)
                {

                    map.put("patientRaceValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.ORDER_ID)
                {

                    map.put("patientOrdenValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.ORDERTYPE)
                {

                    map.put("OrderTypeValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.BRANCH)
                {

                    map.put("BranchValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.SERVICE)
                {

                    map.put("ServiceValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.ACCOUNT)
                {

                    map.put("accountValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.RATE)
                {

                    map.put("rateValue", demographic.getOrdering());
                }
                if (demographic.getId() == Constants.PHYSICIAN)
                {

                    map.put("physicianValue", demographic.getOrdering());
                }
            }

            if (origin.equals("H"))
            {
                //Historia
                //Tipo de Documento -- Demografico Fijo
                if (Boolean.parseBoolean(configurationServices.get("ManejoTipoDocumento").getValue()))
                {
                    demo = new Demographic();
                    demo.setId(Constants.DOCUMENT_TYPE);
                    demo.setName("0233");
                    demo.setEncoded(true);
                    demo.setLastOrder(true);
                    demo.setModify(true);
                    demo.setObligatory((short) 1);
                    demo.setOrigin("H");
                    if (map.get("typeDocumentValue") != null)
                    {
                        demo.setOrderingDemo(map.get("typeDocumentValue").intValue());
                    } else
                    {
                        demo.setOrderingDemo(1);
                    }
                    demo.setItems(documentTypeService.list(true).stream().map((DocumentType type)
                            ->
                    {
                        DemographicItem d = new DemographicItem();
                        d.setId(type.getId());
                        d.setCode(type.getAbbr());
                        d.setName(type.getName());
                        return d;
                    }).collect(Collectors.toList()));

                    if (Boolean.parseBoolean(configurationServices.get("DemographicsByBranch").getValue()))
                    {
                        demoItemsByBranch = new ArrayList<>();
                        demoItemsByBranch = demographicItemDao.getBranchDemographicsItems(user.getBranch(), Constants.DOCUMENT_TYPE);
                        for (int i = 0; i < demo.getItems().size(); i++)
                        {
                            DemographicItem item = demo.getItems().get(i);
                            BranchDemographic objAux = demoItemsByBranch.stream().filter(demoBranch -> Objects.equals(demoBranch.getDemographicItem().getId(), item.getId())).findAny().orElse(null);
                            if (objAux == null)
                            {
                                demo.getItems().remove(i);
                                i--;
                            }
                        }
                        demos.add(demo);
                    } else
                    {
                        demos.add(demo);
                    }
                }

                //Cedula
                demo = new Demographic();
                demo.setId(Constants.PATIENT_ID);
                demo.setName("0117");
                demo.setEncoded(false);
                demo.setModify(true);
                demo.setObligatory((short) 1);
                demo.setFormat(configurationServices.get("FormatoHistoria").getValue());
                demo.setOrigin("H");
                if (map.get("patientIDValue") != null)
                {
                    demo.setOrderingDemo(map.get("patientIDValue").intValue());
                } else
                {
                    demo.setOrderingDemo(2);
                }
                demos.add(demo);

                //Apellido
                demo = new Demographic();
                demo.setId(Constants.PATIENT_LAST_NAME);
                demo.setName("0234");
                demo.setEncoded(false);
                demo.setModify(true);
                demo.setObligatory((short) 1);
                demo.setOrigin("H");
                if (map.get("patientLastNameValue") != null)
                {
                    demo.setOrderingDemo(map.get("patientLastNameValue").intValue());
                } else
                {
                    demo.setOrderingDemo(3);
                }
                demos.add(demo);

                //Segundo Apellido
                demo = new Demographic();
                demo.setId(Constants.PATIENT_SURNAME);
                demo.setName("0235");
                demo.setEncoded(false);
                demo.setModify(true);
                demo.setObligatory((short) 0);
                demo.setOrigin("H");
                if (map.get("patientSurnameValue") != null)
                {
                    demo.setOrderingDemo(map.get("patientSurnameValue").intValue());
                } else
                {
                    demo.setOrderingDemo(4);
                }
                demos.add(demo);

                //Nombre
                demo = new Demographic();
                demo.setId(Constants.PATIENT_NAME);
                demo.setName("0236");
                demo.setEncoded(false);
                demo.setModify(true);
                demo.setObligatory((short) 1);
                demo.setOrigin("H");
                if (map.get("patientNameValue") != null)
                {
                    demo.setOrderingDemo(map.get("patientNameValue").intValue());
                } else
                {
                    demo.setOrderingDemo(5);
                }
                demos.add(demo);

                //Segundo Nombre
                demo = new Demographic();
                demo.setId(Constants.PATIENT_SECOND_NAME);
                demo.setName("0237");
                demo.setEncoded(false);
                demo.setModify(true);
                demo.setObligatory((short) 0);
                demo.setOrigin("H");
                if (map.get("patientSecondNameValue") != null)
                {
                    demo.setOrderingDemo(map.get("patientSecondNameValue").intValue());
                } else
                {
                    demo.setOrderingDemo(6);
                }
                demos.add(demo);

                //Sexo
                demo = new Demographic();
                demo.setId(Constants.PATIENT_SEX);
                demo.setName("0124");
                demo.setEncoded(true);
                demo.setModify(true);
                demo.setObligatory((short) 1);
                demo.setOrigin("H");
                if (map.get("patientSexValue") != null)
                {
                    demo.setOrderingDemo(map.get("patientSexValue").intValue());
                } else
                {
                    demo.setOrderingDemo(7);
                }
                demo.setItems(listService.list(6).stream().map((Item t)
                        ->
                {
                    DemographicItem d = new DemographicItem();
                    d.setId(t.getId());
                    d.setCode(t.getCode());
                    d.setName(t.getEsCo());
                    return d;
                }).collect(Collectors.toList()));
                demos.add(demo);

                //Fecha Nacimiento
                demo = new Demographic();
                demo.setId(Constants.PATIENT_BIRTHDAY);
                demo.setName("0120");
                demo.setEncoded(false);
                demo.setFormat(configurationServices.get("FormatoFecha").getValue().toUpperCase().replace("D", "0").replace("M", "0").replace("Y", "0"));
                demo.setPlaceholder(configurationServices.get("FormatoFecha").getValue());
                demo.setModify(true);
                demo.setObligatory((short) 1);
                demo.setOrigin("H");
                if (map.get("patientBirthdayValue") != null)
                {
                    demo.setOrderingDemo(map.get("patientBirthdayValue").intValue());
                } else
                {
                    demo.setOrderingDemo(8);
                }
                demos.add(demo);

                //Edad
                demo = new Demographic();
                demo.setId(Constants.PATIENT_AGE);
                demo.setName("0102");
                demo.setEncoded(false);
                demo.setFormat("00.00.00");
                demo.setPlaceholder("yy.mm.dd");
                demo.setModify(true);
                demo.setObligatory((short) 1);
                demo.setOrigin("H");
                if (map.get("patientAgeValue") != null)
                {
                    demo.setOrderingDemo(map.get("patientAgeValue").intValue());
                } else
                {
                    demo.setOrderingDemo(9);
                }
                demos.add(demo);

                //Email
                demo = new Demographic();
                demo.setId(Constants.PATIENT_EMAIL);
                demo.setName("0135");
                demo.setEncoded(false);
                demo.setModify(true);
                demo.setObligatory((short) 0);
                demo.setOrigin("H");
                if (map.get("patientEmailValue") != null)
                {
                    demo.setOrderingDemo(map.get("patientEmailValue").intValue());
                } else
                {
                    demo.setOrderingDemo(10);
                }
                demos.add(demo);

                //Telefono
                demo = new Demographic();
                demo.setId(Constants.PATIENT_PHONE);
                demo.setName("0188");
                demo.setEncoded(false);
                demo.setModify(true);
                demo.setObligatory((short) 0);
                demo.setOrigin("H");
                if (map.get("patientphoneValue") != null)
                {
                    demo.setOrderingDemo(map.get("patientphoneValue").intValue());
                } else
                {
                    demo.setOrderingDemo(11);
                }
                demos.add(demo);

                //Consulta los demograficos fijos
                //Dato Fijo Peso
                if (Boolean.parseBoolean(configurationServices.get("ManejoPeso").getValue()))
                {
                    demo = new Demographic();
                    demo.setId(Constants.WEIGHT);
                    demo.setName("0238");
                    demo.setEncoded(false);
                    demo.setLastOrder(false);
                    demo.setModify(true);
                    demo.setObligatory((short) 0);
                    demo.setOrigin("H");
                    demo.setFormat("999");
                    demo.setPlaceholder("###");
                    if (map.get("WeightValue") != null)
                    {
                        demo.setOrderingDemo(map.get("WeightValue").intValue());
                    } else
                    {
                        demo.setOrderingDemo(12);
                    }
                    demos.add(demo);
                }

                //Manejo Peso
                //Demografico Fijo
                if (Boolean.parseBoolean(configurationServices.get("ManejoTalla").getValue()))
                {
                    demo = new Demographic();
                    demo.setId(Constants.SIZE);
                    demo.setName("0239");
                    demo.setEncoded(false);
                    demo.setLastOrder(false);
                    demo.setModify(true);
                    demo.setObligatory((short) 0);
                    demo.setOrigin("H");
                    demo.setFormat("9.99");
                    demo.setPlaceholder("#.##");
                    if (map.get("patientSizeValue") != null)
                    {
                        demo.setOrderingDemo(map.get("patientSizeValue").intValue());
                    } else
                    {
                        demo.setOrderingDemo(13);
                    }
                    demos.add(demo);
                }

                //Dirección
                demo = new Demographic();
                demo.setId(Constants.PATIENT_ADDRESS);
                demo.setName("0187");
                demo.setEncoded(false);
                demo.setModify(true);
                demo.setObligatory((short) 0);
                demo.setOrigin("H");
                if (map.get("patientAddresValue") != null)
                {
                    demo.setOrderingDemo(map.get("patientAddresValue").intValue());
                } else
                {
                    demo.setOrderingDemo(14);
                }
                demos.add(demo);

                //Manejo Raza
                //Demografico Fijo
                if (Boolean.parseBoolean(configurationServices.get("ManejoRaza").getValue()))
                {
                    demo = new Demographic();
                    demo.setId(Constants.RACE);
                    demo.setName("0091");
                    demo.setEncoded(true);
                    demo.setLastOrder(true);
                    demo.setModify(true);
                    demo.setObligatory((short) 1);
                    demo.setOrigin("H");
                    if (map.get("patientRaceValue") != null)
                    {
                        demo.setOrderingDemo(map.get("patientRaceValue").intValue());
                    } else
                    {
                        demo.setOrderingDemo(15);
                    }
                    List<Race> races = raceService.filterByState(true);
                    demo.setItems(races.stream().map((Race t)
                            ->
                    {
                        DemographicItem d = new DemographicItem();
                        d.setId(t.getId());
                        d.setCode(t.getCode());
                        d.setName(t.getName());
                        return d;
                    }).collect(Collectors.toList()));

                    if (Boolean.parseBoolean(configurationServices.get("DemographicsByBranch").getValue()))
                    {
                        demoItemsByBranch = new ArrayList<>();
                        demoItemsByBranch = demographicItemDao.getBranchDemographicsItems(user.getBranch(), Constants.RACE);
                        for (int i = 0; i < demo.getItems().size(); i++)
                        {
                            DemographicItem item = demo.getItems().get(i);
                            BranchDemographic objAux = demoItemsByBranch.stream().filter(demoBranch -> Objects.equals(demoBranch.getDemographicItem().getId(), item.getId())).findAny().orElse(null);
                            if (objAux == null)
                            {
                                demo.getItems().remove(i);
                                i--;
                            }
                        }
                        demos.add(demo);
                    } else
                    {
                        demos.add(demo);
                    }
                }
            } else
            {
                //ORDEN
                demo = new Demographic();
                demo.setId(Constants.ORDER_ID);
                demo.setName("0110");
                demo.setEncoded(false);
                demo.setModify(!Boolean.parseBoolean(configurationServices.get("NumeroOrdenAutomatico").getValue()));
                demo.setObligatory(demo.isModify() ? (short) 1 : (short) 0);
                demo.setOrigin("O");
                if (map.get("patientOrdenValue") != null)
                {
                    demo.setOrderingDemo(map.get("patientOrdenValue").intValue());
                } else
                {
                    demo.setOrderingDemo(1);
                }
                demos.add(demo);

                //Fecha Registro
                demo = new Demographic();
                demo.setId(Constants.ORDER_DATE);
                demo.setName("0240");
                demo.setEncoded(false);
                demo.setModify(false);
                demo.setObligatory((short) 0);
                demo.setOrigin("O");
                demos.add(demo);

                //Tipo de Orden
                //Demografico Fijo
                demo = new Demographic();
                demo.setId(Constants.ORDERTYPE);
                demo.setName("0088");
                demo.setEncoded(true);
                demo.setModify(false);
                demo.setObligatory((short) 1);
                demo.setOrigin("O");
                if (map.get("OrderTypeValue") != null)
                {
                    demo.setOrderingDemo(map.get("OrderTypeValue").intValue());
                } else
                {
                    demo.setOrderingDemo(2);
                }
                demo.setItems(orderTypeService.filterByState(true).stream().map((OrderType t)
                        ->
                {
                    DemographicItem d = new DemographicItem();
                    d.setId(t.getId());
                    d.setCode(t.getCode());
                    d.setName(t.getName());
                    return d;
                }).collect(Collectors.toList()));

                if (Boolean.parseBoolean(configurationServices.get("DemographicsByBranch").getValue()))
                {
                    demoItemsByBranch = new ArrayList<>();
                    demoItemsByBranch = demographicItemDao.getBranchDemographicsItems(user.getBranch(), Constants.ORDERTYPE);
                    for (int i = 0; i < demo.getItems().size(); i++)
                    {
                        DemographicItem item = demo.getItems().get(i);
                        BranchDemographic objAux = demoItemsByBranch.stream().filter(demoBranch -> Objects.equals(demoBranch.getDemographicItem().getId(), item.getId())).findAny().orElse(null);
                        if (objAux == null)
                        {
                            demo.getItems().remove(i);
                            i--;
                        }
                    }
                    demos.add(demo);
                } else
                {
                    demos.add(demo);
                }

                //Sede
                //Demografico Fijo
                demo = new Demographic();
                demo.setId(Constants.BRANCH);
                demo.setName("0003");
                demo.setEncoded(true);
                demo.setModify(false);
                demo.setObligatory((short) 1);
                demo.setOrigin("O");
                if (map.get("BranchValue") != null)
                {
                    demo.setOrderingDemo(map.get("BranchValue").intValue());
                } else
                {
                    demo.setOrderingDemo(3);
                }
                demo.setItems(branchService.list(true).stream().map((Branch t)
                        ->
                {
                    DemographicItem d = new DemographicItem();
                    d.setId(t.getId());
                    d.setCode(t.getCode());
                    d.setName(t.getName());
                    return d;
                }).collect(Collectors.toList()));

                if (Boolean.parseBoolean(configurationServices.get("DemographicsByBranch").getValue()))
                {
                    demoItemsByBranch = new ArrayList<>();
                    demoItemsByBranch = demographicItemDao.getBranchDemographicsItems(user.getBranch(), Constants.BRANCH);
                    for (int i = 0; i < demo.getItems().size(); i++)
                    {
                        DemographicItem item = demo.getItems().get(i);
                        BranchDemographic objAux = demoItemsByBranch.stream().filter(demoBranch -> Objects.equals(demoBranch.getDemographicItem().getId(), item.getId())).findAny().orElse(null);
                        if (objAux == null)
                        {
                            demo.getItems().remove(i);
                            i--;
                        }
                    }
                    demos.add(demo);
                } else
                {
                    demos.add(demo);
                }

                //Servicio
                //Demografico Fijo
                if (Boolean.parseBoolean(configurationServices.get("ManejoServicio").getValue()))
                {
                    demo = new Demographic();
                    demo.setId(Constants.SERVICE);
                    demo.setName("0090");
                    demo.setEncoded(true);
                    demo.setModify(true);
                    demo.setObligatory((short) 1);
                    demo.setOrigin("O");
                    if (map.get("ServiceValue") != null)
                    {
                        demo.setOrderingDemo(map.get("ServiceValue").intValue());
                    } else
                    {
                        demo.setOrderingDemo(4);
                    }
                    demo.setItems(serviceService.filterByState(true).stream().map((ServiceLaboratory t)
                            ->
                    {
                        DemographicItem d = new DemographicItem();
                        d.setId(t.getId());
                        d.setCode(t.getCode());
                        d.setName(t.getName());
                        return d;
                    }).collect(Collectors.toList()));

                    if (Boolean.parseBoolean(configurationServices.get("DemographicsByBranch").getValue()))
                    {
                        demoItemsByBranch = new ArrayList<>();
                        demoItemsByBranch = demographicItemDao.getBranchDemographicsItems(user.getBranch(), Constants.SERVICE);
                        for (int i = 0; i < demo.getItems().size(); i++)
                        {
                            DemographicItem item = demo.getItems().get(i);
                            BranchDemographic objAux = demoItemsByBranch.stream().filter(demoBranch -> Objects.equals(demoBranch.getDemographicItem().getId(), item.getId())).findAny().orElse(null);
                            if (objAux == null)
                            {
                                demo.getItems().remove(i);
                                i--;
                            }
                        }
                        demos.add(demo);
                    } else
                    {
                        demos.add(demo);
                    }
                }

                //Cliente
                //Demografico Fijo
                if (Boolean.parseBoolean(configurationServices.get("ManejoCliente").getValue()))
                {
                    demo = new Demographic();
                    demo.setId(Constants.ACCOUNT);
                    demo.setName("0085");
                    demo.setEncoded(true);
                    demo.setModify(true);
                    demo.setObligatory((short) 1);
                    demo.setOrigin("O");
                    if (map.get("accountValue") != null)
                    {
                        demo.setOrderingDemo(map.get("accountValue").intValue());
                    } else
                    {
                        demo.setOrderingDemo(5);
                    }
                    demo.setItems(accountService.list(true).stream().map((Account t)
                            ->
                    {
                        DemographicItem d = new DemographicItem();
                        d.setId(t.getId());
                        d.setCode(t.getNit());
                        d.setName(t.getName());
                        return d;
                    }).collect(Collectors.toList()));

                    if (Boolean.parseBoolean(configurationServices.get("DemographicsByBranch").getValue()))
                    {
                        demoItemsByBranch = new ArrayList<>();
                        demoItemsByBranch = demographicItemDao.getBranchDemographicsItems(user.getBranch(), Constants.ACCOUNT);
                        for (int i = 0; i < demo.getItems().size(); i++)
                        {
                            DemographicItem item = demo.getItems().get(i);
                            BranchDemographic objAux = demoItemsByBranch.stream().filter(demoBranch -> Objects.equals(demoBranch.getDemographicItem().getId(), item.getId())).findAny().orElse(null);
                            if (objAux == null)
                            {
                                demo.getItems().remove(i);
                                i--;
                            }
                        }
                        demos.add(demo);
                    } else
                    {
                        demos.add(demo);
                    }
                }

                //Tarifa
                //Demografico Fijo
                if (Boolean.parseBoolean(configurationServices.get("ManejoTarifa").getValue()))
                {
                    demo = new Demographic();
                    demo.setId(Constants.RATE);
                    demo.setName("0087");
                    demo.setEncoded(true);
                    demo.setModify(true);
                    demo.setObligatory((short) 1);
                    demo.setOrigin("O");
                    if (map.get("rateValue") != null)
                    {
                        demo.setOrderingDemo(map.get("rateValue").intValue());
                    } else
                    {
                        demo.setOrderingDemo(6);
                    }
                    demo.setItems(rateService.list(true).stream().map((Rate t)
                            ->
                    {
                        DemographicItem d = new DemographicItem();
                        d.setId(t.getId());
                        d.setCode(t.getCode());
                        d.setName(t.getName());
                        d.setDefaultItem(t.isDefaultItem());
                        return d;
                    }).collect(Collectors.toList()));

                    if (Boolean.parseBoolean(configurationServices.get("DemographicsByBranch").getValue()))
                    {
                        demoItemsByBranch = new ArrayList<>();
                        demoItemsByBranch = demographicItemDao.getBranchDemographicsItems(user.getBranch(), Constants.RATE);
                        for (int i = 0; i < demo.getItems().size(); i++)
                        {
                            DemographicItem item = demo.getItems().get(i);
                            BranchDemographic objAux = demoItemsByBranch.stream().filter(demoBranch -> Objects.equals(demoBranch.getDemographicItem().getId(), item.getId())).findAny().orElse(null);
                            if (objAux == null)
                            {
                                demo.getItems().remove(i);
                                i--;
                            }
                        }
                        demos.add(demo);
                    } else
                    {
                        demos.add(demo);
                    }
                }

                //Medico
                //Demografico Fijo
                if (Boolean.parseBoolean(configurationServices.get("ManejoMedico").getValue()))
                {
                    demo = new Demographic();
                    demo.setId(Constants.PHYSICIAN);
                    demo.setName("0086");
                    demo.setEncoded(true);
                    demo.setModify(true);
                    demo.setObligatory((short) 1);
                    demo.setOrigin("O");
                    if (map.get("physicianValue") != null)
                    {
                        demo.setOrderingDemo(map.get("physicianValue").intValue());
                    } else
                    {
                        demo.setOrderingDemo(7);
                    }
                    demo.setItems(physicianService.filterByState(true).stream().map((Physician t)
                            ->
                    {
                        DemographicItem d = new DemographicItem();
                        d.setId(t.getId());
                        d.setCode(t.getCode());
                        d.setName(t.getName() + " " + t.getLastName());
                        return d;
                    }).collect(Collectors.toList()));
                    if (Boolean.parseBoolean(configurationServices.get("DemographicsByBranch").getValue()))
                    {
                        demoItemsByBranch = new ArrayList<>();
                        demoItemsByBranch = demographicItemDao.getBranchDemographicsItems(user.getBranch(), Constants.PHYSICIAN);
                        for (int i = 0; i < demo.getItems().size(); i++)
                        {
                            DemographicItem item = demo.getItems().get(i);
                            BranchDemographic objAux = demoItemsByBranch.stream().filter(demoBranch -> Objects.equals(demoBranch.getDemographicItem().getId(), item.getId())).findAny().orElse(null);
                            if (objAux == null)
                            {
                                demo.getItems().remove(i);
                                i--;
                            }
                        }
                        demos.add(demo);
                    } else
                    {
                        demos.add(demo);
                    }
                }
                if (Boolean.parseBoolean(configurationServices.get("MedicosAuxiliares").getValue()))
                {
                    int cantPhysician = Integer.parseInt(configurationServices.get("TotalMedicosAuxiliares").getValue());
                    for (int i = 1; i <= cantPhysician; i += 1)
                    {
                        int intTest = 200 + i;
                        intTest = intTest * -1;
                        String ordering = String.valueOf(intTest);
                        String name = "";
                        if (i == 1)
                        {
                            name = "1901";
                        }
                        if (i == 2)
                        {
                            name = "1902";
                        }
                        if (i == 3)
                        {
                            name = "1903";
                        }
                        if (i == 4)
                        {
                            name = "1904";
                        }
                        if (i == 5)
                        {
                            name = "1905";
                        }

                        demo = new Demographic();
                        demo.setId(intTest);
                        demo.setName(name);
                        demo.setEncoded(true);
                        demo.setModify(true);
                        demo.setObligatory((short) 0);
                        demo.setOrigin("O");

                        if (map.get(ordering) != null)
                        {
                            demo.setOrderingDemo(map.get(ordering).intValue());
                        } else
                        {
                            demo.setOrderingDemo(7 + i);
                        }
                        demos.add(demo);
                    }

                }
            }

            List<Demographic> customDemos = origin.equals("H") ? orderDao.listOderingH() : orderDao.listOderingO();

            for (Demographic customDemo : customDemos)
            {
                if (origin.equals("H"))
                {

                    List<Demographic> filters;
                    filters = Demofixed.stream()
                            .filter(p -> p.getId() == customDemo.getId())
                            .collect(Collectors.toList());

                    if (filters.isEmpty())
                    {
                        customDemo.setOrderingDemo((int) customDemo.getOrdering() + 15);
                    } else
                    {
                        customDemo.setOrderingDemo((int) filters.get(0).getOrdering());
                    }

                } else
                {
                    List<Demographic> filters;
                    filters = Demofixed.stream()
                            .filter(p -> p.getId() == customDemo.getId())
                            .collect(Collectors.toList());

                    if (filters.isEmpty())
                    {
                        int cantPhysician = 0;
                        if (Boolean.parseBoolean(configurationServices.get("MedicosAuxiliares").getValue()))
                        {
                            cantPhysician = Integer.parseInt(configurationServices.get("TotalMedicosAuxiliares").getValue());
                        }
                        customDemo.setOrderingDemo((int) customDemo.getOrdering() + 7 + cantPhysician);
                    } else
                    {
                        customDemo.setOrderingDemo((int) filters.get(0).getOrdering());
                    }

                }

                if (Boolean.parseBoolean(configurationServices.get("DemographicsByBranch").getValue()))
                {
                    List<BranchDemographic> demosByBranch = demographicItemDao.getBranchDemographics(user.getBranch());
                    for (BranchDemographic branchDemographic : demosByBranch)
                    {
                        //Validamos que ese demografico si pertenezca a esa sede
                        if (branchDemographic.getDemographic().getId() == customDemo.getId())
                        {
                            if (customDemo.isEncoded())
                            {
                                //Es codificado y se deben obtener los items
                                customDemo.setItems(demographicItemService.get(null, null, null, customDemo.getId(), true));
                                // Se obtienen los items para esa sede:
                                // Se recorre la lista con el fin de solo enviar los items de los demograficos pertinentes para esa sede
                                List<BranchDemographic> demoDinamicItemsByBranch = demographicItemDao.getBranchDemographicsItems(user.getBranch(), customDemo.getId());
                                for (int i = 0; i < customDemo.getItems().size(); i++)
                                {
                                    DemographicItem item = customDemo.getItems().get(i);
                                    BranchDemographic objAux = demoDinamicItemsByBranch.stream().filter(demoBranch -> Objects.equals(demoBranch.getDemographicItem().getId(), item.getId())).findAny().orElse(null);
                                    if (objAux == null)
                                    {
                                        customDemo.getItems().remove(i);
                                        i--;
                                    }
                                }
                            }
                            demos.add(customDemo);
                            break;
                        }
                    }
                } else
                {
                    if (customDemo.isEncoded())
                    {
                        customDemo.setItems(demographicItemService.get(null, null, null, customDemo.getId(), true));
                    }
                    demos.add(customDemo);
                }
            }
            return demos;
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<DemographicItem> getRateByAccount(int account) throws Exception
    {
        try
        {
            AuthorizedUser user = JWT.decode(request);

            List<DemographicItem> demos = new ArrayList<>(0);

            List<RatesOfAccount> rates = accountService.getRatesByAccount(account);

            rates.stream().map(rate ->
            {
                DemographicItem d = new DemographicItem();
                d.setId(rate.getRate().getId());
                d.setCode(rate.getRate().getCode());
                d.setName(rate.getRate().getName());
                return d;
            }).forEachOrdered(d ->
            {
                demos.add(d);
            });

            if (Boolean.parseBoolean(configurationServices.get("DemographicsByBranch").getValue()))
            {
                List<BranchDemographic> demoItemsByBranch = demographicItemDao.getBranchDemographicsItems(user.getBranch(), Constants.RATE);
                for (int i = 0; i < demos.size(); i++)
                {
                    DemographicItem item = demos.get(i);
                    BranchDemographic objAux = demoItemsByBranch.stream().filter(demoBranch -> Objects.equals(demoBranch.getDemographicItem().getId(), item.getId())).findAny().orElse(null);
                    if (objAux == null)
                    {
                        demos.remove(i);
                        i--;
                    }
                }
            }
            return demos;
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public int assignOrder(Order order) throws Exception
    {
        List<String> errors = assignValidation(order);
        if (errors.isEmpty())
        {
            if (order.getOrderNumber() != 0)
            {
                int affected = orderDao.updatePatient(order);
                resultService.updateReferenceValue(order);
                statisticService.saveOrder(order.getOrderNumber());
                //Trazabilidad de la orden
                List<AuditOperation> auditList = new ArrayList<>();
                Order o = getAudit(order.getOrderNumber()).clean();
                auditList.add(new AuditOperation(o.getOrderNumber(), null, null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_ORDER, Tools.jsonObject(o), null, null, null, null));
                trackingService.registerOperationTracking(auditList);
                return affected;
            }
            return 0;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public void assignOrderVerify(Order order) throws Exception
    {
        List<String> errors = assignValidation(order);
        if (!errors.isEmpty())
        {
            throw new EnterpriseNTException(errors);
        }
    }

    /**
     * Realiza validaciones para asignar ordenes
     *
     * @param order número de orden a asignar
     * @param id id de la historía al validar
     *
     * @return Lista de errores encontrados
     * @throws Exception
     */
    private List<String> assignValidation(Order order) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (order.getOrderNumber() != 0)
        {
            Order orderFound = order.getOrderNumber() == null ? null : orderListService.findOrder(order.getOrderNumber());
            Patient patient = (order.getPatient() == null || order.getPatient().getId() == null) ? null : patientService.get(order.getPatient().getId());
            if (orderFound == null)
            {
                errors.add("3|order");
            }
            if (patient == null)
            {
                errors.add("3|patient");
            }
            if (errors.isEmpty())
            {
                orderFound.getTests().stream().filter((test) -> (!isTestAllowedForPatientAge(patient, test))).forEachOrdered((test)
                        ->
                {
                    errors.add("3|test|" + test.getId() + "|" + test.getCode() + "|" + test.getName() + "|1");
                });
                orderFound.getTests().stream().filter((test) -> (!isTestAllowedForPatientGender(patient, test))).forEachOrdered((test)
                        ->
                {
                    errors.add("3|test|" + test.getId() + "|" + test.getCode() + "|" + test.getName() + "|2");
                });
            }
        }
        return errors;
    }

    /**
     * Valida si el examen se le puede realizar al paciente (Edad)
     *
     * @param patient Información del paciente
     * @param test informacion del exámen
     *
     * @return si se le puede realizar el exámen al paciente
     */
    public boolean isTestAllowedForPatientAge(Patient patient, Test test)
    {
        if (test.getPack() != null && test.getPack().getId() != null)
        {
            return isTestAllowedForPatientAge(patient, test.getPack());
        }
        LocalDate dob = Instant.ofEpochMilli(patient.getBirthday().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        long years = DateTools.getAgeInYears(dob, LocalDate.now());
        long days = DateTools.getAgeInDays(dob, LocalDate.now());
        return test.getUnitAge() == 0 || (test.getUnitAge() == 1 && years >= test.getMinAge() && years <= test.getMaxAge()) || (test.getUnitAge() == 2 && days >= test.getMinAge() && days <= test.getMaxAge());
    }

    /**
     * Valida si el examen se le puede realizar al paciente (Genero)
     *
     * @param patient Información del paciente
     * @param test informacion del exámen
     *
     * @return si se le puede realizar el exámen al paciente
     */
    public boolean isTestAllowedForPatientGender(Patient patient, Test test)
    {
        if (test.getPack() != null && test.getPack().getId() != null)
        {
            return isTestAllowedForPatientGender(patient, test.getPack());
        }
        return test.getGender().getId().equals(ListEnum.Gender.BOTH.getValue()) || patient.getSex().equals(test.getGender());
    }

    @Override
    public List<Order> activate(List<Long> orders) throws Exception
    {
        Filter filter = new Filter();
        filter.setRangeType(2);
        filter.setOrders(orders);
        filter.setBasic(true);
        List<Order> toActivate = orderListService.orderState(filter)
                .stream()
                .filter(order -> order.getState() == LISEnum.ResultOrderState.CANCELED.getValue())
                .collect(Collectors.toList());

        List<Order> activated = orderDao.updateToPreviousState(toActivate);
        List<AuditOperation> auditList = new ArrayList<>();
        for (Order order : activated)
        {
            Order o = getAudit(order.getOrderNumber()).clean();
            auditList.add(new AuditOperation(o.getOrderNumber(), null, null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_ORDER, Tools.jsonObject(o), null, null, null, null));
            CompletableFuture.runAsync(() -> statisticService.enableOrders(order.getOrderNumber()));
        }
        //List<Long> activatedOrders = activated.stream().map(Order::getOrderNumber).collect(Collectors.toList());

        trackingService.registerOperationTracking(auditList);

        return activated;
    }

    @Transactional(transactionManager = "transactionManager", isolation = Isolation.READ_COMMITTED, rollbackFor
            =
            {
                EnterpriseNTException.class, Exception.class
            })
    @Override
    public List<Integer> saveTests(Order order, int user, Date date, int branch, int orderType, boolean check, boolean tracking) throws Exception
    {

        List<SampleState> samplesState = new ArrayList<>(0);
        List<Test> testsToInsert = new ArrayList<>(0);
        List<AuditOperation> auditList = new ArrayList<>();
        boolean checkTrazabilidad = !configurationServices.get("Trazabilidad").getValue().equals("1");
        //Elimina los examenes enviados para eliminar
        List<Test> deleteTests = order.getDeleteTests();
        if (deleteTests != null && !deleteTests.isEmpty())
        {
            List<ResultTest> deletetest = new LinkedList<>();
            StringBuilder idTest = new StringBuilder();
            List<Test> toDelete = getTestToDeleteOrder(deleteTests, order.getOrderNumber());

            // Los datos seran enviados a tableros después de la ejecución completa de este metodo
            if (toDelete != null && !toDelete.isEmpty())
            {
                deleteTests.forEach((t) ->
                {
                    ResultTest testdelete = new ResultTest();
                    testdelete.setTestId(t.getId());
                    testdelete.setTestType(t.getTestType());
                    deletetest.add(testdelete);
                    idTest.append(t.getId()).append("|");
                });

                CompletableFuture.runAsync(() ->
                {
                    try
                    {
                        for (Test test : toDelete)
                        {
                            agileStatisticService.updateTestBranch(order.getOrderNumber(), test.getId(), false, null);
                        }

                        
                    } catch (Exception ex)
                    {
                        Logger.getLogger(OrderServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }

          
            //Elimina la trazabilidad de los examenes eliminados
            for (Test test : toDelete)
            {
                Sample sample = sampleDao.getSampleByTest(test.getId());

                if (test.getTestType() == 0)
                {
                    int testSample = sampleTrackingDao.getTestsForSample(sample.getId(), order.getOrderNumber());
                    List<Test> toDeleteSample = toDelete.stream().filter(t -> t.getSample().getId() == sample.getId()).collect(Collectors.toList());

                    if (checkTrazabilidad)
                    {
                        if (sample != null && testSample == toDeleteSample.size())
                        {
                            sampleTrackingDao.deleteSampleStateTracking(order.getOrderNumber(), sample.getId());
                        }
                    }

                    microbiologyDao.deleteResultTemplate(order.getOrderNumber(), test.getId());

                    boolean deletecomment = resultTestDao.deleteResultComment(order.getOrderNumber(), test.getId());
                    if (deletecomment)
                    {
                        auditList.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_RESULTCOMMENT, "", null, null, null, null));
                    }
                }
            }

            resultsDao.deleteTestToOrder(order.getOrderNumber(), toDelete);
            List<Test> nTest = new ArrayList<>();

            if (deleteTests.size() > 0)
            {
                dashBoardService.dashBoardOpportunityTime(order.getOrderNumber(), toDelete.stream().map(testToDelete -> testToDelete.getId()).collect(Collectors.toList()), DashBoardOpportunityTime.ACTION_DELETE);
                integrationMiddlewareService.sendOrderASTM(order.getOrderNumber(), nTest, null, Constants.ANY, null, null, toDelete, branch, false);

                //Registro de auditoria  
                for (Test test : toDelete)
                {
                    auditList.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_TEST, "", null, null, null, null));

                    Sample sample = sampleDao.getSampleByTest(test.getId());
                    if (sample != null)
                    {
                        int testSample = sampleTrackingDao.getTestsForSample(sample.getId(), order.getOrderNumber());
                        if (testSample == 0)
                        {
                            auditList.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_SAMPLE, "", null, null, null, null));
                        }
                    }
                }
            }
            //trackingService.registerOperationTracking(auditList);

        }
        testsToInsert = groupingTestByType(order, branch, orderType);
        //Valida examenes sin laboratorio
        List<String> missingLaboratory = testsToInsert.stream().filter(t -> t.getLaboratory() == null)
                .map(t -> "6|Test id {" + t.getId() + "} not laboratory assigned|" + t.getId())
                .collect(Collectors.toList());
        if (!missingLaboratory.isEmpty())
        {
            throw new EnterpriseNTException(missingLaboratory);
        }
        resultsDao.saveResult(order.getOrderNumber(), testsToInsert, date, user);
        OrderCreationLog.info("eguardo resultado");
        for (Test test : testsToInsert)
        {
            resultService.repeatPatientHistory(order.getOrderNumber(), test.getId(), user, LISEnum.ResultTestState.ORDERED.getValue(), null, null);

        }
        OrderCreationLog.info("eguardo historico");

        agileStatisticService.addOrderTests(order.getOrderNumber(), testsToInsert);
        OrderCreationLog.info("eguardo estadisticas");
        //Actualiza los valores de referencia
        if (order.getPatient().getId() != 0 && order.getPatient().getId() != null)
        {
            resultService.updateReferenceValue(order);
            OrderCreationLog.info("actualizo valores de referencia");
        }
        //Obtiene los ids de la muestra que ya estan registrados para esa orden
        List<Integer> samplesExists = sampleTrackingDao.getNotExistsSampleTracking(order.getOrderNumber());
        OrderCreationLog.info("conlto muestras" + samplesExists);
        //Actualiza la tabla de muestras
        if (!samplesState.isEmpty())
        {
            //Filtra la lista de muestras contra las que ya existen dejando unicamente las que no existen
            samplesState = samplesState.stream().filter(t -> !samplesExists.contains(t.getSample().getId())).collect(Collectors.toList());
            //Inserta el estado de la muestra de las que no existen
            sampleTrackingDao.insertSampleTracking(samplesState);
        } else
        {
            if (!checkTrazabilidad)
            {
                OrderCreationLog.info("trabilidad" + testsToInsert);
                List<Integer> testsToInsertCheckIn = testsToInsert.stream().filter(sample -> sample.getSample() != null && sample.getSample().getId() != null && sample.getSample().getId() > 0)
                        .map(sample -> sample.getSample().getId()).distinct().collect(Collectors.toList());

                OrderCreationLog.info("trabilidad1" + testsToInsertCheckIn);

                testsToInsertCheckIn = testsToInsertCheckIn.stream().filter(t -> !samplesExists.contains(t)).collect(Collectors.toList());
                List<SampleState> listSampleState = new ArrayList<>();

                for (Integer sampleID : testsToInsertCheckIn)
                {
                    SampleState sampleState = new SampleState();
                    sampleState.setBranch(new Branch(branch));
                    sampleState.setDate(date);
                    sampleState.setDestination(null);
                    sampleState.setOrder(order.getOrderNumber());
                    sampleState.setSample(new Sample(sampleID));
                    sampleState.setState(LISEnum.ResultSampleState.CHECKED.getValue());
                    sampleState.setUser(new AuthorizedUser(user));
                    listSampleState.add(sampleState);

                }
                if (!listSampleState.isEmpty())
                {
                    sampleTrackingDao.insertSampleTracking(listSampleState);
                }

            }
        }
        //Auditoria general

        AuthorizedUser userAu = null;
        //Si no se genera token continua-> para interfaces ingreso
        boolean tokenExists = true;
        try
        {
            userAu = JWT.decode(request);
        } catch (Exception e)
        {
            tokenExists = false;
            userAu.setId(user);
            userAu.setBranch(branch);
        }

        if (tokenExists)
        {
            trackingService.registerOperationTracking(auditList);
        }

        List<Integer> testinteger = testsToInsert.stream().map(test -> test.getId()).collect(Collectors.toList());
        //Auditoria del examen
        if (testsToInsert != null && !testsToInsert.isEmpty())
        {
            
            boolean checkTest = !configurationServices.get("Trazabilidad").getValue().equals("1");
            if (!checkTest)
            {
                sampleTrackingService.sampleOrdered(order.getOrderNumber(), userAu, LISEnum.ResultSampleState.CHECKED.getValue(), testinteger);
            } else
            {
                sampleTrackingService.sampleOrdered(order.getOrderNumber(), userAu, !check ? LISEnum.ResultSampleState.CHECKED.getValue() : LISEnum.ResultSampleState.ORDERED.getValue(), LISEnum.OriginModule.ORIGIN.getValue(), testinteger);
            }
        
            
            List<AuditOperation> auditListAdd = new ArrayList<>();
            for (Test test : testsToInsert)
            {
                auditListAdd.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_TEST, LISEnum.ResultTestState.ORDERED.getValue() + "", null, null, null, null));
                auditListAdd.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLETEST, LISEnum.ResultSampleState.ORDERED.getValue() + "", null, null, null, null));
            }
            if (tokenExists)
            {
                trackingService.registerOperationTracking(auditListAdd);
            }

            integrationMiddlewareService.sendOrderASTM(order.getOrderNumber(), testsToInsert, null, Constants.ANY, null, null, null, order.getBranch().getId(), false);

        }

        OrderCreationLog.info("terrmina metodo de guardado de examenes" + testsToInsert);
        return testinteger;
    }

    /**
     * Metodo que nos permite agrupar los examanes ingresados para la orden por
     * si hay paquetes, perfiles y examenes en comun
     *
     * @param order
     * @param branch
     * @param orderType
     * @return
     * @throws Exception
     */
    public List<Test> groupingTestByType(Order order, int branch, int orderType) throws Exception
    {
        OrderCreationLog.info("agrupacion de examenes");
        List<Test> testsToInsert = new ArrayList<>();
        List<Test> tests = order.getTests();
        //Se insertan paquetes
        List<Test> packages = tests.stream().filter((Test t) -> t.getTestType() == (short) 2).collect(Collectors.toList());
        Test childToInsert = null;
        if (!packages.isEmpty())
        {
            List<net.cltech.enterprisent.domain.masters.test.Test> childs = null;
            List<net.cltech.enterprisent.domain.masters.test.Test> childsPanel = null;
            net.cltech.enterprisent.domain.masters.test.Test child = null;
            //Recorre los paquetes
            for (Test aPackage : packages)
            {
                //Por cada paquete lo busca en base de datos para obtener la informacion
                child = testService.get(aPackage.getId(), null, null, null);
                if (child != null)
                {
                    if (!testsToInsert.contains(new Test(aPackage.getId())))
                    {
                        //Inserta el paquete en la tabla
                        childToInsert = new Test();
                        childToInsert.setId(child.getId());
                        childToInsert.setTestState((child.getAutomaticResult() == null || child.getAutomaticResult().trim().isEmpty()) ? LISEnum.ResultTestState.ORDERED.getValue() : LISEnum.ResultTestState.REPORTED.getValue());
                        childToInsert.setPrint(child.getPrintOnReport());
                        childToInsert.setTestType((short) 2);
                        childToInsert.setAutomaticResult(child.getAutomaticResult());
                        childToInsert.setFixedComment(child.getFixedComment());
                        childToInsert.setPrintComment(child.getPrintComment());
                        childToInsert.setCommentResult(child.getCommentResult());

                        childToInsert.setCodeCups(aPackage.getCodeCups());
                        testsToInsert.add(saveTest(childToInsert, branch, orderType));

                        //Obtiene y recorre los hijos del paquete
                        childs = testService.getChilds(child.getId());
                        childs = childs.stream().filter(t -> t.isState()).collect(Collectors.toList());
                        for (net.cltech.enterprisent.domain.masters.test.Test childPackage : childs)
                        {
                            if (!testsToInsert.contains(new Test(childPackage.getId())))
                            {
                                if (childPackage.getTestType() == (short) 1)
                                {
                                    //Son perfiles del paquete
                                    //Inserta el perfil en la tabla
                                    childToInsert = new Test();
                                    childToInsert.setId(childPackage.getId());
                                    childToInsert.setPack(new Test(aPackage.getId()));
                                    childToInsert.setTestState((child.getAutomaticResult() == null || child.getAutomaticResult().trim().isEmpty()) ? LISEnum.ResultTestState.ORDERED.getValue() : LISEnum.ResultTestState.REPORTED.getValue());
                                    childToInsert.setPrint(child.getPrintOnReport());
                                    childToInsert.setTestType((short) 1);
                                    childToInsert.setAutomaticResult(child.getAutomaticResult());
                                    childToInsert.setFixedComment(child.getFixedComment());
                                    childToInsert.setPrintComment(child.getPrintComment());
                                    childToInsert.setCommentResult(child.getCommentResult());
                                    childToInsert.setCodeCups(aPackage.getCodeCups());
                                    testsToInsert.add(saveTest(childToInsert, branch, orderType));
                                    //Consulta los hijos del perfil los recorre y los inserta
                                    childsPanel = testService.getChilds(childPackage.getId());
                                    childsPanel = childsPanel.stream().filter(t -> t.isState()).collect(Collectors.toList());
                                    for (net.cltech.enterprisent.domain.masters.test.Test test : childsPanel)
                                    {
                                        if (!testsToInsert.contains(new Test(test.getId())))
                                        {
                                            childToInsert = new Test();
                                            childToInsert.setId(test.getId());
                                            childToInsert.setTestState((test.getAutomaticResult() == null || test.getAutomaticResult().trim().isEmpty()) ? LISEnum.ResultTestState.ORDERED.getValue() : LISEnum.ResultTestState.REPORTED.getValue());
                                            childToInsert.setSampleState(LISEnum.ResultSampleState.ORDERED.getValue());
                                            childToInsert.setPack(new Test(aPackage.getId()));
                                            childToInsert.setPanel(new Test(childPackage.getId()));
                                            childToInsert.setUnit(test.getUnit());
                                            childToInsert.setTechnique(test.getTechnique());
                                            childToInsert.setPrint(test.getPrintOnReport());
                                            childToInsert.setHistoricGraphic(test.isPrintGraph());
                                            childToInsert.setSample(test.getSample());
                                            childToInsert.setTestType((short) 1);
                                            childToInsert.setAutomaticResult(test.getAutomaticResult());
                                            childToInsert.setFixedComment(test.getFixedComment());
                                            childToInsert.setPrintComment(test.getPrintComment());
                                            childToInsert.setCommentResult(test.getCommentResult());
                                            childToInsert.setCodeCups(aPackage.getCodeCups());
                                            testsToInsert.add(saveTest(childToInsert, branch, orderType));
                                        }
                                    }
                                } else
                                {
                                    if (!testsToInsert.contains(new Test(childPackage.getId())))
                                    {
                                        //Son examenes del paquete
                                        childToInsert = new Test();
                                        childToInsert.setId(childPackage.getId());
                                        childToInsert.setTestState((childPackage.getAutomaticResult() == null || childPackage.getAutomaticResult().trim().isEmpty()) ? LISEnum.ResultTestState.ORDERED.getValue() : LISEnum.ResultTestState.REPORTED.getValue());
                                        childToInsert.setSampleState(LISEnum.ResultSampleState.ORDERED.getValue());
                                        childToInsert.setPack(new Test(aPackage.getId()));
                                        childToInsert.setUnit(childPackage.getUnit());
                                        childToInsert.setTechnique(childPackage.getTechnique());
                                        childToInsert.setPrint(childPackage.getPrintOnReport());
                                        childToInsert.setHistoricGraphic(childPackage.isPrintGraph());
                                        childToInsert.setSample(childPackage.getSample());
                                        childToInsert.setTestType((short) 0);
                                        childToInsert.setAutomaticResult(childPackage.getAutomaticResult());
                                        childToInsert.setFixedComment(childPackage.getFixedComment());
                                        childToInsert.setPrintComment(childPackage.getPrintComment());
                                        childToInsert.setCommentResult(childPackage.getCommentResult());
                                        childToInsert.setCodeCups(aPackage.getCodeCups());
                                        HistoricalResult historicalResult = resultsDao.getBasic(order.getPatient().getId(), childPackage.getId());
                                        if (historicalResult != null)
                                        {
                                            Result lastResult = new Result();
                                            lastResult.setResult(historicalResult.getSecondLastResultTemp() == null ? historicalResult.getLastResult() : historicalResult.getSecondLastResultTemp());
                                            lastResult.setDateResult(historicalResult.getSecondLastResultTemp() == null ? historicalResult.getLastResultDate() : historicalResult.getSecondLastResultDateTemp());
                                            childToInsert.setLastResult(lastResult);

                                            Result secondLastResult = new Result();
                                            secondLastResult.setResult(historicalResult.getLastResult());
                                            secondLastResult.setDateResult(historicalResult.getLastResultDate());
                                            childToInsert.setSecondLastResult(secondLastResult);
                                        }
                                        testsToInsert.add(saveTest(childToInsert, branch, orderType));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //Se insertan perfiles
        List<Test> panels = tests.stream().filter((Test t) -> t.getTestType() == (short) 1).collect(Collectors.toList());
        if (!panels.isEmpty())
        {
            List<net.cltech.enterprisent.domain.masters.test.Test> childsPanel = null;
            net.cltech.enterprisent.domain.masters.test.Test panelDB = null;
            for (Test panel : panels)
            {
                panelDB = testService.get(panel.getId(), null, null, null);
                if (panelDB != null)
                {
                    //Inserta el perfil en la tabla
                    childToInsert = new Test();
                    childToInsert.setId(panelDB.getId());
                    if (!testsToInsert.contains(new Test(panelDB.getId())))
                    {
                        //Inserta el perfil en la tabla
                        childToInsert.setTestState((panelDB.getAutomaticResult() == null || panelDB.getAutomaticResult().trim().isEmpty()) ? LISEnum.ResultTestState.ORDERED.getValue() : LISEnum.ResultTestState.REPORTED.getValue());
                        childToInsert.setPrint(panelDB.getPrintOnReport());
                        childToInsert.setTestType((short) 1);
                        childToInsert.setAutomaticResult(panelDB.getAutomaticResult());
                        childToInsert.setFixedComment(panelDB.getFixedComment());
                        childToInsert.setPrintComment(panelDB.getPrintComment());
                        childToInsert.setCommentResult(panelDB.getCommentResult());
                        childToInsert.setCodeCups(panel.getCodeCups());
                        if (panelDB.isDependentExam())
                        {
                            childToInsert.setSample(panelDB.getSample());
                        }

                        testsToInsert.add(saveTest(childToInsert, branch, orderType));
                        //Consulta los hijos del perfil los recorre y los inserta
                        childsPanel = testService.getChilds(panel.getId());
                        childsPanel = childsPanel.stream().filter(t -> t.isState()).collect(Collectors.toList());
                        for (net.cltech.enterprisent.domain.masters.test.Test test : childsPanel)
                        {
                            if (!testsToInsert.contains(new Test(test.getId())))
                            {
                                childToInsert = new Test();
                                childToInsert.setId(test.getId());
                                childToInsert.setTestState((test.getAutomaticResult() == null || test.getAutomaticResult().trim().isEmpty()) ? LISEnum.ResultTestState.ORDERED.getValue() : LISEnum.ResultTestState.REPORTED.getValue());
                                childToInsert.setSampleState(LISEnum.ResultSampleState.ORDERED.getValue());
                                childToInsert.setPanel(new Test(panel.getId()));
                                childToInsert.setUnit(test.getUnit());
                                childToInsert.setTechnique(test.getTechnique());
                                childToInsert.setPrint(test.getPrintOnReport());
                                childToInsert.setHistoricGraphic(test.isPrintGraph());
                                childToInsert.setSample(test.getSample());
                                childToInsert.setTestType((short) 0);
                                childToInsert.setAutomaticResult(test.getAutomaticResult());
                                childToInsert.setFixedComment(test.getFixedComment());
                                childToInsert.setPrintComment(test.getPrintComment());
                                childToInsert.setCommentResult(test.getCommentResult());
                                childToInsert.setCodeCups(panel.getCodeCups());
                                HistoricalResult historicalResult = resultsDao.getBasic(order.getPatient().getId(), test.getId());

                                if (historicalResult != null)
                                {
                                    Result lastResult = new Result();
                                    lastResult.setResult(historicalResult.getSecondLastResultTemp() == null ? historicalResult.getLastResult() : historicalResult.getSecondLastResultTemp());
                                    lastResult.setDateResult(historicalResult.getSecondLastResultTemp() == null ? historicalResult.getLastResultDate() : historicalResult.getSecondLastResultDateTemp());
                                    childToInsert.setLastResult(lastResult);

                                    Result secondLastResult = new Result();
                                    secondLastResult.setResult(historicalResult.getLastResult());
                                    secondLastResult.setDateResult(historicalResult.getLastResultDate());
                                    childToInsert.setSecondLastResult(secondLastResult);
                                }
                                testsToInsert.add(saveTest(childToInsert, branch, orderType));
                            }
                        }
                    }
                }
            }
        }
        //Se insertan examenes
        List<Test> analites = tests.stream().filter((Test t) -> t.getTestType() == (short) 0).collect(Collectors.toList());
        if (!analites.isEmpty())
        {
            net.cltech.enterprisent.domain.masters.test.Test analiteDB = null;
            //Recorre los examenes
            for (Test analite : analites)
            {
                //Consulta los datos por id
                analiteDB = testService.get(analite.getId(), null, null, null);

                if (analiteDB != null)
                {
                    if (!testsToInsert.contains(new Test(analite.getId())))
                    {
                        childToInsert = new Test();
                        childToInsert.setId(analite.getId());
                        childToInsert.setTestState((analiteDB.getAutomaticResult() == null || analiteDB.getAutomaticResult().trim().isEmpty()) ? LISEnum.ResultTestState.ORDERED.getValue() : LISEnum.ResultTestState.REPORTED.getValue());
                        childToInsert.setSampleState(LISEnum.ResultSampleState.ORDERED.getValue());
                        childToInsert.setUnit(analiteDB.getUnit());
                        childToInsert.setTechnique(analiteDB.getTechnique());
                        childToInsert.setPrint(analiteDB.getPrintOnReport());
                        childToInsert.setHistoricGraphic(analiteDB.isPrintGraph());
                        childToInsert.setSample(analiteDB.getSample());
                        childToInsert.setTestType((short) 0);
                        childToInsert.setAutomaticResult(analiteDB.getAutomaticResult());
                        childToInsert.setFixedComment(analiteDB.getFixedComment());
                        childToInsert.setPrintComment(analiteDB.getPrintComment());
                        childToInsert.setCommentResult(analiteDB.getCommentResult());
                        childToInsert.setCodeCups(analite.getCodeCups());
                        HistoricalResult historicalResult = resultsDao.getBasic(order.getPatient().getId(), analite.getId());
                        if (historicalResult != null)
                        {
                            Result lastResult = new Result();
                            lastResult.setResult(historicalResult.getSecondLastResultTemp() == null ? historicalResult.getLastResult() : historicalResult.getSecondLastResultTemp());
                            lastResult.setDateResult(historicalResult.getSecondLastResultTemp() == null ? historicalResult.getLastResultDate() : historicalResult.getSecondLastResultDateTemp());
                            childToInsert.setLastResult(lastResult);

                            Result secondLastResult = new Result();
                            secondLastResult.setResult(historicalResult.getLastResult());
                            secondLastResult.setDateResult(historicalResult.getLastResultDate());
                            childToInsert.setSecondLastResult(secondLastResult);
                        }

                        testsToInsert.add(saveTest(childToInsert, branch, orderType));

                        List<net.cltech.enterprisent.domain.masters.test.Test> childsPanel = null;
                        //Consulta las concurrencias
                        childsPanel = testService.getChilds(analiteDB.getId());
                        for (net.cltech.enterprisent.domain.masters.test.Test test : childsPanel)
                        {
                            if (!testsToInsert.contains(new Test(test.getId())))
                            {
                                childToInsert = new Test();
                                childToInsert.setId(test.getId());
                                childToInsert.setTestState((test.getAutomaticResult() == null || test.getAutomaticResult().trim().isEmpty()) ? LISEnum.ResultTestState.ORDERED.getValue() : LISEnum.ResultTestState.REPORTED.getValue());
                                childToInsert.setSampleState(LISEnum.ResultSampleState.ORDERED.getValue());
                                childToInsert.setUnit(test.getUnit());
                                childToInsert.setTechnique(test.getTechnique());
                                childToInsert.setPrint(test.getPrintOnReport());
                                childToInsert.setHistoricGraphic(test.isPrintGraph());
                                childToInsert.setSample(test.getSample());
                                childToInsert.setTestType((short) 0);
                                childToInsert.setAutomaticResult(test.getAutomaticResult());
                                childToInsert.setFixedComment(test.getFixedComment());
                                childToInsert.setPrintComment(test.getPrintComment());
                                childToInsert.setCommentResult(test.getCommentResult());
                                childToInsert.setCodeCups(analite.getCodeCups());
                                HistoricalResult historicalResultChild = resultsDao.getBasic(order.getPatient().getId(), test.getId());
                                if (historicalResultChild != null)
                                {
                                    Result lastResult = new Result();
                                    lastResult.setResult(historicalResultChild.getSecondLastResultTemp() == null ? historicalResultChild.getLastResult() : historicalResultChild.getSecondLastResultTemp());
                                    lastResult.setDateResult(historicalResultChild.getSecondLastResultTemp() == null ? historicalResultChild.getLastResultDate() : historicalResultChild.getSecondLastResultDateTemp());
                                    childToInsert.setLastResult(lastResult);

                                    Result secondLastResult = new Result();
                                    secondLastResult.setResult(historicalResultChild.getLastResult());
                                    secondLastResult.setDateResult(historicalResultChild.getLastResultDate());
                                    childToInsert.setSecondLastResult(secondLastResult);
                                }
                                testsToInsert.add(saveTest(childToInsert, branch, orderType));
                            }
                        }
                    }
                }
            }
        }
        //Inserta los examenes
        final List<Integer> testsId = resultsDao.getTestsIds(order.getOrderNumber());
        OrderCreationLog.info("lista de examen" + testsId);
        return testsToInsert.stream().filter(t -> !testsId.contains(t.getId())).collect(Collectors.toList());
    }

    @Override
    public Test saveTest(Test test, int branch, int orderType) throws Exception
    {
        //Consulta el laboratorio del examen, perfil o paquete
        Laboratory laboratory = null;
        if (test.getTestType() == 2)
        {
            //Si es paquete obtiene el laboratorio del primer examen
            List<net.cltech.enterprisent.domain.masters.test.Test> childs = testService.getChilds(test.getId());
            for (net.cltech.enterprisent.domain.masters.test.Test child : childs)
            {
                laboratory = testService.getLaboratory(branch, child.getId(), orderType);
                if (laboratory != null)
                {
                    break;
                }
            }
        } else
        {
            laboratory = testService.getLaboratory(branch, test.getId(), orderType);
        }

        test.setLaboratory(laboratory);
        return test;
    }

    @Override
    public List<String> createBatch(Order order, Long init, int quantity, int type, int user, int branch) throws Exception
    {
        List<String> orders = new ArrayList<>();
        int digitsOrder = Integer.valueOf(configurationServices.get("DigitosOrden").getValue());

        int i = 0;
        int j = 0;
        switch (type)
        {
            case 1:
                Long end = Long.valueOf(init.toString().substring(0, 8) + Tools.repeatChar("9", digitsOrder));
                int quantityOrderCreated = orderDao.getQuantityOrderCreated(init, end);
                if (quantity <= (((Integer.valueOf(Tools.repeatChar("9", digitsOrder)) - Integer.valueOf(init.toString().substring(8))) - quantityOrderCreated) + 1))
                {
                    while (i < quantity)
                    {
                        Order orderInsert = order;
                        order.setOrderNumber(init + j);
                        try
                        {
                            orderInsert = create(orderInsert, user, branch);

                            orders.add(orderInsert.getOrderNumber() + "|Successfully Registered");
                            i++;
                            j++;
                        } catch (DuplicateKeyException ex)
                        {
                            orders.add(order.getOrderNumber() + "|Duplicate Key");
                            j++;
                        } catch (EnterpriseNTException ex)
                        {
                            throw ex;
                        } catch (Exception ex)
                        {
                            throw ex;
                        }
                    }
                } else
                {
                    throw new EnterpriseNTException(Arrays.asList("0|invalid range"));
                }
                break;
            case 2:
                if (quantity <= (Integer.valueOf(Tools.repeatChar("9", digitsOrder)) - toolsDao.nextVal(getNameSequence(order))))
                {
                    for (i = 0; i < quantity; i++)
                    {
                        try
                        {
                            order.setOrderNumber(null);
                            Order orderInsert = create(order, user, branch);

                            orders.add(orderInsert.getOrderNumber() + "|Successfully Registered");
                        } catch (Exception ex)
                        {
                            System.out.println("" + ex);
                            orders.add(i + "|Not Register");
                        }
                    }
                } else
                {
                    throw new EnterpriseNTException(Arrays.asList("0|invalid range"));
                }
                break;
            default:
                List<String> errors = new ArrayList<>(0);
                errors.add("3|type");
                throw new EnterpriseNTException(errors);
        }

        return orders;
    }

    @Override
    public TestPrice getPriceTest(int test, int rate) throws Exception
    {
        int tax = Integer.parseInt(configurationServices.get("Impuesto").getValue());

        TestPrice auxPrice = orderDao.getPriceTest(test, rate, tax);
        TestPrice price = new TestPrice();
        if (auxPrice == null)
        {
            price.setServicePrice(new BigDecimal(0.0));
            price.setInsurancePrice(new BigDecimal(0.0));
            price.setPatientPrice(new BigDecimal(0.0));
            price.setPatientPercentage(new BigDecimal(0.0));
            price.setTax(0.0);
        } else
        {
            price = auxPrice;
        }
        return price;
    }

    @Override
    public int getRateOrder(Long order) throws Exception
    {
        return orderDao.getRateOrder(order);
    }

    @Override
    public int getTypeOrder(Long order) throws Exception
    {
        return orderDao.getTypeOrder(order);
    }

    @Override
    public List<OrderSearch> getByPatientInfo(Integer documentType, String patientId, String lastName, String surName, String name1, String name2, Integer sex, Date birthday, int branch) throws Exception
    {
        if (patientId != null && !patientId.toLowerCase().equals("undefined"))
        {
            patientId = Tools.encrypt(patientId);
        } else
        {
            patientId = null;
        }

        if (lastName != null && !lastName.toLowerCase().equals("undefined"))
        {
            lastName = Tools.encrypt(lastName);
        } else
        {
            lastName = null;
        }

        if (surName != null && !surName.toLowerCase().equals("undefined"))
        {
            surName = Tools.encrypt(surName);
        } else
        {
            surName = null;
        }

        if (name1 != null && !name1.toLowerCase().equals("undefined"))
        {
            name1 = Tools.encrypt(name1);
        } else
        {
            name1 = null;
        }

        if (name2 != null && !name2.toLowerCase().equals("undefined"))
        {
            name2 = Tools.encrypt(name2);
        } else
        {
            name2 = null;
        }
        int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
        return orderDao.getByPatientInfo(documentType, patientId, lastName, surName, name1, name2, sex, birthday, branch, yearsQuery);
    }

    @Override
    public OrderSearch getByOrder(long order, int branch) throws Exception
    {
        return orderDao.getByOrder(order, branch);
    }

    @Override
    public List<OrderSearch> getByEntryDate(int date, int branch) throws Exception
    {
        return orderDao.getByEntryDate(date, branch);
    }

    @Override
    public OrderNumSearch getByEntryDateN(int date, int branch) throws Exception
    {
        return orderDao.getByEntryDateN(date, branch);
    }

    @Override
    public List<OrderSearch> ordersByEntryDate(int date) throws Exception
    {
        return orderDao.ordersByEntryDate(date);
    }

    @Override
    public List<TicketTest> getTicketTest(long order) throws Exception
    {
        return orderDao.getTicketTest(order);
    }

    @Override
    public OrderTestDetail getOrderTestDetail(int patientId, int testId, int testType, int rateId) throws Exception
    {
        //Inicializa el objeto de respuesta
        OrderTestDetail detail = new OrderTestDetail();
        detail.setId(testId);
        //Consultar las muestras y recipientes del examen
        List<Sample> samples = new ArrayList<>(0);
        List<net.cltech.enterprisent.domain.masters.test.Test> childs = null;
        List<Integer> idsToValidity = new ArrayList<>(0);
        switch (testType)
        {
            case 0:
                //Examen
                net.cltech.enterprisent.domain.masters.test.Test test = testService.get(testId, null, null, null);
                if (test.getSample() != null)
                {
                    samples.add(test.getSample());
                }
                if (test.getValidResult() != null && test.getValidResult() != 0)
                {
                    //Revisa los dias de validacion del resultado
                    HistoricalResult historicalResult = resultsDao.get(patientId, testId);
                    if (historicalResult != null && historicalResult.getLastResultDate() != null)
                    {
                        //Si tiene historicos calcula los dias desde el ultimo resultado
                        Date lastResultDate = historicalResult.getLastResultDate();
                        Date currentDate = new Date();
                        long days = DateTools.getElapsedDays(lastResultDate, currentDate);
                        if (days <= test.getValidResult())
                        {
                            //Si los dias del ultimo resultado son menores a los configurados envia la validacion para ese examen
                            TestValidity validity = new TestValidity();
                            validity.setId(testId);
                            validity.setDaysFromLastResult(days);
                            validity.setDateLastResult(historicalResult.getLastResultDate());
                            validity.setUserLastResult(historicalResult.getLastResultUser());
                            detail.setResultValidity(validity);
                        }
                    }
                }
                break;
            case 1:
                //Perfil
                childs = testService.getChilds(testId);
                for (net.cltech.enterprisent.domain.masters.test.Test child : childs)
                {
                    if (child.getValidResult() != null && child.getValidResult() != 0)
                    {
                        idsToValidity.add(child.getId());
                    }
                    if (child.getSample() != null)
                    {
                        if (!samples.contains(child.getSample()))
                        {
                            samples.add(child.getSample());
                        }
                    }
                }

                if (!idsToValidity.isEmpty())
                {
                    //Si tiene hijos configurados para validacion de dias de resultado
                    List<HistoricalResult> results = resultsDao.get(patientId, idsToValidity);
                    if (!results.isEmpty())
                    {
                        results = results.stream().filter(el -> el.getLastResultDate() != null && !el.getLastResultDate().toString().trim().isEmpty()).collect(Collectors.toList());
                        //Si tiene historicos alguno de los examenes del perfil
                        //Busca el que tiene fecha de validacion maxima
                        if (results.size() > 0)
                        {
                            HistoricalResult result = results
                                    .stream()
                                    .max((HistoricalResult o1, HistoricalResult o2) -> o1.getLastResultDate().compareTo(o2.getLastResultDate()))
                                    .get();
                            net.cltech.enterprisent.domain.masters.test.Test testResult = childs
                                    .stream()
                                    .filter(t -> t.getId() == result.getTestId())
                                    .findFirst()
                                    .get();
                            Date lastResultDate = result.getLastResultDate();
                            Date currentDate = new Date();
                            long days = DateTools.getElapsedDays(lastResultDate, currentDate);

                            if (days <= testResult.getValidResult())
                            {
                                //Si los dias del ultimo resultado son menores a los configurados envia la validacion para ese examen
                                TestValidity validity = new TestValidity();
                                validity.setId(result.getTestId());

                                validity.setDaysFromLastResult(days);
                                validity.setDateLastResult(result.getLastResultDate());
                                validity.setUserLastResult(result.getLastResultUser());
                                detail.setResultValidity(validity);
                            }
                        }
                    }
                }
                break;
            case 2:
                //Paquete
                childs = testService.getChilds(testId);
                List<net.cltech.enterprisent.domain.masters.test.Test> subChilds = null;
                for (net.cltech.enterprisent.domain.masters.test.Test child : childs)
                {
                    if (child.getTestType() == 1)
                    {
                        //Si es un perfil
                        subChilds = testService.getChilds(child.getId());
                        for (net.cltech.enterprisent.domain.masters.test.Test subChild : subChilds)
                        {
                            if (subChild.getValidResult() != null && subChild.getValidResult() != 0)
                            {
                                idsToValidity.add(subChild.getId());
                            }
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
                        //Si es un examen
                        if (child.getValidResult() != null && child.getValidResult() != 0)
                        {
                            idsToValidity.add(child.getId());
                        }
                        if (child.getSample() != null)
                        {
                            if (!samples.contains(child.getSample()))
                            {
                                samples.add(child.getSample());
                            }
                        }
                    }
                }
                if (!idsToValidity.isEmpty())
                {
                    //Si tiene hijos configurados para validacion de dias de resultado
                    List<HistoricalResult> results = resultsDao.get(patientId, idsToValidity);
                    if (!results.isEmpty())
                    {
                        //Si tiene historicos alguno de los examenes del perfil
                        //Busca el que tiene fecha de validacion maxima
                        results = results.stream().filter(el -> el.getLastResultDate() != null && !el.getLastResultDate().toString().trim().isEmpty()).collect(Collectors.toList());
                        if (results.size() > 0)
                        {
                            HistoricalResult result = results
                                    .stream()
                                    .max((HistoricalResult o1, HistoricalResult o2) -> o1.getLastResultDate().compareTo(o2.getLastResultDate()))
                                    .get();

                            net.cltech.enterprisent.domain.masters.test.Test testResult = childs
                                    .stream()
                                    .filter(t -> t.getId() == result.getTestId())
                                    .findFirst().orElse(null);

                            if (testResult != null)
                            {
                                Date lastResultDate = result.getLastResultDate();
                                Date currentDate = new Date();
                                long days = DateTools.getElapsedDays(lastResultDate, currentDate);
                                if (days <= testResult.getValidResult())
                                {
                                    //Si los dias del ultimo resultado son menores a los configurados envia la validacion para ese examen
                                    TestValidity validity = new TestValidity();
                                    validity.setId(result.getTestId());
                                    validity.setDaysFromLastResult(days);
                                    validity.setDateLastResult(result.getLastResultDate());
                                    validity.setUserLastResult(result.getLastResultUser());
                                    detail.setResultValidity(validity);
                                }
                            }
                        }
                    }
                }
                break;

        }
        //Establece las muestras encontradas
        detail.setSamples(samples);
        //Consultar el precio del examen
        if (rateId != -1)
        {
            TestPrice price = getPriceTest(testId, rateId);
            detail.setPrice(price.getServicePrice());
            detail.setPatientPrice(price.getPatientPrice());
            detail.setInsurancePrice(price.getInsurancePrice());
        }
        return detail.getSamples().isEmpty() ? null : detail;
    }

    /**
     * Obtiene todos los examenes que seran eliminados de la tabla
     *
     * @param tests Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Test}
     * @return Lista de {net.cltech.enterprisent.domain.operation.orders.Test}
     * con los ids de los examenes eliminados
     * @throws Exception Error en base de datos
     */
    @Override
    public List<Test> getTestToDelete(List<Test> tests) throws Exception
    {
        List<Test> packages = tests.stream().filter((Test t) -> t.getTestType() == (short) 2).collect(Collectors.toList());
        Test childToDelete = null;
        List<Test> testToDelete = new ArrayList<>(0);
        if (!packages.isEmpty())
        {
            List<net.cltech.enterprisent.domain.masters.test.Test> childs = null;
            List<net.cltech.enterprisent.domain.masters.test.Test> childsPanel = null;
            net.cltech.enterprisent.domain.masters.test.Test child = null;
            //Recorre los paquetes
            for (Test aPackage : packages)
            {
                //Por cada paquete lo busca en base de datos para obtener la informacion
                child = testService.get(aPackage.getId(), null, null, null);
                if (child != null)
                {
                    //Agrega el paquete para eliminar
                    childToDelete = new Test();
                    childToDelete.setId(child.getId());
                    childToDelete.setCode(child.getCode());
                    childToDelete.setName(child.getName());
                    childToDelete.setArea(child.getArea());
                    childToDelete.setTestType(child.getTestType());
                    childToDelete.setLaboratory(aPackage.getLaboratory());
                    childToDelete.getSample().setId(child.getSample().getId());
                    childToDelete.getSample().setLaboratorytype(child.getSample().getLaboratorytype());
                    testToDelete.add(childToDelete);
                    //Obtiene y recorre los hijos del paquete
                    childs = testService.getChilds(child.getId());
                    for (net.cltech.enterprisent.domain.masters.test.Test childPackage : childs)
                    {
                        if (childPackage.getTestType() == (short) 1)
                        {
                            //Son perfiles del paquete
                            //Agrega el perfil a la lista
                            childToDelete = new Test();
                            childToDelete.setId(childPackage.getId());
                            childToDelete.setCode(childPackage.getCode());
                            childToDelete.setName(childPackage.getName());
                            childToDelete.setArea(childPackage.getArea());
                            childToDelete.setTestType(childPackage.getTestType());
                            childToDelete.setLaboratory(aPackage.getLaboratory());
                            childToDelete.getSample().setId(childPackage.getSample().getId());
                            childToDelete.getSample().setLaboratorytype(childPackage.getSample().getLaboratorytype());
                            testToDelete.add(childToDelete);
                            //Consulta los hijos del perfil los recorre y los inserta
                            childsPanel = testService.getChilds(childPackage.getId());
                            for (net.cltech.enterprisent.domain.masters.test.Test test : childsPanel)
                            {
                                childToDelete = new Test();
                                childToDelete.setId(test.getId());
                                childToDelete.setCode(test.getCode());
                                childToDelete.setName(test.getName());
                                childToDelete.setArea(test.getArea());
                                childToDelete.setTestType(test.getTestType());
                                childToDelete.setLaboratory(aPackage.getLaboratory());
                                childToDelete.getSample().setId(test.getSample().getId());
                                childToDelete.getSample().setLaboratorytype(test.getSample().getLaboratorytype());
                                testToDelete.add(childToDelete);
                            }
                        } else
                        {
                            //Son examenes del paquete
                            childToDelete = new Test();
                            childToDelete.setId(childPackage.getId());
                            childToDelete.setCode(childPackage.getCode());
                            childToDelete.setName(childPackage.getName());
                            childToDelete.setArea(childPackage.getArea());
                            childToDelete.setTestType(childPackage.getTestType());
                            childToDelete.setLaboratory(aPackage.getLaboratory());
                            childToDelete.getSample().setId(childPackage.getSample().getId());
                            childToDelete.getSample().setLaboratorytype(childPackage.getSample().getLaboratorytype());
                            testToDelete.add(childToDelete);
                        }
                    }
                }
            }
        }

        //Se insertan perfiles
        List<Test> panels = tests.stream().filter((Test t) -> t.getTestType() == (short) 1).collect(Collectors.toList());
        if (!panels.isEmpty())
        {
            List<net.cltech.enterprisent.domain.masters.test.Test> childsPanel = null;
            net.cltech.enterprisent.domain.masters.test.Test panelDB = null;
            for (Test panel : panels)
            {
                panelDB = testService.get(panel.getId(), null, null, null);
                if (panelDB != null)
                {
                    //Inserta el perfil en la tabla
                    childToDelete = new Test();
                    childToDelete.setId(panelDB.getId());
                    childToDelete.setCode(panelDB.getCode());
                    childToDelete.setName(panelDB.getName());
                    childToDelete.setArea(panelDB.getArea());
                    childToDelete.setTestType(panelDB.getTestType());
                    childToDelete.setLaboratory(panel.getLaboratory());
                    childToDelete.getSample().setId(panelDB.getSample().getId());
                    childToDelete.getSample().setLaboratorytype(panelDB.getSample().getLaboratorytype());
                    testToDelete.add(childToDelete);
                    //Consulta los hijos del perfil los recorre y los inserta
                    childsPanel = testService.getChilds(panel.getId());
                    for (net.cltech.enterprisent.domain.masters.test.Test test : childsPanel)
                    {
                        childToDelete = new Test();
                        childToDelete.setId(test.getId());
                        childToDelete.setCode(test.getCode());
                        childToDelete.setName(test.getName());
                        childToDelete.setArea(test.getArea());
                        childToDelete.setTestType(test.getTestType());
                        childToDelete.setLaboratory(panel.getLaboratory());
                        childToDelete.getSample().setId(test.getSample().getId());
                        childToDelete.getSample().setLaboratorytype(test.getSample().getLaboratorytype());
                        testToDelete.add(childToDelete);
                    }
                }
            }
        }

        //Se insertan examenes
        List<Test> analites = tests.stream().filter((Test t) -> t.getTestType() == (short) 0).collect(Collectors.toList());
        if (!analites.isEmpty())
        {
            //Recorre los examenes
            for (Test analite : analites)
            {
                childToDelete = new Test();
                childToDelete.setId(analite.getId());
                childToDelete.setCode(analite.getCode());
                childToDelete.setName(analite.getName());
                childToDelete.setArea(analite.getArea());
                childToDelete.setTestType(analite.getTestType());
                childToDelete.setLaboratory(analite.getLaboratory());
                childToDelete.getSample().setId(analite.getSample().getId());
                childToDelete.getSample().setLaboratorytype(analite.getSample().getLaboratorytype());
                testToDelete.add(childToDelete);
            }
        }
        return testToDelete;
    }

 /**
     * Inserta o actualiza los precios de una orden
     *
     * @param order {@link }
     * @param insert Si es una inserción o una actualizacion
     * @throws Exception Error en base de datos
     */
    private void savePrices(Order order) throws Exception
    {
        if (Boolean.parseBoolean(configurationServices.get(ConfigurationConstants.KEY_RATE_ACTIVE).getValue()))
        {
            // Impuesto configurado para los examenes sin impuesto:
            String testTaxConfStr = configurationService.getValue("Impuesto") == null ? "0" : configurationService.getValue("Impuesto");
            final Double testTaxConf = testTaxConfStr.isEmpty() ? 0.0 : Double.parseDouble(testTaxConfStr);
            List<ResultTest> testorder = resultTestDao.listTestOrder(order.getOrderNumber());
            List<BillingTest> listtest = new ArrayList<>();
            for (ResultTest itemTest : testorder)
            {
                BillingTest billingtest = new BillingTest();
                Test testOrder = order.getTests().stream().filter(test -> Objects.equals(test.getId(), itemTest.getTestId())).findAny().orElse(null);
                billingtest.setTest(new Test());
                billingtest.getTest().setId(itemTest.getTestId());
                if (testOrder == null)
                {
                    billingtest.setRate(order.getRate());
                    billingtest.setDiscount(new BigDecimal(0));
                } else
                {
                    billingtest.setRate(testOrder.getRate() != null ? testOrder.getRate() : order.getRate());
                    billingtest.setDiscount(testOrder.getDiscount() != null ? testOrder.getDiscount() : new BigDecimal(0));
                }
                billingtest.setPatient(order.getPatient());
                billingtest.setOrder(order);

                //billing.setTest(t);
                billingtest.setServicePrice(getPriceTest(itemTest.getTestId(), billingtest.getRate().getId()).getServicePrice());
                // billingtest.setRate(t.getRate());
                // Obtenemos el cliente por medio del Id de este
                billingtest.setAccount(getAccountWithId(order.getAccount()));
                billingtest.setBranch(order.getBranch() == null ? null : order.getBranch());

                BigDecimal patientPercentage = new BigDecimal(orderDao.getPatientPercentageTest(itemTest.getTestId(), billingtest.getRate().getId()));
                if (patientPercentage != null)
                {
                    BigDecimal patientPrice = billingtest.getServicePrice().multiply(patientPercentage).divide(new BigDecimal(100));
                    billingtest.setPatientPrice(patientPrice);
                    BigDecimal insurancePrice = billingtest.getServicePrice().subtract(patientPrice);
                    billingtest.setInsurancePrice(insurancePrice);
                } else
                {
                    billingtest.setPatientPrice(BigDecimal.ZERO);
                    billingtest.setInsurancePrice(billingtest.getServicePrice());
                }
                // Impuesto del examen:
                Double testTax;
                try
                {
                    testTax = billingTestDao.getTestTaxByTestId(itemTest.getTestId());
                } catch (Exception e)
                {
                    testTax = 0.0;
                }
                // Validamos si el examen tiene un impuesto o 
                // Se le adicionara el de la llave de configuración
                if (testTax != null && testTax > 0)
                {
                    billingtest.setTax(new BigDecimal(testTax));
                } else
                {
                    billingtest.setTax(new BigDecimal(testTaxConf));
                }
                listtest.add(billingtest);

            }

            billingTestDao.deletePriceTestOrder(order.getOrderNumber());
            billingTestDao.insert(listtest);
        }
    }

    /**
     * Guarda los precios de los examenes de la orden. este
     *
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public void savePricesOrder(Order order, boolean insert) throws Exception
    {
        savePrices(order);
    }

    public List<Sample> getSamples(List<ResultTest> tests) throws Exception
    {
        //Consultar las muestras y recipientes del examen
        List<Sample> samples = new ArrayList<>(0);
        List<net.cltech.enterprisent.domain.masters.test.Test> childs = null;
        Sample s = null;
        Test h = null;
        for (ResultTest testR : tests)
        {
            switch (testR.getTestType())
            {
                case 0:
                    //Examen
                    net.cltech.enterprisent.domain.masters.test.Test test = testService.get(testR.getTestId(), null, null, null);
                    if (test.getSample() != null)
                    {
                        if (samples.contains(test.getSample()))
                        {
                            h = new Test();
                            h.setId(test.getId());
                            h.setCode(test.getCode());
                            h.setAbbr(test.getAbbr());
                            h.setArea(test.getArea());
                            samples.get(samples.indexOf(test.getSample())).getTests().add(h);
                        } else
                        {

                            s = test.getSample();
                            h = new Test();
                            h.setId(test.getId());
                            h.setCode(test.getCode());
                            h.setAbbr(test.getAbbr());
                            h.setArea(test.getArea());
                            s.getTests().add(h);
                            samples.add(s);
                        }
                    }
                    break;
                case 1:
                    //Perfil
                    childs = testService.getChilds(testR.getTestId());
                    for (net.cltech.enterprisent.domain.masters.test.Test child : childs)
                    {
                        if (child.getSample() != null)
                        {
                            if (samples.contains(child.getSample()))
                            {
                                h = new Test();
                                h.setId(child.getId());
                                h.setCode(child.getCode());
                                h.setAbbr(child.getAbbr());
                                h.setArea(child.getArea());
                                samples.get(samples.indexOf(child.getSample())).getTests().add(h);
                            } else
                            {
                                s = child.getSample();
                                h = new Test();
                                h.setId(testR.getTestId());
                                h.setCode(testR.getTestCode());
                                h.setAbbr(testR.getAbbreviation());
                                Area area = new Area();
                                area.setId(testR.getAreaId());
                                area.setName(testR.getAreaName());
                                area.setAbbreviation(testR.getAreaAbbr());
                                h.setArea(area);
                                s.getTests().add(h);
                                samples.add(child.getSample());
                            }
                        }
                    }
                    break;
                case 2:
                    //Paquete
                    childs = testService.getChilds(testR.getTestId());
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
                                    if (samples.contains(subChild.getSample()))
                                    {
                                        h = new Test();
                                        h.setId(subChild.getId());
                                        h.setCode(subChild.getCode());
                                        h.setAbbr(subChild.getAbbr());
                                        h.setArea(subChild.getArea());
                                        samples.get(samples.indexOf(subChild.getSample())).getTests().add(h);
                                    } else
                                    {
                                        s = subChild.getSample();
                                        h = new Test();
                                        h.setId(testR.getTestId());
                                        h.setCode(testR.getTestCode());
                                        h.setAbbr(testR.getAbbreviation());
                                        Area area = new Area();
                                        area.setId(testR.getAreaId());
                                        area.setName(testR.getAreaName());
                                        area.setAbbreviation(testR.getAreaAbbr());
                                        h.setArea(area);
                                        s.getTests().add(h);
                                        samples.add(s);
                                    }
                                }
                            }
                        } else
                        {
                            //Si es un examen
                            if (child.getSample() != null)
                            {
                                if (!samples.contains(child.getSample()))
                                {
                                    s = child.getSample();
                                    h = new Test();
                                    h.setId(testR.getTestId());
                                    h.setCode(testR.getTestCode());
                                    h.setCode(testR.getAbbreviation());
                                    Area area = new Area();
                                    area.setId(testR.getAreaId());
                                    area.setName(testR.getAreaName());
                                    area.setAbbreviation(testR.getAreaAbbr());
                                    h.setArea(area);
                                    s.getTests().add(h);
                                    samples.add(s);
                                }
                            }
                        }
                    }
                    break;
            }
        }
        return samples;
    }

    public List<Sample> getSamplesAudit(List<ResultTest> tests) throws Exception
    {
        //Consultar las muestras y recipientes del examen
        List<Sample> samples = new ArrayList<>(0);
        List<net.cltech.enterprisent.domain.masters.test.Test> childs = null;
        Sample s = new Sample();
        Test h = null;
        for (ResultTest testR : tests)
        {
            switch (testR.getTestType())
            {
                case 0:
                    //Examen
                    net.cltech.enterprisent.domain.masters.test.Test test = testService.get(testR.getTestId(), null, null, null);
                    if (test.getSample() != null)
                    {
                        if (samples.contains(test.getSample()))
                        {
                            h = new Test();
                            h.setId(test.getId());
                            h.setCode(test.getCode());
                            h.setAbbr(test.getAbbr());
                            samples.get(samples.indexOf(test.getSample())).getTests().add(h);
                        } else
                        {
                            s.setId(test.getSample().getId());
                            s.setName(test.getSample().getName());
                            s.setCodesample(test.getSample().getCodesample());

                            /*h = new Test();
                            h.setId(test.getId());
                            h.setCode(test.getCode());
                            h.setAbbr(test.getAbbr());
                            s.getTests().add(h);*/
                            samples.add(s);
                        }
                    }
                    break;
                case 1:
                    //Perfil
                    childs = testService.getChilds(testR.getTestId());
                    for (net.cltech.enterprisent.domain.masters.test.Test child : childs)
                    {
                        if (child.getSample() != null)
                        {
                            if (samples.contains(child.getSample()))
                            {
                                h = new Test();
                                h.setId(child.getId());
                                h.setCode(child.getCode());
                                h.setAbbr(child.getAbbr());
                                samples.get(samples.indexOf(child.getSample())).getTests().add(h);
                            } else
                            {
                                s.setId(child.getSample().getId());
                                s.setName(child.getSample().getName());
                                s.setCodesample(child.getSample().getCodesample());

                                /*s = child.getSample();
                                h = new Test();
                                h.setId(testR.getTestId());
                                h.setCode(testR.getTestCode());
                                h.setAbbr(testR.getAbbreviation());
                                s.getTests().add(h);*/
                                samples.add(child.getSample());
                            }
                        }
                    }
                    break;
                case 2:
                    //Paquete
                    childs = testService.getChilds(testR.getTestId());
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
                                    if (samples.contains(subChild.getSample()))
                                    {
                                        h = new Test();
                                        h.setId(subChild.getId());
                                        h.setCode(subChild.getCode());
                                        h.setAbbr(subChild.getAbbr());
                                        samples.get(samples.indexOf(subChild.getSample())).getTests().add(h);
                                    } else
                                    {
                                        s.setId(child.getSample().getId());
                                        s.setName(child.getSample().getName());
                                        s.setCodesample(child.getSample().getCodesample());

                                        /*s = subChild.getSample();
                                        h = new Test();
                                        h.setId(testR.getTestId());
                                        h.setCode(testR.getTestCode());
                                        h.setAbbr(testR.getAbbreviation());
                                        s.getTests().add(h);*/
                                        samples.add(s);
                                    }
                                }
                            }
                        } else
                        {
                            //Si es un examen
                            if (child.getSample() != null)
                            {
                                if (!samples.contains(child.getSample()))
                                {
                                    s.setId(child.getSample().getId());
                                    s.setName(child.getSample().getName());
                                    s.setCodesample(child.getSample().getCodesample());

                                    /* s = child.getSample();
                                    h = new Test();
                                    h.setId(testR.getTestId());
                                    h.setCode(testR.getTestCode());
                                    h.setCode(testR.getAbbreviation());
                                    s.getTests().add(h);*/
                                    samples.add(s);
                                }
                            }
                        }
                    }
                    break;
            }
        }
        return samples;
    }

    @Override
    public List<OrderSearch> getRecall(long order) throws Exception
    {
        return orderDao.getRecall(order).stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<OrderSearch> getRecall(int documentTypeId, String patientId) throws Exception
    {
        if (documentTypeId == -1)
        {
            return orderDao.getRecall(null, Tools.encrypt(patientId.trim())).stream().distinct().collect(Collectors.toList());
        } else
        {
            return orderDao.getRecall(documentTypeId, Tools.encrypt(patientId.trim())).stream().distinct().collect(Collectors.toList());
        }
    }

    @Override
    public List<OrderSearch> getRecall(String lastName, String surName, String name1, String name2) throws Exception
    {
        return orderDao.getRecall(lastName.trim().isEmpty() || lastName.trim().equals("undefined") ? null : Tools.encrypt(lastName.trim()), surName.trim().isEmpty() || surName.trim().equals("undefined") ? null : Tools.encrypt(surName.trim()), name1.trim().isEmpty() || name1.trim().equals("undefined") ? null : Tools.encrypt(name1.trim()), name2.trim().isEmpty() || name2.trim().equals("undefined") ? null : Tools.encrypt(name2.trim())).stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<OrderSearch> getRecall(int dateI, int dateF) throws Exception
    {
        return orderDao.getRecall(dateI, dateF).stream().distinct().collect(Collectors.toList());
    }

    @Override
    public Order recall(long order, int user, int branch) throws Exception
    {
        //Obtenemos los examenes para el rellamado
        List<TestToRecall> testsToRecall = sampleTrackingDao.getTestToRecall(order);
        //Obtenemos los datos de la orden para rellamado
        Order orderObject = get(order);
        //Establecemos el numero de la orden en null para que sea creada
        orderObject.setOrderNumber(null);
        //Establecemos el tipo de orden de rellamado
        OrderType type = orderTypeService.filterByCode("C");
        orderObject.setType(type);
        //Obtenemos la tarifa
        Rate rate = orderObject.getRate();
        //Establecemos los examenes de rellamado
        orderObject.setTests(testsToRecall.stream().map((TestToRecall t)
                ->
        {
            Test test = new Test();
            test.setId(t.getTests().get(0).getId());
            test.setCode(t.getTests().get(0).getCode());
            test.setAbbr(t.getTests().get(0).getAbbr());
            test.setName(t.getTests().get(0).getName());
            test.setTestType(t.getTests().get(0).getTestType());
            if (rate != null)
            {
                test.setRate(rate);
                try
                {
                    test.setPrice(getPriceTest(test.getId(), rate.getId()).getServicePrice());
                } catch (Exception ex)
                {
                    test.setPrice(BigDecimal.ZERO);
                }
            }
            return test;
        }).collect(Collectors.toList()));
        //Crea la orden de rellamado
        orderObject = create(orderObject, user, branch);
        if (orderObject != null)
        {
            //Actualiza los examenes a rellamados
            sampleTrackingDao.updateStateToRecall(order, testsToRecall);
        }
        return orderObject;
    }

    @Override
    public Order cancel(long order, int user) throws Exception
    {
        //Actualiza el estado de la orden
        Order orderObj = new Order();
        orderObj.setOrderNumber(order);
        orderObj.setState(LISEnum.ResultOrderState.CANCELED.getValue());
        List<Order> records = new ArrayList<>(0);
        records.add(orderObj);
        orderDao.updateOrderState(records, LISEnum.ResultOrderState.CANCELED);

        //Cambia de estado de orden en estadisticas. 
        List<Long> ordesStadistics = new ArrayList<>(0);
        ordesStadistics.add(orderObj.getOrderNumber());
        statisticService.disableOrders(ordesStadistics, LISEnum.ResultOrderState.CANCELED);

        //Inserta en la auditoria
        orderObj = getAudit(order);
        List<AuditOperation> auditList = new ArrayList<>();
        auditList.add(new AuditOperation(order, null, null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_ORDER, Tools.jsonObject(orderObj), null, null, null, null));
        trackingService.registerOperationTracking(auditList);
        if (Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("anularOrden").getValue()) == true)
        {
            CompletableFuture.runAsync(()
                    ->
            {
                eventsOrderService.cancel(order);
            });
        }
        cashBoxService.createBoxInExternalBilling(new CashBox(), 2, order);
        return records.get(0);
    }

    @Override
    public int createDiagnosticsByOrder(Order order) throws Exception
    {
        return diagnosticDao.createAllByOrder(order);
    }

    @Override
    public int updateDiagnosticsByOrder(Order order) throws Exception
    {
        return diagnosticDao.updateAllByOrder(order);
    }

    @Override
    public Order updateConfigPrint(Order order) throws Exception
    {
        return orderDao.updateConfigPrint(order);
    }

    @Override
    public List<Order> withoutTurn(String history, String type) throws Exception
    {
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        return orderDao.withoutTurn(Tools.encrypt(history), type, laboratorys, idbranch);
    }
    
     @Override
    public List<Order> withoutAppointmentTurn(String history, String type) throws Exception
    {
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        return orderDao.withoutAppointmentTurn(Tools.encrypt(history), type, laboratorys, idbranch);
    }
    
    

    @Override
    public int shiftOrders(ShiftOrder object) throws Exception
    {
        int count = orderDao.shiftOrders(object);
        //Trazabilidad de la orden
        List<AuditOperation> auditList = new ArrayList<>(0);
        object.getOrders().stream().forEach((String o)
                ->
        {
            try
            {
                SigaTurnMovement turn = new SigaTurnMovement();
                SigaTurn sigaTurn = new SigaTurn();
                sigaTurn.setId(object.getId());
                turn.setOrder(Long.parseLong(o));
                turn.setTurn(sigaTurn);
                integrationSigaService.serviceTurnOrder(turn);

                Order order = getAudit(Long.parseLong(o)).clean();
                auditList.add(new AuditOperation(order.getOrderNumber(), null, null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_ORDER, Tools.jsonObject(object.getId()), null, "Se asigna turn a la orden", null, null));
            } catch (Exception ex)
            {
                Logger.getLogger(OrderServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        trackingService.registerOperationTracking(auditList);
        return count;
    }

    @Override
    public List<Order> ordersByTurn(String turn) throws Exception
    {
        return orderDao.ordersByTurn(turn);
    }

    @Override
    public long numberOrders(int date, Integer idUser, Integer idBranch) throws Exception
    {
        return orderDao.numberOrders(date, idUser, idBranch);
    }

    /**
     * Numeros de hijos validados de un paquete o un perfil
     *
     * @param test perfil/paquete
     * @param allTests examenes de la orden
     * @return Cantidad de hijos validados
     */
    public int childsWithValidation(ResultTest test, List<ResultTest> allTests)
    {
        return allTests.stream()
                .filter(t -> t.getPackageId() == test.getTestId() || t.getProfileId() == test.getTestId())
                .filter(t -> t.getState() >= LISEnum.ResultTestState.VALIDATED.getValue())
                .collect(Collectors.toList())
                .size();
    }

    @Override
    public List<Order> getByPatientInfoRecalled(Integer documentType, String patientId, String lastName, String surName, String name1, String name2, Integer sex, Date birthday, int branch) throws Exception
    {
        if (patientId != null && !patientId.toLowerCase().equals("undefined"))
        {
            patientId = Tools.encrypt(patientId);
        } else
        {
            patientId = null;
        }

        if (lastName != null && !lastName.toLowerCase().equals("undefined"))
        {
            lastName = Tools.encrypt(lastName);
        } else
        {
            lastName = null;
        }

        if (surName != null && !surName.toLowerCase().equals("undefined"))
        {
            surName = Tools.encrypt(surName);
        } else
        {
            surName = null;
        }

        if (name1 != null && !name1.toLowerCase().equals("undefined"))
        {
            name1 = Tools.encrypt(name1);
        } else
        {
            name1 = null;
        }

        if (name2 != null && !name2.toLowerCase().equals("undefined"))
        {
            name2 = Tools.encrypt(name2);
        } else
        {
            name2 = null;
        }
        boolean service = configurationService.getValue("ManejoServicio").equalsIgnoreCase("true");
        return orderDao.getByPatientInfoRecalled(documentType, patientId, lastName, surName, name1, name2, sex, birthday, branch, demographicService.demographicsList(), service);
    }

    @Override
    public Order getByOrderRecalled(long order, int branch) throws Exception
    {
        boolean service = configurationService.getValue("ManejoServicio").equalsIgnoreCase("true");
        return orderDao.getByOrderRecalled(order, branch, demographicService.demographicsList(), service);
    }

    @Override
    public List<Order> getByEntryDateRecalled(int date, int branch) throws Exception
    {
        boolean service = configurationService.getValue("ManejoServicio").equalsIgnoreCase("true");
        return orderDao.getByEntryDateRecalled(date, branch, demographicService.demographicsList(), service);
    }

    @Override
    public Order getRecalledOrder(long order) throws Exception
    {
        Order orderRecalled = new Order();
        orderRecalled.setOrderNumber(order);
        orderRecalled.setFatherOrder(orderDao.getFatherOrder(order));
        orderRecalled.setDaughterOrder(orderDao.getDaughterOrder(order));
        return orderRecalled;
    }

    @Override
    public Order updateTurnOrderhis(Order order) throws Exception
    {
        //obtiene el numero de la orden segun orden his
        order.setOrderNumber(orderDao.getOrderLisHis(order.getOrderHis()).get(0));
        //Inserta en la auditoria
        Order orderObj = new Order();
        if (order.getOrderNumber() != null)
        {
            orderDao.updateOrderTurn(order.getOrderNumber(), order.getTurn());

            List<AuditOperation> auditList = new ArrayList<>();
            auditList.add(new AuditOperation(order.getOrderNumber(), null, null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_ORDER, Tools.jsonObject(order.getTurn()), null, "Turno HIS", null, null));
            trackingService.registerOperationTracking(auditList);
        }
        return orderObj;

    }

    private static final int ORDER_TYPE_RECALLED = 4;

    /**
     * Valida si el número de orden es el indicado añadiendo lo que haga falta a
     * este
     *
     * @param idOrder
     * @return True - si este número de orden existe, False - si este no existe
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public boolean isValidOrder(long idOrder) throws Exception
    {
        try
        {
            LocalDate today = LocalDate.now();
            String sToday = String.valueOf(today).replace("-", "");
            String caracters = String.valueOf(idOrder);
            int totalCaracters = 0;
            String idOrderCompleate = "";
            long idOrderC = 0L;
            // Longitud de la orden
            for (int i = 0; i < caracters.length(); i++)
            {
                if (caracters.charAt(i) != ' ')
                {
                    totalCaracters++;
                }
            }

            // Verificamos desde donde debe ser completado el número de la orden
            switch (totalCaracters)
            {
                // Solo llega el número de la orden
                case 1:
                    idOrderCompleate = sToday + "000" + caracters;
                    idOrderC = Long.parseLong(idOrderCompleate);
                    break;
                case 2:
                    idOrderCompleate = sToday + "00" + caracters;
                    idOrderC = Long.parseLong(idOrderCompleate);
                    break;
                case 3:
                    idOrderCompleate = sToday + "0" + caracters;
                    idOrderC = Long.parseLong(idOrderCompleate);
                    break;
                case 4:
                    idOrderCompleate = sToday + caracters;
                    idOrderC = Long.parseLong(idOrderCompleate);
                    break;
                // Llega el día y el número de la orden - El día viene de esta manera -> 01
                // Por ende debe agregarsele el cero, porque el formato long omite el cero cuando esta al inicio de un número
                case 5:
                    idOrderCompleate = sToday.substring(0, 6) + "0" + caracters;
                    idOrderC = Long.parseLong(idOrderCompleate);
                    break;
                // Llega el día y el número de la orden - El día viene de esta manera -> 10
                case 6:
                    idOrderCompleate = sToday.substring(0, 6) + caracters;
                    idOrderC = Long.parseLong(idOrderCompleate);
                    break;
                // Llega el mes, el día y el número de la orden - El mes viene de esta manera -> 01
                // Por ende debe agregarsele el cero, porque el formato long omite el cero cuando esta al inicio de un número
                case 7:
                    idOrderCompleate = sToday.substring(0, 4) + "0" + caracters;
                    idOrderC = Long.parseLong(idOrderCompleate);
                    break;
                // Llega el mes, el día y el número de la orden - El mes viene de esta manera -> 10
                case 8:
                    idOrderCompleate = sToday.substring(0, 4) + caracters;
                    idOrderC = Long.parseLong(idOrderCompleate);
                    break;
                // Llega el año, el mes, el día y el número de la orden -> El año llega de la siguiente manera -> 01
                // Por ende debe agregarsele el cero, porque el formato long omite el cero cuando esta al inicio de un número
                case 9:
                    idOrderCompleate = sToday.substring(0, 2) + "0" + caracters;
                    idOrderC = Long.parseLong(idOrderCompleate);
                    break;
                // Llega el año, el mes, el día y el número de la orden -> El año llega de la siguiente manera -> 10
                case 10:
                    idOrderCompleate = sToday.substring(0, 2) + caracters;
                    idOrderC = Long.parseLong(idOrderCompleate);
                    break;
                // Llega el año, el mes, el día y el número de la orden
                case 12:
                    idOrderC = Long.parseLong(caracters);
                    break;
            }
            Order order = get(idOrderC);
            return order != null;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Obtiene todos los requerimientos asociados a una orden
     *
     * @param idOrder
     * @return Requerimientos de la orden en un string
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public String getRequirements(long idOrder) throws Exception
    {
        try
        {
            // Obtenemos los examenes que pertenecen a una orden
            List<Integer> idsTests = resultsDao.getTestsIds(idOrder);
            String requirements = "";
            for (Integer idTest : idsTests)
            {
                // Obtenemos los requerimientos de la prueba
                List<String> haveReqs = orderDao.getRequirements(idOrder, idTest);
                for (String haveReq : haveReqs)
                {
                    if (requirements.isEmpty())
                    {
                        requirements = haveReq;
                    } else
                    {
                        requirements += "||" + haveReq;
                    }
                }
            }
            return requirements;
        } catch (Exception e)
        {
            return "";
        }
    }

    /**
     * Obtiene los demograficos que son obligatorios pero no son requeridos para
     * dicha sede
     *
     * @return
     * @throws java.lang.Exception Error al retornar los demograficos
     */
    @Override
    public List<User> getUserValidate(long idOrder) throws Exception
    {
        try
        {
            List<User> listUser = orderDao.getUserValidate(idOrder);

            listUser = listUser.stream()
                    .filter(distinctByKey(p -> p.getId()))
                    .collect(Collectors.toList());

            return listUser;
        } catch (Exception e)
        {
            return null;
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Obtiene una orden para ser enviada a infinity
     *
     * @param idOrderExt id de la ordel del sistema externo
     * @return
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public Patient getPatientForExternalIdOrder(String idOrderExt) throws Exception
    {
        try
        {
            long idOrder = orderDao.getOrderLisHis(idOrderExt).get(0);
            Patient patientIdOrderExt = new Patient();
            if (idOrder != 0)
            {
                patientIdOrderExt = patientService.get(idOrder);
                if (patientIdOrderExt != null)
                {
                    patientIdOrderExt.setOrderNumber(idOrder);
                    return patientIdOrderExt;
                } else
                {
                    return null;
                }
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
     * Obtiene los demograficos que son obligatorios pero no son requeridos para
     * dicha sede
     *
     * @param idBranch
     * @return
     * @throws java.lang.Exception Error al retornar los demograficos
     */
    public List<DemographicBranch> getDemographicsExcluded(int idBranch) throws Exception
    {
        try
        {
            List<DemographicBranch> listDemographics = demographicItemDao.demographicsBranch(idBranch);
            return listDemographics.stream().filter(demo -> demo.getSelected() == false && demo.isRequired() == true).collect(Collectors.toList());
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Verifica la existencia de una orden LIS con el id de una orden de un
     * sistema externo
     *
     * @param idOrderExt id de la ordel del sistema externo
     * @return True - la orden LIS existe, False - la orden LIS no existe
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public boolean orderExistsByExternalSystemOrder(String idOrderExt) throws Exception
    {
        try
        {
            return orderDao.getOrderLisHis(idOrderExt).size() > 0;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Consulta un listado de ordenes por un filtro en especifico, obteniendo
     * las ordenes para la realizacion de un pago posterior.
     *
     * @param filter
     * @return Lista de ids de las ordenes
     * @throws Exception Error en el servicio
     */
    @Override
    public List<Long> subsequentPayments(FilterSubsequentPayments filter) throws Exception
    {
        try
        {
            return orderDao.subsequentPayments(filter);
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    private Account getAccountWithId(Account account)
    {
        try
        {
            if (account != null)
            {
                Account accountAux = accountService.get(account.getId(), null, null, null, null);
                return accountAux;
            } else
            {
                return null;
            }
        } catch (Exception ex)
        {
            return null;
        }
    }

    private Test getTestToInsertInResult(int patient, int testId, int branch, int orderType, int packageId, int profileId)
    {
        try
        {
            net.cltech.enterprisent.domain.masters.test.Test testAux = testService.get(testId, null, null, null);
            Test test = null;
            if (testAux != null)
            {
                test = new Test();
                test.setId(testAux.getId());
                test.setTestState((testAux.getAutomaticResult() == null || testAux.getAutomaticResult().trim().isEmpty()) ? LISEnum.ResultTestState.ORDERED.getValue() : LISEnum.ResultTestState.REPORTED.getValue());
                test.setPrint(testAux.getPrintOnReport());
                test.setTestType(testAux.getTestType());
                test.setAutomaticResult(testAux.getAutomaticResult());
                test.setFixedComment(testAux.getFixedComment());
                if (packageId > 0)
                {
                    test.setPack(new Test(packageId));
                }
                if (testAux.getTestType() == 1 && testAux.isDependentExam())
                {
                    test.setSample(testAux.getSample());
                }
                if (test.getTestType() == 0)
                {
                    test.setSampleState(LISEnum.ResultSampleState.ORDERED.getValue());
                    if (profileId > 0)
                    {
                        test.setPanel(new Test(profileId));
                    }
                    test.setUnit(testAux.getUnit());
                    test.setTechnique(testAux.getTechnique());
                    test.setPrint(testAux.getPrintOnReport());
                    test.setHistoricGraphic(testAux.isPrintGraph());
                    test.setSample(testAux.getSample());
                    HistoricalResult historicalResult = resultsDao.getBasic(patient, testId);
                    if (historicalResult != null)
                    {
                        Result lastResult = new Result();
                        lastResult.setResult(historicalResult.getSecondLastResultTemp() == null ? historicalResult.getLastResult() : historicalResult.getSecondLastResultTemp());
                        lastResult.setDateResult(historicalResult.getSecondLastResultTemp() == null ? historicalResult.getLastResultDate() : historicalResult.getSecondLastResultDateTemp());
                        test.setLastResult(lastResult);
                        Result secondLastResult = new Result();
                        secondLastResult.setResult(historicalResult.getLastResult());
                        secondLastResult.setDateResult(historicalResult.getLastResultDate());
                        test.setSecondLastResult(secondLastResult);
                    }
                }
                return saveTest(test, branch, orderType);
            }
            return test;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Actualiza el historico de una orden
     *
     * @param order
     * @param sample
     * @param patient
     * @return Lista de ids de las ordenes
     * @throws java.io.UnsupportedEncodingException
     */
    @Override
    public Boolean updateOrderhistory(Long order, int sample, Integer patient) throws IllegalArgumentException, UnsupportedEncodingException, Exception
    {
        AuthorizedUser session = JWT.decode(request);

        List<Integer> tests = orderDao.getTestSample(order, sample);
        List<Test> testshistory = new LinkedList<>();
        for (Integer test : tests)
        {
            try
            {
                resultService.repeatPatientHistory(order, test, session.getId(), LISEnum.ResultTestState.ORDERED.getValue(), null, null);

                HistoricalResult historicalResult = resultsDao.getBasic(patient, test);
                if (historicalResult != null)
                {
                    Test testhistory = new Test();
                    testhistory.setId(test);
                    Result lastResult = new Result();
                    lastResult.setResult(historicalResult.getSecondLastResultTemp() == null ? historicalResult.getLastResult() : historicalResult.getSecondLastResultTemp());
                    lastResult.setDateResult(historicalResult.getSecondLastResultTemp() == null ? historicalResult.getLastResultDate() : historicalResult.getSecondLastResultDateTemp());
                    testhistory.setLastResult(lastResult);

                    Result secondLastResult = new Result();
                    secondLastResult.setResult(historicalResult.getLastResult());
                    secondLastResult.setDateResult(historicalResult.getLastResultDate());
                    testhistory.setSecondLastResult(secondLastResult);

                    testshistory.add(testhistory);

                }
            } catch (Exception e)
            {

            }
        }

        resultsDao.updateHistoricalResult(order, testshistory);

        return true;
    }

    @Override
    public LastOrderPatient getLastOrder(int id) throws Exception
    {
        if (Boolean.parseBoolean(configurationServices.get("AlarmaUltimaOrden").getValue()) == true)
        {

            AuthorizedUser user = JWT.decode(request);

            int days = Integer.parseInt(configurationServices.get("DiasAlarmaUltimaOrden").getValue());
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            int actual = Integer.parseInt(sf.format(date));

            LastOrderPatient order = orderDao.getLastOrder(actual - days, user.getBranch(), id);
            if (order != null)
            {
                order.setTests(resultService.getTestsLastOrder(order.getOrderNumber()));
                if (order.getTests().size() > 0)
                {
                    return order;
                }
            }
        }
        return null;
    }

    /**
     * Obtiene las ordenes con inconsistencias
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacio en
     * caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    @Override
    public List<InconsistentOrder> getOrdersWithInconsistencies() throws Exception
    {
        try
        {
            AuthorizedUser session = JWT.decode(request);
            return orderDao.listOrdersInconsistencies(session.getBranch());
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    @Override
    public synchronized Order create(Order order, int user) throws Exception
    {
        //Valida que la orden tenga asociado paciente
        if (order.getPatient() == null)
        {
            List<String> errors = new ArrayList<>(0);
            errors.add("1|Order not valid must contains patient");
            throw new EnterpriseNTException(errors);
        }
        //Revisa si el numero de orden viene null entonces se autogenera
        boolean autonumeric = false;
        int orderDigits = Integer.parseInt(configurationServices.get("DigitosOrden").getValue().trim());
        if (order.getOrderNumber() == null || order.getOrderNumber() == 0)
        {
            //Debe genera autonumerico
            autonumeric = true;
        } else if (String.valueOf(order.getOrderNumber()).length() < (8 + orderDigits))
        {
            //El numero de orden enviado no es correcto
            List<String> errors = new ArrayList<>(0);
            errors.add("1|Order " + order.getOrderNumber() + " not valid");
            throw new EnterpriseNTException(errors);
        } else
        {
            //No se debe generar autonumerico porque la orden ya tiene numero asignado
            autonumeric = false;
        }

        long first = System.currentTimeMillis();
        //Realiza el registro o actualizacion del paciente
        Patient patient = patientService.save(order.getPatient(), user);
        SecurityLog.info("Tiempo de respuesta guardado paciente " + (System.currentTimeMillis() - first));

        //Establece el paciente registrado en base de datos al objeto orden
        order.setPatient(patientService.get(patient.getId()));
        Date createdDate = new Date();
        order.setCreatedDate(createdDate);
        order.setLastUpdateDate(createdDate);
        User userBean = new User();
        userBean.setId(user);
        order.setLastUpdateUser(userBean);
        order.setCreateUser(userBean);
        Order orderCreated = null;
        AuthorizedUser session = JWT.decode(request);
        // OBtenemos los demograficos que no son requeridos para esa sede pero son obligatorios
        List<DemographicBranch> listRequeridDemographis = getDemographicsExcluded(session.getBranch());
        first = System.currentTimeMillis();
        try
        {
            //Si es autonumerico invoca el metodo para genera el autonumerico
            if (autonumeric)
            {
                order.setOrderNumber(getAutonumericNumber(order));
            }
            orderCreated = orderDao.create(order, listRequeridDemographis);
        } catch (DuplicateKeyException ex)
        {
            throw new EnterpriseNTException(Arrays.asList("1|order duplicate"));
        }
        SecurityLog.info("Tiempo de respuesta guardado orden " + (System.currentTimeMillis() - first));

        if (order.getListDiagnostic() != null && order.getListDiagnostic().isEmpty() == false)
        {
            if (order.getListDiagnostic().size() > 0)
            {
                diagnosticDao.createAllByOrder(orderCreated);
            }
        }
        boolean check = !configurationServices.get("Trazabilidad").getValue().equals("1");
        first = System.currentTimeMillis();
        List<Integer> tests = saveTests(order, user, createdDate, orderCreated.getBranch().getId(), order.getType().getCode().equals("S") ? 1 : 2, check, false);
        SecurityLog.info("Tiempo de respuesta guardado examenes " + (System.currentTimeMillis() - first));
        List<Test> listTest = new ArrayList<>();
        for (Integer itemTest : tests)
        {
            net.cltech.enterprisent.domain.masters.test.Test objTest = testService.get(itemTest, null, null, null);
            Test objAux = new Test();
            objAux.setId(objTest.getId());
            objAux.setCode(objTest.getCode());
            objAux.setName(objTest.getName());
            objAux.setTestType(objTest.getTestType());
            objAux.setPrint(objTest.getPrintOrder());
            objAux.setAbbr(objTest.getAbbr());
            objAux.setArea(objTest.getArea());
            objAux.setSample(objTest.getSample());
            listTest.add(objAux);
        }

        order.setTests(listTest);
        long orderNumber = orderCreated.getOrderNumber();

        AuthorizedUser sessiona = JWT.decode(request);

        statisticService.saveOrder(orderNumber);
        agileStatisticService.updateOrderBranch(orderNumber, true, sessiona);

        //Hilo para tableros de oportunidad
        CompletableFuture.runAsync(() ->
        {
            try
            {

                List<AuditOperation> auditList = new ArrayList<>(0);
                auditList.add(new AuditOperation(orderNumber, null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_ORDER, Tools.jsonObject(getAsDemographicList(order.getOrderNumber(), 0)), null, null));
                trackingService.registerOperationTracking(auditList);

            } catch (Exception e)
            {
                //return null;
            }

        });
        //Trazabilidad de la orden
        int status = 0;
        if (!configurationServices.get("Trazabilidad").getValue().equals("1"))
        {
            status = LISEnum.ResultSampleState.CHECKED.getValue();
        } else
        {
            status = LISEnum.ResultSampleState.ORDERED.getValue();
        }

        sampleTrackingService.sampleOrdered(orderNumber, session, status, LISEnum.OriginModule.ORIGIN.getValue());
        return orderCreated;
    }

    @Override
    public synchronized Order createIngreso(Order order, int user) throws Exception
    {

        //Revisa si el numero de orden viene null entonces se autogenera
        boolean autonumeric = false;
        int orderDigits = Integer.parseInt(configurationServices.get("DigitosOrden").getValue().trim());
        if (order.getOrderNumber() == null || order.getOrderNumber() == 0)
        {
            //Debe genera autonumerico
            autonumeric = true;
        } else if (String.valueOf(order.getOrderNumber()).length() < (8 + orderDigits))
        {
            //El numero de orden enviado no es correcto
            List<String> errors = new ArrayList<>(0);
            errors.add("1|Order " + order.getOrderNumber() + " not valid");
            OrderCreationLog.info("\"1|Order \" + order.getOrderNumber() + \" not valid\"");
            throw new EnterpriseNTException(errors);
        } else
        {
            //No se debe generar autonumerico porque la orden ya tiene numero asignado
            autonumeric = false;
        }
        long first = System.currentTimeMillis();
        //Realiza el registro o actualizacion del paciente
        patientService.save(order.getPatient(), user);
        OrderCreationLog.info("Tiempo de respuesta guardado paciente " + (System.currentTimeMillis() - first));

        //Establece el paciente registrado en base de datos al objeto orden
        //order.setPatient(patientService.get(patient.getId()));
        Date createdDate = new Date();
        order.setCreatedDate(createdDate);
        order.setLastUpdateDate(createdDate);
        User userBean = new User();
        userBean.setId(user);
        order.setLastUpdateUser(userBean);
        //  order.setCreateUser(userBean);
        Order orderCreated = null;
        List<DemographicBranch> listRequeridDemographis = getDemographicsExcluded(order.getBranch().getId());
        first = System.currentTimeMillis();
        OrderCreationLog.info("Tiempo de respuestaconsultando listRequeridDemographis " + (System.currentTimeMillis() - first));
        try
        {
            //Si es autonumerico invoca el metodo para genera el autonumerico
            if (autonumeric)
            {
                order.setOrderNumber(getAutonumericNumber(order));
            }
            orderCreated = orderDao.create(order, listRequeridDemographis);
            first = System.currentTimeMillis();
            OrderCreationLog.info("Tiempo de respuesta en orderDao.create " + (System.currentTimeMillis() - first));
            OrderCreationLog.info("ORDER FOR INTERFACE CREATE IS  " + orderCreated.getOrderNumber());
        } catch (DuplicateKeyException ex)
        {
            OrderCreationLog.info("1|order duplicate IN DUPLICATER");
            throw new EnterpriseNTException(Arrays.asList("1|order duplicate"));
        }
        OrderCreationLog.info("Tiempo de respuesta guardado orden " + (System.currentTimeMillis() - first));

        boolean check = !configurationServices.get("Trazabilidad").getValue().equals("1");

        //envia al midlerare
        saveTests(order, user, createdDate, orderCreated.getBranch().getId(), order.getType().getCode().equals("S") ? 1 : 2, check, false);
        savePrices(order);
        
        OrderCreationLog.info("EXAMENES CREATE FOR ORDER   " + orderCreated.getOrderNumber());
        first = System.currentTimeMillis();
        OrderCreationLog.info("Tiempo de respuesta guardado Orden" + (System.currentTimeMillis() - first));
        orderCreated.setTests(order.getTests());
        AuthorizedUser sessiona = new AuthorizedUser(user);
        sessiona.setBranch(order.getBranch().getId());
        if (!check)
        {
            sampleTrackingService.sampleOrdered(orderCreated.getOrderNumber(), sessiona, LISEnum.ResultSampleState.CHECKED.getValue(), null);
        } else
        {
            sampleTrackingService.sampleOrdered(orderCreated.getOrderNumber(), sessiona, !check ? LISEnum.ResultSampleState.CHECKED.getValue() : LISEnum.ResultSampleState.ORDERED.getValue(), LISEnum.OriginModule.ORIGIN.getValue(), null);
        }
        OrderCreationLog.info("AGREGA EXAMENS EN ORDES");
        integrationMiddlewareService.sendOrderASTM(order.getOrderNumber(), null, null, Constants.ENTRY, null, null, order.getDeleteTests(), order.getBranch().getId(), false);

        return orderCreated;
    }

    @Override
    public List<TestPrice> getPricesTests(FilterTestPrice filter) throws Exception
    {
        int tax = Integer.parseInt(configurationServices.get("Impuesto").getValue());
        return orderDao.getPricesTests(filter, tax);
    }

    /**
     * Obtiene todos los examenes que seran eliminados de la tabla
     *
     * @param tests Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Test}
     * @param idOrder
     * @return Lista de {net.cltech.enterprisent.domain.operation.orders.Test}
     * con los ids de los examenes eliminados
     * @throws Exception Error en base de datos
     */
    @Override
    public List<Test> getTestToDeleteOrder(List<Test> tests, long idOrder) throws Exception
    {
        List<Test> packages = tests.stream().filter((Test t) -> t.getTestType() == (short) 2).collect(Collectors.toList());
        Test childToDelete = null;
        List<Test> testToDelete = new ArrayList<>(0);
        if (!packages.isEmpty())
        {
            List<net.cltech.enterprisent.domain.masters.test.Test> childs = null;
            List<net.cltech.enterprisent.domain.masters.test.Test> childsPanel = null;
            net.cltech.enterprisent.domain.masters.test.Test child = null;
            //Recorre los paquetes
            for (Test aPackage : packages)
            {
                //Por cada paquete lo busca en base de datos para obtener la informacion
                child = testService.get(aPackage.getId(), null, null, null);
                if (child != null)
                {
                    //Agrega el paquete para eliminar
                    childToDelete = new Test();
                    childToDelete.setId(child.getId());
                    childToDelete.setCode(child.getCode());
                    childToDelete.setName(child.getName());
                    childToDelete.setArea(child.getArea());
                    childToDelete.setTestType(child.getTestType());
                    childToDelete.setLaboratory(aPackage.getLaboratory());
                    childToDelete.getSample().setId(child.getSample().getId());
                    childToDelete.getSample().setLaboratorytype(child.getSample().getLaboratorytype());
                    testToDelete.add(childToDelete);
                    //Obtiene y recorre los hijos del paquete
                    childs = testService.getChildsPackageOrProfile(child.getId(), idOrder, true);
                    for (net.cltech.enterprisent.domain.masters.test.Test childPackage : childs)
                    {
                        if (childPackage.getTestType() == (short) 1)
                        {
                            //Son perfiles del paquete
                            //Agrega el perfil a la lista
                            childToDelete = new Test();
                            childToDelete.setId(childPackage.getId());
                            childToDelete.setCode(childPackage.getCode());
                            childToDelete.setName(childPackage.getName());
                            childToDelete.setArea(childPackage.getArea());
                            childToDelete.setTestType(childPackage.getTestType());
                            childToDelete.setLaboratory(aPackage.getLaboratory());
                            childToDelete.getSample().setId(childPackage.getSample().getId());
                            childToDelete.getSample().setLaboratorytype(childPackage.getSample().getLaboratorytype());
                            childToDelete.setSampleState(childPackage.getSampleState());
                            testToDelete.add(childToDelete);
                            //Consulta los hijos del perfil los recorre y los inserta
                            childsPanel = testService.getChildsPackageOrProfile(childPackage.getId(), idOrder, true);
                            for (net.cltech.enterprisent.domain.masters.test.Test test : childsPanel)
                            {
                                childToDelete = new Test();
                                childToDelete.setId(test.getId());
                                childToDelete.setCode(test.getCode());
                                childToDelete.setName(test.getName());
                                childToDelete.setArea(test.getArea());
                                childToDelete.setTestType(test.getTestType());
                                childToDelete.setLaboratory(aPackage.getLaboratory());
                                childToDelete.getSample().setId(test.getSample().getId());
                                childToDelete.getSample().setLaboratorytype(test.getSample().getLaboratorytype());
                                childToDelete.setSampleState(test.getSampleState());
                                testToDelete.add(childToDelete);
                            }
                        } else
                        {
                            //Son examenes del paquete
                            childToDelete = new Test();
                            childToDelete.setId(childPackage.getId());
                            childToDelete.setCode(childPackage.getCode());
                            childToDelete.setName(childPackage.getName());
                            childToDelete.setArea(childPackage.getArea());
                            childToDelete.setTestType(childPackage.getTestType());
                            childToDelete.setLaboratory(aPackage.getLaboratory());
                            childToDelete.getSample().setId(childPackage.getSample().getId());
                            childToDelete.getSample().setLaboratorytype(childPackage.getSample().getLaboratorytype());
                            childToDelete.setSampleState(childPackage.getSampleState());
                            testToDelete.add(childToDelete);
                        }
                    }
                }
            }
        }

        //Se insertan perfiles
        List<Test> panels = tests.stream().filter((Test t) -> t.getTestType() == (short) 1).collect(Collectors.toList());
        if (!panels.isEmpty())
        {
            List<net.cltech.enterprisent.domain.masters.test.Test> childsPanel = null;
            net.cltech.enterprisent.domain.masters.test.Test panelDB = null;
            for (Test panel : panels)
            {
                panelDB = testService.get(panel.getId(), null, null, null);
                if (panelDB != null)
                {
                    //Inserta el perfil en la tabla
                    childToDelete = new Test();
                    childToDelete.setId(panelDB.getId());
                    childToDelete.setCode(panelDB.getCode());
                    childToDelete.setName(panelDB.getName());
                    childToDelete.setArea(panelDB.getArea());
                    childToDelete.setTestType(panelDB.getTestType());
                    childToDelete.setLaboratory(panel.getLaboratory());
                    childToDelete.getSample().setId(panelDB.getSample().getId());
                    childToDelete.getSample().setLaboratorytype(panelDB.getSample().getLaboratorytype());
                    testToDelete.add(childToDelete);
                    //Consulta los hijos del perfil los recorre y los inserta
                    childsPanel = testService.getChildsPackageOrProfile(panel.getId(), idOrder, false);
                    for (net.cltech.enterprisent.domain.masters.test.Test test : childsPanel)
                    {
                        childToDelete = new Test();
                        childToDelete.setId(test.getId());
                        childToDelete.setCode(test.getCode());
                        childToDelete.setName(test.getName());
                        childToDelete.setArea(test.getArea());
                        childToDelete.setTestType(test.getTestType());
                        childToDelete.setLaboratory(panel.getLaboratory());
                        childToDelete.getSample().setId(test.getSample().getId());
                        childToDelete.getSample().setLaboratorytype(test.getSample().getLaboratorytype());
                        childToDelete.setSampleState(test.getSampleState());
                        testToDelete.add(childToDelete);
                    }
                }
            }
        }

        //Se insertan examenes
        List<Test> analites = tests.stream().filter((Test t) -> t.getTestType() == (short) 0).collect(Collectors.toList());
        if (!analites.isEmpty())
        {
            //Recorre los examenes
            for (Test analite : analites)
            {
                childToDelete = new Test();
                childToDelete.setId(analite.getId());
                childToDelete.setCode(analite.getCode());
                childToDelete.setName(analite.getName());
                childToDelete.setArea(analite.getArea());
                childToDelete.setTestType(analite.getTestType());
                childToDelete.setLaboratory(analite.getLaboratory());
                childToDelete.getSample().setId(analite.getSample().getId());
                childToDelete.getSample().setLaboratorytype(analite.getSample().getLaboratorytype());
                childToDelete.setSampleState(testService.getSampleState(analite.getId(), idOrder));
                testToDelete.add(childToDelete);
            }
        }
        return testToDelete;
    }

    @Override
    public void packageTracking(List<PackageTracking> tracking) throws Exception
    {
        AuthorizedUser session = JWT.decode(request);
        orderDao.packageTracking(tracking, session.getId());
    }

    @Override
    public List<PackageTracking> getPackageTracking(Long idOrder) throws Exception
    {
        List<TestBasic> tests = testDao.listBaisc();
        List<PackageTracking> list = orderDao.getPackageTracking(idOrder);

        list.forEach(tracking ->
        {
            List<Integer> idsChilds = Stream.of(tracking.getIdChilds().split(",")).map(id -> Integer.valueOf(id)).collect(Collectors.toList());
            List<String> childs = tests.stream().filter(test -> idsChilds.contains(test.getId())).map(test -> test.getCode() + " - " + test.getName()).collect(Collectors.toList());
            tracking.setChilds(childs);
        });
        return list;
    }
    
    @Override
    public List<Requirement> getRequirement(long orderNumber) throws Exception
    {
        return testDao.readRequirementbyOrder(orderNumber);
    }
}

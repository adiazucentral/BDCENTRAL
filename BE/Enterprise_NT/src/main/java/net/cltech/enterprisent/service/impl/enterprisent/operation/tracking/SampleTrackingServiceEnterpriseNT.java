package net.cltech.enterprisent.service.impl.enterprisent.operation.tracking;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.common.TrackingDao;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationIngresoDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.TestDao;
import net.cltech.enterprisent.dao.interfaces.masters.tracking.DestinationDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.BillingTestDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultTestDao;
import net.cltech.enterprisent.dao.interfaces.operation.tracking.SampleTrackingDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardOpportunityTime;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsSonControl;
import net.cltech.enterprisent.domain.integration.skl.RequestSampleDestination;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.masters.tracking.DestinationRoute;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.common.Reason;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Result;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.orders.billing.BillingTest;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.tracking.SampleDelayed;
import net.cltech.enterprisent.domain.operation.tracking.SampleState;
import net.cltech.enterprisent.domain.operation.tracking.SampleTracking;
import net.cltech.enterprisent.domain.operation.tracking.TestSampleTake;
import net.cltech.enterprisent.domain.operation.tracking.TestSampleTakeTracking;
import net.cltech.enterprisent.domain.operation.tracking.TrackingAlarm;
import net.cltech.enterprisent.domain.operation.tracking.VerifyDestination;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationDashBoardService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationHomeBoundService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationIngresoService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMiddlewareService;
import net.cltech.enterprisent.service.interfaces.masters.common.HolidayService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.test.DiagnosticService;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.common.CommentService;
import net.cltech.enterprisent.service.interfaces.operation.microbiology.MicrobiologyService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.StatisticService;
import net.cltech.enterprisent.service.interfaces.operation.tracking.SampleTrackingService;
import net.cltech.enterprisent.service.interfaces.operation.widgets.WidgetService;
import net.cltech.enterprisent.service.interfaces.tools.events.EventsSampleTrackingService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Log;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.enums.ListEnum;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import net.cltech.enterprisent.tools.log.integration.MiddlewareLog;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de la verificación de la muestra para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @since 19/09/2017
 * @see Creacion
 */
@Service
public class SampleTrackingServiceEnterpriseNT implements SampleTrackingService
{

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SampleTrackingDao dao;
    @Autowired
    private TrackingDao trackingDao;
    @Autowired
    private ConfigurationDao daoConfig;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private WidgetService widgetService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private DestinationDao destinationDao;
    @Autowired
    private IntegrationMiddlewareService integrationMiddlewareService;
    @Autowired
    private ConfigurationService configurationServices;
    @Autowired
    private EventsSampleTrackingService eventsSampleTrackingService;
    @Autowired
    DemographicService demographicService;
    @Autowired
    private OrdersDao orderDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private ResultsService resultService;
    @Autowired
    private DiagnosticService diagnosticService;
    @Autowired
    private BillingTestDao billingTestDao;
    @Autowired
    private CommentService commentService;
    @Autowired
    private MicrobiologyService microbiologyService;
    @Autowired
    private TestService testService;
    @Autowired
    private IntegrationIngresoService serviceIngreso;
    @Autowired
    private IntegrationHomeBoundService integrationHomeBoundService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private IntegrationDashBoardService dashBoardService;
    @Autowired
    private IntegrationIngresoDao integrationIngresoDao;
    @Autowired
    private ResultTestDao resultTestDao;
    @Autowired
    private HolidayService holidayService;
    @Autowired
    private TestDao testDao;
    @Autowired
    private LaboratorysByBranchesService laboratorysByBranchesService;


    @Override
    public Order getOrder(long idOrder, boolean sampleTracking) throws Exception
    {
        try
        {
            int idbranch = JWT.decode(request).getBranch();
            List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

            Order order = dao.getOrder(idOrder, laboratorys, idbranch);
            
            AuthorizedUser authorizedUser;
            Integer idBranch;

            if (order != null)
            {
                if(order.getTests().size() > 0)
                {
                    List<Sample> samples = order.getTests().stream().filter(test -> (test.getTestType() == 0 && test.getProfile() == 0) || (test.getTestType() > 0 && test.getSample().getId() != null) || (test.getTestType() == 0 && test.getProfile() != 0 && test.getDependentTest() == 0)).map(test -> test.getSample()).distinct().collect(Collectors.toList());
                    if (!samples.isEmpty())
                    {
                        samples.stream().forEach(sample
                                ->
                        {
                            sample.setTests(getTestsSample(order.getTests(), sample, order.getOrderNumber()));
                        });
                    }
                    //permite utilizar este metodo cuando no se tiene token
                    try
                    {
                        authorizedUser = JWT.decode(request);
                        idBranch = authorizedUser.getBranch();

                    } catch (Exception e)
                    {
                        authorizedUser = new AuthorizedUser(1);///user lismanager
                        idBranch = order.getBranch().getId();

                    }

                    if (sampleTracking)
                    {
                        for (Sample sample : samples)
                        {
                            SampleState sampleState = dao.getSampleState(sample.getId(), idOrder, idBranch);

                            if (sampleState == null)
                            {
                                Date currentDate = new Date();
                                dao.insertSampleOrdered(order.getOrderNumber(), authorizedUser.getId(), idBranch, sample.getId(), LISEnum.ResultSampleState.ORDERED.getValue(), currentDate);
                            }

                            sample.setSampleState(dao.getSampleState(sample.getId(), idOrder, idBranch));
                            sample.setSampleTrackings(dao.getSampleTracking(sample.getId(), idOrder));
                            if (sample.getSampleState() != null)
                            {
                                sample.getSampleState().setSample(null);
                            }

                        }
                    }

                    order.setSamples(samples);
                }
                else {
                    return null;
                }
//                order.setTests(null);
            }
            return order;
        } catch (Exception e)
        {
            OrderCreationLog.error(e);
            return new Order();
        }

    }

    public Order getOrder(long idOrder, boolean sampleTracking, AuthorizedUser session) throws Exception
    {
        AuthorizedUser authorizedUser = session;
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);
            
        Order order = dao.getOrder(idOrder, laboratorys, idbranch);
        if (order != null)
        {
            List<Sample> samples = order.getTests().stream().filter(test -> (test.getTestType() == 0 && test.getProfile() == 0) || (test.getTestType() > 0 && test.getSample().getId() != null) || (test.getTestType() == 0 && test.getProfile() != 0 && test.getDependentTest() == 0)).map(test -> test.getSample()).distinct().collect(Collectors.toList());

            samples.stream().forEach(sample
                    ->
            {
                sample.setTests(getTestsSample(order.getTests(), sample, order.getOrderNumber()));
            });

            if (sampleTracking)
            {
                for (Sample sample : samples)
                {
                    SampleState sampleState = dao.getSampleState(sample.getId(), idOrder, authorizedUser.getBranch());

                    if (sampleState == null)
                    {
                        Date currentDate = new Date();
                        dao.insertSampleOrdered(order.getOrderNumber(), authorizedUser.getId(), authorizedUser.getBranch(), sample.getId(), LISEnum.ResultSampleState.ORDERED.getValue(), currentDate);
                    }

                    sample.setSampleState(dao.getSampleState(sample.getId(), idOrder, authorizedUser.getBranch()));
                    sample.setSampleTrackings(dao.getSampleTracking(sample.getId(), idOrder));
                    if (sample.getSampleState() != null)
                    {
                        sample.getSampleState().setSample(null);
                    }

                }
            }
            order.setSamples(samples);
            order.setTests(null);
        }
        return order;
    }

    @Override
    public Order getOrderTracking(long idOrder, boolean sampleTracking) throws Exception
    {
        AuthorizedUser authorizedUser = JWT.decode(request);
        
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        Order order = dao.getOrderTracking(idOrder, authorizedUser.getId(), laboratorys, idbranch);
        if (order != null)
        {
            List<Sample> samples = order.getTests().stream().filter(test -> (test.getTestType() == 0 && test.getProfile() == 0) || (test.getTestType() > 0 && test.getSample().getId() != null) || (test.getTestType() == 0 && test.getProfile() != 0 && test.getDependentTest() == 0)).map(test -> test.getSample()).distinct().collect(Collectors.toList());

            samples.stream().forEach(sample
                    ->
            {
                sample.setTests(getTestsSample(order.getTests(), sample, order.getOrderNumber()));
            });

            order.setSamples(samples);
            order.setTests(null);

            if (sampleTracking)
            {
                for (Sample sample : samples)
                {
                    SampleState sampleState = dao.getSampleState(sample.getId(), idOrder, authorizedUser.getBranch());
                    if (sampleState == null)
                    {
                        OrderCreationLog.info("Llega a insatr en lab159" + order.getOrderNumber() + "muestra" + sample.getId());
                        Date currentDate = new Date();
                        dao.insertSampleOrdered(order.getOrderNumber(), authorizedUser.getId(), authorizedUser.getBranch(), sample.getId(), LISEnum.ResultSampleState.ORDERED.getValue(), currentDate);
                    }

                    sample.setSampleState(dao.getSampleState(sample.getId(), idOrder, authorizedUser.getBranch()));
                    sample.setSampleTrackings(dao.getSampleTracking(sample.getId(), idOrder));
                    if (sample.getSampleState() != null)
                    {
                        sample.getSampleState().setSample(null);
                    }

                }
            }
        }
        return order;
    }

    @Override
    public boolean sampleTracking(long idOrder, String codeSample, int type, Reason reason, Boolean retake, Boolean skipAppointment ) throws Exception
    {
        List<AuditOperation> audit = new ArrayList<>();
        Order order = getOrder(idOrder, false);

        Sample sample = order == null ? null : order.getSamples().stream().filter(s -> s.getCodesample().equals(codeSample)).findAny().orElse(null);

        AuthorizedUser authorizedUser;
        Integer idBranch;
        //permite utilizar este metodo cuando no se tiene token
        try
        {
            authorizedUser = JWT.decode(request);
            idBranch = authorizedUser.getBranch();

        } catch (Exception e)
        {
            authorizedUser = new AuthorizedUser(1);///user lismanager
            idBranch = order.getBranch().getId();

        }

        List<String> errors = validateFields(order);

        if (sample == null || sample.getId() == null || (order.getHasAppointment() == 1 && skipAppointment ))
        {
            errors.add("1|sample");
        }

        if (errors.isEmpty())
        {
            // Variable que guarda los ids de los examenes contiene esa muestra
            StringBuilder examenesOne = new StringBuilder();
            SampleState sampleState = dao.getSampleState(sample.getId(), idOrder, idBranch);
            
            //VERIFICACION DIRECTA POR MUESTRA DESDE OTRAS SEDES
            if(sampleState == null && type == LISEnum.ResultSampleState.CHECKED.getValue()) {
                Date currentDate = new Date();
                dao.insertSampleOrdered(order.getOrderNumber(), authorizedUser.getId(), idBranch, sample.getId(), LISEnum.ResultSampleState.ORDERED.getValue(), currentDate);
                sampleState = dao.getSampleState(sample.getId(), idOrder, idBranch);
            }
            
            int statetemp = sampleState == null ? -1 : sampleState.getState();

            if (type != statetemp)
            {
                if (type == LISEnum.ResultSampleState.CHECKED.getValue() && statetemp < LISEnum.ResultSampleState.COLLECTED.getValue())
                {
                    sampleTracking(idOrder, codeSample, LISEnum.ResultSampleState.COLLECTED.getValue(), null, retake, true);
                }

                if (type == LISEnum.ResultSampleState.REJECTED.getValue() || type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
                {
                    if (reason.getMotive() == null || reason.getMotive().getId() == null || reason.getMotive().getId() == 0)
                    {
                        errors.add("0|motive");
                        throw new EnterpriseNTException(errors);
                    }

                    if (type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
                    {

                        List<Test> testsReject = reason.getTests().stream().collect(Collectors.toList());

                        if (testsReject.isEmpty())
                        {
                            errors.add("0|tests");
                            throw new EnterpriseNTException(errors);
                        } else
                        {
                            sample.setTests(testsReject);
                        }

                        // Variable que guarda los ids de los examenes contiene esa muestra
                        StringBuilder examenesOneretake = new StringBuilder();

                        testsReject.forEach((t) ->
                        {
                            if (t.getProfile() != null && t.getId() != null)
                            {
                                if (t.getProfile() == 0 && !examenesOneretake.toString().contains(t.getId().toString()))
                                {
                                    examenesOneretake.append(t.getId().toString()).append("|");
                                } else
                                {
                                    if (!examenesOneretake.toString().contains(t.getProfile().toString()))
                                    {
                                        examenesOneretake.append(t.getProfile().toString()).append("|");
                                    }
                                }
                            }
                            try
                            {
                                audit.add(new AuditOperation(order.getOrderNumber(), t.getId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(type), null, null, null, null));
                            } catch (JsonProcessingException ex)
                            {
                                Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });

                        // Notificacion al HIS de que la muestra esta en retoma en el LIS
                        serviceIngreso.getMessageTest(idOrder, "retakes", sample.getId(), examenesOneretake.toString(), null);
                    }
                }

                if (!validateStateTracking(idOrder, sample.getId(), type, idBranch))
                {
                    return false;
                } else
                {
                    AuthorizedUser session;

                    //permite utilizar este metodo cuando no se tiene token
                    try
                    {
                        session = JWT.decode(request);
                        idBranch = authorizedUser.getBranch();

                    } catch (Exception e)
                    {
                        session = new AuthorizedUser(1);///user lismanager
                        idBranch = order.getBranch().getId();
                        session.setBranch(idBranch);
                    }
                    final Integer idBranchSession = idBranch;

                    Date currentDate = new Date();
                    List<Test> listaudittest = new ArrayList<>();
                    if (type == LISEnum.ResultSampleState.CHECKED.getValue())
                    {
                        sample.setTests(addPanels(sample.getTests(), idOrder));
                    }

                    if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
                    {
                        List<String> listholiday = holidayService.listBasic();
                        dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), type, reason, null, listholiday);
                    } else
                    {
                        dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), type, reason, null, new ArrayList<>());
                    }
                    //Cambio de estado de la muestra

                    if (type != LISEnum.ResultSampleState.ORDERED.getValue())
                    {
                        for (Test test : listaudittest)
                        {
                            if (test.getProfile() != null && test.getId() != null)
                            {
                                if (test.getProfile() == 0 && !examenesOne.toString().contains(test.getId().toString()))
                                {
                                    examenesOne.append(test.getId().toString()).append("|");
                                } else
                                {
                                    if (!examenesOne.toString().contains(test.getProfile().toString()))
                                    {
                                        examenesOne.append(test.getProfile().toString()).append("|");
                                    }
                                }
                            }

                            audit.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(type), null, null, null, null));
                        }
                    }

                    if (type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue() && retake)
                    {
                        dao.updateDateRetakes(order.getOrderNumber(), sample.getTests(), session, sample.getId());
                        List<String> samplesRetake = new ArrayList<>();
                        samplesRetake.addAll(Arrays.asList(configurationServices.get("MuestrasNotificacionObligatoriaTres").getValue().split(",")));
                        Integer notifyRetake = samplesRetake.stream().filter(string -> (!string.isEmpty())).map((string) -> Integer.parseInt(string.replace("[", "").replace("]", ""))).filter(s -> s.equals(sample.getId())).findAny().orElse(-1);
                        if (notifyRetake != -1)
                        {
                            for (int i = 0; i < reason.getTests().size(); i++)
                            {
                                if (reason.getTests().get(i).getProfileId() > 0)
                                {
                                    int profile = reason.getTests().get(i).getProfileId();
                                    Test test = reason.getTests().stream().filter(t -> t.getId() == profile).findFirst().orElse(null);
                                    if (test == null)
                                    {
                                        reason.getTests().get(i).setProfileId(0);
                                    }
                                }
                            }

                            List<Test> testsReject = reason.getTests().stream().filter(t -> t.getProfileId() == 0 || t.getTestType() > 0).collect(Collectors.toList());
                            if (testsReject.isEmpty())
                            {
                                errors.add("0|tests");
                                throw new EnterpriseNTException(errors);
                            } else
                            {
                                resultService.validsendMailRetake(testsReject, idOrder, authorizedUser.getId(), reason, sample);
                            }
                        }
                    }

                    addSampleWidget(type, sample, idBranchSession, order.getOrderNumber());
                    if (type == LISEnum.ResultSampleState.ORDERED.getValue())
                    {
                        dao.insertSampleOrdered(order.getOrderNumber(), session.getId(), idBranchSession, sample.getId(), type, currentDate);
                    } else
                    {

                        CompletableFuture.runAsync(()
                                ->
                        {
                            try
                            {
                                String tests = "";
                                // Obtenemos todos los examenes de la muestra y si estos examenes pertenecen a algun perfil,
                                // el perfil tambien debera enviarse
                                for (Test test : sample.getTests())
                                {
                                    if (test.getProfile() != null && test.getId() != null)
                                    {
                                        if (test.getProfile() == 0 && !tests.contains(test.getId().toString()))
                                        {
                                            tests += test.getId().toString() + ",";
                                        } else
                                        {
                                            if (!tests.contains(test.getProfile().toString()))
                                            {
                                                tests += test.getProfile().toString() + ",";
                                            }
                                            if (!tests.contains(test.getId().toString()))
                                            {
                                                tests += test.getId().toString() + ",";
                                            }
                                        }
                                    }
                                }
                                // Variable que ira al proceso asincrono:
                                String testsOne = tests;
                                statisticService.updateSampleStatus(type, idOrder, testsOne);
                            } catch (Exception ex)
                            {
                                Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                    statisticService.sampleStateChanged(type, idOrder, sample.getId());
                    audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(type), reason == null ? null : reason.getMotive().getId(), reason == null ? null : reason.getComment(), null, null));
                    trackingService.registerOperationTracking(audit);

                    if (type == LISEnum.ResultSampleState.CHECKED.getValue())
                    {
                        if (order.getService().getHospitalSampling())
                        {
                            try
                            {
                                String url = configurationServices.get("UrlHomebound").getValue();
                                integrationHomeBoundService.verificSample(order.getOrderNumber(), sample.getId(), url);
                            } catch (Exception ex)
                            {
                                Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        CompletableFuture.runAsync(()
                                ->
                        {

                            integrationMiddlewareService.sendOrderASTM(idOrder, null, sample.getId().toString(), Constants.CHECK, null, null, null, idBranchSession, false);

                        });
                        if (Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("verificarMuestra").getValue()) == true)
                        {
                            CompletableFuture.runAsync(()
                                    ->
                            {
                                eventsSampleTrackingService.sampleVerify(idOrder, codeSample);
                            });
                        }

                        if (!order.getExternalId().isEmpty() && !order.getExternalId().equals("0") && !Boolean.parseBoolean(configurationServices.get("ManejoTemperatura").getValue()))
                        {
                            try
                            {
                                ResponsSonControl objControl = new ResponsSonControl();
                                objControl = integrationIngresoDao.ordersSonControl(idOrder);
                                IntegrationHisLog.info("Estado : " + objControl.getEstado());
                                if (objControl != null && objControl.getEstado() < 3)
                                {
                                    CompletableFuture.runAsync(() ->
                                    {
                                        IntegrationHisLog.info("Relizar envio al HIS orden : " + idOrder + "examenes" + examenesOne.toString());
                                        try
                                        {
                                            //Check-IN Segment
                                            serviceIngreso.getMessageTest(idOrder, "check", sample.getId(), examenesOne.toString(), null);
                                        } catch (Exception ex)
                                        {
                                            IntegrationHisLog.error(ex);
                                        }
                                    });
                                }
                            } catch (Exception ex)
                            {
                                IntegrationHisLog.error("ERROR : " + ex);
                            }
                        }

                        CompletableFuture.runAsync(()
                                ->
                        {
                            dashBoardService.dashBoardOpportunityTime(idOrder, sample.getTests().stream().map(test -> test.getId()).collect(Collectors.toList()), DashBoardOpportunityTime.ACTION_UPDATE);
                        });

                        orderService.updateOrderhistory(idOrder, sample.getId(), order.getPatient().getId());
                    } else if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
                    {
                        if (Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("tomarMuestra").getValue()) == true)
                        {
                            CompletableFuture.runAsync(()
                                    ->
                            {
                                eventsSampleTrackingService.sampleTracking(idOrder, codeSample);
                            });
                        }

                        // Enviar a Tablero toma de muestra hospitalaria:
                        CompletableFuture.runAsync(()
                                ->
                        {
                            if (order.getService().getHospitalSampling())
                            {
                                dashBoardService.dashBoardHospitalSampling(idOrder, sample.getId(), sample.getTests().stream().map(test -> test.getId()).collect(Collectors.toList()), DashBoardOpportunityTime.ACTION_UPDATE_HOSPITAL_SAMPLING);
                            }
                        });
                    } else if (type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
                    {
                        if (order.getService().getHospitalSampling())
                        {
                            try
                            {
                                integrationHomeBoundService.retakeSample(order.getOrderNumber(), sample.getId());
                            } catch (Exception ex)
                            {
                                Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }

                    // Eliminar datos del Tablero toma de muestra hospitalaria:
                    CompletableFuture.runAsync(()
                            ->
                    {
                        if (order.getService().getHospitalSampling())
                        {
                            dashBoardService.dashBoardHospitalSampling(idOrder, sample.getId(), sample.getTests().stream().map(test -> test.getId()).collect(Collectors.toList()), DashBoardOpportunityTime.ACTION_DELETE_HOSPITAL_SAMPLING);
                        }
                    });

                }

                return true;
            }
        } else
        {
            throw new EnterpriseNTException(errors);
        }
        return false;
    }

    @Override
    public List<AuditOperation> getTrackingSample(Long idOrder, Integer sample) throws Exception
    {
        return trackingDao.getTrackingOrderSample(idOrder, sample);
    }

    @Override
    public boolean sampleTracking(long idOrder, String codeSample, int type, AuthorizedUser session) throws Exception
    {
        List<AuditOperation> audit = new ArrayList<>();
        boolean takeSample = daoConfig.get("TomaMuestra").getValue().toLowerCase().equals("true");
        if (!takeSample && type == LISEnum.ResultSampleState.CHECKED.getValue())
        {
            sampleTracking(idOrder, codeSample, LISEnum.ResultSampleState.COLLECTED.getValue(), session);
        }

        Order order = getOrder(idOrder, false, session);
        Sample sample = order == null ? null : order.getSamples().stream().filter(s -> s.getCodesample().equals(codeSample)).findAny().orElse(null);
        List<String> errors = validateFields(order);

        if (sample == null || sample.getId() == null)
        {
            errors.add("1|sample");
        }

        if (errors.isEmpty())
        {

            Date currentDate = new Date();
            if (type == LISEnum.ResultSampleState.CHECKED.getValue())
            {
                sample.setTests(addPanels(sample.getTests(), idOrder));
            }

            if (session.getBranch() != 0)
            {
                session.setBranch(order.getBranch().getId());
            }

            int branchTemp = session.getBranch() != 0 ? session.getBranch() : order.getBranch().getId();

            if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
            {
                List<String> listholiday = holidayService.listBasic();
                SampleTracking tracking = dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), type, null, null, listholiday);
            } else
            {
                //cambio de estado de la muestra
                SampleTracking tracking = dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), type, null, null, new ArrayList<>());
            }
            for (Test test : sample.getTests())
            {
                audit.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(type), null, null, null, null));
            }

            if (type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue() && false)
            {
                dao.updateDateRetakes(order.getOrderNumber(), sample.getTests(), session, sample.getId());
            }

            addSampleWidget(type, sample, branchTemp, order.getOrderNumber());
            if (type == LISEnum.ResultSampleState.ORDERED.getValue())
            {
                dao.insertSampleOrdered(order.getOrderNumber(), session.getId(), branchTemp, sample.getId(), type, currentDate);
            } else
            {
                statisticService.sampleStateChanged(type, idOrder, sample.getId());
                CompletableFuture.runAsync(()
                        ->
                {
                    try
                    {
                        String examenesOne = "";
                        // Obtenemos todos los examenes de la muestra y si estos examenes pertenecen a algun perfil,
                        // el perfil tambien debera enviarse
                        for (Test test : sample.getTests())
                        {
                            if (test.getProfile() != null && test.getId() != null)
                            {
                                if (test.getProfile() == 0 && !examenesOne.contains(test.getId().toString()))
                                {
                                    examenesOne += test.getId().toString() + ",";
                                } else
                                {
                                    if (!examenesOne.contains(test.getProfile().toString()))
                                    {
                                        examenesOne += test.getProfile().toString() + ",";
                                    }
                                    if (!examenesOne.contains(test.getId().toString()))
                                    {
                                        examenesOne += test.getId().toString() + ",";
                                    }
                                }
                            }
                        }
                        // Variable que ira al proceso asincrono:
                        String examenes = examenesOne;
                        statisticService.updateSampleStatus(type, idOrder, examenes);
                    } catch (Exception ex)
                    {
                        Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }

            audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(type), null, null, null, null));
            trackingService.registerOperationTracking(audit, session.getId());
            if (type == LISEnum.ResultSampleState.CHECKED.getValue())
            {
                if (order.getService().getHospitalSampling())
                {
                    String url = configurationServices.get("UrlHomebound").getValue();
                    integrationHomeBoundService.verificSample(order.getOrderNumber(), sample.getId(), url);
                }

                if (Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("verificarMuestra").getValue()) == true)
                {
                    CompletableFuture.runAsync(()
                            ->
                    {
                        eventsSampleTrackingService.sampleVerify(idOrder, codeSample);
                    });
                }
            } else if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
            {
                if (Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("tomarMuestra").getValue()) == true)
                {
                    CompletableFuture.runAsync(()
                            ->
                    {
                        eventsSampleTrackingService.sampleTracking(idOrder, codeSample);
                    });
                }
            } else if (type == LISEnum.ResultSampleState.REJECTED.getValue())
            {
                String examenesOne = "";
                // Obtenemos todos los examenes de la muestra y si estos examenes pertenecen a algun perfil,
                // el perfil tambien debera enviarse
                for (Test test : sample.getTests())
                {
                    if (test.getProfile() != null && test.getId() != null)
                    {
                        if (test.getProfile() == 0 && !examenesOne.contains(test.getId().toString()))
                        {
                            examenesOne += test.getId().toString() + ",";
                        } else
                        {
                            if (!examenesOne.contains(test.getProfile().toString()))
                            {
                                examenesOne += test.getProfile().toString() + ",";
                            }
                            if (!examenesOne.contains(test.getId().toString()))
                            {
                                examenesOne += test.getId().toString() + ",";
                            }
                        }
                    }
                }
                // Variable que ira al proceso asincrono:
                String examenes = examenesOne;
                if (!order.getExternalId().isEmpty() && !order.getExternalId().equals("0"))
                {
                    //Check-IN Segment
                    serviceIngreso.getMessageTest(idOrder, "rejection", sample.getId(), examenes, null);
                }

                if (order.getService().getHospitalSampling())
                {
                    try
                    {
                        integrationHomeBoundService.retakeSample(order.getOrderNumber(), sample.getId());
                    } catch (Exception ex)
                    {
                        Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return true;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public boolean sampleCheckRetakeTracking(long idOrder, String codeSample) throws Exception
    {
        try
        {
            AuthorizedUser session = JWT.decode(request);
            Order order = getOrder(idOrder, false);
            Sample sample = order == null ? null : order.getSamples().stream().filter(s -> s.getCodesample().equals(codeSample)).findAny().orElse(null);
            dao.updateCheckRetake(idOrder, sample.getId(), session, LISEnum.ResultSampleState.CHECKED.getValue());

            //cambio de estado de la muetra.
            dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), LISEnum.ResultSampleState.CHECKED.getValue(), null, null, new ArrayList<>());

            List<AuditOperation> audit = new ArrayList<>();
            audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(LISEnum.ResultSampleState.CHECKED.getValue()), null, null, null, null));

            // Variable que guarda los ids de los examenes contiene esa muestra
            StringBuilder examenesOneretake = new StringBuilder();

            sample.getTests().forEach((t) ->
            {
                if (t.getResult().getSampleState() == 1)
                {
                    try
                    {
                        audit.add(new AuditOperation(order.getOrderNumber(), t.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(LISEnum.ResultSampleState.CHECKED.getValue()), null, null, null, null));
                    } catch (JsonProcessingException ex)
                    {
                        Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    examenesOneretake.append(t.getId().toString()).append("|");
                }
            });

            trackingService.registerOperationTracking(audit, session.getId());
            // Notificacion al HIS de que la muestra esta en retoma en el LIS
            serviceIngreso.getMessageTest(idOrder, "check", sample.getId(), examenesOneretake.toString(), null);

            if (order.getService().getHospitalSampling())
            {
                String url = configurationServices.get("UrlHomebound").getValue();
                integrationHomeBoundService.verificSample(order.getOrderNumber(), sample.getId(), url);
            }
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public boolean sampleOrdered(Long idOrder, AuthorizedUser session, int type, List<Integer> tests) throws Exception
    {
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        
        Order order = dao.getOrder(idOrder,laboratorys, idbranch );
        
        if (tests != null)
        {
            List<Test> newTests = order.getTests().stream()
                    .filter(t -> tests.contains(t.getId()))
                    .collect(Collectors.toList());
            order.setTests(newTests);
        }

        List<AuditOperation> audit = new ArrayList<>();
        dao.sampleTrackingDefault(order.getOrderNumber(), order.getTests(), session, LISEnum.ResultSampleState.COLLECTED.getValue());
        dao.sampleTrackingDefault(order.getOrderNumber(), order.getTests(), session, LISEnum.ResultSampleState.CHECKED.getValue());

        Date currentDate = new Date();

        List<Sample> samples = order.getTests().stream().filter(test -> test.getSample().getId() != null).map(test -> test.getSample()).distinct().collect(Collectors.toList());
        for (Sample sample : samples)
        {
            dao.insertSampleTracking(idOrder, session.getId(), order.getBranch().getId(), null, sample.getId(), LISEnum.ResultSampleState.COLLECTED.getValue(), order.getTests(), currentDate);
            dao.insertSampleTracking(idOrder, session.getId(), order.getBranch().getId(), null, sample.getId(), LISEnum.ResultSampleState.CHECKED.getValue(), order.getTests(), currentDate);

            audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(LISEnum.ResultSampleState.ORDERED.getValue()), null, null, null, null));
            audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(LISEnum.ResultSampleState.COLLECTED.getValue()), null, null, null, null));
            audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(LISEnum.ResultSampleState.CHECKED.getValue()), null, null, null, null));
            addSampleWidget(LISEnum.ResultSampleState.CHECKED.getValue(), sample, session.getBranch(), order.getOrderNumber());
            statisticService.sampleStateChanged(type, idOrder, sample.getId());
        }

        for (Test test : order.getTests())
        {
            audit.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(LISEnum.ResultSampleState.COLLECTED.getValue()), null, null, null, null));
            audit.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(LISEnum.ResultSampleState.CHECKED.getValue()), null, null, null, null));
        }
        trackingService.registerOperationTracking(audit);
        return true;
    }

    @Override
    public boolean sampleOrdered(Long idOrder, AuthorizedUser session, int type, int origin, List<Integer> tests) throws Exception
    {
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        Order order = dao.getOrder(idOrder, laboratorys, idbranch);
        if (tests != null)
        {
            List<Test> newTests = order.getTests().stream()
                    .filter(t -> tests.contains(t.getId()))
                    .collect(Collectors.toList());
            order.setTests(newTests);
        }
        List<Sample> samples = order.getTests().stream().filter(test -> test.getTestType() == 0).map(test -> test.getSample()).distinct().collect(Collectors.toList());

        samples.stream().forEach(sample
                ->
        {
            sample.setTests(getTestsSample(order.getTests(), sample, order.getOrderNumber()));
        });

        for (Sample sample : samples)
        {
            List<AuditOperation> audit = new ArrayList<>();
            SampleState sampleState = dao.getSampleState(sample.getId(), order.getOrderNumber(), session.getBranch());
            if (sampleState == null)
            {
                if (type == LISEnum.ResultSampleState.ORDERED.getValue() && sample.isCheck())
                {
                    sampleTracking(order.getOrderNumber(), sample.getCodesample(), LISEnum.ResultSampleState.ORDERED.getValue(), null, false, false);
                } else
                {
                    boolean check = configurationServices.get("Trazabilidad").getValue().equals("1");
                    //ENTRA  VERIFICAR ORDERN
                    // sampleTracking(order.getOrderNumber(), sample.getCodesample(), LISEnum.ResultSampleState.ORDERED.getValue(), null, false);
//                    addSampleWidget(LISEnum.ResultSampleState.ORDERED.getValue(), sample, session.getBranch(), order.getOrderNumber());
                    if ((check && origin == LISEnum.OriginModule.ORIGIN.getValue()) || (origin == LISEnum.OriginModule.ORIGIN.getValue() && !sample.isCheck() && !check) || (origin == LISEnum.OriginModule.RESULT.getValue() && Boolean.parseBoolean(configurationServices.get("AgregarExamenesMuestras").getValue())))
                    {
                        Integer idBranch;
                        
                        //permite utilizar este metodo cuando no se tiene token
                        try
                        {
                            idBranch = session.getBranch();

                        } catch (Exception e)
                        {
                            session = new AuthorizedUser(1);///user lismanager
                            idBranch = order.getBranch().getId();
                            session.setBranch(idBranch);
                        }
                        
                        final Integer idBranchSession = idBranch;

                        Date currentDate = new Date();
                    
                        dao.insertSampleOrdered(order.getOrderNumber(), session.getId(), idBranchSession, sample.getId(), type, currentDate);
                        
                        List<String> listholiday = holidayService.listBasic();
                        dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), LISEnum.ResultSampleState.COLLECTED.getValue(), null, null, listholiday);
                        audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(LISEnum.ResultSampleState.COLLECTED.getValue()), null, null, null, null));
                        sample.setTests(addPanels(sample.getTests(), order.getOrderNumber()));

                        dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), LISEnum.ResultSampleState.CHECKED.getValue(), null, null, new ArrayList<>());
                        audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(LISEnum.ResultSampleState.CHECKED.getValue()), null, null, null, null));
                        addSampleWidget(LISEnum.ResultSampleState.CHECKED.getValue(), sample, session.getBranch(), order.getOrderNumber());
                        statisticService.sampleStateChanged(type, idOrder, sample.getId());
                                
                        AssignmentDestination object = dao.getDestinationRoute(order.getOrderNumber(), order.getType().getId() , idBranchSession, sample.getId());
                        
                        if (object == null)
                        {
                            object = dao.getDestinationRoute(order.getOrderNumber(), 0, idBranchSession, sample.getId());
                        }
                        
                        if(object != null) {
                           DestinationRoute initialDestination = object.getDestinationRoutes().stream().filter(destination -> destination.getDestination().getType().getId() == ListEnum.DestinationType.INITIAL.getValue()).findFirst().orElse(null);
                        
                            if(initialDestination != null) {     
                                dao.verifyDestination(order.getOrderNumber(), initialDestination.getId(), session.getId(), idBranchSession, sample.getId(), object.getId(), initialDestination.getDestination());
                            } 
                        }

                        for (Test test : sample.getTests())
                        {
                            audit.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(LISEnum.ResultSampleState.COLLECTED.getValue()), null, null, null, null));
                            audit.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(LISEnum.ResultSampleState.CHECKED.getValue()), null, null, null, null));
                        }
                    }
                }
            } else
            {
                if (type != LISEnum.ResultSampleState.ORDERED.getValue() && sampleState.getState() != LISEnum.ResultSampleState.CHECKED.getValue())
                {
                    dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), LISEnum.ResultSampleState.COLLECTED.getValue(), null, null, new ArrayList<>());
                    sample.setTests(addPanels(sample.getTests(), order.getOrderNumber()));

                    dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), LISEnum.ResultSampleState.CHECKED.getValue(), null, null, new ArrayList<>());
                    if (sampleState.getState() == LISEnum.ResultSampleState.ORDERED.getValue())
                    {
                        audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(LISEnum.ResultSampleState.COLLECTED.getValue()), null, null, null, null));
                        audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(LISEnum.ResultSampleState.CHECKED.getValue()), null, null, null, null));

                        for (Test test : sample.getTests())
                        {
                            audit.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(LISEnum.ResultSampleState.COLLECTED.getValue()), null, null, null, null));
                            audit.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(LISEnum.ResultSampleState.CHECKED.getValue()), null, null, null, null));
                        }
                    } else if (sampleState.getState() == LISEnum.ResultSampleState.COLLECTED.getValue())
                    {
                        audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(LISEnum.ResultSampleState.CHECKED.getValue()), null, null, null, null));
                        for (Test test : sample.getTests())
                        {
                            audit.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(LISEnum.ResultSampleState.CHECKED.getValue()), null, null, null, null));
                        }
                    }
                    addSampleWidget(LISEnum.ResultSampleState.CHECKED.getValue(), sample, session.getBranch(), order.getOrderNumber());
                } else if ((sample.isCheck() && origin == LISEnum.OriginModule.RESULT.getValue()))
                {
                    sample.setTests(addPanels(sample.getTests(), order.getOrderNumber()));

                    dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), LISEnum.ResultSampleState.CHECKED.getValue(), null, null, new ArrayList<>());
                    audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(LISEnum.ResultSampleState.CHECKED.getValue()), null, null, null, null));
                    for (Test test : sample.getTests())
                    {
                        audit.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(LISEnum.ResultSampleState.CHECKED.getValue()), null, null, null, null));
                    }
                } else if (sampleState.getState().equals(LISEnum.ResultSampleState.CHECKED.getValue()))
                {
                    sample.setTests(addPanels(sample.getTests(), order.getOrderNumber()));
                    dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), LISEnum.ResultSampleState.CHECKED.getValue(), null, null, new ArrayList<>());
                    audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(LISEnum.ResultSampleState.CHECKED.getValue()), null, null, null, null));
                    for (Test test : sample.getTests())
                    {
                        audit.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(LISEnum.ResultSampleState.CHECKED.getValue()), null, null, null, null));
                    }
                }
            }
            trackingService.registerOperationTracking(audit);
        }
        return true;
    }

    private List<Test> addPanels(List<Test> test, Long idOrder)
    {
        List<Test> panels = test.stream()
                .filter(t -> t.getPanel() != null && t.getPanel().getId() != null)
                .map(t -> t.getPanel())
                .distinct()
                .collect(Collectors.toList());
        //validar que estos perfiles no tengan previamente la fecha de validacion
        if (panels != null)
        {
            panels.stream()
                    .filter(p -> p.getId() != null).forEach(p
                    ->
            {
                try
                {
                    Result result = resultTestDao.getInformation(idOrder, p.getId()); //resultsService.getInformation(idOrder, p.getId());
                    if (result.getDateSample() == null)
                    {
                        test.add(p);
                    }
                } catch (Exception ex)
                {
                    Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        return test;
    }

    /**
     * VAlidacion de la orden a verificar
     *
     * @param order
     *
     * @return
     * @throws Exception
     */
    private List<String> validateFields(Order order) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (order == null || order.getOrderNumber() == null)
        {
            errors.add("0|order");
            return errors;
        }

        return errors;
    }

    /**
     * Realiza validaciónes para cambio de estado de la muestra
     *
     * @param idOrder id orden
     * @param idSample id muestra
     * @param type nuevo estado de la muestra
     *
     * @return
     * @throws Exception
     */
    private boolean validateStateTracking(long idOrder, int idSample, int type, Integer idBranch) throws Exception
    {
        // AuthorizedUser authorizedUser = JWT.decode(request);
        SampleState sampleState = dao.getSampleState(idSample, idOrder, idBranch);
        List<String> errors = new ArrayList<>();

        if (type == LISEnum.ResultSampleState.ORDERED.getValue())
        {
            if (sampleState != null && sampleState.getState() != null)
            {
                errors.add("1");
            }
        } else if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
        {
            if (sampleState == null)
            {
                errors.add("4");
                throw new EnterpriseNTException(errors);
            }

            if (sampleState.getState() == LISEnum.ResultSampleState.ORDERED.getValue())
            {
            } else if (sampleState.getState() == LISEnum.ResultSampleState.COLLECTED.getValue())
            {
                errors.add("1");
            } else if (sampleState.getState() == LISEnum.ResultSampleState.REJECTED.getValue() || sampleState.getState() == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
            {
                errors.add("3");
            }
        } else if (type == LISEnum.ResultSampleState.CHECKED.getValue())
        {
            if (sampleState == null)
            {
                errors.add("4");
                throw new EnterpriseNTException(errors);
            }

            if (sampleState.getState() == LISEnum.ResultSampleState.COLLECTED.getValue())
            {
            } else if (sampleState.getState() == LISEnum.ResultSampleState.ORDERED.getValue())
            {
                errors.add("1");
            } else if (sampleState.getState() == LISEnum.ResultSampleState.CHECKED.getValue())
            {
                //errors.add("2");
            } else if (sampleState.getState() == LISEnum.ResultSampleState.REJECTED.getValue() || sampleState.getState() == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
            {
                errors.add("3");
            } else
            {
                errors.add("4");
            }
        } else if (type == LISEnum.ResultSampleState.REJECTED.getValue() || type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
        {
            if (sampleState == null)
            {
                errors.add("4");
                throw new EnterpriseNTException(errors);
            }

            if (sampleState.getState() == LISEnum.ResultSampleState.COLLECTED.getValue() || sampleState.getState() == LISEnum.ResultSampleState.CHECKED.getValue())
            {
            } else if (sampleState.getState() == LISEnum.ResultSampleState.ORDERED.getValue())
            {
                errors.add("1");
            } else if (sampleState.getState() == LISEnum.ResultSampleState.REJECTED.getValue())
            {
                errors.add("2");
            } else if (sampleState.getState() == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
            {
                errors.add("3");
            } else
            {
                errors.add("4");
            }
        } else
        {
            return false;
        }

        if (errors.isEmpty())
        {
            return true;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public boolean verifyDestination(VerifyDestination verifyDestination) throws Exception
    {
        Order order = getOrder(verifyDestination.getOrder(), false);

        AuthorizedUser session = JWT.decode(request);
        verifyDestination.setBranch(session.getBranch());
        verifyDestination.setAssigmentDestination(getDestinationRoute(verifyDestination.getOrder(), verifyDestination.getSample()).getId());

        List<String> errors = validateFields(verifyDestination, null);
        Sample sample = order.getSamples().stream().filter(s -> s.getCodesample().equals(verifyDestination.getSample())).findAny().orElse(null);

        if (errors.isEmpty())
        {

            Destination destinationF = destinationDao.getByRoute(verifyDestination.getDestination());
            dao.verifyDestination(verifyDestination.getOrder(), verifyDestination.getDestination(), session.getId(), session.getBranch(), sample.getId(), verifyDestination.getAssigmentDestination(), destinationF);
            List<AuditOperation> audit = new ArrayList<>();
            audit.add(new AuditOperation(verifyDestination.getOrder(), sample.getId(), destinationF.getId(), AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, LISEnum.ResultSampleState.CHECKED.getValue() + "", null, null, null, null));
            //envia al middlera
            trackingService.registerOperationTracking(audit);
            return true;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public AssignmentDestination getDestinationRoute(long idOrder, String codeSample) throws Exception
    {
        AuthorizedUser session = JWT.decode(request);
        AssignmentDestination assignment = null;
        Order order = getOrder(idOrder, false);
        List<String> errors = validateFields(order);
        Sample sample = order == null ? null : order.getSamples().stream().filter(s -> s.getCodesample().equals(codeSample)).findAny().orElse(null);

        if (sample == null)
        {
            errors.add("0|sample");
        }

        if (errors.isEmpty())
        {
            List<VerifyDestination> destinations = dao.getRouteByOrder(idOrder, sample.getId(), session.getBranch());
            if (destinations.isEmpty())
            {
                assignment = dao.getDestinationRoute(idOrder, order.getType().getId(), session.getBranch(), sample.getId());
                if (assignment == null)
                {
                    assignment = dao.getDestinationRoute(idOrder, 0, session.getBranch(), sample.getId());
                }
            } else
            {
                assignment = dao.getDestinationRouteAssigment(idOrder, sample.getId(), destinations.get(0).getAssigmentDestination());
            }

            if (assignment == null)
            {
                return null;
            } else
            {
                List<DestinationRoute> destinationRoutes = assignment.getDestinationRoutes().stream()
                        .filter(destination -> getTestDestination(destination.getTests(), sample))
                        .map(destination -> destination.setTests(getResultTests(destination.getTests(), sample)))
                        .collect(Collectors.toList());

                assignment.setDestinationRoutes(destinationRoutes);
            }

            assignment.setService(order.getService());
            String answerTemperature = configurationService.get("ManejoTemperatura").getValue();

            if ("True".equals(answerTemperature))
            {
                Sample sampleTemperature;
                sampleTemperature = dao.getTemperatures(sample.getId());
                assignment.getSample().setMinimumTemperature(sampleTemperature.getMinimumTemperature());
                assignment.getSample().setMaximumTemperature(sampleTemperature.getMaximumTemperature());
            }
        } else
        {
            throw new EnterpriseNTException(errors);
        }

        return assignment;
    }

    /**
     * Función para validar la verificacion de la muestra por destino. - El
     * usuario analizador servira para identicar de que maner obtendre la orden
     *
     * @param verify Clase con la informacion para verificar la muestra en el
     * destino.
     * @param user Usuario analizador
     * @return
     * @throws Exception
     */
    private List<String> validateFields(VerifyDestination verify, User userAnalyzer) throws Exception
    {
        List<String> errors = new ArrayList<>();
        Order order = new Order();
        if (verify.getOrder() == null || verify.getOrder() == 0)
        {
            errors.add("0|order");
            return errors;
        }

        if (verify.getBranch() == 0)
        {
            errors.add("0|branch");
        }

        if (verify.getDestination() == 0)
        {
            errors.add("0|destination");
        }

        // Valido que el usuario analizador sea nulo
        if (userAnalyzer == null)
        {
            order = getOrder(verify.getOrder(), true);
        } else
        {
            order = getOrderAnalyzer(verify.getOrder(), true, verify.getBranch(), userAnalyzer);
        }

        if (order == null)
        {
            errors.add("1|order");
            return errors;
        }

        if (order.getType().getId() == null || order.getType().getId() == 0)
        {
            errors.add("0|orderType");
            return errors;
        }

        Sample sample = order.getSamples().stream().filter(s -> s.getCodesample().equals(verify.getSample())).findAny().orElse(null);

        if (sample == null)
        {
            errors.add("1|sample");
            return errors;
        }

        /*if (sample.getSampleState() == null || (sample.getSampleState().getState() != LISEnum.ResultSampleState.CHECKED.getValue()))
        {
            errors.add("3|sample");
        }*/
        AssignmentDestination destination = dao.getDestinationRoute(order.getOrderNumber(), order.getType().getId(), verify.getBranch(), sample.getId());

        if (destination == null)
        {
            destination = dao.getDestinationRoute(order.getOrderNumber(), 0, verify.getBranch(), sample.getId());
        }

        if (destination == null)
        {
            errors.add("1|route");
            return errors;
        }

        /* if (!destination.getDestinationRoutes().stream().anyMatch(d -> d.getId().equals(verify.getDestination())))
        {
            errors.add("1|destination");
        }

        if (dao.verifyDestination(verify.getOrder(), verify.getDestination()))
        {
            errors.add("2|destination");
        }*/
        return errors;
    }

    @Override
    public List<Question> getInterview(long idOrder) throws Exception
    {
        Order order = getOrder(idOrder, false);
        List<String> errors = validateFields(order);
        if (errors.isEmpty())
        {
            String intervieworder = configurationService.get("OrdenEntrevistaPorPrioridad").getValue();
            return dao.getInterviewskl(order, intervieworder);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<Question> getInterviewskl(long idOrder) throws Exception
    {
        Order order = getOrder(idOrder, false);
        List<String> errors = validateFields(order);
        if (errors.isEmpty())
        {
            String intervieworder = configurationService.get("OrdenEntrevistaPorPrioridad").getValue();
            return dao.getInterviewskl(order, intervieworder);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public boolean getInterviewAnswer(long idOrder) throws Exception
    {
        Order order = getOrder(idOrder, false);
        List<String> errors = validateFields(order);
        if (errors.isEmpty())
        {
            return !dao.getInterview(order).stream()
                    .filter(question -> question.isOpen() && (question.getInterviewAnswer() != null && !question.getInterviewAnswer().isEmpty()))
                    .filter(question -> question.isOpen() || (!question.isOpen() && !question.getAnswers().stream().filter(answer -> answer.isSelected()).collect(Collectors.toList()).isEmpty()))
                    .collect(Collectors.toList()).isEmpty();
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public int insertResultInterview(List<Question> questions, long idOrder) throws Exception
    {
        AuthorizedUser session = JWT.decode(request);
        List<AuditOperation> audit = new ArrayList<>();
        int quantity = dao.insertResultInterview(questions, idOrder, session);
        audit.add(new AuditOperation(idOrder, null, null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_INTERVIEW, Tools.jsonObject(questions), null, null, null, null));
        trackingService.registerOperationTracking(audit);
        return quantity;
    }

    @Override
    public List<Sample> sampleOrder(long idOrder) throws Exception
    {
        AssignmentDestination assignment;
        Order order = getOrder(idOrder, true);
        List<Sample> samples = order == null ? new ArrayList<>() : order.getSamples();

        for (Sample sample : samples)
        {
            assignment = getDestinationRoute(idOrder, sample.getCodesample());

            sample.setQuantityDestination(assignment == null ? 0 : assignment.getDestinationRoutes().size());
            sample.setQuantityVerifyDestination(assignment == null ? 0 : assignment.getDestinationRoutes().stream().filter(destination -> destination.isVerify()).collect(Collectors.toList()).size());
            sample.setQualityFlag(getSampleQuality(sample));
        }
        return samples;
    }

    @Override
    public List<Sample> sampleOrderTracking(long idOrder) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        AssignmentDestination assignment;
        
        Order order = getOrderTracking(idOrder, true);
        List<Sample> samples = order == null ? new ArrayList<>() : order.getSamples();

        for (Sample sample : samples)
        {
            assignment = getDestinationRoute(idOrder, sample.getCodesample());
            sample.setQuantityDestination(assignment == null ? 0 : assignment.getDestinationRoutes().size());
            sample.setQuantityVerifyDestination(assignment == null ? 0 : assignment.getDestinationRoutes().stream().filter(destination -> destination.isVerify()).collect(Collectors.toList()).size());
            sample.setQualityFlag(getSampleQuality(sample));

            boolean handlesTemperature = configurationService.get("ManejoTemperatura").getValue().equals("True");
            if (handlesTemperature)
            {
                Double temperature = dao.getTemperature(idOrder, sample.getId(), user.getBranch());
                sample.setTemperature(temperature);
            }
        }
        return samples;
    }

    /**
     * Flag para identificar la calidad de la uestra respecto a la toma
     *
     * @param sample muestra a evaluar
     *
     * @return 1 - Si el tiempo transcurrido no ha sobrepasadoel porcentaje de
     * alarma 2 - Si el tiempo se encuentra entre el porcentaje de alarma y la
     * caducidad de la muestra 3 - Si se sobrepasa el tiempo en que caduca la
     * muestra
     */
    public int getSampleQuality(Sample sample)
    {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = sample.getSampleTrackings()
                .stream()
                .filter(taken -> taken.getState() == LISEnum.ResultSampleState.COLLECTED.getValue())
                .map(taken -> taken.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .findAny()
                .orElse(null);
        long elapsedMinutes = start == null ? 0 : Duration.between(start, end).toMinutes();
        Long validMinutes = sample.getQualityTime() == null ? 0 : sample.getQualityTime();
        long alarmMinutes = sample.getQualityPercentage() == null ? 0 : sample.getQualityPercentage() * validMinutes / 100;

        if (sample.getQualityTime() == null || elapsedMinutes < alarmMinutes)
        {
            return 1;
        } else if (elapsedMinutes >= alarmMinutes && elapsedMinutes < validMinutes)
        {
            return 2;
        } else //if (elapsedMinutes >= validMinutes)
        {
            return 3;
        }

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
        try
        {
            sample.setSampleTrackings(dao.getSampleTracking(sample.getId(), idOrder));

            List<Test> testsFilter = tests.stream().filter(test -> test.getSample() != null && test.getSample().getId() != null && test.getSample().getId().equals(sample.getId())).collect(Collectors.toList());
            SampleTracking tracking = sample.getSampleTrackings().stream().filter(s -> s.getState() == LISEnum.ResultSampleState.CHECKED.getValue()).findAny().orElse(null);
            testsFilter.forEach(test
                    ->
            {
                test.getResult().setDateSample(tracking == null ? null : tracking.getDate());
                test.getResult().setUserSample(tracking == null ? null : tracking.getUser());
                test.setSample(null);
            });
            sample.setSampleTrackings(null);
            return testsFilter;
        } catch (Exception e)
        {
            OrderCreationLog.error(e);
            return new ArrayList<>();
        }

    }

    @Override
    public List<SampleDelayed> sampleDelayed(int idDestination) throws Exception
    {
        AuthorizedUser session = JWT.decode(request);
        List<SampleDelayed> samples = dao.sampleDelayed(idDestination, session.getBranch());
        return samples.stream().filter(sample -> getSampleDelayed(sample) && sample.getDestination().getType().getId() != 48).collect(Collectors.toList());
    }

    /**
     * Valida si una muestra se encuentra en retrazo
     *
     * @param sampleDelayed información de la muestra
     *
     * @return
     */
    private boolean getSampleDelayed(SampleDelayed sampleDelayed)
    {
        LocalDateTime expectedDate = sampleDelayed.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        expectedDate = expectedDate.plus(Duration.ofMinutes(sampleDelayed.getExpectedTime()));

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(expectedDate, now);
        sampleDelayed.setTotalTime(duration.toMinutes());

        return expectedDate.isBefore(now);
    }

    /**
     * Verifica si los examenes de la muestra estan en la lista enviada(examenes
     * del destino)
     *
     * @param tests lista examenes del destino
     * @param sample muestra
     *
     * @return true si encuentra el examen o el destino no tiene
     * examenes(inicial,control y final )
     */
    private boolean getTestDestination(List<TestBasic> tests, Sample sample)
    {
        List<Integer> testsId = sample.getTests().stream().map(test -> test.getId()).collect(Collectors.toList());
        return tests.isEmpty() || tests.stream().anyMatch(test -> testsId.contains(test.getId()));
    }

    /**
     * Filtra examenes de acuerdo a las opciones enviadas
     *
     * @param filter Orden con examenes a filtrar
     * @param search lista de examenes filtrados
     *
     * @return Lista de examenes.
     */
    private List<TestBasic> getResultTests(List<TestBasic> tests, Sample sample)
    {
        try
        {
            for (TestBasic test : tests)
            {
                int index = sample.getTests().indexOf(new Test(test.getId()));
                if (index != -1)
                {
                    test.setResult(sample.getTests().get(index).getResult());
                } else
                {
                    test.setResult(null);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return tests;
    }

    /**
     * Agrega muestras a los registros relacionados con los widgets.
     *
     * @param type Tipo de registro.
     * @param sample Muestra.
     * @param idBranch Id sede.
     * @param idOrder Id orden.
     */
    private void addSampleWidget(int type, Sample sample, int idBranch, long idOrder)
    {
        try
        {
            if (type == LISEnum.ResultSampleState.ORDERED.getValue())
            {
                widgetService.addOrderedWidget(idBranch);
            } else if (type == LISEnum.ResultSampleState.CHECKED.getValue())
            {
                widgetService.addVerifiedWidget(idBranch);
            } else if (type == LISEnum.ResultSampleState.REJECTED.getValue())
            {
                widgetService.addRejectedWidget(idBranch);
            } else if (type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
            {
                sample.setSampleTrackings(dao.getSampleTracking(sample.getId(), idOrder));
                int retake = sample.getSampleTrackings().stream().filter(sampleT -> sampleT.getState() == LISEnum.ResultSampleState.NEW_SAMPLE.getValue()).collect(Collectors.toList()).size();
                if (retake == 1)
                {
                    widgetService.addRetakedWidget(idBranch);
                }
            }
        } catch (Exception e)
        {
            Log.error(SampleTrackingServiceEnterpriseNT.class, e);
        }
    }

    @Override
    public TrackingAlarm trackingAlarmInterview(long idOrder) throws Exception
    {
        TrackingAlarm alarm = new TrackingAlarm();

        Order order = getOrder(idOrder, true);
        boolean validInterview = !dao.getInterview(order).stream()
                .filter(question -> question.isOpen() && (question.getInterviewAnswer() != null && !question.getInterviewAnswer().isEmpty()))
                .filter(question -> question.isOpen() || (!question.isOpen() && !question.getAnswers().stream().filter(answer -> answer.isSelected()).collect(Collectors.toList()).isEmpty()))
                .collect(Collectors.toList()).isEmpty();

        alarm.setExternalQuery(order.getService().isExternal());
        alarm.setInterview(validInterview);

        return alarm;
    }

    @Override
    public TestSampleTake sampleTrackingTest(TestSampleTake testSampleTake) throws Exception
    {

        List<String> listholiday = holidayService.listBasic();
        Boolean validator = true;

        for (int i = 0; i < testSampleTake.getTestSampleTakeTracking().size(); i++)
        {
            if (testSampleTake.getTestSampleTakeTracking().get(i).getMotive() == null && testSampleTake.getTestSampleTakeTracking().get(i).getState() == -1)
            {
                validator = false;
            }
        }

        if (validator == true)
        {
            List<AuditOperation> auditList = new ArrayList<>(0);
            List<Integer> testsToBeTaken = new ArrayList<>();

            TestSampleTake testSampletakeNew = new TestSampleTake();
            Date currentDate = new Date();

            Order order = getOrder(testSampleTake.getOrder(), true);

            Sample sample = order == null ? null : order.getSamples().stream().filter(s -> s.getCodesample().equals(testSampleTake.getSample())).findAny().orElse(null);

            if (sample == null)
            {
                throw new EnterpriseNTException(Arrays.asList("1|The sample is not related to the order"));
            }

            AuthorizedUser user = JWT.decode(request);

            if (sample.getSampleState().getState() == LISEnum.ResultSampleState.ORDERED.getValue())
            {
                dao.insertSampleTracking(testSampleTake.getOrder(), user.getId(), user.getBranch(), null, sample.getId(), LISEnum.ResultSampleState.COLLECTED.getValue(), null, currentDate);
                auditList.add(new AuditOperation(testSampleTake.getOrder(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(LISEnum.ResultSampleState.COLLECTED.getValue()), null, null, null, null));
            }
            ///////////////////////////UPDATE//////////////////////////////////////////            
            for (int i = 0; i < testSampleTake.getTestSampleTakeTracking().size(); i++)
            {
                Integer value = testSampleTake.getTestSampleTakeTracking().get(i).getIdTest();

                Test test = sample.getTests()
                        .stream()
                        .filter(t -> Objects.equals(t.getId(), value))
                        .findAny().orElse(null);

                if (test == null)
                {
                    throw new EnterpriseNTException(Arrays.asList("2| Don't find test"));
                }

                if (test.getResult().getSampleState() < LISEnum.ResultSampleState.COLLECTED.getValue())
                {
                    if (test.getResult().getSampleState() != testSampleTake.getTestSampleTakeTracking().get(i).getState()
                            && testSampleTake.getTestSampleTakeTracking().get(i).getState() == LISEnum.ResultSampleState.COLLECTED.getValue())
                    {

                        Integer stateChange = testSampleTake.getTestSampleTakeTracking().get(i).getState();
                        testSampletakeNew = dao.updateSampleTrackingTestTake(testSampleTake.getOrder(), sample.getId(), user.getId(), testSampleTake, testSampleTake.getTestSampleTakeTracking().get(i).getState(), test.getId(), listholiday, user.getBranch(), sample.getSampleState().getState());

                        auditList.add(new AuditOperation(testSampleTake.getOrder(), test.getId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(stateChange), null, null, null, null));

                        testsToBeTaken.add(testSampleTake.getTestSampleTakeTracking().get(i).getIdTest());
                    } else if (test.getResult().getSampleState() != testSampleTake.getTestSampleTakeTracking().get(i).getState()
                            && testSampleTake.getTestSampleTakeTracking().get(i).getState() == LISEnum.ResultSampleState.PENDING.getValue())
                    {

                        Integer stateChange = testSampleTake.getTestSampleTakeTracking().get(i).getState();
                        Integer motive = testSampleTake.getTestSampleTakeTracking().get(i).getMotive();

                        boolean isProfile;

                        isProfile = dao.validateProfile(test.getId());

                        if (isProfile == false)
                        {
                            testSampletakeNew = dao.updateSampleTrackingTestPending(testSampleTake.getOrder(), sample.getId(), user.getId(), testSampleTake, testSampleTake.getTestSampleTakeTracking().get(i).getState(), test.getId(), motive);
                        } else
                        {
                            testSampletakeNew = dao.updateSampleTrackingIsProfile(testSampleTake.getOrder(), sample.getId(), user.getId(), testSampleTake, testSampleTake.getTestSampleTakeTracking().get(i).getState(), test.getId(), motive);
                        }

                        auditList.add(new AuditOperation(testSampleTake.getOrder(), test.getId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(stateChange), motive, null, null, null));

                        // Mandar notificación al HIS de que esa muestra esta pendiente con estado HD
                        if (testSampleTake.getTestSampleTakeTracking().get(i).getState() == LISEnum.ResultSampleState.PENDING.getValue())
                        {
                            serviceIngreso.getMessageTest(order.getOrderNumber(), "pending", sample.getId(), test.getId().toString(), null);
                        }
                    }
                }
                if (test.getResult().getSampleState() < LISEnum.ResultSampleState.CHECKED.getValue())
                {
                    if (test.getResult().getSampleState() != testSampleTake.getTestSampleTakeTracking().get(i).getState()
                            && testSampleTake.getTestSampleTakeTracking().get(i).getState() == LISEnum.ResultSampleState.CHECKED.getValue())
                    {
                        Integer stateChange = testSampleTake.getTestSampleTakeTracking().get(i).getState();
                        testSampletakeNew = dao.updateSampleTrackingTestTake(testSampleTake.getOrder(), sample.getId(), user.getId(), testSampleTake, testSampleTake.getTestSampleTakeTracking().get(i).getState(), test.getId(), listholiday, user.getBranch(), sample.getSampleState().getState());

                        auditList.add(new AuditOperation(testSampleTake.getOrder(), test.getId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(stateChange), null, null, null, null));
                        testsToBeTaken.add(testSampleTake.getTestSampleTakeTracking().get(i).getIdTest());
                        serviceIngreso.getMessageTest(order.getOrderNumber(), "check", sample.getId(), test.getId().toString(), null);

                    } else if (test.getResult().getSampleState() == LISEnum.ResultSampleState.COLLECTED.getValue()
                            && (testSampleTake.getTestSampleTakeTracking().get(i).getState() == LISEnum.ResultSampleState.PENDING.getValue() || testSampleTake.getTestSampleTakeTracking().get(i).getState() == LISEnum.ResultSampleState.NEW_SAMPLE.getValue()))
                    {
                        Integer stateChange = testSampleTake.getTestSampleTakeTracking().get(i).getState();
                        testSampletakeNew = dao.updateSampleTrackingTestTake(testSampleTake.getOrder(), sample.getId(), user.getId(), testSampleTake, testSampleTake.getTestSampleTakeTracking().get(i).getState(), test.getId(), listholiday, user.getBranch(), sample.getSampleState().getState());

                        auditList.add(new AuditOperation(testSampleTake.getOrder(), test.getId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(stateChange), null, null, null, null));
                        testsToBeTaken.add(testSampleTake.getTestSampleTakeTracking().get(i).getIdTest());

                        // Mandar notificación al HIS de que esa muestra esta pendiente con estado HD
                        serviceIngreso.getMessageTest(order.getOrderNumber(), "pending", sample.getId(), test.getId().toString(), null);
                    }
                }
            }
            statisticService.sampleStateChanged(1, order.getOrderNumber(), sample.getId());
            trackingService.registerOperationTracking(auditList);
            CompletableFuture.runAsync(()
                    ->
            {
                if (order != null)
                {
                    if (order.getService().getHospitalSampling())
                    {
                        dashBoardService.dashBoardHospitalSampling(order.getOrderNumber(), sample.getId(), testsToBeTaken, DashBoardOpportunityTime.ACTION_INSERT_HOSPITAL_SAMPLING);
                    }
                }
            });

            ///////////////////////////////////////
            return testSampletakeNew;
        } else
        {
            throw new EnterpriseNTException(Arrays.asList("0|Motive null in state -1"));
        }
    }

    @Override
    public Order getAudit(long orderNumber) throws Exception
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
                    .filter(t -> t.getTestType() == 0)
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
            order.setResultTest(tests);
            order.setSamples(getSamples(tests));
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
                            samples.get(samples.indexOf(test.getSample())).getTests().add(h);
                        } else
                        {
                            s = test.getSample();
                            h = new Test();
                            h.setId(test.getId());
                            h.setCode(test.getCode());
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
                                samples.get(samples.indexOf(child.getSample())).getTests().add(h);
                            } else
                            {
                                s = child.getSample();
                                h = new Test();
                                h.setId(testR.getTestId());
                                h.setCode(testR.getTestCode());
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
                                        samples.get(samples.indexOf(subChild.getSample())).getTests().add(h);
                                    } else
                                    {
                                        s = subChild.getSample();
                                        h = new Test();
                                        h.setId(testR.getTestId());
                                        h.setCode(testR.getTestCode());
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

    @Override
    public List<TestSampleTakeTracking> listSampleTakeByOrder(long idOrder, String codeSample) throws Exception
    {
        List<TestSampleTakeTracking> listTestSampleTakeTracking = new ArrayList<>();
        Order order = getOrder(idOrder, true);
        Sample sample = order == null ? null : order.getSamples().stream().filter(s -> s.getCodesample().equals(codeSample)).findAny().orElse(null);

        if (sample == null)
        {
            throw new EnterpriseNTException(Arrays.asList("1|The sample is not related to the order"));
        }

        listTestSampleTakeTracking = dao.listSampleTakeByOrder(order.getOrderNumber(), sample.getId());
        return listTestSampleTakeTracking;
    }

    /**
     * Actualiza el estado a verificado de una muestra
     *
     * @param requestSampleDestination
     * @throws Exception Error en la base de datos.
     */
    @Override
    public boolean updateStateToCheck(RequestSampleDestination requestSampleDestination) throws Exception
    {
        try
        {
            AuthorizedUser user = JWT.decode(request);
            List<String> listDestinations = Arrays.asList(requestSampleDestination.getDestinations().replace(" ", "").split(","));
            AssignmentDestination asignment = getDestinationRoute(requestSampleDestination.getIdOrder(), String.valueOf(requestSampleDestination.getIdSample()));
            for (DestinationRoute destination : asignment.getDestinationRoutes())
            {
                for (String idDestination : listDestinations)
                {
                    int idDestParse = Integer.parseInt(idDestination);
                    if (!destination.isVerify() && Objects.equals(destination.getId(), idDestParse))
                    {
                        try
                        {
                            VerifyDestination verifyDest = new VerifyDestination();
                            verifyDest.setOrder(requestSampleDestination.getIdOrder());
                            verifyDest.setSample(String.valueOf(requestSampleDestination.getIdSample()));
                            verifyDest.setDestination(idDestParse);
                            verifyDest.setApproved(true);
                            verifyDest.setBranch(user.getBranch());
                            verifyDestination(verifyDest);
                        } catch (Exception ex)
                        {
                            Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    }
                }
            }
            return true;
        } catch (Exception ex)
        {
            return false;
        }
    }

    /**
     * Verifica que el numero de orden enviado si exista
     *
     * @param idOrder
     * @return True si existe - False si no existe
     * @throws Exception Error en la base de datos.
     */
    @Override
    public boolean orderExists(long idOrder) throws Exception
    {
        try
        {
            Order order = orderService.get(idOrder);
            return order != null;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Verifica que una muestra ya se encuentre verificada
     *
     * @param idOrder
     * @param idRoute
     * @return True si existe - False si no existe
     * @throws Exception Error en la base de datos.
     */
    @Override
    public boolean isSampleCheck(long idOrder, int idRoute) throws Exception
    {
        try
        {
            int thisExist = dao.isSampleCheck(idOrder, idRoute);
            return thisExist > 0;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * Verifica si una muestra ya se encuentra verificada para verificaion
     * sencilla
     *
     * @param idOrder
     * @param sampleId
     * @return True si existe - False si no existe
     * @throws Exception Error en la base de datos.
     */
    @Override
    public boolean isSampleCheckSimple(long idOrder, int sampleId) throws Exception
    {
        try
        {
            int exist = dao.isSampleCheckSimple(idOrder, sampleId);
            return exist > 0;
        } catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public List<Question> getInterviewAnswerDestination(int idDestination) throws Exception
    {
        Destination destination = getDestination(idDestination);
        List<String> errors = validateFieldsDestination(destination);
        if (errors.isEmpty())
        {
            List<Question> question = new ArrayList<>();
            question = dao.getInterviewDestination(destination);
            if (question != null)
            {
                return question;
            } else
            {
                errors.add("0|question");
                throw new EnterpriseNTException(errors);
            }

        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Destination getDestination(Integer idDestination) throws Exception
    {
        Destination destination = destinationDao.get(idDestination, null, null);

        return destination;
    }

    /**
     * VAlidacion de la orden a verificar
     *
     * @param destination
     *
     * @return
     * @throws Exception
     */
    private List<String> validateFieldsDestination(Destination destination) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (destination == null || destination.getId() == null)
        {
            errors.add("0|Destination");
            return errors;
        }

        return errors;
    }

    @Override
    public int insertResultInterviewDestination(List<Question> questions, long idOrder, int idSample, int idDestination, int idBranch) throws Exception
    {
        AuthorizedUser session = JWT.decode(request);
        List<AuditOperation> audit = new ArrayList<>();
        int quantity = dao.insertResultInterviewDestination(questions, idOrder, idSample, idDestination, idBranch, session);
        audit.add(new AuditOperation(idOrder, null, null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_INTERVIEW, Tools.jsonObject(questions), null, null, null, null));
        trackingService.registerOperationTracking(audit);
        return quantity;
    }

    @Override
    public List<Question> getInterviewDestination(long idOrder, int idSample, int idDestination, int idBranch) throws Exception
    {
        Order order = getOrder(idOrder, false);
        List<String> errors = validateFields(order);
        if (errors.isEmpty())
        {
            return dao.getInterviewDestination(order, idSample, idDestination, idBranch);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public boolean sampleTrackingTemperature(long idOrder, String codeSample, int type, String temperature) throws Exception
    {
        List<AuditOperation> audit = new ArrayList<>();

        Order order = getOrder(idOrder, false);

        Sample sample = order == null ? null : order.getSamples().stream().filter(s -> s.getCodesample().equals(codeSample)).findAny().orElse(null);

        AuthorizedUser authorizedUser;
        Integer idBranch;
        //permite utilizar este metodo cuando no se tiene token
        try
        {
            authorizedUser = JWT.decode(request);
            idBranch = authorizedUser.getBranch();

        } catch (Exception e)
        {
            authorizedUser = new AuthorizedUser(1);///user lismanager
            idBranch = order.getBranch().getId();

        }

        SampleState sampleState = dao.getSampleState(sample.getId(), idOrder, idBranch);

        int statetemp = sampleState.getState() == null ? -1 : sampleState.getState();
        if (type != statetemp)
        {
            if (type == LISEnum.ResultSampleState.CHECKED.getValue() && sampleState.getState() < LISEnum.ResultSampleState.COLLECTED.getValue())
            {
                sampleTrackingTemperature(idOrder, codeSample, LISEnum.ResultSampleState.COLLECTED.getValue(), temperature);
            }

            List<String> errors = validateFields(order);
            if (sample == null || sample.getId() == null)
            {
                errors.add("1|sample");
            }

            if (errors.isEmpty())
            {
                if (!validateStateTracking(idOrder, sample.getId(), type, idBranch))
                {
                    return false;
                } else
                {
                    AuthorizedUser session = JWT.decode(request);
                    Date currentDate = new Date();

                    // Variable que guarda los ids de los examenes contiene esa muestra
                    StringBuilder examenesOne = new StringBuilder();

                    List<Test> listaudittest = new ArrayList<>();
                    if (type == LISEnum.ResultSampleState.CHECKED.getValue())
                    {
                        listaudittest = sample.getTests().stream().filter(t
                                -> t.getResult().getSampleState() == LISEnum.ResultSampleState.ORDERED.getValue() || t.getResult().getSampleState() == LISEnum.ResultSampleState.COLLECTED.getValue()).collect(Collectors.toList());

                        sample.setTests(addPanels(sample.getTests(), idOrder));
                        int groupType = order.getType().getCode().equals("S") ? 1 : 2;
                        List<Integer> testsIds = sample.getTests().stream().map(t -> t.getId()).collect(Collectors.toList());
                        List<Integer> laboratoriesIds = sample.getTests().stream().map(t -> t.getLaboratory().getId()).collect(Collectors.toList());
                        laboratoriesIds.removeAll(Collections.singleton(null));

                        List<Test> tests = testDao.testsByBranch(idBranch, testsIds, laboratoriesIds, groupType);

                        List<Test> list = sample.getTests().stream().map(t -> filterTests(t, tests)).collect(Collectors.toList());
                        list.removeAll(Collections.singleton(null));
                        sample.setTests(list);

                        if (sample.getTests() == null || sample.getTests().isEmpty())
                        {
                            errors.add("1|tests");
                        }
                    } else if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
                    {
                        listaudittest = sample.getTests().stream().filter(t
                                -> t.getResult().getSampleState() == LISEnum.ResultSampleState.ORDERED.getValue()).collect(Collectors.toList());

                        int groupType = order.getType().getCode().equals("S") ? 1 : 2;
                        List<Integer> testsIds = sample.getTests().stream().map(t -> t.getId()).collect(Collectors.toList());
                        List<Integer> laboratoriesIds = sample.getTests().stream().map(t -> t.getLaboratory().getId()).collect(Collectors.toList());
                        laboratoriesIds.removeAll(Collections.singleton(null));

                        List<Test> tests = testDao.testsByBranch(idBranch, testsIds, laboratoriesIds, groupType);

                        List<Test> list = sample.getTests().stream().map(t -> filterTests(t, tests)).collect(Collectors.toList());
                        list.removeAll(Collections.singleton(null));
                        sample.setTests(list);

                        List<String> listholiday = holidayService.listBasic();
                        dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), type, null, null, listholiday);
                    } else
                    {
                        dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), type, null, null, Double.parseDouble(temperature.replace(",", ".")));
                    }

                    if (type != LISEnum.ResultSampleState.ORDERED.getValue())
                    {

                        for (Test test : listaudittest)
                        {
                            // el perfil tambien debera enviarse
                            if (test.getProfile() != null && test.getId() != null)
                            {
                                if (test.getProfile() == 0 && !examenesOne.toString().contains(test.getId().toString()))
                                {

                                    examenesOne.append(test.getId().toString()).append("|");
                                } else
                                {
                                    if (!examenesOne.toString().contains(test.getProfile().toString()))
                                    {
                                        examenesOne.append(test.getProfile().toString()).append("|");
                                    }
                                }
                            }

                            audit.add(new AuditOperation(order.getOrderNumber(), test.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(type), null, null, null, null));
                        }
                    }

                    addSampleWidget(type, sample, session.getBranch(), order.getOrderNumber());
                    if (type == LISEnum.ResultSampleState.ORDERED.getValue())
                    {
                        dao.insertSampleOrderedTemperature(order.getOrderNumber(), session.getId(), session.getBranch(), sample.getId(), type, currentDate, temperature);
                    } else
                    {
                        statisticService.sampleStateChanged(type, idOrder, sample.getId());
                        CompletableFuture.runAsync(()
                                ->
                        {
                            try
                            {
                                String examenes = "";
                                // Obtenemos todos los examenes de la muestra y si estos examenes pertenecen a algun perfil,
                                // el perfil tambien debera enviarse
                                for (Test test : sample.getTests())
                                {
                                    if (test.getProfile() != null && test.getId() != null)
                                    {
                                        if (test.getProfile() == 0 && !examenes.contains(test.getId().toString()))
                                        {
                                            examenes += test.getId().toString() + ",";
                                        } else
                                        {
                                            if (!examenes.contains(test.getProfile().toString()))
                                            {
                                                examenes += test.getProfile().toString() + ",";
                                            }
                                            if (!examenes.contains(test.getId().toString()))
                                            {
                                                examenes += test.getId().toString() + ",";
                                            }
                                        }
                                    }
                                }

                                statisticService.updateSampleStatus(type, idOrder, examenes);
                            } catch (Exception ex)
                            {
                                Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }

                    audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(type), null, null, null, null));
                    trackingService.registerOperationTracking(audit);

                    if (type == LISEnum.ResultSampleState.CHECKED.getValue())
                    {
                        if (order.getService().getHospitalSampling())
                        {
                            try
                            {
                                String url = configurationServices.get("UrlHomebound").getValue();
                                integrationHomeBoundService.verificSample(order.getOrderNumber(), sample.getId(), url);
                            } catch (Exception ex)
                            {
                                Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        CompletableFuture.runAsync(()
                                ->
                        {
                            integrationMiddlewareService.sendOrderASTM(idOrder, null, sample.getId().toString(), Constants.CHECK, null, null, null, session.getBranch(), false);
                        });
                        if (Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("verificarMuestra").getValue()) == true)
                        {
                            CompletableFuture.runAsync(()
                                    ->
                            {
                                eventsSampleTrackingService.sampleVerify(idOrder, codeSample);
                            });
                        }
                        IntegrationHisLog.info("Relizar envio al HIS: " + order.getExternalId());
                        if (!order.getExternalId().isEmpty() && !order.getExternalId().equals("0"))
                        {
                            try
                            {
                                ResponsSonControl objControl = new ResponsSonControl();
                                objControl = integrationIngresoDao.ordersSonControl(idOrder);
                                IntegrationHisLog.info("Estado : " + objControl.getEstado());

                                if (objControl.getEstado() < 3)
                                {
                                    CompletableFuture.runAsync(() ->
                                    {
                                        // Variable que guarda los ids de los examenes contiene esa muestra

                                        IntegrationHisLog.info("Relizar envio al HIS orden : " + idOrder + "examenes" + examenesOne.toString());
                                        try
                                        {
                                            //Check-IN Segment
                                            serviceIngreso.getMessageTest(idOrder, "check", sample.getId(), examenesOne.toString(), null);
                                        } catch (Exception ex)
                                        {
                                            IntegrationHisLog.error(ex);
                                        }
                                    });
                                }
                            } catch (Exception ex)
                            {
                                IntegrationHisLog.error("ERROR : " + ex);
                                Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        orderService.updateOrderhistory(idOrder, sample.getId(), order.getPatient().getId());
                    } else if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
                    {
                        if (Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("tomarMuestra").getValue()) == true)
                        {
                            CompletableFuture.runAsync(()
                                    ->
                            {
                                eventsSampleTrackingService.sampleTracking(idOrder, codeSample);
                            });
                        }

                        // Enviar a Tablero toma de muestra hospitalaria:
                        CompletableFuture.runAsync(()
                                ->
                        {
                            if (order.getService().getHospitalSampling())
                            {
                                dashBoardService.dashBoardHospitalSampling(idOrder, sample.getId(), sample.getTests().stream().map(test -> test.getId()).collect(Collectors.toList()), DashBoardOpportunityTime.ACTION_UPDATE_HOSPITAL_SAMPLING);
                            }
                        });
                    } else if (type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
                    {
                        CompletableFuture.runAsync(()
                                ->
                        {
                            if (order.getService().getHospitalSampling())
                            {
                                try
                                {
                                    integrationHomeBoundService.retakeSample(order.getOrderNumber(), sample.getId());
                                } catch (Exception ex)
                                {
                                    Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                    }

                    return true;
                }

            } else
            {
                throw new EnterpriseNTException(errors);
            }
        }
        return false;
    }

    @Override
    public AssignmentDestination getDestinationRouteAnalyzer(long idOrder, String codeSample, int idBranch, User user) throws Exception
    {
        AssignmentDestination assignment = null;
        Order order = getOrderAnalyzer(idOrder, false, idBranch, user);
        List<String> errors = validateFields(order);
        Sample sample = order == null ? null : order.getSamples().stream().filter(s -> s.getCodesample().equals(codeSample)).findAny().orElse(null);

        if (sample == null)
        {
            errors.add("0|sample");
        } else if (idBranch == 0)
        {
            errors.add("1|Branch is 0");
        }

        if (errors.isEmpty())
        {
            List<VerifyDestination> destinations = dao.getRouteByOrder(idOrder, sample.getId(), idBranch);
            if (destinations.isEmpty())
            {
                assignment = dao.getDestinationRoute(idOrder, order.getType().getId(), idBranch, sample.getId());
                if (assignment == null)
                {
                    assignment = dao.getDestinationRoute(idOrder, 0, idBranch, sample.getId());
                }

            } else
            {
                assignment = dao.getDestinationRouteAssigment(idOrder, sample.getId(), destinations.get(0).getAssigmentDestination());
            }

            if (assignment == null)
            {
                return null;
            } else
            {
                List<DestinationRoute> destinationRoutes = assignment.getDestinationRoutes().stream()
                        .filter(destination -> getTestDestination(destination.getTests(), sample))
                        .map(destination -> destination.setTests(getResultTests(destination.getTests(), sample)))
                        .collect(Collectors.toList());

                assignment.setDestinationRoutes(destinationRoutes);
            }

            assignment.setService(order.getService());
            String answerTemperature = configurationService.get("ManejoTemperatura").getValue();

            if ("True".equals(answerTemperature))
            {
                Sample sampleTemperature;
                sampleTemperature = dao.getTemperatures(sample.getId());
                assignment.getSample().setMinimumTemperature(sampleTemperature.getMinimumTemperature());
                assignment.getSample().setMaximumTemperature(sampleTemperature.getMaximumTemperature());
            }
        } else
        {
            throw new EnterpriseNTException(errors);
        }

        return assignment;
    }

    @Override
    public Order getOrderAnalyzer(long idOrder, boolean sampleTracking, int idBranch, User user) throws Exception
    {
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        Order order = dao.getOrder(idOrder, laboratorys, idbranch);
        if (order != null)
        {
            List<Sample> samples = order.getTests().stream().filter(test -> (test.getTestType() == 0 && test.getProfile() == 0) || (test.getTestType() > 0 && test.getSample().getId() != null) || (test.getTestType() == 0 && test.getProfile() != 0 && test.getDependentTest() == 0)).map(test -> test.getSample()).distinct().collect(Collectors.toList());

            samples.stream().forEach(sample
                    ->
            {
                sample.setTests(getTestsSample(order.getTests(), sample, order.getOrderNumber()));
            });

            order.setSamples(samples);
            order.setTests(null);

            if (sampleTracking)
            {
                for (Sample sample : samples)
                {
                    SampleState sampleState = dao.getSampleState(sample.getId(), idOrder, idBranch);

                    if (sampleState == null)
                    {
                        Date currentDate = new Date();
                        dao.insertSampleOrdered(order.getOrderNumber(), user.getId(), idBranch, sample.getId(), LISEnum.ResultSampleState.ORDERED.getValue(), currentDate);
                    }

                    sample.setSampleState(dao.getSampleState(sample.getId(), idOrder, idBranch));
                    sample.setSampleTrackings(dao.getSampleTracking(sample.getId(), idOrder));
                    if (sample.getSampleState() != null)
                    {
                        sample.getSampleState().setSample(null);
                    }

                }
            }
        }
        return order;
    }

    @Override
    public boolean sampleTrackingAnalyzer(long idOrder, String codeSample, int type, Reason reason, Boolean retake, int idBranch, User user) throws Exception
    {
        if (type == LISEnum.ResultSampleState.CHECKED.getValue())
        {
            sampleTrackingAnalyzer(idOrder, codeSample, LISEnum.ResultSampleState.COLLECTED.getValue(), null, retake, idBranch, user);
        }

        Order order = getOrderAnalyzer(idOrder, false, idBranch, user);
        Sample sample = order == null ? null : order.getSamples().stream().filter(s -> s.getCodesample().equals(codeSample)).findAny().orElse(null);

        List<String> errors = validateFields(order);
        if (sample == null || sample.getId() == null)
        {
            errors.add("1|sample");
        }

        if (errors.isEmpty())
        {
            if (type != LISEnum.ResultSampleState.ORDERED.getValue())
            {
                CompletableFuture.runAsync(()
                        ->
                {
                    try
                    {
                        String examenesOne = "";
                        // Obtenemos todos los examenes de la muestra y si estos examenes pertenecen a algun perfil,
                        // el perfil tambien debera enviarse
                        for (Test test : sample.getTests())
                        {
                            if (test.getProfile() != null && test.getId() != null)
                            {
                                if (test.getProfile() == 0 && !examenesOne.contains(test.getId().toString()))
                                {
                                    examenesOne += test.getId().toString() + ",";
                                } else
                                {
                                    if (!examenesOne.contains(test.getProfile().toString()))
                                    {
                                        examenesOne += test.getProfile().toString() + ",";
                                    }
                                    if (!examenesOne.contains(test.getId().toString()))
                                    {
                                        examenesOne += test.getId().toString() + ",";
                                    }
                                }
                            }
                        }
                        // Variable que ira al proceso asincrono:
                        String examenes = examenesOne;
                        statisticService.updateSampleStatus(type, idOrder, examenes);
                    } catch (Exception ex)
                    {
                        Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }

            if (type == LISEnum.ResultSampleState.REJECTED.getValue() || type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
            {
                if (reason.getMotive() == null || reason.getMotive().getId() == null || reason.getMotive().getId() == 0)
                {
                    errors.add("0|motive");
                    throw new EnterpriseNTException(errors);
                }

                if (type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
                {
                    List<Test> testsReject = sample.getTests().stream()
                            .filter(test -> reason.getTests().contains(new Test(test.getId())))
                            .filter(test -> test.getResult().getSampleState() != LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
                            .collect(Collectors.toList());

                    if (testsReject.isEmpty())
                    {
                        errors.add("0|tests");
                        throw new EnterpriseNTException(errors);
                    } else
                    {
                        sample.setTests(testsReject);
                    }
                }
            }

            if (!validateStateTrackingAnalyzer(idOrder, sample.getId(), type, idBranch))
            {
                return false;
            } else
            {
                AuthorizedUser session = new AuthorizedUser();
                session.setBranch(idBranch);
                session.setId(user.getId());
                Date currentDate = new Date();
                //cambio de estado de la muestra
                SampleTracking tracking = dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), type, null, null, new ArrayList<>());

                if (type == LISEnum.ResultSampleState.CHECKED.getValue())
                {
                    sample.setTests(addPanels(sample.getTests(), idOrder));
                }

                if (type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue() && retake)
                {
                    dao.updateDateRetakes(order.getOrderNumber(), sample.getTests(), session, sample.getId());
                }
                List<AuditOperation> audit = new ArrayList<>();
//                CompletableFuture.runAsync(() ->
//                {
                addSampleWidget(type, sample, session.getBranch(), order.getOrderNumber());
                if (type == LISEnum.ResultSampleState.ORDERED.getValue())
                {
                    dao.insertSampleOrdered(order.getOrderNumber(), session.getId(), session.getBranch(), sample.getId(), type, currentDate);
                } else
                {
                    statisticService.sampleStateChanged(type, idOrder, sample.getId());
                }
//                });
                audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(type), reason == null ? null : reason.getMotive().getId(), reason == null ? null : reason.getComment(), null, null));
                //audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(),null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(tracking), reason == null ? null : reason.getMotive().getId(), reason == null ? null : reason.getComment()));
                trackingService.registerOperationTracking(audit, user.getId());

                if (type == LISEnum.ResultSampleState.CHECKED.getValue())
                {
                    if (order.getService().getHospitalSampling())
                    {
                        String url = configurationServices.get("UrlHomebound").getValue();
                        integrationHomeBoundService.verificSample(order.getOrderNumber(), sample.getId(), url);
                    }
                    CompletableFuture.runAsync(()
                            ->
                    {
                        integrationMiddlewareService.sendOrderASTM(idOrder, null, sample.getId().toString(), Constants.CHECK, null, null, null, idBranch, false);
                    });
                    if (Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("verificarMuestra").getValue()) == true)
                    {
                        CompletableFuture.runAsync(()
                                ->
                        {
                            eventsSampleTrackingService.sampleVerify(idOrder, codeSample);
                        });
                    }

                    if (!order.getExternalId().isEmpty() && !order.getExternalId().equals("0"))
                    {
                        CompletableFuture.runAsync(()
                                ->
                        {
                            try
                            {
                                //Check-IN Segment
                                serviceIngreso.getMessageTest(idOrder, "check", sample.getId(), null, null);
                            } catch (Exception ex)
                            {
                                IntegrationHisLog.error(ex);
                            }
                        });
                    }
                } else if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
                {
                    if (Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("tomarMuestra").getValue()) == true)
                    {
                        CompletableFuture.runAsync(()
                                ->
                        {
                            eventsSampleTrackingService.sampleTracking(idOrder, codeSample);
                        });
                    }
                } else if (type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
                {
                    if (order.getService().getHospitalSampling())
                    {
                        try
                        {
                            integrationHomeBoundService.retakeSample(order.getOrderNumber(), sample.getId());
                        } catch (Exception ex)
                        {
                            Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else if (type == LISEnum.ResultSampleState.REJECTED.getValue())
                {
                    if (!order.getExternalId().isEmpty() && !order.getExternalId().equals("0"))
                    {
                        String examenesOne = "";
                        // Obtenemos todos los examenes de la muestra y si estos examenes pertenecen a algun perfil,
                        // el perfil tambien debera enviarse
                        for (Test test : sample.getTests())
                        {
                            if (test.getProfile() != null && test.getId() != null)
                            {
                                if (test.getProfile() == 0 && !examenesOne.contains(test.getId().toString()))
                                {
                                    examenesOne += test.getId().toString() + ",";
                                } else
                                {
                                    if (!examenesOne.contains(test.getProfile().toString()))
                                    {
                                        examenesOne += test.getProfile().toString() + ",";
                                    }
                                    if (!examenesOne.contains(test.getId().toString()))
                                    {
                                        examenesOne += test.getId().toString() + ",";
                                    }
                                }
                            }
                        }
                        // Variable que ira al proceso asincrono:
                        String examenes = examenesOne;
                        //Check-IN Segment
                        CompletableFuture.runAsync(()
                                ->
                        {
                            try
                            {
                                serviceIngreso.getMessageTest(idOrder, "rejection", sample.getId(), examenes, null);
                            } catch (Exception ex)
                            {
                                Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                }

                return true;
            }
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    /**
     * Realiza validaciónes para cambio de estado de la muestra desde un usuario
     * analizador
     *
     * @param idOrder id orden
     * @param idSample id muestra
     * @param type nuevo estado de la muestra
     *
     * @return
     * @throws Exception
     */
    private boolean validateStateTrackingAnalyzer(long idOrder, int idSample, int type, int idBranch) throws Exception
    {

        SampleState sampleState = dao.getSampleState(idSample, idOrder, idBranch);
        OrderCreationLog.info(Tools.jsonObject(sampleState));
        List<String> errors = new ArrayList<>();

        if (type == LISEnum.ResultSampleState.ORDERED.getValue())
        {
            if (sampleState != null && sampleState.getState() != null)
            {
                errors.add("1");
            }
        } else if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
        {
            if (sampleState == null)
            {
                errors.add("4");
                throw new EnterpriseNTException(errors);
            }

            if (sampleState.getState() == LISEnum.ResultSampleState.ORDERED.getValue())
            {
            } else if (sampleState.getState() == LISEnum.ResultSampleState.COLLECTED.getValue())
            {
                //errors.add("1");
            } else if (sampleState.getState() == LISEnum.ResultSampleState.REJECTED.getValue() || sampleState.getState() == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
            {
                errors.add("3");
            }
        } else if (type == LISEnum.ResultSampleState.CHECKED.getValue())
        {
            if (sampleState == null)
            {
                errors.add("4");
                throw new EnterpriseNTException(errors);
            }

            if (sampleState.getState() == LISEnum.ResultSampleState.COLLECTED.getValue())
            {
            } else if (sampleState.getState() == LISEnum.ResultSampleState.ORDERED.getValue())
            {
                //errors.add("1");
            } else if (sampleState.getState() == LISEnum.ResultSampleState.CHECKED.getValue())
            {
                errors.add("2");
            } else if (sampleState.getState() == LISEnum.ResultSampleState.REJECTED.getValue() || sampleState.getState() == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
            {
                errors.add("3");
            } else
            {
                errors.add("4");
            }
        } else if (type == LISEnum.ResultSampleState.REJECTED.getValue() || type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
        {
            if (sampleState == null)
            {
                errors.add("4");
                throw new EnterpriseNTException(errors);
            }

            if (sampleState.getState() == LISEnum.ResultSampleState.COLLECTED.getValue() || sampleState.getState() == LISEnum.ResultSampleState.CHECKED.getValue())
            {
            } else if (sampleState.getState() == LISEnum.ResultSampleState.ORDERED.getValue())
            {
                errors.add("1");
            } else if (sampleState.getState() == LISEnum.ResultSampleState.REJECTED.getValue())
            {
                errors.add("2");
            } else if (sampleState.getState() == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
            {
                errors.add("3");
            } else
            {
                errors.add("4");
            }
        } else
        {
            return false;
        }

        if (errors.isEmpty())
        {
            return true;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public boolean verifyDestinationAnalyzer(VerifyDestination verifyDestination, User user) throws Exception
    {
        // Se obtiene la sede del objeto VerifyDestination al cual se le a cargado previamente
        Order order = getOrderAnalyzer(verifyDestination.getOrder(), false, verifyDestination.getBranch(), user);
        AuthorizedUser session = new AuthorizedUser();
        session.setBranch(verifyDestination.getBranch());
        session.setId(user.getId());
        verifyDestination.setAssigmentDestination(getDestinationRouteAnalyzer(verifyDestination.getOrder(), verifyDestination.getSample(), verifyDestination.getBranch(), user).getId());
        List<String> errors = validateFields(verifyDestination, user);
        Sample sample = order.getSamples().stream().filter(s -> s.getCodesample().equals(verifyDestination.getSample())).findAny().orElse(null);

        if (errors.isEmpty())
        {
            dao.verifyDestination(verifyDestination.getOrder(), verifyDestination.getDestination(), session.getId(), session.getBranch(), sample.getId(), verifyDestination.getAssigmentDestination(), destinationDao.getByRoute(verifyDestination.getDestination()));
            List<AuditOperation> audit = new ArrayList<>();
            audit.add(new AuditOperation(verifyDestination.getOrder(), sample.getId(), verifyDestination.getDestination(), AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, LISEnum.ResultSampleState.CHECKED.getValue() + "", null, null, null, null));
            trackingService.registerOperationTracking(audit, user.getId());
            return true;
        } else if (errors.size() == 1 && !errors.stream().filter(error -> error.equalsIgnoreCase("1|destination") || error.equalsIgnoreCase("2|destination")).findFirst().orElse("").isEmpty())
        {
            return false;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    /**
     * Obtiene una lista de ordenes por un rango de fechas (inicial y final) y
     * por un id de una muestra en particular, si el id de esta es 0 debemos
     * traer todas las muestras de esta, lo mismo pasa con el idService, si este
     * es cero no se filtra por servicio, de lo contrario se debera filtrar por
     * ese servicio en especifico
     *
     * @param initialDate
     * @param endDate
     * @param idSample
     * @param idService
     *
     * @return Orden.
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<Order> getSamplesByTemperatureAndDate(long initialDate, long endDate, int idSample, int idService) throws Exception
    {
        try
        {
            int idbranch = JWT.decode(request).getBranch();
            List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

            return dao.getSamplesByTemperatureAndDate(initialDate, endDate, idSample, idService,laboratorys, idbranch ).stream().filter(order -> order != null).collect(Collectors.toList());
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean sampleOrdered(Long idOrder, AuthorizedUser session, int type, int origin) throws Exception
    {
        OrderCreationLog.info("check de examenes");
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        Order order = dao.getOrder(idOrder, laboratorys, idbranch);
        List<Sample> samples = order.getTests().stream().filter(test -> test.getTestType() == 0).map(test -> test.getSample()).distinct().collect(Collectors.toList());

        samples.stream().forEach(sample
                ->
        {
            sample.setTests(getTestsSample(order.getTests(), sample, order.getOrderNumber()));
        });

        for (Sample sample : samples)
        {
            OrderCreationLog.info("sample 1" + sample.getId());
            List<AuditOperation> audit = new ArrayList<>();
            SampleState sampleState = dao.getSampleState(sample.getId(), order.getOrderNumber(), session.getBranch());
            OrderCreationLog.info("estado de la muestra " + Tools.jsonObject(sampleState));
            if (sampleState == null)
            {
                if (type == LISEnum.ResultSampleState.ORDERED.getValue() && sample.isCheck())
                {
                    OrderCreationLog.info("primer if");
                    sampleTracking(order.getOrderNumber(), sample.getCodesample(), LISEnum.ResultSampleState.ORDERED.getValue(), null, false, false);
                } else
                {
                    OrderCreationLog.info("2 if");
                    boolean check = !configurationServices.get("Trazabilidad").getValue().equals("1");
                    if ((check == true && origin == LISEnum.OriginModule.ORIGIN.getValue()) || (origin == LISEnum.OriginModule.ORIGIN.getValue() && !sample.isCheck()) || (origin == LISEnum.OriginModule.RESULT.getValue() && Boolean.parseBoolean(configurationServices.get("AgregarExamenesMuestras").getValue())))
                    {
                        OrderCreationLog.info("if check");
                        //SampleTracking trackingCollected = dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), LISEnum.ResultSampleState.COLLECTED.getValue(), null, null);
                        //audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(trackingCollected), null, null));
                        sample.setTests(addPanels(sample.getTests(), order.getOrderNumber()));
                        SampleTracking trackingChecked = dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), LISEnum.ResultSampleState.CHECKED.getValue(), null, null);
                        audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(trackingChecked), null, null));
                        addSampleWidget(LISEnum.ResultSampleState.CHECKED.getValue(), sample, session.getBranch(), order.getOrderNumber());
                        statisticService.sampleStateChanged(type, idOrder, sample.getId());
                    }
                    sampleTracking(order.getOrderNumber(), sample.getCodesample(), LISEnum.ResultSampleState.ORDERED.getValue(), null, false, false);
//                    addSampleWidget(LISEnum.ResultSampleState.ORDERED.getValue(), sample, session.getBranch(), order.getOrderNumber());
                }
            } else
            {
                OrderCreationLog.info("else check");
                if (type != LISEnum.ResultSampleState.ORDERED.getValue() && sampleState.getState() != LISEnum.ResultSampleState.CHECKED.getValue())
                {
                    OrderCreationLog.info("else if 1");
                    SampleTracking trackingCollected = dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), LISEnum.ResultSampleState.COLLECTED.getValue(), null, null);
                    sample.setTests(addPanels(sample.getTests(), order.getOrderNumber()));
                    SampleTracking trackingChecked = dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), LISEnum.ResultSampleState.CHECKED.getValue(), null, null);
                    if (sampleState.getState() == LISEnum.ResultSampleState.ORDERED.getValue())
                    {
                        audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(trackingCollected), null, null));
                        audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(trackingChecked), null, null));
                    } else if (sampleState.getState() == LISEnum.ResultSampleState.COLLECTED.getValue())
                    {
                        audit.add(new AuditOperation(order.getOrderNumber(), sample.getId(), AuditOperation.ACTION_INSERT, AuditOperation.TYPE_SAMPLE, Tools.jsonObject(trackingChecked), null, null));
                    }
                    addSampleWidget(LISEnum.ResultSampleState.CHECKED.getValue(), sample, session.getBranch(), order.getOrderNumber());
                } else if ((sample.isCheck() && origin == LISEnum.OriginModule.RESULT.getValue()))
                {
                    OrderCreationLog.info("else if 2");
                    sample.setTests(addPanels(sample.getTests(), order.getOrderNumber()));
                    dao.sampleTracking(order.getOrderNumber(), sample.getTests(), session, sample.getId(), LISEnum.ResultSampleState.CHECKED.getValue(), null, null);
                }
            }
            trackingService.registerOperationTracking(audit);
        }
        return true;
    }

    private Test filterTests(Test test, List<Test> tests)
    {
        try
        {
            return tests.stream()
                    .filter(t -> t.getId().equals(test.getId()) && t.getLaboratory().getId().equals(test.getLaboratory().getId()))
                    .findAny()
                    .orElse(null);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Question> interviewsByOrder(long idOrder) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void validateCodePrintHomebound(long idOrder, Sample sample)
    {
        try
        {
            ServiceLaboratory service = orderDao.getServiceByOrder(idOrder);
            if (service.getHospitalSampling() == true)
            {
                List<Integer> tests = sample.getTests().stream().map(t -> t.getId()).distinct().collect(Collectors.toList());
                List<Integer> list = resultTestDao.testByCodeEmptyHomebound(idOrder, tests);
                if (list.size() > 0)
                {
                    resultTestDao.updatePrintCodeHomebound(idOrder, list);
                }
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

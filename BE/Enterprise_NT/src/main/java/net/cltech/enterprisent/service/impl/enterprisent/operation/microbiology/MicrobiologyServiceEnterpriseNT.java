package net.cltech.enterprisent.service.impl.enterprisent.operation.microbiology;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.DiffFlags;
import com.flipkart.zjsonpatch.JsonDiff;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.TemplateDao;
import net.cltech.enterprisent.dao.interfaces.operation.list.OrderListDao;
import net.cltech.enterprisent.dao.interfaces.operation.microbiology.MicrobiologyDao;
import net.cltech.enterprisent.dao.interfaces.operation.microbiology.MicrobiologyTestOrderDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCulture;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCultureTest;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.microbiology.Procedure;
import net.cltech.enterprisent.domain.masters.microbiology.Sensitivity;
import net.cltech.enterprisent.domain.masters.test.GeneralTemplateOption;
import net.cltech.enterprisent.domain.masters.test.OptionTemplate;
import net.cltech.enterprisent.domain.masters.test.ResultTemplate;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.masters.tracking.DestinationRoute;
import net.cltech.enterprisent.domain.operation.audit.AuditEvent;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.filters.MicrobiologyFilter;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobialDetection;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyGrowth;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyTask;
import net.cltech.enterprisent.domain.operation.microbiology.ResultMicrobiology;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.tracking.VerifyDestination;
import net.cltech.enterprisent.domain.operation.widgets.StateCount;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMiddlewareService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.MediaCultureService;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.MicroorganismService;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.ProcedureService;
import net.cltech.enterprisent.service.interfaces.masters.test.SampleService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.common.CommentService;
import net.cltech.enterprisent.service.interfaces.operation.list.OrderListService;
import net.cltech.enterprisent.service.interfaces.operation.microbiology.MicrobiologyService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.service.interfaces.operation.tracking.SampleTrackingService;
import net.cltech.enterprisent.tools.ConfigurationConstants;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.enums.ListEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;

/**
 * Implementacion de microbiologia para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @since 19/01/2017
 * @see Creacion
 */
@Service
public class MicrobiologyServiceEnterpriseNT implements MicrobiologyService
{

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MicrobiologyDao dao;
    @Autowired
    private ConfigurationDao daoConfig;
    @Autowired
    private ConfigurationService configurationServices;
    @Autowired
    private SampleTrackingService sampleTrackingService;
    @Autowired
    private SampleService sampleService;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ResultsService resultService;
    @Autowired
    private MicroorganismService microorganismService;
    @Autowired
    private MediaCultureService mediaCultureService;
    @Autowired
    private ProcedureService procedureService;
    @Autowired
    private OrderListDao listDao;
    @Autowired
    private OrderListService orderListService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MicrobiologyTestOrderDao microbiologyTestOrderDao;
    @Autowired
    private IntegrationMiddlewareService integrationMiddlewareService;
    @Autowired
    private TemplateDao templateDao;
    @Autowired
    private LaboratorysByBranchesService laboratorysByBranchesService;


    @Override
    public MicrobiologyGrowth getOrderMicrobiology(Long idOrder, String codeSample) throws Exception
    {
        MicrobiologyGrowth microbiologyGrowth = new MicrobiologyGrowth();
        microbiologyGrowth.setOrder(sampleTrackingService.getOrder(idOrder, false));
        if (microbiologyGrowth.getOrder() == null || microbiologyGrowth.getOrder().getOrderNumber() == null)
        {
            throw new EnterpriseNTException(Arrays.asList("0|order"));
        }

        microbiologyGrowth.setSample(microbiologyGrowth.getOrder().getSamples().stream().filter(s -> s.getCodesample().equals(codeSample)).findAny().orElse(null));
        if (microbiologyGrowth.getSample() == null)
        {
            throw new EnterpriseNTException(Arrays.asList("0|sample"));
        }

        microbiologyGrowth.setTest(dao.getTestMicrobiologySample(microbiologyGrowth.getSample().getId(), idOrder));
        microbiologyGrowth.setTests(getTestsMicrobiologySample(microbiologyGrowth.getSample().getId(), idOrder));
        if (microbiologyGrowth.getTests() == null || microbiologyGrowth.getTests().isEmpty())
        {
            throw new EnterpriseNTException(Arrays.asList("0|The sample has not been configured"));
        }
        microbiologyGrowth.getSample().setSubSamples(sampleService.subSamples(microbiologyGrowth.getSample().getId()));
        if (microbiologyGrowth.getTest() != null)
        {
            return dao.getMicrobiologyGrowth(microbiologyGrowth);
        } else
        {
            return microbiologyGrowth;
        }
    }

    @Override
    public MicrobiologyGrowth getOrderMicrobiology(Long idOrder, Integer idTest) throws Exception
    {
        MicrobiologyGrowth microbiologyGrowth = new MicrobiologyGrowth();
        microbiologyGrowth.setOrder(sampleTrackingService.getOrder(idOrder, false));
        if (microbiologyGrowth.getOrder() == null || microbiologyGrowth.getOrder().getOrderNumber() == null)
        {
            throw new EnterpriseNTException(Arrays.asList("0|order"));
        }

        microbiologyGrowth.setTest(microbiologyGrowth.getOrder().getSamples().stream().map(sample -> getTestBasic(sample, idTest)).filter(test -> test != null).findFirst().orElse(null));
        if (microbiologyGrowth.getTest() == null || microbiologyGrowth.getTest().getId() == null)
        {
            throw new EnterpriseNTException(Arrays.asList("0|test"));
        }

        microbiologyGrowth.setSample(microbiologyGrowth.getOrder().getSamples().stream().filter(s -> Objects.equals(s.getId(), microbiologyGrowth.getTest().getSample().getId())).findAny().orElse(null));
        if (microbiologyGrowth.getSample() == null || microbiologyGrowth.getSample().getId() == null)
        {
            throw new EnterpriseNTException(Arrays.asList("0|sample"));
        }

        microbiologyGrowth.getSample().setSubSamples(sampleService.subSamples(microbiologyGrowth.getSample().getId()));
        return dao.getAntiobiogram(microbiologyGrowth);
    }

    @Override
    public List<TestBasic> getTestsMicrobiologySample(Integer idSample, Long order) throws Exception
    {
        return dao.getTestsMicrobiologySample(idSample, order);
    }

    private TestBasic getTestBasic(Sample sample, int idTest)
    {
        Test test = sample.getTests().stream().filter(t -> t.getId() == idTest).findFirst().orElse(null);
        if (test != null)
        {
            TestBasic testResult = new TestBasic();
            testResult.setId(test.getId());
            testResult.setCode(test.getCode());
            testResult.setAbbr(test.getAbbr());
            testResult.setName(test.getName());
            testResult.setSample(sample);
            return testResult;
        } else
        {
            return null;
        }
    }

    @Override
    public List<AuditEvent> listTrackingMicrobiology(Long idOrder, String codeSample) throws Exception
    {
        List<Sample> sample = sampleService.get(null, null, codeSample, null, true);
        List<AuditEvent> events = new ArrayList<>();
        if (!sample.isEmpty())
        {
            events = dao.listTrackingMicrobiology(idOrder, sample.get(0).getId());
            ObjectMapper jackson = new ObjectMapper();
            String previous = null;
            for (AuditEvent event : events)
            {
                if (previous != null)
                {
                    EnumSet<DiffFlags> flags = DiffFlags.dontNormalizeOpIntoMoveAndCopy();
                    flags.add(DiffFlags.OMIT_COPY_OPERATION);
                    flags.add(DiffFlags.OMIT_MOVE_OPERATION);

                    String differences = JsonDiff.asJson(jackson.readTree(previous), jackson.readTree(event.getCurrent()), flags).toString();
                    event.setPrevious(previous);
                    event.setDiferences(differences);
                }
                previous = event.getCurrent();
            }
        }

        return events;

    }

    @Override
    public boolean insertAntibiogramTest(MicrobiologyGrowth microbiologyGrowth) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        //audit(microbiologyGrowth, AuditOperation.ACTION_UPDATE); Validar con Edwin
        return dao.verifyMicrobiologyUpdateTest(microbiologyGrowth, timestamp);
    }

    @Override
    public boolean verifyMicrobiology(MicrobiologyGrowth microbiologyGrowth) throws Exception
    {
        int tracking = Integer.valueOf(daoConfig.get("Trazabilidad").getValue());
        int idMicrobiologyDestination = microbiologyGrowth.getDestination() == 0 ? Integer.valueOf(daoConfig.get("DestinoVerificaMicrobiologia").getValue()) : microbiologyGrowth.getDestination();
        microbiologyGrowth.setDestination(idMicrobiologyDestination);
        boolean takeSample = daoConfig.get("TomaMuestra").getValue().toLowerCase().equals("true");
        boolean growthActive = daoConfig.get("SiembraMicrobiologia").getValue().toLowerCase().equals("true");
        AuthorizedUser session = JWT.decode(request);
        microbiologyGrowth.setUser(session);
        boolean validVerific = false;
        int branch = session.getBranch();

        List<Sample> sample = sampleService.get(null, null, microbiologyGrowth.getSample().getCodesample(), null, null);
        validVerific = sampleTrackingService.isSampleCheckSimple(microbiologyGrowth.getOrder().getOrderNumber(), sample.get(0).getId());
        switch (tracking)
        {
            case Constants.SAMPLE_NOTAPPLY:
                break;
            case Constants.SAMPLE_ENTRY:
            case Constants.SAMPLE_LIST:
                if (takeSample && !validVerific)
                {
                    try
                    {
                        sampleTrackingService.sampleTracking(microbiologyGrowth.getOrder().getOrderNumber(), microbiologyGrowth.getSample().getCodesample(), LISEnum.ResultSampleState.COLLECTED.getValue(), null, false, true);
                    } catch (EnterpriseNTException e)
                    {
                        if (!(e.getErrorFields().contains("1") || e.getErrorFields().contains("2")))
                        {
                            throw e;
                        }
                    }
                }
                try
                {
                    if (!validVerific)
                    {
                        sampleTrackingService.sampleTracking(microbiologyGrowth.getOrder().getOrderNumber(), microbiologyGrowth.getSample().getCodesample(), LISEnum.ResultSampleState.CHECKED.getValue(), null, false, true);
                    }
                } catch (EnterpriseNTException e)
                {
                    if (!(e.getErrorFields().contains("1") || e.getErrorFields().contains("2")))
                    {
                        throw e;
                    }
                }
                break;
            case Constants.SAMPLE_COMPLETEVERIFICATION:
                if (takeSample && !validVerific)
                {
                    try
                    {
                        sampleTrackingService.sampleTracking(microbiologyGrowth.getOrder().getOrderNumber(), microbiologyGrowth.getSample().getCodesample(), LISEnum.ResultSampleState.COLLECTED.getValue(), null, false, true);
                    } catch (EnterpriseNTException e)
                    {
                        if (!(e.getErrorFields().contains("1") || e.getErrorFields().contains("2")))
                        {
                            throw e;
                        }
                    }
                }
                try
                {
                    if (takeSample && !validVerific)
                    {
                        sampleTrackingService.sampleTracking(microbiologyGrowth.getOrder().getOrderNumber(), microbiologyGrowth.getSample().getCodesample(), LISEnum.ResultSampleState.CHECKED.getValue(), null, false, true);
                    }
                } catch (EnterpriseNTException e)
                {
                    if (!(e.getErrorFields().contains("1") || e.getErrorFields().contains("2")))
                    {
                        throw e;
                    }
                }

                AssignmentDestination assigmentDestination = sampleTrackingService.getDestinationRoute(microbiologyGrowth.getOrder().getOrderNumber(), microbiologyGrowth.getSample().getCodesample());
                DestinationRoute initialDestination = assigmentDestination.getDestinationRoutes().stream().filter(destination -> destination.getDestination().getType().getId() == ListEnum.DestinationType.INITIAL.getValue()).findFirst().orElse(null);
                DestinationRoute microbiologyDestination = assigmentDestination.getDestinationRoutes().stream().filter(destination -> destination.getDestination().getId().equals(idMicrobiologyDestination)).findFirst().orElse(null);

                try
                {
                    if (!initialDestination.isVerify())
                    {
                        sampleTrackingService.verifyDestination(new VerifyDestination(microbiologyGrowth.getOrder().getOrderNumber(), microbiologyGrowth.getSample().getCodesample(), initialDestination.getId()));
                    }
                } catch (EnterpriseNTException e)
                {
                }
                try
                {
                    if (microbiologyDestination != null)
                    {
                        if (!microbiologyDestination.isVerify())
                        {
                            sampleTrackingService.verifyDestination(new VerifyDestination(microbiologyGrowth.getOrder().getOrderNumber(), microbiologyGrowth.getSample().getCodesample(), microbiologyDestination.getId()));
                        }
                    }
                } catch (EnterpriseNTException e)
                {
                }
                break;
            default:
                return false;
        }

        MicrobiologyGrowth growth = getOrderMicrobiology(microbiologyGrowth.getOrder().getOrderNumber(), microbiologyGrowth.getSample().getCodesample());
        if (growth.getLastTransaction() == null)
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            audit(microbiologyGrowth, AuditOperation.ACTION_INSERT);

            dao.verifyMicrobiologyUpdateTest(microbiologyGrowth, timestamp);
            dao.verifyMicrobiology(microbiologyGrowth, timestamp);
            microbiologyTestOrderDao.create(microbiologyGrowth.getSample().getId(), microbiologyGrowth.getOrder().getOrderNumber(), microbiologyGrowth.getTest().getId());
            if (!growthActive)
            {
                microbiologyGrowth.setUserGrowth(new AuthorizedUser(2));
                MediaCultureTest relationMediaCulture = mediaCultureService.listMediaCulture(microbiologyGrowth.getTest().getId());
                if (relationMediaCulture.getMediaCultures() != null)
                {
                    List<MediaCulture> defaultMedias = relationMediaCulture.getMediaCultures().stream().filter(media -> media.isDefectValue()).collect(Collectors.toList());
                    microbiologyGrowth.setMediaCultures(defaultMedias);
                }
                List<Procedure> defaultProcedures = procedureService.listTestProcedure(microbiologyGrowth.getTest().getId()).stream().map(prc -> prc.getProcedure()).filter(procedure -> procedure.isDefaultvalue()).collect(Collectors.toList());
                microbiologyGrowth.setProcedures(defaultProcedures);
                updateGrowthMicrobiology(microbiologyGrowth);
            }
            commentService.commentMicrobiology(microbiologyGrowth.getCommentsMicrobiology());
        } else
        {
            updateVerificationMicrobiology(microbiologyGrowth);
        }

        integrationMiddlewareService.sendOrderASTM(microbiologyGrowth.getOrder().getOrderNumber(), null, microbiologyGrowth.getSample().getId().toString(), Constants.MICROBIOLOGY, null, microbiologyGrowth, null, branch, false);

        CompletableFuture.runAsync(()
                ->
        {

        });

        return true;
    }

    @Override
    public int updateVerificationMicrobiology(MicrobiologyGrowth growth) throws Exception
    {
        List<String> errors = validateFields(growth);
        if (errors.isEmpty())
        {
            audit(growth, AuditOperation.ACTION_UPDATE);
            return dao.updateVerification(growth);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public int updateGrowthMicrobiology(MicrobiologyGrowth growth) throws Exception
    {
        List<String> errors = validateFields(growth);
        if (errors.isEmpty())
        {
            if (growth.getUserGrowth() == null || growth.getUserGrowth().getId() == null)
            {
                growth.setUserGrowth(trackingService.getRequestUser());
            }
            int updated = dao.updateGrowth(growth);
            dao.updateEntryTestType(growth.getOrder().getOrderNumber(), growth.getTest().getId(), (short) 1);

            dao.deleteCultureMedia(growth.getOrder().getOrderNumber(), growth.getTest().getId());
            if (growth.getMediaCultures() != null)
            {
                dao.insertCultureMedia(growth.getOrder().getOrderNumber(), growth.getTest().getId(), growth.getMediaCultures());
            }
            dao.deleteProcedure(growth.getOrder().getOrderNumber(), growth.getTest().getId());
            if (growth.getProcedures() != null)
            {
                AuthorizedUser session = JWT.decode(request);
                Order order = orderService.get(growth.getOrder().getOrderNumber());

                List<Integer> tests = growth.getProcedures().stream().filter(procedure -> procedure.isSelected()).map(test -> test.getConfirmatorytest()).distinct().collect(Collectors.toList());
                List<Integer> newTests = tests.stream().filter(test -> !order.getTests().stream().map(orderTest -> orderTest.getId()).collect(Collectors.toList()).contains(test)).collect(Collectors.toList());
                List<Test> additionalTests = new ArrayList<>();

                for (Integer test : newTests)
                {
                    if(test != 0){
                        Test orderTest = new Test();
                        orderTest.setId(test);
                        orderTest.setTestType((short) 0);

                        if (Boolean.parseBoolean(configurationServices.get(ConfigurationConstants.KEY_RATE_ACTIVE).getValue()))
                        {
                            orderTest.setRate(order.getRate());
                            orderTest.setPrice(orderService.getPriceTest(test, order.getRate().getId()).getServicePrice());
                        }
                        additionalTests.add(orderTest);
                    }
                }
                if(additionalTests.size() > 0)
                {
                    order.setTests(additionalTests);
                    resultService.addRemoveTest(order, LISEnum.ResultSampleState.CHECKED.getValue(), session);
                    for (Integer test : tests)
                    {
                        dao.updateEntryTestType(order.getOrderNumber(), test, (short) 2);
                    }
                    
                }
                dao.insertProcedures(growth.getOrder().getOrderNumber(), growth.getTest().getId(), growth.getProcedures());
            }
            audit(growth, AuditOperation.ACTION_UPDATE);
            return updated;
        } else
        {
            throw new EnterpriseNTException(errors);
        }

    }

    /**
     * Validacion de campos
     *
     * @param growth Clase para la verificación y siembra de microbiologia.
     * @return Lista de errores.
     */
    public List<String> validateFields(MicrobiologyGrowth growth)
    {
        List<String> errors = new ArrayList<>();
        if (growth.getOrder() == null || growth.getOrder().getOrderNumber() == null)
        {
            errors.add("0|Missing orden number");
        }
        if (growth.getTest() == null || growth.getTest().getId() == null)
        {
            errors.add("0|Missing test");
        }
        return errors;
    }

    /**
     * Metodo para enviar la información de la auditoria
     *
     * @param growth Clase para la verificación y siembra de microbiologia.
     * @param operation Operación.
     */
    private void audit(MicrobiologyGrowth growth, String operation)
    {
        try
        {
            List<AuditOperation> audit = new ArrayList<>();
            growth.setOrder(new Order(growth.getOrder().getOrderNumber()));
            growth.setUser(new AuthorizedUser(growth.getUser().getId()));
            audit.add(new AuditOperation(growth.getOrder().getOrderNumber(), growth.getSample().getId(), null, operation, AuditOperation.TYPE_MICRO, Tools.jsonObject(growth), null, null, null, null));
            trackingService.registerOperationTracking(audit);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public int insertMicrobialDetection(MicrobialDetection microbialDetection) throws Exception
    {
        List<String> errors = new ArrayList<>();
        AuthorizedUser session = JWT.decode(request);
        microbialDetection.setUser(session);

        MicrobialDetection detection = getMicrobialDetection(microbialDetection.getOrder(), microbialDetection.getTest());
        List<Microorganism> insertMicroorganisms = new ArrayList<>();
        List<Microorganism> deleteMicroorganisms = new ArrayList<>();

        for (Microorganism microorganism : microbialDetection.getMicroorganisms())
        {
            Sensitivity sensitivity = microorganismService.getSensitivity(microorganism.getId(), microbialDetection.getTest());
            if (sensitivity == null)
            {
                sensitivity = microorganismService.getSensitivity(microorganism.getId(), null);
                if (sensitivity == null)
                {
                    errors.add("0|sensitivity|" + microorganism.getId());
                } else
                {
                    microorganism.setSensitivity(sensitivity);
                }
            } else
            {
                microorganism.setSensitivity(sensitivity);
            }
        }

        if (errors.isEmpty())
        {
            detection.setMicroorganisms(detection.getMicroorganisms().stream()
                    .filter(microorganism -> microorganism.isSelected())
                    .collect(Collectors.toList()));

            insertMicroorganisms = microbialDetection.getMicroorganisms().stream()
                    .filter(microorganism -> !detection.getMicroorganisms().contains(microorganism))
                    .collect(Collectors.toList());

            deleteMicroorganisms = detection.getMicroorganisms().stream()
                    .filter(microorganism -> microorganism.isSelected())
                    .filter(microorganism -> !microbialDetection.getMicroorganisms().contains(microorganism))
                    .collect(Collectors.toList());

            List<Microorganism> microorganismfilter =  microbialDetection.getMicroorganisms().stream()
                    .filter(microorganism -> microorganism.isSelected() == true)
                    .collect(Collectors.toList());
            
            //Eliminar Microorganismos
            microbialDetection.setMicroorganisms(deleteMicroorganisms);
            dao.deleteMicrobialDetection(microbialDetection);
            //Insertar Microorganismos
            microbialDetection.setMicroorganisms(insertMicroorganisms);
            int quantity = dao.insertMicrobialDetection(microbialDetection);

            //Auditoria
            List<AuditOperation> audit = new ArrayList<>();
            audit.add(new AuditOperation(microbialDetection.getOrder(), microbialDetection.getTest(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_MICRODETECTION, Tools.jsonObject(microbialDetection.getMicroorganisms()), null, null, null, null));
            trackingService.registerOperationTracking(audit);
            //Actualiza resultado
            ResultTest result = resultService.get(microbialDetection.getOrder()).stream().filter(test -> test.getTestId() == microbialDetection.getTest()).findAny().orElse(null);
            if (result != null)
            {   
               
                if (result.getResult() != null && result.getResult().equals(Constants.RESULT_ANTIBIOGRAM))
                {
                    result.setResult(null);
                    result.setResultChanged(true);
                    result.setUserId(session.getId());
                    result.setNewState(LISEnum.ResultTestState.ORDERED.getValue());
                    resultService.reportedTest(result);
                } else if (result.getState() < LISEnum.ResultTestState.REPORTED.getValue())
                {
                    result.setResult(Constants.RESULT_ANTIBIOGRAM);
                    result.setResultChanged(true);
                    result.setUserId(session.getId());
                    result.setNewState(LISEnum.ResultTestState.REPORTED.getValue());
                    resultService.reportedTest(result);
                }
            }
            dao.updateTestMicrobialDetection(microbialDetection.getOrder(), microbialDetection.getTest(), !microorganismfilter.isEmpty(), session.getId());
            return quantity;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public int insertMicrobialDetection(MicrobialDetection microbialDetection, AuthorizedUser session) throws Exception
    {
        List<String> errors = new ArrayList<>();

        microbialDetection.setUser(session);

        MicrobialDetection detection = getMicrobialDetection(microbialDetection.getOrder(), microbialDetection.getTest());
        List<Microorganism> insertMicroorganisms = new ArrayList<>();
        List<Microorganism> deleteMicroorganisms = new ArrayList<>();

        for (Microorganism microorganism : microbialDetection.getMicroorganisms())
        {
            Sensitivity sensitivity = microorganismService.getSensitivity(microorganism.getId(), microbialDetection.getTest());
            if (sensitivity == null)
            {
                sensitivity = microorganismService.getSensitivity(microorganism.getId(), null);
                if (sensitivity == null)
                {
                    errors.add("0|sensitivity|" + microorganism.getId());
                } else
                {
                    microorganism.setSensitivity(sensitivity);
                }
            } else
            {
                microorganism.setSensitivity(sensitivity);
            }
        }

        if (errors.isEmpty())
        {
            detection.setMicroorganisms(detection.getMicroorganisms().stream()
                    .filter(microorganism -> microorganism.isSelected())
                    .collect(Collectors.toList()));

            insertMicroorganisms = microbialDetection.getMicroorganisms().stream()
                    .filter(microorganism -> !detection.getMicroorganisms().contains(microorganism))
                    .collect(Collectors.toList());

            deleteMicroorganisms = detection.getMicroorganisms().stream()
                    .filter(microorganism -> microorganism.isSelected())
                    .filter(microorganism -> !microbialDetection.getMicroorganisms().contains(microorganism))
                    .collect(Collectors.toList());
            
            List<Microorganism> microorganismfilter =  microbialDetection.getMicroorganisms().stream()
                    .filter(microorganism -> microorganism.isSelected() == true)
                    .collect(Collectors.toList());

            //Eliminar Microorganismos
            microbialDetection.setMicroorganisms(deleteMicroorganisms);
            dao.deleteMicrobialDetection(microbialDetection);
            //Insertar Microorganismos
            microbialDetection.setMicroorganisms(insertMicroorganisms);
            int quantity = dao.insertMicrobialDetection(microbialDetection);
            //Auditoria
            List<AuditOperation> audit = new ArrayList<>();
            audit.add(new AuditOperation(microbialDetection.getOrder(), microbialDetection.getTest(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_MICRODETECTION, Tools.jsonObject(microbialDetection), null, null, null, null));
            trackingService.registerOperationTracking(audit, session.getId());
            //Actualiza resultado
            ResultTest result = resultService.get(microbialDetection.getOrder()).stream().filter(test -> test.getTestId() == microbialDetection.getTest()).findAny().orElse(null);
            if (result != null)
            {
                if (result.getState() < LISEnum.ResultTestState.REPORTED.getValue())
                {
                    result.setResult(Constants.RESULT_ANTIBIOGRAM);
                    result.setResultChanged(true);
                    result.setUserId(session.getId());
                    result.setNewState(LISEnum.ResultTestState.REPORTED.getValue());
                    resultService.reportedTest(result, session.getId());
                }

            }
            dao.updateTestMicrobialDetection(microbialDetection.getOrder(), microbialDetection.getTest(), !microorganismfilter.isEmpty(), session.getId());
            return quantity;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public MicrobialDetection getMicrobialDetection(Long idOrder, Integer idTest) throws Exception
    {
        MicrobialDetection microbialDetection = new MicrobialDetection();

        microbialDetection.setOrder(idOrder);
        microbialDetection.setTest(idTest);

        List<Microorganism> list = dao.getByTest(idOrder, idTest);

        if (list.size() > 0)
        {
            microbialDetection.setMicroorganisms(list);
        } else
        {
            microbialDetection.setMicroorganisms(dao.getMicrobialDetection(idOrder, idTest));
        }
        return microbialDetection;
    }

    @Override
    public Microorganism getMicrobialDetectionMicroorganism(Long idOrder, Integer idTest, Integer idMicroorganism) throws Exception
    {
        return dao.getMicrobialDetectionMicroorganism(idOrder, idTest, idMicroorganism);
    }

    @Override
    public StateCount getPendingStateCount(MicrobiologyFilter microbiologyFilter) throws Exception
    {
        StateCount count = new StateCount();

        List<ResultTest> resultList = listResultTestMicrobiology(microbiologyFilter);

        count.setTotal(resultList.stream().mapToLong(result -> result.getOrder()).distinct().count());
        count.setResult(
                resultList.stream()
                        .filter(result -> result.getSampleState() == LISEnum.ResultSampleState.CHECKED.getValue())
                        .filter(result -> matchStates(result, Arrays.asList(0, 1)))
                        .mapToLong(result -> result.getOrder())
                        .distinct().count()
        );
        count.setPreview(
                resultList.stream()
                        .filter(result -> matchStates(result, Arrays.asList(2)))
                        .mapToLong(result -> result.getOrder())
                        .distinct().count()
        );
        count.setValidation(
                resultList.stream()
                        .filter(result -> matchStates(result, Arrays.asList(3))) // Arrays.asList(2, 3)
                        .mapToLong(result -> result.getOrder())
                        .distinct().count()
        );

        return count;

    }

    @Override
    public List<ResultTest> listResultTestMicrobiology(MicrobiologyFilter microbiologyFilter) throws Exception
    {
        List<ResultTest> results;
        ResultFilter resultFilter = new ResultFilter();
        resultFilter.setUserId(JWT.decode(request).getId());
        resultFilter.setFilterId(microbiologyFilter.getRangeType());
        resultFilter.setFirstOrder(microbiologyFilter.getInit());
        resultFilter.setLastOrder(microbiologyFilter.getEnd());
        resultFilter.setApplyGrowth(true);
        if (microbiologyFilter.getTest() != null && microbiologyFilter.getTest() != 0)
        {
            resultFilter.setTestsId(Arrays.asList(microbiologyFilter.getTest()));
        }
        resultFilter = setPendingStates(microbiologyFilter.getPendingStates(), resultFilter);
        List<Integer> sampleStates = resultFilter.getSampleStates();
        List<Integer> testStates = resultFilter.getTestStates();

        results = resultService.getTests(resultFilter);
        System.out.println("Cantidad de examenes sin filtro: " + results.size());
        return results.stream()
                .filter(result -> result.getEntryTestType() != null)
                .filter(result -> microbiologyFilter.getReport() == null || microbiologyFilter.getReport() != 1 || result.getEntryTestType() == 1)
                .filter(result -> microbiologyFilter.getReport() == null || microbiologyFilter.getReport() != 2 || result.getEntryTestType() == 2)
                .filter(result -> matchSampleStates(result, sampleStates))
                .filter(result -> matchStates(result, testStates))
                .filter(result -> microbiologyFilter.getCodeSample() == null || microbiologyFilter.getCodeSample().isEmpty() || result.getSampleCode().equalsIgnoreCase(microbiologyFilter.getCodeSample()))
                .collect(Collectors.toList());
    }

    /**
     * Asigna estados pendientes
     *
     * @param test Examen a evaluar
     * @param filterStates lista de estados
     *
     * @return true si cumple con todos los estados
     */
    private ResultFilter setPendingStates(List<Integer> pendingStates, ResultFilter filter)
    {
        filter.setSampleStates(new ArrayList<>());
        filter.setTestStates(new ArrayList<>());
        if (pendingStates == null)
        {
            pendingStates = new ArrayList<>();
        }

        for (Integer state : pendingStates)
        {
            switch (state)
            {
                case 1://Pendientes resultado
                    filter.getSampleStates().add(LISEnum.ResultSampleState.CHECKED.getValue());
                    filter.getTestStates().add(LISEnum.ResultTestState.ORDERED.getValue());
                    filter.getTestStates().add(LISEnum.ResultTestState.RERUN.getValue());
//                    filter.getSampleState().add(LISEnum.ResultSampleState.ORDERED.getValue());
                    break;
                case 2://Pendiente prevalidacion
                    filter.getTestStates().add(LISEnum.ResultTestState.REPORTED.getValue());
                    break;
                case 3://Pendiente Validación
                    filter.getTestStates().add(LISEnum.ResultTestState.PREVIEW.getValue());
                    //filter.getTestStates().add(LISEnum.ResultTestState.REPORTED.getValue());
                    break;
            }
        }
        return filter;
    }

    /**
     * Indica si el examen cumple con los estados enviados
     *
     * @param test Examen a evaluar
     * @param filterStates lista de estados
     *
     * @return true si cumple con todos los estados
     */
    private boolean matchStates(ResultTest test, List<Integer> filterStates)
    {
        filterStates = filterStates == null ? new ArrayList<>() : filterStates;
        return filterStates.isEmpty() || filterStates.contains(test.getState());
    }

    /**
     * Indica si el examen cumple con los estados de la muestra
     *
     * @param test Examen a evaluar
     * @param filterSampleStates lista de estados
     *
     * @return true si cumple con todos los estados
     */
    private boolean matchSampleStates(ResultTest test, List<Integer> filterSampleStates)
    {
        filterSampleStates = filterSampleStates == null ? new ArrayList<>() : filterSampleStates;
        return filterSampleStates.isEmpty() || filterSampleStates.contains(test.getSampleState());
    }

    @Override
    public List<ResultMicrobiology> listResultMicrobiologySensitivity(Integer idMicrobialDetection, Long order) throws Exception
    {
        return dao.listResultMicrobiologySensitivity(idMicrobialDetection, order);
    }

    @Override
    public List<Order> listResultOrderMicrobiology(MicrobiologyFilter microbiologyFilter) throws Exception
    {
        List<Long> orders = listResultTestMicrobiology(microbiologyFilter).stream()
                .map(result -> result.getOrder())
                .collect(Collectors.toList());

        System.out.println("Cantidad de examenes o ordenes con filtros: " + orders.size());
        if (orders.isEmpty())
        {
            return new ArrayList<>();
        } else
        {
            int idbranch = JWT.decode(request).getBranch();
            List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

            return listDao.list(microbiologyFilter.getInit(), microbiologyFilter.getEnd(), 2, new ArrayList<>(), orders, null, 0, 0, laboratorys, idbranch);
        }
    }

    @Override
    public int insertResultMicrobiologySensitivity(Microorganism microorganism, Long order) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        AuthorizedUser session = JWT.decode(request);

        List<ResultMicrobiology> insertAntibiotics = new ArrayList<>(0);
        List<ResultMicrobiology> updateAntibiotics = new ArrayList<>(0);
        List<ResultMicrobiology> deleteAntibiotics = new ArrayList<>(0);
        int quantity = 0;

        List<AuditOperation> audit = new ArrayList<>();
        MicrobialDetection microbialDetection = dao.getMicrobialDetectionGeneral(microorganism.getIdMicrobialDetection(), order);
        for (ResultMicrobiology result : microorganism.getResultsMicrobiology())
        {
            if (result.isSelected())
            {
                ResultMicrobiology resultMicrobiology = dao.getResultMicrobiologySensitivity(microorganism.getIdMicrobialDetection(), result.getIdAntibiotic(), order);
                if (resultMicrobiology != null)
                {
                    if (Objects.equals(result.getCmi(), resultMicrobiology.getCmi()) && Objects.equals(result.getInterpretationCMI(), resultMicrobiology.getInterpretationCMI()))
                    {
                        result.setDateCMI(resultMicrobiology.getDateCMI());
                        result.setUserCMI(resultMicrobiology.getUserCMI());
                    } else
                    {
                        result.setDateCMI(timestamp);
                        result.setUserCMI(session);
                    }

                    if (Objects.equals(result.getCmiM(), resultMicrobiology.getCmiM()) && Objects.equals(result.getInterpretationCMIM(), resultMicrobiology.getInterpretationCMIM()))
                    {
                        result.setDateCMIM(resultMicrobiology.getDateCMIM());
                        result.setUserCMIM(resultMicrobiology.getUserCMIM());
                    } else
                    {
                        result.setDateCMIM(timestamp);
                        result.setUserCMIM(session);
                    }

                    if (Objects.equals(result.getDisk(), resultMicrobiology.getDisk()) && Objects.equals(result.getInterpretationDisk(), resultMicrobiology.getInterpretationDisk()))
                    {
                        result.setDateDisk(resultMicrobiology.getDateDisk());
                        result.setUserDisk(resultMicrobiology.getUserDisk());
                    } else
                    {
                        result.setDateDisk(timestamp);
                        result.setUserDisk(session);
                    }
                    updateAntibiotics.add(result);
                } else
                {
                    if (!((result.getCmi() == null || result.getCmi().isEmpty()) && (result.getInterpretationCMI() == null || result.getInterpretationCMI().isEmpty()) && (result.getCmiM() == null || result.getCmiM().isEmpty()) && (result.getInterpretationCMIM() == null || result.getInterpretationCMIM().isEmpty()) && (result.getDisk() == null || result.getDisk().isEmpty()) && (result.getInterpretationDisk() == null || result.getInterpretationDisk().isEmpty())))
                    {
                        if ((result.getCmi() != null && !result.getCmi().isEmpty()) || (result.getInterpretationCMI() != null && !result.getInterpretationCMI().isEmpty()))
                        {
                            result.setDateCMI(timestamp);
                            result.setUserCMI(session);
                        }

                        if ((result.getCmiM() != null && !result.getCmiM().isEmpty()) || (result.getInterpretationCMIM() != null && !result.getInterpretationCMIM().isEmpty()))
                        {
                            result.setDateCMIM(timestamp);
                            result.setUserCMIM(session);
                        }

                        if ((result.getDisk() != null && !result.getDisk().isEmpty()) || (result.getInterpretationDisk() != null && !result.getInterpretationDisk().isEmpty()))
                        {
                            result.setDateDisk(timestamp);
                            result.setUserDisk(session);
                        }
                        insertAntibiotics.add(result);

                    }
                }
            } else
            {
                deleteAntibiotics.add(result);
            }
        }
        if (insertAntibiotics.size() > 0)
        {
            audit.add(new AuditOperation(microbialDetection.getOrder(), microbialDetection.getTest(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_MICRODETECTION, Tools.jsonObject(insertAntibiotics), null, null, null, null));
            quantity = dao.insertAntibioticsMicrobialDetection(microorganism.getIdMicrobialDetection(), insertAntibiotics, microbialDetection.getOrder());
        }
        quantity = quantity + dao.updateAntibioticsMicrobialDetection(microorganism.getIdMicrobialDetection(), updateAntibiotics, microbialDetection.getOrder());
        dao.deleteAntibioticsMicrobialDetection(microorganism.getIdMicrobialDetection(), deleteAntibiotics, microbialDetection.getOrder());

        MicrobialDetection microbialDetectionDetail = dao.getMicrobialDetection(microorganism.getIdMicrobialDetection(), microbialDetection.getOrder());
        String commentMicrobialDetection = dao.getCommentMicrobialDetection(microbialDetectionDetail.getOrder(), microbialDetectionDetail.getTest(), microorganism.getIdMicrobialDetection());
        commentMicrobialDetection = commentMicrobialDetection == null ? "" : commentMicrobialDetection;
        if (!commentMicrobialDetection.equals(microorganism.getComment()))
        {
            microorganism.setUser(session);
            microorganism = dao.updateCommentMicrobialDetection(microorganism, microbialDetectionDetail.getOrder());
            audit.add(new AuditOperation(microbialDetection.getOrder(), microbialDetection.getTest(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_MICRODETECTIONCOMMENT, Tools.jsonObject(microorganism.getComment()), null, null, null, null));
        }
        //Auditoria

        /*for (Microorganism micro : microbialDetectionDetail.getMicroorganisms())
        {
            if (micro.getResultsMicrobiology() != null)
            {
                micro.setResultsMicrobiology(micro.getResultsMicrobiology().stream()
                        .filter(m -> m.isSelected())
                        .collect(Collectors.toList()));
            }
        }*/
        //Actualiza resultado
        ResultTest result = resultService.get(microbialDetectionDetail.getOrder()).stream().filter(test -> test.getTestId() == microbialDetectionDetail.getTest()).findAny().orElse(null);
        if (result != null)
        {
            if (result.getState() < LISEnum.ResultTestState.REPORTED.getValue())
            {
                result.setResult(Constants.RESULT_ANTIBIOGRAM);
                result.setResultChanged(true);
                result.setUserId(session.getId());
                result.setNewState(LISEnum.ResultTestState.REPORTED.getValue());
                resultService.reportedTest(result);
            }
        }

        trackingService.registerOperationTracking(audit);
        return quantity;
    }

    @Override
    public int insertResultMicrobiologySensitivity(Microorganism microorganism, AuthorizedUser session, Long order) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        List<ResultMicrobiology> insertAntibiotics = new ArrayList<>(0);
        List<ResultMicrobiology> updateAntibiotics = new ArrayList<>(0);
        List<ResultMicrobiology> deleteAntibiotics = new ArrayList<>(0);
        int quantity = 0;

        for (ResultMicrobiology result : microorganism.getResultsMicrobiology())
        {
            if (result.isSelected())
            {
                ResultMicrobiology resultMicrobiology = dao.getResultMicrobiologySensitivity(microorganism.getIdMicrobialDetection(), result.getIdAntibiotic(), order);
                if (resultMicrobiology != null)
                {
                    if (Objects.equals(result.getCmi(), resultMicrobiology.getCmi()) && Objects.equals(result.getInterpretationCMI(), resultMicrobiology.getInterpretationCMI()))
                    {
                        result.setDateCMI(resultMicrobiology.getDateCMI());
                        result.setUserCMI(resultMicrobiology.getUserCMI());
                    } else
                    {
                        result.setDateCMI(timestamp);
                        result.setUserCMI(session);
                    }

                    if (Objects.equals(result.getCmiM(), resultMicrobiology.getCmiM()) && Objects.equals(result.getInterpretationCMIM(), resultMicrobiology.getInterpretationCMIM()))
                    {
                        result.setDateCMIM(resultMicrobiology.getDateCMIM());
                        result.setUserCMIM(resultMicrobiology.getUserCMIM());
                    } else
                    {
                        result.setDateCMIM(timestamp);
                        result.setUserCMIM(session);
                    }

                    if (Objects.equals(result.getDisk(), resultMicrobiology.getDisk()) && Objects.equals(result.getInterpretationDisk(), resultMicrobiology.getInterpretationDisk()))
                    {
                        result.setDateDisk(resultMicrobiology.getDateDisk());
                        result.setUserDisk(resultMicrobiology.getUserDisk());
                    } else
                    {
                        result.setDateDisk(timestamp);
                        result.setUserDisk(session);
                    }
                    updateAntibiotics.add(result);
                } else
                {
                    if (!((result.getCmi() == null || result.getCmi().isEmpty()) && (result.getInterpretationCMI() == null || result.getInterpretationCMI().isEmpty()) && (result.getCmiM() == null || result.getCmiM().isEmpty()) && (result.getInterpretationCMIM() == null || result.getInterpretationCMIM().isEmpty()) && (result.getDisk() == null || result.getDisk().isEmpty()) && (result.getInterpretationDisk() == null || result.getInterpretationDisk().isEmpty())))
                    {
                        if ((result.getCmi() != null && !result.getCmi().isEmpty()) || (result.getInterpretationCMI() != null && !result.getInterpretationCMI().isEmpty()))
                        {
                            result.setDateCMI(timestamp);
                            result.setUserCMI(session);
                        }

                        if ((result.getCmiM() != null && !result.getCmiM().isEmpty()) || (result.getInterpretationCMIM() != null && !result.getInterpretationCMIM().isEmpty()))
                        {
                            result.setDateCMIM(timestamp);
                            result.setUserCMIM(session);
                        }

                        if ((result.getDisk() != null && !result.getDisk().isEmpty()) || (result.getInterpretationDisk() != null && !result.getInterpretationDisk().isEmpty()))
                        {
                            result.setDateDisk(timestamp);
                            result.setUserDisk(session);
                        }
                        insertAntibiotics.add(result);
                    }
                }
            } else
            {
                deleteAntibiotics.add(result);
            }
        }

        quantity = dao.insertAntibioticsMicrobialDetection(microorganism.getIdMicrobialDetection(), insertAntibiotics, order);
        quantity = quantity + dao.updateAntibioticsMicrobialDetection(microorganism.getIdMicrobialDetection(), updateAntibiotics, order);
        dao.deleteAntibioticsMicrobialDetection(microorganism.getIdMicrobialDetection(), deleteAntibiotics, order);

        microorganism.setUser(session);
        microorganism = dao.updateCommentMicrobialDetection(microorganism, order);

        //Auditoria
        MicrobialDetection microbialDetection = dao.getMicrobialDetection(microorganism.getIdMicrobialDetection(), order);
        for (Microorganism micro : microbialDetection.getMicroorganisms())
        {
            if (micro.getResultsMicrobiology() != null)
            {
                micro.setResultsMicrobiology(micro.getResultsMicrobiology().stream()
                        .filter(m -> m.isSelected())
                        .collect(Collectors.toList()));
            }
        }
        //Actualiza resultado
        ResultTest result = resultService.get(microbialDetection.getOrder()).stream().filter(test -> test.getTestId() == microbialDetection.getTest()).findAny().orElse(null);
        if (result != null)
        {
            if (result.getState() < LISEnum.ResultTestState.REPORTED.getValue())
            {
                result.setResult(Constants.RESULT_ANTIBIOGRAM);
                result.setResultChanged(true);
                result.setUserId(session.getId());
                result.setNewState(LISEnum.ResultTestState.REPORTED.getValue());
                resultService.reportedTest(result);
            }
        }

        List<AuditOperation> audit = new ArrayList<>();
        audit.add(new AuditOperation(microbialDetection.getOrder(), microbialDetection.getTest(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_MICRODETECTION, Tools.jsonObject(microbialDetection), null, null, null, null));
        trackingService.registerOperationTracking(audit, session.getId());
        return quantity;
    }

    @Override
    public MicrobiologyTask insertTask(MicrobiologyTask microbiologyTask) throws Exception
    {
        List<AuditOperation> audit = new ArrayList<>();
        microbiologyTask.setUser(JWT.decode(request));
        microbiologyTask = dao.insertMicrobiologyTask(microbiologyTask);
        audit.add(new AuditOperation(microbiologyTask.getOrder(), microbiologyTask.getIdTest(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_MICROTASK, Tools.jsonObject(microbiologyTask), null, null, null, null));
        trackingService.registerOperationTracking(audit);
        return microbiologyTask;
    }

    @Override
    public MicrobiologyGrowth getOrderMicrobiologyTask(Long idOrder, String codeSample) throws Exception
    {
        MicrobiologyGrowth growth = getOrderMicrobiology(idOrder, codeSample);
        growth.getMediaCultures().forEach((mediaCulture) ->
        {
            mediaCulture.setTasks(dao.getMicrobiologyTask(idOrder, growth.getTest().getId(), mediaCulture.getId(), (short) 1));
        });

        growth.getProcedures().forEach((procedure) ->
        {
            procedure.setTasks(dao.getMicrobiologyTask(idOrder, growth.getTest().getId(), procedure.getId(), (short) 2));
        });

        return growth;
    }

    @Override
    public MicrobiologyTask updateTask(MicrobiologyTask microbiologyTask) throws Exception
    {
        List<AuditOperation> audit = new ArrayList<>();
        microbiologyTask.setUser(JWT.decode(request));
        microbiologyTask = dao.updateMicrobiologyTask(microbiologyTask);
        audit.add(new AuditOperation(microbiologyTask.getOrder(), microbiologyTask.getIdTest(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_MICROTASK, Tools.jsonObject(microbiologyTask), null, null, null, null));
        trackingService.registerOperationTracking(audit);
        return microbiologyTask;
    }

    @Override
    public List<MicrobiologyTask> listTrackingMicrobiologyTask(Long idOrder, Integer idTest) throws Exception
    {
        List<AuditEvent> audit = dao.listTrackingMicrobiologyTask(idOrder, idTest);
        List<MicrobiologyTask> tasks = new ArrayList<>();
        for (AuditEvent task : audit)
        {
            tasks.add(Tools.jsonObject(task.getCurrent(), MicrobiologyTask.class));
        }
        return tasks;
    }

    @Override
    public List<AuditEvent> listTrackingMicrobiologyCommentTask(Long idOrder, Integer idTest, Integer id) throws Exception
    {
        List<AuditEvent> audit = dao.listTrackingMicrobiologyTask(idOrder, idTest);
        List<AuditEvent> tasks = audit.stream().filter(task -> Tools.jsonObject(task.getCurrent(), MicrobiologyTask.class).getId().equals(id)).collect(Collectors.toList());

        List<AuditEvent> comments = new ArrayList<>();
        String commentPrevious = "";
        String commentCurrent;
        for (AuditEvent task : tasks)
        {
            commentCurrent = Tools.jsonObject(task.getCurrent(), MicrobiologyTask.class).getComment();

            task.setCurrent(commentCurrent);
            task.setPrevious(commentPrevious);
            comments.add(task);

            commentPrevious = commentCurrent;
        }

        return comments;
    }

    @Override
    public List<MicrobiologyTask> listTrackingMicrobiologyTaskReport(MicrobiologyFilter microbiologyFilter) throws Exception
    {
        int idUser = JWT.decode(request).getId();
        List<ResultTest> results = listResultTestMicrobiology(microbiologyFilter);

        List<MicrobiologyTask> tasks = new ArrayList<>();
        results.forEach((result) ->
        {
            tasks.addAll(dao.listMicrobiologyTaskReported(result.getOrder(), result.getTestId(), false));
            dao.updateStateMicrobiologyTask(result.getOrder(), result.getTestId(), idUser, true);
        });

        List<AuditOperation> audit = new ArrayList<>();
        for (MicrobiologyTask task : tasks)
        {
            task.setReported(true);
            audit.add(new AuditOperation(task.getOrder(), task.getIdTest(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_MICROTASK, Tools.jsonObject(task), null, null, null, null));
        }
        trackingService.registerOperationTracking(audit);
        return tasks;
    }

    @Override
    public int listTrackingMicrobiologyTaskRestart(MicrobiologyFilter microbiologyFilter) throws Exception
    {
        int idUser = JWT.decode(request).getId();
        List<ResultTest> results = listResultTestMicrobiology(microbiologyFilter);

        List<MicrobiologyTask> tasks = new ArrayList<>();
        results.forEach((result) ->
        {
            tasks.addAll(dao.listMicrobiologyTaskReported(result.getOrder(), result.getTestId(), true));
            dao.updateStateMicrobiologyTask(result.getOrder(), result.getTestId(), idUser, false);
        });

        List<AuditOperation> audit = new ArrayList<>();
        for (MicrobiologyTask task : tasks)
        {
            task.setReported(false);
            audit.add(new AuditOperation(task.getOrder(), task.getIdTest(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_MICROTASK, Tools.jsonObject(task), null, null, null, null));
        }
        trackingService.registerOperationTracking(audit);
        return tasks.size();
    }

    @Override
    public List<OrderList> listPendingMicrobiologyVerification(MicrobiologyFilter microbiologyFilter) throws Exception
    {
        List<ResultTest> results;
        List<OrderList> orders = new LinkedList<OrderList>();

        ResultFilter resultFilter = new ResultFilter();
        resultFilter.setFilterId(microbiologyFilter.getRangeType());
        resultFilter.setFirstOrder(microbiologyFilter.getInit());
        resultFilter.setLastOrder(microbiologyFilter.getEnd());

        results = dao.listMicrobiologyVerificationPending(microbiologyFilter);

        if (!results.isEmpty())
        {
            Long init = results.stream().mapToLong(record -> record.getOrder()).min().orElse(0);
            Long end = results.stream().mapToLong(record -> record.getOrder()).max().orElse(0);
            Filter orderFilter = new Filter();
            orderFilter.setRangeType(Filter.RANGE_TYPE_ORDER);
            orderFilter.setInit(init);
            orderFilter.setEnd(end);
            orders = orderListService.listFilters(orderFilter)
                    .stream()
                    .map(order -> order.setResultTest(results.stream().filter(result -> result.getOrder() == order.getOrderNumber()).collect(Collectors.toList())))
                    .filter(order -> order.getResultTest() != null && !order.getResultTest().isEmpty())
                    .collect(Collectors.toList());
        }
        return orders;
    }

    @Override
    public GeneralTemplateOption getGeneralTemplate(Long idOrder, Integer idTest)
    {
        GeneralTemplateOption general = new GeneralTemplateOption();

        List<OptionTemplate> optionsTemplate = dao.listResultTemplate(idOrder, idTest);
        for (OptionTemplate optionTemplate : optionsTemplate)
        {
            if (optionTemplate.getResult() == null && optionTemplate.getNormalResult() == null)
            {
                optionTemplate.setNormalResult(optionTemplate.getResults().stream().filter(result -> result.isReference()).findFirst().orElse(new ResultTemplate()).getResult());
            }
        }

        general.setOptionTemplates(optionsTemplate.stream().distinct().collect(Collectors.toList()));
        return general;
    }

    @Override
    public int insertResultTemplate(List<OptionTemplate> templates, Long idOrder, Integer idTest, Integer userdatabank) throws Exception
    {
        List<AuditOperation> audit = new ArrayList<>();
        AuthorizedUser session = JWT.decode(request);
        int quantity = dao.insertResultTemplate(templates, idOrder, idTest, session, userdatabank);

        ResultTest result = resultService.get(idOrder).stream().filter(test -> test.getTestId() == idTest).findAny().orElse(null);
        if (result != null)
        {
            dao.updateTestTemplate(idOrder, idTest, dao.getHasTemplate(idOrder, idTest), session.getId());

            //Order o = orderService.getAudit(idOrder).clean();
            audit.add(new AuditOperation(idOrder, idTest, null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_RESULTTEMPLATE, Tools.jsonObject(templates), null, null, null, null));
            trackingService.registerOperationTracking(audit);

            if (result.getState() < LISEnum.ResultTestState.REPORTED.getValue())
            {
                result.setResult(Constants.RESULT_COMMENT);
                result.setResultChanged(true);
                result.setUserId(userdatabank == null ? session.getId() : userdatabank);
                result.setNewState(LISEnum.ResultTestState.REPORTED.getValue());
                if(userdatabank == null){
                    resultService.reportedTest(result);
                }
                else {
                    resultService.reportedTest(result, userdatabank);
                }
            }
        }
        return quantity;
    }

}

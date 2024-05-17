package net.cltech.enterprisent.service.impl.enterprisent.operation.results;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.common.TrackingDao;
import net.cltech.enterprisent.dao.interfaces.document.DocumentDao;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationIngresoDao;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationResultadosDao;
import net.cltech.enterprisent.dao.interfaces.integration.MiddlewareDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.masters.interview.InterviewDao;
import net.cltech.enterprisent.dao.interfaces.masters.interview.QuestionDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.LiteralResultDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.TestDao;
import net.cltech.enterprisent.dao.interfaces.operation.list.OrderListDao;
import net.cltech.enterprisent.dao.interfaces.operation.microbiology.MicrobiologyDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.BillingTestDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.dao.interfaces.operation.reports.DeliveryResultDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultOrderDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultOrderDetailDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultStatisticDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultTestDao;
import net.cltech.enterprisent.dao.interfaces.operation.statistics.StatisticDao;
import net.cltech.enterprisent.dao.interfaces.tools.BarcodeDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.control.PatientResult;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardOpportunityTime;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsDemographicIngreso;
import net.cltech.enterprisent.domain.integration.qm.ItemsDiscount;
import net.cltech.enterprisent.domain.integration.resultados.DetailFilter;
import net.cltech.enterprisent.domain.integration.resultados.DetailStatus;
import net.cltech.enterprisent.domain.integration.resultados.OrderHeader;
import net.cltech.enterprisent.domain.integration.resultados.ResultHeader;
import net.cltech.enterprisent.domain.integration.resultados.ResultHeaderFilter;
import net.cltech.enterprisent.domain.integration.resultados.TestDetail;
import net.cltech.enterprisent.domain.integration.resultados.TestHeader;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.interview.Interview;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.interview.TypeInterview;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.microbiology.Sensitivity;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.domain.masters.test.AutomaticTest;
import net.cltech.enterprisent.domain.masters.test.GeneralTemplateOption;
import net.cltech.enterprisent.domain.masters.test.OptionTemplate;
import net.cltech.enterprisent.domain.masters.test.Profile;
import net.cltech.enterprisent.domain.masters.test.ReferenceValue;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.user.Email;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.common.Reason;
import net.cltech.enterprisent.domain.operation.list.RemissionLaboratory;
import net.cltech.enterprisent.domain.operation.microbiology.ResultMicrobiology;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.orders.billing.BillingTest;
import net.cltech.enterprisent.domain.operation.results.BatchResultFilter;
import net.cltech.enterprisent.domain.operation.results.CentralSystemResults;
import net.cltech.enterprisent.domain.operation.results.FindShippedOrders;
import net.cltech.enterprisent.domain.operation.results.HistoricalResult;
import net.cltech.enterprisent.domain.operation.results.HistoryFilter;
import net.cltech.enterprisent.domain.operation.results.ImageTest;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.domain.operation.results.ResultFilterByResult;
import net.cltech.enterprisent.domain.operation.results.ResultOrder;
import net.cltech.enterprisent.domain.operation.results.ResultOrderDetail;
import net.cltech.enterprisent.domain.operation.results.ResultStatistic;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.results.ResultTestBlock;
import net.cltech.enterprisent.domain.operation.results.ResultTestComment;
import net.cltech.enterprisent.domain.operation.results.ResultTestHistory;
import net.cltech.enterprisent.domain.operation.results.ResultTestPrint;
import net.cltech.enterprisent.domain.operation.results.ResultTestRepetition;
import net.cltech.enterprisent.domain.operation.results.ResultTestStateOrder;
import net.cltech.enterprisent.domain.operation.results.ResultTestValidate;
import net.cltech.enterprisent.domain.operation.results.TestHistory;
import net.cltech.enterprisent.domain.operation.results.UpdateResult;
import net.cltech.enterprisent.domain.operation.results.ValidationRelationship;
import net.cltech.enterprisent.domain.operation.tracking.TestInformationTracking;
import net.cltech.enterprisent.domain.tools.OrderTestPatientHistory;
import net.cltech.enterprisent.service.interfaces.common.ListService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationDashBoardService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMiddlewareService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.interview.InterviewService;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.SensitivityService;
import net.cltech.enterprisent.service.interfaces.masters.test.DiagnosticService;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
import net.cltech.enterprisent.service.interfaces.masters.test.WorksheetService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
import net.cltech.enterprisent.service.interfaces.operation.common.CommentService;
import net.cltech.enterprisent.service.interfaces.operation.microbiology.MicrobiologyService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.AgileStatisticService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.StatisticService;
import net.cltech.enterprisent.service.interfaces.operation.tracking.SampleTrackingService;
import net.cltech.enterprisent.service.interfaces.tools.events.EventsResultsService;
import net.cltech.enterprisent.tools.ConfigurationConstants;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.StreamFilters;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.enums.LISEnum.ResultTestPathology;
import net.cltech.enterprisent.tools.enums.LISEnum.ResultTestResultType;
import net.cltech.enterprisent.tools.enums.LISEnum.ResultTestState;
import net.cltech.enterprisent.tools.log.events.EventsLog;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import net.cltech.enterprisent.tools.log.results.ResultsLog;
import org.mariuszgromada.math.mxparser.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Implementa los servicios de órdenes en el módulo de registro de resultados
 *
 * @version 1.0.0
 * @author jblanco
 * @since 04/07/2017
 * @see Creación
 */
@Service
public class ResultServiceEnterpriseNT implements ResultsService
{

    @Autowired
    private IntegrationMiddlewareService integrationMiddlewareService;
    @Autowired
    public DocumentDao documentDao;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ResultStatisticDao resultStatisticDao;
    @Autowired
    private ResultOrderDao resultOrderDao;
    @Autowired
    private ResultTestDao resultTestDao;
    @Autowired
    private TestDao testDao;
    @Autowired
    private ResultDao resultDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ResultOrderDetailDao resultOrderDetailDao;
    @Autowired
    private AgileStatisticService agileStatisticService;
    @Autowired
    private SampleTrackingService sampleTrackingService;
    @Autowired
    private OrderService serviceOrder;
    @Autowired
    private OrderListDao listDao;
    @Autowired
    private ConfigurationService configurationServices;
    @Autowired
    private MicrobiologyService microbiologyService;
    @Autowired
    private InterviewDao surveyDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private IntegrationDashBoardService dashBoardService;
    @Autowired
    private LiteralResultDao literalResultDao;
    @Autowired
    private ListService listServices;
    @Autowired
    private MiddlewareDao middlewareDao;
    @Autowired
    private EventsResultsService eventsResultsService;
    @Autowired
    private WorksheetService worksheetService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private StatisticDao statisticDao;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    DemographicService demographicService;
    @Autowired
    private OrdersDao orderDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ResultsService resultService;
    @Autowired
    private TestService testService;
    @Autowired
    private BillingTestDao billingTestDao;
    @Autowired
    private CommentService commentService;
    @Autowired
    private DiagnosticService diagnosticService;
    @Autowired
    private UserService userService;
    @Autowired
    private SensitivityService sensitivityService;
    @Autowired
    private MicrobiologyDao microbiologyDao;
    @Autowired
    private TrackingDao trackingDao;
    @Autowired
    private IntegrationResultadosDao integrationResultadosDao;
    @Autowired
    private DeliveryResultDao deliveryResultDao;
    @Autowired
    private InterviewService interviewService;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private IntegrationIngresoDao integrationIngresoDao;
    @Autowired
    private ConfigurationDao daoConfig;
    @Autowired
    private LaboratorysByBranchesService laboratorysByBranchesService;

    @Override
    public ResultStatistic getStatistic(ResultFilter filter) throws Exception
    {
        return resultStatisticDao.statistic(filter);
    }

    @Override
    public List<ResultOrder> getOrders(ResultFilter filter) throws Exception
    {
        filter.setUser(JWT.decode(request));
        if (!filter.getWorkSheets().isEmpty() && filter.getWorkSheets().size() > 0)
        {
            final List<Integer> tests = new ArrayList<>(0);
            filter.getWorkSheets().stream().forEach((Integer w)
                    ->
            {
                try
                {
                    tests.addAll(
                            worksheetService
                                    .testsByWorkSheets(w)
                                    .stream()
                                    .map((TestBasic t) -> t.getId())
                                    .collect(Collectors.toList())
                    );
                } catch (Exception ex)
                {
                    Logger.getLogger(ResultServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            filter.setTestList(tests.stream().distinct().sorted().collect(Collectors.toList()));
        }
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);
        
        List<ResultOrder> list = resultOrderDao.list(filter, configurationServices.get("OrdenamientoRegistroResultados").getValue().equals("1"),laboratorys,idbranch);
        if (filter.getFilterByResult() != null && !filter.isTimeFilter())
        {
            for (ResultFilterByResult resultFilterByResult : filter.getFilterByResult())
            {
                if (resultFilterByResult.getOperator() != null && !resultFilterByResult.getOperator().isEmpty())
                {
                    if (resultFilterByResult.getOperator().equals("BETWEEN") || resultFilterByResult.getOperator().equals("=") || resultFilterByResult.getOperator().equals(">") || resultFilterByResult.getOperator().equals("<") || resultFilterByResult.getOperator().equals(">=") || resultFilterByResult.getOperator().equals("<=") || resultFilterByResult.getOperator().equals("<>"))
                    {
                        list = list.stream()
                                .filter(test -> filter.getFilterByResult().stream().map(result -> result.getTest()).collect(Collectors.toList()).contains(test.getTest()))
                                .filter(result -> filterResultTest(result, resultFilterByResult))
                                .collect(Collectors.toList());
                    } else
                    {
                        throw new EnterpriseNTException(Arrays.asList("Invalid Operator:" + resultFilterByResult.getOperator()));
                    }
                }
            }
        } else
        {
            //TODO: El filtro por resultado sería exclueyente con el de tiempo de oportunidad.
            if (filter.isTimeFilter())
            {
                list = list.stream().filter(result -> filterTimeTest(result) == 1).collect(Collectors.toList());
            }
        }

        list.stream().forEach((result)
                ->
        {
            result.setStateOportunity(filterTimeTest(result));
        });
        return list;
    }

    @Override
    public List<ResultTest> getTests(Long orderId) throws Exception
    {
        ResultFilter resultFilter = new ResultFilter();
        resultFilter.setApplyGrowth(true);
        resultFilter.setFilterId(2);
        int yearsQuery = Integer.parseInt(configurationServices.getValue("AniosConsultas"));
        return resultTestDao.list(resultFilter, Arrays.asList(orderId), null, yearsQuery);
    }

    @Override
    public List<ResultTest> getTests(List<Long> orderId, int idTest) throws Exception
    {
        int yearsQuery = Integer.parseInt(configurationServices.getValue("AniosConsultas"));
        if (idTest != 0)
        {
            ResultFilter resultFilter = new ResultFilter();
            List<Integer> tests = new LinkedList<>();
            tests.add(idTest);
            resultFilter.setTestsId(tests);
            resultFilter.setFilterId(2);
            return resultTestDao.list(resultFilter, orderId, null, yearsQuery);
        } else
        {
            return resultTestDao.list(null, orderId, null, yearsQuery);
        }

    }

    @Override
    public List<ResultTest> getTestsUnionDaughter(List<Long> orderId, String idOrdenHis, boolean isConfidential) throws Exception
    {
        Boolean areabypackage = Boolean.parseBoolean(configurationServices.get("verPaquetePorAreaInforme").getValue());

        if (idOrdenHis.isEmpty())
        {
            ResultFilter resultFilter = new ResultFilter();
            resultFilter.setConfidential(isConfidential);
            resultFilter.setApplyGrowth(true);
            resultFilter.setFilterId(3);
            return resultTestDao.getTestsUnionDaughter(resultFilter, orderId, areabypackage);
        } else
        {
            ResultFilter resultFilter = new ResultFilter();
            resultFilter.setIdExternalOrder(idOrdenHis);
            resultFilter.setFilterId(2);
            resultFilter.setConfidential(isConfidential);
            return resultTestDao.getTestsUnionDaughter(resultFilter, orderId, areabypackage);
        }
    }

    @Override
    public ResultTest reportedCommentTest(ResultTest obj) throws Exception
    {

            resultTestDao.updateResultComment(obj.getOrder(), obj.getTestId(),obj.getCommentResult());
            List<AuditOperation> audit = new ArrayList<>();
            audit.add(new AuditOperation(obj.getOrder(), obj.getTestId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_RESULTCOMMENT2, obj.getCommentResult(), null, null, null, null));
            trackingService.registerOperationTracking(audit);
        

        return obj;
    }

    @Override
    public ResultTest reportedTest(ResultTest obj) throws Exception
    {
        ResultTestStateOrder resul = resultTestDao.getResultState(obj.getOrder(), obj.getTestId());
        resul.setDateResult(obj.getResultDate());

        AuthorizedUser session = JWT.decode(request);

        if (resul != null)
        {

            resul.setDateResult(obj.getResultDate());
            obj.setPreviousResult(resul.getResult() != null && !resul.getResult().equals(obj.getResult()) ? resul.getResult() : resul.getPreviousResult());
            ResultTest aux = obj;

            if (obj.getResultType() == LISEnum.ResultTestResultType.NUMERIC.getValue() && isNumericResult(obj.getResult()))
            {
                BigDecimal result2 = new BigDecimal(obj.getResult());
                BigDecimal dec2 = result2.setScale(obj.getDigits(), RoundingMode.HALF_EVEN);
                obj.setResult(dec2.toString());
            }

            if (obj.getNewState() == ResultTestState.REPORTED.getValue())
            {
                String resulttemp = resul.getResult() == null ? "" : resul.getResult();
                if ((!resulttemp.equals(obj.getResult() == null ? "" : obj.getResult())) || obj.getNewState() != resul.getState() || obj.getResultComment().getCommentChanged())
                {
                    obj.setPathology(validatePathology(obj));
                    obj = resultTestDao.reported(obj);

                    if (obj.getPathology() != LISEnum.ResultTestPathology.NORMAL.getValue())
                    {
                        agileStatisticService.updateTestBranch(obj.getOrder(), obj.getTestId(), true, Constants.STATISTICS_TEST_PATHOLOGY); //Agregar valor de entrada en estadisticas rapidas
                    }

                    if (obj.getResultComment().getCommentChanged())
                    {
                        List<AuditOperation> audit = new ArrayList<>();
                        audit.add(new AuditOperation(obj.getOrder(), obj.getTestId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_RESULTCOMMENT, Tools.jsonObject(obj.getResultComment().getComment()), null, null, null, null));
                        trackingService.registerOperationTracking(audit);
                    }

                    //Trazabilidad del examen
                    if (obj.getNewState() != resul.getState())
                    {

                        List<AuditOperation> audit = new ArrayList<>();

                        audit.add(new AuditOperation(obj.getOrder(), obj.getTestId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_TEST, Tools.jsonObject(obj.getNewState()), null, null, obj.getResult(), null));
                        trackingService.registerOperationTracking(audit);

                        statisticDao.changeStateTest(obj.getOrder(), obj.getTestId(), LISEnum.ResultTestCommonState.REPORTED.getValue());
                        statisticService.testStateChanged(ResultTestState.REPORTED.getValue(), obj.getOrder(), obj.getTestId());

                        // Modificamos el indicador para esa orden antes enviada a un sistema central externo
                        resultDao.updateOrdersToTheExternalCentralSystem(obj.getOrder(), obj.getTestId());
                        resultDao.updateOrdersToTheDashboard(obj.getOrder(), obj.getTestId());

                        int state = obj.getNewState();// resultTestDao.getTestState(obj.getOrder(), obj.getTestId());
                        repeatPatientHistory(obj.getOrder(), obj.getTestId(), obj.getUserId(), state, null, null);

                        //Reportar a QM
                        AuthorizedUser userA = JWT.decode(request);
                        reportQM(obj.getTestId(), userA);
                    } else
                    {
                        if (obj.getResultChanged() == true)
                        {

                            List<AuditOperation> audit = new ArrayList<>();
                            audit.add(new AuditOperation(obj.getOrder(), obj.getTestId(), null, AuditOperation.ACTION_UPDATE_RESULT, AuditOperation.TYPE_TEST, Tools.jsonObject(obj.getNewState()), null, null, obj.getResult(), null));
                            trackingService.registerOperationTracking(audit);
                        }
                    }
                }
                // Envio al HIS (lab57c50) lo cargamos como dato en cero para poder ser re enviado el resultado al HIS
                integrationResultadosDao.changeToResultNotSentToHIS(obj.getOrder(), obj.getTestId());
            } else if (obj.getNewState() == ResultTestState.RERUN.getValue() || obj.getNewState() == ResultTestState.ORDERED.getValue())
            {
                int state = resul.getState(); //resultTestDao.getTestState(obj.getOrder(), obj.getTestId());
                //Consultar resultado de la orden
                obj.setPathology(ResultTestPathology.NORMAL.getValue());
                agileStatisticService.updateTestBranch(obj.getOrder(), obj.getTestId(), false, null); //Descontar valores en estadisticas rapidas

                if (obj.getNewState() == ResultTestState.RERUN.getValue())
                {
                    //Actualiza el estado del resultado a ordenado
                    obj = resultTestDao.rerun(obj);
                    statisticService.testStateChanged(ResultTestState.RERUN.getValue(), obj.getOrder(), obj.getTestId());
                } else if (obj.getNewState() == ResultTestState.ORDERED.getValue())
                {
                    //Actualiza el estado del resultado a ordenado
                    obj = resultTestDao.ordered(obj);
                    statisticService.testStateChanged(ResultTestState.ORDERED.getValue(), obj.getOrder(), obj.getTestId());
                }
                agileStatisticService.updateTestBranch(obj.getOrder(), obj.getTestId(), true, Constants.STATISTICS_TEST_ENTRY); //Agregar valor de entrada en estadisticas rapidas
                //Trazabalidad del examen
                List<AuditOperation> audit = new ArrayList<>();
                //Order o = serviceOrder.getAudit(obj.getOrder()).clean();
                audit.add(new AuditOperation(obj.getOrder(), obj.getTestId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_TEST, Tools.jsonObject(obj.getNewState()), null, null, null, null));
                trackingService.registerOperationTracking(audit);

                //dashBoardService.dashBoardOpportunityTime(obj.getOrder(), idsTests, DashBoardOpportunityTime.ACTION_UPDATE);
                repeatPatientHistory(obj.getOrder(), obj.getTestId(), obj.getUserId(), state, null, null);

                //Reportar repeticion
                OrderType orderType = orderDao.getOrderTypeByOrder(obj.getOrder());
                List<Test> tests = new LinkedList<>();
                if (orderType != null)
                {
                    int groupType = orderType.getCode().equals("S") ? 1 : 2;
                    List<Integer> testsIds = new LinkedList<>();
                    testsIds.add(obj.getTestId());

                    List<Integer> laboratoriesIds = new LinkedList<>();
                    laboratoriesIds.add(obj.getLaboratoryId());
                    tests = testDao.testsByBranch(session.getBranch(), testsIds, laboratoriesIds, groupType);
                }

                if (tests.size() > 0)
                {
                    integrationMiddlewareService.sendOrderASTM(obj.getOrder(), null, Integer.toString(obj.getSampleId()), Constants.CHECK, null, null, tests, session.getBranch(), true);
                }
            }

            if (obj.getNewState() == ResultTestState.RERUN.getValue() && Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("repetirResultado").getValue()) == true)
            {
                //Repetir Resultado
                CompletableFuture.runAsync(()
                        ->
                {
                    eventsResultsService.rerun(aux);
                });
            } else if (resul.getState() == ResultTestState.ORDERED.getValue() && Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("ingresarResultado").getValue()) == true)
            {
                //Ingresar Resultado
                CompletableFuture.runAsync(()
                        ->
                {
                    eventsResultsService.create(aux);
                });
            } else if (resul.getState() == ResultTestState.REPORTED.getValue() && Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("modificarResultado").getValue()) == true)
            {
                //Modificar Resultado
                CompletableFuture.runAsync(()
                        ->
                {
                    eventsResultsService.update(aux);
                });
            }
        }

        return obj;
    }

    @Override
    public ResultTest reportedTest(ResultTest obj, int usermw) throws Exception
    {
        ResultTestStateOrder resul = resultTestDao.getResultState(obj.getOrder(), obj.getTestId());

        if (resul != null)
        {
            ResultTest aux = obj;
            List<Integer> idsTests = new ArrayList<>();
            idsTests.add(obj.getTestId());
            if (obj.getNewState() == ResultTestState.REPORTED.getValue())
            {
                obj.setPathology(validatePathology(obj));
                obj = resultTestDao.reported(obj);

                if (obj.getPathology() != LISEnum.ResultTestPathology.NORMAL.getValue())
                {
                    agileStatisticService.updateTestBranch(obj.getOrder(), obj.getTestId(), true, Constants.STATISTICS_TEST_PATHOLOGY); //Agregar valor de entrada en estadisticas rapidas
                }

                if (obj.getResultComment().getCommentChanged())
                {
                    List<AuditOperation> audit = new ArrayList<>();
                    audit.add(new AuditOperation(obj.getOrder(), obj.getTestId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_RESULTCOMMENT, Tools.jsonObject(obj.getResultComment().getComment()), null, null, null, null));
                    trackingService.registerOperationTracking(audit, usermw);
                }
                //Trazabilidad del examen
                if (obj.getNewState() != resul.getState())
                {
                    //Trazabilidad del examen
                    List<AuditOperation> audit = new ArrayList<>();
                    //Order o = serviceOrder.getAudit(obj.getOrder()).clean();
                    audit.add(new AuditOperation(obj.getOrder(), obj.getTestId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_TEST, Tools.jsonObject(obj.getNewState()), null, null, obj.getResult(), null));
                    trackingService.registerOperationTracking(audit, usermw);

                    dashBoardService.dashBoardOpportunityTime(obj.getOrder(), idsTests, DashBoardOpportunityTime.ACTION_UPDATE);
                    statisticDao.changeStateTest(obj.getOrder(), obj.getTestId(), LISEnum.ResultTestCommonState.REPORTED.getValue());
                    statisticService.testStateChanged(ResultTestState.REPORTED.getValue(), obj.getOrder(), obj.getTestId());
                }
            } else if (obj.getNewState() == ResultTestState.RERUN.getValue() || obj.getNewState() == ResultTestState.ORDERED.getValue())
            {
                int state = resultTestDao.getTestState(obj.getOrder(), obj.getTestId());
                //Consultar resultado de la orden
                obj.setPathology(ResultTestPathology.NORMAL.getValue());
                agileStatisticService.updateTestBranch(obj.getOrder(), obj.getTestId(), false, null); //Descontar valores en estadisticas rapidas

                if (obj.getNewState() == ResultTestState.RERUN.getValue())
                {
                    obj = resultTestDao.rerun(obj);
                    statisticService.testStateChanged(ResultTestState.RERUN.getValue(), obj.getOrder(), obj.getTestId());
                } else if (obj.getNewState() == ResultTestState.ORDERED.getValue())
                {
                    obj = resultTestDao.ordered(obj);
                    statisticService.testStateChanged(ResultTestState.ORDERED.getValue(), obj.getOrder(), obj.getTestId());
                }

                agileStatisticService.updateTestBranch(obj.getOrder(), obj.getTestId(), true, Constants.STATISTICS_TEST_ENTRY); //Agregar valor de entrada en estadisticas rapidas

                //Trazabalidad del examen
                List<AuditOperation> audit = new ArrayList<>();
                //Order o = serviceOrder.getAudit(obj.getOrder()).clean();
                audit.add(new AuditOperation(obj.getOrder(), obj.getTestId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_TEST, Tools.jsonObject(obj.getNewState()), null, null, null, null));
                trackingService.registerOperationTracking(audit, usermw);

                dashBoardService.dashBoardOpportunityTime(obj.getOrder(), idsTests, DashBoardOpportunityTime.ACTION_UPDATE);
                repeatPatientHistory(obj.getOrder(), obj.getTestId(), obj.getUserId(), state, null, null);
            }

            if (obj.getNewState() == ResultTestState.RERUN.getValue() && Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("repetirResultado").getValue()) == true)
            {
                //Repetir Resultado
                CompletableFuture.runAsync(()
                        ->
                {
                    eventsResultsService.rerun(aux);
                });
            } else if (resul.getState() == ResultTestState.ORDERED.getValue() && Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("ingresarResultado").getValue()) == true)
            {
                //Ingresar Resultado
                CompletableFuture.runAsync(()
                        ->
                {
                    eventsResultsService.create(aux);
                });
            } else if (resul.getState() == ResultTestState.REPORTED.getValue() && Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("modificarResultado").getValue()) == true)
            {
                //Modificar Resultado
                CompletableFuture.runAsync(()
                        ->
                {
                    eventsResultsService.update(aux);
                });
            }
        }
        return obj;
    }

    @Override
    public ImageTest getResultGraphics(long order, int testId) throws Exception
    {
        return resultTestDao.getResultGraphics(order, testId);
    }

    @Override
    public List<ImageTest> getResultGraphics(long order, List<Integer> testsfilter) throws Exception
    {
        return resultTestDao.getResultGraphics(order, testsfilter);
    }

    @Override
    public boolean middlewareImages(ImageTest imageTest) throws Exception
    {
        resultTestDao.deleteResultGraphics(imageTest);

        imageTest.setImage1(decodeToImage(imageTest.getImage1()));

        if (imageTest.getImage2() != null)
        {
            imageTest.setImage2(decodeToImage(imageTest.getImage2()));
        }
        if (imageTest.getImage3() != null)
        {
            imageTest.setImage3(decodeToImage(imageTest.getImage3()));
        }
        if (imageTest.getImage4() != null)
        {
            imageTest.setImage4(decodeToImage(imageTest.getImage4()));
        }
        if (imageTest.getImage5() != null)
        {
            imageTest.setImage5(decodeToImage(imageTest.getImage5()));
        }
        return resultTestDao.insertResultGraphics(imageTest);
    }

    public static String decodeToImage(String imageString)
    {
        BufferedImage image = null;
        byte[] imageByte;
        try
        {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);

            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);

            BufferedImage result = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            result.createGraphics().drawImage(image, 0, 0, Color.white, null);

            String base64 = encodeToString(result, "jpg");
            //bis.close();
            return base64;
        } catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String encodeToString(BufferedImage image, String type)
    {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try
        {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return imageString;
    }

    @Override
    public void validatedTest(Long orderId, int testId, int userId) throws Exception
    {
        agileStatisticService.updateTestBranch(orderId, testId, true, Constants.STATISTICS_TEST_VALIDATE);
        validatedByTest(orderId, testId, userId);

        //Insertar el paciente en la tabla de control 
        PatientResult patientResult = resultOrderDao.getPatientResult(orderId, testId);
        resultDao.insertPatientResults(patientResult);
    }

    /* Metodo que permite hacer la validacion por examen de una orden y revisa
     * si ya se valido completamente la orden
     *
     * @param orderId
     * @param testId
     * @param userId
     * @throws Exception
     */
    private void validatedByTest(Long orderId, int testId, int userId) throws Exception
    {
        //Cambio de estado del examen
        int rowsAffected = resultTestDao.validated(orderId, testId, userId, new Date());
        if (rowsAffected > 0)
        {
            ResultsLog.info("VALID ORDER SUCCESFULL: " + orderId + " TEST: " + testId);
        } else
        {
            resultTestDao.validated(orderId, testId, userId, new Date());
        }

        //Registrar historico del paciente
        CompletableFuture.runAsync(() ->
        {
            try
            {
                registerPatientHistory(orderId, testId, userId);
            } catch (Exception ex)
            {
                ResultsLog.error(ex);
            }
        });

        //Trazabalidad del examen
        List<AuditOperation> audit = new ArrayList<>();
        audit.add(new AuditOperation(orderId, testId, null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_TEST, Tools.jsonObject(ResultTestState.VALIDATED.getValue()), null, null, resultTestDao.getResultTest(orderId, testId), null));
        trackingService.registerOperationTracking(audit, userId);

        //Examen a enviar al tablero de oportunidad
        List<Integer> idsTests = new LinkedList<>();
        idsTests.add(testId);

        /*if (configurationServices.getValue("IntegracionDashBoard").equalsIgnoreCase("true"))
        {
            dashBoardService.dashBoardOpportunityTime(orderId, idsTests, DashBoardOpportunityTime.ACTION_UPDATE);
        }*/

        //validar una orden por examen
        CompletableFuture.runAsync(() ->
        {
            try
            {
                //Registro de estadisticas del examen
                statisticDao.changeStateTest(orderId, testId, LISEnum.ResultTestCommonState.VALIDATED.getValue());
                statisticService.testStateChanged(ResultTestState.VALIDATED.getValue(), orderId, testId);
            } catch (Exception ex)
            {
                ResultsLog.error(ex);
            }
        });

        // Envio de correo dependiendo los valores de referencia de la prueba
        if (Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("validaPorExamen").getValue()) == true)
        {
            CompletableFuture.runAsync(()
                    ->
            {
                eventsResultsService.validatedTest(orderId, testId);
            });
        }
        //Orden completa

        if (Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("validaOrden").getValue()) == true)
        {
            if (!resultTestDao.list(orderId).stream().filter((t) -> ResultTestState.VALIDATED.getValue() != t.getState()).findAny().isPresent())
            {
                CompletableFuture.runAsync(()
                        ->
                {
                    eventsResultsService.validateOrden(orderId);
                });
            }
        }
    }

    /**
     * Este metodo se encargara de la validacion de los valores de referencia
     * con el valor de respuesta, porque segun sea esta es preciso que se envie
     * un correo
     *
     * @param result
     * @throws Exception Error al consultar con el id de la orden y id de la
     * prueba
     */
    @Override
    public void validsendMailPathology(ResultTest result) throws Exception
    {
        try
        {
            if (result.getPathology() >= 1)
            {
                //Llaves de configuracion primer evento envio de correo
                List<String> keyRecipients = new ArrayList<>();
                keyRecipients = Arrays.asList(configurationServices.get("CorreosNotificacionObligatoria").getValue().split(","));
                String keySubject = configurationServices.get("AsuntoNotificacionObligatoria").getValue();
                String keyBody = configurationServices.get("PlantillaNotificacionObligatoria").getValue();

                List<String> mandatoryTestsNotiAux = new ArrayList<>();
                List<Integer> keyTest = new ArrayList<>();
                mandatoryTestsNotiAux.addAll(Arrays.asList(configurationServices.get("PruebasNotificacionObligatoria").getValue().split(",")));
                mandatoryTestsNotiAux.stream().filter(string -> (!string.isEmpty())).map((string) -> Integer.parseInt(string.replace("[", "").replace("]", ""))).forEachOrdered((idsTests)
                        ->
                {
                    keyTest.add(idsTests);
                });

                //Llaves de configuracion segundo evento envio de correo
                List<String> keyRecipientsOne = new ArrayList<>();
                keyRecipientsOne = Arrays.asList(configurationServices.get("CorreosNotificacionObligatoriaUno").getValue().split(","));
                String keySubjectOne = configurationServices.get("AsuntoNotificacionObligatoriaUno").getValue();
                String keyBodyOne = configurationServices.get("PlantillaNotificacionObligatoriaUno").getValue();

                List<String> mandatoryTestsNotiAuxOne = new ArrayList<>();
                List<Integer> keyTestOne = new ArrayList<>();
                mandatoryTestsNotiAuxOne.addAll(Arrays.asList(configurationServices.get("PruebasNotificacionObligatoriaUno").getValue().split(",")));
                mandatoryTestsNotiAuxOne.stream().filter((string) -> (!string.isEmpty())).map((string) -> Integer.parseInt(string.replace("[", "").replace("]", ""))).forEachOrdered((idsTests)
                        ->
                {
                    keyTestOne.add(idsTests);
                });

                //Llaves de configuracion tercer evento envio de correo
                List<String> keyRecipientsTwo = new ArrayList<>();
                keyRecipientsTwo = Arrays.asList(configurationServices.get("CorreosNotificacionObligatoriaDos").getValue().split(","));
                String keySubjectTwo = configurationServices.get("AsuntoNotificacionObligatoriaDos").getValue();
                String keyBodyTwo = configurationServices.get("PlantillaNotificacionObligatoriaDos").getValue();

                List<String> mandatoryTestsNotiAuxTwo = new ArrayList<>();
                List<Integer> keyTestTwo = new ArrayList<>();
                mandatoryTestsNotiAuxTwo.addAll(Arrays.asList(configurationServices.get("PruebasNotificacionObligatoriaDos").getValue().split(",")));
                mandatoryTestsNotiAuxTwo.stream().filter((string) -> (!string.isEmpty())).map((string) -> Integer.parseInt(string.replace("[", "").replace("]", ""))).forEachOrdered((idsTests)
                        ->
                {
                    keyTestTwo.add(idsTests);
                });

                boolean coincidences = false;
                boolean coincidencesOne = false;
                boolean coincidencesTwo = false;

                StringBuilder templateSend = new StringBuilder();
                //Plantilla de resultados
                if (result.getHasTemplate())
                {
                    GeneralTemplateOption general = microbiologyService.getGeneralTemplate(result.getOrder(), result.getTestId());
                    if (general != null)
                    {
                        templateSend.append("<table>");
                        general.getOptionTemplates().forEach(template ->
                        {
                            templateSend.append("<tr>");
                            templateSend.append("<td>");
                            templateSend.append(template.getOption());
                            templateSend.append("</td>");
                            templateSend.append("<td>");
                            templateSend.append(template.getResult());
                            templateSend.append("</td>");
                            templateSend.append("</tr>");
                        });
                        templateSend.append("</table>");
                    }
                }

                if (!result.getHasAntibiogram())
                {
                    if (!keyTest.isEmpty())
                    {
                        for (Integer test : keyTest)
                        {
                            if (test == result.getTestId())
                            {
                                coincidences = true;
                                break;
                            }
                        }
                        if (coincidences)
                        {
                            sendMail(keyRecipients, keySubject, keyBody, result, templateSend.toString());
                        }
                    }
                    if (!keyTestOne.isEmpty())
                    {
                        for (Integer test : keyTestOne)
                        {
                            if (test == result.getTestId())
                            {
                                coincidencesOne = true;
                                break;
                            }
                        }
                        if (coincidencesOne)
                        {
                            sendMail(keyRecipientsOne, keySubjectOne, keyBodyOne, result, templateSend.toString());
                        }
                    }
                    if (!keyTestTwo.isEmpty())
                    {
                        for (Integer test : keyTestTwo)
                        {
                            if (test == result.getTestId())
                            {
                                coincidencesTwo = true;
                                break;
                            }
                        }
                        if (coincidencesTwo)
                        {
                            sendMail(keyRecipientsTwo, keySubjectTwo, keyBodyTwo, result, templateSend.toString());
                        }
                    }
                } else if (result.getHasAntibiogram())
                {
                    List<Sensitivity> antibiogram = sensitivityService.getAntibiogramByOrderIdByTestId(result.getOrder(), result.getTestId());
                    Sensitivity sensitivity = antibiogram.stream().findFirst().orElse(null);
                    List<ResultMicrobiology> listResultMicrobiologys = new ArrayList<>();
                    boolean hasResistent = false;
                    coincidences = false;
                    coincidencesOne = false;
                    coincidencesTwo = false;
                    // Luego cargo una lista de resultados de deteccion microbiana antibiotico
                    // Con el fin de conocer conocer que antibiograma dio resistente y hacer el envio del correo
                    listResultMicrobiologys.addAll(microbiologyDao.resultMicrobiologySensitivityByidMicrobialDetection(sensitivity.getIdMicrobialDeteccion(), result.getOrder()));
                    for (ResultMicrobiology objAux : listResultMicrobiologys)
                    {
                        if (objAux.getCmi().contains("Resistente") || objAux.getInterpretationCMI().contains("Resistente") || objAux.getCmiM().contains("Resistente") || objAux.getInterpretationCMIM().contains("Resistente") || objAux.getDisk().contains("Resistente") || objAux.getInterpretationDisk().contains("Resistente"))
                        {
                            hasResistent = true;
                            break;
                        }

                    }
                    if (hasResistent)
                    {
                        if (!keyTest.isEmpty())
                        {
                            for (Integer test : keyTest)
                            {
                                if (test == result.getTestId())
                                {
                                    coincidences = true;
                                    break;
                                }
                            }
                            if (coincidences)
                            {
                                sendMail(keyRecipients, keySubject, keyBody, result, templateSend.toString());
                            }
                        }
                        if (!keyTestOne.isEmpty())
                        {
                            for (Integer test : keyTestOne)
                            {
                                if (test == result.getTestId())
                                {
                                    coincidencesOne = true;
                                    break;
                                }
                            }
                            if (coincidencesOne)
                            {
                                sendMail(keyRecipientsOne, keySubjectOne, keyBodyOne, result, templateSend.toString());
                            }
                        }
                        if (!keyTestTwo.isEmpty())
                        {
                            for (Integer test : keyTestTwo)
                            {
                                if (test == result.getTestId())
                                {
                                    coincidencesTwo = true;
                                    break;
                                }
                            }
                            if (coincidencesTwo)
                            {
                                sendMail(keyRecipientsTwo, keySubjectTwo, keyBodyTwo, result, templateSend.toString());
                            }
                        }
                    }
                }
            }

            /*else
            {
                //Llaves de configuracion para envio de correo por retoma
                List<String> keyRecipientsTree = new ArrayList<>();
                keyRecipientsTree = Arrays.asList(configurationServices.get("CorreosNotificacionObligatoriaTres").getValue().split(","));
                String keySubjectTree = configurationServices.get("AsuntoNotificacionObligatoriaTres").getValue();
                String keyBodyTree = configurationServices.get("PlantillaNotificacionObligatoriaTres").getValue();

                List<String> mandatorySampleNotiAuxTree = new ArrayList<>();
                List<Integer> keyTestTree = new ArrayList<>();
                mandatorySampleNotiAuxTree.addAll(Arrays.asList(configurationServices.get("MuestrasNotificacionObligatoriaTres").getValue().split(",")));
                mandatorySampleNotiAuxTree.stream().filter((string) -> (!string.isEmpty())).map((string) -> Integer.parseInt(string.replace("[", "").replace("]", ""))).forEachOrdered((idSample)
                        ->
                {
                    keyTestTree.add(idSample);
                });

                getSendEmail(keyTestTree, false, keyRecipientsTree, keySubjectTree, keyBodyTree, result, isRetake, sampleID);
            }*/
        } catch (Exception e)
        {
            ResultsLog.error(e);
        }
    }

    public void getSendEmail(List<Integer> keyTest, boolean coincidences, List<String> recipients, String subject, String body, ResultTest result, boolean isRetake, int sampleID)
    {
        try
        {
            if (!keyTest.isEmpty())
            {
                for (Integer id : keyTest)
                {
                    if (isRetake && id == sampleID)
                    {
                        coincidences = true;
                        break;
                    } else
                    {
                        if (id == result.getTestId())
                        {
                            coincidences = true;
                            break;
                        }
                    }
                }
                if (coincidences)
                {
                    //sendMail(recipients, subject, body, result, isRetake, sampleID);
                }
            }
        } catch (Exception e)
        {
        }
    }

    /**
     * Este metodo se encargara de la validacion de los valores de referencia
     * con el valor de respuesta, porque segun sea esta es preciso que se envie
     * un correo
     *
     * @param recipients
     * @param subject
     * @param result
     * @param body
     * @param templateResult
     *
     * @throws Exception Error al consultar con el id de la orden y id de la
     * prueba
     */
    public void sendMail(List<String> recipients, String subject, String body, ResultTest result, String templateResult) throws Exception
    {
        // Creo un objeto paciente, para cargarlo dependiendo al paciente al cual se le asigno dicha orden
        Patient patient = patientService.get(result.getOrder());

        if (body.contains("||TEST||"))
        {
            body = body.replaceAll("\\|\\|TEST\\|\\|", " " + result.getTestCode() + " " + result.getTestName() + " ");
        }
        if (body.contains("||PATIENT||"))
        {
            if (patient.getName2() == null && patient.getSurName() == null)
            {
                body = body.replaceAll("\\|\\|PATIENT\\|\\|", " " + patient.getName1() + " " + patient.getLastName() + " ");
            } else if (patient.getName2() == null)
            {
                body = body.replaceAll("\\|\\|PATIENT\\|\\|", " " + patient.getName1() + " " + patient.getLastName() + " " + patient.getSurName() + " ");
            } else if (patient.getSurName() == null)
            {
                body = body.replaceAll("\\|\\|PATIENT\\|\\|", " " + patient.getName1() + " " + patient.getName2() + " " + patient.getLastName() + " ");
            } else
            {
                body = body.replaceAll("\\|\\|PATIENT\\|\\|", " " + patient.getName1() + " " + patient.getName2() + " " + patient.getLastName() + " " + patient.getSurName() + " ");
            }
        }

        if (body.contains("||ORDER||"))
        {
            body = body.replaceAll("\\|\\|ORDER\\|\\|", " " + result.getOrder() + " ");
        }
        if (body.contains("||HISTORY||"))
        {
            body = body.replaceAll("\\|\\|HISTORY\\|\\|", " " + patient.getPatientId() + " ");
        }
        if (body.contains("||RESULT||"))
        {
            body = body.replaceAll("\\|\\|RESULT\\|\\|", " " + ("MEMO".equals(result.getResult()) ? " " : result.getResult()) + " " + templateResult);
        }
        if (body.contains("||DATE||"))
        {
            String currentYear = DateTools.dateFormatyyyy_MM_dd_hh_mm_ss(new Date());
            body = body.replaceAll("\\|\\|DATE\\|\\|", " " + currentYear + " ");
        }

        if (body.contains("||BACTERIOLOGISTNAME||") || body.contains("||BACTERIOLOGISTCODE||"))
        {
            User uservalidate = userService.getBasicUser(result.getValidationUserId());
            if (body.contains("||BACTERIOLOGISTNAME||"))
            {
                body = body.replaceAll("\\|\\|BACTERIOLOGISTNAME\\|\\|", " " + uservalidate.getName() + " " + uservalidate.getLastName());
            }
            if (body.contains("||BACTERIOLOGISTCODE||"))
            {
                body = body.replaceAll("\\|\\|BACTERIOLOGISTCODE\\|\\|", " " + uservalidate.getIdentification());
            }
        }

        if (body.contains("||DEMO"))
        {
            Order completeOrder = orderService.get(result.getOrder());

            if (body.contains("||DEMO_-1||"))
            {
                body = body.replaceAll("\\|\\|DEMO_-1\\|\\|", " " + completeOrder.getAccount().getName() + " ");
            }

            if (body.contains("||DEMO_-2||"))
            {
                body = body.replaceAll("\\|\\|DEMO_-2\\|\\|", " " + completeOrder.getPhysician().getName() + " ");
            }

            if (body.contains("||DEMO_-3||"))
            {
                body = body.replaceAll("\\|\\|DEMO_-3\\|\\|", " " + completeOrder.getRate().getName() + " ");
            }

            if (body.contains("||DEMO_-4||"))
            {
                body = body.replaceAll("\\|\\|DEMO_-4\\|\\|", " " + completeOrder.getType().getName() + " ");
            }

            if (body.contains("||DEMO_-5||"))
            {
                body = body.replaceAll("\\|\\|DEMO_-5\\|\\|", " " + completeOrder.getBranch().getName() + " ");
            }

            if (body.contains("||DEMO_-6||"))
            {
                body = body.replaceAll("\\|\\|DEMO_-6\\|\\|", " " + completeOrder.getService().getName() + " ");
            }
            if (body.contains("||DEMO_-104||"))
            {
                body = body.replaceAll("\\|\\|DEMO_-104\\|\\|", " " + patient.getSex().getEsCo() + " ");
            }

            if (body.contains("||DEMO_-10||"))
            {
                body = body.replaceAll("\\|\\|DEMO_-10\\|\\|", " " + patient.getDocumentType().getName() + " ");
            }
            if (body.contains("||DEMO_-106||"))
            {
                body = body.replaceAll("\\|\\|DEMO_-106\\|\\|", " " + patient.getEmail() + " ");
            }

            String demo;
            String demoItemValue;

            for (DemographicValue demographic : completeOrder.getAllDemographics())
            {
                if (body.contains("||DEMO"))
                {
                    demo = "DEMO_" + demographic.getIdDemographic();
                    if (body.contains("||" + demo + "||"))
                    {
                        if (demographic.isEncoded())
                        {
                            demoItemValue = demographic.getCodifiedName();
                        } else
                        {
                            demoItemValue = demographic.getValue();
                        }
                        body = body.replaceAll("\\|\\|" + demo + "\\|\\|", " " + demoItemValue + " ");
                    }
                } else
                {
                    break;
                }
            }
        }

        Email email = new Email();
        email.setRecipients(recipients);
        email.setSubject(subject);
        email.setBody(body);
        String send = userService.sendEmail(email);
        if (send.contains("error"))
        {
            System.out.println(send);
        }
    }

    /**
     * Metodo que permite hacer la validacion por examen de una orden y revisa
     * si ya se valido completamente la orden
     *
     * @param orderId
     * @param testId
     * @param userId
     * @throws Exception
     */
    @Override
    public void prevalidatedByTest(Long orderId, int testId, int userId) throws Exception
    {
        resultTestDao.prevalidated(orderId, testId, userId, new Date());
        statisticDao.changeStateTest(orderId, testId, LISEnum.ResultTestState.PREVIEW.getValue());
        statisticService.testStateChanged(ResultTestState.PREVIEW.getValue(), orderId, testId);

        //Trazabalidad del examen
        List<AuditOperation> audit = new ArrayList<>();
        audit.add(new AuditOperation(orderId, testId, null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_TEST, Tools.jsonObject(LISEnum.ResultTestState.PREVIEW.getValue()), null, null, resultTestDao.getResultTest(orderId, testId), null));
        trackingService.registerOperationTracking(audit, userId);

    }

    /**
     * Metodo que permite hacer el registro del historico del paciente
     *
     * @param orderId
     * @param testId
     * @param userId
     * @throws Exception
     */
    private void registerPatientHistory(Long orderId, int testId, int userId) throws Exception
    {
        ResultTestStateOrder result = resultTestDao.getResultState(orderId, testId);
        HistoricalResult historicalResult = resultDao.get(result.getPatientId(), testId);

        if (historicalResult == null)
        {

            historicalResult = new HistoricalResult();
            historicalResult.setTestId(testId);
            historicalResult.setPatientId(result.getPatientId());
            historicalResult.setSecondLastResultTemp(result.getResult());
            historicalResult.setSecondLastResultUserTemp(new User());
            historicalResult.getSecondLastResultUserTemp().setId(userId);
            patientService.createPatientHistory(historicalResult, userId);

        } else
        {

            repeatPatientHistory(orderId, testId, userId, ResultTestState.VALIDATED.getValue(), historicalResult, result);
        }
    }

    /**
     * Metodo que permite hacer la repeticion del resultado en historico del
     * paciente
     *
     * @param orderId
     * @param testId
     * @param userId
     * @param state
     * @param historicalResult
     * @param result
     * @throws Exception
     */
    @Override
    public void repeatPatientHistory(Long orderId, int testId, int userId, int state, HistoricalResult historicalResult, ResultTestStateOrder result) throws Exception
    {

        if (historicalResult == null)
        {
            result = resultTestDao.getResultState(orderId, testId);
            if (result != null)
            {
                historicalResult = resultDao.get(result.getPatientId(), testId);
            }
        }

        if (historicalResult != null)
        {
            switch (state)
            {
                case 4:// ResultTestState.VALIDATED.getValue():

                    historicalResult.setSecondLastResultTemp(result.getResult());
                    historicalResult.setSecondLastResultDateTemp(result.getDateResult());

                    User user = new User();
                    user.setId(result.getUserResult());
                    historicalResult.setSecondLastResultUserTemp(user);

                    patientService.updatePatientHistory(historicalResult);

                    break;
                case 2: //ResultTestState.REPORTED.getValue()

                    historicalResult.setSecondLastResultTemp(null);
                    historicalResult.setSecondLastResultDateTemp(null);
                    historicalResult.setSecondLastResultUserTemp(null);
                    patientService.updatePatientHistory(historicalResult);

                    break;
                case 0://state == ResultTestState.ORDERED.getValue()
                    if (historicalResult.getLastResult() != null)
                    {
                        historicalResult.setSecondLastResult(historicalResult.getLastResult());
                        if (historicalResult.getLastResultDate() == null)
                        {
                            OrderTestPatientHistory order = new OrderTestPatientHistory(orderId, testId, historicalResult.getPatientId());
                            order.setNumberOrderLastValidate(barcodeDao.lastOrderValidate(order));
                            historicalResult.setLastResultDate(barcodeDao.lastDateTestValidate(order));
                        }

                        historicalResult.setSecondLastResultDate(historicalResult.getLastResultDate());
                        historicalResult.setSecondLastResultUser(historicalResult.getLastResultUser());
                    }
                    if (historicalResult.getSecondLastResultTemp() != null)
                    {
                        historicalResult.setLastResult(historicalResult.getSecondLastResultTemp().equals("") ? null : historicalResult.getSecondLastResultTemp());

                        if (historicalResult.getSecondLastResultDate() != null)
                        {
                            historicalResult.setLastResultDate(historicalResult.getSecondLastResultTemp().equals("") ? null : historicalResult.getSecondLastResultDateTemp());
                        } else
                        {
                            OrderTestPatientHistory order = new OrderTestPatientHistory(orderId, testId, historicalResult.getPatientId());
                            order.setNumberOrderLastValidate(barcodeDao.lastOrderValidate(order));
                            historicalResult.setLastResultDate(barcodeDao.lastDateTestValidate(order));
                        }
                        historicalResult.setLastResultDate(historicalResult.getSecondLastResultTemp().equals("") ? null : historicalResult.getSecondLastResultDateTemp());
                        historicalResult.setLastResultUser(historicalResult.getSecondLastResultTemp().equals("") ? null : historicalResult.getSecondLastResultUserTemp());
                    }
                    historicalResult.setSecondLastResultTemp(null);
                    historicalResult.setSecondLastResultDateTemp(null);
                    historicalResult.setSecondLastResultUserTemp(null);
                    patientService.updatePatientHistory(historicalResult);
                    break;
            }
        }
    }

    @Override
    public void printedTest(Long orderId, int testId, int userId) throws Exception
    {
        agileStatisticService.updateTestBranch(orderId, testId, true, Constants.STATISTICS_TEST_PRINT);
        resultTestDao.printed(orderId, testId, userId);
        statisticDao.changeStateTest(orderId, testId, LISEnum.ResultTestState.DELIVERED.getValue());
        statisticService.testStateChanged(ResultTestState.DELIVERED.getValue(), orderId, testId);

        List<AuditOperation> audit = new ArrayList<>();
        audit.add(new AuditOperation(orderId, testId, null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_TEST, Tools.jsonObject(LISEnum.ResultTestState.DELIVERED.getValue()), null, null, resultTestDao.getResultTest(orderId, testId), null));
        trackingService.registerOperationTracking(audit, userId);

        if (Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("imprimirExamen").getValue()) == true)
        {
            CompletableFuture.runAsync(()
                    ->
            {
                eventsResultsService.printedTest(orderId, testId);
            });
        }
    }

    @Override
    public int validatePathology(ResultTest obj)
    {
        //TODO-BANNER: Emplear decimales para hacer la conversión, y validar el separador.
        if (obj.getResultComment().getCommentChanged() && obj.getResultComment().getComment() != null)
        {
            if (obj.getResultComment().getComment().isEmpty())
            {
                if (obj.getResult().equals(Constants.RESULT_COMMENT))
                {
                    obj.setResult("");
                }
            } else
            {
                try
                {
                    if (obj.getResult().isEmpty())
                    {
                        obj.setResult(Constants.RESULT_COMMENT);
                    }
                } catch (Exception e)
                {
                    obj.setResult(Constants.RESULT_COMMENT);
                }
            }
        }

        if (obj.getResultChanged())
        {
            if (obj.getResult() != null && obj.getResult().isEmpty())
            {
                return ResultTestPathology.NORMAL.getValue();
            }

            if (obj.getResultType() == ResultTestResultType.NUMERIC.getValue() && isNumericResult(obj.getResult()))
            {
                BigDecimal result = new BigDecimal(obj.getResult());

                if (obj.getPanicMin() != null && obj.getPanicInterval() != null)
                {
                    if (lessThan(result, obj.getPanicMin()))
                    {
                        if (obj.getCritic() == 1)
                        {
                            return ResultTestPathology.LOW_CRITICAL.getValue();
                        } else
                        {
                            return ResultTestPathology.LOW_PANIC.getValue();
                        }
                    }
                    if (greaterThan(result, obj.getPanicMax()))
                    {
                        if (obj.getCritic() == 1)
                        {
                            return ResultTestPathology.HIGH_CRITICAL.getValue();
                        } else
                        {
                            return ResultTestPathology.HIGH_PANIC.getValue();
                        }
                    }
                }

                if (obj.getRefMin() != null && obj.getRefInterval() != null)
                {
                    if (lessThan(result, obj.getRefMin()))
                    {
                        return ResultTestPathology.LOW_REFERENCE.getValue();
                    }
                    if (greaterThan(result, obj.getRefMax()))
                    {
                        return ResultTestPathology.HIGH_REFERENCE.getValue();
                    }
                }
            } else
            {
                if (obj.getPanicLiteral() != null && obj.getPanicLiteral().equals(obj.getResult()))
                {
                    if (obj.getCritic() == 1)
                    {
                        return ResultTestPathology.CRITICAL.getValue();
                    } else
                    {
                        return ResultTestPathology.PANIC.getValue();
                    }
                }
                if (obj.getRefLiteral() != null && !obj.getRefLiteral().equals(obj.getResult()))
                {
                    return ResultTestPathology.PATOLOGY.getValue();
                }
            }
        }
        if (obj.getResultComment().getCommentChanged() && obj.getResultComment().getPathology() != ResultTestPathology.NORMAL.getValue())
        {
            return ResultTestPathology.PATOLOGY.getValue();
        }
        return ResultTestPathology.NORMAL.getValue();
    }

    public static boolean lessThan(BigDecimal x, BigDecimal y)
    {
        return (-1 == x.compareTo(y));
    }

    public static boolean greaterThan(BigDecimal x, BigDecimal y)
    {
        return (1 == x.compareTo(y));
    }

    private boolean isNumericResult(String result)
    {
        try
        {
            if (result != null)
            {
                Float.parseFloat(result);
            } else
            {
                return false;
            }
            return true;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

    @Override
    public int updateReferenceValue(Order order) throws Exception
    {

        int updated = 0;
        Patient orderFound = patientService.getBasicPatientbyOrder(order.getOrderNumber());

        List<Integer> tests = new LinkedList<Integer>();
        tests = resultDao.getTestsIdsReferenceValue(order.getOrderNumber());

        if (orderFound != null)
        {
            for (Integer test : tests)
            {
                try
                {
                    ReferenceValue reference = matchReferenceValue(orderFound, testDao.listReferenceValues(test));

                    ResultTest obj = new ResultTest();
                    obj.setResultComment(new ResultTestComment());
                    obj.setResultChanged(true);
                    obj.setResultType(resultDao.getTestTypeResult(test));
                    obj.setPanicInterval(reference.getPanicMin() == null ? null : (reference.getPanicMin() + " " + reference.getPanicMax()));
                    obj.setPanicMin(reference.getPanicMin());
                    obj.setPanicMax(reference.getPanicMax());
                    obj.setRefInterval(reference.getNormalMin() == null ? null : (reference.getNormalMin() + " " + reference.getNormalMax()));
                    obj.setRefMin(reference.getNormalMin());
                    obj.setRefMax(reference.getNormalMax());
                    short critical = reference.isCriticalCh() == true ? (short) 1 : (short) 0;
                    obj.setCritic(critical);
                    obj.setPanicLiteral(reference.getPanic().getName());
                    obj.setRefLiteral(reference.getNormal().getName());
                    obj.setResult(resultDao.getTestResult(order.getOrderNumber(), test));

                    int patology = validatePathology(obj);

                    resultDao.updateReferenceValue(order.getOrderNumber(), test, reference, patology);
                    updated++;
                } catch (Exception e)
                {
                    ResultsLog.error(e);
                }
            }

        }
        return updated;
    }

    /**
     * Obtiene el valor de refecencia que coincida con las condiciones del
     * paciente
     *
     * @param patient información del paciente
     * @param testReferenceValues valores de referencia del exámen
     *
     * @return Valos de terefencia correspondiente, null si no aplica al
     * paciente
     * @throws Exception Error
     */
    public ReferenceValue matchReferenceValue(Patient patient, List<ReferenceValue> testReferenceValues) throws Exception
    {

        LocalDate dob = Instant.ofEpochMilli(patient.getBirthday().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        long years = DateTools.getAgeInYears(dob, LocalDate.now());
        long days = DateTools.getAgeInDays(dob, LocalDate.now());

        return testReferenceValues.stream()
                .filter(reference -> reference.getRace().getId() == null || reference.getRace().getId() == 0 || Objects.equals(reference.getRace().getId(), patient.getRace().getId()))
                .filter(reference -> reference.getGender().getId().equals(42) || Objects.equals(reference.getGender().getId(), patient.getSex().getId()))
                .filter(reference -> reference.getUnitAge() == 2 || (years >= reference.getAgeMin() && years <= reference.getAgeMax()))
                .filter(reference -> reference.getUnitAge() == 1 || (days >= reference.getAgeMin() && days <= reference.getAgeMax()))
                .filter(reference -> reference.isState() == true)
                .findFirst()
                .orElse(new ReferenceValue());
    }

    @Override
    public int batchUpdateResult(BatchResultFilter filter) throws Exception
    {
        /*List<Order> foundedOrders = resultTestDao.orderResultList(filter.getInit(), filter.getEnd(), BatchResultFilter.RANGE_TYPE_ORDER, null, hasTraceability)
                .stream()
                .filter(order -> !order.getState().equals(LISEnum.ResultOrderState.CANCELED.getValue()))
                .map(order -> order.setResultTest(filterTestForUpdateResult(order.getResultTest(), filter.getTest())))
                .filter(order -> !order.getResultTest().isEmpty())
                .collect(Collectors.toList());*/

        int records = 0;
        boolean hasTraceability = configurationServices.getIntValue("Trazabilidad") > 1;

        ResultFilter filterResult = new ResultFilter();
        AuthorizedUser user = JWT.decode(request);
        
        filterResult.setFilterByDemo(filter.getFilterByDemo());
        filterResult.setFilterId(1);
        filterResult.setFirstOrder(filter.getInit());
        filterResult.setLastOrder(filter.getEnd());
        filterResult.setUser(user);

        List<Integer> tests = new ArrayList<Integer>();
        tests.add(filter.getTest());
        filterResult.setTestList(tests);
        filterResult.setOrderType(0);
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);
        List<ResultOrder> foundedOrders = resultOrderDao.list(filterResult, configurationServices.get("OrdenamientoRegistroResultados").getValue().equals("1"), laboratorys, idbranch);

        for (ResultOrder foundedOrder : foundedOrders)
        {
            ResultFilter filterResultTest = new ResultFilter();
            filterResultTest.setFilterId(1);
            filterResultTest.setFirstOrder(foundedOrder.getOrder());
            filterResultTest.setLastOrder(foundedOrder.getOrder());
            filterResultTest.setUser(user);
            filterResultTest.setTestList(tests);
            filterResultTest.setOrderType(0);

            // Obtenemos el comentario viejo y le adicionamos los nuevos parametros
            ResultTest test = resultService.getTests(filterResultTest, null).get(0);
            //ResultTest test = foundedOrder.getTestResult().get(0);
            if (test.getState() < LISEnum.ResultTestState.REPORTED.getValue())
            {
                if (test.getResultComment().getComment() == null)
                {
                    test.getResultComment().setComment(filter.getComment());
                    test.getResultComment().setCommentChanged(true);
                } else
                {
                    test.getResultComment().setComment(test.getResultComment().getComment() + "  " + filter.getComment());
                    test.getResultComment().setCommentChanged(true);
                }

                test.setResult(filter.getResult());
                test.setResultEnglish(filter.getResultEnglish());
                test.setNewState(2);
                test.setResultChanged(true);

                test.setNewState(LISEnum.ResultTestState.REPORTED.getValue());
                test.setUserId(trackingService.getRequestUser().getId());
                reportedTest(test);
                records++;
            }
        }
        return records;
    }

    /**
     * filtra examenes que tengan el id enviado y no se encuentren reportados o
     * esten reporado pero no posean comentario
     *
     * @param tests lista de examenes de una orden
     * @param idTest id de examen buscado
     *
     * @return lista de examenes
     */
    private List<ResultTest> filterTestForUpdateResult(List<ResultTest> tests, int idTest)
    {
        return tests.stream()
                .filter(result -> result.getTestId() == idTest)
                .filter(result -> result.getState() < LISEnum.ResultTestState.REPORTED.getValue() || (result.getResultComment() == null && result.getResultType() == LISEnum.ResultTestState.REPORTED.getValue()))
                .limit(1)
                .collect(Collectors.toList());
    }

    /**
     * Consulta el detalle de una orden para el registro de resultados
     *
     * @param orderId Identificador de la orden
     * @return ResultOrderDetail Detalle de resultados.
     * @throws java.lang.Exception Error en la base de datos.
     */
    @Override
    public ResultOrderDetail getOrderDetail(long orderId) throws Exception
    {
        return resultOrderDetailDao.get(orderId);
    }

    @Override
    public List<ResultTest> getTests(ResultFilter resultFilter) throws Exception
    {
        int yearsQuery = Integer.parseInt(configurationServices.getValue("AniosConsultas"));
        return resultTestDao.list(resultFilter, null, null, yearsQuery);
    }

    @Override
    public List<ResultTest> getTests(ResultFilter resultFilter, List<Long> orders) throws Exception
    {
        int yearsQuery = Integer.parseInt(configurationServices.getValue("AniosConsultas"));
        
        List<ResultTest> listtest = resultTestDao.listResultEncrypt(resultFilter, orders, LISEnum.ResultSampleState.CHECKED.getValue(), yearsQuery);
        for (ResultTest test : listtest)
        {
            test.setResult(Tools.decryptResult(test.getResultencript()));
        }
        return listtest;
    }

    @Override
    public ResultTestComment getCommentResultTests(long orderNumber, int idTest) throws Exception
    {
        return resultTestDao.getCommentResultTests(orderNumber, idTest);
    }

    /**
     * Actualiza el comentario de la orden
     *
     * @param obj Detalle de resultados.
     * @return ResultOrderDetail Detalle de resultados.
     * @throws java.lang.Exception Error en la base de datos.
     */
    @Override
    public ResultOrderDetail updateOrderDetail(ResultOrderDetail obj) throws Exception
    {
        return resultOrderDetailDao.update(obj);
    }

    @Override
    public List<ResultTestRepetition> listTestRepetitions(Long orderNumber, int testId) throws Exception
    {
        return resultTestDao.listTestReruns(orderNumber, testId);
    }

    @Override
    public List<TestHistory> listTestHistory(HistoryFilter filter) throws Exception
    {
        List<TestHistory> results = new ArrayList<>();
        int yearsQuery = Integer.parseInt(configurationServices.getValue("AniosConsultas"));
        for (Integer testId : filter.getTestId())
        {
            TestHistory item = new TestHistory();
//            List<ResultTestHistory> list = new ArrayList<>();

//            list.addAll(resultTestDao.listTestHistory(filter.getId(), testId, yearsQuery).stream().limit(20).collect(Collectors.toList()));
            
            List<ResultTestHistory> list = resultTestDao.listTestHistory(filter.getId(), testId, yearsQuery);
            list = list.stream().skip(Math.max(0, list.size() - 20)).collect(Collectors.toList());

            list.stream().filter((itemList) -> (itemList.getResultType() == 1 && isNumeric(itemList.getResult()))).forEachOrdered((itemList)
                    ->
            {
                itemList.setResultNumber(new BigDecimal(itemList.getResult()));
            });

            item.setTestId(testId);
            item.setHistory(list);
            if (!list.isEmpty())
            {
                item.setTestCode(list.get(0).getTestCode());
                item.setTestName(list.get(0).getTestName());
                item.setAbbr(list.get(0).getAbbr());
                item.setResultType(list.get(0).getResultType());
            }

            results.add(item);
        }
        return results;
    }
    
    
    @Override
    public List<TestHistory> listTestHistoryFilterUser(HistoryFilter filter) throws Exception
    {
       List<TestHistory> results = new ArrayList<>();
        int yearsQuery = Integer.parseInt(configurationServices.getValue("AniosConsultas"));
        int demographicquery = Integer.parseInt(configurationServices.getValue("DemograficoConsultaWeb"));
        for (Integer testId : filter.getTestId())
        {
            TestHistory item = new TestHistory();
//            List<ResultTestHistory> list = new ArrayList<>();

           //consultar en la tabla lab181 con el getIduser -323 cosnultar id id del item demografico 181c8
           
           //y enviar 181 enviar al metodo de consulta de historicos. 
            
//            list.addAll(resultTestDao.listTestHistoryFilterUser(filter.getId() ,testId, filter.getIduser(), filter.getTypeuser(),  yearsQuery, demographicquery).stream().limit(20).collect(Collectors.toList()));
            
            List<ResultTestHistory> list = resultTestDao.listTestHistoryFilterUser(filter.getId() ,testId, filter.getIduser(), filter.getTypeuser(),  yearsQuery, demographicquery);
            list = list.stream().skip(Math.max(0, list.size() - 20)).collect(Collectors.toList());
            
            list.stream().filter((itemList) -> (itemList.getResultType() == 1 && isNumeric(itemList.getResult()))).forEachOrdered((itemList)
                    ->
            {
                itemList.setResultNumber(new BigDecimal(itemList.getResult()));
            });

            item.setTestId(testId);
            item.setHistory(list);
            if (!list.isEmpty())
            {
                item.setTestCode(list.get(0).getTestCode());
                item.setTestName(list.get(0).getTestName());
                item.setAbbr(list.get(0).getAbbr());
                item.setResultType(list.get(0).getResultType());
            }

            results.add(item);
        }
        return results;
    }
            
            
            

    private boolean isNumeric(String value)
    {
        try
        {
            if (value != null)
            {
                new BigDecimal(value);
                return true;
            } else
            {
                return false;
            }
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

    private boolean filterResultTest(ResultOrder result, final ResultFilterByResult filter)
    {
        if (result.getTestResult() != null)
        {
            if (result.getTest() == filter.getTest())
            {
                if (isNumeric(result.getTestResult()) && isNumeric(filter.getResult1()) && (filter.getResult2() == null || isNumeric(filter.getResult2())))
                {
                    switch (filter.getOperator())
                    {
                        //TODO: Confirmar los operadores con el Front-End
                        case "BETWEEN":
                            return new BigDecimal(result.getTestResult()).compareTo(new BigDecimal(filter.getResult1())) != -1 && new BigDecimal(result.getTestResult()).compareTo(new BigDecimal(filter.getResult2())) != 1;
                        case "=":
                            return new BigDecimal(result.getTestResult()).compareTo(new BigDecimal(filter.getResult1())) == 0;
                        case ">":
                            return new BigDecimal(result.getTestResult()).compareTo(new BigDecimal(filter.getResult1())) == 1;
                        case "<":
                            return new BigDecimal(result.getTestResult()).compareTo(new BigDecimal(filter.getResult1())) == -1;
                        case ">=":
                            return new BigDecimal(result.getTestResult()).compareTo(new BigDecimal(filter.getResult1())) != -1;
                        case "<=":
                            return new BigDecimal(result.getTestResult()).compareTo(new BigDecimal(filter.getResult1())) != 1;
                        case "<>":
                            return new BigDecimal(result.getTestResult()).compareTo(new BigDecimal(filter.getResult1())) != 0;
                        default:
                            return false;
                    }
                } else
                {
                    switch (filter.getOperator())
                    {
                        //TODO: Confirmar los operadores con el Front-End
                        case "=":
                            return result.getTestResult().equals(filter.getResult1());
                        case "<>":
                            return !result.getTestResult().equals(filter.getResult1());
                        default:
                            return false;
                    }
                }
            } else
            {
                return true;
            }
        } else
        {
            return false;
        }
    }

    private int filterTimeTest(ResultOrder result)
    {
        if (result.getVerificationDate() != null)
        {
            if (result.getTime() > 0)
            {
                long auxTime = DateTools.getElapsedMinutes(result.getVerificationDate(), new Date());
                if (auxTime > result.getTime()) // Vencido
                {
                    return 1;
                } else if (auxTime > (result.getTime() / 2)) // Por Vencer
                {
                    return 2;
                }
                {
                    return 0;
                }
            } else
            {
                return 0;
            }
        } else
        {
            return 0;
        }
    }

    /**
     * Metodo que calcula el resultado del examen apartir de la formula
     * configurada
     *
     * @param list Lista de examenes
     * @param formula
     * @param digits
     * @param rounding
     * @return
     */
    private String getFormulation(ResultTestValidate list, String formula, int digits, RoundingMode rounding) throws Exception
    {
        String result = "";
        Float gender = (list.getSex().getId() == 7) ? Float.parseFloat(configurationServices.getValue("MaleValue")) : (list.getSex().getId() == 8) ? Float.parseFloat(configurationServices.getValue("FemaleValue")) : Float.parseFloat(configurationServices.getValue("UndefinedValue"));
        formula = formula.toLowerCase();
        //TODO: Confirmar la lista de operadores y funciones
        //TODO: Valida si la formula utiliza el SEXO y la RAZA
        List<String> abrr = Arrays.asList(formula.replace(" ", "").split("\\+|\\-|\\*|\\/|\\^|\\!|\\#|ln|log10|exp|sqrt"));

        try
        {
            if (formula.contains("||gender||"))
            {
                if (gender > 0)
                {
                    formula = formula.replace("||gender||", gender.toString());
                } else
                {
                    return "";
                }
            }

            if (formula.contains("||race||"))
            {
                if (list.getRace().getValue() > 0)
                {
                    formula = formula.replace("||race||", list.getRace().getValue().toString());
                } else
                {
                    return "";
                }
            }

            if (formula.contains("||size||"))
            {
                if (list.getSize() > 0)
                {
                    formula = formula.replace("||size||", list.getSize().toString());
                } else
                {
                    return "";
                }
            }

            if (formula.contains("||weight||"))
            {
                if (list.getWeight() > 0)
                {
                    formula = formula.replace("||weight||", list.getWeight().toString());
                } else
                {
                    return "";
                }
            }

            if (formula.contains("||"))
            {
                List<ResultTest> listTests = list.getTests().stream()
                        .filter(item -> abrr.stream()
                        .filter(o -> o.replace("(", "").replace(")", "")
                        .equals("||" + item.getTestId() + "||")).findFirst().isPresent())
                        .collect(Collectors.toList());

                if (!listTests.isEmpty())
                {

                    for (ResultTest listTest : listTests)
                    {
                        if (!listTest.getResult().isEmpty())
                        {
                            if (listTest.getResult() != null)
                            {
                                formula = formula.replace("||" + listTest.getTestId() + "||", listTest.getResult());
                            }
                        } else
                        {
                            return "";
                        }
                    }
                } else
                {
                    return "";
                }
            }

            Expression expression2 = new Expression(formula);
            BigDecimal result2 = new BigDecimal(expression2.calculate());
            BigDecimal dec2 = result2.setScale(digits, rounding);
            result = dec2.toString();
        } catch (Exception e)
        {

        }
        return result;
    }

    /**
     *
     * @param list Contiene la lista de pruebas para realizar la validación y la
     * entrevista de pánico.
     * @param user Usuario que realiza la acciòn
     * @param isAlarms true: No se valida y se consultan las alarmas sin
     * validar, false: Se valida y no se consultan las alarmas
     * @return Lista de pruebas validadas y las alarmas de validación
     * @throws Exception
     */
    @Override
    public ResultTestValidate validatedTests(ResultTestValidate list, AuthorizedUser user, boolean isAlarms) throws Exception
    {
        List<Test> tests = new LinkedList<>();
        List<ResultTest> panicSurveyTests = new LinkedList<>();
        List<ValidationRelationship> alarms = new LinkedList<>();

        ResultTestState state = list.isFinalValidate() ? ResultTestState.VALIDATED : ResultTestState.PREVIEW;

        for (ResultTest obj : list.getTests())
        {
            try
            {
                if ((obj.getNewState() == ResultTestState.VALIDATED.getValue() || obj.getNewState() == ResultTestState.PREVIEW.getValue()) && obj.isGrantValidate())
                {
                    try
                    {
                        if (obj.getState() == ResultTestState.REPORTED.getValue() || (list.isFinalValidate() && obj.getState() == ResultTestState.PREVIEW.getValue()))
                        {
                            if (isAlarms)
                            {
                                alarmTest(obj.getTestId(), obj.getResult(), alarms);
                            } else
                            {
                                //Prevalidacion del examen
                                if (obj.getState() == ResultTestState.REPORTED.getValue() && !list.isFinalValidate() && obj.getNewState() == ResultTestState.PREVIEW.getValue())
                                {
                                    prevalidatedByTest(obj.getOrder(), obj.getTestId(), user.getId());
                                } else
                                {
                                    validatedByTest(obj.getOrder(), obj.getTestId(), user.getId());
                                    reportedCritical(obj.getOrder(), obj.getTestId(), list.getReportedDoctor());

                                    obj.setValidationDate(new java.util.Date());
                                    obj.setValidationUserId(user.getId());

                                    obj.setState(state.getValue());
                                    if (obj.getApplyInterview() == 1)
                                    {
                                        //Exámenes para la entrevista de pánico
                                        panicSurveyTests.add(obj);
                                    }
                                    //Exámenes automáticos
                                    automaticTest(obj.getTestId(), obj.getResult(), tests, obj.getOrder());
                                    validsendMailPathology(obj);
                                }
                            }
                        }
                    } catch (Exception ex)
                    {
                        //TODO: Cómo se controla la excepción
                        ResultsLog.error(ex);
                    }

                }
            } catch (Exception ex)
            {
                Logger.getLogger(ResultServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Registro de la entrevista de pánico
        if (!isAlarms && list.getQuestions() != null && list.getQuestions().size() > 0 && panicSurveyTests.size() > 0)
        {
            //TODO: Se realizará entrevista de pánico para validaciones preliminares?
            resultTestDao.insertPanicSurvey(list.getQuestions(), panicSurveyTests, list.getOrderId());
        }
        //Registro de pruebas automáticas
        if (!tests.isEmpty())
        {
            //REVISAR LOS DAO A CONSULTAR 
            Order order = listDao.orderBranch(list.getOrderId());
            order.setTests(tests);
            addRemoveTest(order, LISEnum.ResultSampleState.CHECKED.getValue(), user);

        }

        if (isAlarms)
        {
            alarmSurvey(list.getOrderId(), alarms);

            //Informe de alerta de validación
            if (!alarms.isEmpty())
            {
                list.setAlarms(alarms.stream().distinct().collect(Collectors.toList()));
            }
        }

        return list;
    }

    /**
     *
     * @param testId identificador de la prueba
     * @param result resultado de la prueba
     * @param tests lista de pruebas que se adicionan a la orden
     */
    private void automaticTest(int testId, String result, List<Test> tests, Long order)
    {
        try
        {
            testDao.listAutomaticTest(testId).forEach((AutomaticTest filter)
                    ->
            {
                boolean add = false;
                if (isNumeric(result))
                {
                    switch (filter.getSign().getId())
                    {
                        case 56:
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult1())) != -1 && new BigDecimal(result).compareTo(new BigDecimal(filter.getResult2())) != 1;
                            break;
                        case 50:
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult1())) == 0;
                            break;
                        case 54:
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult1())) == 1;
                            break;
                        case 53:
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult1())) == -1;
                            break;
                        case 51:
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult1())) != -1;
                            break;
                        case 52:
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult1())) != 1;
                            break;
                        case 55:
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult1())) != 0;
                            break;
                        default:
                            break;
                    }
                } else
                {
                    switch (filter.getSign().getId())
                    {
                        case 50:
                            add = result.equals(filter.getResult1());
                            break;
                        case 55:
                            add = !result.equals(filter.getResult1());
                            break;
                        default:
                            break;
                    }
                }

                if (add && testId != filter.getAutomaticTest().getId())
                {
                    Test test = new Test();
                    test.setId(filter.getAutomaticTest().getId());
                    test.setTestType((short) 0);

                    try
                    {
                        if (Boolean.parseBoolean(configurationServices.get(ConfigurationConstants.KEY_RATE_ACTIVE).getValue()))
                        {
                            int rate = orderService.getRateOrder(order);
                            Rate rateorder = new Rate();
                            rateorder.setId(rate);
                            test.setRate(rateorder);
                            test.setPrice(orderService.getPriceTest(filter.getAutomaticTest().getId(), rate).getServicePrice());
                        }
                    } catch (Exception ex)
                    {
                        Logger.getLogger(ResultServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    tests.add(test);
                }
            });
        } catch (Exception ex)
        {
        }
    }

    /**
     *
     * @param testId dentificador de la prueba
     * @param result resultado de la prueba
     * @param alarms lista de alarmas que se reportan al usuario final
     */
    private void alarmTest(int testId, String result, List<ValidationRelationship> alarms)
    {
        try
        {
            List<ValidationRelationship> list = new ArrayList<>();
            list.addAll(resultTestDao.listAlarms(1, testId, -1L));

            list.forEach((ValidationRelationship filter)
                    ->
            {
                boolean add = false;
                if (isNumeric(result))
                {
                    switch (filter.getOperator())
                    {
                        case "56": //"BETWEEN"
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult())) != -1 && new BigDecimal(result).compareTo(new BigDecimal(filter.getResult2())) != 1;
                            break;
                        case "50": //"="
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult())) == 0;
                            break;
                        case "54": //">"
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult())) == 1;
                            break;
                        case "53": //"<"
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult())) == -1;
                            break;
                        case "51": //">="
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult())) != -1;
                            break;
                        case "52": //"<="
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult())) != 1;
                            break;
                        case "55": //"<>"
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult())) != 0;
                            break;
                        default:
                            break;
                    }
                } else
                {
                    switch (filter.getOperator())
                    {
                        case "50": //"="
                            add = result.equals(filter.getResult());
                            break;
                        case "55": //"<>"
                            add = !result.equals(filter.getResult());
                            break;
                        default:
                            break;
                    }
                }

                if (add)
                {
                    alarms.add(filter);
                }
            });
        } catch (Exception ex)
        {
            ResultsLog.error(ex);
        }
    }

    /**
     *
     * @param orderId dentificador de la prueba
     * @param alarms lista de alarmas que se reportan al usuario final
     */
    private void alarmSurvey(Long orderId, List<ValidationRelationship> alarms)
    {
        try
        {
            resultTestDao.listAlarms(2, -1, orderId).forEach((ValidationRelationship filter)
                    ->
            {
                boolean add = false;
                String result = filter.getSurveyText() != null && !filter.getSurveyText().isEmpty() ? filter.getSurveyText() : String.valueOf(filter.getSurveyCode());
                if (isNumeric(result))
                {
                    switch (filter.getOperator())
                    {
                        case "56": //"BETWEEN"
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult())) != -1 && new BigDecimal(result).compareTo(new BigDecimal(filter.getResult2())) != 1;
                            break;
                        case "50": //"="
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult())) == 0;
                            break;
                        case "54": //">"
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult())) == 1;
                            break;
                        case "53": //"<"
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult())) == -1;
                            break;
                        case "51": //">="
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult())) != -1;
                            break;
                        case "52": //"<="
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult())) != 1;
                            break;
                        case "55": //"<>"
                            add = new BigDecimal(result).compareTo(new BigDecimal(filter.getResult())) != 0;
                            break;
                        default:
                            break;
                    }
                } else
                {
                    switch (filter.getOperator())
                    {
                        case "50": //"="
                            add = result.equals(filter.getResult());
                            break;
                        case "55": //"<>"
                            add = !result.equals(filter.getResult());
                            break;
                        default:
                            break;
                    }
                }

                if (add)
                {
                    alarms.add(filter);
                }
            });
        } catch (Exception ex)
        {
            ResultsLog.error(ex);
        }
    }

    @Override
    public int reportTests(Order order, int idUser, String receivesPerson, Boolean sendAutomaticResult) throws Exception
    {
        if (order.getDeliveryType() == null || order.getDeliveryType().getId() == null)
        {
            throw new EnterpriseNTException(Arrays.asList("0|deliveryType"));
        }
        if (!listServices.list(Constants.LIST_DELIVERY_TYPE).contains(order.getDeliveryType()))
        {
            throw new EnterpriseNTException(Arrays.asList("1|invalid deliveryType"));
        }

        //Trazabalidad del examen
        List<AuditOperation> audit = new ArrayList<>();

        int quantity = resultTestDao.printTest(order, 0, idUser, sendAutomaticResult);

        order.getResultTest().stream().forEach(rs
                ->
        {
            try
            {
                audit.add(new AuditOperation(order.getOrderNumber(), rs.getTestId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_TEST, Tools.jsonObject(ResultTestState.DELIVERED.getValue()), null, sendAutomaticResult == true ? Tools.jsonObject(LISEnum.MailSendinType.AUTOMATIC.getValue()) : Tools.jsonObject(LISEnum.MailSendinType.MANUALLY.getValue()), resultTestDao.getResultTest(order.getOrderNumber(), rs.getTestId()), order.getDeliveryType().getId(), receivesPerson));
                int statetest = statisticDao.getTestState(order.getOrderNumber(), rs.getTestId());
                if (statetest < 5)
                {
                    statisticDao.changeStateTest(order.getOrderNumber(), rs.getTestId(), LISEnum.ResultTestCommonState.PRINTED.getValue());
                    statisticService.testStateChanged(ResultTestState.DELIVERED.getValue(), order.getOrderNumber(), rs.getTestId());
                }
            } catch (Exception ex)
            {
                Logger.getLogger(ResultServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        if (Boolean.parseBoolean(configurationServices.get("manejoEventos").getValue()) == true && Boolean.parseBoolean(configurationServices.get("imprimirExamen").getValue()) == true)
        {
            CompletableFuture.runAsync(()
                    ->
            {
                for (ResultTest test : order.getResultTest())
                {
                    eventsResultsService.printedTest(order.getOrderNumber(), test.getTestId());
                }

            });
        }

        if (idUser == -1)
        {
            trackingService.registerOperationTracking(audit);
        } else
        {
            trackingService.registerOperationTracking(audit, idUser);
        }

        return quantity;
    }

    @Override
    public List<Test> getTestToResultInOrderEntry(long order) throws Exception
    {
        List<Test> tests = resultDao.getOrderTestByResultInOrderEntry(order);
        if (!tests.isEmpty())
        {
            List<Test> testsWithLiteral = null;
            for (Test test : tests)
            {
                testsWithLiteral = tests.stream().filter(t -> Objects.equals(t.getId(), test.getId())).collect(Collectors.toList());
                testsWithLiteral.forEach((Test t)
                        ->
                {
                    test.getLiteralResults().addAll(t.getLiteralResults());
                });
            }
            return tests;
        } else
        {
            return tests;
        }
    }

    @Override
    public List<ResultTest> addRemoveTest(Order order, int type, AuthorizedUser user) throws Exception
    {

        List<Integer> tests = serviceOrder.saveTests(order, user.getId(), new Date(), order.getBranch().getId(), order.getType().getCode().equals("S") ? 1 : 2, true, true);

        CompletableFuture.runAsync(() -> statisticService.saveOrder(order.getOrderNumber()), Constants.ex);

        ResultFilter resultFilter = new ResultFilter();
        resultFilter.setFilterId(2);
        resultFilter.setTestsId(tests);
        orderService.savePricesOrder(order, false);
       
        return getTests(resultFilter, Arrays.asList(order.getOrderNumber()));
    }

    @Override
    public List<ResultTest> get(long order) throws Exception
    {
        return resultTestDao.list(order);
    }

    @Override
    public ResultTestComment getInternalComment(long order, int test) throws Exception
    {
        return resultDao.getCommentInternalResult(order, test);
    }

    @Override
    public ResultTestComment getInternalObservations(long order) throws Exception
    {
        return resultDao.getObservations(order);
    }

    @Override
    public ResultTestComment saveInternalComment(ResultTestComment comment)
    {

        try
        {
            AuthorizedUser session = JWT.decode(request);

            if (resultDao.getCommentInternalResult(comment.getOrder(), comment.getTestId()) == null)
            {
                resultDao.insertCommentInternalResult(comment, session.getId());
            } else
            {
                resultDao.updateCommentInternalResult(comment, session.getId());
            }

            List<AuditOperation> audit = new ArrayList<>();
            audit.add(new AuditOperation(comment.getOrder(), comment.getTestId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_COMMENTINTERNALRESULT, null, null, comment.getComment(), null, null));
            trackingService.registerOperationTracking(audit);

            return comment;
        } catch (Exception ex)
        {
            ResultsLog.error(ex);
            return comment;
        }
    }

    @Override
    public ResultTestComment saveObservations(ResultTestComment comment)
    {

        try
        {
            AuthorizedUser session = JWT.decode(request);
            resultDao.updateObservacion(comment, session.getId());
            List<AuditOperation> audit = new ArrayList<>();
            audit.add(new AuditOperation(comment.getOrder(), null, null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_OBSERVATIONS, null, null, comment.getComment(), null, null));
            trackingService.registerOperationTracking(audit);
            return comment;
        } catch (Exception ex)
        {
            ResultsLog.error(ex);
            return comment;
        }
    }

    @Override
    public ResultTestBlock blockTest(ResultTestBlock block) throws Exception
    {
        block.setDate(new Date());
        ResultTestBlock obj = resultTestDao.blocked(block);

        //Trazabilidad del bloqueo
        List<AuditOperation> audit = new ArrayList<>();
        audit.add(new AuditOperation(obj.getOrder(), obj.getTestId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_BLOCKTEST, block.isBlocked() ? "1" : "0", obj.getReasonId(), obj.getReasonComment(), null, null));
        trackingService.registerOperationTracking(audit);

        return obj;
    }

    @Override
    public ResultTestPrint printTest(ResultTestPrint test) throws Exception
    {

        ResultTestPrint obj = resultTestDao.print(test);

        //Trazabilidad del bloqueo
        List<AuditOperation> audit = new ArrayList<>();
        audit.add(new AuditOperation(obj.getOrder(), obj.getTestId(), null, AuditOperation.TYPE_PRINTTEST, AuditOperation.TYPE_BLOCKTEST, test.isPrint() ? "1" : "0", null, null, null, null));
        trackingService.registerOperationTracking(audit);

        return obj;
    }

    @Override
    public List<Order> addAdditional(List<Order> orders) throws Exception
    {
        for (Order order : orders)
        {
            for (ResultTest resultTest : order.getResultTest())
            {
                if (resultTest.getHasAntibiogram())
                {
                    resultTest.setMicrobialDetection(microbiologyService.getMicrobialDetection(order.getOrderNumber(), resultTest.getTestId()));
                    resultTest.getMicrobialDetection().setMicroorganisms(resultTest.getMicrobialDetection().getMicroorganisms().stream().filter(microorganism -> microorganism.isSelected()).collect(Collectors.toList()));
                    for (Microorganism microorganism : resultTest.getMicrobialDetection().getMicroorganisms())
                    {
                        microorganism.setResultsMicrobiology(microorganism.getResultsMicrobiology().stream().filter(result -> result.isSelected()).collect(Collectors.toList()));
                        microorganism.setCountResultMicrobiology(microorganism.getResultsMicrobiology().size());
                    }
                }

                if (resultTest.getHasTemplate())
                {
                    resultTest.setOptionsTemplate(microbiologyService.getGeneralTemplate(order.getOrderNumber(), resultTest.getTestId()).getOptionTemplates().stream().filter(result -> result.getResult() != null && !result.getResult().isEmpty()).collect(Collectors.toList()));
                    if (resultTest.getOptionsTemplate() != null && !resultTest.getOptionsTemplate().isEmpty())
                    {
                        for (OptionTemplate optionTemplate : resultTest.getOptionsTemplate())
                        {
                            optionTemplate.setId(null);
                            optionTemplate.setResults(null);
                        }
                    }
                }
            }
        }
        return orders;
    }

    @Override
    public List<ResultTest> addTestAdditional(List<ResultTest> resultTests) throws Exception
    {
        for (ResultTest resultTest : resultTests)
        {
            if (resultTest.getHasAntibiogram())
            {
                resultTest.setMicrobialDetection(microbiologyService.getMicrobialDetection(resultTest.getOrder(), resultTest.getTestId()));
                resultTest.getMicrobialDetection().setMicroorganisms(resultTest.getMicrobialDetection().getMicroorganisms().stream().filter(microorganism -> microorganism.isSelected()).collect(Collectors.toList()));
                for (Microorganism microorganism : resultTest.getMicrobialDetection().getMicroorganisms())
                {
                    microorganism.setResultsMicrobiology(microorganism.getResultsMicrobiology().stream().filter(result -> result.isSelected()).collect(Collectors.toList()));
                    microorganism.setCountResultMicrobiology(microorganism.getResultsMicrobiology().size());
                }
            }

            if (resultTest.getHasTemplate())
            {
                resultTest.setOptionsTemplate(microbiologyService.getGeneralTemplate(resultTest.getOrder(), resultTest.getTestId()).getOptionTemplates().stream().filter(result -> result.getResult() != null && !result.getResult().isEmpty()).collect(Collectors.toList()));
                if (resultTest.getOptionsTemplate() != null && !resultTest.getOptionsTemplate().isEmpty())
                {
                    for (OptionTemplate optionTemplate : resultTest.getOptionsTemplate())
                    {
                        optionTemplate.setId(null);
                        optionTemplate.setComment(null);
                        optionTemplate.setResults(null);
                    }
                }
            }
        }
        return resultTests;
    }

    @Override
    public TestInformationTracking getInformation(Long idOrder, Integer idTest) throws Exception
    {
        TestInformationTracking testInformationTracking = new TestInformationTracking();
        //Result result = resultTestDao.getInformation(idOrder, idTest);
        testInformationTracking.setAuditOperation(trackingDao.getTrackingOrderTest(idOrder, idTest));
        testInformationTracking.setDeliveryTypes(deliveryResultDao.listDeliveryTestOrder(idOrder, idTest));
        //List<AuditOperation> list = trackingDao.getTrackingOrderTest(idOrder, idTest);
        //result.setDeliveryTypes(resultTestDao.listDeliverytypesByOrderAndTestId(idOrder, idTest));
        return testInformationTracking;
    }

    @Override
    public List<Question> getPanicSurvey() throws Exception
    {
        List<Interview> surveys = surveyDao.list().stream().filter(x -> x.isPanic() && x.isState()).collect(Collectors.toList());
        List<Question> questions = new ArrayList<>();
        if (!surveys.isEmpty())
        {
            questions = surveyDao.listQuestion(surveys.get(0).getId()).stream().filter(x -> x.isState()).collect(Collectors.toList());
            questions.stream().forEach((question)
                    ->
            {
                if (!question.isOpen())
                {
                    questionDao.readAnswer(question);
                    question.setAnswers(question.getAnswers().stream().filter(x -> x.isSelected() && x.isState()).collect(Collectors.toList()));
                    question.getAnswers().stream().forEach((answer)
                            ->
                    {
                        answer.setSelected(false);
                    });
                }
            });
        }
        return questions;
    }

    @Override
    public List<Test> getTestToResultRegister(long order) throws Exception
    {
        List<Test> tests = resultDao.getTestToResultRegister(order);
        if (tests != null && !tests.isEmpty())
        {
            literalResultDao.listByOrder(order).forEach((listLiterale)
                    ->
            {
                Test literal = new Test();
                literal.setId(listLiterale.getTestId());
                Test newTest = tests.indexOf(literal) > 0 ? tests.get(tests.indexOf(literal)) : null;
                if (newTest != null)
                {
                    newTest.getLiteralResultsLiteral().add(listLiterale);
                }
            });
        }
        return tests;
    }

    @Override
    public List<Order> orderResultList(Long orderNumber, List<Demographic> demographics, String action, String idSample, String laboratorys) throws Exception
    {
        return middlewareDao.orderResultList(orderNumber, demographics, action, idSample, laboratorys);
    }
    
      @Override
    public List<Order> orderResultListDeleteTest(Long orderNumber, List<Demographic> demographics, String action, String idSample, String laboratorys) throws Exception
    {
        return middlewareDao.orderResultListDeleteTest(orderNumber, demographics, action, idSample, laboratorys);
    }

    @Override
    public List<Order> orderResultListRemision(RemissionLaboratory orders, List<Demographic> demographics, String action) throws Exception
    {
        return middlewareDao.orderResultListRemision(orders, demographics, action);
    }

    @Override
    public List<Test> allTestByOrder(long order, String action, String idSample, String laboratorys) throws Exception
    {
        return middlewareDao.allTestByOrder(order, action, idSample, laboratorys);
    }

    @Override
    public List<Order> orderResultRange(ResultFilter resultFilter, List<Demographic> demographics, String action, String laboratorys) throws Exception
    {
        return middlewareDao.orderResultRange(resultFilter, demographics, action, laboratorys);
    }

    @Override
    public List<Long> rangeOrders(ResultFilter resultFilter) throws Exception
    {
        return resultOrderDao.rangeOrders(resultFilter);
    }

    @Override
    public ResultTestValidate assignFormulaValue(ResultTestValidate list, AuthorizedUser user) throws Exception
    {

        list.getTests().stream()
                .filter((ResultTest obj) -> obj.getState() == ResultTestState.ORDERED.getValue())
                .filter((ResultTest obj) -> !StringUtils.isEmpty(obj.getFormula()))
                .forEach((ResultTest obj)
                        ->
                {
                    String resultTmp = obj.getResult();
                    int stateTmp = obj.getNewState();
                    int userTmp = obj.getUserId();

                    try
                    {
                        obj.setResult(getFormulation(list, obj.getFormula(), obj.getDigits(), RoundingMode.HALF_EVEN));

                        if (!StringUtils.isEmpty(obj.getResult()))
                        {
                            obj.setResultChanged(true);
                            obj.setNewState(ResultTestState.REPORTED.getValue());
                            obj.setUserId(user.getId());
                            reportedTest(obj);
                        }
                    } catch (Exception ex)
                    {
                        //TODO: Cómo se controla la excepción?
                        obj.setResult(resultTmp);
                        obj.setNewState(stateTmp);
                        obj.setUserId(userTmp);
                    }
                });
        return list;
    }

    @Override
    public void updateResultForExam(UpdateResult updateResult) throws Exception
    {
        // UpdateResult updateResultOld = resultOrderDao.getOldObject(updateResult);
        Integer valueSample = resultOrderDao.getSample(updateResult);
        resultOrderDao.updateReference(updateResult, valueSample);

        // Long orderNumber = updateResult.getNumberOrder();
        List<AuditOperation> auditList = new ArrayList<>(0);
        Order order = new Order();
        order.setOrderNumber(updateResult.getNumberOrder());

        // Order o = getAudit(order.getOrderNumber()).clean();
        auditList.add(new AuditOperation(updateResult.getNumberOrder(), updateResult.getOldExamIdentifier(), null, AuditOperation.ACTION_DELETE, AuditOperation.TYPE_TEST, Tools.jsonObject(ResultTestState.ORDERED.getValue()), null, null, null, null));
        auditList.add(new AuditOperation(updateResult.getNumberOrder(), updateResult.getNewExamIdentifier(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_TEST, Tools.jsonObject(ResultTestState.ORDERED.getValue()), null, null, null, null));

        trackingService.registerOperationTracking(auditList);
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

    @Override
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
    public List<TestBasic> getTestByOrderSample(long order, int sample) throws Exception
    {
        List<TestBasic> testBasic = resultDao.getTestByOrderSample(order, sample);
        return testBasic;
    }

    @Override
    public List<Area> getAreasByOrderSample(long order, int sample) throws Exception
    {
        List<Area> area = resultDao.getAreasByOrderSample(order, sample);
        return area;
    }

    @Override
    public List<Sample> getSamplesToTake(long order) throws Exception
    {
        List<Sample> sample = resultDao.getSamplesToTake(order);
        return sample;
    }

    @Override
    public List<Integer> getProfiles(long order) throws Exception
    {
        List<Integer> perfilOrder = resultDao.getProfiles(order);
        return perfilOrder;
    }

    @Override
    public boolean isAllChildTaked(long order, int idProfile) throws Exception
    {
        List<TestBasic> testBasic = resultDao.isAllChildTaked(order, idProfile);
        if (testBasic.size() > 0)
        {
            return false;
        }

        return true;
    }

    @Override
    public void checkProfileAsTaked(long order, int profile) throws Exception
    {
        List<AuditOperation> auditList = new ArrayList<>(0);

        AuthorizedUser user = JWT.decode(request);

        List<ResultTest> profilesToUpdate = new ArrayList<>();

        profilesToUpdate = resultDao.testToUpdate(order, profile);

        for (int i = 0; i < profilesToUpdate.size(); i++)
        {
            profilesToUpdate.get(i).setTakenUserId(user.getId());

            ResultTest list = resultDao.checkProfileAsTaked(profilesToUpdate.get(i), order, profile);

            //Trazabilidad de la orden y examen   
            //pendiente guadar la trazabilidad de la muestra el perfil
            //auditList.add(new AuditOperation(list.getOrder(), list.getTestId(),null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_TEST, Tools.jsonObject(list), null, null));
        }

        trackingService.registerOperationTracking(auditList);
    }

    @Override
    public boolean isSampleTaken(long order, String codeSample) throws Exception
    {
        List<Sample> sample = resultDao.isSampleTaken(order, codeSample);

        if (sample.size() > 0)
        {
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * Obtiene un listado de ordenes dentro de un rango de fechas que aun no se
     * han enviado a un determinado sistema central
     *
     * @param centralSystemResults
     * @return Lista de ordenes que aun no se han enviado al sistema central
     * @throws Exception Error base de datos
     */
    @Override
    public List<Order> sendOrderResultsCentralSystem(CentralSystemResults centralSystemResults) throws Exception
    {
        try
        {
            List<Order> ordersFound = new ArrayList<>();
            List<String> testsIds = Arrays.asList(centralSystemResults.getIncludeTests().split(","));
            // Obtenemos las ordenes según el rango de fechas
            List<Long> orderRange = resultDao.rangeOrders(centralSystemResults.getStartDate(), centralSystemResults.getEndDate());
            if (!orderRange.isEmpty())
            {
                for (Long idOrder : orderRange)
                {
                    List<ResultTest> listResults = new ArrayList<>();
                    Order order = orderService.get(idOrder);
                    for (int i = 0; i < order.getResultTest().size(); i++)
                    {
                        ResultTest result = order.getResultTest().get(i);
                        CentralSystemResults shippingOrder = resultDao.getOrdersToTheExternalCentralSystem(order.getOrderNumber(), result.getTestId(), centralSystemResults.getCentralSystem());
                        // Validamos que el examen de la orden ya haya sido validado(Estado 4)
                        // Y Validamos que la orden aún no se ha enviado al sistema central
                        if (shippingOrder != null)
                        {
                            if (result.getState() == 4 && shippingOrder.getIndicatore() == 0)
                            {
                                // Verificamos si el resultado de ese examen debemos incluirlo o no
                                for (String testId : testsIds)
                                {
                                    int testIdParser = Integer.parseInt(testId.trim());
                                    if (testIdParser == result.getTestId())
                                    {
                                        listResults.add(result);
                                    }
                                }
                            }
                        }
                    }

                    // Adicionamos una nueva lista con los resultados de solo los examenes incluidos
                    order.setResultTest(listResults);
                    // Se carga la orden a la lista de ordenes
                    // Solo si esta tiene resultados de examenes pendientes por enviar al sistema central
                    if (order.getResultTest().size() > 0)
                    {
                        ordersFound.add(order);
                    }
                }
            }

            return ordersFound;
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Actualizamos el registro de resultados en el campo lab57c50 el cual
     * indica que ese examen de esa orden ya fue enviado a un sistema central y
     * tambien se inserta la orden enviada, en la tabla de envio de ordenes al
     * sistema central externo
     *
     * @param idOrder
     * @param idTest
     * @param centralSystem
     * @return Lista de ordenes que aun no se han enviado al sistema central
     * @throws Exception Error base de datos
     */
    @Override
    public int updateSentCentralSystem(long idOrder, int idTest, int centralSystem) throws Exception
    {
        try
        {
            CentralSystemResults orderShipped = new CentralSystemResults();
            orderShipped.setCentralSystem(centralSystem);
            orderShipped.setIdTest(idTest);
            orderShipped.setIdOrder(idOrder);
            orderShipped.setIndicatore(1);
            Date entryDate = new Date();
            orderShipped.setDateOfDispatch(new Timestamp(entryDate.getTime()));
            // Obtenemos el codigo del perfil al que pertenece ese examen:
            List<Profile> listProfiles = testService.getProfiles();
            // Guradamos el id del perfil al cual pertenece dicho examen
            int idProfile = listProfiles.stream().filter(profile -> profile.getTestId() == idTest)
                    .map(profile -> profile.getProfileId()).findFirst().orElse(0);
            // Evaluamos que la variable sea distinta a cero, si es cero el examen no pertenece a ningún perfil
            if (idProfile > 0)
            {
                net.cltech.enterprisent.domain.masters.test.Test profileObj = testService.get(idProfile, null, null, null);
                if (profileObj != null)
                {
                    orderShipped.setCodeProfile(profileObj.getCode());
                }
            } else
            {
                orderShipped.setCodeProfile("");
            }
            // Insertamos el registro
            resultDao.insertOrdersToTheExternalCentralSystem(orderShipped);
            return resultDao.updateSentCentralSystem(idOrder, String.valueOf(idTest), centralSystem);
        } catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Listar de las ordenes enviadas a un sistema central externo
     *
     * @param idTest
     * @return Lista de ordenes que han sido enviado a un sistema central
     * externo
     * @throws Exception Error base de datos
     */
    @Override
    public List<FindShippedOrders> findShippedOrdersCentralSystem(int idTest) throws Exception
    {
        try
        {
            return resultDao.findShippedOrdersCentralSystem(idTest);
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Eliminar registros de resultados desvalidados
     *
     * @param idOrden
     * @param idTest
     * @return Eliminacion de regitro por resultado desvalidado
     * @throws Exception Error base de datos
     */
    @Override
    public int removeDevalidatedResults(Long idOrden, int idTest) throws Exception
    {
        try
        {
            return resultDao.deletePatientResults(idOrden, idTest);
        } catch (Exception e)
        {
            return -1;
        }
    }

    @Override
    public List<ResultTest> getTestsUnionDaughterApp(List<Long> orderId, String idOrdenHis, boolean isConfidential) throws Exception
    {
        List<ResultTest> resultTest = new ArrayList<>();
        if (idOrdenHis.isEmpty())
        {
            resultTest = resultTestDao.getTestsUnionDaughterApp(null, orderId);
        } else
        {
            Boolean areabypackage = Boolean.parseBoolean(configurationServices.get("verPaquetePorAreaInforme").getValue());
            ResultFilter resultFilter = new ResultFilter();
            resultFilter.setIdExternalOrder(idOrdenHis);
            resultFilter.setFilterId(2);
            resultFilter.setConfidential(isConfidential);
            resultTest = resultTestDao.getTestsUnionDaughter(resultFilter, orderId, areabypackage);
        }

        resultTest.forEach(result ->
        {
            if (result.getHasAntibiogram())
            {
                try
                {
                    result.setMicrobialDetection(microbiologyService.getMicrobialDetection(result.getOrder(), result.getTestId()));
                    result.getMicrobialDetection().setMicroorganisms(result.getMicrobialDetection().getMicroorganisms().stream().filter(microorganism -> microorganism.isSelected()).collect(Collectors.toList()));
                    for (Microorganism microorganism : result.getMicrobialDetection().getMicroorganisms())
                    {
                        microorganism.setResultsMicrobiology(microorganism.getResultsMicrobiology().stream().filter(test -> test.isSelected()).collect(Collectors.toList()));
                        microorganism.setCountResultMicrobiology(microorganism.getResultsMicrobiology().size());
                    }
                } catch (Exception ex)
                {
                    Logger.getLogger(ResultServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        return resultTest;
    }

    @Override
    public void reportQM(int idTest, AuthorizedUser user) throws Exception
    {
        try
        {
            final String url = configurationServices.getValue("UrlQM") + "/api/lis/items/discount";

            try
            {
                final HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);

                ItemsDiscount itemsDiscount = new ItemsDiscount();

                itemsDiscount.setTest(idTest);
                itemsDiscount.getUser().setId(user.getId());
                itemsDiscount.getUser().setName(user.getName());
                itemsDiscount.getUser().setLastname(user.getLastName());

                final HttpEntity<ItemsDiscount> httpEntity = new HttpEntity<>(itemsDiscount, headers);

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                if (responseEntity.getStatusCodeValue() == 200)
                {
                    System.out.println("QM - Artículos descontados");
                    System.out.println(Tools.jsonObject(responseEntity.getBody()));
                }
            } catch (IllegalArgumentException | HttpClientErrorException ex)
            {
                EventsLog.error("Test " + idTest + " not configured");
            } catch (JsonProcessingException | RestClientException ex)
            {
                EventsLog.error(ex);
            }
        } catch (Exception e)
        {
            EventsLog.error(e);
        }
    }

    /**
     * Actualiza los valores de referencia de un examen de una orden sin
     * resultados
     *
     * @param idOrder
     * @return True - Actualización de valores de referencia exitosa, False - No
     * se actualizaron los valores de referencia de manera exitosa
     * @throws Exception Error en el servicio
     */
    @Override
    public boolean updateReferenceValues(long idOrder) throws Exception
    {
        try
        {
            int affectedRecords = 0;
            Patient orderFound = patientService.getBasicPatientbyOrder(idOrder);

            List<Integer> testsWithOutResults;
            testsWithOutResults = resultDao.getTestsWithoutResult(idOrder);

            if (orderFound != null)
            {
                for (Integer testId : testsWithOutResults)
                {
                    ReferenceValue reference = matchReferenceValue(orderFound, testDao.listReferenceValues(testId));

                    ResultTest obj = new ResultTest();
                    obj.setResultComment(new ResultTestComment());
                    obj.setResultChanged(true);
                    obj.setResultType(resultDao.getTestTypeResult(testId));

                    obj.setPanicMin(reference.getPanicMin());
                    obj.setPanicMax(reference.getPanicMax());
                    obj.setRefInterval(reference.getNormalMin() + " " + reference.getNormalMax());
                    obj.setRefMin(reference.getNormalMin());
                    obj.setRefMax(reference.getNormalMax());

                    short critical = reference.isCriticalCh() == true ? (short) 1 : (short) 0;
                    obj.setCritic(critical);

                    if (reference.getPanic() != null)
                    {
                        obj.setPanicLiteral(reference.getPanic().getName());
                    }
                    if (reference.getNormal() != null)
                    {
                        obj.setRefLiteral(reference.getNormal().getName());
                    }
                    obj.setResult(resultDao.getTestResult(idOrder, testId));

                    int patology = 0;
                    if (obj.getResult() != null)
                    {
                        patology = validatePathology(obj);
                    }

                    affectedRecords += resultDao.updateReferenceValue(idOrder, testId, reference, patology);
                }
            }
            return affectedRecords > 0;
        } catch (Exception e)
        {
            OrderCreationLog.info("excepcion" + e);
            return false;
        }
    }

    /**
     * Metodo que permite guardar la hora reportada de un critico
     *
     * @param orderId
     * @param testId
     * @param date
     * @throws Exception
     */
    private void reportedCritical(Long orderId, int testId, Date date) throws Exception
    {
        resultTestDao.reportedCritical(orderId, testId, date);
    }

    @Override
    public List<Test> getTestsLastOrder(long order) throws Exception
    {
        return resultTestDao.testsLastOrder(order);
    }

    @Override
    public List<Order> getOrdersResults(long date) throws Exception
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.mmm");

        //Restan 15 minutos a la fecha actual
        Calendar dateEnd = Calendar.getInstance();
        dateEnd.add(Calendar.MINUTE, -15);

        // restan  45 minutos a la fecha actual
        Calendar dateInit = Calendar.getInstance();
        dateInit.add(Calendar.MINUTE, -45);

        Timestamp timestampInit = Timestamp.valueOf(formatter.format(dateInit.getTime()));
        Timestamp timestampBefore = Timestamp.valueOf(formatter.format(dateEnd.getTime()));

        List<Demographic> demographics = demographicService.demographicsList();

        String printDemographic = configurationServices.getValue("DemograficoEnvioCorreoAutomatico");
        String itemPrintDemographic = configurationServices.getValue("ItemDemograficoEnvioCorreoAutomatico");

        return resultTestDao.listOrdersResults(date, timestampInit, timestampBefore, demographics, printDemographic, itemPrintDemographic);
    }

    @Override
    public boolean getProccesOrder(long idOrder) throws Exception
    {
        return resultTestDao.getProccesOrder(idOrder);
    }

    @Override
    public List<TypeInterview> getTestPanicSurvey() throws Exception
    {
        List<Interview> surveys = surveyDao.list().stream().filter(x -> x.isPanic() && x.isState()).collect(Collectors.toList());
        List<TypeInterview> typeInterviews = new ArrayList<>();
        if (!surveys.isEmpty())
        {
            typeInterviews = interviewService.listTypeInterview(surveys.get(0).getId(), surveys.get(0).getType().intValue());

        }
        return typeInterviews.stream().filter(type -> type.isSelect()).collect(Collectors.toList());
    }

    /**
     * Este metodo se encargara del envio de correos por retoma
     *
     * @param result
     * @param order
     * @param idUser
     * @param reason
     * @param sample
     * @throws Exception Error al consultar con el id de la orden y id de la
     * prueba
     */
    @Override
    public void validsendMailRetake(List<Test> result, Long order, int idUser, Reason reason, Sample sample) throws Exception
    {
        try
        {
            //Llaves de configuracion para envio de correo por retoma
            List<String> recipients = new ArrayList<>();
            recipients = Arrays.asList(configurationServices.get("CorreosNotificacionObligatoriaTres").getValue().split(","));
            String subject = configurationServices.get("AsuntoNotificacionObligatoriaTres").getValue();
            String body = configurationServices.get("PlantillaNotificacionObligatoriaTres").getValue();

            // Creo un objeto paciente, para cargarlo dependiendo al paciente al cual se le asigno dicha orden
            Patient patient = patientService.get(order);

            if (body.contains("||TEST||"))
            {
                body = body.replaceAll("\\|\\|TEST\\|\\|", " " + result.stream().map(t -> t.getCode() + " " + t.getName()).collect(Collectors.joining(",")));
            }
            if (body.contains("||PATIENT||"))
            {
                if (patient.getName2() == null && patient.getSurName() == null)
                {
                    body = body.replaceAll("\\|\\|PATIENT\\|\\|", " " + patient.getName1() + " " + patient.getLastName() + " ");
                } else if (patient.getName2() == null)
                {
                    body = body.replaceAll("\\|\\|PATIENT\\|\\|", " " + patient.getName1() + " " + patient.getLastName() + " " + patient.getSurName() + " ");
                } else if (patient.getSurName() == null)
                {
                    body = body.replaceAll("\\|\\|PATIENT\\|\\|", " " + patient.getName1() + " " + patient.getName2() + " " + patient.getLastName() + " ");
                } else
                {
                    body = body.replaceAll("\\|\\|PATIENT\\|\\|", " " + patient.getName1() + " " + patient.getName2() + " " + patient.getLastName() + " " + patient.getSurName() + " ");
                }
            }

            if (body.contains("||ORDER||"))
            {
                body = body.replaceAll("\\|\\|ORDER\\|\\|", " " + order + " ");
            }
            if (body.contains("||HISTORY||"))
            {
                body = body.replaceAll("\\|\\|HISTORY\\|\\|", " " + patient.getPatientId() + " ");
            }
            if (body.contains("||RESULT||"))
            {
                body = body.replaceAll("\\|\\|RESULT\\|\\|", " " + result.stream().map(t -> t.getResult().getResult()).collect(Collectors.joining(",")));
            }
            if (body.contains("||DATE||"))
            {
                String currentYear = DateTools.dateFormatyyyy_MM_dd_hh_mm_ss(new Date());
                body = body.replaceAll("\\|\\|DATE\\|\\|", " " + currentYear + " ");
            }

            if (body.contains("||BACTERIOLOGISTNAME||") || body.contains("||BACTERIOLOGISTCODE||"))
            {
                User uservalidate = userService.getBasicUser(idUser);
                if (body.contains("||BACTERIOLOGISTNAME||"))
                {
                    body = body.replaceAll("\\|\\|BACTERIOLOGISTNAME\\|\\|", " " + uservalidate.getName() + " " + uservalidate.getLastName());
                }
                if (body.contains("||BACTERIOLOGISTCODE||"))
                {
                    body = body.replaceAll("\\|\\|BACTERIOLOGISTCODE\\|\\|", " " + uservalidate.getIdentification());
                }
            }

            if (body.contains("||DEMO"))
            {
                Order completeOrder = orderService.get(order);

                if (body.contains("||DEMO_-1||"))
                {
                    body = body.replaceAll("\\|\\|DEMO_-1\\|\\|", " " + completeOrder.getAccount().getName() + " ");
                }

                if (body.contains("||DEMO_-2||"))
                {
                    body = body.replaceAll("\\|\\|DEMO_-2\\|\\|", " " + completeOrder.getPhysician().getName() + " ");
                }

                if (body.contains("||DEMO_-3||"))
                {
                    body = body.replaceAll("\\|\\|DEMO_-3\\|\\|", " " + completeOrder.getRate().getName() + " ");
                }

                if (body.contains("||DEMO_-4||"))
                {
                    body = body.replaceAll("\\|\\|DEMO_-4\\|\\|", " " + completeOrder.getType().getName() + " ");
                }

                if (body.contains("||DEMO_-5||"))
                {
                    body = body.replaceAll("\\|\\|DEMO_-5\\|\\|", " " + completeOrder.getBranch().getName() + " ");
                }

                if (body.contains("||DEMO_-6||"))
                {
                    body = body.replaceAll("\\|\\|DEMO_-6\\|\\|", " " + completeOrder.getService().getName() + " ");
                }
                if (body.contains("||DEMO_-104||"))
                {
                    body = body.replaceAll("\\|\\|DEMO_-104\\|\\|", " " + patient.getSex().getEsCo() + " ");
                }

                if (body.contains("||DEMO_-10||"))
                {
                    body = body.replaceAll("\\|\\|DEMO_-10\\|\\|", " " + patient.getDocumentType().getName() + " ");
                }
                if (body.contains("||DEMO_-106||"))
                {
                    body = body.replaceAll("\\|\\|DEMO_-106\\|\\|", " " + patient.getEmail() + " ");
                }

                String demo;
                String demoItemValue;

                for (DemographicValue demographic : completeOrder.getAllDemographics())
                {
                    if (body.contains("||DEMO"))
                    {
                        demo = "DEMO_" + demographic.getIdDemographic();
                        if (body.contains("||" + demo + "||"))
                        {
                            if (demographic.isEncoded())
                            {
                                demoItemValue = demographic.getCodifiedName();
                            } else
                            {
                                demoItemValue = demographic.getValue();
                            }
                            body = body.replaceAll("\\|\\|" + demo + "\\|\\|", " " + demoItemValue + " ");
                        }
                    } else
                    {
                        break;
                    }
                }
            }

            if (body.contains("||REASON||"))
            {
                body = body.replaceAll("\\|\\|REASON\\|\\|", " " + reason.getMotive().getName().trim() + " ");
            }

            if (body.contains("||COMMENT||"))
            {
                body = body.replaceAll("\\|\\|COMMENT\\|\\|", " " + reason.getComment() + " ");
            }

            if (body.contains("||SAMPLE||"))
            {
                body = body.replaceAll("\\|\\|SAMPLE\\|\\|", " " + sample.getCodesample() + " " + sample.getName() + " ");
            }

            Email email = new Email();
            email.setRecipients(recipients);
            email.setSubject(subject);
            email.setBody(body);
            String send = userService.sendEmail(email);
            if (send.contains("error"))
            {
                System.out.println(send);
            }

        } catch (Exception e)
        {
            ResultsLog.error(e);
        }
    }

    @Override
    public int checkValidationOrder(Long orderId) throws Exception
    {
        return resultTestDao.checkValidationOrder(orderId);
    }

    @Override
    public void updateFechaIngresoDate(Long orderId, int test, Timestamp resuldate) throws Exception
    {
        IntegrationHisLog.info("DATOS ACTUALIZAR  " + resuldate);
        resultTestDao.updateFechaIngresoDate(orderId, test, resuldate);
    }

    @Override
    public List<ResultHeader> header(ResultHeaderFilter filter) throws Exception
    {
        List<ResultHeader> listFinal = new LinkedList<>();
        int orderDigits = configurationServices.getIntValue("DigitosOrden");
        String initDate = String.valueOf(Tools.buildInitialOrder((int) filter.getDays(), orderDigits));
        String finalDate = String.valueOf(Tools.buildFinalOrder(orderDigits));

        List<OrderHeader> orders = resultTestDao.preheader(filter, initDate, finalDate);

        List<ResultHeader> ordersFilters = orders.stream()
                .map((OrderHeader order) -> order.setTests(checkTypeValidation(filter.getTypeValidation(), order.getTests())))
                .filter((OrderHeader order) -> order.getTests() != null && order.getTests().size() > 0)
                .map((OrderHeader order) -> new ResultHeader(order.getOrder()))
                .collect(Collectors.toList());

        List<Demographic> listdemos = demographicService.list(true);
        List<Demographic> demos = new LinkedList<>();

        filter.getDemographics().forEach(demo ->
        {
            if (demo.getId() > 0)
            {
                Demographic value = listdemos.stream().filter(d -> d.getId().equals(demo.getId())).findFirst().orElse(null);
                if (value != null)
                {
                    demos.add(value);
                }
            } else
            {
                demos.add(new Demographic(demo.getId()));
            }
        });

        boolean account = daoConfig.get("ManejoCliente").getValue().equalsIgnoreCase("true");
        boolean physician = daoConfig.get("ManejoMedico").getValue().equalsIgnoreCase("true");
        boolean rate = daoConfig.get("ManejoTarifa").getValue().equalsIgnoreCase("true");
        boolean service = daoConfig.get("ManejoServicio").getValue().equalsIgnoreCase("true");
        boolean race = daoConfig.get("ManejoRaza").getValue().equalsIgnoreCase("true");
        boolean documenttype = daoConfig.get("ManejoTipoDocumento").getValue().equalsIgnoreCase("true");

        List<ResultHeader> list = resultTestDao.header(filter, initDate, finalDate, demos, account, physician, rate, service, race, documenttype);

        if (list.size() > 0)
        {
            List<ResponsDemographicIngreso> demographics = integrationIngresoDao.getDemographicCentralSystem(filter.getIdSystemDemographics());
            listFinal = list.stream()
                    .filter(order -> ordersFilters.contains(order))
                    .filter(order -> filter.getDemographics().isEmpty() || StreamFilters.containsDemographicHeader(order, filter.getDemographics()))
                    .map((ResultHeader r) -> setHomologation(r, demographics))
                    .collect(Collectors.toList());

            return listFinal;
        }
        return listFinal;
    }

    /**
     * Setea la homologacion de los examenes
     *
     * @param result Orden a la cual se le realizara el filtro
     * @param homologation lista de homologacion
     *
     * @return Lista de examenes filtrados
     */
    private ResultHeader setHomologation(ResultHeader result, List<ResponsDemographicIngreso> homologation)
    {
        try
        {
            if (result.getOrderTypeId() > 0)
            {
                ResponsDemographicIngreso homo = homologation.stream().filter(h -> h.getIdDemographic() == Constants.ORDERTYPE).findFirst().orElse(null);
                if (homo != null)
                {
                    result.setOrderType(homo.getIdItemDemographicHis());
                }
            }

            result.getDemographics().forEach(demo ->
            {
                ResponsDemographicIngreso homo = null;
                if (demo.getIdItem() > 0)
                {
                    homo = homologation.stream().filter(h -> h.getIdDemographic() == demo.getId() && h.getIdItemDemographicLis() == demo.getIdItem()).findFirst().orElse(null);
                } else
                {
                    homo = homologation.stream().filter(h -> h.getIdDemographic() == demo.getId()).findFirst().orElse(null);
                }
                if (homo != null)
                {
                    demo.setValue(homo.getIdItemDemographicHis());
                }
            });
            return result;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Valida el tipo de validacion enviado
     *
     * @param typeValidation Tipo de validacion enviado
     * @param tests lista de examenes
     *
     * @return Lista de examenes filtrados
     */
    private List<TestHeader> checkTypeValidation(Integer typeValidation, List<TestHeader> tests)
    {
        try
        {
            switch (typeValidation)
            {
                case 0:
                    return tests.stream().filter(test -> test.getTestStatus() > 3).collect(Collectors.toList());
                case 1:
                    List<TestHeader> listTests = new LinkedList<>();
                    final Map<Integer, List<TestHeader>> testByProfile = tests.stream().collect(Collectors.groupingBy(test -> test.getProfileId()));
                    testByProfile.entrySet().forEach((entry) ->
                    {
                        List<TestHeader> validate = entry.getValue().stream().filter(test -> test.getTestStatus() <= 3).collect(Collectors.toList());
                        if (validate.isEmpty())
                        {
                            listTests.addAll(entry.getValue());
                        }
                    });
                    return listTests;
                case 2:
                    List<TestHeader> validate = tests.stream().filter(test -> test.getTestStatus() <= 3).collect(Collectors.toList());
                    if (validate.isEmpty())
                    {
                        return tests;
                    }
                    break;
                case 3:
                    return tests.stream().filter(test -> !"".equals(test.getResult()) && test.getResult() != null).collect(Collectors.toList());
            }

            return null;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<TestDetail> detail(DetailFilter filter) throws Exception
    {
        List<TestDetail> results = resultTestDao.detail(filter);

        switch (filter.getTypeHomologation())
        {
            case 0:
                List<TestDetail> listTests = new LinkedList<>();

                final Map<Integer, List<TestDetail>> testByProfile = results.stream().collect(Collectors.groupingBy(test -> test.getProfileId()));
                testByProfile.entrySet().forEach((entry) ->
                {
                    List<TestDetail> validate = entry.getValue().stream().filter(test -> test.getHomologationProfile() == null).collect(Collectors.toList());
                    if (validate.isEmpty())
                    {
                        listTests.addAll(entry.getValue());
                    }
                });
                results = listTests;

                break;
            case 1:
                results = results.stream().filter(test -> test.getHomologationCode() != null).collect(Collectors.toList());
                break;
        }

        switch (filter.getTypeValidation())
        {
            case 0:
                results = results.stream().filter(test -> test.getState() > 3).collect(Collectors.toList());
                break;
            case 1:
                List<TestDetail> listgroup = new LinkedList<>();
                final Map<Integer, List<TestDetail>> testByProfile = results.stream().collect(Collectors.groupingBy(test -> test.getProfileId()));
                testByProfile.entrySet().forEach((entry) ->
                {
                    List<TestDetail> validate = entry.getValue().stream().filter(test -> test.getState() <= 3).collect(Collectors.toList());
                    if (validate.isEmpty())
                    {
                        listgroup.addAll(entry.getValue());
                    }
                });
                results = listgroup;
                break;
            case 2:
                List<TestDetail> validate = results.stream().filter(test -> test.getState() <= 3).collect(Collectors.toList());
                if (!validate.isEmpty())
                {
                    results = new LinkedList<>();
                }
                break;
            case 3:
                results = results.stream().filter(test -> !"".equals(test.getResult()) && test.getResult() != null).collect(Collectors.toList());
        }

        return results;
    }

    @Override
    public DetailStatus status(DetailStatus status) throws Exception
    {
        try
        {
            return resultTestDao.status(status);
        } catch (Exception ex)
        {
            IntegrationHisLog.error(ex);
            return null;
        }
    }
    
}

package net.cltech.enterprisent.service.impl.enterprisent.operation.reports;

import com.google.common.base.Objects;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.masters.user.UserDao;
import net.cltech.enterprisent.dao.interfaces.operation.list.OrderListDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.dao.interfaces.operation.reports.DeliveryResultDao;
import net.cltech.enterprisent.dao.interfaces.operation.reports.SerialDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultTestDao;
import net.cltech.enterprisent.dao.interfaces.tools.BarcodeDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.common.Print;
import net.cltech.enterprisent.domain.common.PrintNode;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsDemographicIngreso;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.configuration.DemographicReportEncryption;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.common.FilterOrder;
import net.cltech.enterprisent.domain.operation.common.FilterOrderBarcode;
import net.cltech.enterprisent.domain.operation.common.FilterOrderHeader;
import net.cltech.enterprisent.domain.operation.common.PrintOrder;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderReportINS;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.SuperTest;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.orders.barcode.BarcodeSample;
import net.cltech.enterprisent.domain.operation.orders.excel.OrderReportAidaAcs;
import net.cltech.enterprisent.domain.operation.reports.DeliveryResult;
import net.cltech.enterprisent.domain.operation.reports.JsonToBufferNT;
import net.cltech.enterprisent.domain.operation.reports.PrintBarcodeLog;
import net.cltech.enterprisent.domain.operation.reports.PrintReportLog;
import net.cltech.enterprisent.domain.operation.reports.ReportBarcode;
import net.cltech.enterprisent.domain.operation.reports.SerialPrint;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.tools.BarcodeDesigner;
import net.cltech.enterprisent.service.impl.enterprisent.operation.orders.OrderServiceEnterpriseNT;
import net.cltech.enterprisent.service.impl.enterprisent.operation.results.ResultServiceEnterpriseNT;
import net.cltech.enterprisent.service.interfaces.document.DocumentService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.common.CommentService;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.service.interfaces.operation.reports.ReportService;
import net.cltech.enterprisent.service.interfaces.operation.reports.ServicePrintService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.StatisticService;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.StreamFilters;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import net.cltech.enterprisent.websocket.PrintHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

/**
 * Implementacion de informes para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @since 21/11/2017
 * @see Creacion
 */
@Service
public class ReportServiceEnterpriseNT implements ReportService
{

    @Autowired
    private OrderListDao listDao;
    @Autowired
    private ResultServiceEnterpriseNT resultServiceEnterpriseNT;
    @Autowired
    private ResultsService serviceResult;
    @Autowired
    private DemographicService serviceDemographic;
    @Autowired
    private ConfigurationService configurationServices;
    @Autowired
    private DocumentService documentServices;
    @Autowired
    private CommentService commentService;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private ResultTestDao resultTestDao;
    @Autowired
    private OrderServiceEnterpriseNT orderServiceEnterpriseNT;
    @Autowired
    private DemographicService demographicService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private SerialDao serialDao;
    @Autowired
    private PrintHandler printHandler;
    @Autowired
    private ServicePrintService servicePrintService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private ConfigurationDao configurationDao;
    @Autowired
    private DeliveryResultDao deliveryResultDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TestService testService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private LaboratorysByBranchesService laboratorysByBranchesService;



    @Value("${jdbc.rep.url}")
    private String jdbcRepUrl;

    @Override
    public List<Order> listFilters(Filter search) throws Exception
    {
        String textPending = configurationServices.get("ComentarioResultadoPendiente").getValue();
        boolean fullFinalReport = configurationServices.get("ImprimirInformeFinal").getValue().equals("2");
        boolean printAttachments = configurationServices.get("ImprimirAdjuntos").getValue().equalsIgnoreCase("True");
        
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        List<Order> list = listDao.list(search.getInit(), search.getEnd(), search.getRangeType(), serviceDemographic.list(true), search.getOrders(), search.getTestFilterType() == 2 ? search.getTests() : null, 0, 0, laboratorys, idbranch);
        List<Order> orders = list.stream()
                .filter(order -> order.getState() != LISEnum.ResultOrderState.CANCELED.getValue())
                .filter(order -> order.getPatient().getId() != 0)
                .filter(order -> StreamFilters.containsDemographic(order, search.getDemographics()))
                .collect(Collectors.toList());

        final List<ResultTest> tests = getTestResults(orders.stream().map(order -> order.getOrderNumber()).collect(Collectors.toList()), "", false)
                .stream()
                .collect(Collectors.toList());

        orders = orders.stream()
                .map(order -> order.setResultTest(getOrderTests(order.getOrderNumber(), tests, search, fullFinalReport, textPending)))
                .filter(order -> !order.getResultTest().isEmpty())
                .collect(Collectors.toList());

        for (Order order : orders)
        {
            order.setComments(commentService.listCommentOrder(order.getOrderNumber(), null).stream().filter(comment -> comment.isPrint()).collect(Collectors.toList()));
            if (printAttachments && search.isAttached())
            {
                order.setAttachments(new ArrayList<>());
                order.getAttachments().addAll(documentServices.list(order.getOrderNumber()));
                order.getAttachments().addAll(documentServices.list(order.getOrderNumber(), null));
            }

            final List<Integer> testsfilter = order.getResultTest().stream().map((s) -> s.getTestId()).collect(Collectors.toList());
            order.setImageTest(serviceResult.getResultGraphics(order.getOrderNumber(), testsfilter));

            //Cambio de estado de Finales y re impreso            
            FilterOrderHeader filterOrderHeader = new FilterOrderHeader();
            filterOrderHeader.setRangeType(search.getRangeType());
            filterOrderHeader.setInit(search.getInit());
            filterOrderHeader.setEnd(search.getEnd());
            filterOrderHeader.setReprintFinalReport(search.isReprintFinalReport());
            filterOrderHeader.setTypeReport(search.getTypeReport());
            filterOrderHeader.setPrintingMedium(4);

            AuthorizedUser user = JWT.decode(request);
            int userId = user.getId();
            List<ResultTest> listOld = order.getResultTest();
            changeStateTest(filterOrderHeader, order, userId);
            order.setResultTest(listOld);

        }

        return serviceResult.addAdditional(orders);
    }

    /**
     * Obtiene examenes de una orden
     *
     * @param order Número de la orden
     * @param idOrdenHis Número de la orden externa
     * @return lista de examenes con información del resultado
     * {@link net.cltech.enterprisent.domain.operation.results.ResultTest}
     */
    private List<ResultTest> getTestResults(List<Long> orders, String idOrdenHis, boolean isConfidential) throws Exception
    {

        long first = System.currentTimeMillis();
        List<ResultTest> result = serviceResult.getTestsUnionDaughter(orders, idOrdenHis, isConfidential);
        // Validamos que la llave de configuración verAnalitosDelPerfil este activa:
        // Esta me indica si analitos fuera de un perfil pero de la misma orden que pertenecen a dicho perfil
        // se veran dentro de este perfil en el informe de resultados
        List<ResultTest> resultListAux = new ArrayList<>();
        if (configurationServices.getValue("verAnalitosDelPerfil").equalsIgnoreCase("True"))
        {

            List<Integer> testsOrProfiles;
            ResultTest resultTestOrigin;
            ResultTest resultTestAuxTwo;

            List<ResultTest> orderProfile = result.stream()
                    .filter(test -> test.getProfileId() > 0)
                    .filter(distinctByKey(p -> p.getProfileId()))
                    .collect(Collectors.toList());

            // Este codigo no es funcional
            for (ResultTest resultTest : orderProfile)
            {

                testsOrProfiles = testService.getChildrenIds(resultTest.getProfileId());

                for (Integer testOrProfile : testsOrProfiles)
                {

                    resultTestOrigin = result.stream()
                            .filter(test -> Objects.equal(test.getTestId(), testOrProfile))
                            .findFirst().orElse(null);
                    if (resultTestOrigin != null)
                    {
                        if (resultTestOrigin.getProfileId() != resultTest.getProfileId())
                        {
                            resultTestAuxTwo = (ResultTest) resultTestOrigin.clone();
                            resultTestAuxTwo.setPrintSort(resultTest.getPrintSort());
                            resultTestAuxTwo.setProfileId(resultTest.getProfileId());
                            resultTestAuxTwo.setPrintSortProfile(resultTest.getPrintSortProfile());
                            resultTestAuxTwo.setProfileName(resultTest.getProfileName());
                            resultTestAuxTwo.setPackageId(resultTest.getPackageId());
                            resultTestAuxTwo.setPackageName(resultTest.getPackageName());
                            net.cltech.enterprisent.domain.masters.test.Test detail = testService.get(resultTest.getTestId(), null, null, null);
                            resultTestAuxTwo.setAreaId(detail.getArea().getId());
                            resultTestAuxTwo.setAreaAbbr(detail.getArea().getAbbreviation());
                            resultTestAuxTwo.setAreaName(detail.getArea().getName());
                            resultListAux.add(resultTestAuxTwo);
                        }
                    }
                }
            }

            List<ResultTest> orderPackage = result.stream()
                    .filter(test -> test.getProfileId() == 0 && test.getPackageId() > 0)
                    .filter(distinctByKey(p -> p.getPackageId()))
                    .collect(Collectors.toList());

            for (ResultTest resultTest : orderPackage)
            {
                testsOrProfiles = testService.getChildrenIds(resultTest.getPackageId());
                for (Integer testOrProfile : testsOrProfiles)
                {
                    resultTestOrigin = result.stream()
                            .filter(test -> Objects.equal(test.getTestId(), testOrProfile))
                            .findFirst().orElse(null);

                    if (resultTestOrigin != null)
                    {
                        if (resultTestOrigin.getPackageId() != resultTest.getPackageId())
                        {
                            resultTestAuxTwo = (ResultTest) resultTestOrigin.clone();

                            resultTestAuxTwo.setPrintSort(resultTest.getPrintSort());
                            resultTestAuxTwo.setProfileId(resultTest.getProfileId());
                            resultTestAuxTwo.setPrintSortProfile(resultTest.getPrintSortProfile());
                            resultTestAuxTwo.setProfileName(resultTest.getProfileName());
                            resultTestAuxTwo.setPackageId(resultTest.getPackageId());
                            resultTestAuxTwo.setPackageName(resultTest.getPackageName());
                            resultTestAuxTwo.setAreaId(0);
                            resultTestAuxTwo.setAreaCode("");
                            resultTestAuxTwo.setAreaAbbr("");
                            resultTestAuxTwo.setAreaName("");
                            resultListAux.add(resultTestAuxTwo);
                        }
                    }
                }
            }

            result.addAll(resultListAux);
            System.out.println("Tiempo de respuesta examenes con union - " + (System.currentTimeMillis() - first));
            return result;
        } else
        {
            return result;
        }

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Filtra examenes por area, examen o confidenciales
     *
     * @param filter Orden con examenes a filtrar
     * @param filter lista de examenes filtrados
     *
     * @return
     */
    private boolean filterTests(ResultTest tests, Filter filter)
    {
        switch (filter.getTestFilterType())
        {
            case 0:
                return true;
            case 1:
                if (!filter.getTests().contains(tests.getAreaId()))
                {
                    return false;
                }
                break;
            case 2:
            case 3:
                if (!filter.getTests().contains(tests.getTestId()))
                {
                    return false;
                }
                break;
            default:

                break;
        }
        return filter.getTestFilterType() == 3 ? tests.isConfidential() : !tests.isConfidential();
    }

    /**
     * Filtra examenes por area, examen o confidenciales
     *
     * @param filter Orden con examenes a filtrar
     * @param filter lista de examenes filtrados
     *
     * @return
     */
    private boolean filterTestsState(ResultTest test, Filter filter, String textPending)
    {
        //Reimpreso
        if (filter.isReprintFinalReport())
        {
            return test.getState() == LISEnum.ResultTestState.DELIVERED.getValue() || test.getState() == LISEnum.ResultTestState.VALIDATED.getValue();
        }
        switch (filter.getTypeReport())
        {
            case 1:
                //Final
                if (test.getState() != LISEnum.ResultTestState.VALIDATED.getValue())
                {
                    return false;
                }
                break;
            case 2:
                //Preliminar
//                if (test.getState() == LISEnum.ResultTestState.VALIDATED.getValue() || test.getState() == LISEnum.ResultTestState.DELIVERED.getValue())
//                {
//                    return false;
//                }
                break;
            case 3:
                //Copias
                if (test.getState() != LISEnum.ResultTestState.DELIVERED.getValue())
                {
                    return false;
                }
                break;
            case 4:
                //Previo
                if (test.getState() != LISEnum.ResultTestState.VALIDATED.getValue() && test.getState() != LISEnum.ResultTestState.DELIVERED.getValue())
                {
                    test.setResult(textPending);
                }
                break;
            case 5:
                //reimpresion 
                if (test.getState() != LISEnum.ResultTestState.DELIVERED.getValue())
                {
                    return false;
                }
                break;
            case 0:
                //validados e impresos 
                if (test.getState() < LISEnum.ResultTestState.VALIDATED.getValue())
                {
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Filtra examenes de la orden
     *
     * @param order numero de orden
     * @param allTests lista de examenes de consulta
     *
     * @return examenes que corresponden a la orden
     */
    private List<ResultTest> getOrderTests(long order, List<ResultTest> allTests, Filter search, boolean fullFinalReport, String textPending)
    {
        List<ResultTest> orderResults = allTests.stream()
                .filter(test -> test.getOrder() == order)
                .collect(Collectors.toList());

        if (search.getTypeReport() == 1 && fullFinalReport)
        {
            if (orderResults.stream().filter(orderResult -> orderResult.getState() != LISEnum.ResultTestState.VALIDATED.getValue()).collect(Collectors.toList()).size() > 0)
            {
                return new ArrayList<>();
            }
        }
        
        for (ResultTest test : allTests)
        {
            test.setResult(Tools.decryptResult(test.getResult()));
        }

        return orderResults.stream()
                .filter(test -> filterTests(test, search))
                .filter(test -> filterTestsState(test, search, textPending))
                .collect(Collectors.toList());
    }

    @Override
    public List<PrintBarcodeLog> zplReports(ReportBarcode report) throws Exception
    {
        try
        {
            final List<PrintBarcodeLog> barcodes = new ArrayList<>(0);
            final List<BarcodeDesigner> listBarcode = barcodeDao.barcodePredetermined(BARCODE);
            final BarcodeDesigner barcode = listBarcode.size() > 0 ? listBarcode.get(0) : null;
            final List<BarcodeDesigner> additionalLabels = barcodeDao.barcodePredetermined(ADDITIONAL_LABEL);
            if (report.getOrdersprint().size() > 0)
            {

                //Obtener Zpl, si la muestra es null se busca un etiqueta adicional, si no es un codigo de barras
                if (!report.getSamples().isEmpty())
                {

                    barcodes.addAll(buildZplDesigner(barcode, report.getOrdersprint(), BARCODE, report.getSamples()));
                }
                if (report.isPrintAddLabel())
                {

                    additionalLabels.stream().forEach(b
                            ->
                    {
                        barcodes.addAll(buildZplDesigner(b, report.getOrdersprint(), ADDITIONAL_LABEL, new ArrayList<>(0)));
                    });
                }
            }
            return barcodes;
        } catch (Exception e)
        {
            OrderCreationLog.error(e.getMessage());
            return new ArrayList<>(0);
        }

    }

    private List<PrintBarcodeLog> buildZplDesigner(BarcodeDesigner barcode, List<Order> order, int type, List<BarcodeSample> samples)
    {
        final List<PrintBarcodeLog> barcodes = new ArrayList<>(0);
        if (barcode != null)
        {
            final String barcodeDesigner = barcode.getCommand();
            final String orderTypeDesigner = barcode.getOrderType();
            if (barcodeDesigner != null && barcodeDesigner.trim().equals("") == false)
            {
                try
                {
                    Date dateCurrent = new Date();
                    final String dataandtime = new SimpleDateFormat(configurationServices.get("FormatoFecha").getValue() + " hh:mm").format(dateCurrent);
                    final String date = new SimpleDateFormat(configurationServices.get("FormatoFecha").getValue()).format(dateCurrent);
                    final String time = new SimpleDateFormat("hh:mm").format(dateCurrent);
                    order.stream().forEach((Order o)
                            ->
                    {
                        Long years = o.getPatient() == null ? null : Tools.calculateAge(o.getPatient().getBirthday());
                        if (type == BARCODE)
                        {
                            o.getSamples().stream().forEach((Sample s)
                                    ->
                            {
                                String zpl = buildZpl(barcodeDesigner, dataandtime, date, time, years, s, o, orderTypeDesigner);
                                BarcodeSample barcodeSample = samples.get(samples.indexOf(new BarcodeSample(s.getId())));
                                int quantity = (barcodeSample.getQuantity() == null ) ? 0 : barcodeSample.getQuantity();

                                for (int i = 0; i < quantity; i++)
                                {
                                    barcodes.add(new PrintBarcodeLog(o.getOrderNumber(), s.getId(), false, zpl));
                                }
                            });
                        } else
                        {
                            barcodes.add(new PrintBarcodeLog(o.getOrderNumber(), null, false, buildZpl(barcodeDesigner, dataandtime, date, time, years, null, o, orderTypeDesigner)));
                        }
                    });
                } catch (Exception ex)
                {
                    OrderCreationLog.error(ex.getMessage());
                    Logger.getLogger(ReportServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return barcodes;
    }

    private List<Order> buildOrder(ReportBarcode report) throws Exception
    {
        final String samplesReport = report.getSamples() == null ? "" : report.getSamples().stream().map((BarcodeSample s) -> s.getIdSample().toString()).collect(Collectors.joining(","));
        //Obtener informacion de los demograficos basicos de la orden
        final List<Demographic> demos = demographicService.list().stream().filter((Demographic t) -> t.isState()).collect(Collectors.toList());
        
        int idbranch = JWT.decode(request).getBranch();
        List<Integer> laboratorys = laboratorysByBranchesService.getidLaboratorybyBranch(idbranch);

        //Obtener informacion de la orden por rango y filtro de demograficos
        List<Order> order = ordersDao.orderReport(report.getInit(), report.getEnd(), demos, report.getDemographics(), report.getRangeType(), laboratorys, idbranch);
        if (order.size() > 0)
        {
            order.stream().forEach((Order o)
                    ->
            {
                try
                {
                    o.setPatient(patientService.get(o.getPatient().getId()));
                    String testReport = o.getTests().stream().map((Test t) -> t.getId().toString()).collect(Collectors.joining(","));
                    List<ResultTest> allTests = resultTestDao.listResultTest(o.getOrderNumber(), samplesReport, testReport);
//                        o.setResultTest(tests);
                    o.setSamples(orderServiceEnterpriseNT.getSamples(allTests));
                } catch (Exception ex)
                {
                    Logger.getLogger(ReportServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        return order.stream().filter((Order o) -> !o.getSamples().isEmpty() && o.getPatient().getId() != 0).collect(Collectors.toList());

    }

    private String buildZpl(String barcodeDesigner, String dataandtime, String date, String time, Long years, Sample s, Order o, String orderType)
    {
        List<Test> orderTests;
        List<Test> orderProfile;
        List<Test> orderTestsProfile;

        try
        {
            String orderdigit = configurationServices.get("DigitoAño").getValue();
            List<Test> orderTestsTemp1 = new ArrayList<>();
            List<Test> orderTestsTemp2 = new ArrayList<>();
            List<Test> distinctElements = new ArrayList<>();
            if (s != null)
            {
                orderTests = o.getTests().stream().filter((t -> (t.getProfile() == null || t.getProfile() == 0) && t.getSample().getId().equals(s.getId()))).collect(Collectors.toList());
                orderProfile = o.getTests().stream().filter((t -> (t.getProfile() == null || t.getProfile() == 0) && t.getSample().getId() == 0)).collect(Collectors.toList());

                for (int i = 0; i < orderProfile.size(); i++)
                {
                    int idProfile = orderProfile.get(i).getId();
                    orderTestsProfile = o.getTests().stream().filter((t -> (t.getProfile() == idProfile) && t.getSample().getId().equals(s.getId()))).collect(Collectors.toList());
                    if (orderTestsProfile.size() > 0)
                    {
                        orderTests.add(orderProfile.get(i));
                        break;
                    }
                }

                for (int i = 0; i < orderTests.size(); i++)
                {
                    if (i <= 7)
                    {
                        orderTestsTemp1.add(orderTests.get(i));
                    } else
                    {
                        orderTestsTemp2.add(orderTests.get(i));
                    }
                }

                distinctElements = s.getTests().stream()
                        .filter(distinctByKey(p -> p.getArea().getId()))
                        .collect(Collectors.toList());
            }
            String barcode = barcodeDesigner
                    .replace("{demo_-1}", o.getAccount() != null ? (o.getAccount().getName() == null ? "" : o.getAccount().getName()) : "")
                    .replace("{demo_-6}", o.getService() != null ? (o.getService().getName() == null ? "" : o.getService().getName()) : "")
                    .replace("{demo_-2}", o.getPhysician() != null ? (o.getPhysician().getName() == null ? "" : o.getPhysician().getName()) : "")
                    .replace("{demo_-3}", o.getRate() != null ? (o.getRate().getName() == null ? "" : o.getRate().getName()) : "")
                    .replace("{demo_-4}", orderType != null ? (o.getType().getId().toString().equals(orderType) ? (o.getType().getName() == null ? "" : o.getType().getName()) : "") : "")
                    .replace("{demo_-5}", o.getBranch() != null ? (o.getBranch().getName() == null ? "" : o.getBranch().getName()) : "")
                    .replace("{demo_-5_code}", o.getBranch() != null ? (o.getBranch().getCode() == null ? "" : o.getBranch().getCode()) : "")
                    .replace("{demo_-5_abbr}", o.getBranch() != null ? (o.getBranch().getAbbreviation() == null ? "" : o.getBranch().getAbbreviation()) : "")
                    .replace("{demo_-7}", o.getPatient() != null ? (o.getPatient().getRace() != null ? (o.getPatient().getRace().getName() == null ? "" : o.getPatient().getRace().getName()) : "") : "")
                    .replace("{demo_-10}", o.getPatient() != null ? (o.getPatient().getDocumentType() != null ? (o.getPatient().getDocumentType().getName() == null ? "" : o.getPatient().getDocumentType().getName()) : "") : "")
                    .replace("{externalid}", o.getExternalId() == null ? "" : o.getExternalId())
                    .replace("{demo_-111}", o.getPatient() != null ? (o.getPatient().getEmail() != null ? o.getPatient().getEmail() : "") : "")
                    .replace("{test}", s == null ? "" : orderTestsTemp1.stream().filter((Test t) -> t.getAbbr() != null).map((Test t) -> t.getAbbr()).collect(Collectors.joining("-")))
                    .replace("{test1}", s == null ? "" : orderTestsTemp2.stream().filter((Test t) -> t.getAbbr() != null).map((Test t) -> t.getAbbr()).collect(Collectors.joining("-")))
                    .replace("{area}", s == null ? "" : distinctElements.stream().filter((Test t) -> t.getArea().getAbbreviation() != null).map((Test t) -> t.getArea().getAbbreviation()).collect(Collectors.joining(", ")))
                    .replace("{dateandtime}", dataandtime)
                    .replace("{date}", date)
                    .replace("{time}", time)
                    .replace("{codeSample}", s == null ? "" : s.getCodesample())
                    .replace("{nameSample}", s == null ? "" : s.getName())
                    .replace("{container}", s == null ? "" : String.valueOf(s.getContainer().getName()))
                    .replace("{order}", String.valueOf(o.getOrderNumber()).substring(4 - Integer.parseInt(orderdigit)))
                    .replace("{ordersample}", s == null ? "" : String.valueOf(Tools.getOrderNumberToPrint(o.getOrderNumber(), Integer.parseInt(configurationServices.getValue("DigitoAño"))) + configurationServices.get("SeparadorMuestra").getValue() + s.getCodesample()))
                    .replace("{barcode}", s == null ? "" : String.valueOf(Tools.getOrderNumberToPrint(o.getOrderNumber(), Integer.parseInt(configurationServices.getValue("DigitoAño")))) + configurationServices.get("SeparadorMuestra").getValue() + (s == null ? "" : s.getCodesample()))
                    .replace("{barcodeText}", s == null ? "" : String.valueOf(Tools.getOrderNumberToPrint(o.getOrderNumber(), Integer.parseInt(configurationServices.getValue("DigitoAño"))) + configurationServices.get("SeparadorMuestra").getValue() + s.getCodesample()))
                    .replace("{ordertypecode}", o.getType().getCode())
                    .replace("{ordertypename}", o.getType().getName())
                    .replace("{history}", o.getPatient().getPatientId())
                    .replace("{namePatient}", (o.getPatient().getName1() != null ? o.getPatient().getName1().toUpperCase() : "") + " " + (o.getPatient().getName2() != null ? o.getPatient().getName2().toUpperCase() : "") + " " + (o.getPatient().getLastName() != null ? o.getPatient().getLastName().toUpperCase() : "") + " " + (o.getPatient().getSurName() != null ? o.getPatient().getSurName().toUpperCase() : ""))
                    .replace("{age}", years == null ? "" : String.valueOf(years))
                    .replace("{gender}", o.getPatient().getSex().getCode().equals("2") ? "F" : o.getPatient().getSex().getCode().equals("1") ? "M" : "I")
                    .replace("{documenttypecode}", o.getPatient().getDocumentType().getAbbr())
                    .replace("{documenttypename}", o.getPatient().getDocumentType().getName())
                    .replace("{birthday}", new SimpleDateFormat(configurationServices.get("FormatoFecha").getValue()).format(o.getPatient().getBirthday()));

            for (DemographicValue demographic : o.getDemographics())
            {
                barcode = barcode.replace("{demo_" + demographic.getIdDemographic() + "}", demographic.isEncoded() ? (demographic.getCodifiedName() == null ? "" : demographic.getCodifiedName()) : demographic.getNotCodifiedValue() == null ? "" : demographic.getNotCodifiedValue());
            }
            for (DemographicValue demographic : o.getPatient().getDemographics())
            {
                barcode = barcode.replace("{demo_" + demographic.getIdDemographic() + "}", demographic.isEncoded() ? demographic.getCodifiedName() == null ? "" : demographic.getCodifiedName() : demographic.getNotCodifiedValue() == null ? "" : demographic.getNotCodifiedValue());
            }

            return barcode;
        } catch (Exception ex)
        {
            OrderCreationLog.error(ex);
            Logger.getLogger(ReportServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<String> listSerials() throws Exception
    {
        return serialDao.list();
    }

    @Override
    public SerialPrint createSerial() throws Exception
    {
        String serial = "";
        // se valida si ya existe el serial generado, si ya existe lo vuelve a generar
        do
        {
            serial = Tools.generateUUID();
        } while (isSerial(serial));
        return serialDao.create(serial, request.getRemoteAddr());
    }

    @Override
    public boolean isSerial(String serial) throws Exception
    {
        return serialDao.isSerial(serial);
    }

    @Override
    public List<PrintReportLog> printingByType(final FilterOrderHeader search) throws Exception
    {
        final List<PrintReportLog> list = new ArrayList<>();
        if (search.getPrintingType() == PRINTINGTYPE_REPORT)
        {
            printByReport(search, list, configurationServices.get("TipoImpresion").getValue());
        }
        return list;
    }

    @Override
    public PrintOrder finalReport(final FilterOrderHeader search) throws Exception
    {
        String languageReport = configurationServices.get("IdiomaReporteResultados").getValue();
        //Funcion que manda a imprimir el reporte por orden
        if (search.getPrintOrder() != null && search.getPrintOrder().size() > 0)
        {
            //Obtener ordenes completas
            for (int i = 0; i < search.getPrintOrder().size(); i++)
            {
                for (int j = 0; j < search.getPrintOrder().get(i).getListOrders().size(); j++)
                {
                    Order order = printReporByOrder(search, search.getPrintOrder().get(i).getListOrders().get(j).getOrder());
                    if (order != null)
                    {
                        
                        search.getPrintOrder().get(i).getListOrders().get(j).setOrderNumber(order.getOrderNumber());
                        search.getPrintOrder().get(i).getListOrders().get(j).setAttachments(order.getAttachments());
                        order.setAttachments(null);
                        search.getPrintOrder().get(i).getListOrders().get(j).setPatientName(
                                (order.getPatient().getName1() != null ? order.getPatient().getName1().equals("") ? "" : order.getPatient().getName1() : "")
                                + (order.getPatient().getName2() != null ? order.getPatient().getName2().equals("") ? "" : " " + order.getPatient().getName2() : "")
                                + (order.getPatient().getLastName() != null ? order.getPatient().getLastName().equals("") ? "" : " " + order.getPatient().getLastName() : "")
                                + (order.getPatient().getSurName() != null ? order.getPatient().getSurName().equals("") ? "" : " " + order.getPatient().getSurName() : ""));
                        
                        search.getPrintOrder().get(i).getListOrders().get(j).setListTestPending(gettestpendingorder(order.getOrderNumber()));
                        
                        switch (search.getTypeNameFile())
                        {
                            case 1:
                                search.getPrintOrder().get(i).getListOrders().get(j).setNameFile(order.getOrderNumber().toString());
                                break;
                            case 2:
                                search.getPrintOrder().get(i).getListOrders().get(j).setNameFile(order.getOrderNumber().toString() + "_" + order.getPatient().getPatientId());
                                break;
                            case 3:
                                search.getPrintOrder().get(i).getListOrders().get(j).setNameFile(order.getOrderNumber().toString() + "_" + search.getPrintOrder().get(i).getListOrders().get(j).getPatientName().replace(" ", "_"));
                                break;
                            default:
                                search.getPrintOrder().get(i).getListOrders().get(j).setNameFile(order.getOrderNumber().toString());
                                break;
                        }
                        search.getPrintOrder().get(i).getListOrders().get(j).setPatientEmail(order.getPatient().getEmail());
                        search.getPrintOrder().get(i).getListOrders().get(j).setPatientHistory(order.getPatient().getPatientId());
                        search.getPrintOrder().get(i).getListOrders().get(j).setJsonOrder(Tools.jsonObject(order));
                        search.getPrintOrder().get(i).getListOrders().get(j).getOrder().setLanguageReport(languageReport);
                    } else
                    {
                        return null;
                    }
                }
            }
            //Enviar ordenes
            for (int i = 0; i < search.getPrintOrder().size(); i++)
            {
                search.getPrintOrder().get(i).setListOrders(search.getPrintOrder().get(i).getListOrders().stream().filter(p -> p.getJsonOrder() != null).collect(Collectors.toList()));
                if (search.getPrintOrder().get(i).getListOrders().size() > 0)
                {
                    Order order = search.getPrintOrder().get(i).getListOrders().get(0).getOrder();
                    DemographicValue object = new DemographicValue();
                    Configuration valueEmail = configurationDao.get("DemograficoEncriptacionCorreo");
                    object = order.getDemographics().stream()
                            .filter(demo -> Objects.equal(demo.getIdDemographic(), Integer.parseInt(valueEmail.getValue())))
                            .findFirst().orElse(null);

                    if (object != null)
                    {
                        List<DemographicReportEncryption> countList = new ArrayList<>();

                        countList = demographicService.getDemographicByIdAndDemographicitem(object.getIdDemographic(), object.getCodifiedId());
                        if (!countList.isEmpty())
                        {
                            search.getPrintOrder().get(i).getListOrders().get(0).setEncrypt(true);
                        }
                    } else
                    {
                        search.getPrintOrder().get(i).getListOrders().get(0).setEncrypt(false);
                    }

                    if (search.getPrintOrder().get(i).getPhysician() == null)
                    {

                    } else
                    {
                        List<String> mails = new ArrayList<>();
                        mails.add(search.getPrintOrder().get(i).getPhysician().getEmail());
                        if (search.getPrintOrder().get(i).getPhysician().getAlternativeMails() != null)
                        {
                            mails.addAll(Arrays.asList(search.getPrintOrder().get(i).getPhysician().getAlternativeMails().replace("[", "").replace("]", "").replaceAll("\"", "").split(",")));
                            search.getPrintOrder().get(i).getPhysician().setEmail(String.join(";", mails));
                        }
                    }
                    search.getPrintOrder().get(i).getListOrders().get(0).setJsonOrder("");
                }

            }

        } else
        {
            throw new EnterpriseNTException(Arrays.asList("0| Order"));
        }

        return search.getPrintOrder().get(0);
    }

    @Override
    public boolean printFinalReport(PrintNode printOrder) throws Exception
    {
        List<String> emailData = new ArrayList<>(0);
        if (printOrder.getPrintingMedium() == 3)
        {
            emailData = configurationServices.bodyEmail();
        }
        String serial = printOrder.getSerial();

        if (configurationServices.get("TipoImpresion").getValue().equals("2"))
        {
            SerialPrint serialPrint = servicePrintService.getByService(printOrder.getBranch(), printOrder.getService());
            if (serialPrint != null)
            {
                serial = serialPrint.getSerial();
            }
        }
        if (configurationServices.get("EnviarCopiaInformes").getValue().equals("True"))
        {
            printOrder.setPatientEmail(printOrder.getPatientEmail() + ";" + configurationServices.get("SmtpAuthUser").getValue());

        }
        PrintNode printnew = new PrintNode(null, null, null,
                (printOrder.getNumberCopies() < 1) ? 1 : printOrder.getNumberCopies(),
                false,
                printOrder.getPrintingMedium(),
                printOrder.getSendEmail(),
                emailData.size() == 0 ? "" : emailData.get(0).replace("||PATIENT||", printOrder.getPatientName() != null ? printOrder.getPatientName() : ""),
                emailData.size() == 0 ? "" : emailData.get(1),
                emailData.size() == 0 ? "" : emailData.get(2),
                printOrder.getEncryptionReportResult(),
                printOrder.getBufferReport(),
                printOrder.getNameFile(),
                printOrder.getPatientEmail(),
                printOrder.getPhysicianEmail(),
                printOrder.getOrder());

        printnew.setBranch(printOrder.getBranch());

        boolean send = sendPrinting(Tools.jsonObject(printnew), serial, Print.PRINTER_REPORT);
        
        IntegrationHisLog.info("Orden printFinalReport  " + printOrder.getOrder());
        IntegrationHisLog.info("Respuesta  " + send);
        IntegrationHisLog.info("===============================================================");
        return send;

    }

    @Override
    public List<PrintBarcodeLog> printingByBarcode(final FilterOrderBarcode search) throws Exception
    {
        List<PrintBarcodeLog> list = new ArrayList<>();
        if (search.getPrintingType() == PRINTINGTYPE_BARCODE)
        {
            list = printByBarcode(search);
            updatePrintSample(search.getOrdersprint());
        }
        return list;
    }

    /**
     * Logica para la impresion de una etiqueta de barras
     *
     * @param search
     * {@link net.cltech.enterprisent.domain.operation.common.FilterOrderHeader}
     * @param list Lista de informacion de impresion
     * {@link net.cltech.enterprisent.domain.operation.reports.PrintReportLog}
     * @throws Exception
     */
    private List<PrintBarcodeLog> printByBarcode(final FilterOrderBarcode search) throws Exception
    {
        //Impresion a codigo de barras y etiqueta adicional
        List<Demographic> demos = search.getDemographics().stream().map((FilterDemographic d)
                ->
        {
            Demographic demographicNew = new Demographic();
            demographicNew.setId(d.getDemographic());
            demographicNew.setDemographicItem(d.getDemographicItems().isEmpty() ? null : d.getDemographicItems().get(0));
            return demographicNew;
        }).collect(Collectors.toList());
        ReportBarcode report = new ReportBarcode(search.getInit(), search.getEnd(), search.getSamples(), search.getSerial(), search.getRangeType(), search.isPrintAddLabel(), demos);
        report.setOrdersprint(search.getOrdersprint());
        List<PrintBarcodeLog> jsons = zplReports(report);
        for (PrintBarcodeLog json : jsons)
        {
            json.setPrinting(sendPrinting(json.getZpl(), search.getSerial(), Print.PRINTER_LABEL));
            json.setZpl("");
        }
        return jsons;
    }

    /**
     * Logica para la impresion de un reporte
     *
     * @param search
     * {@link net.cltech.enterprisent.domain.operation.common.FilterOrderHeader}
     * @param list Lista de informacion de impresion
     * {@link net.cltech.enterprisent.domain.operation.reports.PrintReportLog}
     * @throws Exception
     */
    private void printByReport(final FilterOrderHeader search, List<PrintReportLog> list, String typePrint) throws Exception
    {
        //Funcion que manda a imprimir el reporte por orden
        if (search.getPrintOrder() != null && search.getPrintOrder().size() > 0)
        {
            List<String> emailData = new ArrayList<>(0);
            if (search.getPrintingMedium() == 3)
            {
                emailData = configurationServices.bodyEmail();
            }
            //Obtener ordenes completas
            for (int i = 0; i < search.getPrintOrder().size(); i++)
            {
                for (int j = 0; j < search.getPrintOrder().get(i).getListOrders().size(); j++)
                {
                    Order order = printReporByOrder(search, search.getPrintOrder().get(i).getListOrders().get(j).getOrder());
                    if (order == null)
                    {
                        order = search.getPrintOrder().get(i).getListOrders().get(j).getOrder();
                        search.getPrintOrder().get(i).getListOrders().get(j).setOrderNumber(order.getOrderNumber());

                        order.getPatient().setName2(order.getPatient().getName2() == null ? "" : order.getPatient().getName2());
                        order.getPatient().setSurName(order.getPatient().getSurName() == null ? "" : order.getPatient().getSurName());

                        PrintReportLog aux = new PrintReportLog(order.getOrderNumber(), order.getPatient().getName1() + (order.getPatient().getName2().equals("") ? "" : " " + order.getPatient().getName2()) + " " + order.getPatient().getLastName() + (order.getPatient().getSurName().equals("") ? "" : " " + order.getPatient().getSurName()), null);
                        aux.setPrinting(false);
                        list.add(aux);
                        search.getPrintOrder().get(i).getListOrders().get(j).setJsonOrder(null);
                    } else
                    {
                        search.getPrintOrder().get(i).getListOrders().get(j).setOrderNumber(order.getOrderNumber());
                        search.getPrintOrder().get(i).getListOrders().get(j).setAttachments(order.getAttachments());
                        order.setAttachments(null);
                        search.getPrintOrder().get(i).getListOrders().get(j).setPatientName(order.getPatient().getName1() + (order.getPatient().getName2().equals("") ? "" : " " + order.getPatient().getName2()) + " " + order.getPatient().getLastName() + (order.getPatient().getSurName().equals("") ? "" : " " + order.getPatient().getSurName()));
                        switch (search.getTypeNameFile())
                        {
                            case 1:
                                search.getPrintOrder().get(i).getListOrders().get(j).setNameFile(order.getOrderNumber().toString());
                                break;
                            case 2:
                                search.getPrintOrder().get(i).getListOrders().get(j).setNameFile(order.getOrderNumber().toString() + "_" + order.getPatient().getPatientId());
                                break;
                            case 3:
                                search.getPrintOrder().get(i).getListOrders().get(j).setNameFile(order.getOrderNumber().toString() + "_" + search.getPrintOrder().get(i).getListOrders().get(j).getPatientName().replace(" ", "_"));
                                break;
                            default:
                                search.getPrintOrder().get(i).getListOrders().get(j).setNameFile(order.getOrderNumber().toString());
                                break;
                        }
                        search.getPrintOrder().get(i).getListOrders().get(j).setPatientEmail(order.getPatient().getEmail());
                        search.getPrintOrder().get(i).getListOrders().get(j).setPatientHistory(order.getPatient().getPatientId());
                        search.getPrintOrder().get(i).getListOrders().get(j).setJsonOrder(Tools.jsonObject(order));
                        // Correos a lo que se les enviara dicho preporte

                    }
                }
            }
            //Enviar ordenes
            for (int i = 0; i < search.getPrintOrder().size(); i++)
            {
                search.getPrintOrder().get(i).setListOrders(search.getPrintOrder().get(i).getListOrders().stream().filter(p -> p.getJsonOrder() != null).collect(Collectors.toList()));
                if (search.getPrintOrder().get(i).getListOrders().size() > 0)
                {
                    Order order = search.getPrintOrder().get(i).getListOrders().get(0).getOrder();
                    boolean encryptionReportResult = false;
                    DemographicValue object = new DemographicValue();
                    Configuration valueEmail = configurationDao.get("DemograficoEncriptacionCorreo");
                    object = order.getDemographics().stream()
                            .filter(demo -> Objects.equal(demo.getIdDemographic(), Integer.parseInt(valueEmail.getValue())))
                            .findFirst().orElse(null);

                    if (object != null)
                    {
                        List<DemographicReportEncryption> countList = new ArrayList<>();

                        countList = demographicService.getDemographicByIdAndDemographicitem(object.getIdDemographic(), object.getCodifiedId());
                        if (!countList.isEmpty())
                        {
                            encryptionReportResult = true;
                        }
                    }

                    if (search.getPrintOrder().get(i).getPhysician() == null)
                    {

                        PrintReportLog aux = new PrintReportLog(order.getOrderNumber(), order.getPatient().getName1() + (order.getPatient().getName2().equals("") ? "" : " " + order.getPatient().getName2()) + " " + order.getPatient().getLastName() + (order.getPatient().getSurName().equals("") ? "" : " " + order.getPatient().getSurName()), null);
                        search.getPrintOrder().get(i).getListOrders().get(0).setOrder(null);
                        String serial = search.getSerial();

                        if (typePrint.equals("2"))
                        {
                            SerialPrint serialPrint = servicePrintService.getByService(order.getBranch().getId(), order.getService().getId());
                            if (serialPrint != null)
                            {
                                serial = serialPrint.getSerial();
                            }
                        }

                        boolean send = sendPrinting(Tools.jsonObject(new PrintNode(search.getPrintOrder().get(i), search.getLabelsreport(), search.getVariables(), (search.getNumberCopies() < 1) ? 1 : search.getNumberCopies(), search.isAttached(), search.getPrintingMedium(), search.getSendEmail(), emailData.size() == 0 ? "" : emailData.get(0), emailData.size() == 0 ? "" : emailData.get(1), emailData.size() == 0 ? "" : emailData.get(2), encryptionReportResult, null, null, null, null, null)), serial, Print.PRINTER_REPORT);
                        aux.setPrinting(send);
                        if (send)
                        {
                            //Cambio de estado de Finales y re impreso
                            changeStateTest(search, order, -1);
                        }
                        list.add(aux);
                    } else
                    {
                        List<String> mails = new ArrayList<>();
                        mails.add(search.getPrintOrder().get(i).getPhysician().getEmail());
                        if (search.getPrintOrder().get(i).getPhysician().getAlternativeMails() != null)
                        {
                            mails.addAll(Arrays.asList(search.getPrintOrder().get(i).getPhysician().getAlternativeMails().replace("[", "").replace("]", "").replaceAll("\"", "").split(",")));
                            search.getPrintOrder().get(i).getPhysician().setEmail(String.join(";", mails));
                        }
                        //Enviar lista de ordenes a imprimir
                        boolean send = sendPrinting(Tools.jsonObject(new PrintNode(search.getPrintOrder().get(i), search.getLabelsreport(), search.getVariables(), (search.getNumberCopies() < 1) ? 1 : search.getNumberCopies(), search.isAttached(), search.getPrintingMedium(), search.getSendEmail(), emailData.size() == 0 ? "" : emailData.get(0), emailData.size() == 0 ? "" : emailData.get(1), emailData.size() == 0 ? "" : emailData.get(2), encryptionReportResult, null, null, null, null, null)), search.getSerial(), Print.PRINTER_REPORT);
                        //Cambiar estados de las ordenes
                        search.getPrintOrder().get(i).getListOrders().stream().forEach(o
                                ->
                        {
                            if (send)
                            {
                                try
                                {
                                    //Cambio de estado de Finales y re impreso
                                    changeStateTest(search, o.getOrder(), -1);
                                } catch (Exception ex)
                                {
                                    Logger.getLogger(ReportServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            list.add(new PrintReportLog(o.getOrder().getOrderNumber(), o.getOrder().getPatient().getName1() + (o.getOrder().getPatient().getName2().equals("") ? "" : " " + o.getOrder().getPatient().getName2()) + " " + o.getOrder().getPatient().getLastName() + (o.getOrder().getPatient().getSurName().equals("") ? "" : " " + o.getOrder().getPatient().getSurName()), null, send));
                        });
                    }
                }
            }
        } else
        {
            throw new EnterpriseNTException(Arrays.asList("0| Order"));
        }
    }

    /**
     * Metodos para imprimir etiquetas de codigo de barras directamente
     *
     * @param code
     */
    private void printBarcode(String code)
    {
        try
        {
            PrintService pri = findPrintService("ZDesigner LP 2824 Plus (ZPL)");
            barcodePrint(pri, code);
        } catch (PrintException | InterruptedException ex)
        {
            Logger.getLogger(ReportServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Buscar el servicio de la impresora por nombre
     *
     * @param printerName
     * @return
     */
    private PrintService findPrintService(String printerName)
    {
        printerName = printerName.toLowerCase();
        PrintService service = null;
        PrintService services[] = PrinterJob.lookupPrintServices();
        for (int index = 0; service == null && index < services.length; index++)
        {
            if (services[index].getName().toLowerCase().contains(printerName))
            {
                service = services[index];
            }
        }
        return service;
    }

    /**
     * Enviar a imprimir la etiqueta
     *
     * @param printer
     * @param cod
     * @throws PrintException
     * @throws InterruptedException
     */
    public void barcodePrint(PrintService printer, String cod) throws PrintException, InterruptedException
    {
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        DocPrintJob job = printer.createPrintJob();
        StringBuilder buffer = new StringBuilder();
        buffer.append(cod);

        Doc doc = new SimpleDoc(buffer.toString().getBytes(), flavor, null);
        job.print(doc, null);
        Thread.sleep(20L);
    }

    /**
     * Obtener informacion completa de la orden para imprimir
     *
     * @param filterOrder
     * @return
     * @throws Exception
     */
    @Override
    public Order printReporByOrder(FilterOrderHeader filterOrder, Order order) throws Exception
    {
        Order orderFinal = null;
        try
        {
            AuthorizedUser user = JWT.decode(request);
            if (StreamFilters.containsDemographic(order, filterOrder.getDemographics()))
            {
                // List<Demographic> demographics = serviceDemographic.demographicsList();
                String textPending = configurationServices.get("ComentarioResultadoPendiente").getValue();
                boolean fullFinalReport = Objects.equal(filterOrder.getCompleteOrder(), 2);
                boolean printAttachments = configurationServices.get("ImprimirAdjuntos").getValue().equalsIgnoreCase("True");
                order.setBranch(order.getBranch() != null && order.getBranch().getId() != null && order.getBranch().getId() > 0 ? order.getBranch() : new Branch());
                Order completeOrder = order;//orderBody(new FilterOrder(new ArrayList<>(0), demographics, order));

                List<ResultTest> tests = getTestResults(Arrays.asList(completeOrder.getOrderNumber()), "", user.isConfidential())
                        .stream()
                        .filter(testTwo -> testTwo.getSampleState() > 3)
                        .collect(Collectors.toList());
                

                completeOrder.setResultTest(getOrderTests(completeOrder.getOrderNumber(), tests, filterOrder, fullFinalReport, textPending));

                if (!completeOrder.getResultTest().isEmpty())
                {
                    completeOrder.setComments(commentService.listCommentOrder(completeOrder.getOrderNumber(), null).stream().filter(comment -> comment.isPrint()).collect(Collectors.toList()));
                    if (printAttachments)
                    {
                        completeOrder.setAttachments(new ArrayList<>());
                        completeOrder.getAttachments().addAll(documentServices.listattachments(completeOrder.getOrderNumber()));
                        completeOrder.getAttachments().addAll(documentServices.listattachments(completeOrder.getOrderNumber(), null));
                    }

                    final List<Integer> testsfilter = completeOrder.getResultTest().stream().map((s) -> s.getTestId()).collect(Collectors.toList());
                    completeOrder.setImageTest(serviceResult.getResultGraphics(completeOrder.getOrderNumber(), testsfilter));
                    orderFinal = serviceResult.addAdditional(Arrays.asList(completeOrder)).get(0);
                }
            }
        } catch (Exception ex)
        {
            Logger.getLogger(ReportServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orderFinal;
    }

    /**
     * Metodo para cambiar el estado de los examenes
     *
     * @param filterOrder
     * @param ordernew
     * @param iduser
     *
     * @throws Exception
     */
    @Override
    public void changeStateTest(FilterOrderHeader filterOrder, Order ordernew, int iduser) throws Exception
    {
        Order orderComplete = new Order();
        orderComplete = ordernew;
        Item item = new Item();
        AuthorizedUser user = JWT.decode(request);
        String personResive = user.getName() + " " + user.getLastName();
        switch (filterOrder.getPrintingMedium())
        {
            case 1:
                item.setId(59);
                break;
            case 2:
                item.setId(62);
                break;
            case 3:
                personResive = filterOrder.getPersonReceive();
                item.setId(60);
                break;
            case 4:
                personResive = filterOrder.getPersonReceive();
                item.setId(61);
                break;
            case 5:
                item.setId(62);
                break;
            default:
                break;
        }
        orderComplete.setDeliveryType(item);
        IntegrationHisLog.info("Resultado automatico " + filterOrder.getSendAutomaticResult());
        IntegrationHisLog.info("orden " + orderComplete.getOrderNumber() );
        IntegrationHisLog.info("Examenes " + Tools.jsonObject(orderComplete.getResultTest()));
        IntegrationHisLog.info("===============================================================");
        
        if (filterOrder.getTypeReport() == 0 || filterOrder.getTypeReport() == 5 )
        {
            orderComplete.setResultTest(orderComplete.getResultTest().stream().filter((ResultTest test) -> test.getState() == LISEnum.ResultTestState.DELIVERED.getValue() || test.getState() == LISEnum.ResultTestState.VALIDATED.getValue()).collect(Collectors.toList()));
            resultServiceEnterpriseNT.reportTests(orderComplete, iduser, personResive, filterOrder.getSendAutomaticResult());
        } else if (filterOrder.getTypeReport() == 1)
        {
            resultServiceEnterpriseNT.reportTests(orderComplete, iduser, personResive, filterOrder.getSendAutomaticResult());
            orderComplete.setResultTest(orderComplete.getResultTest().stream().filter((ResultTest test) -> test.getState() == LISEnum.ResultTestState.VALIDATED.getValue()).collect(Collectors.toList()));
            for (ResultTest test : orderComplete.getResultTest())
            {
                if (!deliveryResultDao.getDeliverybyOrderTest(orderComplete.getOrderNumber(), test.getTestId()))
                {
                    DeliveryResult deliveryResult = new DeliveryResult();
                    deliveryResult.setOrderNumber(orderComplete.getOrderNumber());
                    deliveryResult.setReceivesPerson(personResive);
                    deliveryResult.setIdTest(test.getTestId());
                    deliveryResult.setIdProfile(test.getProfileId());
                    deliveryResult.setTypeDelivery(filterOrder.getPrintingMedium());

                    user.setId(iduser);
                    deliveryResult.setUser(user);

                    deliveryResultDao.create(deliveryResult);
                }
            }
        }
    }

    @Override
    public List<Order> orderHeader(Filter search) throws Exception
    {
        List<Demographic> demographics = serviceDemographic.demographicsList();
        List<Order> list = listDao.ordersHeader(search.getInit(), search.getEnd(), search.getRangeType(), search.getOrders(), search.getTests(), search.getOrderingPrint(), search.getDemographics(), demographics, search.getTestFilterType());        //Se remueven las ordenes hijas de rellamado si su padre se encuentra dentro del filtro
        List<Order> listDuplicate = list.stream()
                .filter(o -> o.getFatherOrder() == null || o.getFatherOrder() == 0)
                .collect(Collectors.toList());
        list.removeIf(o -> o.getFatherOrder() > 0 && containsFather(listDuplicate, o.getFatherOrder()));
        
        
        //Se consultan los medicos auxiliares
        if ("True".equals(configurationServices.get("MedicosAuxiliares").getValue()))
        {
            list.forEach(order -> {
                try {
                    order.setAuxiliaryPhysicians(getAuxPhysicians(order.getOrderNumber()));
                } catch (Exception ex) {
                    Logger.getLogger(ReportServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            });      
        }
        return list;
    }
    
    private List<Physician> getAuxPhysicians(Long idOrder) throws Exception
    {
        return ordersDao.getAuxiliaryPhysicians(idOrder);
    }

    private boolean containsFather(List<Order> list, Long orderNumber)
    {
        return list.contains(new Order(orderNumber));
    }

    @Override
    public Order orderBody(FilterOrder order) throws Exception
    {
        return listDao.ordersBody(order.getOrder(), order.getTests(), order.getDemographics());
    }

    @Override
    public boolean sendPrinting(String json, String serial, int type) throws Exception
    {
        boolean send = false;
        if (!json.equals(""))
        {
            Print message = new Print();
            message.setMessage(json);
            message.setType(2);
            message.setTypePrinter(type);
            message.setSerial(serial);
            List<WebSocketSession> sessions = printHandler.findSerialSessions(serial);
            if (sessions != null && !sessions.isEmpty() && sessions.size() > 0)
            {
                message.setReceivers(Arrays.asList(serial));
                send = printHandler.sendPrint(message, sessions.get(0));
            } else
            {
                throw new EnterpriseNTException(Arrays.asList("0| the client is not connected"));
            }
        }
        return send;
    }

    @Override
    public List<Order> ordersBarcode(FilterOrderBarcode search) throws Exception
    {
        //Impresion a codigo de barras y etiqueta adicional
        List<Demographic> demos = search.getDemographics().stream().map((FilterDemographic d)
                ->
        {
            Demographic demographicNew = new Demographic();
            demographicNew.setId(d.getDemographic());
            demographicNew.setDemographicItem(d.getDemographicItems().isEmpty() ? null : d.getDemographicItems().get(0));
            return demographicNew;
        }).collect(Collectors.toList());
        ReportBarcode report = new ReportBarcode(search.getInit(), search.getEnd(), search.getSamples(), search.getSerial(), search.getRangeType(), search.isPrintAddLabel(), demos);
        return buildOrder(report);
    }

    private static final int PRINTINGTYPE_REPORT = 1;
    private static final int PRINTINGTYPE_BARCODE = 2;
    private static final int BARCODE = 1;
    private static final int ADDITIONAL_LABEL = 2;
    private static final int BARCODE_PATIENT = 3;

    /**
     * Recive un id de una orden y retorna un dato en base64
     *
     * @param idOrderHis
     * @return Dato en base 64
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public String getBase64(String idOrderHis, String userName, String password) throws Exception
    {
        try
        {
            if (!jdbcRepUrl.equals("NA"))
            {
                // Autenticación del usuario para obtener el reporte final
                byte[] decodedBytes = Base64.getDecoder().decode(userName);
                byte[] decodedBytesTwo = Base64.getDecoder().decode(password);
                String userNameConvert = new String(decodedBytes);
                String passConvert = new String(decodedBytesTwo);
                User userExist = userDao.get(userNameConvert, passConvert);
                if (userExist != null)
                {
                    List<Order> listOrder;
                    List<ResultTest> resultsList = new ArrayList<>();
                    Long idOrder = ordersDao.getOrderLisHis(idOrderHis).get(0);
                    String toBaseSixtyFour;
                    if (idOrder != 0)
                    {
                        Filter search = new Filter();
                        search.setRangeType(1);
                        search.setInit(idOrder);
                        search.setEnd(idOrder);
                        // Obtenemos el listado de una orden
                        listOrder = orderHeader(search);
                        // Obtenemos los resultados de la orden enviada
                        for (ResultTest resultTest : getTestResults(null, idOrderHis, userExist.isConfidential()))
                        {
                            if (resultTest != null)
                            {
                                if (resultTest.getState() == 4)
                                {
                                    // Agregamos dichos resultados
                                    resultsList.add(resultTest);
                                }
                            }
                        }

                        listOrder.get(0).setResultTest(resultsList);
                        if (listOrder.size() > 0 && listOrder.get(0).getResultTest().size() > 0)
                        {
                            Date objDate = new Date();
                            String strDateFormat = configurationServices.getValue("FormatoFecha");
                            SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);

                            FilterOrderHeader filterOrder = new FilterOrderHeader();
                            filterOrder.setOrder(Tools.jsonObject(listOrder.get(0)));
                            filterOrder.setLabelsreport("{\"1000\":\"Resultado literal\",\"1001\":\"Valores de referencia\",\"1002\":\"Valores de referencia de antibiótico\",\"1003\":\"Destino de microbiología\",\"1004\":\"Sistema central\",\"1005\":\"Homologación de usuarios\",\"1006\":\"Tipo de pago\",\"1007\":\"Entidad\",\"1008\":\"Protocolo EDI\",\"1009\":\"Resoluciones\",\"1010\":\"Vigencia\",\"1011\":\"Receptor EDI\",\"1012\":\"Pruebas por laboratorio\",\"1013\":\"Rol\",\"1014\":\"Ingresado\",\"1015\":\"Alarma\",\"1016\":\"Auditoría del usuario\",\"1017\":\"Permite el transporte y clasificacion de muestras sanguineas en tubo, frascos de orina y coprológicos\",\"1018\":\"Inteligencia de negocios\",\"1019\":\"¡Debe estar en el módulo de ingreso o verificación de muestra para la gestión de turnos!\",\"1020\":\"¡Debe configurar en servicio para el módulo de ingreso de órdenes!\",\"1021\":\"¡Debe configurar en servicio para el módulo de entrada de muestra!\",\"1022\":\"Lista de selección\",\"1023\":\"Lista de selección múltiple\",\"1024\":\"Remisiones\",\"1025\":\"Anormal\",\"1026\":\"Maligno\",\"1027\":\"Analizador\",\"1028\":\"Química Clínica\",\"1029\":\"Patología\",\"1030\":\"Citología\",\"1031\":\"No Imprimir\",\"1032\":\"Imprimir en modificaciones\",\"1033\":\"Externamente\",\"1034\":\"Paquete\",\"1035\":\"Orden de impresión\",\"1036\":\"Interno\",\"1037\":\"Externo\",\"1038\":\"Opción\",\"1039\":\"Prueba automática\",\"1040\":\"Resultado literal por prueba\",\"1041\":\"Asignación de destino\",\"1042\":\"Línea\",\"1043\":\"Valor por defecto\",\"1044\":\"Procedimiento por prueba\",\"1045\":\"Medio de cultivo por prueba\",\"1046\":\"Muestra por servicio\",\"1047\":\"Oportunidad de la muestra\",\"1048\":\"Oportunidad de la prueba\",\"1049\":\"Homologación de demográficos\",\"1050\":\"Homologación de pruebas\",\"1051\":\"Aseguradora del estado\",\"1052\":\"Aseguradora privada\",\"1053\":\"Cuenta privada\",\"1054\":\"Particular\",\"1055\":\"Otros\",\"1056\":\"Original\",\"1057\":\"Corregido\",\"1058\":\"Reemplazo o vacío\",\"1059\":\"Producción\",\"1060\":\"Tarifa por cliente\",\"1061\":\"Días de alarma pendientes de pago\",\"1062\":\"Impuesto por prueba\",\"1063\":\"Asignación de precios por tarifa\",\"1064\":\"Excluir pruebas por demográfico\",\"1065\":\"Copia\",\"1066\":\"Informe previo\",\"1067\":\"¡Debe configurar el serial de impresión!\",\"1068\":\"Herramientas de impresión\",\"1069\":\"Serial de impresión\",\"1070\":\"No configurado\",\"1071\":\"Cliente de impresión\",\"1072\":\"Informe de órdenes no impresas\",\"1073\":\"Órdenes completas\",\"1074\":\"Verifique la conexión con el cliente de impresión\",\"1075\":\"Control de órdenes\",\"1076\":\"Órdenes no impresas\",\"1077\":\"¡No existen registros para el filtro seleccionado!\",\"1078\":\"¡No existen registros para este día!\",\"1079\":\"Permite al laboratorio alcanzar los requisitos exigidos por la norma  ISO 15189. Con el objetivo de alcanzar el reconocimiento de la acreditación.\",\"1080\":\"Permite transformar los datos en conocimiento, con el objetivo de generar inteligencia de negocios y optimizar el proceso de toma de decisiones\",\"1081\":\"Activar código de barras\",\"1082\":\"Desactivar código de barras\",\"1083\":\"Activar etiqueta adicional\",\"1084\":\"Desactivar etiqueta adicional\",\"1085\":\"¡El servidor node de impresión no se encuentra activo, comuniquese con el administrador!\",\"1086\":\"¿Desea activar la versión ## de etiqueta adicional?\",\"1087\":\"¿Desea desactivar la versión ## de etiqueta adicional?\",\"1088\":\"¡Ha superado el tiempo de inactividad!\",\"1089\":\"La sesión ha expirado\",\"1090\":\"Representación numérica\",\"1091\":\"Activo\",\"1092\":\"Inactivo\",\"1093\":\"Copia de informe de resultados\",\"1094\":\"Responsable\",\"1095\":\"Consulta externa\",\"1096\":\"Rango mínimo\",\"1097\":\"Rango máximo\",\"1098\":\"Color\",\"1099\":\"Respuesta\",\"1100\":\"Cargando\",\"1101\":\"Módulo para consultar, ingresar y/o actualizar las órdenes del laboratorio.\",\"1102\":\"Módulo para consultar, ingresar y/o actualizar los pacientes del laboratorio.\",\"1103\":\"Módulo para la gestión de los resultados de las pruebas del laboratorio.\",\"1104\":\"Módulo para la verificación, siembra e ingreso de resultados de las ordenes que pertenecen a muestras de microbiología.\",\"1105\":\"Módulo para generar informes o consultas detalladas acerca de órdenes que contienen resultados.\",\"1106\":\"Módulo para la generación de informes estadísticos, que permiten el análisis de los datos ingresados en el sistema.\",\"1107\":\"Módulo para controlar los cambios realizados por los usuarios en el sistema, para el control y/o verificación de los módulos del sistema. \",\"1108\":\"Módulo que permite al usuario realizar procesos de control y opciones especiales dentro del sistema.\",\"1109\":\"Gestión de órdenes\",\"1110\":\"Gestión de pacientes\",\"1111\":\"Menú principal\",\"1112\":\"Lista de accesos directos\",\"1113\":\"Órdenes ingresadas\",\"1114\":\"Turnos atendidos\",\"1115\":\"Turnos en espera\",\"1116\":\"Calificación del servicio\",\"1117\":\"Excelente\",\"1118\":\"Bueno\",\"1119\":\"Regular\",\"1120\":\"Malo\",\"1121\":\"Módulo para obtener información relevante y oportuna para evaluar diversos aspectos del laboratorio.\",\"1122\":\"Por su seguridad es necesario establecer una nueva contraseña\",\"1123\":\"La contraseña ha sido actualizada\",\"1124\":\"El usuario no se encuentra activo\",\"1125\":\"La contraseña anterior y la nueva contraseña son iguales\",\"1126\":\"Lun\",\"1127\":\"Mar\",\"1128\":\"Mie\",\"1129\":\"Jue\",\"1130\":\"Vie\",\"1131\":\"Sab\",\"1132\":\"Dom\",\"1133\":\"Reenvío a middleware\",\"1134\":\"Menú principal\",\"1135\":\"Tendencia del día\",\"1136\":\"Tendencia semanal\",\"1137\":\"Verificadas\",\"1138\":\"Gráfica de tendencia\",\"1139\":\"Bloquear prueba\",\"1140\":\"Prueba bloqueada\",\"1141\":\"Búsqueda de pacientes\",\"1142\":\"¡Ha excedido el límite de numeración configurado para la orden!\",\"1143\":\"La order '@@@@' ha sido anulada!\",\"1144\":\"Prueba en retoma\",\"1145\":\"Resultado validado\",\"1146\":\"Necesita crear motivos de tipo retoma de la muestra para poder realizar la acción\",\"1147\":\"Solicitud de rellamado\",\"1148\":\"Solicitudes de retoma\",\"1149\":\"Las muestras de la orden no han sido verificadas\",\"1150\":\"Anulada\",\"1151\":\"Activa\",\"1152\":\"Ciudad\",\"1153\":\"Fax\",\"1154\":\"ID licencia médico\",\"1155\":\"ID NPI\",\"1156\":\"Identificación Entidad\",\"1157\":\"Código postal\",\"1158\":\"Departamento\",\"1159\":\"Codificado\",\"1160\":\"Formato\",\"1161\":\"Obligatorio\",\"1162\":\"Agregar ítems en ingreso de órdenes\",\"1163\":\"Estadísticas\",\"1164\":\"Cargar histórico en Ingreso de órdenes\",\"1165\":\"Ítem por defecto\",\"1166\":\"Validación parcial\",\"1167\":\"Factor conversión\",\"1168\":\"Unidad internacional\",\"1169\":\"Unidad de tiempo\",\"1170\":\"Rango final\",\"1171\":\"Rango inicial\",\"1172\":\"Aplica para\",\"1173\":\"Almacenamiento especial\",\"1174\":\"Días de almacenamiento\",\"1175\":\"Código 128\",\"1176\":\"Tiempo de calidad\",\"1177\":\"Alarma tiempo de calidad\",\"1178\":\"Información del manejo de la muestra\",\"1179\":\"Rango mínimo de años\",\"1180\":\"Rango máximo de años\",\"1181\":\"Título estadístico\",\"1182\":\"Imprimir en informes\",\"1183\":\"Titulo de grupo\",\"1184\":\"Procesamiento por\",\"1185\":\"Multiplicar por\",\"1186\":\"Eliminar de perfil\",\"1187\":\"Imprimir gráfica de seguimiento\",\"1188\":\"Actividades básicas\",\"1189\":\"Días máximos para modificar después de validado\",\"1190\":\"Días de entrega\",\"1191\":\"Días máximos para modificar después de impreso\",\"1192\":\"Vigencia de resultados\",\"1193\":\"Fórmula\",\"1194\":\"Concurrencia\",\"1195\":\"Factor de conversión\",\"1196\":\"Resultado en ingreso\",\"1197\":\"Comentario al imprimir la prueba\",\"1198\":\"Comentario de información general\",\"1199\":\"Tipo prueba\",\"1200\":\"Contacto\",\"1201\":\"Ruta de exportación\",\"1202\":\"Rutina\",\"1203\":\"Urgencia\",\"1204\":\"Ítem demográfico\",\"1205\":\"Excluyente\",\"1206\":\"letra\",\"1207\":\"Suma\",\"1208\":\"Número de días\",\"1209\":\"Delta máximo %\",\"1210\":\"Delta mínimo %\",\"1211\":\"Ruta\",\"1212\":\"Crítico\",\"1213\":\"Pánico mínimo\",\"1214\":\"Pánico máximo\",\"1215\":\"Reportable mínimo\",\"1216\":\"Reportable máximo\",\"1217\":\"Resultado pánico\",\"1218\":\"Aplicar reglas de supresión\",\"1219\":\"Método\",\"1220\":\"Operador\",\"1221\":\"Valor mínimo\",\"1222\":\"Valor máximo\",\"1223\":\"Reportar Tareas\",\"1224\":\"Medio de cultivo\",\"1225\":\"Repetir código\",\"1226\":\"Código central\",\"1227\":\"Cliente particular\",\"1228\":\"Conectividad EMR\",\"1229\":\"Envío de resultados por fax automático\",\"1230\":\"Envío de resultados por correo automático\",\"1231\":\"Impresión de resultados\",\"1232\":\"Código EPS\",\"1233\":\"Nombre impreso\",\"1234\":\"Estado o departamento\",\"1235\":\"Colonia o barrio\",\"1236\":\"Monto máximo per cápita\",\"1237\":\"Monto actual\",\"1238\":\"Monto alarma\",\"1239\":\"Verificar pago\",\"1240\":\"Ver precios en ingreso de órdenes\",\"1241\":\"Página web\",\"1242\":\"Tipo de pagador\",\"1243\":\"Verificar relación diagnóstico\",\"1244\":\"Asignar todas las cuentas\",\"1245\":\"Aplicar diagnóstico\",\"1246\":\"Aplicar homebound\",\"1247\":\"Aplicar venipunture\",\"1248\":\"Correo pagador\",\"1249\":\"Código de reclamación\",\"1250\":\"ID intercambio (ISA06)\",\"1251\":\"Tipo de reclamación\",\"1252\":\"Código de envío (GS02)\",\"1253\":\"Formato miembro\",\"1254\":\"ID calificador (ISA07)\",\"1255\":\"Tipo de Transacción\",\"1256\":\"ID laboratorio (NM109)\",\"1257\":\"Identificación del pagador\",\"1258\":\"Archivo de salida\",\"1259\":\"Consecutivo\",\"1260\":\"Aplicar elegibilidad\",\"1261\":\"Aplicar a tipo de pagador\",\"1262\":\"Asignar beneficios\",\"1263\":\"Firma del proveedor en archivo\",\"1264\":\"Reclamación por\",\"1265\":\"Resolución de la DIAN\",\"1266\":\"Desde\",\"1267\":\"Hasta\",\"1268\":\"Prefijo\",\"1269\":\"Número inicial\",\"1270\":\"Código receptor de la aplicación (GS03)\",\"1271\":\"ID receptor (NM109)\",\"1272\":\"Receptor de intercambio (ISA09)\",\"1273\":\"Aplicar automáticamente\",\"1274\":\"Vigencia activa\",\"1275\":\"Administrador\",\"1276\":\"Ítems demográficos\",\"1277\":\"Edición de pruebas en bloque\",\"1278\":\"Prueba por laboratorio\",\"1279\":\"Pruebas por demográfico. PyP\",\"1280\":\"Integración\",\"1281\":\"Integración de middleware por laboratorio\",\"1282\":\"Información de reenvío de middleware\",\"1283\":\"Facturación\",\"1284\":\"Tipos de pago\",\"1285\":\"Almacenamiento de muestras\",\"1286\":\"Ordenes de laboratorio\",\"1287\":\"Confirmar\",\"1288\":\"Activación usuario\",\"1289\":\"Expiración usuario\",\"1290\":\"Expiración contraseña\",\"1291\":\"Código de firma\",\"1292\":\"Descuento %\",\"1293\":\"Tipo de usuario\",\"1294\":\"Tipo de orden por defecto\",\"1295\":\"Roles\",\"1296\":\"Cantidad de dígitos para la orden\",\"1297\":\"Número de orden automático\",\"1298\":\"Tipo de numeración de orden\",\"1299\":\"Días máximos para editar una orden\",\"1300\":\"Permite anular órdenes validadas\",\"1301\":\"Agregar pruebas con muestras verificadas\",\"1302\":\"Alarma de la última orden del paciente\",\"1303\":\"Definición de la historia genérica-Nombre\",\"1304\":\"Definición de la historia genérica-Apellido\",\"1305\":\"Máscara para la historia clínica\",\"1306\":\"Captura de talla\",\"1307\":\"Captura de peso\",\"1308\":\"Contraseña del paciente\",\"1309\":\"Visualizar foto\",\"1310\":\"Separador de muestras\",\"1311\":\"Imprimir etiqueta automáticamente\",\"1312\":\"Ver preliminar del talón\",\"1313\":\"Dígitos del año en código de barras\",\"1314\":\"Comentario para resultados restringidos\",\"1315\":\"Comentario para resultados pendientes\",\"1316\":\"Actualización widget (min)\",\"1317\":\"Motivo de modificación del resultado\",\"1318\":\"Servidor node de impresión\",\"1319\":\"Ver preliminar completo en registro de resultados\",\"1320\":\"Entrega de informes por sede\",\"1321\":\"Generar nombre de PDF con historia clínica\",\"1322\":\"Identificar copias del informe de resultados\",\"1323\":\"Imprimir adjuntos\",\"1324\":\"Imprimir adjuntos en informe preliminar\",\"1325\":\"Tipo de impresión\",\"1326\":\"Resolver impresora por\",\"1327\":\"Imprimir informe final\",\"1328\":\"Protocolo\",\"1329\":\"Puerto\",\"1330\":\"Tipo manejo de seguridad\",\"1331\":\"Correo origen\",\"1332\":\"Configuración de correo electrónico-Contraseña\",\"1333\":\"Enviar informe de resultados por correo a\",\"1334\":\"Correo del médico - asunto\",\"1335\":\"Cuerpo del correo\",\"1336\":\"Mostrar indicador de re-llamado en grilla de resultados\",\"1337\":\"Ordenamiento en registro de resultados\",\"1338\":\"Entrevista obligatoria para pánico\",\"1339\":\"Restar impuesto\",\"1340\":\"Indica si se usan centavos\",\"1341\":\"Impuesto %\",\"1342\":\"Moneda\",\"1343\":\"Símbolo monetario\",\"1344\":\"Tipo de trazabilidad\",\"1345\":\"Destino que verifica almacenamiento de muestras\",\"1346\":\"Destino que verifica desecho de muestras\",\"1347\":\"Color Gradilla general\",\"1348\":\"Color Gradilla de pruebas pendientes de resultados\",\"1349\":\"Color Gradilla de pruebas confidenciales\",\"1350\":\"Estado gradilla de pruebas pendientes de resultados\",\"1351\":\"Estado gradilla de pruebas confidenciales\",\"1352\":\"Horario de atención (24 h) desde\",\"1353\":\"Horario de atención (24 h) hasta\",\"1354\":\"Intervalo entre citas\",\"1355\":\"Cantidad de citas por intervalo\",\"1356\":\"LDPA Servidor\",\"1357\":\"LDPA Usuario\",\"1358\":\"LDPA Contraseña\",\"1359\":\"LDPA Grupo LIS\",\"1360\":\"Ingreso de entrevista\",\"1361\":\"Re-llamado Rango inicial\",\"1362\":\"Re-llamado Rango final\",\"1363\":\"Re-llamado Separador de listas de archivos CSV\",\"1364\":\"Re-llamado Días de cambio de clave\",\"1365\":\"Destino que verifica microbiología\",\"1366\":\"Registro de interpretación CMI-M y disco en antibiograma\",\"1367\":\"Plano whonet THM\",\"1368\":\"Plano whonet APB\",\"1369\":\"Plano whonet EDTA\",\"1370\":\"Plano whonet Código SPEC\",\"1371\":\"Activar Dashboard\",\"1372\":\"URL Dashboard\",\"1373\":\"Activar SIGA\",\"1374\":\"Sede SIGA\",\"1375\":\"Servicio ingreso de órdenes SIGA\",\"1376\":\"Servicio ruta de la muestra SIGA\",\"1377\":\"Gestión de eventos activar eventos\",\"1378\":\"Gestión de eventos URL\",\"1379\":\"Gestión de eventos de la orden Crear\",\"1380\":\"Gestión de eventos de la orden Modificar\",\"1381\":\"Gestión de eventos de la orden Anular\",\"1382\":\"Gestión de eventos de la orden Validar\",\"1383\":\"Gestión de eventos de la muestra Hacer toma\",\"1384\":\"Gestión de eventos de la muestra Verificar\",\"1385\":\"Gestión de eventos de resultados Ingresar\",\"1386\":\"Gestión de eventos de resultados Modificar\",\"1387\":\"Gestión de eventos de resultados Repetir\",\"1388\":\"Gestión de eventos de pruebas Validar\",\"1389\":\"Gestión de eventos de pruebas Quitar validación\",\"1390\":\"Gestión de eventos de pruebas Imprimir\",\"1391\":\"Abreviatura de la entidad\",\"1392\":\"Máscara de teléfono\",\"1393\":\"Formato de fecha\",\"1394\":\"Indica si se usan servicios\",\"1395\":\"Indica si se usan médicos\",\"1396\":\"Indica si se usan razas\",\"1397\":\"Indica si se usa tipo de documento\",\"1398\":\"Historia automática\",\"1399\":\"Items tipo de entrevista\",\"1400\":\"Control\",\"1401\":\"Campo\",\"1402\":\"Nombre del agrupamiento\",\"1403\":\"Agrupación de órdenes\",\"1404\":\"Agrupaciones\",\"1405\":\"Lista de selección única\",\"1406\":\"Tipo de respuesta\",\"1407\":\"Cantidad de etiquetas\",\"1408\":\"Volumen mínimo requerido\",\"1409\":\"Bloqueo por días máximos para modificar después de validado\",\"1410\":\"Bloqueo por días máximos para modificar después de impreso\",\"1411\":\"Ninguna prueba puede ser validada\",\"1412\":\"Impresión por servicio\",\"1413\":\"Edición de pruebas en lista\",\"1414\":\"Excluir pruebas por\",\"1415\":\"Reenvío al middleware\",\"1416\":\"Edición de Reportes\",\"1417\":\"Edición de código de barras\",\"1418\":\"Módulos con permisos\",\"1419\":\"Prueba final\",\"1420\":\"Municipio o ciudad\",\"1421\":\"Envió de resultados\",\"1422\":\"Tipo de cuenta\",\"1423\":\"Institucional\",\"1424\":\"Persona\",\"1425\":\"Entre\",\"1426\":\"No Entre\",\"1427\":\"Reglas\",\"1428\":\"Plantilla\",\"1429\":\"Días para la modificación de la contraseña\",\"1430\":\"Cierre de sesión por inactividad (minutos)\",\"1431\":\"Tiempo de expiración de la sesión (minutos)\",\"1432\":\"Demográfico para excluir pruebas\",\"1433\":\"Demográfico PyP\",\"1434\":\"Demográfico para título del informe\",\"1435\":\"Demográfico que valida inconsistencias\",\"1436\":\"Demográfico de histograma\",\"1437\":\"Item demográfico de histograma\",\"1438\":\"Gestión de muestras\",\"1439\":\"Sin permisos para el módulo\",\"1440\":\"Resultado con validación preliminar\",\"1441\":\"Consulta de repeticiones\",\"1442\":\"No se ha encontrado la numeración de la orden\",\"1443\":\"No se ha encontrado la numeración de la orden de rellamado\",\"1444\":\"El resultado no se encuentra dentro del rango de reportables\",\"1445\":\"No tiene licencia para el producto\",\"1446\":\"No tiene licencia para Ingresar a los módulos del sistema\",\"1447\":\"No tiene permisos para esta sede\",\"1448\":\"El Limite de usuarios conectados a llegado a su limite\",\"1449\":\"No hay conexión con la llave de seguridad\",\"1450\":\"Se requiere tipo de documento\",\"1451\":\"Paciente creado con tipo de documento.\",\"1452\":\"Paciente creado sin tipo de documento.\",\"1453\":\"Paciente creado con historia automática.\",\"1454\":\"Existen más pacientes con el mismo número de historia.\",\"1455\":\"La historia @@@@ ha sido desbloqueada satisfactoriamente!\",\"1456\":\"La orden fue ingresada en una sede diferente\",\"1457\":\"La licencia registrada ha expirado\",\"1458\":\"Débil\",\"1459\":\"Medio\",\"1460\":\"Buena\",\"1461\":\"Segura\",\"1462\":\"Muy segura\",\"1463\":\"Por su seguridad es necesario establecer una nueva contraseña, para cumplir con nuestra política de contraseña segura, debe utilizar una contraseña suficientemente segura\",\"1464\":\"Esta contraseña ya ha sido usada por favor cambiar contraseña\",\"1465\":\"La contraseña ingresada no debe ser igual a contraseñas usadas anteriormente\",\"1466\":\"La contraseña debe tener mínimo\",\"1467\":\"caracteres\",\"1468\":\"La contraseña debe contener mayúsculas\",\"1469\":\"La contraseña debe contener minúsculas\",\"1470\":\"La contraseña debe contener números\",\"1471\":\"La contraseña debe contener caracteres especiales\",\"1472\":\"La contraseña no es igual\",\"1473\":\"Ha excedido el número máximo de intentos de inicio de sesión, el usuario se ha desactivado\",\"1474\":\"Usuario inactivo\",\"1475\":\"No existen datos de trazabilidad\",\"1476\":\"Ver en informe final\",\"1477\":\"Fecha inválida\",\"1478\":\"Use un formato de fecha válido\",\"1479\":\"Elija una fecha después de \",\"1480\":\"Elija una fecha antes de \",\"1481\":\"Filtro de muestras\",\"1482\":\"Pendientes de verificación\",\"1483\":\"Ir almacén de muestra\",\"1484\":\"Ir a verificación de muestra\",\"1485\":\"No se ha realizado entrevista al paciente\",\"1486\":\"Cambiar prueba\",\"0000\":\"esCo\",\"0001\":\"Usuario\",\"0002\":\"Contraseña\",\"0003\":\"Sede\",\"0004\":\"Iniciar sesión\",\"0005\":\" Nuevo\",\"0006\":\" Editar\",\"0007\":\" Guardar\",\"0008\":\" Cancelar\",\"0009\":\" Reporte\",\"0010\":\"Órdenes de laboratorio\",\"0011\":\"Ingreso de órdenes\",\"0012\":\"Citas\",\"0013\":\"Pruebas\",\"0014\":\"Entrada de muestras\",\"0015\":\"Listados\",\"0016\":\"Asignación de historias\",\"0017\":\"Historias clínicas\",\"0018\":\"Hojas de trabajo\",\"0019\":\"Registro de resultados\",\"0020\":\"Registro de resultados por demanda\",\"0021\":\"Registro de resultados por lote\",\"0022\":\"Validación de resultados\",\"0023\":\"Revisión de resultados\",\"0024\":\"Verificación de microbiología\",\"0025\":\"Siembra de microbiología\",\"0026\":\"Lectura de microbiología\",\"0027\":\"Resultados\",\"0028\":\"Informes y consultas\",\"0029\":\"Estadísticas\",\"0030\":\"Utilidades\",\"0031\":\"Informes\",\"0032\":\"Control entrega de informes\",\"0033\":\"Consultas\",\"0034\":\"Estadísticas especiales\",\"0035\":\"Estadísticas con precios\",\"0036\":\"Alerta temprana\",\"0037\":\"Indicadores\",\"0038\":\"Histograma\",\"0039\":\"Plano whonet\",\"0040\":\"Listado de microbiología\",\"0041\":\"Muestras en destinos\",\"0042\":\"Reporte estadística y facturación\",\"0043\":\"Almacén de muestras\",\"0044\":\"Reasignación  de historias\",\"0045\":\"Activación de órdenes\",\"0046\":\"Trazabilidad\",\"0047\":\"Visor de sucesos\",\"0048\":\"Kardex\",\"0049\":\"Rips\",\"0050\":\"Borrados especiales\",\"0051\":\"Inconsistencias\",\"0052\":\"Prueba en analizador\",\"0053\":\"Consulta web\",\"0054\":\"Notas\",\"0055\":\"Desbloqueo de historia/orden\",\"0056\":\"Liberación de sesiones\",\"0057\":\"Prueba de impresión\",\"0058\":\"Log ingreso\",\"0059\":\"Acceso directo\",\"0060\":\"Requerido\",\"0061\":\"Número de orden\",\"0062\":\"Fecha de muestra\",\"0063\":\"Sin accesos directos\",\"0064\":\"Alarmas\",\"0065\":\"Sin alarmas\",\"0066\":\"Cerrar sesión\",\"0067\":\"Usuario inválido\",\"0068\":\"Contraseña incorrecta\",\"0069\":\"La contraseña ha expirado\",\"0070\":\"Muestras rechazadas\",\"0071\":\"Filtrar por rangos\",\"0072\":\"Seleccionar año\",\"0073\":\"Orden inicial\",\"0074\":\"Orden final\",\"0075\":\"Fecha inicial\",\"0076\":\"Fecha final\",\"0077\":\"Filtrar por áreas o pruebas\",\"0078\":\"General\",\"0079\":\"Áreas\",\"0080\":\"Pruebas y perfiles\",\"0081\":\"Pruebas confidenciales\",\"0082\":\"Filtrar por demográficos\",\"0083\":\"Demográficos\",\"0084\":\"Filtrar por otros demográficos\",\"0085\":\"Cliente\",\"0086\":\"Médico\",\"0087\":\"Tarifa\",\"0088\":\"Tipo de orden\",\"0089\":\"Seleccionar sede\",\"0090\":\"Servicio\",\"0091\":\"Raza\",\"0092\":\"Descripción de paquetes\",\"0093\":\"Listado normal\",\"0094\":\"Listado por áreas\",\"0095\":\"Listado por pruebas\",\"0096\":\"Listado sin agrupar\",\"0097\":\"Listado de órdenes\",\"0098\":\"Código\",\"0099\":\"Laboratorios externos\",\"0100\":\"Total órdenes\",\"0101\":\"Total pruebas\",\"0102\":\"Edad\",\"0103\":\"Años\",\"0106\":\"Español\",\"0107\":\"Inglés\",\"0108\":\"Inicio de Sesión\",\"0109\":\"Versión\",\"0110\":\"Orden\",\"0111\":\"Muestra\",\"0112\":\"Rechazo\",\"0113\":\"Motivo\",\"0114\":\"Comentario\",\"0115\":\"Necesita crear motivos de tipo rechazo de la muestra para poder realizar la acción\",\"0116\":\"Verificación de muestra\",\"0117\":\"Historia\",\"0118\":\"Nombre\",\"0119\":\"Fecha de verificación\",\"0120\":\"Fecha nacimiento\",\"0121\":\"Rechazo de la muestra\",\"0122\":\"Fecha de rechazo\",\"0123\":\"Motivos de rechazo\",\"0124\":\"Género\",\"0125\":\"Unidad\",\"0126\":\"Registro general\",\"0127\":\"Sin verificar\",\"0128\":\"Rechazada\",\"0129\":\"Listado de pacientes por área\",\"0130\":\"Información general\",\"0131\":\"Permisos\",\"0132\":\"Identificación\",\"0133\":\"Nombre(s)\",\"0134\":\"Apellido(s)\",\"0135\":\"Correo\",\"0136\":\"Contraseña actual\",\"0137\":\"Contraseña incorrecta\",\"0138\":\"Nueva contraseña\",\"0139\":\"Las contraseñas deben coincidir\",\"0140\":\"Repetir contraseña\",\"0141\":\"Confidenciales\",\"0142\":\"Impresión de informes\",\"0143\":\"Adicionar y quitar exámenes en registro de resultados\",\"0144\":\"Segunda validación\",\"0145\":\"Editar información de pacientes en ingreso de órdenes\",\"0146\":\"Creación de demográficos en ingreso de órdenes\",\"0147\":\"Imprimir resultados en ingreso de órdenes\",\"0148\":\"Deshacer validación\",\"0149\":\"Datos guardados con éxito\",\"0150\":\"Perfil de usuario\",\"0151\":\"Impresión de códigos de barras\",\"0152\":\"No existen datos para generar el reporte.\",\"0153\":\"Seleccionar\",\"0154\":\"Buscar\",\"0155\":\"Solo se permiten 6 accesos directos por usuario\",\"0156\":\"Mensaje\",\"0157\":\"Destino\",\"0158\":\" Imprimir\",\"0159\":\"Imprimir etiqueta adicional\",\"0160\":\"Muestras en re-llamado\",\"0161\":\"Tipo de reporte\",\"0162\":\"No aplica\",\"0163\":\"Filtrar por laboratorios\",\"0164\":\"Aplazamiento\",\"0165\":\"Toma de muestra\",\"0166\":\"Aplazamiento de la muestra\",\"0167\":\"Fecha del aplazamiento\",\"0168\":\"Motivo del aplazamiento\",\"0169\":\"Retoma de la muestra\",\"0170\":\"Fecha de la retoma\",\"0171\":\"Motivo de la retoma\",\"0172\":\"Digite la muestra\",\"0173\":\"¡La muestra ha sido aplazada con anterioridad!\",\"0174\":\"¡La muestra ha sido retoma con anterioridad!'\",\"0175\":\"¡La muestra ha sido tomada!\",\"0176\":\"¡La muestra ha sido verificada con anterioridad!\",\"0177\":\"¡La muestra ha sido rechazada con anterioridad!\",\"0178\":\"¡La muestra ha sido tomada con anterioridad!\",\"0179\":\"¡La orden no existe!\",\"0180\":\"¡La muestra no se encuentra relacionada a esa orden!\",\"0181\":\"¡La muestra a sido retomada!\",\"0182\":\"Fecha de toma\",\"0183\":\"¡La muestra ha sido verificada!\",\"0184\":\"¡La muestra ha sido rechazada!\",\"0185\":\"Listado por muestras\",\"0186\":\"Listado por recipientes\",\"0187\":\"Dirección\",\"0188\":\"Teléfono\",\"0189\":\"Fecha de ingreso\",\"0190\":\"Origen\",\"0191\":\"Entrevista\",\"0192\":\"¡No existen entrevistas configuradas para la orden!\",\"0193\":\"¡La muestra no ha sido tomada!\",\"0194\":\"Cantidad\",\"0195\":\"Etiqueta adicional\",\"0196\":\"No se encuentra la impresora\",\"0197\":\"Etiquetas han sido impresas\",\"0198\":\"Abreviatura\",\"0199\":\"Tiempo esperado\",\"0200\":\"Tiempo total (min)\",\"0201\":\"Muestras retrazadas\",\"0202\":\"¡La muestra no tiene configurado el destino seleccionado!\",\"0203\":\"¡La muestra ya completo la ruta!\",\"0204\":\"La muestra debe ser verificada en el destino \",\"0205\":\"Muestra\",\"0206\":\"Manejo de la muestra\",\"0207\":\"Ingreso\",\"0208\":\"Verificación\",\"0209\":\"Retoma\",\"0210\":\"Pendiente de verificación\",\"0211\":\"Código de barras\",\"0212\":\"Seleccionar hoja de trabajo\",\"0213\":\"Anterior\",\"0214\":\"Reiniciar secuencia\",\"0215\":\"Todas\",\"0216\":\"Pendientes de resultado\",\"0217\":\"Generar\",\"0218\":\"Nueva\",\"0219\":\"Sí\",\"0220\":\"No\",\"0221\":\"¿Está seguro de reiniciar la secuencia de la hoja de trabajo seleccionada?\",\"0222\":\"Vertical\",\"0223\":\"Horizontal\",\"0224\":\"Orientación\",\"0225\":\"Microbiología\",\"0226\":\"Secuencia\",\"0227\":\"Volumen\",\"0228\":\"Volumen total\",\"0229\":\"La muestra debe ser verificada en el destino \",\"0230\":\"Nuevo paciente\",\"0231\":\"El paciente no existe\",\"0232\":\"Las siguientes pruebas del paciente no cumplen con las condiciones de la orden\",\"0233\":\"Tipo documento\",\"0234\":\"Primer apellido\",\"0235\":\"Segundo apellido\",\"0236\":\"Primer nombre\",\"0237\":\"Segundo nombre\",\"0238\":\"Peso\",\"0239\":\"Talla\",\"0240\":\"Fecha de ingreso\",\"0241\":\"Servicios\",\"0242\":\"Desbloquear por\",\"0243\":\"Documento\",\"0244\":\"Número de orden no encontrado\",\"0245\":\"Número de documento no encontrado\",\"0246\":\"Aceptar\",\"0247\":\"Ruta de la muestra\",\"0248\":\"La orden @@@@ ha sido desbloqueada satisfactoriamente!\",\"0249\":\"La historia con documento @@@@ ha sido desbloqueada satisfactoriamente!\",\"0250\":\"La orden @@@@ no se encuentra bloqueada\",\"0251\":\"La historia con documento @@@@ no se encuentra bloqueada\",\"0252\":\"Creación de órdenes sin historia\",\"0253\":\"Seleccione una prueba\",\"0254\":\"La prueba @@@@ ya se encuentra en la lista\",\"0255\":\"La prueba digitada no existe, está inactiva o no está apta para el ingreso de órdenes\",\"0256\":\"Total órdenes ingresadas\",\"0257\":\"Precio\",\"0258\":\"Diagnóstico\",\"0259\":\"Rango\",\"0260\":\"Listado de órdenes inactivas\",\"0261\":\"Informe generado por\",\"0262\":\"Página\",\"0263\":\"de\",\"0264\":\"Usuario que anulo\",\"0265\":\"Fecha de anulación\",\"0266\":\"Negrita\",\"0267\":\"Cursiva\",\"0268\":\"Subrayado\",\"0269\":\"Tachado\",\"0270\":\"Subíndice\",\"0271\":\"Superíndice\",\"0272\":\"Eliminar formato\",\"0273\":\"Numeración\",\"0274\":\"Viñeta\",\"0275\":\"Outdent\",\"0276\":\"Sangría\",\"0277\":\"Justificación a la izquierda\",\"0278\":\"Justificación del centro\",\"0279\":\"Justificar a la derecha\",\"0280\":\"Mis órdenes ingresadas\",\"0281\":\"Cita\",\"0282\":\"Párrafo\",\"0283\":\"Color de fuente\",\"0284\":\"Resaltar color\",\"0285\":\"Tamaño de fuente\",\"0286\":\"Fuente\",\"0287\":\"Cantidad de órdenes automáticas\",\"0288\":\"Cantidad de órdenes manuales\",\"0289\":\"Resultado\",\"0290\":\"Comentario codificado\",\"0291\":\"¿Está seguro de afectar el resultado de la prueba '@@@' para las órdenes que pertenecen a ese rango?\",\"0292\":\"Órdenes han sido afectadas.\",\"0293\":\"Insertar al inicio del comentario\",\"0294\":\"Se ingresaron ### órdenes desde la '@@@1' hasta la '@@@2'.\",\"0295\":\"Demográfico\",\"0296\":\"Resolver inconsistencia\",\"0297\":\"HIS\",\"0298\":\"LIS\",\"0299\":\"¿Está seguro que desea elegir los datos del\",\"0300\":\"para resolver la inconsistencia?\",\"0301\":\"Inconsistencias\",\"0302\":\"La inconsistencia se ha resuelto\",\"0303\":\"No se encuentran inconsistencias en este rango de fechas\",\"0304\":\"Las pruebas de las siguientes órdenes no se pueden anular:\",\"0305\":\"Listado de datos borrados\",\"0306\":\"Fecha de borrado\",\"0307\":\"Motivo del borrado\",\"0308\":\"Pruebas validadas\",\"0309\":\"Necesita crear motivos para poder realizar la acción\",\"0310\":\"Impresión\",\"0311\":\"Pendientes de impresión\",\"0312\":\"Validación\",\"0313\":\"Pendientes de toma de muestra\",\"0314\":\"Pendientes de validación\",\"0315\":\"Con comentario\",\"0316\":\"Sin comentario\",\"0317\":\"Pendientes\",\"0318\":\"Gestión\",\"0319\":\"Tipo de reporte\",\"0320\":\"Resumido\",\"0321\":\"Detallado\",\"0322\":\"Consulta de pacientes\",\"0323\":\"Patológico\",\"0324\":\"Las órdenes entre la número '@@@1' y la '@@@2' ya se encuentran registradas en el sistema.\",\"0325\":\"Fecha\",\"0326\":\"30 Días\",\"0327\":\"60 Días\",\"0328\":\"60 Ó más días\",\"0329\":\"Cerrar\",\"0330\":\"Mensaje de órdenes\",\"0331\":\"¡Existen pruebas sin laboratorio asignado!\",\"0332\":\"Descripción\",\"0333\":\"Primera advertencia\",\"0334\":\"Está seguro de borrar\",\"0335\":\"Órdenes que se encuentran entre las órdenes\",\"0336\":\"al\",\"0337\":\"Esta seguro de borrar 1 orden que se encuentra entre las órdenes\",\"0338\":\"Esta seguro de borrar la orden\",\"0339\":\"Esta seguro de borrar los resultados de las pruebas de \",\"0340\":\"órdenes que se encuentran entre las órdenes\",\"0341\":\"Esta seguro de borrar los resultados de la pruebas de la orden que se encuentra entre las órdenes\",\"0342\":\"Esta seguro de borrar los resultados de la prueba de la orden\",\"0343\":\"Esta seguro de borrar las pruebas de\",\"0344\":\"órdenes que se encuentran entre las órdenes\",\"0345\":\"Esta seguro de borrar las pruebas de la orden que se encuentra entre las órdenes\",\"0346\":\"Esta seguro de borrar las prueba de la orden\",\"0347\":\"Segunda advertencia\",\"0348\":\"No se encuentran órdenes para desactivar en este rango\",\"0349\":\"Las órdenes se han anulado exitosamente\",\"0350\":\"Los resultados se han anulado exitosamente\",\"0351\":\"Las pruebas se han anulado exitosamente\",\"0352\":\"Listado de pacientes\",\"0353\":\"Todos\",\"0354\":\"Modificado\",\"0355\":\"Repetición\",\"0356\":\"Pánico\",\"0357\":\"Delta Check\",\"0358\":\"Impresos\",\"0359\":\"Validados\",\"0360\":\"Órdenes\",\"0361\":\"Gestión resumida\",\"0362\":\"Femenino\",\"0363\":\"Masculino\",\"0364\":\"Estado\",\"0365\":\"Verificados\",\"0366\":\"Con resultado\",\"0367\":\"Pruebas entregadas\",\"0368\":\"Pruebas pendientes de entrega\",\"0369\":\"Usuario que entregó\",\"0370\":\"Persona que recibe\",\"0371\":\"Fecha de entrega\",\"0372\":\"Ninguno\",\"0373\":\"Ningun item seleccionado\",\"0374\":\"Gestión detallada\",\"0375\":\"Reimpresión\",\"0376\":\"Adjuntos\",\"0377\":\"Copias\",\"0378\":\"Ordenamiento de impresión por\",\"0379\":\"Impreso\",\"0380\":\"E-mail\",\"0381\":\"Finales\",\"0382\":\"Previos\",\"0383\":\"Informe de órdenes impresas\",\"0384\":\"Envío de correo a\",\"0385\":\"Adicionar ítem\",\"0386\":\"Informes entregados\",\"0387\":\"Filtrar por\",\"0388\":\"En los últimos\",\"0389\":\"Recibe resultados\",\"0390\":\"Informes pendientes de entrega\",\"0391\":\"Saldo\",\"0392\":\"No se encontraron registros\",\"0393\":\"Entregar\",\"0394\":\"Valores de referencia\",\"0395\":\"Informe preliminar\",\"0396\":\"Fecha y hora de ingreso\",\"0397\":\"Fecha de impresión\",\"0398\":\"Paciente\",\"0399\":\"Informe final\",\"0400\":\"Histórico\",\"0401\":\"Indefinido\",\"0402\":\"Ambos\",\"0403\":\"Muestras vencidas\",\"0404\":\"Muestras con retoma\",\"0405\":\"Resultado anterior\",\"0406\":\"Consulta normal\",\"0407\":\"Historia por prueba\",\"0408\":\"Área\",\"0409\":\"Vlr. referencia\",\"0410\":\"Consulta del día\",\"0411\":\"Descargar archivos\",\"0412\":\"Item demográfico\",\"0413\":\"Pendiente\",\"0414\":\"Antibiograma\",\"0415\":\"Gráfica\",\"0416\":\"Validado\",\"0417\":\"Fecha recepción\",\"0418\":\"Fecha validación\",\"0419\":\"Órdenes impresas\",\"0420\":\"Norma mínima\",\"0421\":\"Norma máxima\",\"0422\":\"Imprimir histórico\",\"0423\":\"Imprimir gráfica\",\"0424\":\"Búsqueda\",\"0425\":\"Quitar\",\"0426\":\"Sexo\",\"0427\":\"Filtro de demográficos\",\"0428\":\"Año\",\"0429\":\"Laboratorio\",\"0430\":\"Buscar por\",\"0431\":\"Estado de la prueba\",\"0432\":\"Condensado\",\"0433\":\"Detallado sin agrupar\",\"0434\":\"Fecha de validación\",\"0435\":\"Ordenadas\",\"0436\":\"Validadas\",\"0437\":\"Impresas\",\"0438\":\"Hombre\",\"0439\":\"Mujer\",\"0440\":\"Primer nivel de agrupación\",\"0441\":\"Estadísticas por\",\"0442\":\"Nivel\",\"0443\":\"Subtotal\",\"0444\":\"Total órdenes grupo\",\"0445\":\"Promedio Pruebas/Órdenes\",\"0446\":\"Total final\",\"0447\":\"Ene\",\"0448\":\"Abr\",\"0449\":\"Ago\",\"0450\":\"Dic\",\"0451\":\"Muestras\",\"0452\":\"Segundo nivel de agrupación\",\"0453\":\"Tercer nivel de agrupación\",\"0454\":\"Cuarto nivel de agrupación\",\"0455\":\"Precio paciente\",\"0456\":\"Precio empresa\",\"0457\":\"Total pruebas grupo\",\"0458\":\"Total historias\",\"0459\":\"Prueba\",\"0460\":\"Submuestra\",\"0461\":\"Método de recolección\",\"0462\":\"Sitio anatómico\",\"0463\":\"El valor ya existe\",\"0464\":\"Diagnóstico permanente\",\"0465\":\"Comentario de la orden\",\"0466\":\"Comentario de microbiología\",\"0467\":\"Verificar\",\"0468\":\"Motivos repetición\",\"0469\":\"Agente etiológico\",\"0470\":\"Impuesto\",\"0471\":\"Abono\",\"0472\":\"Saldo\",\"0473\":\"Mensual\",\"0474\":\"Informe de órdenes en PDF\",\"0475\":\"Informe de envío de correos\",\"0476\":\"Días\",\"0477\":\"Horas\",\"0478\":\"Minutos\",\"0479\":\"Segundos\",\"0480\":\"¡Alerta! La calidad de la muestra venció hace\",\"0481\":\"¡Precaución! la calidad de la muestra vence en\",\"0482\":\"La muestra no tiene una ruta configurada\",\"0483\":\"Seguimiento\",\"0484\":\"PDF con número de historia\",\"0485\":\"PDF con nombre de paciente\",\"0486\":\"Medios de cultivo\",\"0487\":\"Procedimientos\",\"0488\":\"Contenido de la nota\",\"0489\":\"Color nota\",\"0490\":\"Tiempo promedio\",\"0491\":\"Tiempo de oportunidad\",\"0492\":\"Tiempo (Minutos)\",\"0493\":\"Filtro\",\"0494\":\"Por demográficos\",\"0495\":\"Listado\",\"0496\":\"Numérico\",\"0497\":\"Edad mínima\",\"0498\":\"Edad máxima\",\"0499\":\"Referencia mínima\",\"0500\":\"Referencia máxima\",\"0501\":\"El sitio anatómico ya existe\",\"0502\":\"El Código ya existe\",\"0503\":\"El código y el sitio anatómico ya existen\",\"0504\":\"Actualizar\",\"0505\":\"La muestra no se encuentra configurada para microbiología\",\"0506\":\"¡Las pruebas de esa muestra no tienen medios de cultivo o procedimientos asociados!\",\"0507\":\"La muestra se encuentra rechazada\",\"0508\":\"La orden no tiene un paciente asignado\",\"0509\":\"Siembra\",\"0510\":\"La muestra no ha sido verificada en el módulo de verificación de microbiología\",\"0511\":\"La muestra ya se encuentra sembrada\",\"0512\":\"La muestra se ha sembrado con éxito\",\"0513\":\"Grupos etarios\",\"0514\":\"Tiempo máximo\",\"0515\":\"No cumplen\",\"0516\":\"Procentaje de cumplimiento\",\"0517\":\"Minutos por vencer\",\"0518\":\"Minutos vencidos\",\"0519\":\"Fecha de resultado\",\"0520\":\"Diferencia en minutos\",\"0521\":\"Rango de resultados\",\"0522\":\"Próximos a vencerse\",\"0523\":\"Vencidos\",\"0524\":\"Totales\",\"0525\":\"Tiempos de los estados del resultado\",\"0526\":\"Máximo\",\"0527\":\"Línea\",\"0528\":\"Descargar todo\",\"0529\":\"Usuario que validó\",\"0530\":\"Tipos de agrupamiento\",\"0531\":\"Buscar tareas\",\"0532\":\"Exportar\",\"0533\":\"Promedio\",\"0534\":\"Sta. Devi.\",\"0535\":\"Moda\",\"0536\":\"Mediana\",\"0537\":\"Percentil\",\"0538\":\"Clase\",\"0539\":\"Inferior\",\"0540\":\"Superior\",\"0541\":\"Frecuencia\",\"0542\":\"Acumulado\",\"0543\":\"Usuario ingresa\",\"0544\":\"Usuario válida\",\"0545\":\"Usuario verifica\",\"0546\":\"Calcular a partir de\",\"0547\":\"¡No es posible eliminar todos los items de la lista!\",\"0548\":\"Reporte de alerta temprana\",\"0549\":\"Numerador\",\"0550\":\"DI Totales\",\"0551\":\"DI Básicas\",\"0552\":\"Básico\",\"0553\":\"Áreas\",\"0554\":\"Buscar microorganismo\",\"0555\":\"Pre-validación\",\"0556\":\"Editar comentario\",\"0557\":\"Antibiótico\",\"0558\":\"Grupo\",\"0559\":\"CMI\",\"0560\":\"Interpretación\",\"0561\":\"Disco\",\"0562\":\"Sensible\",\"0563\":\"Intermedio\",\"0564\":\"Resistente\",\"0565\":\"Pruebas totales\",\"0566\":\"Pruebas básicas\",\"0567\":\"Mes\",\"0568\":\"Día\",\"0569\":\"Meses\",\"0570\":\"Edad no valida\",\"0571\":\"Sin correo electrónico\",\"0572\":\"Verificada\",\"0573\":\"Total muestras\",\"0574\":\"¡No existen datos en el rango configurado!\",\"0575\":\"Primer destino\",\"0576\":\"Segundo destino\",\"0577\":\"Tercer destino\",\"0578\":\"Tareas\",\"0579\":\"Fecha de modificación\",\"0580\":\"Valor\",\"0581\":\"Valor anterior\",\"0582\":\"Seguimiento de microbiología\",\"0583\":\"Tareas por paciente\",\"0584\":\"Fecha de tarea\",\"0585\":\"Adjuntos de la orden\",\"0586\":\"Adjuntos de la prueba\",\"0587\":\"Formato de archivo incorrecto\",\"0588\":\"Eliminar\",\"0589\":\"Archivos permitidos\",\"0590\":\"¿Está seguro de reiniciar las tareas de las ordenes que están entre el rango @@@@ a @@@@?\",\"0591\":\"Reiniciar tareas\",\"0592\":\"Reporte de tareas\",\"0593\":\"Reportar\",\"0594\":\"Repeticiones\",\"0595\":\"¡No existen datos para esta orden!\",\"0596\":\"¡No existen datos para esta orden y esta muestra!\",\"0597\":\"Observaciones\",\"0598\":\"Pendientes de pre validación\",\"0599\":\"Plantilla de resultados\",\"0600\":\"Filtrar ordenes por\",\"0601\":\"Opciones de informe\",\"0602\":\"Cultivo\",\"0603\":\"Directo\",\"0604\":\"Expandir\",\"0605\":\"Pantalla completa\",\"0606\":\"Cargar más\",\"0607\":\"Estadísticas de microbiología\",\"0608\":\"Microorganismo\",\"0609\":\"Línea de tiempo\",\"0610\":\"Nota adicionada\",\"0611\":\"Nota editada\",\"0612\":\"Nota eliminada\",\"0613\":\"Captura de fotografía\",\"0614\":\"¡Verifique que tenga una cámara conectada y/o que la cámara no esté siendo utilizada por otra aplicación!\",\"0615\":\"Cámara\",\"0616\":\"Foto\",\"0617\":\"Nombre de la imagen\",\"0618\":\"¡El nombre de la imagen ya existe!\",\"0619\":\"Agregar fotografía\",\"0620\":\"Capturar fotografía\",\"0621\":\"¡El nombre del  adjunto ya existe!\",\"0622\":\"¿Está seguro que desea reemplazarlo?\",\"0623\":\"Resultado normal\",\"0624\":\"Borrar\",\"0625\":\"Reiniciar\",\"0626\":\"La plantilla de resultado ha sido almacenada con éxito!\",\"0627\":\"Trazabilidad de la muestra\",\"0628\":\"Referencia\",\"0629\":\"Muestras ingresadas\",\"0630\":\"Muestras Verificadas\",\"0631\":\"Comentario de la prueba\",\"0632\":\"Áreas o pruebas\",\"0633\":\"Próxima actualización\",\"0634\":\"Resultados específicos\",\"0635\":\"Reportado\",\"0636\":\"Urgencias\",\"0637\":\"Pánicos\",\"0638\":\"Críticos\",\"0639\":\"Oportunidad\",\"0640\":\"Buscar por datos de la orden\",\"0641\":\"Avance\",\"0642\":\"Filtro general\",\"0643\":\"Aplicar\",\"0644\":\"Filtro por resultados\",\"0645\":\"Signo\",\"0646\":\"Y\",\"0647\":\"No tiene permisos para editar esta nota\",\"0648\":\"Texto\",\"0649\":\"Numéricos sin agrupar\",\"0650\":\"Numéricos agrupados\",\"0651\":\"¡No existe un informe de históricos para las pruebas escogidas!\",\"0652\":\"Normal\",\"0653\":\"Procesados\",\"0654\":\"Agregar / Quitar pruebas\",\"0655\":\"Buscar pruebas por código o nombre\",\"0656\":\"Tiempo\",\"0657\":\"En la orden @@@@ se agregaron A## pruebas y se borraron E##\",\"0658\":\"¡Advertencia!\",\"0659\":\"¡Atención!\",\"0660\":\"La prueba digitada ya tiene resultado. ¿Desea quitarla de la lista?\",\"0661\":\"La prueba digitada ya está validada, por lo tanto no podrá quitarse de la lista.\",\"0662\":\"No es posible guardar órdenes sin pruebas.\",\"0663\":\"Debe diligenciar los demográficos obligatorios marcados en rojo.\",\"0664\":\"Sin asignar\",\"0665\":\"Configuración del sistema\",\"0666\":\"El usuario @@@@ a cambiado la configuración del sistema, esto podría afectar la aplicación tanto funcional como visualmente, le recomendamos que cierre sesión y vuelva a ingresar al sistema.\",\"0667\":\"Ayuda\",\"0668\":\"¡No existen datos para la fecha! Debe crear órdenes sin historia en la página correspondiente.\",\"0669\":\"Paciente destino\",\"0670\":\"Los datos del paciente han sido ingresados.\",\"0671\":\"Los datos del paciente han sido actualizados.\",\"0672\":\"¡El paciente ya existe en el sistema!\",\"0673\":\"¡El paciente no fue actualizado!\",\"0674\":\"El género del paciente no corresponde con el de la prueba.\",\"0675\":\"La edad del paciente está fuera del rango de la edad de la prueba.\",\"0676\":\"¡La orden @@@@ no posee pruebas registradas, por lo tanto no podrá asignársele a ningún paciente!\",\"0677\":\"Detalle del error\",\"0678\":\"Mensaje del evento\",\"0679\":\"Detalle del evento\",\"0680\":\"Tipo\",\"0681\":\"Sin definir\",\"0682\":\"¿Está seguro que desea activar las órdenes seleccionadas?\",\"0683\":\"Activar orden\",\"0684\":\"En el rango seleccionado no se encuentran órdenes inactivas\",\"0685\":\"La orden a sido activada satisfactoriamente\",\"0686\":\"Apellido\",\"0687\":\"Enviar mensaje\",\"0688\":\"Destinatario\",\"0689\":\"Email inválido\",\"0690\":\"Asunto\",\"0691\":\"Nombre del adjunto\",\"0692\":\"Enviar\",\"0693\":\"Correo exitosamente enviado\",\"0694\":\"Total errores\",\"0695\":\"Bloqueo de la prueba\",\"0696\":\"Desbloquear\",\"0697\":\"Gradilla general\",\"0698\":\"Abierta\",\"0699\":\"Cerrada\",\"0700\":\"Gradilla pendiente\",\"0701\":\"Gradilla especial\",\"0702\":\"Configuración de gradilla\",\"0703\":\"Tipo de gradilla\",\"0704\":\"La muestra ha sido almacenada satisfactoriamente\",\"0705\":\"Gradilla\",\"0706\":\"Posición\",\"0707\":\"La muestra ha sido almacenada con anterioridad en la(s) gradilla(s)\",\"0708\":\"¿Desea volver a almacenar la muestra?\",\"0709\":\"Cierre de gradilla\",\"0710\":\"Nevera\",\"0711\":\"Piso\",\"0712\":\"Detalle de la orden\",\"0713\":\"Búsqueda de muestras\",\"0714\":\"Agregar gradilla\",\"0715\":\"Cargar gradilla\",\"0716\":\"¡Seleccione una gradilla de cada tipo!\",\"0717\":\"¡Todas las gradillas seleccionadas deben estar abiertas para poder realizar el almacenamiento!\",\"0718\":\"¡La muestra no ha sido verificada!\",\"0719\":\"¡La gradilla para almacenar esta muestra no cuenta con espacio suficiente, seleccione otra gradilla!\",\"0720\":\"Confidencial\",\"0721\":\"Gradilla cerrada correctamente\",\"0722\":\"Listado de trazabilidad de la muestra\",\"0723\":\"Listado de trazabilidad de la orden\",\"0724\":\"Acción\",\"0725\":\"Advertencias de validación\",\"0726\":\"Por resultado\",\"0727\":\"Por entrevista\",\"0728\":\"Desactivar órdenes\",\"0729\":\"Borrar resultado\",\"0730\":\"Borrar pruebas\",\"0731\":\"Las pruebas de la siguientes ordenes no pueden ser eliminadas. ¡Verifique que la orden contenga más pruebas!\",\"0732\":\"Para usar este módulo debe crear motivos que sean de tipo modificación de ingreso\",\"0733\":\"Para usar este módulo debe crear motivos que sean de tipo modificación de resultado\",\"0734\":\"No se encuentran órdenes para borrar resultados en las pruebas seleccionadas\",\"0735\":\"No se encuentran órdenes para borrar pruebas en el rango seleccionado\",\"0736\":\"Desecho de muestras\",\"0737\":\"Extraer muestra\",\"0738\":\"Comentario extracción de la muestra\",\"0739\":\"Observaciones al resultado\",\"0740\":\"Técnica\",\"0741\":\"No controlada\",\"0742\":\"Base de datos\",\"0743\":\"Aplicación\",\"0744\":\"Se desbloquearon todas las órdenes e historias de los pacientes.\",\"0745\":\"No se encontraron ordenes ni historias bloqueadas.\",\"0746\":\"Perfil\",\"0747\":\"Información\",\"0748\":\"Ingreso\",\"0749\":\"Trazabilidad de la prueba\",\"0750\":\"Edición del resultado\",\"0751\":\"Validación del resultado\",\"0752\":\"Entrega del resultado\",\"0753\":\"Registro del resultado\",\"0754\":\"Sistema de monitoreo de posición, temperatura y humedad\",\"0755\":\"Sistema de monitoreo automatizado, para el rastreo y localización de neveras de recolección de muestras\",\"0756\":\"Ver más\",\"0757\":\"Software para banco de sangre y servicio transfusiónal\",\"0758\":\"Software que permite la gestión integral de la información, los procesos de bancos de sangre y servicios trasfusionales\",\"0759\":\"Sistema de información para laboratorio clínico\",\"0760\":\"Nuestros productos LIS gestionan toda la información del laboratorio químico, patología, microbiología y citología\",\"0761\":\"Sistema integrado para la gestión de turnos de atención\",\"0762\":\"Organizar y agilizar la atención de usuarios a partir de la categorización y priorización del servicio\",\"0763\":\"Sistema de banda transportadora de muestras\",\"0764\":\"Permite el transporte y clasificación de muestras sanguíneas en tubos, frascos de orina y coprológicos\",\"0765\":\"Automático\",\"0766\":\"Órdenes entregadas\",\"0767\":\"Ordenado\",\"0768\":\"Preliminar\",\"0769\":\"Serie\",\"0770\":\"Generar por\",\"0771\":\"¿Esta seguro de realizar la anulación de la orden?\",\"0772\":\"Última prueba realizada\",\"0773\":\"Diario\",\"0774\":\"Anular\",\"0775\":\"Talón\",\"0776\":\"Deshacer\",\"0777\":\"Cotización\",\"0778\":\"Re-llamado\",\"0779\":\"Caja\",\"0780\":\"Dependencia de demográficos\",\"0781\":\"El demográfico seleccionado ya se encuentra en la lista!\",\"0782\":\"Editor de informes\",\"0783\":\"Especial\",\"0784\":\"Creación acta de desecho\",\"0785\":\"Abiertas\",\"0786\":\"Cerradas\",\"0787\":\"Verifique la conexión con el cliente de impresión.\",\"0788\":\"Archivo mandado a la cola de impresión.\",\"0789\":\"Acta\",\"0790\":\"Acta desecho de muestra\",\"0791\":\"Remover muestra\",\"0792\":\"¡Debe configurar el tiempo de almacenamiento para la muestra seleccionada!\",\"0793\":\"Detalle\",\"0794\":\"Desechar gradillas\",\"0795\":\"Desechar muestra\",\"0796\":\"No se encuentran muestras desechadas en el acta seleccionada\",\"0797\":\"Días de almacenamiento\",\"0798\":\"No se encuentran gradillas candidatas para desechar\",\"0799\":\"Seleccione la muestra que desea desechar\",\"0800\":\"¡La orden digitada ya se encuentra registrada en el sistema!\",\"0801\":\"Muestra almacenada\",\"0802\":\"Muestra extraída\",\"0803\":\"Muestra desechada\",\"0804\":\"Cierre de acta\",\"0805\":\"Usuario desecho\",\"0806\":\"Fecha desecho\",\"0807\":\"Filas\",\"0808\":\"Columnas\",\"0809\":\"La muestra no puede ser desechada por una de las siguientes razones\",\"0810\":\"La muestra en la que se encuentra la gradilla no ha sido cerrada\",\"0811\":\"La muestra fue extraída de la posición en la que fue almacenada\",\"0812\":\"La muestra ha sido desechada con anterioridad\",\"0813\":\"¡La muestra ingresada no se encuentra almacenada en ninguna gradilla!\",\"0814\":\"Muestra(s) han sido agregadas al acta\",\"0815\":\"¡Debe seleccionar el género y la edad del paciente!\",\"0816\":\"¡Debe seleccionar la tarifa de la orden!\",\"0817\":\"¡Existen ## pruebas que aún no se le han asignado tarifa!\",\"0818\":\"¡Existe una prueba que aún no se le ha asignado tarifa!\",\"0819\":\"Agregar pago\",\"0820\":\"Forma de pago\",\"0821\":\"Banco\",\"0822\":\"Tarjeta de crédito\",\"0823\":\"Tarjeta\",\"0824\":\"Número de cuenta\",\"0825\":\"Tipo de descuento\",\"0826\":\"Valor de descuento\",\"0827\":\"Porcentaje\",\"0828\":\"No existen pacientes con los datos ingresados. ¿Desea crearlo?\",\"0829\":\"¡Ya existe un paciente con la historia ingresada!\",\"0830\":\"Cargar\",\"0832\":\"Búsqueda de pacientes\",\"0833\":\"Tipo de documento\",\"0834\":\"Pruebas configuradas para registro de resultados\",\"0835\":\"Existen pruebas relacionadas a este diagnóstico. ¿Está seguro que desea eliminarlo, esto eliminara las pruebas relacionadas?\",\"0836\":\"¡Existen pruebas validadas, por lo tanto el diagnostico no puede ser eliminado!\",\"0837\":\"Existen pruebas con resultado. ¿Está seguro que desea eliminarlas?\",\"0838\":\"¡La prueba no aplica para ningún diagnóstico seleccionado!\",\"0839\":\"Información de la prueba\",\"0840\":\"Rango de edad\",\"0841\":\"Características generales\",\"0842\":\"Eliminar del perfil\",\"0843\":\"Participa en facturación\",\"0844\":\"Participa en estadísticas\",\"0845\":\"Estadística procesados\",\"0846\":\"Ver en consultas\",\"0847\":\"Ver en ingreso de órdenes\",\"0848\":\"Procesamiento\",\"0849\":\"Autovalidación\",\"0850\":\"Vigencia del resultado\",\"0851\":\"Días de procesamiento\",\"0852\":\"Requiere validación preliminar\",\"0853\":\"Genera alerta por tendencia\",\"0854\":\"Tipo de resultado\",\"0855\":\"Decimales\",\"0856\":\"Resultado automático\",\"0857\":\"Resultado en el ingreso\",\"0858\":\"Requisitos\",\"0859\":\"Comentario fijo\",\"0860\":\"Comentario al imprimir\",\"0861\":\"Información general\",\"0862\":\"Manualmente\",\"0863\":\"Valor de referencia\",\"0864\":\"Resultado Analizador\",\"0865\":\"Siempre\",\"0866\":\"Area\",\"0867\":\"Recipiente\",\"0868\":\"Total a pagar\",\"0869\":\"Total con descuento\",\"0870\":\"El valor digitado supera al saldo total.\",\"0871\":\"Iniciar atención de turnos\",\"0872\":\"Gestionando turnos\",\"0873\":\"Gestión de turnos pausado\",\"0874\":\"Módulo\",\"0875\":\"Taquilla\",\"0876\":\"Motivo del receso\",\"0877\":\"Iniciar\",\"0878\":\"Pausar\",\"0879\":\"Terminar\",\"0880\":\"A la orden @@@@ no se le puede agregar o quitar pruebas debido a que tiene caja asociada.\",\"0881\":\"Existe un saldo pendiente ¿Desea guardar?\",\"0882\":\"No tiene pagos registrados ¿Desea guardar?\",\"0883\":\"Recibo de caja\",\"0884\":\"Pagos\",\"0885\":\"Llamado de turno\",\"0886\":\"Turno\",\"0887\":\"Tipo de turno\",\"0888\":\"Prioridad\",\"0889\":\"Tiempo de espera\",\"0890\":\"Llamar\",\"0891\":\"No se encontraron turnos disponible\",\"0892\":\"Contador en segundos\",\"0893\":\"Motivo de cancelación\",\"0894\":\"¿Esta seguro? El turno no podra ser llamado por otro usuario\",\"0895\":\"Posponer turno\",\"0896\":\"Tiempo a posponer\",\"0897\":\"Asociar a la orden\",\"0898\":\"No existen órdenes con el filtro seleccionado\",\"0899\":\"Atender\",\"0900\":\"Reservar\",\"0901\":\"Aplazar\",\"0902\":\"Asociar a la orden\",\"0903\":\"Ingresar a la orden\",\"0904\":\"Transferencia\",\"0905\":\"Terminar turno\",\"0906\":\"El turno finalizó\",\"0907\":\"El turno se canceló\",\"0908\":\"El turno se reservó\",\"0909\":\"El turno se aplazó\",\"0910\":\"llamar Turno\",\"0911\":\"Permitir transferencia en\",\"0912\":\"Transferir a\",\"0913\":\"Debe configurar servicios de transferencia en SIGA\",\"0914\":\"Transferir\",\"0915\":\"El turno se transfiere\",\"0916\":\"Gestionar turno\",\"0917\":\"No existen turnos para asignar\",\"0918\":\"El turno ya se encuentra asignado\",\"0919\":\"SIN FILTRO\",\"0920\":\"Turno manual\",\"0921\":\"Turno automático\",\"0922\":\"¿Esta seguro? De finalizar el turno en ingreso de órdenes\",\"0923\":\"¿Esta seguro? De finalizar el turno en ruta de la muestra\",\"0924\":\"¿Esta seguro? De finalizar el turno en entrada de muestra\",\"0925\":\"Auditoría\",\"0926\":\"Auditoría de órdenes\",\"0927\":\"Auditoría de maestros\",\"0928\":\"Auditoria de usuarios\",\"0929\":\"Control de acceso de usuarios\",\"0930\":\"El perfil o paquete digitado contiene @@ prueba(s) validada(s), por lo tanto no podrá quitarse de la lista.\",\"0931\":\"Descuento\",\"0932\":\"¿Desea eliminar todos los datos de la caja de la orden @@@@?\",\"0933\":\"Visor de sesiones\",\"0934\":\"El informe ha sido cargado con éxito, está disponible para editar.\",\"0935\":\"Editor de códigos de barra\",\"0936\":\"El archivo '@@@@' tiene problemas en su estructura, debe ser revisado para ejecutar la vista previa del informe.\",\"0937\":\"Seleccione un nombre de archivo para crear un informe de resultados.\",\"0938\":\"Informe de resultados\",\"0939\":\"Informe de estadísticas con gráficos\",\"0940\":\"Informe agrupado de estadísticas\",\"0941\":\"Informe de estadísticas sin agrupar\",\"0942\":\"Informe de estadísticas por año\",\"0943\":\"Informe de estadísticas por año fijo\",\"0944\":\"¡El archivo '@@@@' no existe!\",\"0945\":\"Manual\",\"0946\":\"Programada\",\"0947\":\"¿Está seguro de reiniciar el contador de ingreso de ordenes?\",\"0948\":\"Se reiniciará el contador de ingreso de órdenes a las\",\"0949\":\"Horas\",\"0950\":\"Se a reiniciado el contador en ingreso de órdenes\",\"0951\":\"Reiniciar contador\",\"0952\":\"La orden '@@@@' se creó hace ## días, por lo tanto no podrá ser editada debido a que excede el número máximo de días configurados (%%).\",\"0953\":\"Auditoría por orden\",\"0954\":\"Eliminada\",\"0955\":\"Demográficos actuales\",\"0956\":\"Demográficos orden\",\"0957\":\"Demográficos historia\",\"0958\":\"Auditoría demográficos\",\"0959\":\"Auditoría pruebas\",\"0960\":\"Antes\",\"0961\":\"Después\",\"0962\":\"No se encontraron datos relacionados al filtro Seleccionado\",\"0963\":\"Auditoría de la orden\",\"0964\":\"Resultado Actual\",\"0965\":\"Modificación Resultado\",\"0966\":\"Eliminado\",\"0967\":\"Debe agregar mínimo un elemento a la etiqueta\",\"0968\":\"Texto abierto\",\"0969\":\"Fecha y hora\",\"0970\":\"Hora\",\"0971\":\"Muestra nombre\",\"0972\":\"Muestra código\",\"0973\":\"Código de barras con texto\",\"0974\":\"Tipo de Orden código\",\"0975\":\"Tipo de Orden nombre\",\"0976\":\"Fecha de nacimiento\",\"0977\":\"Tipo de documento código\",\"0978\":\"Tipo de documento nombre\",\"0979\":\"Etiqueta\",\"0980\":\"Dimensiones\",\"0981\":\"Elemento\",\"0982\":\"Rotación\",\"0983\":\"Fuente\",\"0984\":\"Alto\",\"0985\":\"Crear nueva versión\",\"0986\":\"¿Está seguro que desea cambiar el código de barras predeterminado?\",\"0987\":\"¡Seleccione las dimensiones de la etiqueta!\",\"0988\":\"Auditoría de la muestra\",\"0989\":\"¡Atención! El archivo '@@@' se guardó en el repositorio de reportes del servidor.\",\"0990\":\"Hubo un error al guardar el reporte '@@@'. Revisar el proceso del archivo routes.js\",\"0991\":\"Especialidad\",\"0992\":\"Configuración general\",\"0993\":\"Festivos\",\"0994\":\"Pregunta\",\"0995\":\"Diagnóstico por prueba\",\"0996\":\"Pruebas por demográfico PYP\",\"0997\":\"Excluir pruebas por usuario\",\"0998\":\"Contador hematológico\",\"0999\":\"Relación de resultados\"}");
                            filterOrder.setVariables("{\"username\":\"" + userExist.getUserName() + "\",\"titleReport\":\"Informe final\",\"date\":\"" + objSDF.format(objDate) + "\",\"formatDate\":\"DD/MM/YYYY\",\"templateReport\":\"reports.mrt\",\"typePrint\":0}");
                            filterOrder.setPrintingMedium(4);
                            filterOrder.setPrintingType(1);
                            filterOrder.setTypeReport(1);
                            String urlImpressionServ = configurationServices.getValue("UrlNodeJs") + "printReportOrders";
                            JsonToBufferNT bufferExtern = integrationService.post(Tools.jsonObject(filterOrder), JsonToBufferNT.class, urlImpressionServ);
                            toBaseSixtyFour = Base64.getEncoder().encodeToString(bufferExtern.getData());
                            String isInsert = deliveryResultDao.insertFinalReport(idOrder, toBaseSixtyFour);
                            if (isInsert.contains("OK"))
                            {
                                //Cambio de estado de Finales y re impreso
                                changeStateTest(filterOrder, listOrder.get(0), userExist.getId());
                                return idOrder.toString();
                            } else
                            {
                                return isInsert;
                            }
                        } else
                        {
                            return "Error";
                        }
                    } else
                    {
                        return "Error";
                    }
                } else
                {
                    return "El nombre de usuario o la contraseña son incorrectos";
                }
            } else
            {
                return "BD no configurada";
            }
        } catch (Exception e)
        {
            return "Error";
        }
    }

    @Override
    public FilterOrderBarcode orderPrintbarcode(FilterOrderBarcode filter) throws Exception
    {

        List<Order> order = ordersBarcode(filter);

        //FilterOrderBarcode filterOrderBarcode = new FilterOrderBarcode();
        filter.setOrdersprint(order);
        //filterOrderBarcode.setPrintingType(PRINTINGTYPE_BARCODE);
        //filterOrderBarcode.setSamples(filter.getSamples());
        //filterOrderBarcode.setSerial(filter.getSerial());
        printingByBarcode(filter);

        return filter;
    }

    private boolean updatePrintSample(List<Order> orders) throws Exception
    {
        try
        {
            List<AuditOperation> auditList = new ArrayList<>(0);
            AuthorizedUser user = JWT.decode(request);

            for (int i = 0; i < orders.size(); i++)
            {
                resultTestDao.updatePrintSample(orders, user.getId());
                for (int j = 0; j < orders.get(i).getSamples().size(); j++)
                {
                    statisticService.sampleStateChanged(1, orders.get(i).getOrderNumber(), orders.get(i).getSamples().get(j).getId());
                }
                List<Integer> samples = orders.get(i).getSamples().stream().map(sample -> sample.getId()).collect(Collectors.toList());
                List<Integer> tests = ordersDao.getTestSampleTake(orders.get(i).getOrderNumber(), samples);
                for (int j = 0; j < tests.size(); j++)
                {
                    auditList.add(new AuditOperation(orders.get(i).getOrderNumber(), tests.get(j), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_SAMPLETEST, Tools.jsonObject(LISEnum.SampleStateHomebound.PRINT.getValue()), null, null, null, null));
                }
            }

            try
            {
                trackingService.registerOperationTracking(auditList);
                IntegrationHisLog.info("registro auditoria impresion");
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
    public List<OrderReportINS> listFiltersOrderRange(long init, long end) throws Exception
    {
        return listDao.listOrderRange(init, end, serviceDemographic.list(true));
    }

    @Override
    public List<OrderReportAidaAcs> getListOrderFacturation(long init, long end, boolean isSearchOrder, boolean groupByProfil) throws Exception
    {
        Integer digitsOrder = configurationServices.getIntValue("DigitosOrden");

        int idCentralSystem = 0;

        boolean account = configurationServices.get("ManejoCliente").getValue().equalsIgnoreCase("true");
        boolean physician = configurationServices.get("ManejoMedico").getValue().equalsIgnoreCase("true");
        boolean rate = configurationServices.get("ManejoTarifa").getValue().equalsIgnoreCase("true");

        boolean checkCentralSystem = configurationServices.get("ConsultarSistemaCentral").getValue().equalsIgnoreCase("true");
        if (checkCentralSystem)
        {
            idCentralSystem = Integer.parseInt(configurationServices.get("SistemaCentralListados").getValue());
        }

        List<OrderReportAidaAcs> list = listDao.listOrderFacturationRange(init, end, isSearchOrder, digitsOrder, serviceDemographic.list(true), rate, account, physician, checkCentralSystem, idCentralSystem, groupByProfil);

        if (list.size() > 0)
        {
            list.stream().forEach(o -> setAgeGroup(o));
        }

        return list;
    }

    @Override
    public List<PrintBarcodeLog> barcodePatient(ReportBarcode report) throws Exception
    {
        try
        {
            final List<PrintBarcodeLog> barcodes = new ArrayList<>(0);
            final List<BarcodeDesigner> listBarcodePatient = barcodeDao.barcodePredetermined(BARCODE_PATIENT);
            final BarcodeDesigner barcode = listBarcodePatient.size() > 0 ? listBarcodePatient.get(0) : null;

            if (report.getIdPatient() != null && report.getIdPatient() > 0)
            {
                Patient patient = patientService.get(report.getIdPatient());
                barcodes.addAll(buildZplDesignerPatient(barcode, patient));
            }
            return barcodes;
        } catch (Exception e)
        {
            OrderCreationLog.error(e.getMessage());
            return new ArrayList<>(0);
        }
    }

    private List<PrintBarcodeLog> buildZplDesignerPatient(BarcodeDesigner barcode, Patient patient)
    {
        final List<PrintBarcodeLog> barcodes = new ArrayList<>(0);
        if (barcode != null)
        {
            final String barcodeDesigner = barcode.getCommand();
            if (barcodeDesigner != null && barcodeDesigner.trim().equals("") == false)
            {
                try
                {
                    Date dateCurrent = new Date();
                    final String dataandtime = new SimpleDateFormat(configurationServices.get("FormatoFecha").getValue() + " hh:mm").format(dateCurrent);
                    final String date = new SimpleDateFormat(configurationServices.get("FormatoFecha").getValue()).format(dateCurrent);
                    final String time = new SimpleDateFormat("hh:mm").format(dateCurrent);
                    Long years = patient == null ? null : Tools.calculateAge(patient.getBirthday());

                    barcodes.add(new PrintBarcodeLog(false, buildZplPatient(barcodeDesigner, dataandtime, date, time, years, patient), patient.getId()));

                } catch (Exception ex)
                {
                    OrderCreationLog.error(ex.getMessage());
                    Logger.getLogger(ReportServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return barcodes;
    }

    private String buildZplPatient(String barcodeDesigner, String dataandtime, String date, String time, Long years, Patient patient)
    {
        try
        {
            String barcode = barcodeDesigner
                    .replace("{demo_-7}", patient != null ? (patient.getRace() != null ? (patient.getRace().getName() == null ? "" : patient.getRace().getName()) : "") : "")
                    .replace("{demo_-10}", patient != null ? (patient.getDocumentType() != null ? (patient.getDocumentType().getName() == null ? "" : patient.getDocumentType().getName()) : "") : "")
                    .replace("{demo_-111}", patient != null ? (patient.getEmail() != null ? patient.getEmail() : "") : "")
                    .replace("{dateandtime}", dataandtime)
                    .replace("{date}", date)
                    .replace("{time}", time)
                    .replace("{history}", patient.getPatientId())
                    .replace("{namePatient}", (patient.getName1() != null ? patient.getName1().toUpperCase() : "") + " " + (patient.getName2() != null ? patient.getName2().toUpperCase() : "") + " " + (patient.getLastName() != null ? patient.getLastName().toUpperCase() : "") + " " + (patient.getSurName() != null ? patient.getSurName().toUpperCase() : ""))
                    .replace("{age}", years == null ? "" : String.valueOf(years))
                    .replace("{gender}", patient.getSex().getCode().equals("2") ? "F" : patient.getSex().getCode().equals("1") ? "M" : "I")
                    .replace("{documenttypecode}", patient.getDocumentType().getAbbr())
                    .replace("{documenttypename}", patient.getDocumentType().getName())
                    .replace("{birthday}", new SimpleDateFormat(configurationServices.get("FormatoFecha").getValue()).format(patient.getBirthday()));

            for (DemographicValue demographic : patient.getDemographics())
            {
                barcode = barcode.replace("{demo_" + demographic.getIdDemographic() + "}", demographic.isEncoded() ? demographic.getCodifiedName() == null ? "" : demographic.getCodifiedName() : demographic.getNotCodifiedValue() == null ? "" : demographic.getNotCodifiedValue());
            }
            return barcode;
        } catch (Exception ex)
        {
            OrderCreationLog.error(ex);
            Logger.getLogger(ReportServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Logica para la impresion de una etiqueta de barras de un paciente
     *
     * @param search Filtros para la impresion
     * @throws Exception
     */
    @Override
    public List<PrintBarcodeLog> printByBarcodePatient(final FilterOrderBarcode search) throws Exception
    {

        ReportBarcode report = new ReportBarcode(search.getSerial(), search.getPatientId());
        List<PrintBarcodeLog> jsons = barcodePatient(report);
        for (PrintBarcodeLog json : jsons)
        {
            json.setPrinting(sendPrinting(json.getZpl(), search.getSerial(), Print.PRINTER_LABEL));
            json.setZpl("");
        }
        return jsons;
    }

    @Override
    public Boolean getValidOrderComplete(long order) throws Exception
    {
        Integer totaltest = resultTestDao.getTotalTestOrder(order);
        Integer totaltestvalid = resultTestDao.getTotalTestValidOrder(order);

        return java.util.Objects.equals(totaltest, totaltestvalid);
    }
    
    @Override
    public List<SuperTest> gettestpendingorder(long order) throws Exception
    {
       return resultTestDao.gettestpendingorder(order);
    }

    /**
     * Establece unidad, edad del paciente
     *
     * @param order informacion de la orden
     */
    private void setAgeGroup(OrderReportAidaAcs order)
    {
        try
        {
            long ages = 0;
            int unit = 1;
            //Calculo de Edad y unidad de la edad

            ages = DateTools.getAgeInYears(DateTools.localDate(order.getBirthday()), DateTools.localDate(order.getCreatedDateShort()));
            if (ages <= 0)
            {
                ages = DateTools.getAgeInMonths(DateTools.localDate(order.getBirthday()), DateTools.localDate(order.getCreatedDateShort()));
                unit = 3;
                if (ages <= 0)
                {
                    ages = DateTools.getAgeInDays(DateTools.localDate(order.getBirthday()), DateTools.localDate(order.getCreatedDateShort()));
                    unit = 2;
                }
            }

            order.setAge((int) ages);
            order.setAgeUnit(unit);
        } catch (Exception ex)
        {
            OrderCreationLog.error(ex);
        }
    }
}

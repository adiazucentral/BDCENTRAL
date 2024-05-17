package net.cltech.enterprisent.service.impl.enterprisent.integration.Middleware;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.integration.MiddlewareDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicDao;
import net.cltech.enterprisent.dao.interfaces.masters.tracking.DestinationDao;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.dao.interfaces.operation.microbiology.MicrobiologyDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultTestDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.middleware.AlicuotaWithVolum;
import net.cltech.enterprisent.domain.integration.middleware.AnatomicalSiteMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.AntibioticMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.CheckDestination;
import net.cltech.enterprisent.domain.integration.middleware.DemographicItemMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.DemographicMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.DestinationCovered;
import net.cltech.enterprisent.domain.integration.middleware.MicroorganismsMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.MiddlewareMessage;
import net.cltech.enterprisent.domain.integration.middleware.MiddlewareUrl;
import net.cltech.enterprisent.domain.integration.middleware.ReferenceValueMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.SampleMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.SendAstmMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.TestMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.TestToMiddleware;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.microbiology.Antibiotic;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.microbiology.Sensitivity;
import net.cltech.enterprisent.domain.masters.test.ReferenceValue;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.masters.tracking.DestinationRoute;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.masters.user.UserAnalyzer;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobialDetection;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyGrowth;
import net.cltech.enterprisent.domain.operation.microbiology.ResultMicrobiology;
import net.cltech.enterprisent.domain.operation.orders.CommentOrder;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.results.HistoricalResult;
import net.cltech.enterprisent.domain.operation.results.ImageTest;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.results.ResultTestComment;
import net.cltech.enterprisent.domain.operation.results.ResultTestRepetition;
import net.cltech.enterprisent.domain.operation.results.ResultTestValidate;
import net.cltech.enterprisent.domain.operation.tracking.VerifyDestination;
import net.cltech.enterprisent.service.impl.enterprisent.operation.results.ResultServiceEnterpriseNT;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMiddlewareService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.AntibioticService;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.MicroorganismService;
import net.cltech.enterprisent.service.interfaces.masters.test.SampleService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
import net.cltech.enterprisent.service.interfaces.operation.common.CommentService;
import net.cltech.enterprisent.service.interfaces.operation.microbiology.MicrobiologyService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.service.interfaces.operation.tracking.SampleTrackingService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.HtmlToTxt;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.enums.ListEnum;
import net.cltech.enterprisent.tools.log.integration.MiddlewareLog;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Implementacion para integración de Middleware
 *
 * @version 1.0.0
 * @author eacuna
 * @since 18/04/2018
 * @see Creacion
 */
@Service
public class IntegrationMiddlewareServiceEnterpriseNT implements IntegrationMiddlewareService
{

    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private DemographicService demographicService;
    @Autowired
    private ResultsService resultService;
    @Autowired
    private ConfigurationDao configurationDao;
    @Autowired
    private MiddlewareDao middlewareDao;
    @Autowired
    private DemographicDao demographicDao;
    @Autowired
    private ResultTestDao resultTestDao;
    @Autowired
    private MicrobiologyService microbiologyService;
    @Autowired
    private TestService testService;
    @Autowired
    private SampleService sampleService;
    @Autowired
    private UserService userService;
    @Autowired
    private SampleTrackingService sampleTrackingService;
    @Autowired
    private MicroorganismService microorganismService;
    @Autowired
    private AntibioticService antibioticService;
    @Autowired
    private DemographicService serviceDemographic;
    @Autowired
    private ResultDao resultDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ResultServiceEnterpriseNT resultServiceEnterpriseNT;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CommentService commentService;
    @Autowired
    private MicrobiologyDao microbiologyDao;
    @Autowired
    private DestinationDao destinationDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private ResultDao resultsDao;

    public final static char CR = (char) 0x0D;
    public final static char LF = (char) 0x0A;
    public final static String CRLF = "" + CR + LF;
    private final String HEADER_RECORD = "H|\\^&|||{Middleware}|||||{user}||P|1.0|{date}" + CRLF;
    //private final String PATIENT_INFO_RECORD = "P|1||{record}||{name}^{secondName}|{lastName}^{secondLastName}|{dob}|{gender}|{patientDemo}||{orderDemo}||{physician}|||||||||||||||||||{service}|{institution}||" + CRLF;
    private final String PATIENT_INFO_RECORD = "P|1||{record}||{name}^{secondName}|{lastName}^{secondLastName}|{dob}|{gender}|{patientDemo}||{orderDemo}{physician}{service}{account}{institution}||||||||||||||||||||||||" + CRLF;
    //private final String TEST_RECORD = "O|{seq}|{order}|{microFlag}|^^^{testCode}|R|{sampleDate}|||||{action}||||||||^{serviceMicro}^^^{sampleMicro}^^^{aSite}^^^{procedure}^^|{middlewareFlag}|{laboratory}||||F|||||" + CRLF;
    private final String TEST_RECORD = "O|{seq}|{order}|{microFlag}|^^^{testCode}|R|{sampleDate}|||||{action}||||||||^{serviceMicro}^^^{sampleMicro}^^^{aSite}^^^{procedure}^^|{laboratory}|||||F|||||" + CRLF;
    private final String COMMENT_RECORD = "C|1|{comment}|" + CRLF;
    private final String HISTORICAL_RESULTS_RECORD = "R|1|^^^{testCode}|{result}||||||||{date}" + CRLF; 
    private final String END_RECORD = "L|1|N" + CRLF;
    private final String DATE_FORMAT = "yyyyMMddhhmmss";
    private String SEPARATOR;
    private int ORDERDIGITS;

    /**
     *
     * @param order Numero de orden
     * @param listTestBefore Lista de examenes despues del proceso
     * @param sample Id de la muestra
     * @param origin Si es 1 -> ingreso 2 -> verificacion 4 -> Verificacion
     * microbiologia
     * @param laboratorys
     * @param branch
     * @param retake
     */
    @Override
    public int sendOrderASTM(long order, List<Test> listTestBefore, String sample, int origin, String laboratorys, MicrobiologyGrowth microbiologyGrowth, List<Test> listTestDelete, int branch, Boolean retake)
    {
        MiddlewareLog.info("entro  AST MW ORDER  : " + order);
        int sendMessages = 0;
        try
        {
            if (order > 0)
            {

                SEPARATOR = configurationService.getValue("SeparadorMiddleware");
                ORDERDIGITS = Integer.parseInt(configurationService.getValue("DigitoAño"));
                List<Demographic> demos = serviceDemographic.list(true).stream().filter((Demographic t) -> t.getOrigin().equals("O") && t.isState()).collect(Collectors.toList());

                ResultFilter resultFilter = new ResultFilter();
                resultFilter.setFirstOrder(order);
                resultFilter.setLastOrder(order);
                resultFilter.setFilterId(1);
                List<Order> orders = resultService.orderResultList(order, demos, "A", sample, laboratorys);

                
                if(!orders.isEmpty() && (listTestBefore == null || listTestBefore.size() > 0)){
                    
                    orders.get(0).setTests(addListDelete(listTestBefore, orders.get(0).getTests()));              
                    sendMessages = buildListMessagesOrder(orders, origin, microbiologyGrowth);
                }

                if (listTestDelete != null)
                {
                    if (listTestDelete.size() > 0)
                    {
                        if(orders.isEmpty()){
                           orders = resultService.orderResultListDeleteTest(order, demos, "A", sample, laboratorys);
                        }
                        List<Test> deleteTests = listTestDelete;
                        //List<Test> toDelete = orderService.getTestToDeleteOrder(deleteTests, order);
                        orders.get(0).setDeleteTests(listTestDelete);
                        sendTestDeleteMiddleware(orders.get(0), branch, retake);
                    }
                }
            } else
            {
                MiddlewareLog.error("No se encontro la orden");
            }

            MiddlewareLog.info("MENSAGUE ENVIADO PARA ORDER : " + order);
            MiddlewareLog.info("MENSAGUE  : " + sendMessages);
            return sendMessages;
        } catch (Exception ex)
        {
            MiddlewareLog.error(ex);
            return 0;
        }
    }



    private List<Test> addListDelete(List<Test> listTestBefore, List<Test> listTestLater)
    {
        //Agregar a la lista los eliminados
        if (listTestBefore != null && listTestBefore.isEmpty() == false)
        {
            listTestLater.addAll(listTestBefore.stream()
                    .filter(t -> !listTestLater.contains(t))
                    .filter(t -> t.getTestType() == 0)
                    .map(t -> t.setAction("C"))
                    .collect(Collectors.toList()));
        }
        return listTestLater;
    }

    private List<MiddlewareMessage> createMessageBySample(List<Test> laboratory, Order orderGeneral, MicrobiologyGrowth microbiologyGrowth)
    {
        List<MiddlewareMessage> list = new ArrayList<>();
        //Agrupar por muestra
        laboratory.stream().collect(Collectors.groupingBy(t -> t.getSample().getId()))
                .entrySet().stream()
                .forEach((Map.Entry<Integer, List<Test>> samples) ->
                {
                    try
                    {
                        Date date = new Date();
                        long time = date.getTime();

                        MiddlewareMessage message = new MiddlewareMessage();
                        //Asignarle a la orden los examanes por muestra
                        orderGeneral.setTests(samples.getValue());
                        //Construir mensaje
                        String messageSend;
                        if (microbiologyGrowth != null)
                        {
                            messageSend = buildCheckMicrobiologyASTM(orderGeneral, microbiologyGrowth, SEPARATOR, ORDERDIGITS);
                        } else
                        {
                            messageSend = buildOrderToMiddleASTM(orderGeneral, SEPARATOR, ORDERDIGITS);
                        }

                        if (!messageSend.isEmpty())
                        {
                            message.setMessage(messageSend);
                            message.setDate(Long.toString(time));
                            list.add(message);
                        }
                    } catch (Exception ex)
                    {
                        MiddlewareLog.error(ex);
                    }
                });
        return list;
    }

    private int buildListMessagesOrder(List<Order> orders, int origin, MicrobiologyGrowth microbiologyGrowth)
    {

        final List<Integer> sendListMessages = new ArrayList<>();
        orders.stream().filter((o) -> o.getTests().size() > 0).forEach((Order orderGeneral) ->
        {
            try {
                //COMENTARIOS
                List<CommentOrder> comments = commentDao.getlistCommentOrder(orderGeneral.getOrderNumber());
                List<CommentOrder> diagnostic = commentDao.listCommentOrder(null, orderGeneral.getPatient().getId());
                
                if(comments.size() > 0) {
                    comments.forEach( (CommentOrder comm) -> {
                        String comment = "";
                        if(!"".equals(comm.getComment())) {
                            comment = comm.getComment().substring(comm.getComment().indexOf(">", 0), comm.getComment().indexOf("</", 0)).replaceFirst(">", "");
                            if(!comment.isEmpty() && !"".equals(comment)) {
                                comment = HtmlToTxt.htmlToString(comment);
                            }     
                        } 
                        comm.setComment(comment);
                    });
                }
                
                if(diagnostic.size() > 0) {
                    diagnostic.forEach( (CommentOrder comm) -> {
                        String comment = "";
                        if(!"".equals(comm.getComment())) {
                            comment = comm.getComment().substring(comm.getComment().indexOf(">", 0), comm.getComment().indexOf("</", 0)).replaceFirst(">", "");
                            if(!comment.isEmpty() && !"".equals(comment)) {
                                comment = HtmlToTxt.htmlToString(comment);
                            }     
                        } 
                        comm.setComment(comment);
                    });
                }
                
                orderGeneral.setComments(comments);
                orderGeneral.getPatient().setDiagnostic(diagnostic);

                List<Integer> idsToValidity = orderGeneral.getTests().stream().map(test -> test.getId()).distinct().collect(Collectors.toList());
                List<HistoricalResult> results = resultsDao.get(orderGeneral.getPatient().getId(), idsToValidity);

                orderGeneral.setHistoricalResult(results);
                 
            } catch (Exception ex) {
                Logger.getLogger(IntegrationMiddlewareServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            int total = buildListMessagesTest(orderGeneral.getTests(), origin, orderGeneral, microbiologyGrowth);
            if (total > 0)
            {
                sendListMessages.add(0);
            }

        });
        return sendListMessages.size();
    }

    private int buildListMessagesTest(List<Test> listTestLater, int origin, Order orderGeneral, MicrobiologyGrowth microbiologyGrowth)
    {
        final List<Integer> sendMessages = new ArrayList<>();
        //Agrupar por laboratorio
        listTestLater.stream().collect(Collectors.groupingBy(t -> t.getLaboratory().getId()))
                .entrySet().stream()
                .filter((Map.Entry<Integer, List<Test>> laboratory) -> laboratory.getKey() > 0)
                .forEach((Map.Entry<Integer, List<Test>> laboratory) ->
                {

                    Test test = laboratory.getValue().get(0);

                    if (test.getLaboratory().getUrl() != null && !test.getLaboratory().getUrl().trim().equals(""))
                    {
                        if (validate(origin, test))
                        {
                            try
                            {
                                StringBuilder string = new StringBuilder();
                                string.append("\n\nEnvio de mensaje al middleware");
                                string.append("\nLaboratorio: ")
                                        .append(test.getLaboratory().getId())
                                        .append(" Con url: ")
                                        .append(test.getLaboratory().getUrl());
                                orderGeneral.setTests(null);
                                List<MiddlewareMessage> list = createMessageBySample(laboratory.getValue(), orderGeneral, microbiologyGrowth);
                                string.append("\n").append(Tools.jsonObject(list));
                                // Se envía mensaje con orden al Middleware. Si no se tiene respuesta exitosa, se inserta en tabla de control
                                if (!list.isEmpty())
                                {
                                    if (!sendMiddleware(list, test.getLaboratory().getUrl()))
                                    {
                                        // Se insertan todos los mensajes ASTM sobre la tabla de control
                                        for (MiddlewareMessage middlewareMessage : list)
                                        {
                                            OrderCreationLog.info("MENSAJE DE ORDEN DE MW : " + middlewareMessage.getMessage());
                                            SendAstmMiddleware sendMessageAstm = new SendAstmMiddleware();
                                            sendMessageAstm.setRestServiceRoute(test.getLaboratory().getUrl());
                                            sendMessageAstm.setMessageASTM(middlewareMessage.getMessage());
                                            sendMessageAstm.setIndicator(0);

                                            if (middlewareDao.insertASTMInControlTable(sendMessageAstm))
                                            {
                                                string.append("\nEnvio del mensaje al middleware correcto\n");
                                            } else
                                            {
                                                string.append("\nEnvio del mensaje al middleware fallido\n");
                                            }
                                            MiddlewareLog.info(string.toString());

                                        }
                                    } else
                                    {
                                        sendMessages.add(0);
                                    }
                                } else
                                {
                                    OrderCreationLog.info("LISTA DE ORDENES A MW ESTA VACIA");
                                }
                            } catch (Exception ex)
                            {
                                System.out.println("error del laboratorio");
                                MiddlewareLog.error(ex);
                            }
                        } else
                        {
                            MiddlewareLog.error("El laboratorio " + laboratory.getValue().get(0).getLaboratory().getId() + " no es valido para ingreso o verificacion para enviar mensaje al middleware");
                        }
                    } else
                    {
                        MiddlewareLog.error("El laboratorio " + laboratory.getValue().get(0).getLaboratory().getId() + " no tiene url para enviar mensaje al middleware");
                    }
                });
        return sendMessages.size();
    }

    /**
     * Crea un mensaje para enviar al Middleware con orden de laboratorio
     *
     * @param order Número de orden
     * @param separator Separador de muestra
     * @param digits Dígitos de la orden
     * @return Mensaje con orden para Middleware
     */
    public String buildOrderToMiddleASTM(Order order, String separator, int digits) throws Exception
    {
        try
        {
            if (order.getService().getId() == 0)
            {
                order.setService(null);
            }
        } catch (Exception ex)
        {
            order.setService(null);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        StringBuilder astm = new StringBuilder();
        String gender = ListEnum.Gender.MALE.getValue() == order.getPatient().getSex().getId() ? "M" : ListEnum.Gender.FEMALE.getValue() == order.getPatient().getSex().getId() ? "F" : "U";
        //Obtiene el string que representa al paciente, reemplazando sus recpectivos variables por los valores del paciente.
        String patientInfo = PATIENT_INFO_RECORD.replace("{record}", order.getPatient().getPatientId())
                .replace("{name}", order.getPatient() != null ? (order.getPatient().getName1() != null ? order.getPatient().getName1() : "") : "")
                .replace("{secondName}", order.getPatient() != null ? (order.getPatient().getName2() != null ? order.getPatient().getName2() : "") : "")
                .replace("{lastName}", order.getPatient() != null ? (order.getPatient().getLastName() != null ? order.getPatient().getLastName() : "") : "")
                .replace("{secondLastName}", order.getPatient() != null ? (order.getPatient().getSurName() != null ? order.getPatient().getSurName() : "") : "")
                .replace("{dob}", order.getPatient() != null && order.getPatient().getBirthday() != null ? "" + DateTools.dateToNumber(order.getPatient().getBirthday()) : "")
                .replace("{gender}", gender)
                .replace("{patientDemo}", patientDemographicASTM(order.getPatient())) //vacio
                .replace("{orderDemo}", orderDemographicASTM(order))
                .replace("{physician}", order.getPhysician().getId() == null ? "" : (order.getPhysician().getName() == null ? "" : "/-2^MEDICO^^" + order.getPhysician().getId() + "^1"))
                .replace("{institution}", order.getBranch().getId() == null ? "" : (order.getBranch().getName() == null ? "" : "/-5^SEDE^^" + order.getBranch().getId() + "^1"))
                .replace("{service}", order.getService() == null ? "" : (order.getService().getName() == null ? "" : "/-6^SERVICIO^^" + order.getService().getId() + "^1"))
                .replace("{account}", order.getAccount() == null ? "" : (order.getAccount().getName() == null ? "" : "/-1^CLIENTE^^" + order.getAccount().getId() + "^1"));

        //Obtiene y agrega la cabecera del mensaje
        astm.append(HEADER_RECORD.replace("{date}", sdf.format(new Date())).replace("{user}", "-1").replace("{Middleware}","Middleware"));
        //Agrega al astm el string del paciente
        astm.append(patientInfo);
        
        //Agregar diagnostico
        for (CommentOrder comment : order.getPatient().getDiagnostic())
        {
            if (!comment.getComment().trim().isEmpty())
            {
                //Agrega al astm el string del comentario
                astm.append(COMMENT_RECORD.replace("{comment}", comment.getComment()));
            }
        }
        
        int sequence = 1;
        
        for (Test test : order.getTests())
        {
            if (test.getSample().getLaboratorytype() == null)
            {
                Sample sample = sampleService.getTypeLaboratoryBySample(test.getSample().getId());
                test.getSample().setLaboratorytype(sample.getLaboratorytype());
                test.getSample().setCodesample(sample.getCodesample());
            }

            if (!test.getSample().getLaboratorytype().equals("3") || test.getAction().equalsIgnoreCase("R"))
            {
                try
                {
                    if (order.getService().getId() == 0)
                    {
                        order.setService(null);
                    }
                } catch (Exception ex)
                {
                    order.setService(null);
                }

                String testInfo = TEST_RECORD.replace("{seq}", "" + sequence)
                        .replace("{order}", Tools.getOrderNumberToPrint(order.getOrderNumber(), digits) + separator + test.getSample().getCodesample())
                        .replace("{testCode}", test.getCode())
                        .replace("{action}", test.getAction() == null ? "A" : test.getAction())
                        .replace("{microFlag}", test.getSample().getLaboratorytype().contains("3") ? "1" : "0")
                        //.replace("{middlewareFlag}", test.getLaboratory().isMiddleware() ? "1" : "0")
                        .replace("{laboratory}", test.getLaboratory().getCode() + "^" + test.getLaboratory().getName())
                        .replaceAll("\\{sampleDate\\}", test.getSample().getTakeDate() == null ? "" : sdf.format(test.getSample().getTakeDate()))
                        .replace("{serviceMicro}", order.getService() == null ? "" : order.getService().getId().toString())
                        .replace("{sampleMicro}", test.getSampleIdMicro() == null ? "" : test.getSampleIdMicro())
                        .replace("{aSite}", test.getAnatomicalSite() == null ? "" : test.getAnatomicalSite())
                        .replace("{procedure}", "");

                sequence++;
                //Agrega al astm el string de la informacion del examen
                astm.append(testInfo);
            }
        }
        
        //Agregar comentarios al mensaje
        for (CommentOrder comment : order.getComments())
        {
            if (!comment.getComment().trim().isEmpty())
            {
                //Agrega al astm el string del comentario
                astm.append(COMMENT_RECORD.replace("{comment}", comment.getComment()));
            }
        }
                
        if( order.getHistoricalResult() != null && order.getHistoricalResult().size() > 0 ) {
            //Historicos
            for (HistoricalResult result : order.getHistoricalResult())
            {
                if( result.getLastResult() != null ) {
                    String resultInfo = HISTORICAL_RESULTS_RECORD.replace("{testCode}", result.getTestCode())
                            .replace("{result}", result.getLastResult())
                            .replace("{date}", result.getLastResultDate() == null ? "" : sdf.format(result.getLastResultDate()));

                    astm.append(resultInfo);
                }
            }
        }

        //Agrega al astm el string del final del mensaje
        astm.append(END_RECORD);
        String astmString = sequence > 1 ? astm.toString() : "";
        return astmString;
    }

    /**
     * Crea un mensaje para enviar al Middleware con orden de Microbiología
     *
     * @param order Numero de orden
     * @param microbiologyGrowth
     * @param separator Separador de muestra
     * @param digits Dígitos de la orden
     * @return Mensaje con orden para Middleware
     */
    public String buildCheckMicrobiologyASTM(Order order, MicrobiologyGrowth microbiologyGrowth, String separator, int digits)
    {
        try
        {
            net.cltech.enterprisent.domain.masters.test.Test test = testService.get(microbiologyGrowth.getTest().getId(), null, null, null);

            Test testLaboratory = order.getTests().stream()
                    .filter(line -> line.getId().equals(microbiologyGrowth.getTest().getId()))
                    .findFirst()
                    .orElse(null);

            List<UserAnalyzer> listUsers = userService.getUsersAnalyzers();
            int idMicrobiologyDestination = microbiologyGrowth.getDestination() == 0 ? Integer.valueOf(configurationDao.get("DestinoVerificaMicrobiologia").getValue()) : microbiologyGrowth.getDestination();
            
            Destination destination =  destinationDao.getNameDestination(idMicrobiologyDestination);

            UserAnalyzer analizer = listUsers.stream()
                    .filter(line -> line.getReferenceLaboratory().equals(testLaboratory.getLaboratory().getId())
                    && line.getDestination().equals(idMicrobiologyDestination))
                    .findFirst()
                    .orElse(null);

            try
            {
                if (order.getService().getId() == 0)
                {
                    order.setService(null);
                }
            } catch (Exception ex)
            {
                order.setService(null);
            }

            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            StringBuilder astm = new StringBuilder();
            String gender = ListEnum.Gender.MALE.getValue() == order.getPatient().getSex().getId() ? "M" : ListEnum.Gender.FEMALE.getValue() == order.getPatient().getSex().getId() ? "F" : "U";
            //Obtiene el string que representa al paciente, reemplazando sus recpectivos variables por los valores del paciente.
            String patientInfo = PATIENT_INFO_RECORD.replace("{record}", order.getPatient().getPatientId())
                    .replace("{name}", order.getPatient().getName1())
                    .replace("{secondName}", order.getPatient().getName2() == null ? "" : order.getPatient().getName2())
                    .replace("{lastName}", order.getPatient().getLastName())
                    .replace("{secondLastName}", order.getPatient().getSurName() == null ? "" : order.getPatient().getSurName())
                    .replace("{dob}", "" + DateTools.dateToNumber(order.getPatient().getBirthday()))
                    .replace("{gender}", gender)
                    .replace("{patientDemo}", patientDemographicASTM(order.getPatient())) //vacio
                    .replace("{orderDemo}", orderDemographicASTM(order))
                    .replace("{physician}", order.getPhysician() == null ? "" : (order.getPhysician().getName() == null ? "" : "/-2^MEDICO^^" + order.getPhysician().getId() + "^1"))
                    .replace("{institution}", order.getBranch() == null ? "" : (order.getBranch().getName() == null ? "" : "/-5^SEDE^^" + order.getBranch().getId() + "^1"))
                    .replace("{service}", order.getService() == null ? "" : (order.getService().getName() == null ? "" : "/-6^SERVICIO^^" + order.getService().getId() + "^1"))
                    .replace("{account}", order.getAccount() == null ? "" : (order.getAccount().getName() == null ? "" : "/-1^CLIENTE^^" + order.getAccount().getId() + "^1"));
            //Obtiene y agrega la cabecera del mensaje
            astm.append(HEADER_RECORD.replace("{date}", sdf.format(new Date())).replace("{user}", analizer == null ? "-1" : analizer.getIdentification() + "^" + analizer.getUserName()).replace("{Middleware}",destination == null ? "Middleware" : destination.getName()));
            //Agrega al astm el string del paciente
            astm.append(patientInfo);
            
            //Agregar diagnostico
            for (CommentOrder comment : order.getPatient().getDiagnostic())
            {
                if (!comment.getComment().trim().isEmpty())
                {
                    //Agrega al astm el string del comentario
                    astm.append(COMMENT_RECORD.replace("{comment}", comment.getComment()));
                }
            }

            //Long[] idsprocedures = microbiologyGrowth.getProcedures().stream().map(am -> am.getId()).distinct().toArray(Long[]::new);
            String testInfo = TEST_RECORD.replace("{seq}", "" + 1)
                    .replace("{order}", Tools.getOrderNumberToPrint(order.getOrderNumber(), digits) + separator + test.getSample().getCodesample())
                    .replace("{testCode}", test.getCode())
                    .replace("{action}", "A")
                    .replace("{microFlag}", "1")
                    //.replace("{middlewareFlag}", testLaboratory.getLaboratory().isMiddleware() ? "1" : "0")
                    .replace("{laboratory}", testLaboratory.getCode() + "^" + testLaboratory.getName())
                    .replaceAll("\\{sampleDate\\}", test.getSample().getTakeDate() == null ? "" : sdf.format(test.getSample().getTakeDate()))
                    .replace("{serviceMicro}", order.getService() == null ? "" : order.getService().getId().toString()) // test.getService() == null ? "" : test.getService())
                    .replace("{sampleMicro}", microbiologyGrowth.getSubSample().getId().toString())
                    .replace("{aSite}", microbiologyGrowth.getAnatomicalSite().getId().toString())
                    .replace("{procedure}", "");
            //.replace("{procedure}", Arrays.toString(idsprocedures));
            astm.append(testInfo);

            //Agregar comentarios al mensaje
            for (CommentOrder comment : order.getComments())
            {
                if (!comment.getComment().trim().isEmpty())
                {
                    //Agrega al astm el string del comentario
                    astm.append(COMMENT_RECORD.replace("{comment}", comment.getComment()));
                }
            }

            if( order.getHistoricalResult() != null && order.getHistoricalResult().size() > 0 ) {
                //Historicos
                for (HistoricalResult result : order.getHistoricalResult())
                {
                    if( result.getLastResult() != null ) {
                        String resultInfo = HISTORICAL_RESULTS_RECORD.replace("{testCode}", result.getTestCode())
                                .replace("{result}", result.getLastResult())
                                .replace("{date}", result.getLastResultDate() == null ? "" : sdf.format(result.getLastResultDate()));

                        astm.append(resultInfo);
                    }
                }
            }
                    
            List<Test> testMicrobiology1 = order.getTests().stream()
                    .filter(line -> !line.getId().equals(microbiologyGrowth.getTest().getId()) && line.getSample().getLaboratorytype().equals("3"))
                    .collect(Collectors.toList());

            int sequence = 1;
            for (Test test1 : testMicrobiology1)
            {
                if (test1.getSample().getLaboratorytype() == null)
                {
                    Sample sample = sampleService.getTypeLaboratoryBySample(test1.getSample().getId());
                    test1.getSample().setLaboratorytype(sample.getLaboratorytype());
                    test1.getSample().setCodesample(sample.getCodesample());
                }

                try
                {
                    if (order.getService().getId() == 0)
                    {
                        order.setService(null);
                    }
                } catch (Exception ex)
                {
                    order.setService(null);
                }

                String testInfo1 = TEST_RECORD.replace("{seq}", "" + sequence)
                        .replace("{order}", Tools.getOrderNumberToPrint(order.getOrderNumber(), digits) + separator + test1.getSample().getCodesample())
                        .replace("{testCode}", test1.getCode())
                        .replace("{action}", test1.getAction() == null ? "A" : test1.getAction())
                        .replace("{microFlag}", test1.getSample().getLaboratorytype().contains("3") ? "1" : "0")
                        //.replace("{middlewareFlag}", test.getLaboratory().isMiddleware() ? "1" : "0")
                        .replace("{laboratory}", test1.getLaboratory().getCode() + "^" + test1.getLaboratory().getName())
                        .replaceAll("\\{sampleDate\\}", test1.getSample().getTakeDate() == null ? "" : sdf.format(test1.getSample().getTakeDate()))
                        .replace("{serviceMicro}", order.getService() == null ? "" : order.getService().getId().toString())
                        .replace("{sampleMicro}", test1.getSampleIdMicro() == null ? "" : test1.getSampleIdMicro())
                        .replace("{aSite}", test1.getAnatomicalSite() == null ? "" : test1.getAnatomicalSite())
                        .replace("{procedure}", "");

                sequence++;
                //Agrega al astm el string de la informacion del examen
                astm.append(testInfo1);

            }
            //Agrega al astm el string del final del mensaje
            astm.append(END_RECORD);
            //return astm.toString();
            return astm.toString();
        } catch (Exception ex)
        {
            MiddlewareLog.error(ex);
            return "";
        }
    }

    private String patientDemographicASTM(Patient patient)
    {
        //Agregar demograficos por paciente
        List<String> demos = new ArrayList<>();
        if (!patient.getDemographics().isEmpty())
        {
            demos.addAll(patient.getDemographics().stream()
                    .map(demo -> buildDemographic(Integer.toString(demo.getIdDemographic()), demo.getDemographic(), !demo.isEncoded() ? "" : Integer.toString(demo.getCodifiedId()), demo.isEncoded() ? "" : demo.getNotCodifiedValue().replace("/", "-"), demo.isEncoded() ? "1" : "0"))
                    .filter(string -> !string.isEmpty())
                    .collect(Collectors.toList()));
        }
        return demos.stream()
                .collect(Collectors.joining("/"));
    }

    private String orderDemographicASTM(Order order)
    {
        //Agregar demograficos por orden
        List<String> demos = new ArrayList<>();
        demos.addAll(order.getDemographics().stream()
                .map(demo -> buildDemographic(Integer.toString(demo.getIdDemographic()), demo.getDemographic(), !demo.isEncoded() ? "" : demo.getCodifiedId() == null ? "" : Integer.toString(demo.getCodifiedId()), demo.isEncoded() ? "" : demo.getNotCodifiedValue(), demo.isEncoded() ? "1" : "0"))
                .filter(string -> !string.isEmpty())
                .collect(Collectors.toList()));
        return demos.stream()
                .collect(Collectors.joining("/"));
    }

    private String buildDemographic(String id, String name, String item, String itemname, String type)
    {
        //Construye el demografico
        if (name != null && !name.trim().isEmpty())
        {
            if (type.equals("1") && !item.isEmpty() || type.equals("0") && itemname != null)
            {
                return new StringBuilder(id).append("^").append(name).append("^").append(itemname == null ? "" : itemname.replace("/", "-")).append("^").append(item).append("^").append(type).toString();
            }
        }
        return "";
    }

    @Override
    public boolean testASTM(MiddlewareUrl url) throws Exception
    {
        try
        {
            final String urlLIS = url.getUrl() + "/integration/tester";
            final HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            int status = restTemplate.exchange(urlLIS, HttpMethod.GET, entity, Void.class).getStatusCodeValue();
            return status == 200 || status == 204;
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            MiddlewareLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }

    /**
     * Se envía mensaje con órdenes al Middleware
     *
     * @param json Lista de
     * net.​cltech.​enterprisent.​domain.​integration.​middleware.MiddlewareMessage
     * @param url
     * @return
     * @throws Exception
     */
    @Override
    public boolean sendMiddleware(List<MiddlewareMessage> json, String url) throws Exception
    {
        try
        {
            MiddlewareLog.info("mensaje:  " +Tools.jsonObject(json));
            final String urlLIS = url + "/integration/insertOrUpdateOrder";
            final HttpHeaders headers = new HttpHeaders();
            final HttpEntity<List<MiddlewareMessage>> httpEntity = new HttpEntity<>(json, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(urlLIS, HttpMethod.POST, httpEntity, String.class);
            MiddlewareLog.info("mensaje enviado al middleware");
            return responseEntity.getStatusCodeValue() == 200 || responseEntity.getStatusCodeValue() == 202 || responseEntity.getStatusCodeValue() == 204;

        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            MiddlewareLog.error(ex);
            throw new EnterpriseNTException(Arrays.asList("URL not found " + url));
        }
    }

    /**
     * Se envía mensaje con órdenes al Middleware
     *
     * @param order
     * @param branch
     * @param retake indica si es una retoma
     * @return
     * @throws Exception
     */
    @Override
    public boolean sendTestDeleteMiddleware(Order order, int branch, Boolean retake) throws Exception
    {

        MiddlewareLog.info("entro por sendTestDeleteMiddleware ");
        
        String action = retake ? "R" : "C";
        order.getDeleteTests().stream().map(testToDelete -> testToDelete.setAction(action)).collect(Collectors.toList());
        order.setTests(order.getDeleteTests().stream().filter(testToDelete -> testToDelete.getSampleState() == LISEnum.ResultSampleState.CHECKED.getValue()).collect(Collectors.toList()));
        
        int groupType = order.getType().getCode().equals("S") ? 1 : 2;
        for (Test t : order.getTests())
        {
            t = orderService.saveTest(t, branch, groupType);
        }
        
        if (buildListMessagesTest(order.getTests(), Constants.ANY, order, null) > 0)
        {
            return true;
        } else
        {
            return false;
        }
    }

    @Override
    public int resend(ResultFilter resultFilter) throws Exception
    {
        int sendMessages = 0;
        try
        {
            String laboratorys = resultFilter.getLaboratorys().stream().map((l) -> l.getId().toString()).collect(Collectors.joining(","));
            if (resultFilter.getLaboratorys().size() > 0 && !laboratorys.equals(""))
            {
                SEPARATOR = configurationService.getValue("SeparadorMiddleware");
                ORDERDIGITS = Integer.parseInt(configurationService.getValue("DigitoAño"));
                List<Demographic> demos = demographicService.list().stream().filter((Demographic t) -> t.getOrigin().equals("O") && t.isState()).collect(Collectors.toList());
                List<Order> orders = resultService.orderResultRange(resultFilter, demos, "A", laboratorys);
                if (orders.size() > 0)
                {
                    sendMessages = buildListMessagesOrder(orders, Constants.ANY, null);
                    MiddlewareLog.info("entro por resend ");
                } else
                {
                    MiddlewareLog.error("No hay ordenes que enviar al middleware");
                }
            } else
            {
                MiddlewareLog.error("No hay laboratorios con middleware registrado");
            }
            return sendMessages;
        } catch (Exception ex)
        {
            MiddlewareLog.error(ex);
            return 0;
        }
    }

    private boolean validate(int origin, Test test)
    {
        boolean state = false;
        MiddlewareLog.info("origen " + origin + " examen " + test.getAbbr() + " laboratorio ENTRY " + test.getLaboratory().isEntry() + " laboratorio isCheck " + test.getLaboratory().isCheck());
        if (origin == Constants.ANY)
        {
            if (test.getLaboratory().isEntry() == true && test.getTestState() < LISEnum.ResultTestState.VALIDATED.getValue())
            {
                state = true;
            } else if (test.getLaboratory().isCheck() == true)
            {
                state = true;
            }
        } else if (origin == Constants.ENTRY && test.getLaboratory().isEntry() == true && test.getTestState() < LISEnum.ResultTestState.VALIDATED.getValue())
        {
            state = true;
        } else if (origin == Constants.CHECK && test.getLaboratory().isCheck() == true && test.getTestState() <= LISEnum.ResultTestState.VALIDATED.getValue())
        {
            state = true;
        } else if (origin == Constants.MICROBIOLOGY)
        {
            state = true;
        }
        return state;
    }

    /**
     * Muestras para la importacion al middleware
     *
     * @return si la muestra es de los tipos enviados
     * @throws java.lang.Exception
     */
    @Override
    public List<SampleMiddleware> list() throws Exception
    {
        return middlewareDao.list();
    }

    /**
     * Muestras para la importacion al middleware
     *
     * @return si la muestra es de los tipos enviados
     * @throws java.lang.Exception
     */
    @Override
    public List<TestMiddleware> listTest() throws Exception
    {
        return middlewareDao.listTest();
    }

    /**
     * Lista para la importaciÃ³n de antibiÃ³ticos al Middleware
     *
     * @return lista de antibioticos
     * @throws Exception
     */
    @Override
    public List<AntibioticMiddleware> listAntibiotics() throws Exception
    {
        return middlewareDao.listAntibiotics();
    }

    /**
     * Lista todos los demograficos de la aplicacion para la importacion desde
     * el middleware
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<DemographicMiddleware> listDemographicAll() throws Exception
    {
        List<DemographicMiddleware> demographics = new ArrayList<>();
        if (configurationDao.get("ManejoCliente").getValue().equalsIgnoreCase("true"))
        {
            demographics.add(new DemographicMiddleware(Constants.ACCOUNT, "CLIENTE", "O", true, true));
        }
        if (configurationDao.get("ManejoMedico").getValue().equalsIgnoreCase("true"))
        {
            demographics.add(new DemographicMiddleware(Constants.PHYSICIAN, "MEDICO", "O", true, true));
        }
        if (configurationDao.get("ManejoTarifa").getValue().equalsIgnoreCase("true"))
        {
            demographics.add(new DemographicMiddleware(Constants.RATE, "TARIFA", "O", true, true));
        }
        demographics.add(new DemographicMiddleware(Constants.BRANCH, "SEDE", "O", true, true));
        if (configurationDao.get("ManejoServicio").getValue().equalsIgnoreCase("true"))
        {
            demographics.add(new DemographicMiddleware(Constants.SERVICE, "SERVICIO", "O", true, true));
        }
        if (configurationDao.get("ManejoRaza").getValue().equalsIgnoreCase("true"))
        {
            demographics.add(new DemographicMiddleware(Constants.RACE, "RAZA", "H", true, true));
        }
        demographics.add(new DemographicMiddleware(Constants.ORDERTYPE, "TIPO ORDEN", "O", true, true));
        if (configurationDao.get("ManejoTipoDocumento").getValue().equalsIgnoreCase("true"))
        {
            demographics.add(new DemographicMiddleware(Constants.DOCUMENT_TYPE, "TIPO DE DOCUMENTO", "H", true, true));
        }
        demographics.add(new DemographicMiddleware(Constants.ORDER_HIS, "ORDEN HIS", "O", false, true));
        demographics.add(new DemographicMiddleware(Constants.PATIENT_EMAIL, "EMAIL", "H", false, true));
        demographics.addAll(list(true));
        return demographics;
    }

    public List<DemographicMiddleware> list(boolean state) throws Exception
    {
        return new ArrayList<>(demographicDao.listMiddleware());
    }

    /**
     * Lista para la importaciÃ³n de sitios anatomicos al middleware
     *
     * @throws Exception
     */
    @Override
    public List<AnatomicalSiteMiddleware> listAnatomicalSite() throws Exception
    {
        return middlewareDao.listAnatomicalSite();
    }

    /**
     * Lista para la importaciÃ³n de microorganismos al middleware
     *
     * @throws Exception
     */
    @Override
    public List<MicroorganismsMiddleware> listMicroorganisms() throws Exception
    {
        return middlewareDao.listMicroorganisms();
    }

    /**
     * Lista para la importaciÃ³n de demogrÃ¡ficos Ã­tem al Middleware
     */
    @Override
    public List<DemographicItemMiddleware> listDemographicItem(Integer demographic) throws Exception
    {
        List<DemographicItemMiddleware> listDemographicItem = new ArrayList<>();
        if (demographic >= 0)
        {
            listDemographicItem = middlewareDao.listDemographicItem_default(demographic);
        } else
        {
            if (demographic == Constants.ACCOUNT)
            {
                listDemographicItem = middlewareDao.listDemographicItem_ACCOUNT();
            }
            if (demographic == Constants.PHYSICIAN)
            {
                listDemographicItem = middlewareDao.listDemographicItem_PHYSICIAN();
            }
            if (demographic == Constants.RATE)
            {
                listDemographicItem = middlewareDao.listDemographicItem_RATE();
            }
            if (demographic == Constants.ORDERTYPE)
            {
                listDemographicItem = middlewareDao.listDemographicItem_ORDERTYPE();
            }
            if (demographic == Constants.BRANCH)
            {
                listDemographicItem = middlewareDao.listDemographicItem_BRANCH();
            }
            if (demographic == Constants.SERVICE)
            {
                listDemographicItem = middlewareDao.listDemographicItem_SERVICE();
            }
            if (demographic == Constants.RACE)
            {
                listDemographicItem = middlewareDao.listDemographicItem_RACE();
            }
            if (demographic == Constants.DOCUMENT_TYPE)
            {
                listDemographicItem = middlewareDao.listDemographicItem_DOCUMENT_TYPE();
            }
            if (demographic == Constants.AGE_GROUP)
            {
                listDemographicItem = middlewareDao.listDemographicItem_AGE_GROUP();
            }
        }
        return listDemographicItem;
    }

    @Override
    public boolean middlewareResults(List<MiddlewareMessage> message) throws Exception
    {
        AuthorizedUser session = new AuthorizedUser();
        ResultTest result = map(message, session);
        return result.getOrder() > 0;
    }

    private ResultTest map(List<MiddlewareMessage> message, AuthorizedUser session) throws Exception
    {
        try
        {
            ResultTest result = new ResultTest();
            Antibiotic antibiotic = null;
            Microorganism microorganismC = new Microorganism();
            ResultMicrobiology resultMicrobiology = null;
            ResultTestComment resultComment = new ResultTestComment();
            List<Microorganism> microList = new ArrayList();
            List<Antibiotic> antibiotics = new ArrayList();
            Microorganism microorganismtempcoment = new Microorganism();
            net.cltech.enterprisent.domain.masters.test.Test test = new net.cltech.enterprisent.domain.masters.test.Test();
            String[] segments = null;
            String[] fields = null;
            // Ayudara a identificar que tipo de modificación debe realizace sobre el comentario
            String modifyComment = "";
            int flag = 0; // 0 sin microbiologia 1 con microbiologia
            int flagResultComment = 0;
            int count = 0;
            String resultDilution = "";
            int countresultorder = 0;

            // Variables de validacion del resultado
            String validResult = "";
            String preValidResult = "";
            String resultAnt = "";
            boolean orderMicro = false;
            boolean deletecommentresult = false;
            

            List<ImageTest> listimageTests = new ArrayList();

            for (MiddlewareMessage middlewareMessage : message)
            {
                segments = middlewareMessage.getMessage().split("" + (char) 13 + (char) 10);
                for (String segment : segments)
                {
                    count++;
                    fields = segment.split("\\|");

                    if (!"C".equals(fields[0]) && microorganismtempcoment.getId() != null)
                    {
                        microbiologyDao.updateCommentMicrobialDetection(microorganismtempcoment, result.getOrder());
                    }

                    switch (fields[0])
                    {
                        case "H":
                            //Extrae datos de la cabecera
                            break;
                        case "P":
                            //Extrae datos del paciente
                            break;
                        case "O":
                            //Extrae datos de la orden
                            String order;
                            flag = 0;
                            Integer digits = configurationService.getIntValue("DigitoAño");
                            Integer digitsOrder = configurationService.getIntValue("DigitosOrden");
                            if (segment.split("\\|")[2].contains("-"))
                            {
                                order = segment.split("\\|")[2].split("-")[0];
                            } else if (segment.split("\\|")[2].contains("."))
                            {
                                order = segment.split("\\|")[2].split("\\.")[0];
                            } else
                            {
                                order = segment.split("\\|")[2];
                                order = order.substring(0, digits + 4 + digitsOrder).trim();
                            }

                            result.setOrder(Long.parseLong(Tools.getOrderNumberYear(order, digits)));
                            result.setMicro(segment.split("\\|")[3].equals("1"));
                            orderMicro = segment.split("\\|")[3].equals("1");
                            deletecommentresult = segment.split("\\|")[8].equals("D");
                            MiddlewareLog.info("Indicador de borrado" + deletecommentresult);
                            if (result.isMicro())
                            {
                                result.setTestCode(segment.split("\\|")[4].split("\\^")[3].trim());
                                test = testService.get(null, result.getTestCode(), null, null);
                            }
                            break;
                        case "C":
                            //Extrae datos del comentario
                            if (flag == 0 && flagResultComment == 0)
                            {
                                resultComment.setCommentChanged(true);
                                resultComment.setComment("{\"content\":\"" + (segment.split("\\|")[3]).replaceAll("\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;") + "\",\"checklist\":[]}");

                            } else if (flag == 1 && flagResultComment == 0)
                            {
                                if (segment.split("\\|").length == 4)
                                {
                                    microorganismtempcoment.setComment((segment.split("\\|")[3]).replaceAll("\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
                                    microorganismC.setComment((segment.split("\\|")[3]).replaceAll("\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
                                } else if (segment.split("\\|").length == 5)
                                {
                                    int opcion = Integer.parseInt(segment.split("\\|")[4]);

                                    switch (opcion)
                                    {
                                        case 1:
                                        {
                                            microorganismtempcoment.setRecount((segment.split("\\|")[3]).replaceAll("\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
                                            break;
                                        }
                                        case 3:
                                        {
                                            microorganismtempcoment.setComplementations((segment.split("\\|")[3]).replaceAll("\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
                                            break;
                                        }
                                    }
                                }
                            }

                            if (flagResultComment == 1)
                            {
                                ResultTest resultC = new ResultTest();
                                //insertar comentario del resultado.
                                if ("F".equals(modifyComment))
                                {
                                    resultC.setOrder(result.getOrder());
                                    resultC.setTestId(test.getId());

                                    ResultTestComment resultCommentTwo = resultService.getCommentResultTests(result.getOrder(), test.getId());
                                    if (resultCommentTwo.getComment() == null)
                                    {
                                        resultC.setHasComment(false);
                                    } else
                                    {
                                        resultC.setHasComment(true);
                                    }

                                    resultCommentTwo.setComment((segment.split("\\|")[3]).replaceAll("\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
                                    resultCommentTwo.setCommentChanged(true);
                                    resultC.setResultChanged(false);

                                    resultC.setNewState(result.getState());
                                    //resultC.setNewState(2);
                                    resultC.setResult(resultCommentTwo.getResult());
                                    // Registramos en auditoria la trazabilidad del comentario
                                    resultC.setResultComment(resultCommentTwo);
                                    resultService.reportedTest(resultC, session.getId());
                                } else if ("P".equals(modifyComment))
                                {
                                    resultC.setOrder(result.getOrder());
                                    resultC.setTestId(test.getId());

                                    ResultTestComment resultCommentTwo = resultService.getCommentResultTests(result.getOrder(), test.getId());

                                    if (resultCommentTwo.getComment() == null)
                                    {
                                        resultC.setHasComment(false);
                                    } else
                                    {
                                        resultC.setHasComment(true);
                                    }

                                    String oldComment = resultCommentTwo.getComment() == null ? "" : resultCommentTwo.getComment();
                                    String newComment = oldComment + "<br>" + segment.split("\\|")[3];

                                    // Adicionamos al objeto el comentario para ese resultado
                                    resultCommentTwo.setComment(newComment.replaceAll("\n", "<br>").replaceAll("\t", ":&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
                                    resultCommentTwo.setCommentChanged(true);
                                    resultC.setResultChanged(false);

                                    resultC.setNewState(result.getState());
                                    //resultC.setNewState(2);
                                    resultC.setResult(resultCommentTwo.getResult());
                                    // Registramos en auditoria la trazabilidad del comentario
                                    resultC.setResultComment(resultCommentTwo);

                                    resultService.reportedTest(resultC, session.getId());
                                }
                            }

                            if (preValidResult.equals("1"))
                            {
                                resultService.prevalidatedByTest(result.getOrder(), test.getId(), session.getId());
                            } else if (validResult.equals("1"))
                            {
                                resultService.validatedTest(result.getOrder(), test.getId(), result.getUserId());
                            }
                            break;
                        case "R":
                            //Extrae datos del resultado
                            flagResultComment = 0;
                            User user = new User();
                            user = userService.get(null, segment.split("\\|")[13].trim().toUpperCase(), null, null);
                            boolean dilution;
                            if (!segment.split("\\|")[4].isEmpty())
                            {
                                if (segment.split("\\|")[4].split("\\^").length > 1)
                                {
                                    String validdD = segment.split("\\|")[4].split("\\^")[1];

                                    if (validdD.equals("1"))
                                    {
                                        dilution = true;
                                    } else
                                    {
                                        dilution = false;
                                    }
                                } else
                                {
                                    dilution = false;
                                }
                            } else
                            {
                                dilution = false;
                            }
                            // Obtenemos una orden para de este objeto tomar el objeto paciente
                            Order orderNTCaseR = orderService.get(result.getOrder());
                            session.setBranch(orderNTCaseR.getBranch().getId());
                            session.setId(user.getId());
                            session.setUserName(user.getUserName());
                            session.setConfidential(user.isConfidential());
                            session.setName(user.getName());
                            session.setLastName(user.getLastName());

                            modifyComment = segment.split("\\|")[8].trim();
                            if (user != null)
                            {
                                if (resultComment.getCommentChanged() == true && countresultorder == 0)
                                {
                                    String comment = resultComment.getComment();
                                    List<CommentOrder> commentOrder = new ArrayList<>();
                                    CommentOrder commentO = new CommentOrder();
                                    commentO.setType((short) 1);
                                    commentO.setIdRecord(result.getOrder());
                                    commentO.setState((short) 1);
                                    commentO.setComment(comment.replaceAll("\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
                                    commentO.setPrint(true);
                                    commentO.setUser(session);
                                    commentOrder.add(commentO);
                                    commentService.commentOrder(commentOrder);
                                    countresultorder = countresultorder + 1;

                                }

                                result.setTestCode(segment.split("\\|")[2].split("\\^")[3].trim());

                                ResultFilter resultFilter = new ResultFilter();
                                resultFilter.setFilterId(1);
                                resultFilter.setFirstOrder(result.getOrder());
                                resultFilter.setLastOrder(result.getOrder());
                                resultFilter.setUserId(user.getId());

                                List<ResultTest> resultTestList = resultTestDao.list(result.getOrder());

                                Microorganism microorganism = new Microorganism();
                                microorganism.setName(segment.split("\\|")[7].trim());
                                if (microorganismC != null)
                                {
                                    microorganism.setComment(microorganismC.getComment());
                                }
                                if (orderMicro && !microorganism.getName().isEmpty())
                                {
                                    flag = 1;

                                    microorganism.setId(microorganismService.findByName(microorganism.getName()).getId());

                                    if (microorganismtempcoment.getId() != null)
                                    {
                                        microorganism.setComment(microorganismtempcoment.getComment());
                                        microorganism.setRecount(microorganismtempcoment.getRecount());
//                                        microorganism.setDeterminations(microorganismtempcoment.getDeterminations());
                                        microorganism.setComplementations(microorganismtempcoment.getComplementations());
                                    }

                                    MicrobialDetection microbialDetection = new MicrobialDetection();
                                    microbialDetection.setUser(session);
                                    microbialDetection.setOrder(result.getOrder());
                                    microbialDetection.setTest(test.getId());

                                    List<ResultMicrobiology> listResultMicrobiology = new ArrayList<>(0);

                                    antibiotic = new Antibiotic();
                                    antibiotic.setName(segment.split("\\|")[2].split("\\^")[3].trim());
                                    antibiotic.setId(antibioticService.findByName(antibiotic.getName()).getId());
                                    antibiotics.add(antibiotic);

                                    resultMicrobiology = new ResultMicrobiology();
                                    resultMicrobiology.setInterpretationCMI(segment.split("\\|")[3].split("\\^").length > 1 ? segment.split("\\|")[3].split("\\^")[0].trim() : "");
                                    resultMicrobiology.setCmi(segment.split("\\|")[3].split("\\^").length > 1 ? segment.split("\\|")[3].split("\\^")[1].trim() : "");
                                    resultMicrobiology.setSelected(true);
                                    resultMicrobiology.setIdAntibiotic(antibiotic.getId());
                                    resultMicrobiology.setUserCMI(session);

                                    Sensitivity sensitivity = microorganismService.getSensitivity(microorganism.getId(), test.getId());
                                    if (sensitivity == null)
                                    {
                                        sensitivity = microorganismService.getSensitivity(microorganism.getId(), null);
                                        if (sensitivity != null)
                                        {
                                            microorganism.setSensitivity(sensitivity);
                                        }
                                    } else
                                    {
                                        microorganism.setSensitivity(sensitivity);
                                    }

                                    resultMicrobiology.setIdSensitivity(sensitivity.getId());

                                    listResultMicrobiology.add(resultMicrobiology);

                                    microorganism.setResultsMicrobiology(listResultMicrobiology);
                                    microorganism.setAntibiotics(antibiotics);
                                    microorganism.setSelected(true);
                                    if (!microList.contains(microorganism))
                                    {
                                        microList.add(microorganism);
                                    }

                                    microbialDetection.setMicroorganisms(microList);
                                    microbiologyService.insertMicrobialDetection(microbialDetection, session);

                                    Microorganism detectionUpdate = microbiologyService.getMicrobialDetectionMicroorganism(microbialDetection.getOrder(), microbialDetection.getTest(), microorganism.getId());

                                    microorganism.setIdMicrobialDetection(detectionUpdate.getIdMicrobialDetection());
                                    microbiologyService.insertResultMicrobiologySensitivity(microorganism, session, microbialDetection.getOrder());
                                    result = (resultTestList.stream().filter(o -> o.getTestId() == microbialDetection.getTest()).collect(Collectors.toList())).get(0);

                                    microorganismtempcoment = microorganism;
                                    validResult = segment.split("\\|")[14].trim();
                                    preValidResult = segment.split("\\|")[15].trim();
                                    break;
                                } else
                                {
                                    test = testService.get(null, result.getTestCode(), null, null);
                                    int testid = test.getId();

                                    if (modifyComment.equals("I"))
                                    {
                                        ImageTest imageTest = new ImageTest();
                                        long ordernumber = result.getOrder();
                                        List<ImageTest> testimagefind = listimageTests.stream().filter((ImageTest testt) -> testt.getOrder() == ordernumber && testt.getTestId() == testid).collect(Collectors.toList());

                                        if (testimagefind.size() > 0)
                                        {
                                            imageTest = testimagefind.get(0);
                                            imageTest.setCount(imageTest.getCount() + 1);
                                            listimageTests = listimageTests.stream().filter((ImageTest testt) -> testt.getOrder() != ordernumber && testt.getTestId() != testid).collect(Collectors.toList());
                                        }

                                        //Imagen de analizador
                                        String imageName = segment.split("\\|")[3].split("\\^")[0].trim();
                                        String image = segment.split("\\|")[3].split("\\^")[1].trim();

                                        //TO DO: Determinar decompresión de imagen
                                        imageTest.setOrder(result.getOrder());
                                        imageTest.setTestId(testid);

                                        switch (imageTest.getCount())
                                        {
                                            case 0:
                                                imageTest.setImageName1(imageName);
                                                imageTest.setImage1(image);
                                                break;
                                            case 1:
                                                imageTest.setImageName2(imageName);
                                                imageTest.setImage2(image);
                                                break;
                                            case 2:
                                                imageTest.setImageName3(imageName);
                                                imageTest.setImage3(image);
                                                break;
                                            case 3:
                                                imageTest.setImageName4(imageName);
                                                imageTest.setImage4(image);
                                                break;
                                            case 4:
                                                imageTest.setImageName5(imageName);
                                                imageTest.setImage5(image);
                                                break;
                                        }
                                        listimageTests.add(imageTest);
                                        break;
                                    }

                                    result = (resultTestList.stream().filter(o -> o.getTestId() == testid).collect(Collectors.toList())).get(0);

                                    result.setTestId(test.getId());
                                    result.setTestCode(segment.split("\\|")[2].split("\\^")[3].trim());

                                    if (!segment.split("\\|")[3].trim().equals(result.getResult()))
                                    {
                                        if (result.getState() < LISEnum.ResultTestState.REPORTED.getValue()  || result.getState() == LISEnum.ResultTestState.PREVIEW.getValue() )
                                        {
                                            if (test.getResultType() == LISEnum.ResultTestResultType.NUMERIC.getValue())
                                            {

                                                if (isNumeric(segment.split("\\|")[3].split("\\^")[0].trim()) == true)
                                                {
                                                    BigDecimal result2 = new BigDecimal(segment.split("\\|")[3].split("\\^")[0].trim());
                                                    BigDecimal dec2 = result2.setScale(test.getDecimal(), RoundingMode.HALF_UP);
                                                    result.setResult(dec2.toString());
                                                } else
                                                {
                                                    result.setResult(segment.split("\\|")[3].split("\\^")[0].trim());
                                                }
                                            } else
                                            {
                                                result.setResult(segment.split("\\|")[3].split("\\^")[0].trim());
                                            }
                                        }
                                        result.setResultChanged(true);
                                    } else
                                    {
                                        result.setResultChanged(false);
                                    }
                                    result.setUserId(user.getId());
                                    result.setMicro(false);

                                    // Obtenemos todos los valores de referencia para un examen y un usuario analizador
                                    List<ReferenceValue> referenceValues = middlewareDao.referenceValuesByUserAnalyzer(test.getId(), user.getId());
                                    if (referenceValues.size() > 0)
                                    {
                                        // Obtenemos una orden para de este objeto tomar el objeto paciente
                                        Order orderNT = orderService.get(result.getOrder());
                                        ReferenceValue referenceValue = resultServiceEnterpriseNT.matchReferenceValue(orderNT.getPatient(), referenceValues);
                                        ResultTest obj = new ResultTest();
                                        obj.setResultComment(new ResultTestComment());
                                        obj.setResultChanged(true);
                                        obj.setResultType(resultDao.getTestTypeResult(test.getId()));
                                        obj.setPanicMin(referenceValue.getPanicMin());
                                        obj.setPanicMax(referenceValue.getPanicMax());
                                        obj.setPanicInterval(referenceValue.getPanicMin() == null ? null : (referenceValue.getPanicMin() + " " + referenceValue.getPanicMax()));
                                        obj.setRefInterval(referenceValue.getNormalMin() == null ? null : (referenceValue.getNormalMin() + " " + referenceValue.getNormalMax()));
                                        obj.setRefMin(referenceValue.getNormalMin());
                                        obj.setRefMax(referenceValue.getNormalMax());
                                        short critical = referenceValue.isCriticalCh() == true ? (short) 1 : (short) 0;
                                        obj.setCritic(critical);
                                        obj.setPanicLiteral(referenceValue.getPanic().getName());
                                        obj.setRefLiteral(referenceValue.getNormal().getName());
                                        obj.setResult(result.getResult());

                                        int patology = resultServiceEnterpriseNT.validatePathology(obj);
                                        // Obtenemos el primero de los valores de referencia con adicion de otros ciertos filtros

                                        resultDao.updateReferenceValue(result.getOrder(), test.getId(), referenceValue, patology);
                                    }

                                    if (result.getSampleState() != LISEnum.ResultSampleState.CHECKED.getValue())
                                    {
                                        boolean sample = sampleTrackingService.sampleTracking(result.getOrder(), result.getSampleCode(), LISEnum.ResultSampleState.CHECKED.getValue(), session);
                                    }

                                    if (configurationService.getValue("Trazabilidad").equals("3"))
                                    {
                                        verificDestinationTest(result, user, test.getSample().getCodesample(), testid);
                                    }

                                    if (dilution == true)
                                    {
                                        ResultTestComment resultCommentTwo = resultService.getCommentResultTests(result.getOrder(), test.getId());

                                        Long orderValid = resultCommentTwo.getOrder();
                                        if (orderValid.equals(result.getOrder()) && (resultCommentTwo.getComment() != null))
                                        {
                                            result.setHasComment(true);
                                        } else
                                        {
                                            result.setHasComment(false);
                                        }

                                        String oldComment = resultCommentTwo.getComment() == null ? "" : resultCommentTwo.getComment();
                                        String newComment = oldComment + "<br>" + " Resultado previo: " + segment.split("\\|")[3].split("\\^")[1].trim() + "<br>";

                                        // Adicionamos al objeto el comentario para ese resultado
                                        resultCommentTwo.setComment(newComment.replaceAll("\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"));
                                        resultCommentTwo.setCommentChanged(true);
                                        // Registramos en auditoria la trazabilidad del comentario
                                        result.setResultComment(resultCommentTwo);
                                    }
                                    
                                     String nextInitialSegment = "";
                                    if (count < segments.length)
                                    {
                                        nextInitialSegment = segments[count].split("\\|")[0].trim();
                                    }

                                    if (result.getState() < LISEnum.ResultTestState.REPORTED.getValue())
                                    {
                                        result.setDilution(dilution);
                                        result.setResultChanged(true);
                                        // Se adiciona el dato de dilución
                                        result.setNewState(LISEnum.ResultTestState.REPORTED.getValue());
                                        resultService.reportedTest(result, result.getUserId());
                                    } else if ((result.getState() == LISEnum.ResultTestState.REPORTED.getValue() && result.getResultChanged()) || result.getState() == LISEnum.ResultTestState.PREVIEW.getValue()) 
                                    {

                                        result.setNewState(LISEnum.ResultTestState.RERUN.getValue());

                                        ResultTestRepetition resultTestRepetition = new ResultTestRepetition();
                                        resultTestRepetition.setType('R');
                                        resultTestRepetition.setReasonId(7);
                                        result.setResultRepetition(resultTestRepetition);
                                        resultService.reportedTest(result, result.getUserId());

                                        //ResultTest newResult = map(message, session);
                                        result.setResultRepetition(null);
                                        result.setDilution(dilution);
                                        result.setNewState(LISEnum.ResultTestState.REPORTED.getValue());

                                        if (test.getResultType() == LISEnum.ResultTestResultType.NUMERIC.getValue())
                                        {
                                            if (isNumeric(segment.split("\\|")[3].split("\\^")[0].trim()) == true)
                                            {
                                                BigDecimal result2 = new BigDecimal(segment.split("\\|")[3].split("\\^")[0].trim());
                                                BigDecimal dec2 = result2.setScale(test.getDecimal(), RoundingMode.HALF_UP);
                                                result.setResult(dec2.toString());
                                            } else
                                            {
                                                result.setResult(segment.split("\\|")[3].split("\\^")[0].trim());
                                            }
                                        } else
                                        {
                                            result.setResult(segment.split("\\|")[3].split("\\^")[0].trim());
                                        }

                                        resultService.reportedTest(result, result.getUserId());
                                    }
                                   
                                    
                                   

                                    if (!nextInitialSegment.equalsIgnoreCase("C"))
                                    {
                                        if (segment.split("\\|").length >= 16)
                                        {
                                            if ("1".equals(segment.split("\\|")[15].trim()))
                                            {
                                                ResultTestValidate resultTestValidate = new ResultTestValidate();
                                                Item itemOne = new Item();
                                                resultTestValidate.setFinalValidate(false);
                                                resultTestValidate.setOrderId(result.getOrder());
                                                List<ResultTest> listResultTest = new ArrayList<>();
                                                listResultTest = resultTestDao.listByIdTest(result.getOrder(), testid);
                                                //listResultTest.get(0).setNewState(3);
                                                listResultTest.get(0).setNewState(result.getState() > 0 ? result.getState() : 3);
                                                listResultTest.get(0).setGrantValidate(true);
                                                resultTestValidate.setTests(listResultTest);
                                                itemOne = resultTestDao.getGenderTest(testid);
                                                resultTestValidate.setSex(itemOne);

                                                resultServiceEnterpriseNT.validatedTests(resultTestValidate, session, false);
                                            } else if ("1".equals(segment.split("\\|")[14].trim()))
                                            {
                                                resultService.validatedTest(result.getOrder(), testid, result.getUserId());
                                            }
                                        } else if (segment.split("\\|").length >= 14)
                                        {
                                            if ("1".equals(segment.split("\\|")[14].trim()))
                                            {
                                                resultService.validatedTest(result.getOrder(), testid, result.getUserId());
                                            }
                                        }
                                    } else
                                    {
                                        validResult = segment.split("\\|")[14].trim();
                                        preValidResult = segment.split("\\|")[15].trim();
                                        resultAnt = segment.split("\\|")[3].trim();
                                    }

                                    flagResultComment = 1;
                                    break;
                                }

                            } else
                            {
                                return null;
                            }
                    }

                }

                for (int i = 0; i < listimageTests.size(); i++)
                {
                    try
                    {
                        resultService.middlewareImages(listimageTests.get(i));
                    } catch (Exception ex)
                    {
                        Logger.getLogger(IntegrationMiddlewareServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                MiddlewareLog.info("Borrado de comentario" + deletecommentresult);
                if(deletecommentresult){                    
                    resultTestDao.deleteResultComment(result.getOrder(), test.getId());
                }
               
                if (preValidResult.equals("1") && orderMicro)
                {
                    resultService.prevalidatedByTest(result.getOrder(), test.getId(), session.getId());
                } else if (validResult.equals("1") && orderMicro)
                {
                    resultService.validatedTest(result.getOrder(), test.getId(), session.getId());
                }
                
            }
            result.setResultComment(resultComment);
            return result;
        } catch (Exception e)
        {
            MiddlewareLog.error(e);
            MiddlewareLog.error(e.getMessage());
            return new ResultTest();
        }

    }

    public static boolean isNumeric(String cadena)
    {

        boolean resultado;

        try
        {
            Float.parseFloat(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion)
        {
            try
            {
                Integer.parseInt(cadena);
                resultado = true;
            } catch (NumberFormatException excepcione)
            {
                resultado = false;
            }
        }

        return resultado;
    }

    public void verificDestinationTest(ResultTest result, User user, String sampleCode, Integer testid) throws Exception
    {

        // Verificación de la muestra para el destino que tiene asociado ese usuario analizado
        Order createdOrder = orderService.get(result.getOrder());

        if (result.getSampleState() != LISEnum.ResultSampleState.CHECKED.getValue())
        {
            //sampleTrackingService.sampleTrackingAnalyzer(result.getOrder(), sampleCode, LISEnum.ResultSampleState.CHECKED.getValue(), null, false, createdOrder.getBranch().getId(), user);
        }

        AssignmentDestination assigment = sampleTrackingService.getDestinationRouteAnalyzer(createdOrder.getOrderNumber(), sampleCode, createdOrder.getBranch().getId(), user);

        if(assigment != null){
            List<DestinationRoute> destinationFilter = assigment.getDestinationRoutes().stream()
                    .filter(destination -> Objects.equals(destination.getDestination().getId(), user.getDestination().getId()) && destination.isVerify() == false)
                    .collect(Collectors.toList());

            if (destinationFilter.size() > 0)
            {

                List<TestBasic> testFilter = destinationFilter.get(0).getTests().stream()
                        .filter(test -> Objects.equals(test.getId(), testid))
                        .collect(Collectors.toList());

                if (testFilter.size() > 0)
                {

                    VerifyDestination verify = new VerifyDestination();
                    verify.setApproved(true);
                    verify.setBranch(createdOrder.getBranch().getId());
                    verify.setDestination(destinationFilter.get(0).getId());
                    verify.setOrder(result.getOrder());
                    verify.setSample(sampleCode);

                    sampleTrackingService.verifyDestinationAnalyzer(verify, user);
                }
            }
        }
    }

    /**
     * Obtiene una lista de examenes con valores de referencia para ser envaidos
     * al middleware
     *
     * @return Lista de examenes para el middleware
     * @throws Exception Error en el servicio
     */
    @Override
    public List<TestToMiddleware> deltacheck() throws Exception
    {
        try
        {
            List<TestToMiddleware> listTests = middlewareDao.deltacheck();
            List<ReferenceValueMiddleware> listReferenceValue;
            for (TestToMiddleware test : listTests)
            {
                List<ReferenceValueMiddleware> defaultReferenceValues = new ArrayList<>();
                List<ReferenceValueMiddleware> analyzerReferenceValues = new ArrayList<>();
                listReferenceValue = middlewareDao.referenceValueToMiddleware(test.getId());
                if (!listReferenceValue.isEmpty())
                {
                    for (ReferenceValueMiddleware referenceValueMiddleware : listReferenceValue)
                    {
                        Integer analyzerUserId = middlewareDao.getAnalyzerUserId(referenceValueMiddleware.getId());
                        if (analyzerUserId == 0)
                        {
                            defaultReferenceValues.add(referenceValueMiddleware);
                        } else
                        {
                            analyzerReferenceValues.add(referenceValueMiddleware);
                        }
                    }

                    if (!defaultReferenceValues.isEmpty())
                    {
                        test.setReferenceValues(defaultReferenceValues);
                    }
                    if (!analyzerReferenceValues.isEmpty())
                    {
                        test.setAnalyzerReferenceValues(analyzerReferenceValues);
                    }
                }
            }

            // Eliminar examenes que no tengan valores de referencia:
            for (int i = 0; i < listTests.size(); i++)
            {
                if (listTests.get(i).getReferenceValues() == null && listTests.get(i).getAnalyzerReferenceValues() == null)
                {
                    listTests.remove(i);
                    i--;
                }
            }
            return listTests;
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public DestinationCovered covered(String order, String samplecode) throws Exception
    {
        AuthorizedUser authorizedUser = JWT.decode(request);

        DestinationCovered bean = new DestinationCovered();
        User user = userService.get(authorizedUser.getId(), null, null, null);
        ORDERDIGITS = Integer.parseInt(configurationService.getValue("DigitoAño"));
        // Verificación de la muestra para el destino que tiene asociado ese usuario analizado
        String ordercomplete = String.valueOf(order);
        Order createdOrder = orderService.get(Long.parseLong(Tools.getOrderNumberYear(ordercomplete, ORDERDIGITS)));
        if (createdOrder == null)
        {
            throw new EnterpriseNTException(Arrays.asList("2|order not found"));
        }

        AssignmentDestination assigment = sampleTrackingService.getDestinationRouteAnalyzer(createdOrder.getOrderNumber(), samplecode, createdOrder.getBranch().getId(), user);
        if (assigment == null)
        {
            throw new EnterpriseNTException(Arrays.asList("1|Route not configured"));
        }

        for (int i = 0; i < assigment.getDestinationRoutes().size(); i++)
        {
            if (assigment.getDestinationRoutes().get(i).isVerify() == false)
            {
                bean.setDestinationNext(assigment.getDestinationRoutes().get(i).getDestination().getCode());
                break;
            }
        }

        if (bean.getDestinationNext() == null)
        {
            throw new EnterpriseNTException(Arrays.asList("2|sample verific all destination"));
        }

        Sample sample = sampleService.get(null, null, samplecode, null, null).get(0);
        List<Test> listOrderExams = middlewareDao.getTestWhitVolumes(createdOrder.getOrderNumber());
        // Se le asigna a la orden los examenes de la misma, solo los examenes, ni perfiles ni paquetes
        createdOrder.setTests(listOrderExams);
        bean.setCover(sample.isCoveredSample());
        createdOrder.getTests().stream().filter((test) -> (test.getSample().getCodesample().equals(sample.getCodesample()))).forEachOrdered((test) ->
        {
            // Se realizara este proceso entre todos los examenes que tiene esa orden con la misma muestra
            int volumParseI;
            double volumParseD;
            double volumParse;
            if (!(test.getVolume().trim().isEmpty() || test.getVolume() == null))
            {

                if (!test.getVolume().contains("."))
                {
                    volumParseI = Integer.parseInt(test.getVolume());
                    volumParse = volumParseI;
                } else
                {
                    volumParseD = Double.parseDouble(test.getVolume());
                    volumParse = volumParseD;
                }
            } else
            {
                volumParse = 0.0;
            }

            double totalVolumParse = volumParse + bean.getTotalVolume();
            bean.setTotalVolume(totalVolumParse);
            if (test.isLicuota())
            {
                if (!(test.getVolume().trim().isEmpty() || test.getVolume() == null))
                {
                    if (!test.getVolume().contains("."))
                    {
                        volumParseI = Integer.parseInt(test.getVolume());
                        volumParse = volumParseI;
                    } else
                    {
                        volumParseD = Double.parseDouble(test.getVolume());
                        volumParse = volumParseD;
                    }
                } else
                {
                    volumParse = 0.0;
                }

                double totalVolumParseLicuota = volumParse + bean.getTotalVolumeLicuota();
                bean.setTotalVolumeLicuota(totalVolumParseLicuota);

                AlicuotaWithVolum alicuotaWithVolum = new AlicuotaWithVolum();
                alicuotaWithVolum.setIdTest(test.getId());
                alicuotaWithVolum.setVolumen(volumParse);
                bean.getAlicuotaWithVolum().add(alicuotaWithVolum);
            }
        });

        return bean;
    }

    @Override
    public CheckDestination checkDestinationMiddleware(CheckDestination destination) throws Exception
    {
        MiddlewareLog.info("Verificacion de automate : " + Tools.jsonObject(destination));
        
        
        AuthorizedUser authorizedUser = JWT.decode(request);
        User user = userService.get(authorizedUser.getId(), null, null, null);

        ORDERDIGITS = Integer.parseInt(configurationService.getValue("DigitoAño"));
        // Verificación de la muestra para el destino que tiene asociado ese usuario analizado

        Order createdOrder = orderService.get(Long.parseLong(Tools.getOrderNumberYear(destination.getOrder(), ORDERDIGITS)));

        Sample sample = sampleService.get(null, null, destination.getSample(), null, null).get(0);

        boolean verific = sampleTrackingService.isSampleCheckSimple(createdOrder.getOrderNumber(), sample.getId());
        if (verific)
        {
            sampleTrackingService.sampleTrackingAnalyzer(createdOrder.getOrderNumber(), destination.getSample(), LISEnum.ResultSampleState.CHECKED.getValue(), null, false, createdOrder.getBranch().getId(), user);
        }

        AssignmentDestination assigment = sampleTrackingService.getDestinationRouteAnalyzer(createdOrder.getOrderNumber(), destination.getSample(), createdOrder.getBranch().getId(), user);
        if (assigment == null)
        {
            //throw new EnterpriseNTException(Arrays.asList("1|Route not configured"));
        }
        if (assigment != null)
        {
            boolean assingDestination = false;
            boolean verificDestination = false;
            int idDestination = 0;
            for (int i = 0; i < assigment.getDestinationRoutes().size(); i++)
            {
                if (assigment.getDestinationRoutes().get(i).getDestination().getCode() == null ? destination.getDestination() == null : assigment.getDestinationRoutes().get(i).getDestination().getCode().equals(destination.getDestination()))
                {
                    assingDestination = true;
                    idDestination = assigment.getDestinationRoutes().get(i).getId();
                    verificDestination = assigment.getDestinationRoutes().get(i).isVerify();
                    break;
                }
            }

            if (assingDestination)
            {
                if(!verificDestination)
                {        
                    VerifyDestination verify = new VerifyDestination();
                    verify.setApproved(true);
                    verify.setBranch(createdOrder.getBranch().getId());
                    verify.setDestination(idDestination);
                    verify.setOrder(createdOrder.getOrderNumber());
                    verify.setSample(destination.getSample());
                    boolean verifyTwo = sampleTrackingService.verifyDestinationAnalyzer(verify, user);
                    if (!verifyTwo)
                    {
                        destination.setError("2|destination");
                    }
                }
            } else
            {
                throw new EnterpriseNTException(Arrays.asList("2|destination not related to the sample"));
            }
        }
        return destination;
    }
}

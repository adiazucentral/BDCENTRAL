package net.cltech.enterprisent.service.impl.enterprisent.integration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationBandDao;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationSklDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.skl.ContainerSkl;
import net.cltech.enterprisent.domain.integration.skl.InterviewInformedConsent;
import net.cltech.enterprisent.domain.integration.skl.ListTestOrderSkl;
import net.cltech.enterprisent.domain.integration.skl.OrderConsent;
import net.cltech.enterprisent.domain.integration.skl.OrderConsentBase64;
import net.cltech.enterprisent.domain.integration.skl.OrderInformedConsent;
import net.cltech.enterprisent.domain.integration.skl.RequestSampleDestination;
import net.cltech.enterprisent.domain.integration.skl.SklAnswer;
import net.cltech.enterprisent.domain.integration.skl.SklOrderAnswer;
import net.cltech.enterprisent.domain.integration.skl.SklOrderToTake;
import net.cltech.enterprisent.domain.integration.skl.SklQuestion;
import net.cltech.enterprisent.domain.integration.skl.SklSampleDestination;
import net.cltech.enterprisent.domain.integration.skl.TestConsent;
import net.cltech.enterprisent.domain.integration.skl.TestConsentBase64;
import net.cltech.enterprisent.domain.integration.skl.patientTestPending;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.interview.Answer;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.test.Container;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.masters.tracking.DestinationRoute;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.RequestFormatDate;
import net.cltech.enterprisent.domain.operation.tracking.VerifyDestination;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationSklService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.interview.QuestionService;
import net.cltech.enterprisent.service.interfaces.masters.test.ContainerService;
import net.cltech.enterprisent.service.interfaces.masters.test.SampleService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.DestinationService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.service.interfaces.operation.tracking.SampleTrackingService;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.log.integration.SigaLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Recipientes para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author JDuarte
 * @see 12/05/2017
 * @see Creaciòn
 */
@Service
public class IntegrationSklServiceEnterpriseNT implements IntegrationSklService
{
    
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private IntegrationSklDao sklDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private SampleTrackingService sampleTrackingService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private IntegrationBandDao integrationBandDao;
    @Autowired
    private DestinationService destinationService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private SampleService sampleService;
    @Autowired
    private ContainerService containerService;
    
    @Override
    public List<ContainerSkl> listContainer() throws Exception
    {
        return sklDao.listContainer();
    }

    /**
     * Obtiene el codigo del tipo de una orden
     *
     * @param idOrder
     * @return Codigo del tipo de una orden
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public String getOrderCode(long idOrder) throws Exception
    {
        try
        {
            Order order = orderService.get(idOrder);
            return order.getType().getCode();
        } catch (Exception e)
        {
            return "";
        }
    }

    /**
     * Obtiene informacion basica del paciente por medio de la orden
     *
     * @param idOrder
     * @return idPaciente + Nombres del paciente
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public String getPatientBasicInfo(long idOrder) throws Exception
    {
        try
        {
            String basicInfo = sklDao.getPatientBasicInfo(idOrder);
            return basicInfo;
        } catch (Exception e)
        {
            return "";
        }
    }

    /**
     * Obtiene el nombre del tipo de una orden
     *
     * @param idOrder
     * @return Nombre del tipo de orden
     * @throws Exception Error en la base de datos.
     */
    @Override
    public String getOrderType(long idOrder) throws Exception
    {
        try
        {
            String nameType = sklDao.getOrderType(idOrder);
            return nameType;
        } catch (Exception e)
        {
            return "";
        }
    }
    
    @Override
    public List<String> getTestByTestsIdSample(ListTestOrderSkl resultListTest) throws Exception
    {
        ArrayList<String> testsAbbr = new ArrayList<String>();
        for (String i : resultListTest.getTests())
        {
            String testAbbr = sklDao.getTestByTestsIdSample(i, resultListTest);
            testsAbbr.add(testAbbr);
        }
        
        return testsAbbr;
        
    }

    /**
     * Consulta la ruta del siguiente destino y retorna el id del mismo
     *
     * @param verifyDestination
     * @return Retorna id de la siguiente ruta de destino
     * @throws Exception Error en la base de datos.
     */
    @Override
    public int nextDestination(Long idOrder, String codeSample) throws Exception
    {
        try
        {
            int idNextDest = -1;
            AssignmentDestination assignmentDestination = sampleTrackingService.getDestinationRoute(idOrder, codeSample);
            
            for (DestinationRoute destination : assignmentDestination.getDestinationRoutes())
            {
                if (!destination.isVerify())
                {
                    idNextDest = destination.getId();
                    break;
                }
            }
            return idNextDest;
        } catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Obtiene un paciente con un formato de fecha de nacimiento distinto
     *
     * @return Paciente con un formato de fecha de nacimiento distinto
     * @throws Exception Error presentado en el servicio
     */
    @Override
    public Patient getPatientByOrder(RequestFormatDate request) throws Exception
    {
        try
        {
            Patient patientFormat = patientService.get(request.getIdOrder());
            if (!request.getFormatDate().isEmpty())
            {
                SimpleDateFormat format = new SimpleDateFormat(request.getFormatDate());
                String birthDayS = format.format(patientFormat.getBirthday());
                patientFormat.setBirthDayFormat(birthDayS);
                return patientFormat;
            } else
            {
                return patientFormat;
            }
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene todas las preguntas para una orden determinada
     *
     * @param idOrder
     * @return Lista de preguntas asociadas a dicha orden
     * @throws java.lang.Exception
     */
    @Override
    public List<SklQuestion> getOrderQuestions(long idOrder) throws Exception
    {
        List<SklQuestion> listQuestionsForOrder = new ArrayList<>();
        try
        {
            List<Question> listQuestions = sampleTrackingService.getInterviewskl(idOrder);
            List<SklAnswer> listAnswers = new ArrayList<>();
            for (Question question : listQuestions)
            {
                SklQuestion objSklQuestion = new SklQuestion();
                SklAnswer objSklAnswer = new SklAnswer();
                objSklQuestion.setId(question.getId());
                objSklQuestion.setQuestion(question.getQuestion());
                objSklQuestion.setOpenAnswer(question.isOpen());
                objSklQuestion.setOrder(question.getOrder());
                if (!objSklQuestion.isOpenAnswer())
                {
                    for (Answer answer : question.getAnswers())
                    {
                        SklAnswer sklAnswers = new SklAnswer();
                        sklAnswers.setIdQuestion(question.getId());
                        sklAnswers.setId(answer.getId());
                        sklAnswers.setAnswer(answer.getName());
                        listAnswers.add(sklAnswers);
                        if (answer.isSelected())
                        {
                            objSklQuestion.setAnswer(sklAnswers);
                        }
                        
                    }
                } else
                {
                    objSklAnswer.setIdQuestion(question.getId());
                    objSklAnswer.setId(null);
                    objSklAnswer.setAnswer(question.getInterviewAnswer());
                    objSklQuestion.setAnswer(objSklAnswer);
                }
                
                if (!listAnswers.isEmpty())
                {
                    objSklQuestion.setAvailableAnswer(listAnswers);
                    listAnswers = new ArrayList<>();
                }
                
                listQuestionsForOrder.add(objSklQuestion);
            }
            return listQuestionsForOrder;
        } catch (Exception e)
        {
            SigaLog.info("ERROR EN SIGA IS " + e);
            return listQuestionsForOrder;
        }
    }
    
    @Override
    public List<ContainerSkl> getRecipientOrderByTestList(String listTests) throws Exception
    {
        try
        {
            List<ContainerSkl> listContainers = new ArrayList<>();
            List<String> idsTests = Arrays.asList(listTests.split(","));
            for (String id : idsTests)
            {
                int idParse = 0;
                idParse = Integer.parseInt(id.replace(" ", ""));
                if (idParse != 0)
                {
                    ContainerSkl container = sklDao.getRecipientOrderByTestList(idParse);
                    if (container != null && listContainers.stream().filter(cont -> cont.getId() == container.getId()).findFirst().orElse(null) == null)
                    {
                        listContainers.add(container);
                    }
                }
            }
            return listContainers;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Se obtienen todos los recipientes asignados a una orden
     *
     * @param idOrder
     * @param pendingToTake
     * @return Lista de recipientes
     * @throws Exception
     */
    @Override
    public List<ContainerSkl> getRecipients(long idOrder, int pendingToTake) throws Exception
    {
        try
        {
            List<ContainerSkl> listContainers = sklDao.getRecipients(idOrder, pendingToTake);
            for (ContainerSkl container : listContainers)
            {
                
                Container containerImage = containerService.get(container.getId(), null, null);
                container.setImageBase64(containerImage.getImage());
                
                container.setTakePending(sklDao.testPendingSample(idOrder, container.getIdSample()));
            }
            return listContainers;
        } catch (Exception e)
        {
            return null;
        }
    }
    
    @Override
    public String listCommentOrder(Long idOrder) throws Exception
    {
        try
        {
            return sklDao.listCommentOrder(idOrder);
        } catch (Exception e)
        {
            return "";
        }
    }
    
    @Override
    public String listCommentPatient(int idPatient) throws Exception
    {
        try
        {
            return sklDao.listCommentPatient(idPatient);
        } catch (Exception e)
        {
            return "";
        }
    }

    /**
     * Obtiene todas las respuestas asociadas a una pregunta cerrada
     *
     * @param idQuestion
     * @return Lista de respuestas por pregunta
     * @throws java.lang.Exception Error en la base de datos.
     */
    @Override
    public List<SklAnswer> listAnswersByIdQuestion(int idQuestion) throws Exception
    {
        try
        {
            return sklDao.listAnswersByIdQuestion(idQuestion);
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene todas las respuestas asociadas a una orden
     *
     * @param idOrder
     * @return Lista de respuestas por orden
     * @throws java.lang.Exception Error en la base de datos.
     */
    @Override
    public List<SklAnswer> listAnswersByIdOrder(long idOrder) throws Exception
    {
        try
        {
            List<Question> questionList = new ArrayList();
            questionList = sampleTrackingService.getInterview(idOrder);
            
            List<SklAnswer> listSklQuestions = new ArrayList();
            
            for (Question question : questionList)
            {
                for (Answer answer : question.getAnswers())
                {
                    SklAnswer questionAux = new SklAnswer();
                    questionAux.setIdQuestion(question.getId());
                    questionAux.setId(answer.getId());
                    questionAux.setAnswer(answer.getName());
                    
                    listSklQuestions.add(questionAux);
                }
            }
            
            return listSklQuestions;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene todas las preguntas con el formato requerido para SKL
     *
     * @return Lista de preguntas
     * @throws java.lang.Exception Error en la base de datos.
     */
    @Override
    public List<SklQuestion> getGeneralInterview() throws Exception
    {
        try
        {
            List<Question> listQuestions = questionService.list();
            List<SklQuestion> listSklQuestions = new ArrayList<>();
            for (Question question : listQuestions)
            {
                SklQuestion questionAux = new SklQuestion();
                questionAux.setId(question.getId());
                questionAux.setQuestion(question.getQuestion());
                questionAux.setOpenAnswer(question.isOpen());
                questionAux.setOrder(question.getOrder() == null ? 0 : question.getOrder());
                if (!questionAux.isOpenAnswer())
                {
                    questionAux.setAvailableAnswer(listAnswersByIdQuestion(questionAux.getId()));
                }
                listSklQuestions.add(questionAux);
            }
            
            return listSklQuestions;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Obtiene el id de la pregunta asignada a una orden
     *
     * @param idOrder
     * @param idQuestion
     * @return Lista de respuestas asociadas a dicha orden
     * @throws java.lang.Exception
     */
    @Override
    public Integer getOrderQuestionId(long idOrder, int idQuestion) throws Exception
    {
        try
        {
            Integer idOfTheQuestion = sklDao.getOrderQuestionId(idOrder, idQuestion);
            return idOfTheQuestion;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Consulta la ruta de destinos de una muestra
     *
     * @param requestSampleDestination
     * @return Lista de destinos de una muestra
     *
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<SklSampleDestination> getSampleDestinations(RequestSampleDestination requestSampleDestination) throws Exception
    {
        List<SklSampleDestination> destinationList = new ArrayList<>();
        try
        {
            AuthorizedUser user = JWT.decode(request);
            requestSampleDestination.setIdBranch(user.getBranch());
            destinationList = sklDao.getSampleDestinations(requestSampleDestination);
            return destinationList;
        } catch (Exception e)
        {
            return destinationList;
        }
    }

    /**
     * Actualiza la respuesta de una pregunta a una orden
     *
     * @param answer
     * @throws Exception Error en la base de datos.
     */
    @Override
    public void updateAnswer(SklOrderAnswer answer) throws Exception
    {
        try
        {
            AuthorizedUser user = JWT.decode(request);
            sklDao.updateAnswer(answer, user);
        } catch (Exception e)
        {
            e.getMessage();
        }
    }
    
    @Override
    public SklOrderAnswer createAnswer(SklOrderAnswer answer) throws Exception
    {
        if (answer.getIdQuestion() != 0)
        {
            int typeQuestion = sklDao.typeQuestion(answer);
            Order order = orderService.get(answer.getOrder());
            answer.setOrderType(order != null ? order.getType().getId() : 0);
            SklOrderAnswer created = sklDao.createAnswer(answer, typeQuestion);
            trackingService.registerConfigurationTracking(null, created, SklAnswer.class);
            
            return answer;
        } else
        {
            throw new EnterpriseNTException(Arrays.asList("1|The Answer is null"));
        }
    }

    /**
     * Verifica la muestra en el destino asignado destino si ese destino esta
     * asignado para esa muestra
     *
     * @param idSample
     * @param idDestination
     * @param idOrder
     * @return La fecha de verificacion de esa muestra en el destino asignado
     * @throws Exception Error en la base de datos.
     */
    @Override
    public Date checkSample(int idSample, int idDestination, long idOrder) throws Exception
    {
        try
        {
            Date verifiedDate = null;
            AuthorizedUser user = JWT.decode(request);
            Order order = sampleTrackingService.getOrder(idOrder, true);
            Sample testSample = order.getSamples().stream().filter(sample -> Objects.equals(sample.getId(), idSample)).findFirst().orElse(null);
            int idAssignRout = integrationBandDao.getIdAssignRout(idSample, user.getBranch(), idDestination);
            VerifyDestination destination = new VerifyDestination();
            destination.setOrder(order.getOrderNumber());
            destination.setSample(testSample.getCodesample());
            destination.setDestination(idAssignRout);
            boolean verified = sampleTrackingService.verifyDestination(destination);
            if (verified)
            {
                verifiedDate = sklDao.checkSample(idAssignRout, idOrder);
            }
            return verifiedDate;
        } catch (Exception e)
        {
            return null;
        }
    }
    
    @Override
    public Integer destinationInitial(int idSample) throws Exception
    {
        
        try
        {
            
            AuthorizedUser session = JWT.decode(request);
            int branch = session.getBranch();
            int typeorder = 0;
            AssignmentDestination object = new AssignmentDestination();
            object = destinationService.getRoute(branch, idSample, typeorder, false);
            
            int valueFirstDestination;
            
            if (object.getDestinationRoutes().isEmpty())
            {
                valueFirstDestination = 0;
                
            } else
            {
                
                valueFirstDestination = object.getDestinationRoutes().get(0).getDestination().getId();
            }
            return valueFirstDestination;
            
        } catch (Exception e)
        {
            
            return -1;
        }
        
    }

    /**
     * Obtiene las ordenes con muestra pendiente por toma
     *
     * @param date
     * @return Lista de ordenes pendientes por toma de muestra
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<SklOrderToTake> getOrdersToTake(int date) throws Exception
    {
        try
        {
            Configuration hospitalLocation = configurationService.get("UbicacionHospitalaria");
            List<SklOrderToTake> ordersToTakes = new ArrayList<>();
            // Se obtienen las ordenes registradas con esa fecha
            List<Long> listOrders = sklDao.getOrdersToTake(date);
            for (Long idOrder : listOrders)
            {
                Order order = sampleTrackingService.getOrder(idOrder, true);
                if (order != null && order.getService().isExternal() && order.getSamples() != null && !order.getSamples().isEmpty())
                {
                    for (Sample sample : order.getSamples())
                    {
                        int sampleStatus = sklDao.getSampleStatus(sample.getId(), idOrder);
                        if (sampleStatus == -1 || sampleStatus == 1 || sampleStatus == 2)
                        {
                            String name2 = order.getPatient().getName2().isEmpty() ? " " : " " + order.getPatient().getName2() + " ";
                            String surName = order.getPatient().getSurName().isEmpty() ? "" : " " + order.getPatient().getSurName();
                            String location = hospitalLocation.getValue().isEmpty() ? null : hospitalLocation.getValue();
                            SklOrderToTake sklOrder = new SklOrderToTake();
                            sklOrder.setOrder(order.getOrderNumber());
                            sklOrder.setIdPatient(order.getPatient().getId());
                            sklOrder.setPatientId(order.getPatient().getPatientId());
                            sklOrder.setPatient(order.getPatient().getName1() + name2 + order.getPatient().getLastName() + surName);
                            sklOrder.setSex(order.getPatient().getSex().getEsCo());
                            sklOrder.setLocation(location);
                            
                            if (ordersToTakes.stream().filter(skl -> skl.getOrder() == sklOrder.getOrder()).findFirst().orElse(null) == null)
                            {
                                ordersToTakes.add(sklOrder);
                            }
                        }
                    }
                }
            }
            
            return ordersToTakes;
        } catch (Exception e)
        {
            return null;
        }
    }
    
    @Override
    public List<InterviewInformedConsent> listInformedConsent() throws Exception
    {
        return sklDao.listInterviewInformedConsent();
    }
    
    @Override
    public OrderConsent getOrderConsent(long order) throws Exception
    {
        try
        {
            
            OrderConsent object = new OrderConsent();
            
            object = sklDao.getOrderConsent(order);
            
            List<TestConsent> listTest = new ArrayList<>();
            listTest = sklDao.listTestInformedConsent(order);
            object.setTestConsent(listTest);
            
            return object;
            
        } catch (Exception e)
        {
            return null;
        }
    }
    
    @Override
    public OrderInformedConsent createConsent(OrderInformedConsent consent) throws Exception
    {
        
        try
        {
            AuthorizedUser session = JWT.decode(request);
            
            OrderInformedConsent newBean = sklDao.createConsent(consent, session.getId());
            
            trackingService.registerConfigurationTracking(null, newBean, OrderInformedConsent.class);
            
            return newBean;
            
        } catch (Exception e)
        {
            return null;
        }
        
    }
    
    public OrderConsentBase64 getAllConsentInformed(long order) throws Exception
    {
        
        try
        {
            
            OrderConsentBase64 orderConsentBase64 = new OrderConsentBase64();
            
            orderConsentBase64 = sklDao.getAllConsentInformed(order);
            
            List<TestConsentBase64> listTest = new ArrayList<>();
            listTest = sklDao.listTestConsentbase64(order);
            orderConsentBase64.setTestConsentBase64(listTest);
            
            return orderConsentBase64;
            
        } catch (Exception e)
        {
            return null;
        }
    }
    
    @Override
    public List<OrderConsentBase64> getListBase64(String history, int documenttype) throws Exception
    {
        try
        {
            List<OrderConsentBase64> litsConsentOrders = new ArrayList<>();
            litsConsentOrders = sklDao.litsConsentOrders(history, documenttype);
            
            for (int i = 0; i < litsConsentOrders.size(); i++)
            {
                List<TestConsentBase64> listTest = new ArrayList<>();
                
                listTest = sklDao.listTestConsentbase64(litsConsentOrders.get(i).getOder());
                
                litsConsentOrders.get(i).setTestConsentBase64(listTest);
                
            }
            
            return litsConsentOrders;
        } catch (Exception e)
        {
            return null;
        }
    }
    
    @Override
    public List<TestConsent> getListTestsSample(long order, String sample) throws Exception
    {
        try
        {
            List<TestConsent> listTest = new ArrayList<>();
            List<Sample> sampleList = new ArrayList<>();
            sampleList = sampleService.get(null, null, sample, null, null);
            listTest = sklDao.getListTestsSample(order, sampleList.get(0).getId());
            listTest = listTest.stream()
                    .filter(str -> (str.getType() == 0 && str.getProfile() == 0) || (str.getType() > 0 && str.getProfile() == 0) || (str.getType() == 0 && str.getProfile() != 0 && str.getDependentTest() == 0)) //Multiple conditions
                    .collect(Collectors.toList());
            
            return listTest;
        } catch (Exception e)
        {
            return null;
        }
    }
    
    @Override
    public List<patientTestPending> getPatientTestPending(String document) throws Exception
    {
        try
        {            
            List<patientTestPending> patientList = new ArrayList<>();
            List<patientTestPending> patientListResponse = new ArrayList<>();
            patientListResponse = sklDao.getPatientTestPending(document);
//            for (patientTestPending testPending : patientList)
//            {
//                patientTestPending patientAux = patientListResponse.stream().filter(aux -> Objects.equals(aux.getIdPatient(), testPending.getIdPatient())).findAny().orElse(null);
//                if (patientAux != null)
//                {
//                    if (Long.parseLong(testPending.getNumOrder()) > Long.parseLong(patientAux.getNumOrder()))
//                    {
//                        patientListResponse.remove(patientAux);
//                        patientListResponse.add(testPending);
//                    }
//                }
//                else
//                {
//                    patientListResponse.add(testPending);
//                }
//            }
            return patientListResponse;
        } catch (Exception e)
        {
            return null;
        }
        
    }
    
}

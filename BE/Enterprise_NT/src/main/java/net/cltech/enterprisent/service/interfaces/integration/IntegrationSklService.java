package net.cltech.enterprisent.service.interfaces.integration;

import java.util.Date;
import java.util.List;
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
import net.cltech.enterprisent.domain.integration.skl.patientTestPending;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.RequestFormatDate;

/**
 * Interfaz de servicios de la consulta web HIS
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 12/05/2020
 * @see Creación
 */
public interface IntegrationSklService
{

    /**
     * Lista los recipientes desde la base de datos integracion SKL
     *
     * @return Lista de recipientes
     * @throws Exception Error en base de datos
     */
    public List<ContainerSkl> listContainer() throws Exception;

    /**
     * Se obtienen todos los recipientes asignados a una orden
     *
     * @param idOrder
     * @param pendingToTake
     * @return Lista de recipientes
     * @throws Exception
     */
    public List<ContainerSkl> getRecipients(long idOrder, int pendingToTake) throws Exception;

    /**
     * Obtiene el codigo del tipo de una orden
     *
     * @param idOrder
     * @return Codigo del tipo de una orden
     * @throws Exception Error presentado en el servicio
     */
    public String getOrderCode(long idOrder) throws Exception;

    /**
     * Obtiene informacion basica del paciente por medio de la orden
     *
     * @param idOrder
     * @return idPaciente + Nombres del paciente
     * @throws Exception Error presentado en el servicio
     */
    public String getPatientBasicInfo(long idOrder) throws Exception;

    /**
     * Obtiene el nombre del tipo de una orden
     *
     * @param idOrder
     * @return Nombre del tipo de orden
     * @throws Exception Error en la base de datos.
     */
    public String getOrderType(long idOrder) throws Exception;

    /**
     * Lista la abreviatura de los examenes.
     *
     * @param resultListTest
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<String> getTestByTestsIdSample(ListTestOrderSkl resultListTest) throws Exception;

    /**
     * Consulta la ruta del siguiente destino y retorna el id del mismo
     *
     * @param idOrder
     * @param codeSample
     * @return Retorna id de la siguiente ruta de destino
     *
     * @throws Exception Error en la base de datos.
     */
    public int nextDestination(Long idOrder, String codeSample) throws Exception;

    /**
     * Obtiene un paciente con un formato de fecha de nacimiento distinto
     *
     * @param request
     * @return Paciente con un formato de fecha de nacimiento distinto
     * @throws Exception Error presentado en el servicio
     */
    public Patient getPatientByOrder(RequestFormatDate request) throws Exception;

    /**
     * Obtiene todas las preguntas para una orden determinada
     *
     * @param idOrder
     * @return Lista de preguntas asociadas a dicha orden
     * @throws java.lang.Exception
     */
    public List<SklQuestion> getOrderQuestions(long idOrder) throws Exception;

    /**
     * Se obtienen todos los recipientes de una lista de examenes
     *
     * @param listTests
     * @return Lista de recipientes
     * @throws Exception
     */
    public List<ContainerSkl> getRecipientOrderByTestList(String listTests) throws Exception;

    /**
     * Obtiene los comentarios de una orden o el diagnostico permanente de un
     * paciente.
     *
     * @param idOrder Numero de la Orden.
     * @return Lista de comentarios.
     * @throws java.lang.Exception Error en la base de datos.
     */
    public String listCommentOrder(Long idOrder) throws Exception;

    /**
     * Obtiene los comentarios de una orden o el diagnostico permanente de un
     * paciente.
     *
     * @param idPatient id del paciente .
     * @return Lista de comentarios.
     * @throws java.lang.Exception Error en la base de datos.
     */
    public String listCommentPatient(int idPatient) throws Exception;

    /**
     * Obtiene todas las respuestas asociadas a una pregunta cerrada
     *
     * @param idQuestion
     * @return Lista de respuestas por pregunta
     * @throws java.lang.Exception Error en la base de datos.
     */
    public List<SklAnswer> listAnswersByIdQuestion(int idQuestion) throws Exception;

    /**
     * Obtiene todas las respuestas asociadas a una orden
     *
     * @param idOrder
     * @return Lista de respuestas por orden
     * @throws java.lang.Exception Error en la base de datos.
     */
    public List<SklAnswer> listAnswersByIdOrder(long idOrder) throws Exception;

    /**
     * Obtiene todas las preguntas con el formato requerido para SKL
     *
     * @return Lista de preguntas
     * @throws java.lang.Exception Error en la base de datos.
     */
    public List<SklQuestion> getGeneralInterview() throws Exception;

    /**
     * Obtiene el id de la pregunta asignada a una orden
     *
     * @param idOrder
     * @param idQuestion
     * @return Lista de respuestas asociadas a dicha orden
     * @throws java.lang.Exception
     */
    public Integer getOrderQuestionId(long idOrder, int idQuestion) throws Exception;

    /**
     * Consulta la ruta de destinos de una muestra
     *
     * @param requestSampleDestination
     * @return Lista de destinos de una muestra
     *
     * @throws Exception Error en la base de datos.
     */
    public List<SklSampleDestination> getSampleDestinations(RequestSampleDestination requestSampleDestination) throws Exception;

    /**
     * Actualiza la respuesta de una pregunta a una orden
     *
     * @param answer
     * @throws Exception Error en la base de datos.
     */
    public void updateAnswer(SklOrderAnswer answer) throws Exception;

    /**
     * Registra una nueva respuesta en la base de datos.
     *
     * @param answer Instancia con los datos de la respuesta.
     *
     * @return Instancia con las datos de la respuesta.
     * @throws Exception Error en la base de datos.
     */
    public SklOrderAnswer createAnswer(SklOrderAnswer answer) throws Exception;

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
    public Date checkSample(int idSample, int idDestination, long idOrder) throws Exception;

    /**
     * Obtiene el destino inicial de la muestra.
     *
     * @param idSample
     *
     * @return Instancia con las datos de la respuesta.
     * @throws Exception Error en la base de datos.
     */
    public Integer destinationInitial(int idSample) throws Exception;

    /**
     * Obtiene las ordenes con muestra pendiente por toma
     *
     * @param date
     * @return Lista de ordenes pendientes por toma de muestra
     * @throws Exception Error en la base de datos.
     */
    public List<SklOrderToTake> getOrdersToTake(int date) throws Exception;

    /**
     * Lista las preguntas de una entrevista con consentimiento informado.
     *
     * @return Lista de entrevista.
     * @throws Exception Error en la base de datos.
     */
    public List<InterviewInformedConsent> listInformedConsent() throws Exception;

    /**
     * Obtiene el objeto Orden con consentimiento informado.
     *
     * @param order
     * @return Objeto Orden con consentimiento informado.
     * @throws Exception Error en la base de datos.
     */
    public OrderConsent getOrderConsent(long order) throws Exception;

    /**
     * Crea un nuevo registro.
     *
     * @param consent
     * @return Objeto Orden con consentimiento informado.
     * @throws Exception Error en la base de datos.
     */
    public OrderInformedConsent createConsent(OrderInformedConsent consent) throws Exception;

    /**
     * Toda la informacion con base 64 por orden.
     *
     * @param order
     * @return Objeto Orden con consentimiento informado.
     * @throws Exception Error en la base de datos.
     */
    public OrderConsentBase64 getAllConsentInformed(long order) throws Exception;

    /**
     * Se obtienen todas las ordenes con sus examenes con su base 64
     *
     * @param history
     * @param documenttype
     * @return Lista de recipientes
     * @throws Exception
     */
    public List<OrderConsentBase64> getListBase64(String history, int documenttype) throws Exception;
    
     /**
     * Se obtiene lista de examenes de una muestra
     *
     * @param order
     * @param sample   
     * @return Lista de examenes
     * @throws Exception
     */
    public List<TestConsent> getListTestsSample(long order, String sample) throws Exception;
    
    
    
    /**
     * Obtiene lista de examenes pendientes del paciente 
     *
     * @param document
     * @return lista de pendingExams
     * @throws Exception Error en la base de datos.
     */
    public List<patientTestPending> getPatientTestPending(String document) throws Exception;
}

package net.cltech.enterprisent.service.interfaces.integration;

import java.util.List;
import net.cltech.enterprisent.domain.integration.homebound.AccountHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.Appointment;
import net.cltech.enterprisent.domain.integration.homebound.BasicHomeboundPatient;
import net.cltech.enterprisent.domain.integration.homebound.BillingTestHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.DemographicHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.DocumentTypeHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.GenderHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.ListOrders;
import net.cltech.enterprisent.domain.integration.homebound.PatientHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.PaymentTypeHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.ProfileHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.QuestionHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.RateHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.TestHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.Track;
import net.cltech.enterprisent.domain.integration.homebound.TransportSampleHomebound;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.operation.orders.Order;

/**
 * @version 1.0.0
 * @author JDuarte
 * @since 17/02/2020
 * @see Creacion
 */
public interface IntegrationHomeBoundService
{

    /**
     * Obtener lista de pruebas en las que la prueba forme parte del Home Bound.
     *
     * @paramTestHomeBound id Id de la prueba.
     *
     * @return Instancia con los datos de la prueba.
     * @throws Exception Error en la base de datos.
     */
    public List<TestHomeBound> testHomeBound() throws Exception;
    
     /**
     * Obtener lista de pruebas en las que la prueba forme parte del Home Bound.
     *
     * @paramTestHomeBound id Id de la prueba.
     *
     * @return Instancia con los datos de la prueba.
     * @throws Exception Error en la base de datos.
     */
    public List<ProfileHomeBound> userPermissionHomeBound() throws Exception;

    /**
     * Lista las ordenes y examenes por un filtro especifico dado por la clase de listados.
     *
     * @param listOrders
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Order> listFilters(ListOrders listOrders) throws Exception;
    
    /**
     * Lista las ordenes por un filtro especifico dado por la clase de listados.
     *
     * @param listOrders
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Order> listBasicFilters(ListOrders listOrders) throws Exception;

    /**
     * Lista las ordenes por un filtro especifico dado por la clase de listados.
     *
     * @param order
     * @param sample
     * @param url
     *
     * @return Lista de ordenes.
     *
     */
    public Boolean verificSample(Long order, Integer sample, String url);

    /**
     * envia la retoma la muestra de una orden a homebound.
     *
     * @param order
     * @param sample
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public Boolean retakeSample(Long order, Integer sample) throws Exception;

    /**
     * Obtiene una lista de los tipos de documentos de NT y los devuelve con el
     * formato correcto a HomeBound
     *
     *
     * @return Lista de tipos de documentos.
     * @throws Exception Error en la base de datos.
     */
    public List<DocumentTypeHomeBound> listDocumentTypes() throws Exception;

    /**
     * Obtiene una lista de los clientes y los envia con el formato correcto a
     * HomeBound
     *
     *
     * @return Lista de clientes (Cuentas).
     * @throws Exception Error en la base de datos.
     */
    public List<AccountHomeBound> listAccounts() throws Exception;

    /**
     * Obtiene una lista de generos de NT para ser enviados a Homebound
     *
     * @return Lista de generos
     * @throws Exception Error en la base de datos.
     */
    public List<GenderHomeBound> listGenders() throws Exception;

    /**
     * Obtiene una lista de los tipos de pagos para ser enviados a Homebound
     *
     * @return Lista de formas de pagos
     * @throws Exception Error en la base de datos.
     */
    public List<PaymentTypeHomeBound> listPaymentTypes() throws Exception;

    /**
     * Obtiene un paciente segun ciertos parametros de busqueda, tales como el tipo de documento,
     * el número de identificacion del paciente (historia), y el lenguaje
     *
     * @param documentType
     * @param patientId
     * @param lang
     * 
     * @return Paciente Homebound
     * @throws Exception Error en la base de datos.
     */
    public PatientHomeBound getPatientHomeBound(int documentType, String patientId, String lang) throws Exception;

    /**
     * Obtiene una lista de tipos de orndenes para ser enviados a Homebound
     *
     * @return Paciente con el formato requerido por Homebound
     * @throws Exception Error en la base de datos.
     */
    public List<OrderType> listOrderTypes() throws Exception;

    /**
     * Crea una orden a partir de una cita de homebound
     *
     * @param appointment
     * @return Cita
     * @throws Exception Error en la base de datos.
     */
    public Appointment createAppointment(Appointment appointment) throws Exception;

    /**
     * Obtiene un paciente por su identificador en la base de datos para ser
     * enviados a Homebound
     *
     * @param id
     * @return Paciente con el formato requerido por Homebound
     * @throws Exception Error en la base de datos.
     */
    public PatientHomeBound getPatientByIdHomeBound(int id) throws Exception;

    /**
     * Crea un paciente en la base de datos desde un servicio externo que
     * consume este servicio de NT
     *
     * @param patientHomebound
     * @return Paciente creado desde homebound
     * @throws Exception Error en la base de datos.
     */
    public PatientHomeBound createPatientHomeBound(PatientHomeBound patientHomebound) throws Exception;

    /**
     * Actualiza un paciente en la base de datos desde un servicio externo que
     * consume este servicio de NT
     *
     * @param patient
     * @return Paciente creado desde homebound
     * @throws Exception Error en la base de datos.
     */
    public PatientHomeBound updatePatientHomeBound(PatientHomeBound patient) throws Exception;

    /**
     * Obtiene un objeto BillingTestHomeBound por el id de la prueba y el
     * identificador de tarifa en la base de datos para ser enviados a Homebound
     *
     * @param rate
     * @param test
     * @return Paciente con el formato requerido por Homebound
     * @throws Exception Error en la base de datos.
     */
    public BillingTestHomeBound getRatetByIdHomeBound(Integer rate, Integer test) throws Exception;

    /**
     * Realiza la actualizacion de la toma de muestra segun los datos enviados
     * por Homebound
     *
     * @param appointment
     * @return Paciente creado desde homebound
     * @throws Exception Error en la base de datos.
     */
    public Appointment updateSampleTake(Appointment appointment) throws Exception;

    /**
     * Realiza la actualizacion de la entrevista segun los datos enviados por
     * Homebound
     *
     * @param appointment
     * @return Paciente creado desde homebound
     * @throws Exception Error en la base de datos.
     */
    public boolean updateInterview(Appointment appointment) throws Exception;

    /**
     * Cancela el estado y trazabilidad de una orden desde
     * Homebound
     *
     * @param order
     * @throws Exception Error en la base de datos.
     */
    public void cancelOrderHomeBound(long order) throws Exception;

    /**
     * Actualiza una cita desde Homebound
     *
     * @param appointment
     * @return 
     * @throws Exception Error en la base de datos.
     */
    public long updateAppointmentHomeBound(Appointment appointment) throws Exception;

    /**
     * Lista las citas por un listado de ordenes.
     *
     * @param Orders
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Appointment> getAppointments(List<Long> Orders) throws Exception;
    
    /**
     * Consulta a un paciente por el id de la orden a la que este paciente fue asignado
     *
     * @param orders
     *
     * @return Paciente con información basica
     * @throws Exception Error en la base de datos.
     */
    public List<BasicHomeboundPatient> getPatientByOrderId( List<Long> orders) throws Exception;
    
    /**
     * Consulta una entrevista con sus preguntas y sus respectivas respuestas o respuesta
     * por el id de una orden
     *
     * @param orderId Id de la orden
     *
     * @return Entrevista con preguntas y respuestas
     * @throws Exception Error en la base de datos.
     */
    public List<QuestionHomeBound> getInterviewByOrderId(long orderId) throws Exception;
    
    /**
     * Consulta todos los items demográficos de un demográfico por el id de este
     *
     * @param demographicId
     *
     * @return Lista de items demográficos
     * @throws Exception Error en la base de datos.
     */
    public List<DemographicHomeBound> getItemDemographicsByDemoId(int demographicId) throws Exception;
    
    /**
     * Consulta todos los demográficos almacenados en el sistema
     *
     * @return Lista de demográficos
     * @throws Exception Error en la base de datos.
     */
    public List<DemographicHomeBound> getAllDemographics() throws Exception;
    
    /**
     * Consulta la auditoria de la orden
     *
     * @param orderNumber
     * @return Lista de demográficos
     * @throws Exception Error en la base de datos.
     */
    public Track getTracking(Long orderNumber) throws Exception;
    
    
    
    /**
     * Actualiza una orden proveniente de una cita de homebound
     *
     * @param appointment
     * @return True - Si la orden se actualizo, False- Si no fue así
     * @throws Exception Error en la base de datos.
     */
    public boolean updateAppointment(Appointment appointment) throws Exception;
    
    /**
     * Actualiza la fecha de atencion de una orden
     *
     * @param orderId 
     * @return True - Si la fecha de atendicon de la orden se actualizo, False- Si no fue así
     * @throws Exception Error en la base de datos.
     */
    public boolean updateAppointmentDate(long orderId) throws Exception;
    
    /**
     * Actualiza la fecha de tranporte de las muestras de una orden 
     *
     * @param transportSampleHomebound
     * @return True - Si la fecha de atendicon de la orden se actualizo, False- Si no fue así
     * @throws Exception Error en la base de datos.
     */
    public boolean updateTransportSample(List<TransportSampleHomebound> transportSampleHomebound) throws Exception;
    
    /**
     * Consulta las tarifas de un cliente por el id de este
     *
     * @param accountId
     * @return Lista de tarifas
     * @throws Exception Error en la base de datos.
     */
    public List<RateHomeBound> getRatesByAccountId(int accountId) throws Exception;
}

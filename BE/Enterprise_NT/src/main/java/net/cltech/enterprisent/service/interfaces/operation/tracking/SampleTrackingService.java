package net.cltech.enterprisent.service.interfaces.operation.tracking;

import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.skl.RequestSampleDestination;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.common.Reason;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.tracking.SampleDelayed;
import net.cltech.enterprisent.domain.operation.tracking.TestSampleTake;
import net.cltech.enterprisent.domain.operation.tracking.TestSampleTakeTracking;
import net.cltech.enterprisent.domain.operation.tracking.TrackingAlarm;
import net.cltech.enterprisent.domain.operation.tracking.VerifyDestination;

/**
 * Interfaz de servicios a la informacion de la verificacion de la muestra.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 19/09/2017
 * @see Creación
 */
public interface SampleTrackingService
{

    /**
     * Obtiene la información de la orden enviada.
     *
     * @param idOrder Id de la orden.
     * @param sampleTracking Indica si se consulta la trazabilidad de la
     * muestra.
     * @return Orden.
     * @throws Exception Error en la base de datos.
     */
    public Order getOrder(long idOrder, boolean sampleTracking) throws Exception;

    /**
     * Obtiene la información de la orden enviada.
     *
     * @param idOrder Id de la orden.
     * @param sampleTracking Indica si se consulta la trazabilidad de la
     * muestra.
     * @return Orden.
     * @throws Exception Error en la base de datos.
     */
    public Order getOrderTracking(long idOrder, boolean sampleTracking) throws Exception;

    /**
     * Inserta la trazabilidad de la muestra.
     *
     * @param idOrder Id de la orden.
     * @param codeSample Codigo de la muestra.
     * @param type Tipo de trazabilidad de la muestra.
     * @param reason Razon por la cual se rechazo o retoma la muestra.
     * @param retake Retoma.
     * @param skipAppointment
     * @return Si se inserto la trazabilidad de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public boolean sampleTracking(long idOrder, String codeSample, int type, Reason reason, Boolean retake, Boolean skipAppointment) throws Exception;

    /**
     * Obtener información de un resultado o examen de una orden.
     *
     * @param idOrder Id de la orden.
     * @param sample Id de la muestra.
     * @return Listado de Ordenes actualizado.
     *
     * @throws Exception Error
     */
    public List<AuditOperation> getTrackingSample(Long idOrder, Integer sample) throws Exception;

    /**
     * Inserta la trazabilidad de la muestra.
     *
     * @param idOrder Id de la orden.
     * @param codeSample Codigo de la muestra.
     * @return Si se inserto la trazabilidad de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public boolean sampleCheckRetakeTracking(long idOrder, String codeSample) throws Exception;

    /**
     * Inserta la trazabilidad de la muestra desde el middleware.
     *
     * @param idOrder Id de la orden.
     * @param codeSample Codigo de la muestra.
     * @param type Tipo de trazabilidad de la muestra.
     * @param sesion sesion de usuario que realiza la accion
     * @return Si se inserto la trazabilidad de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public boolean sampleTracking(long idOrder, String codeSample, int type, AuthorizedUser sesion) throws Exception;

    /**
     * Inserta la trazabilidad de la muestra por orden(Ingresadas)
     *
     * @param idOrder Numero de orden
     * @param session Sesion del usuario.
     * @param type Tipo de ingreso
     * @param origin Origen del llamado del servicio 1 -> Ingreso de ordenes, 2
     * -> Resultados
     * @param tests
     * @return Si se inserto la trazabilidad de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public boolean sampleOrdered(Long idOrder, AuthorizedUser session, int type, int origin, List<Integer> tests) throws Exception;

    /**
     * Inserta la trazabilidad de la muestra por orden(Ingresadas)
     *
     * @param idOrder Numero de orden
     * @param session Sesion del usuario.
     * @param type Tipo de ingreso
     * @param tests
     * @return Si se inserto la trazabilidad de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public boolean sampleOrdered(Long idOrder, AuthorizedUser session, int type, List<Integer> tests) throws Exception;

    /**
     * Inserta la trazabilidad de la muestra por orden(Ingresadas)
     *
     * @param idOrder Numero de orden
     * @param session Sesion del usuario.
     * @param type Tipo de ingreso
     * @param origin Origen del llamado del servicio 1 -> Ingreso de ordenes, 2
     * -> Resultados
     * @return Si se inserto la trazabilidad de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public boolean sampleOrdered(Long idOrder, AuthorizedUser session, int type, int origin) throws Exception;

    /**
     * Insertar la vericicacion de la muestra en el destino.
     *
     * @param verifyDestination Información para verificar la muestra.
     * @return Orden.
     * @throws Exception Error en la base de datos.
     */
    public boolean verifyDestination(VerifyDestination verifyDestination) throws Exception;

    /**
     * Consulta la ruta de destinos asociados a la orden insertada.
     *
     * @param idOrder Id de la orden.
     * @param codeSample Codigo de la muestra.
     * @return Retorna la ruta de destinos para la orden.
     *
     * @throws Exception Error en la base de datos.
     */
    public AssignmentDestination getDestinationRoute(long idOrder, String codeSample) throws Exception;

    /**
     * Obtiene las preguntas que se le deben aplicar a una orden.
     *
     * @param idOrder Orden a la cual se le realizara la entrevista.
     * @return Retorna la lista de preguntas que se le deben hacer a la orden.
     * @throws Exception Error en la base de datos.
     */
    public List<Question> getInterview(long idOrder) throws Exception;
    
    /**
     * Obtiene las preguntas que se le deben aplicar a una orden.
     *
     * @param idOrder Orden a la cual se le realizara la entrevista.
     * @return Retorna la lista de preguntas que se le deben hacer a la orden.
     * @throws Exception Error en la base de datos.
     */
    public List<Question> interviewsByOrder(long idOrder) throws Exception;

    /**
     * Obtiene las preguntas que se le deben aplicar a una orden.
     *
     * @param idOrder Orden a la cual se le realizara la entrevista.
     * @return Retorna la lista de preguntas que se le deben hacer a la orden.
     * @throws Exception Error en la base de datos.
     */
    public List<Question> getInterviewskl(long idOrder) throws Exception;

    /**
     * Indica si la entrevista de la orden ya se le han dado respuestas.
     *
     * @param idOrder Orden a la cual se le realizara la entrevista.
     * @return Indica si la entrevista de la orden ya se le han dado respuestas.
     * @throws Exception Error en la base de datos.
     */
    public boolean getInterviewAnswer(long idOrder) throws Exception;

    /**
     * Inserta los resultados de la entrevista de la orden.
     *
     * @param questions Preguntas que se deben insertar.
     * @param idOrder Id de la orden a rechazar.
     * @return Retorna la cantidad de registros insertados.
     *
     * @throws Exception Error en la base de datos.
     */
    public int insertResultInterview(List<Question> questions, long idOrder) throws Exception;

    /**
     * Obtiene las muestras de una orden.
     *
     * @param idOrder Orden.
     * @return Retorna la lista de muestras con sus respectivos examenes de la
     * orden.
     * @throws Exception Error en la base de datos.
     */
    public List<Sample> sampleOrder(long idOrder) throws Exception;

    /**
     * Obtiene las muestras de una orden.
     *
     * @param idOrder Orden.
     * @return Retorna la lista de muestras con sus respectivos examenes de la
     * orden.
     * @throws Exception Error en la base de datos.
     */
    public List<Sample> sampleOrderTracking(long idOrder) throws Exception;

    /**
     * Consulta las muestras retrasadas.
     *
     * @param idDestination Id del destino.
     * @return Retorna la lista de muestras retrasadas.
     *
     * @throws Exception Error en la base de datos.
     */
    public List<SampleDelayed> sampleDelayed(int idDestination) throws Exception;

    /**
     * Valida si una orden pertenece a una orden de consulta externa y si posee
     * entrevista aplicada.
     *
     * @param idOrder Id de la orden
     * @return net.cltech.enterprisent.domain.operation.tracking.trackingAlarm
     *
     * @throws Exception Error en la base de datos.
     */
    public TrackingAlarm trackingAlarmInterview(long idOrder) throws Exception;

    /**
     * Inserta la trazabilidad de la muestra.
     *
     * @param testSampleTake
     *
     * @return Si se inserto la trazabilidad de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public TestSampleTake sampleTrackingTest(TestSampleTake testSampleTake) throws Exception;

    /**
     * Obtiene Numero de ordenpara auditoria.
     *
     * @param orderNumber
     *
     * @return Si se inserto la trazabilidad de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public Order getAudit(long orderNumber) throws Exception;

    /**
     * Consulta las listas de examenes segun estado.
     *
     * @param idOrder
     * @param codeSample
     * @return Id de la ruta de destino inicial
     *
     * @throws Exception Error en la base de datos.
     */
    public List<TestSampleTakeTracking> listSampleTakeByOrder(long idOrder, String codeSample) throws Exception;

    /**
     * Actualiza el estado a verificado de una muestra
     *
     * @param requestSampleDestination
     * @return
     * @throws Exception Error en la base de datos.
     */
    public boolean updateStateToCheck(RequestSampleDestination requestSampleDestination) throws Exception;

    /**
     * Verifica que el numero de orden enviado si exista
     *
     * @param idOrder
     * @return True si existe - False si no existe
     * @throws Exception Error en la base de datos.
     */
    public boolean orderExists(long idOrder) throws Exception;

    /**
     * Verifica que una muestra ya se encuentre verificada
     *
     * @param idOrder
     * @param idRoute
     * @return True si existe - False si no existe
     * @throws Exception Error en la base de datos.
     */
    public boolean isSampleCheck(long idOrder, int idRoute) throws Exception;

    /**
     * Verifica si una muestra ya se encuentra verificada para verificaion
     * sencilla
     *
     * @param idOrder
     * @param sampleId
     * @return True si existe - False si no existe
     * @throws Exception Error en la base de datos.
     */
    public boolean isSampleCheckSimple(long idOrder, int sampleId) throws Exception;

    /**
     * Indica si la entrevista del destino ya se le han dado respuestas.
     *
     * @param idDestination
     * @return Indica si la entrevista del destino ya se le han dado respuestas.
     * @throws Exception Error en la base de datos.
     */
    public List<Question> getInterviewAnswerDestination(int idDestination) throws Exception;

    /**
     * Obtiene el objeto destino.
     *
     * @param idDestination
     * @return El objeto destino de la basee de datos.
     * @throws Exception Error en la base de datos.
     */
    public Destination getDestination(Integer idDestination) throws Exception;

    /**
     * Inserta los resultados de la entrevista de la orden con destino y sede.
     *
     * @param questions Preguntas que se deben insertar.
     * @param idOrder Id de la orden a rechazar.
     * @param idDestination
     * @param idBranch
     * @return Retorna la cantidad de registros insertados.
     *
     * @throws Exception Error en la base de datos.
     */
    public int insertResultInterviewDestination(List<Question> questions, long idOrder, int idSample, int idDestination, int idBranch) throws Exception;

    /**
     * Obtiene las preguntas que se le deben aplicar a una orden con destino y
     * sede.
     *
     * @param idOrder Orden a la cual se le realizara la entrevista.
     * @param idSample
     * @param idDestination
     * @param idBranch
     * @return Retorna la lista de preguntas que se le deben hacer a la orden.
     * @throws Exception Error en la base de datos.
     */
    public List<Question> getInterviewDestination(long idOrder, int idSample, int idDestination, int idBranch) throws Exception;

    /**
     * Inserta la trazabilidad de la muestra con temperatura.
     *
     * @param idOrder Id de la orden.
     * @param codeSample Codigo de la muestra.
     * @param type Tipo de trazabilidad de la muestra.
     * @param temperature
     * @return Si se inserto la trazabilidad de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public boolean sampleTrackingTemperature(long idOrder, String codeSample, int type, String temperature) throws Exception;

    /**
     * Consulta la ruta de destinos asociados a la orden desde el middleware
     * para un usuario analizador
     *
     * @param idOrder Id de la orden.
     * @param codeSample Codigo de la muestra.
     * @param idBranch Enviada desde el mensaje
     * @param user Usuario analizador
     * @return Retorna la ruta de destinos para la orden.
     *
     * @throws Exception Error en la base de datos.
     */
    public AssignmentDestination getDestinationRouteAnalyzer(long idOrder, String codeSample, int idBranch, User user) throws Exception;

    /**
     * Obtiene la información de la orden enviada.
     *
     * @param idOrder Id de la orden.
     * @param sampleTracking Indica si se consulta la trazabilidad de la
     * muestra.
     * @param idBranch
     * @param user Usuario analizador
     * @return Orden.
     * @throws Exception Error en la base de datos.
     */
    public Order getOrderAnalyzer(long idOrder, boolean sampleTracking, int idBranch, User user) throws Exception;

    /**
     * Inserta la trazabilidad de la muestra para los usuarios analizadores
     *
     * @param idOrder Id de la orden.
     * @param codeSample Codigo de la muestra.
     * @param type Tipo de trazabilidad de la muestra.
     * @param reason Razon por la cual se rechazo o retoma la muestra.
     * @param retake Retoma.
     * @param idBranch Enviado desde el mensaje
     * @param user Usuario analizador
     * @return Si se inserto la trazabilidad de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public boolean sampleTrackingAnalyzer(long idOrder, String codeSample, int type, Reason reason, Boolean retake, int idBranch, User user) throws Exception;

    /**
     * Insertar la vericicacion de la muestra en el destino desde un usuario
     * analizador
     *
     * @param verifyDestination Información para verificar la muestra.
     * @param user Usuario analizador
     * @return Orden.
     * @throws Exception Error en la base de datos.
     */
    public boolean verifyDestinationAnalyzer(VerifyDestination verifyDestination, User user) throws Exception;

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
    public List<Order> getSamplesByTemperatureAndDate(long initialDate, long endDate, int idSample, int idService) throws Exception;
}

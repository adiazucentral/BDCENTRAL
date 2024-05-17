package net.cltech.enterprisent.service.interfaces.operation.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.test.GeneralTemplateOption;
import net.cltech.enterprisent.domain.masters.test.OptionTemplate;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.operation.audit.AuditEvent;
import net.cltech.enterprisent.domain.operation.filters.MicrobiologyFilter;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobialDetection;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyGrowth;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyTask;
import net.cltech.enterprisent.domain.operation.microbiology.ResultMicrobiology;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.widgets.StateCount;

/**
 * Interfaz de servicios a la informacion de microbiologia
 *
 * @version 1.0.0
 * @author cmartin
 * @since 18/01/2018
 * @see Creación
 */
public interface MicrobiologyService
{

    /**
     * Obtiene la información de una orden por muestra para la verificación y
     * siembra en microbiologia.
     *
     * @param idOrder Id de la orden.
     * @param codeSample Codigo de la muestra.
     * @return Orden con información de microbiologia.
     * @throws Exception Error en la base de datos.
     */
    public MicrobiologyGrowth getOrderMicrobiology(Long idOrder, String codeSample) throws Exception;

    /**
     * Obtiene la información del antibiograma de una orden por examen.
     *
     * @param idOrder Id de la orden.
     * @param idTest Id del examen.
     * @return Orden con información de microbiologia.
     * @throws Exception Error en la base de datos.
     */
    public MicrobiologyGrowth getOrderMicrobiology(Long idOrder, Integer idTest) throws Exception;

    /**
     * Lista de examenes con medio de cultivo por muestra
     *
     * @param idSample Id de la muestra.
     * @param order
     *
     * @return Lista de examanes
     * {@link net.cltech.enterprisent.domain.masters.test.TestBasic}.
     * @throws Exception Error en la base de datos.
     */
    public List<TestBasic> getTestsMicrobiologySample(Integer idSample, Long order) throws Exception;

    /**
     * Obtiene la trazabilidad de una orden por muestra en microbiologia.
     *
     * @param idOrder Id de la orden.
     * @param codeSample Codigo de la muestra.
     * @return Orden con información de microbiologia.
     * @throws Exception Error en la base de datos.
     */
    public List<AuditEvent> listTrackingMicrobiology(Long idOrder, String codeSample) throws Exception;

    /**
     * Ingresa la información relacionada con el antibiograma asociado a una
     * orden y examen en la base de datos.
     *
     * @param microbiologyGrowth Instancia con los datos de la microbiologia.
     *
     * @return Instancia con las datos del medio de cultivo.
     * @throws Exception Error en la base de datos.
     */
    public boolean insertAntibiogramTest(MicrobiologyGrowth microbiologyGrowth) throws Exception;

    /**
     * Registra la verificación de microbiologia en la base de datos.
     *
     * @param microbiologyGrowth Instancia con los datos de la microbiologia.
     *
     * @return Instancia con las datos del medio de cultivo.
     * @throws Exception Error en la base de datos.
     */
    public boolean verifyMicrobiology(MicrobiologyGrowth microbiologyGrowth) throws Exception;

    /**
     * Actualiza la informacion de la siembra en la verificación
     *
     * @param growth informacion a actualizar
     * @return Registros afectados
     * @throws Exception Error en base de datos
     */
    public int updateVerificationMicrobiology(MicrobiologyGrowth growth) throws Exception;

    /**
     * Actualiza la informacion de la siembra cuando se realiza esta accion
     *
     * @param growth informacion a actualizar (medios de cultivo,
     * procedimientos, comentario, fecha y usuario de la siembra)
     * @return Registros afectados
     * @throws Exception Error en base de datos
     */
    public int updateGrowthMicrobiology(MicrobiologyGrowth growth) throws Exception;

    /**
     * Inserta la informacion de la detección microbial
     *
     * @param idOrder Numero de la orden.
     * @param idTest Examen.
     * @return Registros afectados
     * @throws Exception Error en base de datos
     */
    public MicrobialDetection getMicrobialDetection(Long idOrder, Integer idTest) throws Exception;
    
       /**
     * Inserta la informacion de la detección microbial
     *
     * @param idOrder Numero de la orden.
     * @param idTest Examen.
     * @param idMicroorganism Examen.
     * @return Registros afectados
     * @throws Exception Error en base de datos
     */
    public Microorganism getMicrobialDetectionMicroorganism(Long idOrder, Integer idTest, Integer idMicroorganism) throws Exception;

    /**
     * Inserta la informacion de la detección microbial
     *
     * @param microbialDetection informacion (orden, examen, microorganismos,
     * @param session usuario del mw
     * comentario, fecha y usuario)
     * @return Registros afectados
     * @throws Exception Error en base de datos
     */
    public int insertMicrobialDetection(MicrobialDetection microbialDetection, AuthorizedUser session) throws Exception;

     /**
     * Inserta la informacion de la detección microbial desde el mw
     *
     * @param microbialDetection informacion (orden, examen, microorganismos,
     * comentario, fecha y usuario)
     * @return Registros afectados
     * @throws Exception Error en base de datos
     */
    public int insertMicrobialDetection(MicrobialDetection microbialDetection) throws Exception;

    /**
     * Obtiene los resultados de examenes asociados a microbiologia, asi como la
     * siembra y verificación del mismo.
     *
     * @param microbiologyFilter Clase con los filtros especificos para
     * microbiologia.
     * @return Orden con información de microbiologia.
     * @throws Exception Error en la base de datos.
     */
    public List<ResultTest> listResultTestMicrobiology(MicrobiologyFilter microbiologyFilter) throws Exception;

    /**
     * Realiza conteo de estados pendientes
     *
     * @param microbiologyFilter filtro de la consulta
     * @return Conteo ordenes por estado pendiente
     * @throws Exception Error en el servicio
     */
    public StateCount getPendingStateCount(MicrobiologyFilter microbiologyFilter) throws Exception;

    /**
     * Obtiene los resultados de examenes asociados a microbiologia, asi como la
     * siembra y verificación del mismo.
     *
     * @param microbiologyFilter Clase con los filtros especificos para
     * microbiologia.
     * @return Orden con información de microbiologia.
     * @throws Exception Error en la base de datos.
     */
    public List<Order> listResultOrderMicrobiology(MicrobiologyFilter microbiologyFilter) throws Exception;

    /**
     * Consulta los antibioticos asociados al antibiograma de la detección
     * microbiana
     *
     * @param idMicrobialDetection Id de la detección microbiana
     * @param order
     * @return Lista de Resultados de Microbiologia
     * @throws Exception en la base de datos.
     */
    public List<ResultMicrobiology> listResultMicrobiologySensitivity(Integer idMicrobialDetection, Long order) throws Exception;

    /**
     * Inserta, actualiza y elimina antibioticos asociados a la deteccion
     * microbian.
     *
     * @param microorganism Microorganismo con antibioticos.
     * @param order
     * @return Cantidad de registros afectados.
     * @throws Exception en la base de datos.
     */
    public int insertResultMicrobiologySensitivity(Microorganism microorganism, Long order) throws Exception;
    
     /**
     * Inserta, actualiza y elimina antibioticos asociados a la deteccion
     * microbian.
     *
     * @param microorganism Microorganismo con antibioticos.
     * @param session
     * @return Cantidad de registros afectados.
     * @throws Exception en la base de datos.
     */
    public int insertResultMicrobiologySensitivity(Microorganism microorganism, AuthorizedUser session, Long Order) throws Exception;

    /**
     * Inserta tareas de microbiologia.
     *
     * @param microbiologyTask Tareas de Microbiologia.
     * @return Objeto Insertado.
     * @throws Exception en la base de datos.
     */
    public MicrobiologyTask insertTask(MicrobiologyTask microbiologyTask) throws Exception;

    /**
     * Obtiene la información de una orden por muestra con las tareas para los
     * medios de cultivo y procedimientos.
     *
     * @param idOrder Id de la orden.
     * @param codeSample Codigo de la muestra.
     * @return Orden con información de microbiologia.
     * @throws Exception Error en la base de datos.
     */
    public MicrobiologyGrowth getOrderMicrobiologyTask(Long idOrder, String codeSample) throws Exception;

    /**
     * Actualizar tareas de microbiologia.
     *
     * @param microbiologyTask Tareas de Microbiologia.
     * @return Objeto Actualizado.
     * @throws Exception en la base de datos.
     */
    public MicrobiologyTask updateTask(MicrobiologyTask microbiologyTask) throws Exception;

    /**
     * Obtiene la trazabilidad de las tareas de microbiologia.
     *
     * @param idOrder Numero de la Orden.
     * @param idTest Id del Examen.
     * @return Orden con información de los comentarios.
     * @throws Exception Error en la base de datos.
     */
    public List<MicrobiologyTask> listTrackingMicrobiologyTask(Long idOrder, Integer idTest) throws Exception;

    /**
     * Obtiene la trazabilidad de los comentarios de las tareas de
     * microbiologia.
     *
     * @param idOrder Numero de la Orden.
     * @param idTest Id del Examen.
     * @param id Id asociado de las tareas de microbiologia de una orden,
     * examen, medio de cultivo o procedimiento y destino.
     * @return Orden con información de los comentarios.
     * @throws Exception Error en la base de datos.
     */
    public List<AuditEvent> listTrackingMicrobiologyCommentTask(Long idOrder, Integer idTest, Integer id) throws Exception;

    /**
     * Obtiene y reporta las tareas de microbiologia por filtros especificos.
     *
     * @param microbiologyFilter Clase con los filtros especificos para reportar
     * tareas de microbiologia.
     * @return Orden con información de los comentarios.
     * @throws Exception Error en la base de datos.
     */
    public List<MicrobiologyTask> listTrackingMicrobiologyTaskReport(MicrobiologyFilter microbiologyFilter) throws Exception;

    /**
     * Reinicia tareas de microbiologia por filtros especificos.
     *
     * @param microbiologyFilter Clase con los filtros especificos para
     * reiniciar tareas de microbiologia.
     * @return Cantidad de datos afectados.
     * @throws Exception Error en la base de datos.
     */
    public int listTrackingMicrobiologyTaskRestart(MicrobiologyFilter microbiologyFilter) throws Exception;

    /**
     * Lista ordenes y exámenes pendientes de verificación de microbiología
     *
     * @param microbiologyFilter rango y tipo de filtro
     * @return Lista de ordenes
     * @throws Exception Error en el servicio
     */
    public List<OrderList> listPendingMicrobiologyVerification(MicrobiologyFilter microbiologyFilter) throws Exception;

    /**
     * Consulta la plantilla de resultados asociada a una orden y examen.
     *
     * @param idOrder Numero de Orden.
     * @param idTest Id del examen.
     * @return Plantilla de Resultados.
     * @throws java.lang.Exception Error en la base de datos.
     */
    public GeneralTemplateOption getGeneralTemplate(Long idOrder, Integer idTest) throws Exception;

    /**
     * Inserta la plantilla de resultados asociada a una orden y examen.
     *
     * @param templates Lista de plantillas a ser insertadas.
     * @param idOrder Numero de Orden.
     * @param idTest Id del examen.
     * @param userDatabank
     * @return Plantilla de Resultados.
     * @throws Exception Error en la base de datos.
     */
    public int insertResultTemplate(List<OptionTemplate> templates, Long idOrder, Integer idTest, Integer userDatabank) throws Exception;

}

package net.cltech.enterprisent.service.interfaces.operation.results;

import java.sql.Timestamp;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.resultados.DetailFilter;
import net.cltech.enterprisent.domain.integration.resultados.DetailStatus;
import net.cltech.enterprisent.domain.integration.resultados.ResultHeader;
import net.cltech.enterprisent.domain.integration.resultados.ResultHeaderFilter;
import net.cltech.enterprisent.domain.integration.resultados.TestDetail;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.interview.TypeInterview;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.operation.common.Reason;
import net.cltech.enterprisent.domain.operation.list.RemissionLaboratory;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.results.BatchResultFilter;
import net.cltech.enterprisent.domain.operation.results.CentralSystemResults;
import net.cltech.enterprisent.domain.operation.results.FindShippedOrders;
import net.cltech.enterprisent.domain.operation.results.HistoricalResult;
import net.cltech.enterprisent.domain.operation.results.HistoryFilter;
import net.cltech.enterprisent.domain.operation.results.ImageTest;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.domain.operation.results.ResultOrder;
import net.cltech.enterprisent.domain.operation.results.ResultOrderDetail;
import net.cltech.enterprisent.domain.operation.results.ResultStatistic;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.results.ResultTestBlock;
import net.cltech.enterprisent.domain.operation.results.ResultTestComment;
import net.cltech.enterprisent.domain.operation.results.ResultTestPrint;
import net.cltech.enterprisent.domain.operation.results.ResultTestRepetition;
import net.cltech.enterprisent.domain.operation.results.ResultTestStateOrder;
import net.cltech.enterprisent.domain.operation.results.ResultTestValidate;
import net.cltech.enterprisent.domain.operation.results.TestHistory;
import net.cltech.enterprisent.domain.operation.results.UpdateResult;
import net.cltech.enterprisent.domain.operation.tracking.TestInformationTracking;

/**
 * Interface para el servicio de gestión de órdenes en el módulo de resultados
 *
 * @version 1.0.0
 * @author jblanco
 * @since 04/07/2017
 * @see Creación
 */
public interface ResultsService
{

    /**
     * Obtiene la información de las estadísticas para el módulo de resultados
     *
     * @param filter filtro del módulo de resultados
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.ResultStatistic}
     * null en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public ResultStatistic getStatistic(ResultFilter filter) throws Exception;

    /**
     * Obtiene la lista de órdenes para el módulo de resultados
     *
     * @param filter filtro del módulo de resultados
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder} null
     * en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public List<ResultOrder> getOrders(ResultFilter filter) throws Exception;

    /**
     * Obtiene la lista de órdenes para el módulo de resultados.
     *
     * @param resultFilter Clase con filtros de resultados
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.ResultTest} null
     * en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public List<ResultTest> getTests(ResultFilter resultFilter) throws Exception;

    /**
     * Obtiene la lista de órdenes para el módulo de resultados.
     *
     * @param resultFilter Clase con filtros de resultados
     * @param orders Lista de ordenes
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.ResultTest} null
     * en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public List<ResultTest> getTests(ResultFilter resultFilter, List<Long> orders) throws Exception;

    /**
     * Obtiene la lista de órdenes para el módulo de resultados.
     *
     * @param order
     * @param test
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.ResultTest} null
     * en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public ResultTestComment getInternalComment(long order, int test) throws Exception;
    /**
     * Obtiene la lista de órdenes para el módulo de resultados.
     *
     * @param order     
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.ResultTest} null
     * en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public ResultTestComment getInternalObservations(long order) throws Exception;

    /**
     * Obtiene el comentario del resultado de un examen.
     *
     * @param orderNumber
     * @param idTest
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.ResultTest} null
     * en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public ResultTestComment getCommentResultTests(long orderNumber, int idTest) throws Exception;

    /**
     * Obtiene la lista de órdenes para el módulo de resultados
     *
     * @param orderId
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.ResultTest} null
     * en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public List<ResultTest> getTests(Long orderId) throws Exception;

    /**
     * Obtiene la lista de órdenes para el módulo de resultados
     *
     * @param orderId numero de orden
     * @param idTest
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.ResultTest} null
     * en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public List<ResultTest> getTests(List<Long> orderId, int idTest) throws Exception;

    /**
     * Obtiene la lista de órdenes para el módulo de resultados con la union de
     * los examenes de las ordenes hijas
     *
     *
     * @param orderId Número de orden
     * @param idOrdenHis Número de la orden externa
     * @param isConfidential Si es confidencial
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.ResultTest} null
     * en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public List<ResultTest> getTestsUnionDaughter(List<Long> orderId, String idOrdenHis, boolean isConfidential) throws Exception;
    
    /**
     * Realiza el reporte de un resultado
     *
     * @param obj
     *
     * @return ResultTest
     * @throws Exception Error en el servicio
     */
    public ResultTest reportedCommentTest(ResultTest obj) throws Exception;

    /**
     * Realiza el reporte de un resultado
     *
     * @param obj
     *
     * @return ResultTest
     * @throws Exception Error en el servicio
     */
    public ResultTest reportedTest(ResultTest obj) throws Exception;

    /**
     * Realiza el reporte de un resultado desde el middleware
     *
     * @param obj
     * @param usermw
     *
     * @return ResultTest
     * @throws Exception Error en el servicio
     */
    public ResultTest reportedTest(ResultTest obj, int usermw) throws Exception;

    /**
     * Se obtienen las gráficas de una orden y un examen
     *
     * @param order Numero de la orden
     * @param testId Id del examen
     * @return Objeto de imagenes de analizador
     * @throws Exception
     */
    public ImageTest getResultGraphics(long order, int testId) throws Exception;

    /**
     * Se obtienen las gráficas de una orden
     *
     * @param order Numero de la orden
     * @param testsfilter
     * @return Objeto de imagenes de analizador
     * @throws Exception
     */
    public List<ImageTest> getResultGraphics(long order, List<Integer> testsfilter) throws Exception;

    /**
     * Inserta las imagenes enviadas por el Middleware
     *
     * @param imageTest Image enviada del Middlware
     * @return True si inserta la imagen<br>False si hay errores en la insercion
     * @throws Exception Error en el servicio
     */
    public boolean middlewareImages(ImageTest imageTest) throws Exception;

    /**
     * Realiza la validación de un resultado
     *
     * @param orderId
     * @param testId
     * @param userId
     *
     * @throws Exception Error en el servicio
     */
    public void validatedTest(Long orderId, int testId, int userId) throws Exception;

    /**
     * Realiza la validación de un resultado
     *
     * @param orderId
     * @param testId
     * @param userId
     *
     * @throws Exception Error en el servicio
     */
    public void prevalidatedByTest(Long orderId, int testId, int userId) throws Exception;

    /**
     * Realiza la impresion del resultado
     *
     * @param orderId
     * @param testId
     * @param userId
     *
     * @throws Exception Error en el servicio
     */
    public void printedTest(Long orderId, int testId, int userId) throws Exception;

    /**
     * Actualiza los valores de referencia de los exámenes de la orden enviada
     *
     * @param order Se calculan los valores con respecto al paciente asignado en
     * la base de datos Se calculan los valores de los examenes enviados o de
     * todos los examenes si se envia una lista vacia.
     *
     * @return registros afectados
     * @throws Exception
     */
    public int updateReferenceValue(Order order) throws Exception;

    /**
     * Realiza el reporte de un resultado para varias ordenes
     *
     * @param filter filtro y datos a actualizar
     *
     * @return ResultTest
     * @throws Exception Error en el servicio
     */
    public int batchUpdateResult(BatchResultFilter filter) throws Exception;

    /**
     * Consulta el detalle de una orden para el módulo de registro de resultados
     *
     * @param orderId Identificador de la orden
     *
     * @return ResultOrderDetail
     * @throws Exception Error en el servicio
     */
    public ResultOrderDetail getOrderDetail(long orderId) throws Exception;

    /**
     * Actualiza el comentario a la orden en el módulo de registro de resultados
     *
     * @param obj
     *
     * @return ResultOrderDetail
     * @throws Exception Error en el servicio
     */
    public ResultOrderDetail updateOrderDetail(ResultOrderDetail obj) throws Exception;

    /**
     * Lista las repeticiones de un exámen
     *
     * @param orderNumber Numero de orden
     * @param testId id del exámen
     * @return Lista de repeticiones
     * @throws Exception Error en el servicio
     */
    public List<ResultTestRepetition> listTestRepetitions(Long orderNumber, int testId) throws Exception;

    /**
     * Lista las histórico de resultados de una o varias pruebas.
     *
     * @param filter Filtros para el historico de resultados.
     * @return Lista de resultados históricos
     * @throws Exception Error en el servicio
     */
    public List<TestHistory> listTestHistory(HistoryFilter filter) throws Exception;
    
    /**
     * Lista las histórico de resultados de una o varias pruebas.
     *
     * 
     * @param historyFilterIduserTypeUser
     * @return Lista de resultados históricos
     * @throws Exception Error en el servicio
     */
    public List<TestHistory> listTestHistoryFilterUser(HistoryFilter filter) throws Exception;
    

    /**
     * Realiza la validación de una lista de pruebas
     *
     * @param list Contiene la lista de pruebas para realizar la validación y la
     * entrevista de pánico.
     * @param user identicicarod del usuario que realiza la validación
     * @param isAlarms true: No se valida y se consultan las alarmas sin
     * validar, false: Se valida y no se consultan las alarmas
     * @return Lista de ResultTest
     * @throws Exception Error en el servicio
     */
    public ResultTestValidate validatedTests(ResultTestValidate list, AuthorizedUser user, boolean isAlarms) throws Exception;

    /**
     * Obtiene los examenes que tiene configuracion de resultado en el ingreso
     * para una orden
     *
     * @param order Numero de orden
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Test}
     * @throws Exception Error en el servicio
     */
    public List<Test> getTestToResultInOrderEntry(long order) throws Exception;

    /**
     * Cambia estado de exámenes a reportado
     *
     * @param order bean con número de orden y examenes a reportar(ResultTest)
     * @param idUser
     * @param personReceive
     * @param sendAutomaticResult
     * @return registros afectados
     * @throws Exception Error en el servicio
     */
    public int reportTests(Order order, int idUser, String personReceive, Boolean sendAutomaticResult) throws Exception;

    /**
     * Agregar o eliminar examanes de una orden.
     *
     * @param order Orden con examenes a ser ingresados y eliminados.
     * @param type Tipo.
     * @param user Usuario.
     * @return Lista de resultados de los examenes agregados.
     *
     * @throws Exception Error en el servicio.
     */
    public List<ResultTest> addRemoveTest(Order order, int type, AuthorizedUser user) throws Exception;

    /**
     * Obtiene todos los examenes, perfiles y paquetes registrados a una orden
     *
     * @param order Numero de Orden
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultTest}
     * @throws Exception Error en servicio
     */
    public List<ResultTest> get(long order) throws Exception;

    /**
     * Guarda notas internas al resultado de una orden
     *
     * @param comment
     */
    public ResultTestComment saveInternalComment(ResultTestComment comment);
    
    /**
     * Guarda notas internas al resultado de una orden
     *
     * @param comment
     */
    public ResultTestComment saveObservations(ResultTestComment comment);

    /**
     * Realiza el bloqueo o desbloqueo de una prueba
     *
     * @param block
     * @return Información del bloqueo de la prueba.
     *
     * @throws Exception Error en el servicio
     */
    public ResultTestBlock blockTest(ResultTestBlock block) throws Exception;

    /**
     * imprime o no imprime el resultado del examen
     *
     * @param test
     * @return Información del bloqueo de la prueba.
     *
     * @throws Exception Error en el servicio
     */
    public ResultTestPrint printTest(ResultTestPrint test) throws Exception;

    /**
     * Agrega información adicional a resultados.
     *
     * @param orders Listado de Ordenes.
     * @return Listado de Ordenes actualizado.
     *
     * @throws Exception Error
     */
    public List<Order> addAdditional(List<Order> orders) throws Exception;

    /**
     * Agrega información adicional a resultados.
     *
     * @param resultTests Listado de examenes.
     * @return Listado de Examenes actualizado.
     *
     * @throws Exception Error
     */
    public List<ResultTest> addTestAdditional(List<ResultTest> resultTests) throws Exception;

    /**
     * Obtener información de un resultado o examen de una orden.
     *
     * @param idOrder Id de la orden.
     * @param idTest Id del examen.
     * @return Listado de Ordenes actualizado.
     *
     * @throws Exception Error
     */
    public TestInformationTracking getInformation(Long idOrder, Integer idTest) throws Exception;

    /**
     * Obtener la entrevista de pánico.
     *
     * @return Listado de preguntas.
     *
     * @throws Exception Error
     */
    public List<Question> getPanicSurvey() throws Exception;

    /**
     * Obtiene los examenes que estan asociados a una orden por resultado de
     * ingreso
     *
     * @param order Numero de orden
     * @return Lista de pruebas
     * {@link net.cltech.enterprisent.domain.operation.orders.Test}
     * @throws Exception Error en el servicio
     */
    public List<Test> getTestToResultRegister(long order) throws Exception;

    /**
     * Obtiene la informacion de la orden con los examenes no validados
     *
     * @param orderNumber
     * @param demographics
     * @param action Si tiene una acción predeterminada
     * @param idSample El id de la muestra
     * @param laboratorys Ids de los laboratorios separados por comas
     * @return Lista de pruebas
     * {@link net.cltech.enterprisent.domain.operation.orders.Test}
     * @throws Exception Error en el servicio
     */
    public List<Order> orderResultList(Long orderNumber, List<Demographic> demographics, String action, String idSample, String laboratorys) throws Exception;
    
    public List<Order> orderResultListDeleteTest(Long orderNumber, List<Demographic> demographics, String action, String idSample, String laboratorys) throws Exception;

    /**
     * Obtiene la informacion de la orden con los examenes no validados y que
     * han sido remitidos
     *
     * @param orders
     * @param demographics
     * @param action Si tiene una acción predeterminada
     * @return Lista de pruebas
     * {@link net.cltech.enterprisent.domain.operation.orders.Test}
     * @throws Exception Error en el servicio
     */
    public List<Order> orderResultListRemision(RemissionLaboratory orders, List<Demographic> demographics, String action) throws Exception;

    /**
     * Obtiene la lista de ordenes por rango con examenes no validados
     *
     * @param resultFilter
     * @param demographics
     * @param action Si tiene una acción predeterminada
     * @param laboratorys Ids de los laboratorios separados por comas
     * @return Lista de pruebas
     * {@link net.cltech.enterprisent.domain.operation.orders.Test}
     * @throws Exception Error en el servicio
     */
    public List<Order> orderResultRange(ResultFilter resultFilter, List<Demographic> demographics, String action, String laboratorys) throws Exception;

    /**
     * Obtiene los examenes que estan asociados a una orden
     *
     * @param order Numero de orden
     * @param action Si tiene una acción predeterminada
     * @param idSample El id de la muestra
     * @param laboratorys Ids de los laboratorios separados por comas
     * @return Lista de pruebas
     * {@link net.cltech.enterprisent.domain.operation.orders.Test}
     * @throws Exception Error en el servicio
     */
    public List<Test> allTestByOrder(long order, String action, String idSample, String laboratorys) throws Exception;

    /**
     * Obtiene los numeros de ordenes por tango de fecha o rango de numero de
     * ordenes
     *
     * @param resultFilter
     * @return Lista de numeros de ordenes
     * @throws Exception Error en el servicio
     */
    public List<Long> rangeOrders(ResultFilter resultFilter) throws Exception;

    /**
     * Realiza la obtencion de resultados apartir de la formula en una lista de
     * pruebas
     *
     * @param list Contiene la lista de pruebas para realizar el calculo de los
     * resultados apartir de la formula
     * @param user
     * @return Lista de ResultTest
     * @throws Exception Error en el servicio
     */
    public ResultTestValidate assignFormulaValue(ResultTestValidate list, AuthorizedUser user) throws Exception;

    /**
     * Actualización de la orden por el respectivo numero de la orden y id del
     * examen
     *
     * @param updateResult
     * @throws Exception Error en la base de datos
     */
    public void updateResultForExam(UpdateResult updateResult) throws Exception;

    /**
     * Realiza la obtencion de resultados apartir de un numero de orden pruebas
     *
     * @param orderNumber
     * @return Lista de ResultTest
     * @throws Exception Error en el servicio
     */
    public Order getAudit(long orderNumber) throws Exception;

    /**
     * Realiza la obtencion de resultados apartir de la formula en una lista de
     * pruebas
     *
     * @param tests
     * @return Lista de ResultTest
     * @throws Exception Error en el servicio
     */
    public List<Sample> getSamples(List<ResultTest> tests) throws Exception;

    /**
     * Lista de nombre de Examen por orden y muestra
     *
     * @param order
     * @param sample Entidad con la información de la relación
     *
     * @return numero de registros afectados
     * @throws Exception Error base de datos
     */
    public List<TestBasic> getTestByOrderSample(long order, int sample) throws Exception;

    /**
     * Lista de nombre de Areas por orden y muestra
     *
     * @param order
     * @param sample Entidad con la información de la relación
     *
     * @return numero de registros afectados
     * @throws Exception Error base de datos
     */
    public List<Area> getAreasByOrderSample(long order, int sample) throws Exception;

    /**
     * Lista muestras pendiente por toma de muestra
     *
     * @param order
     *
     * @return numero de registros afectados
     * @throws Exception Error base de datos
     */
    public List<Sample> getSamplesToTake(long order) throws Exception;

    /**
     * Lista los perfiles de una orden
     *
     * @param order
     *
     * @return numero de registros afectados
     * @throws Exception Error base de datos
     */
    public List<Integer> getProfiles(long order) throws Exception;

    /**
     * Verifica si los hijos de un perfil estan tomados como marcados
     *
     * @param order
     * @param idProfile
     *
     * @return numero de registros afectados
     * @throws Exception Error base de datos
     */
    public boolean isAllChildTaked(long order, int idProfile) throws Exception;

    /**
     * Marca el perfil como tomado
     *
     * @param order
     * @param profile
     * @throws Exception Error base de datos
     */
    public void checkProfileAsTaked(long order, int profile) throws Exception;

    /**
     * Verifica si la muestra esta tomada
     *
     * @param order
     * @param codeSample
     * @return
     * @throws Exception Error base de datos
     */
    public boolean isSampleTaken(long order, String codeSample) throws Exception;

    /**
     * Obtiene un listado de ordenes dentro de un rango de fechas que aun no se
     * han enviado a un determinado sistema central
     *
     * @param centralSystemResults
     * @return Lista de ordenes que aun no se han enviado al sistema central
     * @throws Exception Error base de datos
     */
    public List<Order> sendOrderResultsCentralSystem(CentralSystemResults centralSystemResults) throws Exception;

    /**
     * Actualizamos el registro de resultados en el campo lab57c50 el cual
     * indica que ese examen de esa orden ya fue enviado a un sistema central
     *
     * @param idOrder
     * @param idTest
     * @param centralSystem
     * @return Lista de ordenes que aun no se han enviado al sistema central
     * @throws Exception Error base de datos
     */
    public int updateSentCentralSystem(long idOrder, int idTest, int centralSystem) throws Exception;

    /**
     * Listar de las ordenes enviadas a un sistema central externo
     *
     * @param idTest
     * @return Lista de ordenes que han sido enviado a un sistema central
     * externo
     * @throws Exception Error base de datos
     */
    public List<FindShippedOrders> findShippedOrdersCentralSystem(int idTest) throws Exception;

    /**
     * Eliminar registros de resultados desvalidados
     *
     * @param idOrden
     * @param idTest
     * @return Eliminacion de regitro por resultado desvalidado
     * @throws Exception Error base de datos
     */
    public int removeDevalidatedResults(Long idOrden, int idTest) throws Exception;

    /**
     * Obtiene la lista de órdenes para el módulo de resultados con la union de
     * los examenes de las ordenes hijas
     *
     *
     * @param orderId Número de orden
     * @param idOrdenHis Número de la orden externa
     * @param isConfidential Si es confidencial
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.ResultTest} null
     * en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public List<ResultTest> getTestsUnionDaughterApp(List<Long> orderId, String idOrdenHis, boolean isConfidential) throws Exception;

    /**
     * cambia el historico de un paciente al momento de ingresar una orden y un
     * examen
     *
     * @param orderId
     * @param testId
     * @param userId
     * @param state
     * @param historicalResult
     * @param result
     * @throws Exception Error base de datos
     */
    public void repeatPatientHistory(Long orderId, int testId, int userId, int state, HistoricalResult historicalResult, ResultTestStateOrder result) throws Exception;

    /**
     * Reporta el ingreso de un resultado de un examen a QM
     *
     * @param idTest
     * @param user usuario autenticado
     * @throws Exception Error base de datos
     */
    public void reportQM(int idTest, AuthorizedUser user) throws Exception;

    /**
     * Actualiza los valores de referencia de un examen de una orden sin
     * resultados
     *
     * @param idOrder
     * @return True - Actualización de valores de referencia exitosa, False - No
     * se actualizaron los valores de referencia de manera exitosa
     * @throws Exception Error en el servicio
     */
    public boolean updateReferenceValues(long idOrder) throws Exception;

    public int validatePathology(ResultTest obj) throws Exception;

    /**
     * Obtiene todos los examenes registrados en la ultima orden de un paciente
     *
     * @param order Numero de Orden
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Test}
     * @throws Exception Error en servicio
     */
    public List<Test> getTestsLastOrder(long order) throws Exception;

    /**
     * Obtiene todas las ordenes del dia que ya tengan resultados validados y no
     * sean confidenciales
     *
     * @param date
     * @return Lista de {String}
     * @throws Exception Error en servicio
     */
    public List<Order> getOrdersResults(long date) throws Exception;

    /**
     * Consulta las ordenes que tengan todos los examenes validados
     *
     * @param idOrder
     * @return True - Actualización de valores de referencia exitosa, False - No
     * se actualizaron los valores de referencia de manera exitosa
     * @throws Exception Error en el servicio
     */
    public boolean getProccesOrder(long idOrder) throws Exception;

    /**
     *
     * @param result
     * @throws Exception Error en la base de datos
     */
    public void validsendMailPathology(ResultTest result) throws Exception;

    /**
     * Obtener los examenes asociados a la entrevista de pánico.
     *
     * @return Listado de examenes.
     *
     * @throws Exception Error
     */
    public List<TypeInterview> getTestPanicSurvey() throws Exception;

    /**
     *
     * @param result
     * @param order
     * @param idUser
     * @param reason
     * @param sample
     * @throws Exception Error en la base de datos
     */
    public void validsendMailRetake(List<Test> result, Long order, int idUser, Reason reason, Sample sample) throws Exception;

    /**
     * obtener el estado minimo de la lista de examenes de una orden.
     *
     * @param orderId Número de orden de laboratorio
     *
     * @return estado minimo
     * @throws Exception Error en el servicio
     */
    public int checkValidationOrder(Long orderId) throws Exception;

    /**
     * obtener el estado minimo de la lista de examenes de una orden.
     *
     * @param orderId Número de orden de laboratorio
     *
     * @return estado minimo
     * @throws Exception Error en el servicio
     */
    public void updateFechaIngresoDate(Long orderId, int test, Timestamp resuldate) throws Exception;
    
    /**
     * Obtiene la lista de órdenes para el módulo de resultados
     *
     * @param filter filtro del módulo de resultados
     *
     * @return null
     * @throws Exception Error en el servicio
     */
    public List<ResultHeader> header(ResultHeaderFilter filter) throws Exception;
    
    /**
     * Obtiene el detalle de los examenes para el módulo de resultados
     *
     * @param filter filtro del módulo de resultados
     *
     * @return null en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public List<TestDetail> detail(DetailFilter filter) throws Exception;
    
    /**
     * Marca el examen por sistema central
     *
     * @param status Datos del envio
     *
     * @return null en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public DetailStatus status(DetailStatus status) throws Exception;   
}

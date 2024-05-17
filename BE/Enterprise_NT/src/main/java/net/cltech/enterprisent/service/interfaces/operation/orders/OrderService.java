package net.cltech.enterprisent.service.interfaces.operation.orders;

import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.integration.orderForExternal.OrderForExternal;
import net.cltech.enterprisent.domain.integration.sendOrderExternalLIS.IdsPatientOrderTest;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.domain.masters.test.Requirement;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.billing.FilterTestPrice;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.FilterSubsequentPayments;
import net.cltech.enterprisent.domain.operation.orders.InconsistentOrder;
import net.cltech.enterprisent.domain.operation.orders.LastOrderPatient;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderNumSearch;
import net.cltech.enterprisent.domain.operation.orders.OrderSearch;
import net.cltech.enterprisent.domain.operation.orders.OrderTestDetail;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.ShiftOrder;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.orders.TestPrice;
import net.cltech.enterprisent.domain.operation.orders.TicketTest;
import net.cltech.enterprisent.domain.operation.tracking.PackageTracking;

/**
 * Servicios sobre las ordenes del sistema
 *
 * @version 1.0.0
 * @author dcortes
 * @since 17/07/2017
 * @see Creacion
 */
public interface OrderService
{

    /**
     * Obtiene una orden por numero de orden
     *
     * @param orderNumber Numero de orden
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null en caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public Order get(long orderNumber) throws Exception;
    
    /**
     * Obtiene una orden por numero de orden
     *
     * @param orderNumber Numero de orden
     * @param appointment
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null en caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public Order get(long orderNumber, int appointment) throws Exception;

    /**
     * Obtiene una orden por numero de orden
     *
     * @param orderNumber Numero de orden
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null en caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public OrderForExternal getforBuilder(IdsPatientOrderTest patientOrderTest) throws Exception;

    /**
     * Obtiene una orden por numero de orden, con examenes padres
     *
     * @param orderNumber Numero de orden
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null en caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public Order getEntry(long orderNumber) throws Exception;
    
            /**
     * Obtiene una orden por numero de orden
     *
     * @param orderNumber Numero de orden
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null en caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public Order getOrder(long orderNumber) throws Exception;
    
    /**
     * Obtiene una orden por numero de orden, con examenes padres
     *
     * @param orderNumber Numero de orden
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null en caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public Order getOrderEditTest(long orderNumber) throws Exception;
    
    /**
     * Obtiene una orden por numero de orden, con examenes padres
     *
     * @param orderNumber Numero de orden
     * @param appointment
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null en caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public Order getEntry(long orderNumber, int appointment) throws Exception;
      /**
     * Obtiene una los demograficos de una orden
     *
     * @param orderNumber Numero de orden
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null en caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public Order getEmail(long orderNumber) throws Exception;
    
    /**
     * Obtiene una los demograficos de una orden
     *
     * @param orderNumber Numero de orden
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null en caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public Order getConfigPrint(long orderNumber) throws Exception;

    /**
     * Obtiene una orden por numero de orden, con la informacion de todos los
     * examenes hijos
     *
     * @param orderNumber Numero de orden
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null en caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public Order getAudit(long orderNumber) throws Exception;

    /**
     * Obtiene las ordenes activas por fecha de registro
     *
     * @param dateShort Fecha en formato yyyyMMdd
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacio en
     * caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public List<Order> get(int dateShort) throws Exception;

    /**
     * Obtiene las ordenes buscandolas por apellido del paciente
     *
     * @param lastName Apellido
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacio en
     * caso de no tener coincidencias
     * @throws Exception Error en el servicio
     */
    public List<Order> getByLastName(String lastName) throws Exception;

    /**
     * Obtiene las ordenes buscandolas por nombre del paciente
     *
     * @param name Nombre
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacio en
     * caso de no tener coincidencias
     * @throws Exception Error en el servicio
     */
    public List<Order> getByName(String name) throws Exception;

    /**
     * Obtiene las ordenes asignadas a un numero de historia de un paciente
     *
     * @param patientId Historia del paciente
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacio en
     * caso de no tener coincidencias
     *
     * @return
     *
     * @throws Exception Error en base de datos
     */
    public List<Order> getByPatientId(String patientId) throws Exception;

    /**
     * Obtiene los datos de la orden como una lista de demograficos
     *
     * @param orderNumber Numero de la orden
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.DemographicValue}
     * @throws Exception Error presentado en el servicio
     */
    public List<DemographicValue> getAsDemographicList(long orderNumber, int appointment) throws Exception;

    /**
     * Crea una nueva orden en el sistema generando el autonumerico de la orden
     *
     * @param order
     * {@link net.cltech.enterprisent.domain.operation.orders.Order} orden a ser
     * guardada
     * @param user Id de usuario que realiza la insercion
     * @param branch
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order} con
     * el numero de orden generado
     * @throws Exception Error presentado en el servicio
     */
    public Order create(Order order, int user, int branch) throws Exception;

    /**
     * Crea una nueva orden en el sistema generando el autonumerico de la orden
     *
     * @param order
     * {@link net.cltech.enterprisent.domain.operation.orders.Order} orden a ser
     * guardada
     * @param user Id de usuario que realiza la insercion
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order} con
     * el numero de orden generado
     * @throws Exception Error presentado en el servicio
     */
    public Order create(Order order, int user) throws Exception;

    /**
     * Crea una nueva orden en el sistema Enviada desde un interface de ingreso
     *
     * @param order
     * {@link net.cltech.enterprisent.domain.operation.orders.Order} orden a ser
     * guardada
     * @param user Id de usuario que realiza la insercion
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order} con
     * el numero de orden generado
     * @throws Exception Error presentado en el servicio
     */
    public Order createIngreso(Order order, int user) throws Exception;

    /**
     * Crea ordenes en el sistema generando el autonumerico de la orden o por un
     * rango especifico.
     *
     * @param order Objeto de la orden a ser creada
     * @param init Orden Inicial
     * @param quantity Cantidad
     * @param type Tipo para generar ordenes por rango.
     * @param user Id de usuario que realiza la insercion
     * @param branch
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order} con
     * el numero de orden generado
     * @throws Exception Error presentado en el servicio
     */
    public List<String> createBatch(Order order, Long init, int quantity, int type, int user, int branch) throws Exception;

    /**
     * Actualiza la orden
     * {@link net.cltech.enterprisent.domain.operation.orders.Order} orden a ser
     * guardada
     *
     * @param order
     * @param user Id de usuario que realiza la actualizacion
     * @param branch
     * @param appointment
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order} con
     * la orden generada
     * @throws Exception Error presentado en el servicio
     */
    public Order update(Order order, int user, int branch, int appointment) throws Exception;

    /**
     * Obtiene los examenes a eliminar de un perfil
     * {@link net.cltech.enterprisent.domain.operation.orders.Order} orden a ser
     * guardada
     *
     * @param tests
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order} con
     * la orden generada
     * @throws Exception Error presentado en el servicio
     */
    public List<Test> getTestToDelete(List<Test> tests) throws Exception;

    /**
     * Lista de demograficos fijos y variables asociados al origen seleccionado
     *
     * @param origin
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.demographic.Demographic}
     * @throws Exception Error presentado en el servicio
     */
    public List<Demographic> getDemographics(String origin) throws Exception;

    /**
     * Lista de demograficos fijos y variables asociados al origen seleccionado
     *
     * @param account
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.demographic.Demographic}
     * @throws Exception Error presentado en el servicio
     */
    public List<DemographicItem> getRateByAccount(int account) throws Exception;

    /**
     * Asigna una orden sin paciente a un paciente
     *
     * @param order objeto orden con numero de la orden y id del paciente
     *
     * @return registros afectados
     * @throws Exception Error en el servicio
     */
    public int assignOrder(Order order) throws Exception;

    /**
     * Asigna una orden sin paciente a un paciente.(Verificación)
     *
     * @param order Objeto orden con numero de la orden y id del paciente
     *
     * @throws Exception Error en el servicio
     */
    public void assignOrderVerify(Order order) throws Exception;

    /**
     * Realiza la activación de las ordenes enviadas
     *
     * @param orders lista con los números de ordenes
     *
     * @return Listado de las ordenes activadas
     * @throws Exception Error
     */
    public List<Order> activate(List<Long> orders) throws Exception;

    /**
     * Inserta o elimina examenes de la orden.
     *
     * @param order Numero de la Orden
     * @param user Usuario en crear examenes
     * @param date Fecha actual
     * @param branch Sede
     * @param orderType Tipo de Orden
     * @param check
     * @param tracking Indica si se guardara la trazabilidad
     *
     * @return Lista de id examen
     * @throws Exception
     */
    public List<Integer> saveTests(Order order, int user, Date date, int branch, int orderType, boolean check, boolean tracking) throws Exception;

    public Test saveTest(Test test, int branch, int orderType) throws Exception;

    /**
     * Obtiene el precio de una examen por tarifa y la vigencia activa
     *
     * @param test Id del examen
     * @param rate Id de la tarifa
     *
     * @return Precio del examen
     * @throws Exception Error
     */
    public TestPrice getPriceTest(int test, int rate) throws Exception;

    /**
     * Obtiene la tarifa de una orden
     *
     *
     * @param order
     * @return id de la tarifa
     * @throws Exception Error
     */
    public int getRateOrder(Long order) throws Exception;
    
    
    /**
     * Obtiene el tipo de orden de una orden
     *
     *
     * @param order
     * @return id de la tarifa
     * @throws Exception Error
     */
    public int getTypeOrder(Long order) throws Exception;

    /**
     * Busca ordenes de acuerdo a los criterios enviados
     *
     * @param documentType Id tipo documento (si se envia null no se busca por
     * este filtro)
     * @param patientId Historia (si se envia null no se busca por este filtro)
     * @param lastName Apellido (si se envia null no se busca por este filtro)
     * @param surName Segundo Apellido (si se envia null no se busca por este
     * filtro)
     * @param name1 Nombre 1 (si se envia null no se busca por este filtro)
     * @param name2 Nombre 2 (si se envia null no se busca por este filtro)
     * @param sex Sexo (si se envia null no se busca por este filtro)
     * @param birthday Fecha de Nacimiento (si se envia null no se busca por
     * este filtro)
     * @param branch Id Sede de consulta, -1 si no se manejan sedes
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch},
     * vacio si no se encuentra ninguna orden
     * @throws Exception Errores presentados en el servicio
     */
    public List<OrderSearch> getByPatientInfo(Integer documentType, String patientId, String lastName, String surName, String name1, String name2, Integer sex, Date birthday, int branch) throws Exception;

    /**
     * Busca por numero de orden
     *
     * @param order Numero de orden
     * @param branch Id Sede, -1 en caso de no realizar filtro por sede
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch}, null
     * en caso de no encontrar
     * @throws Exception Error en el servicio
     */
    public OrderSearch getByOrder(long order, int branch) throws Exception;

    /**
     * Busca las ordenes por fecha de ingreso
     *
     * @param date Fecha en formato YYYYMMDD
     * @param branch Id Sede, -1 en caso de no realizar filtro por sede
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch}
     * @throws Exception Error en el servicio
     */
    public List<OrderSearch> getByEntryDate(int date, int branch) throws Exception;

    /**
     * Busca el numero de ordene por fecha de ingreso
     *
     * @param date Fecha en formato YYYYMMDD
     * @param branch Id Sede, -1 en caso de no realizar filtro por sede
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch}
     * @throws Exception Error en el servicio
     */
    public OrderNumSearch getByEntryDateN(int date, int branch) throws Exception;

    /**
     * Busca todas las ordenes por fecha de ingreso
     *
     * @param date Fecha en formato YYYYMMDD
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch}
     * @throws Exception Error en el servicio
     */
    public List<OrderSearch> ordersByEntryDate(int date) throws Exception;

    /**
     * Obtiene los examenes para el talon de reclamación
     *
     * @param order Numero de Orden
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.TicketTest}
     * @throws Exception Error en no controlado en el servicio
     */
    public List<TicketTest> getTicketTest(long order) throws Exception;

    /**
     * Obtiene elementos de configuracion de un examen cuando esta siendo
     * ingresado a una orden: Precio, Vigencia, Muestas y Recipientes
     *
     * @param patientId Id base de datos paciente (-1 si el paciente es nuevo)
     * @param testId Id base de datos examen
     * @param testType Tipo de examen
     * @param rateId Id tarifa seleccionada (-1 si no se maneja tarifa)
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderTestDetail},
     * null si el id examen enviado no es encontrado en la base de datos
     * @throws Exception Error en el servicio
     */
    public OrderTestDetail getOrderTestDetail(int patientId, int testId, int testType, int rateId) throws Exception;

    /**
     * Obtiene las ordenes con examenes para nueva toma
     *
     * @param order Numero de Orden
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch}
     * @throws Exception Error presentado en el servicio
     */
    public List<OrderSearch> getRecall(long order) throws Exception;

    /**
     * Obtiene las ordenes con examenes para nueva toma
     *
     * @param documentTypeId Id tipo documento
     * @param patientId Historia
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch}
     * @throws Exception Error presentado en el servicio
     */
    public List<OrderSearch> getRecall(int documentTypeId, String patientId) throws Exception;

    /**
     * Obtiene las ordenes con examenes para nueva toma
     *
     * @param lastName Apellido, vacio si no se realiza filtro
     * @param surName Segundo Apellido, vacio si no se realiza filtro
     * @param name1 Nombre, vacio si no se realiza filtro
     * @param name2 Segundo Nombre, vacio si no se realiza filtro
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch}
     * @throws Exception Error presentado en el servicio
     */
    public List<OrderSearch> getRecall(String lastName, String surName, String name1, String name2) throws Exception;

    /**
     * Obtiene las ordenes con examenes para nueva toma
     *
     * @param dateI Fecha inicial en formato YYYYMMDD
     * @param dateF Fecha final en formato YYYYMMDD
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.OrderSearch}
     * @throws Exception Error presentado en el servicio
     */
    public List<OrderSearch> getRecall(int dateI, int dateF) throws Exception;

    /**
     * Realiza el rellamado de una orden
     *
     * @param order Numero de orden para realizar rellamado
     * @param user Id usuario realiza el rellamado
     * @param branch
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * orden creada como rellamado
     * @throws Exception Error presentado en el servicio
     */
    public Order recall(long order, int user, int branch) throws Exception;

    /**
     * Realiza la cancelacion de una orden
     *
     * @param order Numero de orden
     * @param user Id usuario
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order}
     * @throws Exception Error presentado en el servicio
     */
    public Order cancel(long order, int user) throws Exception;

    /**
     * Realiza el registro de los diagnosticos por order
     *
     * @param order Instancia del objeto orden con la lista de diagnosticos
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order}
     * @throws Exception Error presentado en el servicio
     */
    public int createDiagnosticsByOrder(Order order) throws Exception;

    /**
     * Realiza la actualizacion de los diagnosticos por order
     *
     * @param order Instancia del objeto orden con la lista de diagnosticos
     * @return id de la orden
     * @throws Exception Error presentado en el servicio
     */
    public int updateDiagnosticsByOrder(Order order) throws Exception;
    
      /**
     * Realiza la actualizacion de los diagnosticos por order
     *
     * @param order Instancia del objeto orden con la lista de diagnosticos
     * @return id de la orden
     * @throws Exception Error presentado en el servicio
     */
    public Order updateConfigPrint(Order order) throws Exception;

    /**
     * Lista de ordenes sin turno
     *
     * @param history Historia relacionada a la orden
     * @param type tipo de documento
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order}
     * @throws Exception Error presentado en el servicio
     */
    public List<Order> withoutTurn(String history, String type) throws Exception;
    
    /**
     * Lista de ordenes sin turno
     *
     * @param history Historia relacionada a la orden
     * @param type tipo de documento
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order}
     * @throws Exception Error presentado en el servicio
     */
    public List<Order> withoutAppointmentTurn(String history, String type) throws Exception;

    /**
     * Relacion entre turno y ordenes
     *
     * @param object Relacion entre turno y ordenes
     * @return numero de elementos modificados
     * @throws Exception Error presentado en el servicio
     */
    public int shiftOrders(ShiftOrder object) throws Exception;

    /**
     * Lista de ordenes por turno
     *
     * @param turn Turno
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order}
     * @throws Exception Error presentado en el servicio
     */
    public List<Order> ordersByTurn(String turn) throws Exception;

    /**
     * Numero de ordenes por dia.
     *
     * @param date Fecha de la creacion de la orden
     * @param idUser Id del usuario que crea la orden
     * @param idBranch Id de la sede que crea la orden
     * @return Retorna la lista de inconsistencias.
     * @throws java.lang.Exception
     */
    public long numberOrders(int date, Integer idUser, Integer idBranch) throws Exception;

    /**
     * Busca ordenes con examenes en estado de retoma de acuerdo a los criterios
     * enviados
     *
     * @param documentType Id tipo documento (si se envia null no se busca por
     * este filtro)
     * @param patientId Historia (si se envia null no se busca por este filtro)
     * @param lastName Apellido (si se envia null no se busca por este filtro)
     * @param surName Segundo Apellido (si se envia null no se busca por este
     * filtro)
     * @param name1 Nombre 1 (si se envia null no se busca por este filtro)
     * @param name2 Nombre 2 (si se envia null no se busca por este filtro)
     * @param sex Sexo (si se envia null no se busca por este filtro)
     * @param birthday Fecha de Nacimiento (si se envia null no se busca por
     * este filtro)
     * @param branch Id Sede de consulta, -1 si no se manejan sedes
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacio si
     * no se encuentra ninguna orden
     * @throws Exception Errores presentados en el servicio
     */
    public List<Order> getByPatientInfoRecalled(Integer documentType, String patientId, String lastName, String surName, String name1, String name2, Integer sex, Date birthday, int branch) throws Exception;

    /**
     * Busca por numero de orden con examenes en estado de retoma
     *
     * @param order Numero de orden
     * @param branch Id Sede, -1 en caso de no realizar filtro por sede
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null en caso de no encontrar
     * @throws Exception Error en el servicio
     */
    public Order getByOrderRecalled(long order, int branch) throws Exception;

    /**
     * Busca las ordenes con examenes en estado de retoma por fecha de ingreso
     *
     * @param date Fecha en formato YYYYMMDD
     * @param branch Id Sede, -1 en caso de no realizar filtro por sede
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}
     * @throws Exception Error en el servicio
     */
    public List<Order> getByEntryDateRecalled(int date, int branch) throws Exception;

    /**
     * Busca una orden por numero de orden y valida si es padre o hija de otra y
     * trae el respectivo numero de orden
     *
     * @param order Numero de orden
     * @return Informacion basica de la orden
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}
     * @throws Exception Error en el servicio
     */
    public Order getRecalledOrder(long order) throws Exception;

    /**
     * Busca la orden LIS que corresponde a al numero de la orden HIS mandado
     * como parametro y modifica el turno
     *
     * @param order Instancia del objeto orden con la lista de diagnosticos
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order}
     * @throws Exception Error presentado en el servicio
     */
    public Order updateTurnOrderhis(Order order) throws Exception;

    /**
     * Valida si el número de orden es el indicado añadiendo lo que haga falta a
     * este
     *
     * @param idOrder
     * @return True - si este número de orden existe, False - si este no existe
     * @throws Exception Error presentado en el servicio
     */
    public boolean isValidOrder(long idOrder) throws Exception;

    /**
     * Guarda los precios de los examenes de la orden.
     *
     * @param order
     * @param insert
     * @throws Exception Error presentado en el servicio
     */
    public void savePricesOrder(Order order, boolean insert) throws Exception;

    /**
     * Obtiene todos los requerimientos asociados a una orden
     *
     * @param idOrder
     * @return Requerimientos de la orden en un string
     * @throws Exception Error presentado en el servicio
     */
    public String getRequirements(long idOrder) throws Exception;

    /**
     * Obtiene los usuarios q han validado la orden con su respectiva firma
     *
     * @param idOrder
     * @return lista de usuarios
     * @throws Exception Error presentado en el servicio
     */
    public List<User> getUserValidate(long idOrder) throws Exception;

    /**
     * Obtiene un paciente con el id de la orden del sistema externo
     *
     * @param idOrderExt id de la ordel del sistema externo
     * @return Paciente asignado a dicha orden del sistema externo
     * @throws Exception Error presentado en el servicio
     */
    public Patient getPatientForExternalIdOrder(String idOrderExt) throws Exception;

    /**
     * Verifica la existencia de una orden LIS con el id de una orden de un
     * sistema externo
     *
     * @param idOrderExt id de la ordel del sistema externo
     * @return True - la orden LIS existe, False - la orden LIS no existe
     * @throws Exception Error presentado en el servicio
     */
    public boolean orderExistsByExternalSystemOrder(String idOrderExt) throws Exception;

    /**
     * Consulta un listado de ordenes por un filtro en especifico, obteniendo
     * las ordenes para la realizacion de un pago posterior.
     *
     * @param filter
     * @return Lista de ids de las ordenes
     * @throws Exception Error en el servicio
     */
    public List<Long> subsequentPayments(FilterSubsequentPayments filter) throws Exception;

    /**
     * Actualiza el historico de un paciente
     *
     * @param idOrder
     * @param sample
     * @param patient
     * @return Lista de ids de las ordenes
     * @throws Exception Error en el servicio
     */
    public Boolean updateOrderhistory(Long idOrder, int sample, Integer patient) throws Exception;

    /**
     * Obtiene la ultima orden de un paciente con examenes
     *
     * @param id id del paciente
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order},
     * null en caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public LastOrderPatient getLastOrder(int id) throws Exception;

    /**
     * Obtiene las ordenes con inconsistencias
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacio en
     * caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public List<InconsistentOrder> getOrdersWithInconsistencies() throws Exception;
    
    
    /**
     * Obtiene el precio de una lista de examenes por tarifa y la vigencia activa
     *
     * @param filter Filtros de busqueda
     *
     * @return Lista de examenes con sus precios 
     * @throws Exception Error
     */
    public List<TestPrice> getPricesTests(FilterTestPrice filter) throws Exception;
    
    /**
     * Obtiene los examenes a eliminar de un perfil o de un paquete
     * {@link net.cltech.enterprisent.domain.operation.orders.Order} orden a ser
     * guardada
     *
     * @param tests lista de examenes a consultar
     * @param idOrder numero de la orden
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order} con
     * la orden generada
     * @throws Exception Error presentado en el servicio
     */
    public List<Test> getTestToDeleteOrder(List<Test> tests, long idOrder) throws Exception;
    
     /**
     * Inserta la auditoria de los paquetes de una orden
     *
     * @param tracking Lista de auditoria
     *
     * @throws Exception Error presentado en el servicio
     */
    public void packageTracking(List<PackageTracking> tracking) throws Exception;
    
    /**
     * Lista de demograficos fijos y variables asociados al origen seleccionado
     *
     * @param idOrder Id de la orden
     *
     * @return Lista de auditoria
     * @throws Exception Error presentado en el servicio
     */
    public List<PackageTracking> getPackageTracking(Long idOrder) throws Exception;
    
    public List<Requirement> getRequirement(long orderNumber) throws Exception;
}

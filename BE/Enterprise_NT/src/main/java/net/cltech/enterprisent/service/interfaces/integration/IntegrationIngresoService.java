package net.cltech.enterprisent.service.interfaces.integration;

import java.util.List;
import net.cltech.enterprisent.domain.DTO.integration.order.OrderDto;
import net.cltech.enterprisent.domain.DTO.integration.order.ServiceResponse;
import net.cltech.enterprisent.domain.integration.ingreso.RequestBarCode;
import net.cltech.enterprisent.domain.integration.ingreso.RequestCentralCode;
import net.cltech.enterprisent.domain.integration.ingreso.RequestDemographicAuto;
import net.cltech.enterprisent.domain.integration.ingreso.RequestHomologationDemographicIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.RequestHomologationTestIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.RequestTest;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsHomologationDemographicIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsHomologationTestIngreso;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsSample;
import net.cltech.enterprisent.domain.integration.ingreso.ResponseBarCode;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.results.ResultTest;

/**
 * Interface para integracion de interfaz de ingreso
 *
 * @version 1.0.0
 * @author BValero
 * @since 22/01/2020
 * @see Creación
 */
public interface IntegrationIngresoService
{

    /**
     * Homologa demograficos para interfaz de ingreso
     *
     * @param homo Objeto con la informacion de los demograficos y id del
     * sistema central
     * @return Demograficos homologados
     * @throws java.lang.Exception
     */
    public ResponsHomologationDemographicIngreso demographicHomologation(RequestHomologationDemographicIngreso homo) throws Exception;

    /**
     * Obtiene una peticion de homologacion de Examen y retorna un tipo de
     * objeto en respuesta a dicha peticion
     *
     * @param requestHomologationTestIngreso
     * @return Objeto de respuesta - ResponsTestIngreso
     * @throws Exception Error de base de datos
     */
    public ResponsHomologationTestIngreso testHomologation(RequestHomologationTestIngreso requestHomologationTestIngreso) throws Exception;

    /**
     * Obtiene una peticion de homologacion de demografico y se encarga de
     * verificar que no exista, en dado caso que este demografico no exista, lo
     * crea y homologa.
     *
     * @param requestDemographic
     * @return Me retorna exactamente el mismo objeto que se le solicito
     * @throws Exception Error de base de datos
     */
    public boolean autoCreationDemographic(RequestDemographicAuto requestDemographic) throws Exception;

    /**
     * Actualizar codigo central
     *
     * @param requestItemCentralCode
     * @return
     * @throws java.lang.Exception
     */
    public int updateCentralCode(RequestCentralCode requestItemCentralCode) throws Exception;

    /**
     * Obtiene las muestras segun sea el id de la orden y los respectivos ids de
     * pruebas que vienen llegando
     *
     * @param requestCentralCode
     * @return
     * @throws Exception Error de base de datos o al procesar alguna de las
     * peticiones
     */
    public ResponsSample getSampleByIdOrderAndIdTest(RequestCentralCode requestCentralCode) throws Exception;

    /**
     * Obtendra la informacion necesaria del codigo de barras para hacer la
     * impresión del mismo
     *
     * @param requestBarCode
     * @return
     * @throws Exception error al cargar algn componente del objeto
     */
    public ResponseBarCode getBarcode(RequestBarCode requestBarCode) throws Exception;

    /**
     * Resultados de mensaje transmitido al cliente
     *
     * @param idOrder
     * @param executedFrom
     * @param muestra
     * @param examenes
     * @param deletetest
     * @return respuesta del mensaje
     * @throws Exception Error en conexion al socket.
     */
    public String getMessageTest(long idOrder, String executedFrom, Integer muestra, String examenes, List<ResultTest> deletetest) throws Exception;

    /**
     * Obtiene todas las ordenes en el LIS que correspondan a ese id del HIS
     *
     * @param externalId
     * @return Lista de ordenes
     * @throws Exception Error al conectar con la base de datos
     */
    public List<Order> getOrdersByOrderHIS(String externalId) throws Exception;

    /**
     * Obtiene todas las ordenes en el LIS que correspondan a ese id del HIS
     * para minsa
     *
     * @param externalId
     * @return Lista de ordenes
     * @throws Exception Error al conectar con la base de datos
     */
   
    public List<Order> getOrdersByOrderHISMinsa(String externalId, int branchId) throws Exception;

    /**
     * Obtiene todas las ordenes en el LIS que correspondan a ese id del HIS y
     * al codigo de la sede
     *
     * @param externalId
     * @param branch
     * @return boolean si la orden exite o no
     * @throws Exception Error al conectar con la base de datos
     */
    public Boolean getValidOrdersByHIS(String externalId, String branch) throws Exception;

    /**
     * Obtiene todas las ordenes en el LIS que correspondan a ese id del HIS y
     * al codigo de la sede
     *
     * @param system
     * @return lista de demograficos
     * @throws Exception Error al conectar con la base de datos
     */
    public ResponsHomologationDemographicIngreso getDemographicCentralSystem(Integer system) throws Exception;

    /**
     * Obtiene el examen en el LIS que correspondan a ese id
     *
     * @param idTest
     * @param idBranch
     * @param orderType
     *
     * @return Lista de ordenes
     * @throws Exception Error al conectar con la base de datos
     */
    public RequestTest getTestsByOrderHIS(int idTest, int idBranch, int orderType) throws Exception;

    /**
     * Crea un codigo aleatorio para un nuevo item demografico a menos que este
     * itemDemografico ya exista por lo cual este devolvera su codigo
     * correspondiente
     *
     * @param nameDemographicItem
     * @param idDemographic
     * @return Codigo de homologación
     * @throws Exception Error al conectar con la base de datos
     */
    public String createRandomCode(String nameDemographicItem, int idDemographic) throws Exception;

    /**
     * Actualizar codigo central
     *
     * @param requestItemCentralCode
     * @return
     * @throws java.lang.Exception
     */
    public int updateCentralCodeMyM(RequestCentralCode requestItemCentralCode) throws Exception;

    public ServiceResponse create(OrderDto orderDto);
}

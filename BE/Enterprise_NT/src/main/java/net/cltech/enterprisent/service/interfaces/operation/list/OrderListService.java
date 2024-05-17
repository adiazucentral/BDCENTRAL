package net.cltech.enterprisent.service.interfaces.operation.list;

import java.util.List;
import net.cltech.enterprisent.domain.operation.billing.integration.OrderBilling;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.list.FilterRejectSample;
import net.cltech.enterprisent.domain.operation.list.RemissionLaboratory;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.remission.RemissionOrderCentral;

/**
 * Interfaz de servicios a la informacion de listados.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 05/09/2017
 * @see Creación
 */
public interface OrderListService
{

    /**
     * Lista las ordenes por un filtro especifico dado por la clase de listados.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<OrderList> listFilters(Filter search) throws Exception;
    
       /**
     * Lista las ordenes por un filtro especifico dado por la clase de listados.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<OrderList> listFiltersAppointment(Filter search) throws Exception;
    
        /**
     * Lista las ordenes por un filtro especifico dado por la clase de listados.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Order> orderbyDateDelete(Filter search) throws Exception;
    
        /**
     * Lista las ordenes por un filtro especifico dado por la clase de listados.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Order> orderState(Filter search) throws Exception;

    /**
     * Lista las ordenes por un filtro especifico dado por la clase de listados.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Order> listFiltersRejectSample(FilterRejectSample search) throws Exception;

    /**
     * Lista las ordenes por un filtro especifico dado por la clase de listados.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Order> listFiltersLaboratory(Filter search) throws Exception;
    
    
     /**
     * Guarda las ordenes a remitir.
     *
     * @param orders
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Order> savelaboratoryremmision(RemissionLaboratory orders) throws Exception;
    
     /**
     * Lista las ordenes por un filtro especifico dado por la clase de listados ordenes para remitir.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Order> listFilterLaboratoryRemmision(Filter search) throws Exception;

    /**
     * Lista las ordenes por un filtro especifico dado por la clase de listados.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes en formato HL7.
     * @throws Exception Error en la base de datos.
     */
    public String listFiltersLaboratoryHL7(Filter search) throws Exception;

    /**
     * Envia impresion de códigos de barras de las ordenes encontradas .
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<String> barcodeGeneration(Filter search) throws Exception;

    /**
     * Lista las ordenes que no tienen paciente asignado por un filtro
     * especifico .
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Order> listFiltersNoPatient(Filter search) throws Exception;

    /**
     * Obtiene la información de una orden
     *
     * @param order numero de la orden
     *
     * @return Orden encontrada null si no encuentra
     * @throws Exception
     */
    public Order findOrder(long order) throws Exception;
    
    /**
     * Obtiene una lista ordenes con examenes pendientes dentro de un rango de ordenes
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes
     * @throws Exception Error en la base de datos.
     */
    public List<Order> listPendingExams(Filter search) throws Exception;
    
    /**
     * Obtiene una lista de ordenes egun el rango con las sedes 
     *    
     * 
     * @param search
     * @return Lista de ResponsTestCheckByBranch
     * @throws Exception Error en la base de datos.
     */    
     public List<Order> getTestChekByBranch (Filter search) throws Exception;
     
    
     /**
     * Obtiene una lista de ordenes enviadas a remision
     *    
     * @param search Filtros generales
     * @return Lista de ordenes
     * @throws Exception Error en la base de datos.
     */    
     public List<Order> getRemissionOrders(Filter search) throws Exception;
    
         
    /**
     * Obtiene las ordenes para la integracion con facturacion kbits 
     *
     * @param startDate Fecha inicio
     * @param endDate Fecha final
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.Order}, vacio en
     * caso de no encontrarse
     * @throws Exception Error en el servicio
     */
    public List<OrderBilling> getOrderBilling(String startDate, String endDate) throws Exception;
    
    
    
     /**
     * Obtiene una lista de ordenes enviadas a remision
     *    
     * @param search Filtros generales
     * @return Lista de ordenes
     * @throws Exception Error en la base de datos.
     */    
     public List<Order> getListRemissionOrders(Filter search) throws Exception;
     
    /**
     * Obtiene una lista de ordenes enviadas a remision
     *    
     * @param order
     * @return Lista de ordenes
     * @throws Exception Error en la base de datos.
     */    
    public List<RemissionOrderCentral> getRemissionCentralOrders(long order) throws Exception;
     
     /**
     * inserta ordenes enviadas a remision
     *    
     * @param testList
     * @return Lista de ordenes
     * @throws Exception Error en la base de datos.
     */  
    public int insertRemmisioncTest(List<RemissionOrderCentral> testList) throws Exception;
    
    
      /**
     * consulta las remisiones en un rango de fechas
     *    
     * @param filter
     * @return Lista de ordenes
     * @throws Exception Error en la base de datos.
     */    
    public List<RemissionOrderCentral> listremissionCentralOrders(Filter filter) throws Exception;
      
}

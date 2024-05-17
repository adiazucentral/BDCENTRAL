package net.cltech.enterprisent.service.interfaces.operation.orders;

import java.util.List;
import net.cltech.enterprisent.domain.operation.filters.OrderSearchFilter;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.orders.Patient;

/**
 * Servicios sobre las ordenes del sistema
 *
 * @version 1.0.0
 * @author eacuna
 * @since 24/11/2017
 * @see Creacion
 */
public interface OrderSearchService
{

    /**
     * Lista las ordenes activas por la historia del paciente
     *
     * @param record historia del paciente
     * @param documentType Id tipo de documento - 0 - null
     * @param init Fecha inicial de la orden - 0 - null
     * @param end Fecha final de la orden - 0 - null
     *
     * @return lista de pacientes
     * @throws Exception Error
     */
    public List<Patient> listByRecord(String record, int documentType, int init, int end) throws Exception;

    /**
     * Lista pacientes por apellido.
     *
     * @param name 
     * @param name1
     * @param lastname Primer apellido
     * @param surname Segundo apellido
     * @param gender id genero
     * @param init Fecha inicial de la orden - 0 - null
     * @param end Fecha final de la orden - 0 - null
     *
     * @return lista de pacientes
     * @throws Exception Error
     */
    public List<Patient> listByLastName(String name, String name1, String lastname, String surname, int gender, int init, int end) throws Exception;

    /**
     * Lista pacientes por rango
     *
     * @param init Rango Inicial
     * @param end Rango Final
     *
     * @return Lista de pacientes
     * @throws Exception Error
     */
    public List<Patient> listByDates(int init, int end) throws Exception;

    /**
     * Lista pacientes por orden
     *
     * @param order Orden
     *
     * @return Lista de pacientes por orden.
     * @throws Exception Error
     */
    public List<Patient> listByOrder(long order) throws Exception;

    /**
     * Lista ordenes por un filtro
     *
     * @param filter filtros de ordenes
     *
     * @return Listado de ordenes encontradas
     * @throws Exception Error
     */
    public List<Order> listByFilter(OrderSearchFilter filter) throws Exception;
    
        /**
     * Lista ordenes por filtro de datos del paciente
     *
     * @param filter filtros de ordenes
     *
     * @return Listado de ordenes encontradas
     * @throws Exception Error
     */
    public List<OrderList> getOrdersbyPatient(OrderSearchFilter filter) throws Exception;
    
          /**
+     * Lista ordenes con muestras que se encuentren almacenadas por filtro de datos del paciente
+     *
+     * @param filter filtros de ordenes
+     *
+     * @return Listado de ordenes encontradas
+     * @throws Exception Error
+     */
    public List<OrderList> getOrdersbyPatientStorage(OrderSearchFilter filter) throws Exception;

    /**
     * Servicio para obtener el listado de ordenes con informacion del paciente.
     *
     * @param filter filtros de ordenes
     *
     * @return Listado de ordenes encontradas
     * @throws Exception Error
     */
    public List<Order> ordersWithPatient(OrderSearchFilter filter) throws Exception;
}

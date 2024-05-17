package net.cltech.enterprisent.service.interfaces.migration;

import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.migration.Inconsistency;
import net.cltech.enterprisent.domain.operation.orders.Order;

/**
 * Interfaz de servicios a la informacion de las inconsistencias.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/11/2017
 * @see Creación
 */
public interface InconsistencyService
{

    /**
     * Crea una nueva orden en el sistema generando el autonumerico de la orden
     * envio desde una interfaz, ademas de validar si se presentan
     * inconsistencias en los datos del paciente
     *
     * @param order Orden
     * @param user Id de usuario que realiza la insercion
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Order}
     * @throws Exception Error presentado en el servicio
     */
    public Order create(Order order, int user) throws Exception;

    /**
     * Lista las inconsistencias registradas en un rango de fechas.
     *
     * @param init Fecha Inicial
     * @param end Fecha Final
     * @return Retorna la lista de inconsistencias.
     */
    public List<Inconsistency> list(Date init, Date end);

    /**
     * Lista las inconsistencias registradas en un rango de fechas.
     *
     * @param idOrder Identificador de la orden
     * @return Retorna la inconsistencia de una orden.
     */
    public Inconsistency getByOrderId(Long idOrder);
    
    /**
     * Lista las inconsistencias registradas en un rango de fechas.
     *
     * @param idOrder Fecha Inicial
     * @param resolveLIS Indica si la incidencia
     * @return Retorna la lista de inconsistencias.
     */
    public boolean resolveInconsistency(long idOrder, boolean resolveLIS) throws Exception;
}

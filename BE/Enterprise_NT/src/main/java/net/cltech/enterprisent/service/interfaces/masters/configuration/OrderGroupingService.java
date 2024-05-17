package net.cltech.enterprisent.service.interfaces.masters.configuration;

import java.util.List;
import net.cltech.enterprisent.domain.masters.configuration.OrderGrouping;

/**
 * Servicios de configuracion general para la agrupacion de la orden
 *
 * @version 1.0.0
 * @author equijano
 * @since 18/03/2019
 * @see Creacion
 */
public interface OrderGroupingService
{

    /**
     * Obtiene la lista de agrupacion de ordenes
     *
     * @return Lista de ordenes agrupadas
     * @throws Exception Error en el servicio
     */
    public List<OrderGrouping> list() throws Exception;

    /**
     * Inserta la agrupacion de la orden
     *
     * @param inserts Lista de ordenes agrupadas
     * @return Numero de lementos registrados
     * @throws Exception Error en el servicio
     */
    public Integer insertGroups(List<OrderGrouping> inserts) throws Exception;

    /**
     * Eliminacion de las agrupaciones registradas
     *
     * @throws Exception Error en el servicio
     */
    public void deleteGroups() throws Exception;
}

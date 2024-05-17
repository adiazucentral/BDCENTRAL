package net.cltech.enterprisent.service.interfaces.tools.events;

import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.tools.EventsResponse;

/**
 * Interfaz de servicios a la informacion de Eventos Orden
 *
 * @version 1.0.0
 * @author equijano
 * @since 11/01/2019
 * @see Creación
 */
public interface EventsOrderService
{

    /**
     * Creacion de una orden
     *
     * @param order
     */
    public void create(Order order);

    /**
     * Actualiza la orden
     *
     * @param order Objeto de la orden
     */
    public void update(Order order);

    /**
     * Realiza la cancelacion de una orden
     *
     * @param order Numero de la orden
     */
    public void cancel(long order);

    /**
     * Validar url
     *
     * @param url
     * @return
     * @throws net.cltech.enterprisent.domain.exception.EnterpriseNTException
     */
    public EventsResponse validateUrl(EventsResponse url) throws EnterpriseNTException, Exception;

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.reports;

import java.util.List;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.reports.DeliveryOrder;

/**
 * Interfaz de servicios a la informacion de entrega de resultados.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 21/11/2017
 * @see Creación
 */
public interface DeliveryResultService
{

    /**
     * Lista las ordenes por un filtro especifico dado por la entrega de
     * resultados.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes con resultados entregados.
     * @throws Exception Error en la base de datos.
     */
    public List<DeliveryOrder> listFilters(Filter search) throws Exception;


    
    /**
     * Lista las ordenes pendientes por entrega de resultados por un filtro.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes pendientes de entrega.
     * @throws Exception Error en la base de datos.
     */
    public List<OrderList> listDeliveryResultPending(Filter search) throws Exception;
}

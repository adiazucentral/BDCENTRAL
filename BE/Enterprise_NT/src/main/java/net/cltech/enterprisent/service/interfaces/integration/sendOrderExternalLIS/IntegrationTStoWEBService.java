/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.integration.sendOrderExternalLIS;

import java.util.List;
import net.cltech.enterprisent.domain.integration.orderForExternal.OrderForExternal;

/**
 *
 * @author hpoveda
 */
public interface IntegrationTStoWEBService
{

    /**
     * Lista todas las ordenes asociadas a un laboratorio en un rango de fechas
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws java.lang.Exception
     */
    public List<OrderForExternal> getListOrder(long startDate, long endDateint, int laboratoryID) throws Exception;
    public boolean updateOrderStatus(long orderNumber, int idTestF, int status) throws Exception;

}

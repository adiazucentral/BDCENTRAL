/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.billing.integration;

import java.util.List;
import net.cltech.enterprisent.domain.operation.billing.integration.OrderBilling;

/**
 * @version 1.0.0
 * @author hpoveda
 * @since 12/05/2022
 * @see Creación
 */
public interface OrderBillingService
{

    public List<OrderBilling> get(long daysToSearch) throws Exception;

    public Boolean updateStatusOrderTest(long orderNumber, int idTest, int status) throws Exception;

}

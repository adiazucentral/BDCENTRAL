/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.operation.billing.integration;

import java.util.LinkedList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.operation.billing.integration.CashBoxBilling;
import net.cltech.enterprisent.domain.operation.billing.integration.OrderBilling;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.operation.billing.integration.OrderBillingService;
import net.cltech.enterprisent.service.interfaces.operation.list.OrderListService;
import net.cltech.enterprisent.service.interfaces.operation.orders.CashBoxService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version 1.0.0
 * @author hpoveda
 * @since 12/05/2022
 * @see Creación
 */
@Service
public class OrderBillingServiceEnterpriseNT implements OrderBillingService
{

    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private OrderListService listService;
    @Autowired
    private CashBoxService cashboxService;

    @Override
    public List<OrderBilling> get(long daysToSearch) throws Exception
    {
        int orderDigits = configurationService.getIntValue("DigitosOrden");
        String initDate = String.valueOf(Tools.buildInitialOrder((int) daysToSearch, orderDigits));
        String finalDate = String.valueOf(Tools.buildFinalOrder(orderDigits));
        
        List<OrderBilling> list = listService.getOrderBilling(initDate, finalDate);
        List<CashBoxBilling> listCashbox = cashboxService.getListCashboxBilling(initDate, finalDate);
        List<OrderBilling> finalList = new LinkedList<>();
        
        list.forEach( order -> {
            CashBoxBilling cashbox = listCashbox.stream().filter( cash -> cash.getOrder().equals(order.getOrder())).findFirst().orElse(null);
            if(cashbox != null) {
                order.setCashBoxBilling(cashbox);
                finalList.add(order);
            }
        });
        return finalList;
    }

    @Override
    public Boolean updateStatusOrderTest(long orderNumber, int idTest, int status) throws Exception
    {
        Boolean statusTest = ordersDao.updateOrderStatus(orderNumber, idTest, status);
        if(statusTest) {
            cashboxService.updateCashBoxStatus(orderNumber, status);
        } else {
            cashboxService.updateCashBoxStatus(orderNumber, Constants.SEND);
        }
        return statusTest;
    }
}

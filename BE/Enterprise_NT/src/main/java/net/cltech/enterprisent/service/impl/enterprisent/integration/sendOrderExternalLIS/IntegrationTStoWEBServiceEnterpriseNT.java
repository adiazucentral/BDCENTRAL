/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.integration.sendOrderExternalLIS;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.integration.sendOrderExternalLIS.IntegrationTStoWEBDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.domain.integration.orderForExternal.OrderForExternal;
import net.cltech.enterprisent.domain.integration.sendOrderExternalLIS.IdsPatientOrderTest;
import net.cltech.enterprisent.service.interfaces.integration.sendOrderExternalLIS.IntegrationTStoWEBService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author hpoveda
 */
@Service
public class IntegrationTStoWEBServiceEnterpriseNT implements IntegrationTStoWEBService
{

    @Autowired
    private IntegrationTStoWEBDao integrationTStoWEBDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrdersDao ordersDao;

    @Override
    public List<OrderForExternal> getListOrder(long startDate, long endDate, int laboratoryID) throws Exception

    {
        List<IdsPatientOrderTest> idOrdersByLaboratory = ordersDao.getIdOrdersByLaboratory(startDate, endDate, laboratoryID);

        List<OrderForExternal> orders = new ArrayList<>();
        idOrdersByLaboratory.forEach(ids ->
        {
            try
            {
                ids.setIdTests(ordersDao.getIdTestbyOrderAndLaboratory(ids.getOrderNumber(), laboratoryID));
                orders.add(orderService.getforBuilder(ids));
            } catch (Exception e)
            {
                OrderCreationLog.error("ERROR AL CONSULTAR LA ORDEN" + ids.getOrderNumber() + " POR " + e);
            }

        });
        return orders;
    }

    @Override
    public boolean updateOrderStatus(long orderNumber, int idTestF, int status) throws Exception
    {
       return ordersDao.updateOrderStatus(orderNumber, idTestF, status);
    }
}

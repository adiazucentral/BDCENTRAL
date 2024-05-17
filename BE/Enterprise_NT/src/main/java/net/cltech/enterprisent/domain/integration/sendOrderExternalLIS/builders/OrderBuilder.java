/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.sendOrderExternalLIS.builders;

import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.integration.sendOrderExternalLIS.buildersGeneral.OrdersBuilders;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.results.ResultTest;

/**
 *
 * @author hpoveda
 */
public class OrderBuilder implements OrdersBuilders
{

    private Order order;

    public OrderBuilder()
    {
        this.order = new Order();
        this.order.setType(null);
        this.order.setPatient(null);
        this.order.setLastUpdateUser(null);
        this.order.setBranch(null);
        this.order.setService(null);
        this.order.setPhysician(null);
        this.order.setAccount(null);
        this.order.setRate(null);
        this.order.setTests(null);
        this.order.setReason(null);
        this.order.setComments(null);
        this.order.setCreateUser(null);

    }

    @Override
    public OrdersBuilders setOrderNumber(Long orderNumber)
    {
        this.order.setOrderNumber(orderNumber);
        return this;
    }

    @Override
    public OrdersBuilders setCreatedDate(Date createdDate)
    {
        this.order.setCreatedDate(createdDate);
        return this;
    }

    @Override
    public OrdersBuilders setPatient(Patient patient)
    {
        this.order.setPatient(patient);
        return this;
    }

    @Override
    public OrdersBuilders setService(ServiceLaboratory service)
    {
        this.order.setService(service);
        return this;
    }

    @Override
    public OrdersBuilders setdemographics(List<DemographicValue> demographicValues)
    {
        this.order.setDemographics(demographicValues);
        return this;
    }

    @Override
    public OrdersBuilders setSamples(List<Sample> samples)
    {
        this.order.setSamples(samples);
        return this;
    }

    @Override
    public OrdersBuilders setTest(List<ResultTest> tests)
    {
        this.order.setResultTest(tests);
        return this;
    }

    @Override
    public OrdersBuilders setCreateUser(User user)
    {
        this.order.setCreateUser(user);
        return this;
    }

    @Override
    public Order build()
    {
        return this.order;
    }

}

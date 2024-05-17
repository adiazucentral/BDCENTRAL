/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.sendOrderExternalLIS.buildersGeneral;

import java.util.Date;
import java.util.List;
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
public interface OrdersBuilders
{
    //atributos comunes a todas las ordenes 

    OrdersBuilders setOrderNumber(Long orderNumber);

    OrdersBuilders setCreatedDate(Date createdDate);

    OrdersBuilders setPatient(Patient patient);

    OrdersBuilders setService(ServiceLaboratory service);

    OrdersBuilders setdemographics(List<DemographicValue> demographicValues);

    OrdersBuilders setSamples(List<Sample> samples);

    OrdersBuilders setTest(List<ResultTest> tests);

    OrdersBuilders setCreateUser(User user);

    Order build();

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.order.interfaces;

import net.cltech.enterprisent.domain.DTO.integration.order.DemographicDto;
import net.cltech.enterprisent.domain.DTO.integration.order.OrderDto;
import net.cltech.enterprisent.domain.DTO.integration.order.PatientDto;
import net.cltech.enterprisent.domain.DTO.integration.order.TestDto;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Test;

/**
 *
 * @author hpoveda
 */
public interface IngresoOrders
{

    public Patient toPatient(PatientDto patientDto);

    public Order toOrder(OrderDto orderDto);

    public Test toTest(TestDto testDto);

    public DemographicValue toDemographic(DemographicDto demographicDto);

}

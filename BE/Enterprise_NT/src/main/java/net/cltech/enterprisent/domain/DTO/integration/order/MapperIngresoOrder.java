/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.cltech.enterprisent.domain.DTO.integration.order.interfaces.IngresoOrders;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.tools.log.integration.IntegrationHisLog;
import org.springframework.stereotype.Component;

/**
 *
 * @author bbonilla
 */
@Component
public class MapperIngresoOrder implements IngresoOrders
{

    public MapperIngresoOrder()
    {
    }

    public Patient toPatient(PatientDto patientDto)
    {
        Date birthDate = null;
        try
        {
            birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(patientDto.getBirthdate());
        } catch (ParseException ex)
        {
            IntegrationHisLog.info("Patient birthdate " + patientDto.getBirthdate() + " not valid. Using actual date.");
        }
        Patient patient = new Patient();
        patient.setPatientId(patientDto.getRecord());
        patient.setName1(patientDto.getName());
        patient.setLastName(patientDto.getName());
        patient.setSurName(patientDto.getSecondLastName());
        patient.setSex(new Item(patientDto.getGender()));
        patient.setBirthday(birthDate);
        patient.setDocumentType(patientDto.getDocumentType());

        return patient;
    }

    public Order toOrder(OrderDto orderDto)
    {
        return new Order(orderDto.getOrder(), orderDto.getComment());
    }

    public Test toTest(TestDto testDto)
    {
        Test test = new Test();
        test.setCode(testDto.getCode());
        test.setName(testDto.getName());
        test.setAction(testDto.getActionCode());
        return test;
    }

    public DemographicValue toDemographic(DemographicDto demographicDto)
    {
        DemographicValue demographicValue = new DemographicValue();
        demographicValue.setIdDemographic(demographicDto.getId());
        demographicValue.setCodifiedName(demographicDto.getName());
        demographicValue.setEncoded(demographicDto.isEncoded());
        demographicValue.setCodifiedCode(demographicDto.getCode());
        return demographicValue;
    }
}

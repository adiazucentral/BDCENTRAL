/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.sendOrderExternalLIS.buildersGeneral;

import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Patient;

/**
 *
 * @author hpoveda
 */
public interface PatientsBuilders
{
    //atributos comunes a todos los pacientes

    PatientsBuilders setPatientId(String patientId);

    PatientsBuilders setName1(String name1);

    PatientsBuilders setName2(String name2);

    PatientsBuilders setLastName(String lastName);

    PatientsBuilders setSurName(String surName);

    PatientsBuilders setAddress(String address);

    PatientsBuilders setPhone(String phone);

    PatientsBuilders setEmail(String email);

    PatientsBuilders setBirthday(Date birthday);

    PatientsBuilders setGender(Item gender);

    PatientsBuilders setdemographics(List<DemographicValue> demographicValues);

    Patient build();

}

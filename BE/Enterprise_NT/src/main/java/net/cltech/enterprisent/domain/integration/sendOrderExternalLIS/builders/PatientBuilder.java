/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.sendOrderExternalLIS.builders;

import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.integration.sendOrderExternalLIS.buildersGeneral.PatientsBuilders;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Patient;

/**
 *
 * @author hpoveda
 */
public class PatientBuilder implements PatientsBuilders
{

    private Patient patient;

    public PatientBuilder()
    {
        this.patient = new Patient();
        this.patient.setSex(null);
        this.patient.setDiagnostic(null);
        this.patient.setRace(null);
        this.patient.setDocumentType(null);
        this.patient.setDemographics(null);
        this.patient.setDiagnosis(null);
        this.patient.setOrders(null);
        this.patient.setFirstUser(null);
        this.patient.setListAllTestsOfPatient(null);
        this.patient.setDemographicsHomebound(null);
        //this.patient.setHomebound(null);
    }

    @Override
    public PatientsBuilders setPatientId(String patientId)
    {
        patient.setPatientId(patientId);
        return this;
    }

    @Override
    public PatientsBuilders setName1(String name1)
    {
        patient.setName1(name1);
        return this;
    }

    @Override
    public PatientsBuilders setName2(String name2)
    {
        patient.setName2(name2);
        return this;
    }

    @Override
    public PatientsBuilders setLastName(String lastName)
    {
        patient.setLastName(lastName);
        return this;
    }

    @Override
    public PatientsBuilders setSurName(String surName)
    {
        patient.setSurName(surName);
        return this;
    }

    @Override
    public PatientsBuilders setAddress(String address)
    {
        patient.setAddress(address);
        return this;
    }

    @Override
    public PatientsBuilders setPhone(String phone)
    {
        patient.setPhone(phone);
        return this;
    }

    @Override
    public PatientsBuilders setEmail(String email)
    {
        patient.setEmail(email);
        return this;
    }

    @Override
    public PatientsBuilders setBirthday(Date birthday)
    {
        patient.setBirthday(birthday);
        return this;
    }

    @Override
    public PatientsBuilders setGender(Item gender)
    {
        patient.setSex(gender);
        return this;
    }

    @Override
    public PatientsBuilders setdemographics(List<DemographicValue> demographicValues)
    {
        patient.setDemographics(demographicValues);
        return this;
    }

    @Override
    public Patient build()
    {
        return this.patient;
    }

}

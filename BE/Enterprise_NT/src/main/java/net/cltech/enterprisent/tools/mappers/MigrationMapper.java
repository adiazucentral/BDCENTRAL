/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.tools.mappers;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.DemographicNT;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.OrderNT;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.PatientNT;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.SampleNT;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.TestNT;
import net.cltech.enterprisent.domain.masters.demographic.DemographicBranch;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Test;

/**
 *
 * @author hpoveda
 */
public class MigrationMapper
{

    public static DemographicBranch toDtoDemographicBranch(DemographicValue demographicValue)
    {
        return new DemographicBranch(demographicValue);

    }

    public static Patient toDtoPatient(PatientNT patient)
    {

        return new Patient(patient);
    }

    public static PatientNT toDtoPatientNT(Patient patient)
    {

        return new PatientNT(patient);
    }

    public static List<DemographicValue> toDtoDemo(List<DemographicNT> demographicNT)
    {
        List<DemographicValue> demographicValues = new ArrayList<>();
        demographicNT.forEach(demo ->
        {
            demographicValues.add(new DemographicValue(demo));

        });

        return demographicValues;
    }

    public static List< DemographicNT> toDtoDemoNT(List<DemographicValue> demographicValue)
    {
        List< DemographicNT> demographicNTs = new ArrayList<>();

        demographicValue.forEach(demo ->
        {
            demographicNTs.add(new DemographicNT(demo));

        });

        return demographicNTs;
    }

    public static List<Test> toDtoTest(List<TestNT> testNTs)
    {
        List<Test> tests = new ArrayList<>();
        testNTs.forEach(test ->
        {
            tests.add(new Test(test));

        });

        return tests;

    }

    public static List<TestNT> toDtoTestNT(List<Test> tests)
    {
        List<TestNT> testsNT = new ArrayList<>();

        tests.forEach(test ->
        {
            testsNT.add(new TestNT(test));

        });

        return testsNT;

    }

    public static Order toDtoOrder(OrderNT orderNT)
    {

        return new Order(orderNT);

    }

    public static OrderNT toDtoOrderNT(Order order)
    {

        return new OrderNT(order);

    }

    public static SampleNT toDtoSampleNT(Sample sample)
    {

        return new SampleNT(sample);

    }

}

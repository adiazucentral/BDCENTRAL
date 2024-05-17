/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.orderForExternal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.operation.orders.Test;

/**
 *
 * @author hpoveda
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TestForExternal
{

    private int testsIdField;
    private String testsField;
    private boolean withSampleField;
    //demos
    private int sampleIDField;
    private String SampleField;

    public TestForExternal(Test test)
    {
        this.testsIdField = test.getId();
        this.testsField = test.getName();
        this.sampleIDField = test.getSample().getId();
        this.SampleField = test.getSample().getName();
        this.withSampleField = true;

    }

}

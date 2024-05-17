/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.results;

import java.util.List;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Race;

/**
 * @version 1.0.0
 * @author jvellojin
 * @since 01-03-2022
 * @see Creación
 */
public class ResultAutomaticSendEmail
{

    private Long orderId;
    private Item sex = new Item();
    private Race race;
    private List<ResultTest> tests;

    public Race getRace()
    {
        return race;
    }

    public void setRace(Race race)
    {
        this.race = race;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Item getSex()
    {
        return sex;
    }

    public void setSex(Item sex)
    {
        this.sex = sex;
    }

    public List<ResultTest> getTests()
    {
        return tests;
    }

    public void setTests(List<ResultTest> tests)
    {
        this.tests = tests;
    }
}

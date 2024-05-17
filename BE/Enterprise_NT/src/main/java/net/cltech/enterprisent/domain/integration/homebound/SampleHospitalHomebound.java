/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.homebound;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author adiaz
 */
public class SampleHospitalHomebound {
 
    @ApiObjectField(name = "order", description = "Informacion de la orden", required = true, order = 1)
    private Long order;
    @ApiObjectField(name = "sample", description = "Id de la muestra", required = true, order = 2)
    private Integer sample;
    @ApiObjectField(name = "state", description = "Estado 1 -> Ordenenada, 2 -> Pendiente, 3 -> Impresa, 4 -> Tomada, 5 -> Transporte, 6 -> recibida, 7 -> verificada  ", required = false, order = 3)
    private int state;

    public SampleHospitalHomebound(Long order, Integer sample, int state)
    {
        this.order = order;
        this.sample = sample;
        this.state = state;
    }

    public SampleHospitalHomebound()
    {
    }

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public Integer getSample()
    {
        return sample;
    }

    public void setSample(Integer sample)
    {
        this.sample = sample;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }
}


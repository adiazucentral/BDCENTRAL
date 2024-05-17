/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.order;

import lombok.Data;

/**
 * Entidad del item de demografico
 *
 * @author oarango
 * @since 2022-04-14
 * @see Creacion
 */
@Data
public class DemographicItemIngresoOrder
{

    private int id;
    private String code;
    private String name;
    private String description;
    private int demographic;
    private int user;
    private boolean status;

    public DemographicItemIngresoOrder()
    {
    }

    public DemographicItemIngresoOrder(int id)
    {
        this.id = id;
    }

    public DemographicItemIngresoOrder(int id, String code, String name)
    {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}

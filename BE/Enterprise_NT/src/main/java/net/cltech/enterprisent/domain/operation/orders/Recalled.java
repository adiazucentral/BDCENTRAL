/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una objeto rellamado
 *
 * @version 1.0.0
 * @author equijano
 * @since 16/08/2019
 * @see Creación
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Rellamado de ordenes",
        description = "Representa el rellamado de las ordenes de laboratorio de la aplicación"
)
@JsonInclude(Include.NON_NULL)
public class Recalled
{

    @ApiObjectField(name = "fatherOrder", description = "Orden de rellamado de la cual proviene", required = false, order = 35)
    private Order fatherOrder;
    @ApiObjectField(name = "daughterOrder", description = "Orden de rellamado hija", required = false, order = 36)
    private Order daughterOrder;

    public Recalled(Order fatherOrder, Order daughterOrder)
    {
        this.fatherOrder = fatherOrder;
        this.daughterOrder = daughterOrder;
    }

    public Recalled()
    {
    }

    public Order getFatherOrder()
    {
        return fatherOrder;
    }

    public void setFatherOrder(Order fatherOrder)
    {
        this.fatherOrder = fatherOrder;
    }

    public Order getDaughterOrder()
    {
        return daughterOrder;
    }

    public void setDaughterOrder(Order daughterOrder)
    {
        this.daughterOrder = daughterOrder;
    }

}

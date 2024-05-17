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
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;

/**
 *
 * @author hpoveda
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DemographicForExternal
{

    private int id;
    private String codificado;
    private String type;
    private String autoCreate;
    private String value;

    public DemographicForExternal(DemographicValue demo)
    {
        this.autoCreate = "O";
        this.codificado = demo.isEncoded() ? "C" : "N";
        this.id = demo.getIdDemographic();
        this.value = demo.getValue();

    }

}

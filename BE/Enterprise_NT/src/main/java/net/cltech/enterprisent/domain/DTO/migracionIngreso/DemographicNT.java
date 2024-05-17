/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.migracionIngreso;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author hpoveda
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Demografico migration",
        description = "Representa el valor de un demografico asociado al paciente o a la orden"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemographicNT
{

    @ApiObjectField(name = "id", description = "Id Demografico", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "value", description = "value", required = true, order = 1)
    private String value;
    @ApiObjectField(name = "name", description = "Nombre Demografico", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "code", description = "code", required = false, order = 3)
    private String code;
    @ApiObjectField(name = "encode", description = "encode", required = false, order = 4)
    private Boolean encode;
    @ApiObjectField(name = "autoCreate", description = "autoCreate", required = false, order = 5)
    private boolean autoCreate = false;

    public DemographicNT(DemographicValue demo)
    {

        this.id = demo.getIdDemographic();
        this.name = demo.getDemographic();
        this.value = demo.getValue();
        this.code = demo.getCodifiedCode();
        this.encode = demo.isEncoded();

    }

}

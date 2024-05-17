/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.migracionIngreso;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cltech.enterprisent.domain.operation.orders.Test;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author hpoveda
 */
@ApiObject(
        group = "Operación - Listados",
        name = "Examen de Laboratorio migration",
        description = "Representa un Examen"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestNT
{

    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 1)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "id", description = "Id de la prueba", required = true, order = 3)
    private String keyHIS;
    @ApiObjectField(name = "action", description = "Accion de examen", required = true, order = 4)
    private String actionCode;

    public TestNT(Test test)
    {
        this.code = test.getCode();
        this.name = test.getName();
        this.actionCode = test.getAction();

    }

    public TestNT(net.cltech.enterprisent.domain.masters.test.Test test)
    {
        this.code = test.getCode();
        this.name = test.getName();

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.migracionIngreso;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author hpoveda
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Orden de migration",
        description = "Representa una orden de laboratorio en migration"
)

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class OrderIngreso
{

    @ApiObjectField(name = "order", description = "Tipo de la orden", required = true, order = 2)
    private OrderNT order;
    @ApiObjectField(name = "options", description = "Tipo de la orden", required = true, order = 2)
    private Options options;
    @ApiObjectField(name = "daysUpdate", description = "dias de consulta para la modificacion de la orden", required = true, order = 2)
    private int daysUpdate;
    @ApiObjectField(name = "updateGroupBy", description = "dias de consulta para la modificacion de la orden", required = true, order = 2)
    private Boolean updateGroupBy;
}

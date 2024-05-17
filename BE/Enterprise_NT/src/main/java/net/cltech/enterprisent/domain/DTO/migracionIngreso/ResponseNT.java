/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.migracionIngreso;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseNT
{

    @ApiObjectField(name = "order", description = "Numero de Orden", required = false, order = 1)
    private Long order;
    @ApiObjectField(name = "error", description = "Numero de Orden", required = false, order = 2)
    private String error;

}

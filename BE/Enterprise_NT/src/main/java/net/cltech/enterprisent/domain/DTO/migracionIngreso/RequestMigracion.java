/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.migracionIngreso;

import lombok.AllArgsConstructor;
import lombok.Data;
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
        name = "Request de migration",
        description = "Representa una orden de laboratorio en migration"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestMigracion
{

    @ApiObjectField(name = "order", description = "Numero de Orden", required = false, order = 1)
    private String order;
    @ApiObjectField(name = "demos", description = "Demos separados por una coma", required = false, order = 2)
    private String demos;

}

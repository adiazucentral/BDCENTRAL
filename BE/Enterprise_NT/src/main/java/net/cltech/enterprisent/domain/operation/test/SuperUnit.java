/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(
        group = "Prueba",
        name = "Unidad",
        description = "Muestra informacion de la unidad del exámen"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class SuperUnit {
    @ApiObjectField(name = "id", description = "Identificador autonumérico de la unidad", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de la unidad", required = true, order = 2)
    private String name;
}

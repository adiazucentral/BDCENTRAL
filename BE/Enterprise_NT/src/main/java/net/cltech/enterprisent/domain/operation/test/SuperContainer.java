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
        name = "Recipiente",
        description = "Representa un recipiente"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class SuperContainer {
    @ApiObjectField(name = "id", description = "Id del recipiente", order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre del recipiente", order = 2)
    private String name;
}

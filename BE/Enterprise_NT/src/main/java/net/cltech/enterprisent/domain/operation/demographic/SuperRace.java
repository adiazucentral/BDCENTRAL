/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObjectField;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class SuperRace {
    @ApiObjectField(name = "id", description = "Id base de datos", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de la raza", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "code", description = "Codigo de la raza", required = true, order = 5)
    private String code;
}

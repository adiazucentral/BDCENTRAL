/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObjectField;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class SuperTechnique {
    @ApiObjectField(name = "id", description = "Identificador autonumérico de la tecnica", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Código de la Tecnica", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre de la tecnica", required = true, order = 3)
    private String name;
}

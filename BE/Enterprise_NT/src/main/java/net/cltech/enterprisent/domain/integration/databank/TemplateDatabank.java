/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.databank;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa las plantillas de las pruebas
 *
 * @version 1.0.0
 * @author omendez
 * @since 11/05/2022
 * @see Creación
 */
@ApiObject(
        group = "Integracion",
        name = "Plantillas",
        description = "Representa las plantillas de las pruebas"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class TemplateDatabank {
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 1)
    private String name;
    @ApiObjectField(name = "result", description = "Resultado", required = true, order = 1)
    private String result;
}

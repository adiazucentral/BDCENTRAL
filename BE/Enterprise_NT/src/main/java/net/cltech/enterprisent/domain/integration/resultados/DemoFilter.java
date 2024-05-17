/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.resultados;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Filtros
 * 
 * @version 1.0.0
 * @author omendez
 * @since 17/02/2023
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Filtros Demograficos",
        description = "Filtros Demograficos"
)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class DemoFilter {
    @ApiObjectField(name = "id", description = "Id demografico", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "required", description = "Requerido", required = true, order = 2)
    private boolean required;
    @ApiObjectField(name = "idFilterValues", description = "Ids de items demograficos", required = true, order = 3)
    private String idFilterValues;
    @ApiObjectField(name = "encoded", description = "Codificado", required = true, order = 4)
    private boolean encoded;
}

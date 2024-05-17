/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa el filtro de consulta de precios de examenes por tarifa
* @version 1.0.0
* @author omendez
* @since 04/11/2022
* @see Creación
*/

@ApiObject(
        group = "Operación - Ordenes",
        name = "Filtro Precio Examen",
        description = "Representa el filtro de consulta de precios de examenes por tarifa"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class FilterTestPrice {
    
    @ApiObjectField(name = "rateId", description = "Id de la tarifa", required = true, order = 1)
    private int rateId;
    @ApiObjectField(name = "tests", description = "Lista de examenes", order = 2)
    private List<Integer> tests;
}

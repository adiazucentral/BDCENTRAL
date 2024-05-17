/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa los Impuestos
*
* @version 1.0.0
* @author deicy
* @see Creación
*/

@ApiObject(
        group = "Impuestos por Factura",
        name = "Impuestos",
        description = "Representa los datos necesarios para el Impuesto"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Getter
@Setter
@NoArgsConstructor
public class Tax {
    
    @ApiObjectField(name = "taxId", description = "Id del impuesto", order = 1)
    private Integer taxId;
    @ApiObjectField(name = "name", description = "Nombre impuesto", order = 1)
    private String name;
    @ApiObjectField(name = "value", description = "valor", order = 2)
    private Double value;
    
}

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
 *
 * @author deicy
 */

@ApiObject(
        group = "Impuesto por Factura",
        name = "Impuesto",
        description = "Representa los datos para el impuesto por Facturacion"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Getter
@Setter
@NoArgsConstructor
public class TaxInvoice {

    @ApiObjectField(name = "id", description = "Id del Impuesto por Factura", order = 1)
    private Integer id;  
    @ApiObjectField(name = "headerId", description = "Id de la cabecera", order = 2)
    private Long headerId;
    @ApiObjectField(name = "taxId", description = "Id del Impuesto", order = 3)
    private Integer taxId;
    @ApiObjectField(name = "value", description = "Valor", order = 4)
    private Double value;
    @ApiObjectField(name = "name", description = "Nombre del impuesto", order = 2)
    private String name;

}

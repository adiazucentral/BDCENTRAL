/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase para el detalle de los tipos de pago asociados a una caja
 *
 * @version 1.0.0
 * @author adiaz
 * @since 14/05/2021
 * @see Creación
 */

@ApiObject(
        group = "Facturacion",
        name = "Detalles tipos de pago",
        description = " Representa clase para el detalle de los tipos de pago."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class DetailPaymentType {
    @ApiObjectField(name = "name", description = "Nombre del tipo de pago", required = true, order = 1)
    private String name;
    @ApiObjectField(name = "value", description = "valor del pagado por el correspondiente tipo de pado", required = true, order = 2)
    private Double value;
}

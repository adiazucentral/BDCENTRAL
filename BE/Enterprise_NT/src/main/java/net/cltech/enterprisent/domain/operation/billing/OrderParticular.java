package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con para el guardado de una orden particilar
 *
 * @version 1.0.0
 * @author adiaz
 * @since 24/04/2021
 * @see Creación
 */

@ApiObject(
        group = "Operación - Facturación",
        name = "Orden Particular",
        description = "Representa el objeto para enviar una orden particular a facturar."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class OrderParticular 
{
    @ApiObjectField(name = "order", description = "Numero de orden a facturar", required = true, order = 1)
    private Long order;
    @ApiObjectField(name = "comment", description = "Comentario de la factura particular", required = true, order = 2)
    private String comment;
    @ApiObjectField(name = "invoiceNumber", description = "Numero de factura manual", required = true, order = 3)
    private String invoiceNumber;
}

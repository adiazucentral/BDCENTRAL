package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa el pago de una factura
*
* @version 1.0.0
* @author Julian
* @since 13/08/2021
* @see Creación
*/

@ApiObject(
        name = "Pago Factura",
        group = "Operación - Facturación",
        description = "Representa los datos necesarios para realizar el pago de una factura"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class InvoicePayment
{
    @ApiObjectField(name = "invoiceNumber", description = "Número de la factura", required = true, order = 1)
    private String invoiceNumber;
    @ApiObjectField(name = "paymentDate", description = "Fecha de pago", required = true, order = 3)
    private Date paymentDate;
}

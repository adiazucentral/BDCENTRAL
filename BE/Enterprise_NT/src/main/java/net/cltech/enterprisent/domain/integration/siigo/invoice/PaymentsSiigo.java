package net.cltech.enterprisent.domain.integration.siigo.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa el medio de pago que se usara la para la factura Siigo
*
* @version 1.0.0
* @author Julian
* @since 29/04/2021
* @see Creación
*/

@ApiObject(
        group = "Integración con Siigo",
        name = "Metodo De Pago Siigo",
        description = "Representa el medio de pago que se usara para la factura Siigo"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class PaymentsSiigo 
{
    @ApiObjectField(name = "id", description = "ID del medio de pago", required = false, order = 1)
    private int id;
    @ApiObjectField(name = "value", description = "Valor asociado al medio de pago", required = true, order = 2)
    private double value;
    @ApiObjectField(name = "due_date", description = "Fecha pago cuota, formato yyyy-MM-dd", required = false, order = 3)
    private String due_date;
}
package net.cltech.enterprisent.domain.operation.billing.statement;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la orden que no ha sido facturada
*
* @version 1.0.0
* @author Julian
* @since 13/08/2021
* @see Creación
*/

@ApiObject(
        name = "Orden No Facturada",
        group = "Operación - Facturación",
        description = "Representa la orden que no ha sido facturada"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class UnbilledOrder
{
    @ApiObjectField(name = "orderNumber", description = "Número de la orden", required = true, order = 1)
    private long orderNumber;
    @ApiObjectField(name = "value", description = "Valor de pago de esa orden", required = true, order = 2)
    private Double value;
}

package net.cltech.enterprisent.domain.masters.billing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa los parametros especificos para validar si un contrato puede ser activado o no
 * 
 * @version 1.0.0
 * @author javila
 * @since 08/07/2021
 * @see Creación
 */

@ApiObject(
        group = "Facturacion",
        name = "Validador De Activación",
        description = "Representa los parametros especificos para validar si un contrato puede ser activado o no"
)
@Getter
@Setter
@NoArgsConstructor
public class ActivationValidator
{
    @ApiObjectField(name = "customerId", description = "Id del cliente", required = true, order = 1)
    private Integer customerId;
    @ApiObjectField(name = "contractId", description = "Id del contrato", required = true, order = 2)
    private Integer contractId;
    @ApiObjectField(name = "rates", description = "Id de las tarifas separadas por comas", required = true, order = 3)
    private String rates;
}

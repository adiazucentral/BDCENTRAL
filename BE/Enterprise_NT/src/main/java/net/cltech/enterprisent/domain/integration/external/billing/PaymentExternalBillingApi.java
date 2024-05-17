package net.cltech.enterprisent.domain.integration.external.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el pago que se enviará junto a la caja de la API de México
 * 
 * @version 1.0.0
 * @author javila
 * @since 14/07/2021
 * @see Creación
 */

@ApiObject(
        name = "Examen De Facturación Externa",
        group = "Operación - Ordenes",
        description = "Representa el examen (prueba) que se enviará junto a la caja de la API de México"
)
@Getter
@Setter
@NoArgsConstructor
public class PaymentExternalBillingApi
{
    @ApiObjectField(name = "FormaPago", description = "Nombre de la forma de pago", required = true, order = 1)
    @JsonProperty("FormaPago")
    private String FormaPago;
    @ApiObjectField(name = "Valor", description = "Valor", required = true, order = 2)
    @JsonProperty("Valor")
    private BigDecimal Valor;
    @ApiObjectField(name = "FechaPago", description = "Fecha en la que se realizó el pago", required = true, order = 3)
    @JsonProperty("FechaPago")
    private String FechaPago;
    @ApiObjectField(name = "UsuarioPago", description = "Nombre del usuario que realizó el pago", required = true, order = 4)
    @JsonProperty("UsuarioPago")
    private String UsuarioPago;
}

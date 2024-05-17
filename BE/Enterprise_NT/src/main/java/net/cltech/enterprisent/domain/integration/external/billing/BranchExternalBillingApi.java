package net.cltech.enterprisent.domain.integration.external.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la sede que se enviará junto a la caja de la API de México
 * 
 * @version 1.0.0
 * @author javila
 * @since 2021/07/12
 * @see Creación
 */

@ApiObject(
        name = "Sede De Facturación Externa",
        group = "Operación - Ordenes",
        description = "Representa la sede que se enviará junto a la caja de la API de México"
)
@Getter
@Setter
@NoArgsConstructor
public class BranchExternalBillingApi
{
    @ApiObjectField(name = "CodigoSede", description = "Codigo de la sede", required = true, order = 1)
    @JsonProperty("CodigoSede")
    private String CodigoSede;
    @ApiObjectField(name = "DescripcionSede", description = "Descripcion de la sede", required = true, order = 2)
    @JsonProperty("DescripcionSede")
    private String DescripcionSede;
}

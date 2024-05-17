package net.cltech.enterprisent.domain.integration.external.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el cliente que se enviará junto a la caja de la API de México
 *
 * @version 1.0.0
 * @author javila
 * @since 2021/07/12
 * @see Creación
 */
@ApiObject(
        name = "Cliente De Facturación Externa",
        group = "Operación - Ordenes",
        description = "Representa el cliente que se enviará junto a la caja de la API de México"
)
@Getter
@Setter
@NoArgsConstructor
public class AccountExternalBillingApi
{

    @ApiObjectField(name = "RFC", description = "RFC del cliente", required = true, order = 1)
    @JsonProperty("RFC")
    private String RFC;
    @ApiObjectField(name = "RazonSocial", description = "Razón social", required = true, order = 2)
    @JsonProperty("RazonSocial")
    private String RazonSocial;
    @ApiObjectField(name = "Correo", description = "Correo", required = true, order = 3)
    @JsonProperty("Correo")
    private String Correo;
    @ApiObjectField(name = "Facturar", description = "Facturar: SI o NO", required = true, order = 4)
    @JsonProperty("Facturar")
    private String Facturar;
    @ApiObjectField(name = "Convenio", description = "Convenio: SI o NO", required = true, order = 5)
    @JsonProperty("Convenio")
    private String Convenio;
    @ApiObjectField(name = "UsoCFDI", description = "UsoCFDI", required = true, order = 6)
    @JsonProperty("UsoCFDI")
    private String UsoCFDI;
    @JsonProperty("PostalCode")
    @ApiObjectField(name = "postalCode", description = "Codigo Postal del cliente", required = true, order = 7)
    private String PostalCode;
    @ApiObjectField(name = "phone", description = "Telefono del cliente", required = true, order = 8)
    @JsonProperty("Phone")
    private String Phone;
    @ApiObjectField(name = "address", description = "Direccion del cliente", required = true, order = 9)
    @JsonProperty("Address")
    private String Address;
    @ApiObjectField(name = "additionalAddress", description = "Direccion Adicional del cliente", required = true, order = 10)
    @JsonProperty("AdditionalAddress")
    private String AdditionalAddress;
    @ApiObjectField(name = "RegimenFiscal", description = "RegimenFiscal del cliente", required = true, order = 11)
    @JsonProperty("RegimenFiscal")
    private String RegimenFiscal;
}

package net.cltech.enterprisent.domain.masters.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Emisor
 *
 * @version 1.0.0
 * @author eacuna
 * @since 02/05/2018
 * @see Creación
 */
@ApiObject(
        group = "Facturación",
        name = "Emisor",
        description = "Muestra informacion del maestro Emisor que usa el API"
)
@JsonInclude(Include.NON_NULL)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class Provider extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "nit", description = "NIT", required = true, order = 2)
    private String nit;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "phone", description = "Telefono", required = true, order = 4)
    private String phone;
    @ApiObjectField(name = "responsable", description = "Responsable", required = true, order = 6)
    private String responsable;
    @ApiObjectField(name = "address", description = "Direccion", required = true, order = 14)
    private String address;
    @ApiObjectField(name = "code", description = "Codigo de entidad de salud ", required = true, order = 15)
    private String code;
    @ApiObjectField(name = "postalCode", description = "Codigo Postal", required = true, order = 16)
    private String postalCode;
    @ApiObjectField(name = "active", description = "Indica si se encuentra activo", required = true, order = 16)
    private boolean active;
    @ApiObjectField(name = "taxRegimen", description = "Regimen fiscal", required = true, order = 17)
    private String taxRegimen;
    @ApiObjectField(name = "currentNumber", description = "Número actual", required = true, order = 18)
    private Integer currentNumber;
    @ApiObjectField(name = "state", description = "Estado (Departamento)", required = true, order = 19)
    private Integer state;
    @ApiObjectField(name = "municipality", description = "Municipio", required = true, order = 20)
    private Integer municipality;
    @ApiObjectField(name = "nameElectronicInvoicing", description = "Nombre facturación electronica", required = true, order = 21)
    private String nameElectronicInvoicing;
    @ApiObjectField(name = "electronicBillingPhone", description = "Telefono facturación electronica", required = true, order = 22)
    private String electronicBillingPhone;
    @ApiObjectField(name = "privateKey", description = "Llave privada", required = true, order = 23)
    private String privateKey;
    @ApiObjectField(name = "certificate", description = "Certificado", required = true, order = 24)
    private String certificate;
    @ApiObjectField(name = "password", description = "Contraseña", required = true, order = 25)
    private String password;
    @ApiObjectField(name = "applyParticular", description = "aplica para facturacion particular", required = true, order = 26)
    private Boolean applyParticular;
}

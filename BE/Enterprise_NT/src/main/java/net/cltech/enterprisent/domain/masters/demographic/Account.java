package net.cltech.enterprisent.domain.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.billing.DiscountRate;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Clientes
 *
 * @version 1.0.0
 * @author cmartin
 * @since 31/05/2017
 * @see Creación
 */
@ApiObject(
        group = "Demografico",
        name = "Clientes",
        description = "Muestra informacion del maestro Clientes que usa el API"
)
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class Account extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id del cliente", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "nit", description = "NIT del cliente", required = true, order = 2)
    private String nit;
    @ApiObjectField(name = "name", description = "Nombre del cliente", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "phone", description = "Telefono del cliente", required = true, order = 4)
    private String phone;
    @ApiObjectField(name = "fax", description = "Fax del cliente", required = true, order = 5)
    private String fax;
    @ApiObjectField(name = "responsable", description = "Responsable del cliente", required = true, order = 6)
    private String responsable;
    @ApiObjectField(name = "observation", description = "Observacion del cliente", required = true, order = 11)
    private String observation;
    @ApiObjectField(name = "institutional", description = "Tipo Cuenta del cliente", required = true, order = 12)
    private boolean institutional;
    @ApiObjectField(name = "epsCode", description = "Codigo EPS del cliente", required = true, order = 13)
    private String epsCode;
    @ApiObjectField(name = "address", description = "Direccion del cliente", required = true, order = 14)
    private String address;
    @ApiObjectField(name = "additionalAddress", description = "Direccion Adicional del cliente", required = true, order = 15)
    private String additionalAddress;
    @ApiObjectField(name = "postalCode", description = "Codigo Postal del cliente", required = true, order = 16)
    private String postalCode;
    @ApiObjectField(name = "city", description = "Ciudad del cliente", required = true, order = 17)
    private Integer city;
    @ApiObjectField(name = "faxSend", description = "Envio Fax", required = true, order = 18)
    private boolean faxSend;
    @ApiObjectField(name = "print", description = "Impresion", required = true, order = 19)
    private boolean print;
    @ApiObjectField(name = "connectivityEMR", description = "Conectividad EMR", required = true, order = 20)
    private boolean connectivityEMR;
    @ApiObjectField(name = "email", description = "Correo del cliente", required = true, order = 21)
    private String email;
    @ApiObjectField(name = "automaticEmail", description = "Correo Automatico", required = true, order = 22)
    private boolean automaticEmail;
    @ApiObjectField(name = "selfPay", description = "Cliente Particular del cliente", required = true, order = 23)
    private boolean selfPay;
    @ApiObjectField(name = "username", description = "Usuario del cliente", required = true, order = 24)
    private String username;
    @ApiObjectField(name = "password", description = "Contraseña del cliente", required = true, order = 25)
    private String password;
    @ApiObjectField(name = "state", description = "Estado del cliente", required = true, order = 26)
    private boolean state;
    @ApiObjectField(name = "department", description = "Departamento del cliente", required = true, order = 28)
    private Integer department;
    @ApiObjectField(name = "colony", description = "Colonia o Barrio del cliente", required = true, order = 29)
    private Integer colony;
    @ApiObjectField(name = "namePrint", description = "Nombre a imprimir", required = true, order = 30)
    private String namePrint;
    @ApiObjectField(name = "sendEnd", description = "Envio final o previo", required = true, order = 31)
    private boolean sendEnd;
    @ApiObjectField(name = "encryptionReportResult", description = "Envio final o previo", required = true, order = 32)
    private boolean encryptionReportResult;
    @ApiObjectField(name = "centralSystem", description = "Sistema Central", required = true, order = 33)
    private Integer centralSystem;
    @ApiObjectField(name = "taxes", description = "Lista de impuestos", required = true, order = 34)
    private List<DiscountRate> taxes;
    @ApiObjectField(name = "invoice", description = "Facturar: 0 - False, 1 - True", required = true, order = 35)
    private boolean invoice;
    @ApiObjectField(name = "agreement", description = "Convenio: 0 - False, 1 - True", required = true, order = 36)
    private boolean agreement;
    @ApiObjectField(name = "usoCfdi", description = "UsoCFDI", required = true, order = 37)
    private String usoCfdi;
    @ApiObjectField(name = "regimenFiscal", description = "regimenFiscal", required = true, order = 38)
    private String usoRegimenFiscal;
    @ApiObjectField(name = "priceCompany", description = "Precio de la compañia", required = true, order = 37)
    private float priceCompany;

    public Account(Integer id)
    {
        this.id = id;
    }
}

package net.cltech.enterprisent.domain.integration.siigo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la informacion del maestro cliente que se enviara a SIIGO
*
* @version 1.0.0
* @author Julian
* @since 16/04/2021
* @see Creación
*/

@ApiObject(
        group = "Integración con Siigo",
        name = "Cuenta Siigo",
        description = "Maestro de cliente con el que se enviará información a SIIGO"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter 
@Setter
public class AccountSiigo 
{
    @ApiObjectField(name = "type", description = "Tipo de cliente: Customer, Supplier, Other - valor por default Customer.", required = false, order = 1)
    private String type;
    @ApiObjectField(name = "person_type", description = "Tipo de persona: Person, Company", required = true, order = 2)
    private String person_type;
    @ApiObjectField(name = "id_type", description = "Código del tipo de identificación del cliente.", required = true, order = 3)
    private String id_type;
    @ApiObjectField(name = "identification", description = "Número de identificación del cliente.", required = true, order = 4)
    private String identification;
    @ApiObjectField(name = "check_digit", description = "Dígito verificación, se calcula automáticamente.", required = false, order = 5)
    private String check_digit;
    @ApiObjectField(name = "name", description = "Razón social o nombres y apellidos del cliente.", required = true, order = 6)
    private List<String> name;
    @ApiObjectField(name = "commercial_name", description = "Nombre comercial o Nombre de fantasía de la empresa ciente.", required = false, order = 7)
    private String commercial_name;
    @ApiObjectField(name = "branch_office", description = "Sucursal, valor por default 0.", required = false, order = 8)
    private int branch_office;
    @ApiObjectField(name = "active", description = "Estado del cliente en Siigo, valor por default true.", required = false, order = 9)
    private boolean active;
    @ApiObjectField(name = "vat_responsible", description = "Tipo de régimen IVA. True si es responsable de IVA, False si no es responsable de IVA, valor por default false.", required = false, order = 10)
    private boolean vat_responsible;
    @ApiObjectField(name = "address", description = "Dirección del cliente", required = true, order = 12)
    private AddressSiigo address;
    @ApiObjectField(name = "phones", description = "Indica los teléfonos asociados al cliente", required = true, order = 13)
    private List<PhoneSiigo> phones;
    @ApiObjectField(name = "contacts", description = "Indica los contactos asociados al cliente", required = true, order = 14)
    private List<ContactSiigo> contacts;
    @ApiObjectField(name = "comments", description = "Observaciones.", required = false, order = 15)
    private String comments;
    @ApiObjectField(name = "idDocumentType", description = "Id del tipo de documento", required = false, order = 16)
    private Integer idDocumentType;

    public AccountSiigo()
    {
        this.name = new ArrayList<>();
        this.address = new AddressSiigo();
        this.phones = new ArrayList<>();
        this.contacts = new ArrayList<>();
    }
}
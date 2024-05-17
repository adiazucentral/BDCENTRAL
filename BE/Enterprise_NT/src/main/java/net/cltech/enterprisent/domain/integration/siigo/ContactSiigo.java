package net.cltech.enterprisent.domain.integration.siigo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa el contacto que sera enviado a Siigo
*
* @version 1.0.0
* @author Julian
* @since 29/04/2021
* @see Creación
*/

@ApiObject(
        group = "Integración con Siigo",
        name = "Contacto Siigo",
        description = "Representa el contacto de siigo que se enviará a SIIGO"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ContactSiigo 
{
    @ApiObjectField(name = "first_name", description = "Nombres del contacto", required = true, order = 1)
    private String first_name;
    @ApiObjectField(name = "last_name", description = "Apellidos del contacto", required = true, order = 2)
    private String last_name;
    @ApiObjectField(name = "email", description = "Correo electrónico del contacto", required = true, order = 3)
    private String email;
    @ApiObjectField(name = "phone", description = "Indica el teléfono asociado al contacto", required = false, order = 4)
    private PhoneSiigo phone;

    public ContactSiigo()
    {
        this.phone = new PhoneSiigo();
    }
}
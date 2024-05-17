package net.cltech.enterprisent.domain.integration.siigo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la dirección del cliente.
*
* @version 1.0.0
* @author Julian
* @since 13/05/2021
* @see Creación
*/

@ApiObject(
        group = "Integración con Siigo",
        name = "Dirección del cliente",
        description = "Representa la dirección del cliente."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class AddressSiigo 
{
    @ApiObjectField(name = "address", description = "Indicativo", required = true, order = 1)
    private String address;
    @ApiObjectField(name = "city", description = "Ciudad del cliente.", required = true, order = 2)
    private CitySiigo city;
    @ApiObjectField(name = "postal_code", description = "Código postal", required = false, order = 3)
    private String postal_code;

    public AddressSiigo()
    {
        this.city = new CitySiigo();
    }
}
package net.cltech.enterprisent.domain.integration.siigo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la información de la ciudad de la cuenta que se enviará a SIIGO
*
* @version 1.0.0
* @author Julian
* @since 16/04/2021
* @see Creación
*/

@ApiObject(
        group = "Integración con Siigo",
        name = "Ciudad Siigo",
        description = "Representa la información que se enviará de la ciudad a Siigo"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class CitySiigo 
{
    @ApiObjectField(name = "country_code", description = "Código del país", required = true, order = 1)
    private String country_code;
    @ApiObjectField(name = "state_code", description = "Código del departamento/estado", required = true, order = 2)
    private String state_code;
    @ApiObjectField(name = "city_code", description = "Código de la ciudad", required = true, order = 3)
    private String city_code;
}
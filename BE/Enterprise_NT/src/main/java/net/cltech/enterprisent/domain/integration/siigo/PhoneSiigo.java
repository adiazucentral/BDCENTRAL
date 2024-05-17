package net.cltech.enterprisent.domain.integration.siigo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa el telefono de la oficina del contacto asociado a la factura
*
* @version 1.0.0
* @author Julian
* @since 29/04/2021
* @see Creación
*/

@ApiObject(
        group = "Integración con Siigo",
        name = "Telefono Del Contacto Siigo",
        description = "Representa el telefono de la oficina del contacto asociado a la factura"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class PhoneSiigo 
{
    @ApiObjectField(name = "indicative", description = "Indicativo", required = false, order = 1)
    private String indicative;
    @ApiObjectField(name = "number", description = "Número", required = true, order = 2)
    private String number;
    @ApiObjectField(name = "extension", description = "Extensión", required = false, order = 3) 
    private String extension;
}
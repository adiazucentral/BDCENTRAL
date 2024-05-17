package net.cltech.enterprisent.domain.integration.siigo.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa el impuesto de Siigo
*
* @version 1.0.0
* @author Julian
* @since 14/05/2021
* @see Creación
*/

@ApiObject(
        group = "Integración con Siigo",
        name = "Impuesto De Siigo",
        description = "Representa el impuesto de Siigo"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class TaxSiigo 
{
    @ApiObjectField(name = "id", description = "Identificador único del impuesto", required = true, order = 1)
    private Integer id;
}
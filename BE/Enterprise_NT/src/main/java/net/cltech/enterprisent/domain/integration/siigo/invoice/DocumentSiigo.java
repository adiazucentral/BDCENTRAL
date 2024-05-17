package net.cltech.enterprisent.domain.integration.siigo.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el tipo de Comprobante
 *
 * @version 1.0.0
 * @author Julian
 * @since 29/04/2021
 * @see Creación
 */

@ApiObject(
        group = "Integración con Siigo",
        name = "Tipo De Comprobante",
        description = "Representa el tipo de Comprobante en Siigo"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class DocumentSiigo
{    
    @ApiObjectField(name = "id", description = "Identificador del comprobante", required = true, order = 1)
    private Integer id;
}

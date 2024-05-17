package net.cltech.enterprisent.domain.integration.generalMinsa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la respuesta general
 *
 * @version 1.0.0
 * @author hpoveda
 *
 * @see Creación
 */
@ApiObject(
        group = "Integración (General)",
        name = "Respuesta General",
        description = "Representa una respuesta general"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralAnswer
{

    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 1)
    private String name;
    @ApiObjectField(name = "selected", description = "Seleccionada", required = true, order = 2)
    private boolean selected;
}

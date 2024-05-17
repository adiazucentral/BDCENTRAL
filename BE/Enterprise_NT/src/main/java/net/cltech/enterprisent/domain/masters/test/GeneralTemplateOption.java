package net.cltech.enterprisent.domain.masters.test;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la opción de plantilla general
*
* @version 1.0.0
* @author Julian
* @since 18/05/2021
* @see Creación
*/

@ApiObject(
        group = "Prueba",
        name = "Opción De Plantilla General",
        description = "Representa los datos de la plantilla general"
)

@Getter
@Setter
public class GeneralTemplateOption 
{
    @ApiObjectField(name = "comment", description = "Comentario del examenes para las plantillas", required = true, order = 1)
    private String comment;
    @ApiObjectField(name = "optionTemplates", description = "Plantillas de resultados", required = true, order = 2)
    private List<OptionTemplate> optionTemplates;

    public GeneralTemplateOption()
    {
        this.optionTemplates = new ArrayList<>();
    }
}
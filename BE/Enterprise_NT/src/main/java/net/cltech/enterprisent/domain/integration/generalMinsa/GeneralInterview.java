package net.cltech.enterprisent.domain.integration.generalMinsa;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(
        group = "Integración (General)",
        name = "Entrevista General",
        description = "Representa una entrevista general"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralInterview
{

    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 1)
    private String name;
    @ApiObjectField(name = "questions", description = "Lista de preguntas", required = true, order = 2)
    private List<GeneralQuestion> questions;
}

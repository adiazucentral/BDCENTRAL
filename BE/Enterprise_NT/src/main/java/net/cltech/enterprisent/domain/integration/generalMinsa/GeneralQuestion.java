
package net.cltech.enterprisent.domain.integration.generalMinsa;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author hpoveda
 */

@ApiObject(
        group = "Integración (General)",
        name = "Pregunta General",
        description = "Representa una pregunta general"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralQuestion
{

    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "question", description = "Nombre de la pregunta", required = true, order = 2)
    private String question;
    @ApiObjectField(name = "open", description = "Es una pregunta abierta", required = true, order = 3)
    private boolean open;
    @ApiObjectField(name = "answers", description = "Lista de respuestas", required = true, order = 4)
    private List<GeneralAnswer> answers = new ArrayList<>();
    @ApiObjectField(name = "interviewAnswer", description = "Respuesta de la entrevista", required = true, order = 5)
    private String interviewAnswer;
}

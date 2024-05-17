package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un cliente
 *
 * @version 1.0.0
 * @author JDuate
 * @since 12/06/2020
 * @see Creación
 */
@ApiObject(
        group = "LIS",
        name = "Pregunta Home Bound",
        description = "Datos de una pregunta desde Home Bound"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class QuestionHomeBound
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "question", description = "Pregunta Completa", required = true, order = 2)
    private String question;
    @ApiObjectField(name = "name", description = "Nombre de la pregunta", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "control", description = "Control: Control de la Pregunta: 1 -> Texto. 5 -> Combobox", required = true, order = 4)
    private Integer control;
    @ApiObjectField(name = "answers", description = "Respuestas", required = false, order = 5)
    private List<AnswerHomeBound> answers;
    @ApiObjectField(name = "answeredIds", description = "Ids de las respuestas", required = false, order = 6)
    private List<Integer> answeredIds;
    @ApiObjectField(name = "answeredText", description = "Respuesta para preguntas abiertas", required = false, order = 7)
    private String answeredText;
}

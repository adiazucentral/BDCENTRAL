package net.cltech.enterprisent.domain.integration.skl;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa las preguntas de una entrevista con consentimiento
 * informado
 *
 * @author JDuarte
 * @version 1.0.0
 * @since 05/06/2020
 * @see Creación
 */
@ApiObject(
        group = "SKL",
        name = "Entrevista consentimiento informado",
        description = "Entrevista consentimiento informado"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InterviewInformedConsent
{

    @ApiObjectField(name = "id", description = "Id de la pregunta", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "question", description = "Nombre de la pregunta", required = true, order = 2)
    private String question;

    public InterviewInformedConsent()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

}

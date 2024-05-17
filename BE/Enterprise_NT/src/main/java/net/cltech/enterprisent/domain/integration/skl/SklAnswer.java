package net.cltech.enterprisent.domain.integration.skl;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto de Respuesta requerido por SKL 
 * para su integracion con NT
 * 
 * @version 1.0.0
 * @author Julian
 * @since 12/05/20
 * @see Creación
 */

@ApiObject(
        group = "SKL",
        name = "Respuesta",
        description = "Objeto de tipo respuesta que se le enviara a SKL según lo requiera para su integración con NT"
)
public class SklAnswer
{
    @ApiObjectField(name = "idQuestion", description = "Id de la pregunta a la que corresponde esta respuesta", required = true, order = 1)
    private int idQuestion;
    @ApiObjectField(name = "id", description = "Id de la respuesta", required = true, order = 2)
    private Integer id;
    @ApiObjectField(name = "answer", description = "Nombre de la respuesta", required = true, order = 3)
    private String answer;

    public SklAnswer()
    {
    }

    public int getIdQuestion()
    {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion)
    {
        this.idQuestion = idQuestion;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }
}

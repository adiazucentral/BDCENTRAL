package net.cltech.enterprisent.domain.integration.skl;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto de Pregunta requerido por SKL 
 * para su integracion con NT
 * 
 * @version 1.0.0
 * @author Julian
 * @since 12/05/20
 * @see Creación
 */

@ApiObject(
        group = "SKL",
        name = "Pregunta",
        description = "Objeto de tipo pregunta que se le enviara a SKL según lo requiera para su integración con NT"
)
public class SklQuestion
{
    @ApiObjectField(name = "id", description = "Id de la pregunta en NT", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "question", description = "Nombre de la pregunta en NT", required = true, order = 2)
    private String question;
    @ApiObjectField(name = "answer", description = "Respuesta a dicha pregunta en NT", required = true, order = 3)
    private SklAnswer answer;
    @ApiObjectField(name = "availableAnswer", description = "Respuestas disponibles", required = true, order = 4)
    private List<SklAnswer> availableAnswer;
    @ApiObjectField(name = "openAnswer", description = "La pregunta es abierta - (True - SI) - (False - NO)", required = true, order = 5)
    private boolean openAnswer;
    @ApiObjectField(name = "order", description = "Id de la orden a la que corresponde esta pregunta", required = true, order = 6)
    private int order;

    public SklQuestion()
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

    public boolean isOpenAnswer()
    {
        return openAnswer;
    }

    public void setOpenAnswer(boolean openAnswer)
    {
        this.openAnswer = openAnswer;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public SklAnswer getAnswer()
    {
        return answer;
    }

    public void setAnswer(SklAnswer answer)
    {
        this.answer = answer;
    }

    public List<SklAnswer> getAvailableAnswer()
    {
        return availableAnswer;
    }

    public void setAvailableAnswer(List<SklAnswer> availableAnswer)
    {
        this.availableAnswer = availableAnswer;
    }
}

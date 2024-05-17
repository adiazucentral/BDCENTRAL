package net.cltech.enterprisent.domain.integration.skl;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto de Orden de respuesta requerido por SKL 
 * para su integracion con NT
 * 
 * @version 1.0.0
 * @author Julian
 * @since 20/05/20
 * @see Creación
 */

@ApiObject(
        group = "SKL",
        name = "Orden de respuesta",
        description = "Objeto de tipo orden de respuesta que se le enviara a SKL según lo requiera para su integración con NT"
)
public class SklOrderAnswer
{
    @ApiObjectField(name = "order", description = "Id de la orden en NT", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "idPatient", description = "Id del paciente", required = true, order = 2)
    private int idPatient;
    @ApiObjectField(name = "idQuestion", description = "Id de la pregunta", required = true, order = 3)
    private int idQuestion;
    @ApiObjectField(name = "idSelectAnswer", description = "Id de la respuesta seleccionada", required = true, order = 4)
    private int idSelectAnswer;
    @ApiObjectField(name = "textAnswer", description = "El texto de la respuesta por si es abierta", required = true, order = 5)
    private String textAnswer;
    @ApiObjectField(name = "orderType", description = "Tipo de orden", required = true, order = 6)
    private Integer orderType;

    public long getOrder()
    {
        return order;
    }

    public void setOrder(long order)
    {
        this.order = order;
    }

    public int getIdPatient()
    {
        return idPatient;
    }

    public void setIdPatient(int idPatient)
    {
        this.idPatient = idPatient;
    }

    public int getIdQuestion()
    {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion)
    {
        this.idQuestion = idQuestion;
    }

    public int getIdSelectAnswer()
    {
        return idSelectAnswer;
    }

    public void setIdSelectAnswer(int idSelectAnswer)
    {
        this.idSelectAnswer = idSelectAnswer;
    }

    public String getTextAnswer()
    {
        return textAnswer;
    }

    public void setTextAnswer(String textAnswer)
    {
        this.textAnswer = textAnswer;
    }

    public Integer getOrderType()
    {
        return orderType;
    }

    public void setOrderType(Integer orderType)
    {
        this.orderType = orderType;
    }
}

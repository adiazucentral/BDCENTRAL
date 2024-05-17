package net.cltech.enterprisent.domain.masters.interview;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Preguntas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 14/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Entrevista",
        name = "Pregunta",
        description = "Muestra informacion del maestro Preguntas que usa el API"
)
public class Question extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id de la pregunta", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de la pregunta", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "question", description = "Pregunta", required = true, order = 3)
    private String question;
    @ApiObjectField(name = "open", description = "Tipo Pregunta", required = true, order = 4)
    private boolean open;
    @ApiObjectField(name = "control", description = "Control de la Pregunta: <br> <b>Abierta:</b> <br> 1 -> Texto. <br> 2 -> Numerico. <br> 3 -> Comentario. <br> 4 -> Fecha. <br> <b>Cerrada:</b> <br> 5 -> Combobox <br> 6 -> Combobox Multiple.", required = true, order = 5)
    private Short control;
    @ApiObjectField(name = "state", description = "Estado de la pregunta", required = true, order = 6)
    private boolean state;
    @ApiObjectField(name = "answers", description = "Respuestas de la pregunta", required = true, order = 7)
    private List<Answer> answers;
    @ApiObjectField(name = "order", description = "Orden de la pregunta asociada a la entrevista", required = true, order = 8)
    private Integer order;
    @ApiObjectField(name = "select", description = "Si esta seleccionada", required = true, order = 9)
    private boolean select;
    @ApiObjectField(name = "interviewAnswer", description = "Se usa como respuesta en la entrevista", required = false, order = 10)
    private String interviewAnswer;
    @ApiObjectField(name = "required", description = "Valida si la pregunta es obligatoria", required = false, order = 11)
    private boolean required;
    @ApiObjectField(name = "lastModificationDate", description = "Ultima fecha de modificación", required = false, order = 12)
    private Date lastModificationDate;
    @ApiObjectField(name = "lastUserModify", description = "Ultimo usuario en modificar la pregunta", required = false, order = 13)
    private int lastUserModify;
    @ApiObjectField(name = "interview", description = "Tipo de entrevista a la que pertenece la pregunta", required = true, order = 14)
    private Integer interview;
    @ApiObjectField(name = "typeInterview", description = "Tipo de entrevista asociada a la pregunta que puede ser : Tipo de Orden - Laboratorio - Examen - Destino", required = true, order = 15)
    private TypeInterview typeInterview;
    @ApiObjectField(name = "orderNumber", description = "Numero de orden asociada a la pregunta", required = true, order = 8)
    private Long orderNumber;

    public Question(Integer id)
    {
        this.id = id;
        this.typeInterview = new TypeInterview();
    }

    public Question()
    {
        this.answers = new ArrayList<>();
        this.typeInterview = new TypeInterview();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public boolean isOpen()
    {
        return open;
    }

    public void setOpen(boolean open)
    {
        this.open = open;
    }

    public Short getControl()
    {
        return control;
    }

    public void setControl(Short control)
    {
        this.control = control;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public List<Answer> getAnswers()
    {
        return answers;
    }

    public void setAnswers(List<Answer> answers)
    {
        this.answers = answers;
    }

    public Integer getOrder()
    {
        return order;
    }

    public void setOrder(Integer order)
    {
        this.order = order;
    }

    public boolean isSelect()
    {
        return select;
    }

    public void setSelect(boolean select)
    {
        this.select = select;
    }

    public String getInterviewAnswer()
    {
        return interviewAnswer;
    }

    public void setInterviewAnswer(String interviewAnswer)
    {
        this.interviewAnswer = interviewAnswer;
    }

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }

    public Date getLastModificationDate()
    {
        return lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate)
    {
        this.lastModificationDate = lastModificationDate;
    }

    public int getLastUserModify()
    {
        return lastUserModify;
    }

    public void setLastUserModify(int lastUserModify)
    {
        this.lastUserModify = lastUserModify;
    }

    public Integer getInterview()
    {
        return interview;
    }

    public void setInterview(Integer interview)
    {
        this.interview = interview;
    }

    public TypeInterview getTypeInterview()
    {
        return typeInterview;
    }

    public void setTypeInterview(TypeInterview typeInterview)
    {
        this.typeInterview = typeInterview;
    }

    public Long getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Question other = (Question) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }
}

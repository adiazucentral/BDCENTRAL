package net.cltech.enterprisent.domain.masters.interview;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Entrevista
 *
 * @version 1.0.0
 * @author enavas
 * @since 17/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Entrevista",
        name = "Entrevista",
        description = "Muestra informacion del maestro Entrevista que usa el API"
)
public class Interview extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id de la entrevista", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de la entrevista", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "type", description = "Tipo de entrevista:  1 -> Orden. 2 -> Laboratorio. 3 -> Examen. 4 -> Destino.", required = true, order = 3)
    private Short type;
    @ApiObjectField(name = "panic", description = "Indica si es de panico", required = true, order = 4)
    private boolean panic;
    @ApiObjectField(name = "state", description = "Estado de la entrevista", required = true, order = 5)
    private boolean state;
    @ApiObjectField(name = "questions", description = "Lista de presguntas asociadas", required = true, order = 6)
    private List<Question> questions;
    @ApiObjectField(name = "typeInterview", description = "Lista de tipo de entrevista ", required = true, order = 7)
    private List<TypeInterview> typeInterview;
    @ApiObjectField(name = "informedConsent", description = "Consentimiento informado", required = true, order = 8)
    private boolean informedConsent;
    @ApiObjectField(name = "ordering", description = "Ordenamiento", required = true, order = 9)
    private Short ordering;

    public Short getOrdering()
    {
        return ordering;
    }

    public void setOrdering(Short ordering)
    {
        this.ordering = ordering;
    }

    public Interview()
    {
        this.questions = new ArrayList<>();
        this.typeInterview = new ArrayList<>();
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

    public Short getType()
    {
        return type;
    }

    public void setType(Short type)
    {
        this.type = type;
    }

    public boolean isPanic()
    {
        return panic;
    }

    public void setPanic(boolean panic)
    {
        this.panic = panic;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public List<Question> getQuestions()
    {
        return questions;
    }

    public void setQuestions(List<Question> questions)
    {
        this.questions = questions;
    }

    public List<TypeInterview> getTypeInterview()
    {
        return typeInterview;
    }

    public void setTypeInterview(List<TypeInterview> typeInterview)
    {
        this.typeInterview = typeInterview;
    }

    public boolean isInformedConsent()
    {
        return informedConsent;
    }

    public void setInformedConsent(boolean informedConsent)
    {
        this.informedConsent = informedConsent;
    }

}

package net.cltech.enterprisent.domain.integration.homebound;

import java.sql.Timestamp;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de las preguntas para calificar el
 * servicio
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 12/06/2020
 * @see Creación
 */
@ApiObject(
        group = "Entrevista",
        name = "Preguntas del servicio desde Home Bound",
        description = "Muestra informacion de las preguntas para calificar el servicio en la API"
)
public class QuestionService
{

    @ApiObjectField(name = "id", description = "Id de la pregunta", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "question", description = "Enunciado de la pregunta", required = true, order = 2)
    private String question;
    @ApiObjectField(name = "state", description = "Estado de la pregunta", required = true, order = 3)
    private int state;
    @ApiObjectField(name = "userCreate", description = "Usuario que creo la pregunta", required = true, order = 4)
    private UserHomeBound userCreate;
    @ApiObjectField(name = "dateCreate", description = "Fecha en la que se creo la pregunta", required = true, order = 5)
    private Timestamp dateCreate;
    @ApiObjectField(name = "userUpdate", description = "Usuario que creo la pregunta", required = false, order = 6)
    private UserHomeBound userUpdate;
    @ApiObjectField(name = "dateUpdate", description = "Fecha en la que se actualizo la pregunta", required = false, order = 7)
    private Timestamp dateUpdate;
    @ApiObjectField(name = "order", description = "Orden de la pregunta", required = true, order = 8)
    private int order;
    @ApiObjectField(name = "responseService", description = "Respuesta de la pregunta", required = true, order = 9)
    private ResponseService responseService;

    public QuestionService()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public UserHomeBound getUserCreate()
    {
        return userCreate;
    }

    public void setUserCreate(UserHomeBound userCreate)
    {
        this.userCreate = userCreate;
    }

    public Timestamp getDateCreate()
    {
        return dateCreate;
    }

    public void setDateCreate(Timestamp dateCreate)
    {
        this.dateCreate = dateCreate;
    }

    public UserHomeBound getUserUpdate()
    {
        return userUpdate;
    }

    public void setUserUpdate(UserHomeBound userUpdate)
    {
        this.userUpdate = userUpdate;
    }

    public Timestamp getDateUpdate()
    {
        return dateUpdate;
    }

    public void setDateUpdate(Timestamp dateUpdate)
    {
        this.dateUpdate = dateUpdate;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public ResponseService getResponseService()
    {
        return responseService;
    }

    public void setResponseService(ResponseService responseService)
    {
        this.responseService = responseService;
    }

}

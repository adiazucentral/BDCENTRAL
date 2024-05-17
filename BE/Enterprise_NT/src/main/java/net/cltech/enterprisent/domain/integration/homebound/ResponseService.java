package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de las respuestas dadas a las preguntas
 * de calificacion el servicio
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 12/06/2020
 * @see Creación
 */
@ApiObject(
        group = "Entrevista",
        name = "Respuestas de las preguntas",
        description = "Respuestas de las preguntas sobre servicio"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseService
{

    @ApiObjectField(name = "idQuestion", description = "Id de la pregunta", required = true, order = 1)
    private Integer idQuestion;
    @ApiObjectField(name = "idAppointment", description = "Id de la cita", required = true, order = 2)
    private Integer idAppointment;
    @ApiObjectField(name = "user", description = "Usuario al que se califica", required = true, order = 3)
    private UserHomeBound user;
    @ApiObjectField(name = "date", description = "Fecha en la que se califico", required = true, order = 4)
    private Timestamp date;
    @ApiObjectField(name = "responseValue", description = "Valor de la respuesta", required = true, order = 5)
    private int responseValue;

    public ResponseService()
    {
    }

    public Integer getIdQuestion()
    {
        return idQuestion;
    }

    public void setIdQuestion(Integer idQuestion)
    {
        this.idQuestion = idQuestion;
    }

    public Integer getIdAppointment()
    {
        return idAppointment;
    }

    public void setIdAppointment(Integer idAppointment)
    {
        this.idAppointment = idAppointment;
    }

    public UserHomeBound getUser()
    {
        return user;
    }

    public void setUser(UserHomeBound user)
    {
        this.user = user;
    }

    public Timestamp getDate()
    {
        return date;
    }

    public void setDate(Timestamp date)
    {
        this.date = date;
    }

    public int getResponseValue()
    {
        return responseValue;
    }

    public void setResponseValue(int responseValue)
    {
        this.responseValue = responseValue;
    }

}

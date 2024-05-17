package net.cltech.enterprisent.domain.integration.mobile;

import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa el objeto de retorno cuando restauro una clave
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 24/08/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "RestorePassword",
        description = "Objeto retorno de restaurar la contraseña del paciente en la aplicación"
)
public class RestorePassword
{

    @ApiObjectField(name = "success", description = "True es si se logro dejar en null el campo de la contraseña", required = true, order = 1)
    private boolean success;
    @ApiObjectField(name = "state", description = "1 - es si se logro dejar en null el campo de la contraseña", required = true, order = 2)
    private Integer state;
    @ApiObjectField(name = "date", description = " Fecha actual", required = true, order = 3)
    private Date date;

    public RestorePassword()
    {
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

}

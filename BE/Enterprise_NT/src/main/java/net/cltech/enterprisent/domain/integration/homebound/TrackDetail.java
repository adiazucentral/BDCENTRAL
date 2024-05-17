package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa detalle de trazabilidad para homebound
 *
 * @version 1.0.0
 * @author eacuna
 * @since 05/04/2021
 * @see Creación
 */
@ApiObject(
        group = "Homebound",
        name = "Trazabilidad Detalle",
        description = "Cita de homebound"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrackDetail
{

    @ApiObjectField(name = "group", description = "Grupo de trazabilidad(orden,demoorden)", order = 1)
    private String group;
    @ApiObjectField(name = "action", description = "Acción que se realiza", order = 2)
    private String action;
    @ApiObjectField(name = "field", description = "Campo afectado", order = 3)
    private String field;
    @ApiObjectField(name = "actionDate", description = "Fecha de la acción yyyy-MM-dd hh:mm:ss.sss", order = 4)
    private String actionDate;
    @ApiObjectField(name = "user", description = "Username del usurio que realiza la acción", order = 5)
    private String user;
    @ApiObjectField(name = "currentValue", description = "Valor actual", order = 6)
    private String currentValue;
    @ApiObjectField(name = "previosValue", description = "Valor anterior", order = 7)
    private String previosValue;
    @ApiObjectField(name = "code", description = "Código del examen", order = 8)
    private String code;
    @ApiObjectField(name = "test", description = "Nombre del examen", order = 9)
    private String test;

    public String getGroup()
    {
        return group;
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public String getField()
    {
        return field;
    }

    public void setField(String field)
    {
        this.field = field;
    }

    public String getActionDate()
    {
        return actionDate;
    }

    public void setActionDate(String actionDate)
    {
        this.actionDate = actionDate;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getCurrentValue()
    {
        return currentValue;
    }

    public void setCurrentValue(String currentValue)
    {
        this.currentValue = currentValue;
    }

    public String getPreviosValue()
    {
        return previosValue;
    }

    public void setPreviosValue(String previosValue)
    {
        this.previosValue = previosValue;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getTest()
    {
        return test;
    }

    public void setTest(String test)
    {
        this.test = test;
    }

}

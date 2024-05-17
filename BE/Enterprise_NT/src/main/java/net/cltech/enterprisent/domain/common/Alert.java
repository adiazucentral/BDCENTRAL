package net.cltech.enterprisent.domain.common;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Alarmas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 06/06/2017
 * @see Creacion
 */
@ApiObject(
        group = "Comun",
        name = "Alerta",
        description = "Representa alertas del sistema"
)
public class Alert
{

    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "description", description = "Descripción", required = true, order = 3)
    private String description;
    @ApiObjectField(name = "type", description = "Tipo alerta", required = true, order = 3)
    private Integer type;
    @ApiObjectField(name = "item", description = "Item que es afectado por la alerta", required = true, order = 3)
    private String item;
    @ApiObjectField(name = "form", description = "Formulario donde se visualiza la alerta", required = true, order = 3)
    private String form;
    @ApiObjectField(name = "state", description = "Estado de la alerta", required = true, order = 4)
    private Integer state;

    public Alert()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getItem()
    {
        return item;
    }

    public void setItem(String item)
    {
        this.item = item;
    }

    public String getForm()
    {
        return form;
    }

    public void setForm(String form)
    {
        this.form = form;
    }

}

package net.cltech.enterprisent.domain.integration.homebound;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Motivos
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 12/06/2020
 * @see Creación
 */
@ApiObject(
        group = "Común",
        name = "Motivo Desde Home Bound",
        description = "Muestra los motivos que usa el API"
)
public class ReasonHomeBound
{

    @ApiObjectField(name = "id", description = "Id del motivo", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre del motivo", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "type", description = "Tipo del motivo Cancelado->1, Reprogramado->2", required = true, order = 3)
    private Integer type;
    @ApiObjectField(name = "state", description = "Estado del motivo", required = true, order = 4)
    private boolean state;
    @ApiObjectField(name = "description", description = "Descripcion del motivo", required = true, order = 4)
    private String description;

    public ReasonHomeBound()
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
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

}

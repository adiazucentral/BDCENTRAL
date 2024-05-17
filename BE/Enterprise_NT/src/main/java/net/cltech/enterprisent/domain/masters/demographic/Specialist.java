package net.cltech.enterprisent.domain.masters.demographic;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Especialista
 *
 * @version 1.0.0
 * @author eacuna
 * @since 09/05/2017
 * @see Creación
 */
@ApiObject(
        group = "Demográfico",
        name = "Especialista",
        description = "Muestra informacion de  especialistas "
)
public class Specialist extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id base de datos", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de especialista", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "state", description = "Estado del especialista", required = true, order = 3)
    private boolean state;

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

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    @Override
    public String toString()
    {
        return "Specialist{" + "id=" + id + ", name=" + name + ", state=" + state + '}';
    }

}

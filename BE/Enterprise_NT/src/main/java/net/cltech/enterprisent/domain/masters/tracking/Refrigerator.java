package net.cltech.enterprisent.domain.masters.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Neveras
 *
 * @version 1.0.0
 * @author eacuna
 * @since 08/06/2017
 * @see Creacion
 */
@ApiObject(
        group = "Trazabilidad",
        name = "Nevera",
        description = "Representa neveras"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Refrigerator extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "state", description = "Si esta activo", required = true, order = 4)
    private boolean state;
    @ApiObjectField(name = "branch", description = "Sede a la que pertenece la nevera", required = true, order = 5)
    private Branch branch;

    public Refrigerator()
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

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public Branch getBranch()
    {
        return branch;
    }

    public void setBranch(Branch branch)
    {
        this.branch = branch;
    }

    @Override
    public String toString()
    {
        return "Refrigerator{" + "id=" + id + ", name=" + name + ", state=" + state + '}';
    }

}

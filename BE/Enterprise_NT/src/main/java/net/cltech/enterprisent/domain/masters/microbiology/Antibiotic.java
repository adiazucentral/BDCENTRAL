package net.cltech.enterprisent.domain.masters.microbiology;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Antibioticos
 *
 * @version 1.0.0
 * @author eacuna
 * @since 07/06/2017
 * @see Creacion
 */
@ApiObject(
        group = "Microbiología",
        name = "Antibiotico",
        description = "Representa antibioticos"
)
public class Antibiotic extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "state", description = "Si esta activo", required = true, order = 3)
    private boolean state;
    
    public Antibiotic()
    {
    }

    public Antibiotic(Integer id)
    {
        this.id = id;
    }

    public Antibiotic(String name)
    {
        this.name = name;
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

    @Override
    public String toString()
    {
        return "Antibiotic{" + "id=" + id + ", name=" + name + ", state=" + state + '}';
    }

}

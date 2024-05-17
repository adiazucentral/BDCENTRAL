package net.cltech.enterprisent.domain.masters.test;

import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
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
        group = "Prueba",
        name = "Alarma",
        description = "Representa alarmas"
)
public class Alarm extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "description", description = "Descripción", required = true, order = 3)
    private String description;
    @ApiObjectField(name = "state", description = "Si esta activo", required = true, order = 4)
    private boolean state;
    @ApiObjectField(name = "rules", description = "Reglas de alarma", required = true, order = 5)
    private List<ResultRelationship> rules;

    public Alarm()
    {
    }

    public Alarm(Integer id)
    {
        this.id = id;
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<ResultRelationship> getRules()
    {
        return rules;
    }

    public void setRules(List<ResultRelationship> rules)
    {
        this.rules = rules;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Alarm other = (Alarm) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}

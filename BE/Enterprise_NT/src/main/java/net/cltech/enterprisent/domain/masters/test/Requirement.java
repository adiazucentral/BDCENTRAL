package net.cltech.enterprisent.domain.masters.test;

import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa un requisito para la toma del examen
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/04/2017
 * @see Creación
 */
@ApiObject(
        group = "PRUEBA",
        name = "Requisito",
        description = "Muestra informacion del requisito para la realización del exámen"
)
public class Requirement extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Identificador autonumérico del requisito", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Código que identifica el requisito", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "requirement", description = "Requisito", required = true, order = 3)
    private String requirement;
    @ApiObjectField(name = "active", description = "Identifica si el requisito se encuentra activo o inactivo ", required = true, order = 4)
    private boolean state;
    @ApiObjectField(name = "selected", description = "Indica si el requerimiento esta asociado al examen consultado", required = true, order = 4)
    private boolean selected;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getRequirement()
    {
        return requirement;
    }

    public void setRequirement(String requirement)
    {
        this.requirement = requirement;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final Requirement other = (Requirement) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }
}

package net.cltech.enterprisent.domain.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Grupo Etario
 *
 * @version 1.0.0
 * @author eacuna
 * @since 31/01/2018
 * @see Creación
 */
@ApiObject(
        group = "Demográfico",
        name = "Grupo Etario",
        description = "Muestra informacion de grupos etarios "
)
@JsonInclude(Include.NON_NULL)
public class AgeGroup extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id base de datos", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "gender", description = "Genero", required = true, order = 4)
    private Item gender;
    @ApiObjectField(name = "unitAge", description = "Unidad de edad", required = true, order = 5)
    private Short unitAge;
    @ApiObjectField(name = "ageMin", description = "Edad minima", required = true, order = 6)
    private Integer ageMin;
    @ApiObjectField(name = "ageMax", description = "Edad maxima", required = true, order = 7)
    private Integer ageMax;
    @ApiObjectField(name = "state", description = "Estado", required = true, order = 8)
    private boolean state;

    public AgeGroup()
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

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public Item getGender()
    {
        return gender;
    }

    public void setGender(Item gender)
    {
        this.gender = gender;
    }

    public Short getUnitAge()
    {
        return unitAge;
    }

    public void setUnitAge(Short unitAge)
    {
        this.unitAge = unitAge;
    }

    public Integer getAgeMin()
    {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin)
    {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax()
    {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax)
    {
        this.ageMax = ageMax;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
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
        final AgeGroup other = (AgeGroup) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}

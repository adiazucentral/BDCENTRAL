package net.cltech.enterprisent.domain.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Areas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 09/05/2017
 * @see Creación
 */
@ApiObject(
        group = "Demográfico",
        name = "Raza",
        description = "Muestra informacion de las razas "
)
@JsonInclude(Include.NON_NULL)
public class Race extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id base de datos", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de la raza", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "state", description = "Estado del area", required = true, order = 3)
    private boolean state;
    @ApiObjectField(name = "value", description = "Valor para las formulas", required = false, order = 4)
    private Float value;
    @ApiObjectField(name = "code", description = "Codigo de la raza", required = true, order = 5)
    private String code;
    @ApiObjectField(name = "email", description = "Email", required = true, order = 6)
    private String email;

    public Race()
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

    public Float getValue()
    {
        return value;
    }

    public void setValue(Float value)
    {
        this.value = value;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
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
        final Race other = (Race) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}

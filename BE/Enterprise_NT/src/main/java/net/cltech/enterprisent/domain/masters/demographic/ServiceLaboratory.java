package net.cltech.enterprisent.domain.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Servicio
 *
 * @version 1.0.0
 * @author eacuna
 * @since 11/05/2017
 * @see Creación
 */
@ApiObject(
        group = "Demográfico",
        name = "Servicio",
        description = "Muestra informacion de  servicios "
)
@JsonInclude(Include.NON_NULL)
public class ServiceLaboratory extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id base de datos", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Código del servicio", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre de servicio", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "min", description = "Valor minimo de la orden", required = true, order = 3)
    private Integer min;
    @ApiObjectField(name = "max", description = "Valor maximo de la orden", required = true, order = 4)
    private Integer max;
    @ApiObjectField(name = "external", description = "Si es de consulta externa", required = true, order = 5)
    private boolean external;
    @ApiObjectField(name = "state", description = "Estado del servicio", required = true, order = 6)
    private boolean state;
    @ApiObjectField(name = "hospitalSampling", description = "Toma de muestra hospitalaria", required = true, order = 7)
    private boolean hospitalSampling;
    @ApiObjectField(name = "priorityAlarm", description = "Alarma por prioridad", required = true, order = 8)
    private boolean priorityAlarm;
    @ApiObjectField(name = "email", description = "Email", required = true, order = 6)
    private String email;

    

    public ServiceLaboratory()
    {
    }

    public ServiceLaboratory(Integer id)
    {
        this.id = id;
    }

    /**
     * @return the min
     */
    public Integer getMin()
    {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(Integer min)
    {
        this.min = min;
    }

    /**
     * @return the max
     */
    public Integer getMax()
    {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(Integer max)
    {
        this.max = max;
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

    public boolean isExternal()
    {
        return external;
    }

    public void setExternal(boolean external)
    {
        this.external = external;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public boolean getHospitalSampling()
    {
        return hospitalSampling;
    }

    public void setHospitalSampling(boolean hospitalSampling)
    {
        this.hospitalSampling = hospitalSampling;
    }

    public boolean getPriorityAlarm()
    {
        return priorityAlarm;
    }

    public void setPriorityAlarm(boolean priorityAlarm)
    {
        this.priorityAlarm = priorityAlarm;
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
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final ServiceLaboratory other = (ServiceLaboratory) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "ServiceLaboratory{" + "id=" + id + ", code=" + code + ", name=" + name + ", min=" + min + ", max=" + max + ", external=" + external + ", state=" + state + ", hospitalSampling=" + hospitalSampling + ", priorityAlarm=" + priorityAlarm + '}';
    }

}

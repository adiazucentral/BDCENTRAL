package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Objects;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un cambio
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 11/06/2020
 * @see Creación
 */
@ApiObject(
        group = "Configuracion",
        name = "Jornada",
        description = "Jornada de ruteros"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Shift
{

    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "init", description = "Hora inicio de jornada hhmm", required = true, order = 3)
    private Integer init;
    @ApiObjectField(name = "end", description = "Hora fin de jornada hhmm", required = true, order = 3)
    private Integer end;
    @ApiObjectField(name = "days", description = "Días de la jornada(1-Lunes ... 7-Domingo)", required = true, order = 3)
    private List<Integer> days;
    @ApiObjectField(name = "state", description = "Si se encuentra activa", required = true, order = 4)
    private boolean state;
    @ApiObjectField(name = "selected", description = "Seleccionado", required = true, order = 5)
    private boolean selected;
    @ApiObjectField(name = "quantity", description = "Indica la cantidad de servicios que puede atender el rutero en la jornada", required = true, order = 4)
    private Integer quantity;
    @ApiObjectField(name = "amount", description = "Indica la cantidad de servicios que tiene asignado el rutero en la jornada por dia", required = true, order = 4)
    private Integer amount;

    public Shift()
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

    public Integer getInit()
    {
        return init;
    }

    public void setInit(Integer init)
    {
        this.init = init;
    }

    public Integer getEnd()
    {
        return end;
    }

    public void setEnd(Integer end)
    {
        this.end = end;
    }

    public List<Integer> getDays()
    {
        return days;
    }

    public void setDays(List<Integer> days)
    {
        this.days = days;
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

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public Integer getAmount()
    {
        return amount;
    }

    public void setAmount(Integer amount)
    {
        this.amount = amount;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final Shift other = (Shift) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}

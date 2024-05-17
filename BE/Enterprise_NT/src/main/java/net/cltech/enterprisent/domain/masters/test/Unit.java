package net.cltech.enterprisent.domain.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la unidad para un examen
 *
 * @version 1.0.0
 * @author eacuna
 * @since 12/04/2017
 * @see Creación
 */
@ApiObject(
        group = "PRUEBA",
        name = "Unidad",
        description = "Muestra informacion de la unidad del exámen"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Unit extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Identificador autonumérico de la unidad", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de la unidad", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "international", description = "Unidad internacional", required = false, order = 3)
    private String international;
    @ApiObjectField(name = "conversionFactor", description = "Factor de conversion para la unidad internacional", required = false, order = 4)
    private BigDecimal conversionFactor;
    @ApiObjectField(name = "state", description = "Identifica si la unidad se encuentra activa o inactiva ", required = false, order = 5)
    private boolean state;

    public Unit()
    {
    }

    public Unit(Integer id)
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

    public String getInternational()
    {
        return international;
    }

    public void setInternational(String international)
    {
        this.international = international;
    }

    public BigDecimal getConversionFactor()
    {
        return conversionFactor;
    }

    public void setConversionFactor(BigDecimal conversionFactor)
    {
        this.conversionFactor = conversionFactor;
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
        final Unit other = (Unit) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}

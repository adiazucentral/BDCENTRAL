/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.domain.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;
import net.cltech.securitynt.domain.masters.common.Item;
import net.cltech.securitynt.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Areas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 12/04/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Area",
        description = "Muestra informacion del maestro Areas que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Area extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id del area", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "ordering", description = "Ordenamiento del area", required = true, order = 2)
    private Short ordering;
    @ApiObjectField(name = "abbreviation", description = "Abreviatura del area", required = true, order = 3)
    private String abbreviation;
    @ApiObjectField(name = "name", description = "Nombre del area", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "color", description = "Color del area", required = true, order = 5)
    private String color;
    @ApiObjectField(name = "type", description = "Tipo del area", required = true, order = 6)
    private Item type;
    @ApiObjectField(name = "state", description = "Estado del area", required = true, order = 9)
    private boolean state;
    @ApiObjectField(name = "partialValidation", description = "Validacion parcial", required = true, order = 10)
    private boolean partialValidation;

    public Area(Integer id)
    {
        this.id = id;
    }

    public Area()
    {
        type = new Item();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Short getOrdering()
    {
        return ordering;
    }

    public void setOrdering(Short ordering)
    {
        this.ordering = ordering;
    }

    public String getAbbreviation()
    {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public Item getType()
    {
        return type;
    }

    public void setType(Item type)
    {
        this.type = type;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public boolean isPartialValidation()
    {
        return partialValidation;
    }

    public void setPartialValidation(boolean partialValidation)
    {
        this.partialValidation = partialValidation;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
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
        final Area other = (Area) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro de Diagnostico
 *
 * @author enavas
 * @version 1.0.0
 * @since 21/06/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Diagnostico",
        description = "Representa un diagnostico"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Diagnostic extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id del diagnostico", order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo del diagnostico", order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre del diagnostico", order = 3)
    private String name;
    @ApiObjectField(name = "type", description = "Tipo de diagnostico", order = 4)
    private Item type;
    @ApiObjectField(name = "state", description = "Estado del diagnostico", order = 5)
    private boolean state;
    @ApiObjectField(name = "selected", description = "Indica si el diagnostico se encuentra asignado", order = 6)
    private Boolean selected;

    public Diagnostic()
    {
        type = new Item();
    }

    public Diagnostic(Integer id)
    {
        this.id = id;
    }

    public Diagnostic(Integer id, String code, String name, Integer type)
    {
        this.id = id;
        this.code = code;
        this.name = name;
        this.type = new Item(type);

    }

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public Boolean isSelected()
    {
        return selected;
    }

    public void setSelected(Boolean selected)
    {
        this.selected = selected;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.code);
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
        final Diagnostic other = (Diagnostic) obj;
        if (!Objects.equals(this.code, other.code))
        {
            return false;
        }
        return true;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.domain.masters.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro de modulos
 *
 * @author enavas
 * @since 26/05/2017
 * @see Creación
 */
@ApiObject(
        group = "Usuario",
        name = "Modulo",
        description = "Información de modulos registrados en el sistema"
)
public class Module
{

    @ApiObjectField(name = "id", description = "Id del modulo", order = 1)
    private Integer id;
    @ApiObjectField(name = "idFather", description = "Id del modulo padre", order = 2)
    private Integer idFather;
    @ApiObjectField(name = "name", description = "nombre del modulo", order = 3)
    private String name;
    @ApiObjectField(name = "submodules", description = "hijos del modulo", order = 4)
    private List<Module> submodules = new ArrayList<>();
    @ApiObjectField(name = "access", description = "Indica si tiene acceso al módulo", order = 5)
    private boolean access;

    public Module()
    {

    }

    public Module(Integer id)
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

    public Integer getIdFather()
    {
        return idFather;
    }

    public void setIdFather(Integer idFather)
    {
        this.idFather = idFather;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Module> getSubmodules()
    {
        return submodules;
    }

    public void setSubmodules(List<Module> submodules)
    {
        this.submodules = submodules;
    }

    public boolean isAccess()
    {
        return access;
    }

    public void setAccess(boolean access)
    {
        this.access = access;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
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
        final Module other = (Module) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}

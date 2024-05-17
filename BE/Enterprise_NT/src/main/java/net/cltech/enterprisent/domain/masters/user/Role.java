package net.cltech.enterprisent.domain.masters.user;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa un Role del sistema
 *
 * @version 1.0.0
 * @author eacuna
 * @since 05/05/2017
 * @see Creación
 */
@ApiObject(
        group = "Usuario",
        name = "Rol",
        description = "Información de roles registrados en el sistema"
)
public class Role extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Identificador autonumérico del rol", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre del rol en el sistema", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "administrator", description = "Identifica si el rol es administrador", required = true, order = 3)
    private boolean administrator;
    @ApiObjectField(name = "state", description = "Identifica si el rol se encuentra activo(true) o inactivo(false) ", required = false, order = 4)
    private boolean state;
    private List<Module> modules;

    public Role()
    {
        modules = new ArrayList<>();
    }

    public List<Module> getModules()
    {
        return modules;
    }

    public Role setModules(List<Module> modules)
    {
        this.modules = modules;
        return this;
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

    public boolean isAdministrator()
    {
        return administrator;
    }

    public void setAdministrator(boolean administrator)
    {
        this.administrator = administrator;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public List<Module> getAllowedModules(List<Module> all, List<Module> allowed)
    {
        all.forEach((module) ->
        {
            if (allowed.contains(module))
            {
                all.get(all.indexOf(module)).setAccess(true);
            }
            getAllowedModules(module.getSubmodules(), allowed);

        });
        return all;
    }

}

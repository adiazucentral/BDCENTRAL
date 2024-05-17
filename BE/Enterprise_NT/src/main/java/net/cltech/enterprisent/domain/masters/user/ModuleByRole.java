/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.user;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro de modulos por rol
 *
 * @author enavas
 * @since 26/05/2017
 * @see Creación
 */
@ApiObject(
        group = "Usuario",
        name = "Modulo por Rol",
        description = "Información de modulos  por Rol registrados en el sistema"
)
public class ModuleByRole
{

    @ApiObjectField(name = "module", description = "Modulos", required = true, order = 1)
    private Module modules;
    @ApiObjectField(name = "rol", description = "rol del usuario", required = true, order = 2)
    private Role rol;
    @ApiObjectField(name = "access", description = "Acceso", required = false, order = 3)
    private boolean access;

    public ModuleByRole()
    {
        modules = new Module();
        rol = new Role();
    }

    public ModuleByRole(int idModule)
    {
        modules = new Module();
        rol = new Role();
        modules.setId(idModule);
    }

    public Module getModules()
    {
        return modules;
    }

    public void setModules(Module module)
    {
        this.modules = module;
    }

    public Role getRol()
    {
        return rol;
    }

    public void setRol(Role rol)
    {
        this.rol = rol;
    }

    public boolean isAccess()
    {
        return access;
    }

    public void setAccess(boolean access)
    {
        this.access = access;
    }

}

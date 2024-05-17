/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.domain.masters.user;

import net.cltech.securitynt.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la combinación entre rol y usuario
 *
 * @version 1.0.0
 * @author cmartin
 * @since 09/05/2017
 * @see Creacion
 */
@ApiObject(
        group = "Usuario",
        name = "Rol por usuario",
        description = "Representa la combinación entre rol y usuario"
)
public class RoleByUser
{

    @ApiObjectField(name = "access", description = "Acceso", required = false, order = 1)
    private boolean access;
    @ApiObjectField(name = "user", description = "Usuario", required = true, order = 2)
    private AuthorizedUser user;
    @ApiObjectField(name = "role", description = "Rol", required = true, order = 3)
    private Role role;

    public RoleByUser(Integer idRol)
    {
        user = new AuthorizedUser();
        role = new Role();
        role.setId(idRol);
    }

    public RoleByUser()
    {
        user = new AuthorizedUser();
        role = new Role();
    }

    public boolean isAccess()
    {
        return access;
    }

    public void setAccess(boolean access)
    {
        this.access = access;
    }

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser user)
    {
        this.user = user;
    }

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }
}

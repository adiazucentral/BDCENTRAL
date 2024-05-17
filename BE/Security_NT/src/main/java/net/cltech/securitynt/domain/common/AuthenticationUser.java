/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.domain.common;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un usuario que desea autenticarse en la api
 *
 * @version 1.0.0
 * @author dcortes
 * @since 03/04/2017
 * @see Creacion
 */
@ApiObject(
        group = "Seguridad",
        name = "AuthenticationUser",
        description = "Representa un usuario que desea autenticarse en la API"
)
public class AuthenticationUser
{

    @ApiObjectField(name = "user", description = "Campo de Usuario", required = true, order = 1)
    public String user;
    @ApiObjectField(name = "password", description = "Contraseña", required = true, order = 2)
    public String password;
    @ApiObjectField(name = "branch", description = "Sede, Sin o se maneja enviar null o -1", required = true, order = 3)
    public Integer branch;

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Integer getBranch()
    {
        return branch;
    }

    public void setBranch(Integer branch)
    {
        this.branch = branch;
    }
}

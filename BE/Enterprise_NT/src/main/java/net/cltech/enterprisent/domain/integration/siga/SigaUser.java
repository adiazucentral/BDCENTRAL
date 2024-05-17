/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un usuario para el Siga.
 *
 * @version 1.0.0
 * @author equijano
 * @since 16/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Usuario",
        description = "Representa el objeto de usuario del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaUser
{

    @ApiObjectField(name = "id", description = "Id del usuario", order = 1)
    private int id;
    @ApiObjectField(name = "lastName", description = "Apellido del usuario", order = 2)
    private String lastName;
    @ApiObjectField(name = "name", description = "Nombre del usuario", order = 3)
    private String name;
    @ApiObjectField(name = "user", description = "Usuario de autenticacion", order = 4)
    private String user;
    @ApiObjectField(name = "password", description = "Contraseña del usuario", order = 5)
    private String password;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

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

}

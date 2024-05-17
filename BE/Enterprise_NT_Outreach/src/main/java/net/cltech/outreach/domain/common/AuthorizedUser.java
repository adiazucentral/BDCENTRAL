/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.HashMap;
import java.util.Objects;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un usuario autorizado
 *
 * @version 1.0.0
 * @author dcortes
 * @since 31/03/2017
 * @see Creación
 */
@ApiObject(
        group = "Usuarios",
        name = "AuthorizedUser",
        description = "Representa un usuario del sistema",
        show = false
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizedUser
{

    @ApiObjectField(name = "id", description = "Id del usuario", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "identification", description = "Identificación", required = true, order = 2)
    private String identification;
    @ApiObjectField(name = "userName", description = "Usuario", required = true, order = 3)
    private String userName;
    @ApiObjectField(name = "lastName", description = "Apellido del usuario", required = true, order = 4)
    private String lastName;
    @ApiObjectField(name = "name", description = "Nombre del usuario", required = true, order = 5)
    private String name;
    @ApiObjectField(name = "password", description = "Contraseña del usuario", required = true, order = 6)
    private String password;
    @ApiObjectField(name = "type", description = "Tipo de Usuario: 0 -> Administrador, 1 -> Medico, 2 -> Paciente, 3 -> Cliente, 4 -> Usuario", required = true, order = 7)
    private Integer type;
    @ApiObjectField(name = "administrator", description = "Indica si tiene un rol administrados", required = false, order = 8)
    private boolean administrator;
    @ApiObjectField(name = "email", description = "Email", required = true, order = 8)
    private String email;
    @ApiObjectField(name = "licenses", description = "Licencias del producto", required = true, order = 13)
    private HashMap<String, Boolean> licenses;
    @ApiObjectField(name = "changePassword", description = "Cambio de cantraseña", required = true, order = 37)
    private boolean changePassword;
    @ApiObjectField(name = "confidential", description = "Indica si el usuario tiene acceso a los confidenciales", required = true, order = 38)
    private boolean confidential;
    

    public AuthorizedUser()
    {
    }

    public AuthorizedUser(Integer id)
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

    public String getIdentification()
    {
        return identification;
    }

    public void setIdentification(String identification)
    {
        this.identification = identification;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String user)
    {
        this.userName = user;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
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

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public boolean isAdministrator()
    {
        return administrator;
    }

    public void setAdministrator(boolean administrator)
    {
        this.administrator = administrator;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

    public boolean isConfidential() {
        return confidential;
    }

    public void setConfidential(boolean confidential) {
        this.confidential = confidential;
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
        final AuthorizedUser other = (AuthorizedUser) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }
    
    public HashMap<String, Boolean> getLicenses()
    {
        return licenses;
    }

    public void setLicenses(HashMap<String, Boolean> licenses)
    {
        this.licenses = licenses;
    }
}

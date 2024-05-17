package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un usuario autorizado
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 12/06/2020<1
 * @see Creación
 */
@ApiObject(
        group = "Usuarios",
        name = "AuthorizedUser",
        description = "Representa un usuario del sistema",
        show = false
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizedUserHomeBound
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
    @ApiObjectField(name = "administrator", description = "Indica si tiene un rol administrador", required = false, order = 7)
    private boolean administrator;
    @ApiObjectField(name = "administratorHospitable", description = "Indica si tiene un rol administrativo hospitalario", required = false, order = 8)
    private boolean administratorHospitable;
    @ApiObjectField(name = "email", description = "Email", required = true, order = 9)
    private String email;
    @ApiObjectField(name = "photo", description = "Foto del usuario", required = true, order = 10)
    private String photo;

    public AuthorizedUserHomeBound()
    {
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

    public void setUserName(String userName)
    {
        this.userName = userName;
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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isAdministrator()
    {
        return administrator;
    }

    public void setAdministrator(boolean administrator)
    {
        this.administrator = administrator;
    }

    public boolean isAdministratorHospitable()
    {
        return administratorHospitable;
    }

    public void setAdministratorHospitable(boolean administratorHospitable)
    {
        this.administratorHospitable = administratorHospitable;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }

}

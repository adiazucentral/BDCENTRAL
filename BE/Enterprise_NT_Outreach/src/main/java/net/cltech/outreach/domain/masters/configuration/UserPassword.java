package net.cltech.outreach.domain.masters.configuration;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la modificacion de la contraseña del usuario
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 03/02/2020
 * @see Creacion
 */
@ApiObject(
        group = "Usuario",
        name = "Modificacion de contraseña",
        description = "Representa el objeto para realizar la modificacion de contraseña"
)
public class UserPassword
{

    @ApiObjectField(name = "idUser", description = "Id del usuario", required = false, order = 1)
    private int idUser;
    @ApiObjectField(name = "userName", description = "Nickname del usaurio", required = true, order = 2)
    private String userName;
    @ApiObjectField(name = "passwordOld", description = "Contraseña antigua", required = true, order = 3)
    private String passwordOld;
    @ApiObjectField(name = "passwordNew", description = "Contraseña nueva", required = true, order = 4)
    private String passwordNew;
    @ApiObjectField(name = "type", description = "Tipo de usuario", required = false, order = 5)
    public Integer type;

    public int getIdUser()
    {
        return idUser;
    }

    public void setIdUser(int idUser)
    {
        this.idUser = idUser;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPasswordOld()
    {
        return passwordOld;
    }

    public void setPasswordOld(String passwordOld)
    {
        this.passwordOld = passwordOld;
    }

    public String getPasswordNew()
    {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew)
    {
        this.passwordNew = passwordNew;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

}

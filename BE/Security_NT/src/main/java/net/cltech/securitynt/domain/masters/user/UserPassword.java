/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.domain.masters.user;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la modificacion de la contraseña del usuario
 *
 * @version 1.0.0
 * @author equijano
 * @since 01/08/2019
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
    @ApiObjectField(name = "passwordOldSecond", description = "Contraseña antepenultima", required = false, order = 5)
    private String passwordOldSecond;
   

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

    public String getPasswordOldSecond()
    {
        return passwordOldSecond;
    }

    public void setPasswordOldSecond(String passwordOldSecond)
    {
        this.passwordOldSecond = passwordOldSecond;
    }



}

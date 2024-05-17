/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.domain.common;

import java.sql.Timestamp;
import net.cltech.securitynt.domain.masters.user.User;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto visor de sesiones de usuarios
 *
 * @version 1.0.0
 * @author equijano
 * @since 30/11/2018
 * @see Creacion
 */
@ApiObject(
        group = "Seguridad",
        name = "Autenticacion de sesiones",
        description = "Representa un objeto visor de sesiones de usuarios"
)
public class AuthenticationSession
{

    @ApiObjectField(name = "idSession", description = "Id de la sesion", required = true, order = 1)
    public String idSession;
    @ApiObjectField(name = "dateRegister", description = "Fecha de registro", required = false, order = 2)
    public Timestamp dateRegister;
    @ApiObjectField(name = "user", description = "Usuario que registra la sesion", required = false, order = 3)
    public User user;
    @ApiObjectField(name = "ip", description = "Ip del dispositivo conectado", required = false, order = 4)
    public String ip;
    @ApiObjectField(name = "branch", description = "Id de la sede", required = true, order = 5)
    public Integer branch;

    public String getIdSession()
    {
        return idSession;
    }

    public void setIdSession(String idSession)
    {
        this.idSession = idSession;
    }

    public Timestamp getDateRegister()
    {
        return dateRegister;
    }

    public void setDateRegister(Timestamp dateRegister)
    {
        this.dateRegister = dateRegister;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
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

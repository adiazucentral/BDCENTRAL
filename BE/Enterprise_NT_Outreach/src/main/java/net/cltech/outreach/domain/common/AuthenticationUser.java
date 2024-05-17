package net.cltech.outreach.domain.common;

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
    @ApiObjectField(name = "type", description = "Tipo de usuario: 1 -> Medico, 2 -> Paciente, 3 -> Cliente, 4 -> Usuario", required = true, order = 3)
    public Integer type;
    @ApiObjectField(name = "historyType", description = "Tipo de documento", required = true, order = 4)
    public Integer historyType;

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

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getHistoryType()
    {
        return historyType;
    }

    public void setHistoryType(Integer historyType)
    {
        this.historyType = historyType;
    }

    @Override
    public String toString()
    {
        return "AuthenticationUser{" + "user=" + user + ", password=" + password + ", type=" + type + ", historyType=" + historyType + '}';
    }

}

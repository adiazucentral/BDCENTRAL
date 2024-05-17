package net.cltech.enterprisent.domain.common;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un usuario que desea autenticarse en la api de Home Bound
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 13/02/2020
 * @see Creacion
 */
@ApiObject(
        group = "Seguridad",
        name = "UserHomeBound",
        description = "Representa un usuario que desea autenticarse en la API"
)
public class UserHomeBound
{

    @ApiObjectField(name = "user", description = "Campo de Usuario", required = true, order = 1)
    public String user;
    @ApiObjectField(name = "password", description = "Contraseña", required = true, order = 2)
    public String password;
    @ApiObjectField(name = "type", description = "Tipo de usuario: 1 -> Medico, 2 -> Paciente, 3 -> Cliente, 4 -> Usuario", required = true, order = 3)
    public Integer type;

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

}

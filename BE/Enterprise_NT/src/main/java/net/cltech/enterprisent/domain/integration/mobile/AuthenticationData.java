package net.cltech.enterprisent.domain.integration.mobile;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa el usuario que ingresa a la app móvil
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 19/08/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Usuario del paciente",
        description = "Representa un usuario del sistema"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationData
{

    @ApiObjectField(name = "user", description = "Usuario - Paciente - email", required = true, order = 1)
    private String user;
    @ApiObjectField(name = "password", description = "Ultima Contraseña de usuario", required = true, order = 2)
    private String password;

    public AuthenticationData()
    {
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

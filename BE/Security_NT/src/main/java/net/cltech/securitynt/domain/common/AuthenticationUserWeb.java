package net.cltech.securitynt.domain.common;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un usuario que desea autenticarse en la api
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 21/01/2020
 * @see Creacion
 */
@ApiObject(
        group = "Seguridad",
        name = "AuthenticationUserWeb",
        description = "Representa un usuario que desea autenticarse en la API desde Consulta Web"
)
public class AuthenticationUserWeb {

    @ApiObjectField(name = "user", description = "Campo de Usuario", required = true, order = 1)
    public String user;
    @ApiObjectField(name = "password", description = "Contraseña", required = true, order = 2)
    public String password;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

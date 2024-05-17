package net.cltech.enterprisent.domain.integration.mobile;

import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la respuesta de un token de la aplicacion para la app móvil
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 20/08/2020
 * @see Creación
 */
public class JWTTokenPatient
{

    @ApiObjectField(name = "success", description = "Resultado de la peticion. True si hizo login, false de lo contrario", order = 1)
    private boolean success;
    @ApiObjectField(name = "token", description = "Token de seguridad asignado", order = 2)
    private String token;
    @ApiObjectField(name = "user", description = "Autorizacion de usuario Objeto AuthorizedPatient", order = 3)
    private AuthorizedPatient user;

    public JWTTokenPatient()
    {
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public AuthorizedPatient getUser()
    {
        return user;
    }

    public void setUser(AuthorizedPatient user)
    {
        this.user = user;
    }

}

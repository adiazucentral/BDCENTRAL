package net.cltech.securitynt.domain.common;

import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la respuesta de un token de la aplicacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 03/04/2017
 * @see Creación
 */
public class JWTToken
{

    @ApiObjectField(name = "success", description = "Respuesta true -> aceptada false -> no aceptada", order = 1)
    private boolean success;
    @ApiObjectField(name = "token", description = "Cadena de token", order = 2)
    private String token;
    @ApiObjectField(name = "user", description = "Autorizacion de usuario", order = 3)
    private AuthorizedUser user;
    @ApiObjectField(name = "key", description = "Validacion de licencias ", order = 4)
    private boolean key;

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

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser user)
    {
        this.user = user;
    }

    public boolean isKey()
    {
        return key;
    }

    public void setKey(boolean key)
    {
        this.key = key;
    }

}

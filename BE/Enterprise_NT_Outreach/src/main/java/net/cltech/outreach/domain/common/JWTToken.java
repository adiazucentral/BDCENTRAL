package net.cltech.outreach.domain.common;

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

    private boolean success;
    private String token;
    private AuthorizedUser user;
    private boolean changePassword;

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

    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }
    
    
}

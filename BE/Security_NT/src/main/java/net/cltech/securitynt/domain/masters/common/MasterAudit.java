package net.cltech.securitynt.domain.masters.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.securitynt.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un usuario del sistema
 *
 * @version 1.0.0
 * @author eacuna
 * @since 17/04/2017
 * @see Creacion
 */
@ApiObject(
        group = "Master",
        name = "MasterAudit",
        description = "Clase de auditoria de maestros",
        show = false
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MasterAudit
{

    @ApiObjectField(name = "lastTransaction", description = "Fecha de la creación o ultima actualización", required = true, order = 100)
    private Date lastTransaction;
    @ApiObjectField(name = "user", description = "Usuario que realiza que realiza la operación", required = true, order = 101)
    private AuthorizedUser user;

    public MasterAudit()
    {
        user = new AuthorizedUser();
    }

    public Date getLastTransaction()
    {
        return lastTransaction;
    }

    public void setLastTransaction(Date lastTransaction)
    {
        this.lastTransaction = lastTransaction;
    }

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser authorizedUser)
    {
        this.user = authorizedUser;
    }

}

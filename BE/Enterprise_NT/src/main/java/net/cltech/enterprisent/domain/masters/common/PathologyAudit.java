/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.common;

import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObjectField;
import org.jsondoc.core.annotation.ApiObject;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Representa la auditoria para el registro de actividades de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/08/2020
 * @see Creacion
 */
@ApiObject(
        group = "Patología",
        name = "Auditoría",
        description = "Clase de auditoria para el registro de actividades de patología",
        show = false
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PathologyAudit 
{
    @ApiObjectField(name = "createdAt", description = "Fecha de la creación", required = true, order = 100)
    private Date createdAt;
    @ApiObjectField(name = "userCreated", description = "Usuario que registra la operación", required = true, order = 101)
    private AuthorizedUser userCreated;
    @ApiObjectField(name = "updatedAt", description = "Fecha de la ultima modificación", required = true, order = 102)
    private Date updatedAt;
    @ApiObjectField(name = "userUpdated", description = "Usuario que actualiza la operación", required = true, order = 103)
    private AuthorizedUser userUpdated;
    
    public PathologyAudit()
    {
        userCreated = new AuthorizedUser();
        userUpdated = new AuthorizedUser();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public AuthorizedUser getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(AuthorizedUser userCreated) {
        this.userCreated = userCreated;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AuthorizedUser getUserUpdated() {
        return userUpdated;
    }

    public void setUserUpdated(AuthorizedUser userUpdated) {
        this.userUpdated = userUpdated;
    }
    
}

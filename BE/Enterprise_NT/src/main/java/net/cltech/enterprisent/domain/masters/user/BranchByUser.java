/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.user;

import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la combinación entre sede y usuario
 *
 * @version 1.0.0
 * @author cmartin
 * @since 09/05/2017
 * @see Creacion
 */
@ApiObject(
        group = "Usuario",
        name = "Sede por usuario",
        description = "Representa la combinación entre sede y usuario"
)
public class BranchByUser
{

    @ApiObjectField(name = "access", description = "Acceso", required = false, order = 1)
    private boolean access;
    @ApiObjectField(name = "batchPrint", description = "imprimir por lote", required = true, order = 2)
    private boolean batchPrint;
    @ApiObjectField(name = "user", description = "Usuario", required = true, order = 3)
    private AuthorizedUser user;
    @ApiObjectField(name = "branch", description = "Sede", required = true, order = 4)
    private Branch branch;

    public BranchByUser()
    {
        user = new AuthorizedUser();
        branch = new Branch();
    }

    public boolean isAccess()
    {
        return access;
    }

    public void setAccess(boolean access)
    {
        this.access = access;
    }

    public boolean isBatchPrint()
    {
        return batchPrint;
    }

    public void setBatchPrint(boolean batchPrint)
    {
        this.batchPrint = batchPrint;
    }

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser user)
    {
        this.user = user;
    }

    public Branch getBranch()
    {
        return branch;
    }

    public void setBranch(Branch branch)
    {
        this.branch = branch;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.cltech.enterprisent.domain.operation.results;

import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la información para el bloqueo o desbloqueo de una prueba
 * @version 1.0.0
 * @author jblanco
 * @since Jun 10, 2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Bloqueo Examen",
        description = "Representa la información para el bloqueo o desbloqueo de una prueba"
)
public class ResultTestBlock 
{
    @ApiObjectField(name = "order", description = "Número de orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "testId", description = "Identificador del examen", required = true, order = 2)
    private int testId;
    @ApiObjectField(name = "blocked", description = "Indica si la operación es para bloquear (true) o desbloquear (false) la prueba", required = true, order = 3)
    private boolean blocked;
    @ApiObjectField(name = "date", description = "Fecha en la que se realiza el bloqueo de la prueba", required = false, order = 4)
    private Date date;
    @ApiObjectField(name = "reasonId", description = "Identificador del motivo de las repeticiones", required = false, order = 5)
    private int reasonId;
    @ApiObjectField(name = "reasonComment", description = "Justificación de la repetición", required = false, order = 6)
    private String reasonComment;
    @ApiObjectField(name = "user", description = "Usuario que realiza la siembra", required = false, order = 7)
    private AuthorizedUser user = new AuthorizedUser();

    public long getOrder()
    {
        return order;
    }

    public void setOrder(long order)
    {
        this.order = order;
    }

    public int getTestId()
    {
        return testId;
    }

    public void setTestId(int testId)
    {
        this.testId = testId;
    }

    public boolean isBlocked()
    {
        return blocked;
    }

    public void setBlocked(boolean blocked)
    {
        this.blocked = blocked;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public int getReasonId()
    {
        return reasonId;
    }

    public void setReasonId(int reasonId)
    {
        this.reasonId = reasonId;
    }

    public String getReasonComment()
    {
        return reasonComment;
    }

    public void setReasonComment(String reasonComment)
    {
        this.reasonComment = reasonComment;
    }

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser user)
    {
        this.user = user;
    }
    
}


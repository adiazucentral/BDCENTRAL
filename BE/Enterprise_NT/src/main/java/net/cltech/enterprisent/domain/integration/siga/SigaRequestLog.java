/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una solicitud para el Siga.
 *
 * @version 1.0.0
 * @author equijano
 * @since 16/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Solicitud",
        description = "Representa el objeto de solicitud del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaRequestLog
{

    @ApiObjectField(name = "action", description = "Valor de la accion", order = 1)
    private int action;
    @ApiObjectField(name = "branch", description = "Objeto de la sede", order = 2)
    private SigaBranch branch;
    @ApiObjectField(name = "point", description = "Objeto que representa a la taquilla", order = 3)
    private SigaPointOfCare point;
    @ApiObjectField(name = "user", description = "Objeto que representa al usuario", order = 4)
    private SigaUser user;
    @ApiObjectField(name = "reason", description = "Objeto que representa el motivo", order = 5)
    private SigaReason reason;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public SigaBranch getBranch() {
        return branch;
    }

    public void setBranch(SigaBranch branch) {
        this.branch = branch;
    }

    public SigaPointOfCare getPoint() {
        return point;
    }

    public void setPoint(SigaPointOfCare point) {
        this.point = point;
    }

    public SigaUser getUser() {
        return user;
    }

    public void setUser(SigaUser user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "SigaRequestLog{" + "action=" + action + ", branch=" + branch + ", point=" + point + ", user=" + user + '}';
    }

    public SigaReason getReason() {
        return reason;
    }

    public void setReason(SigaReason reason) {
        this.reason = reason;
    }

}

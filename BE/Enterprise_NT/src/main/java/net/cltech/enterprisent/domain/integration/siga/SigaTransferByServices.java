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
 * Representa una transferecnia por servicio para el Siga.
 *
 * @version 1.0.00
 * @author equijano
 * @since 26/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Transferencia por servicio",
        description = "Representa el objeto de transferencia por servicio del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaTransferByServices {

    @ApiObjectField(name = "id", description = "Id de la transferencia por el servicio", order = 1)
    private int id;
    @ApiObjectField(name = "branch", description = "Sede de la transferencia", order = 2)
    private SigaBranch branch;
    @ApiObjectField(name = "serviceOri", description = "Servicio de origen de la transferencia", order = 3)
    private SigaService serviceOri;
    @ApiObjectField(name = "serviceDes", description = "Servicio de destino de la transferencia", order = 4)
    private SigaService serviceDes;
    @ApiObjectField(name = "waitTime", description = "Tiempo de espera de la transferencia", order = 5)
    private int waitTime;
    @ApiObjectField(name = "pendingTurns", description = "Turnos pendiente de la transferencia", order = 6)
    private int pendingTurns;
    @ApiObjectField(name = "attentionState", description = "Estado de la atencion de la transferencia", order = 7)
    private int attentionState;
    @ApiObjectField(name = "enabled", description = "Estado habilitado o no de la transferencia", order = 8)
    private boolean enabled;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SigaBranch getBranch() {
        return branch;
    }

    public void setBranch(SigaBranch branch) {
        this.branch = branch;
    }

    public SigaService getServiceOri() {
        return serviceOri;
    }

    public void setServiceOri(SigaService serviceOri) {
        this.serviceOri = serviceOri;
    }

    public SigaService getServiceDes() {
        return serviceDes;
    }

    public void setServiceDes(SigaService serviceDes) {
        this.serviceDes = serviceDes;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getPendingTurns() {
        return pendingTurns;
    }

    public void setPendingTurns(int pendingTurns) {
        this.pendingTurns = pendingTurns;
    }

    public int getAttentionState() {
        return attentionState;
    }

    public void setAttentionState(int attentionState) {
        this.attentionState = attentionState;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}

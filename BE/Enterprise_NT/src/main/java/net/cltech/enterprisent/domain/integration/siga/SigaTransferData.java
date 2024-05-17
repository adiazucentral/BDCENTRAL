/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
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
        group = "SigaTransferData",
        name = "Informacion de la transferencia",
        description = "Representa el objeto de informacion de la transferencia del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaTransferData {

    @ApiObjectField(name = "idTurn", description = "Id de la transferencia", order = 1)
    private Integer idTurn;
    @ApiObjectField(name = "user", description = "Usuario de la transferencia", order = 2)
    private SigaUser user;
    @ApiObjectField(name = "turnsMovement", description = "Movimiento del turno en transferencia", order = 3)
    private List<SigaTurnMovement> turnsMovement;
    @ApiObjectField(name = "pendingServices", description = "Servicios pendientes de la transferencia", order = 4)
    private List<SigaService> pendingServices;

    public Integer getIdTurn() {
        return idTurn;
    }

    public void setIdTurn(Integer idTurn) {
        this.idTurn = idTurn;
    }

    public SigaUser getUser() {
        return user;
    }

    public void setUser(SigaUser user) {
        this.user = user;
    }

    public List<SigaTurnMovement> getTurnsMovement() {
        return turnsMovement;
    }

    public void setTurnsMovement(List<SigaTurnMovement> turnsMovement) {
        this.turnsMovement = turnsMovement;
    }

    public List<SigaService> getPendingServices() {
        return pendingServices;
    }

    public void setPendingServices(List<SigaService> pendingServices) {
        this.pendingServices = pendingServices;
    }

}

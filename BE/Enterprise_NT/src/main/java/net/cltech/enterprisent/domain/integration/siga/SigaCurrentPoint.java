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
 * Representa la taquilla actual para el Siga.
 *
 * @version 1.0.0
 * @author equijano
 * @since 18/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Turno actual",
        description = "Representa el objeto del turno actual del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaCurrentPoint {

    @ApiObjectField(name = "branch", description = "Sede del turno actual", order = 1)
    private SigaBranch branch;
    @ApiObjectField(name = "service", description = "Servicio del turno actual", order = 2)
    private SigaService service;
    @ApiObjectField(name = "point", description = "Taquilla del turno actual", order = 3)
    private SigaPointOfCare point;
    @ApiObjectField(name = "user", description = "Usuario del turno actual", order = 4)
    private SigaUser user;

    public SigaBranch getBranch() {
        return branch;
    }

    public void setBranch(SigaBranch branch) {
        this.branch = branch;
    }

    public SigaService getService() {
        return service;
    }

    public void setService(SigaService service) {
        this.service = service;
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

}

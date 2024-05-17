/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el turno para el Siga.
 *
 * @version 1.0.0
 * @author equijano
 * @since 19/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Turno",
        description = "Representa el objeto de turno del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaTurnGrid {

    @ApiObjectField(name = "id", description = "Id del turno", order = 1)
    private Integer id;
    @ApiObjectField(name = "number", description = "Numero del turno", order = 2)
    private String number;
    @ApiObjectField(name = "priority", description = "Prioridad del turno", order = 3)
    private Integer priority;
    @ApiObjectField(name = "date", description = "Fecha del turno", order = 4)
    private Timestamp date;
    @ApiObjectField(name = "standbyTime", description = "Tiempo de espera del turno", order = 5)
    private Integer standbyTime;
    @ApiObjectField(name = "turnType", description = "Tipo del turno", order = 6)
    private SigaTurnType turnType;
    @ApiObjectField(name = "patient", description = "Paciente para el turno", order = 7)
    private SigaPatient patient;
    @ApiObjectField(name = "service", description = "Servicio del turno", order = 8)
    private SigaService service;
    @ApiObjectField(name = "branch", description = "Sede del turno", order = 9)
    private SigaBranch branch;
    @ApiObjectField(name = "state", description = "Estado del turno", order = 10)
    private int state;
    @ApiObjectField(name = "attended", description = "Asistio al turno", order = 11)
    private boolean attended;
    @ApiObjectField(name = "transferible", description = "Transferencia del turno", order = 12)
    private boolean transferible;
    @ApiObjectField(name = "finalizable", description = "Finalizacion del turno", order = 13)
    private boolean finalizable;
    @ApiObjectField(name = "rate", description = "Tarifa del turno", order = 14)
    private boolean rate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Integer getStandbyTime() {
        return standbyTime;
    }

    public void setStandbyTime(Integer standbyTime) {
        this.standbyTime = standbyTime;
    }

    public SigaTurnType getTurnType() {
        return turnType;
    }

    public void setTurnType(SigaTurnType turnType) {
        this.turnType = turnType;
    }

    public SigaPatient getPatient() {
        return patient;
    }

    public void setPatient(SigaPatient patient) {
        this.patient = patient;
    }

    public SigaService getService() {
        return service;
    }

    public void setService(SigaService service) {
        this.service = service;
    }

    public SigaBranch getBranch() {
        return branch;
    }

    public void setBranch(SigaBranch branch) {
        this.branch = branch;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public boolean isTransferible() {
        return transferible;
    }

    public void setTransferible(boolean transferible) {
        this.transferible = transferible;
    }

    public boolean isFinalizable() {
        return finalizable;
    }

    public void setFinalizable(boolean finalizable) {
        this.finalizable = finalizable;
    }

    public boolean isRate() {
        return rate;
    }

    public void setRate(boolean rate) {
        this.rate = rate;
    }
    
}

package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un turno para el Siga.
 *
 * @version 1.0.0
 * @author equijano
 * @since 22/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Turno",
        description = "Representa el objeto del turno del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaTurn {

    @ApiObjectField(name = "id", description = "Id del turno", order = 1)
    private Integer id;
    @ApiObjectField(name = "branch", description = "Sede del turno", order = 2)
    private SigaBranch branch;
    @ApiObjectField(name = "company", description = "Compañia del turno", order = 3)
    private SigaCompany company;
    @ApiObjectField(name = "turnType", description = "Tipo del turno", order = 4)
    private SigaTurnType turnType;
    @ApiObjectField(name = "patient", description = "Paciente del turno", order = 5)
    private SigaPatient patient;
    @ApiObjectField(name = "number", description = "Numero del turno", order = 6)
    private Integer number;
    @ApiObjectField(name = "date", description = "Fecha del turno", order = 7)
    private Date date;
    @ApiObjectField(name = "dateYYYYMMDD", description = "Fecha con formato del turno", order = 8)
    private int dateYYYYMMDD;
    @ApiObjectField(name = "hourHHMM", description = "Hora del turno", order = 9)
    private int hourHHMM;
    @ApiObjectField(name = "movements", description = "Movimientos del turno", order = 10)
    private List<SigaTurnMovement> movements;
    @ApiObjectField(name = "client", description = "Cliente del turno", order = 11)
    private String client;
    @ApiObjectField(name = "state", description = "Estado del turno que esta atendiendo. 2 - Llamando el turno, 3 - Atendiendo el turno")
    private Integer state;
    @ApiObjectField(name = "attended", description = "A sido Atendido")
    private boolean attended;
    @ApiObjectField(name = "transferible", description = "Es transferible")
    private boolean transferible;
    @ApiObjectField(name = "finalizable", description = "Es finalizable")
    private boolean finalizable;
    @ApiObjectField(name = "point", description = "Taquilla desde donde realizo accion")
    private SigaPointOfCare point;
        
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SigaBranch getBranch() {
        return branch;
    }

    public void setBranch(SigaBranch branch) {
        this.branch = branch;
    }

    public SigaCompany getCompany() {
        return company;
    }

    public void setCompany(SigaCompany company) {
        this.company = company;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDateYYYYMMDD() {
        return dateYYYYMMDD;
    }

    public void setDateYYYYMMDD(int dateYYYYMMDD) {
        this.dateYYYYMMDD = dateYYYYMMDD;
    }

    public int getHourHHMM() {
        return hourHHMM;
    }

    public void setHourHHMM(int hourHHMM) {
        this.hourHHMM = hourHHMM;
    }

    public List<SigaTurnMovement> getMovements() {
        return movements;
    }

    public void setMovements(List<SigaTurnMovement> movements) {
        this.movements = movements;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public boolean isAttended()
    {
        return attended;
    }

    public void setAttended(boolean attended)
    {
        this.attended = attended;
    }

    public boolean isTransferible()
    {
        return transferible;
    }

    public void setTransferible(boolean transferible)
    {
        this.transferible = transferible;
    }

    public boolean isFinalizable()
    {
        return finalizable;
    }

    public void setFinalizable(boolean finalizable)
    {
        this.finalizable = finalizable;
    }

    public SigaPointOfCare getPoint()
    {
        return point;
    }

    public void setPoint(SigaPointOfCare point)
    {
        this.point = point;
    }
}

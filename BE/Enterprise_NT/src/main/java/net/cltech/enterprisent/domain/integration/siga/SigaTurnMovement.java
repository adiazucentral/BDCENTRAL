/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un movimiento del turno para el Siga.
 *
 * @version 1.0.0
 * @author equijano
 * @since 22/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Movimiento del turno",
        description = "Representa el objeto de movimientos de turno del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaTurnMovement
{

    @ApiObjectField(name = "id", description = "Id del movimiento del turno", order = 1)
    private Integer id;
    @ApiObjectField(name = "turn", description = "Turno al que pertenece", order = 2)
    private SigaTurn turn;
    @ApiObjectField(name = "service", description = "Servicio del movimiento del turno", order = 3)
    private SigaService service;
    @ApiObjectField(name = "pointOfCare", description = "Taquilla del movimiento del turno", order = 4)
    private SigaPointOfCare pointOfCare;
    @ApiObjectField(name = "date", description = "Fecha de movimiento del turno", order = 5)
    private Date date;
    @ApiObjectField(name = "state", description = "Estado del movimiento del turno 1-Pendiente, 2-Llamado, 3-InicioAtencion, 4-Cancelado, 5-Aplazado, 6-Terminado, 7-Reservado", order = 6)
    private int state;
    @ApiObjectField(name = "active", description = "Verifica si esta activo el movimiento del turno", order = 7)
    private int active;
    @ApiObjectField(name = "user", description = "Usuario del movimiento del turno", order = 8)
    private SigaUser user;
    @ApiObjectField(name = "transfer", description = "Transferencia del movimiento del turno", order = 9)
    private boolean transfer;
    @ApiObjectField(name = "minutes", description = "Tiempo en minutos del movimiento del turno", order = 10)
    private int minutes;
    @ApiObjectField(name = "callDate", description = "Fecha de llamada del movimiento del turno", order = 11)
    private Date callDate;
    @ApiObjectField(name = "rating", description = "Puntuacion del movimiento del turno", order = 12)
    private Integer rating;
    @ApiObjectField(name = "reason", description = "Motivo del movimiento del turno", order = 13)
    private SigaReason reason;
    @ApiObjectField(name = "order", description = "Orden asociada al turno", order = 14)
    private Long order;

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public SigaTurn getTurn()
    {
        return turn;
    }

    public void setTurn(SigaTurn turn)
    {
        this.turn = turn;
    }

    public SigaService getService()
    {
        return service;
    }

    public void setService(SigaService service)
    {
        this.service = service;
    }

    public SigaPointOfCare getPointOfCare()
    {
        return pointOfCare;
    }

    public void setPointOfCare(SigaPointOfCare pointOfCare)
    {
        this.pointOfCare = pointOfCare;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public int getActive()
    {
        return active;
    }

    public void setActive(int active)
    {
        this.active = active;
    }

    public SigaUser getUser()
    {
        return user;
    }

    public void setUser(SigaUser user)
    {
        this.user = user;
    }

    public boolean isTransfer()
    {
        return transfer;
    }

    public void setTransfer(boolean transfer)
    {
        this.transfer = transfer;
    }

    public int getMinutes()
    {
        return minutes;
    }

    public void setMinutes(int minutes)
    {
        this.minutes = minutes;
    }

    public Date getCallDate()
    {
        return callDate;
    }

    public void setCallDate(Date callDate)
    {
        this.callDate = callDate;
    }

    public Integer getRating()
    {
        return rating;
    }

    public void setRating(Integer rating)
    {
        this.rating = rating;
    }

    public SigaReason getReason()
    {
        return reason;
    }

    public void setReason(SigaReason reason)
    {
        this.reason = reason;
    }

}

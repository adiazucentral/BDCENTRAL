/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.widgets;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import net.cltech.enterprisent.domain.masters.user.User;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion del widget de ingreso de ordenes
 *
 * @version 1.0.0
 * @author equijano
 * @since 22/07/2019
 * @see Creación
 */
@ApiObject(
        group = "Widgets",
        name = "Widgets - Ingreso de ordenes",
        description = "Representa la información del widget de ingreso de ordenes"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WidgetOrderEntry
{

    @ApiObjectField(name = "numberOrders", description = "Cantidad de ordenes por sede y usuario", required = true, order = 1)
    private long numberOrders;
    @ApiObjectField(name = "branch", description = "Sede", required = true, order = 2)
    private int branch;
    @ApiObjectField(name = "turns", description = "Turnos por sede", required = true, order = 3)
    private int turns;
    @ApiObjectField(name = "shiftsWorked", description = "Turnos atendidos por el usuario", required = true, order = 4)
    private int shiftsWorked;
    @ApiObjectField(name = "waitingTurns", description = "Turnos en espera por sede", required = true, order = 5)
    private int waitingTurns;
    @ApiObjectField(name = "qualifications", description = "Calificacion de servicio por sede", required = true, order = 6)
    private List<TurnsRatingWidgetInfo> qualifications;
    @ApiObjectField(name = "user", description = "Usuario de la sesion", required = true, order = 7)
    private User user;
    @ApiObjectField(name = "service", description = "Servicio", required = true, order = 8)
    private int service;
    @ApiObjectField(name = "numberOrdersByBranch", description = "Cantidad de ordenes por sede", required = true, order = 9)
    private long numberOrdersByBranch;

    public long getNumberOrders()
    {
        return numberOrders;
    }

    public void setNumberOrders(long numberOrders)
    {
        this.numberOrders = numberOrders;
    }

    public int getBranch()
    {
        return branch;
    }

    public void setBranch(int branch)
    {
        this.branch = branch;
    }

    public int getTurns()
    {
        return turns;
    }

    public void setTurns(int turns)
    {
        this.turns = turns;
    }

    public int getShiftsWorked()
    {
        return shiftsWorked;
    }

    public void setShiftsWorked(int shiftsWorked)
    {
        this.shiftsWorked = shiftsWorked;
    }

    public int getWaitingTurns()
    {
        return waitingTurns;
    }

    public void setWaitingTurns(int waitingTurns)
    {
        this.waitingTurns = waitingTurns;
    }

    public List<TurnsRatingWidgetInfo> getQualifications()
    {
        return qualifications;
    }

    public void setQualifications(List<TurnsRatingWidgetInfo> qualifications)
    {
        this.qualifications = qualifications;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public int getService()
    {
        return service;
    }

    public void setService(int service)
    {
        this.service = service;
    }

    public long getNumberOrdersByBranch()
    {
        return numberOrdersByBranch;
    }

    public void setNumberOrdersByBranch(long numberOrdersByBranch)
    {
        this.numberOrdersByBranch = numberOrdersByBranch;
    }

}

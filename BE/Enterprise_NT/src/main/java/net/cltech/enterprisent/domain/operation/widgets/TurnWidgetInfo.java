/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.widgets;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un objeto con la información de atención de turnos para widget del
 * LIS
 *
 * @version 1.0.0
 * @author equijano
 * @since 23/07/2019
 * @see Creación
 */
@ApiObject(
        group = "Widgets",
        name = "Widgets - Informacion turnos",
        description = "Representa la informacion activa del widget"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TurnWidgetInfo
{

    @ApiObjectField(name = "totalTurns", description = "Total turnos de la sede", required = false, order = 1)
    private int totalTurns;
    @ApiObjectField(name = "totalTurnsByUser", description = "Total turnos atendidos por un usuario de la sede", required = false, order = 2)
    private int totalTurnsByUser;
    @ApiObjectField(name = "totalWaitingTurns", description = "Total turnos en espera de ser atendidos de la sede", required = false, order = 3)
    private int totalWaitingTurns;

    public int getTotalTurns()
    {
        return totalTurns;
    }

    public void setTotalTurns(int totalTurns)
    {
        this.totalTurns = totalTurns;
    }

    public int getTotalTurnsByUser()
    {
        return totalTurnsByUser;
    }

    public void setTotalTurnsByUser(int totalTurnsByUser)
    {
        this.totalTurnsByUser = totalTurnsByUser;
    }

    public int getTotalWaitingTurns()
    {
        return totalWaitingTurns;
    }

    public void setTotalWaitingTurns(int totalWaitingTurns)
    {
        this.totalWaitingTurns = totalWaitingTurns;
    }

}

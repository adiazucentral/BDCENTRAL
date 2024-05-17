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
 * Representa una sede para el Siga.
 *
 * @version 1.0.0
 * @author equijano
 * @since 18/10/2018
 * @see Creación
 */
@ApiObject(
        group = "SigaTurnCall",
        name = "Turno llamado",
        description = "Representa el objeto de turno llamado del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaTurnCall {

    @ApiObjectField(name = "idTurn", description = "Id del turno", order = 1)
    private int idTurn;
    @ApiObjectField(name = "point", description = "Taquilla actual para el turno", order = 2)
    private SigaCurrentPoint point;

    public int getIdTurn() {
        return idTurn;
    }

    public void setIdTurn(int idTurn) {
        this.idTurn = idTurn;
    }

    public SigaCurrentPoint getPoint() {
        return point;
    }

    public void setPoint(SigaCurrentPoint point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "SigaTurnCall{" + "idTurn=" + idTurn + ", point=" + point + '}';
    }

}

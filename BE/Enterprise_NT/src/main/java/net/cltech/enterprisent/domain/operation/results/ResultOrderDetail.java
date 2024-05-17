/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.cltech.enterprisent.domain.operation.results;

import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el detalle de una orden para el registro de resultados
 * @version 1.0.0
 * @author jblanco
 * @since Jan 22, 2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Orden Detalle Resultados",
        description = "Representa el detalle de una orden para el módulo de registro de resultados"
)
public class ResultOrderDetail {
    
    @ApiObjectField(name = "order", description = "Número de orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "comment", description = "Comentario de la orden", required = false, order = 2)
    private String comment;
    @ApiObjectField(name = "diagnostic", description = "Diagnóstico permanente del paciente", required = false, order = 3)
    private String diagnostic;
    @ApiObjectField(name = "userId", description = "Identificador del usuario que registra el comentario de la orden", required = false, order = 4)
    private int userId;
    @ApiObjectField(name = "commentDate", description = "Fecha del comentario de la orden", required = false, order = 5)
    private Date commentDate;

    public ResultOrderDetail() {
    }

    public long getOrder() {
        return order;
    }
 
    public void setOrder(long order) {
        this.order = order;
    }

    public String getComment() {
        return comment;
    }
 
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDiagnostic() {
        return diagnostic;
    }
 
    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public int getUserId() {
        return userId;
    }
 
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public Date getCommentDate()
    {
        return commentDate;
    }

    public void setCommentDate(Date commentDate)
    {
        this.commentDate = commentDate;
    }
}
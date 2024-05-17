/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.domain.operation;

import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el comentario de un examen
 *
 * @version 1.0.0
 * @author jblanco
 * @since Nov 6, 2017
 * @see [Para cuando se crea una clase incluir la palabra Creación, en caso de
 * que sea una modificación colocar los cambios. Se pueden usar varias veces
 * esta etiqueta]
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Comentario Resultados Examen",
        description = "Representa el comentario de un examen para el registro de resultados"
)
public class ResultTestComment
{

    @ApiObjectField(name = "order", description = "Número de orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "testId", description = "Identificador del examen", required = true, order = 2)
    private int testId;
    @ApiObjectField(name = "comment", description = "Resultado del examen", required = false, order = 3)
    private String comment;
    @ApiObjectField(name = "commentDate", description = "Fecha del comentario del resultado", required = true, order = 4)
    private Date commentDate;
    @ApiObjectField(name = "pathology", description = "Patología del comentario del examen", required = true, order = 5)
    private int pathology;
    @ApiObjectField(name = "userId", description = "Usuario que realiza la acción", required = true, order = 6)
    private int userId;
    @ApiObjectField(name = "commentChanged", description = "Indicador de cambio del comentario", required = true, order = 7)
    private boolean commentChanged;
    @ApiObjectField(name = "result", description = "Resultado del examen", required = true, order = 8)
    private String result;

    public ResultTestComment()
    {
    }

    public long getOrder()
    {
        return order;
    }

    public void setOrder(long order)
    {
        this.order = order;
    }

    public int getTestId()
    {
        return testId;
    }

    public void setTestId(int testId)
    {
        this.testId = testId;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Date getCommentDate()
    {
        return commentDate;
    }

    public void setCommentDate(Date commentDate)
    {
        this.commentDate = commentDate;
    }

    public int getPathology()
    {
        return pathology;
    }

    public void setPathology(int pathology)
    {
        this.pathology = pathology;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public boolean getCommentChanged()
    {
        return commentChanged;
    }

    public void setCommentChanged(boolean commentChange)
    {
        this.commentChanged = commentChange;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}

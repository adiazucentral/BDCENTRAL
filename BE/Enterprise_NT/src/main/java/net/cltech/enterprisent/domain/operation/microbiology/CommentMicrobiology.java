/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.microbiology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la Detección Microbial de un examen
 *
 * @version 1.0.0
 * @author cmartin
 * @since 28/02/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Microbiologia",
        name = "Microbiologia - Comentario",
        description = "Representa un comentario de microbiologia de la aplicación"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentMicrobiology
{

    @ApiObjectField(name = "id", description = "Id del Comentario", order = 1)
    private Integer id;
    @ApiObjectField(name = "order", description = "Orden", order = 3)
    private Long order;
    @ApiObjectField(name = "idTest", description = "Id de la muestra", order = 3)
    private Integer idTest;
    @ApiObjectField(name = "idSample", description = "Id de la muestra", order = 3)
    private Integer idSample;
    @ApiObjectField(name = "comment", description = "Comentario", order = 4)
    private String comment;
    @ApiObjectField(name = "lastTransaction", description = "Fecha de creación o ultima actualización", required = true, order = 5)
    private Date lastTransaction;
    @ApiObjectField(name = "user", description = "Usuario que realiza la operación", required = true, order = 6)
    private AuthorizedUser user = new AuthorizedUser();
    @ApiObjectField(name = "state", description = "Estado: 0 -> No aplica, 1 -> Ingresar, 2 -> Modificar, 3 -> Eliminar", required = true, order = 7)
    private Short state;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public Integer getIdTest()
    {
        return idTest;
    }

    public void setIdTest(Integer idTest)
    {
        this.idTest = idTest;
    }

    public Integer getIdSample()
    {
        return idSample;
    }

    public void setIdSample(Integer idSample)
    {
        this.idSample = idSample;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Date getLastTransaction()
    {
        return lastTransaction;
    }

    public void setLastTransaction(Date lastTransaction)
    {
        this.lastTransaction = lastTransaction;
    }

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser user)
    {
        this.user = user;
    }

    public Short getState()
    {
        return state;
    }

    public void setState(Short state)
    {
        this.state = state;
    }

}

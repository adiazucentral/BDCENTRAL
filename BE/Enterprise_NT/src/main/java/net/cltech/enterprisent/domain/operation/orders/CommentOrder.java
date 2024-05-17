/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.Objects;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para busquedas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 28/02/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Comentario Orden",
        description = "Representa un comentario de orden, microbiologia, examen o un diagnostico permanente del paciente."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentOrder
{

    @ApiObjectField(name = "id", description = "Id del Comentario", order = 1)
    private Integer id;
    @ApiObjectField(name = "type", description = "Tipo: 1 -> Orden<br>2 -> Paciente", order = 2)
    private Short type;
    @ApiObjectField(name = "idRecord", description = "Numero de la Orden o Id del paciente", order = 3)
    private Long idRecord;
    @ApiObjectField(name = "comment", description = "Comentario", order = 4)
    private String comment;
    @ApiObjectField(name = "lastTransaction", description = "Fecha de la creación o ultima actualización", required = true, order = 5)
    private Date lastTransaction;
    @ApiObjectField(name = "git ", description = "Usuario que realiza que realiza la operación", required = true, order = 6)
    private AuthorizedUser user = new AuthorizedUser();
    @ApiObjectField(name = "state", description = "Estado: 0 -> No aplica, 1 -> Ingresar, 2 -> Modificar, 3 -> Eliminar", required = true, order = 7)
    private Short state;
    @ApiObjectField(name = "print", description = "Indica si el comentario se imprime", required = true, order = 8)
    private boolean print;


    public CommentOrder(AuthorizedUser user)
    {

        this.user = user;
        this.print = false;

    }

    public CommentOrder()
    {

    }

    public CommentOrder(String comment)
    {
        this.comment = comment;
    }

    public CommentOrder(Short type, String comment)
    {
        this.type = type;
        this.comment = comment;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Short getType()
    {
        return type;
    }

    public void setType(Short type)
    {
        this.type = type;
    }

    public Long getIdRecord()
    {
        return idRecord;
    }

    public void setIdRecord(Long idRecord)
    {
        this.idRecord = idRecord;
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

    public boolean isPrint()
    {
        return print;
    }

    public void setPrint(boolean print)
    {
        this.print = print;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final CommentOrder other = (CommentOrder) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}

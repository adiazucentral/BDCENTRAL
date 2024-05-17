/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.enterprisent.domain.masters.user.User;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Vigencia de un examen para un paciente
 *
 * @version 1.0.0
 * @author dcortes
 * @since 5/03/2018
 * @see Creacion
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Vigencia de resultado",
        description = "Representa la vigencia del resultado de la prueba de un paciente"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestValidity
{

    @ApiObjectField(name = "id", description = "Id Prueba", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "dateLastResult", description = "Fecha Ultimo Resultado", required = true, order = 2)
    private Date dateLastResult;
    @ApiObjectField(name = "daysFromLastResult", description = "Dias desde el ultimo resultado", required = true, order = 3)
    private long daysFromLastResult;
    @ApiObjectField(name = "userLastResult", description = "Usuario del ultimo resultado", required = true, order = 4)
    private User userLastResult;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Date getDateLastResult()
    {
        return dateLastResult;
    }

    public void setDateLastResult(Date dateLastResult)
    {
        this.dateLastResult = dateLastResult;
    }

    public long getDaysFromLastResult()
    {
        return daysFromLastResult;
    }

    public void setDaysFromLastResult(long daysFromLastResult)
    {
        this.daysFromLastResult = daysFromLastResult;
    }

    public User getUserLastResult()
    {
        return userLastResult;
    }

    public void setUserLastResult(User userLastResult)
    {
        this.userLastResult = userLastResult;
    }
}

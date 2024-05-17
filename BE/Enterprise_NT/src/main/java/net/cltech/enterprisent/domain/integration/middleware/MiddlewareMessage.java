/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.middleware;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un mensaje a enviar al Middleware.
 *
 * @version 1.0.0
 * @author equijano
 * @since 01/11/2018
 * @see Creación
 */
@ApiObject(
        group = "Middleware",
        name = "Mensaje",
        description = "Representa el objeto de un mensaje del sistema Middleware"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MiddlewareMessage
{

    @ApiObjectField(name = "message", description = "Mensaje a enviar al middleware", order = 1)
    private String message;
    @ApiObjectField(name = "date", description = "Fecha del mensaje al middleware", order = 2)
    private String date;

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }
}

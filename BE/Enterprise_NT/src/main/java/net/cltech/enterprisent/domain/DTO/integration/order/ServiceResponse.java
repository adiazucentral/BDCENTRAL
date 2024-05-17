/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;

/**
 * Respuesta a la invocacion de servicios
 *
 * @author oarango
 * @since 2022-04-14
 * @see Creacion
 */
@ApiObject(
        name = "Respuesta de Servicio",
        group = "Común",
        description = "Respuesta a invocación de servicios"
)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse
{

    private long order;
    private String message;

    public ServiceResponse()
    {
    }

    public ServiceResponse(long order)
    {
        this.order = order;
    }

    public ServiceResponse(long order, String message)
    {
        this.order = order;
        this.message = message;
    }
}

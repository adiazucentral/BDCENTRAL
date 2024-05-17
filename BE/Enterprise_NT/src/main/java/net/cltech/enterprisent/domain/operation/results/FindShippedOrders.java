package net.cltech.enterprisent.domain.operation.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Agregar una descripción de la clase
 * @version 1.0.0
 * @author nmolina
 * @since 4/08/2020
 * @see Creacion
 */

@ApiObject(
        group = "Operación - Resultados",
        name = "Orden enviadas a un sistema central externo",
        description = "Representa la orden enviada a un sistema central externo"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FindShippedOrders {
    @ApiObjectField(name = "nameCentralSystem", description = "Nombre del sistema central externo", required = true, order = 1)
    private String nameCentralsystem;
    
    @ApiObjectField(name = "dispatchedDate", description = "Fecha en que fue enviada al sistema central", required = false, order = 1)
    private Long dispatchedDate;

    public FindShippedOrders()
    {
    }

    public String getNameCentralsystem()
    {
        return nameCentralsystem;
    }

    public void setNameCentralsystem(String nameCentralsystem)
    {
        this.nameCentralsystem = nameCentralsystem;
    }

    public Long getDispatchedDate()
    {
        return dispatchedDate;
    }

    public void setDispatchedDate(Long dispatchedDate)
    {
        this.dispatchedDate = dispatchedDate;
    }
}

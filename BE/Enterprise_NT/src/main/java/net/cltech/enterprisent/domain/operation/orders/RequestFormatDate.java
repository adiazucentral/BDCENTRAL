package net.cltech.enterprisent.domain.operation.orders;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Esta clase representa la petición a un formato de fecha especifico
 * debido a que la fecha no se pueden enviar como parametro por un get 
 * por este tipo de fechas -> dd/MM/yyyy en donde las barras alteran la URI
 * 
 * @version 1.0.0
 * @author Julian
 * @since 15/04/2020
 * @see Creación
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Formato de fechas",
        description = "Representa un formato de fechas"
)
public class RequestFormatDate
{
    @ApiObjectField(name = "idOrder", description = "Id de la orden", required = false, order = 1)
    private long idOrder;
    @ApiObjectField(name = "formatDate", description = "Formato de fecha", required = true, order = 2)
    private String formatDate;

    public RequestFormatDate()
    {
    }

    public RequestFormatDate(long idOrder, String formatDate)
    {
        this.idOrder = idOrder;
        this.formatDate = formatDate;
    }

    public long getIdOrder()
    {
        return idOrder;
    }

    public void setIdOrder(long idOrder)
    {
        this.idOrder = idOrder;
    }

    public String getFormatDate()
    {
        return formatDate;
    }

    public void setFormatDate(String formatDate)
    {
        this.formatDate = formatDate;
    }
}

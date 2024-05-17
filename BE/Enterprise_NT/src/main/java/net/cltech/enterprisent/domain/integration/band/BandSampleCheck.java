package net.cltech.enterprisent.domain.integration.band;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto de petición con el que se realizará
 * la verificación de una muestra desde Banda hasta NT
 * 
 * @version 1.0.0
 * @author Julian
 * @since 22/05/2020
 * @see Creación
 */
@ApiObject(
        name = "Verificación de muestra desde banda",
        group = "Banda Transportadora",
        description = "Objeto de petición con el que se realiza la verificación de una muestra en NT"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BandSampleCheck
{
    @ApiObjectField(name = "orderNumber", description = "Orden de NT", required = true, order = 1)
    private String orderNumber;
    @ApiObjectField(name = "destinationId", description = "Id Destino de NT", required = true, order = 2)
    private Integer destinationId;

    public BandSampleCheck()
    {
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Integer destinationId) {
        this.destinationId = destinationId;
    }

}
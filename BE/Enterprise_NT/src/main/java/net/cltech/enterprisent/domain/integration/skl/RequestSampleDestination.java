package net.cltech.enterprisent.domain.integration.skl;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa el objeto de peticion a getSampleDestinations
 * 
 * @version 1.0.0
 * @author Julian
 * @since 03/04/2020
 * @see Creación
 */
@ApiObject(
        group = "Trazabilidad",
        name = "Destino de la muestra",
        description = "Objeto de petición para la trazabilidad de la muestra en la integración con SKL"
)
public class RequestSampleDestination
{
    @ApiObjectField(name = "state", description = "Estado del destino", required = true, order = 1)
    private int state;
    @ApiObjectField(name = "idSample", description = "Id de la muestra", required = true, order = 2)
    private int idSample;
    @ApiObjectField(name = "idBranch", description = "Id de la Sede", required = false, order = 3)
    private int idBranch;
    @ApiObjectField(name = "idOrder", description = "Id de la orden", required = true, order = 4)
    private long idOrder;
    @ApiObjectField(name = "orderType", description = "Codigo del tipo de orden", required = true, order = 5)
    private String orderType;
    @ApiObjectField(name = "destinations", description = "Id de destinos separados por comas", required = true, order = 7)
    private String destinations;

    public RequestSampleDestination()
    {
    }
    
    public int getIdSample()
    {
        return idSample;
    }

    public void setIdSample(int idSample)
    {
        this.idSample = idSample;
    }

    public long getIdOrder()
    {
        return idOrder;
    }

    public void setIdOrder(long idOrder)
    {
        this.idOrder = idOrder;
    }

    public String getOrderType()
    {
        return orderType;
    }

    public void setOrderType(String orderType)
    {
        this.orderType = orderType;
    }

    public int getIdBranch()
    {
        return idBranch;
    }

    public void setIdBranch(int idBranch)
    {
        this.idBranch = idBranch;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public String getDestinations()
    {
        return destinations;
    }

    public void setDestinations(String destinations)
    {
        this.destinations = destinations;
    }
}

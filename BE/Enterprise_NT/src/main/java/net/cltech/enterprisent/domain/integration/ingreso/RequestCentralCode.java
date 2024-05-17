package net.cltech.enterprisent.domain.integration.ingreso;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la peticion de actualizacion del codigo central
 * 
 * @version 1.0.0
 * @author javila
 * @since 05/02/2020
 * @see Creacion
 */
@ApiObject(
        group = "Integración",
        name = "Items de actualizacion de codigo central",
        description = "Auto creacion de items"
)
public class RequestCentralCode
{
    @ApiObjectField(name = "idOrder", description = "Número de la orden", required = true, order = 1)
    private long idOrder;
    @ApiObjectField(name = "itemOrder", description = "Item de la orden", required = true, order = 2)
    private List<RequestItemCentralCode> itemOrder;
    @ApiObjectField(name = "orders", description = "Identificador de cada orden", required = true, order = 3)
    private List<Long> orders;

    public RequestCentralCode()
    {
    }
    
    public RequestCentralCode(long idOrder, List<RequestItemCentralCode> itemOrder)
    {
        this.idOrder = idOrder;
        this.itemOrder = itemOrder;
    }

    public List<RequestItemCentralCode> getItemOrder()
    {
        return itemOrder;
    }

    public void setItemOrder(List<RequestItemCentralCode> itemOrder)
    {
        this.itemOrder = itemOrder;
    }

    public long getIdOrder()
    {
        return idOrder;
    }

    public void setIdOrder(long idOrder)
    {
        this.idOrder = idOrder;
    }

    public List<Long> getOrders()
    {
        return orders;
    }

    public void setOrders(List<Long> orders)
    {
        this.orders = orders;
    }
}

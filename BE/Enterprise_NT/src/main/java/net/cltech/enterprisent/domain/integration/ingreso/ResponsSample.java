package net.cltech.enterprisent.domain.integration.ingreso;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la respuesta de muestras 
 * de la busqueda por su id de orden, id de la prueba
 * 
 * @version 1.0.0
 * @author javila
 * @since 17/02/2020
 * @see Creación
 */
@ApiObject(
    group = "Integración",
    name = "Muestras",
    description = "respuesta de muestras encontradas por el id de orden, y id de la prueba"
)
public class ResponsSample
{
    @ApiObjectField(name = "idOrden", description = "Id de la orden", required = true, order = 1)
    private long idOrder;
    @ApiObjectField(name = "ItemSamples", description = "Items de las muestras por orden", required = true, order = 2)
    private List<ResponsItemSample> ItemSamples;

    public ResponsSample()
    {
    }

    public ResponsSample(long idOrder, List<ResponsItemSample> ItemSamples)
    {
        this.idOrder = idOrder;
        this.ItemSamples = ItemSamples;
    }

    public List<ResponsItemSample> getItemSamples()
    {
        return ItemSamples;
    }

    public void setItemSamples(List<ResponsItemSample> ItemSamples)
    {
        this.ItemSamples = ItemSamples;
    }

    public long getIdOrder()
    {
        return idOrder;
    }

    public void setIdOrder(long idOrder)
    {
        this.idOrder = idOrder;
    }   
}

package net.cltech.enterprisent.domain.operation.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import net.cltech.enterprisent.domain.operation.orders.Order;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para imprimir codigo de barras
 *
 * @version 1.0.0
 * @author equijano
 * @since 26/06/2019
 * @see Creación
 */
@ApiObject(
        group = "Operación - Comunes",
        name = "Filtro Busquedas codigo de barras",
        description = "Representa filtro con parametros para busquedas para codigo de barras."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterOrderBarcode extends Filter
{

    @ApiObjectField(name = "ordersprint", description = "Lista de ordenes ha imprimir", required = true, order = 1)
    private List<Order> ordersprint;

    public FilterOrderBarcode()
    {
    }

    public List<Order> getOrdersprint()
    {
        return ordersprint;
    }

    public void setOrdersprint(List<Order> ordersprint)
    {
        this.ordersprint = ordersprint;
    }

}

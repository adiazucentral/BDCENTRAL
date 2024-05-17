package net.cltech.enterprisent.domain.operation.results.worklist;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import net.cltech.enterprisent.domain.operation.orders.Order;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase de retorno de la hoja de trabajo
 *
 * @version 1.0.0
 * @author eacuna
 * @since 10/10/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Hoja de trabajo",
        description = "Representa resultado de la generación de la hoja de trabajo"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorklistResult
{

    @ApiObjectField(name = "group", description = "Grupo de la hoja de trabajo", order = 1)
    private int group;
    @ApiObjectField(name = "orders", description = "Ordenes de la hoja de trabajo", order = 2)
    private List<Order> orders;

    public WorklistResult()
    {
    }

    public WorklistResult(int group, List<Order> orders)
    {
        this.group = group;
        this.orders = orders;
    }

    public int getGroup()
    {
        return group;
    }

    public void setGroup(int group)
    {
        this.group = group;
    }

    public List<Order> getOrders()
    {
        return orders;
    }

    public void setOrders(List<Order> orders)
    {
        this.orders = orders;
    }

}

package net.cltech.enterprisent.domain.operation.audit;

import java.util.List;
import net.cltech.enterprisent.domain.operation.orders.Order;

/**
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 30/10/2017
 * @see Creación
 */
public class AuditInformation
{

    private Order order;
    private Action last;
    private List<Action> actions;

    public Order getOrder()
    {
        return order;
    }

    public void setOrder(Order order)
    {
        this.order = order;
    }

    public Action getLast()
    {
        return last;
    }

    public void setLast(Action last)
    {
        this.last = last;
    }

    public List<Action> getActions()
    {
        return actions;
    }

    public void setActions(List<Action> actions)
    {
        this.actions = actions;
    }

}

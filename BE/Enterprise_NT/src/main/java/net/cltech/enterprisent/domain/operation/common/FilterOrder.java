package net.cltech.enterprisent.domain.operation.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.operation.orders.Order;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para orden
 *
 * @version 1.0.0
 * @author eacuna
 * @since 08/09/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Comunes",
        name = "Filtro Busquedas",
        description = "Representa filtro con parametros para busquedas."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterOrder
{

    @ApiObjectField(name = "tests", description = "id´s Examenes/Secciónes a filtrar", order = 1)
    private List<Integer> tests;
    @ApiObjectField(name = "demographics", description = "Lista de filtro por demograficos", order = 2)
    private List<Demographic> demographics;
    @ApiObjectField(name = "order", description = "Informacion del encabezado de la orden", order = 3)
    private Order order;

    public FilterOrder()
    {
    }

    public FilterOrder(List<Integer> tests, List<Demographic> demographics, Order order)
    {
        this.tests = tests;
        this.demographics = demographics;
        this.order = order;
    }

    public List<Integer> getTests()
    {
        return tests;
    }

    public void setTests(List<Integer> tests)
    {
        this.tests = tests;
    }

    public List<Demographic> getDemographics()
    {
        return demographics;
    }

    public void setDemographics(List<Demographic> demographics)
    {
        this.demographics = demographics;
    }

    public Order getOrder()
    {
        return order;
    }

    public void setOrder(Order order)
    {
        this.order = order;
    }

}

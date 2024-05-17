/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.operation.common.FilterOrderHeader;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;


/**
 * Representa clase con filtros para busquedas por demografico
 *
 * @version 1.0.0
 * @author cmartin
 * @since 02/10/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Informes",
        name = "Cambio de estado de los examenes",
        description = "Representa filtro para el cambio de estado de los examenes."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangeState
{
    @ApiObjectField(name = "filterOrderHeader", description = "parametros tipo de reporte", order = 1)
    private FilterOrderHeader filterOrderHeader;
    @ApiObjectField(name = "order", description = "Datos de la orden", order = 2)
    private Order order;
    @ApiObjectField(name = "user", description = "Usuario", order = 4)
    private int user;

    public FilterOrderHeader getFilterOrderHeader() {
        return filterOrderHeader;
    }

    public void setFilterOrderHeader(FilterOrderHeader filterOrderHeader) {
        this.filterOrderHeader = filterOrderHeader;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

}

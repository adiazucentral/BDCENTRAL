/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la entidad para el cambio de precios de los examenes de una orden
 *
 * @version 1.0.0
 * @author omendez
 * @since 11/04/2022
 * @see Creacion
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Cambio de precios",
        description = "Representa la entidad para el cambio de precios de los examenes de una orden"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PriceChange {
    
    @ApiObjectField(name = "order", description = "Orden a la que se le realizo el cambio", required = true, order = 1)
    private Long order;
    @ApiObjectField(name = "tests", description = "Lista de examenes", order = 2)
    private List<BillingTest> tests;
    @ApiObjectField(name = "cashbox", description = "Caja de la orden", required = true, order = 3)
    private CashBox cashbox;

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public List<BillingTest> getTests() {
        return tests;
    }

    public void setTests(List<BillingTest> tests) {
        this.tests = tests;
    }

    public CashBox getCashbox() {
        return cashbox;
    }

    public void setCashbox(CashBox cashbox) {
        this.cashbox = cashbox;
    }
}

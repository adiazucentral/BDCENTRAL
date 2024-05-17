/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.statistic;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la caja de una orden para estadisticas de la aplicacion
 *
 * @version 1.0.0
 * @author omendez
 * @since 23/04/2022
 * @see Creacion
 */
@ApiObject(
        group = "Estadisticas",
        name = "Caja",
        description = "Representa la caja de una orden para estadisticas"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticCashBox {
    
    @ApiObjectField(name = "copay", description = "Copago", required = false, order = 1)
    private Double copay;
    @ApiObjectField(name = "discounts", description = "Descuentos", required = false, order = 2)
    private Double discounts;
    @ApiObjectField(name = "taxe", description = "Impuestos", required = false, order = 3)
    private Double taxe;
    @ApiObjectField(name = "payment", description = "Abonos", required = false, order = 4)
    private Double payment;
    @ApiObjectField(name = "balance", description = "Saldo", required = false, order = 5)
    private Double balance; 
    @ApiObjectField(name = "orderNumber", description = "Número de la orden", required = false, order = 6)
    private Long orderNumber;

    public Double getCopay() {
        return copay;
    }

    public void setCopay(Double copay) {
        this.copay = copay;
    }

    public Double getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Double discounts) {
        this.discounts = discounts;
    }

    public Double getTaxe() {
        return taxe;
    }

    public void setTaxe(Double taxe) {
        this.taxe = taxe;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }
}

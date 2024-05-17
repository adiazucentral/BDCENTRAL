package net.cltech.enterprisent.domain.operation.orders.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el total de la caja
 *
 * @version 1.0.0
 * @author equijano
 * @since 04/10/2018
 * @see Creaciòn
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "TotalPago",
        description = "Representa el total de una caja"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FullPayment
{

    @ApiObjectField(name = "order", description = "Numero de Orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "payment", description = "El abono del pago", required = true, order = 2)
    private BigDecimal payment;
    @ApiObjectField(name = "allpayments", description = "Total a pagar", required = true, order = 3)
    private BigDecimal allpayments;
    @ApiObjectField(name = "totalbalance", description = "Saldo total", required = true, order = 4)
    private BigDecimal totalbalance;
    @ApiObjectField(name = "discountValue", description = "Descuento Valor", required = true, order = 7)
    private BigDecimal discountValue;
    @ApiObjectField(name = "discountPercent", description = "Descuento Porcentaje", required = true, order = 8)
    private BigDecimal discountPercent;
    @ApiObjectField(name = "updateUser", description = "Usuario Actualiza", required = false, order = 5)
    private Long updateUser;
    @ApiObjectField(name = "updateDate", description = "Fecha Actualiza", required = false, order = 6)
    private Date updateDate;
    @ApiObjectField(name = "copay", description = "Copago", required = false, order = 8)
    private BigDecimal copay;
    @ApiObjectField(name = "fee", description = "Cuota Moderadora", required = false, order = 9)
    private BigDecimal fee;
    @ApiObjectField(name = "charge", description = "Cargo domicilio", required = false, order = 10)
    private BigDecimal charge;
    @ApiObjectField(name = "balanceCustomer", description = "Saldo Compañia", required = false, order = 11)
    private BigDecimal balanceCustomer;

    public long getOrder()
    {
        return order;
    }

    public void setOrder(long order)
    {
        this.order = order;
    }

    public BigDecimal getPayment()
    {
        return payment;
    }

    public void setPayment(BigDecimal payment)
    {
        this.payment = payment;
    }

    public BigDecimal getAllpayments()
    {
        return allpayments;
    }

    public void setAllpayments(BigDecimal allpayments)
    {
        this.allpayments = allpayments;
    }

    public BigDecimal getTotalbalance()
    {
        return totalbalance;
    }

    public void setTotalbalance(BigDecimal totalbalance)
    {
        this.totalbalance = totalbalance;
    }

    public Long getUpdateUser()
    {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser)
    {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate()
    {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }

    public BigDecimal getDiscountValue()
    {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue)
    {
        this.discountValue = discountValue;
    }

    public BigDecimal getDiscountPercent()
    {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent)
    {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getCopay() {
        return copay;
    }

    public void setCopay(BigDecimal copay) {
        this.copay = copay;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }

    public BigDecimal getBalanceCustomer() {
        return balanceCustomer;
    }

    public void setBalanceCustomer(BigDecimal balanceCustomer) {
        this.balanceCustomer = balanceCustomer;
    }
}

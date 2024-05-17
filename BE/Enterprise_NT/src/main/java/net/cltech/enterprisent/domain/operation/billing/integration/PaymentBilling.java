/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.billing.integration;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.operation.orders.billing.Payment;

/**
 *
 * @author hpoveda
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentBilling
{

    private String codeMethodOfPayment;
    private String methodOfPayment;
    private String bank;
    private String card;
    private BigDecimal payment;

    public PaymentBilling(Payment payment)
    {
        this.codeMethodOfPayment = payment.getPaymentType().getCode();
        this.methodOfPayment = payment.getPaymentType().getName();
        this.bank = payment.getBank() != null ? payment.getBank().getName() : "";
        this.card = payment.getCard() != null ? payment.getCard().getName() : "";
        this.payment = payment.getPayment();
    }

}

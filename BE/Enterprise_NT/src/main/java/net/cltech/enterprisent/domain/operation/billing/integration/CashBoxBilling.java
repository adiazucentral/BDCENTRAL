/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.billing.integration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.operation.orders.billing.CashBoxHeader;

/**
 *
 * @author hpoveda
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CashBoxBilling
{
    private Long order;
    private BigDecimal discountValue;
    private BigDecimal discountPercent;
    private int copayType;
    private BigDecimal copay;
    private List<PaymentBilling> payMentBillings = new ArrayList<>();
    private String billed;
    private String ruc;
    private int typeCredit;
    private BigDecimal balanceCustomer;
    private BigDecimal chargedPatient;
    private String userCreated;
    private Double copayPercent;
    private BigDecimal balancePatient;

    public CashBoxBilling(CashBoxHeader header)
    {
        this.discountPercent = header.getDiscountPercent();
        this.discountValue = header.getDiscountValue();
        this.copayType = header.getCopayType();
        this.copay = header.getCopay();
        this.billed = header.getBilled();
        this.ruc = header.getRuc();
        this.typeCredit = header.getTypeCredit();
        this.balanceCustomer = header.getBalanceCustomer();
        this.chargedPatient = header.getCharge();
        this.userCreated = header.getEntryUser().getUserName();
        this.copayPercent = header.getCopayPercent();
        this.balancePatient = header.getBalance();
    }

}

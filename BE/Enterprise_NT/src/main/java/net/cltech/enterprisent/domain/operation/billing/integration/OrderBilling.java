/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.billing.integration;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.PatientNT;
import net.cltech.enterprisent.domain.integration.resultados.DemoHeader;
import org.jsondoc.core.annotation.ApiObject;

/**
 * @version 1.0.0
 * @author hpoveda
 * @since 12/05/2022
 * @see Creación
 */
@ApiObject(
        group = "Operación - Facturación",
        name = "Order Facturacion",
        description = "Representa datos de facturacion de una orden "
)
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderBilling
{

    private Long order;
    private String company;
    private PatientNT generalPatient;
    private int branchId;
    private String branchCode;
    private String branch;
    private int typeOfAccountingDocument;
    private int idRate;
    private String rate;
    private List<TestBilling> testBilling = new ArrayList<>();
    private String comment;
    private String createdDate;
    private int idAccount;
    private String Account;
    private CashBoxBilling cashBoxBilling = new CashBoxBilling();
    private int comboInvoice;
    private String idAccountDemo;
    private String nameAccountDemo;
    private String codeAccountDemo;

}

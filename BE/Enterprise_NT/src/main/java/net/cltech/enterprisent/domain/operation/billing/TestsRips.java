/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con información de los examenes asociada a la informacion rips
 *
 * @version 1.0.0
 * @author omendez
 * @since 21/01/2021
 * @see Creación
 */
@ApiObject(
        group = "Facturacion",
        name = "Examenes Rips",
        description = "Representa clase con información de los examenes asociada a la informacion rips."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestsRips 
{
    @ApiObjectField(name = "code", description = "Código", order = 1)
    private String code;
    @ApiObjectField(name = "entryDate", description = "Fecha de ingreso", required = false, order = 57)
    private Date entryDate;
    @ApiObjectField(name = "servicePrice", description = "Precio del servicio", required = true, order = 5)
    private BigDecimal servicePrice;
    @ApiObjectField(name = "priceParticular", description = "Precio del paciente", required = true, order = 5)
    private BigDecimal priceParticular;
    @ApiObjectField(name = "priceAccount", description = "Precio del cliente", required = true, order = 5)
    private BigDecimal priceAccount;
    @ApiObjectField(name = "invoiceP", description = "Factura particular", required = true, order = 6)
    private String invoiceP;
    @ApiObjectField(name = "invoiceC", description = "Factura cliente", required = true, order = 7)
    private String invoiceC;
    @ApiObjectField(name = "cups", description = "CUPS", order = 8)
    private String cups;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getInvoiceP() {
        return invoiceP;
    }

    public void setInvoiceP(String invoiceP) {
        this.invoiceP = invoiceP;
    }

    public String getInvoiceC() {
        return invoiceC;
    }

    public void setInvoiceC(String invoiceC) {
        this.invoiceC = invoiceC;
    }

    public BigDecimal getPriceParticular() {
        return priceParticular;
    }

    public void setPriceParticular(BigDecimal priceParticular) {
        this.priceParticular = priceParticular;
    }

    public BigDecimal getPriceAccount() {
        return priceAccount;
    }

    public void setPriceAccount(BigDecimal priceAccount) {
        this.priceAccount = priceAccount;
    }

    public String getCups() {
        return cups;
    }

    public void setCups(String cups) {
        this.cups = cups;
    }
}

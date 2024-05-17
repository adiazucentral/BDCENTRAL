package net.cltech.enterprisent.domain.operation.orders.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Test;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un examen de la orden para facturacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 10/04/2018
 * @see Creacion
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Examen para facturar",
        description = "Representa un examen que tendra un precio y sera cobrado"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillingTest
{
    @ApiObjectField(name = "id", description = "Id registro", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "patient", description = "Paciente que se le cobra el examen", required = true, order = 2)
    private Patient patient;
    @ApiObjectField(name = "order", description = "Orden a la que se le realizo el examen", required = true, order = 3)
    private Order order;
    @ApiObjectField(name = "test", description = "Examen realizado", required = true, order = 4)
    private Test test;
    @ApiObjectField(name = "servicePrice", description = "Precio del servicio", required = true, order = 5)
    private BigDecimal servicePrice;
    @ApiObjectField(name = "patientPrice", description = "Precio que paga el paciente", required = false, order = 6)
    private BigDecimal patientPrice;
    @ApiObjectField(name = "insurancePrice", description = "Precio que paga la aseguradora", required = false, order = 7)
    private BigDecimal insurancePrice;
    @ApiObjectField(name = "patientInvoice", description = "Id factura del paciente", required = false, order = 8)
    private Integer patientInvoice;
    @ApiObjectField(name = "insuranceInvoice", description = "Id factura de la aseguradora", required = false, order = 9)
    private Integer insuranceInvoice;
    @ApiObjectField(name = "rate", description = "Tarifa que se cobro el examen", required = true, order = 10)
    private Rate rate;
    @ApiObjectField(name = "account", description = "Cliente de la orden", required = false, order = 11)
    private Account account;
    @ApiObjectField(name = "branch", description = "Sede de la orden", required = true, order = 12)
    private Branch branch;
    @ApiObjectField(name = "tax", description = "Impuesto", required = false, order = 13)
    private BigDecimal tax;
    @ApiObjectField(name = "discount", description = "Descuento", required = true, order = 14)
    private BigDecimal discount;
    
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Patient getPatient()
    {
        return patient;
    }

    public void setPatient(Patient patient)
    {
        this.patient = patient;
    }

    public Order getOrder()
    {
        return order;
    }

    public void setOrder(Order order)
    {
        this.order = order;
    }

    public Test getTest()
    {
        return test;
    }

    public void setTest(Test test)
    {
        this.test = test;
    }

    public BigDecimal getServicePrice()
    {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice)
    {
        this.servicePrice = servicePrice;
    }

    public BigDecimal getPatientPrice()
    {
        return patientPrice;
    }

    public void setPatientPrice(BigDecimal patientPrice)
    {
        this.patientPrice = patientPrice;
    }

    public BigDecimal getInsurancePrice()
    {
        return insurancePrice;
    }

    public void setInsurancePrice(BigDecimal insurancePrice)
    {
        this.insurancePrice = insurancePrice;
    }

    public Integer getPatientInvoice()
    {
        return patientInvoice;
    }

    public void setPatientInvoice(Integer patientInvoice)
    {
        this.patientInvoice = patientInvoice;
    }

    public Integer getInsuranceInvoice()
    {
        return insuranceInvoice;
    }

    public void setInsuranceInvoice(Integer insuranceInvoice)
    {
        this.insuranceInvoice = insuranceInvoice;
    }

    public Rate getRate()
    {
        return rate;
    }

    public void setRate(Rate rate)
    {
        this.rate = rate;
    }

    public Account getAccount()
    {
        return account;
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }

    public Branch getBranch()
    {
        return branch;
    }

    public void setBranch(Branch branch)
    {
        this.branch = branch;
    }

    public BigDecimal getTax()
    {
        return tax;
    }

    public void setTax(BigDecimal tax)
    {
        this.tax = tax;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.order);
        hash = 41 * hash + Objects.hashCode(this.test);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final BillingTest other = (BillingTest) obj;
        if (!Objects.equals(this.order, other.order))
        {
            return false;
        }
        if (!Objects.equals(this.test, other.test))
        {
            return false;
        }
        return true;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}

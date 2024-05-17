package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Detalle de la nota crédito de una factura
*
* @version 1.0.0
* @author Julian
* @since 22/04/2021
* @see Creación
*/

@ApiObject(
        group = "Operación - Facturación",
        name = "Detalle Nota Crédito",
        description = "Representa el detalle de la nota crédito de una factura"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditNoteDetail 
{
    @ApiObjectField(name = "creditNoteId", description = "Id de la nota de credito", required = true, order = 1)
    private Long creditNoteId;
    @ApiObjectField(name = "orderId", description = "Id de la orden", required = true, order = 2)
    private Long orderId;
    @ApiObjectField(name = "testId", description = "Id del examen", required = true, order = 3)
    private Integer testId;
    @ApiObjectField(name = "patientId", description = "Id del paciente", required = true, order = 4)
    private Integer patientId;
    @ApiObjectField(name = "servicePrice", description = "Precio del servicio", required = true, order = 5)
    private Double servicePrice;
    @ApiObjectField(name = "patientPrice", description = "Precio del paciente", required = true, order = 6)
    private Double patientPrice;
    @ApiObjectField(name = "insurancePrice", description = "Precio de la aseguradora", required = true, order = 7)
    private Double insurancePrice;
    @ApiObjectField(name = "rateId", description = "Id de la tarifa", required = true, order = 8)
    private Integer rateId;
    @ApiObjectField(name = "customerId", description = "Id del cliente", required = true, order = 9)
    private Integer customerId;
    @ApiObjectField(name = "branchId", description = "Id de la sede", required = true, order = 10)
    private Integer branchId;

    public CreditNoteDetail()
    {
    }

    public Long getCreditNoteId()
    {
        return creditNoteId;
    }

    public void setCreditNoteId(Long creditNoteId)
    {
        this.creditNoteId = creditNoteId;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Integer getTestId()
    {
        return testId;
    }

    public void setTestId(Integer testId)
    {
        this.testId = testId;
    }

    public Integer getPatientId()
    {
        return patientId;
    }

    public void setPatientId(Integer patientId)
    {
        this.patientId = patientId;
    }

    public Double getServicePrice()
    {
        return servicePrice;
    }

    public void setServicePrice(Double servicePrice)
    {
        this.servicePrice = servicePrice;
    }

    public Double getPatientPrice()
    {
        return patientPrice;
    }

    public void setPatientPrice(Double patientPrice)
    {
        this.patientPrice = patientPrice;
    }

    public Double getInsurancePrice()
    {
        return insurancePrice;
    }

    public void setInsurancePrice(Double insurancePrice)
    {
        this.insurancePrice = insurancePrice;
    }

    public Integer getRateId()
    {
        return rateId;
    }

    public void setRateId(Integer rateId)
    {
        this.rateId = rateId;
    }

    public Integer getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Integer customerId)
    {
        this.customerId = customerId;
    }

    public Integer getBranchId()
    {
        return branchId;
    }

    public void setBranchId(Integer branchId)
    {
        this.branchId = branchId;
    }
}
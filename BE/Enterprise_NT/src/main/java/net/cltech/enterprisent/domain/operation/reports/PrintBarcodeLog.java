/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la clase de registro del log de impresion de codigo de barras
 *
 * @version 1.0.0
 * @author equijano
 * @since 24/05/2019
 * @see Creación
 */
@ApiObject(
        group = "Operación - Informes",
        name = "Regitro de log de impresion de codigo de barras",
        description = "Representa el objeto de log de impresion de codigo de barras."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrintBarcodeLog
{

    @ApiObjectField(name = "orderNumber", description = "Numero de orden", required = true, order = 1)
    private Long orderNumber;
    @ApiObjectField(name = "sample", description = "Id de la muestra", required = true, order = 2)
    private Integer sample;
    @ApiObjectField(name = "printing", description = "Valida si se imprimio o no", required = true, order = 3)
    private boolean printing;
    @ApiObjectField(name = "zpl", description = "Zpl del codigo de barras", required = false, order = 4)
    private String zpl;
    @ApiObjectField(name = "patientId", description = "Id del paciente", required = true, order = 5)
    private Integer patientId;

    public PrintBarcodeLog(Long orderNumber, Integer sample, boolean printing, String zpl)
    {
        this.orderNumber = orderNumber;
        this.sample = sample;
        this.printing = printing;
        this.zpl = zpl;
    }

    public PrintBarcodeLog(boolean printing, String zpl, Integer patientId) {
        this.printing = printing;
        this.zpl = zpl;
        this.patientId = patientId;
    }

    public Long getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public Integer getSample()
    {
        return sample;
    }

    public void setSample(Integer sample)
    {
        this.sample = sample;
    }

    public boolean isPrinting()
    {
        return printing;
    }

    public void setPrinting(boolean printing)
    {
        this.printing = printing;
    }

    public String getZpl()
    {
        return zpl;
    }

    public void setZpl(String zpl)
    {
        this.zpl = zpl;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }
}

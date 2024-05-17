/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la clase de registro del log de impresion de codigo de barras
 *
 * @version 1.0.0
 * @author omendez
 * @since 10/05/2021
 * @see Creación
 */
@ApiObject(
        group = "Patologia - Informes",
        name = "Regitro de log de impresion de codigo de barras",
        description = "Representa el objeto de log de impresion de codigo de barras."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BarcodeLog 
{
    @ApiObjectField(name = "caseNumber", description = "Numero del caso", required = true, order = 1)
    private Long caseNumber;
    @ApiObjectField(name = "printing", description = "Valida si se imprimio o no", required = true, order = 2)
    private boolean printing;
    @ApiObjectField(name = "zpl", description = "Zpl del codigo de barras", required = false, order = 3)
    private String zpl;
    
     public BarcodeLog(Long caseNumber, boolean printing, String zpl)
    {
        this.caseNumber = caseNumber;
        this.printing = printing;
        this.zpl = zpl;
    }

    public Long getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(Long caseNumber) {
        this.caseNumber = caseNumber;
    }

    public boolean isPrinting() {
        return printing;
    }

    public void setPrinting(boolean printing) {
        this.printing = printing;
    }

    public String getZpl() {
        return zpl;
    }

    public void setZpl(String zpl) {
        this.zpl = zpl;
    }
}

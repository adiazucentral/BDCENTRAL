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
 * Representa la clase del reporte por barcode
 *
 * @version 1.0.0
 * @author omendez
 * @since 10/05/2021
 * @see Creación
 */
@ApiObject(
        group = "Patologia - Informes",
        name = "Reporte zpl",
        description = "Representa el objeto del reporte por barcode."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterBarcodePathology 
{
    @ApiObjectField(name = "casePat", description = "Caso ha imprimir", required = false, order = 1)
    private Case casePat = new Case();
    @ApiObjectField(name = "count", description = "Cantidad de impresiones", order = 4)
    private int count;
    @ApiObjectField(name = "serial", description = "Id unico para imprimir", order = 6)
    private String serial;

    public Case getCasePat() {
        return casePat;
    }

    public void setCasePat(Case casePat) {
        this.casePat = casePat;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
        
    }
}

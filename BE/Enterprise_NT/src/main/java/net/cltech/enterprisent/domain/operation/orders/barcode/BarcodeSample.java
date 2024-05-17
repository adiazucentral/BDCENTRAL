/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders.barcode;

import java.util.Objects;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la muestra y la cantidad de etiquetas en la impresión de
 * códigos de barras
 *
 * @author cmartin
 * @version 1.0.0
 * @since 06/10/2017
 * @see Creación
 */
public class BarcodeSample
{

    @ApiObjectField(name = "idSample", description = "Id de la orden a imprimir.", required = false, order = 1)
    private Integer idSample;
    @ApiObjectField(name = "quantity", description = "Cantidad de etiquetas que se imprimiran de la muestra.", required = true, order = 2)
    private Integer quantity;
    @ApiObjectField(name = "barcode", description = "Zpl del codigo de barras", required = true, order = 2)
    private String barcode;
    @ApiObjectField(name = "codeSample", description = "Codigo de la muestra.", required = false, order = 4)
    private String codeSample;

    public BarcodeSample()
    {

    }

    public BarcodeSample(Integer idSample)
    {
        this.idSample = idSample;
    }

    public BarcodeSample(Integer idSample, Integer quantity)
    {
        this.idSample = idSample;
        this.quantity = quantity;
    }

    public Integer getIdSample()
    {
        return idSample;
    }

    public void setIdSample(Integer idSample)
    {
        this.idSample = idSample;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public String getBarcode()
    {
        return barcode;
    }

    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }

    public String getCodeSample() {
        return codeSample;
    }

    public void setCodeSample(String codeSample) {
        this.codeSample = codeSample;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 3;
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
        final BarcodeSample other = (BarcodeSample) obj;
        return Objects.equals(this.idSample, other.idSample);
    }

}

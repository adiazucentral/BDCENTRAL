package net.cltech.enterprisent.domain.operation.orders.barcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Peticion para envio de impresión de códigos de barras
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 27/09/2017
 * @see Creación
 */
public class BarcodeRequest
{

    private String id;
    private List<String> barcodeString = new ArrayList<>();

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public List<String> getBarcodeString()
    {
        return barcodeString;
    }

    public void setBarcodeString(List<String> barcodeString)
    {
        this.barcodeString = barcodeString;
    }

}

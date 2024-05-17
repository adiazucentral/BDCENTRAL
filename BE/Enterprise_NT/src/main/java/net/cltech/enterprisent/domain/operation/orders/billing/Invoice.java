package net.cltech.enterprisent.domain.operation.orders.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la factura de una caja
*
* @version 1.0.0
* @author Julian
* @since 21/10/2020
* @see Creación
*/
@ApiObject(
        group = "Operación - Ordenes",
        name = "Factura",
        description = "Representa la factura de una caja"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Invoice 
{
    @ApiObjectField(name = "invoiceId", description = "Id de la factura", required = true, order = 1)
    private Long invoiceId;
    @ApiObjectField(name = "state", description = "Estado de la factura", required = true, order = 2)
    private boolean state;

    public Invoice()
    {
        this.invoiceId = 0L;
        this.state = false;
    }

    public Invoice(Long invoiceId, boolean state)
    {
        this.invoiceId = invoiceId;
        this.state = state;
    }
    
    public Long getInvoiceId()
    {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId)
    {
        this.invoiceId = invoiceId;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }
}
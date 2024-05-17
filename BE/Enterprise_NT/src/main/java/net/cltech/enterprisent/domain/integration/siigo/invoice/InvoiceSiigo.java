package net.cltech.enterprisent.domain.integration.siigo.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.integration.siigo.AccountSiigo;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la factura que será enviada a Siigo
*
* @version 1.0.0
* @author Julian
* @since 29/04/2021
* @see Creación
*/

@ApiObject(
        group = "Integración con Siigo",
        name = "Factura Siigo",
        description = "Representa la factura que se enviará a SIIGO"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter 
@Setter
public class InvoiceSiigo 
{
    @ApiObjectField(name = "document", description = "Encabezado de la factura", required = true, order = 1)
    private DocumentSiigo document;
    @ApiObjectField(name = "date", description = "Fecha de la factura, formato yyyy-MM-dd", required = true, order = 2)
    private String date;
    @ApiObjectField(name = "customer", description = "Encabezado de la factura", required = true, order = 3)
    private AccountSiigo customer;
    @ApiObjectField(name = "seller", description = "ID del vendedor asociado a la factura", required = true, order = 4)
    private Integer seller;
    @ApiObjectField(name = "observations", description = "Comentarios para agregar información a la factura", required = false, order = 5)
    private Integer observations;
    @ApiObjectField(name = "items", description = "Productos o Servicios asociados a la factura", required = true, order = 6)
    private List<ProductSiigo> items;
    @ApiObjectField(name = "payments", description = "Formas de pago asociadas a la factura", required = true, order = 7)
    private List<PaymentsSiigo> payments;
    @ApiObjectField(name = "lisInvoiceId", description = "Id de la factura del LIS", required = true, order = 8)
    private Long lisInvoiceId;

    public InvoiceSiigo()
    {
        this.document = new DocumentSiigo();
        this.customer = new AccountSiigo();
        this.items = new ArrayList<>();
        this.payments = new ArrayList<>();
    }
}
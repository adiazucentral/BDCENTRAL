package net.cltech.enterprisent.domain.integration.siigo.invoice;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la nota de crédito que se enviará como petición a Siigo
*
* @version 1.0.0
* @author Julian
* @since 27/05/2021
* @see Creación
*/

@ApiObject(
        group = "Integración con Siigo",
        name = "Nota De Crédito Siigo",
        description = "Representa la nota de crédito que se enviará como petición a Siigo"
)
@Getter
@Setter
public class CreditNoteSiigo 
{
    @ApiObjectField(name = "document", description = "Encabezado de la factura", required = true, order = 1)
    private DocumentSiigo document;
    @ApiObjectField(name = "date", description = "Fecha de la factura, formato yyyy-MM-dd", required = true, order = 2)
    private String date;
    @ApiObjectField(name = "invoice", description = "Id de la factura generada en Siigo", required = true, order = 3)
    private String invoice;
    @ApiObjectField(name = "items", description = "Productos o Servicios asociados a la factura.", required = true, order = 4)
    private List<ProductSiigo> items;
    @ApiObjectField(name = "payments", description = "Formas de pago asociadas a la Nota Crédito.", required = true, order = 5)
    private List<PaymentsSiigo> payments;
    @ApiObjectField(name = "number", description = "Consecutivo/número del comprobante, el campo es obligatorio según la configuración del tipo de comprobante.", required = true, order = 6)
    private Long number;

    public CreditNoteSiigo()
    {
        this.document = new DocumentSiigo();
        this.items = new ArrayList<>();
        this.payments = new ArrayList<>();
    }
}
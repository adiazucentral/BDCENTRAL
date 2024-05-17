package net.cltech.enterprisent.domain.operation.billing.statement;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el detalle del reporte de la factura
 * 
 * @version 1.0.0
 * @author javila
 * @since 13/08/2021
 * @see Creación
 */

@ApiObject(
        group = "Operación - Facturación",
        name = "Detalle Del Reporte De La Factura",
        description = "Representa el detalle del reporte de la factura"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class InvoiceReportDetail
{
    @ApiObjectField(name = "invoiceNumber", description = "Número de la factura", required = true, order = 1)
    private String invoiceNumber;
    @ApiObjectField(name = "totalValue", description = "Valor total de la factura", required = true, order = 2)
    private Double totalValue;
    @ApiObjectField(name = "invoiceDate", description = "Fecha de la factura", required = true, order = 3)
    private Date invoiceDate;
    @ApiObjectField(name = "expirationDate", description = "Fecha de vencimiento", required = true, order = 4)
    private Date expirationDate;
    @ApiObjectField(name = "paymentDate", description = "Fecha de pago", required = true, order = 5)
    private Date paymentDate;
    @ApiObjectField(name = "paymentStatus", description = "Estado de pago: Pendiente, Vencida", required = true, order = 5)
    private String paymentStatus;
}

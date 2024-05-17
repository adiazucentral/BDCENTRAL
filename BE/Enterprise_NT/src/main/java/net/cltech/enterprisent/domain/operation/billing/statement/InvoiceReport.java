package net.cltech.enterprisent.domain.operation.billing.statement;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el reporte de la factura
 * 
 * @version 1.0.0
 * @author javila
 * @since 13/08/2021
 * @see Creación
 */

@ApiObject(
        group = "Operación - Facturación",
        name = "Reporte De Factura",
        description = "Representa el reporte de factura"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class InvoiceReport
{
    @ApiObjectField(name = "startDate", description = "Fecha de inicio formato ddMMyyyy", required = true, order = 1)
    private String startDate;
    @ApiObjectField(name = "endDate", description = "Fecha de finalización formato ddMMyyyy", required = true, order = 2)
    private String endDate;
    @ApiObjectField(name = "dateOfPrinting", description = "Fecha de la impresión", required = true, order = 3)
    private Date dateOfPrinting;
    @ApiObjectField(name = "userName", description = "Nickname del usuario que lo imprime", required = true, order = 4)
    private String userName;
    @ApiObjectField(name = "details", description = "Detalles del reporte de la factura", required = true, order = 5)
    private List<InvoiceReportDetail> details = new ArrayList<>();
    @ApiObjectField(name = "unbilledOrders", description = "Ordenes sin facturar", required = true, order = 6)
    private List<UnbilledOrder> unbilledOrders = new ArrayList<>();
}

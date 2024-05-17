package net.cltech.enterprisent.domain.operation.tracking;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.operation.billing.Invoice;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa los datos de la auditoria de una factura
 * 
 * @version 1.0.0
 * @author javila
 * @since 08/09/2021
 * @see Creación
 */

@ApiObject(
        name = "Auditoria De La Factura",
        group = "Trazabilidad",
        description = "Representa los datos de la auditoria de una factura"
)
@Getter
@Setter
@NoArgsConstructor
public class InvoiceAudit
{
    @ApiObjectField(name = "invoiceAudits", description = "Lista de auditorias de esa factura", order = 1)
    private List<AuditOperation> invoiceAudits = new ArrayList<>();
    @ApiObjectField(name = "invoice", description = "Detalle de la factura", order = 2)
    private Invoice invoice = new Invoice();
}

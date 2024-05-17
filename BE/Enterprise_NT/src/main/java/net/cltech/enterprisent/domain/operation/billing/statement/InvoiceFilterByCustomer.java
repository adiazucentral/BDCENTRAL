package net.cltech.enterprisent.domain.operation.billing.statement;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el filtro para consultar las facturas por cliente
 * 
 * @version 1.0.0
 * @author javila
 * @since 17/08/2021
 * @see Creación
 */

@ApiObject(
        group = "Operación - Facturación",
        name = "Filtro De Factura Por Cliente",
        description = "Representa el filtro para consultar las facturas por cliente"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class InvoiceFilterByCustomer
{
    @ApiObjectField(name = "startDate", description = "Fecha de inicio del filtro", required = true, order = 1)
    private Date startDate;
    @ApiObjectField(name = "endDate", description = "Fecha final del filtro", required = true, order = 2)
    private Date endDate;
    @ApiObjectField(name = "customerId", description = "Id del cliente", required = true, order = 3)
    private int customerId;
}

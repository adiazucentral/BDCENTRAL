package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el filtro simple de una factura
 *
 * @version 1.0.0
 * @author Julian
 * @since 22/04/2021
 * @see Creación
 */
@ApiObject(
        group = "Operación - Facturación",
        name = "Filtro Simple Para La Factura",
        description = "Representa el filtro con el que se obtendra un detalle simple de una factura"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class FilterSimpleInvoice
{
    @ApiObjectField(name = "initialInvoice", description = "Factura inicial", required = true, order = 1)
    private String initialInvoice;
    @ApiObjectField(name = "finalInvoice", description = "Factura final", required = true, order = 2)
    private String finalInvoice;
    @ApiObjectField(name = "invoiceDate", description = "Fecha de facturación", required = true, order = 3)
    private String invoiceDate;
    @ApiObjectField(name = "creditNoteId", description = "Id de la nota de credito", required = true, order = 4)
    private Integer creditNoteId;
    @ApiObjectField(name = "idInitialInvoice", description = "Id de la factura inicial", required = true, order = 5)
    private long idInitialInvoice;
    @ApiObjectField(name = "idFinalInvoice", description = "Id de la factura final", required = true, order = 6)
    private long idFinalInvoice;
    @ApiObjectField(name = "customer", description = "Id del cliente ", required = true, order = 7)
    private Integer customer;
    
}

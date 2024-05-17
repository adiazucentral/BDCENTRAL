package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la nota crédito
*
* @version 1.0.0
* @author Julian
* @since 21/04/2021
* @see Creación
*/
@ApiObject(
        group = "Operación - Facturación",
        name = "Nota crédito",
        description = "Representa una nota crédito"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class CreditNote 
{
    @ApiObjectField(name = "invoiceNumber", description = "Número de la factura", required = true, order = 1)
    private String invoiceNumber;
    @ApiObjectField(name = "id", description = "identificacion de la nota credito", required = true, order = 2)
    private int id;
    @ApiObjectField(name = "reserveNumber", description = "Reservar número", required = true, order = 2)
    private Long reserveNumber;
    @ApiObjectField(name = "dateOfNote", description = "Fecha de la creación de la nota", required = true, order = 3)
    private Timestamp dateOfNote;
    @ApiObjectField(name = "userId", description = "Id del usuario que genero la nota", required = true, order = 4)
    private Integer userId;
    @ApiObjectField(name = "username", description = "username del usuario que anulo la factura", required = true, order = 4)
    private String username;
    @ApiObjectField(name = "cancellationReason", description = "Motivo de anulación", required = true, order = 5)
    private String cancellationReason;
    @ApiObjectField(name = "value", description = "Valor", required = true, order = 6)
    private Double value;
    @ApiObjectField(name = "details", description = "Detalles de la factura que se asociaran a la nota credito", required = false, order = 7)
    private List<CreditNoteDetail> details;
    @ApiObjectField(name = "invoiceId", description = "Id de la factura", required = true, order = 8)
    private Long invoiceId;
    @ApiObjectField(name = "type", description = "tipo de nota credito 0->particular. 1->clientes", required = true, order = 8)
    private int type;

    public CreditNote()
    {
    }
}
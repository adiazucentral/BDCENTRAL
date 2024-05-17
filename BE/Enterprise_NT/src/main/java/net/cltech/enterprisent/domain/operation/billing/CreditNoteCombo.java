/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
        name = "Nota crédito combo",
        description = "Representa una nota crédito"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class CreditNoteCombo {
    @ApiObjectField(name = "idInvoice", description = "Número de la factura", required = true, order = 1)
    private Integer idInvoice;
    @ApiObjectField(name = "id", description = "identificacion de la nota credito", required = true, order = 2)
    private int id;
    @ApiObjectField(name = "dateOfNote", description = "Fecha de la creación de la nota", required = true, order = 3)
    private Timestamp dateOfNote;
    @ApiObjectField(name = "userId", description = "Id del usuario que genero la nota", required = true, order = 4)
    private Integer userId;
    @ApiObjectField(name = "userNames", description = "Nombres del usuario", required = true, order = 5)
    private String userNames;
    @ApiObjectField(name = "userLastNames", description = "Apellidos del usuario", required = true, order = 6)
    private String userLastNames;
    @ApiObjectField(name = "username", description = "Nombre de usuario", required = true, order = 7)
    private String username;
    @ApiObjectField(name = "totalOrders", description = "Total de ordenes", required = true, order = 8)
    private int totalOrders;
    @ApiObjectField(name = "cancellationReason", description = "Motivo de anulación", required = true, order = 9)
    private String cancellationReason;

    public CreditNoteCombo()
    {
    }
}

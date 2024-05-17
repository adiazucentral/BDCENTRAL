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
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.operation.billing.integration.OrderBilling;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la factura combo
*
* @version 1.0.0
* @author Julian
* @since 13/04/2021
* @see Creación
*/

@ApiObject(
        group = "Operación - Facturación",
        name = "Factura Combo",
        description = "Representa los datos necesarios para la factura combo"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class ComboInvoice {
    
    @ApiObjectField(name = "invoiceId", description = "Id de la factura (Identificador BD)", required = true, order = 1)
    private Integer invoiceId;
    @ApiObjectField(name = "dateOfInvoice", description = "Fecha de creación de la factura", required = true, order = 2)
    private Timestamp dateOfInvoice;
    @ApiObjectField(name = "userId", description = "Id del usuario", required = true, order = 3)
    private Integer userId;
    @ApiObjectField(name = "state", description = "Estado de la factura", required = true, order = 4)
    private boolean state;
    @ApiObjectField(name = "comment", description = "Comentario", required = true, order = 5)
    private String comment;
    @ApiObjectField(name = "orders", description = "Lista de ordenes asociadas a la factura", required = false, order = 6)
    private List<OrderBilling> orders;
    @ApiObjectField(name = "userNames", description = "Nombres del usuario", required = true, order = 7)
    private String userNames;
    @ApiObjectField(name = "userLastNames", description = "Apellidos del usuario", required = true, order = 8)
    private String userLastNames;
    @ApiObjectField(name = "username", description = "Nombre de usuario", required = true, order = 9)
    private String username;

}

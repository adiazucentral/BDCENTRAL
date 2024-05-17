package net.cltech.enterprisent.domain.masters.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un pago en la aplicación Homebound
 *
 * @version 1.0.0
 * @author jbarbosa
 * @since 15/07/2021
 * @see Creaciòn
 */
@ApiObject(
        group = "Facturacíon",
        name = "Pago De Homebound",
        description = "Representa un pago en la aplicación homebound"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class HomeboundPayment
{
    @ApiObjectField(name = "id", description = "Identificador del registro de pago, si no se envia se adiciona un nuevo pago", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "documentNo", description = "Número de tarjeta/cheque/bono", required = false, order = 2)
    private String documentNo;
    @ApiObjectField(name = "price", description = "Valor del pago realizado", required = false, order = 3)
    private Double price;
    @ApiObjectField(name = "paymentId", description = "Id Tipo de pago", required = false, order = 4)
    private Integer paymentId;
    @ApiObjectField(name = "cardId", description = "Id de la tarjeta", required = false, order = 5)
    private Integer cardId;
    @ApiObjectField(name = "bankId", description = "Id del banco", required = false, order = 6)
    private Integer bankId;
}

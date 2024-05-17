package net.cltech.enterprisent.domain.operation.orders.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.billing.Bank;
import net.cltech.enterprisent.domain.masters.billing.Card;
import net.cltech.enterprisent.domain.masters.billing.PaymentType;
import net.cltech.enterprisent.domain.masters.user.User;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el abono realizado a una orden
 *
 * @version 1.0.0
 * @author dcortes
 * @since 27/04/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Caja - Pagos",
        description = "Representa los pagos realizados a una orden"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class Payment
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "order", description = "Numero de Orden", required = true, order = 2)
    private long order;
    @ApiObjectField(name = "paymentType", description = "Tipo Pago", required = true, order = 3)
    private PaymentType paymentType;
    @ApiObjectField(name = "number", description = "Numero", required = true, order = 4)
    private String number;
    @ApiObjectField(name = "bank", description = "Banco", required = true, order = 5)
    private Bank bank;
    @ApiObjectField(name = "card", description = "Tarjeta", required = true, order = 6)
    private Card card;
    @ApiObjectField(name = "payment", description = "Valor del pago", required = true, order = 7)
    private BigDecimal payment;
    @ApiObjectField(name = "active", description = "Si esta activo", required = true, order = 8)
    private boolean active;
    @ApiObjectField(name = "entryUser", description = "Usuario ingreso", required = true, order = 9)
    private User entryUser;
    @ApiObjectField(name = "entryDate", description = "Fecha Ingreso", required = true, order = 10)
    private Date entryDate;
    @ApiObjectField(name = "updateUser", description = "Usuario Actualiza", required = false, order = 11)
    private User updateUser;
    @ApiObjectField(name = "updateDate", description = "Fecha Actualiza", required = false, order = 12)
    private Date updateDate;
}

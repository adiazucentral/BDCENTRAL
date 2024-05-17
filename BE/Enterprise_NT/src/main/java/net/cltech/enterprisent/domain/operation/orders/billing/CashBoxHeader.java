package net.cltech.enterprisent.domain.operation.orders.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.user.User;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la cabecera de la caja
 *
 * @version 1.0.0
 * @author dcortes
 * @since 27/04/2018
 * @see Creacion
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Caja - Cabecera",
        description = "Representa el cobro de una orden para una tarifa"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class CashBoxHeader
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "order", description = "Numero de Orden", required = true, order = 2)
    private long order;
    @ApiObjectField(name = "rate", description = "Tarifa", required = true, order = 3)
    private Rate rate;
    @ApiObjectField(name = "subTotal", description = "Sub total", required = true, order = 4)
    private BigDecimal subTotal;
    @ApiObjectField(name = "discountValue", description = "Descuento Valor", required = true, order = 5)
    private BigDecimal discountValue;
    @ApiObjectField(name = "discountPercent", description = "Descuento Porcentaje", required = true, order = 6)
    private BigDecimal discountPercent;
    @ApiObjectField(name = "taxValue", description = "Impuesto en valor", required = true, order = 7)
    private BigDecimal taxValue;
    @ApiObjectField(name = "copay", description = "Copago", required = false, order = 8)
    private BigDecimal copay;
    @ApiObjectField(name = "fee", description = "Cuota Moderadora", required = false, order = 9)
    private BigDecimal fee;
    @ApiObjectField(name = "totalPaid", description = "Total Pagado", required = true, order = 10)
    private BigDecimal totalPaid;
    @ApiObjectField(name = "balance", description = "Saldo", required = true, order = 11)
    private BigDecimal balance;
    @ApiObjectField(name = "entryUser", description = "Usuario ingreso", required = true, order = 12)
    private User entryUser;
    @ApiObjectField(name = "entryDate", description = "Fecha Ingreso", required = true, order = 13)
    private Date entryDate;
    @ApiObjectField(name = "updateUser", description = "Usuario Actualiza", required = false, order = 14)
    private User updateUser;
    @ApiObjectField(name = "updateDate", description = "Fecha Actualiza", required = false, order = 15)
    private Date updateDate;
    @ApiObjectField(name = "discountValueRate", description = "Descuento Valor por tarifa", required = true, order = 16)
    private BigDecimal discountValueRate;
    @ApiObjectField(name = "discountPercentRate", description = "Descuento Porcentaje por tarifa", required = true, order = 17)
    private BigDecimal discountPercentRate;
    @ApiObjectField(name = "copayType", description = "Tipo de copago", required = true, order = 18)
    private int copayType;
    @ApiObjectField(name = "charge", description = "Cargo de domicilio", required = false, order = 19)
    private BigDecimal charge;
    @ApiObjectField(name = "billed", description = "Facturado a", required = true, order = 20)
    private String billed;
    @ApiObjectField(name = "ruc", description = "RUC", required = true, order = 21)
    private String ruc;
    @ApiObjectField(name = "typeCredit", description = "Tipo de credito", required = true, order = 22)
    private int typeCredit;
    @ApiObjectField(name = "comboBills", description = "Orden marcada para factura combo", required = true, order = 23)
    private int comboBills;
    @ApiObjectField(name = "phone", description = "telefono de la caja", required = true, order = 24)
    private String phone;
    @ApiObjectField(name = "balanceCustomer", description = "Saldo Compañia", required = false, order = 25)
    private BigDecimal balanceCustomer;
    @ApiObjectField(name = "copayPercent", description = "Copago Porcentaje", required = true, order = 26)
    private Double copayPercent;
}

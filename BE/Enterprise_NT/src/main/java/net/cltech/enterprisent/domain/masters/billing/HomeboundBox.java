package net.cltech.enterprisent.domain.masters.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un registro de caja en la aplicación Homebound
 *
 * @version 1.0.0
 * @author jbarbosa
 * @since 15/07/2021
 * @see Creaciòn
 */
@ApiObject(
        group = "Facturacíon",
        name = "homeboundBox",
        description = "Representa un registro de caja en la aplicación Homebound"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class HomeboundBox
{
    @ApiObjectField(name = "order", description = "Numero de Orden", required = false, order = 1)
    private Long order;
    @ApiObjectField(name = "subtotal", description = "Subtotal", required = false, order = 2)
    private Double subtotal;
    @ApiObjectField(name = "tax", description = "Impuesto", required = false, order = 3)
    private Double tax;
    @ApiObjectField(name = "totalpayment", description = "Total de abonos", required = false, order = 4)
    private Double totalpayment;
    @ApiObjectField(name = "discountType", description = "Tipo de descuento: (0) - Porcentaje (1) - Valor", required = false, order = 5)
    private int discountType;
    @ApiObjectField(name = "discount", description = "Valor del descuento", required = false, order = 6)
    private Double discount;
    @ApiObjectField(name = "discountPercent", description = "Porcentaje de descuento", required = false, order = 7)
    private Double discountPercent;
    @ApiObjectField(name = "copay", description = "Copago", required = false, order = 8)
    private Double copay;
    @ApiObjectField(name = "patientResponsability", description = "Valor cuota moderadora", required = false, order = 9)
    private Double patientResponsability;
    @ApiObjectField(name = "user", description = "Id del usuario", required = false, order = 10)
    private Integer user;
    @ApiObjectField(name = "userDiscount", description = "Valor de descuento permitido por usuario", required = false, order = 11)
    private Double userDiscount;
    @ApiObjectField(name = "id", description = "Id del registro de la caja", required = false, order = 12)
    private Integer id;
    @ApiObjectField(name = "payments", description = "Lista de los abonos", required = false, order = 13)
    private List<HomeboundPayment> payments;
}

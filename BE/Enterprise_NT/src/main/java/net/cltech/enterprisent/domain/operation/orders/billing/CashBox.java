package net.cltech.enterprisent.domain.operation.orders.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la caja de una orden
 *
 * @version 1.0.0
 * @author dcortes
 * @since 2/05/2018
 * @see Creaciòn
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Caja",
        description = "Representa la caja cobrada a una orden"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
// Lombok
@Getter
@Setter

public class CashBox
{
    @ApiObjectField(name = "order", description = "Numero de Orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "header", description = "Cabecera de la caja de la orden", required = true, order = 2)
    private CashBoxHeader header;
    @ApiObjectField(name = "payments", description = "Pagos efectuados", required = true, order = 3)
    private List<Payment> payments = new ArrayList<>();

    public CashBox() {
        this.header = new CashBoxHeader();
    }
    
    
}

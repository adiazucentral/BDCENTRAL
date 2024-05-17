package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa el detalle para recalcular
*
* @version 1.0.0
* @author Julian
* @since 27/04/2021
* @see Creación
*/

@ApiObject(
        group = "Operación - Facturación",
        name = "Detalle Para Recalcular",
        description = "Representa el detalle con el que se podra recalcular"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class DetailToRecalculate 
{
    @ApiObjectField(name = "orderId", description = "Id de la orden", required = true, order = 1)
    private Long orderId;
    @ApiObjectField(name = "testId", description = "Id del examen", required = true, order = 2)
    private Integer testId;
    @ApiObjectField(name = "servicePrice", description = "Precio del servicio", required = true, order = 3)
    private BigDecimal servicePrice;
    @ApiObjectField(name = "patientPrice", description = "Precio que paga el paciente", required = true, order = 4)
    private BigDecimal patientPrice;
    @ApiObjectField(name = "insurancePrice", description = "Precio que paga la aseguradora", required = true, order = 5)
    private BigDecimal insurancePrice;
    @ApiObjectField(name = "testName", description = "Nombre del examen", required = true, order = 6)
    private String testName;
}
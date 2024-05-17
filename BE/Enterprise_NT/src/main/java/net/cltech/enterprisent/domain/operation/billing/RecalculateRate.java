package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa los filtros que se usaran para realizar el
* recalculo de tarifas
*
* @version 1.0.0
* @author Julian
* @since 27/04/2021
* @see Creación
*/

@ApiObject(
        group = "Operación - Facturación",
        name = "Recalcular Tarifa",
        description = " Representa los filtros que se usaran para realizar el recalculo de tarifas"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class RecalculateRate 
{
    @ApiObjectField(name = "customerId", description = "Id del cliente", required = true, order = 1)
    private Integer customerId;
    @ApiObjectField(name = "rateId", description = "Id de la tarifa", required = true, order = 2)
    private Integer rateId;
    @ApiObjectField(name = "startDate", description = "Fecha de inicio", required = true, order = 3)
    private String startDate;
    @ApiObjectField(name = "endDate", description = "Fecha de finalización", required = true, order = 4)
    private String endDate;
}
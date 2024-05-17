package net.cltech.enterprisent.domain.masters.billing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el filtro para obtener los contratos que participan al momento de facturar a un cliente
 * 
 * @version 1.0.0
 * @author javila
 * @since 06/07/2021
 * @see Creación
 */

@ApiObject(
        group = "Facturacion",
        name = "Filtro De Contratos De Un Cliente",
        description = "Representa el filtro para obtener los contratos que participan al momento de facturar a un cliente"
)
@Getter
@Setter
@NoArgsConstructor
public class CustomerContractFilter
{
    @ApiObjectField(name = "customerId", description = "Id del cliente", required = true, order = 1)
    private Integer customerId;
    @ApiObjectField(name = "branchId", description = "Id de la sede", required = true, order = 2)
    private Integer branchId;
    @ApiObjectField(name = "startDate", description = "Fecha inicio (yyyyMMdd)", required = true, order = 3)
    private Integer startDate;
    @ApiObjectField(name = "endDate", description = "Fecha final (yyyyMMdd)", required = true, order = 4)
    private Integer endDate;
}

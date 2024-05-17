/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.reports.DeliveryOrder;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(
        group = "Trazabilidad",
        name = "Trazabilidad de la Muestra",
        description = "Muestra la informacion de toda la trabilidad de una prueba"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class TestInformationTracking {
    @ApiObjectField(name = "auditOperation", description = "listado de toda la auditoria de ña prueba", order = 1)
    private List<AuditOperation> auditOperation;
    @ApiObjectField(name = "deliveryTypes", description = "Tipos de entregas", required = false, order = 27)
    private List<DeliveryOrder> deliveryTypes = new ArrayList<>();
}

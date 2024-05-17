package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa el detalle ya recalculado
*
* @version 1.0.0
* @author Julian
* @since 28/04/2021
* @see Creación
*/

@ApiObject(
        group = "Operación - Facturación",
        name = "Detalle Recalculado",
        description = "Representa el detalle de las ordenes y examenes que fueron recalculadas"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class RecalculatedDetail 
{
    @ApiObjectField(name = "customerName", description = "Nombre del cliente", required = true, order = 1)
    private String customerName;
    @ApiObjectField(name = "rateName", description = "Nombre de la tarifa (Sucursal)", required = true, order = 2)
    private String rateName;
    @ApiObjectField(name = "details", description = "Detalle ordenes y examenes recalculados", required = true, order = 3)
    private List<DetailToRecalculate> details;
}
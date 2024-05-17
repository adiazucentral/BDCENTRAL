package net.cltech.enterprisent.domain.integration.his;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Agregar una descripción de la clase
 *
 * @version 1.0.0
 * @author hpoveda
 * @since 04/02/2022
 * @see Creacion
 */
@ApiObject(
        group = "Integración",
        name = "HIS",
        description = "Filtro para consulta de resultados para interfaces de resultados HIS"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultsQueryFilter
{

   
    @ApiObjectField(name = "centralSystemId", description = "Id del Sistema HIS", required = true, order = 3)
    private int centralSystemId;
    @ApiObjectField(name = "days", description = "Días de consulta del actual hacia atrás", required = true, order = 4)
    private int days;
}

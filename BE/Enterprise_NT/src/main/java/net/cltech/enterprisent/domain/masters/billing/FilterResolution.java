package net.cltech.enterprisent.domain.masters.billing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el filtro que se usara para obtener un tipo de resolución 4505
 * 
 * @version 1.0.0
 * @author javila
 * @since 19/08/2021
 * @see Creación
 */

@ApiObject(
        name = "Filtro De Resolución",
        group = "Facturacion",
        description = "Representa el filtro que se usara para obtener un tipo de resolución 4505"
)
@Getter
@Setter
@NoArgsConstructor
public class FilterResolution
{

    @ApiObjectField(name = "startDate", description = "Fecha inicio (yyyyMMdd)", required = true, order = 3)
    private Integer startDate;
    @ApiObjectField(name = "endDate", description = "Fecha final (yyyyMMdd)", required = true, order = 4)
    private Integer endDate;
    @ApiObjectField(name = "idTests", description = "Id de los examenes que aplicarán", required = true, order = 3)
    private List<Integer> idTests = new ArrayList<>();
    @ApiObjectField(name = "demographics", description = "Lista de demográficos", required = true, order = 4)
    private List<FilterDemographic> demographics = new ArrayList<>();
    @ApiObjectField(name = "groupProfiles", description = "Agrupar por perfiles", order = 5)
    private boolean groupProfiles;
}

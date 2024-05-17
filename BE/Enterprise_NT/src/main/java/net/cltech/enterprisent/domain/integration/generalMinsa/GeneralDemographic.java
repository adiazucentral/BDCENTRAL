package net.cltech.enterprisent.domain.integration.generalMinsa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un demográfico general
 *
 * @version 1.0.0
 * @author @author hpoveda
 * @see Creación
 */
@ApiObject(
        group = "Integración (General)",
        name = "Demográfico General",
        description = "Representa un demográfico general"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralDemographic
{

    @ApiObjectField(name = "id", description = "Id del demográfico (Codigo del mismo)", required = true, order = 1)
    private String id;
    @ApiObjectField(name = "value", description = "Valor del demográfico", required = true, order = 2)
    private String value;
    @ApiObjectField(name = "demographic", description = "Nombre del demográfico", required = true, order = 3)
    private String demographic;
}

package net.cltech.enterprisent.domain.masters.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Resolución
 *
 * @version 1.0.0
 * @author cmartin
 * @since 02/05/2018
 * @see Creación
 */
@ApiObject(
        group = "Facturación",
        name = "Resolución",
        description = "Muestra informacion del maestro Resolución que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class Resolution extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Id de la resolución", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "resolutionDIAN", description = "Numero de la resolución DIAN", required = true, order = 2)
    private String resolutionDIAN;
    @ApiObjectField(name = "fromNumber", description = "Numero Desde", required = true, order = 3)
    private Integer fromNumber;
    @ApiObjectField(name = "toNumber", description = "Numero Hasta", required = true, order = 4)
    private Integer toNumber;
    @ApiObjectField(name = "prefix", description = "Prefijo", required = true, order = 5)
    private String prefix;
    @ApiObjectField(name = "initialNumber", description = "Numero Inicial", required = true, order = 6)
    private Integer initialNumber;
    @ApiObjectField(name = "provider", description = "Empresa", required = true, order = 7)
    private Provider provider = new Provider();
    @ApiObjectField(name = "state", description = "Estado de la resolución", required = true, order = 8)
    private boolean state;
}

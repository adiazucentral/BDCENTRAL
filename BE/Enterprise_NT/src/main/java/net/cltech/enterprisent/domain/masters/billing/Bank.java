package net.cltech.enterprisent.domain.masters.billing;

import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Bancos
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/06/2017
 * @see Creación
 */
@ApiObject(
        group = "Facturacion",
        name = "Banco",
        description = "Muestra informacion del maestro Banco que usa el API"
)
@Getter
@Setter
public class Bank extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Id del banco", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre del banco", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "state", description = "Estado del banco", required = true, order = 3)
    private boolean state;
}

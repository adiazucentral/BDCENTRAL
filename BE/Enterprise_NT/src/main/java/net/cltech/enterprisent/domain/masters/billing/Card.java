package net.cltech.enterprisent.domain.masters.billing;

import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Tarjeta de Credito
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/06/2017
 * @see Creación
 */
@ApiObject(
        group = "Facturacion",
        name = "Tarjeta de Credito",
        description = "Muestra informacion del maestro Tarjeta de Credito que usa el API"
)
@Getter
@Setter
public class Card extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Id de la tarjeta de credito", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de la tarjeta de credito", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "state", description = "Estado de la tarjeta de credito", required = true, order = 3)
    private boolean state;
}

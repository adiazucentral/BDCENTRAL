package net.cltech.enterprisent.domain.operation.tracking;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el filtro que usaremos para las auditorias
 * 
 * @version 1.0.0
 * @author javila
 * @since 08/09/2021
 * @see Creación
 */

@ApiObject(
        name = "Filtro Para Auditorias",
        group = "Trazabilidad",
        description = "Representa el filtro que usaremos para las auditorias"
)
@Getter
@Setter
@NoArgsConstructor
public class AuditFilter
{
    @ApiObjectField(name = "initDate", description = "Fecha inicial (yyyyMMdd)", order = 1)
    private String initDate;
    @ApiObjectField(name = "endDate", description = "Fecha final (yyyyMMdd)", order = 2)
    private String endDate;
    @ApiObjectField(name = "module", description = "Modulo", required = false, order = 3)
    private String module;
    @ApiObjectField(name = "user", description = "Usuario", required = false, order = 4)
    private Integer user;
}

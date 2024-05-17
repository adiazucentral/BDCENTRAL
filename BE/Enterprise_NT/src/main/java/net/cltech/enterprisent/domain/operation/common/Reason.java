package net.cltech.enterprisent.domain.operation.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.domain.operation.orders.Test;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con la información necesaria para rechazar, retomar o
 * realizar borrados especiales.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 08/09/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Comunes",
        name = "Razón",
        description = "Representa los diferentes motivos de la aplicación. "
)
@Getter
@Setter
public class Reason
{

    @ApiObjectField(name = "deleteType", description = "Tipo de Borrado: 1 -> Ordenes, 2 -> Resultados, 3 -> Todos los Examenes, 4 -> Examenes Pendientes de Resultado, 5 -> Examenes Validados", order = 1)
    private Integer deleteType;
    @ApiObjectField(name = "orderNumber", description = "Numero de orden", order = 2)
    private Long orderNumber;
    @ApiObjectField(name = "init", description = "Rango inicial", order = 2)
    private Long init;
    @ApiObjectField(name = "end", description = "Rango final", order = 3)
    private Long end;
    @ApiObjectField(name = "motive", description = "Motivo", order = 4)
    private Motive motive = new Motive();
    @ApiObjectField(name = "comment", description = "Comentario", order = 5)
    private String comment;
    @ApiObjectField(name = "tests", description = "Examenes", order = 6)
    private List<Test> tests = new ArrayList<>();
    @ApiObjectField(name = "testsList", description = "lista de examenes en formato lista de enteros", order = 6)
    private List<Integer> testsList = new ArrayList<>();
    @ApiObjectField(name = "deleteDate", description = "Fecha de Eliminación", required = true, order = 7)
    private Date deleteDate;
    @ApiObjectField(name = "user", description = "Usuario", required = false, order = 8)
    private AuthorizedUser user = new AuthorizedUser();

    /**
     * "Tipo de Borrado: 1 -> Ordenes, 2 -> Resultados, 3 -> Todos los Examenes,
     * 4 -> Examenes Pendientes de Resultado, 5 -> Examenes Validados
     *
     * @return
     */

}

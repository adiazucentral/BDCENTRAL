package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.Item;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el tipo de entrega de resultados.
 *
 * @version 1.0.0
 * @author Jrodriguez
 * @since 29/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Tipo de entrega",
        description = "Representa el tipo de entrega de los resultados"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Deliverytype
{
    @ApiObjectField(name = "id", description = "id de la tabla ", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "order", description = "Numero de la orden", required = true, order = 2)
    private long order;
    @ApiObjectField(name = "test", description = "id del examen", required = true, order = 3)
    private int test;
    @ApiObjectField(name = "code", description = "Codigo de la prueba", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "abbr", description = "Abreviatura de la prueba", required = true, order = 3)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre de la prueba", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "deliverytype", description = "Lista de tipos de entrega", required = true, order = 4)
    private Item deliverytype;
    @ApiObjectField(name = "updateUser", description = "Id del usuario que actualiza", required = false, order = 5)
    private Long updateUser;
    @ApiObjectField(name = "updateDate", description = "Fecha Actualiza", required = false, order = 6)
    private Date updateDate;
    @ApiObjectField(name = "userFullName", description = "Nombre completo del usuario", required = false, order = 7)
    private String userFullName;
    @ApiObjectField(name = "userName", description = "Nick name del usuario", required = false, order = 8)
    private String userName;
}

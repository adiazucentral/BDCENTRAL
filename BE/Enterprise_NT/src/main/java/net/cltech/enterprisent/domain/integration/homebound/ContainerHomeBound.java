package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un container desde Home Bound
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 12/06/2020
 * @see Creación
 */
@ApiObject(
        group = "Homebound",
        name = "Recipiente",
        description = "Datos de un recipiente"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class ContainerHomeBound
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "photoBase64", description = "Foto del recipiente", required = true, order = 3)
    private String photoBase64;
    @ApiObjectField(name = "image", description = "Imagen del resipiente", order = 4)
    private String image;
}

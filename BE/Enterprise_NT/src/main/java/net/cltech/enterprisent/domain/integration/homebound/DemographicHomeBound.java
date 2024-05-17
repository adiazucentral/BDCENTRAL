package net.cltech.enterprisent.domain.integration.homebound;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el demografico para Homebound
 * 
 * @version 1.0.0
 * @author javila
 * @since 27/07/2021
 * @see Creación
 */

@ApiObject(
        group = "Homebound",
        name = "Demográfico Para Homebound",
        description = "Representa el demografico para Homebound"
)
@Getter
@Setter
@NoArgsConstructor
public class DemographicHomeBound
{
    @ApiObjectField(name = "id", description = "Identificador del demografico", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "item", description = "Id del item para demograficos codificados", required = false, order = 2)
    private Integer item;
    @ApiObjectField(name = "value", description = "Valor del demografico para demografico abierto", required = false, order = 3)
    private String value;
    @ApiObjectField(name = "code", description = "Código del item", required = false, order = 4)
    private String code;
    @ApiObjectField(name = "source", description = "Origen del demografico: H-Historia, O-Orden", required = false, order = 5)
    private String source;
    @ApiObjectField(name = "name", description = "Nombre del demográfico", required = false, order = 6)
    private String name;
    @ApiObjectField(name = "defaultValue", description = "Valor por defecto", required = false, order = 7)
    private String defaultValue;
    @ApiObjectField(name = "type", description = "Tipo del demografico: 0-No Codificado, 1-Codificado", required = false, order = 8)
    private Integer type;
    @ApiObjectField(name = "format", description = "Formato del demográfico", required = false, order = 9)
    private String format;
    @ApiObjectField(name = "coded", description = "Codificado: False - No Codificado, True - Codificado", required = false, order = 10)
    private boolean coded;
}

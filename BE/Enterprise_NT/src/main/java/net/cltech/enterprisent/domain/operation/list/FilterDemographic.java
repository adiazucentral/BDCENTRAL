package net.cltech.enterprisent.domain.operation.list;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para busquedas por demografico
 *
 * @version 1.0.0
 * @author cmartin
 * @since 02/10/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Listados",
        name = "Filtro Busquedas - Demograficos",
        description = "Representa filtro con parametros para busquedas por demografico."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class FilterDemographic
{

    @ApiObjectField(name = "demographic", description = "Id demográfico : 0 - No filtrar", order = 1)
    private Integer demographic;
    @ApiObjectField(name = "demographicItems", description = "id´s Demograficos a filtrar", order = 2)
    private List<Integer> demographicItems = new ArrayList<>();
    @ApiObjectField(name = "value", description = "Valor del filtro (no codificados)", order = 3)
    private String value;
    @ApiObjectField(name = "origin", description = "Origen", required = true, order = 4)
    private String origin;
    @ApiObjectField(name = "encoded", description = "Tipo del demografico", required = true, order = 5)
    private boolean encoded;
    @ApiObjectField(name = "name", description = "Nombre del demografico", required = true, order = 6)
    private String name;
    @ApiObjectField(name = "operator", description = "Operador cuando se hace el filtro por edad", required = false, order = 7)
    private String operator;
    @ApiObjectField(name = "unidAge", description = "Unidad cuando se hace filtro por edad 1 -> años, 2 -> meses, 3 -> dias ", required = false, order = 8)
    private String unidAge;
    @ApiObjectField(name = "initDate", description = "Fecha inicial", required = false, order = 8)
    private String initDate;
    @ApiObjectField(name = "endDate", description = "Fecha final", required = false, order = 8)
    private String endDate;
    @ApiObjectField(name = "format", description = "Formato", required = false, order = 8)
    private String format;
    @ApiObjectField(name = "promiseTime", description = "Hora prometida", required = true, order = 25)
    private boolean promiseTime;
    

    public FilterDemographic(Integer demographic, List<Integer> demographicItems)
    {
        this.demographic = demographic;
        this.demographicItems = demographicItems;
    }

    public FilterDemographic(Integer demographic, String value)
    {
        this.demographic = demographic;
        this.value = value;
    }
}

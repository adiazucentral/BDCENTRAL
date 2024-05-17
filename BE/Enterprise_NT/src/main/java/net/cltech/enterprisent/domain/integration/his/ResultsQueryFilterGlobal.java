/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.his;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Agregar una descripción de la clase
 *
 * @version 1.0.0
 * @author hpoveda
 * @since 04/02/2022
 * @see Creacion
 */
@ApiObject(
        group = "Integración",
        name = "HIS",
        description = "Filtro para consulta de resultados para interfaces de resultados HIS"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultsQueryFilterGlobal extends ResultsQueryFilter
{

    @ApiObjectField(name = "orderI", description = "Order inicial", required = true, order = 1)
    private Long orderI;
    @ApiObjectField(name = "orderF", description = "order Final", required = true, order = 2)
    private Long orderF;
    @ApiObjectField(name = "dateI", description = "Fecha Inicial", required = true, order = 3)
    private String dateI;
    @ApiObjectField(name = "dateF", description = "Fecha Final", required = true, order = 4)
    private String dateF;
    @ApiObjectField(name = "type", description = "Tipo -2:fecha,1:ordenes", required = true, order = 5)
    private long type;
    @ApiObjectField(name = "origin", description = "demografico paciente :1,demografico orden :2", required = true, order = 6)
    private String origin;
    @ApiObjectField(name = "demographicId", description = "Id demographic", required = true, order = 7)
    private String demographicId;
    @ApiObjectField(name = "demographicItemsId", description = "code demographic ", required = true, order = 8)
    private String demographicItemsId;

}

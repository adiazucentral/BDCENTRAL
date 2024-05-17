/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.appointment;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(
        group = "Configuracion",
        name = "Jornada",
        description = "Jornada de ruteros"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class Shift extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "init", description = "Hora inicio de jornada hhmm", required = true, order = 3)
    private Integer init;
    @ApiObjectField(name = "end", description = "Hora fin de jornada hhmm", required = true, order = 3)
    private Integer end;
    @ApiObjectField(name = "days", description = "Días de la jornada(1-Lunes ... 7-Domingo)", required = true, order = 3)
    private List<Integer> days;
    @ApiObjectField(name = "state", description = "Si se encuentra activa", required = true, order = 4)
    private boolean state;
    @ApiObjectField(name = "selected", description = "Seleccionado", required = true, order = 5)
    private boolean selected;
    @ApiObjectField(name = "quantity", description = "Indica la cantidad de servicios que puede atender el rutero en la jornada", required = true, order = 6)
    private Integer quantity;
    @ApiObjectField(name = "amount", description = "Indica la cantidad de servicios que tiene asignado el rutero en la jornada por dia", required = true, order = 7)
    private Integer amount;
    @ApiObjectField(name = "branch", description = "Id usuario asociado a la jornada", required = true, order = 8)
    private Integer branch;

}
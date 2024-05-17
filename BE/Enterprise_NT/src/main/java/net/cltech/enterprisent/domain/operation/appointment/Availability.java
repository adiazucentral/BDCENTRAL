/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.appointment;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.appointment.Shift;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(
        group = "Operación",
        name = "Disponibilidad",
        description = "Representa la disponibilidad de flebotomistas para atender servicios"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Availability
{
    @ApiObjectField(name = "date", description = "Fecha Agendamiento", required = true, order = 1)
    private Integer date;
    @ApiObjectField(name = "quantity", description = "Indica la cantidad de servicios que se pueden atender", required = true, order = 4)
    private Integer quantity;
    @ApiObjectField(name = "amount", description = "Indica la cantidad de servicios que se han asignado", required = true, order = 4)
    private Integer amount;
    @ApiObjectField(name = "shifts", description = "Jornadas asociadas a el rutero", required = true, order = 11)
    private List<Shift> shifts;
}

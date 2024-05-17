/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.appointment;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.appointment.Shift;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(
        group = "Operacion",
        name = "Cita",
        description = "Datos de una cita"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Appointment 
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "date", description = "Fecha de registro", required = true, order = 2)
    private Integer date;
    @ApiObjectField(name = "shift", description = "Jornada", required = true, order = 5)
    private Shift shift;
    @ApiObjectField(name = "orderNumber", description = "Numero de Orden", required = true, order = 18)
    private Long orderNumber;
    @ApiObjectField(name = "state", description = "Estado Actual", required = true, order = 20)
    private Integer state;
    @ApiObjectField(name = "branch", description = "Sede a la que pertenece el rutero", required = true, order = 23)
    private Branch branch = new Branch();
    @ApiObjectField(name = "reason", description = "Motivo de la cancelación o el rechazo", required = true, order = 24)
    private Motive reason;
    @ApiObjectField(name = "creationTransaction", description = "Fecha de la creación", required = true, order = 33)
    private Date creationTransaction;
    @ApiObjectField(name = "userCreation", description = "Usuario que crea la cita", required = true, order = 34)
    private AuthorizedUser userCreation;
    @ApiObjectField(name = "idConcurrence", description = "id de la concurrencia", required = true, order = 39)
    private Integer idConcurrence;
 
}

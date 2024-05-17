/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.enterprisent.domain.masters.common.PathologyAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Clase que representa la informacion de la agenda de los patologos
*
* @version 1.0.0
* @author omendez
* @since 22/04/2021
* @see Creación
*/
@ApiObject(
        group = "Patología",
        name = "Agenda",
        description = "Muestra informacion de la agenda de los patólogos"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Schedule extends PathologyAudit
{
    @ApiObjectField(name = "id", description = "Id de la agenda", order = 1)
    private Integer id;
    @ApiObjectField(name = "pathologist", description = "Patólogo", required = true, order = 2)
    private Integer pathologist;
    @ApiObjectField(name = "init", description = "Fecha y hora inicial", required = true, order = 3)
    private Date init;
    @ApiObjectField(name = "end", description = "Fecha y hora final", required = true, order = 4)
    private Date end;
    @ApiObjectField(name = "status", description = "Estado", required = true, order = 5)
    private Integer status;
    @ApiObjectField(name = "event", description = "Id del evento", order = 6)
    private EventPathology event = new EventPathology();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPathologist() {
        return pathologist;
    }

    public void setPathologist(Integer pathologist) {
        this.pathologist = pathologist;
    }

    public Date getInit() {
        return init;
    }

    public void setInit(Date init) {
        this.init = init;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public EventPathology getEvent() {
        return event;
    }

    public void setEvent(EventPathology event) {
        this.event = event;
    }
}

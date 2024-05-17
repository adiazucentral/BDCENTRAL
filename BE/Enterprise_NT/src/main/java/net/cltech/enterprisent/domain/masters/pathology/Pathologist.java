/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.PathologyAudit;
import net.cltech.enterprisent.domain.masters.user.User;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Clase que representa la informacion de los patologos
*
* @version 1.0.0
* @author omendez
* @since 16/04/2021
* @see Creación
*/
@ApiObject(
        group = "Patología",
        name = "Patólogo",
        description = "Muestra informacion de los patólogos"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pathologist extends PathologyAudit
{
    @ApiObjectField(name = "id", description = "Id del especimen", order = 1)
    private Integer id;
    @ApiObjectField(name = "pathologist", description = "Patólogo", required = true, order = 2)
    private User pathologist;
    @ApiObjectField(name = "organs", description = "Organos", required = true, order = 3)
    private List<Organ> organs = new ArrayList<>();
    @ApiObjectField(name = "schedule", description = "Agenda", required = true, order = 4)
    private List<Schedule> schedule = new ArrayList<>();
    @ApiObjectField(name = "quantity", description = "Cantidad de casos asignados", order = 5)
    private Integer quantity;

    public Pathologist() {
    }
    
    public Pathologist(Integer id)
    {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getPathologist() {
        return pathologist;
    }

    public void setPathologist(User pathologist) {
        this.pathologist = pathologist;
    }

    public List<Organ> getOrgans() {
        return organs;
    }

    public void setOrgans(List<Organ> organs) {
        this.organs = organs;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

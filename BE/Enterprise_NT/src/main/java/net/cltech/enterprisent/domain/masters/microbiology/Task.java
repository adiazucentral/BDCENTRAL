/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.microbiology;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Tareas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 09/06/2017
 * @see Creación
 */
@ApiObject(
        group = "Microbiología",
        name = "Tareas",
        description = "Muestra informacion del maestro Tareas que usa el API"
)
public class Task extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id de la tarea", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "description", description = "Descripción de la tarea", required = true, order = 2)
    private String description;
    @ApiObjectField(name = "state", description = "Estado de la tarea", required = true, order = 3)
    private boolean state;

    public Task()
    {

    }

    public Task(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

}

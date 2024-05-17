/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.microbiology;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyTask;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un medio de cultivo
 *
 * @version 1.0.0
 * @author enavas
 * @since 10/08/2017
 * @see Creacion
 */
@ApiObject(
        group = "Prueba",
        name = "Medio de cultivo",
        description = "Representa un medio de cultivo"
)
public class MediaCulture extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id del medio de cultivo", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo del medio de cultivo", required = false, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre del medio de cultivo", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "state", description = "Si esta activo", required = true, order = 4)
    private boolean state;
    /* Aplica para la relacion por prueba */
    @ApiObjectField(name = "defectValue", description = "Indica si se selecciona automáticamente en la siembra de microbiologia", required = true, order = 5)
    private boolean defectValue;
    @ApiObjectField(name = "select", description = "Esta asignado al prueba", required = true, order = 6)
    private boolean select;
    @ApiObjectField(name = "tasks", description = "Tareas asociadas al medio de cultivo", required = true, order = 10)
    private List<MicrobiologyTask> tasks = new ArrayList<>();

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public boolean isDefectValue()
    {
        return defectValue;
    }

    public void setDefectValue(boolean defectValue)
    {
        this.defectValue = defectValue;
    }

    public boolean isSelect()
    {
        return select;
    }

    public void setSelect(boolean select)
    {
        this.select = select;
    }

    public List<MicrobiologyTask> getTasks()
    {
        return tasks;
    }

    public void setTasks(List<MicrobiologyTask> tasks)
    {
        this.tasks = tasks;
    }

}

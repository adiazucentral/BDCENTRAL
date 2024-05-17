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
 * Clase que representa la informacion del maestro Procedimientos
 *
 * @version 1.0.0
 * @author cmartin
 * @since 11/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Microbiología",
        name = "Procedimiento",
        description = "Muestra informacion del maestro Procedimientos que usa el API"
)
public class Procedure extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id ", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo procedimiento", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre del procedimiento", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "state", description = "Estado", required = true, order = 4)
    private boolean state;
    @ApiObjectField(name = "defaultvalue", description = "Valor por Defecto", required = true, order = 5)
    private boolean defaultvalue;
    @ApiObjectField(name = "confirmatorytest", description = "Id Prueba Confirmatoria", required = true, order = 6)
    private Integer confirmatorytest;
    @ApiObjectField(name = "confirmatorytestname", description = "Nombre Prueba Confirmatoria", required = true, order = 7)
    private String confirmatorytestname;
    @ApiObjectField(name = "confirmatorytestcode", description = "Codigo  Prueba Confirmatoria", required = true, order = 8)
    private String confirmatorytestcode;
    @ApiObjectField(name = "selected", description = "Esta seleccionado", required = true, order = 9)
    private boolean selected;
    @ApiObjectField(name = "tasks", description = "Tareas asociadas al procedimientos", required = true, order = 10)
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

    public boolean isDefaultvalue()
    {
        return defaultvalue;
    }

    public void setDefaultvalue(boolean defaultvalue)
    {
        this.defaultvalue = defaultvalue;
    }

    public Integer getConfirmatorytest()
    {
        return confirmatorytest;
    }

    public void setConfirmatorytest(Integer confirmatorytest)
    {
        this.confirmatorytest = confirmatorytest;
    }

    public String getConfirmatorytestname()
    {
        return confirmatorytestname;
    }

    public void setConfirmatorytestname(String confirmatorytestname)
    {
        this.confirmatorytestname = confirmatorytestname;
    }

    public String getConfirmatorytestcode()
    {
        return confirmatorytestcode;
    }

    public void setConfirmatorytestcode(String confirmatorytestcode)
    {
        this.confirmatorytestcode = confirmatorytestcode;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
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

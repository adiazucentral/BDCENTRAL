/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import java.util.Date;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una vigencia
 *
 * @version 1.0.0
 * @author enavas
 * @since 09/08/2017
 * @see Creacion
 */
@ApiObject(
        group = "Prueba",
        name = "Vigencia",
        description = "Representa una vigencia"
)

public class FeeSchedule extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id de la vigencia", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de la vigencia", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "initialDate", description = "Fecha inicial", required = true, order = 3)
    private Date initialDate;
    @ApiObjectField(name = "endDate", description = "Fecha final", required = true, order = 4)
    private Date endDate;
    @ApiObjectField(name = "automatically", description = "Aplicar automaticamente 0- No aplica , 1- Aplica", required = true, order = 5)
    private boolean automatically;
    @ApiObjectField(name = "state", description = "Si esta activo", required = true, order = 4)
    private boolean state;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getInitialDate()
    {
        return initialDate;
    }

    public void setInitialDate(Date initialDate)
    {
        this.initialDate = initialDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public boolean isAutomatically()
    {
        return automatically;
    }

    public void setAutomatically(boolean automatically)
    {
        this.automatically = automatically;
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

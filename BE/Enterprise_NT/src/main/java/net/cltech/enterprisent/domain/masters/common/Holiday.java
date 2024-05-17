/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.common;

import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Festivos
 *
 * @version 1.0.0
 * @author cmartin
 * @since 30/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Utilidades",
        name = "Festivos",
        description = "Muestra los Festivos que usa el API"
)
public class Holiday extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id del festivo", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre del festivo", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "date", description = "Fecha del festivo", required = true, order = 3)
    private Date date;
    @ApiObjectField(name = "state", description = "Estado del festivo", required = true, order = 4)
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

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Areas
 *
 * @version 1.0.0
 * @author MPerdomo
 * @since 18/04/2017
 * @see Creacion
 */
@ApiObject(
        group = "Prueba",
        name = "Tecnica",
        description = "Muestra informacion del maestro Tecnica que usa el API"
)
public class Technique extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Identificador autonumérico de la tecnica", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Código de la Tecnica", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre de la tecnica", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "state", description = "Identifica si la tecnica se encuentra activa o inactiva ", required = false, order = 6)
    private boolean state;

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
    
}

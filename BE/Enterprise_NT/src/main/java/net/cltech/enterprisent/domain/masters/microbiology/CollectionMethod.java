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
 * Clase que representa la informacion del maestro Sitio Anatomico
 *
 * @version 1.0.0
 * @author cmartin
 * @since 18/01/2018
 * @see Creación
 */
@ApiObject(
        group = "Microbiología",
        name = "Metodo de Recolección",
        description = "Muestra informacion del maestro Metodo de Recolección que usa el API"
)
public class CollectionMethod extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Id del metodo de recolección", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre del metodo de recolección", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "state", description = "Estado del sitio anatomico", required = true, order = 4)
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

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }
    
    
}

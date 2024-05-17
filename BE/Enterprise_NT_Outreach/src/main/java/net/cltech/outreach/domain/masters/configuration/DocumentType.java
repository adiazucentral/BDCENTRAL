/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.domain.masters.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Tipo de Documento
 *
 * @version 1.0.0
 * @author cmartin
 * @since 29/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Configuracion",
        name = "Tipo de Documento",
        description = "Muestra informacion del maestro Tipos de Documento que usa el API"
)
@JsonInclude(Include.NON_NULL)
public class DocumentType
{

    @ApiObjectField(name = "id", description = "Id del tipo de documento", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "abbr", description = "Abreviatura del tipo de documento", required = true, order = 2)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre del tipo de documento", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "state", description = "Estado ", required = true, order = 9)
    private boolean state;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
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

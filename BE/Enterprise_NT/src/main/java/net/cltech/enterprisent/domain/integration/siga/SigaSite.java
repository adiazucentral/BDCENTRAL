/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la ubicacion del servicio.
 *
 * @version 1.0.0
 * @author Jrodriguez
 * @since 16/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Ubicacion",
        description = "Representa el objeto de la ubicacion del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaSite
{

    @ApiObjectField(name = "id", description = "id de la ubicacion")
    private Integer id;
    @ApiObjectField(name = "code", description = "CÃ³digo de la ubicacion")
    private String code;
    @ApiObjectField(name = "name", description = "Nombre de la ubicacion")
    private String name;
    @ApiObjectField(name = "description", description = "Descripcion de la ubicacion")
    private String description;
    @ApiObjectField(name = "registerDate", description = "Fecha version registro de la ubicacion")
    private Date registerDate;
    @ApiObjectField(name = "state", description = "Estado de la ubicacion")
    private short state;
    @ApiObjectField(name = "branch", description = "Sede de la ubicaciÃ³n")
    private Branch branch;

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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getRegisterDate()
    {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate)
    {
        this.registerDate = registerDate;
    }

    public short getState()
    {
        return state;
    }

    public void setState(short state)
    {
        this.state = state;
    }

    public Branch getBranch()
    {
        return branch;
    }

    public void setBranch(Branch branch)
    {
        this.branch = branch;
    }

}

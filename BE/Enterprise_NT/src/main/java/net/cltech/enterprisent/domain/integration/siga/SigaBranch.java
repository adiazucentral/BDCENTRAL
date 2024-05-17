/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una sede para el Siga.
 *
 * @version 1.0.0
 * @author equijano
 * @since 16/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Sede",
        description = "Representa el objeto de sedes del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaBranch
{

    @ApiObjectField(name = "id", description = "Id de la sede", order = 1)
    private int id;
    @ApiObjectField(name = "code", description = "Codigo de la sede", order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre de la sede", order = 3)
    private String name;
    @ApiObjectField(name = "description", description = "Descripcion de la sede", order = 4)
    private String description;
    @ApiObjectField(name = "registerDate", description = "Fecha de registro de la sede", order = 5)
    private Date registerDate;
    @ApiObjectField(name = "state", description = "Estado de la sede", order = 6)
    private int state;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

}

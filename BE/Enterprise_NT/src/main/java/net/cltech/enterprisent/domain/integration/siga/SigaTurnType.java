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
 * Representa un tipo de turno para el Siga.
 *
 * @version 1.0.0
 * @author equijano
 * @since 19/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Tipo de turno",
        description = "Representa el tipo del turno del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaTurnType
{

    @ApiObjectField(name = "id", description = "Id del tipo de turno", order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo del tipo de turno", order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre del tipo de turno", order = 3)
    private String name;
    @ApiObjectField(name = "color", description = "Color del tipo de turno", order = 4)
    private String color;
    @ApiObjectField(name = "description", description = "Descripcion del tipo de turno", order = 5)
    private String description;
    @ApiObjectField(name = "registerDate", description = "Fecha de registro del tipo de turno", order = 6)
    private Date registerDate;
    @ApiObjectField(name = "minConsecutive", description = "Minimo consecutivo del tipo de turno", order = 7)
    private Integer minConsecutive;
    @ApiObjectField(name = "maxConsecutive", description = "Maximo consecutivo del tipo de turno", order = 8)
    private Integer maxConsecutive;
    @ApiObjectField(name = "state", description = "Estado del tipo de turno", order = 9)
    private short state;

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

    public Integer getMinConsecutive()
    {
        return minConsecutive;
    }

    public void setMinConsecutive(Integer minConsecutive)
    {
        this.minConsecutive = minConsecutive;
    }

    public Integer getMaxConsecutive()
    {
        return maxConsecutive;
    }

    public void setMaxConsecutive(Integer maxConsecutive)
    {
        this.maxConsecutive = maxConsecutive;
    }

    public short getState()
    {
        return state;
    }

    public void setState(short state)
    {
        this.state = state;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

}

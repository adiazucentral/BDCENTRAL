/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.cltech.enterprisent.domain.masters.common.PathologyAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de los casetes de Patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 07/04/2021
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Casete",
        description = "Muestra informacion del maestro de casetes de patologia que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Casete extends PathologyAudit
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Código", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "status", description = "Estado", required = true, order = 4)
    private Integer status;
    @ApiObjectField(name = "colour", description = "Color", required = true, order = 5)
    private String colour;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
    
    public Integer isState()
    {
        return status;
    }
}

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
 * Clase que representa la informacion del maestro Areas de Patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 04/08/2020
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Areas Patología",
        description = "Muestra informacion del maestro Areas de patologia que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Area extends PathologyAudit
{
    @ApiObjectField(name = "id", description = "Id del area", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Código del area", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre del area", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "status", description = "Estado del area", required = true, order = 4)
    private Integer status;

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
    
    public Integer isState()
    {
        return status;
    }

}

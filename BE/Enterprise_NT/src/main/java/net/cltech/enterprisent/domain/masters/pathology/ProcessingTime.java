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
 * Clase que representa la informacion de las horas de procesamiento de muestras de Patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 13/07/2021
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Horas Procesamiento",
        description = "Muestra informacion del maestro de las horas de procesamiento de muestras que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessingTime extends PathologyAudit
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "time", description = "Hora", required = true, order = 2)
    private String time;
    @ApiObjectField(name = "status", description = "Estado", required = true, order = 4)
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

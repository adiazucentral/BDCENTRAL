/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import net.cltech.enterprisent.domain.masters.pathology.ProcessingTime;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion del procesador de tejidos de los casetes
 *
 * @version 1.0.0
 * @author omendez
 * @since 19/07/2021
 * @see Creacion
 */
@ApiObject(
        group = "Patología",
        name = "Procesador",
        description = "Representa la informacion del procesador de tejidos de los casetes"
)
public class TissueProcessor 
{
    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "time", description = "Hora de procesamiento", required = true, order = 2)
    private ProcessingTime time = new ProcessingTime();
    @ApiObjectField(name = "hours", description = "Horas de procesamiento", required = false, order = 3)
    private Integer hours;
    @ApiObjectField(name = "casete", description = "Id del casete", required = false, order = 4)
    private Integer casete;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProcessingTime getTime() {
        return time;
    }

    public void setTime(ProcessingTime time) {
        this.time = time;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getCasete() {
        return casete;
    }

    public void setCasete(Integer casete) {
        this.casete = casete;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.migracionIngreso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author hpoveda
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "status de migration",
        description = "Representa una orden de laboratorio en migration"
)
@Data

public class StatusServer
{

    @ApiObjectField(name = "status", description = "status", required = false, order = 1)
    private boolean status;
    @ApiObjectField(name = "description", description = "descriptions", required = false, order = 2)
    private String description;

    public StatusServer()
    {
        this.status = true;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        this.description = "STATUS IN " + dtf.format(LocalDateTime.now()) + " IS ACTIVE ";

    }

}

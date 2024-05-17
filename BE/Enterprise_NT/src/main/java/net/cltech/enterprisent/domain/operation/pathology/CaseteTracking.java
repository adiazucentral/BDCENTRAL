/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de la trazabilidad de los casetes
 *
 * @version 1.0.0
 * @author omendez
 * @since 26/07/2021
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Casete - Tracking",
        description = "Muestra informacion de la trazabilidad de los casetes"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseteTracking 
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "casete", description = "Casete", required = true, order = 2)
    private Integer casete;
    @ApiObjectField(name = "status", description = "Estado", required = true, order = 3)
    private Integer status;
    @ApiObjectField(name = "causer", description = "Usuario que registra la operación", required = true, order = 4)
    private AuthorizedUser causer;
    @ApiObjectField(name = "date", description = "Fecha", required = true, order = 5)
    private Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCasete() {
        return casete;
    }

    public void setCasete(Integer casete) {
        this.casete = casete;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public AuthorizedUser getCauser() {
        return causer;
    }

    public void setCauser(AuthorizedUser causer) {
        this.causer = causer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

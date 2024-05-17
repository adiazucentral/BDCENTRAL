/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.middleware;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un servicio para el Middleware.
 *
 * @version 1.0.0
 * @author equijano
 * @since 01/11/2018
 * @see Creación
 */
@ApiObject(
        group = "Middleware",
        name = "Url del laboratorio",
        description = "Representa el objeto de la url del middleware"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MiddlewareUrl {

    @ApiObjectField(name = "url", description = "url del middleware", order = 1)
    private String url;
    @ApiObjectField(name = "idLaboratory", description = "id del laboratorio", order = 2)
    private int idLaboratory;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIdLaboratory() {
        return idLaboratory;
    }

    public void setIdLaboratory(int idLaboratory) {
        this.idLaboratory = idLaboratory;
    }

}

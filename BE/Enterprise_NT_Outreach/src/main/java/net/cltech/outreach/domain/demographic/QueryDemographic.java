/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.domain.demographic;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la información del demografico de la consula web
 *
 * @version 1.0.0
 * @author omendez
 * @since 18/11/2022
 * @see Creacion
 */
@ApiObject(
        group = "Demograficos",
        name = "Demografico Consulta Web",
        description = "Representa la información del demografico de la consula web"
)
public class QueryDemographic {
    
    @ApiObjectField(name = "id", description = "id del demografico", order = 1)
    private int id;
    @ApiObjectField(name = "idItem", description = "Id Item demografico", order = 2)
    private Integer idItem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }
}

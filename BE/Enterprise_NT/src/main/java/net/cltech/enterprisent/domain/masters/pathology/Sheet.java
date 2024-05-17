/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de las laminas de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 08/04/2021
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Lamina",
        description = "Muestra información de las laminas de patologia que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sheet 
{
    @ApiObjectField(name = "id", description = "Id de la lamina", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "coloration", description = "Coloración", required = true, order = 2)
    private Coloration coloration = new Coloration();
    @ApiObjectField(name = "quantity", description = "Cantidad de laminas", required = true, order = 3)
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Coloration getColoration() {
        return coloration;
    }

    public void setColoration(Coloration coloration) {
        this.coloration = coloration;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

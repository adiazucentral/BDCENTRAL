/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de los datos para las graficas utilizadas en patologia.
 *
 * @version 1.0.0
 * @author omendez
 * @since 22/07/2021
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Widget",
        description = "Representa la informacion de los datos para las graficas utilizadas en patologia"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WidgetPathology {
    
    @ApiObjectField(name = "label", description = "Etiqueta", required = false, order = 1)
    private String label;
    @ApiObjectField(name = "value", description = "Valores", required = false, order = 2)
    private Integer value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}

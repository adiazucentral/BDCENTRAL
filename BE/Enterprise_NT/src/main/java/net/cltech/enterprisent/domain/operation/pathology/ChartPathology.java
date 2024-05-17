/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
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
        name = "Graficas",
        description = "Representa la informacion de los datos para las graficas utilizadas en patologia"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartPathology 
{
    @ApiObjectField(name = "key", description = "Llave", required = false, order = 1)
    private String key;
    @ApiObjectField(name = "data", description = "Datos", required = false, order = 2)
    private List<WidgetPathology> data = new ArrayList<>();;
    @ApiObjectField(name = "value", description = "Valor", required = false, order = 3)
    private Integer value;
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<WidgetPathology> getData() {
        return data;
    }

    public void setData(List<WidgetPathology> data) {
        this.data = data;
    }
    
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}

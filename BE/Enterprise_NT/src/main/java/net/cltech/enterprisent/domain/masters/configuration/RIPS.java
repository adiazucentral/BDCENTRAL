/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un objeto de configuracion rips
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/01/2021
 * @see Creacion
 */
@ApiObject(
        group = "Configuracion",
        name = "RIPS",
        description = "Representa una llave de configuracion rips"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RIPS 
{
    
    @ApiObjectField(name = "key", description = "Llave de configuracion", required = true, order = 1)
    private String key;
    @ApiObjectField(name = "value", description = "Valor de configuracion", required = true, order = 2)
    private String value;
    @ApiObjectField(name = "type", description = "Identifica si es valor fijo o demografico", required = true, order = 3)
    private Integer type;
    @ApiObjectField(name = "fixedValue", description = "Texto fijo configurable", required = true, order = 4)
    private String fixedValue;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(String fixedValue) {
        this.fixedValue = fixedValue;
    }
}

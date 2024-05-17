/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.Item;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(
        group = "Prueba",
        name = "Area",
        description = "Completa la informacion basica del area. "
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Area extends SuperArea{
    @ApiObjectField(name = "ordering", description = "Ordenamiento del area", required = true, order = 2)
    private Short ordering;
    @ApiObjectField(name = "color", description = "Color del area", required = true, order = 5)
    private String color;
    @ApiObjectField(name = "type", description = "Tipo del area", required = true, order = 6)
    private Item type;
    @ApiObjectField(name = "state", description = "Estado del area", required = true, order = 9)
    private boolean state;
    @ApiObjectField(name = "partialValidation", description = "Validacion parcial", required = true, order = 10)
    private boolean partialValidation;
    
    public Area(Integer id)
    {
        super(id);
    } 
    
}
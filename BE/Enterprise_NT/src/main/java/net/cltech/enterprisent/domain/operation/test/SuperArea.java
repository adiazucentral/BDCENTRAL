/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del basica de areas para el modulo de operacion
 *
 * @version 1.0.0
 * @author adiaz
 * @since 29/11/2021
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Area",
        description = "Muestra informacion del basica de areas para el modulo de operacion"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class SuperArea {
    @ApiObjectField(name = "id", description = "Id del area", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "abbreviation", description = "Abreviatura del area", required = true, order = 2)
    private String abbreviation;
    @ApiObjectField(name = "name", description = "Nombre del area", required = true, order = 3)
    private String name;
    
    public SuperArea() {}
    
    public SuperArea(Integer id)
    {
        this.id = id;
    }

    
}



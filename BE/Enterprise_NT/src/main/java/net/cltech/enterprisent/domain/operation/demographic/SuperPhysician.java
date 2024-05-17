/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObjectField;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class SuperPhysician {
    @ApiObjectField(name = "id", description = "Id de base de datos", order = 1)
    private Integer id;
    @ApiObjectField(name = "identification", description = "Número de Identificación", order = 2)
    private String identification;
    @ApiObjectField(name = "name", description = "Nombre ", order = 3)
    private String name;
    @ApiObjectField(name = "lastName", description = "Apellido ", order = 4)
    private String lastName;
    @ApiObjectField(name = "email", description = "Correo del médico", order = 21)
    private String email;
}

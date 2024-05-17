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
public class Branch extends SuperBranch{
    @ApiObjectField(name = "abbreviation", description = "Abreviación de la sede", required = true, order = 2)
    private String abbreviation;
    @ApiObjectField(name = "responsable", description = "Responsable de la sede", required = true, order = 2)
    private String responsable;
    @ApiObjectField(name = "name", description = "Nombre de la sede", required = true, order = 4)
    private String address;
    @ApiObjectField(name = "phone", description = "Telefono de la sede", required = true, order = 6)
    private String phone;
    @ApiObjectField(name = "email", description = "Email de la sede", required = true, order = 6)
    private String email;
    @ApiObjectField(name = "minimum", description = "Minimo", required = true, order = 7)
    private Integer minimum;
    @ApiObjectField(name = "maximum", description = "Telefono de la sede", required = true, order = 8)
    private Integer maximum;
    @ApiObjectField(name = "state", description = "Estado de la sede", required = true, order = 9)
    private boolean state;
    @ApiObjectField(name = "urlConnection", description = "URL de conexión", required = true, order = 10)
    private String urlConnection;
}

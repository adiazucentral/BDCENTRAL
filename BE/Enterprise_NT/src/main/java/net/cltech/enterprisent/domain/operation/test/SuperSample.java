/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(
        group = "Prueba",
        name = "Muestra",
        description = "Representa una Muestra"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class SuperSample {
    @ApiObjectField(name = "id", description = "Id de la muestra", order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de la muestra", order = 2)
    private String name;
    @ApiObjectField(name = "codesample", description = "Codigo de la muestra", order = 10)
    private String codesample;
    @ApiObjectField(name = "takeDate", description = "Fecha de la toma", order = 19)
    private Date takeDate;
    @ApiObjectField(name = "container", description = "Id del recipiente", order = 9)
    private SuperContainer container;
}

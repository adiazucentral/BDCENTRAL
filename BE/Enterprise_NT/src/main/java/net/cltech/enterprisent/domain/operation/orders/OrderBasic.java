/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(
        group = "Operación - Ordenes",
        name = "Orden de Laboratorio para eliminacion",
        description = "Representa una orden de laboratorio de la aplicación para eliminacion"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class OrderBasic {
    @ApiObjectField(name = "orderNumber", description = "Numero de Orden", required = false, order = 1)
    private Long orderNumber;
      @ApiObjectField(name = "tests", description = "examenes", required = false, order = 19)
    private List<SuperTest> tests = new ArrayList<>();
    
}

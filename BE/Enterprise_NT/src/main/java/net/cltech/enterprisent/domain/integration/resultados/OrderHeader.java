/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.resultados;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Orden cabecera
 * 
 * @version 1.0.0
 * @author omendez
 * @since 22/02/2023
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Orden Interfaz de resultados",
        description = "Orden Interfaz de resultados"
)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class OrderHeader {
    @ApiObjectField(name = "order", description = "Order", required = true, order = 1)
    private Long order;
    @ApiObjectField(name = "tests", description = "Examenes", required = false, order = 8)
    private List<TestHeader> tests = new ArrayList<>();  
    
    public OrderHeader setTests(List<TestHeader> tests)
    {
        this.tests = tests;
        return this;
    }
    
}

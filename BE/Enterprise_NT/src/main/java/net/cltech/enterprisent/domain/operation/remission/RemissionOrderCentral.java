package net.cltech.enterprisent.domain.operation.remission;

import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObjectField;

@Getter
@Setter
public class RemissionOrderCentral {
    @ApiObjectField(name = "order", description = "Número de orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "test", description = "Id de la prueba", required = true, order = 2)
    private int test;
    @ApiObjectField(name = "testCode", description = "Codigo de la prueba", required = true, order = 3)
    private String testCode;
    @ApiObjectField(name = "testName", description = "Nombre de la prueba", required = true, order = 4)
    private String testName;
    @ApiObjectField(name = "testState", description = "estado de la prueba", required = true, order = 4)
    private int testState;
    @ApiObjectField(name = "remission", description = "si ya fue marcada como remision", required = true, order = 5)
    private int remission;
    
      
}

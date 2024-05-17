/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un examen para el registro de resultados
 *
 * @version 1.0.0
 * @author omendez
 * @since 26/08/2022
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Valores de referencia",
        description = "Representa la informacion de los valores de referencia para un examen"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ReferenceValues {
     
    @ApiObjectField(name = "order", description = "Número de orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "testId", description = "Identificador del examen", required = true, order = 2)
    private int testId;
    @ApiObjectField(name = "refMin", description = "Referencia mínima", required = false, order = 3)
    private BigDecimal refMin;
    @ApiObjectField(name = "refMax", description = "Referecia máxima", required = false, order = 4)
    private BigDecimal refMax;
    @ApiObjectField(name = "refInterval", description = "Intervalo de referencia", required = false, order = 5)
    private String refInterval;
    @ApiObjectField(name = "refLiteral", description = "Referencia normal para resultado literal", required = false, order = 6)
    private String refLiteral;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.resultados;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Filtros marcar examen interfaz resultados
 * 
 * @version 1.0.0
 * @author omendez
 * @since 22/02/2023
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Filtros marcar examen interfaz resultados",
        description = "Filtros marcar examen  interfaz resultados"
)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class DetailStatus {
    @ApiObjectField(name = "testId", description = "Id examen", required = true, order = 1)
    private Integer testId;
    @ApiObjectField(name = "orderId", description = "Orden", required = true, order = 2)
    private Long orderId;
    @ApiObjectField(name = "idSystemTest", description = "Id sistema central", required = true, order = 3)
    private Integer idSystemTest;
    @ApiObjectField(name = "status", description = "Estado", required = true, order = 4)
    private Integer status;
}

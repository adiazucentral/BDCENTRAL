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
 * Filtros detallados
 * 
 * @version 1.0.0
 * @author omendez
 * @since 22/02/2023
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Filtros detallados interfaz resultados",
        description = "Filtros detallados interfaz resultados"
)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class DetailFilter {
    
    @ApiObjectField(name = "orderLis", description = "Orden", required = true, order = 1)
    private Long orderLis;
    @ApiObjectField(name = "forwarding", description = "Tipo de envio", required = true, order = 2)
    private Integer forwarding;
    @ApiObjectField(name = "idSystemTest", description = "Id sistema central", required = true, order = 3)
    private Integer idSystemTest;
    @ApiObjectField(name = "systemNameTest", description = "Nombre sistema central", required = true, order = 4)
    private String systemNameTest;
    @ApiObjectField(name = "typeHomologation", description = "Tipo de homologación", required = true, order = 5)
    private Integer typeHomologation;
    @ApiObjectField(name = "idTest", description = "Id examen", required = true, order = 6)
    private String idTest;
    @ApiObjectField(name = "typeValidation", description = "0- examenes validados, 1 - Perfil Completo, 2 - Orden completa,3 exámenes con resultado", required = true, order = 7)
    private int typeValidation;
    
}

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
 * Filtros
 * 
 * @version 1.0.0
 * @author omendez
 * @since 17/02/2023
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Filtros interfaz resultados",
        description = "Filtros interfaz resultados"
)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class ResultHeaderFilter {
    @ApiObjectField(name = "days", description = "Días", required = true, order = 1)
    private int days;
    @ApiObjectField(name = "init", description = "Orden minima", required = true, order = 2)
    private Long init;
    @ApiObjectField(name = "end", description = "Orden maxima", required = true, order = 3)
    private Long end;
    @ApiObjectField(name = "idSystemDemographics", description = "Id sistema central demograficos", required = true, order = 4)
    private int idSystemDemographics;
    @ApiObjectField(name = "idSystemTest", description = "Id sistema central examenes", required = true, order = 5)
    private int idSystemTest;
    @ApiObjectField(name = "typeValidation", description = "0- examenes validados, 1 - Perfil Completo, 2 - Orden completa,3 exámenes con resultado", required = true, order = 6)
    private int typeValidation;
    @ApiObjectField(name = "typeEntry", description = "0 - Todos,1- Ingreso por interfaz, 2. ingreso manual", required = true, order = 7)
    private int typeEntry;
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 8)
    private List<DemoFilter> demographics = new ArrayList<>();  
}

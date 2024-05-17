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
 * Examen cabecera
 * 
 * @version 1.0.0
 * @author omendez
 * @since 17/02/2023
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Examen Interfaz de resultados",
        description = "Examen Interfaz de resultados"
)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class TestHeader {

    @ApiObjectField(name = "testId", description = "Examen", required = true, order = 2)
    private Integer testId;
    @ApiObjectField(name = "testStatus", description = "Estado de la prueba", required = true, order = 3)
    private Integer testStatus;
    @ApiObjectField(name = "profileId", description = "Id del perfil", required = true, order = 4)
    private Integer profileId;
    @ApiObjectField(name = "result", description = "Resultado", required = true, order = 5)
    private String result;
    @ApiObjectField(name = "typeTest", description = "Tipo de examen", required = true, order = 6)
    private Integer typeTest;
}

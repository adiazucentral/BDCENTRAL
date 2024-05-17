/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.results;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Agregar una descripción de la clase
 *
 * @version 1.0.0
 * @author jblanco
 * @since 25/04/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados - Filtro",
        name = "Filtro Histórico",
        description = "Representa un filtro para consulta de los resultados históricos de las pruebas"
)
@Getter
@Setter
public class HistoryFilter {

    @ApiObjectField(name = "id", description = "Identificador del paciente", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "testId", description = "Lista de pruebas que se requieren consultas", required = false, order = 1)
    private List<Integer> testId;
    @ApiObjectField(name = "iduser", description = "Lista de pruebas que se requieren consultas", required = false, order = 2)
    private Integer iduser;
    @ApiObjectField(name = "typeuser", description = "Lista de pruebas que se requieren consultas", required = false, order = 3)
    private Integer typeuser;
    

    
    

}

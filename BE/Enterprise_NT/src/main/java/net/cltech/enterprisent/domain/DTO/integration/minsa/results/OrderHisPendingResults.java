/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.minsa.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 *
 * @version 1.0.0
 * @author bbonilla
 * @since 02/05/2022
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Area",
        description = "Muestra informacion del maestro Areas que usa el API"
)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderHisPendingResults
{

    @ApiObjectField(name = "idCentralSystem", description = "Id del sistema central", required = true, order = 1)
    private int idCentralSystem;
    @ApiObjectField(name = "idTest", description = "Id del examen", required = true, order = 2)
    private int idTest;
    @ApiObjectField(name = "order", description = "Orden", required = true, order = 3)
    private long order;

    public OrderHisPendingResults()
    {
    }

}

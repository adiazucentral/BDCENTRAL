/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de la lista de medicos auxiliares de una orden
 *
 * @author omendez
 * @version 1.0.0
 * @since 09/11/2022
 * @see Creación
 */
@ApiObject(
        group = "Demografico",
        name = "Médicos Auxiliares",
        description = "Representa la lista de medicos auxiliares de una orden"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class AuxiliaryPhysicians {
    
    @ApiObjectField(name = "orderNumber", description = "Numero de Orden", required = false, order = 1)
    private Long orderNumber;
    @ApiObjectField(name = "physician", description = "Medico", required = false, order = 2)
    private Physician physician = new Physician();
}

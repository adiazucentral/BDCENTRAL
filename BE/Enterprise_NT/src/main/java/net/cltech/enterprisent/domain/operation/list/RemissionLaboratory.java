/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.list;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.operation.orders.Order;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para busquedas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 08/09/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Listados",
        name = "Listado de ordenes a remitir",
        description = "Representa objeto para guardar las remisiones de un laboratorio."
)
@Getter
@Setter
public class RemissionLaboratory
{
    @ApiObjectField(name = "laboratory", description = "Laboratorio destino de las ordenes a remitir", order = 1)
    private Integer laboratory;
    @ApiObjectField(name = "type", description = "tipo de laboratorio", order = 1)
    private Integer type;
    @ApiObjectField(name = "orders", description = "Listado de ordenes a remitir", order = 2)
    private List<Order> orders;
}

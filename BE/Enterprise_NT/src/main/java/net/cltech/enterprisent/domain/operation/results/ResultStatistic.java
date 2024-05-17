/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.results;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un objeto de información estadística para el registro de resultados
 *
 * @version 1.0.0
 * @author jblanco
 * @since 16/10/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Estadisticas Resultados",
        description = "Representa un objeto de información estadística para el módulo de registro de resultados"
)
public class ResultStatistic {

    @ApiObjectField(name = "orderCount", description = "Cantidad de órdenes", required = true, order = 1)
    private int orderCount;

    public ResultStatistic() {
    }

    public int getOrderCount() {
        return orderCount;
    }
 
    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

}

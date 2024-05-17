/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.homebound;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion basica del maestro Pruebas
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 17/02/2020
 * @see Creación
 */
@ApiObject(
        group = "Transporte",
        name = "transporte de muestras desde homebound",
        description = "informacion del transporte de la muestra desde homebound"
)
@Getter
@Setter
public class TransportSampleHomebound
{
    @ApiObjectField(name = "order", description = "Informacion de la orden", required = true, order = 1)
    private Long order;
    @ApiObjectField(name = "samples", description = "listado de muestras trasnportadas", required = true, order = 2)
    private List<Integer> samples;
    @ApiObjectField(name = "user", description = "usuario que trasnporto", required = true, order = 3)
    private Integer user;
}
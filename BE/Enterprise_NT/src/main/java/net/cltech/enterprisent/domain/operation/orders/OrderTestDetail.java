/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.test.Sample;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa informacion del examen para el ingreso de ordenes
 *
 * @version 1.0.0
 * @author dcortes
 * @since 5/03/2018
 * @see Creacion
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Prueba Ingreso de Orden",
        description = "Informacion de una prueba para el ingreso de ordenes"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class OrderTestDetail
{

    @ApiObjectField(name = "id", description = "Id Examen", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "price", description = "Precio del examen", required = true, order = 2)
    private BigDecimal price;
    @ApiObjectField(name = "resultValidity", description = "Vigencia del resultado", required = true, order = 3)
    private TestValidity resultValidity;
    @ApiObjectField(name = "samples", description = "Muestras asociados al examen", required = true, order = 4)
    private List<Sample> samples;
    @ApiObjectField(name = "patientPrice", description = "Precio que debe pagar el paciente", required = true, order = 5)
    private BigDecimal patientPrice;
    @ApiObjectField(name = "insurancePrice", description = "Precio que debe pagar el cliente (Aseguradora)", required = true, order = 6)
    private BigDecimal insurancePrice;
}

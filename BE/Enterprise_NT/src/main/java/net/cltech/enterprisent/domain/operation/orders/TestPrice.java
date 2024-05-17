package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa los precios que puede tener asociados un examen tales como:
* precio del servicio
* precio que debe pagar el paciente
* precio que debe pagar la aseguradora
*
* @version 1.0.0
* @author Julian
* @since 28/04/2021
* @see Creación
*/

@ApiObject(
        group = "Operación - Ordenes",
        name = "Precio Examen",
        description = "Representa los precios  que puede tener asociados un examen"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class TestPrice 
{
    @ApiObjectField(name = "servicePrice", description = "Precio del examen (Servicio)", required = true, order = 1)
    private BigDecimal servicePrice;
    @ApiObjectField(name = "patientPrice", description = "Precio que debe pagar el paciente", required = true, order = 2)
    private BigDecimal patientPrice;
    @ApiObjectField(name = "insurancePrice", description = "Precio que debe pagar el cliente (Aseguradora)", required = true, order = 3)
    private BigDecimal insurancePrice;
    @ApiObjectField(name = "tax", description = "Impuesto de la prueba", required = true, order = 4)
    private Double tax;
    @ApiObjectField(name = "testId", description = "Id del examen", required = true, order = 5)
    private int testId;
    @ApiObjectField(name = "patientPercentage", description = "Porcentaje de paciente", required = true, order = 6)
    private BigDecimal patientPercentage;
}
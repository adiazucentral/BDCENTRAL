package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la informacion requerida de cada examen dentro de una orden para la factura
*
* @version 1.0.0
* @author Julian
* @since 13/04/2021
* @see Creación
*/
@ApiObject(
        group = "Operación - Facturación",
        name = "Examen por factura",
        description = "Representa los datos necesarios para el examen dentro de la factura"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class PreInvoiceTest 
{
    @ApiObjectField(name = "testId", description = "Id del examen", required = true, order = 1)
    private Integer testId;
    @ApiObjectField(name = "testCode", description = "Codigo del examen", required = true, order = 2)
    private String testCode;
    @ApiObjectField(name = "testCodeCups", description = "Codigo del examen del examen segun sistema central del cliente", required = true, order = 2)
    private String testCodeCups;
    @ApiObjectField(name = "testName", description = "Nombre del examen", required = true, order = 3)
    private String testName;
    @ApiObjectField(name = "testAbbr", description = "Abreviatura del examen", required = true, order = 4)
    private String testAbbr;
    @ApiObjectField(name = "servicePrice", description = "Precio del servicio", required = true, order = 5)
    private Double servicePrice;
    @ApiObjectField(name = "patientPrice", description = "Precio del paciente", required = true, order = 6)
    private Double patientPrice;
    @ApiObjectField(name = "insurancePrice", description = "Precio de la aseguradora", required = true, order = 7)
    private Double insurancePrice;
}
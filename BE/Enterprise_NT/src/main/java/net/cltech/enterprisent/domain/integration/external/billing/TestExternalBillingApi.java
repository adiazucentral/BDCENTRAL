package net.cltech.enterprisent.domain.integration.external.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el examen (prueba) que se enviará junto a la caja de la API de México
 * 
 * @version 1.0.0
 * @author javila
 * @since 12/07/2021
 * @see Creación
 */

@ApiObject(
        name = "Examen De Facturación Externa",
        group = "Operación - Ordenes",
        description = "Representa el examen (prueba) que se enviará junto a la caja de la API de México"
)
@Getter
@Setter
@NoArgsConstructor
public class TestExternalBillingApi
{
    @ApiObjectField(name = "Codigo", description = "Codigo", required = true, order = 1)
    @JsonProperty("Codigo")
    private String Codigo;
    @ApiObjectField(name = "Descripcion", description = "Descripción", required = true, order = 2)
    @JsonProperty("Descripcion")
    private String Descripcion;
    @ApiObjectField(name = "ClaveProdServSAT", description = "ClaveProdServSAT del examen", required = false, order = 3)
    @JsonProperty("ClaveProdServSAT")
    private String ClaveProdServSAT;
    @ApiObjectField(name = "ClaveUnidadSAT", description = "ClaveUnidadSAT del examen", required = false, order = 4)
    @JsonProperty("ClaveUnidadSAT")
    private String ClaveUnidadSAT;
    @ApiObjectField(name = "Cantidad", description = "Cantidad del examen", required = true, order = 5)
    @JsonProperty("Cantidad")
    private Integer Cantidad;
    @ApiObjectField(name = "Precio", description = "Precio del examen", required = true, order = 6)
    @JsonProperty("Precio")
    private BigDecimal Precio;
    @ApiObjectField(name = "Subtotal", description = "Subtotal del examen", required = true, order = 7)
    @JsonProperty("Subtotal")
    private BigDecimal Subtotal;
    @ApiObjectField(name = "Impuesto", description = "Impuesto del examen", required = true, order = 8)
    @JsonProperty("Impuesto")
    private BigDecimal Impuesto;
    @ApiObjectField(name = "Total", description = "Total del examen", required = true, order = 9)
    @JsonProperty("Total")
    private BigDecimal Total;
    @ApiObjectField(name = "DescuentoPorcentaje", description = "Porcentaje de descuento", required = true, order = 10)
    @JsonProperty("DescuentoPorcentaje")
    private BigDecimal DescuentoPorcentaje;
    @ApiObjectField(name = "DescuentoValor", description = "Valor de descuento", required = true, order = 11)
    @JsonProperty("DescuentoValor")
    private BigDecimal DescuentoValor;
    @ApiObjectField(name = "BaseImpuesto", description = "Base del impuesto", required = true, order = 12)
    @JsonProperty("BaseImpuesto")
    private BigDecimal BaseImpuesto;
    @ApiObjectField(name = "ImpuestoPorcentaje", description = "Porcentaje del impuesto", required = true, order = 13)
    @JsonProperty("ImpuestoPorcentaje")
    private BigDecimal ImpuestoPorcentaje;
}

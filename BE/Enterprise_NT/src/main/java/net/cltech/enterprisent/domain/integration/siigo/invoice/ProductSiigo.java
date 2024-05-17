package net.cltech.enterprisent.domain.integration.siigo.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el producto que esta asociado a una factura enviada a Siigo
 *
 * @version 1.0.0
 * @author Julian
 * @since 29/04/2021
 * @see Creación
 */

@ApiObject(
        group = "Integración con Siigo",
        name = "Producto De La Factura Siigo",
        description = "Representa el producto que esta asociado a una factura enviada a Siigo"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class ProductSiigo
{
    @ApiObjectField(name = "code", description = "Código único del producto", required = true, order = 1)
    private String code;
    @ApiObjectField(name = "description", description = "Nombre o descripción del producto/servicio", required = false, order = 2)
    private String description;
    @ApiObjectField(name = "quantity", description = "Cantidad", required = true, order = 3)
    private Integer quantity;
    @ApiObjectField(name = "price", description = "Precio del producto / Valor unitario", required = true, order = 4)
    private Double price;
    @ApiObjectField(name = "discount", description = "Porcentaje o Valor de descuento. Según configuración de la factura", required = false, order = 5)
    private Double discount;
    @ApiObjectField(name = "taxes", description = "Impuestos que se desean asociar al producto o servicio.", required = true, order = 6)
    private List<TaxSiigo> taxes;
}

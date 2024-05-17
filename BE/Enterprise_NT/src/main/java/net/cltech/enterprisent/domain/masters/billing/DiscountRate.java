package net.cltech.enterprisent.domain.masters.billing;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un tipo de descuento
 * 
 * @version 1.0.0
 * @author javila
 * @since 23/03/2021
 * @see Creación
 */
@ApiObject(
        group = "Facturacion",
        name = "Tipo de descuento",
        description = "Muestra información del maestro de tipo de descuento del API"
)
@Getter
@Setter
@NoArgsConstructor
public class DiscountRate
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "percentage", description = "Porcentaje de descuento", required = true, order = 4)
    private Double percentage;
    @ApiObjectField(name = "state", description = "Estado. false -> Desactivado, true -> Activado", required = true, order = 5)
    private boolean state;
    @ApiObjectField(name = "idUserCreating", description = "Id del usuario que crea", required = false, order = 6)
    private Integer idUserCreating;
    @ApiObjectField(name = "dateCreation", description = "Fecha de creación", required = false, order = 7)
    private Timestamp dateCreation;
    @ApiObjectField(name = "modifyingUserId", description = "Id del usuario que modifica", required = false, order = 8)
    private Integer modifyingUserId;
    @ApiObjectField(name = "dateOfModification", description = "Fecha de modificación", required = false, order = 9)
    private Timestamp dateOfModification;
    @ApiObjectField(name = "customerId", description = "Id del cliente", required = false, order = 10)
    private Integer customerId;
}

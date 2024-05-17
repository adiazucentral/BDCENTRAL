package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el examen para un ticket
 *
 * @version 1.0.0
 * @author dcortes
 * @since 26/02/2018
 * @see Creacion
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Talon",
        description = "Representa los examenes para el talon de reclamación"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class TicketTest
{
    @ApiObjectField(name = "id", description = "Id de la prueba", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "code", description = "Codigo de la prueba", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "abbr", description = "Abreviatura de la prueba", required = true, order = 3)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre de la prueba", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "deliveryDays", description = "Dias de entrega", required = true, order = 5)
    private int deliveryDays;
    @ApiObjectField(name = "proccessDays", description = "Dias de procesamiento", required = true, order = 6)
    private String proccessDays;
    @ApiObjectField(name = "type", description = "Tipo de Prueba 0->Examen, 1->Perfil, 2->Paquete", required = true, order = 7)
    private int type;
    @ApiObjectField(name = "informedConsent", description = "Consentimiento informado: 0 -> sin consentimiento, 1 -> con consentimiento informado", required = true, order = 8)
    private boolean informedConsent;
    @ApiObjectField(name = "dateDelivery", description = "Fecha estimada de entrega", required = false, order = 9)
    private Date dateDelivery;
    @ApiObjectField(name = "servicePrice", description = "Precio del servicio", required = true, order = 10)
    private BigDecimal servicePrice;
    @ApiObjectField(name = "priceParticular", description = "Precio del paciente", required = true, order = 11)
    private BigDecimal priceParticular;
    @ApiObjectField(name = "priceAccount", description = "Precio del cliente", required = true, order = 12)
    private BigDecimal priceAccount;
    @ApiObjectField(name = "idPackage", description = "Id del paquete", required = true, order = 13)
    private int idPackage;
    @ApiObjectField(name = "deployPackage", description = "Desplegar paquetes", required = true, order = 14)
    private int deployPackage;
}

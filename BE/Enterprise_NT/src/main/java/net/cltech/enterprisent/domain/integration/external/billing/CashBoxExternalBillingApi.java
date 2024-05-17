package net.cltech.enterprisent.domain.integration.external.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la caja que se enviará a crear o actualizar en la API de México
 * 
 * @version 1.0.0
 * @author javila
 * @since 2021/07/12
 * @see Creación
 */

@ApiObject(
        name = "Caja De Facturación Externa",
        group = "Operación - Ordenes",
        description = "Representa la caja que se enviará a crear o actualizar en la API de México"
)
@Getter
@Setter
@NoArgsConstructor
public class CashBoxExternalBillingApi
{
    @ApiObjectField(name = "Orden", description = "Número de la Orden", required = true, order = 1)
    @JsonProperty("Orden")
    private long Orden;
    @ApiObjectField(name = "FechaIngreso", description = "Fecha de ingreso", required = true, order = 2)
    @JsonProperty("FechaIngreso")
    private String FechaIngreso;
    @ApiObjectField(name = "Tarifa", description = "Código de la tarifa", required = true, order = 3)
    @JsonProperty("Tarifa")
    private String Tarifa;
    @ApiObjectField(name = "DescripcionTarifa", description = "Descripción de la tarifa", required = true, order = 4)
    @JsonProperty("DescripcionTarifa")
    private String DescripcionTarifa;
    @ApiObjectField(name = "SubTotalOrden", description = "Subtotal de la orden", required = true, order = 5)
    @JsonProperty("SubTotalOrden")
    private BigDecimal SubTotalOrden;
    @ApiObjectField(name = "ImpuestoOrden", description = "Impuesto de la orden", required = true, order = 6)
    @JsonProperty("ImpuestoOrden")
    private BigDecimal ImpuestoOrden;
    @ApiObjectField(name = "TotalOrden", description = "Total de la orden", required = true, order = 7)
    @JsonProperty("TotalOrden")
    private BigDecimal TotalOrden;
    @ApiObjectField(name = "Paciente", description = "Paciente de facturación externa", required = true, order = 8)
    @JsonProperty("Paciente")
    private PatientExternalBillingApi Paciente = new PatientExternalBillingApi();
    @ApiObjectField(name = "Sede", description = "Sede de facturación externa", required = true, order = 9)
    @JsonProperty("Sede")
    private BranchExternalBillingApi Sede = new BranchExternalBillingApi();
    @ApiObjectField(name = "Cliente", description = "Cliente de facturación externa", required = true, order = 10)
    @JsonProperty("Cliente")
    private AccountExternalBillingApi Cliente = new AccountExternalBillingApi();
    @ApiObjectField(name = "Pruebas", description = "Pruebas de facturación externa", required = true, order = 11)
    @JsonProperty("Pruebas")
    private List<TestExternalBillingApi> Pruebas;
    @ApiObjectField(name = "Descuento", description = "Descuento", required = true, order = 12)
    @JsonProperty("Descuento")
    private BigDecimal Descuento;
    @ApiObjectField(name = "Pagos", description = "Tipos de pagos de la caja", required = true, order = 13)
    @JsonProperty("Pagos")
    private List<PaymentExternalBillingApi> Pagos;
    @ApiObjectField(name = "Estado", description = "Estado de la orden: 0-> Creación, 1-> Modificación, 2-> Eliminación", required = true, order = 14)
    @JsonProperty("Estado")
    private int Estado;
    @ApiObjectField(name = "UsuarioIngreso", description = "Nombre de usuario de ingreso", required = true, order = 15)
    @JsonProperty("UsuarioIngreso")
    private String UsuarioIngreso;
    @ApiObjectField(name = "FechaMensaje", description = "Fecha de envío del mensaje", required = true, order = 16)
    @JsonProperty("FechaMensaje")
    private String FechaMensaje;
    @ApiObjectField(name = "UsuarioMensaje", description = "Nombre de usuario que generó el envío del mensaje", required = true, order = 17)
    @JsonProperty("UsuarioMensaje")
    private String UsuarioMensaje;
}

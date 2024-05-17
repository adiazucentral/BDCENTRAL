package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.billing.RatesByContract;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la cabecera de la factura
*
* @version 1.0.0
* @author Julian
* @since 13/04/2021
* @see Creación
*/

@ApiObject(
        group = "Operación - Facturación",
        name = "Cabecera de la factura",
        description = "Representa los datos necesarios para la cabecera de la factura"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class PreInvoiceHeader 
{
    @ApiObjectField(name = "accountNit", description = "NIT del cliente", required = true, order = 1)
    private String accountNit;
    @ApiObjectField(name = "accountName", description = "Nombre del cliente", required = true, order = 2)
    private String accountName;
    @ApiObjectField(name = "accountPhone", description = "Telefono del cliente", required = true, order = 3)
    private String accountPhone;
    @ApiObjectField(name = "accountAddress", description = "Dirección del cliente", required = true, order = 4)
    private String accountAddress;
    @ApiObjectField(name = "accountCity", description = "Ciudad del cliente", required = true, order = 5)
    private String accountCity;
    @ApiObjectField(name = "accountCityName", description = "Nombre de la ciudad del cliente", required = true, order = 5)
    private String accountCityName;
    @ApiObjectField(name = "dateOfInvoice", description = "Fecha de vencimiento de la factura", required = true, order = 6)
    private Timestamp dateOfInvoice;
    @ApiObjectField(name = "formOfPayment", description = "Forma de pago: 1 - pago de contad, 2 - Pago a crédito", required = true, order = 7)
    private int formOfPayment;
    @ApiObjectField(name = "vendorName", description = "Nombre del vendedor", required = true, order = 8)
    private String vendorName;
    @ApiObjectField(name = "signatureOfSeller", description = "Firma del vendedor", required = true, order = 9)
    private String signatureOfSeller;
    @ApiObjectField(name = "orders", description = "Ordenes de la pre factura", required = true, order = 10)
    private List<PreInvoiceOrder> orders;
    @ApiObjectField(name = "discountPercentage", description = "Porcentaje de descuento", required = true, order = 11)
    private Double discountPercentage;
    @ApiObjectField(name = "regimen", description = "Régimen", required = true, order = 12)
    private String regimen;
    @ApiObjectField(name = "maximumAmount", description = "Monto maximo", required = true, order = 13)
    private Double maximumAmount;
    @ApiObjectField(name = "currentAmount", description = "Monto actual", required = true, order = 14)
    private Double currentAmount;
    @ApiObjectField(name = "alarmAmount", description = "Monto alarma", required = true, order = 15)
    private Double alarmAmount;
    @ApiObjectField(name = "capitated", description = "Capitado", required = true, order = 16)
    private Integer capitated;
    @ApiObjectField(name = "vendorIdentifier", description = "Identificador del vendedor", required = true, order = 17)
    private String vendorIdentifier;
    @ApiObjectField(name = "contractCode", description = "Codigo del contrato", required = true, order = 18)
    private String contractCode;
    @ApiObjectField(name = "contractId", description = "id del contrato", required = true, order = 19)
    private Integer contractId;
    @ApiObjectField(name = "entity", description = "Entidad", required = false, order = 20)
    private String entity;
    @ApiObjectField(name = "discount", description = "Descuento", required = true, order = 21)
    private Double discount;
    @ApiObjectField(name = "total", description = "Total", required = true, order = 22)
    private Double total;
    @ApiObjectField(name = "tax", description = "Impuesto (Suma de todos los impuestos que aplican para ese cliente)", required = true, order = 23)
    private Double tax;
    @ApiObjectField(name = "taxs", description = "Lista del Impuesto", required = true, order = 24)
    private List<TaxInvoice> taxs;
    @ApiObjectField(name = "totalPaidInCash", description = "Total pagado en caja (Copago + Cuota Mod)", required = true, order = 25)
    private Double totalPaidInCash;
    @ApiObjectField(name = "customerId", description = "Id del cliente", required = true, order = 26)
    private Integer customerId;
    @ApiObjectField(name = "contractName", description = "Nombre del contrato", required = true, order = 27)
    private String contractName;
    @ApiObjectField(name = "initialOrder", description = "Orden inicial", required = true, order = 28)
    private long initialOrder;
    @ApiObjectField(name = "finalOrder", description = "Orden final", required = true, order = 29)
    private long finalOrder;
    @ApiObjectField(name = "numberOfOrders", description = "Cantidad de ordenes facturadas", required = true, order = 30)
    private Integer numberOfOrders;
    @ApiObjectField(name = "id", description = "Id de la cabecera", required = true, order = 31)
    private Long id;
    @ApiObjectField(name = "copayment", description = "Indica si el contrato maneja copago", required = true, order = 32)
    private Integer copayment;
    @ApiObjectField(name = "moderatingFee", description = "Indica si el contrato maneja cuota moderadora", required = true, order = 33)
    private Integer moderatingFee;
    @ApiObjectField(name = "totalCopayment", description = "Total copagos", required = true, order = 34)
    private Double totalCopayment;
    @ApiObjectField(name = "TotalModeratingFee", description = "Total cuota moderadora", required = true, order = 35)
    private Double TotalModeratingFee;
    @ApiObjectField(name = "totalOrders", description = "Total ordenes facturadas en la cabecera", required = true, order = 36)
    private Integer totalOrders;
    @ApiObjectField(name = "datePerCapita", description = "Fecha para las ordenes por capitado", required = false, order = 37)
    private String datePerCapita;
    @ApiObjectField(name = "rates", description = "Tarifas del contrato", required = false, order = 38)
    private List<RatesByContract> rates;
    @ApiObjectField(name = "particular", description = "Indica si el cliente es particular", required = true, order = 39)
    private Integer particular;
    @ApiObjectField(name = "totalPayments", description = "Total pagos", required = true, order = 40)
    private Double totalPayments;
}
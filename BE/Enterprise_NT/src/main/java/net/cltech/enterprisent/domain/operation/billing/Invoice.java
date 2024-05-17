package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.operation.orders.billing.BillingTest;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la factura
*
* @version 1.0.0
* @author Julian
* @since 13/04/2021
* @see Creación
*/

@ApiObject(
        group = "Operación - Facturación",
        name = "Factura",
        description = "Representa los datos necesarios para la factura"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class Invoice 
{
    @ApiObjectField(name = "invoiceId", description = "Id de la factura (Identificador BD)", required = true, order = 1)
    private Long invoiceId;
    @ApiObjectField(name = "invoiceNumber", description = "Número de la factura", required = true, order = 2)
    private String invoiceNumber;
    @ApiObjectField(name = "discount", description = "Descuento", required = true, order = 3)
    private Double discount;
    @ApiObjectField(name = "total", description = "Total", required = true, order = 4)
    private Double total;
    @ApiObjectField(name = "dateOfInvoice", description = "Fecha de creación de la factura", required = true, order = 5)
    private Timestamp dateOfInvoice;
    @ApiObjectField(name = "resolutionId", description = "Id de la resolución", required = true, order = 6)
    private Integer resolutionId;
    @ApiObjectField(name = "userId", description = "Id del usuario", required = true, order = 8)
    private Integer userId;
    @ApiObjectField(name = "particular", description = "Particular", required = true, order = 12)
    private int particular;
    @ApiObjectField(name = "state", description = "Estado de la factura", required = true, order = 13)
    private boolean state;
    @ApiObjectField(name = "payerId", description = "Id del pagador (Entidad o laboratorio)", required = true, order = 14)
    private Integer payerId;
    @ApiObjectField(name = "typeOfInvoice", description = "Tipo de factura", required = true, order = 15)
    private Integer typeOfInvoice;
    @ApiObjectField(name = "tax", description = "Impuesto (Suma de todos los impuestos)", required = true, order = 16)
    private Double tax;
    @ApiObjectField(name = "idSubsequentCredits", description = "Id de abonos posteriores", required = true, order = 17)
    private Integer idSubsequentCredits;
    @ApiObjectField(name = "comment", description = "Comentario", required = true, order = 18)
    private String comment;
    @ApiObjectField(name = "cfdi", description = "CFDI", required = true, order = 19)
    private String cfdi;
    @ApiObjectField(name = "totalPaid", description = "Total pagado (Total de los abonos posteriores)", required = true, order = 22)
    private Double totalPaid;
    @ApiObjectField(name = "svTypeOfInvoice", description = "SV Tipo de factura: 0 -> Consumidor final, 1 -> Comprobante credito fiscal", required = true, order = 23)
    private int svTypeOfInvoice;
    @ApiObjectField(name = "svRetention", description = "SV retención", required = true, order = 24)
    private Double svRetention;
    @ApiObjectField(name = "svMaximumRetention", description = "SV Tope de retención", required = true, order = 25)
    private BigInteger svMaximumRetention;
    @ApiObjectField(name = "svThirdPartyInvoice", description = "SV Factura a terceros", required = true, order = 26)
    private String svThirdPartyInvoice;
    @ApiObjectField(name = "sent", description = "Enviado: 0-> sin enviar, 1-> Enviada", required = true, order = 27)
    private boolean sent;
    @ApiObjectField(name = "invoiceHeader", description = "Cabecera de la factura", required = true, order = 17)
    private List<PreInvoiceHeader> invoiceHeader;
    @ApiObjectField(name = "patientId", description = "Id del paciente (Factura particular)", required = false, order = 29)
    private Integer patientId;
    @ApiObjectField(name = "listTest", description = "Lista de examenes relacionados a la factura particular", required = false, order = 30)
    private List<BillingTest> BillingTest;
    @ApiObjectField(name = "billingPeriod", description = "Periodo de facturación", required = false, order = 31)
    private String billingPeriod;
    @ApiObjectField(name = "pendingOrders", description = "Ordenes pendientes (Por capitar)", required = false, order = 32)
    private int pendingOrders;
    @ApiObjectField(name = "dueDate", description = "Fecha de vencimiento de la factura", required = true, order = 33)
    private Timestamp dueDate;
    @ApiObjectField(name = "formOfPayment", description = "Forma de pago: 1 - pago de contad, 2 - Pago a crédito", required = true, order = 34)
    private int formOfPayment;
    @ApiObjectField(name = "paymentDate", description = "Fecha de pago de la factura", required = true, order = 35)
    private Timestamp paymentDate;
    @ApiObjectField(name = "paid", description = "Pagado: 0-> Sin pagar, 1-> Pagado", required = true, order = 36)
    private boolean paid;
    @ApiObjectField(name = "id", description = "Identificador", required = false, order = 37)
    private Integer id;
    @ApiObjectField(name = "history", description = "Historia", required = true, order = 38)
    private String history;
    @ApiObjectField(name = "name1", description = "Nombre 1", required = true, order = 39)
    private String name1;
    @ApiObjectField(name = "name2", description = "Nombre 2", required = false, order = 40)
    private String name2;
    @ApiObjectField(name = "lastName", description = "Apellido 1", required = true, order = 41)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Apellido 2", required = false, order = 42)
    private String surName;
    @ApiObjectField(name = "address", description = "Dirección", required = false, order = 43)
    private String address;
    @ApiObjectField(name = "phone", description = "Telefono", required = false, order = 44)
    private String phone;
    @ApiObjectField(name = "customerId", description = "Id del cliente", required = true, order = 45)
    private Integer customerId;
    @ApiObjectField(name = "accountNit", description = "NIT del cliente", required = true, order = 46)
    private String accountNit;
    @ApiObjectField(name = "accountName", description = "Nombre del cliente", required = true, order = 47)
    private String accountName;
    @ApiObjectField(name = "accountPhone", description = "Telefono del cliente", required = true, order = 48)
    private String accountPhone;
    @ApiObjectField(name = "accountAddress", description = "Dirección del cliente", required = true, order = 49)
    private String accountAddress;
    @ApiObjectField(name = "accountCity", description = "Ciudad del cliente", required = true, order = 50)
    private String accountCity;
    @ApiObjectField(name = "accountCityName", description = "Nombre de la ciudad del cliente", required = true, order = 51)
    private String accountCityName;
}
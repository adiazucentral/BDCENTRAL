package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa el reporte de caja detallado
*
* @version 1.0.0
* @author Julian
* @since 7/05/2021
* @see Creación
*/

@ApiObject(
        group = "Operación - Facturación",
        name = "Reporte De Caja Detallado",
        description = "Representan los datos con los que se realizara el reporte de caja detallado"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class DetailedCashReport 
{
    @ApiObjectField(name = "userId", description = "Id del usuario", required = true, order = 1)
    private Integer userId;
    @ApiObjectField(name = "userNames", description = "Nombres y Apellidos del usuario", required = true, order = 2)
    private String userNames;
    @ApiObjectField(name = "orderId", description = "Id de la orden", required = true, order = 3)
    private Long orderId;
    @ApiObjectField(name = "patientName", description = "Nombre complet del paciente", required = true, order = 4)
    private String patientName;
    @ApiObjectField(name = "invoiceNumber", description = "Número de la factura", required = true, order = 5)
    private String invoiceNumber;
    @ApiObjectField(name = "subTotal", description = "Sub total de la factura", required = true, order = 6)
    private Double subTotal;
    @ApiObjectField(name = "copago", description = "Copago", required = true, order = 7)
    private Double copago;
    @ApiObjectField(name = "total", description = "Total", required = true, order = 8)
    private Double total;
    @ApiObjectField(name = "balance", description = "Saldo", required = true, order = 9)
    private Double balance;
    @ApiObjectField(name = "cashRegisterReceipt", description = "Recibo de caja", required = true, order = 10)
    private String cashRegisterReceipt;
    @ApiObjectField(name = "paymentTypeName", description = "Nombre del tipo de pago", required = true, order = 11)
    private String paymentTypeName;
    @ApiObjectField(name = "typeOfDetail", description = "Representa si el tipo de dato es pagado o devolución: 1 -> Pagado, 2 -> Devolución", required = true, order = 12)
    private String typeOfDetail;
    @ApiObjectField(name = "discount", description = "Descuento", required = true, order = 13)
    private Double discount;
    @ApiObjectField(name = "discountPorcent", description = "Descuento por porcentaje", required = true, order = 14)
    private BigDecimal discountPorcent;
    @ApiObjectField(name = "moderatorFee", description = "Descuento", required = true, order = 15)
    private Double moderatorFee;
    @ApiObjectField(name = "tax", description = "impuesto", required = true, order = 16)
    private Double tax;
    @ApiObjectField(name = "creditnote", description = "Numero de nota credito", required = true, order = 17)
    private int creditnote;
    @ApiObjectField(name = "detailPaymentType", description = "Detalle del tipo de pago", required = true, order = 18)
    private List<DetailPaymentType> detailPaymentType;
    @ApiObjectField(name = "date", description = "Fecha de la caja", required = true, order = 18)
    private Timestamp date;
    @ApiObjectField(name = "dateInvoice", description = "Fecha de la factura", required = true, order = 19)
    private Timestamp dateInvoice;
    @ApiObjectField(name = "dateCreditNote", description = "Fecha de la nota credito", required = true, order = 20)
    private Timestamp dateCreditNote;
    @ApiObjectField(name = "rateId", description = "Id de la tarifa", required = true, order = 21)
    private int rateId;
    @ApiObjectField(name = "rateCode", description = "Código de la tarifa", required = true, order = 12)
    private String rateCode;
    @ApiObjectField(name = "rateName", description = "Nombre de la tarifa", required = true, order = 13)
    private String rateName;
    @ApiObjectField(name = "phone", description = "Telefono", required = true, order = 14)
    private String phone;
    @ApiObjectField(name = "accountCode", description = "Código del cliente", required = true, order = 15)
    private String accountCode;
    @ApiObjectField(name = "accountName", description = "Nombre del cliente", required = true, order = 16)
    private String accountName;
    @ApiObjectField(name = "totalPaid", description = "Total Pagado", required = true, order = 17)
    private Double totalPaid;
    @ApiObjectField(name = "type", description = "Tipo 1-> Caja, 2-> Abono", required = true, order = 18)
    private Integer type;
    @ApiObjectField(name = "ruc", description = "ruc", required = true, order = 21)
    private String ruc;
     @ApiObjectField(name = "billed", description = "ruc", required = true, order = 21)
    private String billed;
    @ApiObjectField(name = "phoneBox", description = "Telefono Caja", required = true, order = 14)
    private String phoneBox;    
    @ApiObjectField(name = "branchId", description = "Id de la sede", required = true, order = 1)
    private Integer branchId;
    @ApiObjectField(name = "branchName", description = "Nombre de la sede", required = true, order = 2)
    private String branchName;
    
    
    public DetailedCashReport()
    {
        detailPaymentType = new ArrayList<>(0);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final DetailedCashReport other = (DetailedCashReport) obj;
        if (!Objects.equals(this.orderId, other.orderId) || !Objects.equals(this.userId, other.userId))
        {
            return false;
        }
        return true;
    }
}
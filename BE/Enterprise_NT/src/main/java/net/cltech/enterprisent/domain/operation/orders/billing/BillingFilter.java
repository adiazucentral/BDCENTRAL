package net.cltech.enterprisent.domain.operation.orders.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la clase con los datos necesarios para realizar un filtro de la pre-factura
*
* @version 1.0.0
* @author Julian
* @since 5/04/2021
* @see Creación
*/
@ApiObject(
        group = "Operación - Ordenes",
        name = "Filtro para facturación",
        description = "Representa la clase con los datos necesarios para realizar un filtro de la pre-factura"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class BillingFilter 
{
    @ApiObjectField(name = "branchId", description = "Id de la sede", required = true, order = 1)
    private Integer branchId;
    @ApiObjectField(name = "customers", description = "id´s de los clientes a filtrar", required = true, order = 2)
    private List<Integer> customers;        
    @ApiObjectField(name = "rates", description = "id´s de las tarifas a filtrar", required = true, order = 3)
    private List<Integer> rates;    
    @ApiObjectField(name = "startDate", description = "Fecha de inicio del filtro con formato yyyyMMdd", required = true, order = 4)
    private Integer startDate;
    @ApiObjectField(name = "endDate", description = "Fecha de finalización del filtro con formato yyyyMMdd", required = true, order = 5)
    private Integer endDate;
    @ApiObjectField(name = "dateOfInvoice", description = "Fecha de la factura", required = true, order = 6)
    private Timestamp dateOfInvoice;
    @ApiObjectField(name = "invoiceNumber", description = "Número de la factura", required = true, order = 7)
    private String invoiceNumber;
    @ApiObjectField(name = "payerId", description = "Id del pagador (Entidad o laboratorio)", required = true, order = 8)
    private Integer payerId;
    @ApiObjectField(name = "comment", description = "Comentario de la factura", required = true, order = 9)
    private String comment;
    @ApiObjectField(name = "contractId", description = "Id del contrato", required = true, order = 10)
    private Integer contractId;
    @ApiObjectField(name = "testFilterType", description = "Filtro por examen: 0 - Todos<br> 1 - Sección<br> 2-Examen<br> 3-Confidenciales", order = 11)
    private int testFilterType;
    @ApiObjectField(name = "tests", description = "id´s Examenes/Secciónes a filtrar", order = 12)
    private List<Integer> tests;
    @ApiObjectField(name = "demographics", description = "Lista de filtro por demograficos", order = 13)
    private List<FilterDemographic> demographics = new ArrayList<>();
    @ApiObjectField(name = "filterTpye", description = "Tipo de filtro", required = true, order = 14)
    private Integer filterTpye;
    @ApiObjectField(name = "dueDate", description = "Fecha de vencimiento de la factura", required = true, order = 15)
    private Timestamp dueDate;
    @ApiObjectField(name = "formOfPayment", description = "Forma de pago: 1 - pago de contado, 2 - Pago a crédito", required = true, order = 16)
    private int formOfPayment;

    @Override
    public String toString()
    {
        return "BillingFilter{" + "branchId=" + branchId + ", customers=" + customers + ", rates=" + rates + ", startDate=" + startDate + ", endDate=" + endDate + ", dateOfInvoice=" + dateOfInvoice + ", invoiceNumber=" + invoiceNumber + ", payerId=" + payerId + ", comment=" + comment + ", contractId=" + contractId + ", testFilterType=" + testFilterType + ", tests=" + tests + ", demographics=" + demographics + ", filterTpye=" + filterTpye + ", dueDate=" + dueDate + ", formOfPayment=" + formOfPayment + '}';
    }
    
}
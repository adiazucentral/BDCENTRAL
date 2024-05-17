package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el detalle del reporte de caja
 *
 * @version 1.0.0
 * @author Julian
 * @since 6/05/2021
 * @see Creación
 */
@ApiObject(
        group = "Operación - Facturación",
        name = "Detalle Del Reporte De Caja",
        description = "Representan los datos de detalle del reporte de caja"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class CashReportDetail
{

    @ApiObjectField(name = "userId", description = "Id del usuario", required = true, order = 1)
    private Integer userId;
    @ApiObjectField(name = "userName", description = "Usuario (lab04c4)", required = true, order = 2)
    private String userName;
    @ApiObjectField(name = "names", description = "Nombres del usuario", required = true, order = 3)
    private String names;
    @ApiObjectField(name = "paymentTypeName", description = "Nombre del tipo de pago", required = true, order = 4)
    private String paymentTypeName;
    @ApiObjectField(name = "paymentTypeAmount", description = "Pago en efectivo", required = true, order = 5)
    private Double paymentTypeAmount;
    @ApiObjectField(name = "typeOfDetail", description = "Representa si el tipo de dato es pagado o devolución: 1 -> Pagado, 2 -> Devolución", required = true, order = 6)
    private String typeOfDetail;
    @ApiObjectField(name = "datePayment", description = "Fecha de pago", required = true, order = 7)
    private String datePayment;
    @ApiObjectField(name = "branchId", description = "Id de la sede", required = true, order = 1)
    private Integer branchId;
    @ApiObjectField(name = "branchName", description = "Nombre de la sede", required = true, order = 2)
    private String branchName;
}

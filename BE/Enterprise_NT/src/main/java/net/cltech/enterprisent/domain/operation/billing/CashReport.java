package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa el reporte de caja
*
* @version 1.0.0
* @author Julian
* @since 6/05/2021
* @see Creación
*/

@ApiObject(
        group = "Operación - Facturación",
        name = "Reporte De Caja",
        description = "Representa el reporte de caja"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class CashReport 
{
    @ApiObjectField(name = "dateOfPrinting", description = "Fecha de la impresión", required = true, order = 1)
    private Timestamp dateOfPrinting;
    @ApiObjectField(name = "userName", description = "Usuario que genera el reporte (lab04c4)", required = true, order = 2)
    private String userName;
    @ApiObjectField(name = "branchName", description = "Nombre de la sede", required = true, order = 3)
    private String branchName;
    @ApiObjectField(name = "cashReportDetails", description = "Lista de reporte general de caja", required = true, order = 4)
    private List<CashReportDetail> cashReportDetails;
    @ApiObjectField(name = "detailedCashReport", description = "Lista de reporte detallado de caja", required = true, order = 5)
    private List<DetailedCashReport> detailedCashReport;

    public CashReport()
    {
        this.cashReportDetails = new ArrayList<>();
        this.detailedCashReport = new ArrayList<>();
    }
}
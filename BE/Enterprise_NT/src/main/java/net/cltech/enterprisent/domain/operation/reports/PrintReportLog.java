package net.cltech.enterprisent.domain.operation.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.cltech.enterprisent.domain.operation.orders.Order;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la clase de registro del log de impresion
 *
 * @version 1.0.0
 * @author equijano
 * @since 24/05/2019
 * @see Creación
 */
@ApiObject(
        group = "Operación - Informes",
        name = "Regitro de log de impresion",
        description = "Representa el objeto de log de impresion."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrintReportLog
{

    @ApiObjectField(name = "orderNumber", description = "Numero de orden", required = true, order = 1)
    private Long orderNumber;
    @ApiObjectField(name = "patient", description = "Nombre de paciente", required = true, order = 2)
    private String patient;
    @ApiObjectField(name = "printing", description = "Valida si se imprimio o no", required = true, order = 3)
    private boolean printing;
    @ApiObjectField(name = "order", description = "Orden completa", required = false, order = 4)
    private Order order;

    public PrintReportLog()
    {
    }

    public PrintReportLog(Long orderNumber, String patient, Order order)
    {
        this.orderNumber = orderNumber;
        this.patient = patient;
        this.order = order;
    }

    public PrintReportLog(Long orderNumber, String patient, Order order, boolean printing)
    {
        this.orderNumber = orderNumber;
        this.patient = patient;
        this.order = order;
        this.printing = printing;
    }

    public Long getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public String getPatient()
    {
        return patient;
    }

    public void setPatient(String patient)
    {
        this.patient = patient;
    }

    public boolean isPrinting()
    {
        return printing;
    }

    public void setPrinting(boolean printing)
    {
        this.printing = printing;
    }

    public Order getOrder()
    {
        return order;
    }

    public void setOrder(Order order)
    {
        this.order = order;
    }

}

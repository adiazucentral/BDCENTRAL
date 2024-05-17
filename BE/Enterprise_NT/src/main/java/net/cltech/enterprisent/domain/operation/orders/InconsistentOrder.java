package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa una orden con inconsistencia
*
* @version 1.0.0
* @author Julian
* @since 10/09/2020
* @see Creación
*/

@ApiObject(
        group = "Operación - Ordenes",
        name = "Orden con inconsistencia",
        description = "Representa una orden que venga con inconsistencias"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InconsistentOrder
{
    @ApiObjectField(name = "orderNumber", description = "Identificador de la orden", order = 1)
    private long orderNumber;
    @ApiObjectField(name = "patient", description = "Paciente de la orden", required = true, order = 5)
    private Patient patient = new Patient();

    public InconsistentOrder()
    {
        patient = new Patient();
    }

    public long getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public Patient getPatient()
    {
        return patient;
    }

    public void setPatient(Patient patient)
    {
        this.patient = patient;
    }
}
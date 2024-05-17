package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la información requerida para la construccion de la pre-factura
*
* @version 1.0.0
* @author Julian
* @since 5/04/2021
* @see Creación
*/
@ApiObject(
        group = "Operación - Facturación",
        name = "Pre facturación",
        description = "Representa los datos necesarios para la construcción de la pre-facturación"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class PreInvoiceOrder 
{
    @ApiObjectField(name = "orderId", description = "Id de la orden", required = true, order = 1)
    private long orderId;
    @ApiObjectField(name = "patientId", description = "Id del paciente", required = true, order = 2)
    private Integer patientId;
    @ApiObjectField(name = "historyPatient", description = "Historia del paciente", required = true, order = 3)
    private String historyPatient;
    @ApiObjectField(name = "firstName", description = "Primer nombre del paciente", required = true, order = 4)
    private String firstName;
    @ApiObjectField(name = "secondName", description = "Segundo nombre del paciente", required = true, order = 5)
    private String secondName;
    @ApiObjectField(name = "lastName", description = "Apellido del paciente", required = true, order = 6)
    private String lastName;
    @ApiObjectField(name = "secondLastName", description = "Segundo apellido del paciente", required = true, order = 7)
    private String secondLastName;
    @ApiObjectField(name = "documentTypeId", description = "Id del tipo de documento", required = true, order = 8)
    private Integer documentTypeId;
    @ApiObjectField(name = "copayment", description = "Copago", required = true, order = 9)
    private Double copayment;
    @ApiObjectField(name = "tests", description = "Examenes asociados a una factura", required = true, order = 10)
    private List<PreInvoiceTest> tests;
    @ApiObjectField(name = "moderatorFee", description = "Cuota moderadora", required = true, order = 11)
    private Double moderatorFee;
    @ApiObjectField(name = "orderCreationDate", description = "Fecha de creación de la orden", required = true, order = 12)
    private Timestamp orderCreationDate;
    @ApiObjectField(name = "documentTypeAbbr", description = "Abreviatura del tipo de documento del paciente", required = true, order = 13)
    private String documentTypeAbbr;
    @ApiObjectField(name = "branch", description = "Sede", required = false, order = 14)
    private Integer branch;
    @ApiObjectField(name = "service", description = "Servicio", required = false, order = 15)
    private Integer service;
    @ApiObjectField(name = "physician", description = "Medico", required = false, order = 16)
    private Integer physician;
    @ApiObjectField(name = "account", description = "Cuenta", required = false, order = 17)
    private Integer account;
    @ApiObjectField(name = "rate", description = "Tarifa", required = false, order = 18)
    private Integer rate;
    @ApiObjectField(name = "type", description = "Tipo de la orden", required = true, order = 19)
    private Integer type;
    @ApiObjectField(name = "race", description = "Raza", required = false, order = 20)
    private Integer race;
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 21)
    private List<DemographicValue> AllDemographics;
    @ApiObjectField(name = "nameDoctor", description = "Nombre del medico", required = false, order = 22)
    private String nameDoctor;
    @ApiObjectField(name = "lastNameDoctor", description = "Apellido del medico", required = false, order = 23)
    private String lastNameDoctor;
    @ApiObjectField(name = "branchName", description = "Nombre Sede", required = false, order = 24)
    private String branchName;
    @ApiObjectField(name = "customerId", description = "Id del cliente", required = true, order = 25)
    private Integer customerId;
    @ApiObjectField(name = "contractId", description = "Id del contrato", required = true, order = 26)
    private Integer contractId;
    @ApiObjectField(name = "documentTypeName", description = "Nombre del tipo de documento del paciente", required = true, order = 27)
    private String documentTypeName;
    @ApiObjectField(name = "totalPayments", description = "Total abonos", required = true, order = 28)
    private Double totalPayments;

    public PreInvoiceOrder() {
        this.tests = new LinkedList<>();
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
        final PreInvoiceOrder other = (PreInvoiceOrder) obj;
        if (!Objects.equals(this.orderId, other.orderId))
        {
            return false;
        }
        return true;
    }
}
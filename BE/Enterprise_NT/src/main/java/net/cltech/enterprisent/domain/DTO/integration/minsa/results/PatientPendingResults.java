/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.minsa.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 *
 * @version 1.0.0
 * @author bbonilla
 * @since 12/04/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "PatientPendingResults",
        description = "Muestra informacion del maestro Areas que usa el API"
)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientPendingResults
{

    @ApiObjectField(name = "identification", description = "Id del area", required = true, order = 1)
    private String identification;
    @ApiObjectField(name = "names", description = "Ordenamiento del area", required = true, order = 2)
    private String names;
    @ApiObjectField(name = "firstName", description = "Abreviatura del area", required = true, order = 3)
    private String firstName;
    @ApiObjectField(name = "lastName", description = "Nombre del area", required = true, order = 4)
    private String lastName;
    @ApiObjectField(name = "sex", description = "Color del area", required = true, order = 5)
    private String sex;
    @ApiObjectField(name = "birthDay", description = "Tipo del area", required = true, order = 7)
    private Date birthDay;
    @ApiObjectField(name = "age", description = "Tipo del area", required = true, order = 8)
    private String age;
    @ApiObjectField(name = "email", description = "Tipo del area", required = true, order = 9)
    private String email;
    @ApiObjectField(name = "diagnostic", description = "Tipo del area", required = true, order = 10)
    private String diagnostic;
    @ApiObjectField(name = "typedocument", description = "Tipo del docuemto", required = true, order = 9)
    private String typedocumentName;
    @ApiObjectField(name = "typedocumentCodec", description = "codifo tipo documento", required = true, order = 10)
    private String typedocumentCode;
    @ApiObjectField(name = "demographicValues", description = "Demos pacientes", required = true, order = 10)
    private List<DemographicValue> demographics = new ArrayList<>();

    public PatientPendingResults()
    {
    }

    public PatientPendingResults(String patientID)
    {
        this.identification = patientID;
    }
}

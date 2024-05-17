/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.migracionIngreso;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.tools.mappers.MigrationMapper;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author hpoveda
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

@ApiObject(
        group = "Operación - Ordenes",
        name = "Paciente migration",
        description = "Representa un paciente dentro de la aplicación"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientNT
{

    @ApiObjectField(name = "record", description = "Identificador", required = false, order = 1)
    private String record;
    @ApiObjectField(name = "name", description = "Nombre 1", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "name1", description = "Nombre 1", required = true, order = 3)
    private String name1;
    @ApiObjectField(name = "name2", description = "Nombre 2", required = false, order = 4)
    private String name2;
    @ApiObjectField(name = "lastName", description = "Apellido 1", required = true, order = 3)
    private String lastName;
    @ApiObjectField(name = "secondLastName", description = "Apellido 2", required = false, order = 4)
    private String secondLastName;
    @ApiObjectField(name = "gender", description = "Sexo", required = false, order = 5)
    private int gender;
    @ApiObjectField(name = "birthDate", description = "Fecha Nacimiento", required = false, order = 6)
    private String birthDate;
    @ApiObjectField(name = "comment", description = "comment Permanente", required = false, order = 7)
    private String comment;
    @ApiObjectField(name = "phone", description = "Telefono del Paciente", required = false, order = 22)
    private String phone;
    @ApiObjectField(name = "address", description = "Dirección del Paciente", required = false, order = 23)
    private String address;
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 8)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DemographicNT> demographics;
    @ApiObjectField(name = "documentAbbr", description = "Abreviatura del tipo de documento", required = false, order = 24)
    private String documentAbbr;
    @ApiObjectField(name = "documentName", description = "Nombre del tipo de documento", required = false, order = 25)
    private String documentName;
    @ApiObjectField(name = "email", description = "Correo del paciente", required = false, order = 26)
    private String email;

    public PatientNT(Patient patient)
    {
        this.record = patient.getPatientId();
        this.name = patient.getName1() == null ? "" : patient.getName1();
        this.lastName = patient.getLastName() == null ? "" : patient.getLastName();
        this.secondLastName = patient.getSurName() == null ? "" : patient.getSurName();
        this.gender = patient.getSex().getId() == null ? 0 : patient.getSex().getId();
        //formato de fecha
        DateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
        Date dateFormat = new Date(patient.getBirthday().getTime());
        this.birthDate = simple.format(dateFormat);
        this.documentAbbr = patient.getDocumentType().getAbbr();
        this.documentName = patient.getDocumentType().getName();
        this.phone = patient.getPhone();
        this.email = patient.getEmail();

        if (!patient.getDiagnostic().isEmpty())
        {
            String value = patient.getDiagnostic().get(0).getComment();
            this.comment = (value != null && value.contains(">") && value.contains("</")) ? value.substring(value.indexOf(">", 0), value.indexOf("</", 0)).replaceFirst(">", "") : "";
        }

        if (!patient.getDemographics().isEmpty())
        {
            this.demographics = MigrationMapper.toDtoDemoNT(patient.getDemographics());

        }

    }

}

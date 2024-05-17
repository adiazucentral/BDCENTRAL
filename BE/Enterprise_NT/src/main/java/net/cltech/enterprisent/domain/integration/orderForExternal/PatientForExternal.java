/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.orderForExternal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.operation.orders.Patient;

/**
 *
 * @author hpoveda
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientForExternal
{

    private String patientIdField;
    private String iDTypeField;
    private Date recordField;
    private String nameField;
    private String lastNameField;
    private Integer genderField;
    private Date BirthDateField;
    private List<DemographicForExternal> demographics = new ArrayList<>();

    //CAMPOS PARA DEMOS 
    private String documentType;
    private String email;
    private String phone;
    private String address;


    public PatientForExternal(Patient patient)
    {
        this.patientIdField = patient.getPatientId();
        this.iDTypeField = String.valueOf(patient.getDocumentType().getId());
        this.recordField = patient.getFirstDate() != null ? patient.getFirstDate() : patient.getLastUpdateDate();
        this.nameField = patient.getName1() + " " + patient.getName2();
        this.lastNameField = patient.getLastName();
        this.genderField = Integer.parseInt(patient.getSex().getCode());
        this.BirthDateField = patient.getBirthday();

        patient.getDemographics()
                .stream()
                .filter(demo -> !Objects.equals(demo.getValue(), null))
                .filter(demo -> !demo.getValue().isEmpty() && !demo.getValue().equals("."))
                .forEach(demo ->
                {
                    DemographicForExternal demographic = new DemographicForExternal(demo);
                    demographic.setType("H");
                    demographics.add(demographic);
                });

        this.documentType = patient.getDocumentType().getName();
        this.email = patient.getEmail();
        this.phone = patient.getPhone();
        this.address = patient.getAddress();
    }

}

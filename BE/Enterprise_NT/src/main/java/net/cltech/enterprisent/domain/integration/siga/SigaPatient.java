/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un paciente para el Siga.
 *
 * @version 1.0.0
 * @author equijano
 * @since 16/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Paciente",
        description = "Representa el objeto de pacientes del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaPatient {

    @ApiObjectField(name = "id", description = "Id del paciente", order = 1)
    private Integer id;
    @ApiObjectField(name = "patientId", description = "Identificacion del paciente", order = 2)
    private String patientId;
    @ApiObjectField(name = "lastName", description = "Apellido del paciente", order = 3)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Segundo nombre del paciente", order = 4)
    private String surName;
    @ApiObjectField(name = "name", description = "Nombre del paciente", order = 5)
    private String name;
    @ApiObjectField(name = "sex", description = "Sexo del paciente", order = 6)
    private Integer sex;
    @ApiObjectField(name = "birthday", description = "Fecha de cumpleaños del paciente", order = 7)
    private String birthday;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

}

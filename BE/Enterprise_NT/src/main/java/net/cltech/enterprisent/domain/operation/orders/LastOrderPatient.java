/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la ultima orden de un paciente en el sistema
 *
 * @version 1.0.0
 * @author omendez
 * @since 07/01/2021
 * @see Creación
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Ultima Orden",
        description = "Representa la ultima orden de un paciente en el sistema"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LastOrderPatient 
{
    @ApiObjectField(name = "orderNumber", description = "Numero de Orden", required = false, order = 1)
    private Long orderNumber;
    @ApiObjectField(name = "createdDateShort", description = "Fecha Creación en Formato yyyymmdd", required = true, order = 2)
    private Integer createdDateShort;
    @ApiObjectField(name = "patientIdDB", description = "Id de base de datos", required = true, order = 3)
    private int patientIdDB;
    @ApiObjectField(name = "patientId", description = "Historia", required = true, order = 4)
    private String patientId;
    @ApiObjectField(name = "documentTypeId", description = "Id tipo documento", required = false, order = 5)
    private int documentTypeId;
    @ApiObjectField(name = "documentType", description = "Tipo Documento", required = false, order = 6)
    private String documentType;
    @ApiObjectField(name = "lastName", description = "Apellido", required = true, order = 7)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Segundo Apellido", required = false, order = 8)
    private String surName;
    @ApiObjectField(name = "name1", description = "Nombre", required = true, order = 9)
    private String name1;
    @ApiObjectField(name = "name2", description = "Segundo Nombre", required = false, order = 10)
    private String name2;
    @ApiObjectField(name = "sex", description = "Sexo", required = true, order = 11)
    private int sex;
    @ApiObjectField(name = "birthday", description = "Fecha Nacimiento", required = true, order = 12)
    private Date birthday;
    @ApiObjectField(name = "tests", description = "examenes", required = false, order = 13)
    private List<Test> tests = new ArrayList<>();

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getCreatedDateShort() {
        return createdDateShort;
    }

    public void setCreatedDateShort(Integer createdDateShort) {
        this.createdDateShort = createdDateShort;
    }

    public int getPatientIdDB() {
        return patientIdDB;
    }

    public void setPatientIdDB(int patientIdDB) {
        this.patientIdDB = patientIdDB;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(int documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
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

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }
}

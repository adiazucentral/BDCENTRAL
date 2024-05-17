/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.pathology.StudyType;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un caso de patologia con datos basicos incluidos en busquedas del LIS.
 *
 * @version 1.0.0
 * @author omendez
 * @since 01/03/2021
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Caso para Busquedas",
        description = "Representa un caso de patologia con datos basicos incluidos en busquedas del LIS."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseSearch 
{
    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos del caso", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "numberCase", description = "Identificador del caso", required = false, order = 2)
    private Long numberCase;
    @ApiObjectField(name = "studyType", description = "Tipo de Estudio", required = true, order = 3)
    private StudyType studyType = new StudyType();
    @ApiObjectField(name = "orderNumber", description = "Numero de Orden", required = true, order = 6)
    private Long orderNumber;
    @ApiObjectField(name = "branch", description = "Sede", required = false, order = 21)
    private Branch branch = new Branch();
    @ApiObjectField(name = "patientIdDB", description = "Id de base de datos", required = true, order = 2)
    private int patientIdDB;
    @ApiObjectField(name = "patientId", description = "Historia", required = true, order = 3)
    private String patientId;
    @ApiObjectField(name = "documentTypeId", description = "Id tipo documento", required = false, order = 4)
    private int documentTypeId;
    @ApiObjectField(name = "documentType", description = "Tipo Documento", required = false, order = 5)
    private String documentType;
    @ApiObjectField(name = "lastName", description = "Apellido", required = true, order = 6)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Segundo Apellido", required = false, order = 7)
    private String surName;
    @ApiObjectField(name = "name1", description = "Nombre", required = true, order = 8)
    private String name1;
    @ApiObjectField(name = "name2", description = "Segundo Nombre", required = false, order = 9)
    private String name2;
    @ApiObjectField(name = "sex", description = "Sexo", required = true, order = 10)
    private int sex;
    @ApiObjectField(name = "birthday", description = "Fecha Nacimiento", required = true, order = 11)
    private Date birthday;
    @ApiObjectField(name = "edad", description = "Edad", required = true, order = 12)
    private String age;
    @ApiObjectField(name = "area", description = "Area", required = false, order = 13)
    private Integer area;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getNumberCase() {
        return numberCase;
    }

    public void setNumberCase(Long numberCase) {
        this.numberCase = numberCase;
    }

    public StudyType getStudyType() {
        return studyType;
    }

    public void setStudyType(StudyType studyType) {
        this.studyType = studyType;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }
}

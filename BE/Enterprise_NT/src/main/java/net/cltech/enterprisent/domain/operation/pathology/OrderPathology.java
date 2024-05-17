/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.enterprisent.domain.masters.common.PathologyAudit;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una orden con datos basicos necesarios para patologia.
 *
 * @version 1.0.0
 * @author omendez
 * @since 06/10/2020
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Orden para patología",
        description = "Representa una orden de laboratorio para patología"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderPathology extends PathologyAudit
{
    @ApiObjectField(name = "numberOrder", description = "Identificador autonumerico de base de datos de la orden", required = false, order = 1)
    private Long numberOrder;
    @ApiObjectField(name = "patientIdDB", description = "Id paciente en la base de datos", required = true, order = 2)
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
    @ApiObjectField(name = "sexId", description = "Id sexo", required = true, order = 10)
    private int sexId;
    @ApiObjectField(name = "sex", description = "Sexo", required = true, order = 11)
    private String sex;
    @ApiObjectField(name = "birthday", description = "Fecha Nacimiento", required = true, order = 12)
    private Date birthday;
    @ApiObjectField(name = "email", description = "Email", required = false, order = 13)
    private String email;
    @ApiObjectField(name = "phone", description = "Telefono", required = false, order = 14)
    private String phone;
    @ApiObjectField(name = "direction", description = "Dirección", required = false, order = 15)
    private String direction;
    @ApiObjectField(name = "idService", description = "id de servicio", required = false, order = 16)
    private Integer idService;
    @ApiObjectField(name = "service", description = "Nombre de servicio", required = false, order = 17)
    private String service;
    @ApiObjectField(name = "idDoctor", description = "id del doctor", required = false, order = 18)
    private Integer idDoctor;
    @ApiObjectField(name = "nameDoctor", description = "Nombre doctor", required = false, order = 19)
    private String nameDoctor;
    @ApiObjectField(name = "lastNameDoctor", description = "Apellido doctor", required = false, order = 20)
    private String lastNameDoctor;
    @ApiObjectField(name = "branch", description = "Sede", required = false, order = 21)
    private Branch branch = new Branch();
    @ApiObjectField(name = "sexCode", description = "Codigo del sexo", required = true, order = 22)
    private String sexCode;
    @ApiObjectField(name = "documentTypeCode", description = "Codigo del Tipo Documento", required = false, order = 23)
    private String documentTypeCode;

    public OrderPathology() {
        branch = new Branch();
    }

    public Long getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(Long numberOrder) {
        this.numberOrder = numberOrder;
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

    public int getSexId() {
        return sexId;
    }

    public void setSexId(int sexId) {
        this.sexId = sexId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getIdService() {
        return idService;
    }

    public void setIdService(Integer idService) {
        this.idService = idService;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
    
    public Integer getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Integer idDoctor) {
        this.idDoctor = idDoctor;
    }

    public String getNameDoctor() {
        return nameDoctor;
    }

    public void setNameDoctor(String nameDoctor) {
        this.nameDoctor = nameDoctor;
    }

    public String getLastNameDoctor() {
        return lastNameDoctor;
    }

    public void setLastNameDoctor(String lastNameDoctor) {
        this.lastNameDoctor = lastNameDoctor;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getSexCode() {
        return sexCode;
    }

    public void setSexCode(String sexCode) {
        this.sexCode = sexCode;
    }

    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }
}

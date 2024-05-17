/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.domain.masters.common.PathologyAudit;
import net.cltech.enterprisent.domain.masters.pathology.StudyType;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un rechazo de muestras de patologia.
 *
 * @version 1.0.0
 * @author omendez
 * @since 26/02/2021
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Rechazo Muestras",
        description = "Representa la informacion de un rechazo de muestras de patologia."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleRejection extends PathologyAudit
{
    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos del caso", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "orderNumber", description = "Numero de Orden", required = true, order = 2)
    private Long orderNumber;
    @ApiObjectField(name = "motive", description = "Motivo del rechazo", required = false, order = 3)
    private Motive motive = new Motive();
    @ApiObjectField(name = "observation", description = "Observación", required = false, order = 4)
    private String observation;
    @ApiObjectField(name = "studyType", description = "Tipo de Estudio", required = true, order = 5)
    private StudyType studyType = new StudyType();
    @ApiObjectField(name = "patientId", description = "Historia", required = true, order = 6)
    private String patientId;
    @ApiObjectField(name = "lastName", description = "Apellido", required = true, order = 7)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Segundo Apellido", required = false, order = 8)
    private String surName;
    @ApiObjectField(name = "name1", description = "Nombre", required = true, order = 9)
    private String name1;
    @ApiObjectField(name = "name2", description = "Segundo Nombre", required = false, order = 10)
    private String name2;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Motive getMotive() {
        return motive;
    }

    public void setMotive(Motive motive) {
        this.motive = motive;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public StudyType getStudyType() {
        return studyType;
    }

    public void setStudyType(StudyType studyType) {
        this.studyType = studyType;
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
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.Item;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un objeto con la informacion rips
 *
 * @version 1.0.0
 * @author omendez
 * @since 21/01/2021
 * @see Creacion
 */
@ApiObject(
        group = "Facturacion",
        name = "Informacion RIPS",
        description = "Representa un objeto con la informacion rips"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InformationRips 
{
    @ApiObjectField(name = "documentType", description = "Tipo de documento", required = true, order = 1)
    private String documentType;
    @ApiObjectField(name = "patientId", description = "Historia", required = true, order = 2)
    private String patientId;
    @ApiObjectField(name = "name1", description = "Nombre 1", required = true, order = 3)
    private String name1;
    @ApiObjectField(name = "name2", description = "Nombre 2", required = true, order = 4)
    private String name2;
    @ApiObjectField(name = "lastName", description = "Apellido 1", required = true, order = 5)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Apellido 2", required = true, order = 6)
    private String surName;
    @ApiObjectField(name = "sex", description = "Sexo", required = false, order = 7)
    private Item sex = new Item();
    @ApiObjectField(name = "birthday", description = "Fecha Nacimiento", required = false, order = 8)
    private Date birthday;
    @ApiObjectField(name = "orderNumber", description = "Numero de Orden", required = true, order = 9)
    private Long orderNumber;
    @ApiObjectField(name = "createdDate", description = "Fecha de Creación", required = true, order = 10)
    private Date createdDate;
    @ApiObjectField(name = "clientCode", description = "Codigo del cliente", required = true, order = 11)
    private String clientCode;
    @ApiObjectField(name = "clientName", description = "Nombre del cliente", required = true, order = 12)
    private String clientName;
    @ApiObjectField(name = "totalTest", description = "Cantidad de exámenes por órden", required = true, order = 13)
    private int totalTest;
    @ApiObjectField(name = "copay", description = "Copago", required = false, order = 14)
    private BigDecimal copay;
    @ApiObjectField(name = "tests", description = "Lista de examenes", order = 15)
    private List<TestsRips> tests = new ArrayList<>();
    @ApiObjectField(name = "demographicRips", description = "Demograficos RIPS", required = true, order = 17)
    private List<DemographicRips> demographicRips;
    @ApiObjectField(name = "totalPaid", description = "Total Pagado", required = true, order = 18)
    private BigDecimal totalPaid;
    
    public InformationRips()
    {
        sex = new Item();
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
    public List<TestsRips> getTests() {
        return tests;
    }

    public void setTests(List<TestsRips> tests) {
        this.tests = tests;
    }

    public List<DemographicRips> getDemographicRips() {
        return demographicRips;
    }

    public void setDemographicRips(List<DemographicRips> demographicRips) {
        this.demographicRips = demographicRips;
    }

    public Item getSex() {
        return sex;
    }

    public void setSex(Item sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getTotalTest() {
        return totalTest;
    }

    public void setTotalTest(int totalTest) {
        this.totalTest = totalTest;
    }
    
    public BigDecimal getCopay() {
        return copay;
    }

    public void setCopay(BigDecimal copay) {
        this.copay = copay;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }
    
    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
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
        final InformationRips other = (InformationRips) obj;
        if (!Objects.equals(this.orderNumber, other.orderNumber))
        {
            return false;
        }
        return true;
    }
}

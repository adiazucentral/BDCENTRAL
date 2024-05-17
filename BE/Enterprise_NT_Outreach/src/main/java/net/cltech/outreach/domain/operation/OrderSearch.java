/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.domain.operation;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una orden con datos basicos incluidos en busquedas.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 08/05/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Orden para Busquedas",
        description = "Representa una orden de laboratorio con pocos datos utilizadas para las busquedas"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderSearch
{

    @ApiObjectField(name = "order", description = "Numero de Orden", required = true, order = 1)
    private long order;
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
    @ApiObjectField(name = "age", description = "Edad", required = true, order = 12)
    private String age;

    public long getOrder()
    {
        return order;
    }

    public void setOrder(long order)
    {
        this.order = order;
    }

    public int getPatientIdDB()
    {
        return patientIdDB;
    }

    public void setPatientIdDB(int patientIdDB)
    {
        this.patientIdDB = patientIdDB;
    }

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public int getDocumentTypeId()
    {
        return documentTypeId;
    }

    public void setDocumentTypeId(int documentTypeId)
    {
        this.documentTypeId = documentTypeId;
    }

    public String getDocumentType()
    {
        return documentType;
    }

    public void setDocumentType(String documentType)
    {
        this.documentType = documentType;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getSurName()
    {
        return surName;
    }

    public void setSurName(String surName)
    {
        this.surName = surName;
    }

    public String getName1()
    {
        return name1;
    }

    public void setName1(String name1)
    {
        this.name1 = name1;
    }

    public String getName2()
    {
        return name2;
    }

    public void setName2(String name2)
    {
        this.name2 = name2;
    }

    public int getSex()
    {
        return sex;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public String getAge()
    {
        return age;
    }

    public void setAge(String age)
    {
        this.age = age;
    }

}

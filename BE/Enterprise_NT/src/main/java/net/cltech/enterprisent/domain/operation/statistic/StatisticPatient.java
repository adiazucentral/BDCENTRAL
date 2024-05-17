/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.statistic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa a un paciente de la aplicacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 30/06/2017
 * @see Creacion
 */
@ApiObject(
        group = "Estadisticas",
        name = "Paciente",
        description = "Representa un paciente para estadisticas"
)
@JsonInclude(Include.NON_NULL)
public class StatisticPatient
{

    @ApiObjectField(name = "id", description = "Id base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "patientId", description = "Historia", required = true, order = 2)
    private String patientId;
    @ApiObjectField(name = "name1", description = "Nombre 1", required = true, order = 3)
    private String name1;
    @ApiObjectField(name = "name2", description = "Nombre 2", required = false, order = 4)
    private String name2;
    @ApiObjectField(name = "lastName", description = "Apellido 1", required = true, order = 5)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Apellido 2", required = false, order = 6)
    private String surName;
    @ApiObjectField(name = "sex", description = "Sexo", required = false, order = 7)
    private Integer gender;
    @ApiObjectField(name = "genderSpanish", description = "Genero - Español", required = false, order = 8)
    private String genderSpanish;
    @ApiObjectField(name = "genderSpanish", description = "Genero - Ingles", required = false, order = 9)
    private String genderEnglish;
    @ApiObjectField(name = "documentType", description = "Tipo Documento", required = false, order = 10)
    private Integer documentType;
    @ApiObjectField(name = "documentTypeCode", description = "Código Tipo Documento", required = false, order = 11)
    private String documentTypeCode;
    @ApiObjectField(name = "documentTypeName", description = "Nombre Tipo Documento", required = false, order = 12)
    private String documentTypeName;
    @ApiObjectField(name = "race", description = "Id de la Raza", required = true, order = 13)
    private Integer race;
    @ApiObjectField(name = "raceCode", description = "Código de la raza", required = false, order = 14)
    private String raceCode;
    @ApiObjectField(name = "raceName", description = "Nombre raza", required = true, order = 15)
    private String raceName;
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 16)
    private List<StatisticDemographic> demographics = new ArrayList<>();
    @ApiObjectField(name = "orderNumber", description = "Número de la orden", required = false, order = 17)
    private Long orderNumber;
    @ApiObjectField(name = "dob", description = "Fecha de nacimiento", required = false, order = 18)
    private Date dob;

    public StatisticPatient()
    {

    }

    public StatisticPatient(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
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

    public List<StatisticDemographic> getDemographics()
    {
        return demographics;
    }

    public void setDemographics(List<StatisticDemographic> demographics)
    {
        this.demographics = demographics;
    }

    public Long getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public Integer getGender()
    {
        return gender;
    }

    public void setGender(Integer gender)
    {
        this.gender = gender;
    }

    public String getGenderSpanish()
    {
        return genderSpanish;
    }

    public void setGenderSpanish(String genderSpanish)
    {
        this.genderSpanish = genderSpanish;
    }

    public String getGenderEnglish()
    {
        return genderEnglish;
    }

    public void setGenderEnglish(String genderEnglish)
    {
        this.genderEnglish = genderEnglish;
    }

    public Integer getDocumentType()
    {
        return documentType;
    }

    public void setDocumentType(Integer documentType)
    {
        this.documentType = documentType;
    }

    public String getDocumentTypeCode()
    {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(String documentTypeCode)
    {
        this.documentTypeCode = documentTypeCode;
    }

    public String getDocumentTypeName()
    {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName)
    {
        this.documentTypeName = documentTypeName;
    }

    public Integer getRace()
    {
        return race;
    }

    public void setRace(Integer race)
    {
        this.race = race;
    }

    public String getRaceCode()
    {
        return raceCode;
    }

    public void setRaceCode(String raceCode)
    {
        this.raceCode = raceCode;
    }

    public String getRaceName()
    {
        return raceName;
    }

    public void setRaceName(String raceName)
    {
        this.raceName = raceName;
    }

    public Date getDob()
    {
        return dob;
    }

    public void setDob(Date dob)
    {
        this.dob = dob;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
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
        final StatisticPatient other = (StatisticPatient) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}

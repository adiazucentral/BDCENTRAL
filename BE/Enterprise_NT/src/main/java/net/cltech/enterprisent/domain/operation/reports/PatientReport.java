/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la clase del rango de paginacion del pacientes
 *
 * @version 1.0.0
 * @author equijano
 * @since 04/02/2019
 * @see Creación
 */
@ApiObject(
        group = "Operación - Informes",
        name = "Paginacion de pacientes",
        description = "Representa el objeto del rango de paginacion del paciente."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientReport
{

    @ApiObjectField(name = "page", description = "Numero de la pagina comenzar desde la pagina 1", order = 1)
    private Integer page;
    @ApiObjectField(name = "sizePage", description = "Tamaño de la pagina", order = 2)
    private Integer sizePage;
    @ApiObjectField(name = "demographics", description = "Lista de demograficos", order = 3)
    private List<FilterDemographic> demographics;
    @ApiObjectField(name = "demographics", description = "Lista de demograficos", order = 3)
    private List<Demographic> demographicsQuery;
    @ApiObjectField(name = "email", description = "Correo del paciente", order = 4)
    private boolean email;
    @ApiObjectField(name = "size", description = "Talla del paciente", order = 5)
    private boolean size;
    @ApiObjectField(name = "weight", description = "Peso del paciente", order = 6)
    private boolean weight;
    @ApiObjectField(name = "dateOfDeath", description = "Fecha de fallecimiento del paciente", order = 7)
    private boolean dateOfDeath;
    @ApiObjectField(name = "photo", description = "Foto del paciente", order = 8)
    private boolean photo;
    @ApiObjectField(name = "race", description = "Raza del paciente", order = 9)
    private boolean race;
    @ApiObjectField(name = "phone", description = "Telefono del paciente", order = 10)
    private boolean phone;
    @ApiObjectField(name = "address", description = "Direccion del paciente", order = 11)
    private boolean address;

    public PatientReport()
    {
        this.email = false;
        this.size = false;
        this.weight = false;
        this.dateOfDeath = false;
        this.photo = false;
        this.race = false;
        this.phone = false;
        this.address = false;
    }

    public Integer getPage()
    {
        return page;
    }

    public void setPage(Integer page)
    {
        this.page = page;
    }

    public Integer getSizePage()
    {
        return sizePage;
    }

    public void setSizePage(Integer sizePage)
    {
        this.sizePage = sizePage;
    }

    public List<FilterDemographic> getDemographics()
    {
        return demographics;
    }

    public void setDemographics(List<FilterDemographic> demographics)
    {
        this.demographics = demographics;
    }

    public boolean isEmail()
    {
        return email;
    }

    public void setEmail(boolean email)
    {
        this.email = email;
    }

    public boolean isSize()
    {
        return size;
    }

    public void setSize(boolean size)
    {
        this.size = size;
    }

    public boolean isWeight()
    {
        return weight;
    }

    public void setWeight(boolean weight)
    {
        this.weight = weight;
    }

    public boolean isDateOfDeath()
    {
        return dateOfDeath;
    }

    public void setDateOfDeath(boolean dateOfDeath)
    {
        this.dateOfDeath = dateOfDeath;
    }

    public boolean isPhoto()
    {
        return photo;
    }

    public void setPhoto(boolean photo)
    {
        this.photo = photo;
    }

    public boolean isRace()
    {
        return race;
    }

    public void setRace(boolean race)
    {
        this.race = race;
    }

    public boolean isPhone()
    {
        return phone;
    }

    public void setPhone(boolean phone)
    {
        this.phone = phone;
    }

    public boolean isAddress()
    {
        return address;
    }

    public void setAddress(boolean address)
    {
        this.address = address;
    }

    public List<Demographic> getDemographicsQuery()
    {
        return demographicsQuery;
    }

    public void setDemographicsQuery(List<Demographic> demographicsQuery)
    {
        this.demographicsQuery = demographicsQuery;
    }
    
    

}

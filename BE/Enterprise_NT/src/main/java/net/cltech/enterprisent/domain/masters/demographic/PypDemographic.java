/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.demographic;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Almacena información de examenes asignados a items de promoción y prevención
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 10/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Demografico",
        name = "Demografico PyP",
        description = "Muestra informacion del maestro Demografico PyP"
)
public class PypDemographic extends MasterAudit
{

    @ApiObjectField(name = "id", description = "id demografico item", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "gender", description = "Género del paciente (Listado)", required = true, order = 2)
    private int gender;
    @ApiObjectField(name = "unit", description = "Unidad de tiempo dada en días(0) o años(1)", required = true, order = 3)
    private short unit;
    @ApiObjectField(name = "minAge", description = "Edad minima", required = true, order = 4)
    private int minAge;
    @ApiObjectField(name = "maxAge", description = "Edad maxima", required = true, order = 5)
    private int maxAge;
    @ApiObjectField(name = "tests", description = "Examenes de promoción y prevención", required = true, order = 6)
    private List<TestBasic> tests;
    @ApiObjectField(name = "nameGender", description = "Nombre Género del paciente", required = true, order = 7)
    private String nameGender;
    @ApiObjectField(name = "nameUnit", description = "Nombre de la Unidad de tiempo dada en días(0) o años(1)", required = true, order = 8)
    private String nameUnit;
    @ApiObjectField(name = "demographicItemName", description = "Nombre del item demografico", required = true, order = 9)
    private String demographicItemName;

    public PypDemographic()
    {
        tests = new ArrayList<>();
    }

    public List<TestBasic> getTests()
    {
        return tests;
    }

    public void setTests(List<TestBasic> tests)
    {
        this.tests = tests;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public int getGender()
    {
        return gender;
    }

    public void setGender(Integer gender)
    {
        this.gender = gender;
    }

    public short getUnit()
    {
        return unit;
    }

    public void setUnit(short unit)
    {
        this.unit = unit;
    }

    public int getMinAge()
    {
        return minAge;
    }

    public void setMinAge(Integer minAge)
    {
        this.minAge = minAge;
    }

    public int getMaxAge()
    {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge)
    {
        this.maxAge = maxAge;
    }

    public String getNameGender()
    {
        return nameGender;
    }

    public void setNameGender(String nameGender)
    {
        this.nameGender = nameGender;
    }

    public String getNameUnit()
    {
        return nameUnit;
    }

    public void setNameUnit(String nameUnit)
    {
        this.nameUnit = nameUnit;
    }

    public String getDemographicItemName()
    {
        return demographicItemName;
    }

    public void setDemographicItemName(String demographicItemName)
    {
        this.demographicItemName = demographicItemName;
    }

}

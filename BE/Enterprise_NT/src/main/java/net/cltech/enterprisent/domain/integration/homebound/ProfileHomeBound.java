/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.homebound;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.TestInformation;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion basica del maestro Pruebas y perfiles
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 17/02/2020
 * @see Creación
 */
@ApiObject(
        group = "Pruebas",
        name = "Prueba Home Bound",
        description = "Muestra informacion basica del maestro Pruebas que usa el API"
)
public class ProfileHomeBound
{

    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "abbr", description = "Abreviatura", required = true, order = 3)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "type", description = "Tipo de Examen", required = true, order = 9)
    private Integer type;
    @ApiObjectField(name = "active", description = "Indica si esta activo", required = true, order = 8)
    private boolean active;
    @ApiObjectField(name = "tests", description = "Lista de examenes para perfiles /paquetes", required = true, order = 10)
    private List<TestInformation> tests;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public List<TestInformation> getTests()
    {
        return tests;
    }

    public void setTests(List<TestInformation> tests)
    {
        this.tests = tests;
    }

 }

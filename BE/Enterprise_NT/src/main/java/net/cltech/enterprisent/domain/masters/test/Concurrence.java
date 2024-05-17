/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de las concurrencias del maestro Pruebas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 27/06/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Concurrencia",
        description = "Muestra informacion de las concurrencias del maestro Pruebas que usa el API"
)
public class Concurrence
{

    @ApiObjectField(name = "idTest", description = "Id prueba", required = true, order = 1)
    private Integer idTest;
    @ApiObjectField(name = "concurrence", description = "Concurrencia", required = true, order = 2)
    private TestBasic concurrence;
    @ApiObjectField(name = "gender", description = "Genero de la prueba", required = true, order = 3)
    private Integer gender;
    @ApiObjectField(name = "minAge", description = "Edad minima de la prueba", required = true, order = 4)
    private Integer minAge;
    @ApiObjectField(name = "maxAge", description = "Edad maxima de la prueba", required = true, order = 5)
    private Integer maxAge;
    @ApiObjectField(name = "unitAge", description = "Unidad edad de la prueba", required = true, order = 6)
    private Short unitAge;
    @ApiObjectField(name = "formula", description = "Esta en formula", required = true, order = 7)
    private boolean formula;
    @ApiObjectField(name = "selected", description = "Esta en concurrencia", required = true, order = 8)
    private boolean selected;
    @ApiObjectField(name = "quantity", description = "Cantidad de concurrencias del examen", required = true, order = 5)
    private Integer quantity;
    @ApiObjectField(name = "sample", description = "Muestra de la prueba", required = true, order = 7)
    private Sample sample;


    public Concurrence()
    {
        concurrence = new TestBasic();
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public Integer getIdTest()
    {
        return idTest;
    }

    public void setIdTest(Integer idTest)
    {
        this.idTest = idTest;
    }

    public TestBasic getConcurrence()
    {
        return concurrence;
    }

    public void setConcurrence(TestBasic concurrence)
    {
        this.concurrence = concurrence;
    }

    public Integer getGender()
    {
        return gender;
    }

    public void setGender(Integer gender)
    {
        this.gender = gender;
    }

    public Integer getMinAge()
    {
        return minAge;
    }

    public void setMinAge(Integer minAge)
    {
        this.minAge = minAge;
    }

    public Integer getMaxAge()
    {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge)
    {
        this.maxAge = maxAge;
    }

    public Short getUnitAge()
    {
        return unitAge;
    }

    public void setUnitAge(Short unitAge)
    {
        this.unitAge = unitAge;
    }

    public boolean isFormula()
    {
        return formula;
    }

    public void setFormula(boolean formula)
    {
        this.formula = formula;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

}

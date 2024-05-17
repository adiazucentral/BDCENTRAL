/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de relación de dias alarma y exámenes.
 *
 * @version 1.0.0
 * @author mmunoz
 * @since 10/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Días Alarma",
        description = "Muestra información de relación de días alarma y exámenes"
)

public class AlarmDays
{

    @ApiObjectField(name = "test", description = "examen", required = true, order = 1)
    private List<TestBasic> test;
    @ApiObjectField(name = "demographic", description = "demografico", required = true, order = 2)
    private Demographic demographic;

    public List<TestBasic> getTest()
    {
        return test;
    }

    public void setTest(List<TestBasic> test)
    {
        this.test = test;
    }

    public Demographic getDemographic()
    {
        return demographic;
    }

    public void setDemographic(Demographic demographic)
    {
        this.demographic = demographic;
    }

    public AlarmDays()
    {
        demographic = new Demographic();
        test = new ArrayList<>();
    }

}

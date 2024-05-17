/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para busquedas en estadisticas rapidas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 10/04/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Filtros",
        name = "Estadisticas Rapidas",
        description = "Representa filtro con parametros para busquedas en estadisticas rapidas."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgileStatisticFilter
{
    @ApiObjectField(name = "init", description = "Rango inicial", order = 1)
    private Integer init;
    @ApiObjectField(name = "end", description = "Rango final", order = 2)
    private Integer end;
    @ApiObjectField(name = "years", description = "Años", order = 3)
    private List<String> years;
    @ApiObjectField(name = "tests", description = "Examenes", order = 4)
    private List<Integer> tests;
    @ApiObjectField(name = "branches", description = "Sedes", order = 5)
    private List<Integer> branches;

    public Integer getInit()
    {
        return init;
    }

    public void setInit(Integer init)
    {
        this.init = init;
    }

    public Integer getEnd()
    {
        return end;
    }

    public void setEnd(Integer end)
    {
        this.end = end;
    }

    public List<String> getYears()
    {
        return years;
    }

    public void setYears(List<String> years)
    {
        this.years = years;
    }

    public List<Integer> getTests()
    {
        return tests;
    }

    public void setTests(List<Integer> tests)
    {
        this.tests = tests;
    }

    public List<Integer> getBranches()
    {
        return branches;
    }

    public void setBranches(List<Integer> branches)
    {
        this.branches = branches;
    }
    
}

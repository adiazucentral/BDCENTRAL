/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para busquedas de informacion rips
 *
 * @version 1.0.0
 * @author omendez
 * @since 21/01/2021
 * @see Creación
 */
@ApiObject(
        group = "Operación - Filtros",
        name = "Rips",
        description = "Representa filtro con parametros para busquedas de informacion rips."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RipsFilter {
    
    @ApiObjectField(name = "rangeType", description = "Rango de busqueda: 0 -> Fecha Ingreso<br> 1-> Rango de ordenes", order = 1)
    private Integer rangeType;
    @ApiObjectField(name = "init", description = "Rango inicial", order = 2)
    private Long init;
    @ApiObjectField(name = "end", description = "Rango final", order = 3)
    private Long end;
    @ApiObjectField(name = "demographics", description = "Lista de filtro por demograficos", order = 4)
    private List<FilterDemographic> demographics = new ArrayList<>();
    @ApiObjectField(name = "testFilterType", description = "Filtro por examen: 0 - Todos<br> 1 - Sección<br> 2-Examen<br> 3-Confidenciales", order = 5)
    private int testFilterType;
    @ApiObjectField(name = "tests", description = "id´s Examenes/Secciónes a filtrar", order = 6)
    private List<Integer> tests;
    @ApiObjectField(name = "billing", description = "1-> Con factura, 0-> Sin factura", order = 7)
    private int billing;
    

    public Integer getRangeType() {
        return rangeType;
    }

    public void setRangeType(Integer rangeType) {
        this.rangeType = rangeType;
    }

    public Long getInit() {
        return init;
    }

    public void setInit(Long init) {
        this.init = init;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public List<FilterDemographic> getDemographics() {
        return demographics;
    }

    public void setDemographics(List<FilterDemographic> demographics) {
        this.demographics = demographics;
    }

    public int getTestFilterType() {
        return testFilterType;
    }

    public void setTestFilterType(int testFilterType) {
        this.testFilterType = testFilterType;
    }

    public List<Integer> getTests() {
        return tests;
    }

    public void setTests(List<Integer> tests) {
        this.tests = tests;
    }
    
    public int getBilling() {
        return billing;
    }

    public void setBilling(int billing) {
        this.billing = billing;
    }
}

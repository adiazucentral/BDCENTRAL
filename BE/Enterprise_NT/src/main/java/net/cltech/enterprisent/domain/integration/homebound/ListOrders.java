/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto para la consulta de lista de ordenes registradas por un filtro especifico
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/01/2021
 * @see Creación
 */
@ApiObject(
        group = "HomeBound",
        name = "Filtro Lista de Ordenes",
        description = "Representa el objeto para la consulta de lista de ordenes registradas por un filtro especifico"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListOrders {
    
    @ApiObjectField(name = "orders", description = "Lista de ordenes", required = true, order = 1)
    private List<Long> orders;
    @ApiObjectField(name = "tests", description = "Lista de examenes", required = true, order = 2)
    private List<Integer> tests;
    @ApiObjectField(name = "filterdemographics", description = "Lista de demos", required = true, order = 2)
    private List<Integer> filterdemographics;
    @ApiObjectField(name = "demographics", description = "Lista de filtro por demograficos", order = 3)
    private List<FilterDemographic> demographics = new ArrayList<>();
    @ApiObjectField(name = "samplestate", description = "Lista de estados a filtrar", required = true, order = 2)
    private Integer samplestate;
    @ApiObjectField(name = "samplestatelis", description = "Lista de estados del LIS para filtrar", required = true, order = 2)
    private List<Integer> samplestatelis;
    
    public List<Long> getOrders() {
        return orders;
    }

    public void setOrders(List<Long> orders) {
        this.orders = orders;
    }

    public List<Integer> getTests() {
        return tests;
    }

    public void setTests(List<Integer> tests) {
        this.tests = tests;
    }

    public List<Integer> getFilterdemographics() {
        return filterdemographics;
    }

    public void setFilterdemographics(List<Integer> filterdemographics) {
        this.filterdemographics = filterdemographics;
    }
    
    public List<FilterDemographic> getDemographics() {
        return demographics;
    }

    public void setDemographics(List<FilterDemographic> demographics) {
        this.demographics = demographics;
    }

    public Integer getSamplestate() {
        return samplestate;
    }

    public void setSamplestate(Integer samplestate) {
        this.samplestate = samplestate;
    }

    public List<Integer> getSamplestatelis() {
        return samplestatelis;
    }

    public void setSamplestatelis(List<Integer> samplestatelis) {
        this.samplestatelis = samplestatelis;
    }
    
    
}

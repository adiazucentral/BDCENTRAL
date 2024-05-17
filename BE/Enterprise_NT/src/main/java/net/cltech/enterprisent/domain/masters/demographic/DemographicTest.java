/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.LinkedList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro de examenes por demograficos
 *
 * @version 1.0.0
 * @author omendez
 * @since 31/01/2022
 * @see Creación
 */
@ApiObject(
        group = "Demografico",
        name = "Examenes por Demografico",
        description = "Muestra informacion del maestro examenes por demograficos"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DemographicTest extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Id de la relación", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "idDemographic1", description = "Demografico 1", required = true, order = 2)
    private Integer idDemographic1;
    @ApiObjectField(name = "valueDemographic1", description = "Valor Demografico 1", required = true, order = 3)
    private Integer valueDemographic1;
    @ApiObjectField(name = "idDemographic2", description = "Demografico 2", required = true, order = 4)
    private Integer idDemographic2;
    @ApiObjectField(name = "valueDemographic2", description = "Valor Demografico 2", required = true, order = 5)
    private Integer valueDemographic2;
    @ApiObjectField(name = "idDemographic3", description = "Demografico 3", required = true, order = 6)
    private Integer idDemographic3;
    @ApiObjectField(name = "valueDemographic3", description = "Valor Demografico 3", required = true, order = 7)
    private Integer valueDemographic3;
    @ApiObjectField(name = "tests", description = "Pruebas", required = true, order = 8)
    private List<Integer> tests = new LinkedList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdDemographic1() {
        return idDemographic1;
    }

    public void setIdDemographic1(Integer idDemographic1) {
        this.idDemographic1 = idDemographic1;
    }

    public Integer getValueDemographic1() {
        return valueDemographic1;
    }

    public void setValueDemographic1(Integer valueDemographic1) {
        this.valueDemographic1 = valueDemographic1;
    }

    public Integer getIdDemographic2() {
        return idDemographic2;
    }

    public void setIdDemographic2(Integer idDemographic2) {
        this.idDemographic2 = idDemographic2;
    }

    public Integer getValueDemographic2() {
        return valueDemographic2;
    }

    public void setValueDemographic2(Integer valueDemographic2) {
        this.valueDemographic2 = valueDemographic2;
    }

    public Integer getIdDemographic3() {
        return idDemographic3;
    }

    public void setIdDemographic3(Integer idDemographic3) {
        this.idDemographic3 = idDemographic3;
    }

    public Integer getValueDemographic3() {
        return valueDemographic3;
    }

    public void setValueDemographic3(Integer valueDemographic3) {
        this.valueDemographic3 = valueDemographic3;
    }

    public List<Integer> getTests() {
        return tests;
    }

    public void setTests(List<Integer> tests) {
        this.tests = tests;
    }

}

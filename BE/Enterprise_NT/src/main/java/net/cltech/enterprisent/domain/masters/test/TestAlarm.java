/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro de alarmas de examenes desde ingreso de ordnes
 *
 * @version 1.0.0
 * @author omendez
 * @since 16/12/2021
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Alarma Prueba",
        description = "Muestra informacion del maestro de alarmas de examenes desde ingreso de ordenes que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestAlarm 
{
    @ApiObjectField(name = "idArea", description = "Id area de la lista de examenes", required = true, order = 1)
    private Integer idArea;
    @ApiObjectField(name = "tests", description = "Lista de pruebas", required = true, order = 2)
    private List<TestBasic> tests;
    @ApiObjectField(name = "user", description = "Id usuario que modifica la relación", required = true, order = 3)
    private Integer user;

    public Integer getIdArea() {
        return idArea;
    }

    public void setIdArea(Integer idArea) {
        this.idArea = idArea;
    }

    public List<TestBasic> getTests() {
        return tests;
    }

    public void setTests(List<TestBasic> tests) {
        this.tests = tests;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }
}

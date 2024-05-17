/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.operation.orders.Test;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.jsondoc.core.pojo.ApiVisibility;

/**
 * Representa un examen que sera retomado
 *
 * @version 1.0.0
 * @author dcortes
 * @since 26/04/2018
 * @see Creacion
 */
@ApiObject(
        group = "Trazabilidad",
        name = "Trazabilidad de la Muestra",
        description = "Muestra la Trazabilidad de la muestra que usa el API",
        visibility = ApiVisibility.PRIVATE
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestToRecall
{

    @ApiObjectField(name = "id", description = "Id de la retoma", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "tests", description = "Examen ", required = true, order = 2)
    private List<Test> tests;

    public TestToRecall()
    {
        tests = new ArrayList<>(0);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public List<Test> getTests()
    {
        return tests;
    }

    public void setTests(List<Test> tests)
    {
        this.tests = tests;
    }
}

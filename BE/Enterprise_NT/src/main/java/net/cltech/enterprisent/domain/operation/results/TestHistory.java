/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.results;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la lissta de pruebas con sus resultados historicos
 *
 * @version 1.0.0
 * @author jblanco
 * @since 25/04/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Historico Resultados Examen",
        description = "Representa la lissta de pruebas con sus resultados historicos"
)
public class TestHistory
{

    @ApiObjectField(name = "testId", description = "Identificador de la prueba", required = true, order = 1)
    private int testId;
    @ApiObjectField(name = "testCode", description = "Código de la prueba", required = true, order = 3)
    private String testCode;
    @ApiObjectField(name = "testName", description = "Nombre de la prueba", required = true, order = 4)
    private String testName;
    @ApiObjectField(name = "abbr", description = "Abreviatura de la prueba", required = true, order = 4)
    private String abbr;
    @ApiObjectField(name = "resultType", description = "Tipo de resultado para el examen", required = true, order = 10)
    private short resultType;
    @ApiObjectField(name = "history", description = "Lista de resultados históricos", required = false, order = 2)
    private List<ResultTestHistory> history;

    public int getTestId()
    {
        return testId;
    }

    public void setTestId(int testId)
    {
        this.testId = testId;
    }

    public String getTestCode()
    {
        return testCode;
    }

    public void setTestCode(String testCode)
    {
        this.testCode = testCode;
    }

    public short getResultType()
    {
        return resultType;
    }

    public void setResultType(short resultType)
    {
        this.resultType = resultType;
    }

    public String getTestName()
    {
        return testName;
    }

    public void setTestName(String testName)
    {
        this.testName = testName;
    }

    public List<ResultTestHistory> getHistory()
    {
        return history;
    }

    public void setHistory(List<ResultTestHistory> history)
    {
        this.history = history;
    }
    
     public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

}

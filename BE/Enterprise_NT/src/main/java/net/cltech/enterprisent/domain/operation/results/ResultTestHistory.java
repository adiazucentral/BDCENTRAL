/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.results;

import java.math.BigDecimal;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el historico del resultado de una prueba
 *
 * @version 1.0.0
 * @author jblanco
 * @since Mar 25, 2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Historico Resultados Examen",
        description = "Representa el historico de un resultado de un examen para el registro de resultados"
)
public class ResultTestHistory
{

    @ApiObjectField(name = "order", description = "Número de orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "testId", description = "Identificador del examen", required = true, order = 2)
    private int testId;
    @ApiObjectField(name = "testCode", description = "Código de la prueba", required = true, order = 3)
    private String testCode;
    @ApiObjectField(name = "testName", description = "Nombre de la prueba", required = true, order = 4)
    private String testName;
    @ApiObjectField(name = "abbr", description = "Abreviatura de la prueba", required = true, order = 4)
    private String abbr;
    @ApiObjectField(name = "result", description = "Resultado del examen", required = true, order = 5)
    private String result;
    @ApiObjectField(name = "validateDate", description = "Fecha de validación del resultado", required = true, order = 6)
    private Date validateDate;
    @ApiObjectField(name = "refMin", description = "Referencia mínima", required = false, order = 7)
    private BigDecimal refMin;
    @ApiObjectField(name = "refMax", description = "Referecia máxima", required = false, order = 8)
    private BigDecimal refMax;
    @ApiObjectField(name = "refLiteral", description = "Referencia para resultados literales", required = false, order = 9)
    private String refLiteral;
    @ApiObjectField(name = "resultType", description = "Tipo de resultado para el examen", required = true, order = 10)
    private short resultType;
    @ApiObjectField(name = "resultNumber", description = "Resultado del examen en formato númerico", required = false, order = 11)
    private BigDecimal resultNumber;
    @ApiObjectField(name = "pathology", description = "Patología del examen con respecto al valor de referencia", required = true, order = 12)
    private int pathology;
    @ApiObjectField(name = "entryDate", description = "Fecha de ingreso", required = false, order = 57)
    private Date entryDate;

    public ResultTestHistory()
    {
    }

    public long getOrder()
    {
        return order;
    }

    public void setOrder(long order)
    {
        this.order = order;
    }

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

    public String getTestName()
    {
        return testName;
    }

    public void setTestName(String testName)
    {
        this.testName = testName;
    }

    public Date getValidateDate()
    {
        return validateDate;
    }

    public void setValidateDate(Date validateDate)
    {
        this.validateDate = validateDate;
    }

    public BigDecimal getRefMin()
    {
        return refMin;
    }

    public void setRefMin(BigDecimal refMin)
    {
        this.refMin = refMin;
    }

    public String getRefLiteral()
    {
        return refLiteral;
    }

    public void setRefLiteral(String refLiteral)
    {
        this.refLiteral = refLiteral;
    }

    public short getResultType()
    {
        return resultType;
    }

    public void setResultType(short resultType)
    {
        this.resultType = resultType;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public BigDecimal getRefMax()
    {
        return refMax;
    }

    public void setRefMax(BigDecimal refMax)
    {
        this.refMax = refMax;
    }

    public BigDecimal getResultNumber()
    {
        return resultNumber;
    }

    public void setResultNumber(BigDecimal resultNumber)
    {
        this.resultNumber = resultNumber;
    }

    public int getPathology()
    {
        return pathology;
    }

    public void setPathology(int pathology)
    {
        this.pathology = pathology;
    }
    
    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }
    
    public Date getEntryDate()
    {
        return entryDate;
    }

    public void setEntryDate(Date entryDate)
    {
        this.entryDate = entryDate;
    }
}

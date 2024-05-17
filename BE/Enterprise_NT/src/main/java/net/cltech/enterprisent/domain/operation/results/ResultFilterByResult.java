/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.results;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un el filtro de resultados por el resultado de la prueba
 *
 * @version 1.0.0
 * @author jblanco
 * @since 12/04/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados - Filtro",
        name = "Filtro Por Resultado",
        description = "Representa un el filtro de órdenes por el resultado de la prueba"
)
public class ResultFilterByResult
{

    @ApiObjectField(name = "test", description = "Examen", required = false, order = 1)
    private Integer test;
    @ApiObjectField(name = "operator", description = "Operador del filtro", required = false, order = 1)
    private String operator;
    @ApiObjectField(name = "result1", description = "Primer resultado del filtro", required = false, order = 2)
    private String result1;
    @ApiObjectField(name = "result2", description = "Segundo resultado del filtro", required = false, order = 3)
    private String result2;

    public ResultFilterByResult()
    {
    }

    public Integer getTest()
    {
        return test;
    }

    public void setTest(Integer test)
    {
        this.test = test;
    }

    public String getOperator()
    {
        return operator;
    }

    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getResult1()
    {
        return result1;
    }

    public void setResult1(String result1)
    {
        this.result1 = result1;
    }

    public String getResult2()
    {
        return result2;
    }

    public void setResult2(String result2)
    {
        this.result2 = result2;
    }
    
}


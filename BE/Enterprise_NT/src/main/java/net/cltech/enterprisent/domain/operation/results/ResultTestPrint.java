/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.cltech.enterprisent.domain.operation.results;


import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la información para el bloqueo o desbloqueo de una prueba
 * @version 1.0.0
 * @author jblanco
 * @since Jun 10, 2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Impresion de un Examen",
        description = "Representa la información para el cambiar el estado de impresion de un examen"
)
public class ResultTestPrint 
{
    @ApiObjectField(name = "order", description = "Número de orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "testId", description = "Identificador del examen", required = true, order = 2)
    private int testId;
    @ApiObjectField(name = "print", description = "estado de impresion de un examen", required = true, order = 3)
    private boolean print;


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

    public boolean isPrint()
    {
        return print;
    }

    public void setPrint(boolean print)
    {
        this.print = print;
    }
}


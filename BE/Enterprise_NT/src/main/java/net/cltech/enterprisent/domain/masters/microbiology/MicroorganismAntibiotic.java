/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.microbiology;

import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Microorganismo - Antibiotico
 *
 * @version 1.0.0
 * @author cmartin
 * @since 22/02/2018
 * @see Creación
 */
@ApiObject(
        group = "Microbiología",
        name = "Microorganismo - Antibiotico",
        description = "Muestra informacion del maestro Microorganismo - Antibiotico que usa el API"
)
public class MicroorganismAntibiotic extends MasterAudit
{

    @ApiObjectField(name = "microorganism", description = "Microorganismo", required = true, order = 1)
    private Microorganism microorganism = new Microorganism();
    @ApiObjectField(name = "antibiotic", description = "Antibiotico", required = true, order = 1)
    private Antibiotic antibiotic = new Antibiotic();
    @ApiObjectField(name = "method", description = "Metodo: 1 -> Manual,  2 -> Disco", required = true, order = 4)
    private Short method;
    @ApiObjectField(name = "interpretation", description = "Interpretación: 1 -> Sensible, 2 -> Intermedio, 3 -> Resistente", required = true, order = 5)
    private Short interpretation;
    @ApiObjectField(name = "valueMin", description = "Valor Minimo o Valor Texto", required = true, order = 6)
    private String valueMin;
    @ApiObjectField(name = "valueMax", description = "Valor Maximo", required = true, order = 7)
    private String valueMax;
    @ApiObjectField(name = "operation", description = "Operación", required = true, order = 8)
    private Item operation = new Item();

    public Microorganism getMicroorganism()
    {
        return microorganism;
    }

    public void setMicroorganism(Microorganism microorganism)
    {
        this.microorganism = microorganism;
    }

    public Antibiotic getAntibiotic()
    {
        return antibiotic;
    }

    public void setAntibiotic(Antibiotic antibiotic)
    {
        this.antibiotic = antibiotic;
    }

    public Short getMethod()
    {
        return method;
    }

    public void setMethod(Short method)
    {
        this.method = method;
    }

    public Short getInterpretation()
    {
        return interpretation;
    }

    public void setInterpretation(Short interpretation)
    {
        this.interpretation = interpretation;
    }

    public String getValueMin()
    {
        return valueMin;
    }

    public void setValueMin(String valueMin)
    {
        this.valueMin = valueMin;
    }

    public String getValueMax()
    {
        return valueMax;
    }

    public void setValueMax(String valueMax)
    {
        this.valueMax = valueMax;
    }

    public Item getOperation()
    {
        return operation;
    }

    public void setOperation(Item operation)
    {
        this.operation = operation;
    }

}

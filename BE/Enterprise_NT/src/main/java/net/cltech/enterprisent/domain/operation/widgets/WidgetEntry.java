/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.widgets;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion del widget de ingreso
 *
 * @version 1.0.0
 * @author equijano
 * @since 08/08/2019
 * @see Creación
 */
@ApiObject(
        group = "Widgets",
        name = "Widgets - Ingreso",
        description = "Representa la información del widget de ingreso"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WidgetEntry
{

    @ApiObjectField(name = "sampleEntry", description = "Muestras ingresadas", required = true, order = 1)
    private long sampleEntry;
    @ApiObjectField(name = "branch", description = "Sede", required = true, order = 2)
    private int branch;
    @ApiObjectField(name = "sampleVerified", description = "Muestras verificadas", required = true, order = 3)
    private int sampleVerified;
    @ApiObjectField(name = "sampleByTestEntry", description = "Muestras por examen ingresados", required = true, order = 4)
    private int sampleByTestEntry;
    @ApiObjectField(name = "sampleByTestValidated", description = "Muestras por examen validados", required = true, order = 5)
    private int sampleByTestValidated;
    @ApiObjectField(name = "sampleByTestPrinted", description = "Muestras por examen impresas", required = true, order = 6)
    private int sampleByTestPrinted;
    @ApiObjectField(name = "ordersByWeeks", description = "Ordenes ingresadas las ultimas semanas en un dia especifico", required = true, order = 7)
    private List<Integer> ordersByWeeks;
    @ApiObjectField(name = "ordersByDays", description = "Ordenes ingresadas los ultimos dias de la semana", required = true, order = 8)
    private List<Integer> ordersByDays;
    @ApiObjectField(name = "date", description = "Fecha", required = true, order = 9)
    private int date;

    public long getSampleEntry()
    {
        return sampleEntry;
    }

    public void setSampleEntry(long sampleEntry)
    {
        this.sampleEntry = sampleEntry;
    }

    public int getBranch()
    {
        return branch;
    }

    public void setBranch(int branch)
    {
        this.branch = branch;
    }

    public int getSampleVerified()
    {
        return sampleVerified;
    }

    public void setSampleVerified(int sampleVerified)
    {
        this.sampleVerified = sampleVerified;
    }

    public int getSampleByTestEntry()
    {
        return sampleByTestEntry;
    }

    public void setSampleByTestEntry(int sampleByTestEntry)
    {
        this.sampleByTestEntry = sampleByTestEntry;
    }

    public int getSampleByTestValidated()
    {
        return sampleByTestValidated;
    }

    public void setSampleByTestValidated(int sampleByTestValidated)
    {
        this.sampleByTestValidated = sampleByTestValidated;
    }

    public int getSampleByTestPrinted()
    {
        return sampleByTestPrinted;
    }

    public void setSampleByTestPrinted(int sampleByTestPrinted)
    {
        this.sampleByTestPrinted = sampleByTestPrinted;
    }

    public List<Integer> getOrdersByWeeks()
    {
        return ordersByWeeks;
    }

    public void setOrdersByWeeks(List<Integer> ordersByWeeks)
    {
        this.ordersByWeeks = ordersByWeeks;
    }

    public List<Integer> getOrdersByDays()
    {
        return ordersByDays;
    }

    public void setOrdersByDays(List<Integer> ordersByDays)
    {
        this.ordersByDays = ordersByDays;
    }

    public int getDate()
    {
        return date;
    }

    public void setDate(int date)
    {
        this.date = date;
    }

}

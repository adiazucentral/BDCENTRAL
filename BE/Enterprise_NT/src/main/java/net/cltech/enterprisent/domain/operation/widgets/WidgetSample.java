/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.widgets;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un examen para el registro de resultados
 *
 * @version 1.0.0
 * @author jblanco
 * @since 02/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Widgets",
        name = "Widgets - Ruta de la Muestra",
        description = "Representa la información de los Widgets de la Ruta de la Muestra."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WidgetSample
{
    @ApiObjectField(name = "dateNumber", description = "Fecha en formato yyyyMMdd", required = true, order = 1)
    private int dateNumber;
    @ApiObjectField(name = "sampleOrdered", description = "Cantidad de muestras ordenadas.", required = true, order = 2)
    private int sampleOrdered;
    @ApiObjectField(name = "sampleVerified", description = "Cantidad de muestras verificadas", required = true, order = 3)
    private int sampleVerified;
    @ApiObjectField(name = "sampleRejected", description = "Cantidad de muestras rechazadas", required = true, order = 4)
    private int sampleRejected;
    @ApiObjectField(name = "sampleRetake", description = "Cantidad de muestras retomadas", required = true, order = 5)
    private int sampleRetake;
    @ApiObjectField(name = "sampleDelayed", description = "Cantidad de muestras retrasadas", required = true, order = 6)
    private int sampleDelayed;
    @ApiObjectField(name = "sampleExpired", description = "Cantidad de muestras vencidas", required = true, order = 7)
    private int sampleExpired;

    public WidgetSample()
    {
    }

    public WidgetSample(int dateNumber, int sampleOrdered, int sampleVerified, int sampleRejected, int sampleRetake, int sampleDelayed, int sampleExpired)
    {
        this.dateNumber = dateNumber;
        this.sampleOrdered = sampleOrdered;
        this.sampleVerified = sampleVerified;
        this.sampleRejected = sampleRejected;
        this.sampleRetake = sampleRetake;
        this.sampleDelayed = sampleDelayed;
        this.sampleExpired = sampleExpired;
    }

    public int getDateNumber()
    {
        return dateNumber;
    }

    public void setDateNumber(int dateNumber)
    {
        this.dateNumber = dateNumber;
    }

    public int getSampleOrdered()
    {
        return sampleOrdered;
    }

    public void setSampleOrdered(int sampleOrdered)
    {
        this.sampleOrdered = sampleOrdered;
    }

    public int getSampleVerified()
    {
        return sampleVerified;
    }

    public void setSampleVerified(int sampleVerified)
    {
        this.sampleVerified = sampleVerified;
    }

    public int getSampleRejected()
    {
        return sampleRejected;
    }

    public void setSampleRejected(int sampleRejected)
    {
        this.sampleRejected = sampleRejected;
    }

    public int getSampleRetake()
    {
        return sampleRetake;
    }

    public void setSampleRetake(int sampleRetake)
    {
        this.sampleRetake = sampleRetake;
    }

    public int getSampleDelayed()
    {
        return sampleDelayed;
    }

    public void setSampleDelayed(int sampleDelayed)
    {
        this.sampleDelayed = sampleDelayed;
    }

    public int getSampleExpired()
    {
        return sampleExpired;
    }

    public void setSampleExpired(int sampleExpired)
    {
        this.sampleExpired = sampleExpired;
    }
    
    
}

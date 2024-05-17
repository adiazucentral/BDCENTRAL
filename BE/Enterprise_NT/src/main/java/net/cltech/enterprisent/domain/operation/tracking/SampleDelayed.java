/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.tracking;

import java.util.Date;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Asignacion de Destinos
 *
 * @version 1.0.0
 * @author cmartin
 * @since 17/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Trazabilidad",
        name = "Muestras Rechazadas",
        description = "Muestra informacion de las Muestras Rechazadas que usa el API"
)
public class SampleDelayed
{

    @ApiObjectField(name = "order", description = "Orden", required = false, order = 1)
    private Long order;
    @ApiObjectField(name = "sample", description = "Muestra", required = false, order = 2)
    private Sample sample = new Sample();
    @ApiObjectField(name = "destination", description = "Destino", required = false, order = 3)
    private Destination destination = new Destination();
    @ApiObjectField(name = "expectedTime", description = "Tiempo Esperado", required = false, order = 4)
    private Integer expectedTime;
    @ApiObjectField(name = "totalTime", description = "Tiempo Total", required = false, order = 5)
    private Long totalTime;
    @ApiObjectField(name = "maxTime", description = "Tiempo Maximo", required = false, order = 5)
    private Integer maxTime;
    @ApiObjectField(name = "date", description = "Fecha en la que se verifico el destino.", required = true, order = 6)
    private Date date;

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public Sample getSample()
    {
        return sample;
    }

    public void setSample(Sample sample)
    {
        this.sample = sample;
    }

    public Destination getDestination()
    {
        return destination;
    }

    public void setDestination(Destination destination)
    {
        this.destination = destination;
    }

    public Integer getExpectedTime()
    {
        return expectedTime;
    }

    public void setExpectedTime(Integer expectedTime)
    {
        this.expectedTime = expectedTime;
    }

    public Integer getMaxTime()
    {
        return maxTime;
    }

    public void setMaxTime(Integer maxTime)
    {
        this.maxTime = maxTime;
    }
    
    public Long getTotalTime()
    {
        return totalTime;
    }

    public void setTotalTime(Long totalTime)
    {
        this.totalTime = totalTime;
    }    

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
    
}

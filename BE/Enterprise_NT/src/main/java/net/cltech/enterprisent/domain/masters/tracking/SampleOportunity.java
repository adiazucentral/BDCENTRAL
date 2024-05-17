/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.tracking;

import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Ruta de Destinos
 *
 * @version 1.0.0
 * @author cmartin
 * @since 17/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Trazabilidad",
        name = "Oportunidad de la Muestra",
        description = "Muestra informacion del maestro Oportunidad de la Muestra que usa el API"
)
public class SampleOportunity
{
    @ApiObjectField(name = "service", description = "Servicio", required = false, order = 1)
    private ServiceLaboratory service;
    @ApiObjectField(name = "expectedTime", description = "Tiempo Esperado", required = false, order = 3)
    private Integer expectedTime;
    @ApiObjectField(name = "maximumTime", description = "Tiempo Maximo", required = false, order = 4)
    private Integer maximumTime;
    @ApiObjectField(name = "selected", description = "Indica si el elemento esta seleccionado.", required = false, order = 5)
    private boolean selected;
    
    public SampleOportunity()
    {
        service = new ServiceLaboratory();
    }

    public ServiceLaboratory getService()
    {
        return service;
    }

    public void setService(ServiceLaboratory service)
    {
        this.service = service;
    }

    public Integer getExpectedTime()
    {
        return expectedTime;
    }

    public void setExpectedTime(Integer expectedTime)
    {
        this.expectedTime = expectedTime;
    }

    public Integer getMaximumTime()
    {
        return maximumTime;
    }

    public void setMaximumTime(Integer maximumTime)
    {
        this.maximumTime = maximumTime;
    }   

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }    
    
}

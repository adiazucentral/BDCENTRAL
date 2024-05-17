/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Pruebas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 09/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Muestra por Servicio",
        description = "Muestra informacion del maestro Muestra por Servicio que usa el API"
)
public class SampleByService extends MasterAudit
{
    @ApiObjectField(name = "service", description = "Servicio", required = true, order = 1)
    private ServiceLaboratory service;
    @ApiObjectField(name = "sample", description = "Muestra", required = true, order = 2)
    private Sample sample;
    @ApiObjectField(name = "expectedTime", description = "Tiempo Esperado", required = true, order = 3)
    private Integer expectedTime;
    
    public SampleByService()
    {
        service = new ServiceLaboratory();
        sample = new Sample();
    }

    public ServiceLaboratory getService()
    {
        return service;
    }

    public void setService(ServiceLaboratory service)
    {
        this.service = service;
    }

    public Sample getSample()
    {
        return sample;
    }

    public void setSample(Sample sample)
    {
        this.sample = sample;
    }

    public Integer getExpectedTime()
    {
        return expectedTime;
    }

    public void setExpectedTime(Integer expectedTime)
    {
        this.expectedTime = expectedTime;
    }    
    
}

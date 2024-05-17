/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.operation.orders.Test;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Pruebas por Servicio.
 *
 * @version 1.0.0
 * @author mmunoz
 * @since 18/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Muestra prueba por Servicio",
        description = "Muestra informacion del maestro Prueba por Servicio que usa el API"
)
@Getter
@Setter
public class TestByService extends MasterAudit
{
    

    @ApiObjectField(name = "service", description = "Servicio", required = true, order = 1)
    private ServiceLaboratory service;
    @ApiObjectField(name = "test", description = "Examen", required = true, order = 2)
    private Test test;
    @ApiObjectField(name = "expectedTime", description = "Tiempo Esperado", required = true, order = 3)
    private Integer expectedTime;
    @ApiObjectField(name = "maximumTime", description = "Tiempo Máximo", required = true, order = 4)
    private Integer maximumTime;

    public TestByService(int serviceId, int testId)
    {
        this.service = new ServiceLaboratory(serviceId);
        this.test = new Test(testId);
    }

    public TestByService()
    {
        service = new ServiceLaboratory();
        test = new Test();
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.service);
        hash = 37 * hash + Objects.hashCode(this.test);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final TestByService other = (TestByService) obj;
        if (!Objects.equals(this.service, other.service))
        {
            return false;
        }
        if (!Objects.equals(this.test, other.test))
        {
            return false;
        }
        return true;
    }

}

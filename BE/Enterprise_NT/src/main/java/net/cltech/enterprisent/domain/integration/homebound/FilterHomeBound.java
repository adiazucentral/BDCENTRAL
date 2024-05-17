package net.cltech.enterprisent.domain.integration.homebound;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion basica del maestro Pruebas
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 19/02/2020
 * @see Creación
 */
@ApiObject(
        group = "Pruebas",
        name = "Prueba Home Bound",
        description = "Muestra informacion de ordenes por servicio"
)
public class FilterHomeBound
{

    @ApiObjectField(name = "initDate", description = "Fecha inicial (Solo aplica para filtros con fecha y numero de orden)", order = 1)
    private Long initDate;
    @ApiObjectField(name = "endDate", description = "Fecha final (Solo aplica para filtros con fecha y numero de orden)", order = 2)
    private Long endDate;
    @ApiObjectField(name = "serviceId", description = "Id del servicio", order = 3)
    private Integer serviceId;

    public FilterHomeBound(Long initDate, Long endDate, Integer serviceId)
    {
        this.initDate = initDate;
        this.endDate = endDate;
        this.serviceId = serviceId;
    }
    
     public FilterHomeBound()
    {
    }

    public Long getInitDate()
    {
        return initDate;
    }

    public void setInitDate(Long initDate)
    {
        this.initDate = initDate;
    }

    public Long getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Long endDate)
    {
        this.endDate = endDate;
    }

    public Integer getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(Integer serviceId)
    {
        this.serviceId = serviceId;
    }

}

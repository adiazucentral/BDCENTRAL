package net.cltech.enterprisent.domain.masters.tracking;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.test.Sample;
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
        name = "Asignacion de Destino",
        description = "Muestra informacion del maestro Asignacion de Destinos que usa el API"
)
public class AssignmentDestination extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "orderType", description = "Tipo de Orden", required = false, order = 2)
    private OrderType orderType;
    @ApiObjectField(name = "branch", description = "Sede", required = false, order = 3)
    private Branch branch;
    @ApiObjectField(name = "sample", description = "Muestra", required = false, order = 4)
    private Sample sample;
    @ApiObjectField(name = "state", description = "Estado", required = true, order = 5)
    private boolean state;
    @ApiObjectField(name = "destinationRoutes", description = "Lista de destinos en la ruta", required = true, order = 6)
    private List<DestinationRoute> destinationRoutes;
    @ApiObjectField(name = "service", description = "Servicio", required = false, order = 3)
    private ServiceLaboratory service;

    
    public AssignmentDestination()
    {
        orderType = new OrderType();
        branch = new Branch();
        sample = new Sample();
        destinationRoutes = new ArrayList<>();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public OrderType getOrderType()
    {
        return orderType;
    }

    public void setOrderType(OrderType orderType)
    {
        this.orderType = orderType;
    }

    public Branch getBranch()
    {
        return branch;
    }

    public void setBranch(Branch branch)
    {
        this.branch = branch;
    }

    public Sample getSample()
    {
        return sample;
    }

    public void setSample(Sample sample)
    {
        this.sample = sample;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }    

    public List<DestinationRoute> getDestinationRoutes()
    {
        return destinationRoutes;
    }

    public void setDestinationRoutes(List<DestinationRoute> destinationRoutes)
    {
        this.destinationRoutes = destinationRoutes;
    }
    
    public ServiceLaboratory getService()
    {
        return service;
    }

    public void setService(ServiceLaboratory service)
    {
        this.service = service;
    }
}


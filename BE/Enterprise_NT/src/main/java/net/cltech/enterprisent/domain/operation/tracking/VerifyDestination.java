package net.cltech.enterprisent.domain.operation.tracking;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la clase con la informacion necesaria para verificar el flujo de
 * la muestra.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 28/09/2017
 * @see Creación
 */
    @ApiObject(
        group = "Operación - Trazabilidad",
        name = "Verificar Destinos",
        description = "Representa la clase para verificar el flujo de la muestra."
)
public class VerifyDestination
{

    @ApiObjectField(name = "order", description = "Orden", order = 1)
    private Long order;
    @ApiObjectField(name = "sample", description = "Muestra", order = 2)
    private String sample;
    private int idSample;
    private int branch;
    @ApiObjectField(name = "destination", description = "Destino", order = 3)
    private int destination;
    private int assigmentDestination;
    private boolean approved;

    public VerifyDestination()
    {
    }

    public VerifyDestination(Long order, String sample, int destination)
    {
        this.order = order;
        this.sample = sample;
        this.destination = destination;
    }

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public String getSample()
    {
        return sample;
    }

    public void setSample(String sample)
    {
        this.sample = sample;
    }

    public int getBranch()
    {
        return branch;
    }

    public void setBranch(int branch)
    {
        this.branch = branch;
    }

    public int getDestination()
    {
        return destination;
    }

    public void setDestination(int destination)
    {
        this.destination = destination;
    }

    public boolean isApproved()
    {
        return approved;
    }

    public void setApproved(boolean approved)
    {
        this.approved = approved;
    }

    public int getAssigmentDestination()
    {
        return assigmentDestination;
    }

    public void setAssigmentDestination(int assigmentDestination)
    {
        this.assigmentDestination = assigmentDestination;
    }

    public int getIdSample()
    {
        return idSample;
    }

    public void setIdSample(int idSample)
    {
        this.idSample = idSample;
    }
}

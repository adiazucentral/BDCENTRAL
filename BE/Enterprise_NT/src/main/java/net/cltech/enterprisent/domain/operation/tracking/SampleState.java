/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa en que estado se encuentra una muestra.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 23/10/2017
 * @see Creación
 */
//@ApiObject(
//        group = "Trazabilidad",
//        name = "Estado de la Muestra",
//        description = "Muestra el estado de la muestra que usa el API"
//)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleState
{

    @ApiObjectField(name = "order", description = "Orden", required = false, order = 1)
    private Long order;
    @ApiObjectField(name = "date", description = "Fecha", required = false, order = 2)
    private Date date;
    @ApiObjectField(name = "user", description = "Usuario", required = false, order = 3)
    private AuthorizedUser user = new AuthorizedUser();
    @ApiObjectField(name = "destination", description = "Destino", required = false, order = 4)
    private Destination destination = new Destination();
    @ApiObjectField(name = "branch", description = "Sede", required = false, order = 5)
    private Branch branch = new Branch();
    @ApiObjectField(name = "sample", description = "Muestra", required = false, order = 6)
    private Sample sample;
    @ApiObjectField(name = "state", description = "Estado de la Muestra: 0 -> Rechazado, 1 -> Nueva Muestra, 2 -> Ordenado, 3 -> Tomado, 4 -> Verificado", required = false, order = 7)
    private Integer state;

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser user)
    {
        this.user = user;
    }

    public Destination getDestination()
    {
        return destination;
    }

    public void setDestination(Destination destination)
    {
        this.destination = destination;
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

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

}

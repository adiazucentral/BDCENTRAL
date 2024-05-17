/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.tracking.DestinationRoute;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de la trazabilidad de la muestra
 *
 * @version 1.0.0
 * @author cmartin
 * @since 20/10/2017
 * @see Creación
 */
@ApiObject(
        group = "Trazabilidad",
        name = "Trazabilidad de la Muestra",
        description = "Muestra la Trazabilidad de la muestra que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleTracking
{

    @ApiObjectField(name = "id", description = "Id", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "order", description = "Orden", required = false, order = 2)
    private Long order;
    @ApiObjectField(name = "sample", description = "Id de la Muestra", required = false, order = 3)
    private Integer sample;
    @ApiObjectField(name = "date", description = "Fecha", required = false, order = 4)
    private Date date;
    @ApiObjectField(name = "user", description = "Usuario", required = false, order = 5)
    private AuthorizedUser user = new AuthorizedUser();
    @ApiObjectField(name = "motive", description = "Motivo", required = false, order = 6)
    private Motive motive = new Motive();
    @ApiObjectField(name = "comment", description = "Comentario", required = false, order = 7)
    private String comment;
    @ApiObjectField(name = "branch", description = "Sede", required = false, order = 8)
    private Branch branch = new Branch();
    @ApiObjectField(name = "state", description = "Estado de la Muestra: 0 -> Rechazado, 1 -> Nueva Muestra, 2 -> Ordenado, 3 -> Tomado, 4 -> Verificado", required = false, order = 9)
    private Integer state;
    @ApiObjectField(name = "tests", description = "Examenes de la retoma.", required = false, order = 10)
    private List<Test> tests = new ArrayList<>();
    @ApiObjectField(name = "resultTests", description = "Resultados de los examenes.", required = false, order = 11)
    private List<ResultTest> resultTests = new ArrayList<>();
    @ApiObjectField(name = "qualityTime", description = "Tiempo en minutos de calidad de la muestra", order = 12)
    private Long qualityTime;
    private DestinationRoute destination = new DestinationRoute();

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public Integer getSample()
    {
        return sample;
    }

    public void setSample(Integer sample)
    {
        this.sample = sample;
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

    public Motive getMotive()
    {
        return motive;
    }

    public void setMotive(Motive motive)
    {
        this.motive = motive;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Branch getBranch()
    {
        return branch;
    }

    public void setBranch(Branch branch)
    {
        this.branch = branch;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public List<Test> getTests()
    {
        return tests;
    }

    public void setTests(List<Test> tests)
    {
        this.tests = tests;
    }

    public DestinationRoute getDestination()
    {
        return destination;
    }

    public void setDestination(DestinationRoute destination)
    {
        this.destination = destination;
    }

    public List<ResultTest> getResultTests()
    {
        return resultTests;
    }

    public void setResultTests(List<ResultTest> resultTests)
    {
        this.resultTests = resultTests;
    }

    public Long getQualityTime()
    {
        return qualityTime;
    }

    public void setQualityTime(Long qualityTime)
    {
        this.qualityTime = qualityTime;
    }

}

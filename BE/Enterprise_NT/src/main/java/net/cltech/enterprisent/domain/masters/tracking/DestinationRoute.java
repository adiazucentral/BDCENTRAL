package net.cltech.enterprisent.domain.masters.tracking;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
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
        name = "Ruta de Destino",
        description = "Muestra informacion del maestro Ruta de Destinos que usa el API"
)
public class DestinationRoute
{

    @ApiObjectField(name = "id", description = "Id de la ruta", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "order", description = "Orden", required = false, order = 2)
    private Integer order;
    @ApiObjectField(name = "destination", description = "Destino", required = false, order = 3)
    private Destination destination;
    @ApiObjectField(name = "tests", description = "Lista de pruebas", required = false, order = 4)
    private List<TestBasic> tests;
    @ApiObjectField(name = "sampleOportunitys", description = "Lista de oportunidad de la muestra", required = false, order = 5)
    private List<SampleOportunity> sampleOportunitys;
    @ApiObjectField(name = "verify", description = "Indica si se verifico el destino", required = false, order = 6)
    private boolean verify;
    @ApiObjectField(name = "lastTransaction", description = "Fecha de verificación", required = true, order = 7)
    private Date dateVerify;
    @ApiObjectField(name = "user", description = "Usuario que verifico", required = true, order = 8)
    private AuthorizedUser userVerify;
    @ApiObjectField(name = "branch", description = "Id de la sede", required = true, order = 9)
    private Branch branch;
    @ApiObjectField(name = "typeOrder", description = "Id del tipo de la orden", required = true, order = 10)
    private OrderType typeOrder;
    @ApiObjectField(name = "sample", description = "Id de de la muestra", required = true, order = 11)
    private Sample sample;

    public DestinationRoute()
    {
        destination = new Destination();
        tests = new ArrayList<>();
        sampleOportunitys = new ArrayList<>();
        userVerify = new AuthorizedUser();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getOrder()
    {
        return order;
    }

    public void setOrder(Integer order)
    {
        this.order = order;
    }

    public Destination getDestination()
    {
        return destination;
    }

    public void setDestination(Destination destination)
    {
        this.destination = destination;
    }

    public List<TestBasic> getTests()
    {
        return tests;
    }

    public DestinationRoute setTests(List<TestBasic> tests)
    {
        this.tests = tests;
        return this;
    }

    public List<SampleOportunity> getSampleOportunitys()
    {
        return sampleOportunitys;
    }

    public void setSampleOportunitys(List<SampleOportunity> sampleOportunitys)
    {
        this.sampleOportunitys = sampleOportunitys;
    }

    public boolean isVerify()
    {
        return verify;
    }

    public void setVerify(boolean verify)
    {
        this.verify = verify;
    }

    public Date getDateVerify()
    {
        return dateVerify;
    }

    public void setDateVerify(Date dateVerify)
    {
        this.dateVerify = dateVerify;
    }

    public AuthorizedUser getUserVerify()
    {
        return userVerify;
    }

    public void setUserVerify(AuthorizedUser userVerify)
    {
        this.userVerify = userVerify;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
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
        final DestinationRoute other = (DestinationRoute) obj;
        if (!Objects.equals(this.order, other.order))
        {
            return false;
        }
        if (!Objects.equals(this.destination, other.destination))
        {
            return false;
        }
        if (!Objects.equals(this.tests.stream().sorted((TestBasic o1, TestBasic o2) -> o1.getId().compareTo(o2.getId())).collect(Collectors.toList()), other.tests.stream().sorted((TestBasic o1, TestBasic o2) -> o1.getId().compareTo(o2.getId())).collect(Collectors.toList())))
        {
            return false;
        }
        return true;
    }

    public Branch getBranch()
    {
        return branch;
    }

    public void setBranch(Branch branch)
    {
        this.branch = branch;
    }

    public OrderType getTypeOrder()
    {
        return typeOrder;
    }

    public void setTypeOrder(OrderType typeOrder)
    {
        this.typeOrder = typeOrder;
    }

    public Sample getSample()
    {
        return sample;
    }

    public void setSample(Sample sample)
    {
        this.sample = sample;
    }

}

package net.cltech.enterprisent.domain.operation.statistic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa
 *
 * @version 1.0.0
 * @author eacuna
 * @since 23/02/2018
 * @see Creacion
 */
@ApiObject(
        group = "Estadisticas",
        name = "Muestra en Destino",
        description = "Representa una orden para estadisticas"
)
@JsonInclude(Include.NON_NULL)
public class SampleByDestinationCount
{

    @ApiObjectField(name = "sample", description = "Id grupo etario", required = true, order = 28)
    private Integer sample;
    @ApiObjectField(name = "sampleName", description = "Nombre Grupo Etario", required = true, order = 30)
    private String sampleName;
    @ApiObjectField(name = "orderType", description = "Tipo Orden", required = false, order = 5)
    private Integer orderType;
    @ApiObjectField(name = "orderTypeCode", description = "Código Tipo Orden", required = false, order = 6)
    private String orderTypeCode;
    @ApiObjectField(name = "orderTypeName", description = "Nombre Tipo Orden", required = false, order = 7)
    private String orderTypeName;
    @ApiObjectField(name = "branch", description = "Id de la Sede", required = true, order = 8)
    private Integer branch;
    @ApiObjectField(name = "branchCode", description = "Código de la sede", required = false, order = 9)
    private String branchCode;
    @ApiObjectField(name = "branchName", description = "Nombre sede", required = true, order = 10)
    private String branchName;
    @ApiObjectField(name = "total", description = "Conteo de muestras", required = true, order = 8)
    private Integer total;
    @ApiObjectField(name = "verify", description = "# verificadas", required = true, order = 8)
    private Integer verify;
    @ApiObjectField(name = "notVerify", description = "# no verificadas", required = true, order = 8)
    private Integer notVerify;
    @ApiObjectField(name = "destinations", description = "Ids de destinos", required = true, order = 8)
    private List<Destination> destinations;

    public SampleByDestinationCount()
    {

    }

    public SampleByDestinationCount(Integer sample, Integer orderType, Integer branch)
    {
        this.sample = sample;
        this.orderType = orderType;
        this.branch = branch;
    }

    public Integer getOrderType()
    {
        return orderType;
    }

    public void setOrderType(Integer orderType)
    {
        this.orderType = orderType;
    }

    public String getOrderTypeCode()
    {
        return orderTypeCode;
    }

    public void setOrderTypeCode(String orderTypeCode)
    {
        this.orderTypeCode = orderTypeCode;
    }

    public String getOrderTypeName()
    {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName)
    {
        this.orderTypeName = orderTypeName;
    }

    public Integer getBranch()
    {
        return branch;
    }

    public void setBranch(Integer branch)
    {
        this.branch = branch;
    }

    public String getBranchCode()
    {
        return branchCode;
    }

    public void setBranchCode(String branchCode)
    {
        this.branchCode = branchCode;
    }

    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
    }

    public Integer getSample()
    {
        return sample;
    }

    public void setSample(Integer sample)
    {
        this.sample = sample;
    }

    public String getSampleName()
    {
        return sampleName;
    }

    public void setSampleName(String sampleName)
    {
        this.sampleName = sampleName;
    }

    public Integer getTotal()
    {
        return total;
    }

    public void setTotal(Integer total)
    {
        this.total = total;
    }

    public Integer getVerify()
    {
        return verify;
    }

    public void setVerify(Integer verify)
    {
        this.verify = verify;
    }

    public Integer getNotVerify()
    {
        return notVerify;
    }

    public void setNotVerify(Integer notVerify)
    {
        this.notVerify = notVerify;
    }

    public List<Destination> getDestinations()
    {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations)
    {
        this.destinations = destinations;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.sample);
        hash = 11 * hash + Objects.hashCode(this.orderType);
        hash = 11 * hash + Objects.hashCode(this.branch);
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
        final SampleByDestinationCount other = (SampleByDestinationCount) obj;
        if (!Objects.equals(this.sample, other.sample))
        {
            return false;
        }
        if (!Objects.equals(this.orderType, other.orderType))
        {
            return false;
        }
        if (!Objects.equals(this.branch, other.branch))
        {
            return false;
        }
        return true;
    }

}

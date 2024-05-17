package net.cltech.enterprisent.domain.integration.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la informacion necesaria para el envio de datos hacia el tablero de toma de muestra
* hospitalaria
*
* @version 1.0.0
* @author Julian
* @since 5/02/2021
* @see Creación
*/
@ApiObject(
        group = "DashBoard",
        name = "Toma de muestra hospitalaria",
        description = "Representa la informacion necesario para el tablero toma de muestra hospitalaria"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashBoardSampleTaking 
{
    @ApiObjectField(name = "order", description = "Numero de la orden")
    private Long order;
    @ApiObjectField(name = "orderDate", description = "Fecha de creación de la orden")
    private Date orderDate;
    @ApiObjectField(name = "idBranch", description = "Id de la sede")
    private Integer idBranch;
    @ApiObjectField(name = "branchName", description = "Nombre de la sede")
    private String branchName;
    @ApiObjectField(name = "idService", description = "Id del servicio")
    private Integer idService;
    @ApiObjectField(name = "serviceCode", description = "Codigo del servicio")
    private String serviceCode;
    @ApiObjectField(name = "serviceName", description = "Nombre del servicio")
    private String serviceName;
    @ApiObjectField(name = "orderType", description = "Tipo de orden")
    private String orderType;
    @ApiObjectField(name = "patientName", description = "Nombre del paciente")
    private String patientName;
    @ApiObjectField(name = "hospitalUbication", description = "Ubicacion en el hospital")
    private String hospitalUbication;
    @ApiObjectField(name = "idSample", description = "Id de la muestra")
    private Integer idSample;
    @ApiObjectField(name = "sampleCode", description = "Codigo de la muestra")
    private String sampleCode;
    @ApiObjectField(name = "sampleName", description = "Nombre de la muestra")
    private String sampleName;
    @ApiObjectField(name = "sampleTakeDate", description = "Fecha de toma")
    private Date sampleTakeDate;
    @ApiObjectField(name = "taked", description = "Tomado -> 0 - No. 1 - Si")
    private Integer taked;

    public DashBoardSampleTaking()
    {
    }
    
    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public Integer getIdBranch()
    {
        return idBranch;
    }

    public void setIdBranch(Integer idBranch)
    {
        this.idBranch = idBranch;
    }

    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
    }

    public Integer getIdService()
    {
        return idService;
    }

    public void setIdService(Integer idService)
    {
        this.idService = idService;
    }

    public String getServiceCode()
    {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode)
    {
        this.serviceCode = serviceCode;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public String getOrderType()
    {
        return orderType;
    }

    public void setOrderType(String orderType)
    {
        this.orderType = orderType;
    }

    public String getPatientName()
    {
        return patientName;
    }

    public void setPatientName(String patientName)
    {
        this.patientName = patientName;
    }

    public String getHospitalUbication()
    {
        return hospitalUbication;
    }

    public void setHospitalUbication(String hospitalUbication)
    {
        this.hospitalUbication = hospitalUbication;
    }

    public Integer getIdSample()
    {
        return idSample;
    }

    public void setIdSample(Integer idSample)
    {
        this.idSample = idSample;
    }

    public String getSampleCode()
    {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode)
    {
        this.sampleCode = sampleCode;
    }

    public String getSampleName()
    {
        return sampleName;
    }

    public void setSampleName(String sampleName)
    {
        this.sampleName = sampleName;
    }

    public Date getSampleTakeDate()
    {
        return sampleTakeDate;
    }

    public void setSampleTakeDate(Date sampleTakeDate)
    {
        this.sampleTakeDate = sampleTakeDate;
    }

    public Integer getTaked()
    {
        return taked;
    }

    public void setTaked(Integer taked)
    {
        this.taked = taked;
    }

    public Date getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(Date orderDate)
    {
        this.orderDate = orderDate;
    }
}
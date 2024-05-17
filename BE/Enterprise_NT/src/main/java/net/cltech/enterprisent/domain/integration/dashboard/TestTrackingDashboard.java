package net.cltech.enterprisent.domain.integration.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la informacion necesario para el tablero de seguimiento de pruebas
*
* @version 1.0.0
* @author Julian
* @since 17/02/2021
* @see Creación
*/
@ApiObject(
        group = "DashBoard",
        name = "Seguimiento de pruebas",
        description = "Representa la informacion necesario para el tablero seguimiento de pruebas"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestTrackingDashboard 
{
    @ApiObjectField(name = "order", description = "Numero de la orden", order = 1)
    private Long order;
    @ApiObjectField(name = "idService", description = "Id del servicio", order = 2)
    private Integer idService;
    @ApiObjectField(name = "serviceCode", description = "Codigo del servicio", order = 3)
    private String serviceCode;
    @ApiObjectField(name = "serviceName", description = "Nombre del servicio", order = 4)
    private String serviceName;
    @ApiObjectField(name = "idTest", description = "Id del examen", order = 5)
    private Integer idTest;
    @ApiObjectField(name = "codeTest", description = "Codigo del examen", order = 6)
    private String testCode;
    @ApiObjectField(name = "nameTest", description = "Nombre del examen", order = 7)
    private String testName;
    @ApiObjectField(name = "testAbbreviation", description = "Abreviatura del examen", order = 8)
    private String testAbbreviation;
    @ApiObjectField(name = "idBranch", description = "Id de la sede", order = 9)
    private Integer idBranch;
    @ApiObjectField(name = "branchName", description = "Nombre de la sede", order = 10)
    private String branchName;
    @ApiObjectField(name = "orderType", description = "Tipo de orden", order = 11)
    private String orderType;
    @ApiObjectField(name = "patientHistory", description = "Identificación del paciente", order = 12)
    private String patientHistory;
    @ApiObjectField(name = "patientName", description = "Nombre del paciente", order = 13)
    private String patientName;
    @ApiObjectField(name = "orderDate", description = "Fecha de creación de la orden", order = 14)
    private Date orderDate;
    @ApiObjectField(name = "validateDate", description = "Fecha de validación del examen", order = 15)
    private Date validateDate;
    @ApiObjectField(name = "verifyDate", description = "Fecha de verificacion", order = 16)
    private Date verifyDate;
    @ApiObjectField(name = "dateTake", description = "Fecha de toma", order = 17)
    private Date dateTake;
    @ApiObjectField(name = "state", description = "Indica si el examen ya fue validado: 1 - Ingreso, 2 - Verificado, 7 - Validado", order = 18)
    private Integer state;
    @ApiObjectField(name = "hospitalUbication", description = "Demografico ubicación hospitalaria", order = 19)
    private String hospitalUbication;

    public TestTrackingDashboard()
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

    public Integer getIdTest()
    {
        return idTest;
    }

    public void setIdTest(Integer idTest)
    {
        this.idTest = idTest;
    }

    public String getTestCode()
    {
        return testCode;
    }

    public void setTestCode(String testCode)
    {
        this.testCode = testCode;
    }

    public String getTestName()
    {
        return testName;
    }

    public void setTestName(String testName)
    {
        this.testName = testName;
    }

    public String getTestAbbreviation()
    {
        return testAbbreviation;
    }

    public void setTestAbbreviation(String testAbbreviation)
    {
        this.testAbbreviation = testAbbreviation;
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

    public Date getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(Date orderDate)
    {
        this.orderDate = orderDate;
    }

    public Date getValidateDate()
    {
        return validateDate;
    }

    public void setValidateDate(Date validateDate)
    {
        this.validateDate = validateDate;
    }

    public Date getVerifyDate()
    {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate)
    {
        this.verifyDate = verifyDate;
    }

    public Date getDateTake()
    {
        return dateTake;
    }

    public void setDateTake(Date dateTake)
    {
        this.dateTake = dateTake;
    }

    public String getPatientHistory()
    {
        return patientHistory;
    }

    public void setPatientHistory(String patientHistory)
    {
        this.patientHistory = patientHistory;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public String getHospitalUbication()
    {
        return hospitalUbication;
    }

    public void setHospitalUbication(String hospitalUbication)
    {
        this.hospitalUbication = hospitalUbication;
    }
}
package net.cltech.enterprisent.domain.operation.statistic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.operation.orders.billing.CashBoxHeader;
import net.cltech.enterprisent.domain.operation.results.ReferenceValues;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un resultado estadisticas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 12/12/2017
 * @see Creacion
 */
@ApiObject(
        group = "Estadisticas",
        name = "Resultados",
        description = "Representa resultados para estadisticas"
)
@JsonInclude(Include.NON_NULL)
public class StatisticResult
{

    @ApiObjectField(name = "id", description = "Id examen", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo examen", required = false, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre examen", required = false, order = 3)
    private String name;
    @ApiObjectField(name = "sectionId", description = "Id del area", required = false, order = 4)
    private Integer sectionId;
    @ApiObjectField(name = "sectionCode", description = "Codigo del area", required = false, order = 5)
    private String sectionCode;
    @ApiObjectField(name = "sectionName", description = "Nombre del area", required = false, order = 6)
    private String sectionName;
    @ApiObjectField(name = "laboratoryId", description = "Id del laboratorio", required = false, order = 7)
    private Integer laboratoryId;
    @ApiObjectField(name = "laboratoryCode", description = "Codigo del laboratorio", required = false, order = 8)
    private String laboratoryCode;
    @ApiObjectField(name = "laboratoryName", description = "Nombre del laboratorio", required = false, order = 9)
    private String laboratoryName;
    @ApiObjectField(name = "testState", description = "Estado del examen", required = false, order = 10)
    private Integer testState;
    @ApiObjectField(name = "sampleState", description = "Estado de la muestra", required = false, order = 11)
    private Integer sampleState;
    @ApiObjectField(name = "currentState", description = "Estado actual", required = false, order = 12)
    private Integer currentState;
    @ApiObjectField(name = "pathology", description = "Patologia", required = false, order = 13)
    private Integer pathology;
    @ApiObjectField(name = "repeats", description = "Repeticiones", required = false, order = 14)
    private Integer repeats;
    @ApiObjectField(name = "modifications", description = "Modificaciones", required = false, order = 15)
    private Integer modifications;
    @ApiObjectField(name = "orderNumber", description = "Numero de la orden", required = false, order = 16)
    private Long orderNumber;
    @ApiObjectField(name = "patientId", description = "Id del paciente", required = false, order = 17)
    private Integer patientId;
    @ApiObjectField(name = "levelComplex", description = "nivel de complejidad", required = false, order = 18)
    private Integer levelComplex;
    @ApiObjectField(name = "validationDate", description = "Fecha validación", required = false, order = 19)
    private Integer validationDate;
    @ApiObjectField(name = "rate", description = "Id tarifa", required = true, order = 20)
    private Integer rate;
    @ApiObjectField(name = "rateCode", description = "Código tarifa", required = false, order = 21)
    private String rateCode;
    @ApiObjectField(name = "rateName", description = "Nombre tarifa", required = true, order = 22)
    private String rateName;
    @ApiObjectField(name = "priceService", description = "Precio del Servicio", required = true, order = 23)
    private BigDecimal priceService;
    @ApiObjectField(name = "pricePatient", description = "Precio Paciente", required = true, order = 24)
    private BigDecimal pricePatient;
    @ApiObjectField(name = "priceAccount", description = "Precio Cuenta", required = true, order = 25)
    private BigDecimal priceAccount;
    //--------MOTIVOS DE RECHAZO
    @ApiObjectField(name = "sample", description = "Id de muestra", required = true, order = 26)
    private Integer sample;
    @ApiObjectField(name = "sampleCode", description = "Código de muestra", required = false, order = 27)
    private String sampleCode;
    @ApiObjectField(name = "sampleName", description = "Nombre de muestra", required = true, order = 28)
    private String sampleName;
    @ApiObjectField(name = "idMotive", description = "Id del motivo", required = true, order = 29)
    private Integer idMotive;
    @ApiObjectField(name = "nameMotive", description = "Nombre del motivo", required = true, order = 30)
    private String nameMotive;
    @ApiObjectField(name = "commentMotive", description = "Comentario del rechazo", required = true, order = 31)
    private String commentMotive;
    @ApiObjectField(name = "repeatReasons", description = "Motivos de repeticion", required = true, order = 32)
    List<Motive> repeatReasons;
    @ApiObjectField(name = "typeStatistic", description = "Tipo de Estadistica: 1 -> Aplica, 0 -> No Aplica ", required = true, order = 32)
    private Short typeStatistic;
    @ApiObjectField(name = "typeStatisticBilling", description = "Tipo de Estadistica con precios:  1 -> Aplica, 0 -> No Aplica ", required = true, order = 33)
    private Short typeStatisticBilling;
    @ApiObjectField(name = "opportunityTimes", description = "Tiempos de oportunidad ", required = true, order = 34)
    private StatisticOpportunity opportunityTimes;
    @ApiObjectField(name = "multiplyBy", description = "Multiplicar Por", required = true, order = 35)
    private Integer multiplyBy;

    @ApiObjectField(name = "basic", description = "Basico (Alesta temprana)", required = true, order = 36)
    private Integer basic;
    //--------MICROBIOLOGIA
    @ApiObjectField(name = "microorganisms", description = "Microorganismos", required = false, order = 37)
    private List<Microorganism> microorganisms = new ArrayList<>();
    @ApiObjectField(name = "subSampleCode", description = "Código de la sub-muestra", required = false, order = 38)
    private String subSampleCode;
    @ApiObjectField(name = "anatomicalSiteCode", description = "Codigo del sitio anatomico", required = true, order = 39)
    private String anatomicalSiteCode;

    @ApiObjectField(name = "profile", description = "Id perfil padre", required = true, order = 40)
    private Integer profile;
    @ApiObjectField(name = "typeTest", description = "Tipo del examen ", required = true, order = 41)
    private Integer typeTest;
    @ApiObjectField(name = "profileStaditics", description = "Tipo de estadisticas del perfil", required = true, order = 42)
    private Short profileStaditics;
    @ApiObjectField(name = "profilebilling", description = "Tipo de estadisticas de facturacion del perfil", required = true, order = 43)
    private Short profileBilling;
    @ApiObjectField(name = "profilePacprofilebillingkage", description = "Tipo de estadisticas del paquete", required = true, order = 42)
    private Short packageStaditics;
    @ApiObjectField(name = "packagebilling", description = "Tipo de estadisticas de facturacion del paquete", required = true, order = 43)
    private Short packageBilling;
    @ApiObjectField(name = "idpackage", description = "Id paquete padre", required = true, order = 44)
    private Integer idpackage;
    @ApiObjectField(name = "applyStadistic", description = "Aplica a estadisticas", required = false, order = 45)
    private Integer applyStadistic;
    @ApiObjectField(name = "cashbox", description = "Listado de los registros de caja", required = false, order = 46)
    private List<CashBoxHeader> cashbox;
    @ApiObjectField(name = "homologationCode", description = "Codigos de homologacion", required = false, order = 47)
    private String homologationCode;
    @ApiObjectField(name = "expectedTimeDays", description = "Tiempo esperado en Dias", required = false, order = 48)
    private Long expectedTimeDays;
    @ApiObjectField(name = "laboratoryRemisionName", description = "Laboratorio que realizo la Remision", required = false, order = 49)
    private String laboratoryRemisionName;
    @ApiObjectField(name = "referenceValues", description = "Valores de referencia", required = false, order = 38)
    private ReferenceValues referenceValues = new ReferenceValues();

    public Integer getIdpackage()
    {
        return idpackage;
    }

    public void setIdpackage(Integer idpackage)
    {
        this.idpackage = idpackage;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getSectionId()
    {
        return sectionId;
    }

    public void setSectionId(Integer sectionId)
    {
        this.sectionId = sectionId;
    }

    public String getSectionCode()
    {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode)
    {
        this.sectionCode = sectionCode;
    }

    public String getSectionName()
    {
        return sectionName;
    }

    public void setSectionName(String sectionName)
    {
        this.sectionName = sectionName;
    }

    public Integer getLaboratoryId()
    {
        return laboratoryId;
    }

    public void setLaboratoryId(Integer laboratoryId)
    {
        this.laboratoryId = laboratoryId;
    }

    public String getLaboratoryCode()
    {
        return laboratoryCode;
    }

    public void setLaboratoryCode(String laboratoryCode)
    {
        this.laboratoryCode = laboratoryCode;
    }

    public String getLaboratoryName()
    {
        return laboratoryName;
    }

    public void setLaboratoryName(String laboratoryName)
    {
        this.laboratoryName = laboratoryName;
    }

    public Integer getTestState()
    {
        return testState;
    }

    public void setTestState(Integer testState)
    {
        this.testState = testState;
    }

    public Integer getSampleState()
    {
        return sampleState;
    }

    public void setSampleState(Integer sampleState)
    {
        this.sampleState = sampleState;
    }

    public Integer getCurrentState()
    {
        return currentState;
    }

    public void setCurrentState(Integer currentState)
    {
        this.currentState = currentState;
    }

    public Integer getPathology()
    {
        return pathology;
    }

    public void setPathology(Integer pathology)
    {
        this.pathology = pathology;
    }

    public Integer getRepeats()
    {
        return repeats;
    }

    public void setRepeats(Integer repeats)
    {
        this.repeats = repeats;
    }

    public Integer getModifications()
    {
        return modifications;
    }

    public void setModifications(Integer modifications)
    {
        this.modifications = modifications;
    }

    public Long getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public Integer getPatientId()
    {
        return patientId;
    }

    public void setPatientId(Integer patientId)
    {
        this.patientId = patientId;
    }

    public Integer getLevelComplex()
    {
        return levelComplex;
    }

    public void setLevelComplex(Integer levelComplex)
    {
        this.levelComplex = levelComplex;
    }

    public Integer getValidationDate()
    {
        return validationDate;
    }

    public void setValidationDate(Integer validationDate)
    {
        this.validationDate = validationDate;
    }

    public Integer getRate()
    {
        return rate;
    }

    public void setRate(Integer rate)
    {
        this.rate = rate;
    }

    public String getRateCode()
    {
        return rateCode;
    }

    public void setRateCode(String rateCode)
    {
        this.rateCode = rateCode;
    }

    public String getRateName()
    {
        return rateName;
    }

    public void setRateName(String rateName)
    {
        this.rateName = rateName;
    }

    public BigDecimal getPriceService()
    {
        return priceService;
    }

    public void setPriceService(BigDecimal priceService)
    {
        this.priceService = priceService;
    }

    public BigDecimal getPricePatient()
    {
        return pricePatient;
    }

    public void setPricePatient(BigDecimal pricePatient)
    {
        this.pricePatient = pricePatient;
    }

    public BigDecimal getPriceAccount()
    {
        return priceAccount;
    }

    public void setPriceAccount(BigDecimal priceAccount)
    {
        this.priceAccount = priceAccount;
    }

    public Integer getSample()
    {
        return sample;
    }

    public void setSample(Integer sample)
    {
        this.sample = sample;
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

    public Integer getIdMotive()
    {
        return idMotive;
    }

    public void setIdMotive(Integer idMotive)
    {
        this.idMotive = idMotive;
    }

    public String getNameMotive()
    {
        return nameMotive;
    }

    public void setNameMotive(String nameMotive)
    {
        this.nameMotive = nameMotive;
    }

    public String getCommentMotive()
    {
        return commentMotive;
    }

    public void setCommentMotive(String commentMotive)
    {
        this.commentMotive = commentMotive;
    }

    public List<Motive> getRepeatReasons()
    {
        return repeatReasons;
    }

    public void setRepeatReasons(List<Motive> repeatReasons)
    {
        this.repeatReasons = repeatReasons;
    }

    public Short getTypeStatistic()
    {
        return typeStatistic;
    }

    public void setTypeStatistic(Short typeStatistic)
    {
        this.typeStatistic = typeStatistic;
    }

    public StatisticOpportunity getOpportunityTimes()
    {
        return opportunityTimes;
    }

    public StatisticResult setOpportunityTimes(StatisticOpportunity opportunityTimes)
    {
        this.opportunityTimes = opportunityTimes;
        return this;
    }

    public Integer getMultiplyBy()
    {
        return multiplyBy;
    }

    public void setMultiplyBy(Integer multiplyBy)
    {
        this.multiplyBy = multiplyBy;
    }

    public Integer getBasic()
    {
        return basic;
    }

    public void setBasic(Integer basic)
    {
        this.basic = basic;
    }

    public List<Microorganism> getMicroorganisms()
    {
        return microorganisms;
    }

    public void setMicroorganisms(List<Microorganism> microorganisms)
    {
        this.microorganisms = microorganisms;
    }

    public String getSubSampleCode()
    {
        return subSampleCode;
    }

    public void setSubSampleCode(String subSampleCode)
    {
        this.subSampleCode = subSampleCode;
    }

    public String getAnatomicalSiteCode()
    {
        return anatomicalSiteCode;
    }

    public void setAnatomicalSiteCode(String anatomicalSiteCode)
    {
        this.anatomicalSiteCode = anatomicalSiteCode;
    }

    public Integer getProfile()
    {
        return profile;
    }

    public void setProfile(Integer profile)
    {
        this.profile = profile;
    }

    public Integer getTypeTest()
    {
        return typeTest;
    }

    public void setTypeTest(Integer typeTest)
    {
        this.typeTest = typeTest;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
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
        final StatisticResult other = (StatisticResult) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

    public Short getProfileStaditics()
    {
        return profileStaditics;
    }

    public void setProfileStaditics(Short profileStaditics)
    {
        this.profileStaditics = profileStaditics;
    }

    public Short getProfileBilling()
    {
        return profileBilling;
    }

    public void setProfileBilling(Short profileBilling)
    {
        this.profileBilling = profileBilling;
    }

    public Short getPackageStaditics()
    {
        return packageStaditics;
    }

    public void setPackageStaditics(Short packageStaditics)
    {
        this.packageStaditics = packageStaditics;
    }

    public Short getPackageBilling()
    {
        return packageBilling;
    }

    public void setPackageBilling(Short packageBilling)
    {
        this.packageBilling = packageBilling;
    }

    public Short getTypeStatisticBilling()
    {
        return typeStatisticBilling;
    }

    public void setTypeStatisticBilling(Short typeStatisticBilling)
    {
        this.typeStatisticBilling = typeStatisticBilling;
    }

    public Integer getApplyStadistic()
    {
        return applyStadistic;
    }

    public void setApplyStadistic(Integer applyStadistic)
    {
        this.applyStadistic = applyStadistic;
    }

    public List<CashBoxHeader> getCashbox()
    {
        return cashbox;
    }

    public void setCashbox(List<CashBoxHeader> cashbox)
    {
        this.cashbox = cashbox;
    }

    public String getHomologationCode()
    {
        return homologationCode;
    }

    public void setHomologationCode(String homologationCode)
    {
        this.homologationCode = homologationCode;
    }

    public Long getExpectedTimeDays()
    {
        return expectedTimeDays;
    }

    public void setExpectedTimeDays(Long expectedTimeDays)
    {
        this.expectedTimeDays = expectedTimeDays;
    }

    public String getLaboratoryRemisionName()
    {
        return laboratoryRemisionName;
    }

    public void setLaboratoryRemisionName(String laboratoryRemisionName)
    {
        this.laboratoryRemisionName = laboratoryRemisionName;
    }

    public ReferenceValues getReferenceValues() {
        return referenceValues;
    }

    public void setReferenceValues(ReferenceValues referenceValues) {
        this.referenceValues = referenceValues;
    }

    @Override
    public String toString()
    {
        return "StatisticResult{" + "id=" + id + ", code=" + code + ", name=" + name + ", sectionId=" + sectionId + ", sectionCode=" + sectionCode + ", sectionName=" + sectionName + ", laboratoryId=" + laboratoryId + ", laboratoryCode=" + laboratoryCode + ", laboratoryName=" + laboratoryName + ", testState=" + testState + ", sampleState=" + sampleState + ", currentState=" + currentState + ", pathology=" + pathology + ", repeats=" + repeats + ", modifications=" + modifications + ", orderNumber=" + orderNumber + ", patientId=" + patientId + ", levelComplex=" + levelComplex + ", validationDate=" + validationDate + ", rate=" + rate + ", rateCode=" + rateCode + ", rateName=" + rateName + ", priceService=" + priceService + ", pricePatient=" + pricePatient + ", priceAccount=" + priceAccount + ", sample=" + sample + ", sampleCode=" + sampleCode + ", sampleName=" + sampleName + ", idMotive=" + idMotive + ", nameMotive=" + nameMotive + ", commentMotive=" + commentMotive + ", repeatReasons=" + repeatReasons + ", typeStatistic=" + typeStatistic + ", typeStatisticBilling=" + typeStatisticBilling + ", opportunityTimes=" + opportunityTimes + ", multiplyBy=" + multiplyBy + ", basic=" + basic + ", microorganisms=" + microorganisms + ", subSampleCode=" + subSampleCode + ", anatomicalSiteCode=" + anatomicalSiteCode + ", profile=" + profile + ", typeTest=" + typeTest + ", profileStaditics=" + profileStaditics + ", profileBilling=" + profileBilling + ", packageStaditics=" + packageStaditics + ", packageBilling=" + packageBilling + ", idpackage=" + idpackage + ", applyStadistic=" + applyStadistic + ", cashbox=" + cashbox + ", homologationCode=" + homologationCode + '}';
    }

}

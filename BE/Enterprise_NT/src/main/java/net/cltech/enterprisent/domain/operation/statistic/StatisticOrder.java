/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.statistic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.operation.orders.billing.CashBoxHeader;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa a un paciente de la aplicacion
 *
 * @version 1.0.0
 * @author eacuna
 * @since 12/12/2017
 * @see Creacion
 */
@ApiObject(
        group = "Estadisticas",
        name = "Orden",
        description = "Representa una orden para estadisticas"
)
@JsonInclude(Include.NON_NULL)
public class StatisticOrder
{

    @ApiObjectField(name = "orderNumber", description = "Número de la orden", required = false, order = 1)
    private Long orderNumber;
    @ApiObjectField(name = "id", description = "Id base de datos paciente", required = false, order = 2)
    private Integer id;
    @ApiObjectField(name = "age", description = "Edad paciente", required = false, order = 3)
    private Integer age;
    @ApiObjectField(name = "unit", description = "Unidad de la edad <br> grupo etario -> dias(2), años(1)", required = false, order = 4)
    private Integer unit;
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
    @ApiObjectField(name = "service", description = "Id servicio", required = true, order = 11)
    private Integer service;
    @ApiObjectField(name = "serviceCode", description = "Código servicio", required = false, order = 12)
    private String serviceCode;
    @ApiObjectField(name = "serviceName", description = "Nombre servicio", required = true, order = 13)
    private String serviceName;
    @ApiObjectField(name = "physician", description = "Id servicio", required = true, order = 14)
    private Integer physician;
    @ApiObjectField(name = "physicianCode", description = "Código servicio", required = false, order = 15)
    private String physicianCode;
    @ApiObjectField(name = "physicianName", description = "Nombre servicio", required = true, order = 16)
    private String physicianName;
    @ApiObjectField(name = "account", description = "Id servicio", required = true, order = 17)
    private Integer account;
    @ApiObjectField(name = "accountCode", description = "Código cliente", required = false, order = 18)
    private String accountCode;
    @ApiObjectField(name = "accountName", description = "Nombre cliente", required = true, order = 19)
    private String accountName;
    @ApiObjectField(name = "rate", description = "Id tarifa", required = true, order = 20)
    private Integer rate;
    @ApiObjectField(name = "rateCode", description = "Código tarifa", required = false, order = 21)
    private String rateCode;
    @ApiObjectField(name = "rateName", description = "Nombre tarifa", required = true, order = 22)
    private String rateName;
    @ApiObjectField(name = "date", description = "Fecha ", required = false, order = 23)
    private Integer date;
    @ApiObjectField(name = "dateTime", description = "Fecha y hora ", required = false, order = 24)
    private Date dateTime;
    @ApiObjectField(name = "patient", description = "Paciente", required = false, order = 25)
    private StatisticPatient patient = new StatisticPatient();
    @ApiObjectField(name = "weight", description = "Peso", required = false, order = 26)
    private BigDecimal weight;
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 27)
    private List<StatisticDemographic> demographics = new ArrayList<>();
    @ApiObjectField(name = "results", description = "Examenes", required = false, order = 25)
    private List<StatisticResult> results = new ArrayList<>();
    @ApiObjectField(name = "state", description = "Estado de la orden", required = true, order = 26)
    private Integer state;
    @ApiObjectField(name = "ageGroup", description = "Id grupo etario", required = true, order = 28)
    private Integer ageGroup;
    @ApiObjectField(name = "ageGroupCode", description = "Código Grupo etario", required = false, order = 29)
    private String ageGroupCode;
    @ApiObjectField(name = "ageGroupName", description = "Nombre Grupo Etario", required = true, order = 30)
    private String ageGroupName;
    @ApiObjectField(name = "cashbox", description = "Listado de los registros de caja", required = true, order = 31)
    private List<CashBoxHeader> cashbox;
    @ApiObjectField(name = "externalId", description = "Numero de orden his", required = true, order = 32)
    private String externalId;

    //Informacion de la caja
    @ApiObjectField(name = "copay", description = "Copago", required = false, order = 33)
    private Double copay;
    @ApiObjectField(name = "discounts", description = "Descuento", required = false, order = 34)
    private Double discounts;
    @ApiObjectField(name = "taxe", description = "Impuesto", required = false, order = 35)
    private Double taxe;
    @ApiObjectField(name = "payment", description = "Abono", required = false, order = 36)
    private Double payment;
    @ApiObjectField(name = "balance", description = "Balance", required = false, order = 37)
    private Double balance;

    public StatisticOrder()
    {

    }

    public Long getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getAge()
    {
        return age;
    }

    public void setAge(Integer age)
    {
        this.age = age;
    }

    public Integer getUnit()
    {
        return unit;
    }

    public void setUnit(Integer unit)
    {
        this.unit = unit;
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

    public Integer getService()
    {
        return service;
    }

    public void setService(Integer service)
    {
        this.service = service;
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

    public Integer getPhysician()
    {
        return physician;
    }

    public void setPhysician(Integer physician)
    {
        this.physician = physician;
    }

    public String getPhysicianCode()
    {
        return physicianCode;
    }

    public void setPhysicianCode(String physicianCode)
    {
        this.physicianCode = physicianCode;
    }

    public String getPhysicianName()
    {
        return physicianName;
    }

    public void setPhysicianName(String physicianName)
    {
        this.physicianName = physicianName;
    }

    public Integer getAccount()
    {
        return account;
    }

    public void setAccount(Integer account)
    {
        this.account = account;
    }

    public String getAccountCode()
    {
        return accountCode;
    }

    public void setAccountCode(String accountCode)
    {
        this.accountCode = accountCode;
    }

    public String getAccountName()
    {
        return accountName;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
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

    public Integer getDate()
    {
        return date;
    }

    public void setDate(Integer date)
    {
        this.date = date;
    }

    public Date getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(Date dateTime)
    {
        this.dateTime = dateTime;
    }

    public BigDecimal getWeight()
    {
        return weight;
    }

    public void setWeight(BigDecimal weight)
    {
        this.weight = weight;
    }

    public List<StatisticDemographic> getDemographics()
    {
        return demographics;
    }

    public List<StatisticDemographic> getAllDemographics()
    {
        if (patient != null)
        {
            List<StatisticDemographic> all = new ArrayList<>(demographics);
            all.addAll(patient.getDemographics());
            return all;
        }
        return new ArrayList<>();
    }

    public void setDemographics(List<StatisticDemographic> demographics)
    {
        this.demographics = demographics;
    }

    public StatisticPatient getPatient()
    {
        return patient;
    }

    public void setPatient(StatisticPatient patient)
    {
        this.patient = patient;
    }

    public List<StatisticResult> getResults()
    {
        return results;
    }

    public StatisticOrder setResults(List<StatisticResult> results)
    {
        this.results = results;
        return this;
    }

    public List<CashBoxHeader> getCashBox()
    {
        return cashbox;
    }

    public StatisticOrder setCashBox(List<CashBoxHeader> cashbox)
    {
        this.cashbox = cashbox;
        return this;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getAgeGroup()
    {
        return ageGroup;
    }

    public void setAgeGroup(Integer ageGroup)
    {
        this.ageGroup = ageGroup;
    }

    public String getAgeGroupCode()
    {
        return ageGroupCode;
    }

    public void setAgeGroupCode(String ageGroupCode)
    {
        this.ageGroupCode = ageGroupCode;
    }

    public String getAgeGroupName()
    {
        return ageGroupName;
    }

    public void setAgeGroupName(String ageGroupName)
    {
        this.ageGroupName = ageGroupName;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.orderNumber);
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
        final StatisticOrder other = (StatisticOrder) obj;
        if (!Objects.equals(this.orderNumber, other.orderNumber))
        {
            return false;
        }
        return true;
    }

    public String getExternalId()
    {
        return externalId;
    }

    public void setExternalId(String externalId)
    {
        this.externalId = externalId;
    }

    public List<CashBoxHeader> getCashbox() {
        return cashbox;
    }

    public void setCashbox(List<CashBoxHeader> cashbox) {
        this.cashbox = cashbox;
    }

    public Double getCopay() {
        return copay;
    }

    public void setCopay(Double copay) {
        this.copay = copay;
    }

    public Double getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Double discounts) {
        this.discounts = discounts;
    }

    public Double getTaxe() {
        return taxe;
    }

    public void setTaxe(Double taxe) {
        this.taxe = taxe;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}

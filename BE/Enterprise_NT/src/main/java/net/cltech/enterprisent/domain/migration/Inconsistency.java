/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.migration;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una inconsistencia en el sistema
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/11/2017
 * @see Creación
 */
@ApiObject(
        group = "Migración",
        name = "Inconsistencias",
        description = "Representa una inconsistencia de la aplicación"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Inconsistency
{

    @ApiObjectField(name = "orderNumber", description = "Numero de Orden", required = false, order = 1)
    private Long orderNumber;
    @ApiObjectField(name = "patientHIS", description = "Paciente HIS", required = true, order = 2)
    private Patient patientHIS = new Patient();
    @ApiObjectField(name = "patientLIS", description = "Paciente LIS", required = true, order = 3)
    private Patient patientLIS = new Patient();
    @ApiObjectField(name = "date", description = "Fecha", required = false, order = 4)
    private Date date;
    @ApiObjectField(name = "user", description = "Usuario", required = true, order = 5)
    private AuthorizedUser user = new AuthorizedUser();
    @ApiObjectField(name = "inconsistencies", description = "Indica los campos en los que se registro las inconsistencias", required = true, order = 6)
    private String inconsistencies;

    public Long getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public Patient getPatientHIS()
    {
        return patientHIS;
    }

    public void setPatientHIS(Patient patientHIS)
    {
        this.patientHIS = patientHIS;
    }

    public Patient getPatientLIS()
    {
        return patientLIS;
    }

    public void setPatientLIS(Patient patientLIS)
    {
        this.patientLIS = patientLIS;
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

    public String getInconsistencies()
    {
        return inconsistencies;
    }

    public void setInconsistencies(String inconsistencies)
    {
        this.inconsistencies = inconsistencies;
    }

}

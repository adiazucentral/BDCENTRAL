/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la clase del serial para imprimir
 *
 * @version 1.0.0
 * @author equijano
 * @since 05/03/2019
 * @see Creación
 */
@ApiObject(
        group = "Operación - Informes",
        name = "Serial",
        description = "Representa el objeto del serial para imprimir."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SerialPrint
{

    @ApiObjectField(name = "serial", description = "Serial unico", order = 1)
    private String serial;
    @ApiObjectField(name = "ip", description = "Ip del equipo que pide el serial", order = 2)
    private String ip;
    @ApiObjectField(name = "date", description = "Fecha de creacion del serial", order = 3)
    private Date date;
    @ApiObjectField(name = "branch", description = "Sede", order = 3)
    private Branch branch;
    @ApiObjectField(name = "service", description = "Servicio", order = 3)
    private ServiceLaboratory service;

    public String getSerial()
    {
        return serial;
    }

    public void setSerial(String serial)
    {
        this.serial = serial;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Branch getBranch()
    {
        return branch;
    }

    public void setBranch(Branch branch)
    {
        this.branch = branch;
    }

    public ServiceLaboratory getService()
    {
        return service;
    }

    public void setService(ServiceLaboratory service)
    {
        this.service = service;
    }

}

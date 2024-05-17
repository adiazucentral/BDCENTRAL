/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un servicio para el Siga.
 *
 * @version 1.0.0
 * @author Jrodriguez
 * @since 16/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Servicios",
        description = "Representa el objeto de los servicios del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaService
{

    @ApiObjectField(name = "id", description = "id del servicio", order = 1)
    private int id;
    @ApiObjectField(name = "code", description = "CÃ³digo del servicio", order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre del servicio", order = 3)
    private String name;
    @ApiObjectField(name = "description", description = "Descripcion del servicio", order = 4)
    private String description;
    @ApiObjectField(name = "registerDate", description = "Fecha version registro de la servicio", order = 5)
    private Date registerDate;
    @ApiObjectField(name = "qualifyService", description = "Calificar Servicio", order = 6)
    private boolean qualifyService;
    @ApiObjectField(name = "multiCalled", description = "Llamado Multiple", order = 7)
    private boolean multiCalled;
    @ApiObjectField(name = "state", description = "Estado de la servicio", order = 8)
    private short state;
    @ApiObjectField(name = "site", description = "Ubicaciacin del servicio", order = 9)
    private SigaSite site;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getRegisterDate()
    {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate)
    {
        this.registerDate = registerDate;
    }

    public boolean isQualifyService()
    {
        return qualifyService;
    }

    public void setQualifyService(boolean qualifyService)
    {
        this.qualifyService = qualifyService;
    }

    public boolean isMultiCalled()
    {
        return multiCalled;
    }

    public void setMultiCalled(boolean multiCalled)
    {
        this.multiCalled = multiCalled;
    }

    public short getState()
    {
        return state;
    }

    public void setState(short state)
    {
        this.state = state;
    }

    public SigaSite getSite()
    {
        return site;
    }

    public void setSite(SigaSite site)
    {
        this.site = site;
    }

}

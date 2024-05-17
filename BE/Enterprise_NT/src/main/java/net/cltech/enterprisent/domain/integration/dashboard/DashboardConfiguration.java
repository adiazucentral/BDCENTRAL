/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un el objeto para los tableros configurados.
 *
 * @version 1.0.0
 * @author equijano
 * @since 10/12/2018
 * @see Creación
 */
@ApiObject(
        group = "DashBoard",
        name = "Configuracion de tableros dashboard",
        description = "Representa el objeto de la configuracionb de tableros dashboard"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardConfiguration
{

    @ApiObjectField(name = "idConfiguration", description = "Id de la condifuracion", order = 1)
    private Integer idConfiguration;
    @ApiObjectField(name = "name", description = "Nombre del tablero", order = 2)
    private String name;
    @ApiObjectField(name = "idDefaultBranch", description = "Id de la sede", order = 3)
    private Integer idDefaultBranch;
    @ApiObjectField(name = "beforeHours", description = "Horas previas", order = 4)
    private int beforeHours;
    @ApiObjectField(name = "userId", description = "Id del usuario", order = 5)
    private Integer userId;
    @ApiObjectField(name = "modificationDate", description = "Fecha de modificacion", order = 6)
    private Timestamp modificationDate;
    @ApiObjectField(name = "modificationUser", description = "Usuario", order = 7)
    private Integer modificationUser;

    public Integer getIdConfiguration()
    {
        return idConfiguration;
    }

    public void setIdConfiguration(Integer idConfiguration)
    {
        this.idConfiguration = idConfiguration;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getIdDefaultBranch()
    {
        return idDefaultBranch;
    }

    public void setIdDefaultBranch(Integer idDefaultBranch)
    {
        this.idDefaultBranch = idDefaultBranch;
    }

    public int getBeforeHours()
    {
        return beforeHours;
    }

    public void setBeforeHours(int beforeHours)
    {
        this.beforeHours = beforeHours;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Timestamp getModificationDate()
    {
        return modificationDate;
    }

    public void setModificationDate(Timestamp modificationDate)
    {
        this.modificationDate = modificationDate;
    }

    public Integer getModificationUser()
    {
        return modificationUser;
    }

    public void setModificationUser(Integer modificationUser)
    {
        this.modificationUser = modificationUser;
    }

}

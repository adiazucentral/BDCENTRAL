package net.cltech.enterprisent.domain.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Clase que representa la informacion del maestro Recipientes
 *
 * @author enavas
 * @version 1.0.0
 * @since 12/04/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Recipiente",
        description = "Representa un recipiente"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Container extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id del resipiente", order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre del resipiente", order = 2)
    private String name;
    @ApiObjectField(name = "image", description = "Imagen del resipiente", order = 3)
    private String image;
    @ApiObjectField(name = "priority", description = "Prioridad", order = 4)
    private Integer priority;
    @ApiObjectField(name = "state", description = "Id del estado", order = 5)
    private boolean state;
    @ApiObjectField(name = "unit", description = "Unidad del recipiente", order = 6)
    private Unit unit;
    @ApiObjectField(name = "lastModifyDate", description = "Fecha de ultima modificación", order = 7)
    private Timestamp lastModifyDate;
    @ApiObjectField(name = "idUserModify", description = "Id del usuario que modifica", order = 8)
    private int idUserModify;

    public Container()
    {

    }

    public Integer getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public Integer getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public Unit getUnit()
    {
        return unit;
    }

    public void setUnit(Unit unit)
    {
        this.unit = unit;
    }

    public Timestamp getLastModifyDate()
    {
        return lastModifyDate;
    }

    public void setLastModifyDate(Timestamp lastModifyDate)
    {
        this.lastModifyDate = lastModifyDate;
    }

    public int getIdUserModify()
    {
        return idUserModify;
    }

    public void setIdUserModify(int idUserModify)
    {
        this.idUserModify = idUserModify;
    }
}

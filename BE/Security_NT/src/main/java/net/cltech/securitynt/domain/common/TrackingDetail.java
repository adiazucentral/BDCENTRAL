/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.domain.common;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa los cambios por cada campo de auditoria
 *
 * @version 1.0.0
 * @author enavas
 * @since 14/04/2017
 * @see Creación
 */
@ApiObject(
        group = "Auditoria",
        name = "Auditoria Detalle",
        description = "Representa el detalle de la auditoria"
)
public class TrackingDetail
{

    @ApiObjectField(name = "id", description = "Id del detalle de la  auditoria", order = 1)
    private Integer id;
    @ApiObjectField(name = "idAud", description = "Id de la auditoria", order = 2)
    private Integer idAud;
    @ApiObjectField(name = "field", description = "Nombre del campo", order = 3)
    private String field;
    @ApiObjectField(name = "oldValue", description = "Valor anterior", order = 4)
    private String oldValue;
    @ApiObjectField(name = "newValue", description = "Valor nuevo", order = 5)
    private String newValue;
    @ApiObjectField(name = "fieldList", description = "El tipo de Objeto de la lista", order = 6)
    private String fieldList;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getIdAud()
    {
        return idAud;
    }

    public void setIdAud(Integer idAud)
    {
        this.idAud = idAud;
    }

    public String getField()
    {
        return field;
    }

    public void setField(String field)
    {
        this.field = field;
    }

    public String getOldValue()
    {
        return oldValue;
    }

    public void setOldValue(String oldValue)
    {
        this.oldValue = oldValue;
    }

    public String getNewValue()
    {
        return newValue;
    }

    public void setNewValue(String newValue)
    {
        this.newValue = newValue;
    }

    public String getFieldList()
    {
        return fieldList;
    }

    public void setFieldList(String fieldList)
    {
        this.fieldList = fieldList;
    }

}

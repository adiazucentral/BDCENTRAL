/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.interview;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del tipo Entrevista
 *
 * @version 1.0.0
 * @author enavas
 * @since 17/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Entrevista",
        name = " Tipo Entrevista",
        description = "Muestra informacion del tipo de entrevista que puede ser : Tipo de Orden - Laboratorio - Examen - Destino"
)
public class TypeInterview
{

    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "select", description = "Si esta seleccionada", required = true, order = 4)
    private boolean select;
    @ApiObjectField(name = "abbr", description = "Abreviatura", required = true, order = 5)
    private String abbr;
    @ApiObjectField(name = "area", description = "Area", required = true, order = 6)
    private Integer area;
    @ApiObjectField(name = "type", description = "Tipo", required = true, order = 7)
    private Integer type;
    @ApiObjectField(name = "orderInterview", description = "Ordenamiento de la entrevista", required = true, order = 9)
    private Short orderInterview;

    public Short getOrderInterview()
    {
        return orderInterview;
    }

    public void setOrderInterview(Short orderInterview)
    {
        this.orderInterview = orderInterview;
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

    public boolean isSelect()
    {
        return select;
    }

    public void setSelect(boolean select)
    {
        this.select = select;
    }

    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public Integer getArea()
    {
        return area;
    }

    public void setArea(Integer area)
    {
        this.area = area;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

}

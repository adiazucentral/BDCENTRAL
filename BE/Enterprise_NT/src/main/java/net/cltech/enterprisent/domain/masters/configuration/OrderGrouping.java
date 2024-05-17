/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.user.User;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un objeto de configuracion de agrupacion de la orden
 *
 * @version 1.0.0
 * @author equijano
 * @since 18/03/2019
 * @see Creacion
 */
@ApiObject(
        group = "Configuracion",
        name = "Agrupacion de orden",
        description = "Representa una agrupacion de la orden"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderGrouping
{

    @ApiObjectField(name = "column", description = "Numero de la columna donde esta agrupado este servicio o tipo de orden", required = true, order = 1)
    private int column;
    @ApiObjectField(name = "service", description = "Servicio de laboratorio que se agrupa", required = false, order = 2)
    private ServiceLaboratory service;
    @ApiObjectField(name = "orderType", description = "Tipo de la orden que se agrupa", required = false, order = 3)
    private OrderType orderType;
    @ApiObjectField(name = "user", description = "Usuario que agrupa", required = true, order = 4)
    private User user;
    @ApiObjectField(name = "date", description = "Fecha de modificacion", required = true, order = 5)
    private Timestamp date;
    @ApiObjectField(name = "columnName", description = "Nombre de la columna", required = true, order = 6)
    private String columnName;

    public int getColumn()
    {
        return column;
    }

    public void setColumn(int column)
    {
        this.column = column;
    }

    public ServiceLaboratory getService()
    {
        return service;
    }

    public void setService(ServiceLaboratory service)
    {
        this.service = service;
    }

    public OrderType getOrderType()
    {
        return orderType;
    }

    public void setOrderType(OrderType orderType)
    {
        this.orderType = orderType;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Timestamp getDate()
    {
        return date;
    }

    public void setDate(Timestamp date)
    {
        this.date = date;
    }

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

}

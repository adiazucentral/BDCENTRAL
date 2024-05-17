/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.migration;

import net.cltech.enterprisent.domain.DTO.migracionIngreso.OrderIngreso;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.RequestMigracion;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.ResponseNT;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.ResponseOrderNT;
import net.cltech.enterprisent.domain.operation.orders.Order;

/**
 *
 * @author hpoveda
 */
public interface MigrationTsToNtService
{

    public ResponseNT createNT(OrderIngreso order) throws Exception;

    public ResponseOrderNT getOrderNT(RequestMigracion requestMigracion) throws Exception;

    public Order getEntry(long orderNumber) throws Exception;

    public Order get(long orderNumber) throws Exception;

}

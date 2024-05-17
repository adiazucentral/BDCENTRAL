/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.common;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.operation.orders.Order;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase para realizar auditoria consolidadad de la operación del sistema
 *
 * @version 1.0.0
 * @author omendez
 * @since 29/09/2021
 * @see Creación
 */
public class ConsolidatedAudit 
{
    
    @ApiObjectField(name = "order", description = "Orden", order = 1)
    private Order order = new Order();
    @ApiObjectField(name = "audits", description = "Auditoria", order = 2)
    private List<AuditOperation> audits = new ArrayList<>(); 

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<AuditOperation> getAudits() {
        return audits;
    }

    public void setAudits(List<AuditOperation> audits) {
        this.audits = audits;
    }
}

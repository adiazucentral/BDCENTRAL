package net.cltech.enterprisent.service.interfaces.operation.billing;

import net.cltech.enterprisent.domain.operation.billing.statement.InvoiceFilterByCustomer;
import net.cltech.enterprisent.domain.operation.billing.statement.InvoiceReport;

/**
 * Interfaz de Servicios para el estado de cuenta
 * 
 * @version 1.0.0
 * @author Julian
 * @since 13/08/2021
 * @see Creación
 */
public interface StatementService
{
    /**
     * Consulta todas las facturas asociadas a un cliente en una sede en especifico
     * 
     * @param filters Filtros de busqueda
     * @return Lista de facturas por cliente
     * @throws java.lang.Exception Error del servicio
     */
    public InvoiceReport getCustomerInvoices(InvoiceFilterByCustomer filters)throws Exception;
    
    /**
     * Consulta todas las facturas vencidas para un cliente, una sede, 
     * y creadas durante un rango de fechas muy especifico
     * 
     * @param filters Filtros de busqueda
     * @return Lista de facturas por cliente y que aplican a otros filtros
     * @throws java.lang.Exception Error del servicio
     */
    public InvoiceReport getExpiredInvoicesByCustomer(InvoiceFilterByCustomer filters)throws Exception;
}

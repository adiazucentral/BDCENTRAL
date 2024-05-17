package net.cltech.enterprisent.service.interfaces.operation.billing;

import java.util.List;
import net.cltech.enterprisent.domain.operation.billing.CashReport;
import net.cltech.enterprisent.domain.operation.billing.CashReportFilter;
import net.cltech.enterprisent.domain.operation.billing.ComboInvoice;
import net.cltech.enterprisent.domain.operation.billing.CreditNote;
import net.cltech.enterprisent.domain.operation.billing.CreditNoteCombo;
import net.cltech.enterprisent.domain.operation.billing.FilterSimpleInvoice;
import net.cltech.enterprisent.domain.operation.billing.Invoice;
import net.cltech.enterprisent.domain.operation.billing.InvoicePayment;
import net.cltech.enterprisent.domain.operation.billing.OrderParticular;
import net.cltech.enterprisent.domain.operation.billing.RecalculateRate;
import net.cltech.enterprisent.domain.operation.billing.RecalculatedDetail;
import net.cltech.enterprisent.domain.operation.billing.SimpleInvoice;
import net.cltech.enterprisent.domain.operation.orders.billing.BillingFilter;
import net.cltech.enterprisent.domain.operation.orders.billing.PriceChange;

/**
 * Interfaz de Servicios para la factura
 * 
 * @version 1.0.0
 * @author Julian
 * @since 13/04/2021
 * @see Creación
 */
public interface InvoiceService
{
    /**
     * Obtiene la información necesaria para la pre-factura
     *
     * @param billingFilter 
     * 
     * @return
     * @throws Exception Error presentado en el servicio
     */
    public Invoice preInvoice(BillingFilter billingFilter) throws Exception;
    
    /**
     * Crea la factura
     *
     * @param filter
     * 
     * @return La factura creada
     * @throws Exception Error presentado en el servicio
     */
    public Invoice saveInvoice(BillingFilter filter) throws Exception;
    
    /**
     * Realiza la creación de una nota crédito
     *
     * @param creditNote
     *
     * @return True - si fue cancelada totalmente, False - si no pudo terminar de ser cancelada
     * @throws Exception Error presentado en el servicio
     */
    public Long createCreditNote(CreditNote creditNote) throws Exception;
    
    /**
     * Obtiene el detalle simple de una factura
     *
     * @param invoiceNumber 
     * 
     * @return Detalle de la factura simple
     * @throws Exception Error presentado en el servicio
     */
    public Invoice getInvoiceCreditNotes(String invoiceNumber) throws Exception;
    
    /**
     * Consulta la factura particular de una orden
     *
     * @param order
     * 
     * @return La factura asociada a la orden
     * @throws Exception Error presentado en el servicio
     */
    public Invoice getInvoiceParticular(Long order) throws Exception;
    
    /**
     * Crea la factura para un particular
     *
     * @param order
     * 
     * @return La factura creada para un particular
     * @throws Exception Error presentado en el servicio
     */
    public Invoice saveInvoiceParticular(OrderParticular order) throws Exception;
    
    /**
     * Realiza el recalculo de las tarifas
     *
     * @param recalculateRate 
     * 
     * @return Las tarifas recalculadas
     * @throws Exception Error presentado en el servicio
     */
    public RecalculatedDetail recalculateRates(RecalculateRate recalculateRate) throws Exception;
    
    /**
     * Obtiene los datos requeridos para el reporte general de caja 
     *
     * @param filter 
     * 
     * @return Reporte de caja
     * @throws Exception Error presentado en el servicio
     */
    public CashReport generalCashReport(CashReportFilter filter) throws Exception;
    
    /**
     * Obtiene los datos requeridos para el reporte detallado de caja 
     *
     * @param filter 
     * 
     * @return Reporte detallado de caja
     * @throws Exception Error presentado en el servicio
     */
    public CashReport detailedCashReport(CashReportFilter filter) throws Exception;
    
    /**
     * Obtiene la factura por el número de factura y registra esta consulta en la auditoria
     *
     * @param invoiceNumber 
     * 
     * @return Factura
     * @throws Exception Error presentado en el servicio
     */
    public Invoice getInvoice(String invoiceNumber) throws Exception;
    
     /**
     * Obtiene la factura por el número de factura
     *
     * @param invoiceNumber 
     * 
     * @return Factura
     * @throws Exception Error presentado en el servicio
     */
    public List<CreditNote> getCreditNote(String invoiceNumber) throws Exception;
    
    /**
     * Obtiene el detalle simple de las facturas que apliquen segun que filtros
     *
     * @param filter 
     * 
     * @return Detalle simple de las facturas
     * @throws Exception Error presentado en el servicio
     */
    public List<SimpleInvoice> getSimpleInvoiceDetail(FilterSimpleInvoice filter) throws Exception;
    
    /**
     * Realiza el pago de una factura
     * 
     * @param invoicePayment
     * @return True - pago realizado, False - Error al realizar el pago
     * @throws Exception Error presentado en el servicio
     */
    public boolean paymentOfInvoice(InvoicePayment invoicePayment) throws Exception;
    
    /**
     * Obtiene los datos requeridos para el reporte de saldos de caja
     *
     * @param filter 
     * 
     * @return Reporte de saldos de caja
     * @throws Exception Error presentado en el servicio
     */
    public CashReport cashBalances(CashReportFilter filter) throws Exception;
    
    /**
     * Obtiene las ordenes que no han sido facturadas
     *
     * @param filter 
     * 
     * @return Reporte de saldos de caja
     * @throws Exception Error presentado en el servicio
     */
    public CashReport unbilled(CashReportFilter filter) throws Exception;
    
    /**
     * Realiza el cambio de precios de los examenes de una orden
     *
     * @param prices 
     * 
     * @return 
     * @throws Exception Error presentado en el servicio
     */
    public PriceChange priceChange(PriceChange prices) throws Exception;
    
    /**
     * Obtiene los datos requeridos para el reporte consolidado de convenios
     *
     * @param filter 
     * 
     * @return Reporte consolidado de convenios
     * @throws Exception Error presentado en el servicio
     */
    public CashReport consolidatedAccount(CashReportFilter filter) throws Exception;
    
    /**
     * Obtiene la información necesaria para la pre-factura de facturas combos
     *
     * @param filter 
     * 
     * @return
     * @throws Exception Error presentado en el servicio
     */
    public ComboInvoice preInvoiceComboInvoice(BillingFilter filter) throws Exception;
    
    /**
     * Crea la factura combo
     *
     * @param filter
     * 
     * @return La factura creada
     * @throws Exception Error presentado en el servicio
     */
    public ComboInvoice saveInvoiceCombo(BillingFilter filter) throws Exception;
    
    /**
     * Obtiene una factura combo por id
     *
     * @param idInvoice
     * 
     * @return La factura combo
     * @throws Exception Error presentado en el servicio
     */
    public ComboInvoice getInvoiceCombo(Integer idInvoice) throws Exception;
    
    /**
     * Realiza la creación de una nota crédito combo
     *
     * @param creditNote
     *
     * @return True - si fue cancelada totalmente, False - si no pudo terminar de ser cancelada
     * @throws Exception Error presentado en el servicio
     */
    public CreditNoteCombo creditNoteCombo(CreditNoteCombo creditNote) throws Exception;
    
    /**
     * Obtiene una nota credito combo por id
     *
     * @param idCreditNote
     * 
     * @return La nota credito combo
     * @throws Exception Error presentado en el servicio
     */
    public CreditNoteCombo getCreditNodeCombo(Long idCreditNote) throws Exception;
    
}

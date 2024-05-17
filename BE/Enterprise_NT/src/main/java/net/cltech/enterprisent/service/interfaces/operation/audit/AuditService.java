package net.cltech.enterprisent.service.interfaces.operation.audit;

import java.util.List;
import net.cltech.enterprisent.domain.operation.audit.AuditEvent;
import net.cltech.enterprisent.domain.operation.audit.AuditInformation;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.common.ConsolidatedAudit;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.filters.OrderSearchFilter;
import net.cltech.enterprisent.domain.operation.tracking.AuditFilter;
import net.cltech.enterprisent.domain.operation.tracking.InvoiceAudit;
import net.cltech.enterprisent.domain.operation.tracking.RackDetail;

/**
 * Servicios de auditoria
 *
 * @author Eacuna
 * @version 1.0.0
 * @since 30/10/2017
 */
public interface AuditService
{
    /**
     * Obtiene listado de ordenes anuladas en un rango
     *
     * @param filter Filtros.
     * @return Lista de ordenes anuladas
     * @throws Exception Error en la base de datos
     */
    public List<AuditInformation> listDeleted(Filter filter) throws Exception;

    /**
     * Obtiene la trazabilidad de una orden.
     *
     * @param idOrder Id de la orden.
     * @return Orden con información de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public List<AuditOperation> listTrackingOrder(Long idOrder) throws Exception;
    
         /**
     * Obtiene la trazabilidad de una orden.
     *
     * @param idOrder Id de la orden.
     * @param sample
     * @return Orden con información de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public List<AuditOperation> listTrackingOrderSample(Long idOrder, int sample) throws Exception;

    /**
     * Obtiene la trazabilidad de un paciente.
     *
     * @param idOrder Id de la orden.
     * @return Orden con información del paciente.
     * @throws Exception Error en la base de datos.
     */
    public List<AuditEvent> listTrackingPatient(Long idOrder) throws Exception;

    /**
     * Obtiene la trazabilidad de una orden por muestra.
     *
     * @param idOrder Id de la orden.
     * @param idSample Id de la muestra.
     * @return Orden con información de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public List<AuditEvent> listTrackingSample(Long idOrder, Integer idSample) throws Exception;
    
                /**
     * Obtiene la trazabilidad de una orden.
     *
     * @param idOrder Id de la orden.
     * @param test
     * @return Orden con información de la muestra.
     * @throws Exception Error en la base de datos.
     */
    public List<AuditOperation> listTrackingOrderTest(Long idOrder, int test) throws Exception;

    /**
     * Registra almacenamiento de la muestra en la auditoria, buscando registro
     * en la base de datos
     *
     * @param rackId id de la dragilla
     * @param position posicion de la muestra
     * @param action AuditOperation.ACTION constantes
     */
    public void auditSampleStorage(Integer rackId, String position, String action);

    /**
     * Registra almacenamiento de la muestra en la auditoria
     *
     * @param detail registro del almacenamiento
     * @param action AuditOperation.ACTION constantes
     */
    public void auditSampleStorage(RackDetail detail, String action);

    /**
     * Lista auditoria del almacenamiento de la muestra
     *
     * @param idOrder
     * @param idSample
     * @return
     * @throws Exception
     */
    public List<AuditEvent> listTrackingSampleStorage(Long idOrder, Integer idSample) throws Exception;

    /**
     * Consulta la auditoria de las muestras de una orden agrupadas por muestra
     *
     * @param idOrder
     * @return
     * @throws Exception
     */
    public List<AuditEvent> listTrackingSamplesByOrder(Long idOrder) throws Exception;

    /**
     * Consulta la auditoria de los usuarios
     *
     * @param initialDate
     * @param finalDate
     * @param user
     * @return
     * @throws Exception
     */
    public List<AuditOperation> listTrackingUser(String initialDate, String finalDate, int user) throws Exception;
    
    /**
     * Obtiene la trazabilidad de un rango de ordenes
     *
     * @param init Orden inicial.
     * @param end Orden final.
     * @return Auditoria
     * @throws Exception Error en la base de datos.
     */
    public List<AuditOperation> listTrackingGeneralOrder(Long init, Long end) throws Exception;
    
    /**
     * Obtiene la trazabilidad de un rango de ordenes de forma consolidada
     *
     * @param filter Filtros.
     * @return Auditoria
     * @throws Exception Error en la base de datos.
     */
    public List<ConsolidatedAudit> listTrackingConsolidatedOrder(OrderSearchFilter filter) throws Exception;
    
    /**
     * Obtiene la trazabilidad de las auditorias registradas para una factura durante un rango de fechas
     *
     * @param filter filtro de busqueda para el rango de fechas especificas.
     * @return Lista De Auditorias
     * @throws Exception Error en la base de datos.
     */
    public List<AuditOperation> getTraceabilityOfInvoices(AuditFilter filter) throws Exception;
    
    /**
     * Obtiene el detalle de una factura por el número de esta para su auditoria 
     *
     * @param invoiceNumber 
     * @return Detalle de la factura para la auditoria
     * @throws Exception Error en la base de datos.
     */
    public InvoiceAudit getTraceabilityOfInvoice(String invoiceNumber) throws Exception;
    
    /**
     * Obtiene la auditoria de la caja de una orden.
     *
     * @param orderNumber numero de la orden.
     * @return Lista De Auditorias
     * @throws Exception Error en la base de datos.
     */
    public List<AuditOperation> getTraceabilityCashOrder(Long orderNumber) throws Exception;
}

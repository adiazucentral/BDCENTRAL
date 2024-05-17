package net.cltech.enterprisent.service.impl.enterprisent.operation.tracking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.DiffFlags;
import com.flipkart.zjsonpatch.JsonDiff;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.operation.billing.InvoiceDao;
import net.cltech.enterprisent.dao.interfaces.operation.tracking.AuditDao;
import net.cltech.enterprisent.domain.operation.audit.Action;
import net.cltech.enterprisent.domain.operation.audit.AuditEvent;
import net.cltech.enterprisent.domain.operation.audit.AuditInformation;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.common.ConsolidatedAudit;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.filters.OrderSearchFilter;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.tracking.AuditFilter;
import net.cltech.enterprisent.domain.operation.tracking.InvoiceAudit;
import net.cltech.enterprisent.domain.operation.tracking.RackDetail;
import net.cltech.enterprisent.service.impl.enterprisent.masters.tracking.RackServiceEnterpriseNT;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.RackService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.audit.AuditService;
import net.cltech.enterprisent.service.interfaces.operation.list.OrderListService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderSearchService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion de la auditoria para Enterprise NT
 *
 *
 * @version 1.0.0
 * @author eacuna
 * @see 30/10/2017
 * @see Creacion
 */
@Service
public class AuditServiceEnterpriseNT implements AuditService
{

    @Autowired
    private OrderListService orderListService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private RackService rackService;
    @Autowired
    private AuditDao dao;
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderSearchService orderSearchService;
    @Autowired
    private ConfigurationService configurationService;

    @Override
    public List<AuditInformation> listDeleted(Filter filter) throws Exception
    {
        filter.setBasic(true);
        List <AuditInformation>  orders = orderListService.orderbyDateDelete(filter)
                .stream()
                .map(order -> getOrderAuditDelete(order))
                .collect(Collectors.toList());
        orders = orders.stream().filter(t -> t.getActions().size() > 0).collect(Collectors.toList());
        return orders;
    }

    /**
     * Obtiene la orden para registrar auditoria
     *
     * @param order informacion de la orden
     * @return
     */
    public AuditInformation getOrderAudit(Order order)
    {
        AuditInformation info = new AuditInformation();
        info.setOrder(order);
        try
        {
            info.setActions(dao.getActions(order.getOrderNumber()));
            info.setLast(info.getActions()
                    .stream()
                    .sorted((Action o1, Action o2) -> (-1) * o1.getDate().compareTo(o2.getDate())).findFirst().orElse(new Action()));
            return info;
        } catch (Exception ex)
        {
            Logger.getLogger(AuditServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return info;

    }
    
    /**
     * Obtiene la orden para registrar auditoria
     *
     * @param order informacion de la orden
     * @return
     */
    public AuditInformation getOrderAuditDelete(Order order)
    {
        AuditInformation info = new AuditInformation();
        info.setOrder(order);
        try
        {
            info.setActions(dao.getActionsgetActionsDelete(order.getOrderNumber()));
            info.setLast(info.getActions()
                    .stream()
                    .sorted((Action o1, Action o2) -> (-1) * o1.getDate().compareTo(o2.getDate())).findFirst().orElse(new Action()));
            return info;
        } catch (Exception ex)
        {
            Logger.getLogger(AuditServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return info;

    }
    @Override
    public List<AuditOperation> listTrackingOrder(Long idOrder) throws Exception
    {
        
        List<AuditOperation> audits = dao.getAuditOrder(idOrder);
        return audits;
    }
    
    @Override
    public List<AuditOperation> listTrackingOrderSample(Long idOrder, int sample) throws Exception
    {
        
        List<AuditOperation> audits = dao.getAuditOrderSample(idOrder,sample);
        return audits;
    }

    @Override
    public List<AuditEvent> listTrackingPatient(Long idOrder) throws Exception
    {
        int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
        return buildListTracking(dao.listTrackingPatient(patientService.get(idOrder).getId(), yearsQuery));
    }

    @Override
    public List<AuditEvent> listTrackingSample(Long idOrder, Integer idSample) throws Exception
    {
        return buildListTracking(dao.listTrackingSample(idOrder, idSample));
    }
    
    @Override
    public List<AuditOperation> listTrackingOrderTest(Long idOrder, int test) throws Exception
    {
        
        List<AuditOperation> audits = dao.getAuditOrderTest(idOrder, test);
        return audits;
    }

    /**
     * Recorre el listado de los eventos de auditoria y obtiene la diferencia
     * para cada uno
     *
     * @param events
     * @return
     * @throws Exception
     */
    private List<AuditEvent> buildListTracking(List<AuditEvent> events) throws Exception
    {
        final List<String> previous = new ArrayList<>(0);
        events.stream().forEach((AuditEvent event) ->
        {
            if (previous.size() > 0)
            {
                event.setPrevious(previous.get(0));
                String differences = differencesObjects(event.getPrevious(), event.getCurrent());
                event.setDiferences(differences);
                previous.set(0, event.getCurrent());
            } else
            {
                previous.add(event.getCurrent());
            }
        });
        return events;
    }

    @Override
    public void auditSampleStorage(Integer rackId, String position, String action)
    {
        try
        {
            RackDetail auditDetail = rackService.getSampleDetail(rackId, position);
            auditSampleStorage(auditDetail, action);
        } catch (Exception ex)
        {
            Logger.getLogger(RackServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void auditSampleStorage(RackDetail auditDetail, String action)
    {
        try
        {
            auditDetail.auditClean();
            List<AuditOperation> auditList = new ArrayList<>(0);
            auditList.add(new AuditOperation(auditDetail.getOrder(), auditDetail.getSample().getId(),null, action, AuditOperation.TYPE_SAMPLE_STORAGE, Tools.jsonObject(auditDetail), null, null, null, null));
            trackingService.registerOperationTracking(auditList);
        } catch (Exception ex)
        {
            Logger.getLogger(RackServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<AuditEvent> listTrackingSampleStorage(Long idOrder, Integer idSample) throws Exception
    {
        return buildListTracking(dao.sampleStorageTracking(idOrder, idSample));
    }

    @Override
    public List<AuditEvent> listTrackingSamplesByOrder(Long idOrder) throws Exception
    {
        return buildListTracking(dao.listTrackingSamplesByOrder(idOrder));
    }

    @Override
    public List<AuditOperation> listTrackingUser(String initialDate, String finalDate, int user) throws Exception
    {
        int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
        return dao.getAuditUser(initialDate, finalDate, user, yearsQuery);
    }

    /**
     * Obtiene la cadena de texto de tipo object con las diferencias entre dos
     * objectos
     *
     * @param objectPrevious
     * @param objectCurrent
     * @return
     */
    private String differencesObjects(String objectPrevious, String objectCurrent)
    {
        try
        {
            final ObjectMapper jackson = new ObjectMapper();
            final EnumSet<DiffFlags> flags = DiffFlags.dontNormalizeOpIntoMoveAndCopy();
            flags.add(DiffFlags.OMIT_COPY_OPERATION);
            flags.add(DiffFlags.OMIT_MOVE_OPERATION);
            return JsonDiff.asJson(jackson.readTree(objectPrevious != null ? objectPrevious : "{}"),
                    jackson.readTree(objectCurrent != null ? objectCurrent : "{}"),
                    flags).toString();
        } catch (IOException ex)
        {
            Logger.getLogger(AuditServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @Override
    public List<AuditOperation> listTrackingGeneralOrder(Long init, Long end) throws Exception
    {
        List<AuditOperation> audit = dao.getAuditGeneralOrder(init, end);
        return audit;
    }
    
    @Override
    public List<ConsolidatedAudit> listTrackingConsolidatedOrder(OrderSearchFilter filter) throws Exception
    {
        List<Order> orders = orderSearchService.ordersWithPatient(filter);
        List<ConsolidatedAudit> audits = new ArrayList<>();
        if(orders.size() > 0) {
            orders.forEach( order -> {
                try {
                    ConsolidatedAudit audit = new ConsolidatedAudit();
                    Order found = orderService.getEntry(order.getOrderNumber());
                    if(found != null) {
                        audit.setOrder(found);
                        audit.setAudits(listTrackingOrder(order.getOrderNumber()));
                        audits.add(audit);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(AuditServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        return audits;
    }
    
    @Override
    public List<AuditOperation> getTraceabilityOfInvoices(AuditFilter filter) throws Exception
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date init = sdf.parse(filter.getInitDate());
            Date end = sdf.parse(filter.getEndDate());
            init = DateTools.getInitialDate(init);
            end = DateTools.getFinalDate(end);
            Timestamp initDate = new Timestamp(init.getTime());
            Timestamp endDate = new Timestamp(end.getTime());
            return dao.getTraceabilityOfInvoices(initDate, endDate);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Obtiene el detalle de una factura por el número de esta para su auditoria 
     *
     * @param invoiceNumber 
     * @return Detalle de la factura para la auditoria
     * @throws Exception Error en la base de datos.
     */
    @Override
    public InvoiceAudit getTraceabilityOfInvoice(String invoiceNumber) throws Exception
    {
        try
        {
            InvoiceAudit invoiceAudit = new InvoiceAudit();
            invoiceAudit.setInvoiceAudits(dao.getTraceabilityOfInvoice(invoiceNumber));
            int priceInvoice = Integer.parseInt(configurationService.getValue("PrecioFactura"));
            int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
            invoiceAudit.setInvoice(invoiceDao.getInvoice(invoiceNumber, false, priceInvoice, yearsQuery));
            return invoiceAudit;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
     /**
     * Obtiene la auditoria de la caja de una orden. 
     *
     * @param orderNumber
     * @return Detalle de la factura para la auditoria
     * @throws Exception Error en la base de datos.
     */
    @Override
    public List<AuditOperation> getTraceabilityCashOrder(Long orderNumber) throws Exception
    {
        try
        {
            return dao.getCashAuditByOrderNumber(orderNumber);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}

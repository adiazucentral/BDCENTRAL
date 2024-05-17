package net.cltech.enterprisent.controllers.operation.tracking;

import java.util.List;
import net.cltech.enterprisent.domain.operation.audit.AuditEvent;
import net.cltech.enterprisent.domain.operation.audit.AuditInformation;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.common.ConsolidatedAudit;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.filters.OrderSearchFilter;
import net.cltech.enterprisent.domain.operation.tracking.AuditFilter;
import net.cltech.enterprisent.domain.operation.tracking.InvoiceAudit;
import net.cltech.enterprisent.service.interfaces.operation.audit.AuditService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.annotation.ApiVersion;
import org.jsondoc.core.pojo.ApiVerb;
import org.jsondoc.core.pojo.ApiVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de servicios Rest sobre la auditoria de la orden
 *
 * @version 1.0.0
 * @author eacuna
 * @since 30/10/2017
 * @see Creacion
 */
@Api(
        name = "Auditoría de laboratorios",
        group = "Auditoría",
        description = "Servicios Rest sobre la auditoria"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/audits")
public class AuditController
{

    @Autowired
    private AuditService service;

    //------------ CONSULTAR ORDEN ----------------
    @ApiMethod(
            description = "Obtiene listado de ordenes anuladas en un rango",
            path = "/api/audits/filter/deleted",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuditInformation.class)
    @RequestMapping(value = "/filter/deleted", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditInformation>> listDeleted(
            @ApiBodyObject(clazz = Filter.class) @RequestBody() Filter filter
    ) throws Exception
    {
        List<AuditInformation> auditList = service.listDeleted(filter);
        if (auditList.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(auditList, HttpStatus.OK);
        }
    }

    //------------ CONSULTAR TRAZABILIDAD DE LA ORDEN ----------------
    @ApiMethod(
            description = "Obtiene la trazabilidad de la orden",
            path = "/api/audits/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuditOperation.class)
    @RequestMapping(value = "/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditOperation>> listTrackingOrder(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder
    ) throws Exception
    {
        List<AuditOperation> auditEvents = service.listTrackingOrder(idOrder);
        if (auditEvents.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(auditEvents, HttpStatus.OK);
        }
    }

      //------------ CONSULTAR TRAZABILIDAD DE LA ORDEN ----------------
    @ApiMethod(
            description = "Obtiene la trazabilidad de la orden",
            path = "/api/audits/ordertrazability/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuditOperation.class)
    @RequestMapping(value = "/ordertrazability/{order}/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditOperation>> listTrackingOrderSample(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Sample") @PathVariable(name = "sample") int sample
    ) throws Exception
    {
        List<AuditOperation> auditEvents = service.listTrackingOrderSample(idOrder, sample);
        if (auditEvents.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(auditEvents, HttpStatus.OK);
        }
    }
    
    //------------ CONSULTAR TRAZABILIDAD DE LA ORDEN ----------------
    @ApiMethod(
            description = "Obtiene la trazabilidad del paciente",
            path = "/api/audits/patient/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuditEvent.class)
    @RequestMapping(value = "/patient/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditEvent>> listTrackingPatient(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder
    ) throws Exception
    {
        List<AuditEvent> auditEvents = service.listTrackingPatient(idOrder);
        if (auditEvents.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(auditEvents, HttpStatus.OK);
        }
    }

    //------------ CONSULTAR TRAZABILIDAD DE LA MUESTRA ----------------
    @ApiMethod(
            description = "Obtiene la trazabilidad de muestra por orden",
            path = "/api/audits/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuditEvent.class)
    @RequestMapping(value = "/order/{order}/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditEvent>> listTrackingSample(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") int idSample
    ) throws Exception
    {
        List<AuditEvent> auditEvents = service.listTrackingSample(idOrder, idSample);
        if (auditEvents.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(auditEvents, HttpStatus.OK);
        }
    }
    
       //------------ CONSULTAR TRAZABILIDAD DE LA ORDEN ----------------
    @ApiMethod(
            description = "Obtiene la trazabilidad de la orden",
            path = "/api/audits/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuditOperation.class)
    @RequestMapping(value = "/order/{order}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditOperation>> listTrackingOrderTest(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "test", description = "test") @PathVariable(name = "test") int test
    ) throws Exception
    {
        List<AuditOperation> auditEvents = service.listTrackingOrderTest(idOrder, test);
        if (auditEvents.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(auditEvents, HttpStatus.OK);
        }
    }

    //------------ CONSULTAR TRAZABILIDAD DEL ALMACENAMIENTO DE LA MUESTRA ----------------
    @ApiMethod(
            description = "Obtiene la trazabilidad de almacenamiento de la muestra",
            path = "/api/audits/samplestorage/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuditEvent.class)
    @RequestMapping(value = "/samplestorage/order/{order}/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditEvent>> listTrackingSampleStorage(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") int idSample
    ) throws Exception
    {
        List<AuditEvent> auditEvents = service.listTrackingSampleStorage(idOrder, idSample);
        if (auditEvents.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(auditEvents, HttpStatus.OK);
        }
    }

    //------------ CONSULTA LA AUDITORIA DE LAS MUESTRAS DE UNA ORDEN AGRUPADAS POR MUESTRA ----------------
    @ApiMethod(
            description = "Consulta la auditoria de las muestras de una orden agrupadas por muestra",
            path = "/api/audits/samplesbyorder/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuditEvent.class)
    @RequestMapping(value = "/samplesbyorder/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditEvent>> listTrackingSamplesByOrder(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder
    ) throws Exception
    {
        List<AuditEvent> auditEvents = service.listTrackingSamplesByOrder(idOrder);
        if (auditEvents.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(auditEvents, HttpStatus.OK);
        }
    }

    //------------ CONSULTA LA AUDITORIA DE LOS USUARIOS ----------------
    @ApiMethod(
            description = "Consulta la auditoria de los usuarios",
            path = "/api/audits/user/date/{initial}/{final}/user/{user}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuditOperation.class)
    @RequestMapping(value = "/user/date/{initial}/{final}/user/{user}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditOperation>> listTrackingUser(
            @ApiPathParam(name = "initial", description = "Fecha inicial en formato yyyyMMdd HH:mm:ss") @PathVariable("initial") String initialDate,
            @ApiPathParam(name = "final", description = "Fecha final en formato yyyyMMdd HH:mm:ss") @PathVariable("final") String finalDate,
            @ApiPathParam(name = "user", description = "Id del usuario") @PathVariable("user") int user
    ) throws Exception
    {
        List<AuditOperation> auditEvents = service.listTrackingUser(initialDate, finalDate, user);
        if (auditEvents.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(auditEvents, HttpStatus.OK);
        }
    }

    //------------ CONSULTAR TRAZABILIDAD DE UN RANGO DE ORDENES ----------------
    @ApiMethod(
            description = "Obtiene la trazabilidad de un rango de ordenes",
            path = "/api/audits/order/general/{init}/{end}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuditOperation.class)
    @RequestMapping(value = "/order/general/{init}/{end}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditOperation>> listTrackingGeneralOrder(
            @ApiPathParam(name = "init", description = "Orden inicial") @PathVariable(name = "init") long init,
            @ApiPathParam(name = "end", description = "Orden Final") @PathVariable(name = "end") long end
    ) throws Exception
    {
        List<AuditOperation> auditEvents = service.listTrackingGeneralOrder(init, end);
        if (auditEvents.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(auditEvents, HttpStatus.OK);
        }
    }
    
    //------------ CONSULTAR TRAZABILIDAD DE UN RANGO DE ORDENES DE FORMA CONSOLIDADA ----------------
    @ApiMethod(
            description = "Obtiene la trazabilidad de un rango de ordenes de forma consolidada",
            path = "/api/audits/order/consolidated",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ConsolidatedAudit.class)
    @RequestMapping(value = "/order/consolidated", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ConsolidatedAudit>> listTrackingConsolidatedOrder(
            @ApiBodyObject(clazz = OrderSearchFilter.class) @RequestBody OrderSearchFilter filter
    ) throws Exception
    {
        List<ConsolidatedAudit> auditEvents = service.listTrackingConsolidatedOrder(filter);
        if (auditEvents.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(auditEvents, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene la trazabilidad de las facturas generadas durante un rango de fechas",
            path = "/api/audits/getTraceabilityOfInvoices",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuditOperation.class)
    @RequestMapping(value = "/getTraceabilityOfInvoices", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditOperation>> getTraceabilityOfInvoices(
            @ApiBodyObject(clazz = AuditFilter.class) @RequestBody AuditFilter filter
    ) throws Exception
    {
        List<AuditOperation> auditEvents = service.getTraceabilityOfInvoices(filter);
        if (auditEvents == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if (auditEvents.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(auditEvents, HttpStatus.OK);
        }
    }
    
    @ApiMethod(
            description = "Obtiene el detalle de un facturas requerido para su auditoria por el número de esta",
            path = "/api/audits/getTraceabilityOfInvoice/invoiceNumber/{invoiceNumber}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = InvoiceAudit.class)
    @RequestMapping(value = "/getTraceabilityOfInvoice/invoiceNumber/{invoiceNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvoiceAudit> getTraceabilityOfInvoice(
            @ApiPathParam(name = "invoiceNumber", description = "Numero de la factura") @PathVariable("invoiceNumber") String invoiceNumber
    ) throws Exception
    {
        InvoiceAudit invoice = service.getTraceabilityOfInvoice(invoiceNumber);
        if (invoice == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else
        {
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        }
    }
    
     @ApiMethod(
            description = "Obtiene la auditoria de la caja de una orden",
            path = "/api/audits/getTraceabilityCashOrder/orderNumber/{orderNumber}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuditOperation.class)
    @RequestMapping(value = "/getTraceabilityCashOrder/orderNumber/{orderNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditOperation>> getTraceabilityCashOrder(
            @ApiPathParam(name = "orderNumber", description = "Numero de la orden") @PathVariable("orderNumber") Long orderNumber
    ) throws Exception
    {
        List<AuditOperation> listaudit = service.getTraceabilityCashOrder(orderNumber);
        if (listaudit == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else
        {
            return new ResponseEntity<>(listaudit, HttpStatus.OK);
        }
    }
}

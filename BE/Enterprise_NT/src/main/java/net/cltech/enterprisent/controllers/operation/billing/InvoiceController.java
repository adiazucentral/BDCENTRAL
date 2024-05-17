package net.cltech.enterprisent.controllers.operation.billing;

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
import net.cltech.enterprisent.domain.operation.billing.PreInvoiceOrder;
import net.cltech.enterprisent.domain.operation.billing.RecalculateRate;
import net.cltech.enterprisent.domain.operation.billing.RecalculatedDetail;
import net.cltech.enterprisent.domain.operation.billing.SimpleInvoice;
import net.cltech.enterprisent.domain.operation.orders.billing.BillingFilter;
import net.cltech.enterprisent.domain.operation.orders.billing.PriceChange;
import net.cltech.enterprisent.service.interfaces.operation.billing.InvoiceService;
import net.cltech.enterprisent.tools.log.events.EventsLog;
import net.cltech.enterprisent.tools.log.patient.PatientLog;
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
 * Servicios Rest relacionados con la creación de la factura
 *
 * @version 1.0.0
 * @author Julian
 * @since 13/04/2021
 * @see Creacion
 */
@Api(
        name = "Factura",
        group = "Facturacion",
        description = "Servicios Rest para la factura"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/invoice")
public class InvoiceController
{

    @Autowired
    private InvoiceService invoiceService;

    @ApiMethod(
            description = "Obtiene los datos necesarios para la pre-factura",
            path = "/api/invoice/pre-invoice",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Invoice.class)
    @RequestMapping(value = "/pre-invoice", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Invoice> preInvoice(
            @ApiBodyObject(clazz = BillingFilter.class) @RequestBody BillingFilter filter
    ) throws Exception
    {
        
        Invoice invoice = invoiceService.preInvoice(filter);
        if (invoice != null)
        {
            return new ResponseEntity(invoice, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Crea la factura y la almacena en la BD",
            path = "/api/invoice",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PreInvoiceOrder> saveInvoice(
            @ApiBodyObject(clazz = BillingFilter.class) @RequestBody BillingFilter filter
    ) throws Exception
    {
        Invoice created = invoiceService.saveInvoice(filter);
        if (created != null)
        {
            return new ResponseEntity(created, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Crea una nota crédito asociada a una factura",
            path = "/api/invoice/createCreditNote",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Long.class)
    @RequestMapping(value = "/createCreditNote", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createCreditNote(
            @ApiBodyObject(clazz = CreditNote.class) @RequestBody CreditNote creditNote
    ) throws Exception
    {
        return new ResponseEntity(invoiceService.createCreditNote(creditNote), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene el detalle de la factura con el número de esta",
            path = "/api/invoice/getSimpleInvoiceDetail/invoiceNumber/{invoiceNumber}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Invoice.class)
    @RequestMapping(value = "/getSimpleInvoiceDetail/invoiceNumber/{invoiceNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Invoice> getInvoiceCreditNotes(
            @ApiPathParam(name = "invoiceNumber", description = "Número de la factura") @PathVariable(name = "invoiceNumber") String invoiceNumber
    ) throws Exception
    {
        Invoice invoice = invoiceService.getInvoiceCreditNotes(invoiceNumber);
        if (invoice != null)
        {
            return new ResponseEntity(invoice, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene la factura particular asociada a una orden.",
            path = "/api/invoice/particular/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Invoice.class)
    @RequestMapping(value = "/particular/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Invoice> invoiceparticular(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder
    ) throws Exception
    {
        Invoice invoice = invoiceService.getInvoiceParticular(idOrder);
        if (invoice.getInvoiceNumber() == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Crea una factura para un particular",
            path = "/api/invoice/saveInvoiceParticular",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Invoice.class)
    @RequestMapping(value = "/saveInvoiceParticular", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Invoice> createInvoiceParticular(
            @ApiBodyObject(clazz = OrderParticular.class) @RequestBody OrderParticular order
    ) throws Exception
    {
        Invoice created = invoiceService.saveInvoiceParticular(order);
        if (created != null)
        {
            return new ResponseEntity(created, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiMethod(
            description = "Recalcula las tarifas",
            path = "/api/invoice/recalculateRates",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = RecalculatedDetail.class)
    @RequestMapping(value = "/recalculateRates", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecalculatedDetail> recalculateRates(
            @ApiBodyObject(clazz = RecalculateRate.class) @RequestBody RecalculateRate recalculateRate
    ) throws Exception
    {
        RecalculatedDetail detail = invoiceService.recalculateRates(recalculateRate);
        if (detail != null)
        {
            return new ResponseEntity(detail, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiMethod(
            description = "Obtiene el reporte general de caja",
            path = "/api/invoice/generalCashReport",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CashReport.class)
    @RequestMapping(value = "/generalCashReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CashReport> generalCashReport(
            @ApiBodyObject(clazz = CashReportFilter.class) @RequestBody CashReportFilter cashReportFilter
    ) throws Exception
    {
        CashReport report = invoiceService.generalCashReport(cashReportFilter);
        if (report != null)
        {
            return new ResponseEntity(report, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene el reporte detallado de caja por sede",
            path = "/api/invoice/detailedCashReport",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/detailedCashReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CashReport> detailedCashReport(
            @ApiBodyObject(clazz = CashReportFilter.class) @RequestBody CashReportFilter cashReportFilter
    ) throws Exception
    {
        CashReport report = invoiceService.detailedCashReport(cashReportFilter);
        if (report != null)
        {
            return new ResponseEntity(report, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene la factura por el número de esta",
            path = "/api/invoice/getInvoice/invoiceNumber/{invoiceNumber}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Invoice.class)
    @RequestMapping(value = "/getInvoice/invoiceNumber/{invoiceNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Invoice> getInvoice(
            @ApiPathParam(name = "invoiceNumber", description = "Número de la factura") @PathVariable(name = "invoiceNumber") String invoiceNumber
    ) throws Exception
    {
        Invoice invoice = invoiceService.getInvoice(invoiceNumber);
        if (invoice != null)
        {
            return new ResponseEntity(invoice, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiMethod(
            description = "Obtiene las notas credito relacionadas a una factura",
            path = "/api/invoice/getCreditNote/invoiceNumber/{invoiceNumber}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getCreditNote/invoiceNumber/{invoiceNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CreditNote>> getCreditNote(
            @ApiPathParam(name = "invoiceNumber", description = "Número de la factura") @PathVariable(name = "invoiceNumber") String invoiceNumber
    ) throws Exception
    {
        List<CreditNote> creditnote = invoiceService.getCreditNote(invoiceNumber);
        if (!creditnote.isEmpty())
        {
            return new ResponseEntity(creditnote, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene el detalle simple de las facturas que apliquen a los filtros enviados",
            path = "/api/invoice/getSimpleInvoiceDetail",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getSimpleInvoiceDetail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SimpleInvoice>> getSimpleInvoiceDetail(
            @ApiBodyObject(clazz = FilterSimpleInvoice.class) @RequestBody FilterSimpleInvoice filter
    ) throws Exception
    {
        List<SimpleInvoice> details = invoiceService.getSimpleInvoiceDetail(filter);
        if (!details.isEmpty())
        {
            return new ResponseEntity(details, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Realiza el pago de una factura con su número y con el valor a pagar",
            path = "/api/invoice/paymentOfInvoice",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/paymentOfInvoice", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> paymentOfInvoice(
            @ApiBodyObject(clazz = InvoicePayment.class) @RequestBody InvoicePayment invoicePayment
    ) throws Exception
    {
        Boolean created = invoiceService.paymentOfInvoice(invoicePayment);
        if (created)
        {
            return new ResponseEntity(created, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //SERVICIO PARA CONSULTAR SALDOS PENDIENTES EN ORDENES
    @ApiMethod(
            description = "Obtiene la lista de ordenes con saldos pendientes en caja",
            path = "/api/invoice/cashbalance",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CashReport.class)
    @RequestMapping(value = "/cashbalance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CashReport> cashBalances(
            @ApiBodyObject(clazz = CashReportFilter.class) @RequestBody CashReportFilter cashReportFilter
    ) throws Exception
    {
        CashReport report = invoiceService.cashBalances(cashReportFilter);
        if (report != null)
        {
            return new ResponseEntity(report, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    //SERVICIO PARA CONSULTAR LAS ORDENES QUE NO HAN SIDO FACTURADAS
    @ApiMethod(
            description = "Obtiene la lista de ordenes que no han sido facturadas",
            path = "/api/invoice/unbilled",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CashReport.class)
    @RequestMapping(value = "/unbilled", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CashReport> unbilled(
            @ApiBodyObject(clazz = CashReportFilter.class) @RequestBody CashReportFilter cashReportFilter
    ) throws Exception
    {
        CashReport report = invoiceService.unbilled(cashReportFilter);
        if (report != null)
        {
            return new ResponseEntity(report, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    //MODIFICA LOS PRECIOS DE LOS EXAMENES DE UNA ORDEN
    @ApiMethod(
            description = "Modifica los precios de los examenes de una orden",
            path = "/api/invoice/pricechange",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PriceChange.class)
    @RequestMapping(value = "/pricechange", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PriceChange> pricechange(
            @ApiBodyObject(clazz = PriceChange.class) @RequestBody PriceChange prices
    ) throws Exception
    {
        PriceChange detail = invoiceService.priceChange(prices);
        if (detail != null)
        {
            return new ResponseEntity(detail, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //REPORTE CONSOLIDADO DE CONVENIOS
    @ApiMethod(
            description = "Obtiene el reporte consolidado de convenios",
            path = "/api/invoice/consolidatedaccount",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/consolidatedaccount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CashReport> consolidatedAccount(
            @ApiBodyObject(clazz = CashReportFilter.class) @RequestBody CashReportFilter cashReportFilter
    ) throws Exception
    {
        CashReport report = invoiceService.consolidatedAccount(cashReportFilter);
        if (report != null)
        {
            return new ResponseEntity(report, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Obtiene los datos necesarios para la pre-factura combo",
            path = "/api/invoice/preinvoicecombo",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Invoice.class)
    @RequestMapping(value = "/preinvoicecombo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Invoice> preInvoiceCombo(
            @ApiBodyObject(clazz = BillingFilter.class) @RequestBody BillingFilter filter
    ) throws Exception
    {
        
        ComboInvoice invoice = invoiceService.preInvoiceComboInvoice(filter);
        if (invoice != null)
        {
            return new ResponseEntity(invoice, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Obtiene los datos necesarios para la factura combo",
            path = "/api/invoice/invoicecombo",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Invoice.class)
    @RequestMapping(value = "/invoicecombo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Invoice> invoiceCombo(
            @ApiBodyObject(clazz = BillingFilter.class) @RequestBody BillingFilter filter
    ) throws Exception
    {
        
        ComboInvoice invoice = invoiceService.saveInvoiceCombo(filter);
        if (invoice != null)
        {
            return new ResponseEntity(invoice, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Obtiene la factura combo por id.",
            path = "/api/invoice/invoicecombo/{idInvoice}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ComboInvoice.class)
    @RequestMapping(value = "/invoicecombo/{idInvoice}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ComboInvoice> invoiceparticular(
            @ApiPathParam(name = "idInvoice", description = "idInvoice") @PathVariable(name = "idInvoice") Integer idInvoice
    ) throws Exception
    {
        ComboInvoice invoice = invoiceService.getInvoiceCombo(idInvoice);
        if (invoice.getInvoiceId() == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        }
    }
    
    @ApiMethod(
            description = "Crea una nota crédito combo asociada a una factura combo",
            path = "/api/invoice/creditnotecombo",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CreditNoteCombo.class)
    @RequestMapping(value = "/creditnotecombo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreditNoteCombo> creditNoteCombo(
            @ApiBodyObject(clazz = CreditNoteCombo.class) @RequestBody CreditNoteCombo creditNote
    ) throws Exception
    {
        return new ResponseEntity(invoiceService.creditNoteCombo(creditNote), HttpStatus.OK);
    }
    
    @ApiMethod(
            description = "Obtiene la nota credito combo por id.",
            path = "/api/invoice/creditnotecombo/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CreditNoteCombo.class)
    @RequestMapping(value = "/creditnotecombo/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreditNoteCombo> getCreditNodeCombo(
            @ApiPathParam(name = "id", description = "id") @PathVariable(name = "id") Long id
    ) throws Exception
    {
        CreditNoteCombo creditNote = invoiceService.getCreditNodeCombo(id);
        if (creditNote == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(creditNote, HttpStatus.OK);
        }
    }
}

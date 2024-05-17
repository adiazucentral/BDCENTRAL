package net.cltech.enterprisent.controllers.operation.billing;

import net.cltech.enterprisent.domain.operation.billing.statement.InvoiceFilterByCustomer;
import net.cltech.enterprisent.domain.operation.billing.statement.InvoiceReport;
import net.cltech.enterprisent.service.interfaces.operation.billing.StatementService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.annotation.ApiVersion;
import org.jsondoc.core.pojo.ApiVerb;
import org.jsondoc.core.pojo.ApiVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Servicios Rest relacionados con el estado de cuenta
 *
 * @version 1.0.0
 * @author Julian
 * @since 13/08/2021
 * @see Creación
 */
@Api(
        name = "Estado De Cuenta",
        group = "Facturacion",
        description = "Servicios Rest para el estado de cuenta"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/statement")
public class StatementController
{
    @Autowired
    private StatementService statementService;
    
    @ApiMethod(
            description = "Consulta todas las facturas creadas para un cliente en una sede especifica y que aplican a un rango de fechas",
            path = "/api/statement/getCustomerInvoices",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = InvoiceReport.class)
    @RequestMapping(value = "/getCustomerInvoices", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvoiceReport> getCustomerInvoices(
            @ApiBodyObject(clazz = InvoiceFilterByCustomer.class) @RequestBody InvoiceFilterByCustomer filters
    ) throws Exception
    {
        InvoiceReport report = statementService.getCustomerInvoices(filters);
        if (report == null)
        {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else
        {
            return new ResponseEntity(report, HttpStatus.OK);
        }
    }
    
    @ApiMethod(
            description = "Consulta todas las facturas vencidas para un cliente y otros filtros, al igual que consulta las ordenes sin facturar con los mismos filtros",
            path = "/api/statement/getExpiredInvoicesByCustomer",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 201 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = InvoiceReport.class)
    @RequestMapping(value = "/getExpiredInvoicesByCustomer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvoiceReport> getExpiredInvoicesByCustomer(
            @ApiBodyObject(clazz = InvoiceFilterByCustomer.class) @RequestBody InvoiceFilterByCustomer filters
    ) throws Exception
    {
        InvoiceReport report = statementService.getExpiredInvoicesByCustomer(filters);
        if (report == null)
        {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else
        {
            return new ResponseEntity(report, HttpStatus.OK);
        }
    }
}

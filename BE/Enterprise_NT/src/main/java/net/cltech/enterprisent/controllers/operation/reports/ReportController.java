package net.cltech.enterprisent.controllers.operation.reports;

import java.util.List;
import net.cltech.enterprisent.domain.common.PrintNode;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.common.FilterOrder;
import net.cltech.enterprisent.domain.operation.common.FilterOrderBarcode;
import net.cltech.enterprisent.domain.operation.common.FilterOrderHeader;
import net.cltech.enterprisent.domain.operation.common.PrintOrder;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderReportINS;
import net.cltech.enterprisent.domain.operation.orders.SuperTest;
import net.cltech.enterprisent.domain.operation.orders.excel.OrderReportAidaAcs;
import net.cltech.enterprisent.domain.operation.reports.ChangeState;
import net.cltech.enterprisent.domain.operation.reports.PrintBarcodeLog;
import net.cltech.enterprisent.domain.operation.reports.PrintReportLog;
import net.cltech.enterprisent.domain.operation.reports.ReportBarcode;
import net.cltech.enterprisent.domain.operation.reports.SendPrint;
import net.cltech.enterprisent.domain.operation.reports.SerialPrint;
import net.cltech.enterprisent.service.interfaces.operation.reports.ReportService;
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
 * Controlador de servicios Rest sobre ordenes
 *
 * @version 1.0.0
 * @author cmartin
 * @since 24/11/2017
 * @see Creacion
 */
@Api(
        name = "Informes",
        group = "Operación - Informes",
        description = "Servicios Rest sobre informes"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/reports")
public class ReportController
{

    @Autowired
    private ReportService reportService;

    //------------ LISTAR FILTRO ESPECIFICO ----------------
    @ApiMethod(
            description = "Lista las ordenes registradas por un filtro especifico. Ha imprimir",
            path = "/api/reports",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> listFilter(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter) throws Exception
    {
        long init = System.currentTimeMillis();
        List<Order> list = reportService.listFilters(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            System.out.println("Tiempo transcurrido en consulta 'informe': " + (System.currentTimeMillis() - init) + " - " + list.size());
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Envia la lista de ZPL's con los valores de la orden para imprimir",
            path = "/api/reports",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PrintBarcodeLog.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PrintBarcodeLog>> reportsByOrder(
            @ApiBodyObject(clazz = ReportBarcode.class) @RequestBody ReportBarcode report
    ) throws Exception
    {
        return new ResponseEntity<>(reportService.zplReports(report), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Lista de seriales para imprimir registrados",
            path = "/api/reports/serials",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/serials", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> listSerials() throws Exception
    {
        List<String> list = reportService.listSerials();
        if (list.isEmpty() == true || list.size() < 1)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Generar serial",
            path = "/api/reports/serial",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SerialPrint.class)
    @RequestMapping(value = "/serial", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SerialPrint> createSerial() throws Exception
    {
        return new ResponseEntity<>(reportService.createSerial(), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Validar si existe este serial",
            path = "/api/reports/serial/{serial}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/serial/{serial}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> createSerial(
            @ApiPathParam(name = "serial", description = "Serial ha validar") @PathVariable("serial") String serial
    ) throws Exception
    {
        return new ResponseEntity<>(reportService.isSerial(serial), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Imprimir por tipo: reporte (una orden con su cabecera)",
            path = "/api/reports/printingbytype",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PrintReportLog.class)
    @RequestMapping(value = "/printingbytype", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PrintReportLog>> printingByType(
            @ApiBodyObject(clazz = FilterOrderHeader.class) @RequestBody FilterOrderHeader filter
    ) throws Exception
    {
        return new ResponseEntity<>(reportService.printingByType(filter), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Retorna los datos del reporte final",
            path = "/api/reports/finalReport",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PrintReportLog.class)
    @RequestMapping(value = "/finalReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PrintOrder> finalReport(
            @ApiBodyObject(clazz = FilterOrderHeader.class) @RequestBody FilterOrderHeader filter
    ) throws Exception
    {
        return new ResponseEntity<>(reportService.finalReport(filter), HttpStatus.OK);
    }

    @ApiMethod(
            description = "envia reporte al cliente de impresion",
            path = "/api/reports/printFinalReport",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = boolean.class)
    @RequestMapping(value = "/printFinalReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> printFinalReport(
            @ApiBodyObject(clazz = PrintNode.class) @RequestBody PrintNode filter
    ) throws Exception
    {
        return new ResponseEntity<>(reportService.printFinalReport(filter), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Imprimir por tipo: codigo de barras (por rango)",
            path = "/api/reports/printingbybarcode",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PrintBarcodeLog.class)
    @RequestMapping(value = "/printingbybarcode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PrintBarcodeLog>> printingByBarcode(
            @ApiBodyObject(clazz = FilterOrderBarcode.class) @RequestBody FilterOrderBarcode filter
    ) throws Exception
    {
        return new ResponseEntity<>(reportService.printingByBarcode(filter), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtener informacion de las ordenes a Imprimir por tipo: codigo de barras (por rango)",
            path = "/api/reports/ordersbarcode",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PrintReportLog.class)
    @RequestMapping(value = "/ordersbarcode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> ordersBarcode(
            @ApiBodyObject(clazz = FilterOrderBarcode.class) @RequestBody FilterOrderBarcode filter
    ) throws Exception
    {
        return new ResponseEntity<>(reportService.ordersBarcode(filter), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtener informacion completa de la orden para imprimir",
            path = "/api/reports/printingreport",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/printingreport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> printingReport(
            @ApiBodyObject(clazz = FilterOrderHeader.class) @RequestBody FilterOrderHeader filter
    ) throws Exception
    {
        return new ResponseEntity<>(reportService.printReporByOrder(filter, filter.getPrintOrder().get(0).getListOrders().get(0).getOrder()), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Enviar json al websocket para imprimir",
            path = "/api/reports/changestatetest",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/changestatetest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendPrinting(
            @ApiBodyObject(clazz = ChangeState.class) @RequestBody ChangeState detail
    ) throws Exception
    {
        reportService.changeStateTest(detail.getFilterOrderHeader(), detail.getOrder(), detail.getUser());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Generar cabeceras de la orden",
            path = "/api/reports/orderheader",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/orderheader", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> orderHeader(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<Order> list = reportService.orderHeader(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Generar cuerpo de la orden",
            path = "/api/reports/orderbody",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = FilterOrder.class)
    @RequestMapping(value = "/orderbody", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> orderBody(
            @ApiBodyObject(clazz = FilterOrder.class) @RequestBody FilterOrder filterOrder
    ) throws Exception
    {
        return new ResponseEntity<>(reportService.orderBody(filterOrder), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Enviar json al websocket para imprimir",
            path = "/api/reports/printws",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/printws", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> sendPrinting(
            @ApiBodyObject(clazz = SendPrint.class) @RequestBody SendPrint sendPrint
    ) throws Exception
    {
        reportService.sendPrinting(sendPrint.getJson(), sendPrint.getSerial(), sendPrint.getType());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Retorna un dato base 64",
            path = "/api/reports/getBase64/idOrderHis/{idOrderHis}/userName/{userName}/password/{password}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/getBase64/idOrderHis/{idOrderHis}/userName/{userName}/password/{password}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getBase64(
            @ApiPathParam(name = "idOrderHis", description = "Id de la orden del HIS") @PathVariable("idOrderHis") String idOrderHis,
            @ApiPathParam(name = "userName", description = "Nombre del usuaruio") @PathVariable("userName") String userName,
            @ApiPathParam(name = "password", description = "Contraseña del usuario") @PathVariable("password") String password
    ) throws Exception
    {
        try
        {
            String convertBase = reportService.getBase64(idOrderHis, userName, password);
            if (!convertBase.isEmpty())
            {
                return new ResponseEntity<>(convertBase, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiMethod(
            description = "Obtener informacion de las ordenes a Imprimir por tipo: codigo de barras (por rango)",
            path = "/api/reports/order/printbarcode",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = FilterOrderBarcode.class)
    @RequestMapping(value = "/order/printbarcode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FilterOrderBarcode> printbarcode(
            @ApiBodyObject(clazz = FilterOrderBarcode.class) @RequestBody FilterOrderBarcode filter
    ) throws Exception
    {
        FilterOrderBarcode response = reportService.orderPrintbarcode(filter);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiMethod(
            description = "Lista ordenes por rango de fechas",
            path = "/api/reports/orders/{initDate}/{endDate}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = OrderReportINS.class)
    @RequestMapping(value = "/orders/{initDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderReportINS>> orderRangeSend(
            @ApiPathParam(name = "initDate", description = "Fecha inicial orden") @PathVariable("initDate") long initDate,
            @ApiPathParam(name = "endDate", description = "Fecha final orden") @PathVariable("endDate") long endDate
    ) throws Exception
    {
        List<OrderReportINS> list = reportService.listFiltersOrderRange(initDate, endDate);
        if (!list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Imprimir por tipo: codigo de barras paciente",
            path = "/api/reports/printingbybarcode/patient",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PrintBarcodeLog.class)
    @RequestMapping(value = "/printingbybarcode/patient", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PrintBarcodeLog>> printingByBarcodePatient(
            @ApiBodyObject(clazz = FilterOrderBarcode.class) @RequestBody FilterOrderBarcode filter
    ) throws Exception
    {
        return new ResponseEntity<>(reportService.printByBarcodePatient(filter), HttpStatus.OK);
    }
    
    @ApiMethod(
            description = "Lista ordenes por rango de fechas",
            path = "/api/reports/ordersFacturation/{initDate}/{endDate}/{isSearchOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = OrderReportINS.class)
    @RequestMapping(value = "/ordersFacturation/{initDate}/{endDate}/{isSearchOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderReportAidaAcs>> getFacturationOrderRange(
            @ApiPathParam(name = "initDate", description = "Fecha inicial orden") @PathVariable("initDate") long initDate,
            @ApiPathParam(name = "endDate", description = "Fecha final orden") @PathVariable("endDate") long endDate,
            @ApiPathParam(name = "isSearchOrder", description = "Tipo de busqueda") @PathVariable("isSearchOrder") boolean isSearchOrder
    ) throws Exception
    {        
        List<OrderReportAidaAcs> list = reportService.getListOrderFacturation(initDate, endDate, isSearchOrder, true);
        if (!list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Lista ordenes por rango de fechas",
            path = "/api/reports/validOrderComplete/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/validOrderComplete/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> getValidOrderComplete(
            @ApiPathParam(name = "order", description = "Numero de orden") @PathVariable("order") long order
    ) throws Exception
    {        
        return new ResponseEntity<>(reportService.getValidOrderComplete(order), HttpStatus.OK);
    }
    
    @ApiMethod(
            description = "Consulta los examenes pendientes de validacion",
            path = "/api/reports/gettestpendingorder/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SuperTest.class)
    @RequestMapping(value = "/gettestpendingorder/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SuperTest>> gettestpendingorder(
            @ApiPathParam(name = "order", description = "Numero de orden") @PathVariable("order") long order
    ) throws Exception
    {        
        return new ResponseEntity<>(reportService.gettestpendingorder(order), HttpStatus.OK);
    }
    
    @ApiMethod(
            description = "Lista ordenes por rango de fechas",
            path = "/api/reports/billing/{initDate}/{endDate}/{isSearchOrder}/{profile}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = OrderReportINS.class)
    @RequestMapping(value = "/billing/{initDate}/{endDate}/{isSearchOrder}/{profile}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderReportAidaAcs>> getOrdersBilling(
            @ApiPathParam(name = "initDate", description = "Fecha inicial orden") @PathVariable("initDate") long initDate,
            @ApiPathParam(name = "endDate", description = "Fecha final orden") @PathVariable("endDate") long endDate,
            @ApiPathParam(name = "isSearchOrder", description = "Tipo de busqueda") @PathVariable("isSearchOrder") boolean isSearchOrder,
            @ApiPathParam(name = "profile", description = "Indica si se agrupa por perfiles") @PathVariable("profile") boolean profile
    ) throws Exception
    {        
        List<OrderReportAidaAcs> list = reportService.getListOrderFacturation(initDate, endDate, isSearchOrder, profile);
        if (!list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
}

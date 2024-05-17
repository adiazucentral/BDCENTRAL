package net.cltech.outreach.controllers.operation;

import java.util.List;
import net.cltech.outreach.domain.operation.Filter;
import net.cltech.outreach.domain.operation.HistoryFilter;
import net.cltech.outreach.domain.operation.OrderSearch;
import net.cltech.outreach.domain.operation.ReportFilter;
import net.cltech.outreach.domain.operation.ResultTest;
import net.cltech.outreach.domain.operation.TestHistory;
import net.cltech.outreach.service.interfaces.operation.OrderService;
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
 * @since 08/05/2018
 * @see Creacion
 */
@Api(
        name = "Ordenes",
        group = "Operación - Ordenes",
        description = "Servicios Rest sobre ordenes"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/orders")
public class OrderController
{
    @Autowired
    private OrderService service;
    
    //--------------CONSULTA DE ORDENES POR FILTROS ESPECIFICOS------------
    @ApiMethod(
            description = "Obtiene las ordenes asociadas a los filtros ingresados",
            path = "/api/orders/filter",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderSearch.class)
    @RequestMapping(value = "/filter", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderSearch>> listOrders(@ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter) throws Exception
    {
        
        List<OrderSearch> list = service.listOrders(filter);
        if (!list.isEmpty())
        {
            return new ResponseEntity(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    //--------------CONSULTA DE RESULTADOS POR ORDEN------------
    @ApiMethod(
            description = "Obtiene las ordenes asociadas a los filtros ingresados",
            path = "/api/orders/results/order/{order}/{area}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderSearch.class)
    @RequestMapping(value = "/results/order/{order}/{area}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResultTest>> listResults(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder, 
            @ApiPathParam(name = "area", description = "area") @PathVariable(name = "area") int area) throws Exception
    {
        List<ResultTest> list = service.listResults(idOrder, area);
        if (!list.isEmpty())
        {
            return new ResponseEntity(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    //--------------CONSULTA DE RESULTADOS HISTORICOS------------
    @ApiMethod(
            description = "Realiza la consulta de los resultados históricos de un examen",
            path = "/api/orders/results/history",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultTest.class)
    @RequestMapping(value = "/results/history", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestHistory>> listTestHistory(
            @ApiBodyObject(clazz = HistoryFilter.class) @RequestBody HistoryFilter filter
    ) throws Exception
    {
        List<TestHistory> list = service.listHistory(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ REPORTAR FILTRO ESPECIFICO ----------------
    @ApiMethod(
            description = "Entrega de ordenes por un filtro especifico.",
            path = "/api/orders/reports",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/reports", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listFilter(@ApiBodyObject(clazz = Filter.class) @RequestBody ReportFilter filter) throws Exception
    {
        String content = service.listReport(filter);

        if (content.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(content, HttpStatus.OK);
        }
    }
}

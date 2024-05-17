/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.operation.reports;

import java.util.List;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.reports.DeliveryOrder;
import net.cltech.enterprisent.service.interfaces.operation.reports.DeliveryResultService;
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
 * Controlador de servicios Rest sobre entrega de resultados
 *
 * @version 1.0.0
 * @author cmartin
 * @since 24/11/2017
 * @see Creacion
 */
@Api(
        name = "Entrega de Resultados",
        group = "Operación - Informes",
        description = "Servicios Rest sobre informes"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/deliveryresults")
public class DeliveryResultController
{

    @Autowired
    private DeliveryResultService deliveryService;

    //------------ LISTAR RESULTADOS ENTREGADOS ----------------
    @ApiMethod(
            description = "Lista las ordenes entregadas por un filtro especifico.",
            path = "/api/deliveryresults",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DeliveryOrder.class)
    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeliveryOrder>> listFilter(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<DeliveryOrder> list = deliveryService.listFilters(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR RESULTADOS PENDIENTES DE ENTREGA ----------------
    @ApiMethod(
            description = "Lista las ordenes pendientes de entrega por un filtro especifico.",
            path = "/api/deliveryresults/pending",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderList.class)
    @RequestMapping(value = "/pending", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderList>> listDeliveryResultPending(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<OrderList> list = deliveryService.listDeliveryResultPending(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

}

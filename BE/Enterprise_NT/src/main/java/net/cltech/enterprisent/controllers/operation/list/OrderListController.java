package net.cltech.enterprisent.controllers.operation.list;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.integration.resultados.DetailStatus;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.list.FilterRejectSample;
import net.cltech.enterprisent.domain.operation.list.RemissionLaboratory;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.remission.RemissionOrderCentral;
import net.cltech.enterprisent.service.interfaces.operation.list.OrderListService;
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
 * @version 1.2.1
 * @author bavlero
 * @since 27/05/2020
 * @see Edicion (Creacion de metodo)
 */
@Api(
        name = "Listados",
        group = "Operación - Ordenes",
        description = "Servicios Rest sobre ordenes"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/listorders")
public class OrderListController
{

    @Autowired
    private OrderListService listService;

    //------------ LISTAR FILTRO ESPECIFICO ----------------
    @ApiMethod(
            description = "Lista las ordenes registradas por un filtro especifico.",
            path = "/api/listorders",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderList>> listFilter(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<OrderList> list = listService.listFilters(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
        //------------ LISTAR FILTRO ESPECIFICO ----------------
    @ApiMethod(
            description = "Lista las ordenes registradas por un filtro especifico.",
            path = "/api/listorders/appointment",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/appointment", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderList>> listFiltersAppointment(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<OrderList> list = listService.listFiltersAppointment(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR MUESTRAS RECHAZADAS ----------------
    @ApiMethod(
            description = "Lista las ordenes registradas por un filtro especifico.",
            path = "/api/listorders/rejectsample",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/rejectsample", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> listFilter(
            @ApiBodyObject(clazz = FilterRejectSample.class) @RequestBody FilterRejectSample filter
    ) throws Exception
    {
        List<Order> list = listService.listFiltersRejectSample(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR LABORATORIO DE REFERENCIA ----------------
    @ApiMethod(
            description = "Lista las ordenes registradas por un filtro especifico.",
            path = "/api/listorders/laboratory",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/laboratory", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> listFilterLaboratory(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<Order> list = listService.listFiltersLaboratory(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
      //------------ LISTAR LABORATORIO DE REFERENCIA PARA REMITIR ----------------
    @ApiMethod(
            description = "Lista las ordenes registradas por un filtro especifico.",
            path = "/api/listorders/laboratoryremmision",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/laboratoryremmision", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> listFilterLaboratoryRemmision(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<Order> list = listService.listFilterLaboratoryRemmision(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ LISTAR LABORATORIO DE REFERENCIA PARA REMITIR ----------------
    @ApiMethod(
            description = "Guarda las ordenes remitidas a otro laboratorio.",
            path = "/api/listorders/savelaboratoryremmision",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/savelaboratoryremmision", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> savelaboratoryremmision(
            @ApiBodyObject(clazz = Filter.class) @RequestBody RemissionLaboratory orders
    ) throws Exception
    {
        List<Order> list = listService.savelaboratoryremmision(orders);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR LABORATORIO DE REFERENCIA  ----------------
    @ApiMethod(
            description = "Lista las ordenes registradas por un filtro especifico.",
            path = "/api/listorders/laboratory/hl7",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/laboratory/hl7", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listFilterLaboratoryHL7(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        String hl7 = listService.listFiltersLaboratoryHL7(filter);
        if (hl7.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(hl7, HttpStatus.OK);
        }
    }

    //------------ Envio de códigos de barras en bloque----------------
    @ApiMethod(
            description = "Peticion de envio de generacion de etiquetas por un filtro especifico.",
            path = "/api/listorders/barcode",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/barcode", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> listFilterBarcode(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter, HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(listService.barcodeGeneration(filter), HttpStatus.OK);
    }

    //------------ LISTAR ORDENES SIN PACIENTES ----------------
    @ApiMethod(
            description = "Lista las ordenes registradas que no tienen paciente asignado.",
            path = "/api/listorders/filter/nopatient",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/filter/nopatient", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> listNoPatient(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<Order> list = listService.listFiltersNoPatient(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene una lista ordenes con examenes pendientes dentro de un rango de ordenes",
            path = "/api/listorders/filter/listPendingExams",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/filter/listPendingExams", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> listPendingExams(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<Order> list = listService.listPendingExams(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    // ---------------- EXAMENES VERIFICADOS EN UNA SEDE POR RANGO DE ORDENES------------------
    @ApiMethod(
            description = "Lista de ordenes con muestras verificadas",
            path = "/api/listorders/testcheckbybranch",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/testcheckbybranch", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getTestChekByBranchs(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<Order> lists = listService.getTestChekByBranch(filter);
        if (lists.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(lists, HttpStatus.OK);
        }
    }

    // ---------------- ORDENES PARA REMISION ------------------
    @ApiMethod(
            description = "Lista de ordenes para remision",
            path = "/api/listorders/remission",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/remission", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getRemissionOrders(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<Order> lists = listService.getRemissionOrders(filter);
        if (lists.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(lists, HttpStatus.OK);
        }
    }
    
    
    //---------------------------------------------------------------REMISION SISTEMA CENTRAL PANAMA
    
    
       // ---------------- ORDENES PARA REMISION ------------------
    @ApiMethod(
            description = "Lista de ordenes para remision",
            path = "/api/listorders/remissionCentralOrders",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/remissionCentralOrders", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getListRemissionOrders(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<Order> lists = listService.getListRemissionOrders(filter);
        if (lists.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(lists, HttpStatus.OK);
        }
    }
    
    //------------ LISTAR LABORATORIO DE REFERENCIA PARA REMITIR ----------------
    @ApiMethod(
            description = "Lista los examenes remitidos y no de una orden.",
            path = "/api/listorders/remissionCentralOrders/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = RemissionOrderCentral.class)
    @RequestMapping(value = "/remissionCentralOrders/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RemissionOrderCentral>> getRemissionCentralOrders(
            @ApiPathParam(name = "order", description = "Numero de la Orden") @PathVariable("order") long order) throws Exception
    {
       // List<RemissionOrderCentral> list = listService.getRemissionCentralOrders(order);
        List<RemissionOrderCentral> list = listService.getRemissionCentralOrders(order);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
     
    @ApiMethod(
            description = "Inserta orden y examenes a remitir",
            path = "/api/listorders/remissionCentralOrders",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/remissionCentralOrders", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> status(
            @ApiBodyObject(clazz = RemissionOrderCentral.class) @RequestBody List<RemissionOrderCentral> tests
    ) throws Exception
    {
        return new ResponseEntity<>(listService.insertRemmisioncTest(tests), HttpStatus.OK);
    }
    
      //------------ LISTAR FILTRO ESPECIFICO ----------------
    @ApiMethod(
            description = "consulta las remisiones en un rango de fechas.",
            path = "/api/listorders/listremissionCentralOrders",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = RemissionOrderCentral.class)
    @RequestMapping(value = "/listremissionCentralOrders", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RemissionOrderCentral>> listremissionCentralOrders(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<RemissionOrderCentral> list = listService.listremissionCentralOrders(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
}

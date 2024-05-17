package net.cltech.enterprisent.controllers.operation.orders;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.domain.masters.test.Requirement;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.billing.FilterTestPrice;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.FilterSubsequentPayments;
import net.cltech.enterprisent.domain.operation.orders.InconsistentOrder;
import net.cltech.enterprisent.domain.operation.orders.LastOrderPatient;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderNumSearch;
import net.cltech.enterprisent.domain.operation.orders.OrderSearch;
import net.cltech.enterprisent.domain.operation.orders.OrderTestDetail;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.ShiftOrder;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.orders.TestPrice;
import net.cltech.enterprisent.domain.operation.orders.TicketTest;
import net.cltech.enterprisent.domain.operation.tracking.PackageTracking;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.tools.JWT;
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
 * @author dcortes
 * @since 30/06/2017
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
    private OrderService orderService;
    @Autowired
    private ResultsService resultService;

    @ApiMethod(
            description = "Obtiene una orden del sistema",
            path = "/api/orders/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> get(
            @ApiPathParam(name = "id", description = "Numero de orden completo") @PathVariable("id") long order
    ) throws Exception
    {
        Order orderObject = orderService.getEntry(order);
        if (orderObject != null)
        {
            return new ResponseEntity<>(orderObject, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Obtiene una orden del sistema",
            path = "/api/orders/orderedittest/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/orderedittest/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getOrderEditTest(
            @ApiPathParam(name = "id", description = "Numero de orden completo") @PathVariable("id") long order
    ) throws Exception
    {
        Order orderObject = orderService.getOrderEditTest(order);
        if (orderObject != null)
        {
            return new ResponseEntity<>(orderObject, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene una orden del sistema",
            path = "/api/orders/emailItem/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/emailItem/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getEmail(
            @ApiPathParam(name = "id", description = "Numero de orden completo") @PathVariable("id") long order
    ) throws Exception
    {
        Order orderObject = orderService.getEmail(order);
        if (orderObject != null)
        {
            return new ResponseEntity<>(orderObject, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene una orden del sistema",
            path = "/api/orders/getConfigPrint/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/getConfigPrint/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> configPrint(
            @ApiPathParam(name = "id", description = "Numero de orden completo") @PathVariable("id") long order
    ) throws Exception
    {
        Order orderObject = orderService.getConfigPrint(order);
        if (orderObject != null)
        {
            return new ResponseEntity<>(orderObject, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Agregar configuracion de la impresion",
            path = "/api/orders/updateConfigPrint",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/updateConfigPrint", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertConfigPrint(
            @ApiBodyObject(clazz = Order.class) @RequestBody Order order,
            HttpServletRequest request
    ) throws Exception
    {
        Order record = orderService.updateConfigPrint(order);
        if (record != null)
        {
            return new ResponseEntity(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }
    }

    @ApiMethod(
            description = "Crea una orden en el sistema",
            path = "/api/orders",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> create(
            @ApiBodyObject(clazz = Order.class) @RequestBody Order order,
            HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(orderService.create(order, JWT.decode(request).getId(), JWT.decode(request).getBranch()), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Crea ordenes en el sistema por un rango.",
            path = "/api/orders/batch/init/{init}/quantity/{quantity}/type/{type}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/batch/init/{init}/quantity/{quantity}/type/{type}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> createBatch(
            @ApiPathParam(name = "init", description = "Orden Inicial o Cantidad") @PathVariable("init") long init,
            @ApiPathParam(name = "quantity", description = "Cantidad de Ordenes") @PathVariable("quantity") int quantity,
            @ApiPathParam(name = "type", description = "Tipo: 1 -> Manual, 2 -> Automatico") @PathVariable("type") int type,
            @ApiBodyObject(clazz = Order.class) @RequestBody Order order,
            HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(orderService.createBatch(order, init, quantity, type, JWT.decode(request).getId(), JWT.decode(request).getBranch()), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Actualiza una orden en el sistema",
            path = "/api/orders",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> update(
            @ApiBodyObject(clazz = Order.class) @RequestBody Order order,
            HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(orderService.update(order, JWT.decode(request).getId(), JWT.decode(request).getBranch(),0), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene los demograficos y demograficos items asociados al origen Orden o Historia",
            path = "/api/orders/demographics/{origin}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(value = "/demographics/{origin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Demographic>> getDemographics(
            @ApiPathParam(name = "origin", description = "Origen H->Historia, O->Orden") @PathVariable("origin") String origin,
            HttpServletRequest request
    ) throws Exception
    {
        List<Demographic> demos = orderService.getDemographics(origin);
        if (demos != null && !demos.isEmpty())
        {
            return new ResponseEntity(demos, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene los tarifas asociadas a un cliente en formato Demographic ",
            path = "/api/orders/demographics/account/{account}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(value = "/demographics/account/{account}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicItem>> getRateByAccount(
            @ApiPathParam(name = "account", description = "id de cliente") @PathVariable("account") int account,
            HttpServletRequest request
    ) throws Exception
    {
        List<DemographicItem> demos = orderService.getRateByAccount(account);
        if (demos != null && !demos.isEmpty())
        {
            return new ResponseEntity(demos, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Asigna paciente a una orden",
            path = "/api/orders/assign/",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/assign/", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> assign(
            @ApiBodyObject(clazz = Order.class) @RequestBody Order order,
            HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(orderService.assignOrder(order), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Asigna paciente a una orden.(Verificación)",
            path = "/api/orders/assign/verify",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/assign/verify", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> assignVerify(
            @ApiBodyObject(clazz = Order.class) @RequestBody Order order,
            HttpServletRequest request
    ) throws Exception
    {
        orderService.assignOrderVerify(order);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene la orden como una lista de demograficos",
            path = "/api/orders/filter/map/demographics/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicValue.class)
    @RequestMapping(value = "/filter/map/demographics/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicValue>> getAsDemographicList(
            @ApiPathParam(name = "order", description = "Numero completo de Orden") @PathVariable(name = "order") long order
    ) throws Exception
    {
        List<DemographicValue> records = orderService.getAsDemographicList(order, 0);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Activa una orden inactiva",
            path = "/api/orders/activate",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/activate", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> activate(
            @ApiBodyObject(clazz = List.class) @RequestBody List<Long> orders
    ) throws Exception
    {
        List<Order> activated = orderService.activate(orders);
        if (!activated.isEmpty())
        {
            return new ResponseEntity<>(activated, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene el precio de una examen por tarifa y la vigencia activa.",
            path = "/api/orders/price/test/{test}/rate/{rate}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestPrice.class)
    @RequestMapping(value = "/price/test/{test}/rate/{rate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestPrice> getPrice(
            @ApiPathParam(name = "test", description = "Examen") @PathVariable("test") int test,
            @ApiPathParam(name = "rate", description = "Tarifa") @PathVariable("rate") int rate
    ) throws Exception
    {
        return new ResponseEntity<>(orderService.getPriceTest(test, rate), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene las ordenes asociadas a una historia",
            path = "/api/orders/filter/patientId/{patientId}/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderSearch.class)
    @RequestMapping(value = "/filter/patientId/{patientId}/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderSearch>> getByPatientId(
            @ApiPathParam(name = "patientId", description = "Historia") @PathVariable("patientId") String patientId,
            @ApiPathParam(name = "branch", description = "Id Sede") @PathVariable("branch") int branch
    ) throws Exception
    {
        List<OrderSearch> records = orderService.getByPatientInfo(null, patientId, null, null, null, null, null, null, branch);
        if (!records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene las ordenes asociadas a una historia",
            path = "/api/orders/filter/patientId/{patientId}/documentType/{documentType}/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderSearch.class)
    @RequestMapping(value = "/filter/patientId/{patientId}/documentType/{documentType}/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderSearch>> getByPatientId(
            @ApiPathParam(name = "documentType", description = "Tipo de Documento") @PathVariable("documentType") int documentType,
            @ApiPathParam(name = "patientId", description = "Historia") @PathVariable("patientId") String patientId,
            @ApiPathParam(name = "branch", description = "Id Sede") @PathVariable("branch") int branch
    ) throws Exception
    {
        List<OrderSearch> records = orderService.getByPatientInfo(documentType, patientId, null, null, null, null, null, null, branch);
        if (!records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene las ordenes asociadas buscando por informacion del paciente",
            path = "/api/orders/filter/info/{lastName}/{surName}/{name1}/{name2}/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderSearch.class)
    @RequestMapping(value = "/filter/info/{lastName}/{surName}/{name1}/{name2}/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderSearch>> getByPatientInfo(
            @ApiPathParam(name = "lastName", description = "Apellido, si no se envia colocar undefined") @PathVariable(name = "lastName") String lastName,
            @ApiPathParam(name = "surName", description = "Segundo Apellido, si no se envia colocar undefined") @PathVariable("surName") String surName,
            @ApiPathParam(name = "name1", description = "Nombre 1, si no se envia colocar undefined") @PathVariable("name1") String name1,
            @ApiPathParam(name = "name2", description = "Nombre 2, si no se envia colocar undefined") @PathVariable("name2") String name2,
            @ApiPathParam(name = "branch", description = "Id Sede") @PathVariable("branch") int branch
    ) throws Exception
    {
        List<OrderSearch> records = orderService.getByPatientInfo(null, null, lastName, surName, name1, name2, null, null, branch);
        if (!records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Busca una orden por numero de orden y sede",
            path = "/api/orders/filter/order/{order}/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderSearch.class)
    @RequestMapping(value = "/filter/order/{order}/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderSearch> getByOrder(
            @ApiPathParam(name = "order", description = "Numero de Orden") @PathVariable("order") long order,
            @ApiPathParam(name = "branch", description = "Id Sede") @PathVariable("branch") int branch
    ) throws Exception
    {
        OrderSearch record = orderService.getByOrder(order, branch);
        if (record != null)
        {
            return new ResponseEntity(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Busca una orden por fecha de ingreso y sede",
            path = "/api/orders/filter/entryDate/{entryDate}/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderSearch.class)
    @RequestMapping(value = "/filter/entryDate/{entryDate}/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderSearch>> getByEntryDate(
            @ApiPathParam(name = "entryDate", description = "Fecha de ingreso en formato YYYYMMDD") @PathVariable("entryDate") int entryDate,
            @ApiPathParam(name = "branch", description = "Id Sede") @PathVariable("branch") int branch
    ) throws Exception
    {
        List<OrderSearch> records = orderService.getByEntryDate(entryDate, branch);
        if (!records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Busca el numero de orden por fecha de ingreso y sede",
            path = "/api/orders/filter/entryDateNumOrder/{entryDate}/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderSearch.class)
    @RequestMapping(value = "/filter/entryDateNumOrder/{entryDate}/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderNumSearch> getByEntryDateN(
            @ApiPathParam(name = "entryDate", description = "Fecha de ingreso en formato YYYYMMDD") @PathVariable("entryDate") int entryDate,
            @ApiPathParam(name = "branch", description = "Id Sede") @PathVariable("branch") int branch
    ) throws Exception
    {
        OrderNumSearch records = orderService.getByEntryDateN(entryDate, branch);
        if (!records.getOrders().isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene los examenes para el talon de reclamacion",
            path = "/api/orders/filter/ticket/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TicketTest.class)
    @RequestMapping(value = "/filter/ticket/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TicketTest>> getTicketTest(
            @ApiPathParam(name = "order", description = "Numero de orden") @PathVariable("order") long order
    ) throws Exception
    {
        List<TicketTest> records = orderService.getTicketTest(order);
        if (!records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Consulta opciones de un examen cuando esta siendo ingresado en una orden",
            path = "/api/orders/test/detail/{patient}/{test}/{testType}/{rate}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderTestDetail.class)
    @RequestMapping(value = "/test/detail/{patient}/{test}/{testType}/{rate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderTestDetail> getTestDetail(
            @ApiPathParam(name = "patient", description = "Id BD paciente (-1 si es un paciente nuevo)") @PathVariable("patient") int patientId,
            @ApiPathParam(name = "test", description = "Id BD Prueba") @PathVariable("test") int testId,
            @ApiPathParam(name = "testType", description = "Tipo de prueba(Examen, Perfil, Paquete)") @PathVariable("testType") int testType,
            @ApiPathParam(name = "rate", description = "Id DB Tarifa (-1 si no se maneja tarifa) ") @PathVariable("rate") int rateId
    ) throws Exception
    {
        OrderTestDetail detail = orderService.getOrderTestDetail(patientId, testId, testType, rateId);
        if (detail != null)
        {
            return new ResponseEntity(detail, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene los examenes de una orden que tienen para pedir resultado en el ingreso",
            path = "/api/orders/test/result/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderTestDetail.class)
    @RequestMapping(value = "/test/result/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Test>> getTestToInsertResult(
            @ApiPathParam(name = "order", description = "Numero de Orden") @PathVariable("order") long order
    ) throws Exception
    {
        List<Test> records = resultService.getTestToResultInOrderEntry(order);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene las ordenes para rellamado",
            path = "/api/orders/recall/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderTestDetail.class)
    @RequestMapping(value = "/recall/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderTestDetail> getRecall(
            @ApiPathParam(name = "order", description = "Numero de Orden") @PathVariable("order") long order
    ) throws Exception
    {
        List<OrderSearch> records = orderService.getRecall(order);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene las ordenes para rellamado",
            path = "/api/orders/recall/patientId/{patientId}/{documentType}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderTestDetail.class)
    @RequestMapping(value = "/recall/patientId/{patientId}/{documentType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderTestDetail> getRecall(
            @ApiPathParam(name = "patientId", description = "Historia") @PathVariable("patientId") String patientId,
            @ApiPathParam(name = "documentType", description = "Id Tipo Documento, -1 si no se maneja tipo documento") @PathVariable("documentType") int documentType
    ) throws Exception
    {
        List<OrderSearch> records = orderService.getRecall(documentType, patientId);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene las ordenes para rellamado",
            path = "/api/orders/recall/patient/{lastName}/{surName}/{name1}/{name2}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderTestDetail.class)
    @RequestMapping(value = "/recall/patient/{lastName}/{surName}/{name1}/{name2}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderTestDetail> getRecall(
            @ApiPathParam(name = "lastName", description = "Apellido, vacio si no se realiza filtro") @PathVariable("lastName") String lastName,
            @ApiPathParam(name = "surName", description = "Segundo Apellido, vacio si no se realiza filtro") @PathVariable("surName") String surName,
            @ApiPathParam(name = "name1", description = "Nombre, vacio si no se realiza filtro") @PathVariable("name1") String name1,
            @ApiPathParam(name = "name2", description = "Segundo Nombre, vacio si no se realiza filtro") @PathVariable("name2") String name2
    ) throws Exception
    {
        List<OrderSearch> records = orderService.getRecall(lastName, surName, name1, name2);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene las ordenes para rellamado",
            path = "/api/orders/recall/date/{dateI}/{dateF}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderTestDetail.class)
    @RequestMapping(value = "/recall/date/{dateI}/{dateF}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderTestDetail> getRecall(
            @ApiPathParam(name = "dateI", description = "Fecha inicial en formato YYYYMMDD") @PathVariable("dateI") int dateI,
            @ApiPathParam(name = "dateF", description = "Fecha final en formato YYYYMMDD") @PathVariable("dateF") int dateF
    ) throws Exception
    {
        List<OrderSearch> records = orderService.getRecall(dateI, dateF);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene las ordenes para rellamado",
            path = "/api/orders/recall/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderTestDetail.class)
    @RequestMapping(value = "/recall/{order}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> recall(
            @ApiPathParam(name = "order", description = "Numero de la orden") @PathVariable("order") long order,
            HttpServletRequest request
    ) throws Exception
    {
        Order record = orderService.recall(order, JWT.decode(request).getId(), JWT.decode(request).getBranch());
        if (record != null)
        {
            return new ResponseEntity(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }
    }

    @ApiMethod(
            description = "Cambia el estado de una orden",
            path = "/api/orders/cancel/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/cancel/{order}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> cancel(
            @ApiPathParam(name = "order", description = "Numero de la orden") @PathVariable("order") long order,
            HttpServletRequest request
    ) throws Exception
    {
        Order record = orderService.cancel(order, JWT.decode(request).getId());
        if (record != null)
        {
            return new ResponseEntity(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }
    }

    @ApiMethod(
            description = "Obtiene los examenes de una orden por resultado de ingreso",
            path = "/api/orders/test/entry/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Test.class)
    @RequestMapping(value = "/test/entry/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Test>> getTestToOderByResultEntry(
            @ApiPathParam(name = "order", description = "Numero de Orden") @PathVariable("order") long order
    ) throws Exception
    {
        List<Test> listTest = resultService.getTestToResultRegister(order);
        if (listTest != null && !listTest.isEmpty())
        {
            return new ResponseEntity(listTest, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Agrega diagnosticos a una orden",
            path = "/api/orders/insertdiagnostic",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/insertdiagnostic", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertDiagnostic(
            @ApiBodyObject(clazz = Order.class) @RequestBody Order order,
            HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(orderService.updateDiagnosticsByOrder(order), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Lista de ordenes sin turno por historia y tipo de documento.",
            path = "/api/orders/withoutturn/{history}/{type}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/withoutturn/{history}/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> withoutTurn(
            @ApiPathParam(name = "history", description = "Valor de la historia asociada a la orden") @PathVariable("history") String history,
            @ApiPathParam(name = "type", description = "Tipo de documento") @PathVariable("type") String type
    ) throws Exception
    {
        List<Order> list = orderService.withoutTurn(history, type);
        if (list != null)
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
     @ApiMethod(
            description = "Lista de ordenes sin turno por historia y tipo de documento.",
            path = "/api/orders/appointment/withoutturn/{history}/{type}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/appointment/withoutturn/{history}/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> withoutAppointmentTurn(
            @ApiPathParam(name = "history", description = "Valor de la historia asociada a la orden") @PathVariable("history") String history,
            @ApiPathParam(name = "type", description = "Tipo de documento") @PathVariable("type") String type
    ) throws Exception
    {
        List<Order> list = orderService.withoutAppointmentTurn(history, type);
        if (list != null)
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Lista de ordenes sin turno por historia.",
            path = "/api/orders/withoutturnbyhistory/{history}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/withoutturnbyhistory/{history}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> withoutturnbyhistory(
            @ApiPathParam(name = "history", description = "Valor de la historia asociada a la orden") @PathVariable("history") String history
    ) throws Exception
    {
        List<Order> list = orderService.withoutTurn(history, null);
        if (list != null)
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Lista de ordenes sin turno por historia.",
            path = "/api/orders/appointment/withoutturnbyhistory/{history}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/appointment/withoutturnbyhistory/{history}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> withoutturnbyhistoryappointment(
            @ApiPathParam(name = "history", description = "Valor de la historia asociada a la orden") @PathVariable("history") String history
    ) throws Exception
    {
        List<Order> list = orderService.withoutAppointmentTurn(history, null);
        if (list != null)
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Asociar turno a una orden.",
            path = "/api/orders/shiftorders",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/shiftorders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> shiftOrders(
            @ApiBodyObject(clazz = ShiftOrder.class) @RequestBody ShiftOrder object
    ) throws Exception
    {
        return new ResponseEntity<>(orderService.shiftOrders(object), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Lista de ordenes por turno.",
            path = "/api/orders/ordersbyturn/{turn}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/ordersbyturn/{turn}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> ordersByTurn(
            @ApiPathParam(name = "turn", description = "Turno") @PathVariable("turn") String turn
    ) throws Exception
    {
        List<Order> list = orderService.ordersByTurn(turn);
        if (list != null)
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene las ordenes asociadas a una historia que esten en estado de retoma",
            path = "/api/orders/filter/recalled/patientId/{patientId}/documentType/{documentType}/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/filter/recalled/patientId/{patientId}/documentType/{documentType}/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getByPatientIdRecalled(
            @ApiPathParam(name = "documentType", description = "Tipo de Documento") @PathVariable("documentType") int documentType,
            @ApiPathParam(name = "patientId", description = "Historia") @PathVariable("patientId") String patientId,
            @ApiPathParam(name = "branch", description = "Id Sede") @PathVariable("branch") int branch
    ) throws Exception
    {
        List<Order> records = orderService.getByPatientInfoRecalled(documentType, patientId, null, null, null, null, null, null, branch);
        if (!records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene las ordenes asociadas buscando por informacion del paciente que tenga examenes en estado de retoma",
            path = "/api/orders/filter/recalled/info/{lastName}/{surName}/{name1}/{name2}/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/filter/recalled/info/{lastName}/{surName}/{name1}/{name2}/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getByPatientInfoRecalled(
            @ApiPathParam(name = "lastName", description = "Apellido, si no se envia colocar undefined") @PathVariable(name = "lastName") String lastName,
            @ApiPathParam(name = "surName", description = "Segundo Apellido, si no se envia colocar undefined") @PathVariable("surName") String surName,
            @ApiPathParam(name = "name1", description = "Nombre 1, si no se envia colocar undefined") @PathVariable("name1") String name1,
            @ApiPathParam(name = "name2", description = "Nombre 2, si no se envia colocar undefined") @PathVariable("name2") String name2,
            @ApiPathParam(name = "branch", description = "Id Sede") @PathVariable("branch") int branch
    ) throws Exception
    {
        List<Order> records = orderService.getByPatientInfoRecalled(null, null, lastName, surName, name1, name2, null, null, branch);
        if (!records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Busca una orden por numero de orden y sede que tenga examenes en estado de retoma",
            path = "/api/orders/filter/recalled/order/{order}/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/filter/recalled/order/{order}/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getByOrderRecalled(
            @ApiPathParam(name = "order", description = "Numero de Orden") @PathVariable("order") long order,
            @ApiPathParam(name = "branch", description = "Id Sede") @PathVariable("branch") int branch
    ) throws Exception
    {
        Order record = orderService.getByOrderRecalled(order, branch);
        if (record != null)
        {
            return new ResponseEntity(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Busca una orden por fecha de ingreso y sede que tenga examenes en estado de retoma",
            path = "/api/orders/filter/recalled/entryDatep/{entryDate}/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/filter/recalled/entryDatep/{entryDate}/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getByEntryDateRecalled(
            @ApiPathParam(name = "entryDate", description = "Fecha de ingreso en formato YYYYMMDD") @PathVariable("entryDate") int entryDate,
            @ApiPathParam(name = "branch", description = "Id Sede") @PathVariable("branch") int branch
    ) throws Exception
    {
        List<Order> records = orderService.getByEntryDateRecalled(entryDate, branch);
        if (!records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Busca una orden por numero de orden y valida si es padre o hija de otra y trae el respectivo numero de orden",
            path = "/api/orders/filter/recalled/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/filter/recalled/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getRecalledOrder(
            @ApiPathParam(name = "order", description = "Numero de la orden") @PathVariable("order") long order
    ) throws Exception
    {
        Order records = orderService.getRecalledOrder(order);
        if (records != null)
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualiza el turno de una orden HIS y retona el numero de la orden LIS al q corresponde",
            path = "/api/orders/orderhisturn",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/orderhisturn", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> updateTurnOrderhis(
            @ApiBodyObject(clazz = Order.class) @RequestBody Order order,
            HttpServletRequest request
    ) throws Exception
    {
        Order records = orderService.updateTurnOrderhis(order);
        if (records.getOrderNumber() != null)
        {
            return new ResponseEntity<>(records.getOrderNumber(), HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //------------------------ LA MANERA EN LA QUE SE ENVIA EL NUMERO DE LA ORDEN PUEDE VARIAR -------------------------
    @ApiMethod(
            description = "Verifica que el número de la orden sea valido",
            path = "/api/orders/isValidOrder/idOrder/{idOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/isValidOrder/idOrder/{idOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isValidOrder(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder
    ) throws Exception
    {
        return new ResponseEntity<>(orderService.isValidOrder(idOrder), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene los requerimientos asociados a una orden",
            path = "/api/orders/getRequirements/idOrder/{idOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/getRequirements/idOrder/{idOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRequirements(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder
    ) throws Exception
    {
        String requitements = orderService.getRequirements(idOrder);
        if (!requitements.isEmpty())
        {
            return new ResponseEntity<>(requitements, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene los usuarios q han validado la orden con su respectiva firma",
            path = "/api/orders/getUserValidate/idOrder/{idOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/getUserValidate/idOrder/{idOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getUserValidate(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder
    ) throws Exception
    {
        List<User> userlist = orderService.getUserValidate(idOrder);
        if (!userlist.isEmpty())
        {
            return new ResponseEntity<>(userlist, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene información de una orden para ser enviada a infinity",
            path = "/api/orders/getPatientForExternalIdOrder/idExternalOrder/{idExternalOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/getPatientForExternalIdOrder/idExternalOrder/{idExternalOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Patient> getPatientForExternalIdOrder(
            @ApiPathParam(name = "idExternalOrder", description = "idExternalOrder") @PathVariable("idExternalOrder") String idExternalOrder
    ) throws Exception
    {
        Patient patientOrderExt = orderService.getPatientForExternalIdOrder(idExternalOrder);
        if (patientOrderExt != null)
        {
            return new ResponseEntity<>(patientOrderExt, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Verifica la existencia de una orden en el LIS respecto a un id de una orden de un sistema externo",
            path = "/api/orders/orderExistsByExternalSystemOrder/idExternalOrder/{idExternalOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/orderExistsByExternalSystemOrder/idExternalOrder/{idExternalOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> orderExistsByExternalSystemOrder(
            @ApiPathParam(name = "idExternalOrder", description = "idExternalOrder") @PathVariable("idExternalOrder") String idExternalOrder
    ) throws Exception
    {
        return new ResponseEntity<>(orderService.orderExistsByExternalSystemOrder(idExternalOrder), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene un listado de los números de las ordenes según el filtro enviado, para realizar los pagos posteriores del listado de ordenes",
            path = "/api/orders/filter/subsequentPayments",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/filter/subsequentPayments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Long>> subsequentPayments(
            @ApiBodyObject(clazz = FilterSubsequentPayments.class) @RequestBody FilterSubsequentPayments filter
    ) throws Exception
    {
        List<Long> records = orderService.subsequentPayments(filter);
        if (!records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    //Consulta la ultima orden del paciente
    @ApiMethod(
            description = "Obtiene la ultima orden de un paciente",
            path = "/api/orders/last/patient/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = LastOrderPatient.class)
    @RequestMapping(value = "/last/patient/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LastOrderPatient> getLastOrder(
            @ApiPathParam(name = "id", description = "Id del paciente") @PathVariable("id") int id
    ) throws Exception
    {
        LastOrderPatient orderObject = orderService.getLastOrder(id);
        if (orderObject != null)
        {
            return new ResponseEntity<>(orderObject, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene todas las ordenes con inconsistencias",
            path = "/api/orders/getOrdersWithInconsistencies",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getOrdersWithInconsistencies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InconsistentOrder>> getOrdersWithInconsistencies() throws Exception
    {
        List<InconsistentOrder> orders = orderService.getOrdersWithInconsistencies();
        if (!orders.isEmpty())
        {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene el precio de una lista de examenes por tarifa y la vigencia activa.",
            path = "/api/orders/prices/tests",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/prices/tests", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestPrice>> getPricesTests(
            @ApiBodyObject(clazz = FilterTestPrice.class) @RequestBody FilterTestPrice filter
    ) throws Exception
    {
        List<TestPrice> prices = orderService.getPricesTests(filter);
        if (!prices.isEmpty())
        {
            return new ResponseEntity(prices, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Inserta la auditoria de los paquetes",
            path = "/api/orders/packagetracking",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/packagetracking", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> packageTracking(
            @ApiBodyObject(clazz = PackageTracking.class) @RequestBody List<PackageTracking> tracking,
            HttpServletRequest request
    ) throws Exception
    {
        orderService.packageTracking(tracking);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @ApiMethod(
            description = "Obtiene la auditoria de paquetes de una orden",
            path = "/api/orders/packagetracking/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PackageTracking.class)
    @RequestMapping(value = "/packagetracking/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PackageTracking>> getPackageTracking(
            @ApiPathParam(name = "order", description = "Ordern") @PathVariable("order") Long order,
            HttpServletRequest request
    ) throws Exception
    {
        List<PackageTracking> tracking = orderService.getPackageTracking(order);
        if (tracking != null && !tracking.isEmpty())
        {
            return new ResponseEntity(tracking, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Obtiene los requisitos de una orden del sistema",
            path = "/api/orders/requirement/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/requirement/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Requirement>> getRequeriment(
            @ApiPathParam(name = "id", description = "Numero de orden completo") @PathVariable("id") long order
    ) throws Exception
    {
        
        List<Requirement> orderObject = orderService.getRequirement(order);
        if (orderObject != null)
        {
            return new ResponseEntity<>(orderObject, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}

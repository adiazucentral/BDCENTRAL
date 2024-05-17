package net.cltech.enterprisent.controllers.operation.orders;

import java.util.List;
import net.cltech.enterprisent.domain.operation.filters.OrderSearchFilter;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderSearchService;
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
        name = "Busqueda Ordenes",
        group = "Operación - Ordenes",
        description = "Servicios Rest sobre busquedas de ordenes"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/searchorders")
public class OrderSearchController {

    @Autowired
    private OrderSearchService service;

    @ApiMethod(
            description = "Lista las ordenes por el número de identificación del paciente",
            path = "/api/searchorders/record/{record}/document/{document}/init/{init}/end/{end}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/record/{record}/document/{document}/init/{init}/end/{end}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Patient>> listByRecord(
            @ApiPathParam(name = "record", description = "Historia del paciente") @PathVariable("record") String record,
            @ApiPathParam(name = "document", description = "Tipo de documento (Opcional)") @PathVariable("document") int document,
            @ApiPathParam(name = "init", description = "Fecha inicial orden  (Opcional)") @PathVariable("init") int init,
            @ApiPathParam(name = "end", description = "Fecha final orden (Opcional)") @PathVariable("end") int end
    ) throws Exception {
        List<Patient> patients = service.listByRecord(record, document, init, end);
        if (!patients.isEmpty()) {
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Lista ordenes por apellido",
            path = "/api/searchorders/name/{name}/name1/{name1}/lastname/{lastName}/surname/{surname}/gender/{gender}/init/{init}/end/{end}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/name/{name}/name1/{name1}/lastname/{lastName}/surname/{surname}/gender/{gender}/init/{init}/end/{end}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Patient>> listByLastName(
            @ApiPathParam(name = "name", description = "Primer nombre del paciente") @PathVariable("name") String name,
            @ApiPathParam(name = "name1", description = "name1") @PathVariable("name1") String name1,
            @ApiPathParam(name = "lastName", description = "Primer apellido del paciente") @PathVariable("lastName") String lastName,
            @ApiPathParam(name = "surname", description = "Segundo apellido del paciente") @PathVariable("surname") String surName,
            @ApiPathParam(name = "gender", description = "Genero del paciente") @PathVariable("gender") int gender,
            @ApiPathParam(name = "init", description = "Fecha inicial orden  (Opcional)") @PathVariable("init") int init,
            @ApiPathParam(name = "end", description = "Fecha final orden (Opcional)") @PathVariable("end") int end
    ) throws Exception {
        List<Patient> patients = service.listByLastName(name, name1, lastName, surName, gender, init, end);
        if (!patients.isEmpty()) {
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Lista ordenes por rango de fechas",
            path = "/api/searchorders/init/{init}/end/{end}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/init/{init}/end/{end}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Patient>> listByDates(
            @ApiPathParam(name = "init", description = "Fecha inicial orden  (Opcional)") @PathVariable("init") int init,
            @ApiPathParam(name = "end", description = "Fecha final orden (Opcional)") @PathVariable("end") int end
    ) throws Exception {
        List<Patient> patients = service.listByDates(init, end);
        if (!patients.isEmpty()) {
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Lista ordenes por numero de orden",
            path = "/api/searchorders/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Patient>> listByOrder(
            @ApiPathParam(name = "order", description = "Numero de la orden") @PathVariable("order") long order
    ) throws Exception {
        List<Patient> patients = service.listByOrder(order);
        if (!patients.isEmpty()) {
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Lista ordenes por filtro",
            path = "/api/searchorders/filter",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/filter", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> listByFilter(
            @ApiBodyObject(clazz = OrderSearchFilter.class) @RequestBody OrderSearchFilter filter
    ) throws Exception {
        List<Order> orders = service.listByFilter(filter);
        if (!orders.isEmpty()) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Lista ordenes por filtro de pacientes especificos",
            path = "/api/searchorders/filterpatient",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/filterpatient", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderList>> getOrdersbyPatient(
            @ApiBodyObject(clazz = OrderSearchFilter.class) @RequestBody OrderSearchFilter filter
    ) throws Exception {
        List<OrderList> orders = service.getOrdersbyPatient(filter);
        if (!orders.isEmpty()) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

      @ApiMethod(
            description = "Lista ordenes por filtro de pacientes especificos que tenga muestras almacenadas",
            path = "/api/searchorders/filterpatientstorage",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/filterpatientstorage", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderList>> getOrdersbyPatientStorage( @ApiBodyObject(clazz = OrderSearchFilter.class) @RequestBody OrderSearchFilter filter
    ) throws Exception {
        List<OrderList> orders = service.getOrdersbyPatientStorage(filter);
        if (!orders.isEmpty()) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Servicio para obtener el listado de ordenes con informacion del paciente.",
            path = "/api/searchorders/orderswithpatient",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Patient.class)
    @RequestMapping(value = "/orderswithpatient", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> ordersWithPatient(
            @ApiBodyObject(clazz = OrderSearchFilter.class) @RequestBody OrderSearchFilter filter
    ) throws Exception {
        List<Order> orders = service.ordersWithPatient(filter);
        if (!orders.isEmpty()) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}

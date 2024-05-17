/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.operation.appointment;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.masters.user.Email;
import net.cltech.enterprisent.domain.operation.appointment.Appointment;

import net.cltech.enterprisent.domain.operation.appointment.Availability;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderSearch;
import net.cltech.enterprisent.service.interfaces.operation.appointment.AppointmentService;
import net.cltech.enterprisent.service.interfaces.operation.orders.OrderService;
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

@Api(
        group = "Operación",
        name = "Citas",
        description = "Servicios sobre las citas de la aplicación"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    
    @Autowired
    private AppointmentService service;
    
    @Autowired
    private OrderService orderService;
    
    @ApiMethod(
            description = "Obtiene los dias que hay disponibilidad de citas segùn un rango",
            path = "/api/appointments/availability/branch/{branch}/range/{init}/{quantity}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Availability.class)
    @RequestMapping(value = "/availability/branch/{branch}/range/{init}/{quantity}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Availability>> listAvailability(
            @ApiPathParam(name = "branch", description = "branch") @PathVariable(name = "branch") int branch,
            @ApiPathParam(name = "init", description = "Fecha inicial", format = "yyyMMdd") @PathVariable(name = "init") int init,
            @ApiPathParam(name = "quantity", description = "Cantida de dias") @PathVariable(name = "quantity") int quantity
    ) throws Exception
    {
        List<Availability> records = service.listAvailabilityRange(branch, init, quantity);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
      //------------ CONSULTA POR ZONA Y FECHA ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un festivo",
            path = "/api/appointments/branch/{branch}/date/{date}/day/{day}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Availability.class)
    @RequestMapping(value = "/branch/{branch}/date/{date}/day/{day}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Availability> getAvailability(
            @ApiPathParam(name = "branch", description = "sede") 
            @PathVariable(name = "branch") int branch, 
            @ApiPathParam(name = "date", description = "Fecha Agendamiento") 
            @PathVariable(name = "date") int date,
            @ApiPathParam(name = "day", description = "Día de la semana") 
            @PathVariable(name = "day") int day
    ) throws Exception
    {
        Availability availability = service.getAvailability(branch, date, day);

        if (availability == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {

            return new ResponseEntity<>(availability, HttpStatus.OK);
        }
    }
    
     //------------ REGISTRAR CONCURRENCIA ----------------
    @ApiMethod(
            description = "Registra concurrencia",
            path = "/api/appointments/concurrences",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/concurrences", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> createConcurrence(@ApiBodyObject(clazz = Appointment.class) @RequestBody(required = true) Appointment appointment) throws Exception
    {
        
        return new ResponseEntity<>(service.createConcurrence(appointment), HttpStatus.OK);
    }
    
     //------------ ELIMINAR CONCURRENCIA ----------------
    @ApiMethod(
            description = "Elimina todas concurrencias de citas.",
            path = "/api/appointments/concurrences",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/concurrences", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteConcurrence() throws Exception
    {
        return new ResponseEntity<>(service.deleteConcurrence(), HttpStatus.OK);
    }

    //------------ ELIMINAR CONCURRENCIA ----------------
    @ApiMethod(
            description = "Elimina concurrencias de citas.",
            path = "/api/appointments/concurrences/date/{date}/shift/{shift}/branch/{branch}/user/{user}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/concurrences/date/{date}/shift/{shift}/branch/{branch}/user/{user}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteConcurrence(
            @ApiPathParam(name = "date", description = "Fecha de la cita", format = "yyyMMdd") @PathVariable(name = "date") Integer date,
            @ApiPathParam(name = "shift", description = "Jornada") @PathVariable(name = "shift") Integer idShift,
            @ApiPathParam(name = "branch", description = "branch") @PathVariable(name = "branch") Integer branch,
            @ApiPathParam(name = "user", description = "Usuario que registro") @PathVariable(name = "user") Integer idUser
    ) throws Exception
    {
        return new ResponseEntity<>(service.deleteConcurrence(date, idShift, branch, idUser), HttpStatus.OK);
    }

    //------------ ELIMINAR CONCURRENCIA ----------------
    @ApiMethod(
            description = "Elimina concurrencias de una cita.",
            path = "/api/appointments/concurrences/date/{date}/shift/{shift}/branch/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/concurrences/date/{date}/shift/{shift}/branch/{branch}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteConcurrence(
            @ApiPathParam(name = "date", description = "Fecha de la cita", format = "yyyMMdd") @PathVariable(name = "date") Integer date,
            @ApiPathParam(name = "shift", description = "Jornada") @PathVariable(name = "shift") Integer idShift,
            @ApiPathParam(name = "branch", description = "branch") @PathVariable(name = "branch") Integer branch
    ) throws Exception
    {
        return new ResponseEntity<>(service.deleteConcurrence(date, idShift, branch), HttpStatus.OK);
    }
    
        //------------ ELIMINAR CONCURRENCIA ----------------
    @ApiMethod(
            description = "Elimina concurrencias por id",
            path = "/api/appointments/concurrences/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/concurrences/id/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteConcurrencebyId(
            @ApiPathParam(name = "id", description = "id de la concurrencia") @PathVariable(name = "id") String id
    ) throws Exception
    {
        Integer idc = Integer.parseInt(id);
        return new ResponseEntity<>(service.deleteConcurrence(idc), HttpStatus.OK);
    }
    
    @ApiMethod(
        description = "Realiza la reprogramación de una cita.",
        path = "/api/appointments/reprogram",
        visibility = ApiVisibility.PUBLIC,
        verb = ApiVerb.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/reprogram", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> reprogramAppointment(@ApiBodyObject(clazz = Order.class) @RequestBody Order order) throws Exception
    {
        return new ResponseEntity<>(service.reprogramAppointment(order), HttpStatus.OK);
    }
    
    @ApiMethod(
        description = "Realiza la cancelacion de una cita.",
        path = "/api/appointments/cancel",
        visibility = ApiVisibility.PUBLIC,
        verb = ApiVerb.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/cancel", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> cancelAppointment(@ApiBodyObject(clazz = Order.class) @RequestBody Order order) throws Exception
    {
        return new ResponseEntity<>(service.cancelAppointment(order), HttpStatus.OK);
    }
    
    @ApiMethod(
            description = "Busca una cita por fecha de ingreso y sede",
            path = "/api/appointments/filter/entryDate/{entryDate}/{branch}",
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
        List<OrderSearch> records = service.getByEntryDate(entryDate, branch);
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
            path = "/api/appointments/filter/order/{order}/{branch}",
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
        OrderSearch record = service.getByOrder(order, branch);
        if (record != null)
        {
            return new ResponseEntity(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
     @ApiMethod(
            description = "Obtiene las ordenes asociadas a una historia",
            path = "/api/appointments/filter/patientId/{patientId}/{branch}",
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
        List<OrderSearch> records = service.getByPatientInfo(null, patientId, null, null, null, null, null, null, branch);
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
            path = "/api/appointments/filter/patientId/{patientId}/documentType/{documentType}/{branch}",
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
        List<OrderSearch> records = service.getByPatientInfo(documentType, patientId, null, null, null, null, null, null, branch);
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
            path = "/api/appointments/filter/info/{lastName}/{surName}/{name1}/{name2}/{branch}",
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
        List<OrderSearch> records = service.getByPatientInfo(null, null, lastName, surName, name1, name2, null, null, branch);
        if (!records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
       @ApiMethod(
        description = "Realiza la conversion de una cita en orden.",
        path = "/api/appointments/changeappointment",
        visibility = ApiVisibility.PUBLIC,
        verb = ApiVerb.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/changeappointment", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> changeappointment(@ApiBodyObject(clazz = Order.class) @RequestBody Order order) throws Exception
    {
        return new ResponseEntity<>(service.changeappointment(order), HttpStatus.OK);
    }
    
    @ApiMethod(
            description = "Obtiene una cita del sistema",
            path = "/api/appointments/{id}",
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
        Order orderObject = orderService.getEntry(order,1);
        if (orderObject != null)
        {
            return new ResponseEntity<>(orderObject, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Actualiza una cita en el sistema",
            path = "/api/appointments",
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
        return new ResponseEntity<>(orderService.update(order, JWT.decode(request).getId(), JWT.decode(request).getBranch(), 1), HttpStatus.OK);
    }
    
    @ApiMethod(
            description = "Enviar un correo de citas",
            path = "/api/appointments/email",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/email", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendEmail(
            @ApiBodyObject(clazz = Email.class) @RequestBody Email email
    ) throws Exception
    {
        return new ResponseEntity<>(service.sendEmail(email), HttpStatus.OK);
    }
}

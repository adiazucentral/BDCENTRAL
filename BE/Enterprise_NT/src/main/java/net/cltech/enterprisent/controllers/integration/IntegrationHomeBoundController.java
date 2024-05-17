package net.cltech.enterprisent.controllers.integration;

import java.util.List;
import net.cltech.enterprisent.controllers.common.License;
import net.cltech.enterprisent.domain.integration.homebound.AccountHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.Appointment;
import net.cltech.enterprisent.domain.integration.homebound.BasicHomeboundPatient;
import net.cltech.enterprisent.domain.integration.homebound.BillingTestHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.DemographicHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.DocumentTypeHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.GenderHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.ListOrders;
import net.cltech.enterprisent.domain.integration.homebound.PatientHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.PaymentTypeHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.QuestionHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.RateHomeBound;
import net.cltech.enterprisent.domain.integration.homebound.Track;
import net.cltech.enterprisent.domain.integration.homebound.TransportSampleHomebound;
import net.cltech.enterprisent.domain.masters.billing.HomeboundBox;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationHomeBoundService;
import net.cltech.enterprisent.service.interfaces.masters.billing.HomeboundBoxService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.security.LicenseService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Servicios para el acceso a la informacion de la maestro Pruebas
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 17/02/2020
 * @see Creacion
 */
@Api(
        name = " Integracion HomeBound",
        group = "Integración",
        description = "Servicios generales de integración con Home Bound"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api")
public class IntegrationHomeBoundController
{

    @Autowired
    private IntegrationHomeBoundService integrationHomeBoundService;
    @Autowired
    private HomeboundBoxService homeboundBoxService;
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private ConfigurationService configurationService;

    //------------ LISTAR FILTRO ESPECIFICO ----------------
    @ApiMethod(
            description = "Lista las ordenes registradas por un filtro especifico.",
            path = "/api/homebound/listorders",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @PatchMapping("/homebound/listorders")
    public ResponseEntity<List<Order>> listFilter(
            @ApiBodyObject(clazz = List.class) @RequestBody ListOrders listOrders
    ) throws Exception
    {
        List<Order> list = integrationHomeBoundService.listFilters(listOrders);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ LISTAR FILTRO ESPECIFICO ----------------
    @ApiMethod(
            description = "Lista las ordenes registradas por un filtro especifico.",
            path = "/api/homebound/listbasicorders",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @PatchMapping("/homebound/listbasicorders")
    public ResponseEntity<List<Order>> listBasicFilters(
            @ApiBodyObject(clazz = List.class) @RequestBody ListOrders listOrders
    ) throws Exception
    {
        List<Order> list = integrationHomeBoundService.listBasicFilters(listOrders);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR LOS TIPOS DE DOCUMENTOS CON EL JSON REQUERIDO PARA HOMEBOUND ----------------
    @ApiMethod(
            description = "Lista los tipos de documentos para enviarlos a homebound",
            path = "/api/documentTypes",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DocumentTypeHomeBound.class)
    @GetMapping("/documentTypes")
    public ResponseEntity<List<DocumentTypeHomeBound>> listDocumentTypes() throws Exception
    {
        List<DocumentTypeHomeBound> list = integrationHomeBoundService.listDocumentTypes();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR LOS CLIENTES CON EL JSON REQUERIDO PARA HOMEBOUND ----------------
    @ApiMethod(
            description = "Obtiene los clientes para enviarlos a homebound",
            path = "/api/clients",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AccountHomeBound.class)
    @GetMapping("/clients")
    public ResponseEntity<List<AccountHomeBound>> listAccounts() throws Exception
    {
        List<AccountHomeBound> list = integrationHomeBoundService.listAccounts();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene un listado de los generos en un idioma especifico para enviarlos a homebound",
            path = "/api/sex/{lang}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = GenderHomeBound.class)
    @GetMapping("/sex/{lang}")
    public ResponseEntity<List<GenderHomeBound>> listGenresLanguage(
            @ApiPathParam(name = "lang", description = "Idioma") @PathVariable("lang") String lang
    ) throws Exception
    {
        List<GenderHomeBound> list = integrationHomeBoundService.listGenders();
        if (list == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene una lista de los tipos de pagos para enviarlos a homebound",
            path = "/api/paymentTypes/{lang}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PaymentTypeHomeBound.class)
    @GetMapping("/paymentTypes/{lang}")
    public ResponseEntity<List<PaymentTypeHomeBound>> listPaymentType(
            @ApiPathParam(name = "lang", description = "Idioma") @PathVariable("lang") String lang
    ) throws Exception
    {
        List<PaymentTypeHomeBound> list = integrationHomeBoundService.listPaymentTypes();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene un paciente por su número de historia y tipo de documento para ser enviardo a homebound",
            path = "/api/homebound/patients/filter/{typeDocument}/{patientId}/{lang}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PatientHomeBound.class)
    @GetMapping("/homebound/patients/filter/{typeDocument}/{patientId}/{lang}")
    public ResponseEntity<PatientHomeBound> getPatientHomeBound(
            @ApiPathParam(name = "typeDocument", description = "Tipo de Documento") @PathVariable(name = "typeDocument") int documentType,
            @ApiPathParam(name = "patientId", description = "Historia") @PathVariable(name = "patientId") String patientId,
            @ApiPathParam(name = "lang", description = "Lenguaje") @PathVariable(name = "lang") String lang
    ) throws Exception
    {
        PatientHomeBound patient = integrationHomeBoundService.getPatientHomeBound(documentType, patientId, lang);
        if (patient == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(patient, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Lista los tipos de ordenes para enviarlos a homebound",
            path = "/api/orderTypes/{lang}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderType.class)
    @GetMapping("/orderTypes/{lang}")
    public ResponseEntity<List<OrderType>> listOrderTypes(
            @ApiPathParam(name = "lang", description = "Lenguaje") @PathVariable(name = "lang") String lang
    ) throws Exception
    {
        List<OrderType> list = integrationHomeBoundService.listOrderTypes();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ RECIBE UN JSON DE HOME BOUND PARA CREAR NUEVA ORDEN ----------------     
    @ApiMethod(
            description = "Crea una orden a partir de una cita de homebound",
            path = "/api/homebound",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Long.class)
    @PostMapping("/homebound")
    public ResponseEntity<Long> createAppointment(
            @ApiBodyObject(clazz = Appointment.class) @RequestBody(required = true) Appointment appointment
    ) throws Exception
    {
        return new ResponseEntity<>(integrationHomeBoundService.createAppointment(appointment).getOrderNumber(), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene un paciente por su id para ser enviardo a homebound",
            path = "/api/homebound/patients/{id}/{lang}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PatientHomeBound.class)
    @GetMapping("/homebound/patients/{id}/{lang}")
    public ResponseEntity<PatientHomeBound> getPatientByIdHomeBound(
            @ApiPathParam(name = "id", description = "Identificador del paciente") @PathVariable(name = "id") int id,
            @ApiPathParam(name = "lang", description = "Lenguaje") @PathVariable(name = "lang") String lang
    ) throws Exception
    {
        PatientHomeBound patient = integrationHomeBoundService.getPatientByIdHomeBound(id);
        if (patient == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(patient, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Crea un paciente según el json requerido por homebound",
            path = "/api/homebound/patients",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PatientHomeBound.class)
    @PostMapping("/homebound/patients")
    public ResponseEntity<PatientHomeBound> createPatientHomeBound(
            @ApiBodyObject(clazz = PatientHomeBound.class) @RequestBody PatientHomeBound patient
    ) throws Exception
    {
        return new ResponseEntity<>(integrationHomeBoundService.createPatientHomeBound(patient), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Actualiza un paciente según el json requerido por homebound",
            path = "/api/homebound/patients",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PatientHomeBound.class)
    @PutMapping("/homebound/patients")
    public ResponseEntity<PatientHomeBound> updatePatientHomeBound(
            @ApiBodyObject(clazz = PatientHomeBound.class) @RequestBody PatientHomeBound patient
    ) throws Exception
    {
        return new ResponseEntity<>(integrationHomeBoundService.updatePatientHomeBound(patient), HttpStatus.OK);
    }

    //------------ RECIBE UN JSON DE HOME BOUND PARA CONSULTAR PRECIO DE EXAMEN ---------------- 
    @ApiMethod(
            description = "Obtiene el precio de una prueba para un tarifa requerido por homebound",
            path = "/api/tests/price/{rate}/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = BillingTestHomeBound.class)
    @GetMapping("/tests/price/{rate}/{test}")
    public ResponseEntity<BillingTestHomeBound> getRatetByIdHomeBound(
            @ApiPathParam(name = "rate", description = "Tarifa") @PathVariable(name = "rate") int rate,
            @ApiPathParam(name = "test", description = "Prueba") @PathVariable(name = "test") int test
    ) throws Exception
    {
        BillingTestHomeBound billingTest = integrationHomeBoundService.getRatetByIdHomeBound(rate, test);
        if (billingTest == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(billingTest, HttpStatus.OK);
        }
    }

    //------------ RECIBE UN JSON DE HOMEBOUND REALIZAR LA TOMA DE LA MUESTRA ----------------     
    @ApiMethod(
            description = "Realiza la actualizacion de la toma de muestra según los datos enviados por Homebound",
            path = "/api/homebound/sampletake",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Appointment.class)
    @PutMapping("/homebound/sampletake")
    public ResponseEntity<Appointment> updateSampleTake(
            @ApiBodyObject(clazz = Appointment.class) @RequestBody Appointment appointment
    ) throws Exception
    {
        return new ResponseEntity<>(integrationHomeBoundService.updateSampleTake(appointment), HttpStatus.OK);
    }

    //------------ ACTUALIZA EL ESTADO Y TRAZABILIDAD DE UNA ORDEN DESDE HOME BOUND ----------------
    @ApiMethod(
            description = "Realiza la cancelación de la orden",
            path = "/api/homebound/{appointment}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @DeleteMapping("/homebound/{appointment}")
    public ResponseEntity<Void> updateOrderHomeBound(
            @ApiPathParam(name = "appointment", description = "Id de la Orden") @PathVariable(name = "appointment") long order
    ) throws Exception
    {
        integrationHomeBoundService.cancelOrderHomeBound(order);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //------------ RECIBE UN JSON DE HOME BOUND PARA CANCELAR UNA ORDEN Y CREAR UNA NUEVA DE LA CITA ----------------    
    @ApiMethod(
            description = "Actualiza una cita desde HomeBound",
            path = "/api/homebound/reprogram",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Appointment.class)
    @PutMapping("/homebound/reprogram")
    public ResponseEntity<Long> updateAppointmentHomeBound(
            @ApiBodyObject(clazz = Appointment.class) @RequestBody Appointment appointment
    ) throws Exception
    {
        return new ResponseEntity<>(integrationHomeBoundService.updateAppointmentHomeBound(appointment), HttpStatus.OK);
    }

    //------------ RECIBE UN LISTADO DE ORDENES CON TIPO DE DATO LONG DESDE HOME BOUND ----------------    
    @ApiMethod(
            description = "Retorna un listado de citas dependiendo del listado de ordenes que se envie desde Homebound",
            path = "/api/homebound/orders",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Appointment.class)
    @PostMapping(value = "/homebound/orders")
    public ResponseEntity<List<Appointment>> getAppointments(
            @ApiBodyObject(clazz = List.class) @RequestBody List<Long> orders
    ) throws Exception
    {
        List<Appointment> listAppointments = integrationHomeBoundService.getAppointments(orders);
        if (listAppointments == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(listAppointments, HttpStatus.OK);
        }
    }
    
       //------------ RECIBE UN LISTADO DE ORDENES CON TIPO DE DATO LONG DESDE HOME BOUND ----------------    
    @ApiMethod(
            description = "Consulta a un paciente con su información básica por el número de una orden",
            path = "/api/homebound/getPatientByOrderId/orders",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = BasicHomeboundPatient.class)
    @PostMapping(value = "/homebound/getPatientByOrderId/orders")
    public ResponseEntity<List<BasicHomeboundPatient>> getPatientByOrderId(
            @ApiBodyObject(clazz = List.class) @RequestBody List<Long> orders
    ) throws Exception
    {
        List<BasicHomeboundPatient> listAppointments = integrationHomeBoundService.getPatientByOrderId(orders);
        if (listAppointments == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(listAppointments, HttpStatus.OK);
        }
    }
   
    @ApiMethod(
            description = "Consulta la lista de registros de la caja por el id de la orden",
            path = "/api/cashbook/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = HomeboundBox.class)
    @GetMapping(value = "/cashbook/order/{order}")
    public ResponseEntity<List<HomeboundBox>> getById(
            @ApiPathParam(name = "order", description = "numero de orden") @PathVariable(name = "order") long order
    ) throws Exception
    {
        List<HomeboundBox> homeboundBox = homeboundBoxService.get(order);
        if (homeboundBox == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if (homeboundBox.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(homeboundBox, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Crea una nueva caja",
            path = "/api/cashbook",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = HomeboundBox.class)
    @PostMapping(value = "/cashbook")
    public ResponseEntity<HomeboundBox> createContract(
            @ApiBodyObject(clazz = HomeboundBox.class) @RequestBody HomeboundBox homeboundBox
    ) throws Exception
    {
        return new ResponseEntity<>(homeboundBoxService.create(homeboundBox), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Consulta entrevista de una orden",
            path = "/api/homebound/interview/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = HomeboundBox.class)
    @GetMapping(value = "/homebound/interview/{order}")
    public ResponseEntity<List<QuestionHomeBound>> getInterviewByOrderId(
            @ApiPathParam(name = "order", description = "Número de orden") @PathVariable(name = "order") long orderId
    ) throws Exception
    {
        List<QuestionHomeBound> list = integrationHomeBoundService.getInterviewByOrderId(orderId);
        if (list == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ RECIBE UN JSON DE HOME BOUND PARA REALIZAR LA INSERCION  DE LA ENTREVISTA ----------------
    @ApiMethod(
            description = "Realiza la inserción de la entrevista según los datos enviados por Homebound",
            path = "/api/homebound/interview",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @PutMapping("/homebound/interview")
    public ResponseEntity<Boolean> updateInterview(
            @ApiBodyObject(clazz = Appointment.class) @RequestBody Appointment appointment
    ) throws Exception
    {
        return new ResponseEntity<>(integrationHomeBoundService.updateInterview(appointment), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene los demográficos Item de un Demográfico",
            path = "/api/demographics/demographicItem/{idDemographic}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicHomeBound.class)
    @GetMapping("/demographics/demographicItem/{idDemographic}")
    public ResponseEntity<List<DemographicHomeBound>> getItemDemographicsByDemoId(
            @ApiPathParam(name = "idDemographic", description = "Id del demográfico") @PathVariable(name = "idDemographic") Integer idDemographic
    ) throws Exception
    {
        List<DemographicHomeBound> list = integrationHomeBoundService.getItemDemographicsByDemoId(idDemographic);
        if (list == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene los demograficos del sistema",
            path = "/api/homebound/demographics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicHomeBound.class)
    @GetMapping("/homebound/demographics")
    public ResponseEntity<List<DemographicHomeBound>> getAllDemographics() throws Exception
    {
        List<DemographicHomeBound> list = integrationHomeBoundService.getAllDemographics();
        if (list == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
        @ApiMethod(
            description = "Obtiene los demograficos del sistema",
            path = "/api/homebound/orders/tracking/{orderNumber}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Track.class)
    @GetMapping("/homebound/orders/tracking/{orderNumber}")
    public ResponseEntity<Track> getTracking(
        @ApiPathParam(name = "orderNumber", description = "Numero de orden") @PathVariable(name = "orderNumber") Long orderNumber
    ) throws Exception
    {
        Track list = integrationHomeBoundService.getTracking(orderNumber);
        if (list == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Actualiza una orden proveniente de una cita de homebound",
            path = "/api/homebound",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @PutMapping("/homebound")
    public ResponseEntity<Void> updateAppointment(
            @ApiBodyObject(clazz = Appointment.class) @RequestBody(required = true) Appointment appointment
    ) throws Exception
    {
        boolean updated = integrationHomeBoundService.updateAppointment(appointment);
        if (updated)
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @ApiMethod(
            description = "Actualiza la fecha de atención de la orden",
            path = "/api/homebound/appointmentdate",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @PutMapping("/homebound/appointmentdate")
    public ResponseEntity<Void> updateAppointmentDate(
            @ApiBodyObject(clazz = Long.class) @RequestBody Long orderId
    ) throws Exception
    {
        boolean updated = integrationHomeBoundService.updateAppointmentDate(orderId);
        if (updated)
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @ApiMethod(
            description = "Actualiza la fecha de transporte de la orden",
            path = "/api/homebound/transportsampledate",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @PutMapping("/homebound/transportsampledate")
    public ResponseEntity<Boolean> transportsampledate(
            @ApiBodyObject(clazz = Long.class) @RequestBody List<TransportSampleHomebound> order
    ) throws Exception
    {
        boolean updated = integrationHomeBoundService.updateTransportSample(order);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
    
    @ApiMethod(
            description = "Verifica las licencias de las aplicaciones",
            path = "/api/licenses",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = License.class)
    @GetMapping("/licenses")
    public ResponseEntity<List<License>> licenses() throws Exception
    {
        List<License> licenses = licenseService.licenses();
        if (licenses.size() < 1)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(licenses, HttpStatus.OK);
        }
    }
    
    @ApiMethod(
            description = "Consulta el valor de una llave de configuración",
            path = "/api/settings/handle/{key}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Configuration.class)
    @GetMapping("/settings/handle/{key}")
    public ResponseEntity<Configuration> getKeyValue(
            @ApiPathParam(name = "key", description = "Nombre de la llave de configuración") @PathVariable("key") String key
    ) throws Exception
    {
        Configuration keyValue = configurationService.get(key);
        if (keyValue == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else
        {
            return new ResponseEntity<>(keyValue, HttpStatus.OK);
        }
    }
    
    @ApiMethod(
            description = "Consulta las tarifas de un cliente por el id de este",
            path = "/api/clients/rates/{accountId}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = RateHomeBound.class)
    @GetMapping("/clients/rates/{accountId}")
    public ResponseEntity<List<RateHomeBound>> getRatesByAccountId(
            @ApiPathParam(name = "accountId", description = "Id del cliente") @PathVariable("accountId") int accountId
    ) throws Exception
    {
        List<RateHomeBound> rates = integrationHomeBoundService.getRatesByAccountId(accountId);
        if (rates == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if(rates.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(rates, HttpStatus.OK);
        }
    }
}

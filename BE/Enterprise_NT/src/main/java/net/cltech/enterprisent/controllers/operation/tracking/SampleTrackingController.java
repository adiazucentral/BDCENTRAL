package net.cltech.enterprisent.controllers.operation.tracking;

import java.util.List;
import net.cltech.enterprisent.domain.integration.skl.RequestSampleDestination;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.common.Reason;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.tracking.SampleDelayed;
import net.cltech.enterprisent.domain.operation.tracking.TestSampleTake;
import net.cltech.enterprisent.domain.operation.tracking.TestSampleTakeTracking;
import net.cltech.enterprisent.domain.operation.tracking.TrackingAlarm;
import net.cltech.enterprisent.domain.operation.tracking.VerifyDestination;
import net.cltech.enterprisent.service.interfaces.operation.tracking.SampleTrackingService;
import net.cltech.enterprisent.tools.enums.LISEnum;
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
 * Controlador de servicios Rest sobre la verificacion de la muestra
 *
 * @version 1.0.0
 * @author cmartin
 * @since 19/09/2017
 * @see Creacion
 */
@Api(
        name = "Verificación de la Muestra",
        group = "Operación - Ordenes",
        description = "Servicios Rest sobre la verificacion de la muestra"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/sampletrackings")
public class SampleTrackingController
{

    @Autowired
    private SampleTrackingService service;

    //------------ CONSULTAR ORDEN ----------------
    @ApiMethod(
            description = "Obtiene la información de una orden.",
            path = "/api/sampletrackings/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getOrder(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder
    ) throws Exception
    {
        Order order = service.getOrder(idOrder, true);
        if (order == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
    }

    //------------ TOMAR MUESTRA ----------------
    @ApiMethod(
            description = "Toma la muestra ingresada.",
            path = "/api/sampletrackings/take/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/take/order/{order}/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> sampleTake(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") String codeSample
    ) throws Exception
    {
        return new ResponseEntity<>(service.sampleTracking(idOrder, codeSample, LISEnum.ResultSampleState.COLLECTED.getValue(), null, false, true), HttpStatus.OK);
    }

    //------------ VERIFICAR MUESTRA ----------------
    @ApiMethod(
            description = "Verifica la muestra ingresada.",
            path = "/api/sampletrackings/verify/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/verify/order/{order}/sample/{sample}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> sampleVerify(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") String codeSample
    ) throws Exception
    {
        return new ResponseEntity<>(service.sampleTracking(idOrder, codeSample, LISEnum.ResultSampleState.CHECKED.getValue(), null, false, true), HttpStatus.OK);
    }

    //------------ VERIFICAR LOS EXAMENES DE UNA ORDEN QUE PERTENECE A UN SERVICIO HOSPITALARIO QUE ESTAN E ESTADO RETOMA PARA QUE VUELVAN A QUEDAR VERIFICADOS  ----------------
    @ApiMethod(
            description = "Verificar los examenes de una orden que pertenece a un servicio hospitalario que estan en estado de retoma para que vuelvan a queddar verificados",
            path = "/api/sampletrackings/updateCheckRetake/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/updateCheckRetake/order/{order}/sample/{sample}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateCheckRetake(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") String codeSample
    ) throws Exception
    {
        return new ResponseEntity<>(service.sampleCheckRetakeTracking(idOrder, codeSample), HttpStatus.OK);
    }

    //------------ RECHAZAR MUESTRA ----------------
    @ApiMethod(
            description = "Rechaza la muestra ingresada.",
            path = "/api/sampletrackings/reject/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/reject/order/{order}/sample/{sample}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> sampleReject(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") String codeSample,
            @ApiBodyObject(clazz = Reason.class) @RequestBody Reason reason
    ) throws Exception
    {
        return new ResponseEntity<>(service.sampleTracking(idOrder, codeSample, LISEnum.ResultSampleState.REJECTED.getValue(), reason, false, true), HttpStatus.OK);
    }

    //------------ RETOMA DE LA MUESTRA ----------------
    @ApiMethod(
            description = "Retoma de la muestra ingresada.",
            path = "/api/sampletrackings/retake/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/retake/order/{order}/sample/{sample}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> sampleRetake(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") String codeSample,
            @ApiBodyObject(clazz = Reason.class) @RequestBody Reason reason
    ) throws Exception
    {
        return new ResponseEntity<>(service.sampleTracking(idOrder, codeSample, LISEnum.ResultSampleState.NEW_SAMPLE.getValue(), reason, true, true), HttpStatus.OK);
    }

    //------------ VERIFICAR MUESTRA - DESTINO ----------------
    @ApiMethod(
            description = "Verifica la muestra con destinos ingresada.",
            path = "/api/sampletrackings/verify/destination",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/verify/destination", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> sampleVerifyDestination(
            @ApiBodyObject(clazz = VerifyDestination.class) @RequestBody VerifyDestination verify
    ) throws Exception
    {
        return new ResponseEntity<>(service.verifyDestination(verify), HttpStatus.OK);
    }

    //------------ CONSULTAR RUTA DE LA ORDEN ----------------
    @ApiMethod(
            description = "Obtiene la ruta que se debe asociar a la orden ingresada.",
            path = "/api/sampletrackings/verify/destination/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AssignmentDestination.class)
    @RequestMapping(value = "/verify/destination/order/{order}/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssignmentDestination> getAssigment(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") String codeSample
    ) throws Exception
    {
        AssignmentDestination assigment = service.getDestinationRoute(idOrder, codeSample);
        if (assigment == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(assigment, HttpStatus.OK);
        }
    }

    //------------ ENTREVISTA - CONSULTA ----------------
    @ApiMethod(
            description = "Obtiene las preguntas que se le deben aplicar a una orden.",
            path = "/api/sampletrackings/interview/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/interview/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Question>> getInterview(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder
    ) throws Exception
    {
        List<Question> list = service.getInterview(idOrder);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ ENTREVISTA - RESPUESTAS - CONSULTA ----------------
    @ApiMethod(
            description = "Obtiene las preguntas que se le deben aplicar a una orden.",
            path = "/api/sampletrackings/interview/answer/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/interview/answer/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> getInterviewAnswer(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder
    ) throws Exception
    {
        return new ResponseEntity<>(service.getInterviewAnswer(idOrder), HttpStatus.OK);
    }

    //------------ ENTREVISTA - INSERCION ----------------
    @ApiMethod(
            description = "Verifica la muestra con destinos ingresada.",
            path = "/api/sampletrackings/interview/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Question.class)
    @RequestMapping(value = "/interview/order/{order}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertResultInterview(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiBodyObject(clazz = Question.class) @RequestBody List<Question> questions
    ) throws Exception
    {
        return new ResponseEntity<>(service.insertResultInterview(questions, idOrder), HttpStatus.OK);
    }

    //------------ OBTENER LISTA DE MUESTRA POR ORDEN ----------------
    @ApiMethod(
            description = "Obtiene las muestras de una orden.",
            path = "/api/sampletrackings/samples/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(value = "/samples/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sample>> getSample(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder
    ) throws Exception
    {
        List<Sample> list = service.sampleOrderTracking(idOrder);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ MUESTRAS RETRASADAS ----------------
    @ApiMethod(
            description = "Obtiene las muestras que estan retrasadas para un destino.",
            path = "/api/sampletrackings/delayed/destination/{destination}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SampleDelayed.class)
    @RequestMapping(value = "/delayed/destination/{destination}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SampleDelayed>> getSamplesDelayed(
            @ApiPathParam(name = "destination", description = "Destino") @PathVariable(name = "destination") int idDestination
    ) throws Exception
    {
        List<SampleDelayed> list = service.sampleDelayed(idDestination);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CONSULTAR ORDEN ----------------
    @ApiMethod(
            description = "Valida si una orden pertenece a una orden de consulta externa y si posee entrevista aplicada.",
            path = "/api/sampletrackings/alarminterview/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/alarminterview/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrackingAlarm> trackingAlarmInterview(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder
    ) throws Exception
    {
        TrackingAlarm alarm = service.trackingAlarmInterview(idOrder);
        if (alarm == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(alarm, HttpStatus.OK);
        }
    }

    //------------ TOMAR MUESTRA DESDE EL SKL ----------------
    @ApiMethod(
            description = "Toma la muestra ingresada desde el SKL",
            path = "/api/sampletrackings/sampletaketest",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestSampleTake.class)
    @RequestMapping(value = "/sampletaketest", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestSampleTake> sampleTaketest(
            @ApiBodyObject(clazz = TestSampleTake.class) @RequestBody TestSampleTake testSampleTake
    ) throws Exception
    {
        return new ResponseEntity<>(service.sampleTrackingTest(testSampleTake), HttpStatus.OK);
    }

    //------------ LISTAR MUESTRA DESDE EL SKL ----------------
    @ApiMethod(
            description = "Toma la muestra ingresada desde el SKL",
            path = "/api/sampletrackings/listsampletakebyorder/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestSampleTakeTracking.class)
    @RequestMapping(value = "/listsampletakebyorder/order/{order}/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestSampleTakeTracking>> listSampleTakeByOrder(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") String codeSample
    ) throws Exception
    {
        List<TestSampleTakeTracking> testSampleTake = service.listSampleTakeByOrder(idOrder, codeSample);
        if (testSampleTake == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(testSampleTake, HttpStatus.OK);
        }

    }

    //------------ ACTUALIZA EL ESTADO DE UNA MUESTRA A VERIFICADO  ----------------
    @ApiMethod(
            description = "Actualiza el estado de una muestra a verificado",
            path = "/api/sampletrackings/updateStateToCheck",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/updateStateToCheck", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateStateToCheck(
            @ApiBodyObject(clazz = RequestSampleDestination.class) @RequestBody RequestSampleDestination requestSampleDestination
    ) throws Exception
    {
        service.updateStateToCheck(requestSampleDestination);
    }

    //------------ VERIIFICA SI EL NUMERO DE ORDEN EXISTE ----------------
    @ApiMethod(
            description = "Verifica la existencia de una orden con solo el id de la orden",
            path = "/api/sampletrackings/orderExists/idOrder/{idOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/orderExists/idOrder/{idOrder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> thisOrderExists(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder
    ) throws Exception
    {
        try
        {
            boolean exist = service.orderExists(idOrder);
            if (exist)
            {
                return new ResponseEntity<>(exist, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(exist, HttpStatus.OK);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //------------ VERIFICA QUE UNA MUESTRA YA SE ENCUENTRA VERIFICADA ----------------
    @ApiMethod(
            description = "Verifica que una muestra ya se encuentra verificada",
            path = "/api/sampletrackings/isSampleCheck/idOrder/{idOrder}/idRoute/{idRoute}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/isSampleCheck/idOrder/{idOrder}/idRoute/{idRoute}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isSampleCheck(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder,
            @ApiPathParam(name = "idRoute", description = "Id de la ruta de destino") @PathVariable("idRoute") int idRoute
    ) throws Exception
    {
        try
        {
            boolean exist = service.isSampleCheck(idOrder, idRoute);
            if (exist)
            {
                return new ResponseEntity<>(exist, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(exist, HttpStatus.OK);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //------------ VERIFICA SI UNA MUESTRA YA SE ENCUENTRA VERIFICADA PARA VERIFICACION SENCILLA ----------------
    @ApiMethod(
            description = "Verifica si una muestra ya se encuentra verificada para verifiación sencilla",
            path = "/api/sampletrackings/isSampleCheckSimple/idOrder/{idOrder}/sampleId/{sampleId}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/isSampleCheckSimple/idOrder/{idOrder}/sampleId/{sampleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isSampleCheckSimple(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") long idOrder,
            @ApiPathParam(name = "sampleId", description = "Id de la muestra") @PathVariable("sampleId") int sampleId
    ) throws Exception
    {
        try
        {
            boolean exist = service.isSampleCheckSimple(idOrder, sampleId);
            if (exist)
            {
                return new ResponseEntity<>(exist, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(exist, HttpStatus.OK);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //------------ ENTREVISTA - RESPUESTAS - CONSULTA ----------------
    @ApiMethod(
            description = "Obtiene las preguntas que se le deben aplicar a un destino.",
            path = "/api/sampletrackings/interview/answer/destination/{destination}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/interview/answer/destination/{destination}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Question>> getInterviewAnswerDestination(
            @ApiPathParam(name = "destination", description = "Id del detino") @PathVariable(name = "destination") int destination
    ) throws Exception
    {
        return new ResponseEntity<>(service.getInterviewAnswerDestination(destination), HttpStatus.OK);
    }

    //------------ ENTREVISTA - INSERCION ----------------
    @ApiMethod(
            description = "Verifica la muestra con destinos ingresada.",
            path = "/api/sampletrackings/interview/order/{order}/sample{sample}/destination/{destination}/branch/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Question.class)
    @RequestMapping(value = "/interview/order/{order}/sample{sample}/destination/{destination}/branch/{branch}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertResultInterviewDestination(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") int idSample,
            @ApiPathParam(name = "destination", description = "Destino") @PathVariable(name = "destination") int idDestination,
            @ApiPathParam(name = "branch", description = "Sede") @PathVariable(name = "branch") int idBranch,
            @ApiBodyObject(clazz = Question.class) @RequestBody List<Question> questions
    ) throws Exception
    {
        return new ResponseEntity<>(service.insertResultInterviewDestination(questions, idOrder, idSample, idDestination, idBranch), HttpStatus.OK);
    }

    //------------ ENTREVISTA - CONSULTA CON SEDE Y DESTINO ----------------
    @ApiMethod(
            description = "Obtiene las preguntas que se le deben aplicar a una orden con sede y destino",
            path = "/api/sampletrackings/interview/order/{order}/sample{sample}/destination/{destination}/branch/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/interview/order/{order}/sample{sample}/destination/{destination}/branch/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Question>> getInterviewDestination(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") int idSample,
            @ApiPathParam(name = "destination", description = "Destino") @PathVariable(name = "destination") int idDestination,
            @ApiPathParam(name = "branch", description = "Sede") @PathVariable(name = "branch") int idBranch
    ) throws Exception
    {
        List<Question> list = service.getInterviewDestination(idOrder, idSample, idDestination, idBranch);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ VERIFICAR MUESTRA CON TEMPERATURA ----------------
    @ApiMethod(
            description = "Verifica la muestra ingresada con temperatura",
            path = "/api/sampletrackings/verify/order/{order}/sample/{sample}/temperature/{temperature}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/verify/order/{order}/sample/{sample}/temperature/{temperature}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> sampleVerifyTemperature(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") String codeSample,
            @ApiPathParam(name = "temperature", description = "Temperatura") @PathVariable(name = "temperature") String temperature
    ) throws Exception
    {
        return new ResponseEntity<>(service.sampleTrackingTemperature(idOrder, codeSample, LISEnum.ResultSampleState.CHECKED.getValue(), temperature), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene una lista de ordenes por un rango de fechas, filtra por el servicio y la muestra",
            path = "/api/sampletrackings/getOrdersBySampleAndFilter/initialDate/{initialDate}/endDate/{endDate}/idSample/{idSample}/idService/{idService}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getOrdersBySampleAndFilter/initialDate/{initialDate}/endDate/{endDate}/idSample/{idSample}/idService/{idService}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getSamplesByTemperatureAndDate(
            @ApiPathParam(name = "initialDate", description = "Fecha inicial") @PathVariable(name = "initialDate") long initialDate,
            @ApiPathParam(name = "endDate", description = "Fecha final") @PathVariable(name = "endDate") long endDate,
            @ApiPathParam(name = "idSample", description = "Id de la muestra") @PathVariable(name = "idSample") int idSample,
            @ApiPathParam(name = "idService", description = "Id del servicio de la orden") @PathVariable(name = "idService") int idService
    ) throws Exception
    {
        List<Order> list = service.getSamplesByTemperatureAndDate(initialDate, endDate, idSample, idService);
        if (!list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
        }
    }

    //CONSULTAR INFORMACION DE UN RESULTADO
    @ApiMethod(
            description = "Obtiene la auditoria de la muestra de una orden",
            path = "/api/sampletrackings/information/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuditOperation.class)
    @RequestMapping(value = "/information/order/{order}/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditOperation>> getInformation(
            @ApiPathParam(name = "order", description = "Identificador de la orden") @PathVariable("order") Long order,
            @ApiPathParam(name = "sample", description = "Identificador de la muestra") @PathVariable("sample") Integer sample
    ) throws Exception
    {
        List<AuditOperation> samples = service.getTrackingSample(order, sample);
        if (samples == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(samples, HttpStatus.OK);
        }
    }
}

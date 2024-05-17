package net.cltech.enterprisent.controllers.operation.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.test.GeneralTemplateOption;
import net.cltech.enterprisent.domain.masters.test.OptionTemplate;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.operation.audit.AuditEvent;
import net.cltech.enterprisent.domain.operation.filters.MicrobiologyFilter;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobialDetection;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyGrowth;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyTask;
import net.cltech.enterprisent.domain.operation.microbiology.ResultMicrobiology;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.widgets.StateCount;
import net.cltech.enterprisent.service.interfaces.operation.microbiology.MicrobiologyService;
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
 * Controlador de servicios Rest sobre estadisticas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 19/01/2018
 * @see Creacion
 */
@Api(
        name = "Microbiologia",
        group = "Operación - Microbiologia",
        description = "Servicios Rest sobre estadisticas"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/microbiology")
public class MicrobiologyController
{

    @Autowired
    private MicrobiologyService microbiologyService;

    //------------ ORDEN DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Obtiene la información de una orden por muestra para la verificación y siembra en microbiologia.",
            path = "/api/microbiology/growth/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobiologyGrowth.class)
    @RequestMapping(value = "/growth/order/{order}/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MicrobiologyGrowth> getMicrobiologyOrderSample(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") String codeSample
    ) throws Exception
    {
        MicrobiologyGrowth microbiologyGrowth = microbiologyService.getOrderMicrobiology(idOrder, codeSample);
        if (microbiologyGrowth == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(microbiologyGrowth, HttpStatus.OK);
        }
    }

    //------------ ANTIBIOGRAMA DEL EXAMEN - MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Obtiene la información de una orden por muestra para la verificación y siembra en microbiologia.",
            path = "/api/microbiology/antibiogram/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobiologyGrowth.class)
    @RequestMapping(value = "/antibiogram/order/{order}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MicrobiologyGrowth> getAntibiogramOrderSample(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "test", description = "Examen") @PathVariable(name = "test") int idTest
    ) throws Exception
    {
        MicrobiologyGrowth microbiologyGrowth = microbiologyService.getOrderMicrobiology(idOrder, idTest);
        if (microbiologyGrowth == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(microbiologyGrowth, HttpStatus.OK);
        }
    }

    //------------ Lista de examanes asociados a un medio de cultivo por prueba ----------------
    @ApiMethod(
            description = "Lista de examanes asociados a un medio de cultivo por prueba.",
            path = "/api/microbiology/tests/sample/{sample}/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobiologyGrowth.class)
    @RequestMapping(value = "/tests/sample/{sample}/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestBasic>> getTestsMicrobiologySample(
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") int idSample,
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order
    ) throws Exception
    {
        List<TestBasic> tests = microbiologyService.getTestsMicrobiologySample(idSample, order);
        if (tests.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(tests, HttpStatus.OK);
        }
    }

    //------------ TRAZABILIDAD DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Obtiene la trazabilidad de una orden por muestra para la verificación y siembra en microbiologia.",
            path = "/api/microbiology/tracking/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobiologyGrowth.class)
    @RequestMapping(value = "/tracking/order/{order}/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditEvent>> listTrackingMicrobiology(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") String codeSample
    ) throws Exception
    {
        List<AuditEvent> list = microbiologyService.listTrackingMicrobiology(idOrder, codeSample);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ SIEMBRA DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Realiza la siembra de microbiologia.",
            path = "/api/microbiology/growth",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobiologyGrowth.class)
    @RequestMapping(value = "/growth", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> growth(
            @ApiBodyObject(clazz = MicrobiologyGrowth.class) @RequestBody MicrobiologyGrowth growth
    ) throws Exception
    {
        return new ResponseEntity<>(microbiologyService.updateGrowthMicrobiology(growth), HttpStatus.OK);
    }

    //------------ VERIFICACIÓN DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Realiza la verificación de microbiologia.",
            path = "/api/microbiology/growth",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobiologyGrowth.class)
    @RequestMapping(value = "/growth", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> verifyMicrobiology(
            @ApiBodyObject(clazz = MicrobiologyGrowth.class) @RequestBody MicrobiologyGrowth microbiologyGrowth
    ) throws Exception
    {
        return new ResponseEntity<>(microbiologyService.verifyMicrobiology(microbiologyGrowth), HttpStatus.OK);
    }

    //------------ INSERTA DATOS DEL ANTIBIOGRAMA ----------------
    @ApiMethod(
            description = "Realiza la inserción de datos correspondientes con el antibiograma asociado a una orden y examen.",
            path = "/api/microbiology/antibiogram",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobiologyGrowth.class)
    @RequestMapping(value = "/antibiogram", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> insertAntibiogramTest(
            @ApiBodyObject(clazz = MicrobiologyGrowth.class) @RequestBody MicrobiologyGrowth microbiologyGrowth
    ) throws Exception
    {
        return new ResponseEntity<>(microbiologyService.insertAntibiogramTest(microbiologyGrowth), HttpStatus.OK);
    }

    //------------ DETECCIÓN MICROBIANA ----------------
    @ApiMethod(
            description = "Realiza la detección microbiana.",
            path = "/api/microbiology/microbialdetection",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/microbialdetection", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> microbialDetection(
            @ApiBodyObject(clazz = MicrobialDetection.class) @RequestBody MicrobialDetection microbialDetection
    ) throws Exception
    {
        return new ResponseEntity<>(microbiologyService.insertMicrobialDetection(microbialDetection), HttpStatus.OK);
    }

    //------------ CONSULTA DETECCIÓN MICROBIANA ----------------
    @ApiMethod(
            description = "Obtiene la información de una orden por muestra para realizar la detección microbiana.",
            path = "/api/microbiology/microbialdetection/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobialDetection.class)
    @RequestMapping(value = "/microbialdetection/order/{order}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MicrobialDetection> getMicrobialDetection(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "test", description = "Examen") @PathVariable(name = "test") int idTest
    ) throws Exception
    {
        MicrobialDetection microbialDetection = microbiologyService.getMicrobialDetection(idOrder, idTest);
        if (microbialDetection == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(microbialDetection, HttpStatus.OK);
        }
    }

    //------------ DETECCIÓN MICROBIANA - REGISTRO RESULTADOS ----------------
    @ApiMethod(
            description = "Obtiene los resultados de examenes asociados a microbiologia con filtros especificos.",
            path = "/api/microbiology/result",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobialDetection.class)
    @RequestMapping(value = "/result", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResultTest>> getResultTestMicrobiology(
            @ApiBodyObject(clazz = MicrobiologyFilter.class) @RequestBody MicrobiologyFilter microbiologyFilter
    ) throws Exception
    {
        List<ResultTest> results = microbiologyService.listResultTestMicrobiology(microbiologyFilter);
        if (results.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(results, HttpStatus.OK);
        }
    }

    //-------- DETECCIÓN MICROBIANA - REGISTRO RESULTADOS - ORDENES -----------
    @ApiMethod(
            description = "Obtiene las ordenes de los resultados de examenes asociados a microbiologia con filtros especificos.",
            path = "/api/microbiology/result/order",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobialDetection.class)
    @RequestMapping(value = "/result/order", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getResultOrderMicrobiology(
            @ApiBodyObject(clazz = MicrobiologyFilter.class) @RequestBody MicrobiologyFilter microbiologyFilter
    ) throws Exception
    {
        List<Order> orders = microbiologyService.listResultOrderMicrobiology(microbiologyFilter);
        if (orders.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
    }

    //------------ DETECCIÓN MICROBIANA - ANTIBIOTICOS ----------------
    @ApiMethod(
            description = "Obtiene los resultados asociados a microbiologia con filtros especificos.",
            path = "/api/microbiology/antiobiotics/microbialdetection/{microbialdetection}/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobialDetection.class)
    @RequestMapping(value = "/antiobiotics/microbialdetection/{microbialdetection}/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResultMicrobiology>> getResultMicrobiologySensitivity(
            @ApiPathParam(name = "microbialdetection", description = "Detección Microbial") @PathVariable(name = "microbialdetection") int idMicrobialDetection,
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") Long order
    ) throws Exception
    {
        List<ResultMicrobiology> results = microbiologyService.listResultMicrobiologySensitivity(idMicrobialDetection, order);
        if (results.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(results, HttpStatus.OK);
        }
    }

    //------------ DETECCIÓN MICROBIANA - ANTIBIOTICOS ----------------
    @ApiMethod(
            description = "Asocia los antibioticos a la detección microbiana.",
            path = "/api/microbiology/antiobiotics/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/antiobiotics/order/{order}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertResultMicrobiologySensitivity(
            @ApiBodyObject(clazz = Microorganism.class) @RequestBody Microorganism microorganism,
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") Long order
    ) throws Exception
    {
        return new ResponseEntity<>(microbiologyService.insertResultMicrobiologySensitivity(microorganism, order), HttpStatus.OK);
    }

    //------------ INGRESAR TAREAS DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Inserta tareas de microbiologia.",
            path = "/api/microbiology/mediaculture/tasks",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/mediaculture/tasks", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MicrobiologyTask> insertMediaCultureTask(
            @ApiBodyObject(clazz = MicrobiologyTask.class) @RequestBody MicrobiologyTask microbiologyTask
    ) throws Exception
    {
        microbiologyTask.setType((short) 1);
        return new ResponseEntity<>(microbiologyService.insertTask(microbiologyTask), HttpStatus.OK);
    }

    //------------ INGRESAR TAREAS DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Inserta tareas de microbiologia.",
            path = "/api/microbiology/procedure/tasks",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/procedure/tasks", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MicrobiologyTask> insertProcedureTask(
            @ApiBodyObject(clazz = MicrobiologyTask.class) @RequestBody MicrobiologyTask microbiologyTask
    ) throws Exception
    {
        microbiologyTask.setType((short) 2);
        return new ResponseEntity<>(microbiologyService.insertTask(microbiologyTask), HttpStatus.OK);
    }

    //------------ ORDEN DE MICROBIOLOGIA - TAREAS ----------------
    @ApiMethod(
            description = "Obtiene la información de una orden por muestra para la verificación y siembra en microbiologia.",
            path = "/api/microbiology/tasks/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobiologyGrowth.class)
    @RequestMapping(value = "/tasks/order/{order}/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MicrobiologyGrowth> getMicrobiologyOrderSampleTask(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") String codeSample
    ) throws Exception
    {
        MicrobiologyGrowth microbiologyGrowth = microbiologyService.getOrderMicrobiologyTask(idOrder, codeSample);
        if (microbiologyGrowth == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(microbiologyGrowth, HttpStatus.OK);
        }
    }

    //------------ ACTUALIZAR COMENTARIO DE TAREAS DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Actualizar tareas de microbiologia.",
            path = "/api/microbiology/tasks",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/tasks", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MicrobiologyTask> updateProcedureTask(
            @ApiBodyObject(clazz = MicrobiologyTask.class) @RequestBody MicrobiologyTask microbiologyTask
    ) throws Exception
    {
        return new ResponseEntity<>(microbiologyService.updateTask(microbiologyTask), HttpStatus.OK);
    }

    //------------ TRAZABILIDAD DE TAREAS DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Obtiene la trazabilidad de las tareas de microbiologia.",
            path = "/api/microbiology/tracking/tasks/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobiologyGrowth.class)
    @RequestMapping(value = "/tracking/tasks/order/{order}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MicrobiologyTask>> listTrackingMicrobiologyTasks(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order,
            @ApiPathParam(name = "test", description = "Examen") @PathVariable(name = "test") int test
    ) throws Exception
    {
        List<MicrobiologyTask> list = microbiologyService.listTrackingMicrobiologyTask(order, test);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ TRAZABILIDAD DE COMENTARIO DE TAREAS DE MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Obtiene la trazabilidad de los comentarios de las tareas de microbiologia.",
            path = "/api/microbiology/tracking/tasks/comment/id/{id}/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobiologyGrowth.class)
    @RequestMapping(value = "/tracking/tasks/comment/id/{id}/order/{order}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditEvent>> listTrackingMicrobiologyCommentTasks(
            @ApiPathParam(name = "id", description = "Id asociado de las tareas de microbiologia de una orden, examen, medio de cultivo o procedimiento y destino.") @PathVariable(name = "id") int id,
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order,
            @ApiPathParam(name = "test", description = "Examen") @PathVariable(name = "test") int test
    ) throws Exception
    {
        List<AuditEvent> list = microbiologyService.listTrackingMicrobiologyCommentTask(order, test, id);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ REPORTAR TAREAS DE MICROBIOLOGIA  ----------------
    @ApiMethod(
            description = "Reporta tareas de microbiologia.",
            path = "/api/microbiology/report/tasks",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobiologyGrowth.class)
    @RequestMapping(value = "/report/tasks", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MicrobiologyTask>> listTrackingMicrobiologyTasksReport(
            @ApiBodyObject(clazz = MicrobiologyFilter.class) @RequestBody MicrobiologyFilter microbiologyFilter
    ) throws Exception
    {
        List<MicrobiologyTask> list = microbiologyService.listTrackingMicrobiologyTaskReport(microbiologyFilter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ REINICIAR TAREAS DE MICROBIOLOGIA  ----------------
    @ApiMethod(
            description = "Reiniar tareas de microbiologia.",
            path = "/api/microbiology/restart/tasks",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobiologyGrowth.class)
    @RequestMapping(value = "/restart/tasks", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> listTrackingMicrobiologyTasksRestart(
            @ApiBodyObject(clazz = MicrobiologyFilter.class) @RequestBody MicrobiologyFilter microbiologyFilter
    ) throws Exception
    {
        return new ResponseEntity<>(microbiologyService.listTrackingMicrobiologyTaskRestart(microbiologyFilter), HttpStatus.OK);
    }

    //-------- LISTA PENDIENTES DE VERIFICACION MICROBIOLOGIA -----------
    @ApiMethod(
            description = "Lista pendientes de verificacón en microbiología.",
            path = "/api/microbiology/verification/pending",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobialDetection.class)
    @RequestMapping(value = "/verification/pending", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderList>> getVerificationMicrobiologyPending(
            @ApiBodyObject(clazz = MicrobiologyFilter.class) @RequestBody MicrobiologyFilter microbiologyFilter
    ) throws Exception
    {
        List<OrderList> orders = microbiologyService.listPendingMicrobiologyVerification(microbiologyFilter);
        if (orders.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
    }

    //------------ DETECCIÓN MICROBIANA - REGISTRO RESULTADOS ----------------
    @ApiMethod(
            description = "Obtiene conteo de estados pendientes de resultado, validación y pre-validacion.",
            path = "/api/microbiology/count/pendingstate",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicrobialDetection.class)
    @RequestMapping(value = "/count/pendingstate", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StateCount> getPendingStateCount(
            @ApiBodyObject(clazz = MicrobiologyFilter.class) @RequestBody MicrobiologyFilter microbiologyFilter
    ) throws Exception
    {
        StateCount count = microbiologyService.getPendingStateCount(microbiologyFilter);
        if (count == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(count, HttpStatus.OK);
        }
    }

    //---------------------PLANTILLA DE RESULTADOS------------------
    @ApiMethod(
            description = "Obtiene plantilla resultados de una orden y examen.",
            path = "/api/microbiology/templates/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = GeneralTemplateOption.class)
    @RequestMapping(value = "/templates/order/{order}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneralTemplateOption> listResultTemplate(
            @ApiPathParam(name = "order", description = "Numero de la Orden") @PathVariable(name = "order") Long order,
            @ApiPathParam(name = "test", description = "Id de examen") @PathVariable(name = "test") Integer idTest
    ) throws Exception
    {
        GeneralTemplateOption general = microbiologyService.getGeneralTemplate(order, idTest);
        if (general != null)
        {
            return new ResponseEntity<>(general, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-------------------PLANTILLA DE RESULTADOS----------------
    @ApiMethod(
            description = "Inserta la plantilla resultados de una orden y examen.",
            path = "/api/microbiology/templates/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OptionTemplate.class)
    @RequestMapping(value = "/templates/order/{order}/test/{test}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertResultTemplate(
            @ApiBodyObject(clazz = OptionTemplate.class) @RequestBody List<OptionTemplate> templates,
            @ApiPathParam(name = "order", description = "Numero de la Orden") @PathVariable(name = "order") Long order,
            @ApiPathParam(name = "test", description = "Id de examen") @PathVariable(name = "test") Integer idTest
    ) throws Exception
    {
        return new ResponseEntity<>(microbiologyService.insertResultTemplate(templates, order, idTest, null), HttpStatus.OK);
    }
}

package net.cltech.enterprisent.controllers.operation.results;

import java.util.List;
import net.cltech.enterprisent.domain.masters.interview.PanicInterview;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.service.interfaces.operation.results.CheckResultService;
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
 * @author eacuna
 * @since 10/10/2017
 * @see Creacion
 */
@Api(
        name = "Revision de Resultados",
        group = "Operación - Resultados",
        description = "Servicios Rest de revisión de resultados"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/checkresults")
public class CheckResultController
{

    @Autowired
    private CheckResultService service;

    //------------ Generacion de hoja de trabajo ----------------
    @ApiMethod(
            description = "Lista resultados se examenes permitiendo filtrar por pendientes de estados.",
            path = "/api/checkresults/pending",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/pending", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderList>> listPending(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<OrderList> result = service.listPending(filter);
        if (result.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Lista las ordenes y examenes con resultados .",
            path = "/api/checkresults",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderList.class)
    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderList>> list(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<OrderList> result = service.list(filter);
        if (result.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Lista resultados de un examen de un paciente",
            path = "/api/checkresults/results/record/{record}/document/{document}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultTest.class)
    @RequestMapping(value = "/results/record/{record}/document/{document}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResultTest>> listByRecord(
            @ApiPathParam(name = "record", description = "Historia del paciente") @PathVariable("record") String record,
            @ApiPathParam(name = "document", description = "Tipo de documento (Opcional)") @PathVariable("document") int document,
            @ApiPathParam(name = "test", description = "Examen") @PathVariable("test") int test
    ) throws Exception
    {
        List<ResultTest> results = service.listByRecord(record, document, test);
        if (!results.isEmpty())
        {
            return new ResponseEntity<>(results, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    //------------ Obtiene las entrevistas de panico ----------------
    @ApiMethod(
            description = "Lista entrevista de panico",
            path = "/api/checkresults/panicinterview",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PanicInterview.class)
    @RequestMapping(value = "/panicinterview", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PanicInterview>> getPanicInterview(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<PanicInterview> result = service.getPanicInterview(filter);
        if (result == null || result.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }
    
    //------------ Obtiene los resultados utilizados para los valores criticos ----------------
    @ApiMethod(
            description = "Lista de valores criticos",
            path = "/api/checkresults/criticalvalues",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultTest.class)
    @RequestMapping(value = "/criticalvalues", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResultTest>> getCriticalValues(
            @ApiBodyObject(clazz = Filter.class) @RequestBody Filter filter
    ) throws Exception
    {
        List<ResultTest> result = service.getCriticalValues(filter);
        if (result.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }
}

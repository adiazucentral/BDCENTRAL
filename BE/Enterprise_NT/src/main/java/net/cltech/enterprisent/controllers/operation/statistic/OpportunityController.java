package net.cltech.enterprisent.controllers.operation.statistic;

import java.util.List;
import net.cltech.enterprisent.domain.operation.filters.StatisticFilter;
import net.cltech.enterprisent.domain.operation.statistic.Histogram;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOrder;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOrderAverageTimes;
import net.cltech.enterprisent.service.interfaces.operation.statistics.OpportunityService;
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
 * Controlador de servicios Rest sobre estadisticas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 14/09/2017
 * @see Creacion
 */
@Api(
        name = "Indicadores",
        group = "Estadisticas",
        description = "Servicios Rest sobre indicadores"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/indicators")
public class OpportunityController
{

    @Autowired
    private OpportunityService service;

    //------------ LISTAR SEGUIMIENTO ----------------
    @ApiMethod(
            description = "Consulta de seguimiento, Lista los exámenes que no han sido validados y estan proximos a vencerse o ya se encuentran vencidos "
            + "con respecto a los tiempos pr servicio.",
            path = "/api/indicators/control",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StatisticOrder.class)
    @RequestMapping(value = "/control", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StatisticOrder>> listControl(
            @ApiBodyObject(clazz = StatisticFilter.class) @RequestBody StatisticFilter filter
    ) throws Exception
    {
        List<StatisticOrder> list = service.controlList(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR Indicadores ----------------
    @ApiMethod(
            description = "Consulta de indicadores, Lista los exámenes validados con sus tiempos de servicios",
            path = "/api/indicators",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StatisticOrder.class)
    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StatisticOrder>> listIndicators(
            @ApiBodyObject(clazz = StatisticFilter.class) @RequestBody StatisticFilter filter
    ) throws Exception
    {
        List<StatisticOrder> list = service.opportunityIndicators(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTA DE INDICADORES ----------------
    @ApiMethod(
            description = "Consulta tiempos de oportunidad, Lista todos los exámenes con sus tiempos registrados",
            path = "/api/indicators/opportunitytime",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StatisticOrder.class)
    @RequestMapping(value = "/opportunitytime", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StatisticOrder>> opportunityTime(
            @ApiBodyObject(clazz = StatisticFilter.class) @RequestBody StatisticFilter filter
    ) throws Exception
    {
        List<StatisticOrder> list = service.opportunityTimes(filter, false);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR PARA TIEMPOS PROMEDIO ----------------
    @ApiMethod(
            description = "Consulta tiempos para tiempos promedio de indicadores y alerta temprana.",
            path = "/api/indicators/averagetime",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StatisticOrderAverageTimes.class)
    @RequestMapping(value = "/averagetime", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatisticOrderAverageTimes> averageTime(
            @ApiBodyObject(clazz = StatisticFilter.class) @RequestBody StatisticFilter filter
    ) throws Exception
    {
        StatisticOrderAverageTimes list = service.averageTimes(filter);
        if (list == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR Indicadores ----------------
    @ApiMethod(
            description = "Consulta de indicadores, Lista los exámenes validados con sus tiempos de servicios",
            path = "/api/indicators/histogram",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Histogram.class)
    @RequestMapping(value = "/histogram", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Histogram> histogram(
            @ApiBodyObject(clazz = StatisticFilter.class) @RequestBody StatisticFilter filter
    ) throws Exception
    {
        Histogram histogram = service.histogram(filter);
        if (histogram == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(histogram, HttpStatus.OK);
        }
    }

}

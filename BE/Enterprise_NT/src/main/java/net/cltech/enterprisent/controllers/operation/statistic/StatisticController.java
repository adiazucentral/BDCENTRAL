package net.cltech.enterprisent.controllers.operation.statistic;

import java.util.List;
import net.cltech.enterprisent.domain.operation.filters.StatisticFilter;
import net.cltech.enterprisent.domain.operation.microbiology.WhonetPlain;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.statistic.SampleByDestinationHeader;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOrder;
import net.cltech.enterprisent.service.interfaces.operation.statistics.StatisticService;
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
        name = "Estadisticas",
        group = "Estadisticas",
        description = "Servicios Rest sobre estadisticas"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/statistics")
public class StatisticController
{

    @Autowired
    private StatisticService statisticService;

    //------------ LISTAR ESTADISTICAS FILTRO ESPECIFICO ----------------
    @ApiMethod(
            description = "Lista las estadisticas de ordenes registradas por un filtro especifico.",
            path = "/api/statistics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StatisticOrder.class)
    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StatisticOrder>> listFilter(
            @ApiBodyObject(clazz = StatisticFilter.class) @RequestBody StatisticFilter filter
    ) throws Exception
    {
        List<StatisticOrder> list = statisticService.listFilters(filter, false, false, false, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {                                                                     
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR ESTADISTICAS ESPECIALES ----------------
    @ApiMethod(
            description = "Lista estadisticas especiales por un filtro especifico.",
            path = "/api/statistics/special",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/special", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> listFilterStatisticsSpecial(
            @ApiBodyObject(clazz = StatisticFilter.class) @RequestBody StatisticFilter filter
    ) throws Exception
    {
        List<Order> list = statisticService.listFiltersStatisticsSpecial(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR ESTADISTICAS - MICROBIOLOGIA ----------------
    @ApiMethod(
            description = "Lista estadisticas de microbiologia.",
            path = "/api/statistics/microbiology",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StatisticOrder.class)
    @RequestMapping(value = "/microbiology", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StatisticOrder>> listFilterMicrobiology(
            @ApiBodyObject(clazz = StatisticFilter.class) @RequestBody StatisticFilter filter
    ) throws Exception
    {
        List<StatisticOrder> list = statisticService.listFiltersStatisticsMicrobiology(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR ESTADISTICAS - MOTIVO DE RECHAZO ----------------
    @ApiMethod(
            description = "Lista estadisticas de motivos de rechazo.",
            path = "/api/statistics/motivereject",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StatisticOrder.class)
    @RequestMapping(value = "/motivereject", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StatisticOrder>> listFilterMotiveSampleReject(
            @ApiBodyObject(clazz = StatisticFilter.class) @RequestBody StatisticFilter filter
    ) throws Exception
    {
        List<StatisticOrder> list = statisticService.listFiltersSampleReject(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR ESTADISTICAS CON PRECIO CON FILTRO ESPECIFICO ----------------
    @ApiMethod(
            description = "Lista las estadisticas con precio registradas por un filtro especifico.",
            path = "/api/statistics/price",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StatisticOrder.class)
    @RequestMapping(value = "/price", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StatisticOrder>> listFilterPrice(
            @ApiBodyObject(clazz = StatisticFilter.class) @RequestBody StatisticFilter filter
    ) throws Exception
    {
        List<StatisticOrder> list = statisticService.listFilters(filter, true, true, false, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    //------------ LISTAR ESTADISTICAS CON PRECIO CON FILTRO ESPECIFICO, con y sin caja ----------------
    @ApiMethod(
            description = "Lista las estadisticas con precio registradas por un filtro especifico, independientemente si tiene caja.",
            path = "/api/statistics/priceBox",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StatisticOrder.class)
    @RequestMapping(value = "/priceBox", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StatisticOrder>> listFilterPriceBox(
            @ApiBodyObject(clazz = StatisticFilter.class) @RequestBody StatisticFilter filter
    ) throws Exception
    {
        List<StatisticOrder> list = statisticService.listFiltersBox(filter, true, true, false, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR ESTADISTICAS DE REPETICION ----------------
    @ApiMethod(
            description = "Estadistica exámenes con repetición.",
            path = "/api/statistics/repeated",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StatisticOrder.class)
    @RequestMapping(value = "/repeated", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StatisticOrder>> statisticRepeated(
            @ApiBodyObject(clazz = StatisticFilter.class) @RequestBody StatisticFilter filter
    ) throws Exception
    {
        List<StatisticOrder> list = statisticService.statisticRepeated(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTA MUESTRAS EN DESTINO ----------------
    @ApiMethod(
            description = "Información de muestras en destino",
            path = "/api/statistics/sample/destination",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StatisticOrder.class)
    @RequestMapping(value = "/sample/destination", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SampleByDestinationHeader> statisticSampleDestiny(
            @ApiBodyObject(clazz = StatisticFilter.class) @RequestBody StatisticFilter filter
    ) throws Exception
    {
        SampleByDestinationHeader header = statisticService.sampleByDestiny(filter);
        if (header == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(header, HttpStatus.OK);
        }
    }

    //------------ LISTAR ESTADISTICAS - MICROBIOLOGIA -WHONET ----------------
    @ApiMethod(
            description = "Lista estadisticas de microbiologia.",
            path = "/api/statistics/microbiology/whonet",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StatisticOrder.class)
    @RequestMapping(value = "/microbiology/whonet", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WhonetPlain>> listMicrobiologyWhonet(
            @ApiBodyObject(clazz = StatisticFilter.class) @RequestBody StatisticFilter filter
    ) throws Exception
    {
        List<WhonetPlain> list = statisticService.listMicrobiologyWhonet(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
}

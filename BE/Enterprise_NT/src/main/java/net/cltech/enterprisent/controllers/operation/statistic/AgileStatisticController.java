/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.operation.statistic;

import java.util.List;
import net.cltech.enterprisent.domain.operation.filters.AgileStatisticFilter;
import net.cltech.enterprisent.domain.operation.statistic.AgileStatisticTest;
import net.cltech.enterprisent.service.interfaces.operation.statistics.AgileStatisticService;
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
 * Controlador de servicios REST sobre estadisticas rapidas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 04/04/2018
 * @see Creacion
 */
@Api(
        name = "Estadisticas Rapidas",
        group = "Estadisticas Rapidas",
        description = "Servicios Rest sobre estadisticas"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/agilestatistics")
public class AgileStatisticController
{

    @Autowired
    private AgileStatisticService agileStatisticService;

    //------------ LISTA DE AÑOS - ESTADISTICAS RAPIDAS ----------------
    @ApiMethod(
            description = "Lista los años presentes en estadisticas rapidas.",
            path = "/api/agilestatistics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> listYearsAgileStatistic() throws Exception
    {
        List<String> list = agileStatisticService.getYearsAgileStatistic();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTA POR RANGO - ESTADISTICAS RAPIDAS: SEDE - PRUEBA ----------------
    @ApiMethod(
            description = "Lista los años presentes en estadisticas rapidas.",
            path = "/api/agilestatistics/test/date",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/test/date", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AgileStatisticTest>> listAgileStatisticDateTest(
            @ApiBodyObject(clazz = AgileStatisticFilter.class) @RequestBody AgileStatisticFilter filter
    ) throws Exception
    {
        List<AgileStatisticTest> list = agileStatisticService.listStatisticsTestDate(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTA POR RANGO - ESTADISTICAS RAPIDAS: SEDE - PRUEBA ----------------
    @ApiMethod(
            description = "Lista los años presentes en estadisticas rapidas.",
            path = "/api/agilestatistics/test/years",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/test/years", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AgileStatisticTest>> listAgileStatisticYearsTest(
            @ApiBodyObject(clazz = AgileStatisticFilter.class) @RequestBody AgileStatisticFilter filter
    ) throws Exception
    {
        List<AgileStatisticTest> list = agileStatisticService.listStatisticsTestYears(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    //------------ LISTA POR RANGO - ESTADISTICAS RAPIDAS ----------------

    @ApiMethod(
            description = "Lista los años presentes en estadisticas rapidas.",
            path = "/api/agilestatistics/area/date",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/area/date", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AgileStatisticTest>> listBranchAreaDate(@ApiBodyObject(clazz = AgileStatisticFilter.class) @RequestBody AgileStatisticFilter filter) throws Exception
    {
        List<AgileStatisticTest> list = agileStatisticService.listBranchAreaDate(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTA POR RANGO - ESTADISTICAS RAPIDAS ----------------
    @ApiMethod(
            description = "Lista los años presentes en estadisticas rapidas.",
            path = "/api/agilestatistics/area/years",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/area/years", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AgileStatisticTest>> listBranchAreaYears(
            @ApiBodyObject(clazz = AgileStatisticFilter.class) @RequestBody AgileStatisticFilter filter
    ) throws Exception
    {
        List<AgileStatisticTest> list = agileStatisticService.listBranchAreaYears(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTA POR RANGO - ESTADISTICAS RAPIDAS: SEDE ----------------
    @ApiMethod(
            description = "Lista los años presentes en estadisticas rapidas.",
            path = "/api/agilestatistics/branch/date",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/branch/date", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AgileStatisticTest>> listAgileStatisticDateBranch(
            @ApiBodyObject(clazz = AgileStatisticFilter.class) @RequestBody AgileStatisticFilter filter
    ) throws Exception
    {
        List<AgileStatisticTest> list = agileStatisticService.listStatisticsBranchDate(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTA POR RANGO - ESTADISTICAS RAPIDAS: SEDE ----------------
    @ApiMethod(
            description = "Lista los años presentes en estadisticas rapidas.",
            path = "/api/agilestatistics/branch/years",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/branch/years", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AgileStatisticTest>> listAgileStatisticYearsBranch(
            @ApiBodyObject(clazz = AgileStatisticFilter.class) @RequestBody AgileStatisticFilter filter
    ) throws Exception
    {
        List<AgileStatisticTest> list = agileStatisticService.listStatisticsBranchYears(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
}

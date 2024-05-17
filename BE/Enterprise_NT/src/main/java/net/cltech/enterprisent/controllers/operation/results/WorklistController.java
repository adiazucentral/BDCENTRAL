package net.cltech.enterprisent.controllers.operation.results;

import java.util.List;
import net.cltech.enterprisent.domain.operation.common.Common;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.results.worklist.WorklistFilter;
import net.cltech.enterprisent.domain.operation.results.worklist.WorklistResult;
import net.cltech.enterprisent.service.interfaces.operation.result.WorklistService;
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
        name = "Hojas de Trabajo",
        group = "Operación - Resultados",
        description = "Servicios Rest sobre hojas de trabajo"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/worklists")
public class WorklistController
{

    @Autowired
    private WorklistService service;

    //------------ Generacion de hoja de trabajo ----------------
    @ApiMethod(
            description = "Lista las ordenes registradas por un filtro especifico.",
            path = "/api/worklists/generate",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/generate", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorklistResult> list(
            @ApiBodyObject(clazz = WorklistFilter.class) @RequestBody WorklistFilter filter
    ) throws Exception
    {
        WorklistResult result = service.list(filter);
        if (result == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    //------------ LISTAR Ordenes de hoja de trabajo ----------------
    @ApiMethod(
            description = "Lista las ordenes registradas por un filtro especifico.",
            path = "/api/worklists/filter/group/{group}/worklist/{worklist}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/filter/group/{group}/worklist/{worklist}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorklistResult> previous(
            @ApiPathParam(name = "group", description = "Grupo de la hoja de trabajo generada") @PathVariable("group") int group,
            @ApiPathParam(name = "worklist", description = "Id hoja de trabajo") @PathVariable("worklist") int worklist
    ) throws Exception
    {
        WorklistResult result = service.previous(worklist, group);
        if (result == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    //------------ Reinicia secuencia de la hoja de trabajo ----------------
    @ApiMethod(
            description = "Reinicia secuencia de la hoja de trabajo.",
            path = "/api/worklists/{worklist}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/{worklist}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> delete(
            @ApiPathParam(name = "worklist", description = "Id hoja de trabajo") @PathVariable("worklist") int worklist
    ) throws Exception
    {
        return new ResponseEntity<>(service.reset(worklist), HttpStatus.OK);
    }

    //------------ Generacion de hoja de trabajo ----------------
    @ApiMethod(
            description = "Lista secuencias de una hoja de trabajo.",
            path = "/api/worklists/secuence/{worksheet}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Common.class)
    @RequestMapping(value = "/secuence/{worksheet}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Common>> listSecuence(
            @ApiPathParam(name = "worksheet", description = "Id de la hoja de trabajo") @PathVariable int worksheet
    ) throws Exception
    {
        List<Common> result = service.listSecuence(worksheet);
        if (result.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }
}

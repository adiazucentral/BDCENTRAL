/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.common;

import java.util.List;
import net.cltech.enterprisent.domain.common.Tracking;
import net.cltech.enterprisent.domain.operation.tracking.AuditFilter;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.annotation.ApiQueryParam;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Servicios para el acceso de auditoria
 *
 * @version 1.0.0
 * @author enavas
 * @since 28/04/2017
 * @see Creacion
 */
@Api(
        name = "Auditoria de maestros",
        group = "Auditoría",
        description = "Servicios sobre la Auditoria"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/trackings")
public class TrackingController
{

    @Autowired
    private TrackingService trackingService;

    //------------ CONSULTA POR FECHAS Y  MODULO----------------
    @ApiMethod(
            description = "Obtiene la auditoria por fechas y  modulo",
            path = "/api/trackings/filter/date/{initial}/{final}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Tracking.class)
    @RequestMapping(value = "/filter/date/{initial}/{final}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Tracking>> getByModule(
            @ApiPathParam(name = "initial", description = "Fecha inicial en formato yyyyMMdd") @PathVariable("initial") int initialDate,
            @ApiPathParam(name = "final", description = "Fecha final en formato yyyyMMdd") @PathVariable("final") int finalDate,
            @ApiQueryParam(name = "module", description = "Nombre del mudulo") @RequestParam("module") String module
    ) throws Exception
    {
        List<Tracking> records = trackingService.get(initialDate, finalDate, module, null);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //------------ CONSULTA POR FECHAS Y  USUARIO----------------
    @ApiMethod(
            description = "Obtiene la auditoria por fechas y usuario",
            path = "/api/trackings/filter/date/{initial}/{final}/user/{user}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Tracking.class)
    @RequestMapping(value = "/filter/date/{initial}/{final}/user/{user}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Tracking>> getByUser(
            @ApiPathParam(name = "initial", description = "Fecha inicial en formato yyyyMMdd") @PathVariable("initial") int initialDate,
            @ApiPathParam(name = "final", description = "Fecha final en formato yyyyMMdd") @PathVariable("final") int finalDate,
            @ApiPathParam(name = "user", description = "Id del usuario") @PathVariable("user") Integer user
    ) throws Exception
    {
        List<Tracking> records = trackingService.get(initialDate, finalDate, null, user);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //------------ CONSULTA POR FECHAS----------------
    @ApiMethod(
            description = "Obtiene la auditoria por fechas ",
            path = "/api/trackings/filter/dates/{initial}/{final}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Tracking.class)
    @RequestMapping(value = "/filter/dates/{initial}/{final}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Tracking>> getByDate(
            @ApiPathParam(name = "initial", description = "Fecha inicial en formato yyyyMMdd") @PathVariable("initial") int initialDate,
            @ApiPathParam(name = "final", description = "Fecha final en formato yyyyMMdd") @PathVariable("final") int finalDate
    ) throws Exception
    {
        List<Tracking> records = trackingService.get(initialDate, finalDate);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Obtiene la auditoria por modulo o por usuario",
            path = "/api/trackings/filter/module/user",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Tracking.class)
    @RequestMapping(value = "/filter/module/user", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Tracking>> filterByModuleOrUser(
            @ApiBodyObject(clazz = AuditFilter.class) @RequestBody AuditFilter filter
    ) throws Exception
    {
        List<Tracking> records = trackingService.getAuditFilter(filter);
        if (!records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}

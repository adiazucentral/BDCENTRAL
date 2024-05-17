package net.cltech.enterprisent.controllers.masters.tracking;

import java.util.List;
import net.cltech.enterprisent.domain.masters.tracking.Refrigerator;
import net.cltech.enterprisent.service.interfaces.masters.tracking.RefrigeratorService;
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
 * Servicios REST nevera
 *
 * @version 1.0.0
 * @author Eacuna
 * @since 06/06/2017
 * @see Creacion
 */
@Api(
        group = "Trazabilidad",
        name = "Nevera",
        description = "Servicios sobre neveras"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/refrigerators")
public class RefrigeratorController
{

    @Autowired
    private RefrigeratorService service;

    @ApiMethod(
            description = "Obtiene neveras",
            path = "/api/refrigerators",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Refrigerator.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Refrigerator>> get() throws Exception
    {
        List<Refrigerator> records = service.list();
        if (!records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene neveras por su id",
            path = "/api/refrigerators/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Refrigerator.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Refrigerator> filterById(
            @ApiPathParam(name = "id", description = "Id de neveras") @PathVariable(name = "id") String id
    ) throws Exception
    {
        Refrigerator record = service.filterById(Integer.parseInt(id));
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene neveras por su nombre",
            path = "/api/refrigerators/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Refrigerator.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Refrigerator> filterByName(
            @ApiPathParam(name = "name", description = "Nombre de neveras") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Refrigerator record = service.filterByName(name);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualiza nevera enviada",
            path = "/api/refrigerators",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Refrigerator.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Refrigerator> update(
            @ApiBodyObject(clazz = Refrigerator.class) @RequestBody(required = true) Refrigerator update
    ) throws Exception
    {
        return new ResponseEntity<>(service.update(update), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Crea nevera enviada",
            path = "/api/refrigerators",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Refrigerator.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Refrigerator> create(
            @ApiBodyObject(clazz = Refrigerator.class) @RequestBody Refrigerator create
    ) throws Exception
    {
        return new ResponseEntity<>(service.create(create), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene lista neveras por el estado",
            path = "/api/refrigerators/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Refrigerator.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Refrigerator>> filterByState(
            @ApiPathParam(name = "state", description = "Estado activo(true) o inactivo(false)") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Refrigerator> records = service.filterByState(state);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}

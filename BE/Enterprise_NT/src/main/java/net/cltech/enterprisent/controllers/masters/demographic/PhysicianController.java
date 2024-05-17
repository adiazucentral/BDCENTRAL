package net.cltech.enterprisent.controllers.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.service.interfaces.masters.demographic.PhysicianService;
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
 * Servicios REST médico
 *
 * @version 1.0.0
 * @author Eacuna
 * @since 24/05/2017
 * @see Creacion
 */
@Api(
        group = "Demografico",
        name = "Médico",
        description = "Servicios sobre médicos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/physicians")
public class PhysicianController
{

    @Autowired
    private PhysicianService service;

    @ApiMethod(
            description = "Obtiene lista médico",
            path = "/api/physicians",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Physician.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Physician>> get() throws Exception
    {
        List<Physician> records = service.list();
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(service.list(), HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene médico por su id",
            path = "/api/physicians/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Physician.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Physician> filterById(
            @ApiPathParam(name = "id", description = "Id de médico") @PathVariable(name = "id") String id
    ) throws Exception
    {
        Physician record = service.filterById(Integer.parseInt(id));
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene médico por su identificación",
            path = "/api/physicians/filter/identification/{identification}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Physician.class)
    @RequestMapping(value = "/filter/identification/{identification}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Physician> filterByIdentification(
            @ApiPathParam(name = "identification", description = "Identification de médico") @PathVariable(name = "identification") String identification
    ) throws Exception
    {
        Physician record = service.filterByIdentification(identification);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualiza  médico",
            path = "/api/physicians",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Physician.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Physician> update(
            @ApiBodyObject(clazz = Physician.class) @RequestBody(required = true) Physician update
    ) throws Exception
    {
        return new ResponseEntity<>(service.update(update), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Crea médico",
            path = "/api/physicians",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Physician.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Physician> create(
            @ApiBodyObject(clazz = Physician.class) @RequestBody Physician create
    ) throws Exception
    {
        return new ResponseEntity<>(service.create(create), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene lista médico por el estado",
            path = "/api/physicians/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Physician.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Physician>> filterByState(
            @ApiPathParam(name = "state", description = "Estado activo(true) o inactivo(false)") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Physician> records = service.filterByState(state);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}

package net.cltech.enterprisent.controllers.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Specialist;
import net.cltech.enterprisent.service.interfaces.masters.demographic.SpecialistService;
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
 * Servicios REST especialidad
 *
 * @version 1.0.0
 * @author Eacuna
 * @since 17/04/2017
 * @see Creacion
 */
@Api(
        group = "Demografico",
        name = "Especialidad",
        description = "Servicios sobre especialidades"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/specialists")
public class SpecialistController
{

    @Autowired
    private SpecialistService service;

    @ApiMethod(
            description = "Obtiene especialidades creadas",
            path = "/api/specialists",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Specialist.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Specialist>> get() throws Exception
    {
        List<Specialist> records = service.list();
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(service.list(), HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene especialidad por su id",
            path = "/api/specialists/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Specialist.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Specialist> filterById(
            @ApiPathParam(name = "id", description = "Id de especialidad") @PathVariable(name = "id") String id
    ) throws Exception
    {
        Specialist record = service.filterById(Integer.parseInt(id));
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene especialidad por su nombre",
            path = "/api/specialists/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Specialist.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Specialist> filterByName(
            @ApiPathParam(name = "name", description = "Nombre de especialidad") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Specialist record = service.filterByName(name);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualiza  especialidad enviada",
            path = "/api/specialists",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Specialist.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Specialist> update(
            @ApiBodyObject(clazz = Specialist.class) @RequestBody(required = true) Specialist unit
    ) throws Exception
    {
        return new ResponseEntity<>(service.update(unit), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Crea especialidad enviada",
            path = "/api/specialists",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Specialist.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Specialist> create(
            @ApiBodyObject(clazz = Specialist.class) @RequestBody Specialist unit
    ) throws Exception
    {
        return new ResponseEntity<>(service.create(unit), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene lista especialidades por el estado",
            path = "/api/specialists/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Specialist.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Specialist>> filterByState(
            @ApiPathParam(name = "state", description = "Estado activo(true) o inactivo(false)") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Specialist> records = service.filterByState(state);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}

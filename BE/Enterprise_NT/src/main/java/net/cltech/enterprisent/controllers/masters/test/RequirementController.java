package net.cltech.enterprisent.controllers.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Requirement;
import net.cltech.enterprisent.service.interfaces.masters.test.RequirementService;
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
 * Servicios REST para requisito
 *
 * @version 1.0.0
 * @author Eacuna
 * @since 28/04/2017
 * @see Creacion
 */
@Api(
        group = "Prueba",
        name = "Requisito",
        description = "Servicios sobre los requisitos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/requirements")
public class RequirementController
{

    @Autowired
    private RequirementService requirementService;

    @ApiMethod(
            description = "Obtiene requisitos",
            path = "/api/requirements",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Requirement.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Requirement>> get() throws Exception
    {
        List<Requirement> records = requirementService.list();
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(requirementService.list(), HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene requisito por su id",
            path = "/api/requirements/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Requirement.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Requirement> filterById(
            @ApiPathParam(name = "id", description = "Id de la unidad") @PathVariable(name = "id") String id
    ) throws Exception
    {
        Requirement record = requirementService.filterById(Integer.parseInt(id));
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene requisito por codigo",
            path = "/api/requirements/filter/code/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Requirement.class)
    @RequestMapping(value = "/filter/code/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Requirement> filterByCode(
            @ApiPathParam(name = "code", description = "Nombre de la unidad") @PathVariable(name = "code") String code
    ) throws Exception
    {
        Requirement record = requirementService.filterByCode(code);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualiza requisito",
            path = "/api/requirements",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Requirement.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Requirement> update(
            @ApiBodyObject(clazz = Requirement.class) @RequestBody(required = true) Requirement unit
    ) throws Exception
    {
        requirementService.update(unit);
        return new ResponseEntity<>(unit, HttpStatus.OK);
    }

    @ApiMethod(
            description = "Crea requisito",
            path = "/api/requirements",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Requirement.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Requirement> create(
            @ApiBodyObject(clazz = Requirement.class) @RequestBody Requirement requirement
    ) throws Exception
    {
        return new ResponseEntity<>(requirementService.create(requirement), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene requisito por su estado",
            path = "/api/requirements/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Requirement.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Requirement>> filterByState(
            @ApiPathParam(name = "state", description = "Estado activo(true), inactivo(false)") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Requirement> records = requirementService.filterByState(state);
        if (records != null)
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}

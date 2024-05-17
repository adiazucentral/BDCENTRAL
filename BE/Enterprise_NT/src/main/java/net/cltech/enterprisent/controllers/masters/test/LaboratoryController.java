package net.cltech.enterprisent.controllers.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratoryService;
import net.cltech.enterprisent.tools.Constants;
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
 * Servicios REST laboratorio
 *
 * @version 1.0.0
 * @author Eacuna
 * @since 17/04/2017
 * @see Creacion
 */
@Api(
        group = "Prueba",
        name = "Laboratorio",
        description = "Servicios sobre laboratorios internos o de referencia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/laboratories")
public class LaboratoryController
{

    @Autowired
    private LaboratoryService service;

    @ApiMethod(
            description = "Obtiene laboratorios",
            path = "/api/laboratories",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Laboratory.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Laboratory>> get() throws Exception
    {
        List<Laboratory> records = service.list();
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene laboratorio por su id",
            path = "/api/laboratories/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Laboratory.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Laboratory> filterById(
            @ApiPathParam(name = "id", description = "Id de laboratorio") @PathVariable(name = "id") String id
    ) throws Exception
    {
        Laboratory record = service.filterById(Integer.parseInt(id));
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene laboratorio por su nombre",
            path = "/api/laboratories/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Laboratory.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Laboratory> filterByName(
            @ApiPathParam(name = "name", description = "Nombre de laboratorio") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Laboratory record = service.filterByName(name);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene Id laboratorio por su nombre sin",
            path = "/api/laboratories/filter/name/sendExternal/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )

    @ApiResponseObject(clazz = Laboratory.class)
    @RequestMapping(value = "/filter/name/sendExternal/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> filterByNameSendExternal(
            @ApiPathParam(name = "name", description = "Nombre de laboratorio") @PathVariable(name = "name") String name
    )
    {
        try
        {
            Integer idLaboratorio = service.filterByName(name).getId();
            if (idLaboratorio != null)
            {
                return new ResponseEntity<>(idLaboratorio, HttpStatus.OK);
            } else
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

        } catch (Exception e)
        {
            return new ResponseEntity<>(-1, HttpStatus.OK);
        }

    }

    @ApiMethod(
            description = "Obtiene laboratorio por su código",
            path = "/api/laboratories/filter/code/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Laboratory.class)
    @RequestMapping(value = "/filter/code/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Laboratory> filterByName(
            @ApiPathParam(name = "code", description = "Código de laboratorio") @PathVariable(name = "code") int code
    ) throws Exception
    {
        Laboratory record = service.filterByCode(code);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualiza  laboratorio enviado",
            path = "/api/laboratories",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Laboratory.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Laboratory> update(
            @ApiBodyObject(clazz = Laboratory.class) @RequestBody(required = true) Laboratory update
    ) throws Exception
    {
        return new ResponseEntity<>(service.update(update, Constants.ORIGINLABORATORY), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Actualiza el laboratorio desde integration middleware",
            path = "/api/laboratories/integration",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Laboratory.class)
    @RequestMapping(value = "/integration", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Laboratory> updateIntegration(
            @ApiBodyObject(clazz = Laboratory.class) @RequestBody(required = true) Laboratory update
    ) throws Exception
    {
        return new ResponseEntity<>(service.update(update, Constants.ORIGININTEGRATION), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Crea el laboratorio enviado",
            path = "/api/laboratories",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Laboratory.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Laboratory> create(
            @ApiBodyObject(clazz = Laboratory.class) @RequestBody Laboratory create
    ) throws Exception
    {
        return new ResponseEntity<>(service.create(create), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene lista laboratorios por el estado",
            path = "/api/laboratories/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Laboratory.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Laboratory>> filterByState(
            @ApiPathParam(name = "state", description = "Estado activo(true) o inactivo(false)") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Laboratory> records = service.filterByState(state);
        if (records != null)
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene lista laboratorios de procesamiento",
            path = "/api/laboratories/listprocessing",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Laboratory.class)
    @RequestMapping(value = "/listprocessing", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Laboratory>> listProcessing() throws Exception
    {
        List<Laboratory> records = service.listLaboratorysProcessing();
        if (records != null)
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}

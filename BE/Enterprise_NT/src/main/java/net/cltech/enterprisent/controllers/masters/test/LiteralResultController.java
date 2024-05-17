package net.cltech.enterprisent.controllers.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.LiteralByTest;
import net.cltech.enterprisent.domain.masters.test.LiteralResult;
import net.cltech.enterprisent.service.interfaces.masters.test.LiteralResultService;
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
 * Servicios REST grupo poblacinal
 *
 * @version 1.0.0
 * @author Eacuna
 * @since 22/05/2017
 * @see Creacion
 */
@Api(
        group = "Prueba",
        name = "Resultado Literal",
        description = "Servicios sobre resultados literales"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/literalresults")
public class LiteralResultController
{

    @Autowired
    private LiteralResultService service;

    @ApiMethod(
            description = "Obtiene resultado literals",
            path = "/api/literalresults",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = LiteralResult.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LiteralResult>> get() throws Exception
    {
        List<LiteralResult> records = service.list();
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(service.list(), HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene resultado literal por su id",
            path = "/api/literalresults/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = LiteralResult.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LiteralResult> filterById(
            @ApiPathParam(name = "id", description = "Id de resultado literal") @PathVariable(name = "id") String id
    ) throws Exception
    {
        LiteralResult record = service.filterById(Integer.parseInt(id));
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene resultado literal por su nombre",
            path = "/api/literalresults/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = LiteralResult.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LiteralResult> filterByName(
            @ApiPathParam(name = "name", description = "Nombre de resultado literal") @PathVariable(name = "name") String name
    ) throws Exception
    {
        LiteralResult record = service.filterByName(name);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualiza  resultado literal enviada",
            path = "/api/literalresults",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = LiteralResult.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LiteralResult> update(
            @ApiBodyObject(clazz = LiteralResult.class) @RequestBody(required = true) LiteralResult update
    ) throws Exception
    {
        return new ResponseEntity<>(service.update(update), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Crea resultado literal enviada",
            path = "/api/literalresults",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = LiteralResult.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LiteralResult> create(
            @ApiBodyObject(clazz = LiteralResult.class) @RequestBody LiteralResult create
    ) throws Exception
    {
        return new ResponseEntity<>(service.create(create), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene lista resultado literals por el estado",
            path = "/api/literalresults/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = LiteralResult.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LiteralResult>> filterByState(
            @ApiPathParam(name = "state", description = "Estado activo(true) o inactivo(false)") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<LiteralResult> records = service.filterByState(state);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene lista resultado literals por examen",
            path = "/api/literalresults/filter/test/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = LiteralResult.class)
    @RequestMapping(value = "/filter/test/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LiteralByTest>> filterByState(
            @ApiPathParam(name = "id", description = "Id del examen") @PathVariable(name = "id") Integer id
    ) throws Exception
    {
        List<LiteralByTest> records = service.filterByTest(id);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualiza/Crea asignacion resultados literales a examenes",
            path = "/api/literalresults/assign/test",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/assign/test", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> assignResults(
            @ApiBodyObject(clazz = LiteralByTest.class) @RequestBody(required = true) List<LiteralByTest> update
    ) throws Exception
    {
        return new ResponseEntity<>(service.assignResults(update), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene los resultados literales para cada uno de las pruebas",
            path = "/api/literalresults/resultsentry",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = LiteralResult.class)
    @RequestMapping(value = "/resultsentry", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LiteralResult>> getWithTest() throws Exception
    {
        List<LiteralResult> records = service.listWithTestId();
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene los resultados literales por orden de prueba",
            path = "/api/literalresults/literalOrder/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = LiteralResult.class)
    @RequestMapping(value = "/literalOrder/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LiteralResult>> getWithTest(
            @ApiPathParam(name = "order", description = "id orden") @PathVariable(name = "order") long order
    ) throws Exception
    {
        List<LiteralResult> records = service.listByOrder(order);
        if (records.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        }
    }

}

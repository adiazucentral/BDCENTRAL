package net.cltech.enterprisent.controllers.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Alarm;
import net.cltech.enterprisent.domain.masters.test.ResultRelationship;
import net.cltech.enterprisent.service.interfaces.masters.test.ResultRelationshipService;
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
 * Servicios para el acceso a la informacion de la maestro Relación resultados
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/07/2017
 * @see Creacion
 */
@Api(
        name = "Relación resultados",
        group = "Prueba",
        description = "Servicios de informacion de la maestro Relación resultados"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/resultrelationships")
public class ResultRelationController
{

    @Autowired
    private ResultRelationshipService service;

    @ApiMethod(
            description = "Lista relaciones creadas",
            path = "/api/resultrelationships/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultRelationship.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResultRelationship>> list(
            @ApiPathParam(name = "id", description = "Id de la alarma") @PathVariable(name = "id") int id
    ) throws Exception
    {
        List<ResultRelationship> list = service.list(id);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Crea una nuevas reglas de alarma",
            path = "/api/resultrelationships",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> create(
            @ApiBodyObject(clazz = Alarm.class) @RequestBody Alarm alarm
    ) throws Exception
    {
        return new ResponseEntity<>(service.create(alarm), HttpStatus.OK);
    }
}

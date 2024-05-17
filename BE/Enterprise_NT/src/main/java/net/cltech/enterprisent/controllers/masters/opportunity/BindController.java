package net.cltech.enterprisent.controllers.masters.opportunity;

import java.util.List;
import net.cltech.enterprisent.domain.masters.opportunity.Bind;
import net.cltech.enterprisent.service.interfaces.masters.opportunity.BindService;
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
 * Servicios para el acceso a la informacion del maestro Clases
 *
 * @version 1.0.0
 * @author eacuna
 * @since 15/02/2018
 * @see Creacion
 */
@Api(
        name = "Clase",
        group = "Oportunidad",
        description = "Servicios de informacion del maestro Clases"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/binds")
public class BindController
{

    @Autowired
    private BindService service;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista las clases registradas",
            path = "/api/binds",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Bind.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Bind>> list() throws Exception
    {
        List<Bind> list = service.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR POR ESTADO ----------------
    @ApiMethod(
            description = "Lista las clases registradas por estado",
            path = "/api/binds/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Bind.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Bind>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Bind> list = service.list(state);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR ID ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una clase",
            path = "/api/binds/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Bind.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bind> getById(
            @ApiPathParam(name = "id", description = "Id de la clase") @PathVariable(name = "id") int id
    ) throws Exception
    {
        Bind bind = service.get(id, null);
        if (bind == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(bind, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una clase",
            path = "/api/binds/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Bind.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bind> getByName(
            @ApiPathParam(name = "name", description = "Nombre de la clase") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Bind bind = service.get(null, name);
        if (bind == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(bind, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea una nueva clase",
            path = "/api/binds",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Bind.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bind> create(
            @ApiBodyObject(clazz = Bind.class) @RequestBody Bind bind
    ) throws Exception
    {
        return new ResponseEntity<>(service.create(bind), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar una clase",
            path = "/api/binds",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Bind.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bind> update(
            @ApiBodyObject(clazz = Bind.class) @RequestBody Bind bind
    ) throws Exception
    {
        return new ResponseEntity<>(service.update(bind), HttpStatus.OK);
    }
}

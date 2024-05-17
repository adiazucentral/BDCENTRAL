package net.cltech.enterprisent.controllers.masters.tracking;

import java.util.List;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.masters.tracking.DestinationRoute;
import net.cltech.enterprisent.service.interfaces.masters.tracking.DestinationService;
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
 * Servicios REST destinos
 *
 * @version 1.0.0
 * @author cmartin
 * @since 27/07/2017
 * @see Creacion
 */
@Api(
        group = "Trazabilidad",
        name = "Destino",
        description = "Servicios sobre destinos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/destinations")
public class DestinationController
{

    @Autowired
    private DestinationService destinationService;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los destinos registrados",
            path = "/api/destinations",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Destination.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Destination>> list() throws Exception
    {
        List<Destination> list = destinationService.list();
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
            description = "Lista los destinos registrados por estado",
            path = "/api/destinations/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Destination.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Destination>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Destination> list = destinationService.list(state);
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
            description = "Obtiene la informacion de un destino",
            path = "/api/destinations/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Destination.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Destination> getById(
            @ApiPathParam(name = "id", description = "Id del destino") @PathVariable(name = "id") int id
    ) throws Exception
    {
        Destination destination = destinationService.get(id, null, null);
        if (destination == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(destination, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR CODIGO ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un destino",
            path = "/api/destinations/filter/code/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Destination.class)
    @RequestMapping(value = "/filter/code/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Destination> getByCode(
            @ApiPathParam(name = "code", description = "Codigo del destino") @PathVariable(name = "code") String code
    ) throws Exception
    {
        Destination destination = destinationService.get(null, code, null);
        if (destination == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(destination, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un destino",
            path = "/api/destinations/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Destination.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Destination> getByName(
            @ApiPathParam(name = "name", description = "Nombre del destino") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Destination destination = destinationService.get(null, null, name);
        if (destination == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(destination, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un nuevo destino",
            path = "/api/destinations",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Destination.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Destination> create(
            @ApiBodyObject(clazz = Destination.class) @RequestBody Destination destination
    ) throws Exception
    {
        return new ResponseEntity<>(destinationService.create(destination), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un destino",
            path = "/api/destinations",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Destination.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Destination> update(
            @ApiBodyObject(clazz = Destination.class) @RequestBody Destination destination
    ) throws Exception
    {
        return new ResponseEntity<>(destinationService.update(destination), HttpStatus.OK);
    }

    //------------ LISTAR POR ASIGNACION DE DESTINOS ----------------
    @ApiMethod(
            description = "Lista la ruta de destinos asociadas a una sede, muestra y tipo de orden",
            path = "/api/destinations/assignment/branch/{branch}/sample/{sample}/typeorder/{typeorder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DestinationRoute.class)
    @RequestMapping(value = "/assignment/branch/{branch}/sample/{sample}/typeorder/{typeorder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssignmentDestination> listAssignment(
            @ApiPathParam(name = "branch", description = "Sede") @PathVariable(name = "branch") int branch,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") int sample,
            @ApiPathParam(name = "typeorder", description = "Tipo de Orden") @PathVariable(name = "typeorder") int typeorder
    ) throws Exception
    {
        AssignmentDestination assignment = destinationService.getRoute(branch, sample, typeorder, true);
        if (assignment == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(assignment, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea una nueva asignacion de destinos",
            path = "/api/destinations/assignment",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AssignmentDestination.class)
    @RequestMapping(value = "/assignment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssignmentDestination> createAssignment(
            @ApiBodyObject(clazz = AssignmentDestination.class) @RequestBody AssignmentDestination assignmentDestination
    ) throws Exception
    {
        return new ResponseEntity<>(destinationService.createAssignment(assignmentDestination), HttpStatus.OK);
    }

    //------------ LISTAR OPORTUNIDAD DE LA MUESTRA ----------------
    @ApiMethod(
            description = "Lista la ruta de destinos asociadas a una sede, muestra y tipo de orden",
            path = "/api/destinations/oportunity/branch/{branch}/sample/{sample}/typeorder/{typeorder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DestinationRoute.class)
    @RequestMapping(value = "/oportunity/branch/{branch}/sample/{sample}/typeorder/{typeorder}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssignmentDestination> listOportunity(
            @ApiPathParam(name = "branch", description = "Sede") @PathVariable(name = "branch") int branch,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") int sample,
            @ApiPathParam(name = "typeorder", description = "Tipo de Orden") @PathVariable(name = "typeorder") int typeorder
    ) throws Exception
    {
        AssignmentDestination assignment = destinationService.getRoute(branch, sample, typeorder, false);
        if (assignment == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(assignment, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea nuevos datos en la oportunidad de la muestra",
            path = "/api/destinations/oportunity",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DestinationRoute.class)
    @RequestMapping(value = "/oportunity", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> create(
            @ApiBodyObject(clazz = AssignmentDestination.class) @RequestBody List<DestinationRoute> destinations
    ) throws Exception
    {
        return new ResponseEntity<>(destinationService.createSampleOportunity(destinations), HttpStatus.OK);
    }
}

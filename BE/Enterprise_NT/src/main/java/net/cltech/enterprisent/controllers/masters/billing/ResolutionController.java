package net.cltech.enterprisent.controllers.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.FilterResolution;
import net.cltech.enterprisent.domain.masters.billing.Resolution;
import net.cltech.enterprisent.domain.masters.billing.SingularResolution;
import net.cltech.enterprisent.service.interfaces.masters.billing.ResolutionService;
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
 * Servicios para el acceso a la informacion del maestro Receptor
 *
 * @version 1.0.0
 * @author cmartin
 * @since 05/07/2017
 * @see Creacion
 */
@Api(
        name = "Resolución",
        group = "Facturacion",
        description = "Servicios de informacion del maestro Resolución"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/resolutions")
public class ResolutionController
{

    @Autowired
    private ResolutionService resolutionService;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista las resoluciones registradas",
            path = "/api/resolutions",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Resolution.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Resolution>> list() throws Exception
    {
        List<Resolution> list = resolutionService.list();
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
            description = "Lista las resoluciones registradas por estado",
            path = "/api/resolutions/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Resolution.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Resolution>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Resolution> list = resolutionService.list(state);
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
            description = "Obtiene la informacion de una resolucion",
            path = "/api/resolutions/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Resolution.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resolution> getById(
            @ApiPathParam(name = "id", description = "Id del receptor") @PathVariable(name = "id") int id
    ) throws Exception
    {
        Resolution resolution = resolutionService.get(id);
        if (resolution == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(resolution, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea una nueva resolución",
            path = "/api/resolutions",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Resolution.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resolution> create(
            @ApiBodyObject(clazz = Resolution.class) @RequestBody Resolution resolution
    ) throws Exception
    {
        return new ResponseEntity<>(resolutionService.create(resolution), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar una resolucion",
            path = "/api/resolutions",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Resolution.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resolution> update(
            @ApiBodyObject(clazz = Resolution.class) @RequestBody Resolution resolution
    ) throws Exception
    {
        return new ResponseEntity<>(resolutionService.update(resolution), HttpStatus.OK);
    }
    
    @ApiMethod(
            description = "Consulta la resolución 4505 dependiendo de los filtros enviados bajo el servicio",
            path = "/api/resolutions/getResolution4505",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getResolution4505", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SingularResolution>> getResolution4505(
            @ApiBodyObject(clazz = FilterResolution.class) @RequestBody FilterResolution filter
    ) throws Exception
    {
        List<SingularResolution> resolution = resolutionService.getResolution4505(filter);
        if (resolution == null)
        {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if (resolution.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity(resolution, HttpStatus.OK);
        }
    }
}

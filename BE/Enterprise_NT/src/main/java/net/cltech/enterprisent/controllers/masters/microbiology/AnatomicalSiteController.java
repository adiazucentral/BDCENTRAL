package net.cltech.enterprisent.controllers.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.AnatomicalSite;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.AnatomicalSiteService;
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
 * Servicios para el acceso a la informacion del maestro Sitios Anatomicos
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/06/2017
 * @see Creacion
 */
@Api(
        name = "Sitio Anatomico",
        group = "Microbiología",
        description = "Servicios de informacion del maestro Sitios Anatomicos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/anatomicalsites")
public class AnatomicalSiteController
{

    @Autowired
    private AnatomicalSiteService anatomicalSiteService;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los sitios anatomicos registrados",
            path = "/api/anatomicalsites",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AnatomicalSite.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AnatomicalSite>> list() throws Exception
    {
        List<AnatomicalSite> list = anatomicalSiteService.list();
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
            description = "Lista los sitios anatomicos registrados por estado",
            path = "/api/anatomicalsites/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AnatomicalSite.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AnatomicalSite>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<AnatomicalSite> list = anatomicalSiteService.list(state);
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
            description = "Obtiene la informacion de un sitio anatomico",
            path = "/api/anatomicalsites/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AnatomicalSite.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnatomicalSite> getById(
            @ApiPathParam(name = "id", description = "Id del sitio anatomico") @PathVariable(name = "id") int id
    ) throws Exception
    {
        AnatomicalSite area = anatomicalSiteService.get(id, null, null);
        if (area == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(area, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un sitio anatomico",
            path = "/api/anatomicalsites/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AnatomicalSite.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnatomicalSite> getByName(
            @ApiPathParam(name = "name", description = "Nombre del sitio anatomico") @PathVariable(name = "name") String name
    ) throws Exception
    {
        AnatomicalSite anatomicalSite = anatomicalSiteService.get(null, name, null);
        if (anatomicalSite == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(anatomicalSite, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR ABREVIACION ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un sitio anatomico",
            path = "/api/anatomicalsites/filter/abbr/{abbr}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AnatomicalSite.class)
    @RequestMapping(value = "/filter/abbr/{abbr}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnatomicalSite> getByAbbr(
            @ApiPathParam(name = "abbr", description = "Abreviacion del sitio anatomico") @PathVariable(name = "abbr") String abbr
    ) throws Exception
    {
        AnatomicalSite anatomicalSite = anatomicalSiteService.get(null, null, abbr);
        if (anatomicalSite == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(anatomicalSite, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un nuevo sitio anatomico",
            path = "/api/anatomicalsites",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AnatomicalSite.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnatomicalSite> create(
            @ApiBodyObject(clazz = AnatomicalSite.class) @RequestBody AnatomicalSite anatomicalSite
    ) throws Exception
    {
        return new ResponseEntity<>(anatomicalSiteService.create(anatomicalSite), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un sitio anatomico",
            path = "/api/anatomicalsites",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AnatomicalSite.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnatomicalSite> update(
            @ApiBodyObject(clazz = AnatomicalSite.class) @RequestBody AnatomicalSite anatomicalSite
    ) throws Exception
    {
        return new ResponseEntity<>(anatomicalSiteService.update(anatomicalSite), HttpStatus.OK);
    }
}

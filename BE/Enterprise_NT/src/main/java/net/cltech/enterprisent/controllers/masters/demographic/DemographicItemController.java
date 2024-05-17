package net.cltech.enterprisent.controllers.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicItemService;
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
 * Servicios para el acceso a la informacion del maestro Demografico Item
 *
 * @version 1.0.0
 * @author enavas
 * @since 09/05/2017
 * @see Creacion
 */
@Api(
        name = "DemograficoItem",
        group = "Demografico",
        description = "Servicios de informacion del maestro Demografico Item"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/demographicitems")
public class DemographicItemController
{

    @Autowired
    private DemographicItemService demographicItemService;

    @ApiMethod(
            description = "Lista los demograficos items registrados",
            path = "/api/demographicitems",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicItem.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicItem>> list() throws Exception
    {
        List<DemographicItem> list = demographicItemService.list();
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
            description = "Obtiene la informacion del demograficos items ",
            path = "/api/demographicitems/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicItem.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemographicItem> getById(
            @ApiPathParam(name = "id", description = "Id del demograficos item") @PathVariable(name = "id") int id
    ) throws Exception
    {
        List<DemographicItem> list = demographicItemService.get(id, null, null, null, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list.get(0), HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NAME ----------------
    @ApiMethod(
            description = "Obtiene la informacion del demograficos items ",
            path = "/api/demographicitems/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicItem.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemographicItem> getByName(
            @ApiPathParam(name = "name", description = "Nombre del demograficos item") @PathVariable(name = "name") String name
    ) throws Exception
    {
        List<DemographicItem> list = demographicItemService.get(null, null, name, null, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list.get(0), HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR CODE ----------------
    @ApiMethod(
            description = "Obtiene la informacion del demograficos items ",
            path = "/api/demographicitems/filter/code/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicItem.class)
    @RequestMapping(value = "/filter/code/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemographicItem> getByCode(
            @ApiPathParam(name = "code", description = "Codigo del demograficos item") @PathVariable(name = "code") String code
    ) throws Exception
    {
        List<DemographicItem> list = demographicItemService.get(null, code, null, null, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list.get(0), HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR DEMOGRAPHIC ----------------
    @ApiMethod(
            description = "Obtiene la informacion del demograficos items ",
            path = "/api/demographicitems/filter/demographic/{demographic}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicItem.class)
    @RequestMapping(value = "/filter/demographic/{demographic}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicItem>> getByDemographic(
            @ApiPathParam(name = "demographic", description = "Codigo del demografico") @PathVariable(name = "demographic") int demographic
    ) throws Exception
    {
        List<DemographicItem> list = demographicItemService.get(null, null, null, demographic, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
       //------------ CONSULTA POR DEMOGRAPHIC USUARIOS CONSULTA WEB ----------------
    @ApiMethod(
            description = "Obtiene la informacion del demograficos items ",
            path = "/api/demographicitems/filter/demographicwebquery/{demographic}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicItem.class)
    @RequestMapping(value = "/filter/demographicwebquery/{demographic}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicItem>> getByDemographiccwebquery(
            @ApiPathParam(name = "demographic", description = "Codigo del demografico") @PathVariable(name = "demographic") int demographic
    ) throws Exception
    {
        List<DemographicItem> list = demographicItemService.getwebquery(null, null, null, demographic, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR STATE ----------------
    @ApiMethod(
            description = "Obtiene la informacion del demograficos items ",
            path = "/api/demographicitems/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicItem.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicItem>> getByState(
            @ApiPathParam(name = "state", description = "Estado del demograficos item") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<DemographicItem> list = demographicItemService.get(null, null, null, null, state);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un item del demografico",
            path = "/api/demographicitems",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicItem.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemographicItem> create(
            @ApiBodyObject(clazz = DemographicItem.class) @RequestBody DemographicItem demographicItem
    ) throws Exception
    {
        return new ResponseEntity<>(demographicItemService.create(demographicItem), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un item del demografico",
            path = "/api/demographicitems",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicItem.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemographicItem> update(
            @ApiBodyObject(clazz = DemographicItem.class) @RequestBody DemographicItem demographicItem
    ) throws Exception
    {
        return new ResponseEntity<>(demographicItemService.update(demographicItem), HttpStatus.OK);
    }

   
}

package net.cltech.enterprisent.controllers.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.DemographicWebQuery;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicWebQueryService;
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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Servicio para la configuracion adicional del demografico para la gestion de
 * la consulta web
 *
 * @version 1.0.0
 * @author javila
 * @since 23/01/2020
 * @see Creación
 */
@Api(
        name = "Demografico consulta web",
        group = "Demografico",
        description = "Configuración adicional del demografico"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/demographicwebquery")
public class DemographicWebQueryController
{

    @Autowired
    private DemographicWebQueryService demographicWebQueryService;

    // --------------- CONSULTAR TODOS LOS REGISTROS DE LA TABLA ---------------
    @ApiMethod(
            description = "Obtiene todos los registros de la tabla demografico consulta web",
            path = "/api/demographicwebquery/all",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicWebQuery.class)
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicWebQuery>> listAll() throws Exception
    {
        List<DemographicWebQuery> listDemographicWebQuery = demographicWebQueryService.list();
        if (listDemographicWebQuery.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(listDemographicWebQuery, HttpStatus.OK);
        }
    }

    // ----------- INSERTAR DATOS EN DEMOGRAFICOS CONSILTA WEB ----------
    @ApiMethod(
            description = "Inserta nuevo demografico consulta web",
            path = "/api/demographicwebquery",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicWebQuery.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemographicWebQuery> create(
            @ApiBodyObject(clazz = DemographicWebQuery.class) @RequestBody DemographicWebQuery demographicWebQuery
    ) throws Exception
    {
        return new ResponseEntity<>(demographicWebQueryService.create(demographicWebQuery), HttpStatus.OK);
    }

    // ----------- MODIFICAR DATOS EN DEMOGRAFICAS CONSULTA WEB -----------
    @ApiMethod(
            description = "Modificar un demografico consulta web ya existente",
            path = "/api/demographicwebquery",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicWebQuery.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemographicWebQuery> update(
            @ApiBodyObject(clazz = DemographicWebQuery.class) @RequestBody DemographicWebQuery demographicWebQuery
    ) throws Exception
    {
        return new ResponseEntity<>(demographicWebQueryService.update(demographicWebQuery), HttpStatus.OK);
    }

    // ----------- LISTAR DEMOGRAFICOS CONSULTA WEB POR DEMOGRAFICO Y ID DEL DEMOGRAFICO  ---------------
    @ApiMethod(
            description = "Obtiene los demográficos consulta web según su demográfico id y su demografico",
            path = "/api/demographicwebquery/filter/idDemographicItem/{idDemographicItem}/demographicwebquery/{demographicwebquery}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicWebQuery.class)
    @RequestMapping(value = "/filter/idDemographicItem/{idDemographicItem}/demographicwebquery/{demographicwebquery}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemographicWebQuery> filterByIdDemographic(
            @ApiPathParam(name = "idDemographicItem", description = "Id item demografico") @PathVariable(name = "idDemographicItem") int idDemographicItem,
            @ApiPathParam(name = "demographicwebquery", description = "Id item demografico") @PathVariable(name = "demographicwebquery") int demographicwebquery
    ) throws Exception
    {
        DemographicWebQuery filterByIdDemographic = demographicWebQueryService.filterByIdDemographic(idDemographicItem, demographicwebquery);
        if (filterByIdDemographic != null)
        {
            return new ResponseEntity<>(filterByIdDemographic, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    

    // ----------- LISTAR DEMOGRAFICOS CONSULTA WEB ACTIVOS  ---------------
    @ApiMethod(
            description = "Lista de usuarios tipo demografico consulta web con id y fecha de ultimo ingreso al sistema, para verifiacion de estado",
            path = "/api/demographicwebquery/listdeactivate",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )

    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicWebQuery.class)
    @RequestMapping(value = "/listdeactivate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicWebQuery>> listDeactivate() throws Exception
    {
        List<DemographicWebQuery> list = demographicWebQueryService.listDeactivate();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    // ----------- CAMBIA DE ESTADO UN USUARIO DE CONSULTA WEB  ---------------
    @ApiMethod(
            description = "Desactivar un usuario de tipo consulta web",
            path = "/api/demographicwebquery/deactivate",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicWebQuery.class)
    @RequestMapping(value = "/deactivate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemographicWebQuery> deactivateUser(
            @ApiBodyObject(clazz = DemographicWebQuery.class) @RequestBody DemographicWebQuery user
    ) throws Exception
    {
        user.setState(false);
        return new ResponseEntity<>(demographicWebQueryService.changeStateUser(user), HttpStatus.OK);
    }
}

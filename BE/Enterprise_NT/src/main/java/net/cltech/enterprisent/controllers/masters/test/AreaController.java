/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.service.interfaces.masters.test.AreaService;
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
 * Servicios para el acceso a la informacion del maestro Áreas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 12/04/2017
 * @see Creacion
 */
@Api(
        name = "Area",
        group = "Prueba",
        description = "Servicios de informacion del maestro Areas"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/areas")
public class AreaController
{

    @Autowired
    private AreaService areaService;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista las áreas registradas",
            path = "/api/areas",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Area.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Area>> list() throws Exception
    {
        List<Area> list = areaService.list();
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
            description = "Lista las áreas registradas por estado",
            path = "/api/areas/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Area.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Area>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Area> list = areaService.list(state);
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
            description = "Obtiene la informacion de un área",
            path = "/api/areas/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Area.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Area> getById(
            @ApiPathParam(name = "id", description = "Id del área") @PathVariable(name = "id") int id
    ) throws Exception
    {
        Area area = areaService.get(id, null, null, null);
        if (area == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(area, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR CODIGO ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un área",
            path = "/api/areas/filter/ordering/{ordering}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Area.class)
    @RequestMapping(value = "/filter/ordering/{ordering}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Area> getByOrdering(
            @ApiPathParam(name = "ordering", description = "Ordenamiento del área") @PathVariable(name = "ordering") int ordering
    ) throws Exception
    {
        Area area = areaService.get(null, ordering, null, null);
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
            description = "Obtiene la informacion de un área",
            path = "/api/areas/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Area.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Area> getByName(
            @ApiPathParam(name = "name", description = "Nombre del área") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Area area = areaService.get(null, null, name, null);
        if (area == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(area, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR ABREVIACION ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un área",
            path = "/api/areas/filter/abbr/{abbr}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Area.class)
    @RequestMapping(value = "/filter/abbr/{abbr}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Area> getByAbbr(
            @ApiPathParam(name = "abbr", description = "Abreviacion del área") @PathVariable(name = "abbr") String abbr
    ) throws Exception
    {
        Area area = areaService.get(null, null, null, abbr);
        if (area == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(area, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea una nueva área",
            path = "/api/areas",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Area.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Area> create(
            @ApiBodyObject(clazz = Area.class) @RequestBody Area area
    ) throws Exception
    {
        return new ResponseEntity<>(areaService.create(area), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un área",
            path = "/api/areas",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Area.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Area> update(
            @ApiBodyObject(clazz = Area.class) @RequestBody Area area
    ) throws Exception
    {
        return new ResponseEntity<>(areaService.update(area), HttpStatus.OK);
    }
}

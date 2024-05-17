/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.CellularCounter;
import net.cltech.enterprisent.service.interfaces.masters.test.CellularCounterService;
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
 * Servicios para el acceso a la informacion del maestro Contador Hematologico
 *
 * @version 1.0.0
 * @author cmartin
 * @since 01/08/2017
 * @see Creacion
 */
@Api(
        name = "Contador Hematologico",
        group = "Prueba",
        description = "Servicios de informacion del maestro Contador Hematologico"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/cellularcounters")
public class CellularCounterController
{

    @Autowired
    private CellularCounterService cellularCounterService;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los contadores hematologicos registrados",
            path = "/api/cellularcounters",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CellularCounter.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CellularCounter>> list() throws Exception
    {
        List<CellularCounter> list = cellularCounterService.list();
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
            description = "Lista los contadores hematologicos registrados por estado",
            path = "/api/cellularcounters/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CellularCounter.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CellularCounter>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<CellularCounter> list = cellularCounterService.list(state);
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
            description = "Obtiene la informacion de un contador hematologico",
            path = "/api/cellularcounters/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CellularCounter.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CellularCounter> getById(
            @ApiPathParam(name = "id", description = "Id del contador hematologico") @PathVariable(name = "id") int id
    ) throws Exception
    {
        CellularCounter cellularCounter = cellularCounterService.get(id, null);
        if (cellularCounter == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(cellularCounter, HttpStatus.OK);
        }
    }
    //------------ CONSULTA POR EXAMEN ----------------

    @ApiMethod(
            description = "Obtiene la informacion de un contador hematologico que pertenecen a un examen",
            path = "/api/cellularcounters/filter/test/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CellularCounter.class)
    @RequestMapping(value = "/filter/test/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CellularCounter>> getByIdTest(
            @ApiPathParam(name = "id", description = "Id examen") @PathVariable(name = "id") int id
    ) throws Exception
    {
        List<CellularCounter> cellularCounter = cellularCounterService.geTest(id);
        if (cellularCounter.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(cellularCounter, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR TECLA ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un contador hematologico",
            path = "/api/cellularcounters/filter/key/{key}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CellularCounter.class)
    @RequestMapping(value = "/filter/key/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CellularCounter> getByName(
            @ApiPathParam(name = "key", description = "Tecla del contador hematologico") @PathVariable(name = "key") String key
    ) throws Exception
    {
        CellularCounter cellularCounter = cellularCounterService.get(null, key);
        if (cellularCounter == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(cellularCounter, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea una nueva hoja de trabajo",
            path = "/api/cellularcounters",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CellularCounter.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CellularCounter> create(
            @ApiBodyObject(clazz = CellularCounter.class) @RequestBody CellularCounter cellularCounter
    ) throws Exception
    {
        return new ResponseEntity<>(cellularCounterService.create(cellularCounter), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un contador hematologico",
            path = "/api/cellularcounters",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CellularCounter.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CellularCounter> update(
            @ApiBodyObject(clazz = CellularCounter.class) @RequestBody CellularCounter cellularCounter
    ) throws Exception
    {
        return new ResponseEntity<>(cellularCounterService.update(cellularCounter), HttpStatus.OK);
    }
}

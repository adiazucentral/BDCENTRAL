/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.configuration;

import java.util.List;
import net.cltech.enterprisent.domain.masters.configuration.RIPS;
import net.cltech.enterprisent.service.interfaces.masters.configuration.RipsService;
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
 * Servicios REST para configuracion RIPS 
 *
 * @version 1.0.0
 * @author omendes
 * @since 20/01/2021
 * @see Creacion
 */
@Api(
    group = "Configuración",
    name = "RIPS",
    description = "Servicios sobre la Configuracion RIPS"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/configuration/rips")
public class RipsController {
    
    @Autowired
    private RipsService ripsService;

    @ApiMethod(
            description = "Obtiene todos los parametros de configuración rips",
            path = "/api/configuration/rips",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = RIPS.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RIPS>> get() throws Exception
    {
        List<RIPS> records = ripsService.get();
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene un parametro de configuración rips por su llave",
            path = "/api/configuration/rips/{key}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = RIPS.class)
    @RequestMapping(value = "/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RIPS> get(
            @ApiPathParam(name = "key", description = "Llave de configuración rips") @PathVariable(name = "key") String key
    ) throws Exception
    {
        RIPS record = ripsService.get(key);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualiza las llaves de configuracion rips enviadas",
            path = "/api/configuration/rips",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = RIPS.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RIPS>> update(
            @ApiBodyObject(clazz = RIPS.class) @RequestBody(required = true) List<RIPS> configuration
    ) throws Exception
    {
        ripsService.update(configuration);
        return new ResponseEntity<>(configuration, HttpStatus.OK);
    }
    
}

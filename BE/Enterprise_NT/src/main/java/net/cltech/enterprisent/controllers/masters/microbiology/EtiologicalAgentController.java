/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.EtiologicalAgent;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.EtiologicalAgentService;
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
 * Servicios REST Agente Etiológico
 *
 * @version 1.0.0
 * @author omendez
 * @since 02/06/2022
 * @see Creacion
 */
@Api(
        group = "Microbiología",
        name = "Agente Etiologico",
        description = "Servicios sobre agentes etiologicos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/etiologicalagent")
public class EtiologicalAgentController {
    
    @Autowired
    private EtiologicalAgentService service;

    @ApiMethod(
            description = "Obtiene agentes etiologicos",
            path = "/api/etiologicalagent",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = EtiologicalAgent.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EtiologicalAgent>> get() throws Exception
    {
        List<EtiologicalAgent> records = service.list();
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(service.list(), HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene agentes etiologicos por su id",
            path = "/api/etiologicalagent/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = EtiologicalAgent.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EtiologicalAgent> filterById(
            @ApiPathParam(name = "id", description = "Id del agente etiologico") @PathVariable(name = "id") String id
    ) throws Exception
    {
        EtiologicalAgent record = service.findById(Integer.parseInt(id));
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


    @ApiMethod(
            description = "Actualiza agente etiologico enviado",
            path = "/api/etiologicalagent",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = EtiologicalAgent.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EtiologicalAgent> update(
            @ApiBodyObject(clazz = EtiologicalAgent.class) @RequestBody(required = true) EtiologicalAgent update
    ) throws Exception
    {
        return new ResponseEntity<>(service.update(update), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Crea agente etiologico enviado",
            path = "/api/etiologicalagent",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = EtiologicalAgent.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EtiologicalAgent> create(
            @ApiBodyObject(clazz = EtiologicalAgent.class) @RequestBody EtiologicalAgent create
    ) throws Exception
    {
        return new ResponseEntity<>(service.create(create), HttpStatus.OK);
    }
}

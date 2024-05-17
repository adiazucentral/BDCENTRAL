/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.SpecimenProtocol;
import net.cltech.enterprisent.service.interfaces.masters.pathology.SpecimenProtocolService;
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
 * Servicios para el maestro de configuracion de procolos de especimenes de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/04/2021
 * @see Creacion
 */
@Api(
    group = "Patología",
    name = "Protocolo",
    description = "Servicios de maestros de configuracion de procolos de especimenes de patologia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/protocol")
public class SpecimenProtocolController 
{
    
    @Autowired
    private SpecimenProtocolService specimenProtocolService;
    
    
    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los protocolos registrados",
            path = "/api/pathology/protocol",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SpecimenProtocol.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SpecimenProtocol>> list() throws Exception
    {
        List<SpecimenProtocol> list = specimenProtocolService.list();
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
            description = "Obtiene la informacion de un protocolo por id",
            path = "/api/pathology/protocol/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SpecimenProtocol.class)
    @RequestMapping(value = "filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpecimenProtocol> getById(
            @ApiPathParam(name = "id", description = "Id del protocolo") @PathVariable(name = "id") int id
    ) throws Exception
    {
        SpecimenProtocol protocol = specimenProtocolService.get(id, null);
        if (protocol == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(protocol, HttpStatus.OK);
        }
    }
    
    //------------ CONSULTA POR ESPECIMEN ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un protocolo por especimen",
            path = "/api/pathology/protocol/filter/specimen/{specimen}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SpecimenProtocol.class)
    @RequestMapping(value = "filter/specimen/{specimen}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpecimenProtocol> getBySpecimen(
            @ApiPathParam(name = "specimen", description = "Id del especimen") @PathVariable(name = "specimen") int specimen
    ) throws Exception
    {
        SpecimenProtocol protocol = specimenProtocolService.get(null, specimen);
        if (protocol == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(protocol, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un nuevo protocolo",
            path = "/api/pathology/protocol",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SpecimenProtocol.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpecimenProtocol> create(
            @ApiBodyObject(clazz = SpecimenProtocol.class) @RequestBody SpecimenProtocol protocol
    ) throws Exception
    {
        return new ResponseEntity<>(specimenProtocolService.create(protocol), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un protocolo",
            path = "/api/pathology/protocolo",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SpecimenProtocol.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpecimenProtocol> update(
            @ApiBodyObject(clazz = SpecimenProtocol.class) @RequestBody SpecimenProtocol protocol
    ) throws Exception
    {
        return new ResponseEntity<>(specimenProtocolService.update(protocol), HttpStatus.OK);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.tools;

import java.util.HashMap;
import net.cltech.enterprisent.service.interfaces.tools.EncodeService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.annotation.ApiVersion;
import org.jsondoc.core.pojo.ApiVerb;
import org.jsondoc.core.pojo.ApiVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Servicios para encriptar y desencriptar información
 *
 * @version 1.0.0
 * @author omendez
 * @since 13/12/2021
 * @see Creacion
 */
@Api(
    name = "Encriptar - Desencriptar",
    group = "Herramientas",
    description = "Servicios para encriptar y desencriptar información"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/encode")

public class EncodeController {
    
    @Autowired
    private EncodeService service;
    
    //------------ ENCRIPTAR ----------------
    @ApiMethod(
            description = "Encriptar información",
            path = "/api/encode/encrypt",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT"
    )
    @ApiResponseObject(clazz = HashMap.class)
    @RequestMapping(value = "/encrypt", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap> encrypt(@ApiBodyObject(clazz = HashMap.class) @RequestBody HashMap<String, String> map) throws Exception
    {
        return new ResponseEntity<>(service.encrypt(map), HttpStatus.OK);
    }
    
    //------------ DESENCRIPTAR ----------------
    @ApiMethod(
            description = "Desencriptar información",
            path = "/api/encode/decrypt",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT"
    )
    @ApiResponseObject(clazz = HashMap.class)
    @RequestMapping(value = "/decrypt", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap> decrypt(@ApiBodyObject(clazz = HashMap.class) @RequestBody HashMap<String, String> map) throws Exception
    {
        return new ResponseEntity<>(service.decrypt(map), HttpStatus.OK);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.tools;

import com.auth0.jwt.exceptions.JWTVerificationException;
import net.cltech.enterprisent.domain.tools.EventsResponse;
import net.cltech.enterprisent.tools.JWT;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.annotation.ApiVersion;
import org.jsondoc.core.pojo.ApiVerb;
import org.jsondoc.core.pojo.ApiVisibility;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Servicios generales
 *
 * @version 1.0.0
 * @author equijano
 * @since 14/01/2019
 * @see Creacion
 */
@Api(
        name = "General",
        group = "Herramientas",
        description = "Servicios generales "
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/tools")
public class ToolsController {
    
    //------------ VALIDAR URL ----------------
    @ApiMethod(
            description = "Validar token",
            path = "/api/tools/validateToken",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/validateToken", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> validateUrl(
            @ApiBodyObject(clazz = EventsResponse.class) @RequestBody EventsResponse url
    ) throws Exception
    {
        try
        {
            JWT.validateToken(url.getUrl());
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (JWTVerificationException ex)
        {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}

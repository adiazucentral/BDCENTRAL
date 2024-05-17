package net.cltech.enterprisent.controllers.security;

import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.access.Module;
import net.cltech.enterprisent.service.interfaces.security.AuthorizationService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Servicios de autorizacion para los modulos de la aplicacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 03/04/2017
 * @see Creacion
 */
@Api(
        name = "Autorización",
        group = "Autenticación y Autorizacion",
        description = "Servicios de autorizacion de la aplicación"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/authorization")
public class AuthorizationController
{

    @Autowired
    private AuthorizationService authorizationService;

    @ApiMethod(
            description = "Revisa la autorización del usuario a un modulo",
            path = "/api/authorization/{module}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - Unathorized"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Module.class)
    @RequestMapping(value = "/{module}", method = RequestMethod.GET)
    public ResponseEntity<Module> authorization(
            @ApiPathParam(name = "module", clazz = Integer.class, description = "Id modulo requerido") @PathVariable("module") int moduleCode,
            HttpServletRequest request) throws Exception
    {
        return new ResponseEntity(authorizationService.authorization(moduleCode), HttpStatus.OK);
    }
}

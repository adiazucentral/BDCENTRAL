package net.cltech.securitynt.controllers.security;

import java.util.List;
import net.cltech.securitynt.domain.common.AuthenticationSession;
import net.cltech.securitynt.service.interfaces.security.SessionService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
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
 * Controlador de acceso a los servicios de visor de sesion
 *
 * @version 1.0.0
 * @author equijano
 * @since 30/11/2018
 * @see Creación
 */
@Api(
        group = "Autenticación y Autorizacion",
        name = "Autenticación de sesiones",
        description = "Servicios de autenticación de visor de sesiones"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/sessionviewer")
public class AuthenticationSessionController
{

    @Autowired
    private SessionService sessionService;

    @ApiMethod(
            description = "Registron de sesiones activas en el sistema",
            path = "/api/sessionviewer/create",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponseObject(clazz = AuthenticationSession.class)
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationSession> create(
            @ApiBodyObject(clazz = AuthenticationSession.class) @RequestBody AuthenticationSession session) throws Exception
    {
        return new ResponseEntity(sessionService.create(session), HttpStatus.OK);
    }

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista las sesiones activas",
            path = "/api/sessionviewer",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuthenticationSession.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuthenticationSession>> list() throws Exception
    {
        List<AuthenticationSession> list = sessionService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Elimina todas las sessiones activas",
            path = "/api/sessionviewer",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAll() throws Exception
    {
        sessionService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Elimina una session activa",
            path = "/api/sessionviewer/deleteBySession",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/deleteBySession", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteBySession(
            @ApiBodyObject(clazz = AuthenticationSession.class) @RequestBody AuthenticationSession delete) throws Exception
    {
        return new ResponseEntity<>(sessionService.deleteBySession(delete, true), HttpStatus.OK);
    }

}

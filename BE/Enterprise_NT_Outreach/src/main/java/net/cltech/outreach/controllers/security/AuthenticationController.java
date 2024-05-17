package net.cltech.outreach.controllers.security;

import java.util.List;
import net.cltech.outreach.domain.common.AuthenticationUser;
import net.cltech.outreach.domain.common.Email;
import net.cltech.outreach.domain.common.JWTToken;
import net.cltech.outreach.domain.masters.configuration.UserPassword;
import net.cltech.outreach.service.interfaces.security.AuthenticationService;
import net.cltech.outreach.tools.Log;
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
 * Controlador de acceso a los servicios de autenticacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 03/04/2017
 * @see Creación
 */
@Api(
        name = "Autenticación",
        group = "Autenticación y Autorizacion",
        description = "Servicios de autenticación de la aplicación"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController
{

    @Autowired
    private AuthenticationService service;

    //-----------------INICIO DE SESIÓN CON TOKEN HTTP------------------------
    @ApiMethod(
            description = "Realiza la autenticación del usuario, esta funcion retorna un token de sesion el cual se debe invocar en cada peticion al servidor",
            path = "/api/authentication/web",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponseObject(clazz = JWTToken.class)
    @RequestMapping(value = "/web", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTToken> authenticationWeb(@ApiBodyObject(clazz = AuthenticationUser.class) @RequestBody AuthenticationUser user) throws Exception
    {
        Log.info(getClass(), "service.authenticateWeb(user) " + service.authenticateWeb(user));
        return new ResponseEntity(service.authenticateWeb(user), HttpStatus.OK);
    }

    //-----------------RESTABLECER CONTRASEÑA------------------------
    @ApiMethod(
            description = "Obtiene token para restablecer la contraseña del usuario",
            path = "/api/authentication/passwordrecovery/{user}/{type}/{historyNumber}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = JWTToken.class)
    @RequestMapping(value = "/passwordrecovery/{user}/{type}/{historyNumber}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JWTToken>> passwordRecovery(
            @ApiPathParam(name = "user", description = "username") @PathVariable(name = "user") String user,
            @ApiPathParam(name = "historyNumber", description = "Número de historia del paciente") @PathVariable(name = "historyNumber") String historyNumber,
            @ApiPathParam(name = "type", description = "tipo de usuario", allowedvalues = "1,2,3") @PathVariable(name = "type") int type
    ) throws Exception
    {
        return new ResponseEntity<>(service.recovery(user, type, historyNumber), HttpStatus.OK);
    }

    //-----------------RESTABLECER CONTRASEÑA------------------------
    @ApiMethod(
            description = "Restablece la contraseña del usuario",
            path = "/api/authentication/passwordreset",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = AuthenticationUser.class)
    @RequestMapping(value = "/passwordreset", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> passwordReset(@ApiBodyObject(clazz = AuthenticationUser.class) @RequestBody AuthenticationUser user) throws Exception
    {
        return new ResponseEntity<>(service.reset(user.getPassword()), HttpStatus.OK);
    }

    //--------------- ENVIO DE CORREO ------------------------------
    @ApiMethod(
            description = "Enviar un correo",
            path = "/api/authentication/email",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/email", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendEmail(
            @ApiBodyObject(clazz = Email.class) @RequestBody Email email
    ) throws Exception
    {
        return new ResponseEntity<>(service.sendEmail(email), HttpStatus.OK);
    }

    //-----------------CAMBIAR CONTRASEÑA CONSULTA WEB------------------------
    @ApiMethod(
            description = "Actualizar contraseña de un usuario desde el inicio",
            path = "/api/authentication/updatepassword",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/updatepassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updatepassword(
            @ApiBodyObject(clazz = UserPassword.class) @RequestBody UserPassword userPassword
    ) throws Exception
    {
        service.updatePassword(userPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

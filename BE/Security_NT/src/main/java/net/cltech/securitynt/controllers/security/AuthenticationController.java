package net.cltech.securitynt.controllers.security;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.securitynt.domain.common.AuthenticationUser;
import net.cltech.securitynt.domain.common.AuthenticationUserType;
import net.cltech.securitynt.domain.common.AuthorizedUser;
import net.cltech.securitynt.domain.common.JWTToken;
import net.cltech.securitynt.domain.masters.user.User;
import net.cltech.securitynt.domain.masters.user.UserPassword;
import net.cltech.securitynt.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.securitynt.service.interfaces.masters.user.UserService;
import net.cltech.securitynt.tools.Constants;
import net.cltech.securitynt.tools.JWT;
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
    private UserService userService;
    @Autowired
    private ConfigurationService configurationService;

    @ApiMethod(
            description = "Realiza la autenticación del usuario, esta funcion retorna un token de sesion el cual se debe invocar en cada peticion al servidor",
            path = "/api/authentication",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponseObject(clazz = JWTToken.class)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTToken> authentication(@ApiBodyObject(clazz = AuthenticationUser.class) @RequestBody AuthenticationUser user) throws Exception, Exception
    {
        JWTToken token = new JWTToken();
        AuthorizedUser authorizedUser = userService.authenticate(user.getUser(), user.getPassword(), user.getBranch());
        //Permite un ingreso temporal a un usuario de desarrollo
        if (authorizedUser != null)
        {
            token.setSuccess(true);
            token.setUser(authorizedUser);
            String tokenExpiration = configurationService.get(Constants.TOKEN_EXPIRATION_TIME).getValue();
            token.setToken(JWT.generate(authorizedUser, tokenExpiration.equals("") ? 60 : Integer.parseInt(tokenExpiration), Constants.TOKEN_AUTH_USER));
        }
        else
        {
            token.setSuccess(false);
            token.setToken("");
        }
        return new ResponseEntity(token, HttpStatus.OK);
    }

    //-----------------TOKEN CONSULTA WEB ------------------------
    @ApiMethod(
            description = "Realiza la autenticación del usuario, esta funcion retorna un token de sesion el cual se debe invocar en cada peticion al servidor desde consulta web",
            path = "/api/authentication/webquery",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponseObject(clazz = JWTToken.class)
    @RequestMapping(value = "/webquery", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTToken> authenticationWeb(@ApiBodyObject(clazz = AuthenticationUserType.class) @RequestBody AuthenticationUserType user) throws Exception, Exception
    {
        JWTToken tokenType = new JWTToken();
        AuthorizedUser authorizedUserWeb = userService.authenticateWeb(user);
        //Permite un ingreso temporal a un usuario de desarrollo
        if (authorizedUserWeb != null)
        {
            tokenType.setSuccess(true);
            tokenType.setUser(authorizedUserWeb);
            String tokenExpiration = configurationService.get(Constants.TOKEN_EXPIRATION_TIME).getValue();
            tokenType.setToken(JWT.generate(authorizedUserWeb, tokenExpiration.equals("") ? 60 : Integer.parseInt(tokenExpiration), Constants.TOKEN_AUTH_USER));
        }
        else
        {
            tokenType.setSuccess(false);
            tokenType.setToken("");
        }
        return new ResponseEntity(tokenType, HttpStatus.OK);
    }

    //-----------------CAMBIAR CONTRASEÑA------------------------
    @ApiMethod(
            description = "Actualizar contraseña de un usuario",
            path = "/api/authentication/updateprofile",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/updateprofile", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateProfile(@ApiBodyObject(clazz = User.class) @RequestBody User user) throws Exception
    {
        boolean updatedPassword = userService.updateProfile(user);

        return new ResponseEntity<>(updatedPassword, HttpStatus.OK);
    }

    @ApiMethod(
            description = "Actualizar contraseña de un usuario desde el inicio",
            path = "/api/authentication/updatepassword",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/updatepassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatepassword(
            @ApiBodyObject(clazz = UserPassword.class) @RequestBody UserPassword userPassword
    ) throws Exception
    {
        if (userService.updatePassword(userPassword))
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiMethod(
            description = "Validar inactividad de usuarios",
            path = "/api/authentication/inactivityusers",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/inactivityusers", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> inactivityUsers(
            @ApiBodyObject(clazz = User.class) @RequestBody List<User> users,
            HttpServletRequest request
    ) throws Exception
    {
        userService.deactivateUsers(users, request.getHeader("Authorization"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //-----------------CAMBIAR CONTRASEÑA EN CONSULTA WEB------------------------
    @ApiMethod(
            description = "Actualizar contraseña de un usuario desde el inicio",
            path = "/api/authentication/updatepasswordweb",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/updatepasswordweb", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updatepasswordWeb(
            @ApiBodyObject(clazz = UserPassword.class) @RequestBody UserPassword userPassword
    ) throws Exception
    {
        if (userService.updatePasswordWeb(userPassword))
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiMethod(
            description = "Realiza la autenticación del usuario sin validar la licencia del producto",
            path = "/api/authentication/integrationAuthentication",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponseObject(clazz = JWTToken.class)
    @RequestMapping(value = "/integrationAuthentication", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTToken> integrationAuthentication(           
            @ApiBodyObject(clazz = AuthenticationUser.class) @RequestBody AuthenticationUser user
    ) throws Exception, Exception
    {
        JWTToken token = new JWTToken();
        AuthorizedUser authorizedUser = userService.integrationAuthentication(user.getUser(), user.getPassword(), user.getBranch());
        //Permite un ingreso temporal a un usuario de desarrollo
        if (authorizedUser != null)
        {
            token.setSuccess(true);
            token.setUser(authorizedUser);
            String tokenExpiration = configurationService.get(Constants.TOKEN_EXPIRATION_TIME).getValue();
            token.setToken(JWT.generate(authorizedUser, tokenExpiration.equals("") ? 60 : Integer.parseInt(tokenExpiration), Constants.TOKEN_AUTH_USER));
        }
        else
        {
            token.setSuccess(false);
            token.setToken("");
        }
        return new ResponseEntity(token, HttpStatus.OK);
    }
}

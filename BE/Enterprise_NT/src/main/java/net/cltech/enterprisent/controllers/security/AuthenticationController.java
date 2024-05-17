package net.cltech.enterprisent.controllers.security;

import java.util.List;
import net.cltech.enterprisent.controllers.common.License;
import net.cltech.enterprisent.domain.common.AuthenticationUser;
import net.cltech.enterprisent.domain.common.JWTToken;
import net.cltech.enterprisent.domain.common.UserHomeBound;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.masters.user.UserPassword;
import net.cltech.enterprisent.domain.masters.user.UserRecoveryPassword;
import net.cltech.enterprisent.service.interfaces.masters.demographic.BranchService;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
import net.cltech.enterprisent.service.interfaces.security.LicenseService;
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
    private UserService userService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private LicenseService licenseService;

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
    public ResponseEntity<JWTToken> authentication(@ApiBodyObject(clazz = AuthenticationUser.class) @RequestBody AuthenticationUser user) throws Exception
    {

        return new ResponseEntity(userService.authenticate(user), HttpStatus.OK);
    }

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista las sedes registradas",
            path = "/api/authentication/branches",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Branch.class)
    @RequestMapping(value = "/branches", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Branch>> list() throws Exception
    {
        List<Branch> list = branchService.list(true);

        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Lista las sedes registradas para el usuario",
            path = "/api/authentication/branches/filter/username/{username}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Branch.class)
    @RequestMapping(value = "/branches/filter/username/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Branch>> assignBranches(@ApiPathParam(name = "username", description = "nombre con el que se autentica el usuario") @PathVariable(name = "username") String username) throws Exception
    {
        List<Branch> list = branchService.filterByUsername(username);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
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
        return new ResponseEntity<>(userService.updateProfile(user), HttpStatus.OK);
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
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/updatepassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updatepassword(
            @ApiBodyObject(clazz = UserPassword.class) @RequestBody UserPassword userPassword
    ) throws Exception
    {
        userService.updatePassword(userPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
     @ApiMethod(
            description = "Recuperar la contraseña de los usuarios del laboratorio",
            path = "/api/authentication/recoverpassword",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )    
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/recoverpassword", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> UserRecoveryPassword(
            @ApiBodyObject(clazz = UserRecoveryPassword.class) @RequestBody UserRecoveryPassword userRecoveryPassword
    ) throws Exception
    {
        userService.recoverPassword(userRecoveryPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }       

    
    
    

    //-----------------INICIO DE SESIÓN CON TOKEN HTTP EN HOME BOUND------------------------
    @ApiMethod(
            description = "Realiza la autenticación del usuario, esta funcion retorna un token de sesion el cual se debe invocar en cada peticion al servidor",
            path = "/api/authentication/laboratory",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponseObject(clazz = JWTToken.class)
    @RequestMapping(value = "/laboratory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTToken> authenticateHomeBound(@ApiBodyObject(clazz = UserHomeBound.class) @RequestBody UserHomeBound user) throws Exception
    {
        return new ResponseEntity(userService.authenticateLaboratory(user), HttpStatus.OK);
    }

    //--------- Verifica las licencias de las aplicaciones externas
    @ApiMethod(
            description = " verifica las licencias de las aplicaciones",
            path = "/api/authentication/licenses",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = License.class)
    @RequestMapping(value = "/licenses", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<License>> licenses() throws Exception
    {
        List<License> licenses = licenseService.licenses();
        if (licenses.size() < 1)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(licenses, HttpStatus.OK);
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
    ) throws Exception
    {
        return new ResponseEntity(userService.integrationAuthentication(user), HttpStatus.OK);
    }
}

package net.cltech.enterprisent.controllers.masters.user;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.ExcludeTest;
import net.cltech.enterprisent.domain.masters.user.Email;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.masters.user.UserAnalyzer;
import net.cltech.enterprisent.domain.masters.user.UserByBranchByAreas;
import net.cltech.enterprisent.domain.masters.user.UserIntegration;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
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
 * Servicios para el acceso a la informacion del maestro Usuarios
 *
 * @version 1.0.0
 * @author cmartin
 * @since 11/05/2017
 * @see Creacion
 *
 * @author cmartin
 * @version 1.0.0
 * @since 12/05/2017
 * @see Se agregaron metodos para el funcionamiento maestro usuario.
 */
@Api(
        name = "Usuario",
        group = "Usuario",
        description = "Servicios de informacion del maestro Usuarios"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/users")
public class UserController
{

    @Autowired
    private UserService userService;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los usuarios registrados",
            path = "/api/users",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> list() throws Exception
    {
        List<User> list = userService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista de usuarios con id y fecha de ultimo ingreso al sistema, para verifiacion de estado",
            path = "/api/users/simplelist",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/simplelist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> SimpleUserList() throws Exception
    {
        List<User> list = userService.SimpleUserList();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista de usuarios con id y fecha de ultimo ingreso al sistema, para verifiacion de estado",
            path = "/api/users/listdeactivate",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/listdeactivate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> listDeactivate() throws Exception
    {
        List<User> list = userService.listDeactivate();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR POR ESTADO ----------------
    @ApiMethod(
            description = "Lista los usuarios registrados por estado",
            path = "/api/users/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<User> list = userService.list(state);
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
            description = "Obtiene la informacion de un usuario",
            path = "/api/users/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getById(
            @ApiPathParam(name = "id", description = "Id del usuario") @PathVariable(name = "id") int id
    ) throws Exception
    {
        User user = userService.get(id, null, null, null);
        if (user == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR USUARIO ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un usuario",
            path = "/api/users/filter/username/{username}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/filter/username/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getByUsername(
            @ApiPathParam(name = "username", description = "Usuario") @PathVariable(name = "username") String username
    ) throws Exception
    {
        User user = userService.get(null, username, null, null);
        if (user == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    //------------ CONSULTA si el username tiene permisos para agregar examenes en ingreso de ordenes ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un usuario",
            path = "/api/users/filter/username/{username}/password/{password}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/filter/username/{username}/password/{password}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUpdateTestEntry(
            @ApiPathParam(name = "username", description = "Usuario") @PathVariable(name = "username") String username,
            @ApiPathParam(name = "password", description = "password") @PathVariable(name = "password") String password
    ) throws Exception
    {
        User user = userService.getUpdateTestEntry(username, password);
        if (user == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR IDENTIFICACION ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un usuario",
            path = "/api/users/filter/identification/{identification}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/filter/identification/{identification}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getByidentification(
            @ApiPathParam(name = "identification", description = "Identificacion del usuario") @PathVariable(name = "identification") String identification
    ) throws Exception
    {
        User user = userService.get(null, null, identification, null);
        if (user == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR CODIGO FIRMA ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un usuario",
            path = "/api/users/filter/signaturecode/{signaturecode}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/filter/signaturecode/{signaturecode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getBySignatureCode(
            @ApiPathParam(name = "signaturecode", description = "Codigo firma del usuario") @PathVariable(name = "signaturecode") String signatureCode
    ) throws Exception
    {
        User user = userService.get(null, null, null, signatureCode);
        if (user == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un nuevo usuario",
            path = "/api/users",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(
            @ApiBodyObject(clazz = User.class) @RequestBody User user
    ) throws Exception
    {
        return new ResponseEntity<>(userService.create(user), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un usuario",
            path = "/api/users",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> update(
            @ApiBodyObject(clazz = User.class) @RequestBody User user
    ) throws Exception
    {
        return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Desactivar un usuario",
            path = "/api/users/deactivate",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/deactivate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> deactivateUser(
            @ApiBodyObject(clazz = User.class) @RequestBody User user
    ) throws Exception
    {
        user.setState(false);
        return new ResponseEntity<>(userService.changeStateUser(user), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Cambiar estado de un usuario",
            path = "/api/users/changestate",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/changestate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> changeState(
            @ApiBodyObject(clazz = User.class) @RequestBody User user
    ) throws Exception
    {
        return new ResponseEntity<>(userService.changeStateUser(user), HttpStatus.OK);
    }

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los examenes de un usuario",
            path = "/api/users/exclude/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ExcludeTest.class)
    @RequestMapping(value = "/exclude/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ExcludeTest>> listTest(
            @ApiPathParam(name = "id", description = "Id del usuario") @PathVariable(name = "id") Integer id
    ) throws Exception
    {
        List<ExcludeTest> list = userService.listTest(id);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Insertar examenes",
            path = "/api/users/exclude/",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "exclude/", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertTest(
            @ApiBodyObject(clazz = ExcludeTest.class) @RequestBody List<ExcludeTest> tests
    ) throws Exception
    {
        return new ResponseEntity<>(userService.insertTest(tests), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Elimina los examenes",
            path = "/api/users/exclude/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/exclude/id/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteTest(
            @ApiPathParam(name = "id", description = "Id del usuario") @PathVariable(name = "id") Integer id
    ) throws Exception
    {
        return new ResponseEntity<>(userService.deleteTest(id), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Enviar un correo",
            path = "/api/users/email",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/email", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendEmail(
            @ApiBodyObject(clazz = Email.class) @RequestBody Email email
    ) throws Exception
    {
        return new ResponseEntity<>(userService.sendEmail(email), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtener los usuarios asociados a una sede y a multiples areas",
            path = "/api/users/getbybranchareas",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/getbybranchareas", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getByBranchAreas(
            @ApiBodyObject(clazz = UserByBranchByAreas.class) @RequestBody UserByBranchByAreas filter
    ) throws Exception
    {
        List<User> list = userService.getByBranchAreas(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un nuevo usuario para integracion o analizador",
            path = "/api/users/bytype",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/bytype", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createByType(
            @ApiBodyObject(clazz = User.class) @RequestBody User user
    ) throws Exception
    {
        return new ResponseEntity<>(userService.createByType(user), HttpStatus.OK);
    }

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Crea un nuevo usuario para integracion para la interfaz",
            path = "/api/users/integration/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = UserIntegration.class)
    @RequestMapping(value = "/integration/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserIntegration> userIntegration(
            @ApiPathParam(name = "code", description = "Codigo de usuario") @PathVariable(name = "code") String code
    ) throws Exception
    {
        UserIntegration user = userService.userIntegration(code);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un usuario para integracion o analizador",
            path = "/api/users/bytype",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/bytype", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateByType(
            @ApiBodyObject(clazz = User.class) @RequestBody User user
    ) throws Exception
    {
        return new ResponseEntity<>(userService.updateByType(user), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene una lista con los usuarios de analizadores con destino en microbiologia",
            path = "/api/users/getUsersAnalyzers",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getUsersAnalyzers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserAnalyzer>> getUsersAnalyzers() throws Exception
    {
        List<UserAnalyzer> listUsers = userService.getUsersAnalyzers();
        if (!listUsers.isEmpty())
        {
            return new ResponseEntity<>(listUsers, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}

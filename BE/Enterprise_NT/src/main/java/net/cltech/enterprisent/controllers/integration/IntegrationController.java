package net.cltech.enterprisent.controllers.integration;

import java.util.List;
import net.cltech.enterprisent.domain.common.AuthenticationUser;
import net.cltech.enterprisent.domain.common.JWTToken;
import net.cltech.enterprisent.domain.integration.epidemiology.EpidemiologicalEvents;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMinsaService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationService;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
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
        name = "Servicios Generales",
        group = "Integración",
        description = "Servicios de generales de integración con otras aplicaciones"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/integration")
public class IntegrationController
{

    @Autowired
    private UserService userService;
    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private IntegrationMinsaService integrationMinsaService;

    @ApiMethod(
            description = "Realiza la autenticación del usuario para la integración con otros sistemas, esta funcion retorna un token de sesion el cual se debe invocar en cada peticion al servidor",
            path = "/api/integration/authentications/laboratory",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponseObject(clazz = JWTToken.class)
    @RequestMapping(value = "/authentications/laboratory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTToken> authentication(
            @ApiBodyObject(clazz = AuthenticationUser.class) @RequestBody AuthenticationUser user
    ) throws Exception
    {
        user.setBranch(null);
        return new ResponseEntity(userService.authenticate(user), HttpStatus.OK);
    }

    //------------ LISTAR USUARIO ACTIVOS ----------------
    @ApiMethod(
            description = "Lista los usuarios registrados",
            path = "/api/integration/users",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> list() throws Exception
    {
        List<User> list = integrationService.listUser();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR SEDES ACTIVAS ----------------
    @ApiMethod(
            description = "Lista las sedes",
            path = "/api/integration/branches",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = User.class)
    @RequestMapping(value = "/branches", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Branch>> listBranches() throws Exception
    {
        List<Branch> list = integrationService.listBranches();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ LISTAR PRUEBAS POR AREA EPIDEMIOLOGICA ----------------
    @ApiMethod(
            description = "Lista las pruebas con entrevista por area epidemiologica",
            path = "/api/integration/getTestEpidemiology",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = EpidemiologicalEvents.class)
    @RequestMapping(value = "/getTestEpidemiology", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EpidemiologicalEvents>> epidemiologicalEvents() throws Exception
    {
        List<EpidemiologicalEvents> list = integrationMinsaService.epidemiologicalEvents();
        if (list == null || list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }


}

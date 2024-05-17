package net.cltech.enterprisent.controllers.integration;

import net.cltech.enterprisent.domain.common.AuthenticationUser;
import net.cltech.enterprisent.domain.common.JWTToken;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
import net.cltech.enterprisent.tools.log.stadistics.StadisticsLog;
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
 * Controlador de acceso a los servicios de autenticacion
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 13/07/2020
 * @see Creación
 */
@Api(
        name = "Servicios Generales",
        group = "Integración",
        description = "Servicios de generales de integración con otras aplicaciones"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/authentications")
public class IntegrationSkyLabController {

    @Autowired
    private UserService userService;
   

    //------------ AUTENTICACION DE USUARIO DESDE EL SKY LAB ----------------
    @ApiMethod(
            description = "Realiza la autenticación del usuario para la integración con otros sistemas, esta funcion retorna un token de sesion el cual se debe invocar en cada peticion al servidor",
            path = "/api/authentications/laboratory",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponseObject(clazz = JWTToken.class)
    @RequestMapping(value = "/laboratory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTToken> authentication(
            @ApiBodyObject(clazz = AuthenticationUser.class) @RequestBody AuthenticationUser user
    ) throws Exception {
        user.setBranch(null);
        return new ResponseEntity(userService.authenticate(user), HttpStatus.OK);
    }
    
    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.controllers.masters.configuration;

import java.util.List;
import net.cltech.outreach.domain.masters.configuration.UserType;
import net.cltech.outreach.service.interfaces.masters.configuration.UserTypeService;
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
 * Servicios REST para configuracion de los tipos de usuario
 *
 * @version 1.0.0
 * @author cmartin
 * @since 23/04/2018
 * @see Creacion
 */
@Api(
        group = "Configuración",
        name = "Tipos de Usuario",
        description = "Servicios sobre los tipos de usuario"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/usertypes")
public class UserTypeController
{

    @Autowired
    private UserTypeService userTypeService;

    //-----------------LISTAR-----------------
    @ApiMethod(
            description = "Obtiene los tipos de usuario de la aplicacion",
            path = "/api/usertypes",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = UserType.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserType>> list() throws Exception
    {
        List<UserType> list = userTypeService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //-----------------ACTUALIZAR-----------------
    @ApiMethod(
            description = "aqctualiza los tipos de usuario de la aplicacion",
            path = "/api/usertypes",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = UserType.class)
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> update(@ApiBodyObject(clazz = UserType.class) @RequestBody List<UserType> userTypes) throws Exception
    {
        int quantity = userTypeService.update(userTypes);
        return new ResponseEntity<>(quantity, HttpStatus.OK);
    }
}

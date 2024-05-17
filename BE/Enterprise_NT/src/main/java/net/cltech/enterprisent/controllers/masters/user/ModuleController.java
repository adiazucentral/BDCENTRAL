/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.user;

import java.util.List;
import net.cltech.enterprisent.domain.masters.user.Module;
import net.cltech.enterprisent.service.interfaces.masters.user.ModuleService;
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
 * Servicios para el acceso a la informacion del maestro de modulos
 *
 * @version 1.0.0
 * @author enavas
 * @since 26/05/2017
 * @see Creacion
 */
@Api(
        name = "Modulos",
        group = "Usuario",
        description = "Servicios de informacion del maestro Modulos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/modules")

public class ModuleController
{

    @Autowired
    private ModuleService moduleService;

    @ApiMethod(
            description = "Lista los modulos registrados",
            path = "/api/modules",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Module.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Module>> list() throws Exception
    {
        List<Module> list = moduleService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Lista los modulos a los que tienen acceso el usuario",
            path = "/api/modules/filter/user/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Module.class)
    @RequestMapping(value = "/filter/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Module>> filterByUser(
            @ApiPathParam(name = "id", description = "Id del usuario") @PathVariable(name = "id") int id
    ) throws Exception
    {
        List<Module> list = moduleService.filterByUser(id);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

}

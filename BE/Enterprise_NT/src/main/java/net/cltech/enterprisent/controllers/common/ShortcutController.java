package net.cltech.enterprisent.controllers.common;

import java.util.List;
import net.cltech.enterprisent.domain.access.Shortcut;
import net.cltech.enterprisent.domain.masters.user.Module;
import net.cltech.enterprisent.service.interfaces.common.ShortcutService;
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
 * Servicios para el acceso a la informacion de shortcuts
 *
 * @version 1.0.0
 * @author eacuna
 * @since 19/19/2017
 * @see Creacion
 */
@Api(
        name = "Acceso Directo",
        group = "Común",
        description = "Servicios de informacion a shortcuts"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/shortcuts")

public class ShortcutController
{

    @Autowired
    private ShortcutService service;

    @ApiMethod(
            description = "Lista accesos directos de un usuario",
            path = "/api/shortcuts/filter/user/{user}/module/{module}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Module.class)
    @RequestMapping(value = "/filter/user/{user}/module/{module}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Module>> list(
            @ApiPathParam(name = "user", description = "Id del usuario") @PathVariable(name = "user") int user,
            @ApiPathParam(name = "module", description = "Id módulo (LIS,Billing)<br>Enviar 0 para consultar todos los módulos") @PathVariable(name = "module") int module
    ) throws Exception
    {
        List<Module> list = service.list(user, module);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Agrega acceso directo al usuario",
            path = "/api/shortcuts",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> create(@ApiBodyObject(clazz = Shortcut.class) @RequestBody Shortcut shortcut) throws Exception
    {
        return new ResponseEntity<>(service.add(shortcut), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Elimina acceso directo del usuario",
            path = "/api/shortcuts",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> delete(@ApiBodyObject(clazz = Shortcut.class) @RequestBody Shortcut shortcut) throws Exception
    {
        return new ResponseEntity<>(service.delete(shortcut), HttpStatus.OK);
    }
}

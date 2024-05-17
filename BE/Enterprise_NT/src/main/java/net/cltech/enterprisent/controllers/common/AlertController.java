package net.cltech.enterprisent.controllers.common;

import java.util.List;
import net.cltech.enterprisent.domain.common.Alert;
import net.cltech.enterprisent.service.interfaces.common.AlertService;
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
 * Servicios para el acceso a la informacion de alertas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 19/19/2017
 * @see Creacion
 * ///////
 */
@Api(
        name = "Alerta",
        group = "Común",
        description = "Servicios de informacion alertas del sistema"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/alerts")
public class AlertController
{

    @Autowired
    private AlertService service;

    @ApiMethod(
            description = "Lista alertas de un formulario",
            path = "/api/alerts/{form}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Alert.class)
    @RequestMapping(value = "/{form}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Alert>> list(
            @ApiPathParam(name = "form", description = "Id del formulario") @PathVariable(name = "form") String form
    ) throws Exception
    {
        List<Alert> list = service.list(form);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Agrega alerta",
            path = "/api/alerts",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> create(@ApiBodyObject(clazz = Alert.class) @RequestBody Alert alert) throws Exception
    {
        return new ResponseEntity<>(service.add(alert), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Elimina acceso directo del usuario",
            path = "/api/alerts",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> delete(@ApiBodyObject(clazz = Alert.class) @RequestBody Alert alert) throws Exception
    {
        return new ResponseEntity<>(service.delete(alert), HttpStatus.OK);
    }
}

package net.cltech.enterprisent.controllers.operation.orders;

import net.cltech.enterprisent.service.interfaces.operation.orders.ConcurrencyService;
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
 * Controlador de servicios Rest sobre la verificacion de la muestra
 *
 * @version 1.0.0
 * @author eacuna
 * @since 02/11/2017
 * @see Creacion
 */
@Api(
        name = "Concurrencias",
        group = "Operación - Ordenes",
        description = "Servicios Rest sobre concurrencia de orden e historia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/concurrencies")
public class ConcurrencyController
{

    @Autowired
    private ConcurrencyService service;

    @ApiMethod(
            description = "Realizar Borrado de toda la concurrencia",
            path = "/api/concurrencies",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> delete() throws Exception
    {
        return new ResponseEntity<>(service.deleteAll(), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Realizar Borrado de la concurrencia por la orden",
            path = "/api/concurrencies/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/order/{order}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteOrder(
            @ApiPathParam(name = "order", description = "Número de la orden") @PathVariable(name = "order") Long order
    ) throws Exception
    {
        return new ResponseEntity<>(service.deleteOrder(order), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Realizar Borrado de la concurrencia por la historía",
            path = "/api/concurrencies/record/{type}/{record}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/record/{type}/{record}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteRecord(
            @ApiPathParam(name = "type", description = "Tipo de documento") @PathVariable(name = "type") Integer type,
            @ApiPathParam(name = "record", description = "Historia del paciente") @PathVariable(name = "record") String record
    ) throws Exception
    {
        return new ResponseEntity<>(service.deleteRecord(type, record), HttpStatus.OK);
    }
}

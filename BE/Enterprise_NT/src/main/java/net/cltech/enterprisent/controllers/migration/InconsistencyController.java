package net.cltech.enterprisent.controllers.migration;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.migration.Inconsistency;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.service.interfaces.migration.InconsistencyService;
import net.cltech.enterprisent.tools.JWT;
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
 * Controlador de servicios Rest sobre las inconsistencias
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/11/2017
 * @see Creacion
 */
@Api(
        name = "Inconsistencia",
        group = "Migración",
        description = "Servicios Rest sobre las Inconsistencias"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/inconsistencies")
public class InconsistencyController
{

    @Autowired
    private InconsistencyService inconsistencyService;

    //--------------INGRESO DE ORDEN---------------
    @ApiMethod(
            description = "Crea una orden en el sistema enviada desde una interfaz",
            path = "/api/inconsistencies/order",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/order", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> create(
            @ApiBodyObject(clazz = Order.class) @RequestBody Order order,
            HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(inconsistencyService.create(order, JWT.decode(request).getId()), HttpStatus.OK);
    }

    //------------ CONSULTAR INCONSISTENCIAS POR FECHA ----------------
    @ApiMethod(
            description = "Obtiene la información de una orden.",
            path = "/api/inconsistencies/init/{init}/end/{end}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Inconsistency.class)
    @RequestMapping(value = "/init/{init}/end/{end}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Inconsistency>> getInconsistencies(
            @ApiPathParam(name = "init", description = "Fecha Inicial") @PathVariable(name = "init") long init,
            @ApiPathParam(name = "end", description = "Fecha Final") @PathVariable(name = "end") long end
    ) throws Exception
    {
        List<Inconsistency> list = inconsistencyService.list(new Date(init), new Date(end));
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ RESOLVER INCONSISTENCIA ----------------
    @ApiMethod(
            description = "Resuelve las inconsistencias de la orden.",
            path = "/api/inconsistencies/resolve/order/{order}/lis/{lis}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Inconsistency.class)
    @RequestMapping(value = "/resolve/order/{order}/lis/{lis}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> resolveInconsistency(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order,
            @ApiPathParam(name = "lis", description = "Se resuelve a favor del LIS") @PathVariable(name = "lis") boolean lis
    ) throws Exception
    {
        return new ResponseEntity<>(inconsistencyService.resolveInconsistency(order, lis), HttpStatus.OK);
    }

    //------------ CONSULTAR INCONSISTENCIA POR ORDEN ----------------
    @ApiMethod(
            description = "Consulta la inconsistencia de la orden.",
            path = "/api/inconsistencies/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Inconsistency.class)
    @RequestMapping(value = "/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Inconsistency> resolveInconsistency(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order
    ) throws Exception
    {
        Inconsistency resolve = inconsistencyService.getByOrderId(order);
        if (resolve != null)
        {
            return new ResponseEntity<>(resolve, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}

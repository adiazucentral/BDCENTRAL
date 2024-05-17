/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.operation.orders;

import java.util.List;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.operation.common.Reason;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderBasic;
import net.cltech.enterprisent.service.interfaces.operation.orders.SpecialDeleteService;
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
 * Controlador de servicios Rest sobre la verificacion de la muestra
 *
 * @version 1.0.0
 * @author cmartin
 * @since 19/09/2017
 * @see Creacion
 */
@Api(
        name = "Borrados Especiales",
        group = "Operación - Ordenes",
        description = "Servicios Rest sobre los borrados especiales"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/specialdeletes")
public class SpecialDeleteController
{

    @Autowired
    private SpecialDeleteService service;

    //------------ BORRADOS ESPECIALES ----------------
    @ApiMethod(
            description = "Realizar Borrados Especiales por un Rango de Ordenes",
            path = "/api/specialdeletes",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Destination.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> specialDelete(
            @ApiBodyObject(clazz = Reason.class) @RequestBody Reason reason
    ) throws Exception
    {
        List<Order> list = service.specialDelete(reason);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

     //------------ BORRADOS ESPECIALES ----------------
    @ApiMethod(
            description = "Consulta Ordenes Afectadas por Borrados Especiales",
            path = "/api/specialdeletes/query",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Reason.class)
    @RequestMapping(value = "/query", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderBasic>> specialDeleteQuery(
            @ApiBodyObject(clazz = Reason.class) @RequestBody Reason reason
    ) throws Exception
    {
        return new ResponseEntity<>(service.specialDeleteTest(reason), HttpStatus.OK);
    }
    
        //------------ BORRADOS ESPECIALES ----------------
    @ApiMethod(
            description = "Realizar Borrados Especiales por un numero de orden",
            path = "/api/specialdeletes/specialdeletesbyorder",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Reason.class)
    @RequestMapping(value = "/specialdeletesbyorder",method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> specialDeletebyorder(
            @ApiBodyObject(clazz = Reason.class) @RequestBody Reason reason
    ) throws Exception
    {
        Integer list = service.specialDeletebyorder(reason);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}

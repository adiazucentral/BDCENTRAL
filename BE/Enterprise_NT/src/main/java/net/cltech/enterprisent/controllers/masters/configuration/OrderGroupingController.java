/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.configuration;

import java.util.List;
import net.cltech.enterprisent.domain.masters.configuration.OrderGrouping;
import net.cltech.enterprisent.service.interfaces.masters.configuration.OrderGroupingService;
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
 * Servicios REST para configuracion general de la agrupacion de ordenes
 *
 * @version 1.0.0
 * @author equijano
 * @since 18/03/2019
 * @see Creacion
 */
@Api(
        group = "Configuración",
        name = "Agrupacion de orden",
        description = "Servicios sobre la Configuracion de la agrupacion de la orden"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/ordergrouping")
public class OrderGroupingController
{

    @Autowired
    private OrderGroupingService orderGroupingService;

    @ApiMethod(
            description = "Obtiene todos los elementos agrupados",
            path = "/api/ordergrouping",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderGrouping.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderGrouping>> listGroup() throws Exception
    {
        List<OrderGrouping> list = orderGroupingService.list();
        if (!list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Guarda la configuracion de la agrupacion de la orden",
            path = "/api/ordergrouping",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertGroups(
            @ApiBodyObject(clazz = OrderGrouping.class) @RequestBody(required = true) List<OrderGrouping> list
    ) throws Exception
    {
        return new ResponseEntity<>(orderGroupingService.insertGroups(list), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Elimina la configuracion de la agrupacion de la orden",
            path = "/api/ordergrouping",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteGroups() throws Exception
    {
        orderGroupingService.deleteGroups();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

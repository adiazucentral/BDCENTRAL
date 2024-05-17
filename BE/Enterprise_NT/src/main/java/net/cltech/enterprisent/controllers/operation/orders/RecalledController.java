/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.operation.orders;

import java.util.List;
import net.cltech.enterprisent.domain.operation.orders.Recalled;
import net.cltech.enterprisent.service.interfaces.operation.orders.RecalledService;
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
 * Controlador de servicios Rest sobre rellamado
 *
 * @version 1.0.0
 * @author equijano
 * @since 16/08/2019
 * @see Creacion
 */
@Api(
        name = "Re llamado",
        group = "Operación - Ordenes",
        description = "Servicios Rest sobre ordenes"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/recalled")
public class RecalledController
{

    @Autowired
    private RecalledService recalledService;

    @ApiMethod(
            description = "Obtiene la lista de ordenes rellamadas",
            path = "/api/recalled",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Recalled.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Recalled>> list() throws Exception
    {
        List<Recalled> list = recalledService.list();
        if (!list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Crea un registro de rellamado de la orden",
            path = "/api/recalled",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Recalled.class)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recalled> create(
            @ApiBodyObject(clazz = Recalled.class) @RequestBody Recalled recalled
    ) throws Exception
    {
        return new ResponseEntity<>(recalledService.create(recalled), HttpStatus.OK);
    }

}

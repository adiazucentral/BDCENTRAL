/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.migration;

import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.OrderIngreso;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.RequestMigracion;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.ResponseNT;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.ResponseOrderNT;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.StatusServer;
import net.cltech.enterprisent.service.interfaces.migration.MigrationTsToNtService;
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
 * Controlador de servicios Rest sobre ordenes
 *
 * @version 1.0.0
 * @author hpoveda
 * @since 03/05/2022
 * @see Creacion
 */
@Api(
        name = "interfaz de ingreso TS a NT",
        group = "Migración",
        description = "Servicios Rest sobre ingreso de ordenes "
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/ingresoNT")

public class MigrationTStoNTController
{

    @Autowired
    private MigrationTsToNtService migrationTsToNtService;

    @ApiMethod(
            description = "Crea una orden enviada de un sistema ts",
            path = "/api/ingresoNT/orderNT",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK"
    )
    @ApiResponseObject(clazz = ResponseNT.class)
    @RequestMapping(value = "/orderNT", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseNT> create(
            @ApiBodyObject(clazz = OrderIngreso.class) @RequestBody OrderIngreso order,
            HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(migrationTsToNtService.createNT(order), HttpStatus.OK);
    }

    @ApiMethod(
            description = "obtiene una orden enviadapor un ainterface de ingreso",
            path = "/api/ingresoNT/getOrderNT",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK"
    )
    @ApiResponseObject(clazz = ResponseOrderNT.class)
    @RequestMapping(value = "/getOrderNT", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseOrderNT> getOrderNT(@RequestBody RequestMigracion requestMigracion) throws Exception
    {
        return new ResponseEntity<>(migrationTsToNtService.getOrderNT(requestMigracion), HttpStatus.OK);
    }

    @ApiMethod(
            description = "obtiene una orden del sitema ",
            path = "/api/ingresoNT/StatusNT",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK"
    )

    @ApiResponseObject(clazz = StatusServer.class)
    @RequestMapping(value = "/StatusNT", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusServer> getStatusNT() throws Exception
    {
        return new ResponseEntity<>(new StatusServer(), HttpStatus.OK);
    }

}

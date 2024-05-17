/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.tools;

import java.util.List;
import net.cltech.enterprisent.domain.tools.PrintLog;
import net.cltech.enterprisent.service.interfaces.tools.PrintLogService;
import org.jsondoc.core.annotation.Api;
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
 *
 * @author hpoveda
 */
@Api(
        name = "UpdateDateHistorics",
        group = "Herramientas",
        description = "Servicios actualiza las fechas de historicos cuando no aparecen"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/printLog")
public class PrintLogController
{

    @Autowired
    private PrintLogService service;

    //------------insertar ----------------
    @ApiMethod(
            description = "inserta log de cliente de impresion",
            path = "/api/printLog",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )

    @ApiResponseObject(clazz = PrintLog.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> insertPrint(@RequestBody PrintLog print) throws Exception
    {
        return new ResponseEntity<>(service.InsertPrint(print), HttpStatus.OK);
    }
    //------------listar ----------------

    @ApiMethod(
            description = "inserta log de cliente de impresion",
            path = "/api/printLog",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )

    @ApiResponseObject(clazz = PrintLog.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PrintLog>> list() throws Exception
    {
        return new ResponseEntity<>(service.list(), HttpStatus.OK);
    }

}

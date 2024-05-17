/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.test;

import net.cltech.enterprisent.domain.masters.test.TestAlarm;
import net.cltech.enterprisent.service.interfaces.masters.test.TestAlarmService;
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
 * Servicios para el acceso a la informacion del maestro Alarma de pruebas desde ingreso de ordenes
 *
 * @version 1.0.0
 * @author omendez
 * @since 16/12/2021
 * @see Creacion
 */
@Api(
        name = "Alarma Prueba",
        group = "Prueba",
        description = "Servicios de informacion del maestro Alarma de pruebas desde ingreso de ordenes"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/testalarm")
public class TestAlarmController 
{
    @Autowired
    private TestAlarmService testAlarmService;
    
    
    //------------ INSERTAR RELACION ----------------
    @ApiMethod(
            description = "Crea la relación de los examenes que deben ser alertados desde ingreso de ordenes",
            path = "/api/testalarm",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestAlarm.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestAlarm> create(
            @ApiBodyObject(clazz = TestAlarm.class) @RequestBody TestAlarm testAlarm
    ) throws Exception
    {
        return new ResponseEntity<>(testAlarmService.create(testAlarm), HttpStatus.OK);
    }
}

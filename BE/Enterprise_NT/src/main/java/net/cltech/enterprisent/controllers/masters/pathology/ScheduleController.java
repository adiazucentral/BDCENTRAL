/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Schedule;
import net.cltech.enterprisent.service.interfaces.masters.pathology.ScheduleService;
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
 * Servicios para la configuracion de agenda de patologos
 *
 * @version 1.0.0
 * @author omendez
 * @since 22/04/2021
 * @see Creacion
 */
@Api(
    group = "Patología",
    name = "Agenda",
    description = "Servicios para la configuracion de agenda de patologo"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/schedule")
public class ScheduleController 
{
    @Autowired
    private ScheduleService scheduleService;
    
    //------------ CONSULTA POR ID ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una agenda por patologo",
            path = "/api/pathology/schedule/filter/pathologist/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Schedule.class)
    @RequestMapping(value = "filter/pathologist/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Schedule>> getByPathologist(
            @ApiPathParam(name = "id", description = "Id del patologo") @PathVariable(name = "id") int id
    ) throws Exception
    {
        List<Schedule> list = scheduleService.getByPathologist(id);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea una nueva configuracion de agenda",
            path = "/api/pathology/schedule",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Schedule.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Schedule> create(
            @ApiBodyObject(clazz = Schedule.class) @RequestBody Schedule schedule
    ) throws Exception
    {
        return new ResponseEntity<>(scheduleService.create(schedule), HttpStatus.OK);
    }
    
    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar una configuración de agenda de patologos",
            path = "/api/pathology/schedule",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Schedule.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Schedule> update(
            @ApiBodyObject(clazz = Schedule.class) @RequestBody Schedule schedule
    ) throws Exception
    {
        return new ResponseEntity<>(scheduleService.update(schedule), HttpStatus.OK);
    }
    
    //-----------ELIMINAR --------
    @ApiMethod(
            description = "Elimina una configuracion de agenda de patologo por su id",
            path = "/api/pathology/schedule/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteMicroorganismAntibiotics(
            @ApiPathParam(name = "id", description = "Id de la configuración de la agenda") @PathVariable(name = "id") int id
    ) throws Exception
    {
        scheduleService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

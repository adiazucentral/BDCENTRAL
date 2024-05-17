/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.ProcessingTime;
import net.cltech.enterprisent.service.interfaces.masters.pathology.ProcessingTimeService;
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
 * Servicios para el maestro de horas de procesamiento de las muestras de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 13/07/2021
 * @see Creacion
 */
@Api(
    group = "Patología",
    name = "Horas Procesamiento",
    description = "Servicios de maestros de las horas de procesamiento de las muestras de patologia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/processingtime")
public class ProcessingTimeController 
{
    @Autowired
    private ProcessingTimeService processingTimeService;
    
    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista las horas de procesamiento registradas",
            path = "/api/pathology/processingtime",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ProcessingTime.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProcessingTime>> list() throws Exception
    {
        List<ProcessingTime> list = processingTimeService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ LISTAR POR ESTADO ----------------
    @ApiMethod(
            description = "Lista por estado las horas de procesamiento registradas",
            path = "/api/pathology/processingtime/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ProcessingTime.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProcessingTime>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") int state
    ) throws Exception
    {
        List<ProcessingTime> list = processingTimeService.list(state);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ CONSULTA POR ID ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una hora de procesamiento por id",
            path = "/api/pathology/processingtime/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ProcessingTime.class)
    @RequestMapping(value = "filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessingTime> getById(
            @ApiPathParam(name = "id", description = "Id de la hora de procesamiento") @PathVariable(name = "id") int id
    ) throws Exception
    {
        ProcessingTime processingTime = processingTimeService.get(id, null);
        if (processingTime == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(processingTime, HttpStatus.OK);
        }
    }
    
    //------------ CONSULTA POR HORA ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una hora de procesamiento por hora",
            path = "/api/pathology/processingtime/filter/time/{time}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ProcessingTime.class)
    @RequestMapping(value = "/filter/time/{time}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessingTime> getByTime(
            @ApiPathParam(name = "time", description = "") @PathVariable(name = "time") String time
    ) throws Exception
    {
        ProcessingTime processingTime = processingTimeService.get(null, time);
        if (processingTime == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(processingTime, HttpStatus.OK);
        }
    }
    
    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea una nueva hora de procesamiento",
            path = "/api/pathology/processingtime",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ProcessingTime.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessingTime> create(
            @ApiBodyObject(clazz = ProcessingTime.class) @RequestBody ProcessingTime processingTime
    ) throws Exception
    {
        return new ResponseEntity<>(processingTimeService.create(processingTime), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar una hora de procesamiento",
            path = "/api/pathology/processingtime",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ProcessingTime.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessingTime> update(
            @ApiBodyObject(clazz = ProcessingTime.class) @RequestBody ProcessingTime processingTime
    ) throws Exception
    {
        return new ResponseEntity<>(processingTimeService.update(processingTime), HttpStatus.OK);
    }
}

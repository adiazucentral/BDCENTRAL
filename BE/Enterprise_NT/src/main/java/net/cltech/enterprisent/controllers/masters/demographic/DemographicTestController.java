/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.DemographicTest;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicTestService;
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
 * Servicios para el acceso a la informacion del maestro Examenes por demografico
 *
 * @version 1.0.0
 * @author omendez
 * @since 01/02/2022
 * @see Creacion
 */
@Api(
        name = "Examenes por Demografico",
        group = "Demografico",
        description = "Servicios de informacion del maestro Examenes por demografico"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/demographictest")
public class DemographicTestController {
    
    @Autowired
    private DemographicTestService demographicTestService;
    
    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Obtiene la lista de tipo de estudios",
            path = "/api/demographictest",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED, 500 - INTERNAL SERVER ERROR "
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicTest.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicTest>> get() throws Exception
    {
        List<DemographicTest> list = demographicTestService.list();
        if (list != null && !list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    //------------ OBTENER POR ID ----------------
    @ApiMethod(
            description = "Obtiene los datos de una relación examenes por demograficos por id",
            path = "/api/demographictest/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicTest.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemographicTest> filterById(
            @ApiPathParam(name = "id", description = "Id de la relacion") @PathVariable(name = "id") int id
    ) throws Exception
    {
        DemographicTest demographic = demographicTestService.findById(id);
        if (demographic != null)
        {
            return new ResponseEntity<>(demographic, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea una nueva instancia de examenes por demograficos",
            path = "/api/demographictest",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicTest.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemographicTest> create(
            @ApiBodyObject(clazz = DemographicTest.class) @RequestBody DemographicTest demographic
    ) throws Exception
    {
        return new ResponseEntity<>(demographicTestService.create(demographic), HttpStatus.OK);
    }
    
    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualiza un tipo de estudio",
            path = "/api/demographictest",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicTest.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemographicTest> update(
            @ApiBodyObject(clazz = DemographicTest.class) @RequestBody(required = true) DemographicTest demographic
    ) throws Exception
    {
        return new ResponseEntity<>(demographicTestService.update(demographic), HttpStatus.OK);
    }
}

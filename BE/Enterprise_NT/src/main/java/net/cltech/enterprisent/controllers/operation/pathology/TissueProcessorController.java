/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.TissueProcessor;
import net.cltech.enterprisent.service.interfaces.operation.pathology.TissueProcessorService;
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
 * Servicios para el acceso a la información del proceso de los casetes de las muestras de un caso de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 19/07/2021
 * @see Creacion
 */
@Api(
    name = "Procesador",
    group = "Patología",
    description = "Servicios para el acceso a la información del procesador de los casetes de las muestras de un caso de patologia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/tissueprocessor")
public class TissueProcessorController 
{
    @Autowired
    private TissueProcessorService tissueProcessorService;
    
    //------------ CREAR ----------------
    @ApiMethod(
            description = "Registra los procesos de los casetes de las muestras de un caso de patologia",
            path = "/api/pathology/tissueprocessor",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TissueProcessor.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TissueProcessor>> create(
            @ApiBodyObject(clazz = TissueProcessor.class) @RequestBody List<TissueProcessor> list
    ) throws Exception
    {
        return new ResponseEntity<>(tissueProcessorService.create(list), HttpStatus.OK);
    }
}

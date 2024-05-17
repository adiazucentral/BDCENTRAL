/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.Macroscopy;
import net.cltech.enterprisent.service.interfaces.operation.pathology.MacrocopyService;
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
 * Servicios para el acceso a la información de las descripciones macroscopicas sobre los casos de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 16/06/2021
 * @see Creacion
 */
@Api(
    name = "Macroscopia",
    group = "Patología",
    description = "Servicios para el acceso a la información de las descripciones macroscopicas sobre los casos de patologia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/macroscopy")
public class MacroscopyController 
{
    @Autowired
    private MacrocopyService macrocopyService;
    
    //------------ CONSULTA POR CASO ----------------
    @ApiMethod(
            description = "Obtiene la descripcion macroscopica de un caso",
            path = "/api/pathology/macroscopy/filter/case/{idCase}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Macroscopy.class)
    @RequestMapping(value = "filter/case/{idCase}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Macroscopy> getByCase(
            @ApiPathParam(name = "idCase", description = "Id del caso") @PathVariable(name = "idCase") Integer idCase
    ) throws Exception
    {
        Macroscopy macroscopy = macrocopyService.getByCase(idCase);
        if (macroscopy == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(macroscopy, HttpStatus.OK);
        }
    }
    
    //------------ CREAR ----------------
    @ApiMethod(
        description = "Crea una nueva descripcion macroscopica de un caso de patologia",
        path = "/api/pathology/macroscopy",
        visibility = ApiVisibility.PUBLIC,
        verb = ApiVerb.POST,
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Macroscopy.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Macroscopy> create(
            @ApiBodyObject(clazz = Macroscopy.class) @RequestBody Macroscopy macroscopy
    ) throws Exception
    {
        return new ResponseEntity<>(macrocopyService.create(macroscopy), HttpStatus.OK);
    }
    
    //------------ ACTUALIZA UNA DESCRIPCION MACROSCOPICA DE PATOLOGIA ----------------
    @ApiMethod(
            description = "Actualiza una descripcion macroscopica en el sistema",
            path = "/api/pathology/macroscopy",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Macroscopy.class)
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Macroscopy> update(@ApiBodyObject(clazz = Macroscopy.class) @RequestBody Macroscopy macroscopy
    ) throws Exception
    {
        return new ResponseEntity<>(macrocopyService.update(macroscopy), HttpStatus.OK);
    }
    
    //------------ BUSCAR DESCRIPCIONES PENDIENTES DE TRANSCRIPCION ----------------
    @ApiMethod(
            description = "Busca descripciones pendientes de transcripción",
            path = "/api/pathology/macroscopy/filter/transcription",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Macroscopy.class)
    @RequestMapping(value = "/filter/transcription", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Macroscopy>> getPendingTranscripts() throws Exception
    {
        List<Macroscopy> records = macrocopyService.getPendingTranscripts();
        if (!records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    //------------ INSERTA UNA DESCRIPCION DE UNA DESCRIPCION MACROSCOPICA DE PATOLOGIA ----------------
    @ApiMethod(
            description = "Inserta la transcripcion de una descripcion macroscopica en el sistema",
            path = "/api/pathology/macroscopy/transcription",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Macroscopy.class)
    @RequestMapping(value = "/transcription", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Macroscopy> transcript(@ApiBodyObject(clazz = Macroscopy.class) @RequestBody Macroscopy macroscopy
    ) throws Exception
    {
        return new ResponseEntity<>(macrocopyService.transcript(macroscopy), HttpStatus.OK);
    }
    
    //------------ BUSCAR DESCRIPCIONES PENDIENTES DE AUTORIZACION ----------------
    @ApiMethod(
            description = "Busca descripciones por usuario pendientes de autorizacion",
            path = "/api/pathology/macroscopy/filter/authorization/{idUser}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Macroscopy.class)
    @RequestMapping(value = "filter/authorization/{idUser}",  method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Macroscopy>> getPendingAuthorizations(
        @ApiPathParam(name = "idUser", description = "Id del usuario") @PathVariable(name = "idUser") Integer idUser
    ) throws Exception
    {
        List<Macroscopy> records = macrocopyService.getPendingAuthorizations(idUser);
        if (!records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    //------------ INSERTA UNA AUTORIZACION DE UNA TRANSCRIPCION DE UNA DESCRIPCION MACROSCOPICA DE PATOLOGIA ----------------
    @ApiMethod(
            description = "Inserta la autorizacion de la transcripcion de una descripcion macroscopica en el sistema",
            path = "/api/pathology/macroscopy/authorization",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Macroscopy.class)
    @RequestMapping(value = "/authorization", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Macroscopy> authorization(@ApiBodyObject(clazz = Macroscopy.class) @RequestBody Macroscopy macroscopy
    ) throws Exception
    {
        return new ResponseEntity<>(macrocopyService.authorization(macroscopy), HttpStatus.OK);
    }
}

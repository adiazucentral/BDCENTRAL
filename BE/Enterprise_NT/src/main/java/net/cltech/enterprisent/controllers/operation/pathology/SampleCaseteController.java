/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.FilterPathology;
import net.cltech.enterprisent.domain.operation.pathology.SampleCasete;
import net.cltech.enterprisent.service.interfaces.operation.pathology.SampleCaseteService;
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
 * Servicios para el acceso a la información de los casetes de las muestras de un caso de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 05/05/2021
 * @see Creacion
 */
@Api(
    name = "Casetes Muestras",
    group = "Patología",
    description = "Servicios para el acceso a la información de los casetes de las muestras de un caso de patologia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/samples/casetes")

public class SampleCaseteController 
{
    @Autowired
    private SampleCaseteService sampleCaseteService;
    
    //------------ BUSCAR POR MUESTRA Y CASO ----------------
    @ApiMethod(
            description = "Busca los casetes de las muestras de un caso de patologia",
            path = "/api/pathology/samples/casetes/{sample}/{case}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SampleCasete.class)
    @RequestMapping(value = "/{sample}/{case}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SampleCasete>> getByEntryDate(
            @ApiPathParam(name = "sample", description = "Id de la muestra") @PathVariable("sample") int sample,
            @ApiPathParam(name = "case", description = "Id del caso") @PathVariable("case") int idCase
    ) throws Exception
    {
        List<SampleCasete> records = sampleCaseteService.getBySample(sample, idCase);
        if (!records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    //------------ CREAR ----------------
    @ApiMethod(
            description = "Registra los casetes de las muestras de un caso de patologia",
            path = "/api/pathology/samples/casetes",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SampleCasete.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SampleCasete>> create(
            @ApiBodyObject(clazz = SampleCasete.class) @RequestBody List<SampleCasete> samples
    ) throws Exception
    {
        return new ResponseEntity<>(sampleCaseteService.create(samples), HttpStatus.OK);
    }
    
    
    //------------ OBTIENE UNA LISTA DE CASETES DE CASOS DE PATOLOGIA POR FILTROS ----------------
    @ApiMethod(
            description = "Obtiene una lista de casetes de casos por filtros",
            path = "/api/pathology/samples/casetes/filter/cases",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SampleCasete.class)
    @RequestMapping(value = "/filter/cases", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SampleCasete>> getCasetesByFilterCases(
            @ApiBodyObject(clazz = FilterPathology.class) @RequestBody FilterPathology filter
    ) throws Exception
    {
        List<SampleCasete> list = sampleCaseteService.getCasetesByFilterCases(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ CAMBIAR ESTADO DE UN CASETE ----------------
    @ApiMethod(
            description = "Cambiar el estado de un casete",
            path = "/api/pathology/samples/casetes/status",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SampleCasete.class)
    @RequestMapping(value = "/status", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SampleCasete>> changeStatus(
        @ApiBodyObject(clazz = SampleCasete.class) @RequestBody List<SampleCasete> casetes
    ) throws Exception
    {
        List<SampleCasete> list = sampleCaseteService.changeStatus(casetes);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.masters.pathology.Study;
import net.cltech.enterprisent.service.interfaces.masters.pathology.SpecimenService;
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
 * Servicios para el maestro de especimen de patología
 *
 * @version 1.0.0
 * @author etoro
 * @since 08/10/2020
 * @see Creacion
 */
@Api(
        group = "Patología",
        name = "Especimen",
        description = "Servicios de maestros de los especimenes de patologia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/specimen")
public class SpecimenController {

    @Autowired
    private SpecimenService specimenService;
    
    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los especimenes de patologia",
            path = "/api/pathology/specimen",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Specimen.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Specimen>> list() throws Exception
    {
        List<Specimen> list = specimenService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTA SUBMUESTRAS ----------------
    @ApiMethod(
            description = "Obtiene la lista de submuestras de un especimen",
            path = "/api/pathology/specimen/subsamples/{specimenid}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Specimen.class)
    @RequestMapping(value = "/subsamples/{specimenid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Specimen>> getSubSamples(
            @ApiPathParam(name = "specimenid", description = "Id especimen padre") @PathVariable(name = "specimenid") int specimenId
    ) throws Exception
    {
        List<Specimen> list = specimenService.subSamples(specimenId);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Asigna submuestras a un especimen de patologia",
            path = "/api/pathology/specimen/subsamples",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Specimen.class)
    @RequestMapping(value = "/subsamples", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> assignSubSamples(
            @ApiBodyObject(clazz = Specimen.class) @RequestBody Specimen specimen
    ) throws Exception
    {
        return new ResponseEntity<>(specimenService.assignSubSamples(specimen), HttpStatus.OK);
    }
    
    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los examenes con muestras de patologia",
            path = "/api/pathology/specimen/study",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Study.class)
    @RequestMapping(value = "/study", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Study>> studies() throws Exception
    {
        List<Study> list = specimenService.studies();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
}

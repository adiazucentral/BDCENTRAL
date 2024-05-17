/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Field;
import net.cltech.enterprisent.domain.masters.pathology.MacroscopyTemplate;
import net.cltech.enterprisent.service.interfaces.masters.pathology.MacroscopyTemplateService;
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
 * Servicios para el maestro para las plantillas de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 09/06/2021
 * @see Creacion
 */
@Api(
    group = "Patología",
    name = "Plantillas",
    description = "Servicios de maestros para las plantillas de patologia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/template")
public class MacroscopyTemplateController 
{
    @Autowired
    private MacroscopyTemplateService macroscopyTemplateService;
    
    //------------ LISTA CAMPOS ----------------
    @ApiMethod(
            description = "Obtiene la lista de campos de una plantilla de un especimen",
            path = "/api/pathology/template/fields/{specimenid}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Field.class)
    @RequestMapping(value = "/fields/{specimenid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Field>> getFields(
            @ApiPathParam(name = "specimenid", description = "Id especimen") @PathVariable(name = "specimenid") int specimenId
    ) throws Exception
    {
        List<Field> list = macroscopyTemplateService.fields(specimenId);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ ASIGNA CAMPOS ----------------
    @ApiMethod(
            description = "Asigna campos a una muestra",
            path = "/api/pathology/template",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MacroscopyTemplate.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> assignFields(
            @ApiBodyObject(clazz = MacroscopyTemplate.class) @RequestBody MacroscopyTemplate template
    ) throws Exception
    {
        return new ResponseEntity<>(macroscopyTemplateService.assignFields(template), HttpStatus.OK);
    }
    
    //------------ LISTA PLANTILLAS ----------------
    @ApiMethod(
            description = "Obtiene la lista de plantillas de los especimenes de un caso",
            path = "/api/pathology/template/filter/case/{caseid}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MacroscopyTemplate.class)
    @RequestMapping(value = "/filter/case/{caseid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MacroscopyTemplate>> templates(
            @ApiPathParam(name = "caseid", description = "Id del caso") @PathVariable(name = "caseid") int caseId
    ) throws Exception
    {
        List<MacroscopyTemplate> list = macroscopyTemplateService.templates(caseId);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
}

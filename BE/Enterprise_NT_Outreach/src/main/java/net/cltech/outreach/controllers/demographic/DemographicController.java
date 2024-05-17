package net.cltech.outreach.controllers.demographic;

import net.cltech.outreach.domain.demographic.Demographic;
import net.cltech.outreach.service.interfaces.demographic.DemographicsService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.annotation.ApiVersion;
import org.jsondoc.core.pojo.ApiVerb;
import org.jsondoc.core.pojo.ApiVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de servicios que devuelve un Demografico
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 29/01/2020
 * @see Creacion
 */
@Api(
        group = "Demograficos",
        name = "web",
        description = "Servicios sobre la Configuracion General"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/demographic")
public class DemographicController {
    
    @Autowired   
    private DemographicsService demographicService;
     
      //------------ DETALLE DEMOGRAPHICS WEB QUERY ---------------- 
   @ApiMethod(
            description = "Obtiene todos los parametros de los demograficos",
            path = "/api/demographic/webquery",
            visibility = ApiVisibility.PUBLIC,            
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"    
            
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(value = "/webquery", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Demographic> listDemographic() throws Exception
    {
       Demographic demographic = demographicService.queryDemographic();
       if (demographic != null)
        {
            return new ResponseEntity<>(demographic, HttpStatus.OK);
            
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
    }  
    
}

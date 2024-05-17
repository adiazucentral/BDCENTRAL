/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Pathologist;
import net.cltech.enterprisent.service.interfaces.masters.pathology.PathologistService;
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
import net.cltech.enterprisent.domain.operation.pathology.FilterPathology;

/**
 * Servicios para el maestro de patologos
 *
 * @version 1.0.0
 * @author omendez
 * @since 16/04/2021
 * @see Creacion
 */
@Api(
    group = "Patología",
    name = "Patólogo",
    description = "Servicios de maestros de patologos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/pathologist")
public class PathologistController 
{
   @Autowired
   private PathologistService pathologistService; 
   
    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los patologos registrados",
            path = "/api/pathology/pathologist",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Pathologist.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Pathologist>> list() throws Exception
    {
        List<Pathologist> list = pathologistService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ LISTAR CON FILTROS ----------------
    @ApiMethod(
            description = "Lista los patologos registrados con filtros en la agenda",
            path = "/api/pathology/pathologist/filter/schedule",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Pathologist.class)
    @RequestMapping(value = "filter/schedule", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Pathologist>> listFilter(@ApiBodyObject(clazz = Pathologist.class) @RequestBody FilterPathology filter) throws Exception
    {
        List<Pathologist> list = pathologistService.list(filter);
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
            description = "Obtiene la informacion de un patologo por id",
            path = "/api/pathology/pathologist/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Pathologist.class)
    @RequestMapping(value = "filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pathologist> getById(
            @ApiPathParam(name = "id", description = "Id del patologo") @PathVariable(name = "id") int id
    ) throws Exception
    {
        Pathologist pathologist = pathologistService.get(id);
        if (pathologist == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(pathologist, HttpStatus.OK);
        }
    }
    
    //------------ ASIGNA ORGANOS ----------------
    @ApiMethod(
            description = "Asigna organos a un patologo",
            path = "/api/pathology/pathologist",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Pathologist.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> assignOrgans(
            @ApiBodyObject(clazz = Pathologist.class) @RequestBody Pathologist pathologist
    ) throws Exception
    {
        return new ResponseEntity<>(pathologistService.assignOrgans(pathologist), HttpStatus.OK);
    }
}

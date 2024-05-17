/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.StudyType;
import net.cltech.enterprisent.service.interfaces.masters.pathology.StudyTypeService;
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
 * Servicios para el maestro de tipos de estudio de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 27/10/2020
 * @see Creacion
 */
@Api(
    group = "Patología",
    name = "Tipo de Estudios",
    description = "Servicios de maestros de los tipos de estudios de patologia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/studytype")
public class StudyTypeController 
{
    
    @Autowired
    private StudyTypeService studyTypeService;
    
    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Obtiene la lista de tipo de estudios",
            path = "/api/pathology/studytype",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED, 500 - INTERNAL SERVER ERROR "
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StudyType.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudyType>> get() throws Exception
    {
        List<StudyType> list = studyTypeService.list();
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
            description = "Obtiene un tipo de estudio por id",
            path = "/api/pathology/studytype/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StudyType.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudyType> filterById(
            @ApiPathParam(name = "id", description = "Id del tipo de estudio") @PathVariable(name = "id") int id
    ) throws Exception
    {
        StudyType studyType = studyTypeService.findById(id);
        if (studyType != null)
        {
            return new ResponseEntity<>(studyType, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    //------------ OBTENER POR CODIGO ----------------
    @ApiMethod(
            description = "Obtiene un tipo de estudio por codigo",
            path = "/api/pathology/studytype/filter/code/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StudyType.class)
    @RequestMapping(value = "/filter/code/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudyType> filterByCode(
            @ApiPathParam(name = "code", description = "Codigo del tipo de estudio") @PathVariable(name = "code") String code
    ) throws Exception
    {
        StudyType studyType = studyTypeService.findByCode(code);
        if (studyType != null)
        {
            return new ResponseEntity<>(studyType, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //------------ OBTENER POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene un tipo de estudio por nombre",
            path = "/api/pathology/studytype/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StudyType.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudyType> filterByName(
            @ApiPathParam(name = "name", description = "Nombre del tipo de estudio") @PathVariable(name = "name") String name
    ) throws Exception
    {
        StudyType studyType = studyTypeService.findByName(name);
        if (studyType != null)
        {
            return new ResponseEntity<>(studyType, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un tipo de estudio",
            path = "/api/pathology/studytype",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StudyType.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudyType> create(
            @ApiBodyObject(clazz = StudyType.class) @RequestBody StudyType studyType
    ) throws Exception
    {
        return new ResponseEntity<>(studyTypeService.create(studyType), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualiza un tipo de estudio",
            path = "/api/pathology/studytype",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StudyType.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudyType> update(
            @ApiBodyObject(clazz = StudyType.class) @RequestBody(required = true) StudyType studyType
    ) throws Exception
    {
        return new ResponseEntity<>(studyTypeService.update(studyType), HttpStatus.OK);
    }

    //------------ OBTENER POR ESTADO ----------------
    @ApiMethod(
            description = "Obtiene tipos de estudios por su estado",
            path = "/api/pathology/studytype/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StudyType.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudyType>> filterByState(
            @ApiPathParam(name = "state", description = "Estado activo(1) o inactivo(0)") @PathVariable(name = "state") int state
    ) throws Exception
    {
        List<StudyType> studyType = studyTypeService.filterByState(state);
        if (studyType != null && !studyType.isEmpty())
        {
            return new ResponseEntity<>(studyType, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}

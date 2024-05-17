/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Field;
import net.cltech.enterprisent.service.interfaces.masters.pathology.FieldService;
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
 * Servicios para el maestro de campos para las plantillas de macroscopia
 *
 * @version 1.0.0
 * @author omendez
 * @since 09/06/2021
 * @see Creacion
 */
@Api(
    group = "Patología",
    name = "Campos",
    description = "Servicios de maestros de los campos para las plantillas de macroscopia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/field")
public class FieldController 
{
    @Autowired
    private FieldService fieldService;
    
    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los campos registrados",
            path = "/api/pathology/field",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Field.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Field>> list() throws Exception
    {
        List<Field> list = fieldService.list();
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
            description = "Lista los campos registradas por estado",
            path = "/api/pathology/field/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Field.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Field>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") int state
    ) throws Exception
    {
        List<Field> list = fieldService.list(state);
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
            description = "Obtiene la informacion de un campo por id",
            path = "/api/pathology/field/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Field.class)
    @RequestMapping(value = "filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Field> getById(
            @ApiPathParam(name = "id", description = "Id del campo") @PathVariable(name = "id") int id
    ) throws Exception
    {
        Field field = fieldService.get(id, null);
        if (field == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(field, HttpStatus.OK);
        }
    }
    
    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un campo por nombre",
            path = "/api/pathology/field/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Field.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Field> getByName(
            @ApiPathParam(name = "name", description = "Nombre del campo") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Field field = fieldService.get(null, name);
        if (field == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(field, HttpStatus.OK);
        }
    }
    
    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un nuevo campo",
            path = "/api/pathology/field",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Field.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Field> create(
            @ApiBodyObject(clazz = Field.class) @RequestBody Field field
    ) throws Exception
    {
        return new ResponseEntity<>(fieldService.create(field), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un nuvo campo",
            path = "/api/pathology/field",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Field.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Field> update(
            @ApiBodyObject(clazz = Field.class) @RequestBody Field field
    ) throws Exception
    {
        return new ResponseEntity<>(fieldService.update(field), HttpStatus.OK);
    }
}

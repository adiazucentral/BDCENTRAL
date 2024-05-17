/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.File;
import net.cltech.enterprisent.service.interfaces.operation.pathology.FileService;
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
 * Servicios para el acceso a la información de los documentos de los casos de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 02/03/2021
 * @see Creacion
 */
@Api(
    name = "Archivos",
    group = "Patología",
    description = "Servicios para el acceso a la información de los documentos de los casos de patologia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/file")
public class FileController 
{
    @Autowired
    private FileService fileService;
    
    //------------ LISTA DOCUMENTOS DEL CASO ----------------
    @ApiMethod(
            description = "Lista los documentos de un caso.",
            path = "/api/pathology/file/case/{casePat}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = File.class)
    @RequestMapping(value = "/case/{casePat}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<File>> list(@ApiPathParam(name = "casePat", description = "Caso") @PathVariable(name = "casePat") Integer casePat) throws Exception
    {
        List<File> list = fileService.list(casePat);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ CREAR ----------------
    @ApiMethod(
            description = "Guardar documento de un caso",
            path = "/api/pathology/file",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = File.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<File> save(@ApiBodyObject(clazz = File.class) @RequestBody File file) throws Exception
    {
        return new ResponseEntity<>(fileService.save(file), HttpStatus.OK);
    }
    
    //------------ ELIMINAR ----------------
    @ApiMethod(
            description = "Eliminar documento de un caso",
            path = "/api/pathology/file",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = File.class)
    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<File> delete(@ApiBodyObject(clazz = File.class) @RequestBody File file) throws Exception
    {
        return new ResponseEntity<>(fileService.delete(file), HttpStatus.OK);
    }
}

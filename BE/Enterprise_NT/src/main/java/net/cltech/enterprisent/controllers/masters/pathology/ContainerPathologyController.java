/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.ContainerPathology;
import net.cltech.enterprisent.service.interfaces.masters.pathology.ContainerPathologyService;
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
 * Servicios para el acceso a la información de los contenedores de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 19/10/2020
 * @see Creacion
 */
@Api(
    group = "Patología",
    name = "Contenedor",
    description = "Servicios para el acceso a la información de los contenedores de patología"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/container")
public class ContainerPathologyController 
{
    @Autowired
    private ContainerPathologyService containerService;
    
    
    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los contenedores registrados de patologia",
            path = "/api/pathology/container",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ContainerPathology.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ContainerPathology>> list() throws Exception
    {
        List<ContainerPathology> list = containerService.list();
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
            description = "Lista los contenedores registrados de patologia por estado",
            path = "/api/pathology/container/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ContainerPathology.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ContainerPathology>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") int state
    ) throws Exception
    {
        List<ContainerPathology> list = containerService.list(state);
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
            description = "Obtiene la informacion de un contenedor por id",
            path = "/api/pathology/container/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ContainerPathology.class)
    @RequestMapping(value = "filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContainerPathology> getById(
            @ApiPathParam(name = "id", description = "Id del contenedor") @PathVariable(name = "id") int id
    ) throws Exception
    {
        ContainerPathology container = containerService.get(id, null);
        if (container == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(container, HttpStatus.OK);
        }
    }
    
    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un contenedor por nombre",
            path = "/api/pathology/container/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ContainerPathology.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContainerPathology> getByName(
            @ApiPathParam(name = "name", description = "Nombre del contenedor") @PathVariable(name = "name") String name
    ) throws Exception
    {
        ContainerPathology container = containerService.get(null, name);
        if (container == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(container, HttpStatus.OK);
        }
    }
    
    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un nuevo contenedor de patologia",
            path = "/api/pathology/container",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ContainerPathology.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContainerPathology> create(
            @ApiBodyObject(clazz = ContainerPathology.class) @RequestBody ContainerPathology container
    ) throws Exception
    {
        return new ResponseEntity<>(containerService.create(container), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un contenedor de patologia",
            path = "/api/pathology/container",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ContainerPathology.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContainerPathology> update(
            @ApiBodyObject(clazz = ContainerPathology.class) @RequestBody ContainerPathology container
    ) throws Exception
    {
        return new ResponseEntity<>(containerService.update(container), HttpStatus.OK);
    }
    
}

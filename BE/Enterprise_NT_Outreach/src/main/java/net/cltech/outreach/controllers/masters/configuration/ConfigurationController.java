/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.controllers.masters.configuration;

import java.util.List;
import net.cltech.outreach.domain.masters.configuration.Configuration;
import net.cltech.outreach.domain.masters.configuration.DocumentType;
import net.cltech.outreach.service.interfaces.masters.configuration.ConfigurationService;
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
 * Servicios REST para configuracion general
 *
 * @version 1.0.0
 * @author dcortes
 * @since 14/04/2017
 * @see Creacion
 */
@Api(
        group = "Configuración",
        name = "Configuracion",
        description = "Servicios sobre la Configuracion General"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/configuration")
public class ConfigurationController
{

    @Autowired
    private ConfigurationService configurationService;

    @ApiMethod(
            description = "Obtiene todos los parametros de configuración",
            path = "/api/configuration",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Configuration.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Configuration>> get() throws Exception
    {
        List<Configuration> records = configurationService.get();
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(configurationService.get(), HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
     @ApiMethod(
            description = "Obtiene todos los parametros de configuración",
            path = "/api/configuration/encrypted",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Configuration.class)
    @RequestMapping(value = "/encrypted", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Configuration>> getEncrypted() throws Exception
    {
        List<Configuration> records = configurationService.getEncrypted();
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(configurationService.getEncrypted(), HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene un parametro de configuración por su llave",
            path = "/api/configuration/{key}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Configuration.class)
    @RequestMapping(value = "/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Configuration> getkey(
            @ApiPathParam(name = "key", description = "Llave de configuración") @PathVariable(name = "key") String key
    ) throws Exception
    {
        Configuration record = configurationService.get(key);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualiza las llaves de configuracion enviadas",
            path = "/api/configuration",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Configuration.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Configuration>> update(
            @ApiBodyObject(clazz = Configuration.class) @RequestBody(required = true) List<Configuration> configuration
    ) throws Exception
    {
        configurationService.update(configuration);
        return new ResponseEntity<>(configuration, HttpStatus.OK);
    }

    //------------ LISTAR TIPOS DE DOCUMENTO POR ESTADO ----------------
    @ApiMethod(
            description = "Lista los tipos de documento registrados por estado",
            path = "/api/configuration/documenttypes",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DocumentType.class)
    @RequestMapping(value = "/documenttypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DocumentType>> listDocumentType() throws Exception
    {
        List<DocumentType> list = configurationService.listDocumentType(true);

        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
}

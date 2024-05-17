/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.info;

import net.cltech.enterprisent.domain.info.DatabaseInfo;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.service.interfaces.common.DatabaseInfoService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.stadistics.StadisticsLog;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Servicios para el acceso a la informacion de la base de datos
 *
 * @version 1.0.0
 * @author dcortes
 * @since 06/04/2017
 * @see Creacion
 */
@Api(
        name = "Base de Datos",
        group = "Información",
        description = "Servicios de informacion de la base de datos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/info")
public class DatabaseInfoController
{

    @Autowired
    private DatabaseInfoService dataBaseInfoService;
    @Autowired
    private ConfigurationService configurationService;

    @ApiMethod(
            description = "Obtiene la información de la base de datos conectada",
            path = "/api/info",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DatabaseInfo.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DatabaseInfo> getInfo() throws Exception
    {
        DatabaseInfo data = dataBaseInfoService.getInfo();
        if (data != null)
        {
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
     @ApiMethod(
            description = "Obtiene la información de la version del backend",
            path = "/api/info/version",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Configuration.class)
    @RequestMapping(value = "/version", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Configuration> getInfoVersion() throws Exception
    {
        Configuration data = configurationService.get("version");
        if (data != null)
        {
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Encripta informacion",
            path = "/api/info/encrypt/{text}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/encrypt/{text}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> encrypt(
            @ApiPathParam(name = "text") @PathVariable String text
    ) throws Exception
    {
        return new ResponseEntity(Tools.encrypt(text), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Desencripta informacion",
            path = "/api/info/decrypt/{text}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/decrypt/{text}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> decrypt(
            @ApiPathParam(name = "text") @PathVariable String text
    ) throws Exception
    {
        return new ResponseEntity(Tools.decrypt(text), HttpStatus.OK);
    }
}

package net.cltech.enterprisent.controllers.integration;

import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
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
 * Controlador de acceso a los servicios llaves de configuración
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 13/07/2020
 * @see Creación
 */
@Api(
        name = "LLaves de configuracion para Sky Lab",
        group = "Integración",
        description = "Servicios de generales de integración con otras aplicaciones"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/settings")
public class SettingsSkyLabController
{

    @Autowired
    private ConfigurationService configurationService;

    //------------PARAMETRO DE CONFIGURACION PARA SKY LAB ----------------
    @ApiMethod(
            description = "Obtiene un parametro de configuración por su llave",
            path = "/api/settings/{key}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Configuration.class)
    @RequestMapping(value = "/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Configuration> get(
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

}

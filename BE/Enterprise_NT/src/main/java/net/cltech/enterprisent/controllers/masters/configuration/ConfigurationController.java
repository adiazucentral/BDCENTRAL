package net.cltech.enterprisent.controllers.masters.configuration;

import java.util.List;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.configuration.GeneratePrintConfiguration;
import net.cltech.enterprisent.domain.masters.configuration.InitialConfiguration;
import net.cltech.enterprisent.domain.masters.configuration.PrintConfiguration;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.tools.Constants;
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
    @ApiAuthToken(scheme = "JWT")
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
            description = "Obtiene un parametro de configuración por su llave",
            path = "/api/configuration/{key}",
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

    @ApiMethod(
            description = "Obtiene un parametro de configuración por su llave sin token",
            path = "/api/configuration/not/{key}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Configuration.class)
    @RequestMapping(value = "/not/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getNotoken(
            @ApiPathParam(name = "key", description = "Llave de configuración") @PathVariable(name = "key") String key
    ) throws Exception
    {
        Configuration record = configurationService.get(key);
        if (record != null)
        {
            return new ResponseEntity<>(record.getValue(), HttpStatus.OK);
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
        configurationService.update(configuration, true);
        return new ResponseEntity<>(configuration, HttpStatus.OK);
    }

    @ApiMethod(
            description = "Realiza la configuracion inicial del sistema",
            path = "/api/configuration/start/settings",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Configuration.class)
    @RequestMapping(value = "/start/settings", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> initial(
            @ApiBodyObject(clazz = InitialConfiguration.class) @RequestBody(required = true) InitialConfiguration initial
    ) throws Exception
    {
        configurationService.initial(initial);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Reiniciar la secuencia del numero de orden de forma manual",
            path = "/api/configuration/restartsequencemanually",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/restartsequencemanually", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> restartSequenceManually() throws Exception
    {
        return new ResponseEntity<>(configurationService.restartSequenceManually(), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Asginar hora de reinicio de secuencia del numero de orden de forma automatica",
            path = "/api/configuration/restartsequence/{hour}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/restartsequence/{hour}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> restartsequence(
            @ApiPathParam(name = "hour", description = "Hora de reinicio de numero de orden HHMM") @PathVariable(name = "hour") String hour
    ) throws Exception
    {
        configurationService.restartSequenceAutomatic(hour);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Prueba la url del servidor",
            path = "/api/configuration/test",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> test() throws Exception
    {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene le numero de digitos de la orden ",
            path = "/api/configuration/getorderdigits",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/getorderdigits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getOrderDigits() throws Exception
    {
        return new ResponseEntity<>(configurationService.getIntValue("DigitosOrden"), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtener llaves para configuracion de la aplicacion de impresion",
            path = "/api/configuration/configprint",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = PrintConfiguration.class)
    @RequestMapping(value = "/configprint", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PrintConfiguration> configprint(
            @ApiBodyObject(clazz = GeneratePrintConfiguration.class) @RequestBody(required = true) GeneratePrintConfiguration serial
    ) throws Exception
    {
        return new ResponseEntity<>(configurationService.configprint(serial), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtener llaves cuerpo de correo",
            path = "/api/configuration/bodyemail",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/bodyemail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> bodyEmail() throws Exception
    {
        return new ResponseEntity<>(configurationService.bodyEmail(), HttpStatus.OK);
    }
    
     @ApiMethod(
            description = "Obtener llaves cuerpo de correo",
            path = "/api/configuration/inbranch",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/inbranch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
      public ResponseEntity<Configuration> inBranch() throws Exception 
    {
        return new ResponseEntity<>(configurationService.get(Constants.BRANCH_IN), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtener llave expiracion del token",
            path = "/api/configuration/securitypolitics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Configuration.class)
    @RequestMapping(value = "/securitypolitics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Configuration> securityPolitics() throws Exception 
    {
        return new ResponseEntity<>(configurationService.get(Constants.SEGURITY_MANAGER), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtener llave expiracion de la recuperación de la contraseña",
            path = "/api/configuration/passwordrecovery",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Configuration.class)
    @RequestMapping(value = "/passwordrecovery", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Configuration> passwordrecovery() throws Exception
    {
        return new ResponseEntity<>(configurationService.get(Constants.PASSWORDRECOVERY), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtener llave expiracion del token",
            path = "/api/configuration/tokenexpiration",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Configuration.class)
    @RequestMapping(value = "/tokenexpiration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Configuration> tokenExpiration() throws Exception
    {
        return new ResponseEntity<>(configurationService.get(Constants.TOKEN_EXPIRATION_TIME), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Crear o actualizar secuencia de rellamado",
            path = "/api/configuration/restartsequence/recalled",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/restartsequence/recalled", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> generateSequence() throws Exception
    {
        configurationService.restartSequenceRecalled();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Crear o actualizar secuencia de citas",
            path = "/api/configuration/restartsequence/appointment",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/restartsequence/appointment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> generateSequenceAppointment() throws Exception
    {
        configurationService.restartSequenceAppointment();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Actualiza valor de llaves de url de api seguridad y de api de licenciamiento",
            path = "/api/configuration/updatesecurity",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Configuration.class)
    @RequestMapping(value = "/updatesecurity", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Configuration>> updateSecurity(
            @ApiBodyObject(clazz = Configuration.class) @RequestBody(required = true) List<Configuration> configuration
    ) throws Exception
    {
        configurationService.updateSecurity(configuration);
        return new ResponseEntity<>(configuration, HttpStatus.OK);
    }

    @ApiMethod(
            description = "Consulta el valor de la llave de configuración de uso de sedes del LIS",
            path = "/api/configuration/getBranchConfiguration",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/getBranchConfiguration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getBranchConfiguration() throws Exception
    {
        String record = configurationService.getBranchConfiguration();
        if (record == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (record.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        }
    }
}

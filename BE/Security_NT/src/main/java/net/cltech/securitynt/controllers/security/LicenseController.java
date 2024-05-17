package net.cltech.securitynt.controllers.security;

import java.util.HashMap;
import net.cltech.securitynt.domain.common.License;
import net.cltech.securitynt.domain.common.LicenseResponse;
import net.cltech.securitynt.service.interfaces.security.LicenseService;
import org.jsondoc.core.annotation.Api;
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
 * Controlador de acceso a los servicios de licenciamiento
 *
 * @version 1.0.0
 * @author equijano
 * @since 25/10/2019
 * @see Creación
 */
@Api(
        name = "Licenciamiento",
        group = "Autenticación y Autorizacion",
        description = "Servicios de licenciamiento de la aplicación"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/license")
public class LicenseController
{

    @Autowired
    private LicenseService licenseService;

    @ApiMethod(
            description = "Realiza la consulta de los parametros de licenciamiento de los modulos de la aplicacion",
            path = "/api/license",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponseObject(clazz = LicenseResponse.class)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LicenseResponse> authentication(
            @ApiBodyObject(clazz = License.class) @RequestBody License license
    ) throws Exception, Exception
    {
        return new ResponseEntity(licenseService.getLicenses(license), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Realiza la consulta del licenciamiento completo de la aplicacion",
            path = "/api/license/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponseObject(clazz = HashMap.class)
    @RequestMapping(value = "/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Boolean>> licences(
            @ApiPathParam(name = "branch", description = "Codigo de la sede") @PathVariable("branch") String branchCode
    ) throws Exception, Exception
    {
        return new ResponseEntity(licenseService.licences(branchCode), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Realiza la consulta del licenciamiento para las interfaces",
            path = "/api/license/keyCode/{llave}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponseObject(clazz = HashMap.class)
    @RequestMapping(value = "keyCode/{llave}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> licencesByInterface(
            @ApiPathParam(name = "llave", description = "Llave de la interfaz") @PathVariable("llave") String llave
    ) throws Exception, Exception
    {
        try
        {
            return new ResponseEntity<>(licenseService.validateLicenseInterface(llave), HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @ApiMethod(
            description = "Obtiene las licencias de los tableros",
            path = "/api/license/boardLicenses",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponseObject(clazz = HashMap.class)
    @RequestMapping(value = "/boardLicenses", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Boolean>> boardLicenses() throws Exception, Exception
    {
        return new ResponseEntity(licenseService.boardLicenses(), HttpStatus.OK);
    }
}

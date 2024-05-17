package net.cltech.enterprisent.controllers.integration;

import net.cltech.enterprisent.domain.integration.middleware.MiddlewareUrl;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMiddlewareService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.annotation.ApiVersion;
import org.jsondoc.core.pojo.ApiVerb;
import org.jsondoc.core.pojo.ApiVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementacion de integración para Middleware.
 *
 * @version 1.0.0
 * @author equijano
 * @since 31/10/2018
 * @see Creacion
 */
@Api(
        name = " Integracion Middleware",
        group = "Integración",
        description = "Servicios generales de integración con Middleware"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/integration/middleware")
public class IntegrationMiddlewareControlller
{

    @Autowired
    private IntegrationMiddlewareService integrationMiddlewareService;

    // VERIFICA LA URL DEL LABORATORIO DEL MIDDLEWARE
    @ApiMethod(
            description = "Verificacion de la url del middleware",
            path = "/api/integration/middleware/verification/url",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/verification/url", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> testUrl(
            @ApiBodyObject(clazz = MiddlewareUrl.class) @RequestBody MiddlewareUrl url
    ) throws Exception
    {
        return new ResponseEntity<>(integrationMiddlewareService.testASTM(url), HttpStatus.OK);
    }

    // REENVIO DE ORDENES AL MIDDLEWARE
    @ApiMethod(
            description = "Reenvio de ordenes al middleware por rango de fechas y/o por ordenes",
            path = "/api/integration/middleware/resend",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/resend", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> resend(
            @ApiBodyObject(clazz = ResultFilter.class) @RequestBody ResultFilter resultFilter
    ) throws Exception
    {
        int respose = integrationMiddlewareService.resend(resultFilter);
        if (respose > -1)
        {
            return new ResponseEntity<>(respose, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

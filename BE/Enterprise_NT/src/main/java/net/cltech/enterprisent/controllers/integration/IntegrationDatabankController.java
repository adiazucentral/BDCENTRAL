package net.cltech.enterprisent.controllers.integration;

import java.util.List;
import net.cltech.enterprisent.domain.operation.orders.Result;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationDatabankService;
import net.cltech.enterprisent.tools.log.stadistics.StadisticsLog;
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
 * Controlador de servicios Rest sobre tableros.
 *
 * @version 1.0.0
 * @author jdiaz
 * @since 11/07/2018
 * @see Creacion
 */
@Api(
        name = "Integracion Databank",
        group = "Integración",
        description = "Servicios Rest sobre ordenes"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/databank")
public class IntegrationDatabankController {
    
    @Autowired
    private IntegrationDatabankService IntegrationDatabankService;
    
    // ------------------ RECEPCION DE RESULTADOS DEL MIDDLEWARE ---------------------
    @ApiMethod(
            description = "Inserta los resultados enviados por databank",
            path = "/api/databank/results",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Result.class)
    @RequestMapping(value = "/results", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity insertResult(
            @ApiBodyObject
            @RequestBody List<ResultTest> resultTest
    ) throws Exception
    {
        IntegrationDatabankService.results(resultTest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

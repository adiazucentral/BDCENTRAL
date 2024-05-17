package net.cltech.enterprisent.controllers.integration;

import java.util.List;
import net.cltech.enterprisent.domain.DTO.integration.minsa.results.OrderHisPendingResults;
import net.cltech.enterprisent.domain.DTO.integration.minsa.results.OrderPendingResults;
import net.cltech.enterprisent.domain.integration.generalMinsa.GeneralOrder;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMinsaService;
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
 * Controlador de acceso a los servicios de resultados para Minsa
 *
 * @version 1.0.0
 * @author bbonilla
 * @since 29/04/2022
 * @see Creación
 *
 * @author hpoveda
 * @since 29/04/2022
 * @see Creación
 */
@Api(
        name = "Integracion Minsa",
        group = "Integración",
        description = "Servicios de generales de integración con Minsa"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/integration/minsa")
public class IntegrationMinsaController
{

    @Autowired
    private IntegrationMinsaService integrationMinsaService;

    //------------ Consultar resultados NT minsa ----------------
    @ApiMethod(
            description = "Consulta los resultados en la base para envio a Minsa",
            path = "/api/integration/minsa/pendingResults/",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderPendingResults.class)
    @RequestMapping(value = "/pendingResults/{centralSytem}/{days}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderPendingResults>> pendingResultsOrdersEpi(
            @ApiPathParam(name = "centralSytem", description = "Id sistema central") @PathVariable("centralSytem") int centralSytem,
            @ApiPathParam(name = "days", description = "days hacia atras para consulta ") @PathVariable("days") int days
    ) throws Exception
    {
        List<OrderPendingResults> list = integrationMinsaService.pendingResultsOrdersEpi(centralSytem, days);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }

    }

    //------------ Inserta una orden de resultados enviada al his ----------------
    @ApiMethod(
            description = "Inserta una orden de resultados enviada al his",
            path = "/api/integration/minsa/insertOrderHis",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/insertOrderHis", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> insertOrderHis(
            @ApiBodyObject(clazz = OrderHisPendingResults.class) @RequestBody List<OrderHisPendingResults> hisPendingResults
    ) throws Exception
    {

        boolean result = integrationMinsaService.insertOrderHis(hisPendingResults);
        if (result)
        {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    @ApiMethod(
            description = "Crea una orden en el sistema y realiza otro tipo de operaciones con esta dentro del sistema",
            path = "/api/integration/minsa/reportresult",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/reportresult", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> reportResult(
            @ApiBodyObject(clazz = List.class) @RequestBody List<GeneralOrder> orders
    ) throws Exception
    {
        integrationMinsaService.reportResult(orders);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

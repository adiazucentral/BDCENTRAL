package net.cltech.enterprisent.controllers.integration;

import java.util.List;
import net.cltech.enterprisent.domain.integration.ingresoLIH.OrdenLaboratorio;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationIngresoLIHService;
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
 * Controlador de acceso a los servicios de interfaz de ingreso
 *
 * @version 1.0.0
 * @author bvalero
 * @since 22/04/2020
 * @see Creación
 */
@Api(
        name = "Servicios para interfaz de ingreso LIH",
        group = "Integración",
        description = "Servicios generales de integración con otras aplicaciones"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/integration/ingresoLIH")
public class IntegrationIngresoLIHController {
    
    @Autowired
    private IntegrationIngresoLIHService serviceIngresoLIH; 
    
    // ---------------- Resultados para la interfaz de ingreso por orden y sistema central ------------------
    @ApiMethod(
            description = "Resultados para la interfaz de resultados por orden y sistema central",
            path = "/api/integration/ingresoLIH/getDataEntry/centralSystem/{centralSystem}/orderInitial/{orderInitial}/orderFinal/{orderFinal}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrdenLaboratorio.class)
    @RequestMapping(value = "/getDataEntry/centralSystem/{centralSystem}/orderInitial/{orderInitial}/orderFinal/{orderFinal}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrdenLaboratorio>> getDataByCentralSystem(
            @ApiPathParam(name = "centralSystem", description = "Sistema central") @PathVariable(name = "centralSystem") int centralSystem,
            @ApiPathParam(name = "orderInitial", description = "Orden Inicial") @PathVariable(name = "orderInitial") long orderInitial,
            @ApiPathParam(name = "orderFinal", description = "Orden Final") @PathVariable(name = "orderFinal") long orderFinal
    ) throws Exception
    {
        try
        {
            List<OrdenLaboratorio> listOrders = serviceIngresoLIH.getDataByCentralSystem(centralSystem, orderInitial, orderFinal);
            if (listOrders.isEmpty() || listOrders == null)
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else
            {
                return new ResponseEntity<>(listOrders, HttpStatus.OK);
            }
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
     // ------------------ ACTUALIZACION DE ORDEN ENVIADA AL LIH -------------------
    @ApiMethod(
            description = "Actualización de orden enviada al LIH",
            path = "/api/integration/ingresoLIH/updateOrderSendLIH",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/updateOrderSendLIH", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateOrderStateSendLIH(
            @ApiBodyObject(clazz = OrdenLaboratorio.class) @RequestBody OrdenLaboratorio order
    ) throws Exception
    {
        try
        {
            int updateResult = serviceIngresoLIH.updateOrderStateSendLIH(order);
            return new ResponseEntity<>((updateResult != 0), HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

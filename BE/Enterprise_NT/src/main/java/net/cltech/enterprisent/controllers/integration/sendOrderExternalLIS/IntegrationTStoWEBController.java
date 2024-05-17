/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.integration.sendOrderExternalLIS;

import java.util.List;
import net.cltech.enterprisent.domain.integration.orderForExternal.OrderForExternal;
import net.cltech.enterprisent.service.impl.enterprisent.integration.sendOrderExternalLIS.IntegrationTStoWEBServiceEnterpriseNT;
import net.cltech.enterprisent.tools.Constants;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
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
 * Controlador para envio de ordenes a LIS web
 *
 * @version 1.0.0
 * @author hpoveda
 * @since 03/06/2022
 * @see Creación
 */
@Api(
        name = "Integracion con WEB",
        group = "Integración",
        description = "Controlador para envio de ordenes de LIS a WEB"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/integration/web")
public class IntegrationTStoWEBController
{

    //servicios 
    @Autowired
    private IntegrationTStoWEBServiceEnterpriseNT integrationTStoWEBService;

    //------------ LISTAR ORDENS POR LABORATORIO ----------------
    @ApiMethod(
            description = "Lista de ordenes para enviar al LIS WEB",
            path = "/api/integration/web",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = OrderForExternal.class)
    @RequestMapping(value = "/{startDate}/{endDate}/{laboratoryID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderForExternal>> getListOrder(@PathVariable long startDate, @PathVariable long endDate, @PathVariable int laboratoryID) throws Exception
    {
        return new ResponseEntity(integrationTStoWEBService.getListOrder(startDate, endDate, laboratoryID), HttpStatus.OK);
    }

    //------------ LISTAR ORDENS POR LABORATORIO ----------------
    @ApiMethod(
            description = "Lista de ordenes para enviar al LIS WEB",
            path = "/api/integration/web/updatestatus",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = OrderForExternal.class)
    @RequestMapping(value = "/updatestatus/{orderNumber}/{idTest}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateOrderStatus(@PathVariable long orderNumber, @PathVariable int idTest) throws Exception
    {
        return new ResponseEntity(integrationTStoWEBService.updateOrderStatus(orderNumber, idTest, Constants.SENT ), HttpStatus.OK);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.integration;

import java.util.List;
import net.cltech.enterprisent.domain.operation.billing.integration.OrderBilling;
import net.cltech.enterprisent.service.interfaces.operation.billing.integration.OrderBillingService;
import net.cltech.enterprisent.tools.Constants;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Representa controlller asociado a la facturacion de ona orden
 *
 * @version 1.0.0
 * @author hpoveda
 * @since 13/05/2022
 * @see Creación
 */
@Api(
        name = "Integración facturación",
        group = "Integración",
        description = "Servicios para la integración de facturacion "
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/integration/orderBilling")
public class IntegrationOrderBillingController
{

    @Autowired
    private OrderBillingService orderBillingService;

    @RequestMapping(value = "/{daysToSearch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderBilling>> get(@PathVariable long daysToSearch
    ) throws Exception
    {
        return new ResponseEntity<>((orderBillingService.get(daysToSearch)), HttpStatus.OK);
    }

    @RequestMapping(value = "/{orderNumber}/{idTest}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateStatusOrderTest(@PathVariable long orderNumber, @PathVariable int idTest
    ) throws Exception
    {
        return new ResponseEntity<>(orderBillingService.updateStatusOrderTest(orderNumber, idTest, Constants.SENT), HttpStatus.OK);
    }
}

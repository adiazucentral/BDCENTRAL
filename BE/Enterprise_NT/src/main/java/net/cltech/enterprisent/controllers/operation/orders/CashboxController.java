package net.cltech.enterprisent.controllers.operation.orders;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.masters.test.Concurrence;
import net.cltech.enterprisent.domain.operation.orders.billing.CashBox;
import net.cltech.enterprisent.domain.operation.orders.billing.FullPayment;
import net.cltech.enterprisent.service.interfaces.operation.orders.CashBoxService;
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
 * Controlador de servicios Rest sobre pago de cajas
 *
 * @version 1.0.0
 * @author dcortes
 * @since 30/06/2017
 * @see Creacion
 */
@Api(
        name = "Caja",
        group = "Operación - Ordenes",
        description = "Servicios Rest sobre caja"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/cashboxes")
public class CashboxController
{

    @Autowired
    private CashBoxService cashboxService;

    @ApiMethod(
            description = "Guarda o actualiza la caja de una orden",
            path = "/api/cashboxes",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 304 - NOT MODIFIED, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CashBox.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CashBox> save(
            @ApiBodyObject @RequestBody CashBox cashbox, HttpServletRequest request
    ) throws Exception
    {
        CashBox record = cashboxService.save(cashbox);
        if (record != null)
        {
            return new ResponseEntity(record, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(record, HttpStatus.NOT_MODIFIED);
        }
    }

    @ApiMethod(
            description = "Obtiene la caja de una orden",
            path = "/api/cashboxes/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 2004 - NOT CONTEND, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CashBox.class)
    @RequestMapping(value = "/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CashBox> get(
            @ApiPathParam(name = "order", description = "Numero de la orden") @PathVariable("order") long order
    ) throws Exception
    {
        CashBox record = cashboxService.get(order);
        if (record != null)
        {
            return new ResponseEntity(record, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(record, HttpStatus.NO_CONTENT);
        }
    }

    //    //------------ PAGO DE CAJA TOTAL ----------------
    @ApiMethod(
            description = "Guarda los pagos de una caja",
            path = "/api/cashboxes/totalpayments",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 304 - NOT MODIFIED, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = FullPayment.class)
    @RequestMapping(value = "/totalpayments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FullPayment> saveTotalPayments(
            @ApiBodyObject(clazz = FullPayment.class) @RequestBody FullPayment create
    ) throws Exception
    {
        FullPayment record = cashboxService.insertTotalPayments(create);
        if (record != null)
        {
            return new ResponseEntity(record, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(record, HttpStatus.NOT_MODIFIED);
        }
    }

    @ApiMethod(
            description = "Actualiza los pagos de una caja",
            path = "/api/cashboxes/totalpayments",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 304 - NOT MODIFIED, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = FullPayment.class)
    @RequestMapping(value = "/totalpayments", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FullPayment> updateTotalPayments(
            @ApiBodyObject(clazz = FullPayment.class) @RequestBody FullPayment create
    ) throws Exception
    {
        return new ResponseEntity<>(cashboxService.updateTotalPayments(create), HttpStatus.OK);
    }

    //------------ LISTA LOS DATOS DEL PAGO TOTAL DE LA CAJA----------------
    @ApiMethod(
            description = "Obtiene una lista del pago total de una caja ",
            path = "/api/cashboxes/cashbox/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Concurrence.class)
    @RequestMapping(value = "/cashbox/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FullPayment>> listFullpayment(
            @ApiPathParam(name = "order", description = "numero de orden") @PathVariable(name = "order") long order
    ) throws Exception
    {
        List<FullPayment> list = cashboxService.getLisFullpayment(order);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Elimina la caja",
            path = "/api/cashboxes/delete/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/delete/id/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteCashbox(
            @ApiPathParam(name = "id", description = "Numero de la orden") @PathVariable(name = "id") Long id
    ) throws Exception
    {
        return new ResponseEntity<>(cashboxService.deleteCashbox(id), HttpStatus.OK);
    }
}

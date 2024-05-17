package net.cltech.enterprisent.controllers.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.FormulaOption;
import net.cltech.enterprisent.domain.masters.billing.PriceAssigment;
import net.cltech.enterprisent.domain.masters.billing.PriceAssignmentBatch;
import net.cltech.enterprisent.domain.masters.billing.TestPrice;
import net.cltech.enterprisent.service.interfaces.masters.billing.PriceAssigmentService;
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
 * Servicios para manejo de informacion de precios
 *
 * @version 1.0.0
 * @author eacuna
 * @since 17/08/2017
 * @see Creacion
 */
@Api(
        name = "Asignación Precios",
        group = "Facturacion",
        description = "Servicios de asignacion de precios"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/priceassigments")
public class AssigmentPriceController
{

    @Autowired
    private PriceAssigmentService service;

    @ApiMethod(
            description = "crea/actualiza el precio de un examen",
            path = "/api/priceassigments",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PriceAssigment.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> saveTestPrice(
            @ApiBodyObject(clazz = PriceAssigment.class) @RequestBody PriceAssigment rate
    ) throws Exception
    {
        return new ResponseEntity<>(service.savePrice(rate), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Lista pruebas de con precios por tarifa y vigencia",
            path = "/api/priceassigments/{valid}/{rate}/{area}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestPrice.class)
    @RequestMapping(value = "/{valid}/{rate}/{area}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestPrice>> listTestPrices(
            @ApiPathParam(name = "valid", description = "Id de la vigencía") @PathVariable(name = "valid") int valid,
            @ApiPathParam(name = "rate", description = "Id de la tarifa") @PathVariable(name = "rate") int rate,
            @ApiPathParam(name = "area", description = "Id de la sección") @PathVariable(name = "area") int area
    ) throws Exception
    {
        List<TestPrice> list = service.listTestPrice(valid, rate, area);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "crea/actualiza el precio de un examen",
            path = "/api/priceassigments/import",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PriceAssigment.class)
    @RequestMapping(value = "/import", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> importPrice(
            @ApiBodyObject(clazz = PriceAssigment.class) @RequestBody PriceAssigment rate
    ) throws Exception
    {
        return new ResponseEntity<>(service.importPrices(rate), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Actualiza precios mediante formula",
            path = "/api/priceassigments/formula",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/formula", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> importPrice(
            @ApiBodyObject(clazz = FormulaOption.class) @RequestBody FormulaOption formula
    ) throws Exception
    {
        return new ResponseEntity<>(service.applyFormula(formula), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Aplica la vigencia",
            path = "/api/priceassigments/apply",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/apply", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> applyFeeschedule(
            @ApiBodyObject(clazz = Integer.class) @RequestBody Integer valid
    ) throws Exception
    {
        return new ResponseEntity<>(service.applyFeeschedule(valid), HttpStatus.OK);
    }
    
    @ApiMethod(
            description = "crea/actualiza el precio de una lista de examenes de una o mas tarifas",
            path = "/api/priceassigments/import/batch",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PriceAssignmentBatch.class)
    @RequestMapping(value = "/import/batch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> importPrice(
            @ApiBodyObject(clazz = PriceAssignmentBatch.class) @RequestBody List<PriceAssignmentBatch> rates
    ) throws Exception
    {
        return new ResponseEntity<>(service.importListPrices(rates), HttpStatus.OK);
    }
}

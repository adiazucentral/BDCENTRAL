package net.cltech.enterprisent.controllers.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.Bank;
import net.cltech.enterprisent.domain.masters.billing.DiscountRate;
import net.cltech.enterprisent.service.interfaces.masters.billing.DiscountRateService;
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
 * Servicios para el acceso de datos de los tipo de descuento
 * 
 * @version 1.0.0
 * @author JAVILA
 * @since 23/03/2021
 * @see Creación
 */
@Api(
        name = "Tipos de descuento",
        group = "Facturacion",
        description = "Servicios de informacion del maestro Bancos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/typesDiscounts")
public class DiscountRateController
{
    @Autowired
    private DiscountRateService discountRateService;
    
    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un nuevo tipo de descuento",
            path = "/api/typesDiscounts",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DiscountRate.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiscountRate> create(
            @ApiBodyObject(clazz = DiscountRate.class) @RequestBody DiscountRate discountRate
    ) throws Exception
    {
        return new ResponseEntity<>(discountRateService.create(discountRate), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un tipo de descuento",
            path = "/api/typesDiscounts",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DiscountRate.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiscountRate> update(
            @ApiBodyObject(clazz = DiscountRate.class) @RequestBody DiscountRate discountRate
    ) throws Exception
    {
        return new ResponseEntity<>(discountRateService.update(discountRate), HttpStatus.OK);
    }
    
    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los tipos de descuentos registrados",
            path = "/api/typesDiscounts",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DiscountRate.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DiscountRate>> list() throws Exception
    {
        List<DiscountRate> list = discountRateService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ CONSULTA POR ID ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un banco",
            path = "/api/typesDiscounts/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DiscountRate.class)
    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiscountRate> getById(
            @ApiPathParam(name = "id", description = "Id del tipo de descuento") @PathVariable(name = "id") int id
    ) throws Exception
    {
        DiscountRate discountRate = discountRateService.get(id, null, null);
        if (discountRate == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(discountRate, HttpStatus.OK);
        }
    }
}

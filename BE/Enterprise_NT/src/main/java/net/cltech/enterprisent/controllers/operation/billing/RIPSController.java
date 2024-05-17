package net.cltech.enterprisent.controllers.operation.billing;

import java.util.List;
import net.cltech.enterprisent.domain.operation.billing.InformationRips;
import net.cltech.enterprisent.domain.operation.billing.RipsFilter;
import net.cltech.enterprisent.service.interfaces.operation.billing.InformationRipsService;
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
 * Controlador de servicios Rest sobre RIPS
 *
 * @version 1.0.0
 * @author omendez
 * @since 21/01/2021
 * @see Creacion
 */
@Api(
        name = "Rips",
        group = "Facturacion",
        description = "Servicios Rest sobre rips"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/rips")
public class RIPSController 
{
    @Autowired
    private InformationRipsService informationRipsService;
    
    //------------ LISTAR INFORMACION RIPS POR FILTRO ESPECIFICO ----------------
    @ApiMethod(
            description = "Lista la información rips por un filtro especifico.",
            path = "/api/rips",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = InformationRips.class)
    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InformationRips>> listFilter(
            @ApiBodyObject(clazz = RipsFilter.class) @RequestBody RipsFilter filter
    ) throws Exception
    {
        List<InformationRips> list = informationRipsService.information(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {                                                                     
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
}

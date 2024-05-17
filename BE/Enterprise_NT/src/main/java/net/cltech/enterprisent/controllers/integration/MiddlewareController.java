package net.cltech.enterprisent.controllers.integration;

import java.util.List;
import net.cltech.enterprisent.domain.integration.middleware.AnatomicalSiteMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.AntibioticMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.CheckDestination;
import net.cltech.enterprisent.domain.integration.middleware.DemographicItemMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.DemographicMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.DestinationCovered;
import net.cltech.enterprisent.domain.integration.middleware.MicroorganismsMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.MiddlewareMessage;
import net.cltech.enterprisent.domain.integration.middleware.SampleMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.TestMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.TestToMiddleware;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.microbiology.Antibiotic;
import net.cltech.enterprisent.domain.operation.orders.Result;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationMiddlewareService;
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
 * Implementacion de integración para Middleware.
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 16/01/2019
 * @see Creacion
 */
@Api(
        name = "Middleware",
        group = "Integración",
        description = "Servicios generales de integración con Middleware"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/middleware")
public class MiddlewareController
{

    @Autowired
    private IntegrationMiddlewareService integrationMiddlewareService;

    //------------ LISTAR MUESTRAS ---------------- 
    @ApiMethod(
            description = "Para la importación de muestras al middleware",
            path = "/api/middleware/samples",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = SampleMiddleware.class)
    @RequestMapping(value = "/samples", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SampleMiddleware>> list() throws Exception
    {
        List<SampleMiddleware> list = integrationMiddlewareService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR EXAMENES ----------------    
    @ApiMethod(
            description = "Para la importación de exámenes al Middleware",
            path = "/api/middleware/tests",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = TestMiddleware.class)
    @RequestMapping(value = "/tests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestMiddleware>> listTest() throws Exception
    {
        List<TestMiddleware> listTest = integrationMiddlewareService.listTest();
        if (listTest.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } else
        {
            return new ResponseEntity<>(listTest, HttpStatus.OK);
        }
    }

    // -------------- Lista de Antibioticos --------------------------
    @ApiMethod(
            description = "Importación de antibióticos al Middleware",
            path = "/api/middleware/antibiotics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Antibiotic.class)
    @RequestMapping(value = "/antibiotics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AntibioticMiddleware>> listAntibiotics() throws Exception
    {
        List<AntibioticMiddleware> records = integrationMiddlewareService.listAntibiotics();
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //------------ LISTAR DEMOGRAFICOS ---------------- 
    @ApiMethod(
            description = "Para la importación de demográficos al Middleware",
            path = "/api/middleware/demographics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(value = "/demographics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicMiddleware>> listDemographic() throws Exception
    {
        List<DemographicMiddleware> listDemographic = integrationMiddlewareService.listDemographicAll();
        if (listDemographic.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } else
        {
            return new ResponseEntity<>(listDemographic, HttpStatus.OK);
        }
    }

    //------------- LISTA SITIOS ANATOMICOS ------------------
    @ApiMethod(
            description = "Obtiene una lista de sitios anatómicos para la importación al Middleware",
            path = "/api/middleware/anatomic_sites",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = AnatomicalSiteMiddleware.class)
    @RequestMapping(value = "/anatomic_sites", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AnatomicalSiteMiddleware>> listAnatomicalSite() throws Exception
    {
        List<AnatomicalSiteMiddleware> listAnatomicalSite = integrationMiddlewareService.listAnatomicalSite();
        if (listAnatomicalSite.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(listAnatomicalSite, HttpStatus.OK);
        }
    }

    //-------------- Importación de Microorganismos ------------------
    @ApiMethod(
            description = "Obtiene una lista de microorganismos para la importación al Middleware",
            path = "/api/middleware/microorganisms",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = MicroorganismsMiddleware.class)
    @RequestMapping(value = "/microorganisms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MicroorganismsMiddleware>> listMicroorganisms() throws Exception
    {
        List<MicroorganismsMiddleware> listmMicroorganisms = integrationMiddlewareService.listMicroorganisms();
        if (listmMicroorganisms.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(listmMicroorganisms, HttpStatus.OK);
        }
    }

    //-------------- Importación de Demográficos Ítem --------------------
    @ApiMethod(
            description = "Obtiene la importación de demográficos ítem con un parametro",
            path = "/api/middleware/demographics/items/{demographic}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = DemographicItemMiddleware.class)
    @RequestMapping(value = "/demographics/items/{demographic}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicItemMiddleware>> listDemographicsItems(
            @ApiPathParam(name = "demographic", description = "Id del demográfico") @PathVariable(name = "demographic") Integer demographic
    ) throws Exception
    {
        List<DemographicItemMiddleware> listDemographicItem = integrationMiddlewareService.listDemographicItem(demographic);
        if (listDemographicItem.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(listDemographicItem, HttpStatus.OK);
        }
    }

    // RECEPCION DE RESULTADOS DEL MIDDLEWARE
    @ApiMethod(
            description = "Inserta los resultados enviados por Middleware",
            path = "/api/middleware/results",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 500 - INTERNAL SERVER ERROR, 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = Result.class)
    @RequestMapping(value = "/results", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity insertResult(
            @ApiBodyObject
            @RequestBody List<MiddlewareMessage> message
    ) throws Exception
    {
        boolean success = integrationMiddlewareService.middlewareResults(message);
        return new ResponseEntity<>(success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ApiMethod(
            description = "Obtiene una lista de examenes con valores de referencia para ser enviados al middleware",
            path = "/api/middleware/deltacheck",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
//    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/deltacheck", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestToMiddleware>> deltacheck() throws Exception
    {
        List<TestToMiddleware> list = integrationMiddlewareService.deltacheck();
        if (!list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //------------ VERIFICA SI LA MUESTRA LLEVA TAPA O NO ----------------
    @ApiMethod(
            description = "Verifica si la muestra lleva tapa o no",
            path = "/api/middleware/detailautomate/order/{order}/samplecode/{samplecode}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DestinationCovered.class)
    @RequestMapping(value = "/detailautomate/order/{order}/samplecode/{samplecode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DestinationCovered> getById(
            @ApiPathParam(name = "order", description = "Numero de la orden") @PathVariable(name = "order") String order,
            @ApiPathParam(name = "samplecode", description = "Codigo de la Muestra") @PathVariable(name = "samplecode") String samplecode
    ) throws Exception

    {
        return new ResponseEntity<>(integrationMiddlewareService.covered(order, samplecode), HttpStatus.OK);
    }

    // RECEPCION DE RESULTADOS DEL MIDDLEWARE
    @ApiMethod(
            description = "Verifica la muestra en un destino especifico",
            path = "/api/middleware/checkdestination",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = CheckDestination.class)
    @ApiAuthToken(scheme = "JWT")
    @RequestMapping(value = "/checkdestination", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity CheckDestination(
            @ApiBodyObject
            @RequestBody CheckDestination destination
    ) throws Exception
    {
        return new ResponseEntity<>(integrationMiddlewareService.checkDestinationMiddleware(destination), HttpStatus.OK);
    }

}

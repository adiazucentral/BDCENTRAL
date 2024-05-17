package net.cltech.enterprisent.controllers.operation.reports;

import java.util.List;
import net.cltech.enterprisent.domain.operation.reports.SerialPrint;
import net.cltech.enterprisent.service.interfaces.operation.reports.ServicePrintService;
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
 * Controlador de servicios Rest sobre impresion por servicios
 *
 * @version 1.0.0
 * @author equijano
 * @since 20/06/2019
 * @see Creacion
 */
@Api(
        name = "Impresion por servicios",
        group = "Operación - Informes",
        description = "Servicios Rest sobre impresion por servicios"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/serviceprint")
public class ServicePrintController
{

    @Autowired
    private ServicePrintService servicePrintService;

    @ApiMethod(
            description = "Lista los seriales con servicios",
            path = "/api/serviceprint",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SerialPrint.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SerialPrint>> list() throws Exception
    {
        List<SerialPrint> list = servicePrintService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene un serial por sede y servicio",
            path = "/api/serviceprint/{branch}/{service}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SerialPrint.class)
    @RequestMapping(value = "/{branch}/{service}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SerialPrint> getByService(
            @ApiPathParam(name = "branch", description = "Sede") @PathVariable("branch") int branch,
            @ApiPathParam(name = "service", description = "Servicio") @PathVariable("service") int service
    ) throws Exception
    {
        SerialPrint serialPrint = servicePrintService.getByService(branch, service);
        if (serialPrint == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(serialPrint, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Asigna un servicio a un serial",
            path = "/api/serviceprint",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(
            @ApiBodyObject(clazz = SerialPrint.class) @RequestBody SerialPrint serialPrint
    ) throws Exception
    {
        servicePrintService.create(serialPrint);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @ApiMethod(
            description = "Asigna un servicio a un serial",
            path = "/api/serviceprint/deletebyservice",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/deletebyservice", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@ApiBodyObject(clazz = SerialPrint.class) @RequestBody SerialPrint serialPrint) throws Exception
    {
        servicePrintService.delete(serialPrint);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Asigna una lista de servicios a un serial",
            path = "/api/serviceprint/createall",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/createall", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createAll(
            @ApiBodyObject(clazz = SerialPrint.class) @RequestBody List<SerialPrint> list
    ) throws Exception
    {
        servicePrintService.createAll(list);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Elimina todos los seriales",
            path = "/api/serviceprint/deleteall",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/deleteall", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAll() throws Exception
    {
        servicePrintService.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    

}

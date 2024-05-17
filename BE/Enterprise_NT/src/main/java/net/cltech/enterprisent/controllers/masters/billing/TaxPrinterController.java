package net.cltech.enterprisent.controllers.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.TaxPrinter;
import net.cltech.enterprisent.service.interfaces.masters.billing.TaxPrinterService;
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
 * Servicios para el acceso a la informacion del maestro Bancos
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/06/2017
 * @see Creacion
 */
@Api(
        name = "Impresoras fiscales",
        group = "Facturacion",
        description = "Servicios de informacion del maestro impresoras fiscales"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/taxprinter")
public class TaxPrinterController
{

    @Autowired
    private TaxPrinterService taxPrinterService;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista las impresoras fiscales registradas",
            path = "/api/taxprinter",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TaxPrinter.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaxPrinter>> list() throws Exception
    {
        List<TaxPrinter> list = taxPrinterService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR POR ESTADO ----------------
    @ApiMethod(
            description = "Lista las impresoras fiscales registrados por estado",
            path = "/api/taxprinter/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TaxPrinter.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TaxPrinter>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<TaxPrinter> list = taxPrinterService.list(state);
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
            description = "Obtiene la informacion de una impresora fiscal",
            path = "/api/taxprinter/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TaxPrinter.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaxPrinter> getById(
            @ApiPathParam(name = "id", description = "Id del la impresora fiscal") @PathVariable(name = "id") int id
    ) throws Exception
    {
        TaxPrinter bank = taxPrinterService.get(id, null,null);
        if (bank == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(bank, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una impresora fiscal",
            path = "/api/taxprinter/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TaxPrinter.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaxPrinter> getByName(
            @ApiPathParam(name = "name", description = "Nombre del banco") @PathVariable(name = "name") String name
    ) throws Exception
    {
        TaxPrinter bank = taxPrinterService.get(null, name, null);
        if (bank == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(bank, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea una nueva impresora fiscal",
            path = "/api/taxprinter",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TaxPrinter.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaxPrinter> create(
            @ApiBodyObject(clazz = TaxPrinter.class) @RequestBody TaxPrinter bank
    ) throws Exception
    {
        return new ResponseEntity<>(taxPrinterService.create(bank), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar una impresora fiscal",
            path = "/api/taxprinter",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TaxPrinter.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaxPrinter> update(
            @ApiBodyObject(clazz = TaxPrinter.class) @RequestBody TaxPrinter bank
    ) throws Exception
    {
        return new ResponseEntity<>(taxPrinterService.update(bank), HttpStatus.OK);
    }
}

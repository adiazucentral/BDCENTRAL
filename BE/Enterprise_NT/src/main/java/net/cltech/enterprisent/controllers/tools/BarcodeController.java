package net.cltech.enterprisent.controllers.tools;

import java.util.List;
import net.cltech.enterprisent.domain.tools.BarcodeDesigner;
import net.cltech.enterprisent.service.interfaces.tools.BarcodeService;
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
 * Servicios para codigos de barras
 *
 * @version 1.0.0
 * @author eacuna
 * @since 10/12/2018
 * @see Creacion
 */
@Api(
        name = "Barcode",
        group = "Herramientas",
        description = "Servicios codigos de barras"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/barcode")
public class BarcodeController
{

    @Autowired
    private BarcodeService service;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista barcode´s almacenados",
            path = "/api/barcode/designer",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = BarcodeDesigner.class)
    @RequestMapping(value = "/designer", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BarcodeDesigner>> list() throws Exception
    {
        List<BarcodeDesigner> list = service.list();
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
            description = "Lista por estado",
            path = "/api/barcode/designer/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = BarcodeDesigner.class)
    @RequestMapping(value = "/designer/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BarcodeDesigner>> list(@ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state) throws Exception
    {
        List<BarcodeDesigner> list = service.list(state);
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
            description = "Obtiene la informacion por id",
            path = "/api/barcode/designer/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = BarcodeDesigner.class)
    @RequestMapping(value = "/designer/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BarcodeDesigner> getById(@ApiPathParam(name = "id", description = "Id de barcode") @PathVariable(name = "id") int id) throws Exception
    {
        BarcodeDesigner barcode = service.getById(id);
        if (barcode == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(barcode, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea nuevo barcode",
            path = "/api/barcode/designer",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = BarcodeDesigner.class)
    @RequestMapping(value = "/designer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BarcodeDesigner> create(@ApiBodyObject(clazz = BarcodeDesigner.class) @RequestBody BarcodeDesigner barcode) throws Exception
    {
        return new ResponseEntity<>(service.create(barcode), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar ",
            path = "/api/barcode/designer",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = BarcodeDesigner.class)
    @RequestMapping(value = "/designer", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BarcodeDesigner> update(@ApiBodyObject(clazz = BarcodeDesigner.class) @RequestBody BarcodeDesigner barcode) throws Exception
    {
        return new ResponseEntity<>(service.update(barcode), HttpStatus.OK);
    }

}

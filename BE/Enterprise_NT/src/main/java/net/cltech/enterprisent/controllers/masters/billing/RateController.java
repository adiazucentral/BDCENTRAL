package net.cltech.enterprisent.controllers.masters.billing;

import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.service.interfaces.masters.billing.RateService;
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
 * Servicios para el acceso a la informacion del maestro Tarifas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 13/06/2017
 * @see Creacion
 */
@Api(
        name = "Tarifa",
        group = "Facturacion",
        description = "Servicios de informacion del maestro Tarifas"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/rates")
public class RateController
{

    @Autowired
    private RateService rateService;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista las tarifas registradas",
            path = "/api/rates",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rate.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rate>> list() throws Exception
    {
        List<Rate> list = rateService.list();
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
            description = "Lista las tarifas registradas por estado",
            path = "/api/rates/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rate.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rate>> list(@ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state) throws Exception
    {
        List<Rate> list = rateService.list(state);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR PAGADORES ----------------
    @ApiMethod(
            description = "Lista los pagadores",
            path = "/api/rates/payers",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rate.class)
    @RequestMapping(value = "/payers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rate>> listPayer() throws Exception
    {
        List<Rate> list = rateService.listPayers();
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
            description = "Obtiene la informacion de una tarifa",
            path = "/api/rates/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rate.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rate> getById(
            @ApiPathParam(name = "id", description = "Id de la tarifa") @PathVariable(name = "id") int id
    ) throws Exception
    {
        Rate area = rateService.get(id, null, null);
        if (area == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(area, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR CODIGO ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una tarifa",
            path = "/api/rates/filter/code/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rate.class)
    @RequestMapping(value = "/filter/code/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rate> getByCode(
            @ApiPathParam(name = "code", description = "Codigo de la tarifa") @PathVariable(name = "code") String code
    ) throws Exception
    {
        Rate rate = rateService.get(null, code, null);
        if (rate == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(rate, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una tarifa",
            path = "/api/rates/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rate.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rate> getByName(
            @ApiPathParam(name = "name", description = "Nombre de la tarifa") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Rate rate = rateService.get(null, null, name);
        if (rate == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(rate, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea una nueva tarifa",
            path = "/api/rates",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rate.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rate> create(
            @ApiBodyObject(clazz = Rate.class) @RequestBody Rate rate
    ) throws Exception
    {
        return new ResponseEntity<>(rateService.create(rate), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar una tarifa",
            path = "/api/rates",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rate.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rate> update(
            @ApiBodyObject(clazz = Rate.class) @RequestBody Rate rate
    ) throws Exception
    {
        return new ResponseEntity<>(rateService.update(rate), HttpStatus.OK);
    }

}

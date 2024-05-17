package net.cltech.enterprisent.controllers.operation.tracking;

import java.util.List;
import net.cltech.enterprisent.domain.operation.tracking.DisposalCertificate;
import net.cltech.enterprisent.service.interfaces.operation.tracking.DisposalCertificateService;
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
 * Servicios REST acta de desecho
 *
 * @version 1.0.0
 * @author Eacuna
 * @since 29/06/2018
 * @see Creacion
 */
@Api(
        group = "Trazabilidad",
        name = "Acta Desecho",
        description = "Servicios sobre actas de desechos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/disposalcertificates")
public class DisposalCertificateController
{

    @Autowired
    private DisposalCertificateService service;

    @ApiMethod(
            description = "Obtiene actas de desecho",
            path = "/api/disposalcertificates",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DisposalCertificate.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DisposalCertificate>> get() throws Exception
    {
        List<DisposalCertificate> records = service.list();
        if (!records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene acta por su id",
            path = "/api/disposalcertificates/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DisposalCertificate.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisposalCertificate> filterById(
            @ApiPathParam(name = "id", description = "Id de actas") @PathVariable(name = "id") String id
    ) throws Exception
    {
        DisposalCertificate record = service.filterById(Integer.parseInt(id));
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene actas por su nombre",
            path = "/api/disposalcertificates/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DisposalCertificate.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisposalCertificate> getByName(
            @ApiPathParam(name = "name", description = "Nombre de actas") @PathVariable(name = "name") String name
    ) throws Exception
    {
        DisposalCertificate record = service.filterByName(name);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualiza  acta enviada",
            path = "/api/disposalcertificates",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DisposalCertificate.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisposalCertificate> update(
            @ApiBodyObject(clazz = DisposalCertificate.class) @RequestBody(required = true) DisposalCertificate update
    ) throws Exception
    {
        return new ResponseEntity<>(service.update(update), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Crea acta enviada",
            path = "/api/disposalcertificates",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DisposalCertificate.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisposalCertificate> create(
            @ApiBodyObject(clazz = DisposalCertificate.class) @RequestBody DisposalCertificate create
    ) throws Exception
    {
        return new ResponseEntity<>(service.create(create), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Agrega gradillas al acta de desecho",
            path = "/api/disposalcertificates/add/rack",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DisposalCertificate.class)
    @RequestMapping(value = "/add/rack", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> addRacks(
            @ApiBodyObject(clazz = DisposalCertificate.class) @RequestBody DisposalCertificate create
    ) throws Exception
    {
        return new ResponseEntity<>(service.addRacks(create), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Cierra el acta de desecho",
            path = "/api/disposalcertificates/close",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DisposalCertificate.class)
    @RequestMapping(value = "/close", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> close(
            @ApiBodyObject(clazz = DisposalCertificate.class) @RequestBody DisposalCertificate close
    ) throws Exception
    {
        return new ResponseEntity<>(service.close(close), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene detalle del acta",
            path = "/api/disposalcertificates/detail/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DisposalCertificate.class)
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DisposalCertificate> getDisposalDetail(
            @ApiPathParam(name = "id", description = "Id del acta") @PathVariable(name = "id") Integer id
    ) throws Exception
    {
        DisposalCertificate record = service.listDetail(id);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Agrega muestra al acta de desecho,<br>id acta = add.id <br> id gradilla = add.position.rack.id <br> posición = add.position.position",
            path = "/api/disposalcertificates/add/sample",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DisposalCertificate.class)
    @RequestMapping(value = "/add/sample", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> addSample(
            @ApiBodyObject(clazz = DisposalCertificate.class) @RequestBody DisposalCertificate create
    ) throws Exception
    {
        return new ResponseEntity<>(service.addSampleByPosition(create), HttpStatus.OK);
    }

}

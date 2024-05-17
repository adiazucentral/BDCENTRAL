package net.cltech.enterprisent.controllers.masters.tracking;

import java.util.List;
import net.cltech.enterprisent.domain.masters.tracking.Rack;
import net.cltech.enterprisent.domain.operation.common.RackFilter;
import net.cltech.enterprisent.domain.operation.tracking.RackDetail;
import net.cltech.enterprisent.domain.operation.tracking.SampleStore;
import net.cltech.enterprisent.service.interfaces.masters.tracking.RackService;
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
 * Servicios REST gradilla
 *
 * @version 1.0.0
 * @author Eacuna
 * @since 06/06/2017
 * @see Creacion
 */
@Api(
        group = "Trazabilidad",
        name = "Gradilla",
        description = "Servicios sobre gradillas"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/racks")
public class RackController
{

    @Autowired
    private RackService service;

    @ApiMethod(
            description = "Obtiene gradillas",
            path = "/api/racks",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rack.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rack>> get() throws Exception
    {
        List<Rack> records = service.list();
        if (!records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene gradillas por su id",
            path = "/api/racks/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rack.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rack> filterById(
            @ApiPathParam(name = "id", description = "Id de gradillas") @PathVariable(name = "id") String id
    ) throws Exception
    {
        Rack record = service.filterById(Integer.parseInt(id));
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene gradillas por su nombre",
            path = "/api/racks/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rack.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rack> getByName(
            @ApiPathParam(name = "name", description = "Nombre de gradillas") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Rack record = service.filterByName(name);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene gradillas por sede",
            path = "/api/racks/filter/branch/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rack.class)
    @RequestMapping(value = "/filter/branch/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rack>> filterByName(
            @ApiPathParam(name = "branch", description = "id de la sede") @PathVariable(name = "branch") Integer branch
    ) throws Exception
    {
        List<Rack> records = service.listByBranch(branch);
        if (!records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualiza gradilla enviada",
            path = "/api/racks",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rack.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rack> update(
            @ApiBodyObject(clazz = Rack.class) @RequestBody Rack update
    ) throws Exception
    {
        return new ResponseEntity<>(service.update(update), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Crea gradilla enviada",
            path = "/api/racks",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rack.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rack> create(
            @ApiBodyObject(clazz = Rack.class) @RequestBody Rack create) throws Exception
    {
        Rack rack = service.create(create);
        return new ResponseEntity<>(rack, HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene lista gradillas por filtro",
            path = "/api/racks/filter/",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rack.class)
    @RequestMapping(value = "/filter/", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rack>> filterBy(
            @ApiBodyObject(clazz = RackFilter.class) @RequestBody RackFilter filter
    ) throws Exception
    {
        List<Rack> records = service.filterBy(filter);
        if (!records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Almacena muestra en gradilla",
            path = "/api/racks/store",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/store", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RackDetail> store(
            @ApiBodyObject(clazz = SampleStore.class) @RequestBody SampleStore store
    ) throws Exception
    {
        return new ResponseEntity<>(service.store(store), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Almacena muestra previamente retirada</b>Adicionar la informacion de la gradilla y posicion",
            path = "/api/racks/storeold",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/storeold", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RackDetail> storeold(
            @ApiBodyObject(clazz = SampleStore.class) @RequestBody SampleStore store
    ) throws Exception
    {
        return new ResponseEntity<>(service.storeOld(store), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Lista las muestras almacenadas en una gradilla",
            path = "/api/racks/detail/{rack}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = RackDetail.class)
    @RequestMapping(value = "/detail/{rack}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RackDetail>> listRackDetail(
            @ApiPathParam(name = "rack", description = "id gradilla") @PathVariable(name = "rack") int rack
    ) throws Exception
    {
        List<RackDetail> records = service.listRackDetail(rack);
        if (!records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Lista las muestras encontradas",
            path = "/api/racks/find/{order}/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = RackDetail.class)
    @RequestMapping(value = "/find/{order}/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RackDetail>> findSample(
            @ApiPathParam(name = "order", description = "id orden") @PathVariable(name = "order") long order,
            @ApiPathParam(name = "sample", description = "Código muestra") @PathVariable(name = "sample") String sample
    ) throws Exception
    {
        List<RackDetail> records = service.findSample(order, sample);
        if (!records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Cierra la gradilla enviada,",
            path = "/api/racks/close",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @RequestMapping(value = "/close", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> close(
            @ApiBodyObject(clazz = Rack.class) @RequestBody(required = true) Rack update
    ) throws Exception
    {
        service.close(update);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Retira muestra de la gradilla",
            path = "/api/racks/remove",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/remove", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> removeSample(
            @ApiBodyObject(clazz = SampleStore.class) @RequestBody SampleStore remove
    ) throws Exception
    {
        return new ResponseEntity<>(service.removeSample(remove), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Peticion de envio de generacion de etiquetas por un filtro especifico.",
            path = "/api/racks/barcode",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/barcode", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> rackBarcode(
            @ApiBodyObject(clazz = Rack.class) @RequestBody(required = true) Rack rack
    ) throws Exception
    {
        return new ResponseEntity<>(service.rackBarcode(rack), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene gradillas candidatas para desecho",
            path = "/api/racks/todiscard",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )

    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Rack.class)
    @RequestMapping(value = "/todiscard", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rack>> listRacksToDiscard() throws Exception
    {
        List<Rack> records = service.listRacksToDiscard();
        if (!records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //------------ ALMACENA MUESTRA DE GRADILLA POR CODIGO ----------------
    @ApiMethod(
            description = "Almacena muestra en gradilla",
            path = "/api/racks/storecode",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = String.class)
    @RequestMapping(value = "/storecode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RackDetail> storecode(
            @ApiBodyObject(clazz = SampleStore.class) @RequestBody SampleStore store
    ) throws Exception
    {
        return new ResponseEntity<>(service.storecode(store), HttpStatus.OK);
    }

}

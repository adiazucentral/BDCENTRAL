package net.cltech.enterprisent.controllers.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.service.interfaces.masters.demographic.ServiceService;
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
 * Servicios REST servicio
 *
 * @version 1.0.0
 * @author Eacuna
 * @since 17/04/2017
 * @see Creacion
 */
@Api(
        group = "Demografico",
        name = "Servicio",
        description = "Servicios sobre servicios de laboratorio"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/services")
public class ServiceController
{

    @Autowired
    private ServiceService service;

    @ApiMethod(
            description = "Obtiene servicios de laboratorio creadas",
            path = "/api/services",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ServiceLaboratory.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ServiceLaboratory>> get() throws Exception
    {
        List<ServiceLaboratory> records = service.list();
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(service.list(), HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Obtiene servicios de laboratorio creadas para toma de muestra hospitalaria",
            path = "/api/services/serviceshospitalary",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ServiceLaboratory.class)
    @RequestMapping(value = "/serviceshospitalary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ServiceLaboratory>> getHospitalary() throws Exception
    {
        List<ServiceLaboratory> records = service.listHospitalary();
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Obtiene servicio por su id",
            path = "/api/services/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ServiceLaboratory.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceLaboratory> filterById(
            @ApiPathParam(name = "id", description = "Id de servicio") @PathVariable(name = "id") String id
    ) throws Exception
    {
        ServiceLaboratory record = service.filterById(Integer.parseInt(id));
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene servicio por su nombre",
            path = "/api/services/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ServiceLaboratory.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceLaboratory> filterByName(
            @ApiPathParam(name = "name", description = "Nombre de servicio") @PathVariable(name = "name") String name
    ) throws Exception
    {
        ServiceLaboratory record = service.filterByName(name);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualiza  servicio enviada",
            path = "/api/services",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ServiceLaboratory.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceLaboratory> update(
            @ApiBodyObject(clazz = ServiceLaboratory.class) @RequestBody(required = true) ServiceLaboratory unit
    ) throws Exception
    {
        return new ResponseEntity<>(service.update(unit), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Crea servicio enviada",
            path = "/api/services",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ServiceLaboratory.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceLaboratory> create(
            @ApiBodyObject(clazz = ServiceLaboratory.class) @RequestBody ServiceLaboratory unit
    ) throws Exception
    {
        return new ResponseEntity<>(service.create(unit), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene lista servicios de laboratorio por el estado",
            path = "/api/services/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ServiceLaboratory.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ServiceLaboratory>> filterByState(
            @ApiPathParam(name = "state", description = "Estado activo(true) o inactivo(false)") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<ServiceLaboratory> records = service.filterByState(state);
        if (records != null)
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Obtiene servicio por su id, sistema central y numero de orden solo si este esta homologado",
            path = "/api/services/filterHomologation/idService/{idService}/centralSystem/{centralSystem}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ServiceLaboratory.class)
    @RequestMapping(value = "/filterHomologation/idService/{idService}/centralSystem/{centralSystem}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceLaboratory> filterHomologationById(
            @ApiPathParam(name = "idService", description = "Id de servicio") @PathVariable(name = "idService") int idService,
            @ApiPathParam(name = "centralSystem", description = "Id del sistema central") @PathVariable(name = "centralSystem") int centralSystem
    ) throws Exception
    {
        ServiceLaboratory record = service.filterHomoligationById(idService, centralSystem);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    
}

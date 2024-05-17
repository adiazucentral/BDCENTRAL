package net.cltech.enterprisent.controllers.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.SampleByService;
import net.cltech.enterprisent.service.interfaces.masters.test.SampleService;
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
 * Servicios para el acceso a la informacion del maestro Muestra
 *
 * @version 1.0.0
 * @author enavas
 * @since 28/04/2017
 * @see Creacion
 */
@Api(
        name = "Muestra",
        group = "Prueba",
        description = "Servicios sobre las Muestras"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/samples")
public class SampleController
{

    @Autowired
    private SampleService sampleService;

    @ApiMethod(
            description = "Lista las muestras registradas",
            path = "/api/samples",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sample>> list() throws Exception
    {
        List<Sample> list = sampleService.list();
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
            description = "Obtiene la informacion de una muestra",
            path = "/api/samples/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sample> getById(
            @ApiPathParam(name = "id", description = "Id de la muestra") @PathVariable(name = "id") int id
    ) throws Exception
    {
        List<Sample> list = sampleService.get(id, null, null, null, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list.get(0), HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una muestra",
            path = "/api/samples/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sample> getByName(
            @ApiPathParam(name = "name", description = "Nombre de la muestra") @PathVariable(name = "name") String name
    ) throws Exception
    {
        List<Sample> list = sampleService.get(null, name, null, null, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list.get(0), HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR CODIGO ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una muestra",
            path = "/api/samples/filter/code/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(value = "/filter/code/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sample> getByCode(
            @ApiPathParam(name = "code", description = "Codigo de la muestra 3 caracteres") @PathVariable(name = "code") String code
    ) throws Exception
    {
        List<Sample> list = sampleService.get(null, null, code, null, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list.get(0), HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR RECIPIENTE ----------------
    @ApiMethod(
            description = "Obtiene las muestras filtrado por recipiente",
            path = "/api/samples/filter/container/{container}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(value = "/filter/container/{container}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sample>> getBycontainer(
            @ApiPathParam(name = "container", description = "Id del recipiente") @PathVariable(name = "container") int container
    ) throws Exception
    {
        List<Sample> list = sampleService.get(null, null, null, container, null);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR ESTADO ----------------
    @ApiMethod(
            description = "Obtiene las muestras filtrado por estado",
            path = "/api/samples/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sample>> getByState(
            @ApiPathParam(name = "state", description = "Estado del recipiente") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Sample> list = sampleService.get(null, null, null, null, state);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene las muestras filtrado por tipo de muestra",
            path = "/api/samples/filter/type/{types}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(value = "/filter/type/{types}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sample>> filterByTypes(
            @ApiPathParam(name = "types", description = "Tipo de muestra") @PathVariable(name = "types") String types
    ) throws Exception
    {
        List<Sample> list = sampleService.typeFilter(types);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea una nueva muestra",
            path = "/api/samples",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sample> create(
            @ApiBodyObject(clazz = Sample.class) @RequestBody Sample sample
    ) throws Exception
    {
        return new ResponseEntity<>(sampleService.create(sample), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar una muestra",
            path = "/api/samples",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sample> update(
            @ApiBodyObject(clazz = Sample.class) @RequestBody Sample sample
    ) throws Exception
    {
        return new ResponseEntity<>(sampleService.update(sample), HttpStatus.OK);
    }

    //------------ INSERTAR MUESTRAS POR SERVICIO ----------------
    @ApiMethod(
            description = "Insertar muestras por servicio",
            path = "/api/samples/services",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SampleByService.class)
    @RequestMapping(value = "/services", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SampleByService> insertSampleByService(
            @ApiBodyObject(clazz = SampleByService.class) @RequestBody SampleByService sample
    ) throws Exception
    {
        return new ResponseEntity<>(sampleService.insertSamplesByService(sample), HttpStatus.OK);
    }

    //------------ LISTAR MUESTRAS POR SERVICIO ----------------
    @ApiMethod(
            description = "Lista muestras por servicio.",
            path = "/api/samples/services/{service}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SampleByService.class)
    @RequestMapping(value = "/services/{service}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SampleByService>> listSampleByService(
            @ApiPathParam(name = "service", description = "Id Servicio") @PathVariable(name = "service") int service
    ) throws Exception
    {
        List<SampleByService> list = sampleService.listSampleByService(service);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene las muestras filtrado por tipo de muestra",
            path = "/api/samples/subsamples/{sampleid}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(value = "/subsamples/{sampleid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sample>> getSubSamples(
            @ApiPathParam(name = "sampleid", description = "Id muestra padre") @PathVariable(name = "sampleid") int sampleId
    ) throws Exception
    {
        List<Sample> list = sampleService.subSamples(sampleId);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
     @ApiMethod(
            description = "Obtiene las muestras filtrado por tipo de muestra",
            path = "/api/samples/subsamplesselect/{sampleid}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(value = "/subsamplesselect/{sampleid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sample>> getSubSamplesSelect(
            @ApiPathParam(name = "sampleid", description = "Id muestra padre") @PathVariable(name = "sampleid") int sampleId
    ) throws Exception
    {
        List<Sample> list = sampleService.listSubSampleSelect(sampleId);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    @ApiMethod(
            description = "Obtiene las muestras de microbiologia",
            path = "/api/samples/samplemicrobiology",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(value = "/samplemicrobiology", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sample>> getsamplesMicrobiology() throws Exception
    {
        List<Sample> list = sampleService.samplesMicrobiology();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Asigna submuestras a una muestra de microbiología",
            path = "/api/samples/subsamples/",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(value = "/subsamples/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> assignSubSamples(
            @ApiBodyObject(clazz = Sample.class) @RequestBody Sample sample
    ) throws Exception
    {
        return new ResponseEntity<>(sampleService.assignSubSamples(sample), HttpStatus.OK);
    }

}

package net.cltech.enterprisent.controllers.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.microbiology.MicroorganismAntibiotic;
import net.cltech.enterprisent.domain.masters.microbiology.Sensitivity;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.MicroorganismService;
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
 * Servicios REST microorganismo
 *
 * @version 1.0.0
 * @author Eacuna
 * @since 21/06/2017
 * @see Creacion
 */
@Api(
        group = "Microbiología",
        name = "Microorganismo",
        description = "Servicios sobre microorganismos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/microorganisms")
public class MicroorganismController
{

    @Autowired
    private MicroorganismService service;

    @ApiMethod(
            description = "Obtiene microorganismos",
            path = "/api/microorganisms",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Microorganism.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Microorganism>> get() throws Exception
    {
        List<Microorganism> records = service.list();
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene el antibiograma asociado a un microorganismo y a un examen",
            path = "/api/microorganisms/sensitivity/microorganism/{microorganism}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sensitivity.class)
    @RequestMapping(value = "/sensitivity/microorganism/{microorganism}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sensitivity> filterBySensitivity(
            @ApiPathParam(name = "microorganism", description = "Id del microorganismo") @PathVariable(name = "microorganism") Integer idMicroorganism,
            @ApiPathParam(name = "test", description = "Id del examen") @PathVariable(name = "test") Integer idTest
    ) throws Exception
    {
        Sensitivity sensitivity = service.getSensitivity(idMicroorganism, idTest);
        if (sensitivity == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(sensitivity, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene microorganismos de un antibiograma",
            path = "/api/microorganisms/filter/sensitivity/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )

    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Microorganism.class)
    @RequestMapping(value = "filter/sensitivity/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Microorganism>> filterByMicroorganism(
            @ApiPathParam(name = "id", description = "Id de microorganismo") @PathVariable(name = "id") int id
    ) throws Exception
    {
        List<Microorganism> records = service.filterByMicroorganism(id);
        if (records.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene microorganismos por su id",
            path = "/api/microorganisms/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Microorganism.class)
    @RequestMapping(value = "/filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Microorganism> filterById(
            @ApiPathParam(name = "id", description = "Id de microorganismos") @PathVariable(name = "id") String id
    ) throws Exception
    {
        Microorganism record = service.findById(Integer.parseInt(id));
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene microorganismos por su nombre",
            path = "/api/microorganisms/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Microorganism.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Microorganism> filterByName(
            @ApiPathParam(name = "name", description = "Nombre de microorganismos") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Microorganism record = service.findByName(name);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Actualiza  microorganismo",
            path = "/api/microorganisms",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Microorganism.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Microorganism> update(
            @ApiBodyObject(clazz = Microorganism.class) @RequestBody(required = true) Microorganism update
    ) throws Exception
    {
        return new ResponseEntity<>(service.update(update), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Actualiza antibiograma del microorganismo en bloque",
            path = "/api/microorganisms/sensitivity",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/sensitivity", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateSensitivityBatch(
            @ApiBodyObject(clazz = Microorganism.class) @RequestBody(required = true) List<Microorganism> update
    ) throws Exception
    {
        return new ResponseEntity<>(service.sensitivityUpdate(update), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Crea microorganismo ",
            path = "/api/microorganisms",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Microorganism.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Microorganism> create(
            @ApiBodyObject(clazz = Microorganism.class) @RequestBody Microorganism create
    ) throws Exception
    {
        return new ResponseEntity<>(service.create(create), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Importa microorganismos ",
            path = "/api/microorganisms/import",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/import", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> createAll(
            @ApiBodyObject(clazz = Microorganism.class) @RequestBody List<Microorganism> microorganisms
    ) throws Exception
    {
        return new ResponseEntity<>(service.createAll(microorganisms), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene lista microorganismos por el estado",
            path = "/api/microorganisms/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Microorganism.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Microorganism>> filterByState(
            @ApiPathParam(name = "state", description = "Estado activo(true) o inactivo(false)") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Microorganism> records = service.filterByState(state);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //----------------MICROORGANISMOS - ANTIBIOTICOS------------
    //-----------CONSULTA MICROORGANISMOS - ANTIBIOTICOS--------
    @ApiMethod(
            description = "Obtiene Microorganismos - Antibiograma.",
            path = "/api/microorganisms/antibiotics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicroorganismAntibiotic.class)
    @RequestMapping(value = "/antibiotics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MicroorganismAntibiotic>> getMicroorganismAntibiotics() throws Exception
    {
        List<MicroorganismAntibiotic> list = service.listMicroorganimAntibiotic();
        if (list != null && !list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //-----------CONSULTA POR MICROORGANISMO--------
    @ApiMethod(
            description = "Obtiene Microorganismos - Antibiograma.",
            path = "/api/microorganisms/antibiotics/microorganism/{microorganism}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicroorganismAntibiotic.class)
    @RequestMapping(value = "/antibiotics/microorganism/{microorganism}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MicroorganismAntibiotic>> getAntibioticsByMicroorganism(
            @ApiPathParam(name = "microorganism", description = "Id del microorganismo") @PathVariable(name = "microorganism") int idMicroorganism
    ) throws Exception
    {
        List<MicroorganismAntibiotic> list = service.listMicroorganimAntibiotic(idMicroorganism);
        if (list != null && !list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //-----------CONSULTA MICROORGANISMOS - ANTIBIOTICOS--------
    @ApiMethod(
            description = "Obtiene microorganismos por su id",
            path = "/api/microorganisms/antibiotics/filter/{microorganism}/{antibiotic}/{method}/{interpretation}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicroorganismAntibiotic.class)
    @RequestMapping(value = "/antibiotics/filter/{microorganism}/{antibiotic}/{method}/{interpretation}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MicroorganismAntibiotic> filterMicroorganismAntibiotics(
            @ApiPathParam(name = "microorganism", description = "Id del microorganismo") @PathVariable(name = "microorganism") int idMicroorganism,
            @ApiPathParam(name = "antibiotic", description = "Id del antibiotico") @PathVariable(name = "antibiotic") int idAntibiotic,
            @ApiPathParam(name = "method", description = "Metodo: 1 -> Manual,  2 -> Disco") @PathVariable(name = "method") short method,
            @ApiPathParam(name = "interpretation", description = "Interpretación: 1 -> Sensible, 2 -> Intermedio, 3 -> Resistente") @PathVariable(name = "interpretation") short interpretation
    ) throws Exception
    {
        MicroorganismAntibiotic record = service.getMicroorganismAntibiotic(idMicroorganism, idAntibiotic, method, interpretation);
        if (record != null)
        {
            return new ResponseEntity<>(record, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //-----------INGRESAR MICROORGANISMO - ANTIBIOTICO---------
    @ApiMethod(
            description = "Crea microorganismo - antibiotico",
            path = "/api/microorganisms/antibiotics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicroorganismAntibiotic.class)
    @RequestMapping(value = "/antibiotics", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MicroorganismAntibiotic> insertMicroorganismAntibiotics(
            @ApiBodyObject(clazz = MicroorganismAntibiotic.class) @RequestBody MicroorganismAntibiotic microAntibiotic
    ) throws Exception
    {
        return new ResponseEntity<>(service.create(microAntibiotic), HttpStatus.OK);
    }

    //-----------ACTUALIZAR MICROORGANISMO - ANTIBIOTICO---------
    @ApiMethod(
            description = "Crea microorganismo - antibiotico",
            path = "/api/microorganisms/antibiotics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = MicroorganismAntibiotic.class)
    @RequestMapping(value = "/antibiotics", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MicroorganismAntibiotic> updateMicroorganismAntibiotics(
            @ApiBodyObject(clazz = Microorganism.class) @RequestBody MicroorganismAntibiotic microAntibiotic
    ) throws Exception
    {
        return new ResponseEntity<>(service.update(microAntibiotic), HttpStatus.OK);
    }

    //-----------ELIMINAR MICROORGANISMOS - ANTIBIOTICOS--------
    @ApiMethod(
            description = "Obtiene microorganismos por su id",
            path = "/api/microorganisms/antibiotics/filter/{microorganism}/{antibiotic}/{method}/{interpretation}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/antibiotics/filter/{microorganism}/{antibiotic}/{method}/{interpretation}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteMicroorganismAntibiotics(
            @ApiPathParam(name = "microorganism", description = "Id del microorganismo") @PathVariable(name = "microorganism") int idMicroorganism,
            @ApiPathParam(name = "antibiotic", description = "Id del antibiotico") @PathVariable(name = "antibiotic") int idAntibiotic,
            @ApiPathParam(name = "method", description = "Metodo: 1 -> Manual,  2 -> Disco") @PathVariable(name = "method") short method,
            @ApiPathParam(name = "interpretation", description = "Interpretación: 1 -> Sensible, 2 -> Intermedio, 3 -> Resistente") @PathVariable(name = "interpretation") short interpretation
    ) throws Exception
    {
        service.delete(idMicroorganism, idAntibiotic, method, interpretation);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

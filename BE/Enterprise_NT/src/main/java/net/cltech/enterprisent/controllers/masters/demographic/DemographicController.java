package net.cltech.enterprisent.controllers.masters.demographic;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.masters.configuration.DemographicReportEncryption;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.test.AlarmDays;
import net.cltech.enterprisent.domain.masters.test.ExcludeTest;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
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
 * Servicios para el acceso a la informacion del maestro Demografico
 *
 * @version 1.0.0
 * @author cmartin
 * @since 02/05/2017
 * @see Creacion
 */
@Api(
        name = "Demografico",
        group = "Demografico",
        description = "Servicios de informacion del maestro Demografico"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/demographics")
public class DemographicController
{

    @Autowired
    private DemographicService demographicService;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los demograficos registrados",
            path = "/api/demographics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Demographic>> list() throws Exception
    {
        List<Demographic> list = demographicService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTA DE DEMOGRAFICOS ----------------
    @ApiMethod(
            description = "Lista los demograficos por defecto y demograficos",
            path = "/api/demographics/all",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Demographic>> demographicsList() throws Exception
    {
        List<Demographic> list = demographicService.demographicsList();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ ORDENAMIENTO DE DEMOGRAFICOS ----------------
    @ApiMethod(
            description = "Lista los demograficos por defecto y demograficos",
            path = "/api/demographics/orderingAll/{origin}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(value = "/orderingAll/{origin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Demographic>> demographicsListordering(
            @ApiPathParam(name = "origin", description = "Origen H->Historia, O->Orden") @PathVariable("origin") String origin,
            HttpServletRequest request
    ) throws Exception
    {
        List<Demographic> list = demographicService.demographicsListordering(origin);
        if (list != null && !list.isEmpty())
        {
            return new ResponseEntity(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    //------------ LISTAR POR ESTADO ----------------
    @ApiMethod(
            description = "Lista los demograficos registrados por estado",
            path = "/api/demographics/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Demographic>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Demographic> list = demographicService.list(state);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR POR CODIFICACION ----------------
    @ApiMethod(
            description = "Lista los demograficos registrados por el campo codificado",
            path = "/api/demographics/filter/encoded/{encoded}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(value = "/filter/encoded/{encoded}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Demographic>> listByEncoded(
            @ApiPathParam(name = "encoded", description = "Es codificado") @PathVariable(name = "encoded") boolean encoded
    ) throws Exception
    {
        List<Demographic> list = demographicService.listByEncoded(encoded);
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
            description = "Obtiene la informacion de un demografico",
            path = "/api/demographics/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Demographic> getById(
            @ApiPathParam(name = "id", description = "Id del demografico") @PathVariable(name = "id") int id
    ) throws Exception
    {
        Demographic demographic = demographicService.get(id, null, null);
        if (demographic == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(demographic, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un demografico",
            path = "/api/demographics/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Demographic> getByName(
            @ApiPathParam(name = "name", description = "Nombre del demografico") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Demographic demographic = demographicService.get(null, name, null);
        if (demographic == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(demographic, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR ORDENAMIENTO ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un demografico",
            path = "/api/demographics/filter/sort/{sort}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(value = "/filter/sort/{sort}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Demographic> getBySort(
            @ApiPathParam(name = "sort", description = "Ordenamiento del demografico") @PathVariable(name = "sort") Integer sort
    ) throws Exception
    {
        Demographic demographic = demographicService.get(null, null, sort);
        if (demographic == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(demographic, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR ORIGEN ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un demografico por origen",
            path = "/api/demographics/filter/origin/{origin}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(value = "/filter/origin/{origin}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Demographic>> getByOrigin(
            @ApiPathParam(name = "origin", description = "Origen del Demografico H-> Historia, O-> Orden") @PathVariable(name = "origin") String origin
    ) throws Exception
    {
        List<Demographic> demographic = demographicService.listBySource(origin);
        if (demographic == null || demographic.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(demographic, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un nuevo demografico",
            path = "/api/demographics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Demographic> create(
            @ApiBodyObject(clazz = Demographic.class) @RequestBody Demographic demographic
    ) throws Exception
    {
        return new ResponseEntity<>(demographicService.create(demographic), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un demografico",
            path = "/api/demographics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Demographic.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Demographic> update(
            @ApiBodyObject(clazz = Demographic.class) @RequestBody Demographic demographic
    ) throws Exception
    {
        return new ResponseEntity<>(demographicService.update(demographic), HttpStatus.OK);
    }

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los examenes excluidos de un demografico",
            path = "/api/demographics/exclude/id/{id}/item/{item}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ExcludeTest.class)
    @RequestMapping(value = "/exclude/id/{id}/item/{item}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ExcludeTest>> listDemographicTest(
            @ApiPathParam(name = "id", description = "Id del demográfico") @PathVariable(name = "id") Integer id,
            @ApiPathParam(name = "item", description = "Id del demográfico item") @PathVariable(name = "item") Integer item
    ) throws Exception
    {
        List<ExcludeTest> list = demographicService.listDemographicTest(id, item);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Inserta examenes excluidos a un item",
            path = "/api/demographics/exclude/",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "exclude/", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertDemographicTest(
            @ApiBodyObject(clazz = ExcludeTest.class) @RequestBody List<ExcludeTest> tests
    ) throws Exception
    {
        return new ResponseEntity<>(demographicService.insertDemographicTest(tests), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Elimina los examenes excluidos de un demografico",
            path = "/api/demographics/exclude/id/{id}/item/{item}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/exclude/id/{id}/item/{item}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteDemographicTest(
            @ApiPathParam(name = "id", description = "Id del demográfico") @PathVariable(name = "id") Integer id,
            @ApiPathParam(name = "item", description = "Id del demográfico item") @PathVariable(name = "item") Integer item
    ) throws Exception
    {
        return new ResponseEntity<>(demographicService.deleteDemographicTest(id, item), HttpStatus.OK);
    }

    //------------ LISTAR DIAS CON ALARMA ----------------
    @ApiMethod(
            description = "Lista los examenes con dias de alarma",
            path = "/api/demographics/alarmdays/filter/demographic/{demographic}/demographicitem/{demographicitem}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestBasic.class)
    @RequestMapping(value = "/alarmdays/filter/demographic/{demographic}/demographicitem/{demographicitem}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestBasic>> listAlarmDaysTest(
            @ApiPathParam(name = "demographic", description = "Id del demográfico") @PathVariable(name = "demographic") Integer demographic,
            @ApiPathParam(name = "demographicitem", description = "Id del demográfico item") @PathVariable(name = "demographicitem") Integer demographicItem
    ) throws Exception
    {
        List<TestBasic> list = demographicService.listAlarmDaysTest(demographic, demographicItem);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Inserta examenes con dias de alarma",
            path = "/api/demographics/alarmdays",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/alarmdays", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertAlarmDaysTest(
            @ApiBodyObject(clazz = AlarmDays.class) @RequestBody AlarmDays alarmDays
    ) throws Exception
    {
        return new ResponseEntity<>(demographicService.insertAlarmDaysTest(alarmDays), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Elimina los examenes con dias de alarma",
            path = "/api/demographics/alarmdays/filter/demographic/{demographic}/demographicitem/{demographicitem}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/alarmdays/filter/demographic/{demographic}/demographicitem/{demographicitem}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteAlarmDaysTest(
            @ApiPathParam(name = "demographic", description = "Id del demográfico") @PathVariable(name = "demographic") Integer demographic,
            @ApiPathParam(name = "demographicitem", description = "Id del demográfico item") @PathVariable(name = "demographicitem") Integer demographicItem
    ) throws Exception
    {
        return new ResponseEntity<>(demographicService.deleteAlarmDaysTest(demographic, demographicItem), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Actualizar el ordenamiento de los demograficos",
            path = "/api/demographics/ordering",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/ordering", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateOrder(
            @ApiBodyObject(clazz = Demographic.class) @RequestBody List<Demographic> demographics
    ) throws Exception
    {
        demographicService.updateOrder(demographics);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
     @ApiMethod(
            description = "Actualizar el ordenamiento de los demograficos",
            path = "/api/demographics/orderingAll",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/orderingAll", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateOrderAll(
            @ApiBodyObject(clazz = Demographic.class) @RequestBody List<Demographic> demographics
    ) throws Exception
    {
        demographicService.updateOrderAll(demographics);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Guarda los demográficos con sus respectivos items en encriptación de reportes por demográfico",
            path = "/api/demographics/saveDemographicReportEncrypt",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/saveDemographicReportEncrypt", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveDemographicReportEncrypt(
            @ApiBodyObject(clazz = DemographicReportEncryption.class) @RequestBody List<DemographicReportEncryption> demographics
    ) throws Exception
    {
        demographicService.saveDemographicReportEncrypt(demographics);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene todos los items de un demográfico por su id en encriptación de reportes por demográfico",
            path = "/api/demographics/getDemographicReportEncrypt/idDemographic/{idDemographic}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getDemographicReportEncrypt/idDemographic/{idDemographic}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicReportEncryption>> getDemographicReportEncryptById(
            @ApiPathParam(name = "idDemographic", description = "id del demografico") @PathVariable("idDemographic") int idDemographic
    ) throws Exception
    {
        List<DemographicReportEncryption> list = demographicService.getDemographicReportEncryptById(idDemographic);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

}
